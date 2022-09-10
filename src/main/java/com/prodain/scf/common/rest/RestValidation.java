package com.prodain.scf.common.rest;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RestValidation<T> {
	
	private HttpStatus status;
	
	private Boolean isValid;

}
