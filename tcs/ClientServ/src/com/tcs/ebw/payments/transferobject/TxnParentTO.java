/*
 * Created on Thu Apr 09 12:08:49 IST 2009
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
public class TxnParentTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5828331746414687695L;
	private Double par_txn_no = null;
	private String from_acct = null;
	private String to_acct = null;
	private Timestamp initiation_dt = null;
	private BigDecimal amount = null;
	private String frequency = null;
	private String duration = null;
	private Timestamp untildate = null;
	private BigDecimal thrshold_amt = null;
	private Double max_txn_no = null;
	private BigDecimal accum_amt = null;
	private Double accum_txn_no = null;
	private String last_txn_id = null;
	private Timestamp last_txn_dt = null;
	private Timestamp prefered_previous_txn_dt = null;
	private Timestamp actual_previous_txn_dt = null;
	private String next_txn_id = null;
	private Timestamp next_txn_dt = null;
	private String status = null;
	private Timestamp create_dt = null;
	private String modifiedby = null;
	private Timestamp modifieddate = null;
	private String par_txn_confno = null;
	private Timestamp par_txn_request_dt = null;

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
	 * @return the from_acct
	 */
	public String getFrom_acct() {
		return from_acct;
	}
	/**
	 * @param from_acct the from_acct to set
	 */
	public void setFrom_acct(String from_acct) {
		this.from_acct = from_acct;
	}
	/**
	 * @return the to_acct
	 */
	public String getTo_acct() {
		return to_acct;
	}
	/**
	 * @param to_acct the to_acct to set
	 */
	public void setTo_acct(String to_acct) {
		this.to_acct = to_acct;
	}
	/**
	 * @return the initiation_dt
	 */
	public Timestamp getInitiation_dt() {
		return initiation_dt;
	}
	/**
	 * @param initiation_dt the initiation_dt to set
	 */
	public void setInitiation_dt(Timestamp initiation_dt) {
		this.initiation_dt = initiation_dt;
	}
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	 * @return the untildate
	 */
	public Timestamp getUntildate() {
		return untildate;
	}
	/**
	 * @param untildate the untildate to set
	 */
	public void setUntildate(Timestamp untildate) {
		this.untildate = untildate;
	}
	/**
	 * @return the thrshold_amt
	 */
	public BigDecimal getThrshold_amt() {
		return thrshold_amt;
	}
	/**
	 * @param thrshold_amt the thrshold_amt to set
	 */
	public void setThrshold_amt(BigDecimal thrshold_amt) {
		this.thrshold_amt = thrshold_amt;
	}
	/**
	 * @return the max_txn_no
	 */
	public Double getMax_txn_no() {
		return max_txn_no;
	}
	/**
	 * @param max_txn_no the max_txn_no to set
	 */
	public void setMax_txn_no(Double max_txn_no) {
		this.max_txn_no = max_txn_no;
	}
	/**
	 * @return the accum_amt
	 */
	public BigDecimal getAccum_amt() {
		return accum_amt;
	}
	/**
	 * @param accum_amt the accum_amt to set
	 */
	public void setAccum_amt(BigDecimal accum_amt) {
		this.accum_amt = accum_amt;
	}
	/**
	 * @return the accum_txn_no
	 */
	public Double getAccum_txn_no() {
		return accum_txn_no;
	}
	/**
	 * @param accum_txn_no the accum_txn_no to set
	 */
	public void setAccum_txn_no(Double accum_txn_no) {
		this.accum_txn_no = accum_txn_no;
	}
	/**
	 * @return the last_txn_id
	 */
	public String getLast_txn_id() {
		return last_txn_id;
	}
	/**
	 * @param last_txn_id the last_txn_id to set
	 */
	public void setLast_txn_id(String last_txn_id) {
		this.last_txn_id = last_txn_id;
	}
	/**
	 * @return the last_txn_dt
	 */
	public Timestamp getLast_txn_dt() {
		return last_txn_dt;
	}
	/**
	 * @param last_txn_dt the last_txn_dt to set
	 */
	public void setLast_txn_dt(Timestamp last_txn_dt) {
		this.last_txn_dt = last_txn_dt;
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
	 * @return the actual_previous_txn_dt
	 */
	public Timestamp getActual_previous_txn_dt() {
		return actual_previous_txn_dt;
	}
	/**
	 * @param actual_previous_txn_dt the actual_previous_txn_dt to set
	 */
	public void setActual_previous_txn_dt(Timestamp actual_previous_txn_dt) {
		this.actual_previous_txn_dt = actual_previous_txn_dt;
	}
	/**
	 * @return the next_txn_id
	 */
	public String getNext_txn_id() {
		return next_txn_id;
	}
	/**
	 * @param next_txn_id the next_txn_id to set
	 */
	public void setNext_txn_id(String next_txn_id) {
		this.next_txn_id = next_txn_id;
	}
	/**
	 * @return the next_txn_dt
	 */
	public Timestamp getNext_txn_dt() {
		return next_txn_dt;
	}
	/**
	 * @param next_txn_dt the next_txn_dt to set
	 */
	public void setNext_txn_dt(Timestamp next_txn_dt) {
		this.next_txn_dt = next_txn_dt;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the create_dt
	 */
	public Timestamp getCreate_dt() {
		return create_dt;
	}
	/**
	 * @param create_dt the create_dt to set
	 */
	public void setCreate_dt(Timestamp create_dt) {
		this.create_dt = create_dt;
	}
	/**
	 * @return the modifiedby
	 */
	public String getModifiedby() {
		return modifiedby;
	}
	/**
	 * @param modifiedby the modifiedby to set
	 */
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	/**
	 * @return the modifieddate
	 */
	public Timestamp getModifieddate() {
		return modifieddate;
	}
	/**
	 * @param modifieddate the modifieddate to set
	 */
	public void setModifieddate(Timestamp modifieddate) {
		this.modifieddate = modifieddate;
	}
	/**
	 * @return the par_txn_confno
	 */
	public String getPar_txn_confno() {
		return par_txn_confno;
	}
	/**
	 * @param par_txn_confno the par_txn_confno to set
	 */
	public void setPar_txn_confno(String par_txn_confno) {
		this.par_txn_confno = par_txn_confno;
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

		retValue = "TxnParentTO ( "
			+ super.toString() + TAB
			+ "par_txn_no = " + this.par_txn_no + TAB
			+ "from_acct = " + this.from_acct + TAB
			+ "to_acct = " + this.to_acct + TAB
			+ "initiation_dt = " + this.initiation_dt + TAB
			+ "amount = " + this.amount + TAB
			+ "frequency = " + this.frequency + TAB
			+ "duration = " + this.duration + TAB
			+ "untildate = " + this.untildate + TAB
			+ "thrshold_amt = " + this.thrshold_amt + TAB
			+ "max_txn_no = " + this.max_txn_no + TAB
			+ "accum_amt = " + this.accum_amt + TAB
			+ "accum_txn_no = " + this.accum_txn_no + TAB
			+ "last_txn_id = " + this.last_txn_id + TAB
			+ "last_txn_dt = " + this.last_txn_dt + TAB
			+ "prefered_previous_txn_dt = " + this.prefered_previous_txn_dt + TAB
			+ "actual_previous_txn_dt = " + this.actual_previous_txn_dt + TAB
			+ "next_txn_id = " + this.next_txn_id + TAB
			+ "next_txn_dt = " + this.next_txn_dt + TAB
			+ "status = " + this.status + TAB
			+ "create_dt = " + this.create_dt + TAB
			+ "modifiedby = " + this.modifiedby + TAB
			+ "modifieddate = " + this.modifieddate + TAB
			+ "par_txn_confno = " + this.par_txn_confno + TAB
			+ "par_txn_request_dt = " + this.par_txn_request_dt + TAB
			+ " )";

		return retValue;
	}

}