/*
 * Created on Sun Apr 26 12:30:28 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.transferobject;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class DsGetCancelPayOutTO extends EBWTransferObject implements java.io.Serializable
{
	private static final long serialVersionUID = -7856244974056705688L;

	//Payment Details...
	private Timestamp requested_exe_dt = null;
	private String transferFrequency = null;
	private BigDecimal paydebitamt= null;
	private String paybatchref = null;
	private String payccsstatuscode = null;
	private String txn_type = null;
	private String dont_spawn_flag = null;

	//Recurring payemnt details..
	private String payfrequency = null;
	private String duration = null;
	private BigDecimal accumulatedAmt = null;
	private Double accumulatedTransfers = null;
	private Double payfreqpaymentcount=null;
	private BigDecimal payfreqlimit=null;
	private Timestamp payfreqenddate=null;
	private Double par_txn_no = null;
	private Timestamp prefered_previous_txn_dt = null;
	private Timestamp par_txn_request_dt = null;

	//From Account Details..
	private String frombr_acct_no_fa = null;
	private String keyaccountnumber_from = null;
	private String frmAccNickName = null;
	private String frmAccFriendlyName = null;

	//To Account Details..
	private String tobr_acct_no_fa = null;
	private String keyaccountnumber_to = null;
	private String toAccNickName = null;
	private String toAccFriendlyName = null;

	//Version details..
	private Double versionnum = null;
	private Double batversionnum = null;
	private Double partxnversionnum = null;

	//External account details..
	private String paypayeename1=null;
	private String paypayeebankcode=null;
	private String paypayee_acct_type=null;
	private String payeeaccnum=null;
	private String paypayeeid=null;
	private String extAccOwner=null;

	//Loan Account ...
	private String loanAcnt = null;

	/**
	 * @return the requested_exe_dt
	 */
	public Timestamp getRequested_exe_dt() {
		return requested_exe_dt;
	}
	/**
	 * @param requested_exe_dt the requested_exe_dt to set
	 */
	public void setRequested_exe_dt(Timestamp requested_exe_dt) {
		this.requested_exe_dt = requested_exe_dt;
	}
	/**
	 * @return the transferFrequency
	 */
	public String getTransferFrequency() {
		return transferFrequency;
	}
	/**
	 * @param transferFrequency the transferFrequency to set
	 */
	public void setTransferFrequency(String transferFrequency) {
		this.transferFrequency = transferFrequency;
	}
	/**
	 * @return the paydebitamt
	 */
	public BigDecimal getPaydebitamt() {
		return paydebitamt;
	}
	/**
	 * @param paydebitamt the paydebitamt to set
	 */
	public void setPaydebitamt(BigDecimal paydebitamt) {
		this.paydebitamt = paydebitamt;
	}
	/**
	 * @return the paybatchref
	 */
	public String getPaybatchref() {
		return paybatchref;
	}
	/**
	 * @param paybatchref the paybatchref to set
	 */
	public void setPaybatchref(String paybatchref) {
		this.paybatchref = paybatchref;
	}
	/**
	 * @return the payccsstatuscode
	 */
	public String getPayccsstatuscode() {
		return payccsstatuscode;
	}
	/**
	 * @param payccsstatuscode the payccsstatuscode to set
	 */
	public void setPayccsstatuscode(String payccsstatuscode) {
		this.payccsstatuscode = payccsstatuscode;
	}
	/**
	 * @return the txn_type
	 */
	public String getTxn_type() {
		return txn_type;
	}
	/**
	 * @param txn_type the txn_type to set
	 */
	public void setTxn_type(String txn_type) {
		this.txn_type = txn_type;
	}
	/**
	 * @return the dont_spawn_flag
	 */
	public String getDont_spawn_flag() {
		return dont_spawn_flag;
	}
	/**
	 * @param dont_spawn_flag the dont_spawn_flag to set
	 */
	public void setDont_spawn_flag(String dont_spawn_flag) {
		this.dont_spawn_flag = dont_spawn_flag;
	}
	/**
	 * @return the payfrequency
	 */
	public String getPayfrequency() {
		return payfrequency;
	}
	/**
	 * @param payfrequency the payfrequency to set
	 */
	public void setPayfrequency(String payfrequency) {
		this.payfrequency = payfrequency;
	}
	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}
	/**
	 * @return the accumulatedAmt
	 */
	public BigDecimal getAccumulatedAmt() {
		return accumulatedAmt;
	}
	/**
	 * @param accumulatedAmt the accumulatedAmt to set
	 */
	public void setAccumulatedAmt(BigDecimal accumulatedAmt) {
		this.accumulatedAmt = accumulatedAmt;
	}
	/**
	 * @return the accumulatedTransfers
	 */
	public Double getAccumulatedTransfers() {
		return accumulatedTransfers;
	}
	/**
	 * @param accumulatedTransfers the accumulatedTransfers to set
	 */
	public void setAccumulatedTransfers(Double accumulatedTransfers) {
		this.accumulatedTransfers = accumulatedTransfers;
	}
	/**
	 * @return the payfreqpaymentcount
	 */
	public Double getPayfreqpaymentcount() {
		return payfreqpaymentcount;
	}
	/**
	 * @param payfreqpaymentcount the payfreqpaymentcount to set
	 */
	public void setPayfreqpaymentcount(Double payfreqpaymentcount) {
		this.payfreqpaymentcount = payfreqpaymentcount;
	}
	/**
	 * @return the payfreqlimit
	 */
	public BigDecimal getPayfreqlimit() {
		return payfreqlimit;
	}
	/**
	 * @param payfreqlimit the payfreqlimit to set
	 */
	public void setPayfreqlimit(BigDecimal payfreqlimit) {
		this.payfreqlimit = payfreqlimit;
	}
	/**
	 * @return the payfreqenddate
	 */
	public Timestamp getPayfreqenddate() {
		return payfreqenddate;
	}
	/**
	 * @param payfreqenddate the payfreqenddate to set
	 */
	public void setPayfreqenddate(Timestamp payfreqenddate) {
		this.payfreqenddate = payfreqenddate;
	}
	/**
	 * @return the par_txn_no
	 */
	public Double getPar_txn_no() {
		return par_txn_no;
	}
	/**
	 * @param par_txn_no the par_txn_no to set
	 */
	public void setPar_txn_no(Double par_txn_no) {
		this.par_txn_no = par_txn_no;
	}
	/**
	 * @return the prefered_previous_txn_dt
	 */
	public Timestamp getPrefered_previous_txn_dt() {
		return prefered_previous_txn_dt;
	}
	/**
	 * @param prefered_previous_txn_dt the prefered_previous_txn_dt to set
	 */
	public void setPrefered_previous_txn_dt(Timestamp prefered_previous_txn_dt) {
		this.prefered_previous_txn_dt = prefered_previous_txn_dt;
	}
	/**
	 * @return the par_txn_request_dt
	 */
	public Timestamp getPar_txn_request_dt() {
		return par_txn_request_dt;
	}
	/**
	 * @param par_txn_request_dt the par_txn_request_dt to set
	 */
	public void setPar_txn_request_dt(Timestamp par_txn_request_dt) {
		this.par_txn_request_dt = par_txn_request_dt;
	}
	/**
	 * @return the frombr_acct_no_fa
	 */
	public String getFrombr_acct_no_fa() {
		return frombr_acct_no_fa;
	}
	/**
	 * @param frombr_acct_no_fa the frombr_acct_no_fa to set
	 */
	public void setFrombr_acct_no_fa(String frombr_acct_no_fa) {
		this.frombr_acct_no_fa = frombr_acct_no_fa;
	}
	/**
	 * @return the keyaccountnumber_from
	 */
	public String getKeyaccountnumber_from() {
		return keyaccountnumber_from;
	}
	/**
	 * @param keyaccountnumber_from the keyaccountnumber_from to set
	 */
	public void setKeyaccountnumber_from(String keyaccountnumber_from) {
		this.keyaccountnumber_from = keyaccountnumber_from;
	}
	/**
	 * @return the frmAccNickName
	 */
	public String getFrmAccNickName() {
		return frmAccNickName;
	}
	/**
	 * @param frmAccNickName the frmAccNickName to set
	 */
	public void setFrmAccNickName(String frmAccNickName) {
		this.frmAccNickName = frmAccNickName;
	}
	/**
	 * @return the frmAccFriendlyName
	 */
	public String getFrmAccFriendlyName() {
		return frmAccFriendlyName;
	}
	/**
	 * @param frmAccFriendlyName the frmAccFriendlyName to set
	 */
	public void setFrmAccFriendlyName(String frmAccFriendlyName) {
		this.frmAccFriendlyName = frmAccFriendlyName;
	}
	/**
	 * @return the tobr_acct_no_fa
	 */
	public String getTobr_acct_no_fa() {
		return tobr_acct_no_fa;
	}
	/**
	 * @param tobr_acct_no_fa the tobr_acct_no_fa to set
	 */
	public void setTobr_acct_no_fa(String tobr_acct_no_fa) {
		this.tobr_acct_no_fa = tobr_acct_no_fa;
	}
	/**
	 * @return the keyaccountnumber_to
	 */
	public String getKeyaccountnumber_to() {
		return keyaccountnumber_to;
	}
	/**
	 * @param keyaccountnumber_to the keyaccountnumber_to to set
	 */
	public void setKeyaccountnumber_to(String keyaccountnumber_to) {
		this.keyaccountnumber_to = keyaccountnumber_to;
	}
	/**
	 * @return the toAccNickName
	 */
	public String getToAccNickName() {
		return toAccNickName;
	}
	/**
	 * @param toAccNickName the toAccNickName to set
	 */
	public void setToAccNickName(String toAccNickName) {
		this.toAccNickName = toAccNickName;
	}
	/**
	 * @return the toAccFriendlyName
	 */
	public String getToAccFriendlyName() {
		return toAccFriendlyName;
	}
	/**
	 * @param toAccFriendlyName the toAccFriendlyName to set
	 */
	public void setToAccFriendlyName(String toAccFriendlyName) {
		this.toAccFriendlyName = toAccFriendlyName;
	}
	/**
	 * @return the versionnum
	 */
	public Double getVersionnum() {
		return versionnum;
	}
	/**
	 * @param versionnum the versionnum to set
	 */
	public void setVersionnum(Double versionnum) {
		this.versionnum = versionnum;
	}
	/**
	 * @return the batversionnum
	 */
	public Double getBatversionnum() {
		return batversionnum;
	}
	/**
	 * @param batversionnum the batversionnum to set
	 */
	public void setBatversionnum(Double batversionnum) {
		this.batversionnum = batversionnum;
	}
	/**
	 * @return the partxnversionnum
	 */
	public Double getPartxnversionnum() {
		return partxnversionnum;
	}
	/**
	 * @param partxnversionnum the partxnversionnum to set
	 */
	public void setPartxnversionnum(Double partxnversionnum) {
		this.partxnversionnum = partxnversionnum;
	}
	/**
	 * @return the paypayeename1
	 */
	public String getPaypayeename1() {
		return paypayeename1;
	}
	/**
	 * @param paypayeename1 the paypayeename1 to set
	 */
	public void setPaypayeename1(String paypayeename1) {
		this.paypayeename1 = paypayeename1;
	}
	/**
	 * @return the paypayeebankcode
	 */
	public String getPaypayeebankcode() {
		return paypayeebankcode;
	}
	/**
	 * @param paypayeebankcode the paypayeebankcode to set
	 */
	public void setPaypayeebankcode(String paypayeebankcode) {
		this.paypayeebankcode = paypayeebankcode;
	}
	/**
	 * @return the paypayee_acct_type
	 */
	public String getPaypayee_acct_type() {
		return paypayee_acct_type;
	}
	/**
	 * @param paypayee_acct_type the paypayee_acct_type to set
	 */
	public void setPaypayee_acct_type(String paypayee_acct_type) {
		this.paypayee_acct_type = paypayee_acct_type;
	}
	/**
	 * @return the payeeaccnum
	 */
	public String getPayeeaccnum() {
		return payeeaccnum;
	}
	/**
	 * @param payeeaccnum the payeeaccnum to set
	 */
	public void setPayeeaccnum(String payeeaccnum) {
		this.payeeaccnum = payeeaccnum;
	}
	/**
	 * @return the paypayeeid
	 */
	public String getPaypayeeid() {
		return paypayeeid;
	}
	/**
	 * @param paypayeeid the paypayeeid to set
	 */
	public void setPaypayeeid(String paypayeeid) {
		this.paypayeeid = paypayeeid;
	}
	/**
	 * @return the extAccOwner
	 */
	public String getExtAccOwner() {
		return extAccOwner;
	}
	/**
	 * @param extAccOwner the extAccOwner to set
	 */
	public void setExtAccOwner(String extAccOwner) {
		this.extAccOwner = extAccOwner;
	}
	/**
	 * @return the loanAcnt
	 */
	public String getLoanAcnt() {
		return loanAcnt;
	}
	/**
	 * @param loanAcnt the loanAcnt to set
	 */
	public void setLoanAcnt(String loanAcnt) {
		this.loanAcnt = loanAcnt;
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

		retValue = "DsGetCancelPayOutTO ( "
			+ super.toString() + TAB
			+ "requested_exe_dt = " + this.requested_exe_dt + TAB
			+ "transferFrequency = " + this.transferFrequency + TAB
			+ "paydebitamt = " + this.paydebitamt + TAB
			+ "paybatchref = " + this.paybatchref + TAB
			+ "payccsstatuscode = " + this.payccsstatuscode + TAB
			+ "txn_type = " + this.txn_type + TAB
			+ "dont_spawn_flag = " + this.dont_spawn_flag + TAB
			+ "payfrequency = " + this.payfrequency + TAB
			+ "duration = " + this.duration + TAB
			+ "accumulatedAmt = " + this.accumulatedAmt + TAB
			+ "accumulatedTransfers = " + this.accumulatedTransfers + TAB
			+ "payfreqpaymentcount = " + this.payfreqpaymentcount + TAB
			+ "payfreqlimit = " + this.payfreqlimit + TAB
			+ "payfreqenddate = " + this.payfreqenddate + TAB
			+ "par_txn_no = " + this.par_txn_no + TAB
			+ "prefered_previous_txn_dt = " + this.prefered_previous_txn_dt + TAB
			+ "par_txn_request_dt = " + this.par_txn_request_dt + TAB
			+ "frombr_acct_no_fa = " + this.frombr_acct_no_fa + TAB
			+ "keyaccountnumber_from = " + this.keyaccountnumber_from + TAB
			+ "frmAccNickName = " + this.frmAccNickName + TAB
			+ "frmAccFriendlyName = " + this.frmAccFriendlyName + TAB
			+ "tobr_acct_no_fa = " + this.tobr_acct_no_fa + TAB
			+ "keyaccountnumber_to = " + this.keyaccountnumber_to + TAB
			+ "toAccNickName = " + this.toAccNickName + TAB
			+ "toAccFriendlyName = " + this.toAccFriendlyName + TAB
			+ "versionnum = " + this.versionnum + TAB
			+ "batversionnum = " + this.batversionnum + TAB
			+ "partxnversionnum = " + this.partxnversionnum + TAB
			+ "paypayeename1 = " + this.paypayeename1 + TAB
			+ "paypayeebankcode = " + this.paypayeebankcode + TAB
			+ "paypayee_acct_type = " + this.paypayee_acct_type + TAB
			+ "payeeaccnum = " + this.payeeaccnum + TAB
			+ "paypayeeid = " + this.paypayeeid + TAB
			+ "extAccOwner = " + this.extAccOwner + TAB
			+ "loanAcnt = " + this.loanAcnt + TAB
			+ " )";

		return retValue;
	}

}