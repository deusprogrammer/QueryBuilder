package com.trinary.db;


public abstract class FilterableStatement<T> extends SQLStatement<T> {
	protected AndFilter filters = new AndFilter();
	
	public FilterableStatement(Object obj) {
		super(obj);
		if (obj != null) {
			filters.setType(obj.getClass());
		}
	}

	public FilterableStatement<T> addFilter(Filter filter) {
		filter.setParentFilter(filters);
		filters.addFilter(filter);
		return this;
	}
	
	protected String generateWhereClause() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return String.format("WHERE %s", filters.generatePartialWhereClause());
	}
}