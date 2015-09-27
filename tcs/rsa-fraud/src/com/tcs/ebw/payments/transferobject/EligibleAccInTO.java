package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class EligibleAccInTO extends EBWTransferObject implements java.io.Serializable 
{
	private static final long serialVersionUID = -3945394081261439995L;
	private String paydebitaccnum = null;
	private String paypayeeaccnum = null;
	private String keyaccountnumber_from = null;
	private String keyaccountnumber_to = null;
	private String life_user_id = null;
	private String paypayref = null;

	public String getPaydebitaccnum() {
		return paydebitaccnum;
	}
	public void setPaydebitaccnum(String paydebitaccnum) {
		this.paydebitaccnum = paydebitaccnum;
	}
	public String getPaypayeeaccnum() {
		return paypayeeaccnum;
	}
	public void setPaypayeeaccnum(String paypayeeaccnum) {
		this.paypayeeaccnum = paypayeeaccnum;
	}
	public String getLife_user_id() {
		return life_user_id;
	}
	public void setLife_user_id(String life_user_id) {
		this.life_user_id = life_user_id;
	}
	public String getPaypayref() {
		return paypayref;
	}
	public void setPaypayref(String paypayref) {
		this.paypayref = paypayref;
	}
	public String getKeyaccountnumber_from() {
		return keyaccountnumber_from;
	}
	public void setKeyaccountnumber_from(String keyaccountnumber_from) {
		this.keyaccountnumber_from = keyaccountnumber_from;
	}
	public String getKeyaccountnumber_to() {
		return keyaccountnumber_to;
	}
	public void setKeyaccountnumber_to(String keyaccountnumber_to) {
		this.keyaccountnumber_to = keyaccountnumber_to;
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

		retValue = "EligibleAccInTO ( "
			+ super.toString() + TAB
			+ "paydebitaccnum = " + this.paydebitaccnum + TAB
			+ "paypayeeaccnum = " + this.paypayeeaccnum + TAB
			+ "keyaccountnumber_from = " + this.keyaccountnumber_from + TAB
			+ "keyaccountnumber_to = " + this.keyaccountnumber_to + TAB
			+ "life_user_id = " + this.life_user_id + TAB
			+ "paypayref = " + this.paypayref + TAB
			+ " )";

		return retValue;
	}

}
