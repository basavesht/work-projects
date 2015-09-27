/*
 * Created on Fri Jul 10 10:15:14 IST 2009
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
 *    224703       01-07-2011       P3               CR 262
 * **********************************************************
 */
public class DsPayeeRefLogTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2783619999365873190L;
	private String cpypayeeid = null;
	private String cpybankcode = null;
	private String cpyboname1 = null;
	private String cpyacctype = null;
	private String cpyaccnum = null;
	private String cpypayeename1 = null;
	private Double fa_id = null;
	private Double branch_id = null;
	private Timestamp created_modified_date = null;
	private String created_modified_by_id = null;
	private String created_modified_by_emp_id = null;
	private String created_modified_by_role = null;
	private String last_action = null;
	private String life_user_id = null;
	private Double ver_trials = null;
	private String cpystatus = null;
	private String deleteflag = null;
	private String usercomments = null;
	private String key_client = null;
	private String key_link = null;
	private String sign_up_mode = null;
	private String account_owner = null;
	private String account_form = null;
	private String voided_check = null;
	private String ofac_case_id = null;

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
	 * @param cpypayeeid the cpypayeeid to set.
	 */
	public void setCpypayeeid(String cpypayeeid) {
		this.cpypayeeid=cpypayeeid;
	}
	/**
	 * @return Returns the cpypayeeid.
	 */
	public String getCpypayeeid() {
		return this.cpypayeeid;
	}

	/**
	 * @param cpybankcode the cpybankcode to set.
	 */
	public void setCpybankcode(String cpybankcode) {
		this.cpybankcode=cpybankcode;
	}
	/**
	 * @return Returns the cpybankcode.
	 */
	public String getCpybankcode() {
		return this.cpybankcode;
	}

	public String getUsercomments() {
		return usercomments;
	}
	public void setUsercomments(String usercomments) {
		this.usercomments = usercomments;
	}
	/**
	 * @param cpyboname1 the cpyboname1 to set.
	 */
	public void setCpyboname1(String cpyboname1) {
		this.cpyboname1=cpyboname1;
	}
	/**
	 * @return Returns the cpyboname1.
	 */
	public String getCpyboname1() {
		return this.cpyboname1;
	}

	/**
	 * @param cpyacctype the cpyacctype to set.
	 */
	public void setCpyacctype(String cpyacctype) {
		this.cpyacctype=cpyacctype;
	}
	/**
	 * @return Returns the cpyacctype.
	 */
	public String getCpyacctype() {
		return this.cpyacctype;
	}

	/**
	 * @param cpyaccnum the cpyaccnum to set.
	 */
	public void setCpyaccnum(String cpyaccnum) {
		this.cpyaccnum=cpyaccnum;
	}
	/**
	 * @return Returns the cpyaccnum.
	 */
	public String getCpyaccnum() {
		return this.cpyaccnum;
	}

	/**
	 * @param cpypayeename1 the cpypayeename1 to set.
	 */
	public void setCpypayeename1(String cpypayeename1) {
		this.cpypayeename1=cpypayeename1;
	}
	/**
	 * @return Returns the cpypayeename1.
	 */
	public String getCpypayeename1() {
		return this.cpypayeename1;
	}

	/**
	 * @param fa_id the fa_id to set.
	 */
	public void setFa_id(Double fa_id) {
		this.fa_id=fa_id;
	}
	/**
	 * @return Returns the fa_id.
	 */
	public Double getFa_id() {
		return this.fa_id;
	}

	/**
	 * @param branch_id the branch_id to set.
	 */
	public void setBranch_id(Double branch_id) {
		this.branch_id=branch_id;
	}
	/**
	 * @return Returns the branch_id.
	 */
	public Double getBranch_id() {
		return this.branch_id;
	}

	/**
	 * @param created_modified_date the created_modified_date to set.
	 */
	public void setCreated_modified_date(Timestamp created_modified_date) {
		this.created_modified_date=created_modified_date;
	}
	/**
	 * @return Returns the created_modified_date.
	 */
	public Timestamp getCreated_modified_date() {
		return this.created_modified_date;
	}

	/**
	 * @param created_modified_by_id the created_modified_by_id to set.
	 */
	public void setCreated_modified_by_id(String created_modified_by_id) {
		this.created_modified_by_id=created_modified_by_id;
	}
	/**
	 * @return Returns the created_modified_by_id.
	 */
	public String getCreated_modified_by_id() {
		return this.created_modified_by_id;
	}

	/**
	 * @param created_modified_by_emp_id the created_modified_by_emp_id to set.
	 */
	public void setCreated_modified_by_emp_id(String created_modified_by_emp_id) {
		this.created_modified_by_emp_id=created_modified_by_emp_id;
	}
	/**
	 * @return Returns the created_modified_by_emp_id.
	 */
	public String getCreated_modified_by_emp_id() {
		return this.created_modified_by_emp_id;
	}

	/**
	 * @param created_modified_by_role the created_modified_by_role to set.
	 */
	public void setCreated_modified_by_role(String created_modified_by_role) {
		this.created_modified_by_role=created_modified_by_role;
	}
	/**
	 * @return Returns the created_modified_by_role.
	 */
	public String getCreated_modified_by_role() {
		return this.created_modified_by_role;
	}

	/**
	 * @param last_action the last_action to set.
	 */
	public void setLast_action(String last_action) {
		this.last_action=last_action;
	}
	/**
	 * @return Returns the last_action.
	 */
	public String getLast_action() {
		return this.last_action;
	}

	/**
	 * @param life_user_id the life_user_id to set.
	 */
	public void setLife_user_id(String life_user_id) {
		this.life_user_id=life_user_id;
	}
	/**
	 * @return Returns the life_user_id.
	 */
	public String getLife_user_id() {
		return this.life_user_id;
	}

	/**
	 * @param ver_trials the ver_trials to set.
	 */
	public void setVer_trials(Double ver_trials) {
		this.ver_trials=ver_trials;
	}
	/**
	 * @return Returns the ver_trials.
	 */
	public Double getVer_trials() {
		return this.ver_trials;
	}

	/**
	 * @param cpystatus the cpystatus to set.
	 */
	public void setCpystatus(String cpystatus) {
		this.cpystatus=cpystatus;
	}
	/**
	 * @return Returns the cpystatus.
	 */
	public String getCpystatus() {
		return this.cpystatus;
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

		retValue = "DsPayeeRefLogTO ( "
			+ super.toString() + TAB
			+ "cpypayeeid = " + this.cpypayeeid + TAB
			+ "cpybankcode = " + this.cpybankcode + TAB
			+ "cpyboname1 = " + this.cpyboname1 + TAB
			+ "cpyacctype = " + this.cpyacctype + TAB
			+ "cpyaccnum = " + this.cpyaccnum + TAB
			+ "cpypayeename1 = " + this.cpypayeename1 + TAB
			+ "fa_id = " + this.fa_id + TAB
			+ "branch_id = " + this.branch_id + TAB
			+ "created_modified_date = " + this.created_modified_date + TAB
			+ "created_modified_by_id = " + this.created_modified_by_id + TAB
			+ "created_modified_by_emp_id = " + this.created_modified_by_emp_id + TAB
			+ "created_modified_by_role = " + this.created_modified_by_role + TAB
			+ "last_action = " + this.last_action + TAB
			+ "life_user_id = " + this.life_user_id + TAB
			+ "ver_trials = " + this.ver_trials + TAB
			+ "cpystatus = " + this.cpystatus + TAB
			+ "deleteflag = " + this.deleteflag + TAB
			+ "usercomments = " + this.usercomments + TAB
			+ "key_client = " + this.key_client + TAB
			+ "key_link = " + this.key_link + TAB
			+ "sign_up_mode = " + this.sign_up_mode + TAB
			+ "account_owner = " + this.account_owner + TAB
			+ "account_form = " + this.account_form + TAB
			+ "voided_check = " + this.voided_check + TAB
			+ "ofac_case_id = " + this.ofac_case_id + TAB
			+ " )";

		return retValue;
	}
}