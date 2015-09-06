package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.DsPayeeRefLogTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-07-2011        3               CR 262
 * **********************************************************
 */
public class CreateAccountLogService extends DatabaseService
{
	/**
	 * Create logging for external account..
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 * @throws SQLException
	 */
	public void setExtAccountDetailsLog(int paymentStatusFlag,String loggingAction,HashMap txnDetails,ServiceContext serviceContext)throws Exception,SQLException 
	{
		EBWLogger.trace(this, "Setting the Account Log Details");
		try
		{
			//Payment details..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External AccountDetails..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//DsPayeeRef mapping details...
			DsPayeeRefLogTO dsPayeeRefLog = new DsPayeeRefLogTO();
			dsPayeeRefLog.setCpypayeeid(objExternalAccDetails.getCpypayeeid());
			dsPayeeRefLog.setCpyboname1(objExternalAccDetails.getCpyboname1());
			dsPayeeRefLog.setCpyacctype(objExternalAccDetails.getCpyacctype());
			dsPayeeRefLog.setCpyaccnum(objExternalAccDetails.getCpyaccnum());
			dsPayeeRefLog.setCpypayeename1(objExternalAccDetails.getNick_name());
			dsPayeeRefLog.setCpybankcode(objExternalAccDetails.getCpybankcode());
			dsPayeeRefLog.setAccount_form(objExternalAccDetails.getAccount_form());
			dsPayeeRefLog.setAccount_owner(objExternalAccDetails.getAccount_owner());
			dsPayeeRefLog.setCpystatus(Integer.toString(paymentStatusFlag));
			dsPayeeRefLog.setVoided_check(objExternalAccDetails.getVoided_check());
			dsPayeeRefLog.setSign_up_mode(objExternalAccDetails.getSign_up_mode());
			dsPayeeRefLog.setLife_user_id(objExternalAccDetails.getLife_user_id());
			dsPayeeRefLog.setKey_client(objExternalAccDetails.getKey_client());
			dsPayeeRefLog.setKey_link(objExternalAccDetails.getKey_link());
			dsPayeeRefLog.setFa_id(new Double(1D));
			dsPayeeRefLog.setBranch_id(new Double(1D));
			dsPayeeRefLog.setDeleteflag("N");
			dsPayeeRefLog.setCreated_modified_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			dsPayeeRefLog.setLast_action(loggingAction);
			dsPayeeRefLog.setUsercomments(objPaymentDetails.getCreated_by_comments());
			dsPayeeRefLog.setOfac_case_id(objExternalAccDetails.getOfac_case_id());

			//Update System details...
			updateSystemDetails(txnDetails,dsPayeeRefLog);

			//StatementId and Object mapping for the execute query...
			String[] accountLogStmntId={"setExtAccountLog"};
			Object[] accountLogTOobj={dsPayeeRefLog};
			Boolean isTxnCommitReq = Boolean.TRUE;

			EBWLogger.logDebug(this,"Executing Query : "+accountLogStmntId[0]);
			EBWLogger.logDebug(this,"Input parameters to be mapped : "+accountLogTOobj[0]);
			execute(accountLogStmntId[0], accountLogTOobj[0], isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+accountLogStmntId[0]);
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
	 * Setting the UserDetails to the MM System in case of "System Rejected"
	 * @param dsPayTxnsLog
	 * @throws Exception 
	 */
	public void updateSystemDetails(HashMap txnDetails,DsPayeeRefLogTO dsPayeeRefLog) throws Exception
	{
		try 
		{
			//Getting the payment details ....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			dsPayeeRefLog.setCreated_modified_by_emp_id(objPaymentDetails.getMMSystemDesc());
			dsPayeeRefLog.setCreated_modified_by_role(objPaymentDetails.getMMSystemDesc());
			dsPayeeRefLog.setCreated_modified_by_id(objPaymentDetails.getMMSystemDesc());
		} 
		catch (Exception exception) {
			throw exception;
		}
	}
}
