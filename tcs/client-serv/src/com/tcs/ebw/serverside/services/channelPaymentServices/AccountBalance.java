package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.Payments.EAITO.MS_RTAB_ACNT_DTLS;
import com.tcs.Payments.EAITO.MS_RTAB_OUT_DTL;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.mswitch.common.channel.SI_RETURN;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class AccountBalance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 742373268344620300L;
	public HashMap infoRTAB;
	public ArrayList infoRTABObject;

	public ArrayList getInfoRTABObject() {
		return infoRTABObject;
	}
	public void setInfoRTABObject(ArrayList infoRTABObject) {
		this.infoRTABObject = infoRTABObject;
	}
	public HashMap getInfoRTAB() {
		return infoRTAB;
	}
	public void setInfoRTAB(HashMap infoRTAB) {
		this.infoRTAB = infoRTAB;
	}

	/**
	 * RTAB response extraction code...
	 * @param out
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static AccountBalance getBalance(Vector<Object> out,ServiceContext context) throws Exception 
	{
		EBWLogger.logDebug("AccountBalance","Getting the Account Balance after the RTAB Service Execution");
		AccountBalance objAccbalance = new AccountBalance();
		try 
		{
			//Interface Output attributes...
			SI_RETURN si_return = (SI_RETURN)out.get(0);
			ArrayList<Object> accBalanceOut =new ArrayList<Object>();
			MS_RTAB_OUT_DTL objMS_RTAB_OUT_DTL = new MS_RTAB_OUT_DTL();
			MS_RTAB_ACNT_DTLS objMS_RTAB_ACNT_DTLS = new MS_RTAB_ACNT_DTLS();
			HashMap<String, Comparable> rtabBalanceDetails = new HashMap<String, Comparable>();

			//Output Extraction and storage...
			if(si_return.getReturnCode() == 0)
			{
				objMS_RTAB_OUT_DTL = (MS_RTAB_OUT_DTL)si_return.getOutputVector().get(0);
				objMS_RTAB_ACNT_DTLS = (MS_RTAB_ACNT_DTLS)si_return.getOutputVector().get(1);

				// Setting the RTAB response in the HashMap...
				rtabBalanceDetails.put("OfficeAccount", objMS_RTAB_ACNT_DTLS.getOFFICE_ACCOUNT());
				rtabBalanceDetails.put("AccountNumber", objMS_RTAB_ACNT_DTLS.getACCOUNT());
				rtabBalanceDetails.put("FAID", objMS_RTAB_ACNT_DTLS.getFA_ID());
				rtabBalanceDetails.put("AvailableSpendingPower",objMS_RTAB_ACNT_DTLS.getAVAILABLE_SPENDING_POWER());
				rtabBalanceDetails.put("CashAvailable",objMS_RTAB_ACNT_DTLS.getCASH_AVAILABLE());
				rtabBalanceDetails.put("CashAvailablePending",objMS_RTAB_ACNT_DTLS.getCASH_AVAILABLE_PENDING_TXN());
				rtabBalanceDetails.put("MarginAvailable",objMS_RTAB_ACNT_DTLS.getMARGIN_AVAILABLE());
				rtabBalanceDetails.put("MarginPendingTransaction",objMS_RTAB_ACNT_DTLS.getMARGIN_PENDING_TXN());
				rtabBalanceDetails.put("DelayedDebitCardTxn",objMS_RTAB_ACNT_DTLS.getDELAYED_DEBIT_CARD_TXN());  

				//Setting the RTAB response in ArrayList...
				accBalanceOut.add(objMS_RTAB_OUT_DTL);
				accBalanceOut.add(objMS_RTAB_ACNT_DTLS);

				//Setting the Account balance object...
				objAccbalance.setInfoRTAB(rtabBalanceDetails);
				objAccbalance.setInfoRTABObject(accBalanceOut);
			}
			else
			{
				EBWLogger.logDebug("AccountBalance","The RTAB Call was a failure..");
				for(int j=0;j<si_return.getErrorVector().size();j++){
					if(si_return.getErrorVector().get(j) instanceof MS_INTERFACE_TECH_FAILURE ){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
						break;
					}
				}
			}
			EBWLogger.logDebug("AccountBalance","Returning from the Account Balance after the RTAB Service Execution");
		} 
		catch (Exception exception) {
			throw exception;	
		}
		return objAccbalance;
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

		retValue = "AccountBalance ( "
			+ super.toString() + TAB
			+ "infoRTAB = " + this.infoRTAB + TAB
			+ "infoRTABObject = " + this.infoRTABObject + TAB
			+ " )";

		return retValue;
	}
}
