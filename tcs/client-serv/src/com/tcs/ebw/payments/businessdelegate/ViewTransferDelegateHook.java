/*
 * Created on Fri Jun 05 14:13:50 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;

import com.tcs.Payments.ms360Utils.FormatAccount;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.integration.MMContext;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.businessdelegate.BusinessDelegateHook;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;

import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.GetViewInputDetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
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
public class ViewTransferDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for ViewTransfer Screen, ViewTransfer_INIT State and INIT Event. 
	 * @throws Exception 
	 */
	public void preViewTransferViewTransfer_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preViewTransferViewTransfer_INITINIT"); 
		try 
		{
			//Initialization code...
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();

			//Context sharing for the Input Mappings 
			MMContext objMMContextData = objUserPrincipal.getContextData();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);

			//Updating the input TO...
			GetViewInputDetailsTO objGetViewInputDetailsTO = (GetViewInputDetailsTO)objParam[0];
			objGetViewInputDetailsTO.setLifeUserId(objMSUserDetails.getUuid());
		}
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preViewTransferViewTransfer_INITINIT"); 
	}

	public void postViewTransferViewTransfer_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postViewTransferViewTransfer_INITINIT");
		try 
		{
			ViewTransferForm objViewTransfer=(ViewTransferForm) objSourceForm;
			com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO getViewOutputDetailsTO = (com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO)objReturn;
			HashMap txnDetails = new HashMap();

			//Required Payment Details Mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setFrequency_Type("1");
			objPaymentDetails.setTransfer_Type(ConvertionUtil.convertToString(getViewOutputDetailsTO.getTransferType()));

			//From account mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(getViewOutputDetailsTO.getFrombr_acct_no_fa()!=null){
				objFromMSAcc_Details.setAccountNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(3,9));
				objFromMSAcc_Details.setOfficeNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(0,3));
				objFromMSAcc_Details.setFaNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(9));
				objFromMSAcc_Details.setNickName(getViewOutputDetailsTO.getFrmAccNickName());
				objFromMSAcc_Details.setFriendlyName(getViewOutputDetailsTO.getFrmAccFriendlyName());
			}

			//To account mappings..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(getViewOutputDetailsTO.getTobr_acct_no_fa()!=null){
				objToMSAcc_Details.setAccountNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(3,9));
				objToMSAcc_Details.setOfficeNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(0,3));
				objToMSAcc_Details.setFaNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(9));
				objToMSAcc_Details.setNickName(getViewOutputDetailsTO.getToAccNickName());
				objToMSAcc_Details.setFriendlyName(getViewOutputDetailsTO.getToAccFriendlyName());
			}

			//External account mappings..
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO = new DsExtPayeeDetailsOutTO();
			objDsExtPayeeDetailsOutTO.setNick_name(getViewOutputDetailsTO.getPaypayeename1());
			objDsExtPayeeDetailsOutTO.setCpyaccnum(getViewOutputDetailsTO.getPayeeaccnum());

			//Loan Account mappings..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			objLoanAcntDetails.setLoanAccount(getViewOutputDetailsTO.getLoanAcnt());

			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);
			txnDetails.put("LoanAccountDetails",objLoanAcntDetails);

			//Setting the Frequency Value.....
			objViewTransfer.setFrequencyValue("1");
			objViewTransfer.setViewTransFrequency(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));

			//From & To Account mapping details..
			objViewTransfer.setViewTransFromAcc(FormatAccount.getDebitAccountDisp(txnDetails));
			objViewTransfer.setViewTransToAcc(FormatAccount.getCreditAccountDisp(txnDetails));

			EBWLogger.trace(this, "Finished postViewTransferViewTransfer_INITINIT");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}

	/**
	 * Method for ViewTransfer Screen, ViewTransfer_Recurring State and INIT Event. 
	 * @throws Exception 
	 */
	public void preViewTransferViewTransfer_RecurringINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preViewTransferViewTransfer_RecurringINIT"); 
		try
		{
			//Initialization code...
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();

			//Context sharing for the Input Mappings 
			MMContext objMMContextData = objUserPrincipal.getContextData();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);

			//Updating the input TO...
			GetViewInputDetailsTO objGetViewInputDetailsTO = (GetViewInputDetailsTO)objParam[0];
			objGetViewInputDetailsTO.setLifeUserId(objMSUserDetails.getUuid());
		} 
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preViewTransferViewTransfer_RecurringINIT"); 
	}

	public void postViewTransferViewTransfer_RecurringINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postViewTransferViewTransfer_RecurringINIT");
		try 
		{
			ViewTransferForm objViewTransfer=(ViewTransferForm) objSourceForm;
			com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO getViewOutputDetailsTO = (com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO)objReturn;
			HashMap txnDetails = new HashMap();

			//Required Payment Details Mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setFrequency_Type("2");
			objPaymentDetails.setDuration_AmountLimit(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPayfreqlimit()));
			objPaymentDetails.setDuration_EndDate(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPayfreqenddate()));
			objPaymentDetails.setDuration_NoOfTransfers(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPayfreqpaymentcount()));
			objPaymentDetails.setFrequency_DurationValue(ConvertionUtil.convertToString(getViewOutputDetailsTO.getDurationValue()));
			objPaymentDetails.setFrequency_DurationDesc(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPayfrequency()));
			objPaymentDetails.setTransfer_Type(ConvertionUtil.convertToString(getViewOutputDetailsTO.getTransferType()));

			//From account mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(getViewOutputDetailsTO.getFrombr_acct_no_fa()!=null){
				objFromMSAcc_Details.setAccountNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(3,9));
				objFromMSAcc_Details.setOfficeNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(0,3));
				objFromMSAcc_Details.setFaNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(9));
				objFromMSAcc_Details.setNickName(getViewOutputDetailsTO.getFrmAccNickName());
				objFromMSAcc_Details.setFriendlyName(getViewOutputDetailsTO.getFrmAccFriendlyName());
			}

			//To account mappings..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(getViewOutputDetailsTO.getTobr_acct_no_fa()!=null){
				objToMSAcc_Details.setAccountNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(3,9));
				objToMSAcc_Details.setOfficeNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(0,3));
				objToMSAcc_Details.setFaNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(9));
				objToMSAcc_Details.setNickName(getViewOutputDetailsTO.getToAccNickName());
				objToMSAcc_Details.setFriendlyName(getViewOutputDetailsTO.getToAccFriendlyName());
			}

			//External account mappings..
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO = new DsExtPayeeDetailsOutTO();
			objDsExtPayeeDetailsOutTO.setNick_name(getViewOutputDetailsTO.getPaypayeename1());
			objDsExtPayeeDetailsOutTO.setCpyaccnum(getViewOutputDetailsTO.getPayeeaccnum());

			//Loan Account mappings..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			objLoanAcntDetails.setLoanAccount(getViewOutputDetailsTO.getLoanAcnt());

			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);
			txnDetails.put("LoanAccountDetails",objLoanAcntDetails);

			//Setting the Frequency Value.....
			objViewTransfer.setFrequencyValue("2");
			objViewTransfer.setViewTransFrequency(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));
			objViewTransfer.setViewTransRepeat(MSCommonUtils.getRepeatDesc_View(objPaymentDetails));

			//From & To Account mapping details..
			objViewTransfer.setViewTransFromAcc(FormatAccount.getDebitAccountDisp(txnDetails));
			objViewTransfer.setViewTransToAcc(FormatAccount.getCreditAccountDisp(txnDetails));

			//Setting the LastTransferDate...
			String lastTransferDate = ConvertionUtil.convertToString(getViewOutputDetailsTO.getLastTransactionDate());
			if(lastTransferDate!=null && lastTransferDate.indexOf("1970")==-1)
				objViewTransfer.setViewTransLastTransDate(ConvertionUtil.convertToString(getViewOutputDetailsTO.getLastTransactionDate()));

			EBWLogger.trace(this, "Finished postViewTransferViewTransfer_RecurringINIT");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}

	/**
	 * Method for ViewTransfer Screen, ViewTransfer_OneTimeHistory State and INIT Event. 
	 * @throws Exception 
	 */
	public void preViewTransferViewTransfer_OneTimeHistoryINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preViewTransferViewTransfer_OneTimeHistoryINIT"); 
		try 
		{
			//Initialization code...
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();

			//Context sharing for the Input Mappings 
			MMContext objMMContextData = objUserPrincipal.getContextData();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);

			//Updating the input TO...
			GetViewInputDetailsTO objGetViewInputDetailsTO = (GetViewInputDetailsTO)objParam[0];
			objGetViewInputDetailsTO.setLifeUserId(objMSUserDetails.getUuid());
		} 
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preViewTransferViewTransfer_OneTimeHistoryINIT"); 
	}

	public void postViewTransferViewTransfer_OneTimeHistoryINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postViewTransferViewTransfer_OneTimeHistoryINIT");
		try 
		{
			ViewTransferForm objViewTransfer=(ViewTransferForm) objSourceForm;
			com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO getViewOutputDetailsTO = (com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO)objReturn;
			HashMap txnDetails = new HashMap();

			//Required Payment Details Mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setFrequency_Type("1");
			objPaymentDetails.setTransfer_Type(ConvertionUtil.convertToString(getViewOutputDetailsTO.getTransferType()));

			//From account mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(getViewOutputDetailsTO.getFrombr_acct_no_fa()!=null){
				objFromMSAcc_Details.setAccountNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(3,9));
				objFromMSAcc_Details.setOfficeNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(0,3));
				objFromMSAcc_Details.setFaNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(9));
				objFromMSAcc_Details.setNickName(getViewOutputDetailsTO.getFrmAccNickName());
				objFromMSAcc_Details.setFriendlyName(getViewOutputDetailsTO.getFrmAccFriendlyName());
			}

			//To account mappings..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(getViewOutputDetailsTO.getTobr_acct_no_fa()!=null){
				objToMSAcc_Details.setAccountNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(3,9));
				objToMSAcc_Details.setOfficeNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(0,3));
				objToMSAcc_Details.setFaNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(9));
				objToMSAcc_Details.setNickName(getViewOutputDetailsTO.getToAccNickName());
				objToMSAcc_Details.setFriendlyName(getViewOutputDetailsTO.getToAccFriendlyName());
			}

			//External account mappings..
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO = new DsExtPayeeDetailsOutTO();
			objDsExtPayeeDetailsOutTO.setNick_name(getViewOutputDetailsTO.getPaypayeename1());
			objDsExtPayeeDetailsOutTO.setCpyaccnum(getViewOutputDetailsTO.getPayeeaccnum());

			//Loan Account mappings..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			objLoanAcntDetails.setLoanAccount(getViewOutputDetailsTO.getLoanAcnt());

			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);
			txnDetails.put("LoanAccountDetails",objLoanAcntDetails);

			//Setting the Frequency Value.....
			objViewTransfer.setFrequencyValue("1");
			objViewTransfer.setViewTransFrequency(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));

			//From & To Account mapping details..
			objViewTransfer.setViewTransFromAcc(FormatAccount.getDebitAccountDisp(txnDetails));
			objViewTransfer.setViewTransToAcc(FormatAccount.getCreditAccountDisp(txnDetails));

			EBWLogger.trace(this, "Finished postViewTransferViewTransfer_OneTimeHistoryINIT");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}

	/**
	 * Method for ViewTransfer Screen, ViewTransfer_RecurringHistory State and INIT Event. 
	 * @throws Exception 
	 */
	public void preViewTransferViewTransfer_RecurringHistoryINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preViewTransferViewTransfer_RecurringHistoryINIT"); 
		try 
		{
			//Initialization code...
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();

			//Context sharing for the Input Mappings 
			MMContext objMMContextData = objUserPrincipal.getContextData();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);

			//Updating the input TO...
			GetViewInputDetailsTO objGetViewInputDetailsTO = (GetViewInputDetailsTO)objParam[0];
			objGetViewInputDetailsTO.setLifeUserId(objMSUserDetails.getUuid());
		} 
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preViewTransferViewTransfer_RecurringHistoryINIT"); 
	}

	public void postViewTransferViewTransfer_RecurringHistoryINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postViewTransferViewTransfer_RecurringHistoryINIT");
		try 
		{
			ViewTransferForm objViewTransfer=(ViewTransferForm) objSourceForm;
			com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO getViewOutputDetailsTO = (com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO)objReturn;
			HashMap txnDetails = new HashMap();

			//Required Payment Details Mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setFrequency_Type("2");
			objPaymentDetails.setDuration_AmountLimit(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPayfreqlimit()));
			objPaymentDetails.setDuration_EndDate(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPayfreqenddate()));
			objPaymentDetails.setDuration_NoOfTransfers(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPayfreqpaymentcount()));
			objPaymentDetails.setFrequency_DurationValue(ConvertionUtil.convertToString(getViewOutputDetailsTO.getDurationValue()));
			objPaymentDetails.setFrequency_DurationDesc(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPayfrequency()));
			objPaymentDetails.setTransfer_Type(ConvertionUtil.convertToString(getViewOutputDetailsTO.getTransferType()));

			//From account mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(getViewOutputDetailsTO.getFrombr_acct_no_fa()!=null){
				objFromMSAcc_Details.setAccountNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(3,9));
				objFromMSAcc_Details.setOfficeNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(0,3));
				objFromMSAcc_Details.setFaNumber(getViewOutputDetailsTO.getFrombr_acct_no_fa().substring(9));
				objFromMSAcc_Details.setNickName(getViewOutputDetailsTO.getFrmAccNickName());
				objFromMSAcc_Details.setFriendlyName(getViewOutputDetailsTO.getFrmAccFriendlyName());
			}

			//To account mappings..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(getViewOutputDetailsTO.getTobr_acct_no_fa()!=null){
				objToMSAcc_Details.setAccountNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(3,9));
				objToMSAcc_Details.setOfficeNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(0,3));
				objToMSAcc_Details.setFaNumber(getViewOutputDetailsTO.getTobr_acct_no_fa().substring(9));
				objToMSAcc_Details.setNickName(getViewOutputDetailsTO.getToAccNickName());
				objToMSAcc_Details.setFriendlyName(getViewOutputDetailsTO.getToAccFriendlyName());
			}

			//External account mappings..
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO = new DsExtPayeeDetailsOutTO();
			objDsExtPayeeDetailsOutTO.setNick_name(getViewOutputDetailsTO.getPaypayeename1());
			objDsExtPayeeDetailsOutTO.setCpyaccnum(getViewOutputDetailsTO.getPayeeaccnum());

			//Loan Account mappings..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			objLoanAcntDetails.setLoanAccount(getViewOutputDetailsTO.getLoanAcnt());

			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);
			txnDetails.put("LoanAccountDetails",objLoanAcntDetails);

			//Setting the Frequency Value.....
			objViewTransfer.setFrequencyValue("2");
			objViewTransfer.setViewTransFrequency(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));
			objViewTransfer.setViewTransRepeat(MSCommonUtils.getRepeatDesc_View(objPaymentDetails));

			//From & To Account mapping details..
			objViewTransfer.setViewTransFromAcc(FormatAccount.getDebitAccountDisp(txnDetails));
			objViewTransfer.setViewTransToAcc(FormatAccount.getCreditAccountDisp(txnDetails));

			//Setting the LastTransferDate...
			String lastTransferDate = ConvertionUtil.convertToString(getViewOutputDetailsTO.getLastTransactionDate());
			if(lastTransferDate!=null && lastTransferDate.indexOf("1970")==-1)
				objViewTransfer.setViewTransLastTransDate(ConvertionUtil.convertToString(getViewOutputDetailsTO.getLastTransactionDate()));

		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postViewTransferViewTransfer_RecurringHistoryINIT"); 
	}
}