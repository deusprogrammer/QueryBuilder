package com.trinary.db;

import java.util.ArrayList;
import java.util.List;

public class Select<T> extends FilterableStatement<T> {
	protected List<String> excludes, includes;
	
	public Select(Class<?> clazz) {
		super(null);
		filters.setType(clazz);
		this.clazz = clazz;
		excludes = new ArrayList<String>();
		includes = new ArrayList<String>();
	}

	@Override
	public String generateQuery() throws Exception {
		return builder.createSelectQuery(clazz, filters, includes, excludes);
	}

	public Select<T> include(String field) {
		if (!excludes.isEmpty()) {
			excludes.clear();
		}
		includes.add(field);
		return this;
	}
	
	public Select<T> exclude(String field) {
		if (!includes.isEmpty()) {
			includes.clear();
		}
		excludes.add(field);
		return this;
	}
	
	public Select<T> clear() {
		includes.clear();
		excludes.clear();
		return this;
	}
}
