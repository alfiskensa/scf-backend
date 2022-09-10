package com.prodain.scf.model.repo;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.prodain.scf.model.entity.ERole;
import com.prodain.scf.model.entity.Role;

public interface RoleRepo extends CrudRepository<Role, BigInteger>{

	public Optional<Role> findByName(String name);
}
