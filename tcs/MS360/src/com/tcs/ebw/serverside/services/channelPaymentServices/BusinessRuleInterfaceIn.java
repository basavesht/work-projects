package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.BUS_RULE_INP_CRTL;
import com.tcs.Payments.EAITO.BUS_RUL_TXN_INP;
import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_DTL;
import com.tcs.Payments.EAITO.MS_RTAB_ACNT_DTLS;
import com.tcs.Payments.ms360Utils.BRUtils;
import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 *    224703       23-09-2011        P3-B            PLA  
 * **********************************************************
 */
public class BusinessRuleInterfaceIn {

	public BusinessRuleInterfaceIn(){

	}

	/**
	 * Business Rules for block display case ..
	 * @param txnDetails
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Vector getBRInterfaceBlockDispIn(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.logDebug("BusinessRuleInterfaceIn", "Setting the Business Rule input for the Blocked Display Case...");
		Vector brBlockedDisplayInVector = new Vector();
		try 
		{
			ArrayList blockedAccInputDetails =new ArrayList();
			if(txnDetails.containsKey("BlockedDisplayInput")){
				blockedAccInputDetails = (ArrayList)txnDetails.get("BlockedDisplayInput");
			}

			for( int a=0;a<blockedAccInputDetails.size();a++)
			{
				if(blockedAccInputDetails.get(a) instanceof BUS_RULE_INP_CRTL){
					brBlockedDisplayInVector.add(blockedAccInputDetails.get(a));
				}
				if(blockedAccInputDetails.get(a) instanceof BUS_RUL_TXN_INP){
					brBlockedDisplayInVector.add(blockedAccInputDetails.get(a));
				}
				if(blockedAccInputDetails.get(a) instanceof MS_ACCOUNT_OUT_DTL){
					brBlockedDisplayInVector.add(blockedAccInputDetails.get(a));
				}
			}
		} 
		catch(Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
		EBWLogger.logDebug("BusinessRuleInterfaceIn", "Input parameters for the BR Blocked Display Case is "+brBlockedDisplayInVector.toString());
		return brBlockedDisplayInVector;
	}

	/**
	 * Business Rule for Transaction case
	 * @param txnDetails
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Vector setBRInterfaceTransactionRuleIn(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.logDebug("BusinessRuleInterfaceIn", "Setting the Business Rule input for the transaction case...");
		Vector brTransactionInputVector=new Vector();
		try 
		{
			ArrayList responseRTAB = new ArrayList();
			ArrayList responseMerlin = new ArrayList();

			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//User Details...
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//RTAB Output Details for the From account involved ...
			AccountBalance objAccBalance = new AccountBalance();
			if(txnDetails.containsKey("RTABOutputDetails")){
				objAccBalance = (AccountBalance)txnDetails.get("RTABOutputDetails");
				responseRTAB = (ArrayList)objAccBalance.getInfoRTABObject();
			}

			//Account View Output Details...
			AccountDetails objAccDetails= new AccountDetails();
			if(txnDetails.containsKey("MerlinOutputDetails")){
				objAccDetails= (AccountDetails)txnDetails.get("MerlinOutputDetails");
				responseMerlin=(ArrayList)objAccDetails.getInfoMerlinObject();
			}

			//External account details...
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

			String transfer_Type = objPaymentDetails.getTransfer_Type();
			Date transferDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
			Date businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());

			//Business Rule Input Control Mappings ...
			BUS_RULE_INP_CRTL objBUS_RULE_INP_CRTL = new BUS_RULE_INP_CRTL();
			objBUS_RULE_INP_CRTL.setUSER_ID(objMSUserDetails.getRcafId());
			objBUS_RULE_INP_CRTL.setTXN_USER_ID(objPaymentDetails.getCurrent_owner_id());
			objBUS_RULE_INP_CRTL.setGROUP(MSCommonUtils.getInitiatorRoleDesc(objPaymentDetails.getCurrent_owner_role()));
			objBUS_RULE_INP_CRTL.setROLE(MSCommonUtils.getInitiatorRoleDesc(objPaymentDetails.getCurrent_owner_role()));
			objBUS_RULE_INP_CRTL.setRSA_REQUEST_FLAG("N"); //No fraud check will be done for the transactions created in MS360.
			objBUS_RULE_INP_CRTL.setRSA_REVIEW_FLAG(MSCommonUtils.getRSAReviewFlag(objPaymentDetails)); 
			objBUS_RULE_INP_CRTL.setLIMIT_CHECK_REQ("N");
			objBUS_RULE_INP_CRTL.setAPPL_ID(WSDefaultInputsMap.getBusRuleCntrlAppID(serviceContext));
			objBUS_RULE_INP_CRTL.setSERVER_IP(WSDefaultInputsMap.getBusRuleCntrlServerID(serviceContext));

			//Business Rule Input Transaction Mappings ...
			BUS_RUL_TXN_INP objBUS_RUL_TXN_INP = new BUS_RUL_TXN_INP();
			objBUS_RUL_TXN_INP.setCONFIRM_NUM(objPaymentDetails.getTransactionId());
			objBUS_RUL_TXN_INP.setTYPE(BRUtils.getBRTxnTypeValue(objPaymentDetails.getTransfer_Type()));
			objBUS_RUL_TXN_INP.setACTION(objPaymentDetails.getEventDesc());
			objBUS_RUL_TXN_INP.setRULE_TYPE(Bus_Rule_Input_Desc.RULE_TYPE_TXN); 
			objBUS_RUL_TXN_INP.setAMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			objBUS_RUL_TXN_INP.setEXE_DATE(transferDate);
			objBUS_RUL_TXN_INP.setBUS_DATE(businessDate);
			objBUS_RUL_TXN_INP.setCURRENT_APPRVER(BRUtils.getCurrentApprover(objPaymentDetails));
			objBUS_RUL_TXN_INP.setAUTH_MODE(objPaymentDetails.getAuth_mode());
			objBUS_RUL_TXN_INP.setMS360_SAME_NAME(BRUtils.getSameNameFlag(txnDetails));
			objBUS_RUL_TXN_INP.setUTILIZED_AMNT(BRUtils.getUtitlizedAmount(txnDetails,serviceContext));
			objBUS_RUL_TXN_INP.setPAGE_TYPE(BRUtils.getBRTxn_PageType(objPaymentDetails));
			objBUS_RUL_TXN_INP.setTXN_SUB_TYPE(BRUtils.getBRTxnSub_Type(objPaymentDetails));
			objBUS_RUL_TXN_INP.setFREQUENCY(BRUtils.getBRFrequency_type(objPaymentDetails));
			objBUS_RUL_TXN_INP.setFEE(objPaymentDetails.getTxn_Fee_Charge());
			objBUS_RUL_TXN_INP.setIRA_TXN_IND(objPaymentDetails.getIraTxnFlag());
			objBUS_RUL_TXN_INP.setUN_EDITED_AMT(BRUtils.getUnEditedAmount(objPaymentDetails));
			objBUS_RUL_TXN_INP.setREPEAT(BRUtils.getBRRepeatDesc(objPaymentDetails));
			objBUS_RUL_TXN_INP.setUNTIL_END_DATE(BRUtils.formatDurationEndDt(objPaymentDetails));
			objBUS_RUL_TXN_INP.setUNTIL_DOLLAR_AMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
			objBUS_RUL_TXN_INP.setUNTIL_NO_OF_TXNS(MSCommonUtils.convertToInteger(objPaymentDetails.getDuration_NoOfTransfers()));
			objBUS_RUL_TXN_INP.setTRANSFERS_DONE_TILL_DT(MSCommonUtils.convertToInteger(objPaymentDetails.getAccumulated_transfers()));
			objBUS_RUL_TXN_INP.setAMT_TRNSFRD_TILL_DT(objPaymentDetails.getAccumulated_amount());
			objBUS_RUL_TXN_INP.setIRA_OPTION(objPaymentDetails.getRetirement_mnemonic());
			objBUS_RUL_TXN_INP.setUN_EDITED_FEE(BRUtils.getUnEditedFee(objPaymentDetails));
			objBUS_RUL_TXN_INP.setUN_EDITED_FEE_CHARGED_TO(BRUtils.getUnEditedFeeChargedTo(txnDetails));
			objBUS_RUL_TXN_INP.setQUALIFIER(objPaymentDetails.getQualifier());
			objBUS_RUL_TXN_INP.setREVERSE_QUALIFIER(objPaymentDetails.getReverse_qualifier());
			objBUS_RUL_TXN_INP.setSPOKE_TO_TYPE(BRUtils.getSpokeToType(txnDetails));
			objBUS_RUL_TXN_INP.setSPOKE_TO_NAME(BRUtils.getSpokeToName(txnDetails));

			//Check transaction details....
			if(transfer_Type!=null && transfer_Type.startsWith(ChkReqConstants.CHK)){
				objBUS_RUL_TXN_INP.setCHARGE_TO(BRUtils.getBRFeeChargedTo(objPaymentDetails,objCheckRequest));
				objBUS_RUL_TXN_INP.setCERTIFIED(BRUtils.getBRCheckCertifiedFlg(objPaymentDetails, objCheckRequest));
				objBUS_RUL_TXN_INP.setCHECK_PICKUP_ADDR_STAT(BRUtils.getBRForeignAddressFlg(objPaymentDetails, objCheckRequest));
				objBUS_RUL_TXN_INP.setDFLT_CHK_PCKP_ADDR(BRUtils.getBRDefaultAddressFlg(objPaymentDetails, objCheckRequest));
				objBUS_RUL_TXN_INP.setPICKUP_OPTION(BRUtils.getBRPickUpOption(objPaymentDetails, objCheckRequest));
				objBUS_RUL_TXN_INP.setPRINT_AT_BRANCH(objCheckRequest.getPrintingBranch());
				objBUS_RUL_TXN_INP.setPAYEE_NAME(objCheckRequest.getPayeeName());
				objBUS_RUL_TXN_INP.setTIRD_PARTY_REASON(MSCommonUtils.getThirdPartyReason(objCheckRequest));
				objBUS_RUL_TXN_INP.setEST_PICKUP_TIME(MSCommonUtils.get12HourFormat(objCheckRequest.getEstPickupTime()));
				objBUS_RUL_TXN_INP.setPRINT_ADDR_ON_CHECK(objCheckRequest.getPrintAddressFlag());
				objBUS_RUL_TXN_INP.setADDR_LINE1(objCheckRequest.getDeliveryAddrLine1());
				objBUS_RUL_TXN_INP.setADDR_LINE2(objCheckRequest.getDeliveryAddrLine2());
				objBUS_RUL_TXN_INP.setADDR_LINE3(objCheckRequest.getDeliveryAddrLine3());
				objBUS_RUL_TXN_INP.setADDR_LINE4(objCheckRequest.getDeliveryAddrLine4());
				objBUS_RUL_TXN_INP.setMEMO_LINE1(objCheckRequest.getMemoLine1());
				objBUS_RUL_TXN_INP.setMEMO_LINE2(objCheckRequest.getMemoLine2());
				objBUS_RUL_TXN_INP.setPRINT_MEMO_ON_CHECK(BRUtils.getBRPrintMemoOnCheck(objCheckRequest));
				objBUS_RUL_TXN_INP.setPRINT_MEMO_ON_STUB(BRUtils.getBRPrintMemoOnStub(objCheckRequest));
				objBUS_RUL_TXN_INP.setPAYEE_TYPE(objCheckRequest.getPayeeType().toString());
			}
			if(transferDate.after(businessDate)){
				objBUS_RUL_TXN_INP.setSTATE(Bus_Rule_Input_Desc.STATE_FUTURE);
			}
			else{
				objBUS_RUL_TXN_INP.setSTATE(Bus_Rule_Input_Desc.STATE_CURRENT);
			}

			//Business Rule Input Transaction Mapping for the Limit Check Call only for ACH-OUT transactions..
			setLimitParameters(txnDetails,objBUS_RUL_TXN_INP,objBUS_RULE_INP_CRTL);

			//Mappings the BUS_RULE_TXN_INP attributes from the AccountView Response...
			setExternalSrvcResponseDetails(objBUS_RUL_TXN_INP,txnDetails);

			//Mapping MERLIN OUT (Account view) Details...
			MS_ACCOUNT_OUT_DTL objFromMs_account_out_dtl = new MS_ACCOUNT_OUT_DTL();
			MS_ACCOUNT_OUT_DTL objToMs_account_out_dtl = new MS_ACCOUNT_OUT_DTL();
			for(int i=0;i<responseMerlin.size();i++)
			{
				if(responseMerlin.get(i) instanceof MS_ACCOUNT_OUT_DTL)
				{
					MS_ACCOUNT_OUT_DTL objMS_ACCOUNT_OUT_DTL =(MS_ACCOUNT_OUT_DTL)responseMerlin.get(i);
					if(transfer_Type.equalsIgnoreCase(TxnTypeCode.INTERNAL))
					{
						if(!responseRTAB.isEmpty())
						{
							MS_RTAB_ACNT_DTLS objMS_RTAB_ACNT_DTLS =(MS_RTAB_ACNT_DTLS)responseRTAB.get(1); // Contains the RTAB Account details ... 
							if(objMS_ACCOUNT_OUT_DTL.getACCOUNT_NO()!=null)
							{
								if(getMSAccFormat(objMS_ACCOUNT_OUT_DTL.getACCOUNT_NO()).equalsIgnoreCase(getMSAccFormat(objMS_RTAB_ACNT_DTLS.getACCOUNT())))
								{
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_SPNDG_PWR(BRUtils.getAvailableSpndPower(objMS_RTAB_ACNT_DTLS, txnDetails));
									objMS_ACCOUNT_OUT_DTL.setSPNDG_PWR_FROM_RTAB(objMS_RTAB_ACNT_DTLS.getAVAILABLE_SPENDING_POWER());
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_CASH_BALNCE(objMS_RTAB_ACNT_DTLS.getCASH_AVAILABLE());
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_MARGIN_BALNCE(objMS_RTAB_ACNT_DTLS.getMARGIN_AVAILABLE());
									objMS_ACCOUNT_OUT_DTL.setCASH_PENDNG_DEBIT(objMS_RTAB_ACNT_DTLS.getCASH_AVAILABLE_PENDING_TXN());
									objMS_ACCOUNT_OUT_DTL.setMARGIN_PENDNG_DEBIT(objMS_RTAB_ACNT_DTLS.getMARGIN_PENDING_TXN());
									objMS_ACCOUNT_OUT_DTL.setDR_CR_IND("1"); // DR_CR indicator for the FROM Account .
									objMS_ACCOUNT_OUT_DTL.setRESIDES(Bus_Rule_Input_Desc.RESIDES_IN);
									objFromMs_account_out_dtl = objMS_ACCOUNT_OUT_DTL;
								}
								else {
									objMS_ACCOUNT_OUT_DTL.setDR_CR_IND("2"); // DR_CR indicator for the To Account .
									objMS_ACCOUNT_OUT_DTL.setRESIDES(Bus_Rule_Input_Desc.RESIDES_IN);
									objToMs_account_out_dtl = objMS_ACCOUNT_OUT_DTL;
								}
							}
						}
					}
					else if(transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
					{
						if(!responseRTAB.isEmpty())
						{
							MS_RTAB_ACNT_DTLS objMS_RTAB_ACNT_DTLS =(MS_RTAB_ACNT_DTLS)responseRTAB.get(1); // Contains the RTAB Account details ... 
							if(objMS_ACCOUNT_OUT_DTL.getACCOUNT_NO()!=null)
							{
								if(getMSAccFormat(objMS_ACCOUNT_OUT_DTL.getACCOUNT_NO()).equalsIgnoreCase(getMSAccFormat(objMS_RTAB_ACNT_DTLS.getACCOUNT())))
								{
									//From account mappings..
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_SPNDG_PWR(BRUtils.getAvailableSpndPower(objMS_RTAB_ACNT_DTLS, txnDetails));
									objMS_ACCOUNT_OUT_DTL.setSPNDG_PWR_FROM_RTAB(objMS_RTAB_ACNT_DTLS.getAVAILABLE_SPENDING_POWER());
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_CASH_BALNCE(objMS_RTAB_ACNT_DTLS.getCASH_AVAILABLE());
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_MARGIN_BALNCE(objMS_RTAB_ACNT_DTLS.getMARGIN_AVAILABLE());
									objMS_ACCOUNT_OUT_DTL.setCASH_PENDNG_DEBIT(objMS_RTAB_ACNT_DTLS.getCASH_AVAILABLE_PENDING_TXN());
									objMS_ACCOUNT_OUT_DTL.setMARGIN_PENDNG_DEBIT(objMS_RTAB_ACNT_DTLS.getMARGIN_PENDING_TXN());
									objMS_ACCOUNT_OUT_DTL.setDR_CR_IND("1"); // DR_CR indicator for the FROM Account .
									objMS_ACCOUNT_OUT_DTL.setRESIDES(Bus_Rule_Input_Desc.RESIDES_IN);
									objFromMs_account_out_dtl = objMS_ACCOUNT_OUT_DTL;

									//To account Mappings..
									MS_ACCOUNT_OUT_DTL objMS_ACCOUNT_OUT_DTL_EXT = new MS_ACCOUNT_OUT_DTL();
									objMS_ACCOUNT_OUT_DTL_EXT.setDR_CR_IND("2"); // DR_CR indicator for the TO Account .
									objMS_ACCOUNT_OUT_DTL_EXT.setRESIDES(Bus_Rule_Input_Desc.RESIDES_OUT);
									objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACCT_TYPE(BRUtils.getBRExtAcc_Type(objExternalAccDetails));
									objMS_ACCOUNT_OUT_DTL_EXT.setEXT_SUB_ACCT_TYPE(BRUtils.getBRExtAcc_SubType(objExternalAccDetails));
									objMS_ACCOUNT_OUT_DTL_EXT.setROUTING_CODE(objExternalAccDetails.getCpybankcode());
									objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACCT_NUMBER(objExternalAccDetails.getCpyaccnum());
									objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACCT_LINK(objExternalAccDetails.getExternal_acct_link());
									objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACC_CREATED_BY(objExternalAccDetails.getCreated_by_id());
									objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACC_OWNER_ID(objExternalAccDetails.getKey_client());
									objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACCT_SGN(objExternalAccDetails.getSign_up_mode());
									objToMs_account_out_dtl = objMS_ACCOUNT_OUT_DTL_EXT;
								}
							}
						}
					}
					else if(transfer_Type.startsWith(ChkReqConstants.CHK))
					{
						if(!responseRTAB.isEmpty())
						{
							MS_RTAB_ACNT_DTLS objMS_RTAB_ACNT_DTLS =(MS_RTAB_ACNT_DTLS)responseRTAB.get(1); // Contains the RTAB Account details ... 
							if(objMS_ACCOUNT_OUT_DTL.getACCOUNT_NO()!=null)
							{
								if(getMSAccFormat(objMS_ACCOUNT_OUT_DTL.getACCOUNT_NO()).equalsIgnoreCase(getMSAccFormat(objMS_RTAB_ACNT_DTLS.getACCOUNT())))
								{
									//From account mappings..
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_SPNDG_PWR(BRUtils.getAvailableSpndPower(objMS_RTAB_ACNT_DTLS, txnDetails));
									objMS_ACCOUNT_OUT_DTL.setSPNDG_PWR_FROM_RTAB(objMS_RTAB_ACNT_DTLS.getAVAILABLE_SPENDING_POWER());
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_CASH_BALNCE(objMS_RTAB_ACNT_DTLS.getCASH_AVAILABLE());
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_MARGIN_BALNCE(objMS_RTAB_ACNT_DTLS.getMARGIN_AVAILABLE());
									objMS_ACCOUNT_OUT_DTL.setCASH_PENDNG_DEBIT(objMS_RTAB_ACNT_DTLS.getCASH_AVAILABLE_PENDING_TXN());
									objMS_ACCOUNT_OUT_DTL.setMARGIN_PENDNG_DEBIT(objMS_RTAB_ACNT_DTLS.getMARGIN_PENDING_TXN());
									objMS_ACCOUNT_OUT_DTL.setDR_CR_IND("1"); // DR_CR indicator for the FROM Account .
									objMS_ACCOUNT_OUT_DTL.setRESIDES(Bus_Rule_Input_Desc.RESIDES_IN);
									objFromMs_account_out_dtl = objMS_ACCOUNT_OUT_DTL;

									//To account Mappings..
									MS_ACCOUNT_OUT_DTL objMS_ACCOUNT_OUT_DTL_EXT = new MS_ACCOUNT_OUT_DTL();
									objMS_ACCOUNT_OUT_DTL_EXT.setDR_CR_IND("2"); // DR_CR indicator for the TO Account .
									objMS_ACCOUNT_OUT_DTL_EXT.setRESIDES(Bus_Rule_Input_Desc.RESIDES_OUT);
									objToMs_account_out_dtl = objMS_ACCOUNT_OUT_DTL_EXT;
								}
							}
						}
					}
					else if(transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))
					{
						//From account Mappings..
						MS_ACCOUNT_OUT_DTL objMS_ACCOUNT_OUT_DTL_EXT = new MS_ACCOUNT_OUT_DTL();
						objMS_ACCOUNT_OUT_DTL_EXT.setDR_CR_IND("1"); // DR_CR indicator for the TO Account .
						objMS_ACCOUNT_OUT_DTL_EXT.setRESIDES(Bus_Rule_Input_Desc.RESIDES_OUT);
						objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACCT_TYPE(BRUtils.getBRExtAcc_Type(objExternalAccDetails));
						objMS_ACCOUNT_OUT_DTL_EXT.setEXT_SUB_ACCT_TYPE(BRUtils.getBRExtAcc_SubType(objExternalAccDetails));
						objMS_ACCOUNT_OUT_DTL_EXT.setROUTING_CODE(objExternalAccDetails.getCpybankcode());
						objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACCT_NUMBER(objExternalAccDetails.getCpyaccnum());
						objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACCT_LINK(objExternalAccDetails.getExternal_acct_link());
						objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACC_CREATED_BY(objExternalAccDetails.getCreated_by_id());
						objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACC_OWNER_ID(objExternalAccDetails.getKey_client());
						objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACCT_SGN(objExternalAccDetails.getSign_up_mode());
						objFromMs_account_out_dtl = objMS_ACCOUNT_OUT_DTL_EXT;

						//To account Mappings..
						objMS_ACCOUNT_OUT_DTL.setDR_CR_IND("2");  // DR_CR indicator for the TO Account(In case of ACH-IN).
						objMS_ACCOUNT_OUT_DTL.setRESIDES(Bus_Rule_Input_Desc.RESIDES_IN);
						objToMs_account_out_dtl = objMS_ACCOUNT_OUT_DTL;
					}
					else if(transfer_Type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))
					{
						if(!responseRTAB.isEmpty())
						{
							MS_RTAB_ACNT_DTLS objMS_RTAB_ACNT_DTLS =(MS_RTAB_ACNT_DTLS)responseRTAB.get(1); // Contains the RTAB Account details ... 
							if(objMS_ACCOUNT_OUT_DTL.getACCOUNT_NO()!=null)
							{
								if(getMSAccFormat(objMS_ACCOUNT_OUT_DTL.getACCOUNT_NO()).equalsIgnoreCase(getMSAccFormat(objMS_RTAB_ACNT_DTLS.getACCOUNT())))
								{
									//From account mappings..
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_SPNDG_PWR(BRUtils.getAvailableSpndPower(objMS_RTAB_ACNT_DTLS, txnDetails));
									objMS_ACCOUNT_OUT_DTL.setSPNDG_PWR_FROM_RTAB(objMS_RTAB_ACNT_DTLS.getAVAILABLE_SPENDING_POWER());
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_CASH_BALNCE(objMS_RTAB_ACNT_DTLS.getCASH_AVAILABLE());
									objMS_ACCOUNT_OUT_DTL.setAVAILABLE_MARGIN_BALNCE(objMS_RTAB_ACNT_DTLS.getMARGIN_AVAILABLE());
									objMS_ACCOUNT_OUT_DTL.setCASH_PENDNG_DEBIT(objMS_RTAB_ACNT_DTLS.getCASH_AVAILABLE_PENDING_TXN());
									objMS_ACCOUNT_OUT_DTL.setMARGIN_PENDNG_DEBIT(objMS_RTAB_ACNT_DTLS.getMARGIN_PENDING_TXN());
									objMS_ACCOUNT_OUT_DTL.setDR_CR_IND("1"); // DR_CR indicator for the FROM Account .
									objMS_ACCOUNT_OUT_DTL.setRESIDES(Bus_Rule_Input_Desc.RESIDES_IN);
									objFromMs_account_out_dtl = objMS_ACCOUNT_OUT_DTL;

									//To account Mappings..
									MS_ACCOUNT_OUT_DTL objMS_ACCOUNT_OUT_DTL_PLA = new MS_ACCOUNT_OUT_DTL();
									objMS_ACCOUNT_OUT_DTL_PLA.setPLA_LOAN_EXISTS(BRUtils.isLoanExists(txnDetails));
									objMS_ACCOUNT_OUT_DTL_PLA.setPLA_OUTSTNDNG_AMT(objLoanAcntDetails.getLoanOutstandingBal());
									objMS_ACCOUNT_OUT_DTL_PLA.setDR_CR_IND("2"); // DR_CR indicator for the TO Account .
									objMS_ACCOUNT_OUT_DTL_PLA.setRESIDES(Bus_Rule_Input_Desc.RESIDES_OUT);
									objToMs_account_out_dtl = objMS_ACCOUNT_OUT_DTL_PLA;
								}
							}
						}
					}
				}
			}

			brTransactionInputVector.add(objBUS_RULE_INP_CRTL);
			brTransactionInputVector.add(objBUS_RUL_TXN_INP);
			brTransactionInputVector.add(objFromMs_account_out_dtl);
			brTransactionInputVector.add(objToMs_account_out_dtl);
		} 
		catch (Exception exception){
			exception.printStackTrace();
			throw exception;
		}
		EBWLogger.logDebug("BusinessRuleInterfaceIn", "Business Rule input paramters for the Transaction case "+brTransactionInputVector.toString());
		return brTransactionInputVector;
	}

	/**
	 * Business Rule for Account case
	 * @param txnDetails
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Vector setBRInterfaceAccountRuleIn(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.logDebug("BusinessRuleInterfaceIn", "Setting the Business Rule input for the Account case...");
		Vector brAccountInputVector=new Vector();
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

			//Business Rule Input Control Mappings ...
			BUS_RULE_INP_CRTL objBUS_RULE_INP_CRTL = new BUS_RULE_INP_CRTL();
			objBUS_RULE_INP_CRTL.setUSER_ID(objExternalAccDetails.getCreated_by_id());
			objBUS_RULE_INP_CRTL.setAPPL_ID(WSDefaultInputsMap.getBusRuleCntrlAppID(serviceContext));
			objBUS_RULE_INP_CRTL.setSERVER_IP(WSDefaultInputsMap.getBusRuleCntrlServerID(serviceContext));

			//Business Rule Input Transaction Mappings ...
			BUS_RUL_TXN_INP objBUS_RUL_TXN_INP = new BUS_RUL_TXN_INP();
			objBUS_RUL_TXN_INP.setACTION(objPaymentDetails.getEventDesc());
			objBUS_RUL_TXN_INP.setRULE_TYPE(Bus_Rule_Input_Desc.RULE_TYPE_ACNT); 
			objBUS_RUL_TXN_INP.setMODE(Bus_Rule_Input_Desc.ONLINE_MODE);

			//External Account Mappings..
			MS_ACCOUNT_OUT_DTL objMS_ACCOUNT_OUT_DTL_EXT = new MS_ACCOUNT_OUT_DTL();
			objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACCT_TYPE(BRUtils.getBRExtAcc_Type(objExternalAccDetails));
			objMS_ACCOUNT_OUT_DTL_EXT.setEXT_SUB_ACCT_TYPE(BRUtils.getBRExtAcc_SubType(objExternalAccDetails));
			objMS_ACCOUNT_OUT_DTL_EXT.setROUTING_CODE(objExternalAccDetails.getCpybankcode());
			objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACCT_NUMBER(objExternalAccDetails.getCpyaccnum());
			objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACC_CREATED_BY(objExternalAccDetails.getCreated_by_id());
			objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACC_OWNER_ID(objExternalAccDetails.getKey_client());
			objMS_ACCOUNT_OUT_DTL_EXT.setEXT_ACCT_SGN(objExternalAccDetails.getSign_up_mode());
			objMS_ACCOUNT_OUT_DTL_EXT.setEXT_OWNER(objExternalAccDetails.getAccount_owner());
			objMS_ACCOUNT_OUT_DTL_EXT.setTHRD_PRTY_IND(objExternalAccDetails.getThird_party_ind());
			objMS_ACCOUNT_OUT_DTL_EXT.setNAME_ON_EXT(objExternalAccDetails.getAcnt_business_name());
			objMS_ACCOUNT_OUT_DTL_EXT.setRCVR_ADDR_1(objExternalAccDetails.getRcvr_add1());
			objMS_ACCOUNT_OUT_DTL_EXT.setRCVR_ADDR_2(objExternalAccDetails.getRcvr_add2());
			objMS_ACCOUNT_OUT_DTL_EXT.setRCVR_ADDR_3(objExternalAccDetails.getRcvr_add3());
			objMS_ACCOUNT_OUT_DTL_EXT.setRCVR_ADDR_4(objExternalAccDetails.getRcvr_add4());
			objMS_ACCOUNT_OUT_DTL_EXT.setBANK_NAME(objExternalAccDetails.getCpyboname1());
			objMS_ACCOUNT_OUT_DTL_EXT.setBANK_ADDR_1(objExternalAccDetails.getCpyadd1());
			objMS_ACCOUNT_OUT_DTL_EXT.setBANK_ADDR_2(objExternalAccDetails.getCpyadd2());
			objMS_ACCOUNT_OUT_DTL_EXT.setBANK_ADDR_3(objExternalAccDetails.getCpyadd3());
			objMS_ACCOUNT_OUT_DTL_EXT.setBANK_ADDR_4(objExternalAccDetails.getCpyadd4());

			brAccountInputVector.add(objBUS_RULE_INP_CRTL);
			brAccountInputVector.add(objBUS_RUL_TXN_INP);
			brAccountInputVector.add(objMS_ACCOUNT_OUT_DTL_EXT);
		} 
		catch (Exception exception){
			throw exception;
		}
		EBWLogger.logDebug("BusinessRuleInterfaceIn", "Business Rule input paramters for the Account case " + brAccountInputVector.toString());
		return brAccountInputVector;
	}

	/**
	 * Update the BR transaction input and BR control input for the limit check processing and mappings during Initiate/Edit transfers..
	 * @param objBus_rul_txn_inp
	 * @param objBUS_RULE_INP_CRT
	 * @throws Exception 
	 */
	public static void setLimitParameters(HashMap txnDetails,BUS_RUL_TXN_INP objBUS_RUL_TXN_INP,BUS_RULE_INP_CRTL objBUS_RULE_INP_CRTL) throws Exception
	{
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//From account details..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			String transfer_Type = objPaymentDetails.getTransfer_Type();
			boolean isLimitActionReq = false;

			if(transfer_Type!=null && transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				isLimitActionReq = LimitRequestProcessor.getLimitAction(txnDetails);
				if(isLimitActionReq)
				{
					//Cntrl Input mappings..
					objBUS_RULE_INP_CRTL.setLIMIT_CHECK_REQ("Y");

					//BR Transaction input mapping..
					objBUS_RUL_TXN_INP.setTXN_TYPE(transfer_Type);
					objBUS_RUL_TXN_INP.setDEBIT_ACCOUNT(objFromMSAcc_Details.getKeyAccount());
					objBUS_RUL_TXN_INP.setCREATE_FLG(objPaymentDetails.getCreate_limit());
					objBUS_RUL_TXN_INP.setUPDATE_FLG(objPaymentDetails.getUpdate_limit());
					objBUS_RUL_TXN_INP.setDELETE_FLG(objPaymentDetails.getDelete_limit());

					//Additional details for the transaction processing... 
					if(objPaymentDetails.getUpdate_limit() == 1 ){
						objBUS_RUL_TXN_INP.setORGNL_TXN_AMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getPrevTxnAmount()));
						objBUS_RUL_TXN_INP.setORGNL_TXN_DATE(ConvertionUtil.convertToDate(objPaymentDetails.getPrevTxnDate()));
						objBUS_RUL_TXN_INP.setNEW_TXN_AMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
						objBUS_RUL_TXN_INP.setNEW_TXN_DATE(ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate()));
					}
					else if(objPaymentDetails.getDelete_limit() == 1 ){
						objBUS_RUL_TXN_INP.setORGNL_TXN_AMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getPrevTxnAmount()));
						objBUS_RUL_TXN_INP.setORGNL_TXN_DATE(ConvertionUtil.convertToDate(objPaymentDetails.getPrevTxnDate()));
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
	}


	/** Following function is used to set the external services response details in the BR Service
	 * 
	 * @param toObjects
	 * @param externalSrvResponse
	 */

	public static void setExternalSrvcResponseDetails(BUS_RUL_TXN_INP objBus_rul_txn_inp,HashMap txnDetails)
	{
		String fromAccTier = "";

		//Payment Details ...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		if(txnDetails.containsKey("PaymentDetails")){
			objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
		}
		String txn_type= objPaymentDetails.getTransfer_Type();

		//Account View Response Mappings for getting tier in case of limit check processing...
		if(txnDetails.containsKey("MerlinOutputDetails")){
			AccountDetails objaccDetails= (AccountDetails)txnDetails.get("MerlinOutputDetails");
			ArrayList merlinAccDetails = objaccDetails.getMerlinOutResponse(); 
			for(int j=0;j<merlinAccDetails.size();j++){
				if(merlinAccDetails.get(j) instanceof MerlinOutResponse){
					MerlinOutResponse accMerlinResponse =(MerlinOutResponse)merlinAccDetails.get(j);
					if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						fromAccTier=accMerlinResponse.getCatTier();
					}
				}
			}
			objBus_rul_txn_inp.setTIER(fromAccTier);
		}
	}

	/** Formats the account number before storing or comparing in the DB in 4-7-4 format
	 *  In case the Account Number are not in the correct format ...  
	 * 
	 */
	public static String getMSAccFormat(String msAccNumber)
	{
		do
		{
			if(msAccNumber.length()>=7){
				break;
			}
			else {
				msAccNumber="0"+msAccNumber;
			}
		} while(msAccNumber.length()!=7);
		return msAccNumber;
	}

}
