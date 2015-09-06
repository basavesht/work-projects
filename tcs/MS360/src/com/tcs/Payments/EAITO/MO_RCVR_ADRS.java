package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MO_RCVR_ADRS implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9073823553432510362L;
	private String RMRKS = null;
	private String DISPATCH_ADDR = null;

	public String getRMRKS() {
		return RMRKS;
	}
	public void setRMRKS(String rmrks) {
		RMRKS = rmrks;
	}
	public String getDISPATCH_ADDR() {
		return DISPATCH_ADDR;
	}
	public void setDISPATCH_ADDR(String dispatch_addr) {
		DISPATCH_ADDR = dispatch_addr;
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

		retValue = "MO_RCVR_ADRS ( "
			+ super.toString() + TAB
			+ "RMRKS = " + this.RMRKS + TAB
			+ "DISPATCH_ADDR = " + this.DISPATCH_ADDR + TAB
			+ " )";

		return retValue;
	}
}
