package com.trinary.db.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import com.trinary.db.annotation.Column;
import com.trinary.db.annotation.Table;

public class QueryBuilder {
	protected String getTableName(Object obj) {
		Annotation annotation = obj.getClass().getAnnotation(Table.class);
		
		if (annotation == null) {
			return null;
		}
		
		Table tableInfo = (Table)annotation;
		return tableInfo.name();
	}
	
	protected String getFormattedValue(Object obj, Field field) throws IllegalArgumentException, IllegalAccessException {
		if (String.class.isAssignableFrom(field.getType())) {	// If needs quotes
			return "\"" + field.get(obj).toString() + "\"";
		} else {												// If no needs quotes
			return field.get(obj).toString();
		}
	}
	
	public Query createInsertQuery(Object obj) throws IllegalArgumentException, IllegalAccessException {
		String tableName = getTableName(obj);
		
		StringBuilder insertSegment = new StringBuilder();
		StringBuilder valueSegment = new StringBuilder();
		String seperator = "";
		
		insertSegment.append("INSERT INTO " + tableName + " (");
		valueSegment.append("VALUES (");
		
		for (Field field : obj.getClass().getDeclaredFields()) {
			Annotation colAnnotation = field.getAnnotation(Column.class);
			Column colInfo = (Column)colAnnotation;
			
			field.setAccessible(true);
			
			String columnName = colInfo.name();
			
			insertSegment.append(seperator + columnName);
			
			String value = getFormattedValue(obj, field);
			valueSegment.append(seperator + value);
			
			seperator = ", ";
			
			field.setAccessible(false);
		}
		
		insertSegment.append(") ");
		valueSegment.append(")");
		
		insertSegment.append(valueSegment);
		
		return new Query(QueryType.INSERT, insertSegment.toString());
	}
	
	public Query createUpdateQuery(Object obj) throws IllegalArgumentException, IllegalAccessException {
		return createUpdateQuery(obj, null);
	}
	
	public Query createUpdateQuery(Object obj, List<String> includes) throws IllegalArgumentException, IllegalAccessException {
		String tableName = getTableName(obj);
		
		StringBuilder updateSegment = new StringBuilder();
		String seperator = "";
		
		updateSegment.append("UPDATE " + tableName + " SET ");
		
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (includes == null || includes.contains(field.getName())) {
				Annotation colAnnotation = field.getAnnotation(Column.class);
				Column colInfo = (Column)colAnnotation;
				
				field.setAccessible(true);
				
				String columnName = colInfo.name();
				String updateItem = "";
				String value = getFormattedValue(obj, field);
				updateItem = String.format("%s=%s", columnName, value);
				updateSegment.append(seperator + updateItem);
				
				seperator = ", ";
				
				field.setAccessible(false);
			}
		}
		
		return new Query(QueryType.UPDATE, updateSegment.toString());
	}
}
