package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.HashMap;

import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.InitiatorRoleDesc;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class LimitRequestProcessor {

	/**
	 * Determines the action as per the specifications document while updating the transaction other than child... 
	 * @param txnDetails
	 * @throws Exception 
	 */
	public static boolean getLimitAction(HashMap txnDetails) throws Exception
	{ 
		boolean isLimitCallReq = false;
		int create_limit = 0;
		int update_limit = 0;
		int delete_limit = 0;
		String limit_action = null;
		try 
		{
			//Payments Details attributes...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			String previous_owner_role = objPaymentDetails.getPrevious_owner_role();
			String current_owner_role = objPaymentDetails.getCurrent_owner_role();

			//Child Transaction case...
			if(objPaymentDetails.isChildTxnCreated())
			{
				//Creation case...
				if(current_owner_role!=null && current_owner_role.equalsIgnoreCase(InitiatorRoleDesc.Client)){
					create_limit = 1;
					limit_action = Bus_Rule_Input_Desc.Confirm;
					isLimitCallReq = true;
				}
			}
			else 
			{
				//Initiation case...
				if(objPaymentDetails.isTxnInitiated()){
					if(current_owner_role!=null && current_owner_role.equalsIgnoreCase(InitiatorRoleDesc.Client)){
						create_limit = 1;
						isLimitCallReq = true;
					}
				}

				//Modification case...
				if(objPaymentDetails.isTxnModified()){
					if(previous_owner_role!=null && !previous_owner_role.equalsIgnoreCase(InitiatorRoleDesc.Client)){
						create_limit = 1;
						isLimitCallReq = true;
					}
					else if(previous_owner_role!=null && previous_owner_role.equalsIgnoreCase(InitiatorRoleDesc.Client)){
						update_limit = 1;
						isLimitCallReq = true;
					}
				}

				//Cancellation case....
				if(objPaymentDetails.isTxnCancelled()){
					if(current_owner_role!=null && current_owner_role.equalsIgnoreCase(InitiatorRoleDesc.Client)){
						delete_limit = 1;
						limit_action = Bus_Rule_Input_Desc.Cancel;
						isLimitCallReq = true;
					}
				}
			}

			//Output Mappings...
			objPaymentDetails.setCreate_limit(create_limit);
			objPaymentDetails.setUpdate_limit(update_limit);
			objPaymentDetails.setDelete_limit(delete_limit);
			objPaymentDetails.setLimit_Action(limit_action);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return isLimitCallReq;
	}
}
