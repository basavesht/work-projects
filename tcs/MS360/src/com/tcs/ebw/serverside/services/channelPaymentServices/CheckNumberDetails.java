package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_CHK_NBR_OUT;
import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.Check_Print_Dtls;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.mswitch.common.channel.SI_RETURN;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class CheckNumberDetails {

	/**
	 * CheckNumber output extraction...
	 * @param out
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static void getCheckNumberDetails(HashMap txnDetails,Vector<Object> out, ServiceContext context) throws Exception 
	{
		EBWLogger.logDebug("CheckNumberDetails","Check Number details extraction...");
		Check_Print_Dtls objCheckNumberOut = new Check_Print_Dtls();
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Interface Output attributes...
			SI_RETURN si_return = (SI_RETURN)out.get(0);

			//Output Extraction and storage...
			if(si_return.getReturnCode()==0) 
			{
				//Interface Output attributes...
				MS_CHK_NBR_OUT objChkNumberDtls = new MS_CHK_NBR_OUT();
				objChkNumberDtls = (MS_CHK_NBR_OUT)si_return.getOutputVector().get(0);

				//Output extraction and mappings..
				objCheckNumberOut.setAba_number(objChkNumberDtls.getABA_NUMBER());
				objCheckNumberOut.setBank_account_no(objChkNumberDtls.getBANK_ACCOUNT());
				objCheckNumberOut.setBank_alpha_code(objChkNumberDtls.getBANK_ALPHA_CODE());
				objCheckNumberOut.setBank_city(objChkNumberDtls.getBANK_CITY());
				objCheckNumberOut.setBank_name(objChkNumberDtls.getBANK_NAME());
				objCheckNumberOut.setBank_no(ConvertionUtil.convertToString(objChkNumberDtls.getBANK_NUMBER()));
				objCheckNumberOut.setBank_state(objChkNumberDtls.getBANK_STATE());
				objCheckNumberOut.setBranch_addressLine1(objChkNumberDtls.getBRANCH_ADDRESS());
				objCheckNumberOut.setBranch_city(objChkNumberDtls.getBRANCH_CITY());
				objCheckNumberOut.setBranch_name(objChkNumberDtls.getBRANCH_NAME_LOCATION());
				objCheckNumberOut.setBranch_state(objChkNumberDtls.getBRANCH_STATE());
				objCheckNumberOut.setBranch_zip(objChkNumberDtls.getBRANCH_ZIP());
				objCheckNumberOut.setCheck_no(ConvertionUtil.convertToString(objChkNumberDtls.getCHECK_NUMBER()));
				objCheckNumberOut.setConfirmation_no(objPaymentDetails.getTransactionId());
				objCheckNumberOut.setFractionLine1(objChkNumberDtls.getBANK_FRACTION());
				objCheckNumberOut.setGl_base(ConvertionUtil.convertToString(objChkNumberDtls.getGL_BASE()));
				objCheckNumberOut.setGl_office(ConvertionUtil.convertToString(objChkNumberDtls.getGL_OFFICE()));
				objCheckNumberOut.setGl_type(ConvertionUtil.convertToString(objChkNumberDtls.getGL_TYPE()));
				objCheckNumberOut.setOps_center(null);
				objCheckNumberOut.setPrint_ops_center(objChkNumberDtls.getPRINT_OPS_CENTER());
				objCheckNumberOut.setPrint_ops_office(ConvertionUtil.convertToString(objChkNumberDtls.getPRINT_OPS_OFFICE()));
				objCheckNumberOut.setIssue_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));

				//Putting the Check_Print_Dtls Object details in the HashMap..
				txnDetails.put("PrintCheckDetails", objCheckNumberOut); //Check_Print_Dtls Object.
			}
			else 
			{
				EBWLogger.logDebug("CheckNumberDetails","The Check number generation is a failure....");
				for(int k=0;k<si_return.getErrorVector().size();k++){
					if(si_return.getErrorVector().get(k) instanceof MS_INTERFACE_TECH_FAILURE ){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
						break;
					}
				}
			} 
		}
		catch(Exception exception) {
			throw exception;
		}
	}
}
