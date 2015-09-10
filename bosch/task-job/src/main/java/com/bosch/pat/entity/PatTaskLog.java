package com.bosch.pat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PAT_TASK_LOG", schema = "HHERO")
public class PatTaskLog implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	@SequenceGenerator(name = "taskLogSEQ", sequenceName = "PAT_TASK_LOG_SEQ", allocationSize = 1)
	@GeneratedValue(generator = "taskLogSEQ")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TASK_ID", nullable = true, updatable = false, referencedColumnName = "TASK_ID")
	private PatTask taskId;

	@Column(name = "PAT_ID", nullable = false)
	private Long patId;

	@Column(name = "ACNT_ID", nullable = false)
	private Integer acntId;

	@Column(name = "DUE_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "ACTION")
	private String action;

	@Column(name = "MODIFIED_BY_ID", nullable = false)
	private Long modifiedById;

	@Column(name = "MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PatTask getTaskId() {
		return taskId;
	}

	public void setTaskId(PatTask taskId) {
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(Long modifiedById) {
		this.modifiedById = modifiedById;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

		retValue = "PatTaskLog ( "
				+ super.toString() + TAB
				+ "id = " + this.id + TAB
				+ "taskId = " + this.taskId + TAB
				+ "patId = " + this.patId + TAB
				+ "acntId = " + this.acntId + TAB
				+ "dueDate = " + this.dueDate + TAB
				+ "description = " + this.description + TAB
				+ "action = " + this.action + TAB
				+ "modifiedById = " + this.modifiedById + TAB
				+ "modifiedDate = " + this.modifiedDate + TAB
				+ " )";

		return retValue;
	}


}
