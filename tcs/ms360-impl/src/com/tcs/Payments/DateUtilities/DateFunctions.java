package com.tcs.Payments.DateUtilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 *    224703	   31-05-2011      	 3			     Server-side check. 
 * **********************************************************
 */
public class DateFunctions 
{
	/**
	 * Gets the difference between 2 calendar instance in terms of months ..
	 * @param prefTxnDate
	 * @param parentTxnDate
	 * @param months
	 * @return the number of months between 2 calendar instances..
	 */
	public static int getMonthsBetween(Calendar preferredTxnDate,Calendar parentTxnDate,int months) 
	{  
		Calendar nextDate = (Calendar) parentTxnDate.clone();  
		int monthBetween = 0;  
		while (nextDate.before(preferredTxnDate) || nextDate.equals(preferredTxnDate)) {  
			nextDate.add(Calendar.MONTH, months);  
			monthBetween++;  
		}  
		return monthBetween;  
	} 

	/**
	 * Get the Next possible business date considering the frequency , and type of transaction
	 * @param frequency
	 * @param preferredTxnDate
	 * @param parentTxnDate
	 * @param transferType
	 * @param isDirectDBcall
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public Date getNextPossibleTxnDate(String frequency, Date preferredTxnDate, Date parentTxnDate ,String transferType,Boolean isDirectDBcall,ServiceContext serviceContext) throws Exception
	{
		try 
		{
			boolean isHoliday;
			Date nextTxnDate = null;

			// Transaction preferred execution date as stamped on the table... 
			Calendar c_txnPreferredDate = Calendar.getInstance();
			c_txnPreferredDate.setTime(preferredTxnDate);

			// Parent transaction execution date as stamped on the table...
			Calendar c_parentTxnDate = Calendar.getInstance();
			c_parentTxnDate.setTime(parentTxnDate); 

			EBWLogger.logDebug("DateFunctions","Preferred Transfer Date:"+c_txnPreferredDate.getTime());
			EBWLogger.logDebug("DateFunctions","Parent Transfer Date:"+c_parentTxnDate.getTime());

			if(frequency!=null && frequency.equalsIgnoreCase("W"))
			{
				EBWLogger.logDebug("DateFunctions","Weekly Transfer");
				c_txnPreferredDate.add(Calendar.DATE, 7);
				isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				if(isHoliday==true){
					nextTxnDate = getNextBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				}
				else{
					nextTxnDate = c_txnPreferredDate.getTime();
				}
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("OW"))
			{
				EBWLogger.logDebug("DateFunctions","Every other week");
				c_txnPreferredDate.add(Calendar.DATE, 14);
				isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				if(isHoliday==true){
					nextTxnDate = getNextBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				}
				else{
					nextTxnDate = c_txnPreferredDate.getTime();
				}
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("M"))
			{
				EBWLogger.logDebug("DateFunctions","Monthly");
				c_txnPreferredDate.add(Calendar.MONTH, 1);
				while((c_txnPreferredDate.get(Calendar.DATE)!= c_txnPreferredDate.getActualMaximum(Calendar.DAY_OF_MONTH))
						&& (c_txnPreferredDate.get(Calendar.DATE)!=c_parentTxnDate.get(Calendar.DATE))) { 
					c_txnPreferredDate.add(Calendar.DATE, 1);
				} 
				isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				if(isHoliday==true){
					nextTxnDate = getNextBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				}
				else{
					nextTxnDate = c_txnPreferredDate.getTime();
				}
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("LBD"))
			{    
				EBWLogger.logDebug("DateFunctions","Last Business Day");
				c_txnPreferredDate.add(Calendar.MONTH, 1);
				int daylast = c_txnPreferredDate.getActualMaximum(Calendar.DAY_OF_MONTH);
				c_txnPreferredDate.set(c_txnPreferredDate.get(Calendar.YEAR),c_txnPreferredDate.get(Calendar.MONTH),daylast);
				isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				if(isHoliday==true){
					nextTxnDate = getNextBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				}
				else{
					nextTxnDate = c_txnPreferredDate.getTime();
				}
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("FBD"))
			{ 
				EBWLogger.logDebug("DateFunctions","First Business Day");
				c_txnPreferredDate.add(Calendar.MONTH, 1);
				int dayfirst=c_txnPreferredDate.getActualMinimum(Calendar.DAY_OF_MONTH);
				c_txnPreferredDate.set(c_txnPreferredDate.get(Calendar.YEAR),c_txnPreferredDate.get(Calendar.MONTH),dayfirst);
				isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				if(isHoliday == true){
					nextTxnDate = getNextFutureBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				}
				else{
					nextTxnDate = c_txnPreferredDate.getTime();
				}
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("Q"))
			{ 
				EBWLogger.logDebug("DateFunctions","Quaterly");
				c_txnPreferredDate.add(Calendar.MONTH, 3);
				while((c_txnPreferredDate.get(Calendar.DATE)!= c_txnPreferredDate.getActualMaximum(Calendar.DAY_OF_MONTH))
						&& (c_txnPreferredDate.get(Calendar.DATE)!=c_parentTxnDate.get(Calendar.DATE))) { 
					c_txnPreferredDate.add(Calendar.DATE, 1);
				} 
				isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				if(isHoliday==true){
					nextTxnDate = getNextBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				}
				else{
					nextTxnDate = c_txnPreferredDate.getTime();
				}
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("Y"))
			{
				EBWLogger.logDebug("DateFunctions","Yearly");
				c_txnPreferredDate.add(Calendar.YEAR, 1);
				isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				if(isHoliday == true){
					nextTxnDate = getNextBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				}
				else{
					nextTxnDate = c_txnPreferredDate.getTime();
				}
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("H"))
			{
				EBWLogger.logDebug("DateFunctions","Half Yearly");
				c_txnPreferredDate.add(Calendar.MONTH, 6);
				while((c_txnPreferredDate.get(Calendar.DATE)!= c_txnPreferredDate.getActualMaximum(Calendar.DAY_OF_MONTH))
						&& (c_txnPreferredDate.get(Calendar.DATE)!=c_parentTxnDate.get(Calendar.DATE))) { 
					c_txnPreferredDate.add(Calendar.DATE, 1);
				} 
				isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				if(isHoliday == true){
					nextTxnDate = getNextBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				}
				else{
					nextTxnDate = c_txnPreferredDate.getTime();
				}
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("NBDT"))
			{
				EBWLogger.logDebug("DateFunctions","Next Business Date for cut Off Time check");
				c_txnPreferredDate.add(Calendar.DATE, 1); 
				isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				if(isHoliday == true){
					nextTxnDate = getNextFutureBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				}
				else{
					nextTxnDate = c_txnPreferredDate.getTime();
				}
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("ERDT"))
			{
				EBWLogger.logDebug("DateFunctions","Estimated Arrival Date check");
				int count = 0; 
				String txnSettleTime = MSCommonUtils.getTxnSettlePeriod(transferType,serviceContext);
				if(txnSettleTime!=null && !txnSettleTime.trim().equalsIgnoreCase(""))
				{
					int settlePeriod = ConvertionUtil.convertToint(txnSettleTime.trim());
					do
					{
						//Add only in case of transaction settle period > 1..
						if(settlePeriod > 1) {
							c_txnPreferredDate.add(Calendar.DATE, 1); 
						}
						isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
						if(isHoliday == true){
							nextTxnDate = getNextFutureBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
						}
						else {
							nextTxnDate = c_txnPreferredDate.getTime();
						}
						count++;
					}
					while(count < settlePeriod);
				}
			}
			EBWLogger.logDebug("DateFunctions","The Next transfer date calculated is "+nextTxnDate);
			return nextTxnDate;
		} 
		catch (Exception exception) {
			throw exception;
		}	
	}


	/** Calculate the business date (Prepone).. 
	 * Previous BusinessDay
	 * @param cal
	 * @param transferType
	 * @param isDirectDBCall = To check if the call is from EAR or WAR side .
	 * @param serviceContext = ServiceContext is required in case we are getting the cache from the EAR Side , 
	 * if the call is made from the WAR Side , service context Object is null..
	 * @return
	 * @throws Exception 
	 */
	public static Date getNextBusinessDay(Calendar cal,String transferType,Boolean isDirectDBcall,ServiceContext serviceContext) throws Exception
	{
		boolean holiday = false;
		do {
			cal.add(Calendar.DATE, -1); // Prepone the date to the prev business date (PrePone)..
			holiday = checkHoliday(cal,transferType,isDirectDBcall,serviceContext);
		}
		while(holiday==true);
		return cal.getTime();	
	}

