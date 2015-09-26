package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.BUS_RULE_INP_CRTL;
import com.tcs.Payments.EAITO.BUS_RUL_TXN_INP;
import com.tcs.Payments.EAITO.MO_TXN_MONITORING_REQ;
import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_ADDRESS;
import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_DTL;
import com.tcs.Payments.EAITO.MS_RTAB_ACNT_DTLS;
import com.tcs.Payments.ms360Utils.BRUtils;
import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.InitiatorRoleDesc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.formbean.RequestHeaderInfo;
import com.tcs.ebw.payments.transferobject.ContextAccount;
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
 * **********************************************************
 */
public class BusinessRuleInterfaceIn {

	public BusinessRuleInterfaceIn(){

	}

	/**
	 * Business Rules for block display case 
	 * @param txnDetails
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Vector getBRInterfaceBlockDispIn(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.logDebug("BusinessRuleInterfaceIn", "Setting the Business Rule input for the Blocked Display Case...");
		Vector brBlockedDisplayInVector = new Vector();
		try {
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
	 * Business Rule for transaction case
	 * @param txnDetails
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Vector setBRInterfaceTransactionRuleIn(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.logDebug("BusinessRuleInterfaceIn", "Setting the Business Rule input for the transaction case...");
		Vector brTransactionInputVector=new Vector();
		boolean isRSACallReq = false;
		try 
		{
			ArrayList responseRTAB = new ArrayList();
			ArrayList responseMerlin = new ArrayList();

			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External account details..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//User Details..
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

			//Loan Account Details ..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			if(txnDetails.containsKey("LoanAccountDetails")){
				objLoanAcntDetails = (PortfolioLoanAccount)txnDetails.get("LoanAccountDetails");
			}

			String transfer_Type = objPaymentDetails.getTransfer_Type();
			Date transferDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
			Date businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());
			boolean isTxnModified = objPaymentDetails.isTxnModified();
			String userRole=null;
			boolean isFA= objMSUserDetails.isFA();
			if(isFA)
				userRole=InitiatorRoleDesc.FA;
			else
				userRole=InitiatorRoleDesc.Client;

			// Business Rule Input Control Mappings ...
			BUS_RULE_INP_CRTL objBUS_RULE_INP_CRTL = new BUS_RULE_INP_CRTL();
			objBUS_RULE_INP_CRTL.setUSER_ID(objMSUserDetails.getUuid());
			objBUS_RULE_INP_CRTL.setTXN_USER_ID(objPaymentDetails.getCurrent_owner_id());
			objBUS_RULE_INP_CRTL.setGROUP(MSCommonUtils.getInitiatorRoleDesc(objPaymentDetails.getCurrent_owner_role())); //Needs to be changed according to the Context XML.
			objBUS_RULE_INP_CRTL.setROLE(userRole);
			objBUS_RULE_INP_CRTL.setRSA_REQUEST_FLAG("N"); 
			objBUS_RULE_INP_CRTL.setRSA_REVIEW_FLAG(MSCommonUtils.getRSAReviewFlag(objPaymentDetails)); 
			objBUS_RULE_INP_CRTL.setUUID(objMSUserDetails.getUuid());
			objBUS_RULE_INP_CRTL.setLIMIT_CHECK_REQ("N");
			objBUS_RULE_INP_CRTL.setCLIENT_IDENTFIER(objMSUserDetails.getClientIdentifier());
			objBUS_RULE_INP_CRTL.setAPPL_ID(WSDefaultInputsMap.getBusRuleCntrlAppID(new Boolean(true),serviceContext));
			objBUS_RULE_INP_CRTL.setSERVER_IP(WSDefaultInputsMap.getBusRuleCntrlServerID(new Boolean(true),serviceContext));
			if(isTxnModified){
				objBUS_RULE_INP_CRTL.setELIGIBLE_ACC_CHECK(1);
			}

			// Business Rule Input Transaction Mappings ...
			BUS_RUL_TXN_INP objBUS_RUL_TXN_INP = new BUS_RUL_TXN_INP();
			objBUS_RUL_TXN_INP.setCONFIRM_NUM(objPaymentDetails.getTransactionId());
			objBUS_RUL_TXN_INP.setTYPE(BRUtils.getBRTxnTypeValue(objPaymentDetails.getTransfer_Type()));
			objBUS_RUL_TXN_INP.setACTION(objPaymentDetails.getEventDesc());
			objBUS_RUL_TXN_INP.setRULE_TYPE(Bus_Rule_Input_Desc.RULE_TYPE_TXN); 
			objBUS_RUL_TXN_INP.setAMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			objBUS_RUL_TXN_INP.setEXE_DATE(transferDate);
			objBUS_RUL_TXN_INP.setBUS_DATE(businessDate);
			objBUS_RUL_TXN_INP.setPAGE_TYPE(BRUtils.getBRTxn_PageType(objPaymentDetails));
			objBUS_RUL_TXN_INP.setTXN_SUB_TYPE(BRUtils.getBRTxnSub_Type(objPaymentDetails));
			objBUS_RUL_TXN_INP.setFREQUENCY(BRUtils.getBRFrequency_type(objPaymentDetails));
			objBUS_RUL_TXN_INP.setIRA_TXN_IND(objPaymentDetails.getIraTxnFlag());
			objBUS_RUL_TXN_INP.setMS360_SAME_NAME(BRUtils.getSameNameFlag(txnDetails));
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
			if(transferDate.compareTo(businessDate)==0){
				objBUS_RUL_TXN_INP.setSTATE(Bus_Rule_Input_Desc.STATE_CURRENT);
			}
			else if(transferDate.after(businessDate)){
				objBUS_RUL_TXN_INP.setSTATE(Bus_Rule_Input_Desc.STATE_FUTURE);
			}

			//Mappings if Limit call required...
			setLimitParameters(txnDetails,objBUS_RUL_TXN_INP,objBUS_RULE_INP_CRTL);

			//Mapping if RSA call required...
			MO_TXN_MONITORING_REQ objMO_TXN_MONITORING_REQ = new MO_TXN_MONITORING_REQ();
			isRSACallReq = setRSAParameters(txnDetails,serviceContext,objBUS_RUL_TXN_INP,objBUS_RULE_INP_CRTL,objMO_TXN_MONITORING_REQ);

			//Mapping Account Tier Mappings from account view...
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
								else{
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

						//To account mappings..
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

			//Vector creation...

			//Transaction details mappings in the BR input vector...
			brTransactionInputVector.add(objBUS_RULE_INP_CRTL);
			brTransactionInputVector.add(objBUS_RUL_TXN_INP);
			brTransactionInputVector.add(objFromMs_account_out_dtl);
			brTransactionInputVector.add(objToMs_account_out_dtl);

			//Transaction Monitoring mapping in the BR input vector for RSA Call..
			if(isRSACallReq){
				brTransactionInputVector.add(objMO_TXN_MONITORING_REQ);
			}

			//Eligible Account check mapping in the BR input vector..
			if(isTxnModified && objBUS_RULE_INP_CRTL.getELIGIBLE_ACC_CHECK()==1){
				ArrayList contextAccounts = objMSUserDetails.getContextAccounts();
				if(contextAccounts!=null && !contextAccounts.isEmpty()){
					for(int i=0; i<contextAccounts.size(); i++){
						ContextAccount contextAcc = (ContextAccount)contextAccounts.get(i);
						MS_ACCOUNT_OUT_ADDRESS ms_account_out_add = new MS_ACCOUNT_OUT_ADDRESS();
						ms_account_out_add.setOFFICE(contextAcc.getOffice());
						ms_account_out_add.setACCOUNT_NO(contextAcc.getAccount());
						brTransactionInputVector.add(ms_account_out_add);
					}
				}
			}
		} 
		catch (Exception exception){
			throw exception;
		}
		EBWLogger.logDebug("BusinessRuleInterfaceIn", "Business Rule input paramters for the Transaction case is "+brTransactionInputVector.toString());
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

			if(transfer_Type!=null && transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
			{
				objBUS_RUL_TXN_INP.setTXN_TYPE(transfer_Type);
				objBUS_RUL_TXN_INP.setDEBIT_ACCOUNT(objFromMSAcc_Details.getKeyAccount());
				isLimitActionReq = LimitRequestProcessor.getLimitAction(txnDetails);
				if(isLimitActionReq)
				{
					//Cntrl Input mappings..
					objBUS_RULE_INP_CRTL.setLIMIT_CHECK_REQ("Y");

					//Transaction Input mappings for calling update Limit..
					objBUS_RUL_TXN_INP.setCREATE_FLG(objPaymentDetails.getCreate_limit());
					objBUS_RUL_TXN_INP.setUPDATE_FLG(objPaymentDetails.getUpdate_limit());
					objBUS_RUL_TXN_INP.setDELETE_FLG(objPaymentDetails.getDelete_limit());

					//Additional details for the transaction processing... 
					if(objPaymentDetails.getUpdate_limit() == 1){
						objBUS_RUL_TXN_INP.setORGNL_TXN_AMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getPrevTxnAmount()));
						objBUS_RUL_TXN_INP.setORGNL_TXN_DATE(ConvertionUtil.convertToDate(objPaymentDetails.getPrevTxnDate()));
						objBUS_RUL_TXN_INP.setNEW_TXN_AMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
						objBUS_RUL_TXN_INP.setNEW_TXN_DATE(ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate()));
					}
					else if(objPaymentDetails.getDelete_limit() == 1){
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

	/**
	 * Set the RSA Request Parameters if and only if required...
	 * @throws Exception 
	 */
	public static boolean setRSAParameters(HashMap txnDetails,ServiceContext context,BUS_RUL_TXN_INP objBUS_RUL_TXN_INP,BUS_RULE_INP_CRTL objBUS_RULE_INP_CRTL,MO_TXN_MONITORING_REQ objMO_TXN_MONITORING_REQ) throws Exception
	{
		boolean isRSACallReq = false;
		try
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External Account Details...
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//User Details..
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//Data Extraction...
			String transfer_Type = objPaymentDetails.getTransfer_Type();
			String event_Desc = objPaymentDetails.getEventDesc();
			Date transferDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
			Date businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());
			String frequencyFlag = objPaymentDetails.getFrequency_Type();
			ArrayList validTxnTypes = WSDefaultInputsMap.getRSATxnTypes(Boolean.TRUE,context);

