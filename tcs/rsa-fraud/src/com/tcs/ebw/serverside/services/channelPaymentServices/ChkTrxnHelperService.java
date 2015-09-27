/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.Check_Print_Dtls;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ChkTrxnHelperService extends DatabaseService{

	public ChkTrxnHelperService() {

	}

	public void insertChkTrxnDetails(HashMap txnDetails, boolean isChildTrxn) throws Exception,SQLException 
	{
		EBWLogger.logDebug(this, "Entering insertChkTrxnDetails...");
		try
		{
			CheckRequestTO objCheckRequestTO = new CheckRequestTO();			
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequestTO = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}	
			if (isChildTrxn)
			{
				objCheckRequestTO.setConfirmationNo(objPaymentDetails.getChildTransactionId());
				changeEstPickupTime(objCheckRequestTO,objPaymentDetails.getChildTxnRequestedExecDate());
			}
			else
			{
				objCheckRequestTO.setConfirmationNo(objPaymentDetails.getTransactionId());
			}

			//StatementId and Object mapping for the execute query...
			String stmntId="insertChkTrxnDtls";
			Object chkReqTOObj=objCheckRequestTO;
			Boolean isTxnCommitReq = Boolean.TRUE;	

			EBWLogger.logDebug(this,"Executing Query : "+stmntId);
			EBWLogger.logDebug(this,"Input Parameters mapped"+chkReqTOObj);
			execute(stmntId,chkReqTOObj,isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);
			EBWLogger.logDebug(this, "Exiting insertChkTrxnDetails...");
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	public void updateChkTrxnDetails(HashMap txnDetails) throws Exception,SQLException 
	{
		EBWLogger.logDebug(this, "Entering updateChkTrxnDetails...");
		try
		{
			CheckRequestTO objCheckRequestTO = new CheckRequestTO();			
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequestTO = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			objCheckRequestTO.setConfirmationNo(objPaymentDetails.getTransactionId());

			//StatementId and Object mapping for the execute query...
			String stmntId="updateChkTrxnDtls";
			Object chkReqTOObj=objCheckRequestTO;
			Boolean isTxnCommitReq = Boolean.TRUE;	

			EBWLogger.logDebug(this,"Executing Query : "+stmntId);
			EBWLogger.logDebug(this,"Input Parameters mapped"+chkReqTOObj);
			execute(stmntId,chkReqTOObj,isTxnCommitReq);			
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);
			EBWLogger.logDebug(this, "Exiting updateChkTrxnDetails...");
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	public void changeEstPickupTime(CheckRequestTO objCheckRequestTO, String childTxnRequestedExecDate) throws ParseException
	{
		EBWLogger.logDebug(this, "Entering changeEstPickupTime...");
		SimpleDateFormat dateFormat = null;
		String requestedPrintDate = "";
		String pickUpOption = ConvertionUtil.convertToString(objCheckRequestTO.getPickUpOption());
		if (ChkReqConstants.STANDARD_MAIL.equalsIgnoreCase(pickUpOption) || ChkReqConstants.OVERNIGHT_MAIL.equalsIgnoreCase(pickUpOption))
		{
			dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			requestedPrintDate = childTxnRequestedExecDate;
		}
		else
		{		
			dateFormat = new SimpleDateFormat("h:mm a");
			String formattedTime = dateFormat.format(objCheckRequestTO.getEstPickupTime().getTime());
			dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");
			requestedPrintDate = childTxnRequestedExecDate+" "+formattedTime;
		}

		Date estPickupTime = dateFormat.parse(requestedPrintDate);		
		objCheckRequestTO.setEstPickupTime(new Timestamp(estPickupTime.getTime()));
		EBWLogger.logDebug(this, "Exiting changeEstPickupTime...");
	}

	public void insertChkPrintDetails(HashMap txnDetails) throws Exception,SQLException 
	{
		EBWLogger.logDebug(this, "Entering insertChkPrintDetails...");
		try
		{
			String checkStatus = "";

			//Print Check request mappings details..
			Check_Print_Dtls objCheckPrintOut = new Check_Print_Dtls();
			if(txnDetails.containsKey("PrintCheckDetails")){
				objCheckPrintOut = (Check_Print_Dtls)txnDetails.get("PrintCheckDetails");
			}

			if (ChkReqConstants.PRINT_SUCCESS.equalsIgnoreCase(objCheckPrintOut.getPrinterResponse())){
				checkStatus = ChkReqConstants.CHECK_ACTIVE;
			}
			else{
				checkStatus = ChkReqConstants.CHECK_VOID;
			}			
			objCheckPrintOut.setCheck_status(checkStatus);			

			//StatementId and Object mapping for the execute query...
			String stmntId="insertChkPrintDtls";
			Object chkPrintTOObj=objCheckPrintOut;
			Boolean isTxnCommitReq = Boolean.TRUE;	

			EBWLogger.logDebug(this,"Executing Query : "+stmntId);
			EBWLogger.logDebug(this,"Input Parameters mapped"+chkPrintTOObj);
			execute(stmntId,chkPrintTOObj,isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);
			EBWLogger.logDebug(this, "Exiting insertChkPrintDetails...");
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	public void updateChkNoAndDeliveredTo(HashMap txnDetails) throws Exception,SQLException 
	{
		EBWLogger.logDebug(this, "Entering updateChkNoAndDeliveredTo...");
		try
		{
			CheckRequestTO objCheckRequestTO = new CheckRequestTO();			
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequestTO = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Print Check request mappings details..
			Check_Print_Dtls objCheckPrintOut = new Check_Print_Dtls();
			if(txnDetails.containsKey("PrintCheckDetails")){
				objCheckPrintOut = (Check_Print_Dtls)txnDetails.get("PrintCheckDetails");
			}

			objCheckRequestTO.setConfirmationNo(objPaymentDetails.getTransactionId());
			objCheckRequestTO.setCheckNo(objCheckPrintOut.getCheck_no());

			//StatementId and Object mapping for the execute query...
			String stmntId="updateChkNo";
			Object chkReqTOObj=objCheckRequestTO;
			Boolean isTxnCommitReq = Boolean.TRUE;	

			EBWLogger.logDebug(this,"Executing Query : "+stmntId);
			EBWLogger.logDebug(this,"Input Parameters mapped"+chkReqTOObj);
			execute(stmntId,chkReqTOObj,isTxnCommitReq);			
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);
			EBWLogger.logDebug(this, "Exiting updateChkTrxnDetails...");
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}


}
