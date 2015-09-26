/*
 * Created on Thu Apr 09 12:08:29 IST 2009
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
 *    224703       29-08-2011        3-B             CR 193
 * **********************************************************
 */
public class DsPayTxnsTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1641760460675984153L;
	private Double paytrxkey = null;
	private String paygroupid = null;
	private String paybatchref = null;
	private String paypayref = null;
	private String payinitiationchannelkey = null;
	private String paytypecode = null;
	private String payprocmode = null;
	private String payservicetypecode = null;
	private String paycustref = null;
	private String paychannelpayref = null;
	private String payalliancebankid = null;
	private String paydebitaccnum = null;
	private String paydebitaccctrycode = null;
	private String paydebitacccitycode = null;
	private String paycurrcode = null;
	private BigDecimal payinvoiceamt = null;
	private BigDecimal paydebitamt = null;
	private BigDecimal payvatamt = null;
	private BigDecimal paydiscountamt = null;
	private Timestamp payissueddate = null;
	private Timestamp paypymtdate = null;
	private String paypaydetails1llmcode = null;
	private String paypaydetails1 = null;
	private String paypaydetails2llmcode = null;
	private String paypaydetails2 = null;
	private String paypaydetails1bo = null;
	private String paypaydetails2bo = null;
	private String paypayeeaccnum = null;
	private String paypayeename1llmcode = null;
	private String paypayeename1 = null;
	private String paypayeename2llmcode = null;
	private String paypayeename2 = null;
	private String paypayeeadd1llmcode = null;
	private String paypayeeadd1 = null;
	private String paypayeeadd2llmcode = null;
	private String paypayeeadd2 = null;
	private String paypayeeadd3llmcode = null;
	private String paypayeeadd3 = null;
	private String paypayeeadd4llmcode = null;
	private String paypayeeadd4 = null;
	private String paypayeefaxno = null;
	private String paypayeebankcode = null;
	private String paypayeebanklocalcode = null;
	private String paypayeebranchcode = null;
	private String paypayeebranchscode = null;
	private String paypayeenamebo = null;
	private String paypayeeaddbo1 = null;
	private String paypayeeaddbo2 = null;
	private String paypayeeaddbo3 = null;
	private String payfxcalcmethodcode = null;
	private String payfxtypecode = null;
	private BigDecimal payfxrate = null;
	private BigDecimal payfxcustomerfxrate = null;
	private String payfxinvoiceformat = null;
	private String paywhtformid = null;
	private String paywhttaxid = null;
	private String paywhtrefnum = null;
	private String paywhttype1 = null;
	private String paywhtdesc1 = null;
	private BigDecimal paywhtamount1 = null;
	private BigDecimal paywhtgrossamt1 = null;
	private String paywhttype2 = null;
	private String paywhtdesc2 = null;
	private BigDecimal paywhtamt2 = null;
	private BigDecimal paywhtgrossamt2 = null;
	private String paylocalchargeto = null;
	private String payoverseaschargeto = null;
	private String payinterbankcode = null;
	private String payclearingcodett = null;
	private String payclearingzonecode = null;
	private String paydraweebankcode = null;
	private String paydeliverymethodcode = null;
	private String paydeliveryto = null;
	private String paypickuploccode = null;
	private String paystsallianceflag = null;
	private String payabpaysendflag = null;
	private String payimportref = null;
	private String paycustmemo = null;
	private String payccsstatuscode = null;
	private String paycustname = null;
	private String paycustaddr1 = null;
	private String paycustaddr2 = null;
	private String paycustaddr3 = null;
	private String paypymthash = null;
	private String paywhtprintlocation = null;
	private Double paysbatchno = null;
	private String paybatchflag = null;
	private BigDecimal paydebitamtbce = null;
	private String paypayeeid = null;
	private String payvattype = null;
	private String paydisctype = null;
	private Double paywhtseqno = null;
	private Timestamp createddate = null;
	private String modifiedby = null;
	private Timestamp modifieddate = null;
	private String deleteflag = null;
	private Double versionnum = null;
	private String payumi = null;
	private String payacctype = null;
	private String payfirsttransfercode = null;
	private String payresidencycode = null;
	private String paycharfiller1 = null;
	private String paycharfiller2 = null;
	private String paycharfiller3 = null;
	private String paycharfiller4 = null;
	private String paycharfiller5 = null;
	private String paycharfiller6 = null;
	private String paycharfiller7 = null;
	private String paycharfiller8 = null;
	private String payunicodefiller1 = null;
	private String payunicodefiller2 = null;
	private BigDecimal payamtfiller1 = null;
	private BigDecimal payamtfiller2 = null;
	private BigDecimal payamtfiller3 = null;
	private BigDecimal payamtfiller4 = null;
	private BigDecimal payamtfiller5 = null;
	private BigDecimal payratefiller1 = null;
	private BigDecimal payratefiller2 = null;
	private BigDecimal payratefiller3 = null;
	private Double paypriority = null;
	private String paycustomercode = null;
	private String payconsolidate = null;
	private String payurgent = null;
	private String payrecurring = null;
	private String payfrequency = null;
	private Double payfreqpaymentcount = null;
	private Timestamp payfreqenddate = null;
	private String bfsfilereferenceno = null;
	private BigDecimal payfreqlimit = null;
	private Double par_txn_no = null;
	private Timestamp requested_exe_dt = null;
	private Timestamp actual_exe_dt = null;
	private String trial_depo = null;
	private String sent_to_paymnt = null;
	private Double fromfa_id = null;
	private Double fromfa_id_hist = null;
	private Double tofa_id = null;
	private Double tofa_id_hist = null;
	private Double frombranch_id = null;
	private Double frombranch_id_hist = null;
	private Double tobranch_id = null;
	private Double tobranch_id_hist = null;
	private String fromacct_tier = null;
	private String toacct_tier = null;
	private Double acct_type1 = null;
	private String acct_from_type = null;
	private String acct_to_type = null;
	private String pmnt_confirmationno = null;
	private String acct_user_id = null;
	private String acct_user_name = null;
	private String fromacct_no_hist = null;
	private String toacct_no_hist = null;
	private Double acct_type1_hist = null;
	private String txn_type = null;
	private String locked_by_id = null;
	private String fa_acct_no_br = null;
	private String keyaccountnumber_from = null;
	private String keyaccountnumber_to = null;
	private String friendlyname_from = null;
	private String friendlyname_to = null;
	private String nickname_from = null;
	private String nickname_to = null;
	private String coe_user_name = null;
	private String paypayee_acct_type = null;
	private String frombr_acct_no_fa = null;
	private String tobr_acct_no_fa = null;
	private String fromacct_type1 = null;
	private String toacct_type1 = null;
	private String FLG_ATTR1 = null; // Added in case of the Approving transaction for RTA and Remittance Check....
	private Timestamp paytxneprydate = null; 
	private String rsa_review_flag = null;
	private String next_approver_role = null;
	private String created_by_role = null; 
	private String createdby_name = null; 
	private String modifiedby_name = null;
	private String createdby = null; 
	private String life_user_id = null;
	private String initiator_role = null;
	private String initiator_id =  null; 
	private String current_owner_role = null; 
	private String current_owner_id =null; 
	private String current_owner_name =null; 
	private String dont_spawn_flag =null;
	private String same_name_flag =null;
	private String key_client =null;
	private String key_link =null;
	private String key_clientId =null;
	private String auth_mode = null;
	private String auth_info_reciever =null;
	private String verbal_auth_client_name =null;
	private Timestamp verbal_auth_date =null;
	private String verbal_auth_time =null;
	private String client_verification_desc =null;
	private String auth_confirmed_by =null;
	private String rta_booked_in_flag = null;
	private String retirement_mnemonic = null;
	private String qualifier = null;
	private String reverse_qualifier = null;
	private String ofac_case_id = null;
	private String display_qualifier = null;

	/**
	 * @return the display_qualifier
	 */
	public String getDisplay_qualifier() {
		return display_qualifier;
	}
	/**
	 * @param display_qualifier the display_qualifier to set
	 */
	public void setDisplay_qualifier(String display_qualifier) {
		this.display_qualifier = display_qualifier;
	}
	/**
	 * @return the paytrxkey
	 */
	public Double getPaytrxkey() {
		return paytrxkey;
	}
	/**
	 * @param paytrxkey the paytrxkey to set
	 */
	public void setPaytrxkey(Double paytrxkey) {
		this.paytrxkey = paytrxkey;
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
	 * @return the payinitiationchannelkey
	 */
	public String getPayinitiationchannelkey() {
		return payinitiationchannelkey;
	}
	/**
	 * @param payinitiationchannelkey the payinitiationchannelkey to set
	 */
	public void setPayinitiationchannelkey(String payinitiationchannelkey) {
		this.payinitiationchannelkey = payinitiationchannelkey;
	}
	/**
	 * @return the paytypecode
	 */
	public String getPaytypecode() {
		return paytypecode;
	}
	/**
	 * @param paytypecode the paytypecode to set
	 */
	public void setPaytypecode(String paytypecode) {
		this.paytypecode = paytypecode;
	}
	/**
	 * @return the payprocmode
	 */
	public String getPayprocmode() {
		return payprocmode;
	}
	/**
	 * @param payprocmode the payprocmode to set
	 */
	public void setPayprocmode(String payprocmode) {
		this.payprocmode = payprocmode;
	}
	/**
	 * @return the payservicetypecode
	 */
	public String getPayservicetypecode() {
		return payservicetypecode;
	}
	/**
	 * @param payservicetypecode the payservicetypecode to set
	 */
	public void setPayservicetypecode(String payservicetypecode) {
		this.payservicetypecode = payservicetypecode;
	}
	/**
	 * @return the paycustref
	 */
	public String getPaycustref() {
		return paycustref;
	}
	/**
	 * @param paycustref the paycustref to set
	 */
	public void setPaycustref(String paycustref) {
		this.paycustref = paycustref;
	}
	/**
	 * @return the paychannelpayref
	 */
	public String getPaychannelpayref() {
		return paychannelpayref;
	}
	/**
	 * @param paychannelpayref the paychannelpayref to set
	 */
	public void setPaychannelpayref(String paychannelpayref) {
		this.paychannelpayref = paychannelpayref;
	}
	/**
	 * @return the payalliancebankid
	 */
	public String getPayalliancebankid() {
		return payalliancebankid;
	}
	/**
	 * @param payalliancebankid the payalliancebankid to set
	 */
	public void setPayalliancebankid(String payalliancebankid) {
		this.payalliancebankid = payalliancebankid;
	}
	/**
	 * @return the paydebitaccnum
	 */
	public String getPaydebitaccnum() {
		return paydebitaccnum;
	}
	/**
	 * @param paydebitaccnum the paydebitaccnum to set
	 */
	public void setPaydebitaccnum(String paydebitaccnum) {
		this.paydebitaccnum = paydebitaccnum;
	}
	/**
	 * @return the paydebitaccctrycode
	 */
	public String getPaydebitaccctrycode() {
		return paydebitaccctrycode;
	}
	/**
	 * @param paydebitaccctrycode the paydebitaccctrycode to set
	 */
	public void setPaydebitaccctrycode(String paydebitaccctrycode) {
		this.paydebitaccctrycode = paydebitaccctrycode;
	}
	/**
	 * @return the paydebitacccitycode
	 */
	public String getPaydebitacccitycode() {
		return paydebitacccitycode;
	}
	/**
	 * @param paydebitacccitycode the paydebitacccitycode to set
	 */
	public void setPaydebitacccitycode(String paydebitacccitycode) {
		this.paydebitacccitycode = paydebitacccitycode;
	}
	/**
	 * @return the paycurrcode
	 */
	public String getPaycurrcode() {
		return paycurrcode;
	}
	/**
	 * @param paycurrcode the paycurrcode to set
	 */
	public void setPaycurrcode(String paycurrcode) {
		this.paycurrcode = paycurrcode;
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
	 * @return the payvatamt
	 */
	public BigDecimal getPayvatamt() {
		return payvatamt;
	}
	/**
	 * @param payvatamt the payvatamt to set
	 */
	public void setPayvatamt(BigDecimal payvatamt) {
		this.payvatamt = payvatamt;
	}
	/**
	 * @return the paydiscountamt
	 */
	public BigDecimal getPaydiscountamt() {
		return paydiscountamt;
	}
	/**
	 * @param paydiscountamt the paydiscountamt to set
	 */
	public void setPaydiscountamt(BigDecimal paydiscountamt) {
		this.paydiscountamt = paydiscountamt;
	}
	/**
	 * @return the payissueddate
	 */
	public Timestamp getPayissueddate() {
		return payissueddate;
	}
	/**
	 * @param payissueddate the payissueddate to set
	 */
	public void setPayissueddate(Timestamp payissueddate) {
		this.payissueddate = payissueddate;
	}
	/**
	 * @return the paypymtdate
	 */
	public Timestamp getPaypymtdate() {
		return paypymtdate;
	}
	/**
	 * @param paypymtdate the paypymtdate to set
	 */
	public void setPaypymtdate(Timestamp paypymtdate) {
		this.paypymtdate = paypymtdate;
	}
	/**
	 * @return the paypaydetails1llmcode
	 */
	public String getPaypaydetails1llmcode() {
		return paypaydetails1llmcode;
	}
	/**
	 * @param paypaydetails1llmcode the paypaydetails1llmcode to set
	 */
	public void setPaypaydetails1llmcode(String paypaydetails1llmcode) {
		this.paypaydetails1llmcode = paypaydetails1llmcode;
	}
	/**
	 * @return the paypaydetails1
	 */
	public String getPaypaydetails1() {
		return paypaydetails1;
	}
	/**
	 * @param paypaydetails1 the paypaydetails1 to set
	 */
	public void setPaypaydetails1(String paypaydetails1) {
		this.paypaydetails1 = paypaydetails1;
	}
	/**
	 * @return the paypaydetails2llmcode
	 */
	public String getPaypaydetails2llmcode() {
		return paypaydetails2llmcode;
	}
	/**
	 * @param paypaydetails2llmcode the paypaydetails2llmcode to set
	 */
	public void setPaypaydetails2llmcode(String paypaydetails2llmcode) {
		this.paypaydetails2llmcode = paypaydetails2llmcode;
	}
	/**
	 * @return the paypaydetails2
	 */
	public String getPaypaydetails2() {
		return paypaydetails2;
	}
	/**
	 * @param paypaydetails2 the paypaydetails2 to set
	 */
	public void setPaypaydetails2(String paypaydetails2) {
		this.paypaydetails2 = paypaydetails2;
	}
	/**
	 * @return the paypaydetails1bo
	 */
	public String getPaypaydetails1bo() {
		return paypaydetails1bo;
	}
	/**
	 * @param paypaydetails1bo the paypaydetails1bo to set
	 */
	public void setPaypaydetails1bo(String paypaydetails1bo) {
		this.paypaydetails1bo = paypaydetails1bo;
	}
	/**
	 * @return the paypaydetails2bo
	 */
	public String getPaypaydetails2bo() {
		return paypaydetails2bo;
	}
	/**
	 * @param paypaydetails2bo the paypaydetails2bo to set
	 */
	public void setPaypaydetails2bo(String paypaydetails2bo) {
		this.paypaydetails2bo = paypaydetails2bo;
	}
	/**
	 * @return the paypayeeaccnum
	 */
	public String getPaypayeeaccnum() {
		return paypayeeaccnum;
	}
	/**
	 * @param paypayeeaccnum the paypayeeaccnum to set
	 */
	public void setPaypayeeaccnum(String paypayeeaccnum) {
		this.paypayeeaccnum = paypayeeaccnum;
	}
	/**
	 * @return the paypayeename1llmcode
	 */
	public String getPaypayeename1llmcode() {
		return paypayeename1llmcode;
	}
	/**
	 * @param paypayeename1llmcode the paypayeename1llmcode to set
	 */
	public void setPaypayeename1llmcode(String paypayeename1llmcode) {
		this.paypayeename1llmcode = paypayeename1llmcode;
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
	 * @return the paypayeename2llmcode
	 */
	public String getPaypayeename2llmcode() {
		return paypayeename2llmcode;
	}
	/**
	 * @param paypayeename2llmcode the paypayeename2llmcode to set
	 */
	public void setPaypayeename2llmcode(String paypayeename2llmcode) {
		this.paypayeename2llmcode = paypayeename2llmcode;
	}
	/**
	 * @return the paypayeename2
	 */
	public String getPaypayeename2() {
		return paypayeename2;
	}
	/**
	 * @param paypayeename2 the paypayeename2 to set
	 */
	public void setPaypayeename2(String paypayeename2) {
		this.paypayeename2 = paypayeename2;
	}
	/**
	 * @return the paypayeeadd1llmcode
	 */
	public String getPaypayeeadd1llmcode() {
		return paypayeeadd1llmcode;
	}
	/**
	 * @param paypayeeadd1llmcode the paypayeeadd1llmcode to set
	 */
	public void setPaypayeeadd1llmcode(String paypayeeadd1llmcode) {
		this.paypayeeadd1llmcode = paypayeeadd1llmcode;
	}
	/**
	 * @return the paypayeeadd1
	 */
	public String getPaypayeeadd1() {
		return paypayeeadd1;
	}
	/**
	 * @param paypayeeadd1 the paypayeeadd1 to set
	 */
	public void setPaypayeeadd1(String paypayeeadd1) {
		this.paypayeeadd1 = paypayeeadd1;
	}
	/**
	 * @return the paypayeeadd2llmcode
	 */
	public String getPaypayeeadd2llmcode() {
		return paypayeeadd2llmcode;
	}
	/**
	 * @param paypayeeadd2llmcode the paypayeeadd2llmcode to set
	 */
	public void setPaypayeeadd2llmcode(String paypayeeadd2llmcode) {
		this.paypayeeadd2llmcode = paypayeeadd2llmcode;
	}
	/**
	 * @return the paypayeeadd2
	 */
	public String getPaypayeeadd2() {
		return paypayeeadd2;
	}
	/**
	 * @param paypayeeadd2 the paypayeeadd2 to set
	 */
	public void setPaypayeeadd2(String paypayeeadd2) {
		this.paypayeeadd2 = paypayeeadd2;
	}
	/**
	 * @return the paypayeeadd3llmcode
	 */
	public String getPaypayeeadd3llmcode() {
		return paypayeeadd3llmcode;
	}
	/**
	 * @param paypayeeadd3llmcode the paypayeeadd3llmcode to set
	 */
	public void setPaypayeeadd3llmcode(String paypayeeadd3llmcode) {
		this.paypayeeadd3llmcode = paypayeeadd3llmcode;
	}
	/**
	 * @return the paypayeeadd3
	 */
	public String getPaypayeeadd3() {
		return paypayeeadd3;
	}
	/**
	 * @param paypayeeadd3 the paypayeeadd3 to set
	 */
	public void setPaypayeeadd3(String paypayeeadd3) {
		this.paypayeeadd3 = paypayeeadd3;
	}
	/**
	 * @return the paypayeeadd4llmcode
	 */
	public String getPaypayeeadd4llmcode() {
		return paypayeeadd4llmcode;
	}
	/**
	 * @param paypayeeadd4llmcode the paypayeeadd4llmcode to set
	 */
	public void setPaypayeeadd4llmcode(String paypayeeadd4llmcode) {
		this.paypayeeadd4llmcode = paypayeeadd4llmcode;
	}
	/**
	 * @return the paypayeeadd4
	 */
	public String getPaypayeeadd4() {
		return paypayeeadd4;
	}
	/**
	 * @param paypayeeadd4 the paypayeeadd4 to set
	 */
	public void setPaypayeeadd4(String paypayeeadd4) {
		this.paypayeeadd4 = paypayeeadd4;
	}
	/**
	 * @return the paypayeefaxno
	 */
	public String getPaypayeefaxno() {
		return paypayeefaxno;
	}
	/**
	 * @param paypayeefaxno the paypayeefaxno to set
	 */
	public void setPaypayeefaxno(String paypayeefaxno) {
		this.paypayeefaxno = paypayeefaxno;
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
	 * @return the paypayeebanklocalcode
	 */
	public String getPaypayeebanklocalcode() {
		return paypayeebanklocalcode;
	}
	/**
	 * @param paypayeebanklocalcode the paypayeebanklocalcode to set
	 */
	public void setPaypayeebanklocalcode(String paypayeebanklocalcode) {
		this.paypayeebanklocalcode = paypayeebanklocalcode;
	}
	/**
	 * @return the paypayeebranchcode
	 */
	public String getPaypayeebranchcode() {
		return paypayeebranchcode;
	}
	/**
	 * @param paypayeebranchcode the paypayeebranchcode to set
	 */
	public void setPaypayeebranchcode(String paypayeebranchcode) {
		this.paypayeebranchcode = paypayeebranchcode;
	}
	/**
	 * @return the paypayeebranchscode
	 */
	public String getPaypayeebranchscode() {
		return paypayeebranchscode;
	}
	/**
	 * @param paypayeebranchscode the paypayeebranchscode to set
	 */
	public void setPaypayeebranchscode(String paypayeebranchscode) {
		this.paypayeebranchscode = paypayeebranchscode;
	}
	/**
	 * @return the paypayeenamebo
	 */
	public String getPaypayeenamebo() {
		return paypayeenamebo;
	}
	/**
	 * @param paypayeenamebo the paypayeenamebo to set
	 */
	public void setPaypayeenamebo(String paypayeenamebo) {
		this.paypayeenamebo = paypayeenamebo;
	}
	/**
	 * @return the paypayeeaddbo1
	 */
	public String getPaypayeeaddbo1() {
		return paypayeeaddbo1;
	}
	/**
	 * @param paypayeeaddbo1 the paypayeeaddbo1 to set
	 */
	public void setPaypayeeaddbo1(String paypayeeaddbo1) {
		this.paypayeeaddbo1 = paypayeeaddbo1;
	}
	/**
	 * @return the paypayeeaddbo2
	 */
	public String getPaypayeeaddbo2() {
		return paypayeeaddbo2;
	}
	/**
	 * @param paypayeeaddbo2 the paypayeeaddbo2 to set
	 */
	public void setPaypayeeaddbo2(String paypayeeaddbo2) {
		this.paypayeeaddbo2 = paypayeeaddbo2;
	}
	/**
	 * @return the paypayeeaddbo3
	 */
	public String getPaypayeeaddbo3() {
		return paypayeeaddbo3;
	}
	/**
	 * @param paypayeeaddbo3 the paypayeeaddbo3 to set
	 */
	public void setPaypayeeaddbo3(String paypayeeaddbo3) {
		this.paypayeeaddbo3 = paypayeeaddbo3;
	}
	/**
	 * @return the payfxcalcmethodcode
	 */
	public String getPayfxcalcmethodcode() {
		return payfxcalcmethodcode;
	}
	/**
	 * @param payfxcalcmethodcode the payfxcalcmethodcode to set
	 */
	public void setPayfxcalcmethodcode(String payfxcalcmethodcode) {
		this.payfxcalcmethodcode = payfxcalcmethodcode;
	}
	/**
	 * @return the payfxtypecode
	 */
	public String getPayfxtypecode() {
		return payfxtypecode;
	}
	/**
	 * @param payfxtypecode the payfxtypecode to set
	 */
	public void setPayfxtypecode(String payfxtypecode) {
		this.payfxtypecode = payfxtypecode;
	}
	/**
	 * @return the payfxrate
	 */
	public BigDecimal getPayfxrate() {
		return payfxrate;
	}
	/**
	 * @param payfxrate the payfxrate to set
	 */
	public void setPayfxrate(BigDecimal payfxrate) {
		this.payfxrate = payfxrate;
	}
	/**
	 * @return the payfxcustomerfxrate
	 */
	public BigDecimal getPayfxcustomerfxrate() {
		return payfxcustomerfxrate;
	}
	/**
	 * @param payfxcustomerfxrate the payfxcustomerfxrate to set
	 */
	public void setPayfxcustomerfxrate(BigDecimal payfxcustomerfxrate) {
		this.payfxcustomerfxrate = payfxcustomerfxrate;
	}
	/**
	 * @return the payfxinvoiceformat
	 */
	public String getPayfxinvoiceformat() {
		return payfxinvoiceformat;
	}
	/**
	 * @param payfxinvoiceformat the payfxinvoiceformat to set
	 */
	public void setPayfxinvoiceformat(String payfxinvoiceformat) {
		this.payfxinvoiceformat = payfxinvoiceformat;
	}
	/**
	 * @return the paywhtformid
	 */
	public String getPaywhtformid() {
		return paywhtformid;
	}
	/**
	 * @param paywhtformid the paywhtformid to set
	 */
	public void setPaywhtformid(String paywhtformid) {
		this.paywhtformid = paywhtformid;
	}
	/**
	 * @return the paywhttaxid
	 */
	public String getPaywhttaxid() {
		return paywhttaxid;
	}
	/**
	 * @param paywhttaxid the paywhttaxid to set
	 */
	public void setPaywhttaxid(String paywhttaxid) {
		this.paywhttaxid = paywhttaxid;
	}
	/**
	 * @return the paywhtrefnum
	 */
	public String getPaywhtrefnum() {
		return paywhtrefnum;
	}
	/**
	 * @param paywhtrefnum the paywhtrefnum to set
	 */
	public void setPaywhtrefnum(String paywhtrefnum) {
		this.paywhtrefnum = paywhtrefnum;
	}
	/**
	 * @return the paywhttype1
	 */
	public String getPaywhttype1() {
		return paywhttype1;
	}
	/**
	 * @param paywhttype1 the paywhttype1 to set
	 */
	public void setPaywhttype1(String paywhttype1) {
		this.paywhttype1 = paywhttype1;
	}
	/**
	 * @return the paywhtdesc1
	 */
	public String getPaywhtdesc1() {
		return paywhtdesc1;
	}
	/**
	 * @param paywhtdesc1 the paywhtdesc1 to set
	 */
	public void setPaywhtdesc1(String paywhtdesc1) {
		this.paywhtdesc1 = paywhtdesc1;
	}
	/**
	 * @return the paywhtamount1
	 */
	public BigDecimal getPaywhtamount1() {
		return paywhtamount1;
	}
	/**
	 * @param paywhtamount1 the paywhtamount1 to set
	 */
	public void setPaywhtamount1(BigDecimal paywhtamount1) {
		this.paywhtamount1 = paywhtamount1;
	}
	/**
	 * @return the paywhtgrossamt1
	 */
	public BigDecimal getPaywhtgrossamt1() {
		return paywhtgrossamt1;
	}
	/**
	 * @param paywhtgrossamt1 the paywhtgrossamt1 to set
	 */
	public void setPaywhtgrossamt1(BigDecimal paywhtgrossamt1) {
		this.paywhtgrossamt1 = paywhtgrossamt1;
	}
	/**
	 * @return the paywhttype2
	 */
	public String getPaywhttype2() {
		return paywhttype2;
	}
	/**
	 * @param paywhttype2 the paywhttype2 to set
	 */
	public void setPaywhttype2(String paywhttype2) {
		this.paywhttype2 = paywhttype2;
	}
	/**
	 * @return the paywhtdesc2
	 */
	public String getPaywhtdesc2() {
		return paywhtdesc2;
	}
	/**
	 * @param paywhtdesc2 the paywhtdesc2 to set
	 */
	public void setPaywhtdesc2(String paywhtdesc2) {
		this.paywhtdesc2 = paywhtdesc2;
	}
	/**
	 * @return the paywhtamt2
	 */
	public BigDecimal getPaywhtamt2() {
		return paywhtamt2;
	}
	/**
	 * @param paywhtamt2 the paywhtamt2 to set
	 */
	public void setPaywhtamt2(BigDecimal paywhtamt2) {
		this.paywhtamt2 = paywhtamt2;
	}
	/**
	 * @return the paywhtgrossamt2
	 */
	public BigDecimal getPaywhtgrossamt2() {
		return paywhtgrossamt2;
	}
	/**
	 * @param paywhtgrossamt2 the paywhtgrossamt2 to set
	 */
	public void setPaywhtgrossamt2(BigDecimal paywhtgrossamt2) {
		this.paywhtgrossamt2 = paywhtgrossamt2;
	}
	/**
	 * @return the paylocalchargeto
	 */
	public String getPaylocalchargeto() {
		return paylocalchargeto;
	}
	/**
	 * @param paylocalchargeto the paylocalchargeto to set
	 */
	public void setPaylocalchargeto(String paylocalchargeto) {
		this.paylocalchargeto = paylocalchargeto;
	}
	/**
	 * @return the payoverseaschargeto
	 */
	public String getPayoverseaschargeto() {
		return payoverseaschargeto;
	}
	/**
	 * @param payoverseaschargeto the payoverseaschargeto to set
	 */
	public void setPayoverseaschargeto(String payoverseaschargeto) {
		this.payoverseaschargeto = payoverseaschargeto;
	}
	/**
	 * @return the payinterbankcode
	 */
	public String getPayinterbankcode() {
		return payinterbankcode;
	}
	/**
	 * @param payinterbankcode the payinterbankcode to set
	 */
	public void setPayinterbankcode(String payinterbankcode) {
		this.payinterbankcode = payinterbankcode;
	}
	/**
	 * @return the payclearingcodett
	 */
	public String getPayclearingcodett() {
		return payclearingcodett;
	}
	/**
	 * @param payclearingcodett the payclearingcodett to set
	 */
	public void setPayclearingcodett(String payclearingcodett) {
		this.payclearingcodett = payclearingcodett;
	}
	/**
	 * @return the payclearingzonecode
	 */
	public String getPayclearingzonecode() {
		return payclearingzonecode;
	}
	/**
	 * @param payclearingzonecode the payclearingzonecode to set
	 */
	public void setPayclearingzonecode(String payclearingzonecode) {
		this.payclearingzonecode = payclearingzonecode;
	}
	/**
	 * @return the paydraweebankcode
	 */
	public String getPaydraweebankcode() {
		return paydraweebankcode;
	}
	/**
	 * @param paydraweebankcode the paydraweebankcode to set
	 */
	public void setPaydraweebankcode(String paydraweebankcode) {
		this.paydraweebankcode = paydraweebankcode;
	}
	/**
	 * @return the paydeliverymethodcode
	 */
	public String getPaydeliverymethodcode() {
		return paydeliverymethodcode;
	}
	/**
	 * @param paydeliverymethodcode the paydeliverymethodcode to set
	 */
	public void setPaydeliverymethodcode(String paydeliverymethodcode) {
		this.paydeliverymethodcode = paydeliverymethodcode;
	}
	/**
	 * @return the paydeliveryto
	 */
	public String getPaydeliveryto() {
		return paydeliveryto;
	}
	/**
	 * @param paydeliveryto the paydeliveryto to set
	 */
	public void setPaydeliveryto(String paydeliveryto) {
		this.paydeliveryto = paydeliveryto;
	}
	/**
	 * @return the paypickuploccode
	 */
	public String getPaypickuploccode() {
		return paypickuploccode;
	}
	/**
	 * @param paypickuploccode the paypickuploccode to set
	 */
	public void setPaypickuploccode(String paypickuploccode) {
		this.paypickuploccode = paypickuploccode;
	}
	/**
	 * @return the paystsallianceflag
	 */
	public String getPaystsallianceflag() {
		return paystsallianceflag;
	}
	/**
	 * @param paystsallianceflag the paystsallianceflag to set
	 */
	public void setPaystsallianceflag(String paystsallianceflag) {
		this.paystsallianceflag = paystsallianceflag;
	}
	/**
	 * @return the payabpaysendflag
	 */
	public String getPayabpaysendflag() {
		return payabpaysendflag;
	}
	/**
	 * @param payabpaysendflag the payabpaysendflag to set
	 */
	public void setPayabpaysendflag(String payabpaysendflag) {
		this.payabpaysendflag = payabpaysendflag;
	}
	/**
	 * @return the payimportref
	 */
	public String getPayimportref() {
		return payimportref;
	}
	/**
	 * @param payimportref the payimportref to set
	 */
	public void setPayimportref(String payimportref) {
		this.payimportref = payimportref;
	}
	/**
	 * @return the paycustmemo
	 */
	public String getPaycustmemo() {
		return paycustmemo;
	}
	/**
	 * @param paycustmemo the paycustmemo to set
	 */
	public void setPaycustmemo(String paycustmemo) {
		this.paycustmemo = paycustmemo;
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
	 * @return the paycustname
	 */
	public String getPaycustname() {
		return paycustname;
	}
	/**
	 * @param paycustname the paycustname to set
	 */
	public void setPaycustname(String paycustname) {
		this.paycustname = paycustname;
	}
	/**
	 * @return the paycustaddr1
	 */
	public String getPaycustaddr1() {
		return paycustaddr1;
	}
	/**
	 * @param paycustaddr1 the paycustaddr1 to set
	 */
	public void setPaycustaddr1(String paycustaddr1) {
		this.paycustaddr1 = paycustaddr1;
	}
	/**
	 * @return the paycustaddr2
	 */
	public String getPaycustaddr2() {
		return paycustaddr2;
	}
	/**
	 * @param paycustaddr2 the paycustaddr2 to set
	 */
	public void setPaycustaddr2(String paycustaddr2) {
		this.paycustaddr2 = paycustaddr2;
	}
	/**
	 * @return the paycustaddr3
	 */
	public String getPaycustaddr3() {
		return paycustaddr3;
	}
	/**
	 * @param paycustaddr3 the paycustaddr3 to set
	 */
	public void setPaycustaddr3(String paycustaddr3) {
		this.paycustaddr3 = paycustaddr3;
	}
	/**
	 * @return the paypymthash
	 */
	public String getPaypymthash() {
		return paypymthash;
	}
	/**
	 * @param paypymthash the paypymthash to set
	 */
	public void setPaypymthash(String paypymthash) {
		this.paypymthash = paypymthash;
	}
	/**
	 * @return the paywhtprintlocation
	 */
	public String getPaywhtprintlocation() {
		return paywhtprintlocation;
	}
	/**
	 * @param paywhtprintlocation the paywhtprintlocation to set
	 */
	public void setPaywhtprintlocation(String paywhtprintlocation) {
		this.paywhtprintlocation = paywhtprintlocation;
	}
	/**
	 * @return the paysbatchno
	 */
	public Double getPaysbatchno() {
		return paysbatchno;
	}
	/**
	 * @param paysbatchno the paysbatchno to set
	 */
	public void setPaysbatchno(Double paysbatchno) {
		this.paysbatchno = paysbatchno;
	}
	/**
	 * @return the paybatchflag
	 */
	public String getPaybatchflag() {
		return paybatchflag;
	}
	/**
	 * @param paybatchflag the paybatchflag to set
	 */
	public void setPaybatchflag(String paybatchflag) {
		this.paybatchflag = paybatchflag;
	}
	/**
	 * @return the paydebitamtbce
	 */
	public BigDecimal getPaydebitamtbce() {
		return paydebitamtbce;
	}
	/**
	 * @param paydebitamtbce the paydebitamtbce to set
	 */
	public void setPaydebitamtbce(BigDecimal paydebitamtbce) {
		this.paydebitamtbce = paydebitamtbce;
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
	 * @return the payvattype
	 */
	public String getPayvattype() {
		return payvattype;
	}
	/**
	 * @param payvattype the payvattype to set
	 */
	public void setPayvattype(String payvattype) {
		this.payvattype = payvattype;
	}
	/**
	 * @return the paydisctype
	 */
	public String getPaydisctype() {
		return paydisctype;
	}
	/**
	 * @param paydisctype the paydisctype to set
	 */
	public void setPaydisctype(String paydisctype) {
		this.paydisctype = paydisctype;
	}
	/**
	 * @return the paywhtseqno
	 */
	public Double getPaywhtseqno() {
		return paywhtseqno;
	}
	/**
	 * @param paywhtseqno the paywhtseqno to set
	 */
	public void setPaywhtseqno(Double paywhtseqno) {
		this.paywhtseqno = paywhtseqno;
	}
	/**
	 * @return the createddate
	 */
	public Timestamp getCreateddate() {
		return createddate;
	}
	/**
	 * @param createddate the createddate to set
	 */
	public void setCreateddate(Timestamp createddate) {
		this.createddate = createddate;
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
	 * @return the deleteflag
	 */
	public String getDeleteflag() {
		return deleteflag;
	}
	/**
	 * @param deleteflag the deleteflag to set
	 */
	public void setDeleteflag(String deleteflag) {
		this.deleteflag = deleteflag;
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
	 * @return the payumi
	 */
	public String getPayumi() {
		return payumi;
	}
	/**
	 * @param payumi the payumi to set
	 */
	public void setPayumi(String payumi) {
		this.payumi = payumi;
	}
	/**
	 * @return the payacctype
	 */
	public String getPayacctype() {
		return payacctype;
	}
	/**
	 * @param payacctype the payacctype to set
	 */
	public void setPayacctype(String payacctype) {
		this.payacctype = payacctype;
	}
	/**
	 * @return the payfirsttransfercode
	 */
	public String getPayfirsttransfercode() {
		return payfirsttransfercode;
	}
	/**
	 * @param payfirsttransfercode the payfirsttransfercode to set
	 */
	public void setPayfirsttransfercode(String payfirsttransfercode) {
		this.payfirsttransfercode = payfirsttransfercode;
	}
	/**
	 * @return the payresidencycode
	 */
	public String getPayresidencycode() {
		return payresidencycode;
	}
	/**
	 * @param payresidencycode the payresidencycode to set
	 */
	public void setPayresidencycode(String payresidencycode) {
		this.payresidencycode = payresidencycode;
	}
	/**
	 * @return the paycharfiller1
	 */
	public String getPaycharfiller1() {
		return paycharfiller1;
	}
	/**
	 * @param paycharfiller1 the paycharfiller1 to set
	 */
	public void setPaycharfiller1(String paycharfiller1) {
		this.paycharfiller1 = paycharfiller1;
	}
	/**
	 * @return the paycharfiller2
	 */
	public String getPaycharfiller2() {
		return paycharfiller2;
	}
	/**
	 * @param paycharfiller2 the paycharfiller2 to set
	 */
	public void setPaycharfiller2(String paycharfiller2) {
		this.paycharfiller2 = paycharfiller2;
	}
	/**
	 * @return the paycharfiller3
	 */
	public String getPaycharfiller3() {
		return paycharfiller3;
	}
	/**
	 * @param paycharfiller3 the paycharfiller3 to set
	 */
	public void setPaycharfiller3(String paycharfiller3) {
		this.paycharfiller3 = paycharfiller3;
	}
	/**
	 * @return the paycharfiller4
	 */
	public String getPaycharfiller4() {
		return paycharfiller4;
	}
	/**
	 * @param paycharfiller4 the paycharfiller4 to set
	 */
	public void setPaycharfiller4(String paycharfiller4) {
		this.paycharfiller4 = paycharfiller4;
	}
	/**
	 * @return the paycharfiller5
	 */
	public String getPaycharfiller5() {
		return paycharfiller5;
	}
	/**
	 * @param paycharfiller5 the paycharfiller5 to set
	 */
	public void setPaycharfiller5(String paycharfiller5) {
		this.paycharfiller5 = paycharfiller5;
	}
	/**
	 * @return the paycharfiller6
	 */
	public String getPaycharfiller6() {
		return paycharfiller6;
	}
	/**
	 * @param paycharfiller6 the paycharfiller6 to set
	 */
	public void setPaycharfiller6(String paycharfiller6) {
		this.paycharfiller6 = paycharfiller6;
	}
	/**
	 * @return the paycharfiller7
	 */
	public String getPaycharfiller7() {
		return paycharfiller7;
	}
	/**
	 * @param paycharfiller7 the paycharfiller7 to set
	 */
	public void setPaycharfiller7(String paycharfiller7) {
		this.paycharfiller7 = paycharfiller7;
	}
	/**
	 * @return the paycharfiller8
	 */
	public String getPaycharfiller8() {
		return paycharfiller8;
	}
	/**
	 * @param paycharfiller8 the paycharfiller8 to set
	 */
	public void setPaycharfiller8(String paycharfiller8) {
		this.paycharfiller8 = paycharfiller8;
	}
	/**
	 * @return the payunicodefiller1
	 */
	public String getPayunicodefiller1() {
		return payunicodefiller1;
	}
	/**
	 * @param payunicodefiller1 the payunicodefiller1 to set
	 */
	public void setPayunicodefiller1(String payunicodefiller1) {
		this.payunicodefiller1 = payunicodefiller1;
	}
	/**
	 * @return the payunicodefiller2
	 */
	public String getPayunicodefiller2() {
		return payunicodefiller2;
	}
	/**
	 * @param payunicodefiller2 the payunicodefiller2 to set
	 */
	public void setPayunicodefiller2(String payunicodefiller2) {
		this.payunicodefiller2 = payunicodefiller2;
	}
	/**
	 * @return the payamtfiller1
	 */
	public BigDecimal getPayamtfiller1() {
		return payamtfiller1;
	}
	/**
	 * @param payamtfiller1 the payamtfiller1 to set
	 */
	public void setPayamtfiller1(BigDecimal payamtfiller1) {
		this.payamtfiller1 = payamtfiller1;
	}
	/**
	 * @return the payamtfiller2
	 */
	public BigDecimal getPayamtfiller2() {
		return payamtfiller2;
	}
	/**
	 * @param payamtfiller2 the payamtfiller2 to set
	 */
	public void setPayamtfiller2(BigDecimal payamtfiller2) {
		this.payamtfiller2 = payamtfiller2;
	}
	/**
	 * @return the payamtfiller3
	 */
	public BigDecimal getPayamtfiller3() {
		return payamtfiller3;
	}
	/**
	 * @param payamtfiller3 the payamtfiller3 to set
	 */
	public void setPayamtfiller3(BigDecimal payamtfiller3) {
		this.payamtfiller3 = payamtfiller3;
	}
	/**
	 * @return the payamtfiller4
	 */
	public BigDecimal getPayamtfiller4() {
		return payamtfiller4;
	}
	/**
	 * @param payamtfiller4 the payamtfiller4 to set
	 */
	public void setPayamtfiller4(BigDecimal payamtfiller4) {
		this.payamtfiller4 = payamtfiller4;
	}
	/**
	 * @return the payamtfiller5
	 */
	public BigDecimal getPayamtfiller5() {
		return payamtfiller5;
	}
	/**
	 * @param payamtfiller5 the payamtfiller5 to set
	 */
	public void setPayamtfiller5(BigDecimal payamtfiller5) {
		this.payamtfiller5 = payamtfiller5;
	}
	/**
	 * @return the payratefiller1
	 */
	public BigDecimal getPayratefiller1() {
		return payratefiller1;
	}
	/**
	 * @param payratefiller1 the payratefiller1 to set
	 */
	public void setPayratefiller1(BigDecimal payratefiller1) {
		this.payratefiller1 = payratefiller1;
	}
	/**
	 * @return the payratefiller2
	 */
	public BigDecimal getPayratefiller2() {
		return payratefiller2;
	}
	/**
	 * @param payratefiller2 the payratefiller2 to set
	 */
	public void setPayratefiller2(BigDecimal payratefiller2) {
		this.payratefiller2 = payratefiller2;
	}
	/**
	 * @return the payratefiller3
	 */
	public BigDecimal getPayratefiller3() {
		return payratefiller3;
	}
	/**
	 * @param payratefiller3 the payratefiller3 to set
	 */
	public void setPayratefiller3(BigDecimal payratefiller3) {
		this.payratefiller3 = payratefiller3;
	}
	/**
	 * @return the paypriority
	 */
	public Double getPaypriority() {
		return paypriority;
	}
	/**
	 * @param paypriority the paypriority to set
	 */
	public void setPaypriority(Double paypriority) {
		this.paypriority = paypriority;
	}
	/**
	 * @return the paycustomercode
	 */
	public String getPaycustomercode() {
		return paycustomercode;
	}
	/**
	 * @param paycustomercode the paycustomercode to set
	 */
	public void setPaycustomercode(String paycustomercode) {
		this.paycustomercode = paycustomercode;
	}
	/**
	 * @return the payconsolidate
	 */
	public String getPayconsolidate() {
		return payconsolidate;
	}
	/**
	 * @param payconsolidate the payconsolidate to set
	 */
	public void setPayconsolidate(String payconsolidate) {
		this.payconsolidate = payconsolidate;
	}
	/**
	 * @return the payurgent
	 */
	public String getPayurgent() {
		return payurgent;
	}
	/**
	 * @param payurgent the payurgent to set
	 */
	public void setPayurgent(String payurgent) {
		this.payurgent = payurgent;
	}
	/**
	 * @return the payrecurring
	 */
	public String getPayrecurring() {
		return payrecurring;
	}
	/**
	 * @param payrecurring the payrecurring to set
	 */
	public void setPayrecurring(String payrecurring) {
		this.payrecurring = payrecurring;
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
	 * @return the bfsfilereferenceno
	 */
	public String getBfsfilereferenceno() {
		return bfsfilereferenceno;
	}
	/**
	 * @param bfsfilereferenceno the bfsfilereferenceno to set
	 */
	public void setBfsfilereferenceno(String bfsfilereferenceno) {
		this.bfsfilereferenceno = bfsfilereferenceno;
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
	 * @return the actual_exe_dt
	 */
	public Timestamp getActual_exe_dt() {
		return actual_exe_dt;
	}
	/**
	 * @param actual_exe_dt the actual_exe_dt to set
	 */
	public void setActual_exe_dt(Timestamp actual_exe_dt) {
		this.actual_exe_dt = actual_exe_dt;
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
	 * @return the sent_to_paymnt
	 */
	public String getSent_to_paymnt() {
		return sent_to_paymnt;
	}
	/**
	 * @param sent_to_paymnt the sent_to_paymnt to set
	 */
	public void setSent_to_paymnt(String sent_to_paymnt) {
		this.sent_to_paymnt = sent_to_paymnt;
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
	 * @return the fromfa_id_hist
	 */
	public Double getFromfa_id_hist() {
		return fromfa_id_hist;
	}
	/**
	 * @param fromfa_id_hist the fromfa_id_hist to set
	 */
	public void setFromfa_id_hist(Double fromfa_id_hist) {
		this.fromfa_id_hist = fromfa_id_hist;
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
	 * @return the tofa_id_hist
	 */
	public Double getTofa_id_hist() {
		return tofa_id_hist;
	}
	/**
	 * @param tofa_id_hist the tofa_id_hist to set
	 */
	public void setTofa_id_hist(Double tofa_id_hist) {
		this.tofa_id_hist = tofa_id_hist;
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
	 * @return the frombranch_id_hist
	 */
	public Double getFrombranch_id_hist() {
		return frombranch_id_hist;
	}
	/**
	 * @param frombranch_id_hist the frombranch_id_hist to set
	 */
	public void setFrombranch_id_hist(Double frombranch_id_hist) {
		this.frombranch_id_hist = frombranch_id_hist;
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
	 * @return the tobranch_id_hist
	 */
	public Double getTobranch_id_hist() {
		return tobranch_id_hist;
	}
	/**
	 * @param tobranch_id_hist the tobranch_id_hist to set
	 */
	public void setTobranch_id_hist(Double tobranch_id_hist) {
		this.tobranch_id_hist = tobranch_id_hist;
	}
	/**
	 * @return the fromacct_tier
	 */
	public String getFromacct_tier() {
		return fromacct_tier;
	}
	/**
	 * @param fromacct_tier the fromacct_tier to set
	 */
	public void setFromacct_tier(String fromacct_tier) {
		this.fromacct_tier = fromacct_tier;
	}
	/**
	 * @return the toacct_tier
	 */
	public String getToacct_tier() {
		return toacct_tier;
	}
	/**
	 * @param toacct_tier the toacct_tier to set
	 */
	public void setToacct_tier(String toacct_tier) {
		this.toacct_tier = toacct_tier;
	}
	/**
	 * @return the acct_type1
	 */
	public Double getAcct_type1() {
		return acct_type1;
	}
	/**
	 * @param acct_type1 the acct_type1 to set
	 */
	public void setAcct_type1(Double acct_type1) {
		this.acct_type1 = acct_type1;
	}
	/**
	 * @return the acct_from_type
	 */
	public String getAcct_from_type() {
		return acct_from_type;
	}
	/**
	 * @param acct_from_type the acct_from_type to set
	 */
	public void setAcct_from_type(String acct_from_type) {
		this.acct_from_type = acct_from_type;
	}
	/**
	 * @return the acct_to_type
	 */
	public String getAcct_to_type() {
		return acct_to_type;
	}
	/**
	 * @param acct_to_type the acct_to_type to set
	 */
	public void setAcct_to_type(String acct_to_type) {
		this.acct_to_type = acct_to_type;
	}
	/**
	 * @return the pmnt_confirmationno
	 */
	public String getPmnt_confirmationno() {
		return pmnt_confirmationno;
	}
	/**
	 * @param pmnt_confirmationno the pmnt_confirmationno to set
	 */
	public void setPmnt_confirmationno(String pmnt_confirmationno) {
		this.pmnt_confirmationno = pmnt_confirmationno;
	}
	/**
	 * @return the acct_user_id
	 */
	public String getAcct_user_id() {
		return acct_user_id;
	}
	/**
	 * @param acct_user_id the acct_user_id to set
	 */
	public void setAcct_user_id(String acct_user_id) {
		this.acct_user_id = acct_user_id;
	}
	/**
	 * @return the acct_user_name
	 */
	public String getAcct_user_name() {
		return acct_user_name;
	}
	/**
	 * @param acct_user_name the acct_user_name to set
	 */
	public void setAcct_user_name(String acct_user_name) {
		this.acct_user_name = acct_user_name;
	}
	/**
	 * @return the fromacct_no_hist
	 */
	public String getFromacct_no_hist() {
		return fromacct_no_hist;
	}
	/**
	 * @param fromacct_no_hist the fromacct_no_hist to set
	 */
	public void setFromacct_no_hist(String fromacct_no_hist) {
		this.fromacct_no_hist = fromacct_no_hist;
	}
	/**
	 * @return the toacct_no_hist
	 */
	public String getToacct_no_hist() {
		return toacct_no_hist;
	}
	/**
	 * @param toacct_no_hist the toacct_no_hist to set
	 */
	public void setToacct_no_hist(String toacct_no_hist) {
		this.toacct_no_hist = toacct_no_hist;
	}
	/**
	 * @return the acct_type1_hist
	 */
	public Double getAcct_type1_hist() {
		return acct_type1_hist;
	}
	/**
	 * @param acct_type1_hist the acct_type1_hist to set
	 */
	public void setAcct_type1_hist(Double acct_type1_hist) {
		this.acct_type1_hist = acct_type1_hist;
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
	 * @return the locked_by_id
	 */
	public String getLocked_by_id() {
		return locked_by_id;
	}
	/**
	 * @param locked_by_id the locked_by_id to set
	 */
	public void setLocked_by_id(String locked_by_id) {
		this.locked_by_id = locked_by_id;
	}
	/**
	 * @return the fa_acct_no_br
	 */
	public String getFa_acct_no_br() {
		return fa_acct_no_br;
	}
	/**
	 * @param fa_acct_no_br the fa_acct_no_br to set
	 */
	public void setFa_acct_no_br(String fa_acct_no_br) {
		this.fa_acct_no_br = fa_acct_no_br;
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
	 * @return the friendlyname_from
	 */
	public String getFriendlyname_from() {
		return friendlyname_from;
	}
	/**
	 * @param friendlyname_from the friendlyname_from to set
	 */
	public void setFriendlyname_from(String friendlyname_from) {
		this.friendlyname_from = friendlyname_from;
	}
	/**
	 * @return the friendlyname_to
	 */
	public String getFriendlyname_to() {
		return friendlyname_to;
	}
	/**
	 * @param friendlyname_to the friendlyname_to to set
	 */
	public void setFriendlyname_to(String friendlyname_to) {
		this.friendlyname_to = friendlyname_to;
	}
	/**
	 * @return the nickname_from
	 */
	public String getNickname_from() {
		return nickname_from;
	}
	/**
	 * @param nickname_from the nickname_from to set
	 */
	public void setNickname_from(String nickname_from) {
		this.nickname_from = nickname_from;
	}
	/**
	 * @return the nickname_to
	 */
	public String getNickname_to() {
		return nickname_to;
	}
	/**
	 * @param nickname_to the nickname_to to set
	 */
	public void setNickname_to(String nickname_to) {
		this.nickname_to = nickname_to;
	}
	/**
	 * @return the coe_user_name
	 */
	public String getCoe_user_name() {
		return coe_user_name;
	}
	/**
	 * @param coe_user_name the coe_user_name to set
	 */
	public void setCoe_user_name(String coe_user_name) {
		this.coe_user_name = coe_user_name;
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
	 * @return the fromacct_type1
	 */
	public String getFromacct_type1() {
		return fromacct_type1;
	}
	/**
	 * @param fromacct_type1 the fromacct_type1 to set
	 */
	public void setFromacct_type1(String fromacct_type1) {
		this.fromacct_type1 = fromacct_type1;
	}
	/**
	 * @return the toacct_type1
	 */
	public String getToacct_type1() {
		return toacct_type1;
	}
	/**
	 * @param toacct_type1 the toacct_type1 to set
	 */
	public void setToacct_type1(String toacct_type1) {
		this.toacct_type1 = toacct_type1;
	}
	/**
	 * @return the fLG_ATTR1
	 */
	public String getFLG_ATTR1() {
		return FLG_ATTR1;
	}
	/**
	 * @param flg_attr1 the fLG_ATTR1 to set
	 */
	public void setFLG_ATTR1(String flg_attr1) {
		FLG_ATTR1 = flg_attr1;
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
	 * @return the rsa_review_flag
	 */
	public String getRsa_review_flag() {
		return rsa_review_flag;
	}
	/**
	 * @param rsa_review_flag the rsa_review_flag to set
	 */
	public void setRsa_review_flag(String rsa_review_flag) {
		this.rsa_review_flag = rsa_review_flag;
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
	 * @return the created_by_role
	 */
	public String getCreated_by_role() {
		return created_by_role;
	}
	/**
	 * @param created_by_role the created_by_role to set
	 */
	public void setCreated_by_role(String created_by_role) {
		this.created_by_role = created_by_role;
	}
	/**
	 * @return the createdby_name
	 */
	public String getCreatedby_name() {
		return createdby_name;
	}
	/**
	 * @param createdby_name the createdby_name to set
	 */
	public void setCreatedby_name(String createdby_name) {
		this.createdby_name = createdby_name;
	}
	/**
	 * @return the createdby
	 */
	public String getCreatedby() {
		return createdby;
	}
	/**
	 * @param createdby the createdby to set
	 */
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
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
	 * @return the initiator_role
	 */
	public String getInitiator_role() {
		return initiator_role;
	}
	/**
	 * @param initiator_role the initiator_role to set
	 */
	public void setInitiator_role(String initiator_role) {
		this.initiator_role = initiator_role;
	}
	/**
	 * @return the initiator_id
	 */
	public String getInitiator_id() {
		return initiator_id;
	}
	/**
	 * @param initiator_id the initiator_id to set
	 */
	public void setInitiator_id(String initiator_id) {
		this.initiator_id = initiator_id;
	}
	/**
	 * @return the current_owner_role
	 */
	public String getCurrent_owner_role() {
		return current_owner_role;
	}
	/**
	 * @param current_owner_role the current_owner_role to set
	 */
	public void setCurrent_owner_role(String current_owner_role) {
		this.current_owner_role = current_owner_role;
	}
	/**
	 * @return the current_owner_id
	 */
	public String getCurrent_owner_id() {
		return current_owner_id;
	}
	/**
	 * @param current_owner_id the current_owner_id to set
	 */
	public void setCurrent_owner_id(String current_owner_id) {
		this.current_owner_id = current_owner_id;
	}
	/**
	 * @return the current_owner_name
	 */
	public String getCurrent_owner_name() {
		return current_owner_name;
	}
	/**
	 * @param current_owner_name the current_owner_name to set
	 */
	public void setCurrent_owner_name(String current_owner_name) {
		this.current_owner_name = current_owner_name;
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
	 * @return the key_client
	 */
	public String getKey_client() {
		return key_client;
	}
	/**
	 * @param key_client the key_client to set
	 */
	public void setKey_client(String key_client) {
		this.key_client = key_client;
	}
	/**
	 * @return the key_link
	 */
	public String getKey_link() {
		return key_link;
	}
	/**
	 * @param key_link the key_link to set
	 */
	public void setKey_link(String key_link) {
		this.key_link = key_link;
	}
	/**
	 * @return the key_clientId
	 */
	public String getKey_clientId() {
		return key_clientId;
	}
	/**
	 * @param key_clientId the key_clientId to set
	 */
	public void setKey_clientId(String key_clientId) {
		this.key_clientId = key_clientId;
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
	 * @return the modifiedby_name
	 */
	public String getModifiedby_name() {
		return modifiedby_name;
	}
	/**
	 * @param modifiedby_name the modifiedby_name to set
	 */
	public void setModifiedby_name(String modifiedby_name) {
		this.modifiedby_name = modifiedby_name;
	}
	/**
	 * @return the auth_confirmed_by
	 */
	public String getAuth_confirmed_by() {
		return auth_confirmed_by;
	}
	/**
	 * @param auth_confirmed_by the auth_confirmed_by to set
	 */
	public void setAuth_confirmed_by(String auth_confirmed_by) {
		this.auth_confirmed_by = auth_confirmed_by;
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

		retValue = "DsPayTxnsTO ( "
			+ super.toString() + TAB
			+ "paytrxkey = " + this.paytrxkey + TAB
			+ "paygroupid = " + this.paygroupid + TAB
			+ "paybatchref = " + this.paybatchref + TAB
			+ "paypayref = " + this.paypayref + TAB
			+ "payinitiationchannelkey = " + this.payinitiationchannelkey + TAB
			+ "paytypecode = " + this.paytypecode + TAB
			+ "payprocmode = " + this.payprocmode + TAB
			+ "payservicetypecode = " + this.payservicetypecode + TAB
			+ "paycustref = " + this.paycustref + TAB
			+ "paychannelpayref = " + this.paychannelpayref + TAB
			+ "payalliancebankid = " + this.payalliancebankid + TAB
			+ "paydebitaccnum = " + this.paydebitaccnum + TAB
			+ "paydebitaccctrycode = " + this.paydebitaccctrycode + TAB
			+ "paydebitacccitycode = " + this.paydebitacccitycode + TAB
			+ "paycurrcode = " + this.paycurrcode + TAB
			+ "payinvoiceamt = " + this.payinvoiceamt + TAB
			+ "paydebitamt = " + this.paydebitamt + TAB
			+ "payvatamt = " + this.payvatamt + TAB
			+ "paydiscountamt = " + this.paydiscountamt + TAB
			+ "payissueddate = " + this.payissueddate + TAB
			+ "paypymtdate = " + this.paypymtdate + TAB
			+ "paypaydetails1llmcode = " + this.paypaydetails1llmcode + TAB
			+ "paypaydetails1 = " + this.paypaydetails1 + TAB
			+ "paypaydetails2llmcode = " + this.paypaydetails2llmcode + TAB
			+ "paypaydetails2 = " + this.paypaydetails2 + TAB
			+ "paypaydetails1bo = " + this.paypaydetails1bo + TAB
			+ "paypaydetails2bo = " + this.paypaydetails2bo + TAB
			+ "paypayeeaccnum = " + this.paypayeeaccnum + TAB
			+ "paypayeename1llmcode = " + this.paypayeename1llmcode + TAB
			+ "paypayeename1 = " + this.paypayeename1 + TAB
			+ "paypayeename2llmcode = " + this.paypayeename2llmcode + TAB
			+ "paypayeename2 = " + this.paypayeename2 + TAB
			+ "paypayeeadd1llmcode = " + this.paypayeeadd1llmcode + TAB
			+ "paypayeeadd1 = " + this.paypayeeadd1 + TAB
			+ "paypayeeadd2llmcode = " + this.paypayeeadd2llmcode + TAB
			+ "paypayeeadd2 = " + this.paypayeeadd2 + TAB
			+ "paypayeeadd3llmcode = " + this.paypayeeadd3llmcode + TAB
			+ "paypayeeadd3 = " + this.paypayeeadd3 + TAB
			+ "paypayeeadd4llmcode = " + this.paypayeeadd4llmcode + TAB
			+ "paypayeeadd4 = " + this.paypayeeadd4 + TAB
			+ "paypayeefaxno = " + this.paypayeefaxno + TAB
			+ "paypayeebankcode = " + this.paypayeebankcode + TAB
			+ "paypayeebanklocalcode = " + this.paypayeebanklocalcode + TAB
			+ "paypayeebranchcode = " + this.paypayeebranchcode + TAB
			+ "paypayeebranchscode = " + this.paypayeebranchscode + TAB
			+ "paypayeenamebo = " + this.paypayeenamebo + TAB
			+ "paypayeeaddbo1 = " + this.paypayeeaddbo1 + TAB
			+ "paypayeeaddbo2 = " + this.paypayeeaddbo2 + TAB
			+ "paypayeeaddbo3 = " + this.paypayeeaddbo3 + TAB
			+ "payfxcalcmethodcode = " + this.payfxcalcmethodcode + TAB
			+ "payfxtypecode = " + this.payfxtypecode + TAB
			+ "payfxrate = " + this.payfxrate + TAB
			+ "payfxcustomerfxrate = " + this.payfxcustomerfxrate + TAB
			+ "payfxinvoiceformat = " + this.payfxinvoiceformat + TAB
			+ "paywhtformid = " + this.paywhtformid + TAB
			+ "paywhttaxid = " + this.paywhttaxid + TAB
			+ "paywhtrefnum = " + this.paywhtrefnum + TAB
			+ "paywhttype1 = " + this.paywhttype1 + TAB
			+ "paywhtdesc1 = " + this.paywhtdesc1 + TAB
			+ "paywhtamount1 = " + this.paywhtamount1 + TAB
			+ "paywhtgrossamt1 = " + this.paywhtgrossamt1 + TAB
			+ "paywhttype2 = " + this.paywhttype2 + TAB
			+ "paywhtdesc2 = " + this.paywhtdesc2 + TAB
			+ "paywhtamt2 = " + this.paywhtamt2 + TAB
			+ "paywhtgrossamt2 = " + this.paywhtgrossamt2 + TAB
			+ "paylocalchargeto = " + this.paylocalchargeto + TAB
			+ "payoverseaschargeto = " + this.payoverseaschargeto + TAB
			+ "payinterbankcode = " + this.payinterbankcode + TAB
			+ "payclearingcodett = " + this.payclearingcodett + TAB
			+ "payclearingzonecode = " + this.payclearingzonecode + TAB
			+ "paydraweebankcode = " + this.paydraweebankcode + TAB
			+ "paydeliverymethodcode = " + this.paydeliverymethodcode + TAB
			+ "paydeliveryto = " + this.paydeliveryto + TAB
			+ "paypickuploccode = " + this.paypickuploccode + TAB
			+ "paystsallianceflag = " + this.paystsallianceflag + TAB
			+ "payabpaysendflag = " + this.payabpaysendflag + TAB
			+ "payimportref = " + this.payimportref + TAB
			+ "paycustmemo = " + this.paycustmemo + TAB
			+ "payccsstatuscode = " + this.payccsstatuscode + TAB
			+ "paycustname = " + this.paycustname + TAB
			+ "paycustaddr1 = " + this.paycustaddr1 + TAB
			+ "paycustaddr2 = " + this.paycustaddr2 + TAB
			+ "paycustaddr3 = " + this.paycustaddr3 + TAB
			+ "paypymthash = " + this.paypymthash + TAB
			+ "paywhtprintlocation = " + this.paywhtprintlocation + TAB
			+ "paysbatchno = " + this.paysbatchno + TAB
			+ "paybatchflag = " + this.paybatchflag + TAB
			+ "paydebitamtbce = " + this.paydebitamtbce + TAB
			+ "paypayeeid = " + this.paypayeeid + TAB
			+ "payvattype = " + this.payvattype + TAB
			+ "paydisctype = " + this.paydisctype + TAB
			+ "paywhtseqno = " + this.paywhtseqno + TAB
			+ "createddate = " + this.createddate + TAB
			+ "modifiedby = " + this.modifiedby + TAB
			+ "modifieddate = " + this.modifieddate + TAB
			+ "deleteflag = " + this.deleteflag + TAB
			+ "versionnum = " + this.versionnum + TAB
			+ "payumi = " + this.payumi + TAB
			+ "payacctype = " + this.payacctype + TAB
			+ "payfirsttransfercode = " + this.payfirsttransfercode + TAB
			+ "payresidencycode = " + this.payresidencycode + TAB
			+ "paycharfiller1 = " + this.paycharfiller1 + TAB
			+ "paycharfiller2 = " + this.paycharfiller2 + TAB
			+ "paycharfiller3 = " + this.paycharfiller3 + TAB
			+ "paycharfiller4 = " + this.paycharfiller4 + TAB
			+ "paycharfiller5 = " + this.paycharfiller5 + TAB
			+ "paycharfiller6 = " + this.paycharfiller6 + TAB
			+ "paycharfiller7 = " + this.paycharfiller7 + TAB
			+ "paycharfiller8 = " + this.paycharfiller8 + TAB
			+ "payunicodefiller1 = " + this.payunicodefiller1 + TAB
			+ "payunicodefiller2 = " + this.payunicodefiller2 + TAB
			+ "payamtfiller1 = " + this.payamtfiller1 + TAB
			+ "payamtfiller2 = " + this.payamtfiller2 + TAB
			+ "payamtfiller3 = " + this.payamtfiller3 + TAB
			+ "payamtfiller4 = " + this.payamtfiller4 + TAB
			+ "payamtfiller5 = " + this.payamtfiller5 + TAB
			+ "payratefiller1 = " + this.payratefiller1 + TAB
			+ "payratefiller2 = " + this.payratefiller2 + TAB
			+ "payratefiller3 = " + this.payratefiller3 + TAB
			+ "paypriority = " + this.paypriority + TAB
			+ "paycustomercode = " + this.paycustomercode + TAB
			+ "payconsolidate = " + this.payconsolidate + TAB
			+ "payurgent = " + this.payurgent + TAB
			+ "payrecurring = " + this.payrecurring + TAB
			+ "payfrequency = " + this.payfrequency + TAB
			+ "payfreqpaymentcount = " + this.payfreqpaymentcount + TAB
			+ "payfreqenddate = " + this.payfreqenddate + TAB
			+ "bfsfilereferenceno = " + this.bfsfilereferenceno + TAB
			+ "payfreqlimit = " + this.payfreqlimit + TAB
			+ "par_txn_no = " + this.par_txn_no + TAB
			+ "requested_exe_dt = " + this.requested_exe_dt + TAB
			+ "actual_exe_dt = " + this.actual_exe_dt + TAB
			+ "trial_depo = " + this.trial_depo + TAB
			+ "sent_to_paymnt = " + this.sent_to_paymnt + TAB
			+ "fromfa_id = " + this.fromfa_id + TAB
			+ "fromfa_id_hist = " + this.fromfa_id_hist + TAB
			+ "tofa_id = " + this.tofa_id + TAB
			+ "tofa_id_hist = " + this.tofa_id_hist + TAB
			+ "frombranch_id = " + this.frombranch_id + TAB
			+ "frombranch_id_hist = " + this.frombranch_id_hist + TAB
			+ "tobranch_id = " + this.tobranch_id + TAB
			+ "tobranch_id_hist = " + this.tobranch_id_hist + TAB
			+ "fromacct_tier = " + this.fromacct_tier + TAB
			+ "toacct_tier = " + this.toacct_tier + TAB
			+ "acct_type1 = " + this.acct_type1 + TAB
			+ "acct_from_type = " + this.acct_from_type + TAB
			+ "acct_to_type = " + this.acct_to_type + TAB
			+ "pmnt_confirmationno = " + this.pmnt_confirmationno + TAB
			+ "acct_user_id = " + this.acct_user_id + TAB
			+ "acct_user_name = " + this.acct_user_name + TAB
			+ "fromacct_no_hist = " + this.fromacct_no_hist + TAB
			+ "toacct_no_hist = " + this.toacct_no_hist + TAB
			+ "acct_type1_hist = " + this.acct_type1_hist + TAB
			+ "txn_type = " + this.txn_type + TAB
			+ "locked_by_id = " + this.locked_by_id + TAB
			+ "fa_acct_no_br = " + this.fa_acct_no_br + TAB
			+ "keyaccountnumber_from = " + this.keyaccountnumber_from + TAB
			+ "keyaccountnumber_to = " + this.keyaccountnumber_to + TAB
			+ "friendlyname_from = " + this.friendlyname_from + TAB
			+ "friendlyname_to = " + this.friendlyname_to + TAB
			+ "nickname_from = " + this.nickname_from + TAB
			+ "nickname_to = " + this.nickname_to + TAB
			+ "coe_user_name = " + this.coe_user_name + TAB
			+ "paypayee_acct_type = " + this.paypayee_acct_type + TAB
			+ "frombr_acct_no_fa = " + this.frombr_acct_no_fa + TAB
			+ "tobr_acct_no_fa = " + this.tobr_acct_no_fa + TAB
			+ "fromacct_type1 = " + this.fromacct_type1 + TAB
			+ "toacct_type1 = " + this.toacct_type1 + TAB
			+ "FLG_ATTR1 = " + this.FLG_ATTR1 + TAB
			+ "paytxneprydate = " + this.paytxneprydate + TAB
			+ "rsa_review_flag = " + this.rsa_review_flag + TAB
			+ "next_approver_role = " + this.next_approver_role + TAB
			+ "created_by_role = " + this.created_by_role + TAB
			+ "createdby_name = " + this.createdby_name + TAB
			+ "modifiedby_name = " + this.modifiedby_name + TAB
			+ "createdby = " + this.createdby + TAB
			+ "life_user_id = " + this.life_user_id + TAB
			+ "initiator_role = " + this.initiator_role + TAB
			+ "initiator_id = " + this.initiator_id + TAB
			+ "current_owner_role = " + this.current_owner_role + TAB
			+ "current_owner_id = " + this.current_owner_id + TAB
			+ "current_owner_name = " + this.current_owner_name + TAB
			+ "dont_spawn_flag = " + this.dont_spawn_flag + TAB
			+ "same_name_flag = " + this.same_name_flag + TAB
			+ "key_client = " + this.key_client + TAB
			+ "key_link = " + this.key_link + TAB
			+ "key_clientId = " + this.key_clientId + TAB
			+ "auth_mode = " + this.auth_mode + TAB
			+ "auth_info_reciever = " + this.auth_info_reciever + TAB
			+ "verbal_auth_client_name = " + this.verbal_auth_client_name + TAB
			+ "verbal_auth_date = " + this.verbal_auth_date + TAB
			+ "verbal_auth_time = " + this.verbal_auth_time + TAB
			+ "client_verification_desc = " + this.client_verification_desc + TAB
			+ "auth_confirmed_by = " + this.auth_confirmed_by + TAB
			+ "rta_booked_in_flag = " + this.rta_booked_in_flag + TAB
			+ "retirement_mnemonic = " + this.retirement_mnemonic + TAB
			+ "qualifier = " + this.qualifier + TAB
			+ "reverse_qualifier = " + this.reverse_qualifier + TAB
			+ "ofac_case_id = " + this.ofac_case_id + TAB
			+ "display_qualifier = " + this.display_qualifier + TAB
			+ " )";

		return retValue;
	}

}