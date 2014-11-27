package com.trinary.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.trinary.db.annotation.Column;
import com.trinary.db.annotation.PrimaryKey;
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
	
	protected String getTableName(Class<?> clazz) {
		Annotation annotation = clazz.getAnnotation(Table.class);
		
		if (annotation == null) {
			return null;
		}
		
		Table tableInfo = (Table)annotation;
		return tableInfo.name();
	}
	
	public String getFormattedValue(Object obj, Field field) throws IllegalArgumentException, IllegalAccessException {
		if (String.class.isAssignableFrom(field.getType())) {	// If needs quotes
			return "\"" + field.get(obj).toString() + "\"";
		} else {												// If no needs quotes
			return field.get(obj).toString();
		}
	}
	
	public String getFormattedValue(Class<?> clazz, String fieldName, String value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Field field = clazz.getDeclaredField(fieldName);
		if (String.class.isAssignableFrom(field.getType())) {	// If needs quotes
			return "\"" + value + "\"";
		} else {												// If no needs quotes
			return value;
		}
	}
	
	public String getTableFieldName(Class<?> clazz, String fieldName) throws NoSuchFieldException, SecurityException {
		Field field = clazz.getDeclaredField(fieldName);
		Annotation colAnnotation = field.getAnnotation(Column.class);
		Column colInfo = (Column)colAnnotation;
		
		String columnName;
		if (colInfo != null) {
			columnName = colInfo.name();
		} else {
			columnName = field.getName();
		}
		
		return columnName;
	}
	
	public String createSelectQuery(Class<?> clazz, Filter filter, List<String> includes, List<String> excludes) throws Exception {
		String tableName = getTableName(clazz);
		StringBuilder selectClause = new StringBuilder();
		String seperator = "";
		
		if (includes != null && excludes != null && !includes.isEmpty() && !excludes.isEmpty()) {
			throw new Exception("Only includes or excludes can be set");
		} else if ((includes == null || includes.isEmpty()) && (excludes == null || excludes.isEmpty())) {
			selectClause.append("*");
		} else {
			for (Field field : clazz.getDeclaredFields()) {
				if ((includes.isEmpty() && excludes.contains(field.getName())) || (excludes.isEmpty() && !includes.contains(field.getName()))) {
					continue;
				}
				
				Annotation colAnnotation = field.getAnnotation(Column.class);
				Column colInfo = (Column)colAnnotation;
				
				String columnName;
				if (colInfo != null) {
					columnName = colInfo.name();
				} else {
					columnName = field.getName();
				}
				
				String selectItem = "";
				
				selectItem = String.format("%s", columnName);
				selectClause
					.append(seperator)
					.append(selectItem);
				seperator = ", ";
			}
		}
		
		StringBuilder selectStatement = new StringBuilder();
		selectStatement.append(String.format("SELECT %s FROM %s", selectClause.toString(), tableName));
		
		if (filter != null && !filter.isEmpty()) {
			String whereClause = String.format(" WHERE %s", filter.generatePartialWhereClause());
			selectStatement.append(whereClause);
		}
		
		return selectStatement.toString();
	}
	
	public String createInsertQuery(Object obj) throws IllegalArgumentException, IllegalAccessException {
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
			
			String columnName;
			if (colInfo != null) {
				columnName = colInfo.name();
			} else {
				columnName = field.getName();
			}
			
			insertSegment.append(seperator + columnName);
			
			String value = getFormattedValue(obj, field);
			valueSegment.append(seperator + value);
			
			seperator = ", ";
			
			field.setAccessible(false);
		}
		
		insertSegment.append(") ");
		valueSegment.append(")");
		
		insertSegment.append(valueSegment);
		
		return insertSegment.toString();
	}
	
	public String createUpdateQuery(Object obj, Filter filter) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String tableName = getTableName(obj);
		String setClause = createSetClause(obj);
		String whereClause;
		
		if (filter == null || filter.isEmpty()) {
			whereClause = createWhereClause(obj);
		} else {
			whereClause = filter.generatePartialWhereClause();
		}
		
		return String.format("UPDATE %s SET %s WHERE %s", tableName, setClause, whereClause);
	}
	
	public String createUpdateQuery(Object obj, Filter filter, Map<String, String> updateMap) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String tableName = getTableName(obj);
		String setClause = createSetClause(obj, updateMap);
		String whereClause;
		
		if (filter == null || filter.isEmpty()) {
			whereClause = createWhereClause(obj);
		} else {
			whereClause = filter.generatePartialWhereClause();
		}
		
		return String.format("UPDATE %s SET %s WHERE %s", tableName, setClause, whereClause);
	}
	
	public String createDeleteQuery(Object obj, Filter filter) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String tableName = getTableName(obj);
		String whereClause = createWhereClause(obj);
		
		if (filter == null || filter.isEmpty()) {
			whereClause = createWhereClause(obj);
		} else {
			whereClause = filter.generatePartialWhereClause();
		}
		
		return String.format("DELETE FROM %s WHERE %s", tableName, whereClause);
	}
	
	public String createSetClause(Object obj) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder updateSegment = new StringBuilder();
		String seperator = "";
		
		for (Field field : obj.getClass().getDeclaredFields()) {
			Annotation colAnnotation = field.getAnnotation(Column.class);
			Column colInfo = (Column)colAnnotation;
			
			field.setAccessible(true);
			
			String columnName;
			if (colInfo != null) {
				columnName = colInfo.name();
			} else {
				columnName = field.getName();
			}
			
			String updateItem = "";
			String value = getFormattedValue(obj, field);
			
			updateItem = String.format("%s=%s", columnName, value);
			updateSegment
				.append(seperator)
				.append(updateItem);
			seperator = ", ";
			
			field.setAccessible(false);
		}
		
		return updateSegment.toString();
	}
	
	public String createSetClause(Object obj, Map<String, String> updateMap) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		StringBuilder updateSegment = new StringBuilder();
		String seperator = "";
		
		for (Entry<String, String> entry : updateMap.entrySet()) {
			Field field = obj.getClass().getDeclaredField(entry.getKey());
			
			Annotation colAnnotation = field.getAnnotation(Column.class);
			Column colInfo = (Column)colAnnotation;
			
			field.setAccessible(true);
			
			String columnName;
			if (colInfo != null) {
				columnName = colInfo.name();
			} else {
				columnName = field.getName();
			}
			
			String updateItem = "";
			String value = getFormattedValue(obj.getClass(), field.getName(), entry.getValue());
			
			updateItem = String.format("%s=%s", columnName, value);
			updateSegment
				.append(seperator)
				.append(updateItem);
			seperator = ", ";
			
			field.setAccessible(false);
		}
		
		return updateSegment.toString();
	}
	
	public String createWhereClause(Object obj) throws IllegalArgumentException, IllegalAccessException {		
		StringBuilder whereClause = new StringBuilder();
		String whereSeperator = "";
		
		for (Field field : obj.getClass().getDeclaredFields()) {
			Annotation primaryKey = field.getAnnotation(PrimaryKey.class);
			
			Annotation colAnnotation = field.getAnnotation(Column.class);
			Column colInfo = (Column)colAnnotation;
			
			field.setAccessible(true);
			
			String columnName;
			if (colInfo != null) {
				columnName = colInfo.name();
			} else {
				columnName = field.getName();
			}
			
			String value = getFormattedValue(obj, field);
			
			if (primaryKey != null) {
				String filter = String.format("%s=%s", columnName, value);
				whereClause
					.append(whereSeperator)
					.append(filter);
				whereSeperator = " AND ";
			}
			
			field.setAccessible(false);
		}
		
		return whereClause.toString();
	}
}
