/**
 * 
 */
package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class RSA_CALLBACK_INP implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -617539437707814377L;
	private String TXN_CONF_ID = null;
	private String TXN_BATCH_CONF_ID = null;
	private String CASE_MNGMT_USR_ID = null;
	private String CASE_STATUS = null;
	private String DESCRIPTION = null;

	/**
	 * @return the tXN_CONF_ID
	 */
	public String getTXN_CONF_ID() {
		return TXN_CONF_ID;
	}
	/**
	 * @param txn_conf_id the tXN_CONF_ID to set
	 */
	public void setTXN_CONF_ID(String txn_conf_id) {
		TXN_CONF_ID = txn_conf_id;
	}
	/**
	 * @return the cASE_MNGMT_USR_ID
	 */
	public String getCASE_MNGMT_USR_ID() {
		return CASE_MNGMT_USR_ID;
	}
	/**
	 * @param case_mngmt_usr_id the cASE_MNGMT_USR_ID to set
	 */
	public void setCASE_MNGMT_USR_ID(String case_mngmt_usr_id) {
		CASE_MNGMT_USR_ID = case_mngmt_usr_id;
	}
	/**
	 * @return the cASE_STATUS
	 */
	public String getCASE_STATUS() {
		return CASE_STATUS;
	}
	/**
	 * @param case_status the cASE_STATUS to set
	 */
	public void setCASE_STATUS(String case_status) {
		CASE_STATUS = case_status;
	}
	/**
	 * @return the dESCRIPTION
	 */
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	/**
	 * @param description the dESCRIPTION to set
	 */
	public void setDESCRIPTION(String description) {
		DESCRIPTION = description;
	}
	/**
	 * @return the tXN_BATCH_CONF_ID
	 */
	public String getTXN_BATCH_CONF_ID() {
		return TXN_BATCH_CONF_ID;
	}
	/**
	 * @param txn_batch_conf_id the tXN_BATCH_CONF_ID to set
	 */
	public void setTXN_BATCH_CONF_ID(String txn_batch_conf_id) {
		TXN_BATCH_CONF_ID = txn_batch_conf_id;
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

		retValue = "RSA_CALLBACK_INP ( "
			+ super.toString() + TAB
			+ "TXN_CONF_ID = " + this.TXN_CONF_ID + TAB
			+ "TXN_BATCH_CONF_ID = " + this.TXN_BATCH_CONF_ID + TAB
			+ "CASE_MNGMT_USR_ID = " + this.CASE_MNGMT_USR_ID + TAB
			+ "CASE_STATUS = " + this.CASE_STATUS + TAB
			+ "DESCRIPTION = " + this.DESCRIPTION + TAB
			+ " )";

		return retValue;
	}

}
