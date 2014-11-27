package com.trinary.db;

public class Insert<T> extends SQLStatement<T> {
	public Insert(Object obj) {
		super(obj);
	}

	@Override
	public String generateQuery() throws IllegalArgumentException, IllegalAccessException {
		return builder.createInsertQuery(obj);
	}
}
