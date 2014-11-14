package com.trinary.db.entity.test;

import com.trinary.db.reflect.Query;
import com.trinary.db.reflect.QueryBuilder;

public class Application {
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		User user = new User("mmain", "password");
		
		QueryBuilder qb = new QueryBuilder();
		Query query1 = qb.createInsertQuery(user);
		Query query2 = qb.createUpdateQuery(user);
		
		System.out.println(query1.getSql());
		System.out.println(query2.getSql());
	}
}