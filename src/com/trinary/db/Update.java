package com.trinary.db;

import java.util.HashMap;
import java.util.Map;

public class Update<T> extends FilterableStatement<T> {
	protected Map<String, String> updateMap = new HashMap<String, String>();
	
	public Update(Object obj) {
		super(obj);
	}
	
	public Update<T> set(String key, String value) {
		updateMap.put(key, value);
		return this;
	}

	@Override
	public String generateQuery() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (updateMap != null) {
			return builder.createUpdateQuery(obj, filters, updateMap);
		} else {
			return builder.createUpdateQuery(obj, filters);
		}
	}
}