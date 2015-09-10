package com.bosch.pat.util;

public class TaskConstants {

	//Task Status code...
	public static final int TASK_INCOMPLETE_STATUS = 0;
	public static final int TASK_COMPLETED_STATUS = 1;
	public static final int TASK_REMOVED_STATUS = 2;

	//Check if complete recurring needs to be removed...
	public static final boolean _REMOVE_THIS_RECURRING_INSTANCE = true;

	//Search createria...
	public static final String SEARCH_BY_STATUS = "STATUS";

	//AUDIT-LOG Action events...
	public static final String TASK_MODIFIED_ACTION = "MODIFIED";
	public static final String TASK_REMOVED_ACTION = "REMOVED";
	public static final String TASK_COMPLETED_ACTION = "COMPLETED";
	
	public static final String DEF_TZ = "GMT";
}
