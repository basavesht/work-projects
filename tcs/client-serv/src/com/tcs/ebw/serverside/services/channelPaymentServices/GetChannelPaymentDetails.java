/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsBatchTO;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.TxnParentTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-07-2011        3               CR 262
 * **********************************************************
 */
public class GetChannelPaymentDetails extends DatabaseService
{
	public void selectPaymentDetails(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Starting selectPaymentDetails method in GetChannelPaymentDetails class");
		Object[] editedTransactionDetails = new Object[2];
		try
		{
			//Mapping Payment attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			Boolean isTxnCommitReq = Boolean.TRUE;

			//Input Mappings for DS_BATCH....
			DsBatchTO currentTxnBatchTO = new DsBatchTO();
			currentTxnBatchTO.setBatbatchref(ConvertionUtil.convertToString(objPaymentDetails.getTxnBatchId()));

			//Input Mappings for DS_PAY_TXNS...
			DsPayTxnsTO currentTxnPaymentTO = new DsPayTxnsTO();
			currentTxnPaymentTO.setPaypayref(ConvertionUtil.convertToString(objPaymentDetails.getTransactionId()));
			currentTxnPaymentTO.setPaybatchref(ConvertionUtil.convertToString(objPaymentDetails.getTxnBatchId()));

			//Statement Id and Object mappings for the getting the transaction details..
			String[] selectPayemntDetailsStmntId={"selectModifiedPayBatchDetails","selectModifiedPayTxnsDetails"};
			Object[] selectPayemntDetailsTOObj={currentTxnBatchTO,currentTxnPaymentTO};
			for (int m= 0; m< selectPayemntDetailsTOObj.length;m++){
				editedTransactionDetails[m]= executeQuery(selectPayemntDetailsStmntId[m],selectPayemntDetailsTOObj[m],isTxnCommitReq);
				EBWLogger.logDebug(this,"Execution Completed.... "+selectPayemntDetailsStmntId[m]);
			} 

			//Populate Transaction OutDetails in the PaymentDetails TO..
			if(objPaymentDetails.isTxnModified()){
				populatePaymentDetails_Edit(txnDetails,(DsBatchTO)editedTransactionDetails[0],(DsPayTxnsTO)editedTransactionDetails[1]);
			}
			else{
				populatePaymentDetails_Complete(txnDetails,(DsBatchTO)editedTransactionDetails[0],(DsPayTxnsTO)editedTransactionDetails[1]);
			}

			//Putting the Transaction Object details in the HashMap..
			txnDetails.put("Current_DsBatchObject", editedTransactionDetails[0]); //DS_BATCH Object.
			txnDetails.put("Current_DsPayTxnObject", editedTransactionDetails[1]); //DS_PAY_TXNS Object.

		}
		catch(SQLException sqlexception) {
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * Populate transaction details ...
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void populatePaymentDetails_Edit(HashMap txnDetails,DsBatchTO currentTxnBatchTO,DsPayTxnsTO currentTxnPaymentTO) throws Exception
	{	
		try 
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Setting the payment attributes...
			objPaymentDetails.setScreen_Type(currentTxnPaymentTO.getPaytypecode());
			objPaymentDetails.setTxnBatchId(currentTxnBatchTO.getBatbatchref());
			objPaymentDetails.setTransactionId(currentTxnPaymentTO.getPaypayref());
			objPaymentDetails.setRecParentTxnId(ConvertionUtil.convertToString(currentTxnPaymentTO.getPar_txn_no()));
			objPaymentDetails.setPrevTxnAmount(ConvertionUtil.convertToString(currentTxnPaymentTO.getPaydebitamt()));
			objPaymentDetails.setPrevTxnDate(ConvertionUtil.convertToString(currentTxnPaymentTO.getRequested_exe_dt()));
			objPaymentDetails.setRsa_Review_Flag(currentTxnPaymentTO.getRsa_review_flag());
			objPaymentDetails.setInitiator_id(currentTxnPaymentTO.getInitiator_id());
			objPaymentDetails.setInitiator_role(currentTxnPaymentTO.getInitiator_role());
			objPaymentDetails.setPrevious_owner_role(currentTxnPaymentTO.getCurrent_owner_role());
			objPaymentDetails.setRta_booked_flag(currentTxnPaymentTO.getRta_booked_in_flag());
			objPaymentDetails.setTxnCurrentStatusCode(currentTxnPaymentTO.getPayccsstatuscode());
			objPaymentDetails.setTxnPrevStatusCode(currentTxnPaymentTO.getPayccsstatuscode());
			objPaymentDetails.setOfac_case_id(currentTxnPaymentTO.getOfac_case_id());

			//Setting the LoanAcnt in case of PLA transactions..
			if(objPaymentDetails.getTransfer_Type()!=null && objPaymentDetails.getTransfer_Type().equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				objPaymentDetails.setLoanAcntNo(currentTxnPaymentTO.getPaypayeeaccnum()); //Loan Account in case of PLA Txns..
			}

			//Setting the MS Account attributes in case Account not in the context..
			setMSAccountDetails(txnDetails,currentTxnPaymentTO);

			//Setting the Loan Account attributes..
			setLoanAcntDetails(txnDetails,currentTxnPaymentTO);
		} 
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Populate Complete transaction details to the PaymentTransaction details TO...
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void populatePaymentDetails_Complete(HashMap txnDetails,DsBatchTO currentTxnBatchTO,DsPayTxnsTO currentTxnPaymentTO) throws Exception
	{	
		try 
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Setting the Payment id attributes...
			objPaymentDetails.setTxnBatchId(currentTxnBatchTO.getBatbatchref());
			objPaymentDetails.setTransactionId(currentTxnPaymentTO.getPaypayref());
			objPaymentDetails.setRecParentTxnId(ConvertionUtil.convertToString(currentTxnPaymentTO.getPar_txn_no()));

			//Setting the Previous transaction attributes..
			objPaymentDetails.setPrevTxnAmount(ConvertionUtil.convertToString(currentTxnPaymentTO.getPaydebitamt()));
			objPaymentDetails.setPrevTxnDate(ConvertionUtil.convertToString(currentTxnPaymentTO.getRequested_exe_dt()));

			//Setting the frequency type...
			if(currentTxnPaymentTO.getPayrecurring()!=null && currentTxnPaymentTO.getPayrecurring().equalsIgnoreCase("N")){
				objPaymentDetails.setFrequency_Type("1");
			}
			else if(currentTxnPaymentTO.getPayrecurring()!=null && currentTxnPaymentTO.getPayrecurring().equalsIgnoreCase("Y")){
				objPaymentDetails.setFrequency_Type("2");
			}

			objPaymentDetails.setTransfer_Amount(ConvertionUtil.convertToString(currentTxnPaymentTO.getPaydebitamt()));
			objPaymentDetails.setChildTxnAmount(ConvertionUtil.convertToString(currentTxnPaymentTO.getPaydebitamt()));
			objPaymentDetails.setTransfer_Type(currentTxnPaymentTO.getTxn_type());
			objPaymentDetails.setTransfer_Currency(currentTxnPaymentTO.getPaycurrcode());
			objPaymentDetails.setTrial_depo(ConvertionUtil.convertToString(currentTxnPaymentTO.getTrial_depo()));
			objPaymentDetails.setInitiation_Date(ConvertionUtil.convertToString(currentTxnPaymentTO.getRequested_exe_dt()));
			objPaymentDetails.setRequestedDate(ConvertionUtil.convertToString(currentTxnPaymentTO.getRequested_exe_dt()));
			objPaymentDetails.setRsa_Review_Flag(currentTxnPaymentTO.getRsa_review_flag());
			objPaymentDetails.setFxRate(ConvertionUtil.convertToString(currentTxnPaymentTO.getPayfxrate()));
			objPaymentDetails.setScreen_Type(currentTxnPaymentTO.getPaytypecode());
			objPaymentDetails.setCurrent_owner_id(currentTxnPaymentTO.getCurrent_owner_id());
			objPaymentDetails.setCurrent_owner_role(currentTxnPaymentTO.getCurrent_owner_role());
			objPaymentDetails.setInitiator_id(currentTxnPaymentTO.getInitiator_id());
			objPaymentDetails.setInitiator_role(currentTxnPaymentTO.getInitiator_role());
			objPaymentDetails.setPrevious_owner_role(currentTxnPaymentTO.getCurrent_owner_role());
			objPaymentDetails.setAuth_mode(currentTxnPaymentTO.getAuth_mode());
			objPaymentDetails.setAuth_info_reciever(currentTxnPaymentTO.getAuth_info_reciever());
			objPaymentDetails.setVerbal_auth_client_name(currentTxnPaymentTO.getVerbal_auth_client_name());
			objPaymentDetails.setVerbal_auth_date(ConvertionUtil.convertToString(currentTxnPaymentTO.getVerbal_auth_date()));
			objPaymentDetails.setVerbal_auth_time(currentTxnPaymentTO.getVerbal_auth_time());
			objPaymentDetails.setClient_verification_desc(currentTxnPaymentTO.getClient_verification_desc());
			objPaymentDetails.setSame_name_flag(currentTxnPaymentTO.getSame_name_flag());
			objPaymentDetails.setRta_booked_flag(currentTxnPaymentTO.getRta_booked_in_flag());
			objPaymentDetails.setTxnCurrentStatusCode(currentTxnPaymentTO.getPayccsstatuscode());
			objPaymentDetails.setTxnPrevStatusCode(currentTxnPaymentTO.getPayccsstatuscode());
			objPaymentDetails.setAuth_confirmed_by(currentTxnPaymentTO.getAuth_confirmed_by());
			objPaymentDetails.setOfac_case_id(currentTxnPaymentTO.getOfac_case_id());

			//Check is done since during ApproveConfirm , dont_spawn_flag will be set from delegate and same needs to propagated,hence no need to overwrite..
			if(objPaymentDetails!=null && (objPaymentDetails.getDont_spawn_flag()== null || objPaymentDetails.getDont_spawn_flag().equals(""))){
				objPaymentDetails.setDont_spawn_flag(currentTxnPaymentTO.getDont_spawn_flag());
			}

			//Setting the LoanAcnt in case of PLA transactions..
			if(objPaymentDetails.getTransfer_Type()!=null && objPaymentDetails.getTransfer_Type().equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				objPaymentDetails.setLoanAcntNo(currentTxnPaymentTO.getPaypayeeaccnum()); //Loan Account in case of PLA Txns..
			}

			//Setting the MS Account attributes in case Account not in the context..
			setMSAccountDetails(txnDetails,currentTxnPaymentTO);

			//Setting the Loan Account attributes..
			setLoanAcntDetails(txnDetails,currentTxnPaymentTO);
		} 
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Executes the query for getting the recurring transfer details..
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public void getRecurringTransferDetails(HashMap txnDetails,ServiceContext context) throws Exception
	{		
		EBWLogger.trace(this, "Setting the input details for getting the recurring transfer details..");
		try 
		{
			Boolean isTxnCommitReq = Boolean.TRUE;
			String getRecTxnDetailsStmnt = "getParentTransactionDetails";
			Object output = null;

			//Mapping the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//StatementId and Object mapping for getting the transaction details..
			TxnParentTO objTxnParentInTO = new TxnParentTO();
			objTxnParentInTO.setPar_txn_no(ConvertionUtil.convertToDouble(objPaymentDetails.getRecParentTxnId()));

			//Service call....
			output = executeQuery(getRecTxnDetailsStmnt,objTxnParentInTO,isTxnCommitReq);
			TxnParentTO objTxnParentOutTO = new TxnParentTO();
			objTxnParentOutTO = (TxnParentTO)output;

			//Populate Transaction OutDetails in the PaymentDetails TO..
			if(objPaymentDetails.isTxnModified()){
				populateRecurringDetails_Edit(txnDetails,(TxnParentTO)objTxnParentOutTO);
			}
			else{
				populateRecurringDetails_Complete(txnDetails,(TxnParentTO)objTxnParentOutTO);
			}
		} 
		catch (SQLException exception) {
			throw exception;
		} 
		catch (Exception exception) {
			throw exception;
		}
		finally {

		}
	}

	/**
	 * Updating the complete recurring transactions based on the output...
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void populateRecurringDetails_Edit(HashMap txnDetails,TxnParentTO objTxnParentOutTO) throws Exception
	{
		try 
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Setting the Fee applicable flag in case of any fee required for RTA processing...
			if(objTxnParentOutTO!=null)
			{
				//Updating the recurring transaction payment details...
				objPaymentDetails.setAccumulated_amount(objTxnParentOutTO.getAccum_amt());
				objPaymentDetails.setAccumulated_transfers(objTxnParentOutTO.getAccum_txn_no());
				objPaymentDetails.setPrefered_next_txn_dt(objTxnParentOutTO.getPrefered_previous_txn_dt());
				objPaymentDetails.setNext_txn_dt(objTxnParentOutTO.getNext_txn_dt());
				objPaymentDetails.setParTxnRequestDate(objTxnParentOutTO.getPar_txn_request_dt());
			}
		} 
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Updating the complete recurring transactions based on the output...
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void populateRecurringDetails_Complete(HashMap txnDetails,TxnParentTO objTxnParentOutTO) throws Exception
	{
		try 
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Setting the Fee applicable flag in case of any fee required for RTA processing...
			if(objTxnParentOutTO!=null)
			{
				//Updating the recurring transaction payment details...
				objPaymentDetails.setFrequency_DurationValue(objTxnParentOutTO.getDuration());
				objPaymentDetails.setFrequency_DurationDesc(objTxnParentOutTO.getFrequency());
				objPaymentDetails.setDuration_AmountLimit(ConvertionUtil.convertToString(objTxnParentOutTO.getThrshold_amt()));
				objPaymentDetails.setDuration_EndDate(ConvertionUtil.convertToString(objTxnParentOutTO.getUntildate()));
				objPaymentDetails.setDuration_NoOfTransfers(ConvertionUtil.convertToString(objTxnParentOutTO.getMax_txn_no()));
				objPaymentDetails.setAccumulated_amount(objTxnParentOutTO.getAccum_amt());
				objPaymentDetails.setAccumulated_transfers(objTxnParentOutTO.getAccum_txn_no());
				objPaymentDetails.setPrefered_next_txn_dt(objTxnParentOutTO.getPrefered_previous_txn_dt());
				objPaymentDetails.setNext_txn_dt(objTxnParentOutTO.getNext_txn_dt());
				objPaymentDetails.setParTxnRequestDate(objTxnParentOutTO.getPar_txn_request_dt());
			}
		} 
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Setting the account details in case the account is not present in the context...
	 * @param txnDetails
	 * @param currentTxnPaymentTO
	 */
	public void setMSAccountDetails(HashMap txnDetails,DsPayTxnsTO currentTxnPaymentTO)
	{
		//Mapping the payment detail object with the Payment details...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		if(txnDetails.containsKey("PaymentDetails")){
			objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
		}

		FromMSAcc_DetailsTO objFromMSAcc_DetailsTO = new FromMSAcc_DetailsTO();
		if(txnDetails.containsKey("MSFromAccDetails")){
			objFromMSAcc_DetailsTO = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
		}

		ToMSAcc_DetailsTO objToMSAcc_DetailsTO = new ToMSAcc_DetailsTO();
		if(txnDetails.containsKey("MSToAccDetails")){
			objToMSAcc_DetailsTO = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
		}

		String transfer_Type = objPaymentDetails.getTransfer_Type();

		//In case from account is not present in the context , we need to set the From_Acc object from the previous transaction details..
		if(!objPaymentDetails.isFrmAcc_InContext() && (transfer_Type!=null && (transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transfer_Type.startsWith(TxnTypeCode.INTERNAL) || transfer_Type.startsWith(TxnTypeCode.PORTFOLIO_LOAN))))
		{
			String fromMsOffice = currentTxnPaymentTO.getFrombr_acct_no_fa().substring(0,3);
			String fromMsAccNo = currentTxnPaymentTO.getFrombr_acct_no_fa().substring(3,9);
			String fromMsFaId = currentTxnPaymentTO.getFrombr_acct_no_fa().substring(9);

			objFromMSAcc_DetailsTO.setAccountCategory(null);
			objFromMSAcc_DetailsTO.setAccountClass(null);
			objFromMSAcc_DetailsTO.setAccountNumber(fromMsAccNo);
			objFromMSAcc_DetailsTO.setChoiceFundCode(null);
			objFromMSAcc_DetailsTO.setClientCategory(null);
			objFromMSAcc_DetailsTO.setCollateralAcctInd(null);
			objFromMSAcc_DetailsTO.setDivPay(null);
			objFromMSAcc_DetailsTO.setFaNumber(fromMsFaId);
			objFromMSAcc_DetailsTO.setFriendlyName(null);
			objFromMSAcc_DetailsTO.setIraCode(null);
			objFromMSAcc_DetailsTO.setKeyAccount(currentTxnPaymentTO.getKeyaccountnumber_from());
			objFromMSAcc_DetailsTO.setNickName(null);
			objFromMSAcc_DetailsTO.setNovusSubProduct(null);
			objFromMSAcc_DetailsTO.setOfficeNumber(fromMsOffice);
			objFromMSAcc_DetailsTO.setStatus(null);
			objFromMSAcc_DetailsTO.setTradeControl(null);
			objFromMSAcc_DetailsTO.setViewTransactionFlag(false);
		}

		//In case To account is not present in the context , we need to set the From_Acc object from the previous transaction details..
		if(!objPaymentDetails.isToAcc_InContext()&& (transfer_Type!=null && (transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || transfer_Type.equalsIgnoreCase(TxnTypeCode.INTERNAL))))
		{
			String toMsOffice = currentTxnPaymentTO.getTobr_acct_no_fa().substring(0,3);
			String toMsAccNo = currentTxnPaymentTO.getTobr_acct_no_fa().substring(3,9);
			String toMsFaId = currentTxnPaymentTO.getTobr_acct_no_fa().substring(9);

			objToMSAcc_DetailsTO.setAccountCategory(null);
			objToMSAcc_DetailsTO.setAccountClass(null);
			objToMSAcc_DetailsTO.setAccountNumber(toMsAccNo);
			objToMSAcc_DetailsTO.setChoiceFundCode(null);
			objToMSAcc_DetailsTO.setClientCategory(null);
			objToMSAcc_DetailsTO.setCollateralAcctInd(null);
			objToMSAcc_DetailsTO.setDivPay(null);
			objToMSAcc_DetailsTO.setFaNumber(toMsFaId);
			objToMSAcc_DetailsTO.setFriendlyName(null);
			objToMSAcc_DetailsTO.setIraCode(null);
			objToMSAcc_DetailsTO.setKeyAccount(currentTxnPaymentTO.getKeyaccountnumber_to());
			objToMSAcc_DetailsTO.setNickName(null);
			objToMSAcc_DetailsTO.setNovusSubProduct(null);
			objToMSAcc_DetailsTO.setOfficeNumber(toMsOffice);
			objToMSAcc_DetailsTO.setStatus(null);
			objToMSAcc_DetailsTO.setTradeControl(null);
			objToMSAcc_DetailsTO.setViewTransactionFlag(false);
		}
	}

	/**
	 * Setting the loan Account details ...
	 * @param txnDetails
	 */
	public void setLoanAcntDetails(HashMap txnDetails,DsPayTxnsTO currentTxnPaymentTO)
	{
		//Mapping the payment detail object with the Payment details...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		if(txnDetails.containsKey("PaymentDetails")){
			objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
		}

		String transfer_Type = objPaymentDetails.getTransfer_Type();
		if(transfer_Type!=null && (transfer_Type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
			PortfolioLoanAccount loanAcnt = new PortfolioLoanAccount();
			loanAcnt.setLoanAccount(currentTxnPaymentTO.getPaypayeeaccnum());
			txnDetails.put("LoanAccountDetails",loanAcnt);
		}
	}
}
