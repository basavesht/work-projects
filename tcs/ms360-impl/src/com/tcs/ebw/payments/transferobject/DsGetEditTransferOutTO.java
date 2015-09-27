/**
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
public class DsGetEditTransferOutTO extends EBWTransferObject implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4543516160112627978L;

	//Payment Details ...
	private Timestamp initiationDate = null;
	private String transferFrequency = null;
	private String frombr_acct_no_fa = null;
	private String tobr_acct_no_fa = null;
	private BigDecimal paydebitamt= null;
	private String paybatchref = null;
	private String keyaccountnumber_from = null;
	private String keyaccountnumber_to = null;
	private Double par_txn_no = null;
	private String transactionType = null;
	private String rsa_Review_Flag = null;
	private String payccsstatuscode=null;
	private String same_name_flag = null;

	//Recurring details ...
	private String payfrequencycombo=null;
	private BigDecimal accumulatedamt = null;
	private Double accumulatedtransfers= null;
	private Double payfreqpaymentcount=null;
	private BigDecimal payfreqlimit=null;
	private String duration = null;
	private Timestamp payfreqenddate=null;

	//External account details..
	private String paypayeename1=null;
	private String paypayeebankcode=null;
	private String paypayee_acct_type=null;
	private String payeeaccnum=null;
	private String paypayeeid=null;
	private String extAccOwner=null;

	//Version details ...
	private Double versionnum=null;
	private Double batversionnum = null;
	private Double partxnversionnum = null;

	//IRA Transfer type description...
	private String retirement_txn_type_desc=null;

	//Loan Account ...
	private String loanAcnt = null;

	/**
	 * @return the initiationDate
	 */
	public Timestamp getInitiationDate() {
		return initiationDate;
	}

	/**
	 * @param initiationDate the initiationDate to set
	 */
	public void setInitiationDate(Timestamp initiationDate) {
		this.initiationDate = initiationDate;
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
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the rsa_Review_Flag
	 */
	public String getRsa_Review_Flag() {
		return rsa_Review_Flag;
	}

	/**
	 * @param rsa_Review_Flag the rsa_Review_Flag to set
	 */
	public void setRsa_Review_Flag(String rsa_Review_Flag) {
		this.rsa_Review_Flag = rsa_Review_Flag;
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
	 * @return the same_name_flag
	 */
	public String getSame_name_flag() {
		return same_name_flag;
	}

	/**
	 * @param same_name_flag the same_name_flag to set
	 */
	public void setSame_name_flag(String same_name_flag) {
		this.same_name_flag = same_name_flag;
	}

	/**
	 * @return the payfrequencycombo
	 */
	public String getPayfrequencycombo() {
		return payfrequencycombo;
	}

	/**
	 * @param payfrequencycombo the payfrequencycombo to set
	 */
	public void setPayfrequencycombo(String payfrequencycombo) {
		this.payfrequencycombo = payfrequencycombo;
	}

	/**
	 * @return the accumulatedamt
	 */
	public BigDecimal getAccumulatedamt() {
		return accumulatedamt;
	}

	/**
	 * @param accumulatedamt the accumulatedamt to set
	 */
	public void setAccumulatedamt(BigDecimal accumulatedamt) {
		this.accumulatedamt = accumulatedamt;
	}

	/**
	 * @return the accumulatedtransfers
	 */
	public Double getAccumulatedtransfers() {
		return accumulatedtransfers;
	}

	/**
	 * @param accumulatedtransfers the accumulatedtransfers to set
	 */
	public void setAccumulatedtransfers(Double accumulatedtransfers) {
		this.accumulatedtransfers = accumulatedtransfers;
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
	 * @return the retirement_txn_type_desc
	 */
	public String getRetirement_txn_type_desc() {
		return retirement_txn_type_desc;
	}

	/**
	 * @param retirement_txn_type_desc the retirement_txn_type_desc to set
	 */
	public void setRetirement_txn_type_desc(String retirement_txn_type_desc) {
		this.retirement_txn_type_desc = retirement_txn_type_desc;
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

		retValue = "DsGetEditTransferOutTO ( "
			+ super.toString() + TAB
			+ "initiationDate = " + this.initiationDate + TAB
			+ "transferFrequency = " + this.transferFrequency + TAB
			+ "frombr_acct_no_fa = " + this.frombr_acct_no_fa + TAB
			+ "tobr_acct_no_fa = " + this.tobr_acct_no_fa + TAB
			+ "paydebitamt = " + this.paydebitamt + TAB
			+ "paybatchref = " + this.paybatchref + TAB
			+ "keyaccountnumber_from = " + this.keyaccountnumber_from + TAB
			+ "keyaccountnumber_to = " + this.keyaccountnumber_to + TAB
			+ "par_txn_no = " + this.par_txn_no + TAB
			+ "transactionType = " + this.transactionType + TAB
			+ "rsa_Review_Flag = " + this.rsa_Review_Flag + TAB
			+ "payccsstatuscode = " + this.payccsstatuscode + TAB
			+ "same_name_flag = " + this.same_name_flag + TAB
			+ "payfrequencycombo = " + this.payfrequencycombo + TAB
			+ "accumulatedamt = " + this.accumulatedamt + TAB
			+ "accumulatedtransfers = " + this.accumulatedtransfers + TAB
			+ "payfreqpaymentcount = " + this.payfreqpaymentcount + TAB
			+ "payfreqlimit = " + this.payfreqlimit + TAB
			+ "duration = " + this.duration + TAB
			+ "payfreqenddate = " + this.payfreqenddate + TAB
			+ "paypayeename1 = " + this.paypayeename1 + TAB
			+ "paypayeebankcode = " + this.paypayeebankcode + TAB
			+ "paypayee_acct_type = " + this.paypayee_acct_type + TAB
			+ "payeeaccnum = " + this.payeeaccnum + TAB
			+ "paypayeeid = " + this.paypayeeid + TAB
			+ "extAccOwner = " + this.extAccOwner + TAB
			+ "versionnum = " + this.versionnum + TAB
			+ "batversionnum = " + this.batversionnum + TAB
			+ "partxnversionnum = " + this.partxnversionnum + TAB
			+ "retirement_txn_type_desc = " + this.retirement_txn_type_desc + TAB
			+ "loanAcnt = " + this.loanAcnt + TAB
			+ " )";

		return retValue;
	}

}