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
public class MO_INP_CANCEL_TXN implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2273938345288637627L;
	private Integer PYMNT_TYPE = null;
	private String PYMNT_POS_NUM = null;
	private String TXN_TYPE = null;
	private String PAYPERF = null;
	private String EXT_STR1 = null;
	private Integer EXT_NUM1 = null;

	/**
	 * @return the pYMNT_TYPE
	 */
	public Integer getPYMNT_TYPE() {
		return PYMNT_TYPE;
	}
	/**
	 * @param pymnt_type the pYMNT_TYPE to set
	 */
	public void setPYMNT_TYPE(Integer pymnt_type) {
		PYMNT_TYPE = pymnt_type;
	}
	/**
	 * @return the pYMNT_POS_NUM
	 */
	public String getPYMNT_POS_NUM() {
		return PYMNT_POS_NUM;
	}
	/**
	 * @param pymnt_pos_num the pYMNT_POS_NUM to set
	 */
	public void setPYMNT_POS_NUM(String pymnt_pos_num) {
		PYMNT_POS_NUM = pymnt_pos_num;
	}
	/**
	 * @return the tXN_TYPE
	 */
	public String getTXN_TYPE() {
		return TXN_TYPE;
	}
	/**
	 * @param txn_type the tXN_TYPE to set
	 */
	public void setTXN_TYPE(String txn_type) {
		TXN_TYPE = txn_type;
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
		final String TAB = "    ";

		String retValue = "";

		retValue = "MO_INP_CANCEL_TXN ( "
			+ super.toString() + TAB
			+ "PYMNT_TYPE = " + this.PYMNT_TYPE + TAB
			+ "PYMNT_POS_NUM = " + this.PYMNT_POS_NUM + TAB
			+ "TXN_TYPE = " + this.TXN_TYPE + TAB
			+ "PAYPERF = " + this.PAYPERF + TAB
			+ "EXT_STR1 = " + this.EXT_STR1 + TAB
			+ "EXT_NUM1 = " + this.EXT_NUM1 + TAB
			+ " )";

		return retValue;
	}

}
