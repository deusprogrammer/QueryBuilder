package com.trinary.db;


public class Delete<T> extends FilterableStatement<T> {
	public Delete(Object obj) {
		super(obj);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String generateQuery() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return builder.createDeleteQuery(obj, filters);
	}
}
