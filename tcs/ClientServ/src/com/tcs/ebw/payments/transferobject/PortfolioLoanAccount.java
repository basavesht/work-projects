package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.tcs.bancs.channels.integration.MMAccount;


/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class PortfolioLoanAccount implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7564014779555199767L;
	private String loanAccount = null;
	private String loanAcntClientName = null;
	private BigDecimal loanOutstandingBal = new BigDecimal(0);
	private ArrayList<MMAccount> collateralAcnt = new ArrayList<MMAccount>();

	/**
	 * @return the loanAccount
	 */
	public String getLoanAccount() {
		return loanAccount;
	}
	/**
	 * @param loanAccount the loanAccount to set
	 */
	public void setLoanAccount(String loanAccount) {
		this.loanAccount = loanAccount;
	}
	/**
	 * @return the collateralAcnt
	 */
	public ArrayList<MMAccount> getCollateralAcnt() {
		return collateralAcnt;
	}
	/**
	 * @param collateralAcnt the collateralAcnt to set
	 */
	public void setCollateralAcnt(ArrayList<MMAccount> collateralAcnt) {
		this.collateralAcnt = collateralAcnt;
	}

	/**
	 * @return the loanOutstandingBal
	 */
	public BigDecimal getLoanOutstandingBal() {
		return loanOutstandingBal;
	}
	/**
	 * @param loanOutstandingBal the loanOutstandingBal to set
	 */
	public void setLoanOutstandingBal(BigDecimal loanOutstandingBal) {
		this.loanOutstandingBal = loanOutstandingBal;
	}

	/**
	 * @return the loanAcntClientName
	 */
	public String getLoanAcntClientName() {
		return loanAcntClientName;
	}
	/**
	 * @param loanAcntClientName the loanAcntClientName to set
	 */
	public void setLoanAcntClientName(String loanAcntClientName) {
		this.loanAcntClientName = loanAcntClientName;
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

		retValue = "PortfolioLoanAccount ( "
			+ super.toString() + TAB
			+ "loanAccount = " + this.loanAccount + TAB
			+ "loanAcntClientName = " + this.loanAcntClientName + TAB
			+ "loanOutstandingBal = " + this.loanOutstandingBal + TAB
			+ "collateralAcnt = " + this.collateralAcnt + TAB
			+ " )";

		return retValue;
	}


}