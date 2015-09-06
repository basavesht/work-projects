/**
 * 
 */
package com.tcs.Payments.EAITO;

/**
 * @author 224703
 *
 */
public class MS_INTERFACE_TECH_FAILURE implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9003695641890605524L;
	private String YES_NO_FLG;
	private String ERROR_DESC1;
	private String ERROR_DESC2;
	private int ERROR_TYPE;
	private int ERROR_ID;
	private int ERROR_SEVERITY;
	private String ERROR_SHRT_DESC;
	private String ERROR_LNG_DESC;
	
	public String getYES_NO_FLG() {
		return YES_NO_FLG;
	}
	public void setYES_NO_FLG(String yes_no_flg) {
		YES_NO_FLG = yes_no_flg;
	}

	public String getERROR_DESC1() {
		return ERROR_DESC1;
	}
	public void setERROR_DESC1(String error_desc1) {
		ERROR_DESC1 = error_desc1;
	}
	public String getERROR_DESC2() {
		return ERROR_DESC2;
	}
	public void setERROR_DESC2(String error_desc2) {
		ERROR_DESC2 = error_desc2;
	}
	public int getERROR_TYPE() {
		return ERROR_TYPE;
	}
	public void setERROR_TYPE(int error_type) {
		ERROR_TYPE = error_type;
	}
	public int getERROR_ID() {
		return ERROR_ID;
	}
	public void setERROR_ID(int error_id) {
		ERROR_ID = error_id;
	}
	public int getERROR_SEVERITY() {
		return ERROR_SEVERITY;
	}
	public void setERROR_SEVERITY(int error_severity) {
		ERROR_SEVERITY = error_severity;
	}
	public String getERROR_SHRT_DESC() {
		return ERROR_SHRT_DESC;
	}
	public void setERROR_SHRT_DESC(String error_shrt_desc) {
		ERROR_SHRT_DESC = error_shrt_desc;
	}
	public String getERROR_LNG_DESC() {
		return ERROR_LNG_DESC;
	}
	public void setERROR_LNG_DESC(String error_lng_desc) {
		ERROR_LNG_DESC = error_lng_desc;
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
	    
	    retValue = "MS_INTERFACE_TECH_FAILURE ( "
	        + super.toString() + TAB
	        + "YES_NO_FLG = " + this.YES_NO_FLG + TAB
	        + "ERROR_DESC1 = " + this.ERROR_DESC1 + TAB
	        + "ERROR_DESC2 = " + this.ERROR_DESC2 + TAB
	        + "ERROR_TYPE = " + this.ERROR_TYPE + TAB
	        + "ERROR_ID = " + this.ERROR_ID + TAB
	        + "ERROR_SEVERITY = " + this.ERROR_SEVERITY + TAB
	        + "ERROR_SHRT_DESC = " + this.ERROR_SHRT_DESC + TAB
	        + "ERROR_LNG_DESC = " + this.ERROR_LNG_DESC + TAB
	        + " )";
	
	    return retValue;
	}



}
