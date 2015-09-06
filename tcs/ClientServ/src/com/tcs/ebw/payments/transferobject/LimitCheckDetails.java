package com.tcs.ebw.payments.transferobject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class LimitCheckDetails {

	private boolean isChildTxnCreated = false;
	private Object childTxnDetailObj = null;

	public boolean isChildTxnCreated() {
		return isChildTxnCreated;
	}
	public void setChildTxnCreated(boolean isChildTxnCreated) {
		this.isChildTxnCreated = isChildTxnCreated;
	}
	public Object getChildTxnDetailObj() {
		return childTxnDetailObj;
	}
	public void setChildTxnDetailObj(Object childTxnDetailObj) {
		this.childTxnDetailObj = childTxnDetailObj;
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

		retValue = "LimitCheckDetails ( "
			+ super.toString() + TAB
			+ "isChildTxnCreated = " + this.isChildTxnCreated + TAB
			+ "childTxnDetailObj = " + this.childTxnDetailObj + TAB
			+ " )";

		return retValue;
	}
}
