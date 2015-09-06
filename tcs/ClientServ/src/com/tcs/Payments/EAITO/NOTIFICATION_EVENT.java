package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class NOTIFICATION_EVENT implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3860582263128628338L;
	private String EVENT_DESC = null;

	public String getEVENT_DESC() {
		return EVENT_DESC;
	}

	public void setEVENT_DESC(String event_desc) {
		EVENT_DESC = event_desc;
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

		retValue = "NOTIFICATION_EVENT ( "
			+ super.toString() + TAB
			+ "EVENT_DESC = " + this.EVENT_DESC + TAB
			+ " )";

		return retValue;
	}
}
