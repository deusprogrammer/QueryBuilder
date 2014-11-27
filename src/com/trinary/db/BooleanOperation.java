package com.trinary.db;

public enum BooleanOperation {
	AND ("AND"),
	OR ("OR");
	
	protected String value;
	
	public String getValue() {
		return value;
	}
	
	private BooleanOperation(String value) {
		this.value = value;
	}
}
