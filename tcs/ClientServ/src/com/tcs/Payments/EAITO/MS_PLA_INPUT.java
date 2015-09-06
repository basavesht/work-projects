package com.tcs.Payments.EAITO;

/**
 * @author 259245
 *
 */
public class MS_PLA_INPUT implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -159019411811722212L;
	private String REQUEST_HOST = null;
	private String SUBMIT_TIME = null;
	private String SIGN_ON_RACF_ID = null;
	private String APPL_ID = null;
	private String FA_NUMBER=null;
	private String BRANCH_NUMBER=null;
	private String VERB=null;
	private String RETURN_DEBUG_INFO=null;
	private String RESERVED=null;
	private String PAGINATION=null;
	private String ADDITIONAL_PARAM_LIST=null;
	private String VERSION_INFO=null;
	private String LEVEL=null;
	private String SEARCH_BY=null;
	private String REQUEST_MODE=null;
	private SBLACCOUNT_IN_MSG_INFO[] MS_SBLACCOUNT_IN_MSG_INFO = new SBLACCOUNT_IN_MSG_INFO[50];

	public class SBLACCOUNT_IN_MSG_INFO implements java.io.Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 358452821000220959L;
		String LOAN_NO;
		String OFFICE;
		String ACCT_NO;
		String KEY_ACCT;

		/**
		 * @return the lOAN_NO
		 */
		public String getLOAN_NO() {
			return LOAN_NO;
		}

		/**
		 * @param loan_no the lOAN_NO to set
		 */
		public void setLOAN_NO(String loan_no) {
			LOAN_NO = loan_no;
		}

		/**
		 * @return the oFFICE
		 */
		public String getOFFICE() {
			return OFFICE;
		}

		/**
		 * @param office the oFFICE to set
		 */
		public void setOFFICE(String office) {
			OFFICE = office;
		}

		/**
		 * @return the aCCT_NO
		 */
		public String getACCT_NO() {
			return ACCT_NO;
		}

		/**
		 * @param acct_no the aCCT_NO to set
		 */
		public void setACCT_NO(String acct_no) {
			ACCT_NO = acct_no;
		}

		/**
		 * @return the kEY_ACCT
		 */
		public String getKEY_ACCT() {
			return KEY_ACCT;
		}

		/**
		 * @param key_acct the kEY_ACCT to set
		 */
		public void setKEY_ACCT(String key_acct) {
			KEY_ACCT = key_acct;
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

			retValue = "SBLACCOUNT_IN_MSG_INFO ( "
				+ super.toString() + TAB
				+ "LOAN_NO = " + this.LOAN_NO + TAB
				+ "OFFICE = " + this.OFFICE + TAB
				+ "ACCT_NO = " + this.ACCT_NO + TAB
				+ "KEY_ACCT = " + this.KEY_ACCT + TAB
				+ " )";

			return retValue;
		}
	}

	/**
	 * @return the rEQUEST_HOST
	 */
	public String getREQUEST_HOST() {
		return REQUEST_HOST;
	}

	/**
	 * @param request_host the rEQUEST_HOST to set
	 */
	public void setREQUEST_HOST(String request_host) {
		REQUEST_HOST = request_host;
	}

	/**
	 * @return the sUBMIT_TIME
	 */
	public String getSUBMIT_TIME() {
		return SUBMIT_TIME;
	}

	/**
	 * @param submit_time the sUBMIT_TIME to set
	 */
	public void setSUBMIT_TIME(String submit_time) {
		SUBMIT_TIME = submit_time;
	}

	/**
	 * @return the sIGN_ON_RACF_ID
	 */
	public String getSIGN_ON_RACF_ID() {
		return SIGN_ON_RACF_ID;
	}

	/**
	 * @param sign_on_racf_id the sIGN_ON_RACF_ID to set
	 */
	public void setSIGN_ON_RACF_ID(String sign_on_racf_id) {
		SIGN_ON_RACF_ID = sign_on_racf_id;
	}

	/**
	 * @return the aPPL_ID
	 */
	public String getAPPL_ID() {
		return APPL_ID;
	}

	/**
	 * @param appl_id the aPPL_ID to set
	 */
	public void setAPPL_ID(String appl_id) {
		APPL_ID = appl_id;
	}

	/**
	 * @return the fA_NUMBER
	 */
	public String getFA_NUMBER() {
		return FA_NUMBER;
	}

	/**
	 * @param fa_number the fA_NUMBER to set
	 */
	public void setFA_NUMBER(String fa_number) {
		FA_NUMBER = fa_number;
	}

	/**
	 * @return the bRANCH_NUMBER
	 */
	public String getBRANCH_NUMBER() {
		return BRANCH_NUMBER;
	}

	/**
	 * @param branch_number the bRANCH_NUMBER to set
	 */
	public void setBRANCH_NUMBER(String branch_number) {
		BRANCH_NUMBER = branch_number;
	}

	/**
	 * @return the vERB
	 */
	public String getVERB() {
		return VERB;
	}

	/**
	 * @param verb the vERB to set
	 */
	public void setVERB(String verb) {
		VERB = verb;
	}

	/**
	 * @return the rETURN_DEBUG_INFO
	 */
	public String getRETURN_DEBUG_INFO() {
		return RETURN_DEBUG_INFO;
	}

	/**
	 * @param rETURN_DEBUG_INFO the rETURN_DEBUG_INFO to set
	 */
	public void setRETURN_DEBUG_INFO(String rETURN_DEBUG_INFO) {
		RETURN_DEBUG_INFO = rETURN_DEBUG_INFO;
	}

	/**
	 * @return the rESERVED
	 */
	public String getRESERVED() {
		return RESERVED;
	}

	/**
	 * @param reserved the rESERVED to set
	 */
	public void setRESERVED(String reserved) {
		RESERVED = reserved;
	}

	/**
	 * @return the pAGINATION
	 */
	public String getPAGINATION() {
		return PAGINATION;
	}

	/**
	 * @param pagination the pAGINATION to set
	 */
	public void setPAGINATION(String pagination) {
		PAGINATION = pagination;
	}

	/**
	 * @return the aDDITIONAL_PARAM_LIST
	 */
	public String getADDITIONAL_PARAM_LIST() {
		return ADDITIONAL_PARAM_LIST;
	}

	/**
	 * @param additional_param_list the aDDITIONAL_PARAM_LIST to set
	 */
	public void setADDITIONAL_PARAM_LIST(String additional_param_list) {
		ADDITIONAL_PARAM_LIST = additional_param_list;
	}

	/**
	 * @return the vERSION_INFO
	 */
	public String getVERSION_INFO() {
		return VERSION_INFO;
	}

	/**
	 * @param version_info the vERSION_INFO to set
	 */
	public void setVERSION_INFO(String version_info) {
		VERSION_INFO = version_info;
	}

	/**
	 * @return the lEVEL
	 */
	public String getLEVEL() {
		return LEVEL;
	}

	/**
	 * @param level the lEVEL to set
	 */
	public void setLEVEL(String level) {
		LEVEL = level;
	}

	/**
	 * @return the sEARCH_BY
	 */
	public String getSEARCH_BY() {
		return SEARCH_BY;
	}

	/**
	 * @param search_by the sEARCH_BY to set
	 */
	public void setSEARCH_BY(String search_by) {
		SEARCH_BY = search_by;
	}

	/**
	 * @return the rEQUEST_MODE
	 */
	public String getREQUEST_MODE() {
		return REQUEST_MODE;
	}

	/**
	 * @param request_mode the rEQUEST_MODE to set
	 */
	public void setREQUEST_MODE(String request_mode) {
		REQUEST_MODE = request_mode;
	}

	/**
	 * @return the mS_SBLACCOUNT_IN_MSG_INFO
	 */
	public SBLACCOUNT_IN_MSG_INFO[] getMS_SBLACCOUNT_IN_MSG_INFO() {
		return MS_SBLACCOUNT_IN_MSG_INFO;
	}

	/**
	 * @param ms_sblaccount_in_msg_info the mS_SBLACCOUNT_IN_MSG_INFO to set
	 */
	public void setMS_SBLACCOUNT_IN_MSG_INFO(
			SBLACCOUNT_IN_MSG_INFO[] ms_sblaccount_in_msg_info) {
		MS_SBLACCOUNT_IN_MSG_INFO = ms_sblaccount_in_msg_info;
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

		retValue = "MS_PLA_INPUT ( "
			+ super.toString() + TAB
			+ "REQUEST_HOST = " + this.REQUEST_HOST + TAB
			+ "SUBMIT_TIME = " + this.SUBMIT_TIME + TAB
			+ "SIGN_ON_RACF_ID = " + this.SIGN_ON_RACF_ID + TAB
			+ "APPL_ID = " + this.APPL_ID + TAB
			+ "FA_NUMBER = " + this.FA_NUMBER + TAB
			+ "BRANCH_NUMBER = " + this.BRANCH_NUMBER + TAB
			+ "VERB = " + this.VERB + TAB
			+ "RETURN_DEBUG_INFO = " + this.RETURN_DEBUG_INFO + TAB
			+ "RESERVED = " + this.RESERVED + TAB
			+ "PAGINATION = " + this.PAGINATION + TAB
			+ "ADDITIONAL_PARAM_LIST = " + this.ADDITIONAL_PARAM_LIST + TAB
			+ "VERSION_INFO = " + this.VERSION_INFO + TAB
			+ "LEVEL = " + this.LEVEL + TAB
			+ "SEARCH_BY = " + this.SEARCH_BY + TAB
			+ "REQUEST_MODE = " + this.REQUEST_MODE + TAB
			+ "MS_SBLACCOUNT_IN_MSG_INFO = " + this.MS_SBLACCOUNT_IN_MSG_INFO + TAB
			+ " )";

		return retValue;
	}

}
