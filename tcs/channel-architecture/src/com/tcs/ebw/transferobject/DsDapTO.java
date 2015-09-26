/*
 * Created on Fri Feb 17 16:02:24 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.transferobject;

import java.util.Date;

import com.tcs.ebw.transferobject.EBWTransferObject;

public class DsDapTO extends EBWTransferObject implements java.io.Serializable {

	private String udpgrpid = null;
	private String udpdapid = null;
	private String udpdescription = null;
	private String createdby = null;
	private Date createddate = null;
	private String modifiedby = null;
	private Date modifieddate = null;
	private String deleteflag = null;
	private Double versionnum = null;
	private String authorisedby = null;
	private Date authoriseddate = null;
	private String firstauthorisedby = null;
	private Date firstauthoriseddate = null;
	private String cgrgrpid = null;

	/**
	 * @param udpgrpid the udpgrpid to set.
	 */
	public void setUdpgrpid(String udpgrpid) {
		this.udpgrpid=udpgrpid;
	}
	/**
	 * @return Returns the udpgrpid.
	 */
	public String getUdpgrpid() {
		return this.udpgrpid;
	}

	/**
	 * @param udpdapid the udpdapid to set.
	 */
	public void setUdpdapid(String udpdapid) {
		this.udpdapid=udpdapid;
	}
	/**
	 * @return Returns the udpdapid.
	 */
	public String getUdpdapid() {
		return this.udpdapid;
	}

	/**
	 * @param udpdescription the udpdescription to set.
	 */
	public void setUdpdescription(String udpdescription) {
		this.udpdescription=udpdescription;
	}
	/**
	 * @return Returns the udpdescription.
	 */
	public String getUdpdescription() {
		return this.udpdescription;
	}

	/**
	 * @param createdby the createdby to set.
	 */
	public void setCreatedby(String createdby) {
		this.createdby=createdby;
	}
	/**
	 * @return Returns the createdby.
	 */
	public String getCreatedby() {
		return this.createdby;
	}

	/**
	 * @param createddate the createddate to set.
	 */
	public void setCreateddate(Date createddate) {
		this.createddate=createddate;
	}
	/**
	 * @return Returns the createddate.
	 */
	public Date getCreateddate() {
		return this.createddate;
	}

	/**
	 * @param modifiedby the modifiedby to set.
	 */
	public void setModifiedby(String modifiedby) {
		this.modifiedby=modifiedby;
	}
	/**
	 * @return Returns the modifiedby.
	 */
	public String getModifiedby() {
		return this.modifiedby;
	}

	/**
	 * @param modifieddate the modifieddate to set.
	 */
	public void setModifieddate(Date modifieddate) {
		this.modifieddate=modifieddate;
	}
	/**
	 * @return Returns the modifieddate.
	 */
	public Date getModifieddate() {
		return this.modifieddate;
	}

	/**
	 * @param deleteflag the deleteflag to set.
	 */
	public void setDeleteflag(String deleteflag) {
		this.deleteflag=deleteflag;
	}
	/**
	 * @return Returns the deleteflag.
	 */
	public String getDeleteflag() {
		return this.deleteflag;
	}

	/**
	 * @param versionnum the versionnum to set.
	 */
	public void setVersionnum(Double versionnum) {
		this.versionnum=versionnum;
	}
	/**
	 * @return Returns the versionnum.
	 */
	public Double getVersionnum() {
		return this.versionnum;
	}

	/**
	 * @param authorisedby the authorisedby to set.
	 */
	public void setAuthorisedby(String authorisedby) {
		this.authorisedby=authorisedby;
	}
	/**
	 * @return Returns the authorisedby.
	 */
	public String getAuthorisedby() {
		return this.authorisedby;
	}

	/**
	 * @param authoriseddate the authoriseddate to set.
	 */
	public void setAuthoriseddate(Date authoriseddate) {
		this.authoriseddate=authoriseddate;
	}
	/**
	 * @return Returns the authoriseddate.
	 */
	public Date getAuthoriseddate() {
		return this.authoriseddate;
	}

	/**
	 * @param firstauthorisedby the firstauthorisedby to set.
	 */
	public void setFirstauthorisedby(String firstauthorisedby) {
		this.firstauthorisedby=firstauthorisedby;
	}
	/**
	 * @return Returns the firstauthorisedby.
	 */
	public String getFirstauthorisedby() {
		return this.firstauthorisedby;
	}

	/**
	 * @param firstauthoriseddate the firstauthoriseddate to set.
	 */
	public void setFirstauthoriseddate(Date firstauthoriseddate) {
		this.firstauthoriseddate=firstauthoriseddate;
	}
	/**
	 * @return Returns the firstauthoriseddate.
	 */
	public Date getFirstauthoriseddate() {
		return this.firstauthoriseddate;
	}


	/**
	 * Returns TransferObject data as a String.
	 *
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for DS_DAP \r\n");
		strB.append ("udpgrpid = " + udpgrpid + "\r\n");
		strB.append ("udpdapid = " + udpdapid + "\r\n");
		strB.append ("udpdescription = " + udpdescription + "\r\n");
		strB.append ("createdby = " + createdby + "\r\n");
		strB.append ("createddate = " + createddate + "\r\n");
		strB.append ("modifiedby = " + modifiedby + "\r\n");
		strB.append ("modifieddate = " + modifieddate + "\r\n");
		strB.append ("deleteflag = " + deleteflag + "\r\n");
		strB.append ("versionnum = " + versionnum + "\r\n");
		strB.append ("authorisedby = " + authorisedby + "\r\n");
		strB.append ("authoriseddate = " + authoriseddate + "\r\n");
		strB.append ("firstauthorisedby = " + firstauthorisedby + "\r\n");
		strB.append ("firstauthoriseddate = " + firstauthoriseddate + "\r\n");
		return strB.toString();
	}
    public String getCgrgrpid() {
        return cgrgrpid;
    }
    public void setCgrgrpid(String cgrgrpid) {
        this.cgrgrpid = cgrgrpid;
    }
}