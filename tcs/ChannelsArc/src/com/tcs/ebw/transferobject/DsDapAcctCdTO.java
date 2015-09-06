/*
 * Created on Fri Feb 17 16:02:26 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.transferobject;

import java.util.Date;

import com.tcs.ebw.transferobject.EBWTransferObject;

public class DsDapAcctCdTO extends EBWTransferObject implements java.io.Serializable {

	private String udadapid = null;
	private String udagrpid = null;
	private String udafieldname = null;
	private String udafieldvalue = null;
	private String createdby = null;
	private Date createddate = null;
	private String modifiedby = null;
	private Date modifieddate = null;
	private String deleteflag = null;
	private Double versionnum = null;

	/**
	 * @param udadapid the udadapid to set.
	 */
	public void setUdadapid(String udadapid) {
		this.udadapid=udadapid;
	}
	/**
	 * @return Returns the udadapid.
	 */
	public String getUdadapid() {
		return this.udadapid;
	}

	/**
	 * @param udagrpid the udagrpid to set.
	 */
	public void setUdagrpid(String udagrpid) {
		this.udagrpid=udagrpid;
	}
	/**
	 * @return Returns the udagrpid.
	 */
	public String getUdagrpid() {
		return this.udagrpid;
	}

	/**
	 * @param udafieldname the udafieldname to set.
	 */
	public void setUdafieldname(String udafieldname) {
		this.udafieldname=udafieldname;
	}
	/**
	 * @return Returns the udafieldname.
	 */
	public String getUdafieldname() {
		return this.udafieldname;
	}

	/**
	 * @param udafieldvalue the udafieldvalue to set.
	 */
	public void setUdafieldvalue(String udafieldvalue) {
		this.udafieldvalue=udafieldvalue;
	}
	/**
	 * @return Returns the udafieldvalue.
	 */
	public String getUdafieldvalue() {
		return this.udafieldvalue;
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
	 * Returns TransferObject data as a String.
	 *
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for DS_DAP_ACCT_CD  \r\n");
		strB.append ("udadapid = " + udadapid + "\r\n");
		strB.append ("udagrpid = " + udagrpid + "\r\n");
		strB.append ("udafieldname = " + udafieldname + "\r\n");
		strB.append ("udafieldvalue = " + udafieldvalue + "\r\n");
		strB.append ("createdby = " + createdby + "\r\n");
		strB.append ("createddate = " + createddate + "\r\n");
		strB.append ("modifiedby = " + modifiedby + "\r\n");
		strB.append ("modifieddate = " + modifieddate + "\r\n");
		strB.append ("deleteflag = " + deleteflag + "\r\n");
		strB.append ("versionnum = " + versionnum + "\r\n");
		return strB.toString();
	}
}