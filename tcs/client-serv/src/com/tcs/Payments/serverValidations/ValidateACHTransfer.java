package com.tcs.Payments.serverValidations;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import com.tcs.Payments.DateUtilities.DateFunctions;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.Message;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.formbean.ExternalTransferForm;
import com.tcs.ebw.serverside.services.channelPaymentServices.WSDefaultInputsMap;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ValidateACHTransfer
{
	/**
	 * Server side validation of the External Transfer Form ...
	 * @throws Exception 
	 */

	public String validateACHTransferFrm(ExternalTransferForm objExtTransferForm) throws Exception
	{
		ServiceContext context = new ServiceContext();
		StringBuilder validationErrors = new StringBuilder();
		ResourceBundle messages = ResourceBundle.getBundle("ErrMessage");	
		try 
		{
			if(objExtTransferForm.getState()!=null && objExtTransferForm.getState().equalsIgnoreCase("ExternalTransfer_INIT")){
				//Please select a From Account...
				if(objExtTransferForm.getFromAccount()==null || objExtTransferForm.getFromAccount().trim().equalsIgnoreCase("")){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0001",messages.getString("AppErr_0001"));
				}
				//Please select a To Account...
				if(objExtTransferForm.getToAccount()==null || objExtTransferForm.getToAccount().trim().equalsIgnoreCase("")){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0002",messages.getString("AppErr_0002"));
				}
				//From Account and To Account cannot be same. Please try again...
				if(objExtTransferForm.getFromAccount()!=null && objExtTransferForm.getToAccount()!=null && !objExtTransferForm.getFromAccount().trim().equalsIgnoreCase("") && !objExtTransferForm.getToAccount().trim().equalsIgnoreCase("") && objExtTransferForm.getFromAccount().equalsIgnoreCase(objExtTransferForm.getToAccount())){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0003",messages.getString("AppErr_0003"));
				}
			}

			//Invalid Amount...
			String transferAmount = objExtTransferForm.getPaymentamount();
			if (transferAmount!=null && transferAmount.indexOf("$")!=-1){
				transferAmount = transferAmount.replace("$", "");
			}
			if(transferAmount==null || transferAmount.trim().equalsIgnoreCase("") || !(ConvertionUtil.convertToBigDecimal(transferAmount).compareTo(new BigDecimal(0))==1)){
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0004",messages.getString("AppErr_0004"));
			}

			//Minimum Amount..
			String minAmountLimit = WSDefaultInputsMap.getACHTxnMinAmount(Boolean.FALSE,context);
			if(minAmountLimit!=null && !minAmountLimit.trim().equalsIgnoreCase("")){
				BigDecimal minAmount = new BigDecimal((String)minAmountLimit);
				boolean isCheckRequired = bypassMinAmntCheck(objExtTransferForm);
				if(isCheckRequired && transferAmount!=null && !transferAmount.trim().equalsIgnoreCase("") && ConvertionUtil.convertToBigDecimal(transferAmount).compareTo(minAmount)== -1){
					String formattedMess = MessageFormat.format(messages.getString("AppErr_0042"),MSCommonUtils.formatTxnAmount(minAmount));
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0042",formattedMess);
				}
			}

			//Maximum Amount..
			String maxAmountLimit = WSDefaultInputsMap.getACHTxnMaxAmount(Boolean.FALSE,context);
			if(maxAmountLimit!=null && !maxAmountLimit.trim().equalsIgnoreCase("")){
				BigDecimal maxAmount = new BigDecimal((String)maxAmountLimit);
				if(transferAmount!=null && !transferAmount.trim().equalsIgnoreCase("") && ConvertionUtil.convertToBigDecimal(transferAmount).compareTo(maxAmount)==1){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0004",messages.getString("AppErr_0004"));
				}
			}

			//Invalid Initiation Date...
			boolean isDateValid = false;
			boolean isDurationDateValid =false;
			boolean isDateRangeValid =false;
			boolean isDateFlipped =false;
			boolean isDirectDBcall = false;
			if(objExtTransferForm.getInitiationDate()==null || objExtTransferForm.getInitiationDate().trim().equalsIgnoreCase("") || objExtTransferForm.getInitiationDate().equalsIgnoreCase(MSSystemDefaults.DEFAULT_DATE_TXT)){
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0005",messages.getString("AppErr_0005"));
			}
			if(objExtTransferForm.getInitiationDate()!=null && !objExtTransferForm.getInitiationDate().trim().equalsIgnoreCase("") && !objExtTransferForm.getInitiationDate().equalsIgnoreCase(MSSystemDefaults.DEFAULT_DATE_TXT)){
				isDateValid = validateDateObject(objExtTransferForm.getInitiationDate(),objExtTransferForm.getBusinessDate()); // Validates the Date format and check for holiday..
				isDateRangeValid= validateDateRange(objExtTransferForm.getInitiationDate(),objExtTransferForm.getBusinessDate()); //Validates the initiation date range (In years).
				isDateFlipped = checkServerDate(ConvertionUtil.convertToDate(objExtTransferForm.getInitiationDate())); //Validates if the Initiation Date is before the Server date considering time zone.
				if(!isDateValid || !isDateRangeValid || !isDateFlipped){
					if(!isDateValid || !isDateFlipped){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0045",messages.getString("AppErr_0045"));
					}
					if(!isDateRangeValid){
						String maxDateRange = MSCommonUtils.getMaxFutureDaysParams(TxnTypeCode.ACH_TYPE,context,isDirectDBcall);
						String formattedMess = MessageFormat.format(messages.getString("AppErr_0055"), maxDateRange);
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0055",formattedMess);
					}	
				}
				else {
					isDateValid = true;
				}
			}

			//Check if the hidden recurring option is set ....
			if(objExtTransferForm.getFrequencyValue()!=null && objExtTransferForm.getFrequencyValue().equalsIgnoreCase("2"))
			{
				//Recurring Transfer ...
				//Please select the frequency
				if(objExtTransferForm.getFrequencycombo()== null || objExtTransferForm.getFrequencycombo().trim().equalsIgnoreCase("")){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0008",messages.getString("AppErr_0008"));
				}

				if (objExtTransferForm.getDurationValue()!=null && objExtTransferForm.getDurationValue().equalsIgnoreCase("1")){
					// Until I cancel 
				}
				else if (objExtTransferForm.getDurationValue()!=null && objExtTransferForm.getDurationValue().equalsIgnoreCase("2")){
					//Until selected date...
					//Checking if the date string is having the value (MM/DD/YYYY)
					String durationEndDate = objExtTransferForm.getDurationenddate();
					if (durationEndDate!=null && durationEndDate.indexOf(MSSystemDefaults.DEFAULT_DATE_TXT)!=-1) 
						durationEndDate = durationEndDate.replace(MSSystemDefaults.DEFAULT_DATE_TXT, "");

					if(durationEndDate==null || durationEndDate.trim().equalsIgnoreCase("")){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0009",messages.getString("AppErr_0009"));
					}
					else if(durationEndDate!=null){
						isDurationDateValid = validateDateObject(durationEndDate,objExtTransferForm.getBusinessDate());
						if(!isDurationDateValid){
							context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0012",messages.getString("AppErr_0012"));
						}
						else{
							//'Until a selected date' must occur after the initiation date.
							if(isDateValid){
								Date payDate = ConvertionUtil.convertToDate(objExtTransferForm.getInitiationDate());
								Date durationEndDt = ConvertionUtil.convertToDate(objExtTransferForm.getDurationenddate());
								if(payDate!=null && durationEndDt!=null && durationEndDt.before(payDate)){
									context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0036",messages.getString("AppErr_0036"));
								}
							}
						}
					}
				}
				else if (objExtTransferForm.getDurationValue()!=null && objExtTransferForm.getDurationValue().equalsIgnoreCase("3")){
					//Until dollar limit...
					String untilDollarLimit = objExtTransferForm.getDurationdollarlimit();
					BigDecimal totalDollarLimit = ConvertionUtil.convertToBigDecimal(objExtTransferForm.getTotalDollarlimit());
					BigDecimal accumulatedAmount = null;
					if(totalDollarLimit!=null && (transferAmount!=null && !transferAmount.trim().equalsIgnoreCase(""))){
						accumulatedAmount = ConvertionUtil.convertToBigDecimal(transferAmount).add(totalDollarLimit);
					}

					if (untilDollarLimit!=null && untilDollarLimit.indexOf("$")!=-1){
						untilDollarLimit = untilDollarLimit.replace("$", "");
					}

					//Until dollar limit is not null..
					if(untilDollarLimit==null || untilDollarLimit.trim().equalsIgnoreCase("") || !(ConvertionUtil.convertToBigDecimal(untilDollarLimit).compareTo(new BigDecimal(0))==1)){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0010",messages.getString("AppErr_0010"));
					}

					//Transfer amount should not be greater than the until dollar amount..
					if(transferAmount!=null && untilDollarLimit!=null && !transferAmount.trim().equalsIgnoreCase("") && !untilDollarLimit.trim().equalsIgnoreCase("") 
							&& ConvertionUtil.convertToBigDecimal(transferAmount).compareTo(ConvertionUtil.convertToBigDecimal(untilDollarLimit))==1){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0043",messages.getString("AppErr_0043"));
					}

					//Maximum limit for dollar transfered ...
					String maxDollarLimitStr = WSDefaultInputsMap.getMaxUntilDollarLimit(Boolean.FALSE,context);
					if(maxDollarLimitStr!=null && !maxDollarLimitStr.trim().equalsIgnoreCase("")){
						BigDecimal maxDollarLimit = new BigDecimal((String)maxDollarLimitStr);
						if(untilDollarLimit!=null && !untilDollarLimit.trim().equalsIgnoreCase("") && ConvertionUtil.convertToBigDecimal(untilDollarLimit).compareTo(maxDollarLimit)==1){
							String formattedMess = MessageFormat.format(messages.getString("AppErr_0044"), MSCommonUtils.formatTxnAmount(maxDollarLimit));
							context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0044",formattedMess);
						}
					}

					//Until dollar limit must be greater than the total amount transferred till date including the existing transfer amount (Check will be performed on modification )..
					if(accumulatedAmount!=null && untilDollarLimit!=null 
							&& ConvertionUtil.convertToBigDecimal(untilDollarLimit).compareTo(accumulatedAmount)==-1){
						String formattedMess = MessageFormat.format(messages.getString("AppErr_0052"), MSCommonUtils.formatTxnAmount(totalDollarLimit) , MSCommonUtils.formatTxnAmount(accumulatedAmount));
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0052",formattedMess);
					}
				}
				else if (objExtTransferForm.getDurationValue()!=null && objExtTransferForm.getDurationValue().equalsIgnoreCase("4")){					
					//Until duration no of transfers...
					String untilNoOfTransfers = objExtTransferForm.getDurationNoOfTransfers();
					Double totalTransfers = ConvertionUtil.convertToDouble(objExtTransferForm.getTotalTransfer());

					//Until no of transfers...
					if(untilNoOfTransfers==null || untilNoOfTransfers.trim().equalsIgnoreCase("") || !(ConvertionUtil.convertToBigDecimal(untilNoOfTransfers).compareTo(new BigDecimal(0))==1)){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0011",messages.getString("AppErr_0011"));
					}

					//Maximum limit for txns transfered ...
					Double maxTransfers = new Double(999.00);
					if(untilNoOfTransfers!=null && !untilNoOfTransfers.trim().equalsIgnoreCase("") && ConvertionUtil.convertToDouble(untilNoOfTransfers).doubleValue()>maxTransfers.doubleValue()){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0011",messages.getString("AppErr_0011"));
					}

					//Until no of transfers cannot be less than then total transfers executed till date..
					if(totalTransfers!=null && untilNoOfTransfers!=null 
							&& ConvertionUtil.convertToDouble(untilNoOfTransfers).doubleValue() <= totalTransfers.doubleValue()){
						String formattedMess = MessageFormat.format(messages.getString("AppErr_0053"),totalTransfers);
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0053",formattedMess);
					}
				}
				else {
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0050",messages.getString("AppErr_0050"));
				}
			}
			else if(objExtTransferForm.getFrequencyValue()!=null && objExtTransferForm.getFrequencyValue().equalsIgnoreCase("1")){
				//One Time Transfers...
			}
			else{
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0051",messages.getString("AppErr_0051"));
			}

			if(context.getMaxSeverity()== MessageType.CRITICAL)
			{
				System.out.println("The External Transfer Validation Failed");
				ArrayList contextMessages = context.getMessages(); //Get the error messages from the context...
				if(!contextMessages.isEmpty())
				{
					validationErrors.append("Validation Error");
					for(int i=0;i<contextMessages.size();i++){
						Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
						if(objContextMess.getCode()==ChannelsErrorCodes.VALIDATION_ERROR) // Check the message code from the context, add only if its a BUSINESS ERROR
						{
							ArrayList<Object> objValidationErrString= (ArrayList)objContextMess.getArgs();
							validationErrors.append(objValidationErrString.get(0));
							validationErrors.append("#Err_Code#");
							validationErrors.append(objValidationErrString.get(1)); //All the error codes are separated by comma.
							validationErrors.append("#Err_Separator#");
						}
					}
					System.out.println("External error list" +validationErrors);
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return validationErrors.toString();
	}

	/** To validate the date object .
	 * 
	 * @param date
	 * @return
	 */
	public boolean validateDateObject(String formDateStr,String bussDateStr){
		boolean isValidDate = false;
		boolean isholidayDt = true;
		Date formDate = null;
		Date bussDate = null;
		try 
		{
			System.out.println("Entered ACH Form Holiday check ");
			String appDateFormat = PropertyFileReader.getProperty("APP_DATE_FORMAT"); 
			SimpleDateFormat validDateFormat = new SimpleDateFormat(appDateFormat);
			formDate = validDateFormat.parse(formDateStr.trim());
			bussDate = validDateFormat.parse(bussDateStr.trim());
			if(validDateFormat.format(formDate).equals(formDateStr.trim()) && validDateFormat.format(bussDate).equals(bussDateStr.trim())) 
			{
				//Check if the selected date is less than the business date and also holiday check...
				if(formDate!=null && bussDate!=null && !formDate.before(bussDate))
				{
					isholidayDt = checkHolidayDate(formDate);
					if(!isholidayDt){
						isValidDate = true;
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return isValidDate;
	}

	/** To validate the max date range limit for the Initiation date 
	 * 
	 * @param date
	 * @return
	 */
	public boolean validateDateRange(String date,String bussinessdate)
	{
		boolean isValidDateRange = false;
		boolean isDirectDBcall = false;
		Date formDate = null;
		String transferType = TxnTypeCode.ACH_TYPE;
		ServiceContext serviceContext = new ServiceContext();
		try {
			System.out.println("Entered ACH Form DateRange check ");
			String appDateFormat = PropertyFileReader.getProperty("APP_DATE_FORMAT"); 
			SimpleDateFormat validDateFormat = new SimpleDateFormat(appDateFormat);
			formDate = validDateFormat.parse(date.trim());
			if(validDateFormat.format(formDate).equals(date.trim())){
				//Calendar instance for business date...
				Calendar bussDate = Calendar.getInstance();
				bussDate.setTime(ConvertionUtil.convertToDate(bussinessdate.trim()));

				//Calendar instance for payment date...
				Calendar payDate = Calendar.getInstance();
				payDate.setTime(ConvertionUtil.convertToDate(date.trim()));

				isValidDateRange = DateFunctions.checkDateRange(payDate,bussDate,transferType,isDirectDBcall,serviceContext);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return isValidDateRange;
	}

	/**
	 * Validating if the screen date entered is an holiday..
	 * @param formDate
	 * @return
	 * @throws Exception
	 */
	public boolean checkHolidayDate(Date formDate) throws Exception
	{
		boolean isHolidayDt = true;
		boolean isDirectDBcall = false;
		String transferType = TxnTypeCode.ACH_TYPE;
		ServiceContext serviceContext = new ServiceContext();
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(formDate);
			isHolidayDt = DateFunctions.checkHoliday(c,transferType,isDirectDBcall,serviceContext);
		}
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return isHolidayDt;
	}

	/**
	 * Check if the initiation date has passed the server time..
	 * @param intiationDate
	 * @return
	 * @throws Exception
	 */
	public boolean checkServerDate(Date intiationDate) throws Exception 
	{
		String branch_Id = PropertyFileReader.getProperty("OU_ID");
		boolean isDateFlipped = false; // Flag to check if the initiation date entered is before the server date considering the time zone for the branch opted.  

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

		if(!intiationDate.before(currentDate.getTime())){
			isDateFlipped = true;
		}

		System.out.println("The date flip check returns : Current Date : Initiation Date : "+ isDateFlipped + currentDate.getTime() + intiationDate.toString());
		return isDateFlipped;
	}

	/**
	 * Flag to identify the minimum amount...
	 * @param objExtTransferForm
	 * @return
	 */
	public static boolean bypassMinAmntCheck(ExternalTransferForm objExtTransferForm)
	{
		boolean isCheckReq = true;
		if(objExtTransferForm.getFrequencyValue()!=null && objExtTransferForm.getFrequencyValue().equalsIgnoreCase("2"))
		{
			if (objExtTransferForm.getDurationValue()!=null && objExtTransferForm.getDurationValue().equalsIgnoreCase("3"))
			{
				//Until dollar limit...
				String transferAmount = objExtTransferForm.getPaymentamount();
				String untilDollarLimit = objExtTransferForm.getDurationdollarlimit();
				BigDecimal totalDollarLimit = ConvertionUtil.convertToBigDecimal(objExtTransferForm.getTotalDollarlimit());
				BigDecimal accumulatedAmount = null;
				if(totalDollarLimit!=null && (transferAmount!=null && !transferAmount.trim().equalsIgnoreCase(""))){
					accumulatedAmount = ConvertionUtil.convertToBigDecimal(transferAmount).add(totalDollarLimit);
				}

				//Until dollar limit must be greater than the total amount transferred till date including the existing transfer amount (Check will be performed on modification )..
				if(accumulatedAmount!=null && untilDollarLimit!=null 
						&& ConvertionUtil.convertToBigDecimal(untilDollarLimit).compareTo(accumulatedAmount)== 0){
					isCheckReq = false;
				}
			}	
		}
		return isCheckReq;
	}
}