	/** Calculate the business date (PostPone)..
	 * This is also used for calculating FBD of the moth , where we need to postpone the same ..
	 * @param cal
	 * @param transferType
	 * @return
	 * @throws Exception 
	 */
	public static Date getNextFutureBusinessDay(Calendar cal,String transferType,Boolean isDirectDBcall,ServiceContext serviceContext) throws Exception 
	{
		boolean holiday = false;
		do {
			cal.add(Calendar.DATE, 1); // Add one date to the current date (PostPone)
			holiday = checkHoliday(cal,transferType,isDirectDBcall,serviceContext);
		}
		while(holiday==true);
		return cal.getTime();	
	}


	/** Get holiday list from database
	 * 
	 * @param cal
	 * @param transferType
	 * @param isDirectDBCall = To check if the call is from EAR or WAR side .
	 * @param serviceContext = ServiceContext is required in case we are getting the cache from the EAR Side , 
	 * if the call is made from the WAR Side , service context Object is null..
	 * @return
	 * @throws Exception 
	 */
	public static boolean checkHoliday(Calendar cal,String transferType,Boolean isDirectDBcall,ServiceContext serviceContext) throws Exception
	{
		Object holidayListOut = null;
		boolean holiday = false;
		try 
		{
			//Getting the Statement Id based on the Transfer type...
			holidayListOut = MSCommonUtils.getHolidayListParams(transferType,serviceContext);
			if(holidayListOut!=null)
			{
				ArrayList cacheHolidaysList = (ArrayList)holidayListOut; 
				//Cloning required ....
				ArrayList holidaysList = (ArrayList)cacheHolidaysList.clone();
				holidaysList.remove(0); 
				ArrayList<Date> formatHolidayout = new ArrayList<Date>(); 
				for(int i=0;i<holidaysList.size();i++){
					ArrayList holidayDateValue =(ArrayList)holidaysList.get(i); 
					Date holidayDate = ConvertionUtil.convertToDate((String)holidayDateValue.get(0));
					formatHolidayout.add(holidayDate);
				}
				if(!formatHolidayout.isEmpty()){
					for(int i=0;i<formatHolidayout.size();i++){
						if((cal.getTime()).compareTo(formatHolidayout.get(i))==0){
							holiday=true;
							break;
						}
						else{
							holiday= false;
						}
					}
				}
				EBWLogger.logDebug("DateFunctions","Is date a Holiday : "+holiday+ "Holiday List size:"+cacheHolidaysList.size());
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		return holiday;
	}


	/**
	 * This method is used to get the Preferred next transaction Date in case of Recurring transfers...
	 * @param frequency
	 * @param preferredTxnDate
	 * @param parentTxnDate
	 * @param transferType
	 * @return
	 * @throws Exception
	 */
	public Date getPrefNextTxnDate(String frequency, Date preferredTxnDate, Date parentTxnDate,String transferType) throws Exception
	{
		try 
		{
			Date nextTxnDate = null;

			// Transaction preferred execution date as stamped on the table... (Txn_parent)
			Calendar c_txnPreferredDate = Calendar.getInstance();
			c_txnPreferredDate.setTime(preferredTxnDate);

			// Parent transaction execution date as stamped on the table...(Txn_parent)
			Calendar c_parentTxnDate = Calendar.getInstance();
			c_parentTxnDate.setTime(parentTxnDate); 

			EBWLogger.logDebug("DateFunctions","Current Transfer Date:"+c_txnPreferredDate.getTime());
			EBWLogger.logDebug("DateFunctions","Parent Transfer Date:"+c_parentTxnDate.getTime());

			if(frequency!=null && frequency.equalsIgnoreCase("W"))
			{
				EBWLogger.logDebug("DateFunctions","Weekly");
				c_txnPreferredDate.add(Calendar.DATE, 7);
				nextTxnDate = c_txnPreferredDate.getTime();
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("OW"))
			{
				EBWLogger.logDebug("DateFunctions","Every other week");
				c_txnPreferredDate.add(Calendar.DATE, 14);
				nextTxnDate = c_txnPreferredDate.getTime();
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("M"))
			{
				EBWLogger.logDebug("DateFunctions","Monthly");
				c_txnPreferredDate.add(Calendar.MONTH, 1);
				while((c_txnPreferredDate.get(Calendar.DATE)!= c_txnPreferredDate.getActualMaximum(Calendar.DAY_OF_MONTH))
						&& (c_txnPreferredDate.get(Calendar.DATE)!=c_parentTxnDate.get(Calendar.DATE))) { 
					c_txnPreferredDate.add(Calendar.DATE, 1);
				} 
				nextTxnDate = c_txnPreferredDate.getTime();
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("LBD"))
			{    
				EBWLogger.logDebug("DateFunctions","Lastday");
				c_txnPreferredDate.add(Calendar.MONTH, 1);
				int daylast=c_txnPreferredDate.getActualMaximum(Calendar.DAY_OF_MONTH);
				c_txnPreferredDate.set(c_txnPreferredDate.get(Calendar.YEAR),c_txnPreferredDate.get(Calendar.MONTH),daylast);
				nextTxnDate = c_txnPreferredDate.getTime();
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("FBD"))
			{ 
				EBWLogger.logDebug("DateFunctions","Firstday");
				c_txnPreferredDate.add(Calendar.MONTH, 1);
				int dayfirst=c_txnPreferredDate.getActualMinimum(Calendar.DAY_OF_MONTH);
				c_txnPreferredDate.set(c_txnPreferredDate.get(Calendar.YEAR),c_txnPreferredDate.get(Calendar.MONTH),dayfirst);
				nextTxnDate = c_txnPreferredDate.getTime();
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("Q"))
			{ 
				EBWLogger.logDebug("DateFunctions","Quarterly");
				c_txnPreferredDate.add(Calendar.MONTH, 3);
				while((c_txnPreferredDate.get(Calendar.DATE)!= c_txnPreferredDate.getActualMaximum(Calendar.DAY_OF_MONTH))
						&& (c_txnPreferredDate.get(Calendar.DATE)!=c_parentTxnDate.get(Calendar.DATE))) { 
					c_txnPreferredDate.add(Calendar.DATE, 1);
				} 
				nextTxnDate = c_txnPreferredDate.getTime();
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("Y"))
			{
				EBWLogger.logDebug("DateFunctions","Anually");
				c_txnPreferredDate.add(Calendar.YEAR, 1);
				nextTxnDate = c_txnPreferredDate.getTime();
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("H"))
			{
				EBWLogger.logDebug("DateFunctions","Half Yearly");
				c_txnPreferredDate.add(Calendar.MONTH, 6);
				while((c_txnPreferredDate.get(Calendar.DATE)!= c_txnPreferredDate.getActualMaximum(Calendar.DAY_OF_MONTH))
						&& (c_txnPreferredDate.get(Calendar.DATE)!=c_parentTxnDate.get(Calendar.DATE))) { 
					c_txnPreferredDate.add(Calendar.DATE, 1);
				} 
				nextTxnDate = c_txnPreferredDate.getTime();
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("NBDT"))
			{
				EBWLogger.logDebug("DateFunctions","Next Business Date for cut Off Time check");
				c_txnPreferredDate.add(Calendar.DATE, 1); 
				nextTxnDate = c_txnPreferredDate.getTime();
			}
			EBWLogger.logDebug("DateFunctions","The Next Preferred Transaction date calculated is "+nextTxnDate);
			return nextTxnDate;
		} 
		catch (Exception exception) {
			throw exception;
		}	
	}


	/** Get the Next First or Last business date considering the frequency , and type of transaction
	 *  This method is used to get the next FBD or LBD before executing the current transaction
	 *  This method was added to make sure boundary value conditions are satisfied ..
	 *  This function is used only in case of the calculating the FBD and LBD for external transfers screen (PreConfirmation screen ) 
	 * @param frequency
	 * @param lastTransferDt
	 * @param transferType
	 * @param isDirectDBCall = To check if the call is from EAR or WAR side .
	 * @param serviceContext = ServiceContext is required in case we are getting the cache from the EAR Side , 
	 * if the call is made from the WAR Side , service context Object is null.
	 * @return
	 * @throws Exception
	 */
	public Date getNextFirstOrLastTxnDate(String frequency, Date lastTransferDt, String transferType,Boolean isDirectDBcall,ServiceContext serviceContext) throws Exception
	{
		try 
		{
			boolean isHoliday = false;
			Date nextTxnDate = null;

			Calendar c_txnPreferredDate = Calendar.getInstance();
			c_txnPreferredDate.setTime(lastTransferDt);
			EBWLogger.logDebug("DateFunctions","Current Transfer Date"+c_txnPreferredDate.getTime());

			if(frequency!=null && frequency.equalsIgnoreCase("LBD"))
			{    
				EBWLogger.logDebug("DateFunctions","Last Business Date");
				int daylast = c_txnPreferredDate.getActualMaximum(Calendar.DAY_OF_MONTH);
				c_txnPreferredDate.set(c_txnPreferredDate.get(Calendar.YEAR),c_txnPreferredDate.get(Calendar.MONTH),daylast);
				isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				if(isHoliday == true){
					nextTxnDate = getNextBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
				}
				else{
					nextTxnDate = c_txnPreferredDate.getTime();
				}
			}
			else if(frequency!=null && frequency.equalsIgnoreCase("FBD"))
			{ 
				EBWLogger.logDebug("DateFunctions","First Business Date");
				Calendar firstBussDay = getFirstBussDay(lastTransferDt,transferType,isDirectDBcall,serviceContext);
				if(firstBussDay.equals(c_txnPreferredDate)){
					nextTxnDate = firstBussDay.getTime();
				}
				else {
					c_txnPreferredDate.add(Calendar.MONTH, 1); // Add one month for the initiation date to calculate the very next business date..
					int dayfirst = c_txnPreferredDate.getActualMinimum(Calendar.DAY_OF_MONTH);
					c_txnPreferredDate.set(c_txnPreferredDate.get(Calendar.YEAR),c_txnPreferredDate.get(Calendar.MONTH),dayfirst);
					isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
					if(isHoliday == true){
						nextTxnDate = getNextFutureBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
					}
					else{
						nextTxnDate = c_txnPreferredDate.getTime();
					}
				}
			}
			EBWLogger.logDebug("DateFunctions","The Next transfer date calculated is "+nextTxnDate);
			return nextTxnDate;
		} catch (Exception exception) {
			throw exception;
		}	
	}

	/**
	 * The method gets the First Business Day for the Month of transaction initiation...
	 * Used to compare the boundary conditions for the 
	 * @param transactionDate
	 * @param transferType
	 * @param isDirectDBcall
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public Calendar getFirstBussDay(Date transactionDate,String transferType,Boolean isDirectDBcall,ServiceContext serviceContext) throws Exception
	{
		boolean isFrstDayHoliday = false;
		Date frstBussDate = null;

		// Set the calendar instance to the First Day of the Month ...
		Calendar firstDay = Calendar.getInstance();
		firstDay.setTime(transactionDate);
		firstDay.set(Calendar.DAY_OF_MONTH,firstDay.getActualMinimum(Calendar.DAY_OF_MONTH));

		//Checking the if the First day of month is holiday , loop to get the First business day of the month
		isFrstDayHoliday = checkHoliday(firstDay,transferType,isDirectDBcall,serviceContext);
		if(isFrstDayHoliday == true){
			frstBussDate = getNextFutureBusinessDay(firstDay,transferType,isDirectDBcall,serviceContext);
		}
		else{
			frstBussDate = firstDay.getTime();
		}

		//Returns the First Business Day of the Txn initiation month as Calendar instance...
		Calendar firstBussDay = Calendar.getInstance();
		firstBussDay.setTime(frstBussDate);
		return firstBussDay;
	}

	/** The below function is to get the transaction expiry date, No of the days transaction will be active starts from the requested execution date of the transaction . 
	 * The parameter (expiry time) needs to be taken from the  DB Configurable parameter view .  
	 * In short ,this calculates the FBD or LBD depending on the current initiation date ...
	 * @param frequency
	 * @param lastTransferDt
	 * @param transferType
	 * @param isDirectDBCall = To check if the call is from EAR or WAR side .
	 * @param serviceContext = ServiceContext is required in case we are getting the cache from the EAR Side , 
	 * if the call is made from the WAR Side , service context Object is null.
	 * @return
	 * @throws Exception
	 */
	public Date getTransactionExpiryDate(String frequency, Date lastTransferDt, String transferType,Boolean isDirectDBcall,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug("DateFunctions","Calculating transaction expiry date");
		try
		{
			boolean isHoliday;
			Date nextTxnDate = null;

			Calendar c_txnPreferredDate = Calendar.getInstance();
			c_txnPreferredDate.setTime(lastTransferDt);

			if(frequency!=null && frequency.equalsIgnoreCase("EXPDT"))
			{
				int count=0; 
				String txnExpPeriod = MSCommonUtils.getExpiryPeriodParams(transferType,serviceContext);
				if(txnExpPeriod!=null && !txnExpPeriod.trim().equalsIgnoreCase(""))
				{
					int expPeriod = ConvertionUtil.convertToint(txnExpPeriod.trim());
					do 
					{
						c_txnPreferredDate.add(Calendar.DATE, 1); 
						isHoliday = checkHoliday(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
						if(isHoliday==true){
							nextTxnDate = getNextFutureBusinessDay(c_txnPreferredDate,transferType,isDirectDBcall,serviceContext);
						}
						else {
							nextTxnDate = c_txnPreferredDate.getTime();
						}
						count++;
					} while(count < expPeriod);
				}
			}
			EBWLogger.logDebug("DateFunctions","The Transaction Expiry date calculated is "+nextTxnDate);
			return nextTxnDate;
		} 
		catch (Exception exception) {
			throw exception;
		}	
	}

	/** Returns true in case the Date is within the range allowed. 
	 * Gets the date range from the Configurable parameter list..
	 * @param cal
	 * @param isDirectDBcall
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static boolean checkDateRange(Calendar payDate,Calendar bussDate,String transferType,Boolean isDirectDBcall,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug("DateFunctions","Checking the Transfer initiation date range..");
		boolean isDtRangeValid = false; 
		try 
		{
			String maxDateRange = MSCommonUtils.getMaxFutureDaysParams(transferType,serviceContext);
			if(maxDateRange!=null && !maxDateRange.trim().equalsIgnoreCase("")){
				bussDate.add(Calendar.YEAR, ConvertionUtil.convertToint(maxDateRange.trim()));
				if(payDate.before(bussDate)){
					isDtRangeValid=true;
				}
			}
		}
		catch(Exception exception){
			throw exception;
		}
		EBWLogger.logDebug("DateFunctions","Is Date range Valid "+isDtRangeValid);
		return isDtRangeValid;
	}


	/** Returns true in case the Date is within the range allowed. 
	 * Gets the date range from the Configurable parameter list..
	 * @param cal
	 * @param isDirectDBcall
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static boolean checkVerbalAuthDateRange(Calendar verbalAuthDate,Calendar bussDate,String transferType,Boolean isDirectDBcall,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug("DateFunctions","Checking the verbal Auth date range..");
		boolean isDtRangeValid = false; 
		try 
		{
			String maxDateRange = MSCommonUtils.getMaxPastVerbalAuthDays(transferType,serviceContext);
			if(maxDateRange!=null && !maxDateRange.trim().equalsIgnoreCase("")){
				bussDate.add(Calendar.DATE, - ConvertionUtil.convertToint(maxDateRange.trim()));
				if(!verbalAuthDate.before(bussDate)){
					isDtRangeValid = true;
				}
			}
		}
		catch(Exception exception){
			throw exception;
		}
		EBWLogger.logDebug("DateFunctions","Is Date range Valid "+isDtRangeValid);
		return isDtRangeValid;
	}
}


