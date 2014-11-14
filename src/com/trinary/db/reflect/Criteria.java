package com.trinary.db.reflect;

import java.util.ArrayList;

public class Criteria {
	protected ArrayList<Criteria> subCriteria = new ArrayList<Criteria>();
	protected BooleanOperation bOperation;
}
