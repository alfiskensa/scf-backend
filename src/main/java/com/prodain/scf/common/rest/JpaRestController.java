package com.prodain.scf.common.rest;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphCrudRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphPagingAndSortingRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.prodain.scf.common.repo.EntityGraphOffsetLimitRepository;
import com.prodain.scf.common.repo.OffsetLimit;
import com.prodain.scf.common.repo.OffsetLimitRepository;

public abstract class JpaRestController<T, ID extends Serializable> extends AbstractJpaRestController
{
	@SuppressWarnings("unchecked")
	protected final Class<T> resourceClass = (Class<T>) GenericTypeResolver.resolveTypeArguments(getClass(), JpaRestController.class)[0];
	
	@Autowired
	private CrudRepository<T, ID> repo;
	
	@PostConstruct
	private void init()
	{
		restEngine.registerClass(resourceClass);
	}
	
	@PostMapping
	@Transactional(readOnly = false)
	public ResponseEntity<?> create(@RequestBody T object)
	{
		RestResponse<T> response = createValidation(object);
		if(response != null && !response.getIsValid()) {
			return ResponseEntity.status(response.getStatus()).body(response);
		}
		
		doCreate(object);
		
		RestResponse<T> customResponse =  createCustomResponse(object);
		
		if(customResponse != null) {
			return ResponseEntity.status(customResponse.getStatus()).body(customResponse);
		}
			
		return ResponseEntity.created(null).body(object);
	}
	
	protected T doCreate(T object)
	{
		return save(null, object);
	}
	
	@PutMapping("/{id:\\d+}")
	@Transactional(readOnly = false)
	public ResponseEntity<?> update(@PathVariable ID id, @RequestBody T object)
	{
		RestResponse<T> response = updateValidation(object);
		if(response != null && !response.getIsValid()) {
			return ResponseEntity.status(response.getStatus()).body(response);
		}
		
		doUpdate(id, object);
		
		RestResponse<T> customResponse = updateCustomResponse(object);
		
		if(customResponse != null)
			return ResponseEntity.status(customResponse.getStatus()).body(customResponse);
					
		return ResponseEntity.ok().body(object);
	}
	
	protected T doUpdate(ID id, T object)
	{
		return save(id, object);
	}
	
	protected T save(ID id, T object)
	{
		return repo.save(object);
	}
	
	@DeleteMapping("/{id:\\d+}")
	@Transactional(readOnly = false)
	public ResponseEntity<?> delete(@PathVariable ID id)
	{
		RestResponse<T> deleteCustomeResponse = deleteCustomResponse(repo.findById(id).orElse(null));
		
		doDelete(id);
		
		if(deleteCustomeResponse != null)
			return ResponseEntity.status(deleteCustomeResponse.getStatus()).body(deleteCustomeResponse);
		
		return ResponseEntity.noContent().build();
	}
	
	protected void doDelete(ID id)
	{
		repo.deleteById(id);
	}
	
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<?> findById(@PathVariable ID id, RestRequest restRequest) throws Exception
	{
		Optional<T> object = null;
		
		if(repo instanceof EntityGraphCrudRepository)
			object = ((EntityGraphCrudRepository<T, ID>) repo).findById(id, toResourceEntityGraph(restRequest.getExpand()));
		else
			object = repo.findById(id);
		
		if(!object.isPresent())
			return ResponseEntity.notFound().build();
		
		beforeReturnResponse(object.get(), restRequest);
		
		return ResponseEntity.ok(restEngine.applyJsonFilter(object.get(), restRequest));
	}
	
	@GetMapping("/{ids:\\d+(?:,\\d+)+}")
	public ResponseEntity<?> findByIds(@PathVariable List<ID> ids, RestRequest restRequest) throws Exception
	{
		Iterable<T> list = null;
		
		if(repo instanceof EntityGraphCrudRepository)
			list = ((EntityGraphCrudRepository<T, ID>) repo).findAllById(ids, toResourceEntityGraph(restRequest.getExpand()));
		else
			list = repo.findAllById(ids);
		
		beforeReturnResponse(list, restRequest);
		
		return ResponseEntity.ok(restEngine.applyJsonFilter(list, resourceClass, restRequest));
	}
	
