/**
 * 
 */
package com.tcs.bancs.objects.schema.response.payments.transfers;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 259245
 *
 */
public class AcntBalance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 84032459897823309L;
	private String Office = null;
	private String fa = null;
	private String account= null;
	private String keyAccount = null;
	private BigDecimal availSpndPower = null;
	private BigDecimal cashAvail = null;
	private BigDecimal cashAvailablePend = null;
	private BigDecimal marginAvail = null;
	private BigDecimal marginPendTxn = null;
	private BigDecimal delayedDebitCardTxn = null;

	/**
	 * @return the office
	 */
	public String getOffice() {
		return Office;
	}
	/**
	 * @param office the office to set
	 */
	public void setOffice(String office) {
		Office = office;
	}
	/**
	 * @return the fa
	 */
	public String getFa() {
		return fa;
	}
	/**
	 * @param fa the fa to set
	 */
	public void setFa(String fa) {
		this.fa = fa;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the keyAccount
	 */
	public String getKeyAccount() {
		return keyAccount;
	}
	/**
	 * @param keyAccount the keyAccount to set
	 */
	public void setKeyAccount(String keyAccount) {
		this.keyAccount = keyAccount;
	}
	/**
	 * @return the availSpndPower
	 */
	public BigDecimal getAvailSpndPower() {
		return availSpndPower;
	}
	/**
	 * @param availSpndPower the availSpndPower to set
	 */
	public void setAvailSpndPower(BigDecimal availSpndPower) {
		this.availSpndPower = availSpndPower;
	}
	/**
	 * @return the cashAvail
	 */
	public BigDecimal getCashAvail() {
		return cashAvail;
	}
	/**
	 * @param cashAvail the cashAvail to set
	 */
	public void setCashAvail(BigDecimal cashAvail) {
		this.cashAvail = cashAvail;
	}
	/**
	 * @return the cashAvailablePend
	 */
	public BigDecimal getCashAvailablePend() {
		return cashAvailablePend;
	}
	/**
	 * @param cashAvailablePend the cashAvailablePend to set
	 */
	public void setCashAvailablePend(BigDecimal cashAvailablePend) {
		this.cashAvailablePend = cashAvailablePend;
	}
	/**
	 * @return the marginAvail
	 */
	public BigDecimal getMarginAvail() {
		return marginAvail;
	}
	/**
	 * @param marginAvail the marginAvail to set
	 */
	public void setMarginAvail(BigDecimal marginAvail) {
		this.marginAvail = marginAvail;
	}
	/**
	 * @return the marginPendTxn
	 */
	public BigDecimal getMarginPendTxn() {
		return marginPendTxn;
	}
	/**
	 * @param marginPendTxn the marginPendTxn to set
	 */
	public void setMarginPendTxn(BigDecimal marginPendTxn) {
		this.marginPendTxn = marginPendTxn;
	}
	/**
	 * @return the delayedDebitCardTxn
	 */
	public BigDecimal getDelayedDebitCardTxn() {
		return delayedDebitCardTxn;
	}
	/**
	 * @param delayedDebitCardTxn the delayedDebitCardTxn to set
	 */
	public void setDelayedDebitCardTxn(BigDecimal delayedDebitCardTxn) {
		this.delayedDebitCardTxn = delayedDebitCardTxn;
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

		retValue = "AcntBalance ( "
				+ super.toString() + TAB
				+ "Office = " + this.Office + TAB
				+ "fa = " + this.fa + TAB
				+ "account = " + this.account + TAB
				+ "keyAccount = " + this.keyAccount + TAB
				+ "availSpndPower = " + this.availSpndPower + TAB
				+ "cashAvail = " + this.cashAvail + TAB
				+ "cashAvailablePend = " + this.cashAvailablePend + TAB
				+ "marginAvail = " + this.marginAvail + TAB
				+ "marginPendTxn = " + this.marginPendTxn + TAB
				+ "delayedDebitCardTxn = " + this.delayedDebitCardTxn + TAB
				+ " )";

		return retValue;
	}


}
