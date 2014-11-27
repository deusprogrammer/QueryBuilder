package com.trinary.db;

import java.util.ArrayList;
import java.util.List;

public abstract class ComplexFilter extends Filter {
	protected BooleanOperation op;
	protected List<Filter> filters = new ArrayList<Filter>();
	
	public ComplexFilter(BooleanOperation op) {
		super(null);
		this.op = op;
	}
	
	public ComplexFilter addFilter(Filter filter) {
		filter.setParentFilter(this);
		filters.add(filter);
		return this;
	}
	
	@Override
	protected boolean isEmpty() {
		return filters.isEmpty();
	}

	@Override
	protected String generatePartialWhereClause() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return generatePartialWhereClause(0);
	}
	
	@Override
	protected String generatePartialWhereClause(int level) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		StringBuilder whereClause = new StringBuilder();
		String seperator = "";
		
		for (Filter filter : filters) {
			whereClause
				.append(seperator)
				.append(filter.generatePartialWhereClause(level + 1));
			
			seperator = String.format(" %s ", op.getValue());
		}

		String ret;
		
		if (level > 0) {
			ret = String.format("(%s)", whereClause.toString());
		} else {
			ret = whereClause.toString();
		}
		
		return ret;
	}
}
