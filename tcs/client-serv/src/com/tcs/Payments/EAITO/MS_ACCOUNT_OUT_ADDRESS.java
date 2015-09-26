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
public class MS_ACCOUNT_OUT_ADDRESS implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8875999818393985756L;
	private String OFFICE = null;
	private String ACCOUNT_NO = null;

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
	 * @return the aCCOUNT_NO
	 */
	public String getACCOUNT_NO() {
		return ACCOUNT_NO;
	}
	/**
	 * @param account_no the aCCOUNT_NO to set
	 */
	public void setACCOUNT_NO(String account_no) {
		ACCOUNT_NO = account_no;
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
		retValue = "MS_ACCOUNT_OUT_ADDRESS ( "
			+ super.toString() + TAB
			+ "OFFICE = " + this.OFFICE + TAB
			+ "ACCOUNT_NO = " + this.ACCOUNT_NO + TAB
			+ " )";

		return retValue;
	}

}
