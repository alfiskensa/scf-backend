package com.prodain.scf.common.rest;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;

public abstract class FilterableJpaRestController<T, ID extends Serializable, Q extends EntityPathBase<T>>
		extends JpaRestController<T, ID> {
	@Autowired
	protected QuerydslPredicateExecutor<T> repo;

	@Override
	public ResponseEntity<?> findAll(RestRequest restRequest) throws Exception {
		BooleanExpression predicate = createFilter(restRequest);

		Iterable<T> list = doFindAllFiltered(restRequest, predicate, repo, resourceClass);
		
		beforeReturnResponse(list, restRequest);
		
		return createResponse(list, restRequest, resourceClass);
	}

	@Override
	@GetMapping("/count")
	public ResponseEntity<?> countAll(RestRequest restRequest) throws Exception {
		BooleanExpression predicate = createFilter(restRequest);

		return ResponseEntity.ok(createSingleValueResponse("count", repo.count(predicate)));
	}

	protected BooleanExpression createFilter(RestRequest restRequest) throws Exception {
		BooleanExpression predicate = null;
		Q pathBase = getPathBase();

		for (RestFilter restFilter : processFilter(restRequest)) {
			BooleanExpression curPredicate = createPredicate(pathBase, restFilter);

			if (curPredicate != null) {
				if (predicate == null)
					predicate = curPredicate;
				else
					predicate = predicate.and(curPredicate);
			}
		}
		
		if(restRequest.isSearchRequest()) {
			if (predicate == null)
				predicate = createSearchPredicate(pathBase, restRequest.getSearch());
			else
				predicate = predicate.and(createSearchPredicate(pathBase, restRequest.getSearch()));
		}

		return predicate;
	}
	
	protected Set<RestFilter> processFilter(RestRequest restRequest) throws Exception {
		return restEngine.processFilter(resourceClass, restRequest.getFilter());
	}

	protected abstract Q getPathBase();

	protected abstract BooleanExpression createPredicate(Q pathBase, RestFilter filter);
	
	protected abstract BooleanExpression createSearchPredicate(Q pathBase, String search);
}