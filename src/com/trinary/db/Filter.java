package com.trinary.db;

public abstract class Filter {
	protected Class<?> clazz;
	protected ComplexFilter parentFilter = null;
	
	public Filter(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	protected void setType(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	protected Class<?> getType() {
		return this.clazz;
	}
	
	protected void setParentFilter(ComplexFilter parentFilter) {
		this.parentFilter = parentFilter;
	}
	
	protected ComplexFilter getRootFilter() {
		ComplexFilter filter = parentFilter;
		ComplexFilter lastFilter = null;
		
		while (filter != null) {
			lastFilter = filter;
			filter = filter.parentFilter;
		}
		
		return lastFilter;
	}
	
	protected abstract String generatePartialWhereClause() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException;
	
	protected abstract boolean isEmpty();

	protected String generatePartialWhereClause(int i) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return generatePartialWhereClause();
	}
}