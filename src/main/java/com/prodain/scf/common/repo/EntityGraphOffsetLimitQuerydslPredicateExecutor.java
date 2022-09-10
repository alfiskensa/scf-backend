package com.prodain.scf.common.repo;

import java.util.function.Function;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.querydsl.core.types.Predicate;

@NoRepositoryBean
public interface EntityGraphOffsetLimitQuerydslPredicateExecutor<T> extends OffsetLimitQuerydslPredicateExecutor<T>, EntityGraphQuerydslPredicateExecutor<T>
{
	public Iterable<T> findAllOffsetLimit(Predicate predicate, OffsetLimit offsetLimitRequest, EntityGraph entityGraph);
	
	@Override
	default <S extends T, R> R findBy(Predicate predicate, Function<FetchableFluentQuery<S>, R> queryFunction) {
		// TODO Auto-generated method stub
		return null;
	}
}
