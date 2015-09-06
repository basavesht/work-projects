package com.tcs.Payments.ms360Utils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_DTL;
import com.tcs.Payments.EAITO.MS_RTAB_ACNT_DTLS;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetUtilizedAmount;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *   224703        29-06-2011	    P3               CR 215
 * **********************************************************
 */
public class BRUtils {

	/**
	 * Returns the Same_Name flag for the accounts involved in transaction (Internal Transfers)...
	   The same name logic is to be coded as follows for internal transfers in MS360:
        If Account class, Office, Account sub class and SSN of all the clients match, then the accounts are considered as Same Name.
		OR
		If Account Class is "1" and Sub class is either "A", "B", "N" or "O" and Office and all clients SSN match then also accounts are considered as Same Name.
        AccountsView Attributes:
		Account/AccountClass
		Account/SubClass
		SSN: Both Client/SSN_TAXID_Classfication and Client/SSN for all clients should match when we say SSN have to match
	    OR
	    Account Class of the From account is “0” (which means the From account is a Single Account)
        Account Class of the To account is “1” (which means the To account is a Joint Account)
        The Social Security or Tax Identification number on the From account is also present on the To account (which means the owner of the From account is listed as one of the owners of the To account) 
	 * @param txnDetails
	 * @return
	 * @throws Exception 
	 */

