package com.tcs.Payments.ms360Utils;

import java.util.HashMap;

import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       23-09-2011        P3-B            PLA  
 * **********************************************************
 */
public class FormatAccount 
{
	/**
	 * Formats the Debit side MS/External Account number display in  all the Modal window and PreConfirmation screens...
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

		//External Account Details..
		DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
		if(txnDetails.containsKey("ExternalAccDetails")){
			objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
		}

		StringBuffer accountDisplay = new StringBuffer();
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transfer_type.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
			accountDisplay.append(formatMSAccount(objFromMSAcc_DetailsTO));
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			accountDisplay.append(formatMSAccount(objFromMSAcc_DetailsTO));
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

		//Loan Account Details ..
		PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
		if(txnDetails.containsKey("LoanAccountDetails")){
			objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
		}

		StringBuffer accountDisplay = new StringBuffer();
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL)) {
			accountDisplay.append(formatMSAccount(objToMSAcc_DetailsTO));
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)) {
			accountDisplay.append(formatExternalAccount(objExternalAccDetails));
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)) {
			accountDisplay.append(formatMSAccount(objToMSAcc_DetailsTO));
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)) {
			accountDisplay.append(getLoanAcntFormat(objLoanAcntDetails.getLoanAccount()));
		}
		return accountDisplay.toString();
	}

	/**
	 * Setting the Portfolio Loan Account format .... 
	 */
	public static String getLoanAcntFormat(String loanAcntNo)
	{
		if(loanAcntNo!=null && !loanAcntNo.trim().equalsIgnoreCase(""))
		{
			String bankNum = loanAcntNo.substring(0,3); 
			String loanNum = loanAcntNo.substring(3); 
			if(loanNum.length()>3)
			{
				int dispStartIndex = loanNum.length()-3;
				loanNum = loanNum.substring(dispStartIndex);
			}
			loanAcntNo = MSSystemDefaults.LOAN_ACC_PREFIX_TEXT+" "+bankNum+" "+"XXXX"+loanNum;
		}
		return loanAcntNo;
	}

	/**
	 * Format the MS Account..
	 * @param msAccount
	 * @return
	 */
	public static String formatMSAccount(Object msAccount)
	{
		String acntDisplay = "";
		try 
		{
			String nickName = "" ;
			String friendlyName = "";
			String acntNumber = "";

			//Account Details
			if(msAccount!=null && msAccount instanceof FromMSAcc_DetailsTO) {
				FromMSAcc_DetailsTO objFromMSAcc_DetailsTO = (FromMSAcc_DetailsTO)msAccount;
				nickName = objFromMSAcc_DetailsTO.getNickName() ;
				friendlyName = objFromMSAcc_DetailsTO.getFriendlyName();
				acntNumber = objFromMSAcc_DetailsTO.getAccountNumber();
			}
			else if(msAccount!=null && msAccount instanceof ToMSAcc_DetailsTO){
				ToMSAcc_DetailsTO objToMSAcc_DetailsTO = (ToMSAcc_DetailsTO)msAccount;
				nickName = objToMSAcc_DetailsTO.getNickName() ;
				friendlyName = objToMSAcc_DetailsTO.getFriendlyName();
				acntNumber = objToMSAcc_DetailsTO.getAccountNumber();
			}

			acntDisplay = (nickName!=null && !nickName.trim().equals("")) ? nickName:friendlyName ;
			if(acntDisplay == null || acntDisplay.trim().equals("")) {
				if(acntNumber!=null && !acntNumber.trim().equalsIgnoreCase("")) {
					if(acntNumber.length()>4) {
						int dispStartIndex = acntNumber.length()-4;
						acntNumber = acntNumber.substring(dispStartIndex);
					}
					acntDisplay = "XXX-"+acntNumber;
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return acntDisplay;
	}

	/**
	 * External Account formating ...
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String formatExternalAccount(DsExtPayeeDetailsOutTO objExternalAccDetails)
	{
		StringBuilder accountNumber = new StringBuilder(); 
		if(objExternalAccDetails!=null){
			accountNumber.append(objExternalAccDetails.getNick_name());
			accountNumber.append(" ");
			if(objExternalAccDetails.getCpyaccnum()!=null){
				accountNumber.append("(XX-");
				String ext_Acc_Num = objExternalAccDetails.getCpyaccnum();
				if(ext_Acc_Num.length()>4)
				{
					int dispStartIndex = ext_Acc_Num.length()-4;
					ext_Acc_Num = ext_Acc_Num.substring(dispStartIndex);
				}
				accountNumber.append(ext_Acc_Num);
				accountNumber.append(")");
			}
		}
		return accountNumber.toString().trim();
	}
}
