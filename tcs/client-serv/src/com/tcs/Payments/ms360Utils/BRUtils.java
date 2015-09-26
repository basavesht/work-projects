package com.tcs.Payments.ms360Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.tcs.Payments.EAITO.MS_RTAB_ACNT_DTLS;
import com.tcs.bancs.channels.integration.MMAccount;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *   
 * **********************************************************
 */
public class BRUtils {

	/**
	 * Setting the Business Rule Txn_Type Value depending on the Transaction type... 
	 */
	public static String getBRTxnTypeValue(String txn_Type)
	{
		String brTxn_Typ = null;
		if(txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			brTxn_Typ=Bus_Rule_Input_Desc.TYPE_ACH;
		}
		else if(txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			brTxn_Typ=Bus_Rule_Input_Desc.TYPE_ACH;
		}
		else if(txn_Type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
			brTxn_Typ=Bus_Rule_Input_Desc.TYPE_INT;
		}
		else if(txn_Type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
			brTxn_Typ=Bus_Rule_Input_Desc.TYPE_PORTFOLIO_LOAN;
		}
		return brTxn_Typ;
	}

	/**
	 * Gets the BR Transaction type..
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getBRTxn_PageType(PaymentDetailsTO objPaymentDetails)
	{
		String page_type = "";
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transfer_type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
			page_type = Bus_Rule_Input_Desc.CS_INT;
		}
		else if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
			page_type = Bus_Rule_Input_Desc.CS_ACH;
		}
		return page_type;
	}

	/**
	 * Gets the BR Frequency type...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getBRFrequency_type(PaymentDetailsTO objPaymentDetails)
	{
		String freq_type_desc = "";
		String freq_type = objPaymentDetails.getFrequency_Type();
		String freq_duration = objPaymentDetails.getFrequency_DurationDesc();
		if(freq_type!=null && freq_type.equalsIgnoreCase("1")){
			freq_type_desc = Bus_Rule_Input_Desc.ONE_TIME;
		}
		else if(freq_type!=null && freq_type.equalsIgnoreCase("2"))
		{
			if(freq_duration!=null && freq_duration.equalsIgnoreCase("W")){
				freq_type_desc = Bus_Rule_Input_Desc.REC_W;
			}
			else if(freq_duration!=null && freq_duration.equalsIgnoreCase("OW")){
				freq_type_desc = Bus_Rule_Input_Desc.REC_OW;
			}
			else if(freq_duration!=null && freq_duration.equalsIgnoreCase("M")){
				freq_type_desc = Bus_Rule_Input_Desc.REC_M;
			}
			else if(freq_duration!=null && freq_duration.equalsIgnoreCase("FBD")){
				freq_type_desc = Bus_Rule_Input_Desc.REC_FBD;
			}
			else if(freq_duration!=null && freq_duration.equalsIgnoreCase("LBD")){
				freq_type_desc = Bus_Rule_Input_Desc.REC_LBD;
			}
			else if(freq_duration!=null && freq_duration.equalsIgnoreCase("Q")){
				freq_type_desc = Bus_Rule_Input_Desc.REC_Q;
			}
			else if(freq_duration!=null && freq_duration.equalsIgnoreCase("H")){
				freq_type_desc = Bus_Rule_Input_Desc.REC_H;
			}
			else if(freq_duration!=null && freq_duration.equalsIgnoreCase("Y")){
				freq_type_desc = Bus_Rule_Input_Desc.REC_Y;
			}
		}
		return freq_type_desc;
	}

	/**
	 * Gets the BR Tsn sub type...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getBRTxnSub_Type(PaymentDetailsTO objPaymentDetails)
	{
		String txn_sub_type = "";
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			txn_sub_type = Bus_Rule_Input_Desc.MS_OUTGOING;
		}
		else if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) )){
			txn_sub_type = Bus_Rule_Input_Desc.MS_INCOMING;
		}
		return txn_sub_type;
	}

	/**
	 * Get external account type..
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String getBRExtAcc_Type(DsExtPayeeDetailsOutTO objExternalAccDetails)
	{
		String br_ext_acc_type = "";
		String externalAccType = objExternalAccDetails.getCpyacctype();
		if(externalAccType!=null && (externalAccType.equalsIgnoreCase(ExtAccountTypeDesc.PER_CHECK)
				||(externalAccType.equalsIgnoreCase(ExtAccountTypeDesc.BUSS_CHECK) || 
						(externalAccType.equalsIgnoreCase(ExtAccountTypeDesc.CHECK))))){
			br_ext_acc_type = Bus_Rule_Input_Desc.EXT_ACC_CHECKING;
		}
		else if(externalAccType!=null && (externalAccType.equalsIgnoreCase(ExtAccountTypeDesc.PER_SAVE)
				||(externalAccType.equalsIgnoreCase(ExtAccountTypeDesc.BUSS_SAVE) || 
						(externalAccType.equalsIgnoreCase(ExtAccountTypeDesc.SAVE))))){
			br_ext_acc_type = Bus_Rule_Input_Desc.EXT_ACC_SAVINGS;
		}
		return br_ext_acc_type;
	}

	/**
	 * Get external account sub type..
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String getBRExtAcc_SubType(DsExtPayeeDetailsOutTO objExternalAccDetails)
	{
		String br_ext_acc_sub_type = Bus_Rule_Input_Desc.EXT_ACC_SUB_TYPE_PERSONAL;
		String externalAccType = objExternalAccDetails.getCpyacctype();
		if(externalAccType!=null && (externalAccType.equalsIgnoreCase(ExtAccountTypeDesc.PER_CHECK)
				||(externalAccType.equalsIgnoreCase(ExtAccountTypeDesc.PER_SAVE)))){
			br_ext_acc_sub_type = Bus_Rule_Input_Desc.EXT_ACC_SUB_TYPE_PERSONAL;
		}
		else if(externalAccType!=null && (externalAccType.equalsIgnoreCase(ExtAccountTypeDesc.BUSS_CHECK)
				||(externalAccType.equalsIgnoreCase(ExtAccountTypeDesc.BUSS_SAVE)))){
			br_ext_acc_sub_type = Bus_Rule_Input_Desc.EXT_ACC_SUB_TYPE_BUSINESS;
		}
		return br_ext_acc_sub_type;
	}

	/**
	 * Gets the Unedited amount...
	 * @param objPaymentDetails
	 * @return
	 * @throws Exception 
	 */
	public static BigDecimal getUnEditedAmount(PaymentDetailsTO objPaymentDetails) throws Exception
	{
		BigDecimal unEditedTxnAmount = null;
		try 
		{
			if(objPaymentDetails.isTxnModified()){
				unEditedTxnAmount = ConvertionUtil.convertToBigDecimal(objPaymentDetails.getPrevTxnAmount());
			}
			else {
				unEditedTxnAmount = ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount());
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return unEditedTxnAmount;
	}

	/**
	 * Gets the Unedited fee...
	 * @param objPaymentDetails
	 * @return
	 * @throws Exception 
	 */
	public static BigDecimal getUnEditedFee(PaymentDetailsTO objPaymentDetails) throws Exception
	{
		BigDecimal unEditedTxnFee = null;
		try 
		{
			if(objPaymentDetails.isTxnModified()){
				unEditedTxnFee = objPaymentDetails.getPrev_txn_Fee_Charge();
			}
			else {
				unEditedTxnFee = objPaymentDetails.getTxn_Fee_Charge();
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return unEditedTxnFee;

	}

	/**
	 * Gets the Unedited fee charged to..
	 * @param objPaymentDetails
	 * @return
	 * @throws Exception 
	 */
	public static String getUnEditedFeeChargedTo(HashMap txnDetails) throws Exception
	{
		String unEditedFeeChagedTo = null;
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return unEditedFeeChagedTo;
	}

	/**
	 * BR Repeat type description...
	 * Messages are read from the property file and will be formatted for display..
	 * @param objPaymentDetails
	 * @return
	 * @throws Exception 
	 */
	public static String getBRRepeatDesc(PaymentDetailsTO objPaymentDetails) throws Exception
	{
		String freq_repeat_desc = null;
		String freq_repeat = objPaymentDetails.getFrequency_DurationValue();
		String freq_type = objPaymentDetails.getFrequency_Type();

		if(freq_type!=null && freq_type.equalsIgnoreCase("2"))
		{
			if(freq_repeat!=null && freq_repeat.equalsIgnoreCase("1")){
				freq_repeat_desc = Bus_Rule_Input_Desc.UNTIL_CANCEL;
			}
			else if(freq_repeat!=null && freq_repeat.equalsIgnoreCase("2")){
				freq_repeat_desc = Bus_Rule_Input_Desc.UNTIL_XENDDATE;
			}
			else if(freq_repeat!=null && freq_repeat.equalsIgnoreCase("3")){
				freq_repeat_desc = Bus_Rule_Input_Desc.UNTIL_XDOLLARLIMIT;
			}
			else if(freq_repeat!=null && freq_repeat.equalsIgnoreCase("4")){
				freq_repeat_desc = Bus_Rule_Input_Desc.UNTIL_XTRANSFERS;
			}
		}
		return freq_repeat_desc;
	}

	/**
	 * Duration End date formatting...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String formatDurationEndDt(PaymentDetailsTO objPaymentDetails)
	{
		String durationEndDate = null;
		String end_date = objPaymentDetails.getDuration_EndDate();
		if(end_date!=null && !end_date.trim().equalsIgnoreCase("") && !end_date.equalsIgnoreCase(MSSystemDefaults.DEFAULT_DATE_TXT)){
			durationEndDate = objPaymentDetails.getDuration_EndDate();
		}
		return durationEndDate;
	}

	/**
	 * Gets the Branch Id for the RSA Request...
	 * @param objPaymentDetails
	 * @return
	 * @throws Exception 
	 */
	public static String getTxnMonitoringBranch(HashMap txnDetails) throws Exception
	{
		String branch = null;
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			//Branch Id mappings...
			String transfer_Type = objPaymentDetails.getTransfer_Type();
			if(transfer_Type!=null && transfer_Type.trim().equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				branch = objFromMSAcc_Details.getOfficeNumber();
			}
			else if(transfer_Type!=null && transfer_Type.trim().equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				branch = objToMSAcc_Details.getOfficeNumber();
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return branch;
	}

	/**
	 * Maps the Available spending in the BR input...
	 * Internal and ACH:
          The Spending Power attribute passed in the input to business rule evaluation will be mapped as the actual spending power plus the original transaction amount 
          if the Booked In flag indicates that the amount was posted to RTA Pending.
	 * @param objMS_RTAB_ACNT_DTLS
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal getAvailableSpndPower(MS_RTAB_ACNT_DTLS objMS_RTAB_ACNT_DTLS,HashMap txnDetails) throws Exception
	{
		BigDecimal availableSpndPower = null;
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Initialization....
			String txnType = objPaymentDetails.getTransfer_Type();
			String rta_booked_flg = objPaymentDetails.getRta_booked_flag();
			BigDecimal rtab_spending_pwr = objMS_RTAB_ACNT_DTLS.getAVAILABLE_SPENDING_POWER();
			BigDecimal original_txn_amount = null;
			if(objPaymentDetails.getPrevTxnAmount()!=null) {
				original_txn_amount = ConvertionUtil.convertToBigDecimal(objPaymentDetails.getPrevTxnAmount());
			}

			//Default Spending Power...
			availableSpndPower = rtab_spending_pwr;

			if(txnType!=null && (txnType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || txnType.startsWith(TxnTypeCode.ACH_TYPE) || txnType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))) {
				if(rtab_spending_pwr!=null && original_txn_amount!=null 
						&& rta_booked_flg!=null && rta_booked_flg.equalsIgnoreCase(RTAActionKeyDesc.RTA_PENDING)){
					availableSpndPower =  rtab_spending_pwr.add(original_txn_amount);
				}
			}
		}
		catch (Exception exception) {
			throw exception;
		}
		return availableSpndPower;
	}

	/**
	 * Returns the Same_Name flag for the accounts involved in transaction (Internal Transfers)...
	 * @param txnDetails
	 * @return
	 * @throws Exception 
	 */
	public static String getSameNameFlag(HashMap txnDetails) throws Exception
	{
		EBWLogger.logDebug("com.tcs.Payments.ms360Utils.BRUtils"," Same_Name flag logic begins");
		String same_name_flg = "Y";
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External account details...
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Loan Account Details ..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			if(txnDetails.containsKey("LoanAccountDetails")){
				objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
			}

			//From Account Details..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			String txn_type = objPaymentDetails.getTransfer_Type();
			String third_Party_Ind = objExternalAccDetails.getThird_party_ind();
			if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.INTERNAL))) {
				same_name_flg = "Y";
			}
			else if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || (txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)))) {
				if(third_Party_Ind!=null && third_Party_Ind.equalsIgnoreCase(MSSystemDefaults.THRD_PARTY_Y)){
					same_name_flg = "N";
				}
				else {
					same_name_flg = "Y";
				}
			}
			else if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)) 
			{
				if(objLoanAcntDetails!=null)
				{
					String fromAcnt = objFromMSAcc_Details.getAccountNumber();
					String fromOffice = objFromMSAcc_Details.getOfficeNumber();
					ArrayList<MMAccount> collateralAcnts = objLoanAcntDetails.getCollateralAcnt();
					for(MMAccount acnt : collateralAcnts){
						if((acnt.getAccountNumber()!=null && acnt.getAccountNumber().equalsIgnoreCase(fromAcnt))
								&& (acnt.getOfficeNumber()!=null && acnt.getOfficeNumber().equalsIgnoreCase(fromOffice))) {
							same_name_flg = "Y";
							break;
						}
					}
				}
			}
			EBWLogger.logDebug("com.tcs.Payments.ms360Utils.BRUtils"," Same_Name flag is "+ same_name_flg);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return same_name_flg;
	}

	/**
	 * Check if the loan amount exists or not..
	 * @param txnDetails
	 * @return
	 * @throws Exception 
	 */
	public static String isLoanExists(HashMap txnDetails) throws Exception
	{
		String loanExists = "N";
		try
		{
			//Loan Account Details ..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			if(txnDetails.containsKey("LoanAccountDetails")){
				objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
			}

			//Check for the loan amount...
			if(objLoanAcntDetails!=null){
				String loanAcnt = objLoanAcntDetails.getLoanAccount();
				if(loanAcnt!=null){
					loanExists = "Y";
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return loanExists;
	}
}
