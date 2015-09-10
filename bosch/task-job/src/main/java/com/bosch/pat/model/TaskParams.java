package com.bosch.pat.model;

import java.util.Calendar;

public class TaskParams 
{
	private Integer acntId ;
	private Long patientId ;
	private Long taskId ;
	private String description ;
	private Long createdById;
	private String createdByName;
	private Long assignedTo;
	private String assignedToName;
	private Integer status;
	private Calendar dueDate;
	private String dueDateFormatted;
	private Integer dueInDays;
	private TaskRecurringParams recurringParams = new TaskRecurringParams();
	private TaskOwner owner ;

	public class TaskOwner 
	{
		private Long id;
		private String name;
		private String timeZone;

		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTimeZone() {
			return timeZone;
		}
		public void setTimeZone(String timeZone) {
			this.timeZone = timeZone;
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

			retValue = "TaskOwner ( "
					+ super.toString() + TAB
					+ "id = " + this.id + TAB
					+ "name = " + this.name + TAB
					+ "timeZone = " + this.timeZone + TAB
					+ " )";

			return retValue;
		}
	}

	public class TaskRecurringParams 
	{
		private boolean isRecurring = false;
		private Long recurringId ;
		private Integer frequency ;
		private Calendar next_execution_date ;

		public Long getRecurringId() {
			return recurringId;
		}
		public void setRecurringId(Long recurringId) {
			this.recurringId = recurringId;
		}
		public Integer getFrequency() {
			return frequency;
		}
		public void setFrequency(Integer frequency) {
			this.frequency = frequency;
		}
		public Calendar getNext_execution_date() {
			return next_execution_date;
		}
		public void setNext_execution_date(Calendar next_execution_date) {
			this.next_execution_date = next_execution_date;
		}
		public boolean isRecurring() {
			return isRecurring;
		}
		public void setRecurring(boolean isRecurring) {
			this.isRecurring = isRecurring;
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

			retValue = "TaskRecurringParams ( "
					+ super.toString() + TAB
					+ "isRecurring = " + this.isRecurring + TAB
					+ "recurringId = " + this.recurringId + TAB
					+ "frequency = " + this.frequency + TAB
					+ "next_execution_date = " + this.next_execution_date + TAB
					+ " )";

			return retValue;
		}
	}

	public Integer getAcntId() {
		return acntId;
	}
	public void setAcntId(Integer acntId) {
		this.acntId = acntId;
	}

	public Long getPatientId() {
		return patientId;
	}
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public TaskOwner getOwner() {
		return owner;
	}
	public void setOwner(TaskOwner owner) {
		this.owner = owner;
	}

	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Calendar getDueDate() {
		return dueDate;
	}
	public void setDueDate(Calendar dueDate) {
		this.dueDate = dueDate;
	}

	public TaskRecurringParams getRecurringParams() {
		return recurringParams;
	}
	public void setRecurringParams(TaskRecurringParams recurringParams) {
		this.recurringParams = recurringParams;
	}

	public String getDueDateFormatted() {
		return dueDateFormatted;
	}
	public void setDueDateFormatted(String dueDateFormatted) {
		this.dueDateFormatted = dueDateFormatted;
	}

	public Long getCreatedById() {
		return createdById;
	}
	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public String getCreatedByName() {
		return createdByName;
	}
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Integer getDueInDays() {
		return dueInDays;
	}
	public void setDueInDays(Integer dueInDays) {
		this.dueInDays = dueInDays;
	}

	public Long getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(Long assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getAssignedToName() {
		return assignedToName;
	}
	public void setAssignedToName(String assignedToName) {
		this.assignedToName = assignedToName;
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

		retValue = "TaskParams ( "
				+ super.toString() + TAB
				+ "taskId = " + this.taskId + TAB
				+ "description = " + this.description + TAB
				+ "createdById = " + this.createdById + TAB
				+ "assignedTo = " + this.assignedTo + TAB
				+ "assignedToName = " + this.assignedToName + TAB
				+ "createdByName = " + this.createdByName + TAB
				+ "status = " + this.status + TAB
				+ "dueDate = " + this.dueDate + TAB
				+ "dueDateFormatted = " + this.dueDateFormatted + TAB
				+ "dueInDays = " + this.dueInDays + TAB
				+ "recurringParams = " + this.recurringParams + TAB
				+ " )";

		return retValue;
	}

}
