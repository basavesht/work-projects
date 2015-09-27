package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_BOTTOM_LINE_PRINT_OUT;
import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
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
public class PrintCheckOutDetails 
{
	/**
	 * Print Check output extraction...
	 * @param out
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static void getPrintCheckOutDetails(HashMap txnDetails,Vector<Object> out, ServiceContext context) throws Exception 
	{
		EBWLogger.logDebug("CheckPrintOutDetails","Print Check Number details extraction...");
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Print Check request mappings details..
			Check_Print_Dtls objCheckPrintOut = new Check_Print_Dtls();
			if(txnDetails.containsKey("PrintCheckDetails")){
				objCheckPrintOut = (Check_Print_Dtls)txnDetails.get("PrintCheckDetails");
			}

			//Interface Output attributes...
			SI_RETURN si_return = (SI_RETURN)out.get(0);

			//Output Extraction and storage...
			if(si_return.getReturnCode()==0) 
			{
				//Interface Output attributes...
				MS_BOTTOM_LINE_PRINT_OUT objPrintResponse = new MS_BOTTOM_LINE_PRINT_OUT();
				objPrintResponse = (MS_BOTTOM_LINE_PRINT_OUT)si_return.getOutputVector().get(0);

				//Extraction...
				objCheckPrintOut.setPrimaryPrinterCode(objPrintResponse.getPRINTER_STATUS_CODE());
				objCheckPrintOut.setPrimaryPrinterResponse(getPrintEventMsg(objPrintResponse));
				objCheckPrintOut.setPrinterResponse(objPrintResponse.getRESPONSE());
				objCheckPrintOut.setVoid_reason_code(setVoidReasonCode(objPrintResponse.getPRINTER_STATUS_CODE()));

				//Creating the comments for the payment details..
				objPaymentDetails.setCreated_by_comments(setPrintAuditComments(objCheckPrintOut));
			}
			else 
			{
				EBWLogger.logDebug("CheckPrintOutDetails","The Print Check number generation is a failure....");
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

	/**
	 * Setting the comments on Print success or failure status...
	 * @param objCheckPrintOut
	 * @return
	 */
	public static String setPrintAuditComments(Check_Print_Dtls objCheckPrintOut)
	{
		StringBuffer printComments = new StringBuffer();

		//Creating the comments for the payment details..
		if(objCheckPrintOut!=null)
		{
			if(objCheckPrintOut.getPrinterResponse()!=null && objCheckPrintOut.getPrinterResponse().equalsIgnoreCase(ChkReqConstants.PRINT_SUCCESS)){
				printComments.append("Print Success: Check # ");
				printComments.append(objCheckPrintOut.getCheck_no());
			}
			else if(objCheckPrintOut.getPrinterResponse()!=null && objCheckPrintOut.getPrinterResponse().equalsIgnoreCase(ChkReqConstants.PRINT_FAILED)) {
				printComments.append("Print Failed: Check # ");
				printComments.append(objCheckPrintOut.getCheck_no()+" Voided.");
				printComments.append("Error Code ");
				printComments.append(objCheckPrintOut.getPrimaryPrinterCode());
				printComments.append(":");
				printComments.append(objCheckPrintOut.getPrimaryPrinterResponse());
			}
		}
		return printComments.toString();
	}


	/**
	 * Get the print failure error message description for the response error code..
	 * @param responseCode
	 * @return
	 */
	public static String getPrintEventMsg(MS_BOTTOM_LINE_PRINT_OUT objPrintResponse)
	{
		//Initialization....
		String printEventcode = "";
		String printEventMsg = "";
		ResourceBundle messages = ResourceBundle.getBundle("EBWErrorCodes");

		//Printer response object...
		String defaultEventCode = MSSystemDefaults.BT_ERRORCODE_PREFIX;
		String responseCode = objPrintResponse.getPRINTER_STATUS_CODE();
		String responseState = objPrintResponse.getRESPONSE();
		try 
		{
			printEventcode = defaultEventCode+responseCode.trim();
			printEventMsg = messages.getString(printEventcode.trim());
		} 
		catch (Exception exception) 
		{
			if(responseState!=null && !responseState.equalsIgnoreCase(ChkReqConstants.PRINT_SUCCESS)) {
				printEventcode = defaultEventCode+"NA";
				printEventMsg = messages.getString(printEventcode);
			}
		}
		return printEventMsg;
	}


	/**
	 * Takes String as a parameter and converts into 
	 * a Double object.
	 * 
	 * @param void_reason_code
	 * @return Double object.
	 */
	public static Double setVoidReasonCode(String void_reason_code)
	{
		Double void_reason_code_val = -1D;
		try
		{
			if (void_reason_code!=null && void_reason_code.length() > 0){
				DecimalFormat objDecimalFormat = new DecimalFormat();
				objDecimalFormat.applyPattern("###,###,###,###,###,###,###.###");
				void_reason_code_val = new Double(objDecimalFormat.parse(void_reason_code).doubleValue());
			}
		} 
		catch (Exception exc) {
			void_reason_code_val = -1D;
		}
		return void_reason_code_val;
	}   
}
