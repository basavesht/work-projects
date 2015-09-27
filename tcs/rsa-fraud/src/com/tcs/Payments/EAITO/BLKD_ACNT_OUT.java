package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BLKD_ACNT_OUT implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5805708724161431298L;
	private String OFFICE;
	private String ACNTNO;
	private String FA;
	private String DR_CR;

	public String getACNTNO() {
		return ACNTNO;
	}
	public void setACNTNO(String acntno) {
		ACNTNO = acntno;
	}
	public String getDR_CR() {
		return DR_CR;
	}
	public void setDR_CR(String dr_cr) {
		DR_CR = dr_cr;
	}
	public String getFA() {
		return FA;
	}
	public void setFA(String fa) {
		FA = fa;
	}
	public String getOFFICE() {
		return OFFICE;
	}
	public void setOFFICE(String office) {
		OFFICE = office;
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
	    
	    retValue = "BLKD_ACNT_OUT ( "
	        + super.toString() + TAB
	        + "OFFICE = " + this.OFFICE + TAB
	        + "ACNTNO = " + this.ACNTNO + TAB
	        + "FA = " + this.FA + TAB
	        + "DR_CR = " + this.DR_CR + TAB
	        + " )";
	
	    return retValue;
	}

}
