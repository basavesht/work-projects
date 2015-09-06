
package com.bosch.tmp.integration.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bosch.tmp.integration.persistence.HL7Message;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * Result entity stores the basic session vitals or adhoc vitals related information pushed from Result service through CIA.
 * Result contains generally only identifier like information for each result.
 *  - Result Id
 *  - Result Type (SESSION_VITAL_SIGN/VITAL_SIGN)
 *  - Organization Id
 *  - Session Id (Only in case of Survey)
 *  - Vital Sign Id (Both for SESSION and ADHOC vitals)
 * @author BEA2KOR
 *
 */
@Entity
@Table(name = "RESULT")
@NamedQueries
(
		{       
			@NamedQuery(name = "Result.findAllResults", 
					query = "SELECT r FROM Result r"), 

            @NamedQuery(name = "Result.findById",
            query = "SELECT r FROM Result r WHERE r.id = :id"),

            @NamedQuery(name = "Result.findResultsByOrganizationId",
            query = "SELECT r FROM Result r WHERE r.organizationId = :organizationId AND "
                    +"r.isAcknowledged = :isAcknowledged"),

            @NamedQuery(name = "Result.findResultsByResultId",
            query = "SELECT r FROM Result r WHERE r.resultId = :resultId AND "
                    +"r.isAcknowledged = :isAcknowledged AND "
                    +"r.type = :type"),

            @NamedQuery(name = "Result.findResultsBySessionId",
            query = "SELECT r FROM Result r WHERE r.sessionId = :sessionId AND "
                    +"r.organizationId = :organizationId AND "
                    +"r.isAcknowledged = :isAcknowledged AND "
                    +"r.type = :type"),

            @NamedQuery(name = "Result.findResultsByVitalSignAndOrgId",
            query = "SELECT r FROM Result r where r.vitalSignId = :vitalSignId AND "
                    +"r.organizationId = :organizationId AND "
                    +"r.isAcknowledged = :isAcknowledged AND "
                    +"r.type IN (:type))"),

            @NamedQuery(name = "Result.findResultsByOrganizationIdAndType",
            query = "SELECT r FROM Result r where r.organizationId = :organizationId AND "
                    +"r.isAcknowledged = :isAcknowledged AND "
                    +"r.type = :type")
		}
)
public class Result implements Serializable 
{
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "ID")
	private Long id;

	@Basic(optional = false)
	@Column(name = "CREATETIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimestamp;

	@Column(name = "ISACKNOWLEDGED")
	private Boolean isAcknowledged;

	@Column(name = "LOINCCODE")
	private String loincCode;

	@Column(name = "ORGANIZATIONID")
	private String organizationId;

	@Column(name = "PATIENTID")
	private String patientId;

	@Column(name = "PROGRAMNAME")
	private String programName;

	@Basic(optional = false)
	@Column(name = "RESPONSETIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date responseTimestamp;

	@Column(name = "RESULTID")
	private Long resultId;

	@Column(name = "SESSIONID")
	private Long sessionId;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "VITALSIGNID")
	private String vitalSignId;

	@JoinColumn(name = "HL7MESSAGEID", referencedColumnName = "ID")
	@ManyToOne
	private HL7Message hl7Message;

	public Result() {
	}

	public Result(Long id) {
		this.id = id;
	}

	public Result(Long id, Date createTimestamp, Date responseTimestamp) {
		this.id = id;
		this.createTimestamp = createTimestamp;
		this.responseTimestamp = responseTimestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Boolean getIsAcknowledged() {
		return isAcknowledged;
	}

	public void setIsAcknowledged(Boolean isAcknowledged) {
		this.isAcknowledged = isAcknowledged;
	}

	public String getLoincCode() {
		return loincCode;
	}

	public void setLoincCode(String loincCode) {
		this.loincCode = loincCode;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Date getResponseTimestamp() {
		return responseTimestamp;
	}

	public void setResponseTimestamp(Date responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}

	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVitalSignId() {
		return vitalSignId;
	}

	public void setVitalSignId(String vitalSignId) {
		this.vitalSignId = vitalSignId;
	}

	public HL7Message getHL7Message() {
		return hl7Message;
	}

	public void setHL7Message(HL7Message hl7Message) {
		this.hl7Message = hl7Message;
	}

    /**
     * To automatically generate current timestamp on creation of an entry.
     */
	@PrePersist
	protected void onCreate(){
		this.createTimestamp = new Date();
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Result)) {
			return false;
		}
		Result other = (Result) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.bosch.tmp.integration.entities.Result[id=" + id + "]";
	}

}
