package com.tcs.ebw.serverside.services.channelPaymentServices;

import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_DTL;
import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.FormatAccount;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.TrxnDetailsOutputTO;
import com.tcs.ebw.payments.transferobject.TxnVersionDetails;
import com.tcs.ebw.serverside.services.DatabaseService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       05-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 *    224703       23-09-2011        P3-B            PLA  
 * **********************************************************
 */
public class ExecuteTxnViewDetails extends DatabaseService
{

	public ExecuteTxnViewDetails(){

	}

	/**
	 * Execute the transaction view details...
	 * @param txnDetails
	 * @param context
	 * @throws Exception
	 */
	public void executeTxnViewDetails(HashMap txnDetails,ServiceContext context) throws Exception
	{
		try
		{
			// Get the latest reason codes to display in the ReasonCodes Table .....
			EBWLogger.logDebug(this, "Getting the ReasonCodes for the selected transaction..");
			GetReasonCodes objgetReasonCodes= new GetReasonCodes();
			objgetReasonCodes.setConnection(serviceConnection);
			objgetReasonCodes.getBrReasonCodes(txnDetails,context);
			EBWLogger.logDebug(this, "Finished getting the ReasonCodes for the selected transaction..");

			// Get the latest transaction action details to display in the User Action table .....
			EBWLogger.logDebug(this, "Getting the Action Log details for the selected transaction..");
			GetTxnActionDetails objgetTxnActionLog= new GetTxnActionDetails();
			objgetTxnActionLog.setConnection(serviceConnection);
			objgetTxnActionLog.getTxnActionLogDetails(txnDetails,context);
			EBWLogger.logDebug(this, "Finished getting the Action Log for the selected transaction..");

			// Get the latest transaction client authentications details to display in the client authorization table..
			EBWLogger.logDebug(this, "Getting the client authentications details for the selected transaction..");
			GetClientVerificationDtls objclientAuthDetails= new GetClientVerificationDtls();
			objclientAuthDetails.setConnection(serviceConnection);
			objclientAuthDetails.getClientVerificationDetails(txnDetails,context);
			EBWLogger.logDebug(this, "Finished getting the client authentications for the selected transaction..");

			// Get the latest transaction version details .....
			EBWLogger.logDebug(this, "Getting the GetTxnVersionDetails for the selected transaction..");
			GetTxnVersionDetails objgetTxnVersionDetails= new GetTxnVersionDetails();
			objgetTxnVersionDetails.setConnection(serviceConnection);
			objgetTxnVersionDetails.getTxnVersionDetails(txnDetails,context);
			EBWLogger.logDebug(this, "Finished getting the GetTxnVersionDetails for the selected transaction..");

			// Get the latest TxnProperties details .....
			EBWLogger.logDebug(this, "Getting the TxnProperties for the selected transaction..");
			TxnProperties objTxnProperties= new TxnProperties();
			objTxnProperties.setConnection(serviceConnection);
			objTxnProperties.getIRATxnViewProperties(txnDetails,context);
			EBWLogger.logDebug(this, "Finished getting the TxnProperties for the selected transaction..");

			//Mapping the payment view details attributes...
			populatePaymentViewDetails(txnDetails,context);

			EBWLogger.logDebug(this, "Executed executeTxnViewDetails");
			EBWLogger.trace(this, "Finished with executeTxnViewDetails method of GetTransactionDetailsService class");
		}
		catch(SQLException sqlexception) {
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * Populate Complete transaction details to the PaymentTransaction details TO...
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void populatePaymentViewDetails(HashMap txnDetails,ServiceContext context) throws Exception
	{	
		//Mapping the Transaction details object...
		TrxnDetailsOutputTO txnViewDetails = new TrxnDetailsOutputTO();
		try 
		{
			//Mapping the internal account details from the account view web service...
			setIntAcntPlating(txnDetails,txnViewDetails,context);

			//Mapping the external account details..
			setExtAcntPlating(txnDetails,txnViewDetails,context);

			//Mapping the payment details...
			setTransactionDtls(txnDetails,txnViewDetails,context);

			//Mapping the BR reason codes and Activity Logs...
			setTxnActivityLog(txnDetails,txnViewDetails,context);

			//Check transactions mappings details...
			setCheckTxnDtls(txnDetails,txnViewDetails,context);

			//Loan transactions mappings details...
			setLoanTxnDtls(txnDetails,txnViewDetails,context);

			//Formatting the transaction view details..
			MSCommonUtils.formatAccDetails(txnViewDetails);

			//Output mappings...
			txnDetails.put("TxnViewDetails",txnViewDetails);
		} 
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Setting the acnt plating attributes for the MS account involved in the transaction....
	 * @throws Exception 
	 */
	public void setIntAcntPlating(HashMap txnDetails,TrxnDetailsOutputTO txnViewDetails,ServiceContext context) throws Exception
	{
		try
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the from account attributes...
			FromMSAcc_DetailsTO objFromMSAcc_DetailsTO = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_DetailsTO = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//Mapping the to account attributes...
			ToMSAcc_DetailsTO objToMSAcc_DetailsTO = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_DetailsTO = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			//Mapping the AccountView details object...
			AccountDetails objAccDetails = new AccountDetails();
			if(txnDetails.containsKey("MerlinOutputDetails")){
				objAccDetails = (AccountDetails)txnDetails.get("MerlinOutputDetails");
			}

			AccountPlating acntPlating = new AccountPlating();
			String transferType= objPaymentDetails.getTransfer_Type();
			ArrayList<Object> merlinOutputList = (ArrayList)objAccDetails.getInfoMerlinObject();
			for(int i = 0; i < merlinOutputList.size(); i++)
			{
				//Internal MS Account Name extraction...
				MS_ACCOUNT_OUT_DTL merlinOutputObj = (MS_ACCOUNT_OUT_DTL)merlinOutputList.get(i);

				if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL))
				{
					if(MSCommonUtils.getMSAccFormat(objFromMSAcc_DetailsTO.getAccountNumber()).equalsIgnoreCase(MSCommonUtils.getMSAccFormat(merlinOutputObj.getACCOUNT_NO())))
					{
						//Getting the account plating details...
						acntPlating = getDebitAcntPlatingInfo(txnDetails,context);
						if(acntPlating!=null){
							txnViewDetails.setAccNameFromAcc(acntPlating.getAcntUserName());
							txnViewDetails.setAccDtlsFromAcc(acntPlating.getAcntUserDetails());
						}
						txnViewDetails.setAccClassFromAcc(merlinOutputObj.getACNT_CLS_DESC());
						txnViewDetails.setAccSubClassFromAcc(merlinOutputObj.getSUB_CLASS_DESC());
						txnViewDetails.setAccTierFromAcc(merlinOutputObj.getCATTIER());
						txnViewDetails.setRoutingNoFromAcc(null);
						txnViewDetails.setAccHeldAtFromAcc(MSSystemDefaults.MSACC_HELD_AT);
					} 
					else if(MSCommonUtils.getMSAccFormat(objToMSAcc_DetailsTO.getAccountNumber()).equalsIgnoreCase(MSCommonUtils.getMSAccFormat(merlinOutputObj.getACCOUNT_NO())))
					{
						//Getting the account plating details...
						acntPlating = getCreditAcntPlatingInfo(txnDetails,context);
						if(acntPlating!=null){
							txnViewDetails.setAccNameToAcc(acntPlating.getAcntUserName());
							txnViewDetails.setAccDtlsToAcc(acntPlating.getAcntUserDetails());
						}
						txnViewDetails.setAccClassToAcc(merlinOutputObj.getACNT_CLS_DESC());
						txnViewDetails.setAccSubClassToAcc(merlinOutputObj.getSUB_CLASS_DESC());
						txnViewDetails.setAccTierToAcc(merlinOutputObj.getCATTIER());
						txnViewDetails.setRoutingNoToAcc(null);
						txnViewDetails.setAccHeldAtToAcc(MSSystemDefaults.MSACC_HELD_AT);
					}
				}
				else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
				{
					if(MSCommonUtils.getMSAccFormat(objFromMSAcc_DetailsTO.getAccountNumber()).equalsIgnoreCase(MSCommonUtils.getMSAccFormat(merlinOutputObj.getACCOUNT_NO())))
					{
						//Getting the account plating details...
						acntPlating = getDebitAcntPlatingInfo(txnDetails,context);
						if(acntPlating!=null){
							txnViewDetails.setAccNameFromAcc(acntPlating.getAcntUserName());
							txnViewDetails.setAccDtlsFromAcc(acntPlating.getAcntUserDetails());
						}
						txnViewDetails.setAccClassFromAcc(merlinOutputObj.getACNT_CLS_DESC());
						txnViewDetails.setAccSubClassFromAcc(merlinOutputObj.getSUB_CLASS_DESC());
						txnViewDetails.setAccTierFromAcc(merlinOutputObj.getCATTIER());
						txnViewDetails.setRoutingNoFromAcc(null);
						txnViewDetails.setAccHeldAtFromAcc(MSSystemDefaults.MSACC_HELD_AT);
					} 
				}
				else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))
				{
					if(MSCommonUtils.getMSAccFormat(objToMSAcc_DetailsTO.getAccountNumber()).equalsIgnoreCase(MSCommonUtils.getMSAccFormat(merlinOutputObj.getACCOUNT_NO())))
					{
						//Getting the account plating details...
						acntPlating = getCreditAcntPlatingInfo(txnDetails,context);
						if(acntPlating!=null){
							txnViewDetails.setAccNameToAcc(acntPlating.getAcntUserName());
							txnViewDetails.setAccDtlsToAcc(acntPlating.getAcntUserDetails());
						}
						txnViewDetails.setAccClassToAcc(merlinOutputObj.getACNT_CLS_DESC());
						txnViewDetails.setAccSubClassToAcc(merlinOutputObj.getSUB_CLASS_DESC());
						txnViewDetails.setAccTierToAcc(merlinOutputObj.getCATTIER());
						txnViewDetails.setRoutingNoToAcc(null);
						txnViewDetails.setAccHeldAtToAcc(MSSystemDefaults.MSACC_HELD_AT);
					} 
				}
				else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK))
				{
					if(MSCommonUtils.getMSAccFormat(objFromMSAcc_DetailsTO.getAccountNumber()).equalsIgnoreCase(MSCommonUtils.getMSAccFormat(merlinOutputObj.getACCOUNT_NO())))
					{
						//Getting the account plating details...
						acntPlating = getDebitAcntPlatingInfo(txnDetails,context);
						if(acntPlating!=null){
							txnViewDetails.setAccNameFromAcc(acntPlating.getAcntUserName());
							txnViewDetails.setAccDtlsFromAcc(acntPlating.getAcntUserDetails());
						}
						txnViewDetails.setAccClassFromAcc(merlinOutputObj.getACNT_CLS_DESC());
						txnViewDetails.setAccSubClassFromAcc(merlinOutputObj.getSUB_CLASS_DESC());
						txnViewDetails.setAccTierFromAcc(merlinOutputObj.getCATTIER());
						txnViewDetails.setRoutingNoFromAcc(null);
						txnViewDetails.setAccHeldAtFromAcc(MSSystemDefaults.MSACC_HELD_AT);
					} 
				}
				else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))
				{
					if(MSCommonUtils.getMSAccFormat(objFromMSAcc_DetailsTO.getAccountNumber()).equalsIgnoreCase(MSCommonUtils.getMSAccFormat(merlinOutputObj.getACCOUNT_NO())))
					{
						//Getting the account plating details...
						acntPlating = getDebitAcntPlatingInfo(txnDetails,context);
						if(acntPlating!=null){
							txnViewDetails.setAccNameFromAcc(acntPlating.getAcntUserName());
							txnViewDetails.setAccDtlsFromAcc(acntPlating.getAcntUserDetails());
						}
						txnViewDetails.setAccClassFromAcc(merlinOutputObj.getACNT_CLS_DESC());
						txnViewDetails.setAccSubClassFromAcc(merlinOutputObj.getSUB_CLASS_DESC());
						txnViewDetails.setAccTierFromAcc(merlinOutputObj.getCATTIER());
						txnViewDetails.setRoutingNoFromAcc(null);
						txnViewDetails.setAccHeldAtFromAcc(MSSystemDefaults.MSACC_HELD_AT);
					} 
				}
			}
		} 
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Setting the acnt plating attributes for the External account involved in the transactios....
	 * @throws Exception 
	 */
	public void setExtAcntPlating(HashMap txnDetails,TrxnDetailsOutputTO txnViewDetails,ServiceContext context) throws Exception
	{
		try 
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External Account details...
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			String transferType= objPaymentDetails.getTransfer_Type();
			if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				txnViewDetails.setOwnerFromAcc(FormatAccount.getExtAcntOwner_View(objExternalAccDetails));
				txnViewDetails.setRoutingNoFromAcc(objExternalAccDetails.getCpybankcode());
				txnViewDetails.setAccHeldAtFromAcc(objExternalAccDetails.getCpyboname1());
				txnViewDetails.setAccNameFromAcc(objExternalAccDetails.getNick_name());
				txnViewDetails.setAccDtlsFromAcc(null);
			}
			else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				txnViewDetails.setOwnerToAcc(FormatAccount.getExtAcntOwner_View(objExternalAccDetails));
				txnViewDetails.setRoutingNoToAcc(objExternalAccDetails.getCpybankcode());
				txnViewDetails.setAccHeldAtToAcc(objExternalAccDetails.getCpyboname1());
				txnViewDetails.setAccNameToAcc(objExternalAccDetails.getNick_name());
				txnViewDetails.setAccDtlsToAcc(null);
			}
		}
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Setting the acnt plating attributes for the Loan account involved in the transactions....
	 * @throws Exception 
	 */
	public void setLoanTxnDtls(HashMap txnDetails,TrxnDetailsOutputTO txnViewDetails,ServiceContext context) throws Exception
	{
		try 
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Loan Account Details ..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			if(txnDetails.containsKey("LoanAccountDetails")){
				objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
			}

			String transferType= objPaymentDetails.getTransfer_Type();
			if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				txnViewDetails.setAccHeldAtToAcc(MSSystemDefaults.LOANACC_HELD_AT);
				txnViewDetails.setAccNameToAcc(objLoanAcntDetails.getLoanAcntClientName());
				txnViewDetails.setAccDtlsToAcc(MSSystemDefaults.LOANACC_DTLS);
			}
		}
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Setting the transaction details if applicable...
	 * @throws Exception 
	 */
	public void setTransactionDtls(HashMap txnDetails,TrxnDetailsOutputTO txnViewDetails,ServiceContext context) throws Exception 
	{
		try 
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the from account attributes...
			FromMSAcc_DetailsTO objFromMSAcc_DetailsTO = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_DetailsTO = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//Mapping the to account attributes...
			ToMSAcc_DetailsTO objToMSAcc_DetailsTO = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_DetailsTO = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			//Mapping the AccountView details object...
			TxnVersionDetails txnVersionDetails = new TxnVersionDetails();
			if(txnDetails.containsKey("TxnVersionDetails")){
				txnVersionDetails = (TxnVersionDetails)txnDetails.get("TxnVersionDetails");
			}

			String debitAccount_Num = FormatAccount.getDebitAccountNum_View(txnDetails);
			String creditAccount_Num = FormatAccount.getCreditAccountNum_View(txnDetails);
			String freq_type = objPaymentDetails.getFrequency_Type();
			String frequencyValue = objPaymentDetails.getFrequency_DurationDesc();
			String transferType = objPaymentDetails.getTransfer_Type();
			Date last_transfer_date = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
			Date parentTxnDate = objPaymentDetails.getParTxnRequestDate();

			txnViewDetails.setAccTypeFromAcc(MSCommonUtils.getFrom_AccType(objPaymentDetails));
			txnViewDetails.setAccTypeToAcc(MSCommonUtils.getTo_AccType(objPaymentDetails));
			txnViewDetails.setAccNoFromAcc(debitAccount_Num);
			txnViewDetails.setAccNoToAcc(creditAccount_Num);
			txnViewDetails.setOfficeAccNoFromAcc(objFromMSAcc_DetailsTO.getOfficeNumber());
			txnViewDetails.setOfficeAccNoToAcc(objToMSAcc_DetailsTO.getOfficeNumber());
			txnViewDetails.setFaIdFromAcc(objFromMSAcc_DetailsTO.getFaNumber());
			txnViewDetails.setFaIdToAcc(objToMSAcc_DetailsTO.getFaNumber());
			txnViewDetails.setConfirmationNo(MSCommonUtils.formatConfirmationNum(objPaymentDetails));
			txnViewDetails.setTrxnAmount(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			txnViewDetails.setTrxnType(MSCommonUtils.getTxnTypeDesc_View(objPaymentDetails));
			txnViewDetails.setFrequencyType(MSCommonUtils.formatFrequency(objPaymentDetails));
			txnViewDetails.setStatus(MSCommonUtils.getTxnStatusDesc_View(objPaymentDetails));
			txnViewDetails.setPaybatchref(objPaymentDetails.getTxnBatchId());
			txnViewDetails.setStatusCode(objPaymentDetails.getTxnCurrentStatusCode());
			txnViewDetails.setTxnTypeCode(objPaymentDetails.getTransfer_Type());
			txnViewDetails.setVersionnum(txnVersionDetails.getVersionnum());
			txnViewDetails.setBatversionnum(txnVersionDetails.getBatversionnum());
			txnViewDetails.setPartxnversionnum(txnVersionDetails.getPartxnversionnum());
			txnViewDetails.setTxnEntryDate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getCreatedDate()));
			txnViewDetails.setTxnSettleDate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getActualExecDate()));
			txnViewDetails.setTxnInitiationDate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			txnViewDetails.setBusiness_Date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			txnViewDetails.setRetirement_TxnTypeDesc(MSCommonUtils.getRetirementType(objPaymentDetails));
			txnViewDetails.setQualifier(objPaymentDetails.getQualifier());
			txnViewDetails.setReverse_qualifier(objPaymentDetails.getReverse_qualifier());
			txnViewDetails.setDisplay_qualifier(objPaymentDetails.getDisplay_qualifier());
			txnViewDetails.setRetirement_mnemonic(objPaymentDetails.getRetirement_mnemonic());
			txnViewDetails.setUntilDollarLimit(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
			txnViewDetails.setRecurringFlag(objPaymentDetails.getFrequency_Type());
			txnViewDetails.setAuth_mode(objPaymentDetails.getAuth_mode());
			txnViewDetails.setOfac_case_id(objPaymentDetails.getOfac_case_id());

			//Check for Recurring criteria active flag only in case the transfers are recurring....
			if(freq_type!=null && freq_type.equalsIgnoreCase("2"))
			{
				//The functions to calculate next business date and preferred next business date for the TXN_PARENT Entry...
				Date nextTransferDate = new Date();
				nextTransferDate = MSCommonUtils.evaluateNextTxnDate(frequencyValue,last_transfer_date,parentTxnDate,transferType,context);
				objPaymentDetails.setNext_txn_dt(ConvertionUtil.convertToTimestamp(nextTransferDate));

				boolean isRecurringActive = CheckRecurringStatus.checkRecurringStatus(txnDetails);
				if(isRecurringActive)
					txnViewDetails.setRecurringActiveFlg("Y");
				else 
					txnViewDetails.setRecurringActiveFlg("N");
			}
		} 
		catch (Exception exception){
			throw exception;
		}
	}
	/**
	 * Setting the Check transaction details if applicable...
	 * @throws Exception 
	 */
	public void setCheckTxnDtls(HashMap txnDetails,TrxnDetailsOutputTO txnViewDetails,ServiceContext context) throws Exception
	{
		try 
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			String transferType= objPaymentDetails.getTransfer_Type();
			if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)) 
			{
				txnViewDetails.setRequestedPrintDate(MSCommonUtils.getCheckPrintDateFormat(txnDetails));
				txnViewDetails.setCertifiedFlag(MSCommonUtils.getCheckCertifiedDesc(objCheckRequest));
				txnViewDetails.setCheckNo(objCheckRequest.getCheckNo());
				txnViewDetails.setTrackingId(objCheckRequest.getTrackingId());
				txnViewDetails.setPayeeToName(objCheckRequest.getPayeeName());
				txnViewDetails.setPrintingBranch(objCheckRequest.getPrintingBranch());
				txnViewDetails.setPrintMemoOn(MSCommonUtils.getPrintMemoOn(objCheckRequest));
				txnViewDetails.setDeliveredToName(objCheckRequest.getDeliveredToName());
				txnViewDetails.setDeliveredToType(objCheckRequest.getDeliveredToType());
				txnViewDetails.setTypeOfId(MSCommonUtils.getCheckPrintTypeOfId(objCheckRequest));
				txnViewDetails.setIdNumber(objCheckRequest.getIdNumber());
				txnViewDetails.setFee(MSCommonUtils.setFeeAmntDisplay(txnDetails));
				txnViewDetails.setThirdPartyReason(MSCommonUtils.getThirdPartyReason(objCheckRequest));
				txnViewDetails.setDeliveryAddress(MSCommonUtils.getDeliveryAddress(objCheckRequest));
				txnViewDetails.setCheckMemo(MSCommonUtils.getCheckMemo(objCheckRequest));
				txnViewDetails.setForeignAddressFlag(objCheckRequest.getForeignAddressFlag());
				txnViewDetails.setDefaultAddressFlag(objCheckRequest.getDefaultAddressFlag());
				txnViewDetails.setPrintMemoCheckFlag(objCheckRequest.getPrintMemoCheckFlag());
				txnViewDetails.setPrintMemoStubFlag(objCheckRequest.getPrintMemoStubFlag());
				txnViewDetails.setSameNameFlag(objPaymentDetails.getSame_name_flag());
				txnViewDetails.setDeliveryOption(MSCommonUtils.getChkDeliveryOption(objPaymentDetails,objCheckRequest));
			}
		}
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Setting the BR log, Activity log ,VerbalAuthorisation log ...
	 * @param txnDetails
	 * @param txnViewDetails
	 * @param context
	 * @throws Exception
	 */
	public void setTxnActivityLog(HashMap txnDetails,TrxnDetailsOutputTO txnViewDetails,ServiceContext context) throws Exception
	{
		try 
		{
			//Mapping the AccountView details object...
			ArrayList brReasonCodes = new ArrayList();
			if(txnDetails.containsKey("BRReasonCodes")){
				brReasonCodes = (ArrayList)txnDetails.get("BRReasonCodes");
			}

			//Mapping the AccountView details object...
			ArrayList txnActionDetails = new ArrayList();
			if(txnDetails.containsKey("TxnActionDetails")){
				txnActionDetails = (ArrayList)txnDetails.get("TxnActionDetails");
			}

			//Mapping the Client Verbal authentication details object if present...
			ArrayList verbalAuthDetails = new ArrayList();
			if(txnDetails.containsKey("VerbalAuthDetails")){
				verbalAuthDetails = (ArrayList)txnDetails.get("VerbalAuthDetails");
			}

			//Mapping the Client Signed authentication details object if present...
			ArrayList signedAuthDetails = new ArrayList();
			if(txnDetails.containsKey("SignedAuthDetails")){
				signedAuthDetails = (ArrayList)txnDetails.get("SignedAuthDetails");
			}

			txnViewDetails.setReasonCodesList(brReasonCodes);
			txnViewDetails.setActionsList(txnActionDetails);
			txnViewDetails.setAuthDetailsList(verbalAuthDetails);
			txnViewDetails.setSignedAuthDetailsList(signedAuthDetails);
		} 
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Getting the Debit MS account Name and account Details for the particular account passed... 
	 * @param txnDetails
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public AccountPlating getDebitAcntPlatingInfo(HashMap txnDetails,ServiceContext context) throws Exception
	{
		AccountPlating acntPlating = new AccountPlating();
		try 
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the from account attributes...
			FromMSAcc_DetailsTO objFromMSAcc_DetailsTO = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_DetailsTO = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			String transferType= objPaymentDetails.getTransfer_Type();
			if(!objPaymentDetails.isDebitAcntDataInSession())
			{
				//Removing the acnt plating for the credit account from session if present....
				if(txnDetails.containsKey("AcntPlatingOutputDetails")){
					txnDetails.remove("AcntPlatingOutputDetails");
				}

				//Getting the account properties if required only ..
				AccountProperties acntProperties = new AccountProperties();
				acntProperties.setConnection(serviceConnection);
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
					acntProperties.getDebitAcountProps(txnDetails,context);
					if (context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity() == MessageType.SEVERE){
						return null; 
					}
				}

				//Output extraction from account plating response...
				if(txnDetails.containsKey("AcntPlatingOutputDetails")){
					acntPlating = (AccountPlating)txnDetails.get("AcntPlatingOutputDetails");
				}
			}
			else {
				acntPlating = objFromMSAcc_DetailsTO.getAccountPlating();
			}
		}
		catch (Exception exception){
			throw exception;
		}
		return acntPlating;
	}

	/**
	 * Getting the Credit MS account Name and account Details for the particular account passed... 
	 * @param txnDetails
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public AccountPlating getCreditAcntPlatingInfo(HashMap txnDetails,ServiceContext context) throws Exception
	{
		AccountPlating acntPlating = new AccountPlating();
		try 
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the to account attributes...
			ToMSAcc_DetailsTO objToMSAcc_DetailsTO = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_DetailsTO = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			String transferType= objPaymentDetails.getTransfer_Type();

			if(!objPaymentDetails.isCreditAcntDataInSession())
			{
				//Removing the acnt plating for the debit account from session if present....
				if(txnDetails.containsKey("AcntPlatingOutputDetails")){
					txnDetails.remove("AcntPlatingOutputDetails");
				}

				//Getting the account properties if required only ..
				AccountProperties acntProperties = new AccountProperties();
				acntProperties.setConnection(serviceConnection);
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL))){
					acntProperties.getCreditAcountProps(txnDetails,context);
					if (context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity() == MessageType.SEVERE){
						return null; 
					}
				}

				//Output extraction from account plating response...
				if(txnDetails.containsKey("AcntPlatingOutputDetails")){
					acntPlating = (AccountPlating)txnDetails.get("AcntPlatingOutputDetails");
				}
			}
			else {
				acntPlating = objToMSAcc_DetailsTO.getAccountPlating();
			}
		}
		catch (Exception exception){
			throw exception;
		}
		return acntPlating;
	}
}