	public static String getSameNameFlag(HashMap txnDetails) throws Exception
	{
		EBWLogger.logDebug("com.tcs.Payments.ms360Utils.BRUtils"," Same_Name flag logic begins");
		String same_name_flg = "N";
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Account View Output Details...
			AccountDetails objAccDetails= new AccountDetails();
			if(txnDetails.containsKey("MerlinOutputDetails")){
				objAccDetails= (AccountDetails)txnDetails.get("MerlinOutputDetails");
			}

			//Check Request Mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			//External account details...
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			String txn_type = objPaymentDetails.getTransfer_Type();
			String third_Party_Ind = objExternalAccDetails.getThird_party_ind();
			if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.INTERNAL)))
			{
				ArrayList frm_AccClientDetails = new ArrayList();
				ArrayList to_AccClientDetails = new ArrayList();
				ArrayList<Object> merlinOutputList = (ArrayList)objAccDetails.getInfoMerlinObject();
				List<String> eligibleSubClass = new ArrayList<String>(Arrays.asList("A","B","N","O"));

				//From account client details extraction...
				MS_ACCOUNT_OUT_DTL fromAccMerlin = (MS_ACCOUNT_OUT_DTL)merlinOutputList.get(0);
				String acnt_frm_class=fromAccMerlin.getACNT_CLS();
				String acnt_frm_Subclass=fromAccMerlin.getSUB_CLASS();
				MS_ACCOUNT_OUT_DTL.ACCOUNT_OUT_CLIENT objFrmClientDetails[] = fromAccMerlin.getMS_ACCOUNT_OUT_CLIENT();
				for(int j = 0; j < objFrmClientDetails.length; j++){
					ArrayList clientInfo = new ArrayList();
					clientInfo.add(objFrmClientDetails[j].getSSN());
					clientInfo.add(objFrmClientDetails[j].getSSN_TXID_CLASS());
					frm_AccClientDetails.add(clientInfo);
				}

				//To account details extraction...
				MS_ACCOUNT_OUT_DTL toAccMerlin = (MS_ACCOUNT_OUT_DTL)merlinOutputList.get(1);
				String acnt_to_class=toAccMerlin.getACNT_CLS();
				String acnt_to_Subclass=toAccMerlin.getSUB_CLASS();
				MS_ACCOUNT_OUT_DTL.ACCOUNT_OUT_CLIENT objToClientDetails[] = toAccMerlin.getMS_ACCOUNT_OUT_CLIENT();
				for(int k = 0; k < objToClientDetails.length; k++){
					ArrayList clientInfo = new ArrayList();
					clientInfo.add(objToClientDetails[k].getSSN());
					clientInfo.add(objToClientDetails[k].getSSN_TXID_CLASS());
					to_AccClientDetails.add(clientInfo);
				}

				//Logic for setting the same_name flag..
				if(acnt_frm_class!=null && acnt_to_class!=null && acnt_frm_class.equalsIgnoreCase(acnt_to_class)
						&& acnt_frm_Subclass!=null && acnt_to_Subclass!=null && acnt_frm_Subclass.equalsIgnoreCase(acnt_to_Subclass)
						&& (frm_AccClientDetails.containsAll(to_AccClientDetails) && to_AccClientDetails.containsAll(frm_AccClientDetails))){
					same_name_flg = "Y";
				}
				else if(acnt_frm_class!=null && acnt_to_class!=null  && (acnt_frm_class.equalsIgnoreCase("1") && acnt_to_class.equalsIgnoreCase("1"))
						&& acnt_frm_Subclass!=null && acnt_to_Subclass!=null && (eligibleSubClass.contains(acnt_frm_Subclass) && eligibleSubClass.contains(acnt_to_Subclass))
						&& (frm_AccClientDetails.containsAll(to_AccClientDetails) && to_AccClientDetails.containsAll(frm_AccClientDetails))){
					same_name_flg = "Y";
				}
				else if(acnt_frm_class!=null && acnt_to_class!=null  && (acnt_frm_class.equalsIgnoreCase("0") && acnt_to_class.equalsIgnoreCase("1"))
						&& (to_AccClientDetails.containsAll(frm_AccClientDetails))){
					same_name_flg = "Y";
				}
			}
			else if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || (txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))))
			{
				if(third_Party_Ind!=null && third_Party_Ind.equalsIgnoreCase(MSSystemDefaults.THRD_PARTY_Y)){
					same_name_flg = "N";
				}
				else {
					same_name_flg = "Y";
				}
			}
			else if(txn_type!=null && (txn_type.startsWith(ChkReqConstants.CHK))){
				if(!objCheckRequest.isThirdPartyflag()){
					same_name_flg = "Y";
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
	 * Returns the Utilized amount for the transactions...
	 * @param txnDetails
	 * @return
	 */
	public static BigDecimal getUtitlizedAmount(HashMap txnDetails,ServiceContext context) throws Exception 
	{
		EBWLogger.logDebug("com.tcs.Payments.ms360Utils.BRUtils","Utilized amount fetch logic begins");
		BigDecimal utilisedAmnt = null;
		BigDecimal transferAmount = new BigDecimal(0D);
		try 
		{
			//Mapping the payment attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String transfer_type = objPaymentDetails.getTransfer_Type();
			transferAmount = ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount());

			if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL)))
			{
				//Getting the utilised amount for transaction...
				GetUtilizedAmount objUtilisedAmnt = new GetUtilizedAmount();
				objUtilisedAmnt.setConnection(context.getConnection());
				objUtilisedAmnt.getTxnUtilizedAmount(txnDetails,context);

				utilisedAmnt = objPaymentDetails.getUtilizedAmount().add(transferAmount);
			}
			else if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
			{
				utilisedAmnt = transferAmount;
			}
			else if(transfer_type!=null && (transfer_type.startsWith(ChkReqConstants.CHK)))
			{
				//Getting the utilised amount for checks.....
				GetUtilizedAmount objUtilisedAmnt = new GetUtilizedAmount();
				objUtilisedAmnt.setConnection(context.getConnection());
				objUtilisedAmnt.getCheckUtilizedAmount(txnDetails,context);

				utilisedAmnt = objPaymentDetails.getUtilizedAmount().add(transferAmount);

			}
			EBWLogger.logDebug("com.tcs.Payments.ms360Utils.BRUtils","Utilised amount is "+utilisedAmnt);
		} 
		catch(SQLException sqlexception){
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
		return utilisedAmnt;
	}

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
		else if(txn_Type.startsWith(ChkReqConstants.CHK)){
			brTxn_Typ=Bus_Rule_Input_Desc.TYPE_CHK;
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
		if(transfer_type!=null && transfer_type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
			page_type = Bus_Rule_Input_Desc.MS360_INT;
		}
		else if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
			page_type = Bus_Rule_Input_Desc.MS360_ACH;
		}
		else if(transfer_type!=null && transfer_type.startsWith(ChkReqConstants.CHK)){
			page_type = Bus_Rule_Input_Desc.MS360_CHECK;
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
		else if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))){
			txn_sub_type = Bus_Rule_Input_Desc.MS_INCOMING;
		}
		else if(transfer_type!=null && (transfer_type.startsWith(ChkReqConstants.CHK))){
			txn_sub_type = Bus_Rule_Input_Desc.MS_OUTGOING;
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
	 * Gets the BR Transaction Fee charged to..
	 * @param objPaymentDetails
	 */
	public static String getBRFeeChargedTo(PaymentDetailsTO objPaymentDetails,CheckRequestTO objCheckRequestDetails)
	{
		String fee_charged_to = null;
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && transfer_type.startsWith(ChkReqConstants.CHK))
		{
			fee_charged_to = objCheckRequestDetails.getChargedTo();
			if(fee_charged_to!=null && fee_charged_to.equalsIgnoreCase(ChkReqConstants.CHARGE_TO_BRANCH)){
				fee_charged_to = Bus_Rule_Input_Desc.CHARGED_TO_BRANCH;
			}
			else if(fee_charged_to!=null && fee_charged_to.equalsIgnoreCase(ChkReqConstants.CHARGE_TO_CLIENT)){
				fee_charged_to = Bus_Rule_Input_Desc.CHARGED_TO_CLIENT;
			}
		}
		return fee_charged_to;
	}

	/**
	 * Gets the BR Pick up option..
	 * @param objPaymentDetails
	 * @param objCheckRequestDetails
	 * @return
	 */
	public static String getBRPickUpOption(PaymentDetailsTO objPaymentDetails,CheckRequestTO objCheckRequestDetails)
	{
		Double pick_up_option = null;
		String pick_up_option_desc = null;
		String transfer_type = objPaymentDetails.getTransfer_Type();

		if(transfer_type!=null && transfer_type.startsWith(ChkReqConstants.CHK))
		{
			pick_up_option = objCheckRequestDetails.getPickUpOption();
			if(pick_up_option!=null && pick_up_option.equals(1D)){
				pick_up_option_desc = Bus_Rule_Input_Desc.PICKUPOPTION_SM;
			}
			else if(pick_up_option!=null && pick_up_option.equals(2D)){
				pick_up_option_desc = Bus_Rule_Input_Desc.PICKUPOPTION_OM;
			}
			else if(pick_up_option!=null && pick_up_option.equals(3D)){
				pick_up_option_desc = Bus_Rule_Input_Desc.PICKUPOPTION_PCB;
			}
			else if(pick_up_option!=null && pick_up_option.equals(4D)){
				pick_up_option_desc = Bus_Rule_Input_Desc.PICKUPOPTION_PAB;
			}
		}
		return pick_up_option_desc;
	}

	/**
	 * Check is the check request is certified or not..
	 * @param objPaymentDetails
	 * @param objCheckRequestDetails
	 * @return
	 */
	public static String getBRCheckCertifiedFlg(PaymentDetailsTO objPaymentDetails,CheckRequestTO objCheckRequestDetails)
	{
		String check_certified_flag = null;
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && transfer_type.startsWith(ChkReqConstants.CHK)){
			check_certified_flag = objCheckRequestDetails.getCertifiedFlag();
			if(check_certified_flag!=null && check_certified_flag.equalsIgnoreCase("Y")){
				check_certified_flag = Bus_Rule_Input_Desc.CERTIFIED;
			}
			else if(check_certified_flag!=null && check_certified_flag.equalsIgnoreCase("N")){
				check_certified_flag = Bus_Rule_Input_Desc.NON_CERTIFIED;
			}
		}
		return check_certified_flag;
	}

	/**
	 * Is default address selected flag..
	 * @param objPaymentDetails
	 * @param objCheckRequestDetails
	 * @return
	 */
	public static String getBRDefaultAddressFlg(PaymentDetailsTO objPaymentDetails,CheckRequestTO objCheckRequestDetails)
	{
		String default_address_flag = null;
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && transfer_type.startsWith(ChkReqConstants.CHK)){
			default_address_flag = objCheckRequestDetails.getDefaultAddressFlag();
			if(default_address_flag!=null && default_address_flag.equalsIgnoreCase("Y")){
				default_address_flag = Bus_Rule_Input_Desc.DEFAULT_ADDRESS;
			}
			else if(default_address_flag!=null && default_address_flag.equalsIgnoreCase("N")){
				default_address_flag = Bus_Rule_Input_Desc.NON_DEFAULT_ADDRESS;
			}
		}
		return default_address_flag;
	}

	/**
	 * Is Foreign address selected flag..
	 * @param objPaymentDetails
	 * @param objCheckRequestDetails
	 * @return
	 */
	public static String getBRForeignAddressFlg(PaymentDetailsTO objPaymentDetails,CheckRequestTO objCheckRequestDetails)
	{
		String foreign_address_flag = null;
		String transfer_type = objPaymentDetails.getTransfer_Type();
		if(transfer_type!=null && transfer_type.startsWith(ChkReqConstants.CHK)){
			foreign_address_flag = objCheckRequestDetails.getForeignAddressFlag();
			if(foreign_address_flag!=null && foreign_address_flag.equalsIgnoreCase("Y")){
				foreign_address_flag = Bus_Rule_Input_Desc.FOREIGN;
			}
			else if(foreign_address_flag!=null && foreign_address_flag.equalsIgnoreCase("N")){
				foreign_address_flag = Bus_Rule_Input_Desc.DOMESTIC;
			}
		}
		return foreign_address_flag;
	}


	/**
	 * Gets the BR Pick up option..
	 * @param objPaymentDetails
	 * @param objCheckRequestDetails
	 * @return
	 */
	public static String getFeePickUpOption(PaymentDetailsTO objPaymentDetails,CheckRequestTO objCheckRequestDetails)
	{
		Double pick_up_option = null;
		String pick_up_option_desc = null;
		String transfer_type = objPaymentDetails.getTransfer_Type();

		if(transfer_type!=null && transfer_type.startsWith(ChkReqConstants.CHK))
		{
			pick_up_option = objCheckRequestDetails.getPickUpOption();
			if(pick_up_option!=null && pick_up_option.equals(1D)){
				pick_up_option_desc = Fee_Input_Desc.PICKUPOPTION_SM;
			}
			else if(pick_up_option!=null && pick_up_option.equals(2D)){
				pick_up_option_desc = Fee_Input_Desc.PICKUPOPTION_OM;
			}
			else if(pick_up_option!=null && pick_up_option.equals(3D)){
				pick_up_option_desc = Fee_Input_Desc.PICKUPOPTION_PCB;
			}
			else if(pick_up_option!=null && pick_up_option.equals(4D)){
				pick_up_option_desc = Fee_Input_Desc.PICKUPOPTION_PAB;
			}
		}
		return pick_up_option_desc;
	}

	/**
	 * Returns the Print Memo On for Check....
	 * @param objCheckRequest
	 * @return
	 */
	public static String getBRPrintMemoOnCheck(CheckRequestTO objCheckRequest)
	{
		String printMemoOn = "N";
		if(objCheckRequest.getPrintMemoCheckFlag()!=null && objCheckRequest.getPrintMemoCheckFlag().equalsIgnoreCase("Y")){
			printMemoOn = "Y";
		}
		return printMemoOn;
	}

	/**
	 * Returns the Print Memo On for Stub....
	 * @param objCheckRequest
	 * @return
	 */
	public static String getBRPrintMemoOnStub(CheckRequestTO objCheckRequest)
	{
		String printMemoOn = "N";
		if(objCheckRequest.getPrintMemoStubFlag()!=null && objCheckRequest.getPrintMemoStubFlag().equalsIgnoreCase("Y")){
			printMemoOn = "Y";
		}
		return printMemoOn;
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

			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			if(objPaymentDetails.isTxnModified()){
				unEditedFeeChagedTo = objCheckRequest.getPrevChargedTo();
			}
			else {
				unEditedFeeChagedTo = objCheckRequest.getChargedTo();
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return unEditedFeeChagedTo;
	}

	/**
	 * Sets the current_approver_role based on the event_descp...
	 * @param objPaymentDetails
	 * @return
	 */
	public static String getCurrentApprover(PaymentDetailsTO objPaymentDetails)
	{
		String current_approver = null;
		String event_desc = objPaymentDetails.getEventDesc();
		if(event_desc!=null && (event_desc.equalsIgnoreCase(Bus_Rule_Input_Desc.Approve_Confirm) || event_desc.equalsIgnoreCase(Bus_Rule_Input_Desc.Approve_PreConfirm))){
			current_approver = objPaymentDetails.getCurrent_approver_role();
		}
		return current_approver;
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
	 * Maps the Available spending in the BR input...
	 * Internal and ACH:
          The Spending Power attribute passed in the input to business rule evaluation will be mapped as the actual spending power plus the original transaction amount 
          if the Booked In flag indicates that the amount was posted to RTA Pending.
       Checks:
          The Spending Power attribute passed in the input to business rule evaluation will 
          be mapped as the actual spending power plus the original transaction amount and fee (if charged to client) if the Booked In flag indicates that the amount was posted to RTA Pending.
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

			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
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

			if(txnType!=null && txnType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || txnType.startsWith(TxnTypeCode.ACH_TYPE)) {
				if(rtab_spending_pwr!=null && original_txn_amount!=null 
						&& rta_booked_flg!=null && rta_booked_flg.equalsIgnoreCase(RTAActionKeyDesc.RTA_PENDING)){
					availableSpndPower =  rtab_spending_pwr.add(original_txn_amount);
				}
			}
			else if(txnType!=null && txnType.startsWith(ChkReqConstants.CHK)) {
				if(rtab_spending_pwr!=null && original_txn_amount!=null 
						&& rta_booked_flg!=null && rta_booked_flg.equalsIgnoreCase(RTAActionKeyDesc.RTA_PENDING)){
					availableSpndPower =  rtab_spending_pwr.add(original_txn_amount);
					if(objCheckRequest.getChargedTo()!=null && objCheckRequest.getChargedTo().equalsIgnoreCase(ChkReqConstants.CHARGE_TO_CLIENT)){
						BigDecimal fee_charge = objPaymentDetails.getTxn_Fee_Charge();
						if(fee_charge!=null){
							availableSpndPower =  rtab_spending_pwr.add(original_txn_amount).add(fee_charge);
						}
					}
				}
			} 
		}
		catch (Exception exception) {
			throw exception;
		}
		return availableSpndPower;
	}
}
