package com.bosch.pat.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskRequest 
{
	private Map<String, Object> searchCreateria ;
	private List<TaskParams> tasks;

	public List<TaskParams> getTasks() {
		if(tasks == null) {
			tasks =  new ArrayList<TaskParams>();
		}
		return tasks;
	}
	public Map<String, Object> getSearchCreateria() {
		return searchCreateria;
	}
	public void setSearchCreateria(Map<String, Object> searchCreateria) {
		this.searchCreateria = searchCreateria;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
		final String TAB = "\r\n";

		String retValue = "";

		retValue = "TaskRequest ( "
				+ super.toString() + TAB
				+ "searchCreateria = " + this.searchCreateria + TAB
				+ "tasks = " + this.tasks + TAB
				+ " )";

		return retValue;
	}

}
