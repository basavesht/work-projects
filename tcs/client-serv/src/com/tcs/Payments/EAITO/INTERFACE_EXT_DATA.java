package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class INTERFACE_EXT_DATA implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7826595416429221449L;
	private Double ORIG_TXN_AMNT = null;
	private String prev_Txnstatus = null;

	/**
	 * @return the prev_Txnstatus
	 */
	public String getPrev_Txnstatus() {
		return prev_Txnstatus;
	}

	/**
	 * @param prev_Txnstatus the prev_Txnstatus to set
	 */
	public void setPrev_Txnstatus(String prev_Txnstatus) {
		this.prev_Txnstatus = prev_Txnstatus;
	}

	public Double getORIG_TXN_AMNT() {
		return ORIG_TXN_AMNT;
	}

	public void setORIG_TXN_AMNT(Double orig_txn_amnt) {
		ORIG_TXN_AMNT = orig_txn_amnt;
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
	    
	    retValue = "INTERFACE_EXT_DATA ( "
	        + super.toString() + TAB
	        + "ORIG_TXN_AMNT = " + this.ORIG_TXN_AMNT + TAB
	        + "prev_Txnstatus = " + this.prev_Txnstatus + TAB
	        + " )";
	
	    return retValue;
	}

}
