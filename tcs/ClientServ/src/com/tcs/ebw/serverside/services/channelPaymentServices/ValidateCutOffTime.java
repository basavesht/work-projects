package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TimeZone;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
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
public class ValidateCutOffTime extends DatabaseService{

	/**
	 * Checking the Transfer Cut Off time and returns the warning..
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 * @throws SQLException
	 */
	public void checkCutOffTime_Info(HashMap txnDetails,ServiceContext serviceContext) throws Exception,SQLException
	{
		EBWLogger.trace(this, "Updating the input details in case the cut off time exceeded..");
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String branchTimeZoneId = "";
			Date businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());
			String transferType = objPaymentDetails.getTransfer_Type();

			//Setting the Repeat(Frequency) value...
			String repeatValue = "NBDT";
			if(objPaymentDetails!=null){
				String frequencyValue = objPaymentDetails.getFrequency_DurationDesc();
				if(frequencyValue!=null && (frequencyValue.equalsIgnoreCase("FBD") || frequencyValue.equalsIgnoreCase("LBD"))){
					repeatValue = frequencyValue;
				}
			}

			//Get Branch ID Time Zone and set the server time to the same...
			String branch_Id = objPaymentDetails.getMsBranchId();
			branchTimeZoneId = PropertyFileReader.getPropertyKeyValue("TimeZones",branch_Id);
			Calendar serverTime = Calendar.getInstance();
			if(branchTimeZoneId!=null && !branchTimeZoneId.trim().equalsIgnoreCase("")){
				serverTime.setTimeZone(TimeZone.getTimeZone(branchTimeZoneId));
			}

			//Current Server calendar instance...
			Calendar currentTime = Calendar.getInstance();   
			currentTime.set(Calendar.YEAR, serverTime.get(Calendar.YEAR));   
			currentTime.set(Calendar.MONTH, serverTime.get(Calendar.MONTH));   
			currentTime.set(Calendar.DAY_OF_MONTH, serverTime.get(Calendar.DAY_OF_MONTH));  
			currentTime.set(Calendar.HOUR_OF_DAY, serverTime.get(Calendar.HOUR_OF_DAY)); 
			currentTime.set(Calendar.MINUTE, serverTime.get(Calendar.MINUTE));   
			currentTime.set(Calendar.SECOND, serverTime.get(Calendar.SECOND));   

			//Cut Off time calendar instance...
			Calendar cutOffTime = getCutOffTime(txnDetails, serviceContext);

			//Check if the Cut Off Time is exceeded ..
			if(currentTime.after(cutOffTime))
			{
				EBWLogger.logDebug(this,"Cut Off Time check failed , transaction will be processed on next business day");
				Date nextTransactionDate = MSCommonUtils.evaluateNextTxnDate(repeatValue,businessDate,businessDate,transferType,Boolean.TRUE,serviceContext);
				Date estimatedArrDate = new Date();
				if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
					estimatedArrDate= nextTransactionDate; 
				}
				else{
					estimatedArrDate = MSCommonUtils.calculateEstArrivalDate(nextTransactionDate,transferType,Boolean.TRUE,serviceContext); 
				}

				//Updating the payment details information...
				objPaymentDetails.setRequestedDate(ConvertionUtil.convertToString(nextTransactionDate));
				objPaymentDetails.setEstimatedArrivalDate(ConvertionUtil.convertToString(estimatedArrDate));

