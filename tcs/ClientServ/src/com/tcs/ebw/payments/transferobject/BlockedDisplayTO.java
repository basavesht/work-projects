package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BlockedDisplayTO extends EBWTransferObject implements java.io.Serializable{

	private String office = null;
	private String accountNumber = null;
	private String fa = null;
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getFa() {
		return fa;
	}
	public void setFa(String fa) {
		this.fa = fa;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
}