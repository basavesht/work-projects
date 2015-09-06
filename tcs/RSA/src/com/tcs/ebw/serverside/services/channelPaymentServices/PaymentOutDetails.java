package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import com.tcs.Payments.EAITO.MO_OUT_CANCEL_TXN;
import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.Payments.EAITO.MS_PAYMENT_OUT_DTL;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.mswitch.common.channel.SI_RETURN;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class PaymentOutDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4429979737612034869L;
	private String payConfirmationNumber;
	private String paymentStatus;

	public String getPayConfirmationNumber() {
		return payConfirmationNumber;
	}
	public void setPayConfirmationNumber(String payConfirmationNumber) {
		this.payConfirmationNumber = payConfirmationNumber;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	/**
	 * Payment call response extraction code...
	 * @param out
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static ArrayList getPaymentOutDetails(Vector out,ServiceContext context) throws Exception 
	{
		EBWLogger.logDebug("PaymentOutDetails","SI Returned Vector for PaymentsHub(Remittance or DDRemittance Service");
		ArrayList paymentReturnConf = new ArrayList();
		try 
		{
			//Interface Output attributes...
			MS_PAYMENT_OUT_DTL objMS_PAYMENT_OUT_DTL = new MS_PAYMENT_OUT_DTL();

			//Output Extraction and storage...
			SI_RETURN si_return = (SI_RETURN)out.get(0);
			if(si_return.getReturnCode() == 0){
				for(int k=0;k<si_return.getOutputVector().size();k++)
				{
					if(si_return.getOutputVector().get(k) instanceof MS_PAYMENT_OUT_DTL )
					{
						PaymentOutDetails objPayOutDetails = new PaymentOutDetails();
						objMS_PAYMENT_OUT_DTL = (MS_PAYMENT_OUT_DTL)si_return.getOutputVector().get(k);

						//Set the output details in the PaymentOut Details Object ....
						objPayOutDetails.setPayConfirmationNumber(objMS_PAYMENT_OUT_DTL.getPOS_TYP()+"-"+objMS_PAYMENT_OUT_DTL.getPOS_NUM());
						objPayOutDetails.setPaymentStatus(objMS_PAYMENT_OUT_DTL.getPAYMENT_STATUS());
						paymentReturnConf.add(objPayOutDetails);
						EBWLogger.logDebug("PaymentOutDetails","SI Returned Vector for PaymentsHub(Remittance or DDRemittance Service is:"+objPayOutDetails.toString());
					}
				}
				context.setRTACommitReq(true); // Following value is set in the ServiceContext , to handle the RTA Commit in the EJB Bean class.
			}
			else
			{
				for(int k=0;k<si_return.getErrorVector().size();k++){
					if(si_return.getErrorVector().get(k) instanceof MS_INTERFACE_TECH_FAILURE ){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
						break;
					}
				}
				context.setRTARollbackReq(true);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return paymentReturnConf;
	}

	/**
	 * Payment call response extraction code on cancellation event...
	 * @param out
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static ArrayList getCancelPaymentOutDetails(Vector out,ServiceContext context) throws Exception 
	{
		EBWLogger.logDebug("PaymentOutDetails","SI Returned Vector for PaymentsHub(Remittance or DDRemittance Service");
		ArrayList paymentReturnConf = new ArrayList();
		try 
		{
			//Interface Output attributes...
			MO_OUT_CANCEL_TXN objMO_OUT_CANCEL_TXN = new MO_OUT_CANCEL_TXN();

			//Output Extraction and storage...
			SI_RETURN si_return = (SI_RETURN)out.get(0);
			if(si_return.getReturnCode() == 0){
				for(int k=0;k<si_return.getOutputVector().size();k++)
				{
					if(si_return.getOutputVector().get(k) instanceof MO_OUT_CANCEL_TXN ){
						PaymentOutDetails objPayOutDetails = new PaymentOutDetails();
						objMO_OUT_CANCEL_TXN = (MO_OUT_CANCEL_TXN)si_return.getOutputVector().get(k);

						//Checking Return Flag ...
						Integer ret_status = objMO_OUT_CANCEL_TXN.getRETURN_FLG();
						if(ret_status!=null && !ret_status.equals(new Integer(0))){
							context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
							break;
						}

						//Extracting and setting the output details in the PaymentOut Details Object ....
						objPayOutDetails.setPaymentStatus(ConvertionUtil.convertToString(objMO_OUT_CANCEL_TXN.getRETURN_FLG()));
						paymentReturnConf.add(objPayOutDetails);
						EBWLogger.logDebug("PaymentOutDetails","SI Returned Vector for PaymentsHub(Remittance or DDRemittance Service is:"+objPayOutDetails.toString());
					}
				}
			}
			else {
				for(int k=0;k<si_return.getErrorVector().size();k++){
					if(si_return.getErrorVector().get(k) instanceof MS_INTERFACE_TECH_FAILURE ){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
						break;
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return paymentReturnConf;
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

		retValue = "PaymentOutDetails ( "
			+ super.toString() + TAB
			+ "payConfirmationNumber = " + this.payConfirmationNumber + TAB
			+ "paymentStatus = " + this.paymentStatus + TAB
			+ " )";

		return retValue;
	}
}
