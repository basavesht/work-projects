package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_PAYMENT_OUT_DTL implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -652403458237221025L;
	private String POS_NUM;
	private String POS_TYP;
	private String PAYMENT_STATUS;
	
	public String getPOS_NUM() {
		return POS_NUM;
	}
	public void setPOS_NUM(String pos_num) {
		POS_NUM = pos_num;
	}
	public String getPOS_TYP() {
		return POS_TYP;
	}
	public void setPOS_TYP(String pos_typ) {
		POS_TYP = pos_typ;
	}
	public String getPAYMENT_STATUS() {
		return PAYMENT_STATUS;
	}
	public void setPAYMENT_STATUS(String payment_status) {
		PAYMENT_STATUS = payment_status;
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
	    
	    retValue = "MS_PAYMENT_OUT_DTL ( "
	        + super.toString() + TAB
	        + "POS_NUM = " + this.POS_NUM + TAB
	        + "POS_TYP = " + this.POS_TYP + TAB
	        + "PAYMENT_STATUS = " + this.PAYMENT_STATUS + TAB
	        + " )";
	
	    return retValue;
	}
	
}
