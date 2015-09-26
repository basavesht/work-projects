/**
 * 
 */
package com.tcs.bancs.objects.schema.response.payments.transfers;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 259245
 *
 */
public class PaymentResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1184866968131887868L;
	private ServiceContext serviceContext =null;
	private ArrayList<AccountResponse> accountResponse = new ArrayList<AccountResponse>();
	private String confirmationNo = null;
	private Integer paymentStatus = null;

	/**
	 * @return the serviceContext
	 */
	public ServiceContext getServiceContext() {
		return serviceContext;
	}
	/**
	 * @param serviceContext the serviceContext to set
	 */
	public void setServiceContext(ServiceContext serviceContext) {
		this.serviceContext = serviceContext;
	}
	/**
	 * @return the account
	 */
	public ArrayList<AccountResponse> getAccountResponse() {
		return accountResponse;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccountResponse(ArrayList<AccountResponse> account) {
		this.accountResponse = account;
	}
	/**
	 * @return the confirmationNo
	 */
	public String getConfirmationNo() {
		return confirmationNo;
	}
	/**
	 * @param confirmationNo the confirmationNo to set
	 */
	public void setConfirmationNo(String confirmationNo) {
		this.confirmationNo = confirmationNo;
	}
	/**
	 * @return the paymentStatus
	 */
	public Integer getPaymentStatus() {
		return paymentStatus;
	}
	/**
	 * @param paymentStatus the paymentStatus to set
	 */
	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
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

		retValue = "PaymentResponse ( "
				+ super.toString() + TAB
				+ "serviceContext = " + this.serviceContext + TAB
				+ "account = " + this.accountResponse + TAB
				+ "confirmationNo = " + this.confirmationNo + TAB
				+ "paymentStatus = " + this.paymentStatus + TAB
				+ " )";

		return retValue;
	}

}
