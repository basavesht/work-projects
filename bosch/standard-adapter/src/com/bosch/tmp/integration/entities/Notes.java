
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
 * Notes entity stores the basic note related information pushed from Task service through CIA.
 * Note contains generally only identifier like information for each note.
 *  - Note Id
 *  - Note Type
 *  - Organization Id
 *  - Patient Id
 *  - Session Id (Only in case of Survey Notes)
 * @author BEA2KOR
 *
 */
@Entity
@Table(name = "NOTES")
@NamedQueries
(
		{

            @NamedQuery(name = "Notes.findAll",
            			query = "SELECT n FROM Notes n"),

            @NamedQuery(name = "Notes.findById",
            			query = "SELECT n FROM Notes n WHERE n.id = :id"),

            @NamedQuery(name = "Notes.findNotesByNoteId",
            			query = "SELECT n FROM Notes n WHERE n.notesId = :notesId"),

            @NamedQuery(name = "Notes.findNotesByNoteIdAndOrgId",
            			query = "SELECT n FROM Notes n WHERE n.notesId = :notesId AND "
            					+"n.organizationId = :organizationId"),

            @NamedQuery(name = "Notes.findNotesByOrganizationId",
            			query = "SELECT n FROM Notes n WHERE n.organizationId = :organizationId AND "
            					+"n.isAcknowledged = :isAcknowledged"),

            @NamedQuery(name = "Notes.findNotesByNoteIdAndAckFlag",
            			query = "SELECT n FROM Notes n WHERE n.notesId = :notesId AND "
        						+"n.isAcknowledged = :isAcknowledged ")

		}
)
public class Notes implements Serializable 
{
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "ID")
	private Long id;

	@Basic(optional = false)
	@Column(name = "NOTESID")
	private Long notesId;

	@Column(name = "NOTESTYPE")
	private String notesType;

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

	@Column(name = "MODIFIEDTIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedTimestamp;

	public Notes() {
	}

	public Notes(Long id) {
		this.id = id;
	}

	public Notes(Long id, Long notesId, Long sessionId, Date createTimestamp, Date modifiedTimestamp) {
		this.id = id;
		this.notesId = notesId;
		this.sessionId = sessionId;
		this.createTimestamp = createTimestamp;
		this.modifiedTimestamp = modifiedTimestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNotesId() {
		return notesId;
	}

	public void setNotesId(Long notesId) {
		this.notesId = notesId;
	}

	public String getNotesType() {
		return notesType;
	}

	public void setNotesType(String notesType) {
		this.notesType = notesType;
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

	public Date getModifiedTimestamp() {
		return modifiedTimestamp;
	}

	public void setModifiedTimestamp(Date modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
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
		if (!(object instanceof Notes)) {
			return false;
		}
		Notes other = (Notes) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.bosch.tmp.integration.entities.Notes[id=" + id + "]";
	}

}
