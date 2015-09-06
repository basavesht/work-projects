/**
 * 
 */
package com.tcs.Payments.ms360Utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.Vector;

import net.sf.json.JSONArray;

import org.apache.struts.util.LabelValueBean;

import com.tcs.Payments.DateUtilities.DateFunctions;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.Message;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.channels.integration.MMAccount;
import com.tcs.bancs.channels.integration.MMContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.transferobject.ContextAccount;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.serverside.services.channelPaymentServices.WSDefaultInputsMap;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 *    224703       23-05-2011        3               RTA Mapping
 * **********************************************************
 */
public class MSCommonUtils {

	/**
	 * Setting the PayDebitaccnum for the DS_PAY_TXNS Table based on the Transfer type.
	 * @param objPaymentDetails
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String getPayDebitaccnum(HashMap txnDetails)
	{
		String payDebitaccnum = null;

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

		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transfer_type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
			payDebitaccnum = objFromMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			payDebitaccnum = objFromMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			payDebitaccnum = objToMSAcc_DetailsTO.getAccountNumber();
		}
		return payDebitaccnum;

	}

	/**
	 * Setting the PayPayeeaccnum for the DS_PAY_TXNS Table based on the Transfer type.
	 * @param objPaymentDetails
	 * @param objPaymentDetails
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String getPaypayeeaccnum(HashMap txnDetails)
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

		//External account details attribute mapping...
		DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
		if(txnDetails.containsKey("ExternalAccDetails")){
			objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
		}

		//Loan Account Details ..
		PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
		if(txnDetails.containsKey("LoanAccountDetails")){
			objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
		}

		String payPayeeaccnum = null;
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
			payPayeeaccnum = objToMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			payPayeeaccnum = objExternalAccDetails.getCpypayeeid();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			payPayeeaccnum = objExternalAccDetails.getCpypayeeid();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
			payPayeeaccnum = objLoanAcntDetails.getLoanAccount();
		}
		return payPayeeaccnum;
	}

	/**
	 * Setting the PayPayeeaccnum for the DS_PAY_TXNS Table based on the Transfer type.
	 * @param objPaymentDetails
	 * @param objPaymentDetails
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String getCompleteMSAccNumber(Object msAccountDetails)
	{
		String completeMSAccNumber = null;
		if(msAccountDetails!=null && msAccountDetails instanceof FromMSAcc_DetailsTO){
			FromMSAcc_DetailsTO objFromMSAcc_Details =(FromMSAcc_DetailsTO)msAccountDetails;
			String fromOfficeNum = objFromMSAcc_Details.getOfficeNumber();
			String fromAccNum = objFromMSAcc_Details.getAccountNumber();
			String fromFaNum = objFromMSAcc_Details.getFaNumber();
			if(fromOfficeNum!=null && fromAccNum!=null && fromFaNum!=null){
				completeMSAccNumber = fromOfficeNum + fromAccNum + fromFaNum;
			}
		}
		else if (msAccountDetails!=null && msAccountDetails instanceof ToMSAcc_DetailsTO){
			ToMSAcc_DetailsTO objToMSAcc_Details =(ToMSAcc_DetailsTO)msAccountDetails;
			String toOfficeNum = objToMSAcc_Details.getOfficeNumber();
			String toAccNum = objToMSAcc_Details.getAccountNumber();
			String toFaNum = objToMSAcc_Details.getFaNumber();
			if(toOfficeNum!=null && toAccNum!=null && toFaNum!=null){
				completeMSAccNumber = toOfficeNum + toAccNum + toFaNum;
			}
		}
		return completeMSAccNumber;
	}

	/** Formats the account number before storing in the DB in 3-6-3 format
	 *  In case the FA Number , Account Number , Office Number are not in the correct format ...  
	 * 
	 */
	public static String getMSAccFormat(String msAccountNo)
	{
		//Formatting Account number ...
		do{
			if(msAccountNo.length()>=6){
				break;
			}
			else {
				msAccountNo="0"+msAccountNo;
			}
		}while(msAccountNo.length()!=6);
		return msAccountNo;
	}

