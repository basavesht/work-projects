/*
 * Created on Thu Apr 09 12:08:15 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.transferobject;

import java.sql.Timestamp;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class DsBatchTO extends EBWTransferObject implements java.io.Serializable 
{
	private static final long serialVersionUID = 3623069658293733576L;
	private String batbatchref = null;
	private String batgrpid = null;
	private Timestamp batdatetime = null;
	private String batapplicationid = null;
	private String batbatchedbyuser = null;
	private String batmac1 = null;
	private String batauthby1 = null;
	private String batmac2 = null;
	private String batauthby2 = null;
	private String batmac3 = null;
	private String batauthby3 = null;
	private String batmac4 = null;
	private String batauthby4 = null;
	private String batmac5 = null;
	private String batauthby5 = null;
	private Double batnooftxns = null;
	private String batstatus = null;
	private String batcreatedby = null;
	private Timestamp batcreateddate = null;
	private String batmodifiedby = null;
	private Timestamp batmodifieddate = null;
	private String batumi = null;
	private Double batversionnum = null;
	private String batsourceofstatus = null;
	private String batrejectreason = null;
	private String battxnsequence = null;
	private String deleteflag = null;
	private String bfsfilereferenceno = null;
	private Timestamp txn_entry_date = null;
	private String remarks1 = null;
	private String remarks2 = null;
	private String remarks3 = null;
	private String emp_id1 = null;
	private String emp_id2 = null;
	private String emp_id3 = null;
	private Timestamp approved1date_time = null;
	private Timestamp approved2date_time = null;
	private Timestamp approved3date_time = null;

	/**
	 * @param batbatchref the batbatchref to set.
	 */
	public void setBatbatchref(String batbatchref) {
		this.batbatchref=batbatchref;
	}
	/**
	 * @return Returns the batbatchref.
	 */
	public String getBatbatchref() {
		return this.batbatchref;
	}

	/**
	 * @param batgrpid the batgrpid to set.
	 */
	public void setBatgrpid(String batgrpid) {
		this.batgrpid=batgrpid;
	}
	/**
	 * @return Returns the batgrpid.
	 */
	public String getBatgrpid() {
		return this.batgrpid;
	}

	/**
	 * @param batdatetime the batdatetime to set.
	 */
	public void setBatdatetime(Timestamp batdatetime) {
		this.batdatetime=batdatetime;
	}
	/**
	 * @return Returns the batdatetime.
	 */
	public Timestamp getBatdatetime() {
		return this.batdatetime;
	}

	/**
	 * @param batapplicationid the batapplicationid to set.
	 */
	public void setBatapplicationid(String batapplicationid) {
		this.batapplicationid=batapplicationid;
	}
	/**
	 * @return Returns the batapplicationid.
	 */
	public String getBatapplicationid() {
		return this.batapplicationid;
	}

	/**
	 * @param batbatchedbyuser the batbatchedbyuser to set.
	 */
	public void setBatbatchedbyuser(String batbatchedbyuser) {
		this.batbatchedbyuser=batbatchedbyuser;
	}
	/**
	 * @return Returns the batbatchedbyuser.
	 */
	public String getBatbatchedbyuser() {
		return this.batbatchedbyuser;
	}

	/**
	 * @param batmac1 the batmac1 to set.
	 */
	public void setBatmac1(String batmac1) {
		this.batmac1=batmac1;
	}
	/**
	 * @return Returns the batmac1.
	 */
	public String getBatmac1() {
		return this.batmac1;
	}

	/**
	 * @param batauthby1 the batauthby1 to set.
	 */
	public void setBatauthby1(String batauthby1) {
		this.batauthby1=batauthby1;
	}
	/**
	 * @return Returns the batauthby1.
	 */
	public String getBatauthby1() {
		return this.batauthby1;
	}

	/**
	 * @param batmac2 the batmac2 to set.
	 */
	public void setBatmac2(String batmac2) {
		this.batmac2=batmac2;
	}
	/**
	 * @return Returns the batmac2.
	 */
	public String getBatmac2() {
		return this.batmac2;
	}

	/**
	 * @param batauthby2 the batauthby2 to set.
	 */
	public void setBatauthby2(String batauthby2) {
		this.batauthby2=batauthby2;
	}
	/**
	 * @return Returns the batauthby2.
	 */
	public String getBatauthby2() {
		return this.batauthby2;
	}

	/**
	 * @param batmac3 the batmac3 to set.
	 */
	public void setBatmac3(String batmac3) {
		this.batmac3=batmac3;
	}
	/**
	 * @return Returns the batmac3.
	 */
	public String getBatmac3() {
		return this.batmac3;
	}

	/**
	 * @param batauthby3 the batauthby3 to set.
	 */
	public void setBatauthby3(String batauthby3) {
		this.batauthby3=batauthby3;
	}
	/**
	 * @return Returns the batauthby3.
	 */
	public String getBatauthby3() {
		return this.batauthby3;
	}

	/**
	 * @param batmac4 the batmac4 to set.
	 */
	public void setBatmac4(String batmac4) {
		this.batmac4=batmac4;
	}
	/**
	 * @return Returns the batmac4.
	 */
	public String getBatmac4() {
		return this.batmac4;
	}

	/**
	 * @param batauthby4 the batauthby4 to set.
	 */
	public void setBatauthby4(String batauthby4) {
		this.batauthby4=batauthby4;
	}
	/**
	 * @return Returns the batauthby4.
	 */
	public String getBatauthby4() {
		return this.batauthby4;
	}

	/**
	 * @param batmac5 the batmac5 to set.
	 */
	public void setBatmac5(String batmac5) {
		this.batmac5=batmac5;
	}
	/**
	 * @return Returns the batmac5.
	 */
	public String getBatmac5() {
		return this.batmac5;
	}

	/**
	 * @param batauthby5 the batauthby5 to set.
	 */
	public void setBatauthby5(String batauthby5) {
		this.batauthby5=batauthby5;
	}
	/**
	 * @return Returns the batauthby5.
	 */
	public String getBatauthby5() {
		return this.batauthby5;
	}

	/**
	 * @param batnooftxns the batnooftxns to set.
	 */
	public void setBatnooftxns(Double batnooftxns) {
		this.batnooftxns=batnooftxns;
	}
	/**
	 * @return Returns the batnooftxns.
	 */
	public Double getBatnooftxns() {
		return this.batnooftxns;
	}

	/**
	 * @param batstatus the batstatus to set.
	 */
	public void setBatstatus(String batstatus) {
		this.batstatus=batstatus;
	}
	/**
	 * @return Returns the batstatus.
	 */
	public String getBatstatus() {
		return this.batstatus;
	}

	/**
	 * @param batcreatedby the batcreatedby to set.
	 */
	public void setBatcreatedby(String batcreatedby) {
		this.batcreatedby=batcreatedby;
	}
	/**
	 * @return Returns the batcreatedby.
	 */
	public String getBatcreatedby() {
		return this.batcreatedby;
	}

	/**
	 * @param batcreateddate the batcreateddate to set.
	 */
	public void setBatcreateddate(Timestamp batcreateddate) {
		this.batcreateddate=batcreateddate;
	}
	/**
	 * @return Returns the batcreateddate.
	 */
	public Timestamp getBatcreateddate() {
		return this.batcreateddate;
	}

	/**
	 * @param batmodifiedby the batmodifiedby to set.
	 */
	public void setBatmodifiedby(String batmodifiedby) {
		this.batmodifiedby=batmodifiedby;
	}
	/**
	 * @return Returns the batmodifiedby.
	 */
	public String getBatmodifiedby() {
		return this.batmodifiedby;
	}

	/**
	 * @param batmodifieddate the batmodifieddate to set.
	 */
	public void setBatmodifieddate(Timestamp batmodifieddate) {
		this.batmodifieddate=batmodifieddate;
	}
	/**
	 * @return Returns the batmodifieddate.
	 */
	public Timestamp getBatmodifieddate() {
		return this.batmodifieddate;
	}

	/**
	 * @param batumi the batumi to set.
	 */
	public void setBatumi(String batumi) {
		this.batumi=batumi;
	}
	/**
	 * @return Returns the batumi.
	 */
	public String getBatumi() {
		return this.batumi;
	}

	/**
	 * @param batversionnum the batversionnum to set.
	 */
	public void setBatversionnum(Double batversionnum) {
		this.batversionnum=batversionnum;
	}
	/**
	 * @return Returns the batversionnum.
	 */
	public Double getBatversionnum() {
		return this.batversionnum;
	}

	/**
	 * @param batsourceofstatus the batsourceofstatus to set.
	 */
	public void setBatsourceofstatus(String batsourceofstatus) {
		this.batsourceofstatus=batsourceofstatus;
	}
	/**
	 * @return Returns the batsourceofstatus.
	 */
	public String getBatsourceofstatus() {
		return this.batsourceofstatus;
	}

	/**
	 * @param batrejectreason the batrejectreason to set.
	 */
	public void setBatrejectreason(String batrejectreason) {
		this.batrejectreason=batrejectreason;
	}
	/**
	 * @return Returns the batrejectreason.
	 */
	public String getBatrejectreason() {
		return this.batrejectreason;
	}

	/**
	 * @param battxnsequence the battxnsequence to set.
	 */
	public void setBattxnsequence(String battxnsequence) {
		this.battxnsequence=battxnsequence;
	}
	/**
	 * @return Returns the battxnsequence.
	 */
	public String getBattxnsequence() {
		return this.battxnsequence;
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

	/**
	 * @param txn_entry_date the txn_entry_date to set.
	 */
	public void setTxn_entry_date(Timestamp txn_entry_date) {
		this.txn_entry_date=txn_entry_date;
	}
	/**
	 * @return Returns the txn_entry_date.
	 */
	public Timestamp getTxn_entry_date() {
		return this.txn_entry_date;
	}

	/**
	 * @param remarks1 the remarks1 to set.
	 */
	public void setRemarks1(String remarks1) {
		this.remarks1=remarks1;
	}
	/**
	 * @return Returns the remarks1.
	 */
	public String getRemarks1() {
		return this.remarks1;
	}

	/**
	 * @param remarks2 the remarks2 to set.
	 */
	public void setRemarks2(String remarks2) {
		this.remarks2=remarks2;
	}
	/**
	 * @return Returns the remarks2.
	 */
	public String getRemarks2() {
		return this.remarks2;
	}

	/**
	 * @param remarks3 the remarks3 to set.
	 */
	public void setRemarks3(String remarks3) {
		this.remarks3=remarks3;
	}
	/**
	 * @return Returns the remarks3.
	 */
	public String getRemarks3() {
		return this.remarks3;
	}

	/**
	 * @param emp_id1 the emp_id1 to set.
	 */
	public void setEmp_id1(String emp_id1) {
		this.emp_id1=emp_id1;
	}
	/**
	 * @return Returns the emp_id1.
	 */
	public String getEmp_id1() {
		return this.emp_id1;
	}

	/**
	 * @param emp_id2 the emp_id2 to set.
	 */
	public void setEmp_id2(String emp_id2) {
		this.emp_id2=emp_id2;
	}
	/**
	 * @return Returns the emp_id2.
	 */
	public String getEmp_id2() {
		return this.emp_id2;
	}

	/**
	 * @param emp_id3 the emp_id3 to set.
	 */
	public void setEmp_id3(String emp_id3) {
		this.emp_id3=emp_id3;
	}
	/**
	 * @return Returns the emp_id3.
	 */
	public String getEmp_id3() {
		return this.emp_id3;
	}

	/**
	 * @param approved1date_time the approved1date_time to set.
	 */
	public void setApproved1date_time(Timestamp approved1date_time) {
		this.approved1date_time=approved1date_time;
	}
	/**
	 * @return Returns the approved1date_time.
	 */
	public Timestamp getApproved1date_time() {
		return this.approved1date_time;
	}

	/**
	 * @param approved2date_time the approved2date_time to set.
	 */
	public void setApproved2date_time(Timestamp approved2date_time) {
		this.approved2date_time=approved2date_time;
	}
	/**
	 * @return Returns the approved2date_time.
	 */
	public Timestamp getApproved2date_time() {
		return this.approved2date_time;
	}

	/**
	 * @param approved3date_time the approved3date_time to set.
	 */
	public void setApproved3date_time(Timestamp approved3date_time) {
		this.approved3date_time=approved3date_time;
	}
	/**
	 * @return Returns the approved3date_time.
	 */
	public Timestamp getApproved3date_time() {
		return this.approved3date_time;
	}


	/**
	 * Returns TransferObject data as a String.
	 *
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for DS_BATCH \r\n");
		strB.append ("batbatchref = " + batbatchref + "\r\n");
		strB.append ("batgrpid = " + batgrpid + "\r\n");
		strB.append ("batdatetime = " + batdatetime + "\r\n");
		strB.append ("batapplicationid = " + batapplicationid + "\r\n");
		strB.append ("batbatchedbyuser = " + batbatchedbyuser + "\r\n");
		strB.append ("batmac1 = " + batmac1 + "\r\n");
		strB.append ("batauthby1 = " + batauthby1 + "\r\n");
		strB.append ("batmac2 = " + batmac2 + "\r\n");
		strB.append ("batauthby2 = " + batauthby2 + "\r\n");
		strB.append ("batmac3 = " + batmac3 + "\r\n");
		strB.append ("batauthby3 = " + batauthby3 + "\r\n");
		strB.append ("batmac4 = " + batmac4 + "\r\n");
		strB.append ("batauthby4 = " + batauthby4 + "\r\n");
		strB.append ("batmac5 = " + batmac5 + "\r\n");
		strB.append ("batauthby5 = " + batauthby5 + "\r\n");
		strB.append ("batnooftxns = " + batnooftxns + "\r\n");
		strB.append ("batstatus = " + batstatus + "\r\n");
		strB.append ("batcreatedby = " + batcreatedby + "\r\n");
		strB.append ("batcreateddate = " + batcreateddate + "\r\n");
		strB.append ("batmodifiedby = " + batmodifiedby + "\r\n");
		strB.append ("batmodifieddate = " + batmodifieddate + "\r\n");
		strB.append ("batumi = " + batumi + "\r\n");
		strB.append ("batversionnum = " + batversionnum + "\r\n");
		strB.append ("batsourceofstatus = " + batsourceofstatus + "\r\n");
		strB.append ("batrejectreason = " + batrejectreason + "\r\n");
		strB.append ("battxnsequence = " + battxnsequence + "\r\n");
		strB.append ("deleteflag = " + deleteflag + "\r\n");
		strB.append ("bfsfilereferenceno = " + bfsfilereferenceno + "\r\n");
		strB.append ("txn_entry_date = " + txn_entry_date + "\r\n");
		strB.append ("remarks1 = " + remarks1 + "\r\n");
		strB.append ("remarks2 = " + remarks2 + "\r\n");
		strB.append ("remarks3 = " + remarks3 + "\r\n");
		strB.append ("emp_id1 = " + emp_id1 + "\r\n");
		strB.append ("emp_id2 = " + emp_id2 + "\r\n");
		strB.append ("emp_id3 = " + emp_id3 + "\r\n");
		strB.append ("approved1date_time = " + approved1date_time + "\r\n");
		strB.append ("approved2date_time = " + approved2date_time + "\r\n");
		strB.append ("approved3date_time = " + approved3date_time + "\r\n");
		return strB.toString();
	}
}