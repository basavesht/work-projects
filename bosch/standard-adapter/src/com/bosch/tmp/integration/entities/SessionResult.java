
package com.bosch.tmp.integration.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SessionResult entity stores the basic session related information taken from T400/HB3 which is pushed from Result service through CIA.
 * Result contains generally only identifier like information for each result.
 *  - Session Id
 *  - Organization Id
 * @author BEA2KOR
 *
 */
@Entity
@Table(name = "SESSION_RESULT")
@NamedQueries
(
		{
	        @NamedQuery(name = "SessionResult.findAll", 
	        			query = "SELECT s FROM SessionResult s"), 
	        			
			@NamedQuery(name = "SessionResult.findById", 
						query = "SELECT s FROM SessionResult s WHERE s.id = :id"), 
						
			@NamedQuery(name = "SessionResult.findSessionResultBySessionId", 
						query = "SELECT s FROM SessionResult s WHERE s.sessionId = :sessionId AND "
						+"s.isAcknowledged = :isAcknowledged"), 
						
			@NamedQuery(name = "SessionResult.findSessionResultsByOrganizationId", 
						query = "SELECT s FROM SessionResult s WHERE s.organizationId = :organizationId AND "
						+"s.isAcknowledged = :isAcknowledged"),
			
			@NamedQuery(name = "SessionResult.findSessionResultBySessionIdAndOrgId", 
						query = "SELECT s FROM SessionResult s WHERE s.organizationId = :organizationId AND "
						+"s.sessionId = :sessionId")

		}
)
public class SessionResult implements Serializable 
{
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "ID")
	private Long id;

	@Basic(optional = false)
	@Column(name = "SESSIONID")
	private Long sessionId;

	@Column(name = "PATIENTID")
	private String patientId;

	@Column(name = "ORGANIZATIONID")
	private String organizationId;

	@Column(name = "ISACKNOWLEDGED")
	private Boolean isAcknowledged;

	@Basic(optional = false)
	@Column(name = "CREATETIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimestamp;

	@Basic(optional = false)
	@Column(name = "RESPONSETIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date responseTimestamp;

	@Column(name = "RECIEVEDTIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date recievedTimestamp;

	public SessionResult() {
	}

	public SessionResult(Long id) {
		this.id = id;
	}

	public SessionResult(Long id, Long sessionId, Date createTimestamp, Date responseTimestamp) {
		this.id = id;
		this.sessionId = sessionId;
		this.createTimestamp = createTimestamp;
		this.responseTimestamp = responseTimestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getIsAcknowledged() {
		return isAcknowledged;
	}

	public void setIsAcknowledged(Boolean isAcknowledged) {
		this.isAcknowledged = isAcknowledged;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Date getResponseTimestamp() {
		return responseTimestamp;
	}

	public void setResponseTimestamp(Date responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}

	public Date getRecievedTimestamp() {
		return recievedTimestamp;
	}

	public void setRecievedTimestamp(Date recievedTimestamp) {
		this.recievedTimestamp = recievedTimestamp;
	}

    /**
     * To automatically generate current timestamp on creation of an entry.
     */
    @PrePersist
    protected void onCreate(){
        createTimestamp = new Date();
    }
    
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SessionResult)) {
			return false;
		}
		SessionResult other = (SessionResult) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.bosch.tmp.integration.entities.SessionResult[id=" + id + "]";
	}

}
