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
import net.sf.json.JSONObject;

import org.apache.struts.util.LabelValueBean;

import com.tcs.Payments.DateUtilities.DateFunctions;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.Message;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.ms360.integration.AccountOwner;
import com.tcs.bancs.ms360.integration.Address;
import com.tcs.bancs.ms360.integration.AuthorizedEntity;
import com.tcs.bancs.ms360.integration.MMAccount;
import com.tcs.bancs.ms360.integration.MMContext;
import com.tcs.bancs.ms360.integration.PlatingAddress;
import com.tcs.bancs.ms360.integration.UserInfo;
import com.tcs.bancs.ms360.integration.UserWorkflowRole;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.OtherMSAccountTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.TrxnDetailsOutputTO;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountPlating;
import com.tcs.ebw.serverside.services.channelPaymentServices.WSDefaultInputsMap;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 *    224703       19-05-2011        3               ServerSide Error Check
 *    224703       23-05-2011        3               RTA Mapping
 *    224703       01-07-2011        3               CR 262
 *    224703       23-09-2011        P3-B            PLA  
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
		if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transfer_type.startsWith(ChkReqConstants.CHK))){
			payDebitaccnum = objFromMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			payDebitaccnum = objFromMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			payDebitaccnum = objToMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
			payDebitaccnum = objFromMSAcc_DetailsTO.getAccountNumber();
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
		if(msAccountNo!=null)
		{
			do {
				if(msAccountNo.length()>=6){
					break;
				}
				else {
					msAccountNo="0"+msAccountNo;
				}
			} while(msAccountNo.length()!=6);
		}
		return msAccountNo;
	}

	/**
	 * Setting the RTA Txn_Type Value depending on the Transaction type... 
	 */
	public static Double getRTATxnTypeValue(PaymentDetailsTO objPaymentDetails)
	{
		Double rtaTxn_Type = null;
		String txn_Type = objPaymentDetails.getTransfer_Type();

		if(objPaymentDetails.isReverseRTAFlag()) {
			if(txn_Type.startsWith(ChkReqConstants.CHK)){
				rtaTxn_Type=new Double(9);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				rtaTxn_Type=new Double(4);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				rtaTxn_Type=new Double(5);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
				rtaTxn_Type=new Double(14);
			}
		}
		else if(objPaymentDetails.isCancelRTAFlag()){
			if(txn_Type.startsWith(ChkReqConstants.CHK)){
				rtaTxn_Type=new Double(9);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				rtaTxn_Type=new Double(15);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				rtaTxn_Type=new Double(16);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
				rtaTxn_Type=new Double(14);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				rtaTxn_Type=new Double(19);
			}
		}
		else {
			if(txn_Type.startsWith(ChkReqConstants.CHK)){
				rtaTxn_Type=new Double(7);
			}
			else if(txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
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
	 * Setting the RTA Txn_Type Value depending on the Transaction type and Fee applicable.... 
	 */
	public static Double getRTATxnTypeValue_Fee(PaymentDetailsTO objPaymentDetails)
	{
		Double rtaTxn_Type = null;
		String txn_Type = objPaymentDetails.getTransfer_Type();

		if(objPaymentDetails.isReverseRTAFlag()) {
			if(txn_Type.startsWith(ChkReqConstants.CHK)){
				rtaTxn_Type=new Double(10);
			}
		}
		else if(objPaymentDetails.isCancelRTAFlag()) {
			if(txn_Type.startsWith(ChkReqConstants.CHK)){
				rtaTxn_Type=new Double(17);
			}
		}
		else {
			if(txn_Type.startsWith(ChkReqConstants.CHK)){
				rtaTxn_Type=new Double(8);
			}
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
	 * Setting the RTA JrnlDesc Value depending on the Transaction type ....
	 */
	public static String getRTAJrnlDesc(PaymentDetailsTO objPaymentDetails,CheckRequestTO objCheckRequest)
	{
		String jrnlDesc = null;
		String txn_Type = objPaymentDetails.getTransfer_Type();
		if(txn_Type.startsWith(ChkReqConstants.CHK)){
			if(objCheckRequest.getCertifiedFlag()!=null && objCheckRequest.getCertifiedFlag().equalsIgnoreCase("Y")){
				jrnlDesc = "Y";
			}
			else {
				jrnlDesc = "N";
			}
		}
		return jrnlDesc;
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

			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			//Loan Account Details ..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			if(txnDetails.containsKey("LoanAccountDetails")){
				objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
			}

			//Payment Details..
			String extNickName = objExternalAccDetails.getNick_name();
			String extAccNumber = objExternalAccDetails.getCpyaccnum();
			String transfer_Type = objPaymentDetails.getTransfer_Type();

			if(transfer_Type!=null && transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				frmAccTrlr2 = MSCommonUtils.getRTANameFormat(extNickName,extAccNumber);
			}
			else if(transfer_Type!=null && transfer_Type.startsWith(ChkReqConstants.CHK)){
				frmAccTrlr2 = objCheckRequest.getPayeeName();
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

			//Payment Details..
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

			if(transfer_Type!=null && (transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transfer_Type.startsWith(ChkReqConstants.CHK))){
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
	 * Setting the RTA TO_ACCT_TRLR_1 depending on the Transaction type ....
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
			else if(transfer_Type!=null && transfer_Type.startsWith(ChkReqConstants.CHK)){
				if(!objPaymentDetails.isTxnFeeRequest()){
					if(objPaymentDetails.isReverseRTAFlag() || objPaymentDetails.isCancelRTAFlag()){
						toAccTrlr1 = objPaymentDetails.getReverse_qualifier();
					}
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
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getRSARequestFlag(PaymentDetailsTO objPaymentDetails)
	{
		String rsa_Request_Flg = "N";
		String transfer_Type = objPaymentDetails.getTransfer_Type();
		String eventDesc = objPaymentDetails.getEventDesc();
		if(transfer_Type!=null && (transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
			if(eventDesc!=null && (eventDesc.equalsIgnoreCase(Bus_Rule_Input_Desc.Create_Confirm) || eventDesc.equalsIgnoreCase(Bus_Rule_Input_Desc.Modify_Confirm))){
				rsa_Request_Flg = "Y";
			}
		}
		return rsa_Request_Flg;
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
		if(objPaymentDetails.isTxnModified() || objPaymentDetails.isTxnApproved())
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
		if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transfer_type.startsWith(ChkReqConstants.CHK))){
			debitAccount_Num = objFromMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
			debitAccount_Num = objFromMSAcc_DetailsTO.getAccountNumber();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
			debitAccount_Num = objExternalAccDetails.getCpyaccnum();
		}
		else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
			debitAccount_Num = objFromMSAcc_DetailsTO.getAccountNumber();
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

		String creditAccount_Num = "";
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
	public MSUser_DetailsTO setMSUserDetailsTO(UserPrincipal objUserPrincipal)
	{
		MMContext objMMContextData = objUserPrincipal.getContextData();
		MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
		ArrayList contextAccounts = objMMContextData.getAccounts();

		//User Info details..
		UserInfo objUserInfo = objMMContextData.getUserInfo();
		objMSUserDetails.setFirstName(objUserInfo.getFirstName());
		objMSUserDetails.setLastName(objUserInfo.getLastName());
		objMSUserDetails.setMiddleName(objUserInfo.getMiddleInitial());
		objMSUserDetails.setRcafId(objUserInfo.getRACFId());

		//User Role details..
		UserWorkflowRole objUserWorkflowRole = objMMContextData.getWorkflowRole();
		objMSUserDetails.setApproveRole(objUserWorkflowRole.getApproverRole());
		objMSUserDetails.setInitiatorRole(objUserWorkflowRole.getInitiatorRole());

		//User actions and accessible accounts details..
		objMSUserDetails.setAccessibleAccounts(objMMContextData.getAccessibleAccounts());
		objMSUserDetails.setUserActions(objMMContextData.getUserAction());
		objMSUserDetails.setFunctionalRoleList(objUserPrincipal.getRoleList());
		objMSUserDetails.setContextAccounts(contextAccounts);

		return objMSUserDetails;
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
		objFromMSAcc_DetailsTO.setIraCode(objMMAccount.getIraCode());
		objFromMSAcc_DetailsTO.setKeyAccount(objMMAccount.getKeyAccount());
		objFromMSAcc_DetailsTO.setNovusSubProduct(objMMAccount.getNovusSubProduct());
		objFromMSAcc_DetailsTO.setOfficeNumber(objMMAccount.getOfficeNumber());
		objFromMSAcc_DetailsTO.setStatus(objMMAccount.getStatus());
		objFromMSAcc_DetailsTO.setTradeControl(objMMAccount.getTradeControl());
		objFromMSAcc_DetailsTO.setAccountName(getAccCorporateName(objMMAccount));
		objFromMSAcc_DetailsTO.setAccount_tier(objMMAccount.getAccount_tier());
		objFromMSAcc_DetailsTO.setSubClass(objMMAccount.getSubClass());
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
		objToMSAcc_DetailsTO.setIraCode(objMMAccount.getIraCode());
		objToMSAcc_DetailsTO.setKeyAccount(objMMAccount.getKeyAccount());
		objToMSAcc_DetailsTO.setNovusSubProduct(objMMAccount.getNovusSubProduct());
		objToMSAcc_DetailsTO.setOfficeNumber(objMMAccount.getOfficeNumber());
		objToMSAcc_DetailsTO.setStatus(objMMAccount.getStatus());
		objToMSAcc_DetailsTO.setTradeControl(objMMAccount.getTradeControl());
		objToMSAcc_DetailsTO.setAccountName(getAccCorporateName(objMMAccount));
		objToMSAcc_DetailsTO.setAccount_tier(objMMAccount.getAccount_tier());
		objToMSAcc_DetailsTO.setSubClass(objMMAccount.getSubClass());
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
		if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)||transferType.startsWith(ChkReqConstants.CHK))){
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
		else if(transferType!=null && transferType.equalsIgnoreCase(ChkReqConstants.CHK_LOC)){
			txn_type_desc = TxnTypeDesc.CHECK_LOC;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(ChkReqConstants.CHK_REG)){
			txn_type_desc = TxnTypeDesc.CHECK_REG;
		}
		else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
			txn_type_desc = TxnTypeDesc.PLA;
		}
		return txn_type_desc;
	}

	/**
	 * Frequency type description on PreConfirm screens...
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
	 * Frequency type description on Confirmation screens of MS360...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getFreqTypeDesc_TxnView(PaymentDetailsTO objPaymentDetails)
	{
		String freq_type_desc = "";
		String freq_type = objPaymentDetails.getFrequency_Type();
		String freq_duration = objPaymentDetails.getFrequency_DurationDesc();

		if(freq_type!=null && freq_type.equalsIgnoreCase("1")){
			freq_type_desc = TxnFrequencyDesc.ONE_TIME_1;
		}
		else if(freq_type!=null && freq_type.equalsIgnoreCase("2"))
		{
			if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("W")){
				freq_type_desc = TxnFrequencyDesc.REC_W_1;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("OW")){
				freq_type_desc = TxnFrequencyDesc.REC_OW_1;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("M")){
				freq_type_desc = TxnFrequencyDesc.REC_M_1;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("FBD")){
				freq_type_desc = TxnFrequencyDesc.REC_FBD_1;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("LBD")){
				freq_type_desc = TxnFrequencyDesc.REC_LBD_1;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("Q")){
				freq_type_desc = TxnFrequencyDesc.REC_Q_1;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("H")){
				freq_type_desc = TxnFrequencyDesc.REC_H_1;
			}
			else if(freq_duration!=null && freq_duration.trim().equalsIgnoreCase("Y")){
				freq_type_desc = TxnFrequencyDesc.REC_Y_1;
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
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("101")){
			txn_Status_desc = TxnStatusDesc.STATUS_101;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("102")){
			txn_Status_desc = TxnStatusDesc.STATUS_102;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("103")){
			txn_Status_desc = TxnStatusDesc.STATUS_103;
		}
		else if(txnStatusCode!=null && txnStatusCode.equalsIgnoreCase("110")){
			txn_Status_desc = TxnStatusDesc.STATUS_110;
		}
		return txn_Status_desc;
	}

	/**
	 * Initiator Role description...
	 * @param objMSUserDetails
	 * @return
	 */
	public static String getInitiatorRoleDesc(String initiator_role)
	{
		String initiator_role_desc = "";
		if(initiator_role!=null && initiator_role.trim().equalsIgnoreCase("INITBRAN")){
			initiator_role_desc = InitiatorRoleDesc.INITBRAN;
		}
		else if(initiator_role!=null && initiator_role.trim().equalsIgnoreCase("INITNBOP")){
			initiator_role_desc = InitiatorRoleDesc.INITNBOP;
		}
		else if(initiator_role!=null && initiator_role.trim().equalsIgnoreCase("Client")){
			initiator_role_desc = InitiatorRoleDesc.Client;
		}
		return initiator_role_desc;
	}

	/**
	 * Approver Role description...
	 * @param objMSUserDetails
	 * @return
	 * @throws Exception 
	 */
	public static String getApproverRoleDesc(String approver_role,ServiceContext context) throws Exception
	{
		String approver_role_desc = "";
		try 
		{
			ArrayList apprRoleDescList =  WSDefaultInputsMap.getApprRoleDesc(context);
			if(apprRoleDescList!=null && !apprRoleDescList.isEmpty())
			{
				for(int i=0;i<apprRoleDescList.size();i++)
				{
					ArrayList approverRole = (ArrayList)apprRoleDescList.get(i);
					if(approverRole!=null && !approverRole.isEmpty())
					{
						String apprRoleDomainId = (String)approverRole.get(0);
						String apprDesc = (String)approverRole.get(1);

						if(approver_role!=null && approver_role.trim().equalsIgnoreCase("APRVRISK") 
								&& apprRoleDomainId!=null && apprRoleDomainId.equalsIgnoreCase("RK")){
							approver_role_desc = apprDesc;
							break;
						}
						else if(approver_role!=null && approver_role.trim().equalsIgnoreCase("APRVBRAN")
								&& apprRoleDomainId!=null && apprRoleDomainId.equalsIgnoreCase("B")){
							approver_role_desc = apprDesc;
							break;
						}
						else if(approver_role!=null && approver_role.trim().equalsIgnoreCase("APRVNBOP")
								&& apprRoleDomainId!=null && apprRoleDomainId.equalsIgnoreCase("NB")){
							approver_role_desc = apprDesc;
							break;
						}
						else if(approver_role!=null && approver_role.trim().equalsIgnoreCase("APRVRSSO")
								&& apprRoleDomainId!=null && apprRoleDomainId.equalsIgnoreCase("RSS")){
							approver_role_desc = apprDesc;
							break;
						}
						else if(approver_role!=null && approver_role.trim().equalsIgnoreCase("APRVMARP")
								&& apprRoleDomainId!=null && apprRoleDomainId.equalsIgnoreCase("MR")){
							approver_role_desc = apprDesc;
							break;
						}
						else if(approver_role!=null && approver_role.trim().equalsIgnoreCase("APRVMAMG")
								&& apprRoleDomainId!=null && apprRoleDomainId.equalsIgnoreCase("MM")){
							approver_role_desc = apprDesc;
							break;
						}
						else if(approver_role!=null && approver_role.trim().equalsIgnoreCase("APRVMAVP")
								&& apprRoleDomainId!=null && apprRoleDomainId.equalsIgnoreCase("MV")){
							approver_role_desc = apprDesc;
							break;
						}
						else if(approver_role!=null && approver_role.trim().equalsIgnoreCase("APRVOFAC")
								&& apprRoleDomainId!=null && apprRoleDomainId.equalsIgnoreCase("OFAC")){
							approver_role_desc = apprDesc;
							break;
						}
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return approver_role_desc;
	}

	/**
	 * Extracting the KeyCient Id from the AccountPlating for the selected MS account only ....
	 * @param acntPlating
	 * @return
	 */
	public static String[] extractKeyClientId(AccountPlating acntPlating)
	{
		ArrayList authorisedEntities = new ArrayList();
		ArrayList keyClientId = new ArrayList();
		String[] keyClientIds = null;

		//External Accounts Input Mappings...
		if(acntPlating!=null)
		{
			authorisedEntities = acntPlating.getAuthorisedEntities();
			if(authorisedEntities!=null && !authorisedEntities.isEmpty())
			{
				for(int i =0; i< authorisedEntities.size();i++)
				{
					ArrayList authorisedEntity = ((ArrayList)authorisedEntities.get(i));
					if(authorisedEntity!=null && !authorisedEntity.isEmpty()){
						String keyClient = (String)authorisedEntity.get(0);
						if(keyClient!=null && !keyClient.trim().equalsIgnoreCase("")){
							keyClientId.add(keyClient);
						}
					}
				}
			}
		}
		keyClientIds = convertToStringArr(keyClientId);
		return keyClientIds;
	}

	/**
	 * Extracting the Spoke To from the AccountPlating for the selected MS account only ....
	 * @throws Exception 
	 */
	public static String extractSpokeTo(HashMap txnDetails,String spokeToId) throws Exception
	{
		String spokeTo = "";
		try 
		{
			//Mapping the payment detail object with the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Extract Debit account plating details...
			ArrayList authorisedEntities = new ArrayList();
			AccountPlating acntPlating = extractDebitAcntPlatingDtls(txnDetails);

			//External Accounts Input Mappings...
			if(acntPlating!=null)
			{
				authorisedEntities = acntPlating.getAuthorisedEntities();
				if(authorisedEntities!=null && !authorisedEntities.isEmpty())
				{
					for(int i =0; i< authorisedEntities.size();i++)
					{
						ArrayList authorisedEntity = ((ArrayList)authorisedEntities.get(i));
						if(authorisedEntity!=null && !authorisedEntity.isEmpty()){
							String hashedUniqueId = (String)authorisedEntity.get(0);
							String entityName = (String)authorisedEntity.get(1);
							if(hashedUniqueId!=null && hashedUniqueId.equalsIgnoreCase(spokeToId))
							{
								if(entityName.equalsIgnoreCase(MSSystemDefaults.AUTH_ENTITY_OTHER)){
									if(objPaymentDetails.getOtherAcntOwner()!=null){
										entityName = objPaymentDetails.getOtherAcntOwner();
									}
								}
								spokeTo = entityName;
								break;
							}
						}
					}
				}
			}
		}
		catch (Exception exception) {
			throw exception;
		}
		return spokeTo;
	}

	/**
	 * Extract the account plating instance object based on the transfer types..
	 * @param txnDetails
	 * @param acntPlating
	 * @return
	 * @throws Exception
	 */
	public static AccountPlating extractDebitAcntPlatingDtls(HashMap txnDetails) throws Exception
	{
		AccountPlating acntPlating = new AccountPlating();
		try
		{
			//Payment Details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//From account Details...
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//To account Details...
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			String transfer_type = objPaymentDetails.getTransfer_Type();
			if(transfer_type!=null && 
					(transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transfer_type.startsWith(ChkReqConstants.CHK) || transfer_type.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				acntPlating = objFromMSAcc_Details.getAccountPlating();
			}
			else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				acntPlating = objFromMSAcc_Details.getAccountPlating();
			}
			else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				acntPlating = objToMSAcc_Details.getAccountPlating();
			}
			return acntPlating ;
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Extracting the KeyCient Id from the context....
	 */
	public static String[] extractKeyClientId(MMContext objMMContextData)
	{
		ArrayList keyClientId = new ArrayList();
		ArrayList objMMContextAcc = objMMContextData.getAccounts();
		String[] keyClientIds = null;
		//External Accounts Input Mappings...
		for(int i =0; i< objMMContextAcc.size();i++)
		{
			MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(i);
			ArrayList accOwners = (ArrayList)objMMAccount.getAuthorizedEntities();
			if(accOwners!=null && !accOwners.isEmpty()){
				for(int j =0;j<accOwners.size();j++)
				{
					AuthorizedEntity accOwner = (AuthorizedEntity)accOwners.get(j);
					String keyClient = accOwner.getHashedUniqueID();

					//Mapping the keyClient Id...
					if(keyClient!=null && !keyClient.trim().equalsIgnoreCase("")){
						keyClientId.add(keyClient);
					}
				}
			}
		}
		keyClientIds = convertToStringArr(keyClientId);
		return keyClientIds;
	}

	/**
	 * Gets the account owner or the corporate name from the first element of the owner list...
	 */
	public static String getAccCorporateName(MMAccount objMMAccount)
	{
		String accountName = "";
		if(objMMAccount!=null && objMMAccount.getAccountName()!=null){
			accountName = objMMAccount.getAccountName();
		}
		return accountName;
	}

	/**
	 * Gets the spoke to collections object (Internal and Checks)..
	 * All the authorized entities as part of the account plating response will be added to the list...
	 * @return
	 * @throws Exception 
	 */
	public static Collection getSpokeToDetails(ArrayList authorisedEntities) throws Exception
	{
		EBWLogger.trace("MSCommonUtils", "Starting getSpokeToDetails for getting all the account owners..."); 
		Collection<LabelValueBean> accountOwnerCols;
		try 
		{
			//MMContext mappings..
			ArrayList accountOwners = new ArrayList();

			//Fetching the account owners for the selected accounts..
			if(authorisedEntities!=null && !authorisedEntities.isEmpty())
			{
				for(int i =0; i< authorisedEntities.size();i++)
				{
					ArrayList authorisedEntity = ((ArrayList)authorisedEntities.get(i));
					if(authorisedEntity!=null && !authorisedEntity.isEmpty()){
						String hashedUniqueId = (String)authorisedEntity.get(0);
						String entityName = (String)authorisedEntity.get(1);
						if(!accountOwners.contains(entityName))
						{
							//If the entity name is "Other", we need to show "Enter a name".
							if(entityName.equalsIgnoreCase(MSSystemDefaults.AUTH_ENTITY_OTHER)){
								entityName =  MSSystemDefaults.AUTH_ENTITY_OTHER_VAL;
							}

							//Single account owners (Display and Value pair)..
							ArrayList accountOwner = new ArrayList();
							String value = hashedUniqueId;
							String display = entityName;

							accountOwner.add(value);
							accountOwner.add(display);
							accountOwners.add(accountOwner);
						}
					}
				}
			}

			//Sorting the spoke to details..
			sortSpokeToEntities(accountOwners);

			//Getting the collection in the Label Value bean format..
			accountOwnerCols = getComboboxValueDisp(accountOwners,MSSystemDefaults.DEFAULT_SPOKE_TO_TEXT);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return accountOwnerCols;
	}

	/**
	 * Gets the spoke to collections object (ACH Transfers alone)..
	 * Only that authorized entity whose hashed_unique_id matches with external account owner key client should be added to the list...
	 * @param authorisedEntities
	 * @param objExternalAccDetails
	 * @return
	 * @throws Exception
	 */
	public static Collection getSpokeToDetails(ArrayList authorisedEntities,DsExtPayeeDetailsOutTO objExternalAccDetails) throws Exception
	{
		EBWLogger.trace("MSCommonUtils", "Starting getSpokeToDetails for getting all the account owners..."); 
		Collection<LabelValueBean> accountOwnerCols;
		try 
		{
			//MMContext mappings..
			ArrayList accountOwners = new ArrayList();
			String extAccKeyClientId = objExternalAccDetails.getKey_client();

			//Fetching the account owners for the selected accounts..
			if(authorisedEntities!=null && !authorisedEntities.isEmpty())
			{
				for(int i =0; i< authorisedEntities.size();i++)
				{
					ArrayList authorisedEntity = ((ArrayList)authorisedEntities.get(i));
					if(authorisedEntity!=null && !authorisedEntity.isEmpty()){
						String hashedUniqueId = (String)authorisedEntity.get(0);
						String entityName = (String)authorisedEntity.get(1);
						if(!accountOwners.contains(entityName))	
						{
							//If the entity name is "Other", we need to show "Enter a name".
							if(entityName.equalsIgnoreCase(MSSystemDefaults.AUTH_ENTITY_OTHER)){
								entityName =  MSSystemDefaults.AUTH_ENTITY_OTHER_VAL;
							}

							//Adding only the authorized entity name 
							if(hashedUniqueId!=null && extAccKeyClientId!=null && hashedUniqueId.equalsIgnoreCase(extAccKeyClientId))
							{
								//Single account owners (Display and Value pair)..
								ArrayList accountOwner = new ArrayList();
								String value = hashedUniqueId;
								String display = entityName;

								accountOwner.add(value);
								accountOwner.add(display);
								accountOwners.add(accountOwner);
								break;
							}
						}
					}
				}
			}

			//Sorting the spoke to details..
			sortSpokeToEntities(accountOwners);

			//Getting the collection in the Label Value bean format..
			accountOwnerCols = getComboboxValueDisp(accountOwners,MSSystemDefaults.DEFAULT_SPOKE_TO_TEXT);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return accountOwnerCols;
	}

	/**
	 * Sorting Spoke to details...
	 * @param accountOwners
	 * @throws Exception 
	 */
	public static void sortSpokeToEntities(ArrayList accountOwners) throws Exception 
	{
		try 
		{
			//Sorting the spoke to details...
			for(int i = 0 ;i <accountOwners.size() ;i++) 
			{
				ArrayList acntOwner = (ArrayList)accountOwners.get(i);
				String display = (String)acntOwner.get(1);
				if(display!=null && display.equalsIgnoreCase(MSSystemDefaults.AUTH_ENTITY_OTHER_VAL)){
					accountOwners.remove(i); //removing it from the current position...
					accountOwners.add(acntOwner); //adding at the end of the same list...
					break;
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * JSON Array Object Formatted for Documents bulleted options ..
	 * @param comboData
	 * @return
	 * @throws Exception 
	 */
	public static String formatDocumentsReq(ArrayList documentList) throws Exception
	{
		EBWLogger.trace("MSCommonUtils", "Starting formatDocumentsReq() for formatting the documents req for the Signed or Either auth mode.."); 
		String documentsReq = null;
		try 
		{
			if(documentList!=null && !documentList.isEmpty())
			{
				//Adding the Hash Map for the from accounts in the JSON ArrayObject.
				JSONArray jsonFormattedArr = JSONArray.fromObject(documentList.toArray());

				//Setting the JSON object from the HashMap..
				HashMap docMaps = new HashMap();
				docMaps.put("AuthDocuments", jsonFormattedArr);
				JSONObject jsonDocumentsObject = JSONObject.fromObject(docMaps);  
				documentsReq= jsonDocumentsObject.toString();
			}
		} 
		catch (Exception exception) {
			throw exception;
		}   
		return documentsReq;
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
		for(int i=0; i<populateData.size(); i++)
		{
			dataOption =(ArrayList)populateData.get(i);
			String value= (String)dataOption.get(0);
			String display = (String)dataOption.get(1);
			comboDataOptions.add(new LabelValueBean(display, value));
		}
		return comboDataOptions;
	}

	/**
	 * Formats the verbal auth time in HH:MM format...
	 * @param hour
	 * @param min
	 * @param timeFormat
	 * @return
	 */
	public static String formatAuthTime(String hour,String min,String timeFormat)
	{
		String verbalAuthTime = null;
		if(hour!=null && min!=null && timeFormat!=null && !hour.trim().equalsIgnoreCase("") 
				&& !min.trim().equalsIgnoreCase("") && !timeFormat.trim().equalsIgnoreCase("")){
			verbalAuthTime = hour+":"+min+" "+timeFormat;
		}
		return verbalAuthTime;
	}

	/**
	 * Converts ArrayList to the String [].
	 * @param arraylist
	 * @return
	 */
	public static String[] convertToStringArr(ArrayList arraylist)
	{
		String str [] = null;
		if(!arraylist.isEmpty()){
			str = (String []) arraylist.toArray (new String [arraylist.size ()]);
		}
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
	public static MMAccount getMSAccFormat(MMAccount objMMAccount){

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
			}
			while(officeAcc.length()!=3);
		}
		objMMAccount.setOfficeNumber(officeAcc);

		//Formatting Account number ...
		String accNumber = objMMAccount.getAccountNumber(); 
		if(accNumber!=null){
			do{
				if(accNumber.length()>=6){
					break;
				}
				else {
					accNumber="0"+accNumber;
				}
			}
			while(accNumber.length()!=6);
		}
		objMMAccount.setAccountNumber(accNumber);

		//Formatting FA number...
		String faNumber = objMMAccount.getFaNumber();  
		if(faNumber!=null) {
			do{
				if(faNumber.length()>=3){
					break;
				}
				else {
					faNumber="0"+faNumber;
				}
			}
			while(faNumber.length()!=3);
		}
		objMMAccount.setFaNumber(faNumber);

		return objMMAccount;
	}

	/**
	 * Setting the blank account attributes to NA for all the transaction view details window...
	 */
	public static void formatAccDetails(TrxnDetailsOutputTO txnViewDetails)
	{
		String not_applicable_desc = MSSystemDefaults.NOT_APPLICABLE;

		//Formatting From account details..
		if(txnViewDetails.getAccClassFromAcc()==null || txnViewDetails.getAccClassFromAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setAccClassFromAcc(not_applicable_desc);
		}
		if(txnViewDetails.getAccSubClassFromAcc()==null || txnViewDetails.getAccSubClassFromAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setAccSubClassFromAcc(not_applicable_desc);
		}
		if(txnViewDetails.getAccNameFromAcc()==null || txnViewDetails.getAccNameFromAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setAccNameFromAcc(not_applicable_desc);
		}
		if(txnViewDetails.getAccDtlsFromAcc()==null || txnViewDetails.getAccDtlsFromAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setAccDtlsFromAcc(not_applicable_desc);
		}
		if(txnViewDetails.getAccTierFromAcc()==null || txnViewDetails.getAccTierFromAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setAccTierFromAcc(not_applicable_desc);
		}
		if(txnViewDetails.getRoutingNoFromAcc()==null || txnViewDetails.getRoutingNoFromAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setRoutingNoFromAcc(not_applicable_desc);
		}
		if(txnViewDetails.getOwnerFromAcc()==null || txnViewDetails.getOwnerFromAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setOwnerFromAcc(not_applicable_desc);
		}

		//Formatting To account details..
		if(txnViewDetails.getAccClassToAcc()==null || txnViewDetails.getAccClassToAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setAccClassToAcc(not_applicable_desc);
		}
		if(txnViewDetails.getAccSubClassToAcc()==null || txnViewDetails.getAccSubClassToAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setAccSubClassToAcc(not_applicable_desc);
		}
		if(txnViewDetails.getAccNameToAcc()==null || txnViewDetails.getAccNameToAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setAccNameToAcc(not_applicable_desc);
		}
		if(txnViewDetails.getAccDtlsToAcc()==null || txnViewDetails.getAccDtlsToAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setAccDtlsToAcc(not_applicable_desc);
		}
		if(txnViewDetails.getAccTierToAcc()==null || txnViewDetails.getAccTierToAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setAccTierToAcc(not_applicable_desc);
		}
		if(txnViewDetails.getRoutingNoToAcc()==null || txnViewDetails.getRoutingNoToAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setRoutingNoToAcc(not_applicable_desc);
		}
		if(txnViewDetails.getOwnerToAcc()==null || txnViewDetails.getOwnerToAcc().trim().equalsIgnoreCase("")){
			txnViewDetails.setOwnerToAcc(not_applicable_desc);
		}

		//IRA Transaction Details...
		if(txnViewDetails.getQualifier()==null || txnViewDetails.getQualifier().trim().equalsIgnoreCase("")){
			txnViewDetails.setQualifier("");
		}
		if(txnViewDetails.getReverse_qualifier()==null || txnViewDetails.getReverse_qualifier().trim().equalsIgnoreCase("")){
			txnViewDetails.setReverse_qualifier("");
		}

		//Formatting Check request Details..
		if(txnViewDetails.getCertifiedFlag()==null || txnViewDetails.getCertifiedFlag().trim().equalsIgnoreCase("")){
			txnViewDetails.setCertifiedFlag(not_applicable_desc);
		}
		if(txnViewDetails.getCheckNo()==null || txnViewDetails.getCheckNo().trim().equalsIgnoreCase("")){
			txnViewDetails.setCheckNo(not_applicable_desc);
		}
		if(txnViewDetails.getTrackingId()==null || txnViewDetails.getTrackingId().trim().equalsIgnoreCase("")){
			txnViewDetails.setTrackingId(not_applicable_desc);
		}
		if(txnViewDetails.getPayeeToName()==null || txnViewDetails.getPayeeToName().trim().equalsIgnoreCase("")){
			txnViewDetails.setPayeeToName("");
		}
		if(txnViewDetails.getPrintingBranch()==null || txnViewDetails.getPrintingBranch().trim().equalsIgnoreCase("")){
			txnViewDetails.setPrintingBranch(not_applicable_desc);
		}
		if(txnViewDetails.getPrintMemoOn()==null || txnViewDetails.getPrintMemoOn().trim().equalsIgnoreCase("")){
			txnViewDetails.setPrintMemoOn(not_applicable_desc);
		}
		if(txnViewDetails.getDeliveredToType()==null || txnViewDetails.getDeliveredToType().trim().equalsIgnoreCase("")){
			txnViewDetails.setDeliveredToType("");
		}
		if(txnViewDetails.getTypeOfId()==null || txnViewDetails.getTypeOfId().trim().equalsIgnoreCase("")){
			txnViewDetails.setTypeOfId("");
		}
		if(txnViewDetails.getIdNumber()==null || txnViewDetails.getIdNumber().trim().equalsIgnoreCase("")){
			txnViewDetails.setIdNumber("");
		}
		if(txnViewDetails.getThirdPartyReason()==null || txnViewDetails.getThirdPartyReason().trim().equalsIgnoreCase("")){
			txnViewDetails.setThirdPartyReason(not_applicable_desc);
		}
		if(txnViewDetails.getDeliveryAddress()==null || txnViewDetails.getDeliveryAddress().trim().equalsIgnoreCase("")){
			txnViewDetails.setDeliveryAddress(not_applicable_desc);
		}
		if(txnViewDetails.getCheckMemo()==null || txnViewDetails.getCheckMemo().trim().equalsIgnoreCase("")){
			txnViewDetails.setCheckMemo(not_applicable_desc);
		}
	}

	/**
	 * Formats the frequency...
	 * @param paymentDetails
	 * @return
	 * @throws Exception 
	 */
	public static String formatFrequency(PaymentDetailsTO paymentDetails) throws Exception
	{
		String formatted_Freq = "";
		String freq_type = paymentDetails.getFrequency_Type();

		if(freq_type!=null && freq_type.equalsIgnoreCase("1")){
			formatted_Freq = MSCommonUtils.getFreqTypeDesc_TxnView(paymentDetails);
		}
		else if(freq_type!=null && freq_type.equalsIgnoreCase("2"))
		{
			StringBuffer formatted_freq_desc = new StringBuffer();

			//Frequency description ....
			String frequency = MSCommonUtils.getFreqTypeDesc_TxnView(paymentDetails);

			//Repeat value description...
			String repeat_desc = MSCommonUtils.getRepeatDesc_View(paymentDetails);

			//Accumulated amount and transfers formatting...
			Object[] repeatValue = {MSCommonUtils.formatTxnAmount(paymentDetails.getAccumulated_amount()),paymentDetails.getAccumulated_transfers()};
			String repeatCode = "Repeat_005";
			ResourceBundle messages = ResourceBundle.getBundle("ErrMessage");	
			String accmulated_Amt_Txns = MessageFormat.format(messages.getString(repeatCode), repeatValue);

			if(frequency!=null){
				formatted_freq_desc.append(frequency);
			}
			if(repeat_desc!=null){
				formatted_freq_desc.append(" ("+repeat_desc+")"+"\r\n");
			}
			if(accmulated_Amt_Txns!=null){
				formatted_freq_desc.append(accmulated_Amt_Txns);
			}

			formatted_Freq = formatted_freq_desc.toString();
		}
		return formatted_Freq;
	}

	/**
	 * Formats the confirmation number...
	 * @param paymentDetails
	 * @return
	 */
	public static String formatConfirmationNum(PaymentDetailsTO paymentDetails)
	{
		String formatted_ConfNo = "";
		String freq_type = paymentDetails.getFrequency_Type();
		if(freq_type!=null && freq_type.equalsIgnoreCase("1")){
			formatted_ConfNo = paymentDetails.getTransactionId();
		}
		else if(freq_type!=null && freq_type.equalsIgnoreCase("2"))
		{
			Object[] ConfirmationNumValue = {paymentDetails.getTransactionId(),paymentDetails.getOriginating_parent_conf_no()};
			String ConfNumCode = "ConfNum_001";
			ResourceBundle messages = ResourceBundle.getBundle("ErrMessage");	
			formatted_ConfNo = MessageFormat.format(messages.getString(ConfNumCode), ConfirmationNumValue);
		}
		return formatted_ConfNo;
	}

	/**
	 * Returns the delivery address....
	 * @param objCheckRequest
	 * @return
	 */
	public static String getDeliveryAddress(CheckRequestTO objCheckRequest)
	{
		StringBuffer deliveryAddress = new StringBuffer();
		if(objCheckRequest.getDeliveryAddrLine1()!=null && !objCheckRequest.getDeliveryAddrLine1().trim().equalsIgnoreCase("")){
			deliveryAddress.append(objCheckRequest.getDeliveryAddrLine1());
		}
		if(objCheckRequest.getDeliveryAddrLine2()!=null && !objCheckRequest.getDeliveryAddrLine2().trim().equalsIgnoreCase("")){
			deliveryAddress.append(",");
			deliveryAddress.append(objCheckRequest.getDeliveryAddrLine2());
		}
		if(objCheckRequest.getDeliveryAddrLine3()!=null && !objCheckRequest.getDeliveryAddrLine3().trim().equalsIgnoreCase("")){
			deliveryAddress.append(",");
			deliveryAddress.append(objCheckRequest.getDeliveryAddrLine3());
		}
		if(objCheckRequest.getDeliveryAddrLine4()!=null && !objCheckRequest.getDeliveryAddrLine4().trim().equalsIgnoreCase("")){
			deliveryAddress.append(",");
			deliveryAddress.append(objCheckRequest.getDeliveryAddrLine4());
		}
		return deliveryAddress.toString();
	}

	/**
	 * Returns the Check Memo....
	 * @param objCheckRequest
	 * @return
	 */
	public static String getCheckMemo(CheckRequestTO objCheckRequest)
	{	
		StringBuffer checkMemo = new StringBuffer();
		if(objCheckRequest.getMemoLine1()!=null){
			checkMemo.append(objCheckRequest.getMemoLine1().trim());
			checkMemo.append("\r\n");
		}
		if(objCheckRequest.getMemoLine2()!=null){
			checkMemo.append(objCheckRequest.getMemoLine2().trim());
			checkMemo.append("\r\n");
		}
		return checkMemo.toString();
	}

	/**
	 * Returns the Print Memo On....
	 * @param objCheckRequest
	 * @return
	 */
	public static String getPrintMemoOn(CheckRequestTO objCheckRequest)
	{
		String printMemoOn = "";
		if(objCheckRequest.getPrintMemoCheckFlag()!=null && objCheckRequest.getPrintMemoCheckFlag().equalsIgnoreCase("Y")){
			printMemoOn = ChkReqConstants.MEMO_CHECK;
		}
		else if(objCheckRequest.getPrintMemoStubFlag()!=null && objCheckRequest.getPrintMemoStubFlag().equalsIgnoreCase("Y")){
			printMemoOn = ChkReqConstants.MEMO_STUB;
		}
		return printMemoOn;
	}

	/**
	 * Gets the third party reason code..
	 * @param objCheckRequest
	 * @return
	 */
	public static String getThirdPartyReason(CheckRequestTO objCheckRequest)
	{
		String thridPartyReason_desc = "";
		Double thridPartyReason_id = objCheckRequest.getThirdPartyReason();
		if(thridPartyReason_id!=null && thridPartyReason_id.equals(1D)){
			thridPartyReason_desc = ChkReqConstants.THIRD_PARTY_REASON_1;
		}
		else if(thridPartyReason_id!=null && thridPartyReason_id.equals(2D)){
			thridPartyReason_desc = ChkReqConstants.THIRD_PARTY_REASON_2;
		}
		else if(thridPartyReason_id!=null && thridPartyReason_id.equals(3D)){
			thridPartyReason_desc = ChkReqConstants.THIRD_PARTY_REASON_3;
		}
		else if(thridPartyReason_id!=null && thridPartyReason_id.equals(4D)){
			thridPartyReason_desc = ChkReqConstants.THIRD_PARTY_REASON_4;
		}
		else if(thridPartyReason_id!=null && thridPartyReason_id.equals(5D)){
			thridPartyReason_desc = ChkReqConstants.THIRD_PARTY_REASON_5;
		}
		else if(thridPartyReason_id!=null && thridPartyReason_id.equals(6D)){
			thridPartyReason_desc = ChkReqConstants.THIRD_PARTY_REASON_6;
		}
		return thridPartyReason_desc;
	}

	/**
	 * Gets the certified description as Yes/No..
	 * @param objCheckRequest
	 * @return
	 */
	public static String getCheckCertifiedDesc(CheckRequestTO objCheckRequest)
	{
		String certifiedFlag = ChkReqConstants.CERTIFIED_N;
		if(objCheckRequest.getCertifiedFlag()!=null && objCheckRequest.getCertifiedFlag().equalsIgnoreCase("Y")){
			certifiedFlag = ChkReqConstants.CERTIFIED_Y;
		}
		return certifiedFlag;
	}

	/**
	 * Check print type ID..
	 * @param objCheckRequest
	 * @return
	 */
	public static String getCheckPrintTypeOfId(CheckRequestTO objCheckRequest)
	{
		String type_of_id_desc = "";
		Double type_of_id = objCheckRequest.getTypeOfId();
		String delivered_to_type = objCheckRequest.getDeliveredToType();
		if(delivered_to_type!=null && delivered_to_type.equalsIgnoreCase(ChkReqConstants.DELIVERED_TO_CLIENT)){		
			if(type_of_id!=null && type_of_id.equals(1D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_1;
			}
			else if(type_of_id!=null && type_of_id.equals(2D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_2;
			}
			else if(type_of_id!=null && type_of_id.equals(3D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_3;
			}
			else if(type_of_id!=null && type_of_id.equals(4D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_4;
			}
			else if(type_of_id!=null && type_of_id.equals(5D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_5;
			}
			else if(type_of_id!=null && type_of_id.equals(6D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_7;
			}
		}
		else if(delivered_to_type!=null && delivered_to_type.equalsIgnoreCase(ChkReqConstants.DELIVERED_TO_THIRD_PARTY)){
			if(type_of_id!=null && type_of_id.equals(1D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_2;
			}
			else if(type_of_id!=null && type_of_id.equals(2D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_3;
			}
			else if(type_of_id!=null && type_of_id.equals(3D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_4;
			}
			else if(type_of_id!=null && type_of_id.equals(4D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_5;
			}
			else if(type_of_id!=null && type_of_id.equals(5D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_6;
			}
			else if(type_of_id!=null && type_of_id.equals(6D)){
				type_of_id_desc = ChkReqConstants.TYPE_OF_ID_7;
			}
		}
		else {
			type_of_id_desc = null;
		}
		return type_of_id_desc;
	}

	/**
	 * Check print type ID..
	 * @param objCheckRequest
	 * @return
	 * @throws Exception 
	 */
	public static String setFeeAmntDisplay(HashMap txnDetails) throws Exception
	{
		StringBuffer feeAmntDisplay = new StringBuffer();
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

			if(objPaymentDetails!=null){
				feeAmntDisplay.append(MSCommonUtils.formatTxnAmount(objPaymentDetails.getTxn_Fee_Charge()));
				if(objCheckRequest!=null && objCheckRequest.getFee()!=null && objCheckRequest.getFee().compareTo(new BigDecimal(0)) == 1 && objCheckRequest.getChargedTo()!=null && objCheckRequest.getChargedTo().equalsIgnoreCase(ChkReqConstants.CHARGE_TO_BRANCH)){
					feeAmntDisplay.append("("+ChkReqConstants.FEE_WARRNING_BRANCH+")");
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return feeAmntDisplay.toString();
	}

	/**
	 * Get the other MS Account details and map the same to the acnt object(From/To)..
	 * @param otherMSAcc
	 * @param accntObject
	 * @throws Exception 
	 */
	public Object getOtherMSAccountDetails(OtherMSAccountTO otherMSAcc,Object accntObject) throws Exception
	{
		try 
		{
			String errorCode = "OHTER_MSACC_ERR";
			if(otherMSAcc!=null)
			{
				//Serializing the JSON String to JSON Object...
				String acntNumber = otherMSAcc.getOtherMSAccNumber().substring(3,9);
				String officeNumber = otherMSAcc.getOtherMSAccNumber().substring(0,3);
				String faNumber = otherMSAcc.getOtherMSAccNumber().substring(9);
				String keyAcntNumber = otherMSAcc.getOtherMSKeyAccount();
				String accountName =otherMSAcc.getOtherMSAccName();

				if(acntNumber!=null && officeNumber!=null && faNumber!=null && keyAcntNumber!=null &&  
						!acntNumber.equals("") && !officeNumber.equals("") && !faNumber.equals("")
						&& !keyAcntNumber.equals(""))
				{
					if(accntObject!=null && accntObject instanceof FromMSAcc_DetailsTO)
					{
						FromMSAcc_DetailsTO objAccDetails = (FromMSAcc_DetailsTO)accntObject;
						objAccDetails.setAccountNumber(acntNumber);
						objAccDetails.setFaNumber(faNumber);
						objAccDetails.setOfficeNumber(officeNumber);
						objAccDetails.setKeyAccount(keyAcntNumber);
						objAccDetails.setAccountName(accountName);
					}
					if(accntObject!=null && accntObject instanceof ToMSAcc_DetailsTO)
					{
						ToMSAcc_DetailsTO objAccDetails = (ToMSAcc_DetailsTO)accntObject;
						objAccDetails.setAccountNumber(acntNumber);
						objAccDetails.setFaNumber(faNumber);
						objAccDetails.setOfficeNumber(officeNumber);
						objAccDetails.setKeyAccount(keyAcntNumber);
						objAccDetails.setAccountName(accountName);
					}
				}
				else {
					throw new EbwException(errorCode);
				}
			}
			else {
				throw new EbwException(errorCode);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return accntObject;
	}

	/**
	 * Extracts the data from the service context with the Message Type as CRITICAL...
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
					if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.ACCESS_ERROR){ 
						errorCode = "Access_Err001";
						//Message format ...
						String contactNumber = WSDefaultInputsMap.getMS360ContactCenter(contextData);
						Object[] objAccessCheck = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objAccessCheck);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.NO_ANCHOR_ACCOUNT_SET){ 
						errorCode = "Access_Err002";
						//Message format ...
						String contactNumber = WSDefaultInputsMap.getMS360ContactCenter(contextData);
						Object[] objAccessCheck = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objAccessCheck);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.APPROVER_OWNER_FAILURE){ 
						errorCode = "Approval_Err001";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.APPROVER_ROLE_FAILUE){ 
						errorCode = "Approval_Err002";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.INT_ACC_DAP_FAILURE){ 
						errorCode = "Dap_Err001";
						//Message format ...
						String contactNumber = WSDefaultInputsMap.getMS360ContactCenter(contextData);
						Object[] objAccessCheck = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objAccessCheck);
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
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.EXT_ACC_SPOKE_TO_FAILURE){ 
						errorCode = "Dap_Err004";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()== ChannelsErrorCodes.EXT_ACC_SIGNED_BY_FAILURE){ 
						errorCode = "Dap_Err005";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.STATUS_CONSISTENCY_FAILURE){
						errorCode = "Status_Err001";
						//Message format ...
						String contactNumber = WSDefaultInputsMap.getMS360ContactCenter(contextData);
						Object[] objStatusCheck = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objStatusCheck);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.IRA_TXN_FAILURE){ // Check the message code from the context, add only if its a IRA_TXN_FAILURE
						errorCode = "IRATxn_Err001";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.PRINT_ACCESS_ERROR){ // Check the message code from the context, add only if its a IRA_TXN_FAILURE
						errorCode = "PrintCheck_Err001";
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.INVALID_ROTUING_NUM){ // Check the message code from the context, add only if its a INVALID_ROUTING_NUMBER.
						errorCode = "InvalidABANum_Err001";
						//Message format ...
						String contactNumber = WSDefaultInputsMap.getMS360ContactCenter(contextData);
						Object[] objRoutingNum = {contactNumber};
						errorMessage = MessageFormat.format(messages.getString(errorCode), objRoutingNum);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.MS_ACNT_NOT_FOUND){ // Check the message code from the context, add only if its a MS_ACNT_NOT_FOUND.
						errorCode = "AcntNotFound_Err001";
						//Message format ...
						errorMessage = messages.getString(errorCode);
						break;
					}
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.CANCEL_TXN_NOT_ALLOWED){ // Check the message code from the context, add only if its a CANCEL_TXN_NOT_ALLOWED.
						errorCode = "CancelTxn_Err001";
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
					else if(objContextMess.getType() == MessageType.CRITICAL && objContextMess.getCode()==ChannelsErrorCodes.NO_FEE_CHARGE_OVERNIGHT_MAIL){ // Check the message code from the context, add only if its a NO_FEE_CHARGE_OVERNIGHT_MAIL.
						errorCode = "No_Overnight_Fee_Charge";
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
			for(int i=0;i<contextMessages.size();i++){
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
	 * Gets the check_type for the check number interface only...
	 * @param objCheckRequest
	 * @return
	 */
	public static String getCheckNumber_ChkType(CheckRequestTO objCheckRequest)
	{
		String check_type = " ";
		if(objCheckRequest.getCertifiedFlag()!=null && objCheckRequest.getCertifiedFlag().equalsIgnoreCase("Y")){
			check_type = ChkReqConstants.CHECKTYPE_CERTIFIED;
		}
		else if(objCheckRequest.getPickUpOption()!=null && objCheckRequest.getPickUpOption().equals(2D)){
			check_type = ChkReqConstants.CHECKTYPE_OVERNIGHT;
		}
		return check_type;
	}

	/**
	 * Gets the application name for the print check number interface only...
	 * @param objCheckRequest
	 * @return
	 * @throws Exception 
	 */
	public static String getPrintCheck_ApplName(CheckRequestTO objCheckRequest,ServiceContext context) throws Exception
	{
		String printAppName = "";
		try 
		{
			printAppName = WSDefaultInputsMap.getPrintNonCertifiedAppName(context);
			if(objCheckRequest.getCertifiedFlag()!=null && objCheckRequest.getCertifiedFlag().equalsIgnoreCase("Y")){
				printAppName = WSDefaultInputsMap.getPrintCertifiedAppName(context);
			}
		} catch (Exception exception) {
			throw exception;
		}
		return printAppName;
	}

	/**
	 * Gets the check_type for the print check  interface only...
	 * @param objCheckRequest
	 * @return
	 */
	public static String getPrintCheck_ChkType(CheckRequestTO objCheckRequest)
	{
		String printCheckType = ChkReqConstants.PRINTCHECKTYPE_NON_CERTIFIED;
		if(objCheckRequest.getCertifiedFlag()!=null && objCheckRequest.getCertifiedFlag().equalsIgnoreCase("Y")){
			printCheckType = ChkReqConstants.PRINTCHECKTYPE_CERTIFIED;
		}
		return printCheckType;
	}

	/**
	 * IRA Transfer types for getting the transfer types descriptions..
	 * @param transferType
	 * @return
	 */
	public static String getIRATxnType(String transferType)
	{
		String txn_type = null;
		if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
			txn_type = IRA_Input_Desc.ACH;
		}
		else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
			txn_type = IRA_Input_Desc.CHECK;
		}
		return txn_type;
	}

	/**
	 * IRA Default plan code for getting the transfer types descriptions..
	 * @param transferType
	 * @return
	 * @throws Exception 
	 */
	public static String getDefaultIRAPlanCode(String transferType,ServiceContext context) throws Exception
	{
		String defaultPlanCode = null;
		if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
			defaultPlanCode = WSDefaultInputsMap.getACHDefaultPlanCode(context);
		}
		else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
			defaultPlanCode = WSDefaultInputsMap.getCheckDefaultIRAPlanCode(context);
		}
		return defaultPlanCode;
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
	 * IRA Transaction logic .....
	 * @param acntDetails
	 * @return
	 * @throws Exception
	 */
	public static boolean getIRATxnFlag(MMAccount acntDetails) throws Exception
	{
		boolean iraAccFlag = false;
		try {
			//IRA Flag Mapping logic...
			if(acntDetails!=null){
				if(acntDetails.getDivPay()!=null && (acntDetails.getDivPay().equals("5") || acntDetails.getDivPay().equals("6"))){
					iraAccFlag = true;
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return iraAccFlag;
	}

	/**
	 * Gets the IRA Transfer type description for the View Details screens...
	 * @param objPaymentDetails
	 * @return
	 * @throws Exception
	 */
	public static String getRetirementType(PaymentDetailsTO objPaymentDetails) throws Exception
	{
		StringBuffer retriement_type = new StringBuffer();
		try 
		{
			String retirement_mnemonic_desc = objPaymentDetails.getRetirement_mnemonic_desc();
			if(retirement_mnemonic_desc!=null && !retirement_mnemonic_desc.equalsIgnoreCase("")){
				retriement_type.append(retirement_mnemonic_desc);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return retriement_type.toString();
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

			//Get the cut off time Query Statement Id depending on transfer type..
			if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
				cutOffTime = WSDefaultInputsMap.getIntTxnCutOffTime(context);
			}
			else if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
				cutOffTime = WSDefaultInputsMap.getACHTxnCutOffTime(context);
			}
			else if(transferType!=null && transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)){
				cutOffTime = WSDefaultInputsMap.getPLATxnCutOffTime(context);
			}
			else {
				if(transferType!=null && (transferType.equalsIgnoreCase(ChkReqConstants.CHK_LOC))){
					if(!objPaymentDetails.isCheckPrinted()){
						cutOffTime = WSDefaultInputsMap.getLocalCheckCutOffTime(context);
					}
					else{
						cutOffTime = WSDefaultInputsMap.getLocalCheckPrintCutOffTime(context);
					}
				}
				else if(transferType!=null && (transferType.equalsIgnoreCase(ChkReqConstants.CHK_REG))){
					if(!objPaymentDetails.isCheckPrinted()){
						cutOffTime = WSDefaultInputsMap.getRegCheckCutOffTime(context);
					}
					else{
						cutOffTime = WSDefaultInputsMap.getRegCheckPrintCutOffTime(context);
					}
				}
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
	public static ArrayList getHolidayListParams(String transferType,ServiceContext context) throws Exception
	{
		ArrayList holiday_list = new ArrayList();
		try 
		{
			//Get the Holiday check Query Statement Id depending on transfer type..
			if(transferType!=null && (transferType.startsWith(TxnTypeCode.INTERNAL) || transferType.startsWith(ChkReqConstants.CHK))){
				holiday_list = WSDefaultInputsMap.getInternalHolidayList(context);
			}
			else if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
				holiday_list = WSDefaultInputsMap.getACHHolidayList(context);
			}
			else if(transferType!=null && transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)){
				holiday_list = WSDefaultInputsMap.getACHHolidayList(context); //Includes both ACH and Internal Holidays..
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
	public static String getTxnSettlePeriod(String transferType,ServiceContext context) throws Exception
	{
		String txnSettleTime = "";
		try 
		{
			if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
				txnSettleTime = WSDefaultInputsMap.getACHTxnSettlePeriod(context);
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
	public static String getExpiryPeriodParams(String transferType,ServiceContext context) throws Exception
	{
		String experiyPeriod = "";
		try 
		{
			//Get the Transaction Expiry Query Statement Id depending on transfer type..
			if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
				experiyPeriod = WSDefaultInputsMap.getChkExpiryPeriod(context);
			}
			else if(transferType!=null && 
					(transferType.startsWith(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.ACH_TYPE) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				experiyPeriod = WSDefaultInputsMap.getTxnExpiryPeriod(context);
			}
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
	public static String getMaxFutureDaysParams(String transferType,ServiceContext context) throws Exception
	{
		String maxFutureDays = "";
		try 
		{
			//Get the Maximum Future days Query Statement Id depending on transfer type..
			if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
				maxFutureDays = WSDefaultInputsMap.getCheckTxnMaxFtrDts(context);
			}
			else if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
				maxFutureDays = WSDefaultInputsMap.getACHTxnMaxFtrDts(context);
			}
			else if(transferType!=null && (transferType.startsWith(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				maxFutureDays = WSDefaultInputsMap.getIntTxnMaxFtrDts(context);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return maxFutureDays;
	}

	/**
	 * Gets the Query String for the Maximum Past days for Verbal authorization..
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public static String getMaxPastVerbalAuthDays(String transferType,ServiceContext context) throws Exception
	{
		String maxPastDays = "";
		try 
		{
			//Get the Maximum Past days Query Statement 
			if(transferType!=null ){
				maxPastDays = WSDefaultInputsMap.getMaxPastVerbalAuthDays(context);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return maxPastDays;
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
	 * Gets the BR Pick up option..
	 * @param objPaymentDetails
	 * @param objCheckRequestDetails
	 * @return
	 */
	public static String getChkDeliveryOption(PaymentDetailsTO objPaymentDetails,CheckRequestTO objCheckRequestDetails)
	{
		Double pick_up_option = null;
		String pick_up_option_desc = null;
		String transfer_type = objPaymentDetails.getTransfer_Type();

		if(transfer_type!=null && transfer_type.startsWith(ChkReqConstants.CHK))
		{
			pick_up_option = objCheckRequestDetails.getPickUpOption();
			if(pick_up_option!=null && pick_up_option.equals(1D)){
				pick_up_option_desc = ChkReqConstants.PICK_UP_OPTION_1;
			}
			else if(pick_up_option!=null && pick_up_option.equals(2D)){
				pick_up_option_desc = ChkReqConstants.PICK_UP_OPTION_2;
			}
			else if(pick_up_option!=null && pick_up_option.equals(3D)){
				pick_up_option_desc = ChkReqConstants.PICK_UP_OPTION_3;
			}
			else if(pick_up_option!=null && pick_up_option.equals(4D)){
				pick_up_option_desc = ChkReqConstants.PICK_UP_OPTION_4;
			}
		}
		return pick_up_option_desc;
	}

	/**
	 * Truncates the account owner name to the maximum of 24 characters . 
	 * @param acctName
	 * @return
	 */
	public static String truncateAcctName(String acctName)
	{
		String formattedString = "";
		if(acctName!=null)
		{
			if(acctName.length()>24){
				formattedString = acctName.substring(0,24);
			}
			else {
				formattedString = acctName ;
			}
		}
		return formattedString;
	}

	/**
	 * Formats the date to be displayed on transaction View details screen...
	 * @param txnDetails
	 * @return
	 * @throws Exception 
	 */
	public static String getCheckPrintDateFormat(HashMap txnDetails) throws Exception
	{
		String txn_date = "" ;
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

			//Default setting the date to the requested execution date...
			txn_date = objPaymentDetails.getRequestedDate();

			//Formatting the date to the 12-hour format in case of only local check transactions...
			String transferType= objPaymentDetails.getTransfer_Type();
			if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)) 
			{
				String pickUpOption = ConvertionUtil.convertToString(objCheckRequest.getPickUpOption());
				if (pickUpOption!=null && (pickUpOption.equalsIgnoreCase(ChkReqConstants.PRINT_AT_MY_BRANCH) || pickUpOption.equalsIgnoreCase(ChkReqConstants.PRINT_AT_ANOTHER_BRANCH))){
					txn_date = txn_date+" "+MSCommonUtils.get12HourFormat(objCheckRequest.getEstPickupTime())+ChkReqConstants.ET;
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return txn_date;
	}

	/**
	 * Sets the default address passed in the HashMap...
	 * @param defaultAddress
	 * @return
	 * @throws Exception
	 */
	public static HashMap setClientDefaultAddress(Address defaultAddress) throws Exception
	{
		HashMap defaultAccAdd = new HashMap();
		String line1 = "";
		String line2 = "";
		String line3 = "";
		String city  = "";
		String state = "";
		String zip = "";
		String province = "";
		String foreign_status = "";
		String postal = "";

		try 
		{
			if(defaultAddress.getLine1()!=null ){
				line1 = defaultAddress.getLine1();
			}
			if(defaultAddress.getLine2()!=null ){
				line2 = defaultAddress.getLine2();
			}
			if(defaultAddress.getLine3()!=null ){
				line3 = defaultAddress.getLine3();
			}
			if(defaultAddress.getCity()!=null ){
				city = defaultAddress.getCity();
			}
			if(defaultAddress.getState()!=null ){
				state = defaultAddress.getState();
			}
			if(defaultAddress.getZip5()!=null ){
				zip = defaultAddress.getZip5();
			}
			if(defaultAddress.getProvince()!=null ){
				province = defaultAddress.getProvince();
			}
			if(defaultAddress.getPostal()!=null ){
				postal = defaultAddress.getPostal();
			}
			if(defaultAddress.getForeignStatus()!=null ){
				foreign_status = defaultAddress.getForeignStatus();
			}

			//Creating a Map...
			defaultAccAdd.put("Line1",line1);
			defaultAccAdd.put("Line2",line2);
			defaultAccAdd.put("Line3",line3);
			defaultAccAdd.put("City",city);
			defaultAccAdd.put("State",state);
			defaultAccAdd.put("Zip",zip);
			defaultAccAdd.put("Province",province);
			defaultAccAdd.put("Postal",postal);
			defaultAccAdd.put("ForeignStatus",foreign_status);
		} 
		catch (Exception exception) {
			throw exception;
		}

		return defaultAccAdd;
	}

	/**
	 * Setting the account plating address as received from the account view web service...
	 * @return
	 * @throws Exception 
	 */
	public static ArrayList setAcntPlatingAddress(ArrayList acntPlatingAddressList) throws Exception
	{
		ArrayList acntPlatingAddrList = new ArrayList();
		try 
		{
			for(int j=0;j<acntPlatingAddressList.size();j++)
			{
				PlatingAddress acntPlatingAddress = (PlatingAddress)acntPlatingAddressList.get(j);
				if(acntPlatingAddress !=null) 
				{
					String category = acntPlatingAddress.getAddressCategory();
					if(category!=null && category.equalsIgnoreCase(MSSystemDefaults.ACNT_VIEW_PLATING_ADRSS_CATEGORY)){
						HashMap acntPlatingAddr = new HashMap();
						acntPlatingAddr.put("AddressLineIndex", acntPlatingAddress.getAddresslineindex());
						acntPlatingAddr.put("AddressLine", acntPlatingAddress.getAddressline());
						acntPlatingAddrList.add(acntPlatingAddr);
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return acntPlatingAddrList;
	}

	/**
	 * Sets the Client Infor into HashMap...
	 * @param defaultAddress
	 * @return
	 * @throws Exception
	 */
	public static HashMap setClientDetails(ArrayList clientDtlsList) throws Exception
	{
		HashMap clientInfo = new HashMap();
		try 
		{
			for(int j=0;j<clientDtlsList.size();j++)
			{
				AccountOwner acntOwner = (AccountOwner)clientDtlsList.get(j);
				if(acntOwner !=null) 
				{
					String relationShip = acntOwner.getRelationship();
					if(relationShip!=null && relationShip.equalsIgnoreCase(MSSystemDefaults.ACNT_VIEW_CLIENT_RELATIONSHIP)){
						clientInfo.put("ClientRelationship", acntOwner.getRelationship());
						clientInfo.put("ClientResidentCountry", acntOwner.getResidentCountry());
						break;
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return clientInfo;
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
	 * Formats Zip code to 5 digit US Standard number...
	 * @param zipCode
	 * @return
	 */
	public static String formatZipCode(String zipCode)
	{
		//Formatting Zip Code...
		if(zipCode!=null) 
		{
			do{
				if(zipCode.length()>=5){
					break;
				}
				else {
					zipCode="0"+zipCode;
				}
			} while(zipCode.length()!=5);
		}
		return zipCode;
	}

	/**
	 * Evaluate Next transaction date based on 24x7 access and Recurring frequency(FBD & LBD) ...
	 * @param repeatValue
	 * @param prefPrevBusinessDate
	 * @param parentTxnDate
	 * @param transferType
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Date evaluateNextTxnDate(String repeatValue,Date prefPrevBusinessDate,Date parentTxnDate,String transferType,ServiceContext serviceContext) throws Exception 
	{
		Date nextBusinessDate = new Date();
		try {
			if(MSCommonUtils.check24x7Access(transferType) && !(repeatValue.equalsIgnoreCase("FBD") || repeatValue.equalsIgnoreCase("LBD"))) {
				nextBusinessDate = MSCommonUtils.calculatePrefNextTxnDate(repeatValue,prefPrevBusinessDate,parentTxnDate,transferType);
			}
			else {
				nextBusinessDate = MSCommonUtils.calculateNextBusinessDate(repeatValue,prefPrevBusinessDate,parentTxnDate,transferType,serviceContext);
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
	public static Date calculateNextBusinessDate(String repeatValue,Date prefPrevBusinessDate,Date parentTxnDate,String transferType,ServiceContext serviceContext) throws Exception 
	{
		Date nextBusinessDate = new Date();
		try {
			Boolean isDirectDBCall = true;
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
			nextBusinessDate = objdateFunctions.getPrefNextTxnDate(repeatValue,prefPrevBusinessDate,parentTxnDate,transferType);
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
	public static Date calculateEstArrivalDate(Date prefPrevBusinessDate,String transferType,ServiceContext serviceContext) throws Exception
	{
		Date nextEstArrDate = new Date();
		try {
			DateFunctions objdateFunctions = new DateFunctions();
			boolean isDirectDBCall = true;
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
	public static Date calculateTxnExpiryDate(Date prefPrevBusinessDate,String transferType,ServiceContext serviceContext) throws Exception 
	{
		Date txnExpDate = new Date();
		try {
			DateFunctions objdateFunctions = new DateFunctions();
			boolean isDirectDBCall = true;
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
	 * @param payInitDateVal
	 * @param transferType
	 * @return
	 * @throws Exception
	 */
	public static Date calculateStartBusinessDate(String repeatValue,Date payInitDateVal,String transferType) throws Exception 
	{
		Date startBusinessDate = new Date();
		try {
			Boolean isDirectDBCall = false;
			ServiceContext serviceContext = new ServiceContext();
			DateFunctions objdateFunctions = new DateFunctions();
			startBusinessDate=objdateFunctions.getNextFirstOrLastTxnDate(repeatValue,payInitDateVal,transferType,isDirectDBCall,serviceContext);
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

