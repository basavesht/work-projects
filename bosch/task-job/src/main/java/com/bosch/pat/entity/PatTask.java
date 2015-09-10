package com.bosch.pat.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "PAT_TASK", schema = "HHERO")
@NamedQueries({
	@NamedQuery(
			name  = "PatTask.findTaskByPatId", 
			query = "SELECT task FROM PatTask task WHERE task.patId = :patId AND task.acntId = :acntId"),
	@NamedQuery(
			name  = "PatTask.findNextPossibleTaskDueDateByPatId", 
			query = "SELECT task.patId, task.acntId, MIN(task.dueDate) FROM PatTask task " +
					"WHERE task.patId = :patId AND task.acntId = :acntId AND task.status = :status " +
					"GROUP BY (task.patId,task.acntId)"),
    @NamedQuery(
			name  = "PatTask.findTaskByPatIdAndStatus", 
			query = "SELECT task FROM PatTask task " +
					"WHERE task.patId = :patId AND task.acntId = :acntId AND task.status IN (:statuses) " +
					"ORDER BY task.dueDate ASC")
})
@NamedNativeQueries({
	@NamedNativeQuery(
			name  = "PatTaskNative.findTaskByPatIdAndStatus", 
			query = "SELECT task.*, " +
					  "acnt_ent_create.ACNT_ENT_NAME AS TASK_CREATED_BY_NAME, " +
					  "acnt_ent_assign.ACNT_ENT_NAME AS TASK_ASSIGNED_TO_NAME " +
					"FROM PAT_TASK task " +
					"LEFT OUTER JOIN PAT_ENT pat_ent ON task.ACNT_ID = pat_ent.ACNT_ID AND task.PAT_ID = pat_ent.PAT_ID AND pat_ent.acnt_ent_role_cd = 1 " +
					"LEFT OUTER JOIN ACNT_ENT acnt_ent_create ON task.CREATED_BY_ID = acnt_ent_create.ENT_ID AND task.ACNT_ID = acnt_ent_create.ACNT_ID " +
					"LEFT OUTER JOIN ACNT_ENT acnt_ent_assign ON task.ASSIGNED_TO = acnt_ent_assign.ENT_ID AND task.ACNT_ID = acnt_ent_assign.ACNT_ID " +
					"WHERE task.PAT_ID = :patId AND task.ACNT_ID = :acntId AND task.STATUS IN (:statuses) " +
					"ORDER BY task.DUE_DATE ASC",
			resultSetMapping = "PatTask.Task_Result")
})
@SqlResultSetMappings({
	@SqlResultSetMapping(name = "PatTask.Task_Result",
			entities={
					@EntityResult(
							entityClass = PatTask.class
					)
			},
			columns = {
					@ColumnResult(name = "TASK_CREATED_BY_NAME"),
					@ColumnResult(name = "TASK_ASSIGNED_TO_NAME")
		    })
})

public class PatTask implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TASK_ID", nullable = false)
	@SequenceGenerator(name = "taskSEQ", sequenceName = "PAT_TASK_SEQ", allocationSize = 1)
	@GeneratedValue(generator = "taskSEQ")
	private Long taskId;

	@Column(name = "PAT_ID", nullable = false)
	private Long patId;

	@Column(name = "ACNT_ID", nullable = false)
	private Integer acntId;

	@Column(name = "DUE_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "ASSIGNED_TO", nullable = false)
	private Long assignedTo;

	@Column(name = "CREATED_BY_ID", nullable = false)
	private Long createdById;

	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "CREATED_BY_NAME")
	private String createdByName;

	@Column(name = "MODIFIED_BY_ID")
	private Long modifiedById;

	@JoinColumn(name = "TASK_RECURRING_ID", nullable = true, referencedColumnName = "TASK_RECURRING_ID")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private PatTaskRecurring recurringTaskParams;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taskId", cascade = CascadeType.DETACH)
	private Set<PatTaskLog> taskLogs = new HashSet<PatTaskLog>();

	@Version
	@Column(name = "VERSION_COUNT")
	private Integer version;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
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

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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

	public PatTaskRecurring getRecurringTaskParams() {
		return recurringTaskParams;
	}

	public void setRecurringTaskParams(PatTaskRecurring recurringTaskParams) {
		this.recurringTaskParams = recurringTaskParams;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Set<PatTaskLog> getTaskLogs() {
		return taskLogs;
	}

	public void setTaskLogs(Set<PatTaskLog> taskLogs) {
		this.taskLogs = taskLogs;
	}

	public Long getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Long assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Long getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Long modifiedById) {
		this.modifiedById = modifiedById;
	}

	/**
	 * Check if the TASK is recurring
	 * @return
	 */
	public boolean isRecurringTask() {
		if(recurringTaskParams != null && recurringTaskParams.getTaskRecurringId() != null) {
			return true;
		}
		return false;
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

		retValue = "PatTask ( "
				+ super.toString() + TAB
				+ "taskId = " + this.taskId + TAB
				+ "patId = " + this.patId + TAB
				+ "acntId = " + this.acntId + TAB
				+ "dueDate = " + this.dueDate + TAB
				+ "description = " + this.description + TAB
				+ "status = " + this.status + TAB
				+ "assignedTo = " + this.assignedTo + TAB
				+ "createdById = " + this.createdById + TAB
				+ "createdDate = " + this.createdDate + TAB
				+ "createdByName = " + this.createdByName + TAB
				+ "modifiedById = " + this.modifiedById + TAB
				+ "recurringTaskParams = " + this.recurringTaskParams + TAB
				+ "taskLogs = " + this.taskLogs + TAB
				+ "version = " + this.version + TAB
				+ " )";

		return retValue;
	}

}