				//Add the information in the server context for cut off time check...
				Object[] objCutOffTime = {new Date(cutOffTime.getTimeInMillis())};
				String cutOffTimeCode = "CutOffTime_Err001";
				ResourceBundle messages = ResourceBundle.getBundle("EBWErrorCodes");	
				String cutOffTimeMess = MessageFormat.format(messages.getString(cutOffTimeCode), objCutOffTime);
				serviceContext.addMessage(MessageType.INFORMATION,ChannelsErrorCodes.CUT_OFF_TIME_EXCEEDED,cutOffTimeMess);
			}		
		} 
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
		EBWLogger.trace(this, "Finished getCutOffTime method in ValidateCutOffTime class");
	}


	/**
	 * Checking the Transfer Cut Off time and returns as hard error..
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 * @throws SQLException
	 */
	public void checkCutOffTime_Error(HashMap txnDetails,ServiceContext serviceContext) throws Exception,SQLException
	{
		EBWLogger.trace(this, "Updating the input details in case the cut off time exceeded..");
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String branchTimeZoneId = "";

			//Get Branch ID Time Zone and set the server time to the same...
			String branch_Id = objPaymentDetails.getMsBranchId();
			branchTimeZoneId = PropertyFileReader.getPropertyKeyValue("TimeZones",branch_Id);
			Calendar serverTime = Calendar.getInstance();
			if(branchTimeZoneId!=null && !branchTimeZoneId.trim().equalsIgnoreCase("")){
				serverTime.setTimeZone(TimeZone.getTimeZone(branchTimeZoneId));
			}

			//Current Server calendar instance...
			Calendar currentTime = Calendar.getInstance();   
			currentTime.set(Calendar.YEAR, serverTime.get(Calendar.YEAR));   
			currentTime.set(Calendar.MONTH, serverTime.get(Calendar.MONTH));   
			currentTime.set(Calendar.DAY_OF_MONTH, serverTime.get(Calendar.DAY_OF_MONTH));  
			currentTime.set(Calendar.HOUR_OF_DAY, serverTime.get(Calendar.HOUR_OF_DAY)); 
			currentTime.set(Calendar.MINUTE, serverTime.get(Calendar.MINUTE));   
			currentTime.set(Calendar.SECOND, serverTime.get(Calendar.SECOND));   

			//Cut Off time calendar instance...
			Calendar cutOffTime = getCutOffTime(txnDetails, serviceContext);

			//Check if the Cut Off Time is exceeded ..
			if(currentTime.after(cutOffTime))
			{
				//Add the information in the server context for cut off time check...
				Object[] objCutOffTime = {new Date(cutOffTime.getTimeInMillis())};
				String cutOffTimeCode = "CutOffTime_Err002";
				ResourceBundle messages = ResourceBundle.getBundle("EBWErrorCodes");	
				String cutOffTimeMess = MessageFormat.format(messages.getString(cutOffTimeCode), objCutOffTime);
				serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.CUT_OFF_TIME_EXCEEDED,cutOffTimeMess);
			}		
		} 
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
		EBWLogger.trace(this, "Finished getCutOffTime method in ValidateCutOffTime class");
	}

	/**
	 * Returns the calendar instance of the cut off time...
	 * @param txnDetails
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public Calendar getCutOffTime(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Updating the input details in case the cut off time exceeded..");
		Calendar cutOffTime = Calendar.getInstance();   
		Calendar serverTime = Calendar.getInstance();
		try 
		{
			//Payment details mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Data extraction...
			String branchTimeZoneId = "";
			String branch_Id = objPaymentDetails.getMsBranchId();

			//Get the cut off time depending on transfer type directly from the DB...
			String cutOffTimeStrg = MSCommonUtils.getCutOffTimeParams(txnDetails,serviceContext);
			if(cutOffTimeStrg!=null && !cutOffTimeStrg.trim().equalsIgnoreCase("")) 
			{
				//The following check is done since the External Transfer View is having END_TIME data type as Integer ..
				do{
					if(cutOffTimeStrg.length()>=6){
						break;
					}
					else {
						cutOffTimeStrg="0"+cutOffTimeStrg;
					}
				}while(cutOffTimeStrg.length()!=6);

				//Cut-Off Time mappings..
				int cutOffHrs = Integer.parseInt(cutOffTimeStrg.substring(0, 2));
				int cutOffMin = Integer.parseInt(cutOffTimeStrg.substring(2, 4));
				int cutOffSec = Integer.parseInt(cutOffTimeStrg.substring(4, 6));

				//Get Branch ID Time Zone...
				branchTimeZoneId = PropertyFileReader.getPropertyKeyValue("TimeZones",branch_Id);
				if(branchTimeZoneId!=null && !branchTimeZoneId.trim().equalsIgnoreCase("")){
					serverTime.setTimeZone(TimeZone.getTimeZone(branchTimeZoneId));
				}

				//Setting the Cut-Off time ...
				cutOffTime.set(Calendar.YEAR, serverTime.get(Calendar.YEAR));   
				cutOffTime.set(Calendar.MONTH, serverTime.get(Calendar.MONTH));   
				cutOffTime.set(Calendar.DAY_OF_MONTH, serverTime.get(Calendar.DAY_OF_MONTH));   
				cutOffTime.set(Calendar.HOUR_OF_DAY,cutOffHrs);
				cutOffTime.set(Calendar.MINUTE,cutOffMin);
				cutOffTime.set(Calendar.SECOND,cutOffSec);
			}
			else {
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
			}
		} 
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
		return cutOffTime;
	}
}
