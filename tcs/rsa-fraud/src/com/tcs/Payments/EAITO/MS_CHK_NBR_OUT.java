package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_CHK_NBR_OUT implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8725453036940283191L;
	private Integer RETURN_CODE = null;
	private String ERROR_CODE = null;
	private String ERROR_MSG = null;
	private Integer CHECK_NUMBER = null;
	private String PRINT_OPS_CENTER = null;
	private Integer PRINT_OPS_OFFICE = null;
	private Integer GL_OFFICE  = null;
	private Integer GL_BASE = null;
	private Integer GL_TYPE = null;
	private Integer BANK_NUMBER = null;
	private String BANK_ALPHA_CODE = null;
	private String ABA_NUMBER = null;
	private String BANK_ACCOUNT = null;
	private String BANK_NAME = null;
	private String BANK_CITY = null;
	private String BANK_STATE = null;
	private String BRANCH_NAME_LOCATION = null;
	private String BRANCH_ADDRESS = null;
	private String BRANCH_CITY = null;
	private String BRANCH_STATE = null;
	private String BRANCH_ZIP = null;
	private String LS_OUT_EXPAND = null;
	private String BANK_FRACTION = null;

	/**
	 * @return the rETURN_CODE
	 */
	public Integer getRETURN_CODE() {
		return RETURN_CODE;
	}
	/**
	 * @param return_code the rETURN_CODE to set
	 */
	public void setRETURN_CODE(Integer return_code) {
		RETURN_CODE = return_code;
	}
	/**
	 * @return the eRROR_CODE
	 */
	public String getERROR_CODE() {
		return ERROR_CODE;
	}
	/**
	 * @param error_code the eRROR_CODE to set
	 */
	public void setERROR_CODE(String error_code) {
		ERROR_CODE = error_code;
	}
	/**
	 * @return the eRROR_MSG
	 */
	public String getERROR_MSG() {
		return ERROR_MSG;
	}
	/**
	 * @param error_msg the eRROR_MSG to set
	 */
	public void setERROR_MSG(String error_msg) {
		ERROR_MSG = error_msg;
	}
	/**
	 * @return the cHECK_NUMBER
	 */
	public Integer getCHECK_NUMBER() {
		return CHECK_NUMBER;
	}
	/**
	 * @param check_number the cHECK_NUMBER to set
	 */
	public void setCHECK_NUMBER(Integer check_number) {
		CHECK_NUMBER = check_number;
	}
	/**
	 * @return the pRINT_OPS_CENTER
	 */
	public String getPRINT_OPS_CENTER() {
		return PRINT_OPS_CENTER;
	}
	/**
	 * @param print_ops_center the pRINT_OPS_CENTER to set
	 */
	public void setPRINT_OPS_CENTER(String print_ops_center) {
		PRINT_OPS_CENTER = print_ops_center;
	}
	/**
	 * @return the pRINT_OPS_OFFICE
	 */
	public Integer getPRINT_OPS_OFFICE() {
		return PRINT_OPS_OFFICE;
	}
	/**
	 * @param print_ops_office the pRINT_OPS_OFFICE to set
	 */
	public void setPRINT_OPS_OFFICE(Integer print_ops_office) {
		PRINT_OPS_OFFICE = print_ops_office;
	}
	/**
	 * @return the gL_OFFICE
	 */
	public Integer getGL_OFFICE() {
		return GL_OFFICE;
	}
	/**
	 * @param gl_office the gL_OFFICE to set
	 */
	public void setGL_OFFICE(Integer gl_office) {
		GL_OFFICE = gl_office;
	}
	/**
	 * @return the gL_BASE
	 */
	public Integer getGL_BASE() {
		return GL_BASE;
	}
	/**
	 * @param gl_base the gL_BASE to set
	 */
	public void setGL_BASE(Integer gl_base) {
		GL_BASE = gl_base;
	}
	/**
	 * @return the gL_TYPE
	 */
	public Integer getGL_TYPE() {
		return GL_TYPE;
	}
	/**
	 * @param gl_type the gL_TYPE to set
	 */
	public void setGL_TYPE(Integer gl_type) {
		GL_TYPE = gl_type;
	}
	/**
	 * @return the bANK_NUMBER
	 */
	public Integer getBANK_NUMBER() {
		return BANK_NUMBER;
	}
	/**
	 * @param bank_number the bANK_NUMBER to set
	 */
	public void setBANK_NUMBER(Integer bank_number) {
		BANK_NUMBER = bank_number;
	}
	/**
	 * @return the bANK_ALPHA_CODE
	 */
	public String getBANK_ALPHA_CODE() {
		return BANK_ALPHA_CODE;
	}
	/**
	 * @param bank_alpha_code the bANK_ALPHA_CODE to set
	 */
	public void setBANK_ALPHA_CODE(String bank_alpha_code) {
		BANK_ALPHA_CODE = bank_alpha_code;
	}
	/**
	 * @return the aBA_NUMBER
	 */
	public String getABA_NUMBER() {
		return ABA_NUMBER;
	}
	/**
	 * @param aba_number the aBA_NUMBER to set
	 */
	public void setABA_NUMBER(String aba_number) {
		ABA_NUMBER = aba_number;
	}
	/**
	 * @return the bANK_ACCOUNT
	 */
	public String getBANK_ACCOUNT() {
		return BANK_ACCOUNT;
	}
	/**
	 * @param bank_account the bANK_ACCOUNT to set
	 */
	public void setBANK_ACCOUNT(String bank_account) {
		BANK_ACCOUNT = bank_account;
	}
	/**
	 * @return the bANK_NAME
	 */
	public String getBANK_NAME() {
		return BANK_NAME;
	}
	/**
	 * @param bank_name the bANK_NAME to set
	 */
	public void setBANK_NAME(String bank_name) {
		BANK_NAME = bank_name;
	}
	/**
	 * @return the bANK_CITY
	 */
	public String getBANK_CITY() {
		return BANK_CITY;
	}
	/**
	 * @param bank_city the bANK_CITY to set
	 */
	public void setBANK_CITY(String bank_city) {
		BANK_CITY = bank_city;
	}
	/**
	 * @return the bANK_STATE
	 */
	public String getBANK_STATE() {
		return BANK_STATE;
	}
	/**
	 * @param bank_state the bANK_STATE to set
	 */
	public void setBANK_STATE(String bank_state) {
		BANK_STATE = bank_state;
	}
	/**
	 * @return the bRANCH_NAME_LOCATION
	 */
	public String getBRANCH_NAME_LOCATION() {
		return BRANCH_NAME_LOCATION;
	}
	/**
	 * @param branch_name_location the bRANCH_NAME_LOCATION to set
	 */
	public void setBRANCH_NAME_LOCATION(String branch_name_location) {
		BRANCH_NAME_LOCATION = branch_name_location;
	}
	/**
	 * @return the bRANCH_ADDRESS
	 */
	public String getBRANCH_ADDRESS() {
		return BRANCH_ADDRESS;
	}
	/**
	 * @param branch_address the bRANCH_ADDRESS to set
	 */
	public void setBRANCH_ADDRESS(String branch_address) {
		BRANCH_ADDRESS = branch_address;
	}
	/**
	 * @return the bRANCH_CITY
	 */
	public String getBRANCH_CITY() {
		return BRANCH_CITY;
	}
	/**
	 * @param branch_city the bRANCH_CITY to set
	 */
	public void setBRANCH_CITY(String branch_city) {
		BRANCH_CITY = branch_city;
	}
	/**
	 * @return the bRANCH_STATE
	 */
	public String getBRANCH_STATE() {
		return BRANCH_STATE;
	}
	/**
	 * @param branch_state the bRANCH_STATE to set
	 */
	public void setBRANCH_STATE(String branch_state) {
		BRANCH_STATE = branch_state;
	}
	/**
	 * @return the bRANCH_ZIP
	 */
	public String getBRANCH_ZIP() {
		return BRANCH_ZIP;
	}
	/**
	 * @param branch_zip the bRANCH_ZIP to set
	 */
	public void setBRANCH_ZIP(String branch_zip) {
		BRANCH_ZIP = branch_zip;
	}
	/**
	 * @return the lS_OUT_EXPAND
	 */
	public String getLS_OUT_EXPAND() {
		return LS_OUT_EXPAND;
	}
	/**
	 * @param ls_out_expand the lS_OUT_EXPAND to set
	 */
	public void setLS_OUT_EXPAND(String ls_out_expand) {
		LS_OUT_EXPAND = ls_out_expand;
	}
	/**
	 * @return the bANK_FRACTION
	 */
	public String getBANK_FRACTION() {
		return BANK_FRACTION;
	}
	/**
	 * @param bank_fraction the bANK_FRACTION to set
	 */
	public void setBANK_FRACTION(String bank_fraction) {
		BANK_FRACTION = bank_fraction;
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

		retValue = "MS_CHK_NBR_OUT ( "
			+ super.toString() + TAB
			+ "RETURN_CODE = " + this.RETURN_CODE + TAB
			+ "ERROR_CODE = " + this.ERROR_CODE + TAB
			+ "ERROR_MSG = " + this.ERROR_MSG + TAB
			+ "CHECK_NUMBER = " + this.CHECK_NUMBER + TAB
			+ "PRINT_OPS_CENTER = " + this.PRINT_OPS_CENTER + TAB
			+ "PRINT_OPS_OFFICE = " + this.PRINT_OPS_OFFICE + TAB
			+ "GL_OFFICE = " + this.GL_OFFICE + TAB
			+ "GL_BASE = " + this.GL_BASE + TAB
			+ "GL_TYPE = " + this.GL_TYPE + TAB
			+ "BANK_NUMBER = " + this.BANK_NUMBER + TAB
			+ "BANK_ALPHA_CODE = " + this.BANK_ALPHA_CODE + TAB
			+ "ABA_NUMBER = " + this.ABA_NUMBER + TAB
			+ "BANK_ACCOUNT = " + this.BANK_ACCOUNT + TAB
			+ "BANK_NAME = " + this.BANK_NAME + TAB
			+ "BANK_CITY = " + this.BANK_CITY + TAB
			+ "BANK_STATE = " + this.BANK_STATE + TAB
			+ "BRANCH_NAME_LOCATION = " + this.BRANCH_NAME_LOCATION + TAB
			+ "BRANCH_ADDRESS = " + this.BRANCH_ADDRESS + TAB
			+ "BRANCH_CITY = " + this.BRANCH_CITY + TAB
			+ "BRANCH_STATE = " + this.BRANCH_STATE + TAB
			+ "BRANCH_ZIP = " + this.BRANCH_ZIP + TAB
			+ "LS_OUT_EXPAND = " + this.LS_OUT_EXPAND + TAB
			+ "BANK_FRACTION = " + this.BANK_FRACTION + TAB
			+ " )";

		return retValue;
	}

}