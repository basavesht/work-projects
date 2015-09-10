package com.bosch.pat.model;

import java.sql.Timestamp;

public class TaskJobParameters 
{
	private Timestamp startTime;
	private Timestamp endTime;

	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
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

		retValue = "TaskJobParameters ( "
				+ super.toString() + TAB
				+ "startTime = " + this.startTime + TAB
				+ "endTime = " + this.endTime + TAB
				+ " )";

		return retValue;
	}


}
