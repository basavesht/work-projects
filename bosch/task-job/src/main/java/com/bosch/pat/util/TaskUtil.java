package com.bosch.pat.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;


public class TaskUtil
{
	public static final Logger logger = Logger.getLogger(TaskUtil.class);
	public static final String _TASK_DATE_DISPLAY_FORMAT = "MMM dd, ''yyyy HH:mm:ss";

	/**
	 * Calculate the task next execution date...
	 * @param frequency
	 * @param taskDueDate
	 * @return
	 * @throws Exception
	 */
	public static Calendar getNextExecutionDate(Integer frequency, Date taskDueDate) throws Exception {
		Calendar taskNextExecutionDate = Calendar.getInstance();
		try
		{
			taskNextExecutionDate.setTime(taskDueDate);

			//Check for the frequency and accordingly map the task next execution date...
			if (frequency != 0) {
				logger.info("Frequency : " + frequency);
				taskNextExecutionDate.add(Calendar.DATE, frequency);
			}
			else {
				logger.info("Invalid frequency number");
				throw new IllegalArgumentException("Invalid frequency number");
			}
			return taskNextExecutionDate;
		}
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Get the current date in Input timezone..
	 * @param inputTZ
	 * @return
	 */
	public static Calendar getCurrentDateInInputTZ(String inputTZ) {
		Date localTime = new Date();
		DateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		converter.setTimeZone(TimeZone.getTimeZone(inputTZ));
		converter.format(localTime);

		//Create a current date instance...
		String localTimeStr = converter.format(localTime);
		DateFormat dateConverter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date currentDate = null;
		try {
			currentDate = dateConverter.parse(localTimeStr);
		} catch (ParseException e) {
			currentDate = new Date();
		}

		//Current date in given timezone considering the start time..
		Calendar currentDateStartTime = Calendar.getInstance();
		currentDateStartTime.setTime(currentDate);
		logger.info("Current date in input TZ : " + inputTZ + " computed : " + currentDateStartTime.getTime());

		return currentDateStartTime;
	}

	/**
	 * Convert Timestamp to calendar instance...
	 * @param exeDate
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Calendar getTimeInCalendar(long exeDate) throws IllegalArgumentException {
		if(exeDate == 0) {
			throw new IllegalArgumentException("Invalid Date !!");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(exeDate);
		return calendar;
	}

	/**
	 * Convert Session Date to text format...
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		if(date == null) {
			return null;
		}
		DateFormat textPattern = new SimpleDateFormat(_TASK_DATE_DISPLAY_FORMAT);
		return textPattern.format(date);
	}

	/**
	 * Convert Session response time as date...
	 * @param responseDate
	 * @return
	 */
	public static Date parseDate(Date responseDate) {
		if(responseDate == null) {
			return null;
		}

		//Parse date...
		Date taskDueDate = null;
		try {
			DateFormat datePattern = new SimpleDateFormat("MM/dd/yyyy");
			taskDueDate =  datePattern.parse(datePattern.format(responseDate.getTime()));
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		return taskDueDate;
	}

	/**
	 * Calculates the TaskDue in days..
	 * @param convertDateToCalendar
	 * @param currentDateInCMTZ
	 * @return
	 */
	public static Integer calculateTaskDueInDays(Date taskDueDate, Date currentDateInCMTZ)  {
		Integer dueInDays = 0;
		if(taskDueDate.before(currentDateInCMTZ)) {
			dueInDays = -1;
		}
		else if(taskDueDate.compareTo(currentDateInCMTZ) == 0) {
			dueInDays = 0;
		}
		else if (taskDueDate.after(currentDateInCMTZ)) {
			LocalDate taskDate = new LocalDate(taskDueDate); //Convert to Joda-Readable format..
			LocalDate currentDate = new LocalDate(currentDateInCMTZ); //Convert to Joda-Readable format..
			dueInDays = Days.daysBetween(currentDate,taskDate).getDays();
		}
		return dueInDays;
	}

	/**
	 * Calculate task due date in UTCS..
	 * @param taskDueDate
	 * @return
	 */
	public static Timestamp getTaskDueDateInUTCS (Calendar taskDueDate) {

		//Reset the current taskDueDate to midnight..
		Calendar taskDueDateTime = Calendar.getInstance();
		taskDueDateTime.set(Calendar.DAY_OF_MONTH, taskDueDate.get(Calendar.DAY_OF_MONTH));
		taskDueDateTime.set(Calendar.MONTH, taskDueDate.get(Calendar.MONTH));
		taskDueDateTime.set(Calendar.YEAR, taskDueDate.get(Calendar.YEAR));
		taskDueDateTime.set(Calendar.HOUR_OF_DAY, 23);
		taskDueDateTime.set(Calendar.MINUTE, 59);
		taskDueDateTime.set(Calendar.SECOND, 59);
		taskDueDateTime.set(Calendar.MILLISECOND, 59);
		logger.info("Task due-date as received in Server TZ" + taskDueDateTime.getTime() + " in " + taskDueDateTime.getTimeZone().getDisplayName());

		//Convert task due date to UTCS..
		Calendar taskDueDateTimeInUTCS = convertToUTCSOffset(taskDueDateTime, taskDueDateTime.getTimeZone().getID());
		logger.info("Task due-date in UTCS " + taskDueDateTimeInUTCS.getTime() + " in " + taskDueDateTimeInUTCS.getTimeZone().getDisplayName());

		return new Timestamp(taskDueDateTimeInUTCS.getTimeInMillis());
	}

	/**
	 * Calculate task due date in UTCS..
	 * @param taskDueDate
	 * @return
	 */
	public static Timestamp getTaskNextExecutionInUTCS (Calendar taskNextExecDate) {

		//Reset the current taskDueDate to midnight..
		Calendar taskNextExecDateTime = Calendar.getInstance();
		taskNextExecDateTime.set(Calendar.DAY_OF_MONTH, taskNextExecDate.get(Calendar.DAY_OF_MONTH));
		taskNextExecDateTime.set(Calendar.MONTH, taskNextExecDate.get(Calendar.MONTH));
		taskNextExecDateTime.set(Calendar.YEAR, taskNextExecDate.get(Calendar.YEAR));
		taskNextExecDateTime.set(Calendar.HOUR_OF_DAY, 23);
		taskNextExecDateTime.set(Calendar.MINUTE, 59);
		taskNextExecDateTime.set(Calendar.SECOND, 59);
		taskNextExecDateTime.set(Calendar.MILLISECOND, 59);
		logger.info("Task due-date as received in Server TZ" + taskNextExecDateTime.getTime() + " in " + taskNextExecDateTime.getTimeZone().getDisplayName());

		//Convert task due date to UTCS..
		Calendar tasktaskNextExecDateTimeInUTCS = convertToUTCSOffset(taskNextExecDateTime, taskNextExecDateTime.getTimeZone().getID());
		logger.info("Task due-date in UTCS " + tasktaskNextExecDateTimeInUTCS.getTime() + " in " + tasktaskNextExecDateTimeInUTCS.getTimeZone().getDisplayName());

		return new Timestamp(tasktaskNextExecDateTimeInUTCS.getTimeInMillis());
	}

	/**
	 * Convert to GMT offset either start or end time...
	 * @param date
	 * @param fromTZ
	 * @param isStartTime
	 * @return
	 */
	public static Calendar convertToUTCSOffset(Calendar date, String fromTZ) 
	{
		TimeZone fromTimeZone = TimeZone.getTimeZone(fromTZ);
		TimeZone toTimeZone = TimeZone.getTimeZone(TaskConstants.DEF_TZ);

		// Get a Calendar instance using the default time zone and locale and set the calendar's time with the given date ...
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(fromTimeZone);
		calendar.setTime(date.getTime());

		logger.info("Before applying GMT TZ Offset: " + calendar.getTime() + " in " + fromTimeZone.getDisplayName());

		//Convert 'FROM' TimeZone to UTC considering DST..
		calendar.add(Calendar.MILLISECOND, fromTimeZone.getRawOffset() * -1);
		if (fromTimeZone.inDaylightTime(calendar.getTime())) {
			calendar.add(Calendar.MILLISECOND, calendar.getTimeZone().getDSTSavings() * -1);
		}

		//Convert UTC to 'TO' TimeZone considering DST..
		calendar.add(Calendar.MILLISECOND, toTimeZone.getRawOffset());
		if (toTimeZone.inDaylightTime(calendar.getTime())) {
			calendar.add(Calendar.MILLISECOND, toTimeZone.getDSTSavings());
		}

		//Create another instance and rest the time either to start or end time...
		Calendar convertedOffsetTZ = Calendar.getInstance();
		convertedOffsetTZ.setTimeInMillis(calendar.getTimeInMillis());

		logger.info("After applying GMT TZ Offset: " + convertedOffsetTZ.getTime() + " in " + toTimeZone.getDisplayName());
		return convertedOffsetTZ;
	}

	/**
	 * Convert from one TimeZone to another TimeZone considering DST..
	 * @param time
	 * @return
	 */
	public static Calendar convertToOffsetTimeZone(Calendar date, String fromTZ, String toTZ) 
	{
		TimeZone fromTimeZone = TimeZone.getTimeZone(fromTZ);
		TimeZone toTimeZone = TimeZone.getTimeZone(toTZ);

		// Get a Calendar instance using the default time zone and locale and set the calendar's time with the given date ...
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(fromTimeZone);
		calendar.setTime(date.getTime());

		logger.debug("Before applying TimeZone Offset: " + calendar.getTime() + " in " + fromTimeZone.getDisplayName());

		//Convert 'FROM' TimeZone to UTC considering DST..
		calendar.add(Calendar.MILLISECOND, fromTimeZone.getRawOffset() * -1);
		if (fromTimeZone.inDaylightTime(calendar.getTime())) {
			calendar.add(Calendar.MILLISECOND, fromTimeZone.getDSTSavings() * -1);
		}

		//Convert UTC to 'TO' TimeZone considering DST..
		calendar.add(Calendar.MILLISECOND, toTimeZone.getRawOffset());
		if (toTimeZone.inDaylightTime(calendar.getTime())) {
			calendar.add(Calendar.MILLISECOND, toTimeZone.getDSTSavings());
		}

		//Create another instance ....
		Calendar convertedOffsetTZ = Calendar.getInstance();
		convertedOffsetTZ.setTimeInMillis(calendar.getTimeInMillis());

		logger.debug("After applying TimeZone Offset: " + convertedOffsetTZ.getTime() + " in " + toTimeZone.getDisplayName());
		return convertedOffsetTZ;
	}

	/**
	 * Start time for the scheduled tasks due date.
	 * @return
	 * @throws ParseException 
	 */
	public static Timestamp getScheduledTasksDueStartTime(Date lastSuccesfulJobRunDate, Integer pastNoOfDays) throws ParseException 
	{
		DateTime dt = null;
		if(lastSuccesfulJobRunDate != null) {
			dt = new DateTime(lastSuccesfulJobRunDate);
			dt = dt.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
			dt = dt.toDateTime(DateTimeZone.UTC);
			logger.debug("Starting the job from last sucessful job run date");
		}
		else { //Condition to handle initial run.
			dt = DateTime.now();
			dt = dt.minusDays(pastNoOfDays); // Start the job from past 10 days..
			dt = dt.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
			dt = dt.toDateTime(DateTimeZone.UTC);
			logger.debug("No last job run date found. Starting the job run from last 30 (configured) days..");
		}

		Timestamp startTime = null;
		Date jdkDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(dt.toString("yyyy-MM-dd HH:mm:ss:SSS"));
		startTime = new Timestamp(jdkDate.getTime());
		logger.info("Start date computed for scheduled due task list :" + startTime );
		return startTime;
	}

	/**
	 * End time for the scheduled tasks due date.
	 * @return
	 * @throws ParseException 
	 */
	public static Timestamp getScheduledTasksDueEndTime() throws ParseException 
	{
		DateTime dt = DateTime.now();
		dt = dt.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(59);
		dt = dt.toDateTime(DateTimeZone.UTC);
		
		Timestamp endTime = null;
		Date jdkDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(dt.toString("yyyy-MM-dd HH:mm:ss:SSS"));
		endTime = new Timestamp(jdkDate.getTime());
		logger.info("End date computed for scheduled due task list :" + endTime );
		return endTime;
	}

	public static long getCurrentTimeInUTCS(){
		DateTime currentDateTime = DateTime.now(DateTimeZone.UTC);
		return currentDateTime.getMillis();
	}
}
