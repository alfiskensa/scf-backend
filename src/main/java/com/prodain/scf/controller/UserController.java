package com.prodain.scf.controller;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prodain.scf.common.rest.FilterableJpaRestController;
import com.prodain.scf.common.rest.RestFilter;
import com.prodain.scf.common.rest.RestResponse;
import com.prodain.scf.model.entity.QUser;
import com.prodain.scf.model.entity.User;
import com.prodain.scf.model.repo.RoleRepo;
import com.querydsl.core.types.dsl.BooleanExpression;

@RestController
@RequestMapping("${rest.pathPrefix:api}/user")
public class UserController extends FilterableJpaRestController<User, BigInteger, QUser>{

	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	protected QUser getPathBase() {
		// TODO Auto-generated method stub
		return QUser.user;
	}

	@Override
	protected BooleanExpression createPredicate(QUser pathBase, RestFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RestResponse<User> createValidation(User object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RestResponse<User> updateValidation(User object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BooleanExpression createSearchPredicate(QUser pathBase, String search) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GetMapping("/roles")
	public ResponseEntity<?> getRoles(){
		
		return ResponseEntity.ok().body(roleRepo.findAll());
	}

}
