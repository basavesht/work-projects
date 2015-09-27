package com.tcs.Payments.ms360Utils;

import java.util.ArrayList;
import java.util.ResourceBundle;

import com.tcs.Payments.EAITO.RSA_CALLBACK_INP;
import com.tcs.Payments.EAITO.RSA_CALLBACK_OUT;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.Message;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetChannelPaymentDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetExternalAccountsDetails;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class RSAUtilities {

	/**
	 * 
	 * @param reponseOut
	 * @param serviceContext
	 * @throws Exception 
	 */
	public static Object[] RequestExtractor_Transaction(RSA_CALLBACK_INP rsaRequest,ServiceContext serviceContext) throws Exception
	{
		Object[] requestObj = new Object[2];
		try
		{
			if(rsaRequest!=null)
			{
				//Mapping the payment attributes...
				String branch_Id = PropertyFileReader.getProperty("OU_ID");
				String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
				String applicationId = PropertyFileReader.getProperty("APPL_ID");
				String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");

				//CallBack RSA Comments description...
				StringBuffer comments = new StringBuffer();
				comments.append("Case Status :");
				comments.append(rsaRequest.getCASE_STATUS());
				if(rsaRequest.getDESCRIPTION()!=null && !rsaRequest.getDESCRIPTION().trim().equalsIgnoreCase("")){
					comments.append(", ");
					comments.append("Description :");
					comments.append(rsaRequest.getDESCRIPTION());
				}

				//Mapping MS User details..
				MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
				objMSUserDetails.setFirstName(rsaRequest.getCASE_MNGMT_USR_ID());
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
				objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Approve_PreConfirm);
				objPaymentDetails.setTxnApproved(true);
				objPaymentDetails.setVersionChkReq(true);
				objPaymentDetails.setVersionChkId("Transaction");
				objPaymentDetails.setStatusChkReq(true);
				objPaymentDetails.setStatusChkEventDesc("RSACallback_Genuine_Txn");
				objPaymentDetails.setTransactionId(rsaRequest.getTXN_CONF_ID());
				objPaymentDetails.setTxnBatchId(rsaRequest.getTXN_BATCH_CONF_ID());
				objPaymentDetails.setCreated_by_comments(comments.toString());
				objPaymentDetails.setCase_status(rsaRequest.getCASE_STATUS());

				//Get Transaction Version details...
				GetChannelPaymentDetails objgetChannelPaymentDetails = new GetChannelPaymentDetails();
				objgetChannelPaymentDetails.setConnection(serviceContext.getConnection());
				objgetChannelPaymentDetails.getTransactionVersion(objPaymentDetails,serviceContext);

				requestObj[1] = objPaymentDetails;
			}
			else {
				EBWLogger.trace("RSAUtilities", "RSA Request is Invalid...");
				serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.RSA_REQUEST_VALIDATE_ERR);
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
	 * @throws Exception 
	 */
	public static Object[] RequestExtractor_Account(RSA_CALLBACK_INP rsaRequest,ServiceContext serviceContext) throws Exception
	{
		Object[] requestObj = new Object[2];
		try
		{
			if(rsaRequest!=null)
			{
				//Mapping the payment attributes...
				String branch_Id = PropertyFileReader.getProperty("OU_ID");
				String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
				String applicationId = PropertyFileReader.getProperty("APPL_ID");
				String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");

				//CallBack RSA Comments description...
				StringBuffer comments = new StringBuffer();
				comments.append("Confirmation Number :");
				comments.append(rsaRequest.getTXN_CONF_ID());
				comments.append(", ");
				comments.append("Case Status :");
				comments.append(rsaRequest.getCASE_STATUS());
				if(rsaRequest.getDESCRIPTION()!=null && !rsaRequest.getDESCRIPTION().trim().equalsIgnoreCase("")){
					comments.append(", ");
					comments.append("Description :");
					comments.append(rsaRequest.getDESCRIPTION());
				}

				//Mapping MS User details..
				MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
				objMSUserDetails.setFirstName(rsaRequest.getCASE_MNGMT_USR_ID());
				objMSUserDetails.setLastName("");
				objMSUserDetails.setMiddleName("");
				objMSUserDetails.setRcafId(MSSystemDefaults.DUMMY_RACFID);
				objMSUserDetails.setInitiatorRole("");
				requestObj[0] = objMSUserDetails;

				//Mapping the payment details object..
				PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
				objPaymentDetails.setTransfer_Currency(currencyCode);
				objPaymentDetails.setMsBranchId(branch_Id);
				objPaymentDetails.setApplicationId(applicationId);
				objPaymentDetails.setMMSystemDesc(systemDesc);
				objPaymentDetails.setVersionChkReq(true);
				objPaymentDetails.setVersionChkId("Account");
				objPaymentDetails.setStatusChkReq(true);
				objPaymentDetails.setStatusChkEventDesc("RSACallback_Fraud_Acnt");
				objPaymentDetails.setTransactionId(rsaRequest.getTXN_CONF_ID());
				objPaymentDetails.setTxnBatchId(rsaRequest.getTXN_BATCH_CONF_ID());
				objPaymentDetails.setCreated_by_comments(comments.toString());
				objPaymentDetails.setCase_status(rsaRequest.getCASE_STATUS());

				//Getting external account version details..
				GetExternalAccountsDetails getExtAcntVersion = new GetExternalAccountsDetails();
				getExtAcntVersion.setConnection(serviceContext.getConnection());
				getExtAcntVersion.getExtAccountVersion(objPaymentDetails, serviceContext);

				requestObj[1] = objPaymentDetails;
			}
			else {
				EBWLogger.trace("RSAUtilities", "RSA Request is Invalid...");
				serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.RSA_REQUEST_VALIDATE_ERR);
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
	public static void ResponseCreator(RSA_CALLBACK_OUT responseOut,ServiceContext contextData)
	{
		String errorCode = ""; // Default Error code for INTERNAL_ERRORS
		String errorMessage = "";

		//Error code extraction...
		MSCommonUtils.logEventResponse(contextData);
		if(contextData!=null)
		{
			ResourceBundle messages = ResourceBundle.getBundle("EBWErrorCodes");
			if(contextData.getMaxSeverity() == MessageType.CRITICAL){ 
				errorCode = "1";
				errorMessage = messages.getString("RSA_001");
			}
			else if(contextData.getMaxSeverity() == MessageType.SEVERE)
			{ 
				ArrayList contextMessages = contextData.getMessages(); 
				for(int i=0;i<contextMessages.size();i++)
				{
					Message objContextMess = (Message)contextMessages.get(i);
					if(objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.RSA_REQUEST_VALIDATE_ERR){
						errorCode = "2";
						errorMessage = messages.getString("RSA_002");
						break;
					}
					else if(objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.TXN_NOT_IDENTIFIED){
						errorCode = "3";
						errorMessage = messages.getString("RSA_003");
						break;
					}
					else if(objContextMess.getType()==MessageType.SEVERE && (objContextMess.getCode()== ChannelsErrorCodes.VERSION_MISMATCH_ERROR || objContextMess.getCode()== ChannelsErrorCodes.STATUS_CONSISTENCY_FAILURE)){
						errorCode = "4";
						errorMessage = messages.getString("RSA_004");
						break;
					}
					else if(objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.INVALID_RSA_CASE_STATUS){
						errorCode = "5";
						errorMessage = messages.getString("RSA_006");
						break;
					}
					else if(objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.CUT_OFF_TIME_EXCEEDED){
						errorCode = "6";
						errorMessage = messages.getString("RSA_007");
						break;
					}
					else if(objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.BUSINESS_HOLIDAY){
						errorCode = "7";
						errorMessage = messages.getString("RSA_008");
						break;
					}
					else {
						errorCode = "1";
						errorMessage = messages.getString("RSA_001");
						break;
					}
				}
			}
			else { //SUCCESS... 
				errorCode = "0";
				errorMessage = messages.getString("RSA_005");
			}
		}

		//Output Mappings..
		responseOut.setRETURN_CODE(errorCode);
		responseOut.setERROR_DESC(errorMessage);
		EBWLogger.logDebug("RSAUtilities", responseOut.toString());
	}
}
