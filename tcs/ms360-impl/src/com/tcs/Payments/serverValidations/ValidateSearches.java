package com.tcs.Payments.serverValidations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.services.channelPaymentServices.ValidateCutOffTime;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 * **********************************************************
 */
public class ValidateSearches 
{
	/**
	 * Validates if there are statement linked accounts in the enriched context. 
	 * If there are no accounts present in the enriched context , then user dont have the access to the statement linked accounts.... 
	 * @param objMMContextData
	 * @return
	 * @throws Exception 
	 */
	public ServiceContext validatePageAccess(UserPrincipal objUserPrincipal,PaymentDetailsTO objPaymentDetails) throws Exception
	{
		ServiceContext serviceContext = new ServiceContext();
		try 
		{
			boolean isActionEligible = false;

			//Single functional role for any event ...
			String screen  = objPaymentDetails.getScreen();
			String action  = objPaymentDetails.getAction();
			String state   = objPaymentDetails.getState();
			String functionalRole="F"+screen+state+action;

			//Multiple functional role for the any single event (E.g Cancel) ..
			ArrayList multipleRoleList = objPaymentDetails.getUserFuncRoleList();

			//Checking the functional role..
			ArrayList userActions = objUserPrincipal.getRoleList();
			if(userActions!=null && !userActions.isEmpty() && userActions.contains(functionalRole)){
				isActionEligible = true;
			}
			else if(userActions!=null && !userActions.isEmpty() && multipleRoleList!=null){
				if(!multipleRoleList.isEmpty()){
					for(int i =0;i < multipleRoleList.size();i++){
						if(userActions.contains("F"+(String)multipleRoleList.get(i))){
							isActionEligible = true;
							break;
						}
					}
				}
			}

			if(!isActionEligible){
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.ACCESS_ERROR);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return serviceContext;
	}

	/**
	 * Cancel CASE ONLY...
	 * Validate status consistency...
	 * This check should be done only for executed transaction which are in status Executed and have the execution date as today (Both One-time and Recurring)
	 * @param txnDetails
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public ServiceContext validateCancelRequest(HashMap txnDetails,ArrayList event_valid_statuses) throws Exception
	{
		ServiceContext context = new ServiceContext();
		try 
		{
			//Initialization..
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Needs to checked for the Reject case only...
			String txn_status = objPaymentDetails.getTxnCurrentStatusCode();
			Date settleDate = ConvertionUtil.convertToDate(objPaymentDetails.getActualExecDate());
			Date businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());
			objPaymentDetails.setMsBranchId(branch_Id);

			//Checking for the transaction cancel request ...
			if(txn_status!=null && txn_status.equalsIgnoreCase("4"))
			{
				//Check for settle date ...
				if(settleDate!=null && businessDate!=null && settleDate.compareTo(businessDate) < 0){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.CANCEL_TXN_NOT_ALLOWED);
					return context;
				}

				//Cut-Off Time check for only executed transactions...
				ValidateCutOffTime objValidateCutOffTime = new ValidateCutOffTime();
				objValidateCutOffTime.checkCutOffTime_Error(txnDetails,context);
				if (context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity() == MessageType.SEVERE){
					return context; 
				}
			}

			//Checking for the status consistency ....
			context = statusConsistency.verifyStatusConsistency(event_valid_statuses,txn_status);
			if (context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity() == MessageType.SEVERE){
				return context; 
			}
		} 
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
		return context;
	}
}
