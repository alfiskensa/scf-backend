package com.prodain.scf.model.repo;

import java.math.BigInteger;
import java.util.Optional;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphCrudRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.prodain.scf.model.entity.User;

public interface UserRepo extends EntityGraphCrudRepository<User, BigInteger>, EntityGraphQuerydslPredicateExecutor<User>{

	public Optional<User> findFirstByUsername(String username);
	
	public Boolean existsByUsername(String username);

	public Boolean existsByEmail(String email);
	
}
