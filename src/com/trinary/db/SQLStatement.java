package com.trinary.db;

import java.util.List;

public abstract class SQLStatement<T> {
	protected QueryBuilder builder = new QueryBuilder();
	
	protected Class<?> clazz;
	protected Object obj;
	protected List<T> results;
	
	public SQLStatement(Object obj) {
		this.obj = obj;
	}
	
	public void execute() throws Exception {
		String query = generateQuery();
		System.out.println(String.format("Executing: '%s'", query));
		// ResultSet results = executeQuery(query);
		// Convert to VO
	}
	
	public List<T> getResults() {
		return results;
	}
	
	public abstract String generateQuery() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, Exception;
}