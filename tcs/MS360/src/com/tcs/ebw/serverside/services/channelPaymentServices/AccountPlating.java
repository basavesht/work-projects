package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import com.tcs.Payments.EAITO.MS_ACNT_PLATING_OUT;
import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.Payments.EAITO.MS_ACNT_PLATING_OUT.ACCOUNT_PLATING_INFO;
import com.tcs.Payments.EAITO.MS_ACNT_PLATING_OUT.AUTHORISED_ENTITIES;
import com.tcs.Payments.EAITO.MS_ACNT_PLATING_OUT.PAYEE_INFO;
import com.tcs.Payments.ms360Utils.ChkReqConstants;
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
public class AccountPlating implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 133511961856373406L;
	public ArrayList acntPlatingMap;
	public ArrayList acntPayeeMap;
	public String acntUserName;
	public String acntUserDetails;
	public ArrayList authorisedEntities;

	public ArrayList getAcntPlatingMap() {
		return acntPlatingMap;
	}
	public void setAcntPlatingMap(ArrayList acntPlatingMap) {
		this.acntPlatingMap = acntPlatingMap;
	}
	public ArrayList getAcntPayeeMap() {
		return acntPayeeMap;
	}
	public void setAcntPayeeMap(ArrayList acntPayeeMap) {
		this.acntPayeeMap = acntPayeeMap;
	}
	public String getAcntUserName() {
		return acntUserName;
	}
	public void setAcntUserName(String acntUserName) {
		this.acntUserName = acntUserName;
	}
	public String getAcntUserDetails() {
		return acntUserDetails;
	}
	public void setAcntUserDetails(String acntUserDetails) {
		this.acntUserDetails = acntUserDetails;
	}
	public ArrayList getAuthorisedEntities() {
		return authorisedEntities;
	}
	public void setAuthorisedEntities(ArrayList authorisedEntities) {
		this.authorisedEntities = authorisedEntities;
	}

	static final Comparator<ACCOUNT_PLATING_INFO> SEQUENCE_ORDER =
		new Comparator<ACCOUNT_PLATING_INFO>() {
			public int compare(ACCOUNT_PLATING_INFO e1, ACCOUNT_PLATING_INFO e2) {
				return e1.getSEQUENCE().compareToIgnoreCase(e2.getSEQUENCE());
			}
		};

		/**
		 * Account plating attributes mapping to get the HashMap.
		 * @param out
		 * @param context
		 * @return
		 * @throws Exception
		 */
		public static AccountPlating getAcntPlatingInfo(Vector<Object> out,ServiceContext context) throws Exception 
		{
			EBWLogger.logDebug("AccountPlating","Getting the AccountPlating after the AcntPlating Service");
			AccountPlating objAcntPlating = new AccountPlating();
			try 
			{
				//Interface Output attributes...
				SI_RETURN si_return = (SI_RETURN)out.get(0);

				//Output Extraction and storage...
				if(si_return.getReturnCode() == 0)
				{
					//Extracting the output from the web service response..
					ArrayList payeeToObj = new ArrayList();
					ArrayList authorisedEntities = new ArrayList();
					ArrayList acntPlatingOut = new ArrayList();
					String userName = null;
					String userDetails = null;
					for(int i=0;i<si_return.getOutputVector().size();i++)
					{
						MS_ACNT_PLATING_OUT objMS_ACNT_PLATING_OUT = new MS_ACNT_PLATING_OUT();
						objMS_ACNT_PLATING_OUT = (MS_ACNT_PLATING_OUT)si_return.getOutputVector().get(i);

						ACCOUNT_PLATING_INFO[] acntPlatingResponse = new ACCOUNT_PLATING_INFO[si_return.getOutputVector().size()];
						acntPlatingResponse = objMS_ACNT_PLATING_OUT.getACCOUNT_PLATING_INFO();

						//Setting the payeeName to response in the HashMap...
						PAYEE_INFO objPayeeInfo[] = objMS_ACNT_PLATING_OUT.getPAYEE_INFO();
						for(int p =0; p< objPayeeInfo.length ; p++)
						{
							String payee = objPayeeInfo[p].getPAYEE();
							if(payee!=null && !payee.trim().equalsIgnoreCase("")){
								HashMap payeeToMap = new HashMap();
								payeeToMap.put("AttributePayee", payee);
								payeeToObj.add(payeeToMap);    
							}
						}

						//Setting all the authorized entities in an ArrayList (Spoke To options)..
						AUTHORISED_ENTITIES objAuthEntities[] = objMS_ACNT_PLATING_OUT.getAUTHORISED_ENTITIES();
						for(int a =0;a<objAuthEntities.length;a++)
						{
							//Setting the Hashed Unique Id's for an entity in an ArrayList...
							ArrayList authorisedEntity = new ArrayList();
							String hashedUniqueId = objAuthEntities[a].getHASHED_UNIQUE_ID();
							String entityName = objAuthEntities[a].getAUTH_ENTITY_NAME();
							if(hashedUniqueId!=null && !hashedUniqueId.trim().equalsIgnoreCase("")){
								if(entityName!=null && !entityName.trim().equalsIgnoreCase("")){
									authorisedEntity.add(hashedUniqueId);
									authorisedEntity.add(entityName);
								}
							}
							if(authorisedEntities!=null && !authorisedEntities.contains(authorisedEntity)){
								authorisedEntities.add(authorisedEntity);
							}
						}

						//Setting the Acnt plating details..
						ACCOUNT_PLATING_INFO[] sortedAcntPlatingObj = sortAcntPlatingResponse(acntPlatingResponse);
						for(int j = 0 ; j< sortedAcntPlatingObj.length ;  j++)
						{
							// Setting the Account plating response in the HashMap...
							ACCOUNT_PLATING_INFO objACCOUNT_PLATING_INFO = (ACCOUNT_PLATING_INFO)sortedAcntPlatingObj[j];
							if(objACCOUNT_PLATING_INFO!=null){
								HashMap acntPlatingMap = new HashMap();
								acntPlatingMap.put("AttributeName", objACCOUNT_PLATING_INFO.getLABEL());
								acntPlatingMap.put("AttributeValue", objACCOUNT_PLATING_INFO.getVALUE());
								acntPlatingMap.put("AttributeSequence", objACCOUNT_PLATING_INFO.getSEQUENCE());
								acntPlatingOut.add(acntPlatingMap);
							}
						}

						//Setting the AcntUserName...
						userName = extractAcntUserName(acntPlatingResponse);

						//Setting the AcntUserDetails...
						for(int j = 0 ; j< sortedAcntPlatingObj.length ;  j++)
						{
							// Setting the Account plating response in the HashMap...
							ACCOUNT_PLATING_INFO objACCOUNT_PLATING_INFO = (ACCOUNT_PLATING_INFO)sortedAcntPlatingObj[j];
							if(objACCOUNT_PLATING_INFO!=null){
								if(objACCOUNT_PLATING_INFO.getSEQUENCE()!=null && objACCOUNT_PLATING_INFO.getSEQUENCE().equalsIgnoreCase("R1")){
									userDetails = objACCOUNT_PLATING_INFO.getVALUE();
								}
							}
						}
					}

					//Adding Third Party to the PayeeTo list obtained..
					HashMap thirdPayeeToMap = new HashMap();
					thirdPayeeToMap.put("AttributePayee", ChkReqConstants.THIRD_PARTY);
					payeeToObj.add(thirdPayeeToMap);  

					//Setting the account plating parameters...
					objAcntPlating.setAcntPlatingMap(acntPlatingOut);
					objAcntPlating.setAcntPayeeMap(payeeToObj);
					objAcntPlating.setAcntUserName(userName);
					objAcntPlating.setAcntUserDetails(userDetails);
					objAcntPlating.setAuthorisedEntities(authorisedEntities);
				}
				else
				{
					EBWLogger.logDebug("AccountBalance","The Acnt Plating call was a failure..");
					for(int j=0; j< si_return.getErrorVector().size(); j++){
						if(si_return.getErrorVector().get(j) instanceof MS_INTERFACE_TECH_FAILURE ){
							context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
							break;
						}
					}
				}
				EBWLogger.logDebug("AccountPlating","Returning from the AccountPlating after the AcntPlating Service call");
			} 
			catch (Exception exception) {
				throw exception;	
			}
			return objAcntPlating;
		}

		/**
		 * Sorting the acnt plating attributes as per the sequence values ....
		 * Sorted required order is (L1,R1,L2,R2 .....)
		 * @param acntPlatingDetails
		 */

		public static ACCOUNT_PLATING_INFO[] sortAcntPlatingResponse(ACCOUNT_PLATING_INFO[] acntPlatingDetails)
		{
			//Partially Sorting the acntPlating output as L1,L2,R1,R2 .....
			List anctPlateList = Arrays.asList(acntPlatingDetails);
			Collections.sort(anctPlateList,SEQUENCE_ORDER);

			//Adding the partially sorted sequence list to ArrayList...
			ArrayList unorderedSeqList = new ArrayList();
			for(int i = 0 ;i <anctPlateList.size();i++){
				unorderedSeqList.add(((ACCOUNT_PLATING_INFO)anctPlateList.get(i)).getSEQUENCE().toString());
			}
			EBWLogger.logDebug("AcntPlating unordered list of sequences",unorderedSeqList.toString());

			//Initialization for left side row and right side row label value pair...
			ArrayList leftSideRow =new ArrayList();
			ArrayList rightSideRow =new ArrayList();

			for(int i = 0 ;i <anctPlateList.size();i++){
				String seq = ((ACCOUNT_PLATING_INFO)anctPlateList.get(i)).getSEQUENCE().toString();
				if(seq.indexOf("L")!=-1){
					if(!leftSideRow.contains(seq)){
						leftSideRow.add(seq);
					}
				}
				if(seq.indexOf("R")!=-1){
					if(!rightSideRow.contains(seq)){
						rightSideRow.add(seq);
					}
				}
			}
			EBWLogger.logDebug("AcntPlating left side row sequences",leftSideRow.toString());
			EBWLogger.logDebug("AcntPlating right side row sequences",rightSideRow.toString());

			//Making the left side and right row sequences equal so as to display blank in case not available from WS.

			//Equalizing right side row sequences...
			for(int i = 0 ; i < leftSideRow.size() ; i++){
				if(!(rightSideRow.contains(leftSideRow.get(i).toString().replace("L", "R")))){
					rightSideRow.add(leftSideRow.get(i).toString().replace("L", "R"));
				}
			}

			//Equalizing left side row sequences...
			for(int i = 0 ; i < rightSideRow.size() ; i++){
				if(!(leftSideRow.contains(rightSideRow.get(i).toString().replace("R", "L")))){
					leftSideRow.add(rightSideRow.get(i).toString().replace("R", "L"));
				}
			}

			//Collection sorting the left side and right row sequences distributed equally...
			Collections.sort(leftSideRow);
			Collections.sort(rightSideRow);

			if(leftSideRow.size()== rightSideRow.size()){
				EBWLogger.logDebug("AcntPlating left side row sequences after equalising",leftSideRow.toString());
				EBWLogger.logDebug("AcntPlating right side row sequences after equalising",rightSideRow.toString());
			}

			//Adding both the left side and right side attributes to a single merged arrayList...
			ArrayList mergedAcntPlatingList = new ArrayList();
			for(int k = 0 ; k < leftSideRow.size() ;k++){
				mergedAcntPlatingList.add(leftSideRow.get(k));
				mergedAcntPlatingList.add(rightSideRow.get(k));
			}
			EBWLogger.logDebug("Merged Acnt plating list sequence.." ,mergedAcntPlatingList.toString());

			//Adding the blank object in case the response dosen't contain the following side object..
			ACCOUNT_PLATING_INFO[] sortedAcntPlatingInfo = new ACCOUNT_PLATING_INFO[mergedAcntPlatingList.size()];
			for(int k =0 ; k < mergedAcntPlatingList.size() ; k++){
				String seqOrder = (String)mergedAcntPlatingList.get(k);
				for(int i = 0;  i <acntPlatingDetails.length ; i++) {
					if(unorderedSeqList.contains(seqOrder) && seqOrder.equalsIgnoreCase(acntPlatingDetails[i].getSEQUENCE().toString())){
						sortedAcntPlatingInfo[k]=acntPlatingDetails[i];
						break;
					}
					else if(!unorderedSeqList.contains(seqOrder)) {
						MS_ACNT_PLATING_OUT outerClass = new MS_ACNT_PLATING_OUT();
						MS_ACNT_PLATING_OUT.ACCOUNT_PLATING_INFO objMS_ACNT_PLATING_OUT_TEMP = outerClass.new ACCOUNT_PLATING_INFO();
						objMS_ACNT_PLATING_OUT_TEMP.setSEQUENCE(seqOrder);
						sortedAcntPlatingInfo[k]= objMS_ACNT_PLATING_OUT_TEMP;
						break;
					}
				}
			}

			for(int i = 0 ; i< sortedAcntPlatingInfo.length ; i++){
				EBWLogger.logDebug("Acnt Plating final sorted order is" ,sortedAcntPlatingInfo[i].toString());
			}
			return sortedAcntPlatingInfo;
		}

		/**
		 * Extract the Account UserName from the account plating response....
		 * @return
		 */
		public static String extractAcntUserName(ACCOUNT_PLATING_INFO[] acntPlatingDetails)
		{
			String acntUserName = "";
			List anctPlateList = Arrays.asList(acntPlatingDetails);
			for(int i = 0 ;i <anctPlateList.size();i++){
				ACCOUNT_PLATING_INFO acntPlatingObj = ((ACCOUNT_PLATING_INFO)anctPlateList.get(i));
				String seq = acntPlatingObj.getSEQUENCE().toString();
				if(seq!=null && seq.equalsIgnoreCase("P1")){
					acntUserName = acntPlatingObj.getVALUE().toString();
					break;
				}
			}
			return acntUserName;
		}
}
