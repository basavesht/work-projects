package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;
import java.sql.Timestamp;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class Check_Print_Dtls extends EBWTransferObject implements Serializable {

	private static final long serialVersionUID = 8417024469256528542L;
	private String confirmation_no = null;
	private String check_no = null;
	private String check_status = null;
	private Double void_reason_code = null;
	private String void_reason_desc = null;
	private Timestamp issue_dt = null;
	private String print_ops_center = null;
	private String print_ops_office = null;
	private String gl_office = null;
	private String gl_base = null;
	private String gl_type = null;
	private String ops_center = null;
	private String printer_name = null;
	private String aba_number = null;
	private String bank_no = null;
	private String bank_account_no = null;
	private String bank_alpha_code = null;
	private String bank_name = null;
	private String bank_city = null;
	private String bank_state = null;
	private String branch_name = null;
	private String branch_addressLine1 = null;
	private String branch_city = null;
	private String branch_state = null;
	private String branch_zip = null;
	private String fractionLine1 = null;

	//Print Check Response...
	private String printerResponse = null;
	private String primaryPrinterCode = null;
	private String primaryPrinterResponse = null;

	/**
	 * @return the confirmation_no
	 */
	public String getConfirmation_no() {
		return confirmation_no;
	}
	/**
	 * @param confirmation_no the confirmation_no to set
	 */
	public void setConfirmation_no(String confirmation_no) {
		this.confirmation_no = confirmation_no;
	}
	/**
	 * @return the check_no
	 */
	public String getCheck_no() {
		return check_no;
	}
	/**
	 * @param check_no the check_no to set
	 */
	public void setCheck_no(String check_no) {
		this.check_no = check_no;
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
	 * @return the print_ops_center
	 */
	public String getPrint_ops_center() {
		return print_ops_center;
	}
	/**
	 * @param print_ops_center the print_ops_center to set
	 */
	public void setPrint_ops_center(String print_ops_center) {
		this.print_ops_center = print_ops_center;
	}
	/**
	 * @return the print_ops_office
	 */
	public String getPrint_ops_office() {
		return print_ops_office;
	}
	/**
	 * @param print_ops_office the print_ops_office to set
	 */
	public void setPrint_ops_office(String print_ops_office) {
		this.print_ops_office = print_ops_office;
	}
	/**
	 * @return the gl_office
	 */
	public String getGl_office() {
		return gl_office;
	}
	/**
	 * @param gl_office the gl_office to set
	 */
	public void setGl_office(String gl_office) {
		this.gl_office = gl_office;
	}
	/**
	 * @return the gl_base
	 */
	public String getGl_base() {
		return gl_base;
	}
	/**
	 * @param gl_base the gl_base to set
	 */
	public void setGl_base(String gl_base) {
		this.gl_base = gl_base;
	}
	/**
	 * @return the gl_type
	 */
	public String getGl_type() {
		return gl_type;
	}
	/**
	 * @param gl_type the gl_type to set
	 */
	public void setGl_type(String gl_type) {
		this.gl_type = gl_type;
	}
	/**
	 * @return the ops_center
	 */
	public String getOps_center() {
		return ops_center;
	}
	/**
	 * @param ops_center the ops_center to set
	 */
	public void setOps_center(String ops_center) {
		this.ops_center = ops_center;
	}
	/**
	 * @return the printer_name
	 */
	public String getPrinter_name() {
		return printer_name;
	}
	/**
	 * @param printer_name the printer_name to set
	 */
	public void setPrinter_name(String printer_name) {
		this.printer_name = printer_name;
	}
	/**
	 * @return the aba_number
	 */
	public String getAba_number() {
		return aba_number;
	}
	/**
	 * @param aba_number the aba_number to set
	 */
	public void setAba_number(String aba_number) {
		this.aba_number = aba_number;
	}
	/**
	 * @return the bank_no
	 */
	public String getBank_no() {
		return bank_no;
	}
	/**
	 * @param bank_no the bank_no to set
	 */
	public void setBank_no(String bank_no) {
		this.bank_no = bank_no;
	}
	/**
	 * @return the bank_account_no
	 */
	public String getBank_account_no() {
		return bank_account_no;
	}
	/**
	 * @param bank_account_no the bank_account_no to set
	 */
	public void setBank_account_no(String bank_account_no) {
		this.bank_account_no = bank_account_no;
	}
	/**
	 * @return the bank_alpha_code
	 */
	public String getBank_alpha_code() {
		return bank_alpha_code;
	}
	/**
	 * @param bank_alpha_code the bank_alpha_code to set
	 */
	public void setBank_alpha_code(String bank_alpha_code) {
		this.bank_alpha_code = bank_alpha_code;
	}
	/**
	 * @return the bank_name
	 */
	public String getBank_name() {
		return bank_name;
	}
	/**
	 * @param bank_name the bank_name to set
	 */
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	/**
	 * @return the bank_city
	 */
	public String getBank_city() {
		return bank_city;
	}
	/**
	 * @param bank_city the bank_city to set
	 */
	public void setBank_city(String bank_city) {
		this.bank_city = bank_city;
	}
	/**
	 * @return the bank_state
	 */
	public String getBank_state() {
		return bank_state;
	}
	/**
	 * @param bank_state the bank_state to set
	 */
	public void setBank_state(String bank_state) {
		this.bank_state = bank_state;
	}
	/**
	 * @return the branch_name
	 */
	public String getBranch_name() {
		return branch_name;
	}
	/**
	 * @param branch_name the branch_name to set
	 */
	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}
	/**
	 * @return the branch_addressLine1
	 */
	public String getBranch_addressLine1() {
		return branch_addressLine1;
	}
	/**
	 * @param branch_addressLine1 the branch_addressLine1 to set
	 */
	public void setBranch_addressLine1(String branch_addressLine1) {
		this.branch_addressLine1 = branch_addressLine1;
	}
	/**
	 * @return the branch_city
	 */
	public String getBranch_city() {
		return branch_city;
	}
	/**
	 * @param branch_city the branch_city to set
	 */
	public void setBranch_city(String branch_city) {
		this.branch_city = branch_city;
	}
	/**
	 * @return the branch_state
	 */
	public String getBranch_state() {
		return branch_state;
	}
	/**
	 * @param branch_state the branch_state to set
	 */
	public void setBranch_state(String branch_state) {
		this.branch_state = branch_state;
	}
	/**
	 * @return the branch_zip
	 */
	public String getBranch_zip() {
		return branch_zip;
	}
	/**
	 * @param branch_zip the branch_zip to set
	 */
	public void setBranch_zip(String branch_zip) {
		this.branch_zip = branch_zip;
	}
	/**
	 * @return the fractionLine1
	 */
	public String getFractionLine1() {
		return fractionLine1;
	}
	/**
	 * @param fractionLine1 the fractionLine1 to set
	 */
	public void setFractionLine1(String fractionLine1) {
		this.fractionLine1 = fractionLine1;
	}
	/**
	 * @return the printerResponse
	 */
	public String getPrinterResponse() {
		return printerResponse;
	}
	/**
	 * @param printerResponse the printerResponse to set
	 */
	public void setPrinterResponse(String printerResponse) {
		this.printerResponse = printerResponse;
	}
	/**
	 * @return the primaryPrinterCode
	 */
	public String getPrimaryPrinterCode() {
		return primaryPrinterCode;
	}
	/**
	 * @param primaryPrinterCode the primaryPrinterCode to set
	 */
	public void setPrimaryPrinterCode(String primaryPrinterCode) {
		this.primaryPrinterCode = primaryPrinterCode;
	}
	/**
	 * @return the primaryPrinterResponse
	 */
	public String getPrimaryPrinterResponse() {
		return primaryPrinterResponse;
	}
	/**
	 * @param primaryPrinterResponse the primaryPrinterResponse to set
	 */
	public void setPrimaryPrinterResponse(String primaryPrinterResponse) {
		this.primaryPrinterResponse = primaryPrinterResponse;
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

		retValue = "Check_Print_Dtls ( "
			+ super.toString() + TAB
			+ "confirmation_no = " + this.confirmation_no + TAB
			+ "check_no = " + this.check_no + TAB
			+ "check_status = " + this.check_status + TAB
			+ "void_reason_code = " + this.void_reason_code + TAB
			+ "void_reason_desc = " + this.void_reason_desc + TAB
			+ "issue_dt = " + this.issue_dt + TAB
			+ "print_ops_center = " + this.print_ops_center + TAB
			+ "print_ops_office = " + this.print_ops_office + TAB
			+ "gl_office = " + this.gl_office + TAB
			+ "gl_base = " + this.gl_base + TAB
			+ "gl_type = " + this.gl_type + TAB
			+ "ops_center = " + this.ops_center + TAB
			+ "printer_name = " + this.printer_name + TAB
			+ "aba_number = " + this.aba_number + TAB
			+ "bank_no = " + this.bank_no + TAB
			+ "bank_account_no = " + this.bank_account_no + TAB
			+ "bank_alpha_code = " + this.bank_alpha_code + TAB
			+ "bank_name = " + this.bank_name + TAB
			+ "bank_city = " + this.bank_city + TAB
			+ "bank_state = " + this.bank_state + TAB
			+ "branch_name = " + this.branch_name + TAB
			+ "branch_addressLine1 = " + this.branch_addressLine1 + TAB
			+ "branch_city = " + this.branch_city + TAB
			+ "branch_state = " + this.branch_state + TAB
			+ "branch_zip = " + this.branch_zip + TAB
			+ "fractionLine1 = " + this.fractionLine1 + TAB
			+ "printerResponse = " + this.printerResponse + TAB
			+ "primaryPrinterCode = " + this.primaryPrinterCode + TAB
			+ "primaryPrinterResponse = " + this.primaryPrinterResponse + TAB
			+ " )";

		return retValue;
	}

}