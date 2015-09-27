package com.tcs.Payments.ms360Utils;

import java.util.HashMap;

import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class FormatAccount 
{
	/**
	 * Formats the Debit side MS/External Account number display in  all the Modals window and PreConfirmation screens...
	 * @param accountNumber
	 * @return
	 */
	public static String getDebitAccountDisp(HashMap txnDetails)
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

		DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
		if(txnDetails.containsKey("ExternalAccDetails")){
			objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
		}

		StringBuffer accountDisplay = new StringBuffer();
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transfer_type.startsWith(ChkReqConstants.CHK))){
			accountDisplay.append(objFromMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getFaNumber());
			accountDisplay.append(" ");
			if(objFromMSAcc_DetailsTO.getAccountName()!=null){
				accountDisplay.append(objFromMSAcc_DetailsTO.getAccountName());
			}
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			accountDisplay.append(objFromMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getFaNumber());
			accountDisplay.append(" ");
			if(objFromMSAcc_DetailsTO.getAccountName()!=null){
				accountDisplay.append(objFromMSAcc_DetailsTO.getAccountName());
			}
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			accountDisplay.append(formatExternalAccount(objExternalAccDetails));
		}
		return accountDisplay.toString();
	}

	/**
	 * Formats the Credit side MS/External Account number display in  all the Modals window and PreConfirmation screens...
	 * @param accountNumber
	 * @return
	 */
	public static String getCreditAccountDisp(HashMap txnDetails)
	{
		//Mapping the payment detail object with the Payment details...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		if(txnDetails.containsKey("PaymentDetails")){
			objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
		}

		//Mapping the from account attributes...
		ToMSAcc_DetailsTO objToMSAcc_DetailsTO = new ToMSAcc_DetailsTO();
		if(txnDetails.containsKey("MSToAccDetails")){
			objToMSAcc_DetailsTO = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
		}

		//External accounts details...
		DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
		if(txnDetails.containsKey("ExternalAccDetails")){
			objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
		}

		//Check request mappings details..
		CheckRequestTO objCheckRequest = new CheckRequestTO();
		if(txnDetails.containsKey("CheckDetails")){
			objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
		}

		StringBuffer accountDisplay = new StringBuffer();
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
			accountDisplay.append(objToMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getFaNumber());
			accountDisplay.append(" ");
			if(objToMSAcc_DetailsTO.getAccountName()!=null){
				accountDisplay.append(objToMSAcc_DetailsTO.getAccountName());
			}
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			accountDisplay.append(formatExternalAccount(objExternalAccDetails));
		}
		else if(transfer_type!=null && transfer_type.startsWith(ChkReqConstants.CHK)){
			accountDisplay.append(objCheckRequest.getPayeeName());
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			accountDisplay.append(objToMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getFaNumber());
			accountDisplay.append(" ");
			if(objToMSAcc_DetailsTO.getAccountName()!=null){
				accountDisplay.append(objToMSAcc_DetailsTO.getAccountName());
			}
		}
		return accountDisplay.toString();
	}

	/**
	 * Formats the Debit side MS/External Account number display in Edit initiation screen...
	 * @param accountNumber
	 * @return
	 */
	public static String getDebitAccountDisp_Edit(HashMap txnDetails)
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

		DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
		if(txnDetails.containsKey("ExternalAccDetails")){
			objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
		}

		StringBuffer accountDisplay = new StringBuffer();
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transfer_type.startsWith(ChkReqConstants.CHK))){
			accountDisplay.append(objFromMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getFaNumber());
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			accountDisplay.append(objFromMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getFaNumber());
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			accountDisplay.append(formatExternalAccount_Edit(objExternalAccDetails));
		}
		return accountDisplay.toString();
	}

	/**
	 * Formats the Credit side MS/External Account number display in Edit initiation screen...
	 * @param accountNumber
	 * @return
	 */
	public static String getCreditAccountDisp_Edit(HashMap txnDetails)
	{
		//Mapping the payment detail object with the Payment details...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		if(txnDetails.containsKey("PaymentDetails")){
			objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
		}

		//Mapping the from account attributes...
		ToMSAcc_DetailsTO objToMSAcc_DetailsTO = new ToMSAcc_DetailsTO();
		if(txnDetails.containsKey("MSToAccDetails")){
			objToMSAcc_DetailsTO = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
		}

		DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
		if(txnDetails.containsKey("ExternalAccDetails")){
			objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
		}

		StringBuffer accountDisplay = new StringBuffer();
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
			accountDisplay.append(objToMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getFaNumber());
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			accountDisplay.append(formatExternalAccount_Edit(objExternalAccDetails));
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			accountDisplay.append(objToMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getFaNumber());
		}
		return accountDisplay.toString();
	}

	/**
	 * Setting the Debit MS/External Account number based on the Transfer type in 3-6-3 format for Transaction view details...
	 * @param objPaymentDetails
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String getDebitAccountNum_View(HashMap txnDetails)
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

		DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
		if(txnDetails.containsKey("ExternalAccDetails")){
			objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
		}

		StringBuffer accountDisplay = new StringBuffer();
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transfer_type.startsWith(ChkReqConstants.CHK))){
			accountDisplay.append(objFromMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getFaNumber());
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			accountDisplay.append(objFromMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objFromMSAcc_DetailsTO.getFaNumber());
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			accountDisplay.append(objExternalAccDetails.getCpyaccnum());
		}
		return accountDisplay.toString();
	}

	/**
	 * Setting the Credit MS/External account number based on the Transfer type in 3-6-3 format for Transaction View details...
	 * @param objPaymentDetails
	 * @param objPaymentDetails
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String getCreditAccountNum_View(HashMap txnDetails)
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

		//External account mapping details..
		DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
		if(txnDetails.containsKey("ExternalAccDetails")){
			objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
		}

		StringBuffer accountDisplay = new StringBuffer();
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
			accountDisplay.append(objToMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getFaNumber());
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			accountDisplay.append(objExternalAccDetails.getCpyaccnum());
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			accountDisplay.append(objToMSAcc_DetailsTO.getOfficeNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getAccountNumber());
			accountDisplay.append("-");
			accountDisplay.append(objToMSAcc_DetailsTO.getFaNumber());
		}
		return accountDisplay.toString();
	}

	/**
	 * Formats the External Account Owner display in the All the View Details screens like Transaction View Details screens...
	 * @param accountNumber
	 * @return
	 */
	public static String getExtAcntOwner_View(Object accountNumber)
	{
		StringBuffer accountDisplay = new StringBuffer();
		if(accountNumber instanceof DsExtPayeeDetailsOutTO)
		{
			DsExtPayeeDetailsOutTO objExternalAccDetails = (DsExtPayeeDetailsOutTO)accountNumber;
			if(objExternalAccDetails.getAccount_owner()!=null){
				accountDisplay.append(objExternalAccDetails.getAccount_owner());
			}
			else {
				accountDisplay.append(objExternalAccDetails.getCpypayeename1());
			}
		}
		return accountDisplay.toString();
	}

	/**
	 * External Account formating...
	 * External Accounts:
       XX- + last 4 digits of the external account number + External Account Nickname + (Name of the Account Owner XX-last 4 digits of SSN)
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String formatExternalAccount(DsExtPayeeDetailsOutTO objExternalAccDetails)
	{
		StringBuilder accountNumber = new StringBuilder(); 
		if(objExternalAccDetails!=null){
			accountNumber.append("XX-");
			if(objExternalAccDetails.getCpyaccnum()!=null){
				String ext_Acc_Num = objExternalAccDetails.getCpyaccnum();
				if(ext_Acc_Num.length()>4)
				{
					int dispStartIndex = ext_Acc_Num.length()-4;
					ext_Acc_Num = ext_Acc_Num.substring(dispStartIndex);
				}
				accountNumber.append(ext_Acc_Num);
			}
			accountNumber.append(" ");
			if(objExternalAccDetails.getNick_name()!=null){
				accountNumber.append(objExternalAccDetails.getNick_name());
			}
		}
		return accountNumber.toString();
	}

	/**
	 * External Account formating during Edit case...
	 * External Accounts:
       XX- + last 4 digits of the external account number + External Account Nickname + (Name of the Account Owner XX-last 4 digits of SSN)
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String formatExternalAccount_Edit(DsExtPayeeDetailsOutTO objExternalAccDetails)
	{
		StringBuilder accountNumber = new StringBuilder(); 
		if(objExternalAccDetails!=null){
			accountNumber.append("XX-");
			if(objExternalAccDetails.getCpyaccnum()!=null){
				String ext_Acc_Num = objExternalAccDetails.getCpyaccnum();
				if(ext_Acc_Num.length()>4)
				{
					int dispStartIndex = ext_Acc_Num.length()-4;
					ext_Acc_Num = ext_Acc_Num.substring(dispStartIndex);
				}
				accountNumber.append(ext_Acc_Num);
			}
			accountNumber.append(" ");
			if(objExternalAccDetails.getNick_name()!=null){
				accountNumber.append(objExternalAccDetails.getNick_name());
			}
			accountNumber.append(" ");
			if(objExternalAccDetails.getAccount_owner()!=null){
				accountNumber.append("(");
				accountNumber.append(MSCommonUtils.truncateAcctName(objExternalAccDetails.getAccount_owner()));
				accountNumber.append(")");
			}
		}
		return accountNumber.toString();
	}
}
