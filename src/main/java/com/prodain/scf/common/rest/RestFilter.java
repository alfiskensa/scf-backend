package com.prodain.scf.common.rest;

import lombok.Data;

@Data
public class RestFilter
{
	public enum Operator
	{
		EQ,
		NE,
		LE,
		LT,
		GE,
		GT,
		LIKE
	}
	
	private String path;
	private Operator operator;
	private Object value;
	
	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) this.value;
	}
}