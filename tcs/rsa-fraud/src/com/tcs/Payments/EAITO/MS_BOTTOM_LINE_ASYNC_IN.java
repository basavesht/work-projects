package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_BOTTOM_LINE_ASYNC_IN implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6517670544655251781L;
	private String TYPE = null;
	private String PRINTER_NAME = null;
	private String SPOOL_JOB_NAME = null;
	private String JOB_STATUS = null;
	private String JOB_CODE = null;
	private String TXN_BATCH_CONF_ID = null;
	private String DESCRIPTION = null;
	
	/**
	 * @return the tYPE
	 */
	public String getTYPE() {
		return TYPE;
	}
	/**
	 * @param type the tYPE to set
	 */
	public void setTYPE(String type) {
		TYPE = type;
	}
	/**
	 * @return the pRINTER_NAME
	 */
	public String getPRINTER_NAME() {
		return PRINTER_NAME;
	}
	/**
	 * @param printer_name the pRINTER_NAME to set
	 */
	public void setPRINTER_NAME(String printer_name) {
		PRINTER_NAME = printer_name;
	}
	/**
	 * @return the sPOOL_JOB_NAME
	 */
	public String getSPOOL_JOB_NAME() {
		return SPOOL_JOB_NAME;
	}
	/**
	 * @param spool_job_name the sPOOL_JOB_NAME to set
	 */
	public void setSPOOL_JOB_NAME(String spool_job_name) {
		SPOOL_JOB_NAME = spool_job_name;
	}
	/**
	 * @return the jOB_STATUS
	 */
	public String getJOB_STATUS() {
		return JOB_STATUS;
	}
	/**
	 * @param job_status the jOB_STATUS to set
	 */
	public void setJOB_STATUS(String job_status) {
		JOB_STATUS = job_status;
	}
	/**
	 * @return the jOB_CODE
	 */
	public String getJOB_CODE() {
		return JOB_CODE;
	}
	/**
	 * @param job_code the jOB_CODE to set
	 */
	public void setJOB_CODE(String job_code) {
		JOB_CODE = job_code;
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
	    
	    retValue = "MS_BOTTOM_LINE_ASYNC_IN ( "
	        + super.toString() + TAB
	        + "TYPE = " + this.TYPE + TAB
	        + "PRINTER_NAME = " + this.PRINTER_NAME + TAB
	        + "SPOOL_JOB_NAME = " + this.SPOOL_JOB_NAME + TAB
	        + "JOB_STATUS = " + this.JOB_STATUS + TAB
	        + "JOB_CODE = " + this.JOB_CODE + TAB
	        + "TXN_BATCH_CONF_ID = " + this.TXN_BATCH_CONF_ID + TAB
	        + "DESCRIPTION = " + this.DESCRIPTION + TAB
	        + " )";
	
	    return retValue;
	}

}