package com.tcs.Payments.serverValidations;

import java.util.Collections;
import java.util.HashMap;

import com.tcs.Payments.EAITO.RSA_CALLBACK_INP;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.channelPaymentServices.CheckHoliday;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetQZBusinessDate;
import com.tcs.ebw.serverside.services.channelPaymentServices.ValidateCutOffTime;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *  224703         12-05-2011       Phase 2          Cut-Off Time & Holiday Check
 * **********************************************************
 */
public class ValidateRSARequest 
{
	public void validateRSARequest(RSA_CALLBACK_INP rsaRequest,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Starting validateRSARequest method in ValidateRSARequest class");
		try 
		{
			if(rsaRequest!=null) 
			{
				//RSA Request Inputs..
				String txn_conf_id = rsaRequest.getTXN_CONF_ID();
				String case_status = rsaRequest.getCASE_STATUS();
				String user_id = rsaRequest.getCASE_MNGMT_USR_ID();

				if(txn_conf_id!=null && !txn_conf_id.trim().equalsIgnoreCase("")
						&& case_status!=null && !case_status.trim().equalsIgnoreCase("")
						&& user_id!=null && !user_id.trim().equalsIgnoreCase("")){
					EBWLogger.trace(this, "RSA Request is Valid...");
				}
				else {
					EBWLogger.trace(this, "RSA Request is Invalid...");
					serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.RSA_REQUEST_VALIDATE_ERR);
				}
			}
			else {
				EBWLogger.trace(this, "RSA Request is Invalid...");
				serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.RSA_REQUEST_VALIDATE_ERR);
			}
		}
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Validates the RSA Pooling JOB incoming time. 
	 * If the web-service is invoked during holiday or after cut-off time , then return hard error to RSA system
	 * @param serviceContext
	 * @throws Exception 
	 */
	public void checkRSAPollingTime(ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Starting validateRSAPollingTime method in ValidateRSARequest class");
		HashMap txnDetails = new HashMap();
		Collections.synchronizedMap(txnDetails);
		try 
		{
			//Input Mapping...
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setTransfer_Type(TxnTypeCode.ACH_TYPE);

			//Adding Payment Details to the HashMap ....
			txnDetails.put("PaymentDetails",objPaymentDetails);

			//Get the business Date from the QZ_Dates View 
			EBWLogger.logDebug(this, "Getting the Business Date");
			GetQZBusinessDate getBusinessDt = new GetQZBusinessDate();
			getBusinessDt.setConnection(serviceContext.getConnection());
			getBusinessDt.getQzBusinessDate(txnDetails,serviceContext);
			if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
				return ; 
			}

			//Check if the Approval/Reject date is holiday or not...
			EBWLogger.logDebug(this, "Checking the Cut Off Time for the same day transactions..");
			CheckHoliday objCheckHoliday = new CheckHoliday();
			objCheckHoliday.setConnection(serviceContext.getConnection());
			objCheckHoliday.checkHolidayDate(txnDetails,serviceContext);
			if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
				return ; 
			}

			//Check the Cut Off Time ...
			EBWLogger.logDebug(this, "Checking the Cut Off Time for the same day transactions..");
			ValidateCutOffTime objValidateCutOffTime = new ValidateCutOffTime();
			objValidateCutOffTime.setConnection(serviceContext.getConnection());
			objValidateCutOffTime.checkCutOffTime_Error(txnDetails,serviceContext);
			if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
				return ; 
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
	}
}
