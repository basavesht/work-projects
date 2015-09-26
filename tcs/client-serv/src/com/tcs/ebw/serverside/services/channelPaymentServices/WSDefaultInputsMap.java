package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.ArrayList;

import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.cache.CacheObject;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class WSDefaultInputsMap extends DatabaseService 
{

	/**
	 * Application ID Input to RTAB Web Service 
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getRTABAppID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String appId = "";
		Object objAppId = null; 
		ArrayList<Object> applIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAppId = cacheObj.getCacheData("getRTABUserID",isDirectDBCall,serviceContext);
			if(objAppId!=null)
			{
				applIdVal = (ArrayList<Object>) objAppId;
				if (!applIdVal.isEmpty() && applIdVal.get(1)!= null){
					appId = (String)(((ArrayList)applIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Application ID Input to RTAB Web Service "+ appId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return appId;
	}

	/**
	 * User ID Input to Account plating Web Service 
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getAcntPlatingUserID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String userId = "";
		Object objUserId = null; 
		ArrayList<Object> userIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objUserId = cacheObj.getCacheData("getAcntPlatingUserID",isDirectDBCall,serviceContext);
			if(objUserId!=null)
			{
				userIdVal = (ArrayList<Object>) objUserId;
				if (!userIdVal.isEmpty() && userIdVal.get(1)!= null){
					userId = (String)(((ArrayList)userIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","User ID Input to Account plating Web Service "+ userId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return userId;
	}

	/**
	 * Application ID Input to Account plating Web Service
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getAcntPlatingAppID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String appId = "";
		Object objAppId = null; 
		ArrayList<Object> appIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAppId = cacheObj.getCacheData("getAcntPlatingAppID",isDirectDBCall,serviceContext);
			if(objAppId!=null)
			{
				appIdVal = (ArrayList<Object>) objAppId;
				if (!appIdVal.isEmpty() && appIdVal.get(1)!= null){
					appId = (String)(((ArrayList)appIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Application ID Input to Account plating Web Service "+ appId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return appId;
	}

	/**
	 * Server IP Input to Account plating Web Service 
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getAcntPlatingServerIP(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String serverIp = "";
		Object objServerIp = null; 
		ArrayList<Object> serverIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objServerIp = cacheObj.getCacheData("getAcntPlatingServerIP",isDirectDBCall,serviceContext);
			if(objServerIp!=null)
			{
				serverIdVal = (ArrayList<Object>) objServerIp;
				if (!serverIdVal.isEmpty() && serverIdVal.get(1)!= null){
					serverIp = (String)(((ArrayList)serverIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Server IP Input to Account plating Web Service "+ serverIp);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return serverIp;
	}

	/**
	 * User ID Input to Merlin (Account View) Web Service
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getAcntViewUserID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String userId = "";
		Object objUserId = null; 
		ArrayList<Object> userIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objUserId = cacheObj.getCacheData("getAcntViewUserID",isDirectDBCall,serviceContext);
			if(objUserId!=null)
			{
				userIdVal = (ArrayList<Object>) objUserId;
				if (!userIdVal.isEmpty() && userIdVal.get(1)!= null){
					userId = (String)(((ArrayList)userIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","User ID Input to Merlin (Account View) Web Service"+ userId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return userId;
	}

	/**
	 * Application ID Input to Merlin (Account View) Web Service 
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getAcntViewAppID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String appId = "";
		Object objAppId = null; 
		ArrayList<Object> appIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAppId = cacheObj.getCacheData("getAcntViewAppID",isDirectDBCall,serviceContext);
			if(objAppId!=null)
			{
				appIdVal = (ArrayList<Object>) objAppId;
				if (!appIdVal.isEmpty() && appIdVal.get(1)!= null){
					appId = (String)(((ArrayList)appIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Application ID Input to Merlin (Account View) Web Service "+ appId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return appId;
	}

	/**
	 * Auth Application ID Input to Merlin (Account View) Web Service  
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getAcntViewAuthAppID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String authAppId = "";
		Object objAuthAppId = null; 
		ArrayList<Object> authAppIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAuthAppId = cacheObj.getCacheData("getAcntViewAuthAppID",isDirectDBCall,serviceContext);
			if(objAuthAppId!=null)
			{
				authAppIdVal = (ArrayList<Object>) objAuthAppId;
				if (!authAppIdVal.isEmpty() && authAppIdVal.get(1)!= null){
					authAppId = (String)(((ArrayList)authAppIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Auth Application ID Input to Merlin (Account View) Web Service  "+ authAppId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return authAppId;
	}

	/**
	 * Auth Verb Input to Merlin (Account View) Web Service  
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getAcntViewAuthVerb(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String authVerb = "";
		Object objAuthVerb = null; 
		ArrayList<Object> authVerbVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAuthVerb = cacheObj.getCacheData("getAcntViewAuthVerb",isDirectDBCall,serviceContext);
			if(objAuthVerb!=null)
			{
				authVerbVal = (ArrayList<Object>) objAuthVerb;
				if (!authVerbVal.isEmpty() && authVerbVal.get(1)!= null){
					authVerb = (String)(((ArrayList)authVerbVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Auth Verb Input to Merlin (Account View) Web Service  "+ authVerb);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return authVerb;
	}

	/**
	 * Web Server ID Input to Merlin (Account View) Web Service   
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getAcntViewWebServerID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String webServerId = "";
		Object objWebServerId = null; 
		ArrayList<Object> webServerIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objWebServerId = cacheObj.getCacheData("getAcntViewWebServerID",isDirectDBCall,serviceContext);
			if(objWebServerId!=null)
			{
				webServerIdVal = (ArrayList<Object>) objWebServerId;
				if (!webServerIdVal.isEmpty() && webServerIdVal.get(1)!= null){
					webServerId = (String)(((ArrayList)webServerIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Web Server ID Input to Merlin (Account View) Web Service   "+ webServerId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return webServerId;
	}

	/**
	 * User ID Input to BR Web Service  
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getBusRuleCntrlUserID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String userId = "";
		Object objUserId = null; 
		ArrayList<Object> userIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objUserId = cacheObj.getCacheData("getBusRuleCntrlUserID",isDirectDBCall,serviceContext);
			if(objUserId!=null)
			{
				userIdVal = (ArrayList<Object>) objUserId;
				if (!userIdVal.isEmpty() && userIdVal.get(1)!= null){
					userId = (String)(((ArrayList)userIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","User ID Input to BR Web Service  "+ userId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return userId;
	}

	/**
	 * Application ID Input to BR Web Service  
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getBusRuleCntrlAppID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String appId = "";
		Object objAppId = null; 
		ArrayList<Object> appIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAppId = cacheObj.getCacheData("getBusRuleCntrlAppID",isDirectDBCall,serviceContext);
			if(objAppId!=null)
			{
				appIdVal = (ArrayList<Object>) objAppId;
				if (!appIdVal.isEmpty() && appIdVal.get(1)!= null){
					appId = (String)(((ArrayList)appIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Application ID Input to BR Web Service  "+ appId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return appId;
	}

	/**
	 * Server IP Input to BR Web Service 
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getBusRuleCntrlServerID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String serverId = "";
		Object objServerId = null; 
		ArrayList<Object> serverIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objServerId = cacheObj.getCacheData("getBusRuleCntrlServerID",isDirectDBCall,serviceContext);
			if(objServerId!=null)
			{
				serverIdVal = (ArrayList<Object>) objServerId;
				if (!serverIdVal.isEmpty() && serverIdVal.get(1)!= null){
					serverId = (String)(((ArrayList)serverIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Server IP Input to BR Web Service "+ serverId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return serverId;
	}


	/**
	 * MS360 Client Interaction Center...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getCSContactCenter(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String contactCenterId = "";
		Object objcontactCenterId = null; 
		ArrayList<Object> objcontactCenterVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objcontactCenterId = cacheObj.getCacheData("getContactNumber",isDirectDBCall,serviceContext);
			if(objcontactCenterId!=null)
			{
				objcontactCenterVal = (ArrayList<Object>) objcontactCenterId;
				if (!objcontactCenterVal.isEmpty() && objcontactCenterVal.get(1)!= null){
					contactCenterId = (String)(((ArrayList)objcontactCenterVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Client Interaction Center"+ contactCenterId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return contactCenterId;
	}

	/**
	 * MS360 Internal Min Amount...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getIntTxnMinAmount(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAmountId = cacheObj.getCacheData("getInternalMinAmount",isDirectDBCall,serviceContext);
			if(objAmountId!=null)
			{
				objAmountVal = (ArrayList<Object>) objAmountId;
				if (!objAmountVal.isEmpty() && objAmountVal.get(1)!= null){
					amountId = (String)(((ArrayList)objAmountVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Internal Min Amount"+ amountId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return amountId;
	}

	/**
	 * MS360 Internal Max Amount...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getIntTxnMaxAmount(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAmountId = cacheObj.getCacheData("getInternalMaxAmount",isDirectDBCall,serviceContext);
			if(objAmountId!=null)
			{
				objAmountVal = (ArrayList<Object>) objAmountId;
				if (!objAmountVal.isEmpty() && objAmountVal.get(1)!= null){
					amountId = (String)(((ArrayList)objAmountVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Internal Max Amount"+ amountId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return amountId;
	}

	/**
	 * MS360 External Min Amount...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getACHTxnMinAmount(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAmountId = cacheObj.getCacheData("getExternalMinAmount",isDirectDBCall,serviceContext);
			if(objAmountId!=null)
			{
				objAmountVal = (ArrayList<Object>) objAmountId;
				if (!objAmountVal.isEmpty() && objAmountVal.get(1)!= null){
					amountId = (String)(((ArrayList)objAmountVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 External Min Amount"+ amountId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return amountId;
	}

	/**
	 * MS360 External Max amount...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getACHTxnMaxAmount(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAmountId = cacheObj.getCacheData("getExternalMaxAmount",isDirectDBCall,serviceContext);
			if(objAmountId!=null)
			{
				objAmountVal = (ArrayList<Object>) objAmountId;
				if (!objAmountVal.isEmpty() && objAmountVal.get(1)!= null){
					amountId = (String)(((ArrayList)objAmountVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 External Max amount"+ amountId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return amountId;
	}

	/**
	 * MS360 Maximum dollar limit....
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getMaxUntilDollarLimit(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAmountId = cacheObj.getCacheData("getMaxUntilDollarLimit",isDirectDBCall,serviceContext);
			if(objAmountId!=null)
			{
				objAmountVal = (ArrayList<Object>) objAmountId;
				if (!objAmountVal.isEmpty() && objAmountVal.get(1)!= null){
					amountId = (String)(((ArrayList)objAmountVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Maximum dollar limit.."+ amountId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return amountId;
	}

	/**
	 * MS360 Internal Txn Cut off time....
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getIntTxnCutOffTime(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String cutOffTimeId = "";
		Object objCutOffTimeId = null; 
		ArrayList<Object> objCutOffTimeVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objCutOffTimeId = cacheObj.getCacheData("getInternalCutOffTime",isDirectDBCall,serviceContext);
			if(objCutOffTimeId!=null)
			{
				objCutOffTimeVal = (ArrayList<Object>) objCutOffTimeId;
				if (!objCutOffTimeVal.isEmpty() && objCutOffTimeVal.get(1)!= null){
					cutOffTimeId = (String)(((ArrayList)objCutOffTimeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Internal Txn Cut off time.."+ cutOffTimeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return cutOffTimeId;
	}

	/**
	 * MS360 External Txn Cut off time......
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getACHTxnCutOffTime(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String cutOffTimeId = "";
		Object objCutOffTimeId = null; 
		ArrayList<Object> objCutOffTimeVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objCutOffTimeId = cacheObj.getCacheData("getExternalCutOffTime",isDirectDBCall,serviceContext);
			if(objCutOffTimeId!=null)
			{
				objCutOffTimeVal = (ArrayList<Object>) objCutOffTimeId;
				if (!objCutOffTimeVal.isEmpty() && objCutOffTimeVal.get(1)!= null){
					cutOffTimeId = (String)(((ArrayList)objCutOffTimeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 External Txn Cut off time.."+ cutOffTimeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return cutOffTimeId;
	}

	/**
	 * MS360 Expiry Time....
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getTxnExpiryPeriod(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String expiryTimeId = "";
		Object objExpiryTimeId = null; 
		ArrayList<Object> objExpiryTimeVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objExpiryTimeId = cacheObj.getCacheData("getTxnExpiryPeriod",isDirectDBCall,serviceContext);
			if(objExpiryTimeId!=null)
			{
				objExpiryTimeVal = (ArrayList<Object>) objExpiryTimeId;
				if (!objExpiryTimeVal.isEmpty() && objExpiryTimeVal.get(1)!= null){
					expiryTimeId = (String)(((ArrayList)objExpiryTimeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Expiry Time."+ expiryTimeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return expiryTimeId;
	}

	/**
	 * MS360 ACH Txn Settle Period...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getACHTxnSettlePeriod(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String settlePeriodId = "";
		Object objSettlePeriodId = null; 
		ArrayList<Object> objSettlePeriodVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objSettlePeriodId = cacheObj.getCacheData("getACHTxnSettlePeriod",isDirectDBCall,serviceContext);
			if(objSettlePeriodId!=null)
			{
				objSettlePeriodVal = (ArrayList<Object>) objSettlePeriodId;
				if (!objSettlePeriodVal.isEmpty() && objSettlePeriodVal.get(1)!= null){
					settlePeriodId = (String)(((ArrayList)objSettlePeriodVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 ACH Txn Settle Period"+ settlePeriodId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return settlePeriodId;
	}

	/**
	 * MS360 Max Future Days for Internal Txn......
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getTxnMaxFtrDts(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String futureDaysId = "";
		Object objFutureDaysId = null; 
		ArrayList<Object> objFutureDaysVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objFutureDaysId = cacheObj.getCacheData("getMaxFtrDts",isDirectDBCall,serviceContext);
			if(objFutureDaysId!=null)
			{
				objFutureDaysVal = (ArrayList<Object>) objFutureDaysId;
				if (!objFutureDaysVal.isEmpty() && objFutureDaysVal.get(1)!= null){
					futureDaysId = (String)(((ArrayList)objFutureDaysVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Max Future Days for Internal Txn."+ futureDaysId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return futureDaysId;
	}

	/**
	 * MS360 Max Future Days for External Txn....
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getACHTxnMaxFtrDts(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String futureDaysId = "";
		Object objFutureDaysId = null; 
		ArrayList<Object> objFutureDaysVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objFutureDaysId = cacheObj.getCacheData("getMaxFtrDtsACH",isDirectDBCall,serviceContext);
			if(objFutureDaysId!=null)
			{
				objFutureDaysVal = (ArrayList<Object>) objFutureDaysId;
				if (!objFutureDaysVal.isEmpty() && objFutureDaysVal.get(1)!= null){
					futureDaysId = (String)(((ArrayList)objFutureDaysVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Max Future Days for External Txn."+ futureDaysId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return futureDaysId;
	}

	/**
	 * MS360 Internal Holiday List....
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static ArrayList getInternalHolidayList(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		Object objHolidayDaysId = null; 
		ArrayList<Object> objHolidayDaysVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objHolidayDaysId = cacheObj.getCacheData("getInternalHolidaysListObj",isDirectDBCall,serviceContext);
			objHolidayDaysVal = (ArrayList<Object>) objHolidayDaysId;
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Internal Holiday List."+ objHolidayDaysVal);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return objHolidayDaysVal;
	}

	/**
	 * MS360 External Holiday List....
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static ArrayList getACHHolidayList(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		Object objHolidayDaysId = null; 
		ArrayList<Object> objHolidayDaysVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objHolidayDaysId = cacheObj.getCacheData("getExternalHolidaysListObj",isDirectDBCall,serviceContext);
			objHolidayDaysVal = (ArrayList<Object>) objHolidayDaysId;
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 External Holiday List."+ objHolidayDaysVal);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return objHolidayDaysVal;
	}

	/**
	 * Gets all the transfer type required to call RSA
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static ArrayList getRSATxnTypes(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String transfer_Types = " ";
		Object output = null; 
		ArrayList<Object> outputList = new ArrayList<Object>();
		ArrayList transfer_Type_List = new ArrayList();
		try
		{
			CacheObject cacheObj = new CacheObject();
			output = cacheObj.getCacheData("getRSATxnTypes",isDirectDBCall,serviceContext);
			if(output!=null)
			{
				outputList = (ArrayList<Object>)output;
				if (!outputList.isEmpty() && outputList.get(1)!= null)
				{
					transfer_Types = (String)(((ArrayList)outputList.get(1)).get(0));
					if(transfer_Types!=null){
						if( transfer_Types.trim().indexOf(",")!=-1) {
							String[] txnTypeArr = transfer_Types.split(",");
							for(int i=0; i<txnTypeArr.length ; i++){
								transfer_Type_List.add(txnTypeArr[i]);
							}
						}
						else {
							transfer_Type_List.add(transfer_Types.trim());
						}
					}
				}
				EBWLogger.logDebug("WSDefaultInputsMap","RSA Transfer Type.."+ transfer_Type_List.toString());
			} 
		}
		catch (Exception exception) {
			throw exception;
		}
		return transfer_Type_List;
	}

	/**
	 * Application ID Input to PLA Web Service
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getPLAAppID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String appId = "";
		Object objAppId = null; 
		ArrayList<Object> appIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAppId = cacheObj.getCacheData("getPLAAppID",isDirectDBCall,serviceContext);
			if(objAppId!=null)
			{
				appIdVal = (ArrayList<Object>) objAppId;
				if (!appIdVal.isEmpty() && appIdVal.get(1)!= null){
					appId = (String)(((ArrayList)appIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Application ID Input to PLA Web Service "+ appId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return appId;
	}

	/**
	 * Server IP Input to PLA Web Service 
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getPLARequestHost(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String serverIp = "";
		Object objServerIp = null; 
		ArrayList<Object> serverIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objServerIp = cacheObj.getCacheData("getPLARequestHost",isDirectDBCall,serviceContext);
			if(objServerIp!=null)
			{
				serverIdVal = (ArrayList<Object>) objServerIp;
				if (!serverIdVal.isEmpty() && serverIdVal.get(1)!= null){
					serverIp = (String)(((ArrayList)serverIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Request Host Input to PLA Web Service "+ serverIp);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return serverIp;
	}

	/**
	 * MS360 Internal Txn Cut off time....
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getPLATxnCutOffTime(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		String cutOffTimeId = "";
		Object objCutOffTimeId = null; 
		ArrayList<Object> objCutOffTimeVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objCutOffTimeId = cacheObj.getCacheData("getInternalCutOffTimePLA",isDirectDBCall,serviceContext);
			if(objCutOffTimeId!=null)
			{
				objCutOffTimeVal = (ArrayList<Object>) objCutOffTimeId;
				if (!objCutOffTimeVal.isEmpty() && objCutOffTimeVal.get(1)!= null){
					cutOffTimeId = (String)(((ArrayList)objCutOffTimeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 PLA Txn Cut off time.."+ cutOffTimeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return cutOffTimeId;
	}

	/**
	 * User ID Input to PLA Web Service
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getPLAUserID(Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{ 
		String userId = "";
		Object objUserId = null; 
		ArrayList<Object> userIdVal = new ArrayList<Object>();
		try
		{
			CacheObject cacheObj = new CacheObject();
			objUserId = cacheObj.getCacheData("getPLAViewUserID",isDirectDBCall,serviceContext);
			if(objUserId!=null)
			{
				userIdVal = (ArrayList<Object>) objUserId;
				if (!userIdVal.isEmpty() && userIdVal.get(1)!= null){
					userId = (String)(((ArrayList)userIdVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","User ID Input to PLA Web Service"+ userId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return userId;
	}
}

