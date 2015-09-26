package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_DTL;
import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse;
import com.tcs.mswitch.common.channel.SI_RETURN;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class AccountDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3073424635184670637L;
	public Object infoMerlinObject;	
	public ArrayList merlinOutResponse; 

	public Object getInfoMerlinObject() {
		return infoMerlinObject;
	}
	public void setInfoMerlinObject(Object infoMerlinObject) {
		this.infoMerlinObject = infoMerlinObject;
	}
	public ArrayList getMerlinOutResponse() {
		return merlinOutResponse;
	}
	public void setMerlinOutResponse(ArrayList merlinOutResponse) {
		this.merlinOutResponse = merlinOutResponse;
	}

	/**
	 * Merlin response extraction code...
	 * @param out
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static AccountDetails getAccountDetails(Vector<Object> out,ServiceContext context) throws Exception 
	{
		EBWLogger.trace("AccountDetails","Getting the Account Details after the Merlin/CPS Service Execution");
		try 
		{
			//Interface Output attributes...
			SI_RETURN si_return = (SI_RETURN)out.get(0);
			Object merlinOutput = new Object();
			AccountDetails objAccdetails = new AccountDetails();
			ArrayList<MS_ACCOUNT_OUT_DTL> accountdetails=new ArrayList<MS_ACCOUNT_OUT_DTL>();
			ArrayList<MerlinOutResponse> merlinOutAttribtues = new ArrayList<MerlinOutResponse>();
			boolean acnt_not_found = false;

			//Output Extraction and storage...
			if(si_return.getReturnCode() == 0)
			{
				EBWLogger.logDebug("AccountDetails","SI Returned Vector for Merlin Service is:"+si_return.getOutputVector());
				MS_ACCOUNT_OUT_DTL objMS_ACCOUNT_OUT_DTL = new MS_ACCOUNT_OUT_DTL();
				for(int k=0;k<si_return.getOutputVector().size();k++)
				{
					if(si_return.getOutputVector().get(k) instanceof MS_ACCOUNT_OUT_DTL )
					{
						objMS_ACCOUNT_OUT_DTL = (MS_ACCOUNT_OUT_DTL)si_return.getOutputVector().get(k);
						if(objMS_ACCOUNT_OUT_DTL!=null)
						{
							String acnt_not_fnd_ind = objMS_ACCOUNT_OUT_DTL.getACNT_NOT_FND_IND();
							if(acnt_not_fnd_ind!=null && !acnt_not_fnd_ind.equalsIgnoreCase("Y"))
							{
								//Adding the individual account details to the arrayList....
								accountdetails.add(objMS_ACCOUNT_OUT_DTL);

								//Setting the following attributes from the AcntView response into the MerlinOutResponse Object to be passed to other internal and external service if required ..
								MerlinOutResponse merlinOutresponse = new MerlinOutResponse();
								String msAccNumber = objMS_ACCOUNT_OUT_DTL.getACCOUNT_NO();
								do {
									if(msAccNumber.length()>=6){
										break;
									}
									else {
										msAccNumber="0"+msAccNumber;
									}
								} while(msAccNumber.length()!=6);
								merlinOutresponse.setAccountNumber(msAccNumber);
								merlinOutresponse.setCatTier(objMS_ACCOUNT_OUT_DTL.getCATTIER());
								merlinOutresponse.setKeyAccNumber(objMS_ACCOUNT_OUT_DTL.getKEY_ACCOUNT_NO());
								merlinOutresponse.setAcntCategory(objMS_ACCOUNT_OUT_DTL.getACC_CATEGORY());

								merlinOutAttribtues.add(merlinOutresponse);
							}
							else {
								acnt_not_found = true;
								break;
							}
						}
					}
				}

				//Out object creation...
				if(!acnt_not_found){
					merlinOutput=accountdetails;
					objAccdetails.setInfoMerlinObject(merlinOutput);
					objAccdetails.setMerlinOutResponse(merlinOutAttribtues);
				}
				else {
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.MS_ACNT_NOT_FOUND);
				}
			}
			else
			{
				EBWLogger.logDebug("AccountDetails","The Merlin call was failure....");
				for(int k=0;k<si_return.getErrorVector().size();k++){
					if(si_return.getErrorVector().get(k) instanceof MS_INTERFACE_TECH_FAILURE ){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
						break;
					}
				}
			}
			return objAccdetails;
		} 
		catch (Exception exception) {
			throw exception;	
		}
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

		retValue = "AccountDetails ( "
			+ super.toString() + TAB
			+ "infoMerlinObject = " + this.infoMerlinObject + TAB
			+ "merlinOutResponse = " + this.merlinOutResponse + TAB
			+ " )";

		return retValue;
	}
}