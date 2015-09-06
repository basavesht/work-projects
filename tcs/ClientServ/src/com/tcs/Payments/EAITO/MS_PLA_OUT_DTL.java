/**
 * 
 */
package com.tcs.Payments.EAITO;

import java.math.BigDecimal;

/**
 * @author 259245
 *
 */
public class MS_PLA_OUT_DTL implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4413309005596713067L;
	private String PLA_NO = null;
	private String ACNT_CLIENT_NAME = null;
	private BigDecimal OUTSTANDING_BAL =null;
	private COLLATERAL_ACCT_INFO[] MS_COLLATERAL_ACCT_INFO = new COLLATERAL_ACCT_INFO[50];

	public class COLLATERAL_ACCT_INFO implements java.io.Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 358452821000220959L;
		String OFFICE;
		String ACCT;

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
		 * @return the aCCT
		 */
		public String getACCT() {
			return ACCT;
		}

		/**
		 * @param acct the aCCT to set
		 */
		public void setACCT(String acct) {
			ACCT = acct;
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

			retValue = "COLLATERAL_ACCT_INFO ( "
				+ super.toString() + TAB
				+ "OFFICE = " + this.OFFICE + TAB
				+ "ACCT = " + this.ACCT + TAB
				+ " )";

			return retValue;
		}

	}

	/**
	 * @return the pLA_NO
	 */
	public String getPLA_NO() {
		return PLA_NO;
	}

	/**
	 * @param pla_no the pLA_NO to set
	 */
	public void setPLA_NO(String pla_no) {
		PLA_NO = pla_no;
	}

	/**
	 * @return the oUTSTANDING_BAL
	 */
	public BigDecimal getOUTSTANDING_BAL() {
		return OUTSTANDING_BAL;
	}

	/**
	 * @param outstanding_bal the oUTSTANDING_BAL to set
	 */
	public void setOUTSTANDING_BAL(BigDecimal outstanding_bal) {
		OUTSTANDING_BAL = outstanding_bal;
	}

	/**
	 * @return the mS_COLLATERAL_ACCT_INFO
	 */
	public COLLATERAL_ACCT_INFO[] getMS_COLLATERAL_ACCT_INFO() {
		return MS_COLLATERAL_ACCT_INFO;
	}

	/**
	 * @param ms_collateral_acct_info the mS_COLLATERAL_ACCT_INFO to set
	 */
	public void setMS_COLLATERAL_ACCT_INFO(
			COLLATERAL_ACCT_INFO[] ms_collateral_acct_info) {
		MS_COLLATERAL_ACCT_INFO = ms_collateral_acct_info;
	}

	/**
	 * @return the aCNT_CLIENT_NAME
	 */
	public String getACNT_CLIENT_NAME() {
		return ACNT_CLIENT_NAME;
	}

	/**
	 * @param acnt_client_name the aCNT_CLIENT_NAME to set
	 */
	public void setACNT_CLIENT_NAME(String acnt_client_name) {
		ACNT_CLIENT_NAME = acnt_client_name;
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

		retValue = "MS_PLA_OUT_DTL ( "
			+ super.toString() + TAB
			+ "PLA_NO = " + this.PLA_NO + TAB
			+ "ACNT_CLIENT_NAME = " + this.ACNT_CLIENT_NAME + TAB
			+ "OUTSTANDING_BAL = " + this.OUTSTANDING_BAL + TAB
			+ "MS_COLLATERAL_ACCT_INFO = " + this.MS_COLLATERAL_ACCT_INFO + TAB
			+ " )";

		return retValue;
	}


}
