package com.trinary.db;

public class SimpleFilter extends Filter {
	protected String field;
	protected String value;
	protected String op;
	
	public SimpleFilter(String field, String op, String value) {
		super(null);
		this.field = field;
		this.op = op;
		this.value = value;
	}

	protected SimpleFilter(String field, String op, String value, Class<?> clazz) {
		this(field, op, value);
		this.clazz = clazz;
	}
	
	@Override
	public String generatePartialWhereClause() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		QueryBuilder builder = new QueryBuilder();
		return String.format("%s%s%s", builder.getTableFieldName(this.getRootFilter().getType(), field), op, builder.getFormattedValue(this.getRootFilter().getType(), field, value));
	}

	@Override
	protected boolean isEmpty() {
		return false;
	}
}
