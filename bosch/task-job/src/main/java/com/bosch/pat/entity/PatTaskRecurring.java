package com.bosch.pat.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "PAT_TASK_RECURRING", schema = "HHERO")
@NamedQueries({
	@NamedQuery(
			name  = "PatTaskRecurring.findTasksByExecutionDate", 
			query = "SELECT recurringTask FROM PatTaskRecurring recurringTask " +
					"WHERE recurringTask.nextExecutionDate BETWEEN :startTime AND :endTime ")
})

public class PatTaskRecurring implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4030167172258685007L;

	@Id
	@Column(name = "TASK_RECURRING_ID", nullable = false)
	@SequenceGenerator(name = "taskRecurringSEQ", sequenceName = "PAT_TASK_RECURRING_SEQ", allocationSize = 1)
	@GeneratedValue(generator = "taskRecurringSEQ")
	private Long taskRecurringId;

	@Column(name = "PAT_ID", nullable = false)
	private Long patId;

	@Column(name = "ACNT_ID", nullable = false)
	private Integer acntId;

	@Column(name = "START_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "NEXT_EXECUTION_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date nextExecutionDate;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "FREQUENCY")
	private Integer frequency;

	@Column(name = "CREATED_BY_ID", nullable = false)
	private Long createdById;

	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "CREATED_BY_NAME")
	private String createdByName;

	@Column(name = "ASSIGNED_TO", nullable = false)
	private Long assignedTo;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="recurringTaskParams", cascade = CascadeType.ALL)
	private List<PatTask> taskParamsList;

	@Version
	@Column(name = "VERSION_COUNT")
	private Integer version;

	public Long getTaskRecurringId() {
		return taskRecurringId;
	}

	public void setTaskRecurringId(Long taskRecurringId) {
		this.taskRecurringId = taskRecurringId;
	}

	public Long getPatId() {
		return patId;
	}

	public void setPatId(Long patId) {
		this.patId = patId;
	}

	public Integer getAcntId() {
		return acntId;
	}

	public void setAcntId(Integer acntId) {
		this.acntId = acntId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getNextExecutionDate() {
		return nextExecutionDate;
	}

	public void setNextExecutionDate(Date nextExecutionDate) {
		this.nextExecutionDate = nextExecutionDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public List<PatTask> getTaskParamsList() {
		if(taskParamsList == null) {
			taskParamsList = new ArrayList<PatTask>();
		}
		return taskParamsList;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Long assignedTo) {
		this.assignedTo = assignedTo;
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

		retValue = "PatTaskRecurring ( "
				+ super.toString() + TAB
				+ "taskRecurringId = " + this.taskRecurringId + TAB
				+ "patId = " + this.patId + TAB
				+ "acntId = " + this.acntId + TAB
				+ "startDate = " + this.startDate + TAB
				+ "nextExecutionDate = " + this.nextExecutionDate + TAB
				+ "description = " + this.description + TAB
				+ "frequency = " + this.frequency + TAB
				+ "createdById = " + this.createdById + TAB
				+ "createdDate = " + this.createdDate + TAB
				+ "createdByName = " + this.createdByName + TAB
				+ "assignedTo = " + this.assignedTo + TAB
				+ "taskParamsList = " + this.taskParamsList + TAB
				+ "version = " + this.version + TAB
				+ " )";

		return retValue;
	}

}
