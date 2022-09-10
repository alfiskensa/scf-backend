package com.prodain.scf.common.rest;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonFilter("restResponse.filter")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class RestResponse<T> {

	private HttpStatus status;
	
	private String code;
	
	private String message;
	
	private T data;
	
	private Boolean isValid;
	
	public RestResponse(String message, HttpStatus status, String code) {
		this.status = status;
		this.message = message;
		this.code = code;
	}
}
