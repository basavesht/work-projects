package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TimeZone;

import com.tcs.Payments.DateUtilities.DateFunctions;
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
 *
 * **********************************************************
 */
public class CheckHoliday extends DatabaseService{

	/**
	 * Validating if the server date is an holiday..
	 * @param formDate
	 * @return
	 * @throws Exception
	 */
	public void checkHolidayDate(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		boolean isServerDtHoliday = true;
		boolean isQzDtHoliday = true;
		boolean isDirectDBcall = true;
		try
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			Date businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());
			String transferType = objPaymentDetails.getTransfer_Type();
			String branch_Id = PropertyFileReader.getProperty("OU_ID");

			//Get Branch ID Time Zone...
			String branchTimeZoneId = PropertyFileReader.getPropertyKeyValue("TimeZones",branch_Id);

			// Following code will create a new date Instance and set the hours , minutes , seconds for the calendar object instance..
			Calendar serverTime = Calendar.getInstance();
			if(branchTimeZoneId!=null && !branchTimeZoneId.trim().equalsIgnoreCase("")){
				serverTime.setTimeZone(TimeZone.getTimeZone(branchTimeZoneId));
			}

			//Current Date in the MS Branch as described by OU_ID . 
			Calendar currentDate = Calendar.getInstance();   
			currentDate.set(Calendar.YEAR, serverTime.get(Calendar.YEAR));   
			currentDate.set(Calendar.MONTH, serverTime.get(Calendar.MONTH));   
			currentDate.set(Calendar.DAY_OF_MONTH, serverTime.get(Calendar.DAY_OF_MONTH));  
			currentDate.set(Calendar.HOUR_OF_DAY,0); 
			currentDate.set(Calendar.MINUTE,0);   
			currentDate.set(Calendar.SECOND,0);   
			currentDate.set(Calendar.MILLISECOND,0); 

			//Business date in the MS ....
			Calendar bussDate = Calendar.getInstance();
			bussDate.setTime(businessDate);

			isServerDtHoliday = DateFunctions.checkHoliday(currentDate,transferType,isDirectDBcall,serviceContext);
			isQzDtHoliday = DateFunctions.checkHoliday(bussDate,transferType,isDirectDBcall,serviceContext);

			//Check if the Cut Off Time is exceeded ..
			if(isServerDtHoliday || isQzDtHoliday)
			{
				EBWLogger.logDebug(this,"Approval/Reject date is the Holiday");

				//Add the information in the server context for holiday check...
				String holidayCheckCode = "BussHoliday_Err001";
				if(objPaymentDetails.isTxnApproved() || objPaymentDetails.isTxnRejected() || objPaymentDetails.isTxnCancelled()){
					holidayCheckCode = "BussHoliday_Err002";
				}
				ResourceBundle messages = ResourceBundle.getBundle("EBWErrorCodes");	
				serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.BUSINESS_HOLIDAY,messages.getString(holidayCheckCode));
			}
		}
		catch(Exception exception){
			throw exception;
		}
	}
}
