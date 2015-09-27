package com.tcs.Payments.serverValidations;

import java.util.ArrayList;
import java.util.HashMap;

import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ValidateApprovals {

	/**
	 * Validates if there are statement linked accounts in the enriched context. 
	 * If there are no accounts present in the enriched context , then user dont have the access to the statement linked accounts.... 
	 * @param objMMContextData
	 * @return
	 * @throws Exception 
	 */
	public ServiceContext validatePageAccess(UserPrincipal objUserPrincipal,PaymentDetailsTO objPaymentDetails) throws Exception
	{
		ServiceContext context = new ServiceContext();
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
			if(!userActions.isEmpty() && userActions.contains(functionalRole)){
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
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.ACCESS_ERROR);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return context;
	}

	/**
	 * Following code checks for the status consistency check and cutoff time check...
	 * @param txnDetails
	 * @param event_valid_statuses
	 * @param context
	 * @throws Exception
	 */
	public ServiceContext validateRejectTxn(HashMap txnDetails,ArrayList event_valid_statuses) throws Exception
	{
		ServiceContext context = new ServiceContext();
		try 
		{
			//Setting the payment attributes..
			StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String txn_Paystatus = objPaymentDetails.getTxnCurrentStatusCode();

			//Checking for the status consistency ....
			context = statusConsistency.verifyStatusConsistency(event_valid_statuses,txn_Paystatus);
			if (context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity() == MessageType.SEVERE){
				return context; 
			}
		}
		catch (Exception exception) {
			throw exception;
		}
		return context; 
	}
}
