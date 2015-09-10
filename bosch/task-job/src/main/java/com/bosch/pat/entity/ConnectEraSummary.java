package com.bosch.pat.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author nla4kor
 */
@Entity
@Table(name = "CONNECT_ERA_SUMMARY", schema = "HHERO")
@NamedQueries({
	@NamedQuery(name = "ConnectEraSummary.findByCurrentConnectEraId", 
			query = "SELECT c FROM ConnectEraSummary c WHERE c.patId = :patId AND c.acntId = :acntId")

})
@NamedNativeQueries({
	@NamedNativeQuery(
			name  = "ConnectEraSummaryNative.findByCurrentConnectEraId", 
			query = "SELECT connectEraSummary.* " +
					"FROM CONNECT_ERA_SUMMARY connectEraSummary " +
					"LEFT OUTER JOIN PAT pat ON connectEraSummary.ID = pat.CURRENT_CONNECT_ERA_ID "+
					"WHERE connectEraSummary.ID = pat.CURRENT_CONNECT_ERA_ID AND connectEraSummary.PAT_ID = :patId AND connectEraSummary.ACNT_ID = :acntId ",
					resultSetMapping = "ConnectEraSummary.ConnectEraSummaryDetails")
})
@SqlResultSetMappings({
	@SqlResultSetMapping(name = "ConnectEraSummary.ConnectEraSummaryDetails",
			entities={
			@EntityResult(
					entityClass = ConnectEraSummary.class
					)
	}
			)
})
public class ConnectEraSummary implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ces_seq", sequenceName = "CONNECT_ERA_SUMMARY_SEQ", allocationSize = 1)
	@GeneratedValue(generator = "ces_seq")
	@Column(name = "ID")
	private Long id;

	@Column(name = "FIRST_CONNECT_DATE")
	@Temporal(TemporalType.DATE)
	private Date firstConnectDate;

	@Column(name = "MOST_RECENT_CONNECT_DATE")
	@Temporal(TemporalType.DATE)
	private Date mostRecentConnectDate;

	@Column(name = "CURRENT_SURVEY_INTERFACE_ID")
	private Long currentSurveyInterfaceId;

	@Column(name = "PENDING_DISENROLLMENT_DATE")
	@Temporal(TemporalType.DATE)
	private Date pendingDisenrollmentDate;

	@Column(name = "VA_ADMISSION_ID")
	private Long vaAdmissionId;

	@Column(name = "MOST_RECENT_SESSION_KEY")
	private BigDecimal mostRecentSessionKey;

	@Column(name = "FIRST_SESSION_KEY")
	private BigDecimal firstSessionKey;

	@Column(name = "CURRENT_PAT_ENROLL_RSN_ID")
	private Long currentPatEnrollRsnId;

	@Column(name = "ACNT_ID", nullable = false)
	private Integer acntId;

	@Column(name = "PAT_ID")
	private Long patId;

	@Column(name = "DISENROLLER_ACNT_ID")
	private Integer disenroller_acnt_id;

	@Column(name = "DISENROLLER_ACNT_ENT_ID")
	private Long disenroller_acnt_ent_id;

	@Column(name = "DISENROLLMENT_DATE")
	@Temporal(TemporalType.DATE)
	private Date disenrollmentDate;

	@Column(name = "DISENROLLMENT_REASON_ID")
	private Integer disenrollmentReasonId;

	@Column(name = "ENROLLER_ACNT_ID")
	private Integer enroller_acnt_id;

	@Column(name = "ENROLLER_ACNT_ENT_ID")
	private Long enroller_acnt_ent_id;

	@Column(name = "ENROLLMENT_DATE")
	@Temporal(TemporalType.DATE)
	private Date enrollmentDate;

	@Column(name = "MOST_RECENT_TASK_DUE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date mostRecentTaskDueDate;

	@Version
	@Column(name = "VERSION_COUNT")
	private Integer version;

	public ConnectEraSummary() {

	}

	public ConnectEraSummary(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFirstConnectDate() {
		return firstConnectDate;
	}

	public void setFirstConnectDate(Date firstConnectDate) {
		this.firstConnectDate = firstConnectDate;
	}

	public Date getMostRecentConnectDate() {
		return mostRecentConnectDate;
	}

	public void setMostRecentConnectDate(Date mostRecentConnectDate) {
		this.mostRecentConnectDate = mostRecentConnectDate;
	}

	public Long getCurrentSurveyInterfaceId() {
		return currentSurveyInterfaceId;
	}

	public void setCurrentSurveyInterfaceId(Long currentSurveyInterfaceId) {
		this.currentSurveyInterfaceId = currentSurveyInterfaceId;
	}

	public Date getPendingDisenrollmentDate() {
		return pendingDisenrollmentDate;
	}

	public void setPendingDisenrollmentDate(Date pendingDisenrollmentDate) {
		this.pendingDisenrollmentDate = pendingDisenrollmentDate;
	}

	public Long getVaAdmissionId() {
		return vaAdmissionId;
	}

	public void setVaAdmissionId(Long vaAdmissionId) {
		this.vaAdmissionId = vaAdmissionId;
	}

	public BigDecimal getMostRecentSessionKey() {
		return mostRecentSessionKey;
	}

	public void setMostRecentSessionKey(BigDecimal mostRecentSessionKey) {
		this.mostRecentSessionKey = mostRecentSessionKey;
	}

	public BigDecimal getFirstSessionKey() {
		return firstSessionKey;
	}

	public void setFirstSessionKey(BigDecimal firstSessionKey) {
		this.firstSessionKey = firstSessionKey;
	}

	public Long getCurrentPatEnrollRsnId() {
		return currentPatEnrollRsnId;
	}

	public void setCurrentPatEnrollRsnId(Long currentPatEnrollRsnId) {
		this.currentPatEnrollRsnId = currentPatEnrollRsnId;
	}

	public Integer getAcntId() {
		return acntId;
	}

	public void setAcntId(Integer acntId) {
		this.acntId = acntId;
	}

	public Long getPatId() {
		return patId;
	}

	public void setPatId(Long patId) {
		this.patId = patId;
	}

	public Integer getDisenroller_acnt_id() {
		return disenroller_acnt_id;
	}

	public void setDisenroller_acnt_id(Integer disenroller_acnt_id) {
		this.disenroller_acnt_id = disenroller_acnt_id;
	}

	public Long getDisenroller_acnt_ent_id() {
		return disenroller_acnt_ent_id;
	}

	public void setDisenroller_acnt_ent_id(Long disenroller_acnt_ent_id) {
		this.disenroller_acnt_ent_id = disenroller_acnt_ent_id;
	}

	public Date getDisenrollmentDate() {
		return disenrollmentDate;
	}

	public void setDisenrollmentDate(Date disenrollmentDate) {
		this.disenrollmentDate = disenrollmentDate;
	}

	public Integer getDisenrollmentReasonId() {
		return disenrollmentReasonId;
	}

	public void setDisenrollmentReasonId(Integer disenrollmentReasonId) {
		this.disenrollmentReasonId = disenrollmentReasonId;
	}

	public Integer getEnroller_acnt_id() {
		return enroller_acnt_id;
	}

	public void setEnroller_acnt_id(Integer enroller_acnt_id) {
		this.enroller_acnt_id = enroller_acnt_id;
	}

	public Long getEnroller_acnt_ent_id() {
		return enroller_acnt_ent_id;
	}

	public void setEnroller_acnt_ent_id(Long enroller_acnt_ent_id) {
		this.enroller_acnt_ent_id = enroller_acnt_ent_id;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public Date getMostRecentTaskDueDate() {
		return mostRecentTaskDueDate;
	}

	public void setMostRecentTaskDueDate(Date mostRecentTaskDueDate) {
		this.mostRecentTaskDueDate = mostRecentTaskDueDate;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

		retValue = "ConnectEraSummary ( "
				+ super.toString() + TAB
				+ "id = " + this.id + TAB
				+ "firstConnectDate = " + this.firstConnectDate + TAB
				+ "mostRecentConnectDate = " + this.mostRecentConnectDate + TAB
				+ "currentSurveyInterfaceId = " + this.currentSurveyInterfaceId + TAB
				+ "pendingDisenrollmentDate = " + this.pendingDisenrollmentDate + TAB
				+ "vaAdmissionId = " + this.vaAdmissionId + TAB
				+ "mostRecentSessionKey = " + this.mostRecentSessionKey + TAB
				+ "firstSessionKey = " + this.firstSessionKey + TAB
				+ "currentPatEnrollRsnId = " + this.currentPatEnrollRsnId + TAB
				+ "acntId = " + this.acntId + TAB
				+ "patId = " + this.patId + TAB
				+ "disenroller_acnt_id = " + this.disenroller_acnt_id + TAB
				+ "disenroller_acnt_ent_id = " + this.disenroller_acnt_ent_id + TAB
				+ "disenrollmentDate = " + this.disenrollmentDate + TAB
				+ "disenrollmentReasonId = " + this.disenrollmentReasonId + TAB
				+ "enroller_acnt_id = " + this.enroller_acnt_id + TAB
				+ "enroller_acnt_ent_id = " + this.enroller_acnt_ent_id + TAB
				+ "enrollmentDate = " + this.enrollmentDate + TAB
				+ "mostRecentTaskDueDate = " + this.mostRecentTaskDueDate + TAB
				+ "version = " + this.version + TAB
				+ " )";

		return retValue;
	}



}
