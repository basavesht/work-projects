package com.tcs.Payments.ms360Utils;

import java.util.ArrayList;
import java.util.ResourceBundle;

import com.tcs.Payments.EAITO.MS_BOTTOM_LINE_ASYNC_IN;
import com.tcs.Payments.EAITO.MS_BOTTOM_LINE_ASYNC_OUT;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.Message;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class AsyncBTUtilities {

	/**
	 * 
	 * @param reponseOut
	 * @param serviceContext
	 * @throws Exception 
	 */
	public static Object[] RequestExtractor_Transaction(MS_BOTTOM_LINE_ASYNC_IN asynchBTRequest,ServiceContext serviceContext) throws Exception
	{
		Object[] requestObj = new Object[2];
		try
		{
			if(asynchBTRequest!=null)
			{
				//Mapping the payment attributes...
				String branch_Id = PropertyFileReader.getProperty("OU_ID");
				String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
				String applicationId = PropertyFileReader.getProperty("APPL_ID");
				String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");

				//Extract Confirmation Number.... 
				String txn_conf_number = "";
				String check_number = "";
				String strArr[] = asynchBTRequest.getSPOOL_JOB_NAME().trim().split("_");
				if (strArr.length > 0){
					if(strArr[0]!=null){
						txn_conf_number = new String(strArr[0]);
					}
					if(strArr[1]!=null){
						check_number = new String(strArr[1]);
					}
				}	

				//CallBack RSA Comments description...
				StringBuffer comments = new StringBuffer();
				comments.append("Check # ");
				comments.append(check_number);
				comments.append(", ");
				comments.append("Print Status :");
				comments.append(asynchBTRequest.getJOB_STATUS());
				if(asynchBTRequest.getDESCRIPTION()!=null && !asynchBTRequest.getDESCRIPTION().trim().equalsIgnoreCase("")){
					comments.append(", ");
					comments.append("Description :");
					comments.append(asynchBTRequest.getDESCRIPTION());
				}

				//Mapping MS User details..
				MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
				objMSUserDetails.setFirstName(systemDesc);
				objMSUserDetails.setLastName("");
				objMSUserDetails.setMiddleName("");
				objMSUserDetails.setRcafId(MSSystemDefaults.DUMMY_RACFID);
				objMSUserDetails.setInitiatorRole("");
				requestObj[0] = objMSUserDetails;

				//Mapping the payment details object..
				PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
				objPaymentDetails.setFrmAcc_InContext(false);
				objPaymentDetails.setToAcc_InContext(false);
				objPaymentDetails.setTransfer_Currency(currencyCode);
				objPaymentDetails.setMsBranchId(branch_Id);
				objPaymentDetails.setApplicationId(applicationId);
				objPaymentDetails.setMMSystemDesc(systemDesc);
				objPaymentDetails.setTxnApproved(true);
				objPaymentDetails.setVersionChkReq(false);
				objPaymentDetails.setTransactionId(txn_conf_number);
				objPaymentDetails.setTxnBatchId(asynchBTRequest.getTXN_BATCH_CONF_ID());
				objPaymentDetails.setCreated_by_comments(comments.toString());
				requestObj[1] = objPaymentDetails;
			}
			else {
				EBWLogger.trace("AsyncBTUtilities", "BT Request is Invalid...");
				serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.BT_REQUEST_VALIDATE_ERR);
			}
		} 
		catch (Exception exception){
			throw exception;
		}
		return requestObj;
	}

	/**
	 * 
	 * @param reponseOut
	 * @param serviceContext
	 */
	public static void ResponseCreator(MS_BOTTOM_LINE_ASYNC_OUT responseOut,ServiceContext contextData)
	{
		String errorCode = "1"; // Default Error code for INTERNAL_ERRORS
		String errorMessage = "";

		//Error code extraction...
		MSCommonUtils.logEventResponse(contextData);
		if(contextData!=null)
		{
			ResourceBundle messages = ResourceBundle.getBundle("EBWErrorCodes");
			if(contextData.getMaxSeverity() == MessageType.CRITICAL){ 
				errorCode = "1";
				errorMessage = messages.getString("BT_001");
			}
			else if(contextData.getMaxSeverity() == MessageType.SEVERE){ 
				ArrayList contextMessages = contextData.getMessages(); 
				for(int i=0;i<contextMessages.size();i++)
				{
					Message objContextMess = (Message)contextMessages.get(i);
					if(objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.BT_REQUEST_VALIDATE_ERR){
						errorCode = "2";
						errorMessage = messages.getString("BT_002");
						break;
					}
					else if(objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.TXN_NOT_IDENTIFIED){
						errorCode = "3";
						errorMessage = messages.getString("BT_003");
						break;
					}
					else {
						errorCode = "1";
						errorMessage = messages.getString("BT_001");
						break;
					}
				}
			}
			else { //SUCCESS... 
				errorCode = "0";
				errorMessage = messages.getString("BT_004");
			}
		}

		//Output Mappings..
		responseOut.setRETURN_CODE(errorCode);
		responseOut.setERR_DESC(errorMessage);

		EBWLogger.logDebug("AsyncBTUtilities", responseOut.toString());
	}
}
