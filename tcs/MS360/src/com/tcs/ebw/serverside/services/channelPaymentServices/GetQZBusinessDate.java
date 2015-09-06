/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsConfigDetailsOut;
import com.tcs.ebw.payments.transferobject.DsConfigDetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 * **********************************************************
 */
public class GetQZBusinessDate extends DatabaseService{

	/**
	 * Update the business date in the payment input details TO..
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void getQzBusinessDate(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Getting the quartz business date..");
		try
		{
			//Mapping Payment attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			String transferType = objPaymentDetails.getTransfer_Type();
			Date transferDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
			Date prevBusinessDate = null;
			if(objPaymentDetails.getBusiness_Date()!=null){
				prevBusinessDate = (Date)ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date()).clone();
			}

			//Mapping the Config Input details...
			DsConfigDetailsTO objDsConfigDetails=new DsConfigDetailsTO();
			objDsConfigDetails.setBranch_code(objPaymentDetails.getMsBranchId());

			//StatementId and Object mapping for the business date calculation..
			String bussDateStmntId = "getCurrentBusinessDate";
			Object objOutput=null;
			Boolean isTxnCommitReq = Boolean.TRUE;
			objOutput = executeQuery(bussDateStmntId,objDsConfigDetails,isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+bussDateStmntId);

			//Mapping details to the payment details transfer object..
			DsConfigDetailsOut objDsConfigDetail = (DsConfigDetailsOut)objOutput;
			Date currentBusinessDate = objDsConfigDetail.getBusiness_date();
			objPaymentDetails.setBusiness_Date(ConvertionUtil.convertToAppDateStr(currentBusinessDate));

			//Transfer Date automatically changed to the next calendar date for internal transfers 24x7 ...
			if(MSCommonUtils.check24x7Access(transferType)) {
				if(prevBusinessDate!=null && currentBusinessDate!=null && transferDate!=null) {
					if(prevBusinessDate.compareTo(currentBusinessDate) < 0 && transferDate.compareTo(prevBusinessDate)==0){
						objPaymentDetails.setRequestedDate(ConvertionUtil.convertToAppDateStr(currentBusinessDate));
						objPaymentDetails.setEstimatedArrivalDate(ConvertionUtil.convertToAppDateStr(currentBusinessDate));
					}
				}
			}
		}
		catch(SQLException sqlexception) {
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 * @throws Exception
	 */
	public Object getCurrentBusinessDate(String[] stmntId, Object[] toObjects,Boolean boolean1) throws Exception 
	{
		EBWLogger.trace(this, "Starting getCurrentBusinessDate method in GetQZBusinessDate class");
		Object objOutput=null;
		boolean1 = Boolean.TRUE;
		DsConfigDetailsTO objDsConfigDetails=new DsConfigDetailsTO();
		try
		{
			for( int a=0;a<toObjects.length;a++){
				if(toObjects[a] instanceof DsConfigDetailsTO){
					objDsConfigDetails=(DsConfigDetailsTO)toObjects[a];
				}
			}
			objOutput = executeQuery(stmntId[0],objDsConfigDetails,boolean1);
			EBWLogger.logDebug(this, "Executed getCurrentBusinessDate");
			EBWLogger.trace(this, "Finished with getCurrentBusinessDate method of GetQZBusinessDate class");
		}
		catch(SQLException sqlexception) {
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
		return objOutput;
	}
}

