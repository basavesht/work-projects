package com.tcs.bancs.utils;

import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.config.handler.WorkflowHandlerConfig;
import com.tcs.bancs.helpers.ws.TransferRequestFaultBean;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.objects.schema.request.payments.transfers.Account;
import com.tcs.bancs.objects.schema.request.payments.transfers.PaymentRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferAction;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferSchedule;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferType;
import com.tcs.bancs.objects.schema.request.payments.transfers.UserDetails;
import com.tcs.bancs.objects.schema.response.payments.transfers.MessageCode;
import com.tcs.bancs.objects.schema.response.payments.transfers.ResponseObject;
import com.tcs.bancs.objects.schema.response.payments.transfers.ReturnCode;
import com.tcs.bancs.objects.schema.response.payments.transfers.TransferResponse;
import com.tcs.bancs.channels.context.Message;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.bancs.channels.context.MessageType;

public class TransferRequestUtil {

	private TransferRequestUtil (){

	}

	public static final String contactNumber = "18000333555";

	public static WorkflowHandlerConfig getConfig(WebServiceContext context){
		ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT); 
		WorkflowHandlerConfig config = (WorkflowHandlerConfig)servletContext.getAttribute("handlerConfig");
		return config;
	}

	public static Map<String,Object> createRequestMap(TransferRequest request){
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("TransferRequest", request);
		return requestMap;
	}

	public static Map<String,Object> createBusinessResponse(TransferRequest request,Map response){
		response.put("TransferRequest", request);
		return response;
	}

	public static TransferResponse createResponseObject(Map<String,Object> responseMap){
		TransferResponse response = null;
		if(responseMap!=null && !responseMap.isEmpty() && responseMap.containsKey("TransferResponse")) {
			response = (TransferResponse)responseMap.get("TransferResponse");
		}
		return response;
	}

	public static TransferRequestFaultBean getFaultBean(String faultMessage){
		TransferRequestFaultBean faultBean = new TransferRequestFaultBean();
		faultBean.setMessage(faultMessage);
		return faultBean;
	}

	public static MSUser_DetailsTO setMSUserDetailsTO(UserDetails objUserDetails) {
		//Extracting userdetails to MSUser_DetailsTO
		MSUser_DetailsTO objMSDetails = new MSUser_DetailsTO();
		objMSDetails.setLoginId(objUserDetails.getLoginId());
		objMSDetails.setUuid(objUserDetails.getClientUUID());
		objMSDetails.setFirstName(objUserDetails.getFirstName());
		objMSDetails.setMiddleName(objUserDetails.getMiddleName());
		objMSDetails.setLastName(objUserDetails.getLastName());
		objMSDetails.setClientIdentifier(objUserDetails.getClientIdentifier());
		objMSDetails.setMaskedClientIdentifier(objUserDetails.getMaskedClientIdentifier());
		return objMSDetails;
	}

	/**
	 * Setting the From account details ....
	 * @param objAccount
	 * @return
	 */
	public static FromMSAcc_DetailsTO setFromMSAccDetailsTO(Account objAccount) {
		FromMSAcc_DetailsTO objFromMSAcc_DetailsTO = new FromMSAcc_DetailsTO();
		objFromMSAcc_DetailsTO.setAccountCategory(objAccount.getAccountCategory());
		objFromMSAcc_DetailsTO.setAccountClass(objAccount.getAccountClass());
		objFromMSAcc_DetailsTO.setAccountNumber(formatToMSAcntType(objAccount.getAccount(),6));
		objFromMSAcc_DetailsTO.setChoiceFundCode(objAccount.getChoiceFundCode());
		objFromMSAcc_DetailsTO.setClientCategory(objAccount.getClientCategory());
		objFromMSAcc_DetailsTO.setCollateralAcctInd(objAccount.getCollateralAcctInd());
		objFromMSAcc_DetailsTO.setDivPay(objAccount.getDivPay());
		objFromMSAcc_DetailsTO.setFaNumber(formatToMSAcntType(objAccount.getFa(),3));
		objFromMSAcc_DetailsTO.setIraCode(objAccount.getIraCode());
		objFromMSAcc_DetailsTO.setKeyAccount(objAccount.getKeyAccount());
		objFromMSAcc_DetailsTO.setNovusSubProduct(objAccount.getNovusSubProduct());
		objFromMSAcc_DetailsTO.setOfficeNumber(formatToMSAcntType(objAccount.getOffice(),3));
		objFromMSAcc_DetailsTO.setStatus(objAccount.getAccountStatus());
		objFromMSAcc_DetailsTO.setTradeControl(objAccount.getTradeControl());
		return objFromMSAcc_DetailsTO;
	}

	/**
	 * Setting the To account details ....
	 * @param objAccount
	 * @return
	 */
	public static ToMSAcc_DetailsTO setToMSAccDetailsTO(Account objAccount) {
		ToMSAcc_DetailsTO objToMSAcc_DetailsTO = new ToMSAcc_DetailsTO();
		objToMSAcc_DetailsTO.setAccountCategory(objAccount.getAccountCategory());
		objToMSAcc_DetailsTO.setAccountClass(objAccount.getAccountClass());
		objToMSAcc_DetailsTO.setAccountNumber(formatToMSAcntType(objAccount.getAccount(),6));
		objToMSAcc_DetailsTO.setChoiceFundCode(objAccount.getChoiceFundCode());
		objToMSAcc_DetailsTO.setClientCategory(objAccount.getClientCategory());
		objToMSAcc_DetailsTO.setCollateralAcctInd(objAccount.getCollateralAcctInd());
		objToMSAcc_DetailsTO.setDivPay(objAccount.getDivPay());
		objToMSAcc_DetailsTO.setFaNumber(formatToMSAcntType(objAccount.getFa(),3));
		objToMSAcc_DetailsTO.setIraCode(objAccount.getIraCode());
		objToMSAcc_DetailsTO.setKeyAccount(objAccount.getKeyAccount());
		objToMSAcc_DetailsTO.setNovusSubProduct(objAccount.getNovusSubProduct());
		objToMSAcc_DetailsTO.setOfficeNumber(formatToMSAcntType(objAccount.getOffice(),3));
		objToMSAcc_DetailsTO.setStatus(objAccount.getAccountStatus());
		objToMSAcc_DetailsTO.setTradeControl(objAccount.getTradeControl());
		return objToMSAcc_DetailsTO;
	}

	public static String formatToMSAcntType(String account, int digits) {
		if(account!=null) {
			do {
				if(account.length()>= digits){
					break;
				}
				else {
					account="0"+account;
				}
			} while(account.length()!= digits);
		}
		return account;
	}

	/**
	 * Prints the event log response .... 
	 * @param contextData
	 */
	public static void logEventResponse(ServiceContext contextData) {
		try 
		{
			if(contextData!=null){
				StringBuffer responseBuffer = new StringBuffer();
				responseBuffer.append("Response Severity : " +contextData.getMaxSeverity() + "\r\n");

				responseBuffer.append("Response Messages - \r\n");
				ArrayList contextMessages = contextData.getMessages(); 
				if(contextMessages!=null){
					for(int i=0;i<contextMessages.size();i++){
						com.tcs.bancs.channels.context.Message objContextMess = (com.tcs.bancs.channels.context.Message)contextMessages.get(i); 
						responseBuffer.append("Message Type : " + objContextMess.getType().toString() + "\r\n");
						responseBuffer.append("Message Code : " + objContextMess.getCode() + "\r\n");
						ArrayList objResponse= (ArrayList)objContextMess.getArgs();
						if(objResponse!=null && !objResponse.isEmpty()){
							if(objResponse.get(0)!=null && !((String)objResponse.get(0)).trim().equalsIgnoreCase("")){
								responseBuffer.append("Message Description : " + objResponse.get(0) + "\r\n");
							}
						}
					}
				}
				System.out.println("Event Response : \n" + responseBuffer.toString());
			}
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Extracts the data from the service context during any server side validation or technical failure..
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	// UserPrincipal objUserPrincipal Added for CR 298.
	public static String extractContextErrMessage(ServiceContext contextData) throws Exception 
	{
		String errorCode = "MSERR001"; // Default Error code for INTERNAL_ERRORS
		String errorMessage = "";
		try 
		{
			//TODO: need to add EBWErrorCodes.properties file here..?
			ResourceBundle messages = ResourceBundle.getBundle("EBWErrorCodes");
			errorMessage = messages.getString(errorCode);
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			if(contextMessages!=null && !contextMessages.isEmpty())
			{
				for(int i=0;i<contextMessages.size();i++)
				{
					Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
					if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode() == ChannelsErrorCodes.ACCESS_ERROR){
						errorCode = "Access_Err001";
						//Message format ...
						Object[] objAccessCheck = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objAccessCheck);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.INT_ACC_DAP_FAILURE){
						errorCode = "Dap_Err001";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.EXT_ACC_DAP_FAILURE){
						errorCode = "Dap_Err002";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.EXT_ACC_OWNER_FAILURE){
						errorCode = "Dap_Err003";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.EXT_ACC_OWNER_FAILURE_EDIT){
						errorCode = "Dap_Err004";
						//Message format ...
						Object[] objEditOwnerCheck = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objEditOwnerCheck);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.STATUS_CONSISTENCY_FAILURE){
						errorCode = "Status_Err001";
						//Message format ...
						Object[] objStatusCheck = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objStatusCheck);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.INVALID_ROTUING_NUM){
						errorCode = "InvalidABANum_Err001";
						//Message format ...
						Object[] objRoutingNum = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objRoutingNum);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.MS_ACNT_NOT_FOUND){
						errorCode = "AcntNotFound_Err001";
						//Message format ...
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.EXT_TXN_RULES_VIOLATION){ // Check the message code from the context, add only if its a EXT_TXN_RULES_VIOLATION.
						errorCode = "Ext_Txn_Rule_Violation";
						//Message format ...
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.THIRD_PARTY_EXT_TXN_RULES_VIOLATION){ // Check the message code from the context, add only if its a THIRD_PARTY_EXT_TXN_RULES_VIOLATION.
						errorCode = "Ext_Third_Party_Txn_Violation";
						//Message format ...
						errorMessage = messages.getString(errorCode);
						break;
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return errorMessage;
	}

	/**
	 * Extracts the data from the service context with the Message Type as SEVERE...
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	public static ArrayList<Object> extractContextSevereErrMsg(ServiceContext contextData) throws Exception
	{

		ArrayList<Object> businessErrors = new ArrayList<Object>();
		try 
		{
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			for(int i=0;i<contextMessages.size();i++)
			{
				Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
				if(objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.CUT_OFF_TIME_EXCEEDED){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessErrors.contains(objBussErrString.get(0))){
							businessErrors.add(objBussErrString.get(0));
							break;
						}
					}
				}
				if(objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.VERSION_MISMATCH_ERROR){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessErrors.contains(objBussErrString.get(0))){
							businessErrors.add(objBussErrString.get(0));
							break;
						}
					}
				}
				if(objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.BUSINESS_HOLIDAY){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessErrors.contains(objBussErrString.get(0))){
							businessErrors.add(objBussErrString.get(0));
							break;
						}
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return businessErrors;
	}

	/**
	 * Extracts the data from the service context with the Message Type as ERROR...
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	public static ArrayList<Object> extractContextErrMsg(ServiceContext contextData) throws Exception
	{
		ArrayList<Object> businessErrors = new ArrayList<Object>();
		try 
		{
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			for(int i=0;i<contextMessages.size();i++){
				Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
				if(objContextMess.getType()==MessageType.ERROR && objContextMess.getCode()== ChannelsErrorCodes.BUSSINESS_ERROR){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessErrors.contains(objBussErrString.get(0))){
							businessErrors.add(objBussErrString.get(0));
						}
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return businessErrors;
	}

	/**
	 * Extracts the data from the service context with the Message Type as WARNING...
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	public static ArrayList<Object> extractContextWarningMsg(ServiceContext contextData) throws Exception
	{
		ArrayList<Object> businessWarnings = new ArrayList<Object>();
		try 
		{
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			for(int i=0;i<contextMessages.size();i++){
				Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
				if(objContextMess.getType()==MessageType.WARNING && objContextMess.getCode()== ChannelsErrorCodes.BUSSINESS_WARNING){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessWarnings.contains(objBussErrString.get(0))){
							businessWarnings.add(objBussErrString.get(0));
						}
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return businessWarnings;
	}

	/**
	 * Extracts the data from the service context with the Message Type as INFORMATION...
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	public static ArrayList<Object> extractContextInfoMsg(ServiceContext contextData) throws Exception
	{
		ArrayList<Object> businessInformation = new ArrayList<Object>();
		try 
		{
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			for(int i=0;i<contextMessages.size();i++){
				Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
				if(objContextMess.getType()==MessageType.INFORMATION && objContextMess.getCode()== ChannelsErrorCodes.BUSSINESS_INFORMATION){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessInformation.contains(objBussErrString.get(0))){
							businessInformation.add(objBussErrString.get(0));
						}
					}
				}
				if(objContextMess.getType()==MessageType.INFORMATION && objContextMess.getCode()==ChannelsErrorCodes.CUT_OFF_TIME_EXCEEDED){
					ArrayList<Object> objBussInfoString = (ArrayList)objContextMess.getArgs();
					if(objBussInfoString!=null && !objBussInfoString.isEmpty()){
						if(objBussInfoString.get(0)!=null && !((String)objBussInfoString.get(0)).trim().equalsIgnoreCase("") && !businessInformation.contains(objBussInfoString.get(0))){
							businessInformation.add(objBussInfoString.get(0));
						}
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return businessInformation;
	}

	/**
	 * Extracts the data from the service context with the Message Type as RISK...
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	public static ArrayList<Object> extractContextRiskMsg(ServiceContext contextData) throws Exception
	{
		ArrayList<Object> businessRisk = new ArrayList<Object>();
		try 
		{
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			for(int i=0;i<contextMessages.size();i++){
				Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
				if(objContextMess.getType()==MessageType.RISK && objContextMess.getCode()== ChannelsErrorCodes.BUSSINESS_RISK){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessRisk.contains(objBussErrString.get(0))){
							businessRisk.add(objBussErrString.get(0));
						}
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return businessRisk;
	}

	public static String setBusinessRuleEventDesc(PaymentRequest objPaymentRequest){
		String eventDesc = "";
		TransferAction action = objPaymentRequest.getTransferAction();
		if(TransferAction.CreatePreConfirm.equals(action)){
			eventDesc = Bus_Rule_Input_Desc.Create_PreConfirm;
		}
		else if(TransferAction.CreateConfirm.equals(action)){
			eventDesc = Bus_Rule_Input_Desc.Confirm;
		}
		return eventDesc;
	}

	public static String setTransferSchedule(PaymentRequest objPaymentRequest){
		String schedule = "";
		TransferSchedule transferSchedule = objPaymentRequest.getTransferSchedule();
		if(TransferSchedule.OneTime.equals(transferSchedule)){
			schedule = "1";
		}
		else if(TransferSchedule.Recurring.equals(transferSchedule)){
			schedule = "2";
		}
		return schedule;
	}

	public static String setFrequency(PaymentRequest objPaymentRequest)
	{
		String frequency = "";
		if(TransferSchedule.Recurring.equals(objPaymentRequest.getTransferSchedule()))
		{
			Integer freq_duration  = objPaymentRequest.getRecurringDetails().getFrequency();
			if(freq_duration == 0 ){
				frequency = "W";
			}
			else if(freq_duration == 1 ){
				frequency = "OW";
			}
			else if(freq_duration == 2 ){
				frequency = "M";
			}
			else if(freq_duration == 3 ){
				frequency = "FBD";
			}
			else if(freq_duration == 4 ){
				frequency = "LBD";
			}
			else if(freq_duration == 5 ){
				frequency = "Q";
			}
			else if(freq_duration == 6 ){
				frequency = "H";
			}
			else if(freq_duration == 7 ){
				frequency = "Y";
			}
		}
		return frequency;
	}

	public static String setRepeat(PaymentRequest objPaymentRequest)
	{
		String repeat = null;
		if(TransferSchedule.Recurring.equals(objPaymentRequest.getTransferSchedule()))
		{
			Integer repeatOption = objPaymentRequest.getRecurringDetails().getRepeat();
			if(repeatOption == 0){
				repeat = "1";
			}
			else if(repeatOption == 1){
				repeat = "2";
			}
			else if(repeatOption == 2){
				repeat = "3";
			}
			else if(repeatOption == 3){
				repeat = "4";
			}
		}
		return repeat;
	}

	public static void checkHandlerConfig(WorkflowHandlerConfig workflowHandlerConfig, String configFile) throws ConfigXMLParsingException 
	{
		if(workflowHandlerConfig == null){
			workflowHandlerConfig = WorkflowHandlerConfig.getInstance();
			try {
				workflowHandlerConfig.parse(configFile);
			} catch (FileNotFoundException e) {
				throw new ConfigXMLParsingException("WebService config file not found ..",e);
			} catch (ConfigXMLParsingException e) {
				throw new ConfigXMLParsingException("WebService config file(XML) parsing exception ..",e);
			} 
		}
		else {
			try {
				workflowHandlerConfig.checkNew();
			} catch (FileNotFoundException e) {
				throw new ConfigXMLParsingException("WebService config file not found ..",e);
			} catch (ConfigXMLParsingException e) {
				throw new ConfigXMLParsingException("WebService config file(XML) parsing exception ..",e);
			} 
		}
	}

	public static String getOperation(TransferRequest request) 
	{
		String operation = "";
		if(request!=null) {
			if(request.getPaymentRequest()!=null){
				if(TransferAction.CreatePageLoad.equals(request.getPaymentRequest().getTransferAction())){
					operation = TransferRequestOperation.LOAD_TRANSFER.getOperation();
				}
				else if(TransferAction.GetBalance.equals(request.getPaymentRequest().getTransferAction())){
					operation = TransferRequestOperation.SELECT_ACCOUNT.getOperation();
				}
				else if(TransferAction.CreatePreConfirm.equals(request.getPaymentRequest().getTransferAction())){
					operation = TransferRequestOperation.SUBMIT_TRANSFER.getOperation();
				}
				if(TransferAction.CreateConfirm.equals(request.getPaymentRequest().getTransferAction())){
					operation = TransferRequestOperation.CREATE_TRANSFER.getOperation();
				}
			}
		}
		return operation;
	}

	public static String setAcntFilterType(PaymentRequest objPaymentRequest){
		String filterType = "";
		if(objPaymentRequest!=null){
			if(TransferType.Internal.equals(objPaymentRequest.getTransferType())){
				filterType = TxnTypeCode.INTERNAL;
			}
			else if(TransferType.ACH.equals(objPaymentRequest.getTransferType())){
				filterType = TxnTypeCode.ACH_TYPE;
			}
		}
		return filterType;
	}

	public static String setPageType(PaymentRequest objPaymentRequest){
		String transferType = "";
		if(objPaymentRequest!=null){
			if(TransferType.Internal.equals(objPaymentRequest.getTransferType())){
				transferType = TxnTypeCode.INTERNAL;
			}
			else if(TransferType.ACH.equals(objPaymentRequest.getTransferType())){
				transferType = TxnTypeCode.ACH_TYPE;
			}
		}
		return transferType;
	}

	public static com.tcs.bancs.objects.schema.response.payments.transfers.ServiceContext setRespMessageContext(com.tcs.bancs.channels.context.ServiceContext contextData,MessageType messageType) throws Exception 
	{
		com.tcs.bancs.objects.schema.response.payments.transfers.ServiceContext responseContext= new com.tcs.bancs.objects.schema.response.payments.transfers.ServiceContext();
		List<Object> messageList = new ArrayList<Object>();
		com.tcs.bancs.objects.schema.response.payments.transfers.MessageType messType = null;
		com.tcs.bancs.objects.schema.response.payments.transfers.MessageCode messCode = null;
		if(MessageType.SEVERE.equals(messageType)){
			messageList = TransferRequestUtil.extractContextSevereErrMsg(contextData);
			messType = com.tcs.bancs.objects.schema.response.payments.transfers.MessageType.SEVERE;
			messCode = MessageCode.BUSSINESS_ERROR;
		}
		else if(MessageType.ERROR.equals(messageType)){
			messageList = TransferRequestUtil.extractContextErrMsg(contextData);
			messType = com.tcs.bancs.objects.schema.response.payments.transfers.MessageType.ERROR;
			messCode = MessageCode.BUSSINESS_ERROR;
		}
		else if(MessageType.WARNING.equals(messageType)){
			messageList = TransferRequestUtil.extractContextWarningMsg(contextData);
			messType = com.tcs.bancs.objects.schema.response.payments.transfers.MessageType.WARNING;
			messCode = MessageCode.BUSSINESS_WARNING;
		}
		else if(MessageType.INFORMATION.equals(messageType)){
			messageList = TransferRequestUtil.extractContextInfoMsg(contextData);
			messType = com.tcs.bancs.objects.schema.response.payments.transfers.MessageType.INFORMATION;
			messCode = MessageCode.BUSSINESS_INFORMATION;
		}
		else if(MessageType.RISK.equals(messageType)){
			messageList = TransferRequestUtil.extractContextInfoMsg(contextData);
			messType = com.tcs.bancs.objects.schema.response.payments.transfers.MessageType.RISK;
			messCode = MessageCode.BUSSINESS_RISK;
		}
		if(messageList!=null && !messageList.isEmpty())
		{
			ArrayList<com.tcs.bancs.objects.schema.response.payments.transfers.Message> respMessageList = new ArrayList<com.tcs.bancs.objects.schema.response.payments.transfers.Message>();
			for (Object message : messageList) {
				com.tcs.bancs.objects.schema.response.payments.transfers.Message respMessage = new com.tcs.bancs.objects.schema.response.payments.transfers.Message();
				respMessage.setMessageCode(messCode.getMessageCode());
				respMessage.setMessageDescription((String)message);
				respMessage.setMessageType(messType);
				respMessageList.add(respMessage);
			}
			responseContext.setMessage(respMessageList);
		}
		return responseContext;
	}

	public static ResponseObject setResponseObject(ReturnCode code,String description){
		ResponseObject responseObj = new ResponseObject();
		responseObj.setReturnCode(code.getCode());
		responseObj.setDescription(description);
		return responseObj;
	}
}
