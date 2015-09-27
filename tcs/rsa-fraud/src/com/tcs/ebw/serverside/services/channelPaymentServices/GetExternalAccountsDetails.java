/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.ExtAcntPlatingLabelDesc;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.VersionChkInDtls;
import com.tcs.ebw.payments.transferobject.VersionChkOutDtls;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class GetExternalAccountsDetails extends DatabaseService {

	/**
	 * Get external account details...
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void getExternalPayeeDetails(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Starting getExternalPayeeDetails method in GetExternalAccountsDetails class");
		try
		{
			//Payment Detail attributes...
			Boolean isTxnCommitReq = Boolean.TRUE;
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Input mappings for the transaction details....
			DsExtPayeeDetailsTO dsExtPayeeDetailsTO = new DsExtPayeeDetailsTO();
			dsExtPayeeDetailsTO.setExternalPayeeId(objPaymentDetails.getExtAccount_RefId());

			//StatementId and Object mapping for getting the external account details..
			String externalAccsstmntId="getExternalPayeeDetails";
			Object externalAccsTOObj=dsExtPayeeDetailsTO;
			Object objOutput=null;

			//Service call...
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO=new DsExtPayeeDetailsOutTO();
			objOutput = executeQuery(externalAccsstmntId,externalAccsTOObj,isTxnCommitReq);
			objDsExtPayeeDetailsOutTO = (DsExtPayeeDetailsOutTO)objOutput;
			EBWLogger.logDebug(this,"Execution Completed.... "+externalAccsstmntId);

			//Mapping the external account details in the PaymentDetails Transfer Object...
			objPaymentDetails.setExtAcc_NickName(objDsExtPayeeDetailsOutTO.getCpypayeename1());
			objPaymentDetails.setExtAccount_Number(objDsExtPayeeDetailsOutTO.getCpyaccnum());
			objPaymentDetails.setExtAccount_RefId(objDsExtPayeeDetailsOutTO.getCpypayeeid());

			//Mapping details to the payment details transfer object..
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);
		}
		catch(SQLException sqlexception){
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * Setting the external account plating attributes details..
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void getExtAcntPlatingInfo(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Starting getExternalPayeeDetails method in GetExternalAccountsDetails class");
		try
		{
			//Payment Detail attributes...
			Boolean isTxnCommitReq = Boolean.TRUE;
			ArrayList acntPlatindInfo= new ArrayList();
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Input mappings for the transaction details....
			DsExtPayeeDetailsTO dsExtPayeeDetailsTO = new DsExtPayeeDetailsTO();
			dsExtPayeeDetailsTO.setExternalPayeeId(objPaymentDetails.getExtAccount_RefId());

			//StatementId and Object mapping for getting the external account details..
			String externalAccsstmntId="getExternalPayeeDetails";
			Object externalAccsTOObj=dsExtPayeeDetailsTO;
			Object objOutput=null;
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO=new DsExtPayeeDetailsOutTO();

			//Service call...
			objOutput = executeQuery(externalAccsstmntId,externalAccsTOObj,isTxnCommitReq);
			objDsExtPayeeDetailsOutTO = (DsExtPayeeDetailsOutTO)objOutput;
			EBWLogger.logDebug(this,"Execution Completed.... "+externalAccsstmntId);

			//Extracting the attribute value for attribute details...
			String payeeBankName=(String)objDsExtPayeeDetailsOutTO.getCpyboname1();
			String payeeAccNumber=(String)objDsExtPayeeDetailsOutTO.getCpyaccnum();
			if(payeeAccNumber.length()>4)
			{
				int dispStartIndex = payeeAccNumber.length()-4;
				payeeAccNumber = payeeAccNumber.substring(dispStartIndex);
			}
			String payeeDetailsValue = payeeBankName+" "+"(XX-"+payeeAccNumber+")";

			// Setting the Account plating nickname in the HashMap...
			HashMap acntNameMap = new HashMap();
			acntNameMap.put("AttributeName", ExtAcntPlatingLabelDesc.NAME.trim());
			acntNameMap.put("AttributeValue", objDsExtPayeeDetailsOutTO.getNick_name().trim());
			acntNameMap.put("AttributeSequence", "L1");
			acntPlatindInfo.add(acntNameMap);

			// Setting the Account plating Bank account details in the HashMap...
			HashMap acntDetailsMap = new HashMap();
			acntDetailsMap.put("AttributeName", ExtAcntPlatingLabelDesc.DETAILS.trim());
			acntDetailsMap.put("AttributeValue", payeeDetailsValue.trim());
			acntDetailsMap.put("AttributeSequence", "R1");
			acntPlatindInfo.add(acntDetailsMap);

			// Setting the Account plating owner details in the HashMap...
			if(objDsExtPayeeDetailsOutTO.getAccount_owner()!=null && !objDsExtPayeeDetailsOutTO.getAccount_owner().trim().equalsIgnoreCase("")){
				HashMap acntOnwerMap = new HashMap();
				acntOnwerMap.put("AttributeName", ExtAcntPlatingLabelDesc.ACNT_OWNER.trim());
				acntOnwerMap.put("AttributeValue", objDsExtPayeeDetailsOutTO.getAccount_owner().trim());
				acntOnwerMap.put("AttributeSequence", "L2");
				acntPlatindInfo.add(acntOnwerMap);
			}

			//Mapping details to the payment details transfer object..
			txnDetails.put("ExtAcntPlatingOutputDetails",acntPlatindInfo);
		}
		catch(SQLException sqlexception){
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * Executes the query for getting the External Account version details..
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public void getExtAccountVersion(PaymentDetailsTO objPaymentDetails,ServiceContext context) throws Exception
	{		
		EBWLogger.trace(this, "Setting the input details for getting the Account Version details ..");
		try 
		{
			Boolean isTxnCommitReq = Boolean.TRUE;
			String fetchExtAcntVerStmntId = "fetchAccountVersion";
			Object output = null;

			//Input Mappings..
			VersionChkInDtls objVersionChkInDtls = new VersionChkInDtls();
			objVersionChkInDtls.setTxnReferenceId(objPaymentDetails.getTransactionId());

			//Service call....
			output = executeQuery(fetchExtAcntVerStmntId,objVersionChkInDtls,isTxnCommitReq);
			VersionChkOutDtls objVersionChkOutDtls = (VersionChkOutDtls)output;

			//Output Mappings..
			objPaymentDetails.setExtAccount_Version(ConvertionUtil.convertToString(objVersionChkOutDtls.getVersionnum()));
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
}
