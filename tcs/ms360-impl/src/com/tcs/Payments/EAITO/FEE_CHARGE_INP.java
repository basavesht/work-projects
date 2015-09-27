package com.tcs.Payments.EAITO;

import java.math.BigDecimal;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class FEE_CHARGE_INP implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -408225317536572639L;
	public String TXN_TYPE = null;
	public String TIER = null;
	public String CHK_PICK_UP_OPTION = null;
	public String CHK_CERTIFIED_FLAG = null;
	public BigDecimal TXN_AMOUNT = null;
	
	public String getTXN_TYPE() {
		return TXN_TYPE;
	}
	public void setTXN_TYPE(String txn_type) {
		TXN_TYPE = txn_type;
	}
	public String getTIER() {
		return TIER;
	}
	public void setTIER(String tier) {
		TIER = tier;
	}
	public String getCHK_PICK_UP_OPTION() {
		return CHK_PICK_UP_OPTION;
	}
	public void setCHK_PICK_UP_OPTION(String chk_pick_up_option) {
		CHK_PICK_UP_OPTION = chk_pick_up_option;
	}
	public String getCHK_CERTIFIED_FLAG() {
		return CHK_CERTIFIED_FLAG;
	}
	public void setCHK_CERTIFIED_FLAG(String chk_certified_flag) {
		CHK_CERTIFIED_FLAG = chk_certified_flag;
	}
	public BigDecimal getTXN_AMOUNT() {
		return TXN_AMOUNT;
	}
	public void setTXN_AMOUNT(BigDecimal txn_amount) {
		TXN_AMOUNT = txn_amount;
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
	    
	    retValue = "CAL_FEE_INP ( "
	        + super.toString() + TAB
	        + "TXN_TYPE = " + this.TXN_TYPE + TAB
	        + "TIER = " + this.TIER + TAB
	        + "CHK_PICK_UP_OPTION = " + this.CHK_PICK_UP_OPTION + TAB
	        + "CHK_CERTIFIED_FLAG = " + this.CHK_CERTIFIED_FLAG + TAB
	        + "TXN_AMOUNT = " + this.TXN_AMOUNT + TAB
	        + " )";
	
	    return retValue;
	}

}
