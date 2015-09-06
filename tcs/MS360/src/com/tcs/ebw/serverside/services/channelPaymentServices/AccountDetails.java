package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_DTL;
import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse.AddressDtls;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse.ClientDtls;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse.PlatingAddressDtls;
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
					//Object initialization...
					MerlinOutResponse merlinOutResponse = new MerlinOutResponse();
					ArrayList<AddressDtls> addressDtlsList = new ArrayList<AddressDtls>();
					ArrayList<ClientDtls> clientDtlsList = new ArrayList<ClientDtls>();
					ArrayList<PlatingAddressDtls> platingAddressDtlsList = new ArrayList<PlatingAddressDtls>();

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
								merlinOutResponse.setAccountNumber(MSCommonUtils.getMSAccFormat(objMS_ACCOUNT_OUT_DTL.getACCOUNT_NO()));
								merlinOutResponse.setCatTier(objMS_ACCOUNT_OUT_DTL.getCATTIER());
								merlinOutResponse.setKeyAccNumber(objMS_ACCOUNT_OUT_DTL.getKEY_ACCOUNT_NO());
								merlinOutResponse.setAcntCategory(objMS_ACCOUNT_OUT_DTL.getACC_CATEGORY());

								//Mapping the CLIENT address if any in the response...
								MS_ACCOUNT_OUT_DTL.ACCOUNT_OUT_ADDRESS objClientAddress[] = objMS_ACCOUNT_OUT_DTL.getMS_ACCOUNT_OUT_ADDRESS();
								if(objClientAddress != null)
								{
									for (int j = 0; j < objClientAddress.length; j++)
									{
										String category = objClientAddress[j].getCATEGORY();
										MerlinOutResponse.AddressDtls address = merlinOutResponse.new AddressDtls();
										if(category!=null && category.equalsIgnoreCase(MSSystemDefaults.ACNT_VIEW_MAIL_ADRSS_CATEGORY)){
											address.setLine1(objClientAddress[j].getLINE1());
											address.setLine2(objClientAddress[j].getLINE2());
											address.setLine3(objClientAddress[j].getLINE3());
											address.setCity(objClientAddress[j].getCITY());
											address.setState(objClientAddress[j].getSTATE());
											address.setZip(MSCommonUtils.formatZipCode(objClientAddress[j].getZIP5()));
											address.setProvince(objClientAddress[j].getPROVINCE());
											address.setForiegnStatus(objClientAddress[j].getFOREIGN_STATUS());
											address.setPostal(objClientAddress[j].getPOSTAL());
											address.setCategory(objClientAddress[j].getCATEGORY());
											addressDtlsList.add(address);
											break;
										}
									}
								}
								merlinOutResponse.setAddressDtls(addressDtlsList);

								//Mapping the ACCOUNT Plating address if any in the response...
								MS_ACCOUNT_OUT_DTL.ACCOUNT_PLATING_ADDRESS objPlatingAddress[] = objMS_ACCOUNT_OUT_DTL.getMS_ACCOUNT_PLATING_ADDRESS();
								if(objPlatingAddress != null)
								{
									for (int j = 0; j < objPlatingAddress.length; j++)
									{
										String category = objPlatingAddress[j].getADDRESS_CATEGORY();
										MerlinOutResponse.PlatingAddressDtls platingAddressDtls = merlinOutResponse.new PlatingAddressDtls();
										if(category!=null && category.equalsIgnoreCase(MSSystemDefaults.ACNT_VIEW_PLATING_ADRSS_CATEGORY)){
											platingAddressDtls.setAddress_line_index(objPlatingAddress[j].getADDRESS_LINE_INDEX());
											platingAddressDtls.setAddress_line(objPlatingAddress[j].getADDRESS_LINE());
											platingAddressDtls.setAddress_category(objPlatingAddress[j].getADDRESS_CATEGORY());
											platingAddressDtlsList.add(platingAddressDtls);
										}
									}
								}
								merlinOutResponse.setPlatingAddressDtls(platingAddressDtlsList);

								//Mapping the ACCOUNT CLIENT Details if any in the response...
								MS_ACCOUNT_OUT_DTL.ACCOUNT_OUT_CLIENT objClientDtls[] = objMS_ACCOUNT_OUT_DTL.getMS_ACCOUNT_OUT_CLIENT();
								if(objClientDtls != null)
								{
									for (int j = 0; j < objClientDtls.length; j++)
									{
										String relationship = objClientDtls[j].getRELATIONSHIP();
										MerlinOutResponse.ClientDtls clientDtls = merlinOutResponse.new ClientDtls();
										if(relationship!=null && relationship.equalsIgnoreCase(MSSystemDefaults.ACNT_VIEW_CLIENT_RELATIONSHIP)){
											clientDtls.setRelationship(objClientDtls[j].getRELATIONSHIP());
											clientDtls.setResidentCountry(objClientDtls[j].getRESIDENT_COUNTRY());
											clientDtlsList.add(clientDtls);
											break;
										}
									}
								}
								merlinOutResponse.setClientDtls(clientDtlsList);

								//Final object mappings..
								merlinOutAttribtues.add(merlinOutResponse);
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