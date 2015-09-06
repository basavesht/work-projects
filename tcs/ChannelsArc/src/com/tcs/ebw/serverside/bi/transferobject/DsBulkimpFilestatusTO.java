/*
 * Created on Wed Jul 12 15:41:43 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.serverside.bi.transferobject;

import java.util.Date;

import com.tcs.ebw.transferobject.EBWTransferObject;

public class DsBulkimpFilestatusTO extends EBWTransferObject implements java.io.Serializable {

	private String bfsfilereferenceno = null;
	private String bfsgroupid = null;
	private String bfsfiletype = null;
	private String bfsdescription = null;
	private String createdby = null;
	private Date createddate = null;
	private String modifiedby = null;
	private Date modifieddate = null;
	private Double versionnum = null;
	private String bfsorgfilename = null;
	private String bfsfilehash = null;
	private String status = null;
	
	
	/**
	 * @param bfsfilereferenceno the bfsfilereferenceno to set.
	 */
	public void setBfsfilereferenceno(String bfsfilereferenceno) {
		this.bfsfilereferenceno=bfsfilereferenceno;
	}
	/**
	 * @return Returns the bfsfilereferenceno.
	 */
	public String getBfsfilereferenceno() {
		return this.bfsfilereferenceno;
	}
	public void setStatus(String status) {
		this.status=status;
	}
	/**
	 * @return Returns the bfsfilereferenceno.
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * @param bfsgroupid the bfsgroupid to set.
	 */
	public void setBfsgroupid(String bfsgroupid) {
		this.bfsgroupid=bfsgroupid;
	}
	/**
	 * @return Returns the bfsgroupid.
	 */
	public String getBfsgroupid() {
		return this.bfsgroupid;
	}

	/**
	 * @param bfsfiletype the bfsfiletype to set.
	 */
	public void setBfsfiletype(String bfsfiletype) {
		this.bfsfiletype=bfsfiletype;
	}
	/**
	 * @return Returns the bfsfiletype.
	 */
	public String getBfsfiletype() {
		return this.bfsfiletype;
	}

	/**
	 * @param bfsdescription the bfsdescription to set.
	 */
	public void setBfsdescription(String bfsdescription) {
		this.bfsdescription=bfsdescription;
	}
	/**
	 * @return Returns the bfsdescription.
	 */
	public String getBfsdescription() {
		return this.bfsdescription;
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
	 * @param bfsorgfilename the bfsorgfilename to set.
	 */
	public void setBfsorgfilename(String bfsorgfilename) {
		this.bfsorgfilename=bfsorgfilename;
	}
	/**
	 * @return Returns the bfsorgfilename.
	 */
	public String getBfsorgfilename() {
		return this.bfsorgfilename;
	}

	/**
	 * @param bfsfilehash the bfsfilehash to set.
	 */
	public void setBfsfilehash(String bfsfilehash) {
		this.bfsfilehash=bfsfilehash;
	}
	/**
	 * @return Returns the bfsfilehash.
	 */
	public String getBfsfilehash() {
		return this.bfsfilehash;
	}


	/**
	 * Returns TransferObject data as a String.
	 *
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for DS_BULKIMP_FILESTATUS \r\n");
		strB.append ("bfsfilereferenceno = " + bfsfilereferenceno + "\r\n");
		strB.append ("bfsgroupid = " + bfsgroupid + "\r\n");
		strB.append ("bfsfiletype = " + bfsfiletype + "\r\n");
		strB.append ("bfsdescription = " + bfsdescription + "\r\n");
		strB.append ("createdby = " + createdby + "\r\n");
		strB.append ("createddate = " + createddate + "\r\n");
		strB.append ("modifiedby = " + modifiedby + "\r\n");
		strB.append ("modifieddate = " + modifieddate + "\r\n");
		strB.append ("versionnum = " + versionnum + "\r\n");
		strB.append ("bfsorgfilename = " + bfsorgfilename + "\r\n");
		strB.append ("bfsfilehash = " + bfsfilehash + "\r\n");
		strB.append ("status = " + status + "\r\n");
		return strB.toString();
	}
}