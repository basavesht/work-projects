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
import com.tcs.bancs.ms360.integration.MMContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.formbean.InternalPreConfirmForm;
import com.tcs.ebw.payments.formbean.InternalTransferForm;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.services.channelPaymentServices.WSDefaultInputsMap;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *  224703			31-05-2011      P3				 Server-side check.  
 *  224703          25-09-2011      P3               PLA
 * **********************************************************
 */
public class ValidateINTTransfer 
{
	/**
	 * Server side validation of the Internal Transfer Form ...
	 * @throws Exception 
	 */

	public String validateINTTransferFrm(InternalTransferForm objIntTransferForm) throws Exception
	{
		ServiceContext context = new ServiceContext();
		StringBuilder validationErrors = new StringBuilder();
		ResourceBundle messages = ResourceBundle.getBundle("ErrMessage");
		try 
		{
			if(objIntTransferForm.getState()!=null && objIntTransferForm.getState().equalsIgnoreCase("InternalTransfer_INIT"))
			{
				//Please select a From Account...
				if(objIntTransferForm.getFromAccount()==null || objIntTransferForm.getFromAccount().trim().equalsIgnoreCase("")){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0001",messages.getString("AppErr_0001"));
				}

				//Please select a To Account...
				if(objIntTransferForm.getToAccount()==null && objIntTransferForm.getOtherMSKeyAccount()==null){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0002",messages.getString("AppErr_0002"));
				}
				else if((objIntTransferForm.getToAccount()!=null && objIntTransferForm.getToAccount().trim().equalsIgnoreCase("")) 
						&& (objIntTransferForm.getOtherMSKeyAccount()!=null && objIntTransferForm.getOtherMSKeyAccount().trim().equalsIgnoreCase(""))){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0002",messages.getString("AppErr_0002"));
				}

				//From Account and To Account cannot be same. Please try again...
				if(objIntTransferForm.getFromAccount()!=null && objIntTransferForm.getToAccount()!=null 
						&& !objIntTransferForm.getFromAccount().trim().equalsIgnoreCase("") && !objIntTransferForm.getToAccount().trim().equalsIgnoreCase("") 
						&& objIntTransferForm.getFromAccount().equalsIgnoreCase(objIntTransferForm.getToAccount())){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0003",messages.getString("AppErr_0003"));
				}

				//From Account and Other MS Account cannot be same 
				if(objIntTransferForm.getFromAccount()!=null && objIntTransferForm.getOtherMSKeyAccount()!=null 
						&& !objIntTransferForm.getFromAccount().trim().equalsIgnoreCase("") && !objIntTransferForm.getOtherMSKeyAccount().trim().equalsIgnoreCase("") 
						&& objIntTransferForm.getFromAccount().equalsIgnoreCase(objIntTransferForm.getOtherMSKeyAccount())){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0003",messages.getString("AppErr_0003"));
				}
			}

			//Set the transfer type ...
			String transferType = TxnTypeCode.INTERNAL;
			if(objIntTransferForm.getState()!=null && objIntTransferForm.getState().equalsIgnoreCase("InternalTransfer_INIT")) {
				if(objIntTransferForm.getToAccount()!=null && !objIntTransferForm.getToAccount().trim().equalsIgnoreCase("")){
					if(objIntTransferForm.getToAccount().trim().startsWith(MSSystemDefaults.LOAN_ACC_IND_REGX)){
						transferType = TxnTypeCode.PORTFOLIO_LOAN;
					}
				}
			}
			else {
				transferType = objIntTransferForm.getTransactionType();
			}

			//Invalid Amount...
			String transferAmount = objIntTransferForm.getPaymentamount();
			if (transferAmount!=null && transferAmount.indexOf("$")!=-1){
				transferAmount = transferAmount.replace("$", "");
			}
			if(transferAmount==null || transferAmount.trim().equalsIgnoreCase("") || !(ConvertionUtil.convertToBigDecimal(transferAmount).compareTo(new BigDecimal(0))==1)){
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0004",messages.getString("AppErr_0004"));
			}

			//Minimum Amount..
			String minAmountLimit = WSDefaultInputsMap.getIntTxnMinAmount(context);
			if(minAmountLimit!=null && !minAmountLimit.trim().equalsIgnoreCase("")){
				BigDecimal minAmount = new BigDecimal((String)minAmountLimit);
				boolean isCheckRequired = bypassMinAmntCheck(objIntTransferForm);
				if(isCheckRequired && transferAmount!=null && !transferAmount.trim().equalsIgnoreCase("") && ConvertionUtil.convertToBigDecimal(transferAmount).compareTo(minAmount)==-1){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0004",messages.getString("AppErr_0004"));
				}
			}

			//Maximum Amount..
			String maxAmountLimit = WSDefaultInputsMap.getIntTxnMaxAmount(context);
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
			if(objIntTransferForm.getInitiationDate()==null || objIntTransferForm.getInitiationDate().trim().equalsIgnoreCase("") || objIntTransferForm.getInitiationDate().equalsIgnoreCase(MSSystemDefaults.DEFAULT_DATE_TXT)){
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0005",messages.getString("AppErr_0005"));
			}
			if(objIntTransferForm.getInitiationDate()!=null && !objIntTransferForm.getInitiationDate().trim().equalsIgnoreCase("") && !objIntTransferForm.getInitiationDate().equalsIgnoreCase(MSSystemDefaults.DEFAULT_DATE_TXT)){
				isDateValid = validateDateObject(objIntTransferForm.getInitiationDate(),objIntTransferForm.getBusinessDate(),transferType); // Validates the Date format and check for holiday..
				isDateRangeValid= validateDateRange(objIntTransferForm.getInitiationDate(),objIntTransferForm.getBusinessDate(),transferType); //Validates the initiation date range (In years).
				isDateFlipped = checkServerDate(ConvertionUtil.convertToDate(objIntTransferForm.getInitiationDate())); //Validates if the Initiation Date is before the Server date considering time zone.
				if(!isDateValid || !isDateRangeValid || !isDateFlipped){
					if(!isDateValid || !isDateFlipped){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0045",messages.getString("AppErr_0045"));
					}
					if(!isDateRangeValid){
						String maxDateRange = MSCommonUtils.getMaxFutureDaysParams(transferType,context);
						String formattedMess = MessageFormat.format(messages.getString("AppErr_0055"), maxDateRange);
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0055",formattedMess);
					}
				}
				else {
					isDateValid = true;
				}
			}

			//Check if the hidden recurring option is set ....
			if(objIntTransferForm.getFrequencyValue()!=null && objIntTransferForm.getFrequencyValue().equalsIgnoreCase("2"))
			{
				//Recurring Transfer ...
				//Please select the frequency
				if(objIntTransferForm.getFrequencycombo()== null || objIntTransferForm.getFrequencycombo().trim().equalsIgnoreCase("")){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0008",messages.getString("AppErr_0008"));
				}
				if (objIntTransferForm.getDurationValue()!=null && objIntTransferForm.getDurationValue().equalsIgnoreCase("1")){
					// Until I cancel 
				}
				else if (objIntTransferForm.getDurationValue()!=null && objIntTransferForm.getDurationValue().equalsIgnoreCase("2")){
					//Until selected date...
					//Checking if the date string is having the value (MM/DD/YYYY)
					String durationEndDate = objIntTransferForm.getDurationenddate();
					if (durationEndDate!=null && durationEndDate.indexOf(MSSystemDefaults.DEFAULT_DATE_TXT)!=-1) 
						durationEndDate = durationEndDate.replace(MSSystemDefaults.DEFAULT_DATE_TXT, "");

					if(durationEndDate==null || durationEndDate.trim().equalsIgnoreCase("")){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0009",messages.getString("AppErr_0009"));
					}
					else if(durationEndDate!=null){
						isDurationDateValid = validateDateObject(durationEndDate,objIntTransferForm.getBusinessDate(),transferType);
						if(!isDurationDateValid){
							context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0012",messages.getString("AppErr_0012"));
						}
						else{
							//'Until a selected date' must occur after the initiation date.
							if(isDateValid){
								Date payDate = ConvertionUtil.convertToDate(objIntTransferForm.getInitiationDate());
								Date durationEndDt = ConvertionUtil.convertToDate(objIntTransferForm.getDurationenddate());
								if(payDate!=null && durationEndDt!=null && durationEndDt.before(payDate)){
									context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0036",messages.getString("AppErr_0036"));
								}
							}
						}
					}
				}
				else if (objIntTransferForm.getDurationValue()!=null && objIntTransferForm.getDurationValue().equalsIgnoreCase("3")){
					//Until dollar limit...
					String untilDollarLimit = objIntTransferForm.getDurationdollarlimit();
					BigDecimal totalDollarLimit = ConvertionUtil.convertToBigDecimal(objIntTransferForm.getTotalDollarlimit());
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
					String maxDollarLimitStr = WSDefaultInputsMap.getMaxUntilDollarLimit(context);
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
						String formattedMess = MessageFormat.format(messages.getString("AppErr_0052"), MSCommonUtils.formatTxnAmount(totalDollarLimit),MSCommonUtils.formatTxnAmount(accumulatedAmount));
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0052",formattedMess);
					}
				}
				else if (objIntTransferForm.getDurationValue()!=null && objIntTransferForm.getDurationValue().equalsIgnoreCase("4")){
					//Until duration no of transfers...
					String untilNoOfTransfers = objIntTransferForm.getDurationNoOfTransfers();
					Double totalTransfers = ConvertionUtil.convertToDouble(objIntTransferForm.getTotalTransfer());

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
			else if(objIntTransferForm.getFrequencyValue()!=null && objIntTransferForm.getFrequencyValue().equalsIgnoreCase("1")){
				//One Time Transfers...
			}
			else{
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0051",messages.getString("AppErr_0051"));
			}

			if(context.getMaxSeverity()== MessageType.CRITICAL)
			{
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
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		} 
		return validationErrors.toString();
	}

	/**
	 * Server side validation of the Internal Transfer Confirm Form ...
	 * @param objInternalPreConfirm
	 * @return
	 * @throws Exception
	 */
	public String validateINTTransferConfirmFrm(InternalPreConfirmForm objInternalPreConfirm) throws Exception
	{
		ServiceContext context = new ServiceContext();
		StringBuilder validationErrors = new StringBuilder();
		ResourceBundle messages = ResourceBundle.getBundle("ErrMessage");
		try 
		{
			//Set the transfer type ...
			String transferType = objInternalPreConfirm.getTransactionType();

			String auth_mode = objInternalPreConfirm.getAuth_mode();
			if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_VERBAL)) 
			{
				//Spoke To ..
				if(objInternalPreConfirm.getSpokeTo() == null || objInternalPreConfirm.getSpokeTo().trim().equalsIgnoreCase("")){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0060",messages.getString("AppErr_0060"));
				}

				//Information Received By...
				if(objInternalPreConfirm.getInformationRecvdBy() == null || objInternalPreConfirm.getInformationRecvdBy().trim().equalsIgnoreCase("")){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0061",messages.getString("AppErr_0061"));
				}

				//Invalid Verbal Authorization Date...
				boolean isDateFormatValid = false;
				boolean isDateValid = false;
				boolean isDateRangeValid = false;
				if(objInternalPreConfirm.getVerbalAuthDate() == null || objInternalPreConfirm.getVerbalAuthDate().trim().equalsIgnoreCase("")){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0063",messages.getString("AppErr_0063"));
				}
				if(objInternalPreConfirm.getVerbalAuthDate()!=null && !objInternalPreConfirm.getVerbalAuthDate().trim().equalsIgnoreCase("") && !objInternalPreConfirm.getVerbalAuthDate().equalsIgnoreCase(MSSystemDefaults.DEFAULT_DATE_TXT))
				{
					isDateFormatValid =  validateVerbalAuthDateFormat(objInternalPreConfirm.getVerbalAuthDate(),objInternalPreConfirm.getBusinessDate()); //Validates the Date format ..
					isDateValid = validateVerbalAuthDateObject(objInternalPreConfirm.getVerbalAuthDate(),objInternalPreConfirm.getBusinessDate()); //Validates the Date  < business date ...
					isDateRangeValid = validateVerbalAuthDateRange(objInternalPreConfirm.getVerbalAuthDate(),objInternalPreConfirm.getBusinessDate(),transferType); //Validates the verbal auth date range (In years).
					if(!isDateValid || !isDateRangeValid){
						if(!isDateFormatValid){
							context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0062",messages.getString("AppErr_0062"));
						}
						else if(!isDateValid){
							context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0064",messages.getString("AppErr_0064"));
						}
						else if(!isDateRangeValid){
							String maxDateRange = MSCommonUtils.getMaxPastVerbalAuthDays(transferType,context);
							String formattedMess = MessageFormat.format(messages.getString("AppErr_0065"), maxDateRange);
							context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0065",formattedMess);
						}
					}
					else {
						isDateValid = true;
					}
				}

				//Client Verification..
				if(objInternalPreConfirm.getClientVerfication() == null || objInternalPreConfirm.getClientVerfication().trim().equalsIgnoreCase("")){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0067",messages.getString("AppErr_0067"));
				}
			}
			else if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_SIGNED)) 
			{
				//Signed To ..
				if(objInternalPreConfirm.getSpokeTo() == null || objInternalPreConfirm.getSpokeTo().trim().equalsIgnoreCase("")){
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.VALIDATION_ERROR,"AppErr_0068",messages.getString("AppErr_0068"));
				}
			}

			if(context.getMaxSeverity()== MessageType.CRITICAL)
			{
				ArrayList contextMessages = context.getMessages(); //Get the error messages from the context...
				if(!contextMessages.isEmpty())
				{
					validationErrors.append("Validation Error");
					for(int i=0;i<contextMessages.size();i++){
						Message objContextMess = (Message)contextMessages.get(i); //Get the Message Object from the serviceContext...
						if(objContextMess.getCode()== ChannelsErrorCodes.VALIDATION_ERROR) // Check the message code from the context, add only if its a BUSINESS ERROR
						{
							ArrayList<Object> objValidationErrString = (ArrayList)objContextMess.getArgs();
							validationErrors.append(objValidationErrString.get(0));
							validationErrors.append("#Err_Code#");
							validationErrors.append(objValidationErrString.get(1)); //All the error codes are separated by comma.
							validationErrors.append("#Err_Separator#");
						}
					}
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
	public boolean validateDateObject(String formDateStr,String bussDateStr, String transferType)
	{
		boolean isValidDate = false;
		boolean isholidayDt = true;
		Date formDate = null;
		Date bussDate = null;
		try 
		{
			String appDateFormat = PropertyFileReader.getProperty("APP_DATE_FORMAT"); 
			SimpleDateFormat validDateFormat = new SimpleDateFormat(appDateFormat);
			formDate = validDateFormat.parse(formDateStr.trim());
			bussDate = validDateFormat.parse(bussDateStr.trim());
			if(validDateFormat.format(formDate).equals(formDateStr.trim()) && validDateFormat.format(bussDate).equals(bussDateStr.trim())) 
			{
				//Check if the selected date is less than the business date and also holiday check...
				if(formDate!=null && bussDate!=null && !formDate.before(bussDate))
				{
					isholidayDt = checkHolidayDate(formDate,transferType);
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
	public boolean validateDateRange(String date,String bussinessdate,String transferType)
	{
		boolean isValidDateRange = false;
		Date formDate = null;
		boolean isDirectDBcall = false;
		ServiceContext serviceContext = new ServiceContext();
		try 
		{
			String appDateFormat = PropertyFileReader.getProperty("APP_DATE_FORMAT"); 
			SimpleDateFormat validDateFormat = new SimpleDateFormat(appDateFormat);
			formDate = validDateFormat.parse(date.trim());
			if(validDateFormat.format(formDate).equals(date.trim()))
			{
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
	 * To validate the Verbal authorization date object .
	 * @param formDateStr
	 * @param bussDateStr
	 * @return
	 */
	public boolean validateVerbalAuthDateFormat(String formDateStr,String bussDateStr)
	{
		boolean isValidDate = false;
		Date formDate = null;
		Date bussDate = null;
		try 
		{
			String appDateFormat = PropertyFileReader.getProperty("APP_DATE_FORMAT"); 
			SimpleDateFormat validDateFormat = new SimpleDateFormat(appDateFormat);
			formDate = validDateFormat.parse(formDateStr.trim());
			bussDate = validDateFormat.parse(bussDateStr.trim());
			if(validDateFormat.format(formDate).equals(formDateStr.trim()) && validDateFormat.format(bussDate).equals(bussDateStr.trim())) {
				isValidDate = true;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return isValidDate;
	}

	/**
	 * To validate the Verbal authorization date object .
	 * @param formDateStr
	 * @param bussDateStr
	 * @return
	 */
	public boolean validateVerbalAuthDateObject(String formDateStr,String bussDateStr)
	{
		boolean isValidDate = false;
		Date formDate = null;
		Date bussDate = null;
		try 
		{
			String appDateFormat = PropertyFileReader.getProperty("APP_DATE_FORMAT"); 
			SimpleDateFormat validDateFormat = new SimpleDateFormat(appDateFormat);
			formDate = validDateFormat.parse(formDateStr.trim());
			bussDate = validDateFormat.parse(bussDateStr.trim());
			if(validDateFormat.format(formDate).equals(formDateStr.trim()) && validDateFormat.format(bussDate).equals(bussDateStr.trim())) 
			{
				//Check if the selected date is after business date ...
				if(formDate!=null && bussDate!=null)
				{
					if(!formDate.after(bussDate)){
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
	public boolean validateVerbalAuthDateRange(String date,String bussinessdate,String transferType)
	{
		boolean isValidDateRange = false;
		Date formDate = null;
		boolean isDirectDBcall = false;
		ServiceContext serviceContext = new ServiceContext();
		try 
		{
			String appDateFormat = PropertyFileReader.getProperty("APP_DATE_FORMAT"); 
			SimpleDateFormat validDateFormat = new SimpleDateFormat(appDateFormat);
			formDate = validDateFormat.parse(date.trim());
			if(validDateFormat.format(formDate).equals(date.trim()))
			{
				//Calendar instance for business date...
				Calendar bussDate = Calendar.getInstance();
				bussDate.setTime(ConvertionUtil.convertToDate(bussinessdate.trim()));

				//Calendar instance for payment date...
				Calendar verbalAuthDate = Calendar.getInstance();
				verbalAuthDate.setTime(ConvertionUtil.convertToDate(date.trim()));

				isValidDateRange = DateFunctions.checkVerbalAuthDateRange(verbalAuthDate,bussDate,transferType,isDirectDBcall,serviceContext);
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
	public boolean checkHolidayDate(Date formDate,String transferType) throws Exception
	{
		boolean isHolidayDt = true;
		boolean isDirectDBcall = false;
		ServiceContext serviceContext = new ServiceContext();
		try 
		{
			//Checking for 24x7 access...
			if(MSCommonUtils.check24x7Access(transferType)){
				return false;
			}

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

		return isDateFlipped;
	}

	/**
	 * Validates if there are statement linked accounts in the enriched context. 
	 * If there are no accounts present in the enriched context , then user dont have the access to the statement linked accounts.... 
	 * @param objMMContextData
	 * @return
	 * @throws Exception 
	 */
	public ServiceContext validatePageAccess(boolean isAnchorAccountReq,UserPrincipal objUserPrincipal,PaymentDetailsTO objPaymentDetails) throws Exception
	{
		ServiceContext serviceContext = new ServiceContext();
		try 
		{
			boolean isActionEligible = false;

			//Single functional role for any event ...
			String screen  = objPaymentDetails.getScreen();
			String action  = objPaymentDetails.getAction();
			String state   = objPaymentDetails.getState();
			String functionalRole="F"+screen+state+action;

			//Multiple functional role for the any single event (E.g Cancel) ..
			ArrayList multipleRoleList = objPaymentDetails.getUserFuncRoleList();

			//Checking the functional role..
			ArrayList userActions = objUserPrincipal.getRoleList();
			if(!userActions.isEmpty() && userActions.contains(functionalRole)){
				isActionEligible = true;
			}
			else if(userActions!=null && !userActions.isEmpty() && multipleRoleList!=null){
				if(!multipleRoleList.isEmpty()){
					for(int i =0;i < multipleRoleList.size();i++){
						if(userActions.contains("F"+(String)multipleRoleList.get(i))){
							isActionEligible = true;
							break;
						}
					}
				}
			}

			//Checking for the size of statement linked accounts..
			MMContext objMMContextData = objUserPrincipal.getContextData();
			ArrayList mmAccounts = objMMContextData.getAccounts();

			if(!isActionEligible){
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.ACCESS_ERROR);
			}
			else if(isAnchorAccountReq && mmAccounts.size()<= 0){
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.NO_ANCHOR_ACCOUNT_SET);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return serviceContext;
	}

	/**
	 * Flag to identify the minimum amount...
	 * @param objExtTransferForm
	 * @return
	 */
	public static boolean bypassMinAmntCheck(InternalTransferForm objIntTransferForm)
	{
		boolean isCheckReq = true;
		if(objIntTransferForm.getFrequencyValue()!=null && objIntTransferForm.getFrequencyValue().equalsIgnoreCase("2"))
		{
			if (objIntTransferForm.getDurationValue()!=null && objIntTransferForm.getDurationValue().equalsIgnoreCase("3"))
			{
				//Until dollar limit...
				String transferAmount = objIntTransferForm.getPaymentamount();
				String untilDollarLimit = objIntTransferForm.getDurationdollarlimit();
				BigDecimal totalDollarLimit = ConvertionUtil.convertToBigDecimal(objIntTransferForm.getTotalDollarlimit());
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