			//Mappings...
			if(transfer_Type!=null && validTxnTypes!=null && (validTxnTypes.contains(transfer_Type)))
			{
				if(event_Desc!=null && ((event_Desc.equalsIgnoreCase("Create-Confirm")||event_Desc.equalsIgnoreCase("Modify-Confirm"))))
				{
					objBUS_RULE_INP_CRTL.setRSA_REQUEST_FLAG("Y"); 

					RequestHeaderInfo objReqHeaderInfo = (RequestHeaderInfo)objPaymentDetails.getRequestHeaderInfo();
					objMO_TXN_MONITORING_REQ.setCLIENT_TXN_ID(objPaymentDetails.getTransactionId());
					objMO_TXN_MONITORING_REQ.setBRANCH(BRUtils.getTxnMonitoringBranch(txnDetails));
					objMO_TXN_MONITORING_REQ.setEXT_ACCT_TYPE(BRUtils.getBRExtAcc_Type(objExternalAccDetails));
					objMO_TXN_MONITORING_REQ.setROUTING_CODE(objExternalAccDetails.getCpybankcode());
					objMO_TXN_MONITORING_REQ.setEXT_ACCT_NO(objExternalAccDetails.getCpyaccnum());
					objMO_TXN_MONITORING_REQ.setTXN_MEDIUM_TYPE(TxnTypeCode.ACH_TYPE);
					objMO_TXN_MONITORING_REQ.setAMT(ConvertionUtil.convertToString(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount())));
					objMO_TXN_MONITORING_REQ.setDUE_DATE(MSCommonUtils.getRSADateFormat(objPaymentDetails.getRequestedDate()));
					objMO_TXN_MONITORING_REQ.setEST_DELIVERY_DATE(MSCommonUtils.getRSADateFormat(objPaymentDetails.getEstimatedArrivalDate()));
					objMO_TXN_MONITORING_REQ.setHTTP_ACCEPT(objReqHeaderInfo.getHTTP_ACCEPT());
					objMO_TXN_MONITORING_REQ.setHTTP_ACCEPT_CHARS(objReqHeaderInfo.getHTTP_ACCEPT_CHARS());
					objMO_TXN_MONITORING_REQ.setHTTP_ACCEPT_ENCODING(objReqHeaderInfo.getHTTP_ACCEPT_ENCODING());
					objMO_TXN_MONITORING_REQ.setHTTP_ACCEPT_LANG(objReqHeaderInfo.getHTTP_ACCEPT_LANG());
					objMO_TXN_MONITORING_REQ.setHTTP_REFERRER(objReqHeaderInfo.getHTTP_REFERRER());
					objMO_TXN_MONITORING_REQ.setUSER_AGENT(objReqHeaderInfo.getUSER_AGENT());
					objMO_TXN_MONITORING_REQ.setIP_ADDRESS(objMSUserDetails.getClientIPAddress());
					objMO_TXN_MONITORING_REQ.setUUID(objMSUserDetails.getUuid());
					objMO_TXN_MONITORING_REQ.setUSER_ID(objMSUserDetails.getLoginId());
					objMO_TXN_MONITORING_REQ.setTIME_STAMP(MSCommonUtils.getCurrentTime().toString());
					objMO_TXN_MONITORING_REQ.setDEVICE_COOKIE(objMSUserDetails.getDeviceCookie());
					objMO_TXN_MONITORING_REQ.setDEVICE_FSO(objMSUserDetails.getDeviceFSO());
					objMO_TXN_MONITORING_REQ.setDEVICE_PRINT(objMSUserDetails.getDevicePrint());
					objMO_TXN_MONITORING_REQ.setLAST_ACCT_OPEN_DATE(objMSUserDetails.getLastAccountOpenDate());
					objMO_TXN_MONITORING_REQ.setLAST_ADDRESS_CHANGE_DATE(objMSUserDetails.getLastAddressChangeDate());
					objMO_TXN_MONITORING_REQ.setLAST_EMAIL_CHANGE_DATE(objMSUserDetails.getLastEmailChangeDate());
					objMO_TXN_MONITORING_REQ.setLAST_ONLINE_SVC_PWD_CHNG_DATE(objMSUserDetails.getLastOnlineServicePasswordChangeDate());
					objMO_TXN_MONITORING_REQ.setLAST_PHONE_CHANGE_DATE(objMSUserDetails.getLastPhoneChangeDate());
					objMO_TXN_MONITORING_REQ.setSESSION_ID(objMSUserDetails.getSessionId());
					objMO_TXN_MONITORING_REQ.setONLINE_SERVICE_ENROLL_DATE(objMSUserDetails.getOnlineServiceEnrollDate());
					objMO_TXN_MONITORING_REQ.setCURRENCY(objPaymentDetails.getTransfer_Currency());
					if(frequencyFlag!=null && frequencyFlag.equalsIgnoreCase("2")){
						objMO_TXN_MONITORING_REQ.setSCHEDULE("RECURRING");
					}
					else {
						if(transferDate.compareTo(businessDate)==0){
							objMO_TXN_MONITORING_REQ.setSCHEDULE("IMMEDIATE");
						}
						else if(transferDate.after(businessDate)){
							objMO_TXN_MONITORING_REQ.setSCHEDULE("SCHEDULED");
						}
					}

					//Setting the RSA call required flag...
					isRSACallReq = true;
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return isRSACallReq;
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

		//Account View Response Mappings ..
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
		do{
			if(msAccNumber.length()>=7){
				break;
			}
			else {
				msAccNumber="0"+msAccNumber;
			}
		}while(msAccNumber.length()!=7);
		return msAccNumber;
	}
}
