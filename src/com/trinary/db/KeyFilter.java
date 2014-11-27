package com.trinary.db;


public class KeyFilter extends Filter {
	protected Object obj;
	
	public KeyFilter(Object obj) {
		super(obj.getClass());
		this.obj = obj;
	}
	
	@Override
	protected String generatePartialWhereClause() throws IllegalArgumentException, IllegalAccessException {
		return new QueryBuilder().createWhereClause(obj);
	}

	@Override
	protected boolean isEmpty() {
		return false;
	}
}