	@GetMapping
	public ResponseEntity<?> findAll(RestRequest restRequest) throws Exception
	{
		Iterable<T> list = null;
		
		if(repo instanceof PagingAndSortingRepository || repo instanceof QuerydslPredicateExecutor)
		{
			Sort sort = toSort(restRequest);
			Pageable pageOrOffsetLimit = toPageableOrOffsetLimitRequest(restRequest, sort);
			
			if(repo instanceof PagingAndSortingRepository)
				list = findAllPagingAndSorting(restRequest, sort, pageOrOffsetLimit);
			else
				list = findAllQuerydsl(restRequest, sort, pageOrOffsetLimit);
		}
		else if(repo instanceof EntityGraphCrudRepository)
			list = ((EntityGraphCrudRepository<T, ID>) repo).findAll(toResourceEntityGraph(restRequest.getExpand()));
		else
			list = repo.findAll();
		
		beforeReturnResponse(list, restRequest);
		
		return createResponse(list, restRequest, resourceClass);
	}
	
	private Iterable<T> findAllPagingAndSorting(RestRequest restRequest, Sort sort, Pageable pageable)
	{
		if(repo instanceof EntityGraphPagingAndSortingRepository)
		{
			if(repo instanceof EntityGraphOffsetLimitRepository && pageable != null && pageable instanceof OffsetLimit)
			{
				EntityGraphOffsetLimitRepository<T, ID> offsetLimitRepo = (EntityGraphOffsetLimitRepository<T, ID>) repo;
				return offsetLimitRepo.findAllOffsetLimit((OffsetLimit) pageable, toResourceEntityGraph(restRequest.getExpand()));
			}
			else
			{
				EntityGraphPagingAndSortingRepository<T, ID> pagingRepo = (EntityGraphPagingAndSortingRepository<T, ID>) repo;
				if(pageable != null)
					return pagingRepo.findAll(pageable, toResourceEntityGraph(restRequest.getExpand()));
				else if(sort != null)
					return pagingRepo.findAll(sort, toResourceEntityGraph(restRequest.getExpand()));
				else
					return pagingRepo.findAll(toResourceEntityGraph(restRequest.getExpand()));
			}
		}
		else
		{
			if(repo instanceof OffsetLimitRepository && pageable != null && pageable instanceof OffsetLimit)
			{
				OffsetLimitRepository<T, ID> offsetLimitRepo = (OffsetLimitRepository<T, ID>) repo;
				return offsetLimitRepo.findAllOffsetLimit((OffsetLimit) pageable);
			}
			else
			{
				PagingAndSortingRepository<T, ID> pagingRepo = (PagingAndSortingRepository<T, ID>) repo;
				if(pageable != null)
					return pagingRepo.findAll(pageable);
				else if(sort != null)
					return pagingRepo.findAll(sort);
				else
					return pagingRepo.findAll();
			}
		}
	}
	
	private Iterable<T> findAllQuerydsl(RestRequest restRequest, Sort sort, Pageable pageable)
	{
		if(repo instanceof EntityGraphQuerydslPredicateExecutor)
		{
			@SuppressWarnings("unchecked")
			EntityGraphQuerydslPredicateExecutor<T> dslRepo = (EntityGraphQuerydslPredicateExecutor<T>) repo;
			if(pageable != null)
				return dslRepo.findAll(null, pageable, toResourceEntityGraph(restRequest.getExpand()));
			else if(sort != null)
				return dslRepo.findAll(null, sort, toResourceEntityGraph(restRequest.getExpand()));
			else
				return dslRepo.findAll(null, toResourceEntityGraph(restRequest.getExpand()));
		}
		else
		{
			@SuppressWarnings("unchecked")
			QuerydslPredicateExecutor<T> dslRepo = (QuerydslPredicateExecutor<T>) repo;
			if(pageable != null)
				return dslRepo.findAll(null, pageable);
			else if(sort != null)
				return dslRepo.findAll(null, sort);
			else
				return dslRepo.findAll();
		}
	}
	
	@GetMapping("/count")
	public ResponseEntity<?> countAll(RestRequest restRequest) throws Exception
	{
		return ResponseEntity.ok(createSingleValueResponse("count", repo.count()));
	}
	
	protected EntityGraph toResourceEntityGraph(Collection<String> paths)
	{
		return toEntityGraph(paths, resourceClass);
	}
	
	protected abstract RestResponse<T> createValidation(T object);
	
	protected abstract RestResponse<T> updateValidation(T object);
	
	protected RestResponse<T> createCustomResponse(T object)
	{
		return null; // for override if define custom response
	}
	
	protected RestResponse<T> updateCustomResponse(T object)
	{
		return null; // for override if define custom response
	}
	
	protected RestResponse<T> deleteCustomResponse(T object)
	{
		return null; // for override if define custom response
	}
	
	protected void beforeReturnResponse(T object, RestRequest restRequest)
	{
		// for override
	}
	
	protected void beforeReturnResponse(Iterable<T> list, RestRequest restRequest)
	{
		// for override
	}
	
	
	
}
