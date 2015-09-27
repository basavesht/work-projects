package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BUSINESS_RULE_OUT_DTL implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7834976999578866741L;
	private String RULE_TYPE=null;
	private String 	USR_GRP=null;
	private Integer PRIORITY=null;
	private String 	ERR_LVL=null;
	private Boolean LOGOUT=null;
	private int ID;
	private String 	ERRORID =null;
	private String 	ERRORMSG = null;
	private int CANCEL_TXN_FLG;
	private String ACC_IN_ELIGIBLE;

	public String getERR_LVL() {
		return ERR_LVL;
	}
	public void setERR_LVL(String err_lvl) {
		ERR_LVL = err_lvl;
	}
	public String getERRORID() {
		return ERRORID;
	}
	public void setERRORID(String errorid) {
		ERRORID = errorid;
	}
	public String getERRORMSG() {
		return ERRORMSG;
	}
	public void setERRORMSG(String errormsg) {
		ERRORMSG = errormsg;
	}
	public int getID() {
		return ID;
	}
	public void setID(int id) {
		ID = id;
	}
	public Boolean getLOGOUT() {
		return LOGOUT;
	}
	public void setLOGOUT(Boolean logout) {
		LOGOUT = logout;
	}
	public Integer getPRIORITY() {
		return PRIORITY;
	}
	public void setPRIORITY(Integer priority) {
		PRIORITY = priority;
	}
	public String getRULE_TYPE() {
		return RULE_TYPE;
	}
	public void setRULE_TYPE(String rule_type) {
		RULE_TYPE = rule_type;
	}
	public String getUSR_GRP() {
		return USR_GRP;
	}
	public void setUSR_GRP(String usr_grp) {
		USR_GRP = usr_grp;
	}
	public int getCANCEL_TXN_FLG() {
		return CANCEL_TXN_FLG;
	}
	public void setCANCEL_TXN_FLG(int cancel_txn_flg) {
		CANCEL_TXN_FLG = cancel_txn_flg;
	}
	public String getACC_IN_ELIGIBLE() {
		return ACC_IN_ELIGIBLE;
	}
	public void setACC_IN_ELIGIBLE(String acc_in_eligible) {
		ACC_IN_ELIGIBLE = acc_in_eligible;
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
	    
	    retValue = "BUSINESS_RULE_OUT_DTL ( "
	        + super.toString() + TAB
	        + "RULE_TYPE = " + this.RULE_TYPE + TAB
	        + "USR_GRP = " + this.USR_GRP + TAB
	        + "PRIORITY = " + this.PRIORITY + TAB
	        + "ERR_LVL = " + this.ERR_LVL + TAB
	        + "LOGOUT = " + this.LOGOUT + TAB
	        + "ID = " + this.ID + TAB
	        + "ERRORID = " + this.ERRORID + TAB
	        + "ERRORMSG = " + this.ERRORMSG + TAB
	        + "CANCEL_TXN_FLG = " + this.CANCEL_TXN_FLG + TAB
	        + "ACC_IN_ELIGIBLE = " + this.ACC_IN_ELIGIBLE + TAB
	        + " )";
	
	    return retValue;
	}

}