	/**
	 * Maps the settle date in RTA Mapping code... 
	 * In case actual_exe_date is null, map the current business date...
	 * @param objPaymentDetails
	 * @return
	 * @throws Exception 
	 */
	public static Timestamp getRTASettleDate(PaymentDetailsTO objPaymentDetails) throws Exception 
	{
		Timestamp settleDate = null;
		try 
		{
			String prev_rta_booked_in_flag = objPaymentDetails.getRta_booked_flag();
			String rta_booked_in_flag = objPaymentDetails.getCurrent_rta_booked_flag();
			if((prev_rta_booked_in_flag!=null && prev_rta_booked_in_flag.equalsIgnoreCase(RTAActionKeyDesc.RTA_INTRADAY))
					|| (rta_booked_in_flag!=null && rta_booked_in_flag.equalsIgnoreCase(RTAActionKeyDesc.RTA_INTRADAY)))
			{
				settleDate = ConvertionUtil.convertToTimestamp(objPaymentDetails.getActualExecDate());
				if(settleDate == null){
					settleDate = ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date());
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return settleDate;
	}

	/**
	 * Setting the RTA Txn_Type Value depending on the Transaction type... 
	 */
	public static Double getRTATxnTypeValue(PaymentDetailsTO objPaymentDetails)
	{
		Double rtaTxn_Type = null;
		String txn_Type = objPaymentDetails.getTransfer_Type();

		if(objPaymentDetails.isReverseRTAFlag()) {
			if(txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				rtaTxn_Type=new Double(4);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				rtaTxn_Type=new Double(5);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
				rtaTxn_Type=new Double(14);
			}
		}
		else {
			if(txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				rtaTxn_Type=new Double(2);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				rtaTxn_Type=new Double(3);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
				rtaTxn_Type=new Double(1);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				rtaTxn_Type=new Double(18);
			}
		}
		return rtaTxn_Type;
	}

	/**
	 * Setting the RTA Txn_Type Value depending on the Transaction type and Fee applicable.... 
	 */
	public static Double getRTATxnTypeValue_Fee(PaymentDetailsTO objPaymentDetails)
	{
		Double rtaTxn_Type = null;
		if(!objPaymentDetails.isReverseRTAFlag()) {
			//In case Reversal RTA call is not required...
		}
		else {
			//In case Reversal RTA call is required...
		}
		return rtaTxn_Type;
	}

	/**
	 * Getting the RTA_In_Status flag based on the RTA action key....
	 * @throws Exception 
	 */
	public static Double getRTAInStatusFlag(Double action_key) throws Exception
	{
		Double in_status = null;
		try 
		{
			//RTA In Status mappings...
			if(action_key!=null && action_key.equals(1D)){
				in_status = 1D;
			}
			else if(action_key!=null && action_key.equals(2D)){
				in_status = 1D;
			}
			else if(action_key!=null && action_key.equals(3D)){
				in_status = 3D;
			}
			else if(action_key!=null && action_key.equals(4D)){
				in_status = 2D;
			}
			else if(action_key!=null && action_key.equals(5D)){
				in_status = 2D;
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return in_status;
	}

	/**
	 * Setting the RTA FROM_ACCT_TRLR_2 depending on the Transaction type ....
	 * @throws Exception 
	 */
	public static String getRTAFrmAccTrlr2(HashMap txnDetails) throws Exception
	{
		String frmAccTrlr2 = null;
		try 
		{
			//Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External details..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Loan Account Details ..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			if(txnDetails.containsKey("LoanAccountDetails")){
				objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
			}

			String extNickName = objExternalAccDetails.getNick_name();
			String extAccNumber = objExternalAccDetails.getCpyaccnum();
			String transfer_Type = objPaymentDetails.getTransfer_Type();

			if(transfer_Type!=null && transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				frmAccTrlr2 = MSCommonUtils.getRTANameFormat(extNickName,extAccNumber);
			}
			else if(transfer_Type!=null && transfer_Type.startsWith(TxnTypeCode.PORTFOLIO_LOAN)){
				if(!objPaymentDetails.isCancelRTAFlag() && !objPaymentDetails.isReverseRTAFlag()) {
					frmAccTrlr2 = MSCommonUtils.getRTALoanAcntFormat(objLoanAcntDetails);
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}

		return frmAccTrlr2;
	}

	/**
	 * Setting the RTA TO_ACCT_TRLR_2 depending on the Transaction type ....
	 * @throws Exception 
	 */
	public static String getRTAToAccTrlr2(HashMap txnDetails) throws Exception
	{
		String toAccTrlr2 = null;
		try
		{
			//Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External details..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Loan Account Details ..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			if(txnDetails.containsKey("LoanAccountDetails")){
				objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
			}

			String extNickName = objExternalAccDetails.getNick_name();
			String extAccNumber = objExternalAccDetails.getCpyaccnum();
			String transfer_Type = objPaymentDetails.getTransfer_Type();

			if(transfer_Type!=null && transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				toAccTrlr2 = MSCommonUtils.getRTANameFormat(extNickName,extAccNumber);
			}
			else if(transfer_Type!=null && transfer_Type.startsWith(TxnTypeCode.PORTFOLIO_LOAN)){
				if(objPaymentDetails.isCancelRTAFlag()) {
					toAccTrlr2 = MSCommonUtils.getRTALoanAcntFormat(objLoanAcntDetails);
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}

		return toAccTrlr2;
	}

	/**
	 * Setting the RTA FROM_ACCT_TRLR_2 depending on the Transaction type ....
	 * @throws Exception 
	 */
	public static String getRTAFrmAccTrlr1(HashMap txnDetails) throws Exception
	{
		String frmAccTrlr1 = null;
		try 
		{
			//Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String transfer_Type = objPaymentDetails.getTransfer_Type();

			if(transfer_Type!=null && (transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
				frmAccTrlr1 = objPaymentDetails.getQualifier();
				if(objPaymentDetails.isReverseRTAFlag()){
					frmAccTrlr1 = objPaymentDetails.getReverse_qualifier();
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return frmAccTrlr1;
	}

	/**
	 * Setting the RTA TO_ACCT_TRLR_2 depending on the Transaction type ....
	 * @throws Exception 
	 */
	public static String getRTAToAccTrlr1(HashMap txnDetails) throws Exception
	{
		String toAccTrlr1 = null;
		try 
		{
			//Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String transfer_Type = objPaymentDetails.getTransfer_Type();

			if(transfer_Type!=null && transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				toAccTrlr1 = objPaymentDetails.getQualifier();
				if(objPaymentDetails.isReverseRTAFlag()){
					toAccTrlr1 = objPaymentDetails.getReverse_qualifier();
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return toAccTrlr1;
	}

	/**
	 * Setting the RTA Nick Name format .... 
	 */
	public static String getRTANameFormat(String ext_Nick_Name,String ext_Acc_Num)
	{
		String rtaNickName=null;
		if(ext_Nick_Name!=null && ext_Acc_Num!=null && !ext_Nick_Name.trim().equalsIgnoreCase("") && !ext_Acc_Num.trim().equalsIgnoreCase(""))
		{
			if(ext_Acc_Num.length()>4)
			{
				int dispStartIndex = ext_Acc_Num.length()-4;
				ext_Acc_Num = ext_Acc_Num.substring(dispStartIndex);
			}
			rtaNickName = ext_Nick_Name+" "+"XX-"+ext_Acc_Num;
		}
		return rtaNickName;
	}

	/**
	 * Setting the RTA Loan Account format .... 
	 */
	public static String getRTALoanAcntFormat(PortfolioLoanAccount loanAcnt)
	{
		String loanAcntNo = loanAcnt.getLoanAccount();
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
	 * External Account formating during Notifications...
	 * External Accounts:
       XX- + last 4 digits of the external account number + External Account Nickname + (Name of the Account Owner XX-last 4 digits of SSN)
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String formatExtAcc_Notify_INIT(DsExtPayeeDetailsOutTO objExternalAccDetails)
	{
		StringBuilder accountNumber = new StringBuilder(); 
		if(objExternalAccDetails!=null){
			if(objExternalAccDetails.getNick_name()!=null){
				accountNumber.append(objExternalAccDetails.getNick_name());
			}
			accountNumber.append(" ");
			accountNumber.append("(XX-");
			if(objExternalAccDetails.getCpyaccnum()!=null){
				String ext_Acc_Num = objExternalAccDetails.getCpyaccnum();
				if(ext_Acc_Num.length()>4)
				{
					int dispStartIndex = ext_Acc_Num.length()-4;
					ext_Acc_Num = ext_Acc_Num.substring(dispStartIndex);
				}
				accountNumber.append(ext_Acc_Num.trim());
			}
			accountNumber.append(")");
		}
		return accountNumber.toString();
	}

	/**
	 * Gets the current time considering the Time Zone .
	 * 
	 */
	public static Date getCurrentTime() throws Exception {
		//Get Branch ID Time Zone...
		String branch_Id = PropertyFileReader.getProperty("OU_ID");
		String branchTimeZoneId = PropertyFileReader.getPropertyKeyValue("TimeZones",branch_Id);

		// Following code will create a new date Instance and set the hours , minutes , seconds for the calendar object instance..
		Calendar serverTime = Calendar.getInstance();
		if(branchTimeZoneId!=null && !branchTimeZoneId.trim().equalsIgnoreCase("")){
			serverTime.setTimeZone(TimeZone.getTimeZone(branchTimeZoneId));
		}

		//Current Date in the MS Branch as described by OU_ID . 
		Calendar currentTime = Calendar.getInstance();   
		currentTime.set(Calendar.YEAR, serverTime.get(Calendar.YEAR));   
		currentTime.set(Calendar.MONTH, serverTime.get(Calendar.MONTH));   
		currentTime.set(Calendar.DAY_OF_MONTH, serverTime.get(Calendar.DAY_OF_MONTH));  
		currentTime.set(Calendar.HOUR_OF_DAY, serverTime.get(Calendar.HOUR_OF_DAY)); 
		currentTime.set(Calendar.MINUTE, serverTime.get(Calendar.MINUTE));   
		currentTime.set(Calendar.SECOND, serverTime.get(Calendar.SECOND)); 
		currentTime.set(Calendar.MILLISECOND, serverTime.get(Calendar.MILLISECOND)); 

		return currentTime.getTime();
	}

	/**
	 * Setting the RSA_REQUEST_FLAG for the FTM call...
	 * Y - If the transaction status before modification is Pending Risk Review .
	 * N - Otherwise.
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getRSAReviewFlag(PaymentDetailsTO objPaymentDetails)
	{
		String rsa_Review_Flg = "N";
		int previousTxnStatus = 0;
		if(objPaymentDetails.isTxnModified())
		{
			if(objPaymentDetails.getTxnPrevStatusCode()!=null){
				previousTxnStatus = new Integer(objPaymentDetails.getTxnPrevStatusCode()).intValue();
				if(previousTxnStatus == 80){
					rsa_Review_Flg = "Y";
				}
			}
		}
		return rsa_Review_Flg;
	}

	/**
	 * Setting the Debit account number based on the Transfer type.
	 * @param objPaymentDetails
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String getDebitAccountNum(HashMap txnDetails)
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

		//External account mapping details..
		DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
		if(txnDetails.containsKey("ExternalAccDetails")){
			objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
		}

		String debitAccount_Num = null;
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transfer_type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
			debitAccount_Num = objFromMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			debitAccount_Num = objFromMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			debitAccount_Num = objExternalAccDetails.getCpyaccnum();
		}
		return debitAccount_Num;

	}

	/**
	 * Setting the Credit account number based on the Transfer type.
	 * @param objPaymentDetails
	 * @param objPaymentDetails
	 * @param objExternalAccDetails
	 * @return
	 */
	public static String getCreditAccountNum(HashMap txnDetails)
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

		//Loan Account Details ..
		PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
		if(txnDetails.containsKey("LoanAccountDetails")){
			objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
		}

		String creditAccount_Num = null;
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
			creditAccount_Num = objToMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			creditAccount_Num = objExternalAccDetails.getCpyaccnum();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			creditAccount_Num = objToMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
			creditAccount_Num = objLoanAcntDetails.getLoanAccount();
		}
		return creditAccount_Num;
	}

	/** 
	 * Formats the date to the RSA date-time format
	 * @param transferDate
	 * @return
	 * @throws Exception
	 */
	public static String getRSADateFormat(String transferDate) throws Exception {
		SimpleDateFormat rsaDtFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		String rsaDate = rsaDtFormat.format(ConvertionUtil.convertToTimestamp(transferDate));
		return rsaDate;
	}

	/**
	 * Setting the MSUserdetails..
	 * @param objMMContextData
	 * @return
	 */
	public MSUser_DetailsTO setMSUserDetailsTO(MMContext objMMContextData)
	{
		MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
		ArrayList mmAccounts = objMMContextData.getAccounts();

		objMSUserDetails.setDeviceCookie(objMMContextData.getDeviceCookie());
		objMSUserDetails.setDeviceFSO(objMMContextData.getDeviceFSO());
		objMSUserDetails.setDevicePrint(objMMContextData.getDevicePrint());
		objMSUserDetails.setFA(objMMContextData.isFA());
		objMSUserDetails.setFirstName(objMMContextData.getFirstName());
		objMSUserDetails.setLastAccountOpenDate(objMMContextData.getLastAccountOpenDate());
		objMSUserDetails.setLastAddressChangeDate(objMMContextData.getLastAddressChangeDate());
		objMSUserDetails.setLastName(objMMContextData.getLastName());
		objMSUserDetails.setLastOnlineServicePasswordChangeDate(objMMContextData.getLastOnlineServicePasswordChangeDate());
		objMSUserDetails.setLastPhoneChangeDate(objMMContextData.getLastPhoneChangeDate());
		objMSUserDetails.setLoginId(objMMContextData.getLoginId());
		objMSUserDetails.setMiddleName(objMMContextData.getMiddleName());
		objMSUserDetails.setOnlineServiceEnrollDate(objMMContextData.getOnlineServiceEnrollDate());
		objMSUserDetails.setSessionId(objMMContextData.getCSSessionID());
		objMSUserDetails.setUuid(objMMContextData.getUuid());
		objMSUserDetails.setClientIPAddress(objMMContextData.getClientIPAddress());
		objMSUserDetails.setClientIdentifier(objMMContextData.getClientIdentifier());
		objMSUserDetails.setMaskedClientIdentifier(objMMContextData.getMaskedClientIdentifier());
		objMSUserDetails.setContextAccounts(extractContextAccounts(mmAccounts));
		return objMSUserDetails;
	}

	/**
	 * Extracts the context accounts...
	 * @param mmAccounts
	 * @return
	 */
	public ArrayList extractContextAccounts(ArrayList mmAccounts)
	{
		ArrayList contextAccounts = new ArrayList();
		if(mmAccounts!=null && !mmAccounts.isEmpty())
		{
			for(int i=0; i < mmAccounts.size() ;i++){
				MMAccount mmAccount = (MMAccount)mmAccounts.get(i);
				ContextAccount contextAcc = new ContextAccount();
				contextAcc.setOffice(mmAccount.getOfficeNumber());
				contextAcc.setAccount(mmAccount.getAccountNumber());
				contextAccounts.add(contextAcc);
			}
		}
		return contextAccounts;
	}

	/**
	 * Setting the from account details from the context...
	 * @param objMMAccount
	 * @return
	 */
	public FromMSAcc_DetailsTO setFromMSAccDetailsTO(MMAccount objMMAccount)
	{
		FromMSAcc_DetailsTO objFromMSAcc_DetailsTO = new FromMSAcc_DetailsTO();
		objFromMSAcc_DetailsTO.setAccountCategory(objMMAccount.getAccountCategory());
		objFromMSAcc_DetailsTO.setAccountClass(objMMAccount.getAccountClass());
		objFromMSAcc_DetailsTO.setAccountNumber(objMMAccount.getAccountNumber());
		objFromMSAcc_DetailsTO.setChoiceFundCode(objMMAccount.getChoiceFundCode());
		objFromMSAcc_DetailsTO.setClientCategory(objMMAccount.getClientCategory());
		objFromMSAcc_DetailsTO.setCollateralAcctInd(objMMAccount.getCollateralAcctInd());
		objFromMSAcc_DetailsTO.setDivPay(objMMAccount.getDivPay());
		objFromMSAcc_DetailsTO.setFaNumber(objMMAccount.getFaNumber());
		objFromMSAcc_DetailsTO.setFriendlyName(objMMAccount.getFriendlyName());
		objFromMSAcc_DetailsTO.setIraCode(objMMAccount.getIraCode());
		objFromMSAcc_DetailsTO.setKeyAccount(objMMAccount.getKeyAccount());
		objFromMSAcc_DetailsTO.setNickName(objMMAccount.getNickName());
		objFromMSAcc_DetailsTO.setNovusSubProduct(objMMAccount.getNovusSubProduct());
		objFromMSAcc_DetailsTO.setOfficeNumber(objMMAccount.getOfficeNumber());
		objFromMSAcc_DetailsTO.setStatus(objMMAccount.getStatus());
		objFromMSAcc_DetailsTO.setTradeControl(objMMAccount.getTradeControl());
		objFromMSAcc_DetailsTO.setViewTransactionFlag(objMMAccount.isViewTransactionFlag());
		return objFromMSAcc_DetailsTO;
	}

	/**
	 * Setting the To account details from the context....
	 * @param objMMAccount
	 * @return
	 */
	public ToMSAcc_DetailsTO setToMSAccDetailsTO(MMAccount objMMAccount)
	{
		ToMSAcc_DetailsTO objToMSAcc_DetailsTO = new ToMSAcc_DetailsTO();
		objToMSAcc_DetailsTO.setAccountCategory(objMMAccount.getAccountCategory());
		objToMSAcc_DetailsTO.setAccountClass(objMMAccount.getAccountClass());
		objToMSAcc_DetailsTO.setAccountNumber(objMMAccount.getAccountNumber());
		objToMSAcc_DetailsTO.setChoiceFundCode(objMMAccount.getChoiceFundCode());
		objToMSAcc_DetailsTO.setClientCategory(objMMAccount.getClientCategory());
		objToMSAcc_DetailsTO.setCollateralAcctInd(objMMAccount.getCollateralAcctInd());
		objToMSAcc_DetailsTO.setDivPay(objMMAccount.getDivPay());
		objToMSAcc_DetailsTO.setFaNumber(objMMAccount.getFaNumber());
		objToMSAcc_DetailsTO.setFriendlyName(objMMAccount.getFriendlyName());
		objToMSAcc_DetailsTO.setIraCode(objMMAccount.getIraCode());
		objToMSAcc_DetailsTO.setKeyAccount(objMMAccount.getKeyAccount());
		objToMSAcc_DetailsTO.setNickName(objMMAccount.getNickName());
		objToMSAcc_DetailsTO.setNovusSubProduct(objMMAccount.getNovusSubProduct());
		objToMSAcc_DetailsTO.setOfficeNumber(objMMAccount.getOfficeNumber());
		objToMSAcc_DetailsTO.setStatus(objMMAccount.getStatus());
		objToMSAcc_DetailsTO.setTradeControl(objMMAccount.getTradeControl());
		objToMSAcc_DetailsTO.setViewTransactionFlag(objMMAccount.isViewTransactionFlag());
		return objToMSAcc_DetailsTO;
	}

	/**
	 * Gets the From account type of the account involved in transaction...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getFrom_AccType(PaymentDetailsTO objPaymentDetails)
	{
		String frm_AccType = "";
		String transferType = objPaymentDetails.getTransfer_Type();
		if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
			frm_AccType = TxnTypeCode.INTERNAL;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			frm_AccType = TxnTypeCode.INTERNAL;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			frm_AccType = "EXT";
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
			frm_AccType = TxnTypeCode.INTERNAL;
		}
		return frm_AccType;
	}

	/**
	 * Gets the To account type of the account involved in transaction...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getTo_AccType(PaymentDetailsTO objPaymentDetails)
	{
		String to_AccType = "";
		String transferType = objPaymentDetails.getTransfer_Type();
		if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
			to_AccType = TxnTypeCode.INTERNAL;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			to_AccType = "EXT";
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			to_AccType = TxnTypeCode.INTERNAL;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
			to_AccType = TxnTypeCode.PORTFOLIO_LOAN;
		}
		return to_AccType;
	}

	/**
	 * Initiator Role description...
	 * @param initiator_role
	 * @return
	 */
	public static String getInitiatorRoleDesc(String initiator_role)
	{
		String initiator_role_desc = "";
		if(initiator_role!=null && initiator_role.equalsIgnoreCase("INITBRAN")){
			initiator_role_desc = InitiatorRoleDesc.INITBRAN;
		}
		else if(initiator_role!=null && initiator_role.equalsIgnoreCase("INITNBOP")){
			initiator_role_desc = InitiatorRoleDesc.INITNBOP;
		}
		else if(initiator_role!=null && initiator_role.equalsIgnoreCase("Client")){
			initiator_role_desc = InitiatorRoleDesc.Client;
		}
		return initiator_role_desc;
	}

	/**
	 * Function to get the ComboBox Collections Data in case of both Value and Display being different...
	 * @param accountData
	 * @return
	 */
	public static Collection<LabelValueBean> getComboboxValueDisp(ArrayList populateData,String defaultText)
	{
		Vector<LabelValueBean> comboDataOptions = new Vector<LabelValueBean>();
		comboDataOptions.add(0, new LabelValueBean(defaultText,""));
		ArrayList<Object> dataOption = new ArrayList<Object>();
		for(int i=0;i<populateData.size();i++)
		{
			dataOption =(ArrayList)populateData.get(i);
			String value= (String)dataOption.get(0);
			String display = (String)dataOption.get(1);
			comboDataOptions.add(new LabelValueBean(display, value));
		}
		return comboDataOptions;
	}

	/**
	 * Transaction type description...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getTxnTypeDesc_View(PaymentDetailsTO objPaymentDetails)
	{
		String txn_type_desc = "";
		String transferType = objPaymentDetails.getTransfer_Type();
		if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
			txn_type_desc = TxnTypeDesc.INT;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			txn_type_desc = TxnTypeDesc.ACHOUT;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			txn_type_desc = TxnTypeDesc.ACHIN;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
			txn_type_desc = TxnTypeDesc.PLA;
		}
		return txn_type_desc;
	}

	/**
	 * Frequency type description...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getFreqTypeDesc_View(PaymentDetailsTO objPaymentDetails)
	{
		String freq_type_desc = "";
		String freq_type = objPaymentDetails.getFrequency_Type();
		String freq_duration = objPaymentDetails.getFrequency_DurationDesc();

		if(freq_type!=null && freq_type.equalsIgnoreCase("1")){
			freq_type_desc = TxnFrequencyDesc.ONE_TIME;
		}
		else if(freq_type!=null && freq_type.equalsIgnoreCase("2"))
		{
			if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("W")){
				freq_type_desc = TxnFrequencyDesc.REC_W;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("OW")){
				freq_type_desc = TxnFrequencyDesc.REC_OW;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("M")){
				freq_type_desc = TxnFrequencyDesc.REC_M;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("FBD")){
				freq_type_desc = TxnFrequencyDesc.REC_FBD;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("LBD")){
				freq_type_desc = TxnFrequencyDesc.REC_LBD;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("Q")){
				freq_type_desc = TxnFrequencyDesc.REC_Q;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("H")){
				freq_type_desc = TxnFrequencyDesc.REC_H;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("Y")){
				freq_type_desc = TxnFrequencyDesc.REC_Y;
			}
		}
		return freq_type_desc;
	}

	/**
	 * Repeat type description...
	 * Messages are read from the property file and will be formatted for display..
	 * @param objPaymentDetails
	 * @return
	 * @throws Exception 
	 */
	public static String getRepeatDesc_View(PaymentDetailsTO objPaymentDetails) throws Exception
	{
		String freq_repeat_desc = "";
		String freq_repeat = objPaymentDetails.getFrequency_DurationValue();
		String freq_type = objPaymentDetails.getFrequency_Type();
		ResourceBundle messages = ResourceBundle.getBundle("ErrMessage");	
		String repeatCode = "" ;

		if(freq_type!=null && freq_type.equalsIgnoreCase("2"))
		{
			if(freq_repeat!=null && freq_repeat.equalsIgnoreCase("1")){
				repeatCode = "Repeat_001";
				freq_repeat_desc = messages.getString(repeatCode);
			}
			else if(freq_repeat!=null && freq_repeat.equalsIgnoreCase("2")){
				Object[] repeatValue = {objPaymentDetails.getDuration_EndDate()};
				repeatCode = "Repeat_002";
				freq_repeat_desc= MessageFormat.format(messages.getString(repeatCode), repeatValue);
			}
			else if(freq_repeat!=null && freq_repeat.equalsIgnoreCase("3")){
				Object[] repeatValue = {MSCommonUtils.formatTxnAmount(objPaymentDetails.getDuration_AmountLimit())};
				repeatCode = "Repeat_003";
				freq_repeat_desc = MessageFormat.format(messages.getString(repeatCode), repeatValue);
			}
			else if(freq_repeat!=null && freq_repeat.equalsIgnoreCase("4")){
				Object[] repeatValue = {objPaymentDetails.getDuration_NoOfTransfers()};
				repeatCode = "Repeat_004";
				freq_repeat_desc = MessageFormat.format(messages.getString(repeatCode), repeatValue);
			}
		}
		return freq_repeat_desc;
	}

	/**
	 * Transaction Status Type description...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getTxnStatusDesc_View(PaymentDetailsTO objPaymentDetails)
	{
		String txn_Status_desc = "";
		String txnStatusCode = objPaymentDetails.getTxnCurrentStatusCode();

		if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("2")){
			txn_Status_desc = TxnStatusDesc.STATUS_2;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("4")){
			txn_Status_desc = TxnStatusDesc.STATUS_4;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("6")){
			txn_Status_desc = TxnStatusDesc.STATUS_6;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("20")){
			txn_Status_desc = TxnStatusDesc.STATUS_20;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("44")){
			txn_Status_desc = TxnStatusDesc.STATUS_44;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("46")){
			txn_Status_desc = TxnStatusDesc.STATUS_46;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("48")){
			txn_Status_desc = TxnStatusDesc.STATUS_48;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("50")){
			txn_Status_desc = TxnStatusDesc.STATUS_50;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("52")){
			txn_Status_desc = TxnStatusDesc.STATUS_52;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("80")){
			txn_Status_desc = TxnStatusDesc.STATUS_80;
		}
		return txn_Status_desc;
	}

	/**
	 * Converts ArrayList to the String [].
	 * @param arraylist
	 * @return
	 */
	public static String[] convertToStringArr(ArrayList arraylist)
	{
		String str [] = (String []) arraylist.toArray (new String [arraylist.size ()]);
		return str;
	}

	/**
	 * Formats the Transfer Amount to the default locale 
	 * @param amount
	 * @return
	 * @throws Exception 
	 */
	public static String formatTxnAmount(Object amount) throws Exception
	{
		String formattedAmount = "";
		try 
		{
			//Setting up the current locale based on the configured currency code..
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			Locale currentLocale =  Locale.getDefault();
			if(currencyCode!=null && currencyCode.equalsIgnoreCase("USD")){
				currentLocale = Locale.US;
			}

			//Gets the number formatter and currency instance...
			NumberFormat currencyFormatter;
			currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
			if(amount!=null)
			{
				if(amount instanceof BigDecimal){
					formattedAmount = currencyFormatter.format(amount); 
				}
				else if(amount instanceof String)
				{
					String convertedAmountStr =(String)amount;
					BigDecimal convertedAmount = ConvertionUtil.convertToBigDecimal(convertedAmountStr);
					formattedAmount = currencyFormatter.format(convertedAmount);
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return formattedAmount;
	}

	/** Formats the account number before storing in the DB in 3-6-3 format
	 *  In case the FA Number , Account Number , Office Number are not in the correct format ...  
	 * 
	 */
	public static MMAccount getMSAccFormat(MMAccount objMMAccount)
	{
		//Formatting Office number ...
		String officeAcc = objMMAccount.getOfficeNumber(); 
		if(officeAcc!=null) {
			do{
				if(officeAcc.length()>=3){
					break;
				}
				else {
					officeAcc="0"+officeAcc;
				}
			} while(officeAcc.length()!=3);
			objMMAccount.setOfficeNumber(officeAcc);
		}

		//Formatting Account number ...
		String accNumber = objMMAccount.getAccountNumber(); 
		if(accNumber != null) {
			do{
				if(accNumber.length()>=6){
					break;
				}
				else {
					accNumber="0"+accNumber;
				}
			} while(accNumber.length()!=6);
			objMMAccount.setAccountNumber(accNumber);
		}

		//Formatting FA number...
		String faNumber = objMMAccount.getFaNumber();  
		if(faNumber != null) {
			do{
				if(faNumber.length()>=3){
					break;
				}
				else {
					faNumber="0"+faNumber;
				}
			} while(faNumber.length()!=3);
			objMMAccount.setFaNumber(faNumber);
		}
		return objMMAccount;
	}

	/**
	 * Extracts the data from the service context during any server side validation or technical failure..
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	public static String extractContextErrMessage(ServiceContext contextData) throws Exception
	{
		String errorCode = "MSERR001"; // Default Error code for INTERNAL_ERRORS
		String errorMessage = "";
		try 
		{
			ResourceBundle messages = ResourceBundle.getBundle("EBWErrorCodes");
			errorMessage = messages.getString(errorCode);
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			if(contextMessages!=null && !contextMessages.isEmpty())
			{
				for(int i=0;i<contextMessages.size();i++)
				{
					Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
					if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode() == ChannelsErrorCodes.ACCESS_ERROR){
						errorCode = "Access_Err001";
						//Message format ...
						String contactNumber = WSDefaultInputsMap.getCSContactCenter(Boolean.FALSE,contextData);
						Object[] objAccessCheck = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objAccessCheck);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.INT_ACC_DAP_FAILURE){
						errorCode = "Dap_Err001";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.EXT_ACC_DAP_FAILURE){
						errorCode = "Dap_Err002";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.EXT_ACC_OWNER_FAILURE){
						errorCode = "Dap_Err003";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.EXT_ACC_OWNER_FAILURE_EDIT){
						errorCode = "Dap_Err004";
						//Message format ...
						String contactNumber = WSDefaultInputsMap.getCSContactCenter(Boolean.FALSE,contextData);
						Object[] objEditOwnerCheck = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objEditOwnerCheck);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.STATUS_CONSISTENCY_FAILURE){
						errorCode = "Status_Err001";
						//Message format ...
						String contactNumber = WSDefaultInputsMap.getCSContactCenter(Boolean.FALSE,contextData);
						Object[] objStatusCheck = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objStatusCheck);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.INVALID_ROTUING_NUM){
						errorCode = "InvalidABANum_Err001";
						//Message format ...
						String contactNumber = WSDefaultInputsMap.getCSContactCenter(Boolean.FALSE,contextData);
						Object[] objRoutingNum = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objRoutingNum);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.MS_ACNT_NOT_FOUND){
						errorCode = "AcntNotFound_Err001";
						//Message format ...
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.EXT_TXN_RULES_VIOLATION){ // Check the message code from the context, add only if its a EXT_TXN_RULES_VIOLATION.
						errorCode = "Ext_Txn_Rule_Violation";
						//Message format ...
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.THIRD_PARTY_EXT_TXN_RULES_VIOLATION){ // Check the message code from the context, add only if its a THIRD_PARTY_EXT_TXN_RULES_VIOLATION.
						errorCode = "Ext_Third_Party_Txn_Violation";
						//Message format ...
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.PLA_COLLATERAL_ACNTS_EMPTY){ // Check the message code from the context, add only if its a PLA_COLLATERAL_ACNTS_EMPTY.
						errorCode = "PLA_Collateral_Acnts_Empty";
						//Message format ...
						errorMessage = messages.getString(errorCode);
						break;
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return errorMessage;
	}

	/**
	 * Extracts the data from the service context with the Message Type as SEVERE...
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	public static JSONArray extractContextSevereErrMsg(ServiceContext contextData,boolean showSevereMsg) throws Exception
	{
		JSONArray jsonSevereError = new JSONArray();
		try 
		{
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			ArrayList<Object> businessErrors = new ArrayList<Object>();
			for(int i=0;i<contextMessages.size();i++)
			{
				Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
				if(showSevereMsg && objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.CUT_OFF_TIME_EXCEEDED){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessErrors.contains(objBussErrString.get(0))){
							businessErrors.add(objBussErrString.get(0));
							break;
						}
					}
				}
				if(showSevereMsg  && objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.VERSION_MISMATCH_ERROR){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessErrors.contains(objBussErrString.get(0))){
							businessErrors.add(objBussErrString.get(0));
							break;
						}
					}
				}
				if(showSevereMsg  && objContextMess.getType()==MessageType.SEVERE && objContextMess.getCode()== ChannelsErrorCodes.BUSINESS_HOLIDAY){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessErrors.contains(objBussErrString.get(0))){
							businessErrors.add(objBussErrString.get(0));
							break;
						}
					}
				}
			}
			jsonSevereError = JSONArray.fromObject(businessErrors);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return jsonSevereError;
	}

	/**
	 * Extracts the data from the service context with the Message Type as ERROR...
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	public static JSONArray extractContextErrMsg(ServiceContext contextData,boolean showErrorMsg) throws Exception
	{
		JSONArray jsonHardError = new JSONArray();
		try 
		{
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			ArrayList<Object> businessErrors = new ArrayList<Object>();
			for(int i=0;i<contextMessages.size();i++){
				Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
				if(showErrorMsg  && objContextMess.getType()==MessageType.ERROR && objContextMess.getCode()== ChannelsErrorCodes.BUSSINESS_ERROR){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessErrors.contains(objBussErrString.get(0))){
							businessErrors.add(objBussErrString.get(0));
						}
					}
				}
			}
			jsonHardError = JSONArray.fromObject(businessErrors);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return jsonHardError;
	}

	/**
	 * Extracts the data from the service context with the Message Type as RISK...
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	public static JSONArray extractContextRiskMsg(ServiceContext contextData,boolean showWarnMsg) throws Exception
	{
		JSONArray jsonRisk = new JSONArray();
		try 
		{
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			ArrayList<Object> businessRisk = new ArrayList<Object>();
			for(int i=0;i<contextMessages.size();i++){
				Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
				if(showWarnMsg && objContextMess.getType()==MessageType.RISK && objContextMess.getCode()== ChannelsErrorCodes.BUSSINESS_RISK){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessRisk.contains(objBussErrString.get(0))){
							businessRisk.add(objBussErrString.get(0));
						}
					}
				}
			}
			jsonRisk = JSONArray.fromObject(businessRisk);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return jsonRisk;
	}

	/**
	 * Extracts the data from the service context with the Message Type as WARNING...
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	public static JSONArray extractContextWarningMsg(ServiceContext contextData,boolean showWarnMsg) throws Exception
	{
		JSONArray jsonWarnings = new JSONArray();
		try 
		{
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			ArrayList<Object> businessWarnings = new ArrayList<Object>();
			for(int i=0;i<contextMessages.size();i++){
				Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
				if(showWarnMsg && objContextMess.getType()==MessageType.WARNING && objContextMess.getCode()== ChannelsErrorCodes.BUSSINESS_WARNING){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessWarnings.contains(objBussErrString.get(0))){
							businessWarnings.add(objBussErrString.get(0));
						}
					}
				}
			}
			jsonWarnings = JSONArray.fromObject(businessWarnings);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return jsonWarnings;
	}

	/**
	 * Extracts the data from the service context with the Message Type as INFORMATION...
	 * @param contextData
	 * @return
	 * @throws Exception 
	 */
	public static JSONArray extractContextInfoMsg(ServiceContext contextData,boolean showInfoMsg) throws Exception
	{
		JSONArray jsonInforMsg = new JSONArray();
		try 
		{
			ArrayList contextMessages = contextData.getMessages(); //Get the error messages from the context...
			ArrayList<Object> businessInformation = new ArrayList<Object>();
			for(int i=0;i<contextMessages.size();i++){
				Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
				if(showInfoMsg && objContextMess.getType()==MessageType.INFORMATION && objContextMess.getCode()== ChannelsErrorCodes.BUSSINESS_INFORMATION){
					ArrayList<Object> objBussErrString= (ArrayList)objContextMess.getArgs();
					if(objBussErrString!=null && !objBussErrString.isEmpty()){
						if(objBussErrString.get(0)!=null && !((String)objBussErrString.get(0)).trim().equalsIgnoreCase("") && !businessInformation.contains(objBussErrString.get(0))){
							businessInformation.add(objBussErrString.get(0));
						}
					}
				}
				if(objContextMess.getType()==MessageType.INFORMATION && objContextMess.getCode()==ChannelsErrorCodes.CUT_OFF_TIME_EXCEEDED){
					ArrayList<Object> objBussInfoString = (ArrayList)objContextMess.getArgs();
					if(objBussInfoString!=null && !objBussInfoString.isEmpty()){
						if(objBussInfoString.get(0)!=null && !((String)objBussInfoString.get(0)).trim().equalsIgnoreCase("") && !businessInformation.contains(objBussInfoString.get(0))){
							businessInformation.add(objBussInfoString.get(0));
						}
					}
				}
			}
			jsonInforMsg = JSONArray.fromObject(businessInformation);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return jsonInforMsg;
	}

	/**
	 * Convert to Integer...
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static Integer convertToInteger(Object value) throws Exception
	{
		Integer intval = null;
		try 
		{
			if(value!=null && value instanceof Double){
				String str = ConvertionUtil.convertToString(value);
				if(str!=null && !str.trim().equalsIgnoreCase("")){
					intval = new Integer(str);
				}
			}
			else if(value!=null && value instanceof String){
				String str = (String)value;
				if(str!=null && !str.trim().equalsIgnoreCase("")){
					intval = new Integer(str);
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return intval;
	}

	/**
	 * Gets the 12 Hour format....
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public static String get12HourFormat(Timestamp time) throws Exception 
	{		
		String _12HourFormat ="";	
		try 
		{
			if(time!=null)
			{
				SimpleDateFormat dateFormat = null;
				dateFormat = new SimpleDateFormat("hh:mm a");
				_12HourFormat = dateFormat.format(time);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return _12HourFormat;
	}

	/**
	 * Gets the Query String for the Cut off time check based on the transaction type ...
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public static String getCutOffTimeParams(HashMap txnDetails,ServiceContext context) throws Exception
	{
		String cutOffTime = "";
		try 
		{
			//Payment details mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String transferType = objPaymentDetails.getTransfer_Type();

			if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
				cutOffTime = WSDefaultInputsMap.getIntTxnCutOffTime(Boolean.TRUE,context);
			}
			else if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
				cutOffTime = WSDefaultInputsMap.getACHTxnCutOffTime(Boolean.TRUE,context);
			}
			else if(transferType!=null && transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)){
				cutOffTime = WSDefaultInputsMap.getPLATxnCutOffTime(Boolean.TRUE,context);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return cutOffTime;
	}

	/**
	 * Gets the Query String for the Holiday check based on the transaction type ...
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public static ArrayList getHolidayListParams(String transferType,ServiceContext context,Boolean isDirectDBcall) throws Exception
	{
		ArrayList holiday_list = new ArrayList();
		try 
		{
			if(transferType!=null && transferType.startsWith(TxnTypeCode.INTERNAL)){
				holiday_list = WSDefaultInputsMap.getInternalHolidayList(isDirectDBcall,context);
			}
			else if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
				holiday_list = WSDefaultInputsMap.getACHHolidayList(isDirectDBcall,context);
			}
			else if(transferType!=null && transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)){
				holiday_list = WSDefaultInputsMap.getACHHolidayList(isDirectDBcall,context);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return holiday_list;
	}

	/**
	 * Gets the transaction settlement period (Estimated arrival date)...
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public static String getTxnSettlePeriod(String transferType,ServiceContext context,Boolean isDirectDBcall) throws Exception
	{
		String txnSettleTime = "";
		try 
		{
			if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
				txnSettleTime = WSDefaultInputsMap.getACHTxnSettlePeriod(isDirectDBcall,context);
			}
			else if(transferType!=null && (transferType.startsWith(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				txnSettleTime = "1";
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return txnSettleTime;
	}

	/**
	 * Gets the Query String for the Transaction Expiry based on the transaction type ...
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public static String getExpiryPeriodParams(String transferType,ServiceContext context,Boolean isDirectDBcall) throws Exception
	{
		String experiyPeriod = "";
		try 
		{
			//Transaction Expiry time..
			experiyPeriod = WSDefaultInputsMap.getTxnExpiryPeriod(isDirectDBcall,context);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return experiyPeriod;
	}

	/**
	 * Gets the Query String for the Maximum Future days based on the transaction type ...
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public static String getMaxFutureDaysParams(String transferType,ServiceContext context,Boolean isDirectDBcall) throws Exception
	{
		String maxFutureDays = "";
		try 
		{
			//Max future days..
			maxFutureDays = WSDefaultInputsMap.getTxnMaxFtrDts(isDirectDBcall,context);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return maxFutureDays;
	}

	/**
	 * Extract the complete User name from the UserDetails object..
	 * @param objMSUserDetails
	 * @return
	 * @throws Exception
	 */
	public static String extractCurrentUserName(MSUser_DetailsTO objMSUserDetails) throws Exception 
	{
		StringBuffer userFullName = new StringBuffer();
		try 
		{
			if(objMSUserDetails.getFirstName()!=null && !objMSUserDetails.getFirstName().trim().equalsIgnoreCase("")){
				userFullName.append(objMSUserDetails.getFirstName());
				userFullName.append(" ");
			}
			if(objMSUserDetails.getMiddleName()!=null && !objMSUserDetails.getMiddleName().trim().equalsIgnoreCase("")){
				userFullName.append(objMSUserDetails.getMiddleName());
				userFullName.append(" ");
			}
			if(objMSUserDetails.getLastName()!=null && !objMSUserDetails.getLastName().trim().equalsIgnoreCase("")){
				userFullName.append(objMSUserDetails.getLastName());
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return userFullName.toString();
	}

	/**
	 * Prints the event log response .... 
	 * @param contextData
	 */
	public static void logEventResponse(ServiceContext contextData)
	{
		try 
		{
			if(contextData!=null){
				StringBuffer responseBuffer = new StringBuffer();
				responseBuffer.append("Response Severity : " +contextData.getMaxSeverity() + "\r\n");

				responseBuffer.append("Response Messages - \r\n");
				ArrayList contextMessages = contextData.getMessages(); 
				if(contextMessages!=null){
					for(int i=0;i<contextMessages.size();i++){
						Message objContextMess = (Message)contextMessages.get(i); 
						responseBuffer.append("Message Type : " + objContextMess.getType().toString() + "\r\n");
						responseBuffer.append("Message Code : " + objContextMess.getCode() + "\r\n");
						ArrayList objResponse= (ArrayList)objContextMess.getArgs();
						if(objResponse!=null && !objResponse.isEmpty()){
							if(objResponse.get(0)!=null && !((String)objResponse.get(0)).trim().equalsIgnoreCase("")){
								responseBuffer.append("Message Description : " + objResponse.get(0) + "\r\n");
							}
						}
					}
				}
				System.out.println("Event Response : \n" + responseBuffer.toString());
				EBWLogger.logDebug("Event Response : \n" , responseBuffer.toString());
			}
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Push Transaction version mismatch error....
	 * @param context
	 * @throws Exception
	 */
	public static void pushTxnVersionMismatch(ServiceContext context) throws Exception
	{
		try 
		{
			ResourceBundle messages = ResourceBundle.getBundle("ErrMessage");
			String errorCode ="Pay_0054";
			context.addMessage(MessageType.SEVERE,ChannelsErrorCodes.VERSION_MISMATCH_ERROR,messages.getString(errorCode));
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Push Account version mismatch error....
	 * @param context
	 * @throws Exception
	 */
	public static void pushAccVersionMismatch(ServiceContext context) throws Exception
	{
		try 
		{
			ResourceBundle messages = ResourceBundle.getBundle("ErrMessage");
			String errorCode ="Pay_0060";
			context.addMessage(MessageType.SEVERE,ChannelsErrorCodes.BUSSINESS_ERROR,messages.getString(errorCode));
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Checks the permission for 24x7 Access..
	 * @return false if no permission else true.
	 */
	public static boolean check24x7Access(String transferType)
	{
		boolean isPermissionGranted = false;
		if(MSSystemDefaults.ENABLE_24X7 && MSSystemDefaults.PermittedTxnsList_24x7.contains(transferType)){
			isPermissionGranted = true;
		}
		return isPermissionGranted;
	}

	/**
	 * Evaluate Next transaction date based on 24x7 access..
	 * @param repeatValue
	 * @param prefPrevBusinessDate
	 * @param parentTxnDate
	 * @param transferType
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Date evaluateNextTxnDate(String repeatValue,Date prefPrevBusinessDate,Date parentTxnDate,String transferType,Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception 
	{
		Date nextBusinessDate = new Date();
		try {
			if(MSCommonUtils.check24x7Access(transferType) && !(repeatValue.equalsIgnoreCase("FBD") || repeatValue.equalsIgnoreCase("LBD"))) {
				nextBusinessDate = MSCommonUtils.calculatePrefNextTxnDate(repeatValue,prefPrevBusinessDate,parentTxnDate,transferType);
			}
			else {
				nextBusinessDate = MSCommonUtils.calculateNextBusinessDate(repeatValue,prefPrevBusinessDate,parentTxnDate,transferType,isDirectDBCall,serviceContext);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return nextBusinessDate;
	}

	/**
	 * Calculate Next Business Date function considering holiday..
	 * @param repeatValue
	 * @param prefPrevBusinessDate
	 * @param parentTxnDate
	 * @param transferType
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Date calculateNextBusinessDate(String repeatValue,Date prefPrevBusinessDate,Date parentTxnDate,String transferType,Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception 
	{
		Date nextBusinessDate = new Date();
		try {
			DateFunctions objdateFunctions = new DateFunctions();
			nextBusinessDate=objdateFunctions.getNextPossibleTxnDate(repeatValue,prefPrevBusinessDate,parentTxnDate,transferType,isDirectDBCall,serviceContext);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return nextBusinessDate;
	}

	/**
	 * Calculate Next Business Date function without considering holiday...
	 * @param repeatValue
	 * @param prefPrevBusinessDate
	 * @param parentTxnDate
	 * @param transferType
	 * @return
	 * @throws Exception
	 */
	public static Date calculatePrefNextTxnDate(String repeatValue,Date prefPrevBusinessDate,Date parentTxnDate,String transferType) throws Exception 
	{
		Date nextBusinessDate = new Date();
		try {
			DateFunctions objdateFunctions = new DateFunctions();
			nextBusinessDate=objdateFunctions.getPrefNextTxnDate(repeatValue,prefPrevBusinessDate,parentTxnDate,transferType);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return nextBusinessDate;
	}

	/**
	 * Calculates the Estimated Arrival date...
	 * @param prefPrevBusinessDate
	 * @param transferType
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Date calculateEstArrivalDate(Date prefPrevBusinessDate,String transferType,Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		Date nextEstArrDate = new Date();
		try {
			DateFunctions objdateFunctions = new DateFunctions();
			String repeatValue = "ERDT"; 
			Date parentTxnDate = prefPrevBusinessDate;  // No significance
			nextEstArrDate=objdateFunctions.getNextPossibleTxnDate(repeatValue,prefPrevBusinessDate,parentTxnDate,transferType,isDirectDBCall,serviceContext);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return nextEstArrDate;
	}

	/**
	 * Calculates the Transaction Expire date..
	 * @param prefPrevBusinessDate
	 * @param transferType
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Date calculateTxnExpiryDate(Date prefPrevBusinessDate,String transferType,Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception 
	{
		Date txnExpDate = new Date();
		try {
			DateFunctions objdateFunctions = new DateFunctions();
			String repeatValue = "EXPDT"; //Transaction expire date ...
			txnExpDate=objdateFunctions.getTransactionExpiryDate(repeatValue,prefPrevBusinessDate,transferType,isDirectDBCall,serviceContext);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return txnExpDate;
	}

	/**
	 * Calculate the First or Last business date ....
	 * @param repeatValue
	 * @param estArrivalDateVal
	 * @param transferType
	 * @return
	 * @throws Exception
	 */
	public static Date calculateStartBusinessDate(String repeatValue,Date estArrivalDateVal,String transferType,Boolean isDirectDBCall) throws Exception 
	{
		Date startBusinessDate = new Date();
		try {
			ServiceContext serviceContext = new ServiceContext();
			DateFunctions objdateFunctions = new DateFunctions();
			startBusinessDate=objdateFunctions.getNextFirstOrLastTxnDate(repeatValue,estArrivalDateVal,transferType,isDirectDBCall,serviceContext);
		}
		catch (Exception exception) {
			throw exception;
		}
		return startBusinessDate;
	}

	/**
	 * Calculate the Next Transfer Amount(Check for until dollar limit transfered...
	 * @param durationSelected
	 * @param thresholdAmount
	 * @param accumulatedAmount
	 * @param transferAmount
	 * @return
	 */
	public static BigDecimal calculateNextTxnAmount(int durationSelected,BigDecimal thresholdAmount,BigDecimal accumulatedAmount, BigDecimal transferAmount)
	{
		if(durationSelected==3)
		{
			BigDecimal remaining_amnt = thresholdAmount.subtract(accumulatedAmount);
			if(remaining_amnt.compareTo(transferAmount)==-1){
				return remaining_amnt;
			}
			else{
				return transferAmount;
			}
		}
		else {
			return transferAmount;
		}
	}
}
