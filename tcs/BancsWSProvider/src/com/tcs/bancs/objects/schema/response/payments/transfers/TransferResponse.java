/**
 * 
 */
package com.tcs.bancs.objects.schema.response.payments.transfers;

import java.io.Serializable;

/**
 * @author 259245
 *
 */

public class TransferResponse  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5774412159030999981L;
	private ResponseObject responseObject = null;
	private PaymentResponse paymentResponse = null;

	/**
	 * @return the responseObject
	 */
	public ResponseObject getResponseObject() {
		return responseObject;
	}
	/**
	 * @param responseObject the responseObject to set
	 */
	public void setResponseObject(ResponseObject responseObject) {
		this.responseObject = responseObject;
	}
	/**
	 * @return the paymentResponse
	 */
	public PaymentResponse getPaymentResponse() {
		return paymentResponse;
	}
	/**
	 * @param paymentResponse the paymentResponse to set
	 */
	public void setPaymentResponse(PaymentResponse paymentResponse) {
		this.paymentResponse = paymentResponse;
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

		retValue = "TransferResponse ( "
				+ super.toString() + TAB
				+ "responseObject = " + this.responseObject + TAB
				+ "paymentResponse = " + this.paymentResponse + TAB
				+ " )";

		return retValue;
	}
}
