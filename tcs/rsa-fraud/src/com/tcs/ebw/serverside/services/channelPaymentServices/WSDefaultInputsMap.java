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
	public static String getRTABAppID(ServiceContext serviceContext) throws Exception
	{
		String appId = "";
		Object objAppId = null; 
		ArrayList<Object> appIdVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAppId = cacheObj.getCacheData("getRTABUserID",isDirectDBCall,serviceContext);
			if(objAppId!=null)
			{
				appIdVal = (ArrayList<Object>) objAppId;
				if (!appIdVal.isEmpty() && appIdVal.get(1)!= null){
					appId = (String)(((ArrayList)appIdVal.get(1)).get(0));
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
	public static String getAcntPlatingUserID(ServiceContext serviceContext) throws Exception
	{
		String userId = "";
		Object objUserId = null; 
		ArrayList<Object> userIdVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getAcntPlatingAppID(ServiceContext serviceContext) throws Exception
	{
		String appId = "";
		Object objAppId = null; 
		ArrayList<Object> appIdVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getAcntPlatingServerIP(ServiceContext serviceContext) throws Exception
	{
		String serverIp = "";
		Object objServerIp = null; 
		ArrayList<Object> serverIdVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getAcntViewUserID(ServiceContext serviceContext) throws Exception
	{
		String userId = "";
		Object objUserId = null; 
		ArrayList<Object> userIdVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getAcntViewAppID(ServiceContext serviceContext) throws Exception
	{
		String appId = "";
		Object objAppId = null; 
		ArrayList<Object> appIdVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getAcntViewAuthAppID(ServiceContext serviceContext) throws Exception
	{
		String authAppId = "";
		Object objAuthAppId = null; 
		ArrayList<Object> authAppIdVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getAcntViewAuthVerb(ServiceContext serviceContext) throws Exception
	{
		String authVerb = "";
		Object objAuthVerb = null; 
		ArrayList<Object> authVerbVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getAcntViewWebServerID(ServiceContext serviceContext) throws Exception
	{
		String webServerId = "";
		Object objWebServerId = null; 
		ArrayList<Object> webServerIdVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getBusRuleCntrlUserID(ServiceContext serviceContext) throws Exception
	{
		String userId = "";
		Object objUserId = null; 
		ArrayList<Object> userIdVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getBusRuleCntrlAppID(ServiceContext serviceContext) throws Exception
	{
		String appId = "";
		Object objAppId = null; 
		ArrayList<Object> appIdVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getBusRuleCntrlServerID(ServiceContext serviceContext) throws Exception
	{
		String serverId = "";
		Object objServerId = null; 
		ArrayList<Object> serverIdVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	 * callerPgmId Input to Check number...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getCheckNumCallerPgmId(ServiceContext serviceContext) throws Exception
	{
		String callerPgmId = "";
		Object objcallerPgmId = null; 
		ArrayList<Object> callerPgmVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objcallerPgmId = cacheObj.getCacheData("getCheckNumCallerPgmId",isDirectDBCall,serviceContext);
			if(objcallerPgmId!=null)
			{
				callerPgmVal = (ArrayList<Object>) objcallerPgmId;
				if (!callerPgmVal.isEmpty() && callerPgmVal.get(1)!= null){
					callerPgmId = (String)(((ArrayList)callerPgmVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Caller PgmId for the Check Number "+ callerPgmId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return callerPgmId;
	}

	/**
	 * callerAppId Input to Check number...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getCheckNumCallerAppId(ServiceContext serviceContext) throws Exception
	{
		String callerAppId = "";
		Object objcallerAppId = null; 
		ArrayList<Object> callerAppVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objcallerAppId = cacheObj.getCacheData("getCheckNumCallerAppId",isDirectDBCall,serviceContext);
			if(objcallerAppId!=null)
			{
				callerAppVal = (ArrayList<Object>) objcallerAppId;
				if (!callerAppVal.isEmpty() && callerAppVal.get(1)!= null){
					callerAppId = (String)(((ArrayList)callerAppVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Caller AppId for the Check Number "+ callerAppId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return callerAppId;
	}

	/**
	 * printTypeId Input to Print Check...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getPrintType(ServiceContext serviceContext) throws Exception
	{
		String printTypeId = "";
		Object objprintTypeId = null; 
		ArrayList<Object> printTypeVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objprintTypeId = cacheObj.getCacheData("getPrintType",isDirectDBCall,serviceContext);
			if(objprintTypeId!=null)
			{
				printTypeVal = (ArrayList<Object>) objprintTypeId;
				if (!printTypeVal.isEmpty() && printTypeVal.get(1)!= null){
					printTypeId = (String)(((ArrayList)printTypeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Caller AppId for the Check Number "+ printTypeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return printTypeId;
	}

	/**
	 * printCreatePdfId Input to Print Check...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getPrintCreatePdf(ServiceContext serviceContext) throws Exception
	{
		String printCreatePdfId = "";
		Object objprintCreatePdfId = null; 
		ArrayList<Object> printCreatePdfVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objprintCreatePdfId = cacheObj.getCacheData("getPrintCreatePdf",isDirectDBCall,serviceContext);
			if(objprintCreatePdfId!=null)
			{
				printCreatePdfVal = (ArrayList<Object>) objprintCreatePdfId;
				if (!printCreatePdfVal.isEmpty() && printCreatePdfVal.get(1)!= null){
					printCreatePdfId = (String)(((ArrayList)printCreatePdfVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","CreatePDF for the Print Check "+ printCreatePdfId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return printCreatePdfId;
	}

	/**
	 * printEncryptId Input to Print Check...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getPrintEncrypt(ServiceContext serviceContext) throws Exception
	{
		String printEncryptId = "";
		Object objPrintEncryptId = null; 
		ArrayList<Object> printEncryptVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objPrintEncryptId = cacheObj.getCacheData("getPrintEncrypt",isDirectDBCall,serviceContext);
			if(objPrintEncryptId!=null)
			{
				printEncryptVal = (ArrayList<Object>) objPrintEncryptId;
				if (!printEncryptVal.isEmpty() && printEncryptVal.get(1)!= null){
					printEncryptId = (String)(((ArrayList)printEncryptVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Encrypt for the Print Check "+ printEncryptId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return printEncryptId;
	}


	/**
	 * LegalTxtLineId Input to Print Check...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getLegalTxtLine1(ServiceContext serviceContext) throws Exception
	{
		String legalTxtLineId = "";
		Object objLegalTxtLineId = null; 
		ArrayList<Object> legalTxtLineVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objLegalTxtLineId = cacheObj.getCacheData("getLegalTxtLine1",isDirectDBCall,serviceContext);
			if(objLegalTxtLineId!=null)
			{
				legalTxtLineVal = (ArrayList<Object>) objLegalTxtLineId;
				if (!legalTxtLineVal.isEmpty() && legalTxtLineVal.get(1)!= null){
					legalTxtLineId = (String)(((ArrayList)legalTxtLineVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","LegalTxt Line2 for the Print Check "+ legalTxtLineId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return legalTxtLineId;
	}


	/**
	 * LegalTxtLine2Id Input to Print Check...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getLegalTxtLine2(ServiceContext serviceContext) throws Exception
	{
		String legalTxtLine2Id = "";
		Object objLegalTxtLine2Id = null; 
		ArrayList<Object> legalTxtLine2Val = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objLegalTxtLine2Id = cacheObj.getCacheData("getLegalTxtLine2",isDirectDBCall,serviceContext);
			if(objLegalTxtLine2Id!=null)
			{
				legalTxtLine2Val = (ArrayList<Object>) objLegalTxtLine2Id;
				if (!legalTxtLine2Val.isEmpty() && legalTxtLine2Val.get(1)!= null){
					legalTxtLine2Id = (String)(((ArrayList)legalTxtLine2Val.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","LegalTxt Line2 for the Print Check "+ legalTxtLine2Id);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return legalTxtLine2Id;
	}


	/**
	 * InquiryNumId Input to Print Check...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getInquiryNum(ServiceContext serviceContext) throws Exception
	{
		String inquiryNumId = "";
		Object objInquiryNumId = null; 
		ArrayList<Object> inquiryNumVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objInquiryNumId = cacheObj.getCacheData("getInquiryNum",isDirectDBCall,serviceContext);
			if(objInquiryNumId!=null)
			{
				inquiryNumVal = (ArrayList<Object>) objInquiryNumId;
				if (!inquiryNumVal.isEmpty() && inquiryNumVal.get(1)!= null){
					inquiryNumId = (String)(((ArrayList)inquiryNumVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Inquiry for the Print Check "+ inquiryNumId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return inquiryNumId;
	}

	/**
	 * PlanCodeId Input 
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getACHDefaultPlanCode(ServiceContext serviceContext) throws Exception
	{
		String planCodeId = "";
		Object objPlanCodeId = null; 
		ArrayList<Object> planCodeVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objPlanCodeId = cacheObj.getCacheData("getACHDefaultPlanCode",isDirectDBCall,serviceContext);
			if(objPlanCodeId!=null)
			{
				planCodeVal = (ArrayList<Object>) objPlanCodeId;
				if (!planCodeVal.isEmpty() && planCodeVal.get(1)!= null){
					planCodeId = (String)(((ArrayList)planCodeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","DefaultPlanCode for IRA ACH"+ planCodeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return planCodeId;
	}

	/**
	 * PlanCodeId Input 
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getCheckDefaultIRAPlanCode(ServiceContext serviceContext) throws Exception
	{
		String planCodeId = "";
		Object objPlanCodeId = null; 
		ArrayList<Object> planCodeVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objPlanCodeId = cacheObj.getCacheData("getCheckDefaultIRAPlanCode",isDirectDBCall,serviceContext);
			if(objPlanCodeId!=null)
			{
				planCodeVal = (ArrayList<Object>) objPlanCodeId;
				if (!planCodeVal.isEmpty() && planCodeVal.get(1)!= null){
					planCodeId = (String)(((ArrayList)planCodeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","DefaultPlanCode for IRA Checks"+ planCodeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return planCodeId;
	}

	/**
	 * Certified check application name...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getPrintCertifiedAppName(ServiceContext serviceContext) throws Exception
	{
		String certAppName = "";
		Object certAppNameId = null; 
		ArrayList<Object> certAppNameVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			certAppNameId = cacheObj.getCacheData("getPrintCertifiedAppName",isDirectDBCall,serviceContext);
			if(certAppNameId!=null)
			{
				certAppNameVal = (ArrayList<Object>) certAppNameId;
				if (!certAppNameVal.isEmpty() && certAppNameVal.get(1)!= null){
					certAppName = (String)(((ArrayList)certAppNameVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Application Name for Certified check.."+ certAppName);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return certAppName;
	}

	/**
	 * Non Certified Check Application name.. 
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getPrintNonCertifiedAppName(ServiceContext serviceContext) throws Exception
	{
		String nonCertAppName = "";
		Object objnonCertAppNameId = null; 
		ArrayList<Object> objnonCertAppVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objnonCertAppNameId = cacheObj.getCacheData("getPrintNonCertifiedAppName",isDirectDBCall,serviceContext);
			if(objnonCertAppNameId!=null)
			{
				objnonCertAppVal = (ArrayList<Object>) objnonCertAppNameId;
				if (!objnonCertAppVal.isEmpty() && objnonCertAppVal.get(1)!= null){
					nonCertAppName = (String)(((ArrayList)objnonCertAppVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Application Name for NonCertified check.."+ nonCertAppName);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return nonCertAppName;
	}

	/**
	 * MS360 Client Interaction Center...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getMS360ContactCenter(ServiceContext serviceContext) throws Exception
	{
		String contactCenterId = "";
		Object objcontactCenterId = null; 
		ArrayList<Object> objcontactCenterVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objcontactCenterId = cacheObj.getCacheData("getMS360ContactCenter",isDirectDBCall,serviceContext);
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
	public static String getIntTxnMinAmount(ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getIntTxnMaxAmount(ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getACHTxnMinAmount(ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getACHTxnMaxAmount(ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	 * MS360 Check Min Amount...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getCheckTxnMinAmount(ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAmountId = cacheObj.getCacheData("getCheckMinAmount",isDirectDBCall,serviceContext);
			if(objAmountId!=null)
			{
				objAmountVal = (ArrayList<Object>) objAmountId;
				if (!objAmountVal.isEmpty() && objAmountVal.get(1)!= null){
					amountId = (String)(((ArrayList)objAmountVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Check Min Amount"+ amountId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return amountId;
	}
	/**
	 * MS360 Check Max Amout...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getCheckTxnMaxAmount(ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objAmountId = cacheObj.getCacheData("getCheckMaxAmount",isDirectDBCall,serviceContext);
			if(objAmountId!=null)
			{
				objAmountVal = (ArrayList<Object>) objAmountId;
				if (!objAmountVal.isEmpty() && objAmountVal.get(1)!= null){
					amountId = (String)(((ArrayList)objAmountVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Check Max Amout"+ amountId);
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
	public static String getMaxUntilDollarLimit(ServiceContext serviceContext) throws Exception
	{
		String amountId = "";
		Object objAmountId = null; 
		ArrayList<Object> objAmountVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getIntTxnCutOffTime(ServiceContext serviceContext) throws Exception
	{
		String cutOffTimeId = "";
		Object objCutOffTimeId = null; 
		ArrayList<Object> objCutOffTimeVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getACHTxnCutOffTime(ServiceContext serviceContext) throws Exception
	{
		String cutOffTimeId = "";
		Object objCutOffTimeId = null; 
		ArrayList<Object> objCutOffTimeVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	 * MS360 Local Check Cut off time...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getLocalCheckCutOffTime(ServiceContext serviceContext) throws Exception
	{
		String cutOffTimeId = "";
		Object objCutOffTimeId = null; 
		ArrayList<Object> objCutOffTimeVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objCutOffTimeId = cacheObj.getCacheData("getLocalCheckCutOffTime",isDirectDBCall,serviceContext);
			if(objCutOffTimeId!=null)
			{
				objCutOffTimeVal = (ArrayList<Object>) objCutOffTimeId;
				if (!objCutOffTimeVal.isEmpty() && objCutOffTimeVal.get(1)!= null){
					cutOffTimeId = (String)(((ArrayList)objCutOffTimeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Local Check Cut off time.."+ cutOffTimeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return cutOffTimeId;
	}

	/**
	 * MS360 Local Check Print Cut off time....
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getLocalCheckPrintCutOffTime(ServiceContext serviceContext) throws Exception
	{
		String cutOffTimeId = "";
		Object objCutOffTimeId = null; 
		ArrayList<Object> objCutOffTimeVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objCutOffTimeId = cacheObj.getCacheData("getLocalCheckPrintCutOffTime",isDirectDBCall,serviceContext);
			if(objCutOffTimeId!=null)
			{
				objCutOffTimeVal = (ArrayList<Object>) objCutOffTimeId;
				if (!objCutOffTimeVal.isEmpty() && objCutOffTimeVal.get(1)!= null){
					cutOffTimeId = (String)(((ArrayList)objCutOffTimeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Local Check Print Cut off time.."+ cutOffTimeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return cutOffTimeId;
	}

	/**
	 * MS360 Regional Check Cut off time.....
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getRegCheckCutOffTime(ServiceContext serviceContext) throws Exception
	{
		String cutOffTimeId = "";
		Object objCutOffTimeId = null; 
		ArrayList<Object> objCutOffTimeVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objCutOffTimeId = cacheObj.getCacheData("getRegCheckCutOffTime",isDirectDBCall,serviceContext);
			if(objCutOffTimeId!=null)
			{
				objCutOffTimeVal = (ArrayList<Object>) objCutOffTimeId;
				if (!objCutOffTimeVal.isEmpty() && objCutOffTimeVal.get(1)!= null){
					cutOffTimeId = (String)(((ArrayList)objCutOffTimeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Regional Check Cut off time.."+ cutOffTimeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return cutOffTimeId;
	}

	/**
	 * MS360 Regional Check Print Cut off time...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getRegCheckPrintCutOffTime(ServiceContext serviceContext) throws Exception
	{
		String cutOffTimeId = "";
		Object objCutOffTimeId = null; 
		ArrayList<Object> objCutOffTimeVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objCutOffTimeId = cacheObj.getCacheData("getRegCheckPrintCutOffTime",isDirectDBCall,serviceContext);
			if(objCutOffTimeId!=null)
			{
				objCutOffTimeVal = (ArrayList<Object>) objCutOffTimeId;
				if (!objCutOffTimeVal.isEmpty() && objCutOffTimeVal.get(1)!= null){
					cutOffTimeId = (String)(((ArrayList)objCutOffTimeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Regional Check Print Cut off time.."+ cutOffTimeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return cutOffTimeId;
	}

	/**
	 * MS360 Check Expiry Time....
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getChkExpiryPeriod(ServiceContext serviceContext) throws Exception
	{
		String expiryTimeId = "";
		Object objExpiryTimeId = null; 
		ArrayList<Object> objExpiryTimeVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objExpiryTimeId = cacheObj.getCacheData("getChkExpiryPeriod",isDirectDBCall,serviceContext);
			if(objExpiryTimeId!=null)
			{
				objExpiryTimeVal = (ArrayList<Object>) objExpiryTimeId;
				if (!objExpiryTimeVal.isEmpty() && objExpiryTimeVal.get(1)!= null){
					expiryTimeId = (String)(((ArrayList)objExpiryTimeVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Check Expiry Time."+ expiryTimeId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return expiryTimeId;
	}

	/**
	 * MS360 Expiry Time....
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getTxnExpiryPeriod(ServiceContext serviceContext) throws Exception
	{
		String expiryTimeId = "";
		Object objExpiryTimeId = null; 
		ArrayList<Object> objExpiryTimeVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getACHTxnSettlePeriod(ServiceContext serviceContext) throws Exception
	{
		String settlePeriodId = "";
		Object objSettlePeriodId = null; 
		ArrayList<Object> objSettlePeriodVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	 * MS360 Max Future Days for Check Txn.
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getCheckTxnMaxFtrDts(ServiceContext serviceContext) throws Exception
	{
		String futureDaysId = "";
		Object objFutureDaysId = null; 
		ArrayList<Object> objFutureDaysVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objFutureDaysId = cacheObj.getCacheData("getMaxFtrDtsChk",isDirectDBCall,serviceContext);
			if(objFutureDaysId!=null)
			{
				objFutureDaysVal = (ArrayList<Object>) objFutureDaysId;
				if (!objFutureDaysVal.isEmpty() && objFutureDaysVal.get(1)!= null){
					futureDaysId = (String)(((ArrayList)objFutureDaysVal.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Max Future Days for Check Txn."+ futureDaysId);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return futureDaysId;
	}

	/**
	 * MS360 Max Future Days for Internal Txn......
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getIntTxnMaxFtrDts(ServiceContext serviceContext) throws Exception
	{
		String futureDaysId = "";
		Object objFutureDaysId = null; 
		ArrayList<Object> objFutureDaysVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static String getACHTxnMaxFtrDts(ServiceContext serviceContext) throws Exception
	{
		String futureDaysId = "";
		Object objFutureDaysId = null; 
		ArrayList<Object> objFutureDaysVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static ArrayList getInternalHolidayList(ServiceContext serviceContext) throws Exception
	{
		Object objHolidayDaysId = null; 
		ArrayList<Object> objHolidayDaysVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	public static ArrayList getACHHolidayList(ServiceContext serviceContext) throws Exception
	{
		Object objHolidayDaysId = null; 
		ArrayList<Object> objHolidayDaysVal = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
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
	 * Cancel Transaction flag while system rejecting the transactions...
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static String getCancelTxnFlag(ServiceContext serviceContext) throws Exception
	{
		String cancelTxnFlag = "";
		Object cancelTxnObj = null; 
		ArrayList<Object> cancelTxnIndList = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			cancelTxnObj = cacheObj.getCacheData("getCancelTxnFlag",isDirectDBCall,serviceContext);
			if(cancelTxnObj!=null)
			{
				cancelTxnIndList = (ArrayList<Object>) cancelTxnObj;
				if (!cancelTxnIndList.isEmpty() && cancelTxnIndList.get(1)!= null){
					cancelTxnFlag = (String)(((ArrayList)cancelTxnIndList.get(1)).get(0));
				}
			}
			EBWLogger.logDebug("WSDefaultInputsMap","Cancel Transaction flag for system reject transactions.."+ cancelTxnFlag);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return cancelTxnFlag;
	}

	/**
	 * MS360 Approver Role Descriptions
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static ArrayList getApprRoleDesc(ServiceContext serviceContext) throws Exception
	{
		Object objApprRoleDesc = null; 
		ArrayList<Object> objApprRoleList = new ArrayList<Object>();
		Boolean isDirectDBCall = true;
		try
		{
			CacheObject cacheObj = new CacheObject();
			objApprRoleDesc = cacheObj.getCacheData("getApproverRoleDesc",isDirectDBCall,serviceContext);
			objApprRoleList = (ArrayList<Object>) objApprRoleDesc;
			EBWLogger.logDebug("WSDefaultInputsMap","MS360 Approver Role Description List."+ objApprRoleList);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return objApprRoleList;
	}
}