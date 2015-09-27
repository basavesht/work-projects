/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

import java.sql.Timestamp;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *   224703			30-05-2011		P3				 3rd Party ACH - Alerts
 *   224703         01-07-2011      P3               CR 262
 * **********************************************************
 */
public class DsExtPayeeDetailsOutTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 793211681985313986L;
	private String cpypayeeid = null;
	private String cpyacctype = null;
	private String cpypayeename1 = null;
	private String cpybankcode = null;
	private String cpyaccnum = null;
	private String cpyboname1 = null;
	private String life_user_id = null;
	private String sign_up_mode = null;
	private String account_owner = null;
	private String nick_name = null;
	private String hashedUniqueId = null;
	private String external_acct_link = null;
	private String created_by_id = null;
	private String cpystatus = null;
	private Timestamp created_date = null;
	private String key_client = null;
	private String key_link = null;
	private String account_form = null;
	private String voided_check = null;
	private String third_party_ind = null;
	private String acnt_business_name = null;
	private String cpyadd1 = null;
	private String cpyadd2 = null;
	private String cpyadd3 = null;
	private String cpyadd4 = null;
	private String rcvr_add1 = null;
	private String rcvr_add2 = null;
	private String rcvr_add3 = null;
	private String rcvr_add4 = null;
	private String primaryAccount = null;
	private String ofac_case_id = null;

	public String getCpypayeeid() {
		return cpypayeeid;
	}
	public void setCpypayeeid(String cpypayeeid) {
		this.cpypayeeid = cpypayeeid;
	}
	public String getCpyacctype() {
		return cpyacctype;
	}
	public void setCpyacctype(String cpyacctype) {
		this.cpyacctype = cpyacctype;
	}
	public String getCpypayeename1() {
		return cpypayeename1;
	}
	public void setCpypayeename1(String cpypayeename1) {
		this.cpypayeename1 = cpypayeename1;
	}
	public String getCpybankcode() {
		return cpybankcode;
	}
	public void setCpybankcode(String cpybankcode) {
		this.cpybankcode = cpybankcode;
	}
	public String getCpyaccnum() {
		return cpyaccnum;
	}
	public void setCpyaccnum(String cpyaccnum) {
		this.cpyaccnum = cpyaccnum;
	}
	public String getCpyboname1() {
		return cpyboname1;
	}
	public void setCpyboname1(String cpyboname1) {
		this.cpyboname1 = cpyboname1;
	}
	public String getLife_user_id() {
		return life_user_id;
	}
	public void setLife_user_id(String life_user_id) {
		this.life_user_id = life_user_id;
	}
	public String getSign_up_mode() {
		return sign_up_mode;
	}
	public void setSign_up_mode(String sign_up_mode) {
		this.sign_up_mode = sign_up_mode;
	}
	public String getAccount_owner() {
		return account_owner;
	}
	public void setAccount_owner(String account_owner) {
		this.account_owner = account_owner;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getHashedUniqueId() {
		return hashedUniqueId;
	}
	public void setHashedUniqueId(String hashedUniqueId) {
		this.hashedUniqueId = hashedUniqueId;
	}
	public String getExternal_acct_link() {
		return external_acct_link;
	}
	public void setExternal_acct_link(String external_acct_link) {
		this.external_acct_link = external_acct_link;
	}
	public String getCreated_by_id() {
		return created_by_id;
	}
	public void setCreated_by_id(String created_by_id) {
		this.created_by_id = created_by_id;
	}
	public String getCpystatus() {
		return cpystatus;
	}
	public void setCpystatus(String cpystatus) {
		this.cpystatus = cpystatus;
	}
	public Timestamp getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Timestamp created_date) {
		this.created_date = created_date;
	}
	public String getKey_client() {
		return key_client;
	}
	public void setKey_client(String key_client) {
		this.key_client = key_client;
	}
	public String getKey_link() {
		return key_link;
	}
	public void setKey_link(String key_link) {
		this.key_link = key_link;
	}
	public String getAccount_form() {
		return account_form;
	}
	public void setAccount_form(String account_form) {
		this.account_form = account_form;
	}
	public String getVoided_check() {
		return voided_check;
	}
	public void setVoided_check(String voided_check) {
		this.voided_check = voided_check;
	}
	public String getThird_party_ind() {
		return third_party_ind;
	}
	public void setThird_party_ind(String third_party_ind) {
		this.third_party_ind = third_party_ind;
	}
	public String getAcnt_business_name() {
		return acnt_business_name;
	}
	public void setAcnt_business_name(String acnt_business_name) {
		this.acnt_business_name = acnt_business_name;
	}
	public String getCpyadd1() {
		return cpyadd1;
	}
	public void setCpyadd1(String cpyadd1) {
		this.cpyadd1 = cpyadd1;
	}
	public String getCpyadd2() {
		return cpyadd2;
	}
	public void setCpyadd2(String cpyadd2) {
		this.cpyadd2 = cpyadd2;
	}
	public String getCpyadd3() {
		return cpyadd3;
	}
	public void setCpyadd3(String cpyadd3) {
		this.cpyadd3 = cpyadd3;
	}
	public String getCpyadd4() {
		return cpyadd4;
	}
	public void setCpyadd4(String cpyadd4) {
		this.cpyadd4 = cpyadd4;
	}
	public String getRcvr_add1() {
		return rcvr_add1;
	}
	public void setRcvr_add1(String rcvr_add1) {
		this.rcvr_add1 = rcvr_add1;
	}
	public String getRcvr_add2() {
		return rcvr_add2;
	}
	public void setRcvr_add2(String rcvr_add2) {
		this.rcvr_add2 = rcvr_add2;
	}
	public String getRcvr_add3() {
		return rcvr_add3;
	}
	public void setRcvr_add3(String rcvr_add3) {
		this.rcvr_add3 = rcvr_add3;
	}
	public String getRcvr_add4() {
		return rcvr_add4;
	}
	public void setRcvr_add4(String rcvr_add4) {
		this.rcvr_add4 = rcvr_add4;
	}
	public String getPrimaryAccount() {
		return primaryAccount;
	}
	public void setPrimaryAccount(String primaryAccount) {
		this.primaryAccount = primaryAccount;
	}
	public String getOfac_case_id() {
		return ofac_case_id;
	}
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

		retValue = "DsExtPayeeDetailsOutTO ( "
			+ super.toString() + TAB
			+ "cpypayeeid = " + this.cpypayeeid + TAB
			+ "cpyacctype = " + this.cpyacctype + TAB
			+ "cpypayeename1 = " + this.cpypayeename1 + TAB
			+ "cpybankcode = " + this.cpybankcode + TAB
			+ "cpyaccnum = " + this.cpyaccnum + TAB
			+ "cpyboname1 = " + this.cpyboname1 + TAB
			+ "life_user_id = " + this.life_user_id + TAB
			+ "sign_up_mode = " + this.sign_up_mode + TAB
			+ "account_owner = " + this.account_owner + TAB
			+ "nick_name = " + this.nick_name + TAB
			+ "hashedUniqueId = " + this.hashedUniqueId + TAB
			+ "external_acct_link = " + this.external_acct_link + TAB
			+ "created_by_id = " + this.created_by_id + TAB
			+ "cpystatus = " + this.cpystatus + TAB
			+ "created_date = " + this.created_date + TAB
			+ "key_client = " + this.key_client + TAB
			+ "key_link = " + this.key_link + TAB
			+ "account_form = " + this.account_form + TAB
			+ "voided_check = " + this.voided_check + TAB
			+ "third_party_ind = " + this.third_party_ind + TAB
			+ "acnt_business_name = " + this.acnt_business_name + TAB
			+ "cpyadd1 = " + this.cpyadd1 + TAB
			+ "cpyadd2 = " + this.cpyadd2 + TAB
			+ "cpyadd3 = " + this.cpyadd3 + TAB
			+ "cpyadd4 = " + this.cpyadd4 + TAB
			+ "rcvr_add1 = " + this.rcvr_add1 + TAB
			+ "rcvr_add2 = " + this.rcvr_add2 + TAB
			+ "rcvr_add3 = " + this.rcvr_add3 + TAB
			+ "rcvr_add4 = " + this.rcvr_add4 + TAB
			+ "primaryAccount = " + this.primaryAccount + TAB
			+ "ofac_case_id = " + this.ofac_case_id + TAB
			+ " )";

		return retValue;
	}

}