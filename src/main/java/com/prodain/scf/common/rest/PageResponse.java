package com.prodain.scf.common.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Getter;
import lombok.Setter;

@JsonFilter("pageResponse.filter")
@Getter @Setter
public class PageResponse {

	private Integer count;
	
	private Integer pageSize;
	
	private Integer page;
	
	private Integer pageCount;
	
	private List results;
}
