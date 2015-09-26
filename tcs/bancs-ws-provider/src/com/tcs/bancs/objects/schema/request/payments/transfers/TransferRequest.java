package com.tcs.bancs.objects.schema.request.payments.transfers;

import java.io.Serializable;

/**
 * @author 259245
 *
 */

public class TransferRequest implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6005235464967325484L;
	private PaymentRequest paymentRequest = null;
	private UserDetails userDetails = null;

	/**
	 * @return the paymentRequest
	 */
	public PaymentRequest getPaymentRequest() {
		return paymentRequest;
	}
	/**
	 * @param paymentRequest the paymentRequest to set
	 */
	public void setPaymentRequest(PaymentRequest paymentRequest) {
		this.paymentRequest = paymentRequest;
	}
	/**
	 * @return the userDetails
	 */
	public UserDetails getUserDetails() {
		return userDetails;
	}
	/**
	 * @param userDetails the userDetails to set
	 */
	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
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

		retValue = "TransferRequest ( "
				+ super.toString() + TAB
				+ "paymentRequest = " + this.paymentRequest + TAB
				+ "userDetails = " + this.userDetails + TAB
				+ " )";

		return retValue;
	}


}
