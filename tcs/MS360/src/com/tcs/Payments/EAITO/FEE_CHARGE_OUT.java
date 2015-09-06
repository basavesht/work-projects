package com.tcs.Payments.EAITO;

import java.math.BigDecimal;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class FEE_CHARGE_OUT implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8668331700461708959L;
	public BigDecimal CHARGED_AMNT = null;

	public BigDecimal getCHARGED_AMNT() {
		return CHARGED_AMNT;
	}

	public void setCHARGED_AMNT(BigDecimal charged_amnt) {
		CHARGED_AMNT = charged_amnt;
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

		retValue = "CAL_FEE_OUT ( "
			+ super.toString() + TAB
			+ "CHARGED_AMNT = " + this.CHARGED_AMNT + TAB
			+ " )";

		return retValue;
	}

}
