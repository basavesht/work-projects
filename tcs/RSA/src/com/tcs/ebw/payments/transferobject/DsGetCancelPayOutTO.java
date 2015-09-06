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
public class DsGetCancelPayOutTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4149750242659542013L;

	//Payment Details..
	private String payccsstatuscode = null;
	private String txn_type = null;
	private String frombr_acct_no_fa = null;
	private String tobr_acct_no_fa = null;
	private Timestamp created_date = null;
	private String paybatchref = null;
	private String keyaccountnumber_from = null;
	private String keyaccountnumber_to = null;
	private Timestamp requested_exe_dt = null;
	private Double par_txn_no = null;
	private String dont_spawn_flag = null;

	//Recurring payment details..
	private String payfrequency = null;
	private String duration = null;
	private String frequency = null;
	private BigDecimal accumulatedAmt = null;
	private Double accumulatedTransfers = null;
	private BigDecimal paydebitamt= null;
	private Double payfreqpaymentcount=null;
	private BigDecimal payfreqlimit=null;
	private Timestamp payfreqenddate=null;
	private Timestamp prefered_previous_txn_dt = null;

	//External account details..
	private String paypayeeid = null;
	private String paypayeename1 = null;
	private String payeeaccnum = null;
	private String extAccOwner = null;

	//Check transaction Details..
	private String checkPayeeToName = null;

	//Version details..
	private Double versionnum = null;
	private Double batversionnum = null;
	private Double partxnversionnum = null;
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
	 * @return the created_date
	 */
	public Timestamp getCreated_date() {
		return created_date;
	}
	/**
	 * @param created_date the created_date to set
	 */
	public void setCreated_date(Timestamp created_date) {
		this.created_date = created_date;
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
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
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
	 * @return the checkPayeeToName
	 */
	public String getCheckPayeeToName() {
		return checkPayeeToName;
	}
	/**
	 * @param checkPayeeToName the checkPayeeToName to set
	 */
	public void setCheckPayeeToName(String checkPayeeToName) {
		this.checkPayeeToName = checkPayeeToName;
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
			+ "payccsstatuscode = " + this.payccsstatuscode + TAB
			+ "txn_type = " + this.txn_type + TAB
			+ "frombr_acct_no_fa = " + this.frombr_acct_no_fa + TAB
			+ "tobr_acct_no_fa = " + this.tobr_acct_no_fa + TAB
			+ "created_date = " + this.created_date + TAB
			+ "paybatchref = " + this.paybatchref + TAB
			+ "keyaccountnumber_from = " + this.keyaccountnumber_from + TAB
			+ "keyaccountnumber_to = " + this.keyaccountnumber_to + TAB
			+ "requested_exe_dt = " + this.requested_exe_dt + TAB
			+ "par_txn_no = " + this.par_txn_no + TAB
			+ "dont_spawn_flag = " + this.dont_spawn_flag + TAB
			+ "payfrequency = " + this.payfrequency + TAB
			+ "duration = " + this.duration + TAB
			+ "frequency = " + this.frequency + TAB
			+ "accumulatedAmt = " + this.accumulatedAmt + TAB
			+ "accumulatedTransfers = " + this.accumulatedTransfers + TAB
			+ "paydebitamt = " + this.paydebitamt + TAB
			+ "payfreqpaymentcount = " + this.payfreqpaymentcount + TAB
			+ "payfreqlimit = " + this.payfreqlimit + TAB
			+ "payfreqenddate = " + this.payfreqenddate + TAB
			+ "prefered_previous_txn_dt = " + this.prefered_previous_txn_dt + TAB
			+ "paypayeeid = " + this.paypayeeid + TAB
			+ "paypayeename1 = " + this.paypayeename1 + TAB
			+ "payeeaccnum = " + this.payeeaccnum + TAB
			+ "extAccOwner = " + this.extAccOwner + TAB
			+ "checkPayeeToName = " + this.checkPayeeToName + TAB
			+ "versionnum = " + this.versionnum + TAB
			+ "batversionnum = " + this.batversionnum + TAB
			+ "partxnversionnum = " + this.partxnversionnum + TAB
			+ " )";

		return retValue;
	}

}