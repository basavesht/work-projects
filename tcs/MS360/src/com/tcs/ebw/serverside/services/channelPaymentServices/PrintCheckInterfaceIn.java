package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_BOTTOM_LINE_PRINT_IN;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.Check_Print_Dtls;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class PrintCheckInterfaceIn {

	/**
	 * Print Check call ...
	 * @param txnDetails
	 * @return
	 * @throws Exception 
	 */
	public static Vector setPrintCheckInput(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug("PrintCheckInterfaceIn", "Setting the PrintCheck Inputs..");
		Vector printCheckVector = new Vector();
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			//Print Check request mappings details..
			Check_Print_Dtls objCheckPrint = new Check_Print_Dtls();
			if(txnDetails.containsKey("PrintCheckDetails")){
				objCheckPrint = (Check_Print_Dtls)txnDetails.get("PrintCheckDetails");
			}

			//From account details..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//Check number interface In...
			MS_BOTTOM_LINE_PRINT_IN objPrintCheck = new MS_BOTTOM_LINE_PRINT_IN();
			objPrintCheck.setTYPE(WSDefaultInputsMap.getPrintType(serviceContext));
			objPrintCheck.setAPPLICATION_NAME(MSCommonUtils.getPrintCheck_ApplName(objCheckRequest,serviceContext));
			objPrintCheck.setPRINTER_NAME(objCheckRequest.getPrinterName());
			objPrintCheck.setCREATE_PDF(WSDefaultInputsMap.getPrintCreatePdf(serviceContext));
			objPrintCheck.setBASE_NAME("");
			objPrintCheck.setENCRYPT(WSDefaultInputsMap.getPrintEncrypt(serviceContext));
			objPrintCheck.setSPOOL_TITLE(objPaymentDetails.getTransactionId()+"_"+objCheckPrint.getCheck_no());
			objPrintCheck.setID("");
			objPrintCheck.setCHECK_TYPE(MSCommonUtils.getPrintCheck_ChkType(objCheckRequest));
			objPrintCheck.setCHECK_DATE(objPaymentDetails.getBusiness_Date());
			objPrintCheck.setAMOUNT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()).toString());
			objPrintCheck.setCHECK_NUMBER(objCheckPrint.getCheck_no());
			objPrintCheck.setABA_NUMBER(objCheckPrint.getAba_number());
			objPrintCheck.setBANK_ACCOUNT_NUMBER(objCheckPrint.getBank_account_no());
			objPrintCheck.setMM_CONF_NUMBER(objPaymentDetails.getTransactionId());
			objPrintCheck.setPAYEE_NAME(objCheckRequest.getPayeeName());
			objPrintCheck.setFROM_ACCOUNT(objFromMSAcc_Details.getOfficeNumber()+"-"+objFromMSAcc_Details.getAccountNumber());
			objPrintCheck.setREMITTER(null);
			objPrintCheck.setPRINTING_BRANCH(objCheckRequest.getPrintingBranch());
			objPrintCheck.setPRINTING_BRANCH_NAME(objCheckPrint.getBranch_name());
			objPrintCheck.setPRINTING_BRANCH_ADDRESS_LINE1(objCheckPrint.getBranch_addressLine1());
			objPrintCheck.setPRINTING_BRANCH_CITY(objCheckPrint.getBranch_city());
			objPrintCheck.setPRINTING_BRANCH_STATE(objCheckPrint.getBranch_state());
			objPrintCheck.setPRINTING_BRANCH_ZIP(objCheckPrint.getBranch_zip());
			objPrintCheck.setDELIVERED_TO(objCheckRequest.getDeliveredToType());
			objPrintCheck.setDELIVERED_TO_NAME(objCheckRequest.getDeliveredToName());
			objPrintCheck.setDELIVERED_TO_ID_TYPE(MSCommonUtils.getCheckPrintTypeOfId(objCheckRequest));
			if(objCheckRequest.getPrintMemoCheckFlag()!=null && objCheckRequest.getPrintMemoCheckFlag().equalsIgnoreCase("Y")){
				objPrintCheck.setCHECK_MEMO_LINE1(objCheckRequest.getMemoLine1());
				objPrintCheck.setCHECK_MEMO_LINE2(objCheckRequest.getMemoLine2());
			}
			if(objCheckRequest.getPrintMemoStubFlag()!=null && objCheckRequest.getPrintMemoStubFlag().equalsIgnoreCase("Y")){
				objPrintCheck.setSTUB_MEMO_LINE1(objCheckRequest.getMemoLine1());
				objPrintCheck.setSTUB_MEMO_LINE2(objCheckRequest.getMemoLine2());
			}
			objPrintCheck.setFRACTION_LINE1(objCheckPrint.getFractionLine1().trim());
			objPrintCheck.setBANK_NAME(objCheckPrint.getBank_name().trim());
			objPrintCheck.setBANK_CITY(objCheckPrint.getBank_city().trim());
			objPrintCheck.setBANK_STATE(objCheckPrint.getBank_state().trim());
			objPrintCheck.setBANK_NUMBER(objCheckPrint.getBank_no());
			objPrintCheck.setLEGAL_TEXT_LINE1(WSDefaultInputsMap.getLegalTxtLine1(serviceContext));
			objPrintCheck.setLEGAL_TEXT_LINE2(WSDefaultInputsMap.getLegalTxtLine2(serviceContext));
			objPrintCheck.setINQUIRY_NUMBER(WSDefaultInputsMap.getInquiryNum(serviceContext));

			//Setting the payee address..
			setPayeeAddress(objCheckRequest,objPrintCheck);

			//Adding the RTAB Input Object to the Vector...
			printCheckVector.add(objPrintCheck);	
		} 
		catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
		EBWLogger.logDebug("PrintCheckInterfaceIn", "Input parameters for the PrintCheck is"+printCheckVector.toString());
		return printCheckVector;
	}

	/**
	 * Mapping the payee address as per the requirements..
	 * @param objCheckRequest
	 */
	public static void setPayeeAddress(CheckRequestTO objCheckRequest,MS_BOTTOM_LINE_PRINT_IN objPrintCheck)
	{
		if(objCheckRequest.getDeliveryAddrLine1()!=null && !objCheckRequest.getDeliveryAddrLine1().trim().equalsIgnoreCase("")){
			objPrintCheck.setPAYEE_ADDRESS_LINE1(objCheckRequest.getDeliveryAddrLine1().trim());
		}
		if(objCheckRequest.getDeliveryAddrLine2()!=null && !objCheckRequest.getDeliveryAddrLine2().trim().equalsIgnoreCase("")){
			objPrintCheck.setPAYEE_ADDRESS_LINE2(objCheckRequest.getDeliveryAddrLine2().trim());
			if(objPrintCheck.getPAYEE_ADDRESS_LINE1()==null){
				objPrintCheck.setPAYEE_ADDRESS_LINE1(objCheckRequest.getDeliveryAddrLine2().trim());
				objPrintCheck.setPAYEE_ADDRESS_LINE2(null);
			}
		}
		if(objCheckRequest.getDeliveryAddrLine3()!=null && !objCheckRequest.getDeliveryAddrLine3().trim().equalsIgnoreCase("")){
			objPrintCheck.setPAYEE_ADDRESS_LINE3(objCheckRequest.getDeliveryAddrLine3().trim());
			if(objPrintCheck.getPAYEE_ADDRESS_LINE2()==null){
				objPrintCheck.setPAYEE_ADDRESS_LINE2(objCheckRequest.getDeliveryAddrLine3().trim());
				objPrintCheck.setPAYEE_ADDRESS_LINE3(null);
			}
			if(objPrintCheck.getPAYEE_ADDRESS_LINE1()==null){
				objPrintCheck.setPAYEE_ADDRESS_LINE1(objCheckRequest.getDeliveryAddrLine3().trim());
				objPrintCheck.setPAYEE_ADDRESS_LINE2(null);
				objPrintCheck.setPAYEE_ADDRESS_LINE3(null);
			}
		}
		if(objCheckRequest.getDeliveryAddrLine4()!=null && !objCheckRequest.getDeliveryAddrLine4().trim().equalsIgnoreCase("")){
			objPrintCheck.setPAYEE_ADDRESS_LINE4(objCheckRequest.getDeliveryAddrLine4().trim());
			if(objPrintCheck.getPAYEE_ADDRESS_LINE3()==null){
				objPrintCheck.setPAYEE_ADDRESS_LINE3(objCheckRequest.getDeliveryAddrLine4());
				objPrintCheck.setPAYEE_ADDRESS_LINE4(null);
			}
			if(objPrintCheck.getPAYEE_ADDRESS_LINE2()==null){
				objPrintCheck.setPAYEE_ADDRESS_LINE2(objCheckRequest.getDeliveryAddrLine4().trim());
				objPrintCheck.setPAYEE_ADDRESS_LINE3(null);
				objPrintCheck.setPAYEE_ADDRESS_LINE4(null);
			}
			if(objPrintCheck.getPAYEE_ADDRESS_LINE1()==null){
				objPrintCheck.setPAYEE_ADDRESS_LINE1(objCheckRequest.getDeliveryAddrLine4().trim());
				objPrintCheck.setPAYEE_ADDRESS_LINE2(null);
				objPrintCheck.setPAYEE_ADDRESS_LINE3(null);
				objPrintCheck.setPAYEE_ADDRESS_LINE4(null);
			}
		}
	}
}
