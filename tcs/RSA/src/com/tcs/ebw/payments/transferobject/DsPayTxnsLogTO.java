/*
 * Created on Thu Apr 09 12:09:05 IST 2009
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
 *    224703       01-07-2011        3               CR 262
 * **********************************************************
 */
public class DsPayTxnsLogTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9075681035056192193L;

	//Transaction Details..
	private Double trx_log_key = null;
	private String paygroupid = null;
	private String paybatchref = null;
	private String paypayref = null;
	private String debit_acct_no = null;
	private String credit_act_no = null;
	private Double fromfa_id = null;
	private Double frombranch_id = null;
	private Double tofa_id = null;
	private Double tobranch_id = null;
	private String trial_depo = null;
	private String txn_status = null;
	private BigDecimal amount = null;
	private Timestamp initiation_date = null;
	private Timestamp request_date = null;
	private BigDecimal payinvoiceamt = null;
	private String dont_spawn_flag =null;
	private String same_name_flag =null;
	private String auth_mode = null;
	private String auth_info_reciever =null;
	private String verbal_auth_client_name =null;
	private Timestamp verbal_auth_date =null;
	private String verbal_auth_time =null;
	private String client_verification_desc =null;
	private String rta_booked_in_flag = null;
	private String keyaccountnumber_from = null;
	private String keyaccountnumber_to = null;
	private String paypayeeid = null;
	private String last_action = null;
	private Timestamp paytxneprydate = null;
	private String owner_role = null;
	private String owner_id = null;
	private String next_approver_role = null;
	private String authinfo_confby_name = null;

	//User Details...
	private String life_user_id = null;
	private Timestamp created_modified_date = null;
	private String created_modified_by_id = null;
	private String created_modified_by_emp_id = null;
	private String created_modified_by_role = null;
	private String created_modified_by_comments = null;
	private String created_modified_by_name = null;

	//Recurring transaction details..
	private String frequency = null;
	private String recurring_freq = null;
	private String repeat_untill = null;
	private BigDecimal untilDollarLimit = null;
	private Timestamp untilFreqEndDate = null;
	private Double untilNoOfTransfers = null;

	//IRA Details..
	private String retirement_mnemonic = null;
	private String qualifier = null;
	private String reverse_qualifier = null;

	//Check Transaction Details..
	private String payeeName = null;	
	private Double thirdPartyReason = null;
	private String certifiedFlag = null;
	private BigDecimal fee = new BigDecimal(0D);
	private String chargedTo = null;
	private String prevChargedTo = null;
	private Double pickUpOption = null;	
	private String defaultAddressFlag = null;
	private String foreignAddressFlag = null;
	private String printingBranch = null;	
	private String deliveryAddrLine1 = null;	
	private String deliveryAddrLine2 = null;	
	private String deliveryAddrLine3 = null;	
	private String deliveryAddrLine4 = null;	
	private String printAddressFlag = null;
	private String memoLine1 = null;	
	private String memoLine2 = null;
	private String printMemoCheckFlag = null;
	private String printMemoStubFlag = null;
	private String deliveredToType = null;
	private String deliveredToName = null;
	private Double typeOfId = null;
	private String idNumber = null;
	private String checkNo = null;
	private String trackingId = null;
	private Timestamp estPickupTime = null;
	private String printerName = null;
	private Timestamp issue_dt = null;
	private String check_status = null;
	private Double void_reason_code = null;
	private String void_reason_desc = null;
	private Double payee_Type = null;
	private String ofac_case_id = null;

	/**
	 * @return the trx_log_key
	 */
	public Double getTrx_log_key() {
		return trx_log_key;
	}
	/**
	 * @param trx_log_key the trx_log_key to set
	 */
	public void setTrx_log_key(Double trx_log_key) {
		this.trx_log_key = trx_log_key;
	}
	/**
	 * @return the paygroupid
	 */
	public String getPaygroupid() {
		return paygroupid;
	}
	/**
	 * @param paygroupid the paygroupid to set
	 */
	public void setPaygroupid(String paygroupid) {
		this.paygroupid = paygroupid;
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
	 * @return the paypayref
	 */
	public String getPaypayref() {
		return paypayref;
	}
	/**
	 * @param paypayref the paypayref to set
	 */
	public void setPaypayref(String paypayref) {
		this.paypayref = paypayref;
	}
	/**
	 * @return the debit_acct_no
	 */
	public String getDebit_acct_no() {
		return debit_acct_no;
	}
	/**
	 * @param debit_acct_no the debit_acct_no to set
	 */
	public void setDebit_acct_no(String debit_acct_no) {
		this.debit_acct_no = debit_acct_no;
	}
	/**
	 * @return the credit_act_no
	 */
	public String getCredit_act_no() {
		return credit_act_no;
	}
	/**
	 * @param credit_act_no the credit_act_no to set
	 */
	public void setCredit_act_no(String credit_act_no) {
		this.credit_act_no = credit_act_no;
	}
	/**
	 * @return the fromfa_id
	 */
	public Double getFromfa_id() {
		return fromfa_id;
	}
	/**
	 * @param fromfa_id the fromfa_id to set
	 */
	public void setFromfa_id(Double fromfa_id) {
		this.fromfa_id = fromfa_id;
	}
	/**
	 * @return the frombranch_id
	 */
	public Double getFrombranch_id() {
		return frombranch_id;
	}
	/**
	 * @param frombranch_id the frombranch_id to set
	 */
	public void setFrombranch_id(Double frombranch_id) {
		this.frombranch_id = frombranch_id;
	}
	/**
	 * @return the tofa_id
	 */
	public Double getTofa_id() {
		return tofa_id;
	}
	/**
	 * @param tofa_id the tofa_id to set
	 */
	public void setTofa_id(Double tofa_id) {
		this.tofa_id = tofa_id;
	}
	/**
	 * @return the tobranch_id
	 */
	public Double getTobranch_id() {
		return tobranch_id;
	}
	/**
	 * @param tobranch_id the tobranch_id to set
	 */
	public void setTobranch_id(Double tobranch_id) {
		this.tobranch_id = tobranch_id;
	}
	/**
	 * @return the trial_depo
	 */
	public String getTrial_depo() {
		return trial_depo;
	}
	/**
	 * @param trial_depo the trial_depo to set
	 */
	public void setTrial_depo(String trial_depo) {
		this.trial_depo = trial_depo;
	}
	/**
	 * @return the txn_status
	 */
	public String getTxn_status() {
		return txn_status;
	}
	/**
	 * @param txn_status the txn_status to set
	 */
	public void setTxn_status(String txn_status) {
		this.txn_status = txn_status;
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
	 * @return the initiation_date
	 */
	public Timestamp getInitiation_date() {
		return initiation_date;
	}
	/**
	 * @param initiation_date the initiation_date to set
	 */
	public void setInitiation_date(Timestamp initiation_date) {
		this.initiation_date = initiation_date;
	}
	/**
	 * @return the request_date
	 */
	public Timestamp getRequest_date() {
		return request_date;
	}
	/**
	 * @param request_date the request_date to set
	 */
	public void setRequest_date(Timestamp request_date) {
		this.request_date = request_date;
	}
	/**
	 * @return the payinvoiceamt
	 */
	public BigDecimal getPayinvoiceamt() {
		return payinvoiceamt;
	}
	/**
	 * @param payinvoiceamt the payinvoiceamt to set
	 */
	public void setPayinvoiceamt(BigDecimal payinvoiceamt) {
		this.payinvoiceamt = payinvoiceamt;
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
	 * @return the auth_mode
	 */
	public String getAuth_mode() {
		return auth_mode;
	}
	/**
	 * @param auth_mode the auth_mode to set
	 */
	public void setAuth_mode(String auth_mode) {
		this.auth_mode = auth_mode;
	}
	/**
	 * @return the auth_info_reciever
	 */
	public String getAuth_info_reciever() {
		return auth_info_reciever;
	}
	/**
	 * @param auth_info_reciever the auth_info_reciever to set
	 */
	public void setAuth_info_reciever(String auth_info_reciever) {
		this.auth_info_reciever = auth_info_reciever;
	}
	/**
	 * @return the verbal_auth_client_name
	 */
	public String getVerbal_auth_client_name() {
		return verbal_auth_client_name;
	}
	/**
	 * @param verbal_auth_client_name the verbal_auth_client_name to set
	 */
	public void setVerbal_auth_client_name(String verbal_auth_client_name) {
		this.verbal_auth_client_name = verbal_auth_client_name;
	}
	/**
	 * @return the verbal_auth_date
	 */
	public Timestamp getVerbal_auth_date() {
		return verbal_auth_date;
	}
	/**
	 * @param verbal_auth_date the verbal_auth_date to set
	 */
	public void setVerbal_auth_date(Timestamp verbal_auth_date) {
		this.verbal_auth_date = verbal_auth_date;
	}
	/**
	 * @return the verbal_auth_time
	 */
	public String getVerbal_auth_time() {
		return verbal_auth_time;
	}
	/**
	 * @param verbal_auth_time the verbal_auth_time to set
	 */
	public void setVerbal_auth_time(String verbal_auth_time) {
		this.verbal_auth_time = verbal_auth_time;
	}
	/**
	 * @return the client_verification_desc
	 */
	public String getClient_verification_desc() {
		return client_verification_desc;
	}
	/**
	 * @param client_verification_desc the client_verification_desc to set
	 */
	public void setClient_verification_desc(String client_verification_desc) {
		this.client_verification_desc = client_verification_desc;
	}
	/**
	 * @return the rta_booked_in_flag
	 */
	public String getRta_booked_in_flag() {
		return rta_booked_in_flag;
	}
	/**
	 * @param rta_booked_in_flag the rta_booked_in_flag to set
	 */
	public void setRta_booked_in_flag(String rta_booked_in_flag) {
		this.rta_booked_in_flag = rta_booked_in_flag;
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
	 * @return the last_action
	 */
	public String getLast_action() {
		return last_action;
	}
	/**
	 * @param last_action the last_action to set
	 */
	public void setLast_action(String last_action) {
		this.last_action = last_action;
	}
	/**
	 * @return the paytxneprydate
	 */
	public Timestamp getPaytxneprydate() {
		return paytxneprydate;
	}
	/**
	 * @param paytxneprydate the paytxneprydate to set
	 */
	public void setPaytxneprydate(Timestamp paytxneprydate) {
		this.paytxneprydate = paytxneprydate;
	}
	/**
	 * @return the owner_role
	 */
	public String getOwner_role() {
		return owner_role;
	}
	/**
	 * @param owner_role the owner_role to set
	 */
	public void setOwner_role(String owner_role) {
		this.owner_role = owner_role;
	}
	/**
	 * @return the owner_id
	 */
	public String getOwner_id() {
		return owner_id;
	}
	/**
	 * @param owner_id the owner_id to set
	 */
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	/**
	 * @return the next_approver_role
	 */
	public String getNext_approver_role() {
		return next_approver_role;
	}
	/**
	 * @param next_approver_role the next_approver_role to set
	 */
	public void setNext_approver_role(String next_approver_role) {
		this.next_approver_role = next_approver_role;
	}
	/**
	 * @return the authinfo_confby_name
	 */
	public String getAuthinfo_confby_name() {
		return authinfo_confby_name;
	}
	/**
	 * @param authinfo_confby_name the authinfo_confby_name to set
	 */
	public void setAuthinfo_confby_name(String authinfo_confby_name) {
		this.authinfo_confby_name = authinfo_confby_name;
	}
	/**
	 * @return the life_user_id
	 */
	public String getLife_user_id() {
		return life_user_id;
	}
	/**
	 * @param life_user_id the life_user_id to set
	 */
	public void setLife_user_id(String life_user_id) {
		this.life_user_id = life_user_id;
	}
	/**
	 * @return the created_modified_date
	 */
	public Timestamp getCreated_modified_date() {
		return created_modified_date;
	}
	/**
	 * @param created_modified_date the created_modified_date to set
	 */
	public void setCreated_modified_date(Timestamp created_modified_date) {
		this.created_modified_date = created_modified_date;
	}
	/**
	 * @return the created_modified_by_id
	 */
	public String getCreated_modified_by_id() {
		return created_modified_by_id;
	}
	/**
	 * @param created_modified_by_id the created_modified_by_id to set
	 */
	public void setCreated_modified_by_id(String created_modified_by_id) {
		this.created_modified_by_id = created_modified_by_id;
	}
	/**
	 * @return the created_modified_by_emp_id
	 */
	public String getCreated_modified_by_emp_id() {
		return created_modified_by_emp_id;
	}
	/**
	 * @param created_modified_by_emp_id the created_modified_by_emp_id to set
	 */
	public void setCreated_modified_by_emp_id(String created_modified_by_emp_id) {
		this.created_modified_by_emp_id = created_modified_by_emp_id;
	}
	/**
	 * @return the created_modified_by_role
	 */
	public String getCreated_modified_by_role() {
		return created_modified_by_role;
	}
	/**
	 * @param created_modified_by_role the created_modified_by_role to set
	 */
	public void setCreated_modified_by_role(String created_modified_by_role) {
		this.created_modified_by_role = created_modified_by_role;
	}
	/**
	 * @return the created_modified_by_comments
	 */
	public String getCreated_modified_by_comments() {
		return created_modified_by_comments;
	}
	/**
	 * @param created_modified_by_comments the created_modified_by_comments to set
	 */
	public void setCreated_modified_by_comments(String created_modified_by_comments) {
		this.created_modified_by_comments = created_modified_by_comments;
	}
	/**
	 * @return the created_modified_by_name
	 */
	public String getCreated_modified_by_name() {
		return created_modified_by_name;
	}
	/**
	 * @param created_modified_by_name the created_modified_by_name to set
	 */
	public void setCreated_modified_by_name(String created_modified_by_name) {
		this.created_modified_by_name = created_modified_by_name;
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
	 * @return the recurring_freq
	 */
	public String getRecurring_freq() {
		return recurring_freq;
	}
	/**
	 * @param recurring_freq the recurring_freq to set
	 */
	public void setRecurring_freq(String recurring_freq) {
		this.recurring_freq = recurring_freq;
	}
	/**
	 * @return the repeat_untill
	 */
	public String getRepeat_untill() {
		return repeat_untill;
	}
	/**
	 * @param repeat_untill the repeat_untill to set
	 */
	public void setRepeat_untill(String repeat_untill) {
		this.repeat_untill = repeat_untill;
	}
	/**
	 * @return the untilDollarLimit
	 */
	public BigDecimal getUntilDollarLimit() {
		return untilDollarLimit;
	}
	/**
	 * @param untilDollarLimit the untilDollarLimit to set
	 */
	public void setUntilDollarLimit(BigDecimal untilDollarLimit) {
		this.untilDollarLimit = untilDollarLimit;
	}
	/**
	 * @return the untilFreqEndDate
	 */
	public Timestamp getUntilFreqEndDate() {
		return untilFreqEndDate;
	}
	/**
	 * @param untilFreqEndDate the untilFreqEndDate to set
	 */
	public void setUntilFreqEndDate(Timestamp untilFreqEndDate) {
		this.untilFreqEndDate = untilFreqEndDate;
	}
	/**
	 * @return the untilNoOfTransfers
	 */
	public Double getUntilNoOfTransfers() {
		return untilNoOfTransfers;
	}
	/**
	 * @param untilNoOfTransfers the untilNoOfTransfers to set
	 */
	public void setUntilNoOfTransfers(Double untilNoOfTransfers) {
		this.untilNoOfTransfers = untilNoOfTransfers;
	}
	/**
	 * @return the retirement_mnemonic
	 */
	public String getRetirement_mnemonic() {
		return retirement_mnemonic;
	}
	/**
	 * @param retirement_mnemonic the retirement_mnemonic to set
	 */
	public void setRetirement_mnemonic(String retirement_mnemonic) {
		this.retirement_mnemonic = retirement_mnemonic;
	}
	/**
	 * @return the qualifier
	 */
	public String getQualifier() {
		return qualifier;
	}
	/**
	 * @param qualifier the qualifier to set
	 */
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	/**
	 * @return the reverse_qualifier
	 */
	public String getReverse_qualifier() {
		return reverse_qualifier;
	}
	/**
	 * @param reverse_qualifier the reverse_qualifier to set
	 */
	public void setReverse_qualifier(String reverse_qualifier) {
		this.reverse_qualifier = reverse_qualifier;
	}
	/**
	 * @return the payeeName
	 */
	public String getPayeeName() {
		return payeeName;
	}
	/**
	 * @param payeeName the payeeName to set
	 */
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	/**
	 * @return the thirdPartyReason
	 */
	public Double getThirdPartyReason() {
		return thirdPartyReason;
	}
	/**
	 * @param thirdPartyReason the thirdPartyReason to set
	 */
	public void setThirdPartyReason(Double thirdPartyReason) {
		this.thirdPartyReason = thirdPartyReason;
	}
	/**
	 * @return the certifiedFlag
	 */
	public String getCertifiedFlag() {
		return certifiedFlag;
	}
	/**
	 * @param certifiedFlag the certifiedFlag to set
	 */
	public void setCertifiedFlag(String certifiedFlag) {
		this.certifiedFlag = certifiedFlag;
	}
	/**
	 * @return the fee
	 */
	public BigDecimal getFee() {
		return fee;
	}
	/**
	 * @param fee the fee to set
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	/**
	 * @return the chargedTo
	 */
	public String getChargedTo() {
		return chargedTo;
	}
	/**
	 * @param chargedTo the chargedTo to set
	 */
	public void setChargedTo(String chargedTo) {
		this.chargedTo = chargedTo;
	}
	/**
	 * @return the prevChargedTo
	 */
	public String getPrevChargedTo() {
		return prevChargedTo;
	}
	/**
	 * @param prevChargedTo the prevChargedTo to set
	 */
	public void setPrevChargedTo(String prevChargedTo) {
		this.prevChargedTo = prevChargedTo;
	}
	/**
	 * @return the pickUpOption
	 */
	public Double getPickUpOption() {
		return pickUpOption;
	}
	/**
	 * @param pickUpOption the pickUpOption to set
	 */
	public void setPickUpOption(Double pickUpOption) {
		this.pickUpOption = pickUpOption;
	}
	/**
	 * @return the defaultAddressFlag
	 */
	public String getDefaultAddressFlag() {
		return defaultAddressFlag;
	}
	/**
	 * @param defaultAddressFlag the defaultAddressFlag to set
	 */
	public void setDefaultAddressFlag(String defaultAddressFlag) {
		this.defaultAddressFlag = defaultAddressFlag;
	}
	/**
	 * @return the foreignAddressFlag
	 */
	public String getForeignAddressFlag() {
		return foreignAddressFlag;
	}
	/**
	 * @param foreignAddressFlag the foreignAddressFlag to set
	 */
	public void setForeignAddressFlag(String foreignAddressFlag) {
		this.foreignAddressFlag = foreignAddressFlag;
	}
	/**
	 * @return the printingBranch
	 */
	public String getPrintingBranch() {
		return printingBranch;
	}
	/**
	 * @param printingBranch the printingBranch to set
	 */
	public void setPrintingBranch(String printingBranch) {
		this.printingBranch = printingBranch;
	}
	/**
	 * @return the deliveryAddrLine1
	 */
	public String getDeliveryAddrLine1() {
		return deliveryAddrLine1;
	}
	/**
	 * @param deliveryAddrLine1 the deliveryAddrLine1 to set
	 */
	public void setDeliveryAddrLine1(String deliveryAddrLine1) {
		this.deliveryAddrLine1 = deliveryAddrLine1;
	}
	/**
	 * @return the deliveryAddrLine2
	 */
	public String getDeliveryAddrLine2() {
		return deliveryAddrLine2;
	}
	/**
	 * @param deliveryAddrLine2 the deliveryAddrLine2 to set
	 */
	public void setDeliveryAddrLine2(String deliveryAddrLine2) {
		this.deliveryAddrLine2 = deliveryAddrLine2;
	}
	/**
	 * @return the deliveryAddrLine3
	 */
	public String getDeliveryAddrLine3() {
		return deliveryAddrLine3;
	}
	/**
	 * @param deliveryAddrLine3 the deliveryAddrLine3 to set
	 */
	public void setDeliveryAddrLine3(String deliveryAddrLine3) {
		this.deliveryAddrLine3 = deliveryAddrLine3;
	}
	/**
	 * @return the deliveryAddrLine4
	 */
	public String getDeliveryAddrLine4() {
		return deliveryAddrLine4;
	}
	/**
	 * @param deliveryAddrLine4 the deliveryAddrLine4 to set
	 */
	public void setDeliveryAddrLine4(String deliveryAddrLine4) {
		this.deliveryAddrLine4 = deliveryAddrLine4;
	}
	/**
	 * @return the printAddressFlag
	 */
	public String getPrintAddressFlag() {
		return printAddressFlag;
	}
	/**
	 * @param printAddressFlag the printAddressFlag to set
	 */
	public void setPrintAddressFlag(String printAddressFlag) {
		this.printAddressFlag = printAddressFlag;
	}
	/**
	 * @return the memoLine1
	 */
	public String getMemoLine1() {
		return memoLine1;
	}
	/**
	 * @param memoLine1 the memoLine1 to set
	 */
	public void setMemoLine1(String memoLine1) {
		this.memoLine1 = memoLine1;
	}
	/**
	 * @return the memoLine2
	 */
	public String getMemoLine2() {
		return memoLine2;
	}
	/**
	 * @param memoLine2 the memoLine2 to set
	 */
	public void setMemoLine2(String memoLine2) {
		this.memoLine2 = memoLine2;
	}
	/**
	 * @return the printMemoCheckFlag
	 */
	public String getPrintMemoCheckFlag() {
		return printMemoCheckFlag;
	}
	/**
	 * @param printMemoCheckFlag the printMemoCheckFlag to set
	 */
	public void setPrintMemoCheckFlag(String printMemoCheckFlag) {
		this.printMemoCheckFlag = printMemoCheckFlag;
	}
	/**
	 * @return the printMemoStubFlag
	 */
	public String getPrintMemoStubFlag() {
		return printMemoStubFlag;
	}
	/**
	 * @param printMemoStubFlag the printMemoStubFlag to set
	 */
	public void setPrintMemoStubFlag(String printMemoStubFlag) {
		this.printMemoStubFlag = printMemoStubFlag;
	}
	/**
	 * @return the deliveredToType
	 */
	public String getDeliveredToType() {
		return deliveredToType;
	}
	/**
	 * @param deliveredToType the deliveredToType to set
	 */
	public void setDeliveredToType(String deliveredToType) {
		this.deliveredToType = deliveredToType;
	}
	/**
	 * @return the deliveredToName
	 */
	public String getDeliveredToName() {
		return deliveredToName;
	}
	/**
	 * @param deliveredToName the deliveredToName to set
	 */
	public void setDeliveredToName(String deliveredToName) {
		this.deliveredToName = deliveredToName;
	}
	/**
	 * @return the typeOfId
	 */
	public Double getTypeOfId() {
		return typeOfId;
	}
	/**
	 * @param typeOfId the typeOfId to set
	 */
	public void setTypeOfId(Double typeOfId) {
		this.typeOfId = typeOfId;
	}
	/**
	 * @return the idNumber
	 */
	public String getIdNumber() {
		return idNumber;
	}
	/**
	 * @param idNumber the idNumber to set
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	/**
	 * @return the checkNo
	 */
	public String getCheckNo() {
		return checkNo;
	}
	/**
	 * @param checkNo the checkNo to set
	 */
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	/**
	 * @return the trackingId
	 */
	public String getTrackingId() {
		return trackingId;
	}
	/**
	 * @param trackingId the trackingId to set
	 */
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	/**
	 * @return the estPickupTime
	 */
	public Timestamp getEstPickupTime() {
		return estPickupTime;
	}
	/**
	 * @param estPickupTime the estPickupTime to set
	 */
	public void setEstPickupTime(Timestamp estPickupTime) {
		this.estPickupTime = estPickupTime;
	}
	/**
	 * @return the printerName
	 */
	public String getPrinterName() {
		return printerName;
	}
	/**
	 * @param printerName the printerName to set
	 */
	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}
	/**
	 * @return the issue_dt
	 */
	public Timestamp getIssue_dt() {
		return issue_dt;
	}
	/**
	 * @param issue_dt the issue_dt to set
	 */
	public void setIssue_dt(Timestamp issue_dt) {
		this.issue_dt = issue_dt;
	}
	/**
	 * @return the check_status
	 */
	public String getCheck_status() {
		return check_status;
	}
	/**
	 * @param check_status the check_status to set
	 */
	public void setCheck_status(String check_status) {
		this.check_status = check_status;
	}
	/**
	 * @return the void_reason_code
	 */
	public Double getVoid_reason_code() {
		return void_reason_code;
	}
	/**
	 * @param void_reason_code the void_reason_code to set
	 */
	public void setVoid_reason_code(Double void_reason_code) {
		this.void_reason_code = void_reason_code;
	}
	/**
	 * @return the void_reason_desc
	 */
	public String getVoid_reason_desc() {
		return void_reason_desc;
	}
	/**
	 * @param void_reason_desc the void_reason_desc to set
	 */
	public void setVoid_reason_desc(String void_reason_desc) {
		this.void_reason_desc = void_reason_desc;
	}
	public Double getPayee_Type() {
		return payee_Type;
	}
	public void setPayee_Type(Double payee_Type) {
		this.payee_Type = payee_Type;
	}
	/**
	 * @return the ofac_case_id
	 */
	public String getOfac_case_id() {
		return ofac_case_id;
	}
	/**
	 * @param ofac_case_id the ofac_case_id to set
	 */
	public void setOfac_case_id(String ofac_case_id) {
		this.ofac_case_id = ofac_case_id;
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

		retValue = "DsPayTxnsLogTO ( "
			+ super.toString() + TAB
			+ "trx_log_key = " + this.trx_log_key + TAB
			+ "paygroupid = " + this.paygroupid + TAB
			+ "paybatchref = " + this.paybatchref + TAB
			+ "paypayref = " + this.paypayref + TAB
			+ "debit_acct_no = " + this.debit_acct_no + TAB
			+ "credit_act_no = " + this.credit_act_no + TAB
			+ "fromfa_id = " + this.fromfa_id + TAB
			+ "frombranch_id = " + this.frombranch_id + TAB
			+ "tofa_id = " + this.tofa_id + TAB
			+ "tobranch_id = " + this.tobranch_id + TAB
			+ "trial_depo = " + this.trial_depo + TAB
			+ "txn_status = " + this.txn_status + TAB
			+ "amount = " + this.amount + TAB
			+ "initiation_date = " + this.initiation_date + TAB
			+ "request_date = " + this.request_date + TAB
			+ "payinvoiceamt = " + this.payinvoiceamt + TAB
			+ "dont_spawn_flag = " + this.dont_spawn_flag + TAB
			+ "same_name_flag = " + this.same_name_flag + TAB
			+ "auth_mode = " + this.auth_mode + TAB
			+ "auth_info_reciever = " + this.auth_info_reciever + TAB
			+ "verbal_auth_client_name = " + this.verbal_auth_client_name + TAB
			+ "verbal_auth_date = " + this.verbal_auth_date + TAB
			+ "verbal_auth_time = " + this.verbal_auth_time + TAB
			+ "client_verification_desc = " + this.client_verification_desc + TAB
			+ "rta_booked_in_flag = " + this.rta_booked_in_flag + TAB
			+ "keyaccountnumber_from = " + this.keyaccountnumber_from + TAB
			+ "keyaccountnumber_to = " + this.keyaccountnumber_to + TAB
			+ "paypayeeid = " + this.paypayeeid + TAB
			+ "last_action = " + this.last_action + TAB
			+ "paytxneprydate = " + this.paytxneprydate + TAB
			+ "owner_role = " + this.owner_role + TAB
			+ "owner_id = " + this.owner_id + TAB
			+ "next_approver_role = " + this.next_approver_role + TAB
			+ "authinfo_confby_name = " + this.authinfo_confby_name + TAB
			+ "life_user_id = " + this.life_user_id + TAB
			+ "created_modified_date = " + this.created_modified_date + TAB
			+ "created_modified_by_id = " + this.created_modified_by_id + TAB
			+ "created_modified_by_emp_id = " + this.created_modified_by_emp_id + TAB
			+ "created_modified_by_role = " + this.created_modified_by_role + TAB
			+ "created_modified_by_comments = " + this.created_modified_by_comments + TAB
			+ "created_modified_by_name = " + this.created_modified_by_name + TAB
			+ "frequency = " + this.frequency + TAB
			+ "recurring_freq = " + this.recurring_freq + TAB
			+ "repeat_untill = " + this.repeat_untill + TAB
			+ "untilDollarLimit = " + this.untilDollarLimit + TAB
			+ "untilFreqEndDate = " + this.untilFreqEndDate + TAB
			+ "untilNoOfTransfers = " + this.untilNoOfTransfers + TAB
			+ "retirement_mnemonic = " + this.retirement_mnemonic + TAB
			+ "qualifier = " + this.qualifier + TAB
			+ "reverse_qualifier = " + this.reverse_qualifier + TAB
			+ "payeeName = " + this.payeeName + TAB
			+ "thirdPartyReason = " + this.thirdPartyReason + TAB
			+ "certifiedFlag = " + this.certifiedFlag + TAB
			+ "fee = " + this.fee + TAB
			+ "chargedTo = " + this.chargedTo + TAB
			+ "prevChargedTo = " + this.prevChargedTo + TAB
			+ "pickUpOption = " + this.pickUpOption + TAB
			+ "defaultAddressFlag = " + this.defaultAddressFlag + TAB
			+ "foreignAddressFlag = " + this.foreignAddressFlag + TAB
			+ "printingBranch = " + this.printingBranch + TAB
			+ "deliveryAddrLine1 = " + this.deliveryAddrLine1 + TAB
			+ "deliveryAddrLine2 = " + this.deliveryAddrLine2 + TAB
			+ "deliveryAddrLine3 = " + this.deliveryAddrLine3 + TAB
			+ "deliveryAddrLine4 = " + this.deliveryAddrLine4 + TAB
			+ "printAddressFlag = " + this.printAddressFlag + TAB
			+ "memoLine1 = " + this.memoLine1 + TAB
			+ "memoLine2 = " + this.memoLine2 + TAB
			+ "printMemoCheckFlag = " + this.printMemoCheckFlag + TAB
			+ "printMemoStubFlag = " + this.printMemoStubFlag + TAB
			+ "deliveredToType = " + this.deliveredToType + TAB
			+ "deliveredToName = " + this.deliveredToName + TAB
			+ "typeOfId = " + this.typeOfId + TAB
			+ "idNumber = " + this.idNumber + TAB
			+ "checkNo = " + this.checkNo + TAB
			+ "trackingId = " + this.trackingId + TAB
			+ "estPickupTime = " + this.estPickupTime + TAB
			+ "printerName = " + this.printerName + TAB
			+ "issue_dt = " + this.issue_dt + TAB
			+ "check_status = " + this.check_status + TAB
			+ "void_reason_code = " + this.void_reason_code + TAB
			+ "void_reason_desc = " + this.void_reason_desc + TAB
			+ "payee_Type = " + this.payee_Type + TAB
			+ "ofac_case_id = " + this.ofac_case_id + TAB
			+ " )";

		return retValue;
	} 

}