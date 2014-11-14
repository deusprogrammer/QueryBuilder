package com.trinary.db.reflect;

public class Query {
	protected QueryType type;
	protected String query;
	
	public Query(QueryType type, String query) {
		this.type = type;
		this.query = query;
	}
	
	public void addCriteria(Criteria criteria) {
		switch(type) {
		case INSERT:
			return;
		default:
			return;
		}
	}
	
	public String getSql() {
		return query;
	}
}
