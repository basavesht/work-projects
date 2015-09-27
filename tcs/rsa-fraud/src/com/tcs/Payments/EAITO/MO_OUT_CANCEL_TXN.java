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
public class MO_OUT_CANCEL_TXN implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9016126799662179524L;
	private Integer RETURN_FLG = null;
	private String PAYPERF = null;
	private String EXT_STR1 = null;
	private Integer EXT_NUM1 = null;

	/**
	 * @return the rETURN_FLG
	 */
	public Integer getRETURN_FLG() {
		return RETURN_FLG;
	}
	/**
	 * @param return_flg the rETURN_FLG to set
	 */
	public void setRETURN_FLG(Integer return_flg) {
		RETURN_FLG = return_flg;
	}
	/**
	 * @return the pAYPERF
	 */
	public String getPAYPERF() {
		return PAYPERF;
	}
	/**
	 * @param payperf the pAYPERF to set
	 */
	public void setPAYPERF(String payperf) {
		PAYPERF = payperf;
	}
	/**
	 * @return the eXT_STR1
	 */
	public String getEXT_STR1() {
		return EXT_STR1;
	}
	/**
	 * @param ext_str1 the eXT_STR1 to set
	 */
	public void setEXT_STR1(String ext_str1) {
		EXT_STR1 = ext_str1;
	}
	/**
	 * @return the eXT_NUM1
	 */
	public Integer getEXT_NUM1() {
		return EXT_NUM1;
	}
	/**
	 * @param ext_num1 the eXT_NUM1 to set
	 */
	public void setEXT_NUM1(Integer ext_num1) {
		EXT_NUM1 = ext_num1;
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

		retValue = "MO_OUT_CANCEL_TXN ( "
			+ super.toString() + TAB
			+ "RETURN_FLG = " + this.RETURN_FLG + TAB
			+ "PAYPERF = " + this.PAYPERF + TAB
			+ "EXT_STR1 = " + this.EXT_STR1 + TAB
			+ "EXT_NUM1 = " + this.EXT_NUM1 + TAB
			+ " )";

		return retValue;
	}


}
