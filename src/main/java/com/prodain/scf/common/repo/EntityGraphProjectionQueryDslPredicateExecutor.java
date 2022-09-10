package com.prodain.scf.common.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Predicate;

public interface EntityGraphProjectionQueryDslPredicateExecutor<T> extends EntityGraphQuerydslPredicateExecutor<T> {
	
	public <Projection> Page<Projection> findAll(Predicate predicate, Pageable pageable, EntityGraph entityGraph,
			FactoryExpression<Projection> factoryExpression);

	public <Projection> Iterable<Projection> findAll(Predicate predicate, Sort sort, EntityGraph entityGraph,
			FactoryExpression<Projection> factoryExpression);

	public <Projection> Iterable<Projection> findAll(Predicate predicate, EntityGraph entityGraph, FactoryExpression<Projection> factoryExpression);

}
