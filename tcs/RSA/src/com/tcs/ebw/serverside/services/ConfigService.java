/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 * 
 * This class is used to retrieve Service configuration information
 * by creating and using a local connection. Local Connection can be
 * a database, MQ, File etc based on the settings in ebw.properties file. 
 * 
 */

package com.tcs.ebw.serverside.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.MissingResourceException;
import java.util.Set;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.serverside.connections.DatabaseConnection;
import com.tcs.ebw.serverside.exception.ServiceNotFoundException;
import com.tcs.ebw.serverside.factory.EBWConnectionFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.services.EBWAbstractService;

public class ConfigService extends EBWAbstractService {

	private static HashMap systemInfoCache = new HashMap();
	private static HashMap serviceInfoCache = new HashMap();
	private static HashMap systemPropMap = new HashMap();
	/**
	 * This method is used to get system information from configuration database
	 * using local connection and populate in Hashmap and return
	 * 
	 * @param params -
	 *            Contains service id for reading configuration information.
	 * @return - Returns a HashMap containing all the system information for a
	 *         particular service id.
	 */
	public LinkedHashMap getSystemInfo(LinkedHashMap params)
	throws SQLException, Exception {
		EBWLogger.trace(this, "Getting SystemInformation for " + params);
		EBWLogger.trace(this, "Getting SystemInformation Start Time "
				+ (new Date()).getTime());
		LinkedHashMap systemInfo = new LinkedHashMap();
		String serviceId = (String) params.get("serviceId".toUpperCase());
		//Statement stat = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {

			//StringBuffer query = new StringBuffer();
			String strServiceId = null;

			if (serviceId.indexOf("_") == -1)
				strServiceId = PropertyFileReader
				.getProperty("DEFAULT_SYSTEM_ID");
			else
				strServiceId = serviceId.substring(0, serviceId.indexOf("_"));



			// code added by  prasad on 31st march-09 for getting the system information from externalized location

			boolean propService = true;
			boolean systemInfoExt = false;
			String[] servId = null;
			String extserv=null;
			String systemInfoPropFile = null;

			try
			{
				extserv = PropertyFileReader.getProperty("ExternalisedServiceId").trim();
				systemInfoPropFile = PropertyFileReader.getProperty("ExternalisedSystemPropFile").trim();
			}
			catch(MissingResourceException msre)
			{
				propService=false;
				//System.err.println("caught missing resource exception ");
				EBWLogger.logDebug(this," The resource bundle key \"ExternalisedServiceId\" or \"ExternalisedSystemPropInfo\" is missing in ebw.properties file ");
			} 

			if(extserv!=null&& extserv!="" && propService)
			{
				servId=extserv.split(",");
				for(int i=0;i<servId.length;i++)
				{
					if(servId[i].equals(strServiceId))
						systemInfoExt=true;		
				}

				if(systemPropMap.size()==0 &&systemInfoExt)
				{

					try
					{
						systemPropMap = PropertyFileReader.getProperties(systemInfoPropFile);
					}
					catch(Exception mre)
					{
						systemInfoExt=false;
						//	System.err.println("caught missing resource exception ");
						EBWLogger.logDebug(this," The resource bundle file mentioned in \"ExternalisedSystemPropInfo\" key does not exists or the key \"ExternalisedSystemPropInfo\" is abesent in ebw.properties ");
					}
				}

			}

			// ends here


			if (!systemInfoCache.containsKey(strServiceId)) 
			{

				// code added by prasad on 31st march-09 for getting system information from externalized location
				//System.out.println("service id not present in system cache");

				if(systemInfoExt)
				{
					//System.out.println("service info is from a prop file");
					Set set = systemPropMap.keySet();

					Iterator ite = set.iterator();

					while(ite.hasNext())
					{
						String key = ite.next().toString();

						if(key.startsWith(strServiceId+"_"))
						{
							systemInfo.put(key.split(strServiceId+"_")[1].toString(), systemPropMap.get(key).toString());
						}
					}	

				}


				else
				{
					// ends here

					//System.out.println("service info is from database");
					//System.out.println("Getting ConfigConnection..");
					//stat = getConfigConnection().createStatement();
					//System.out.println("Got connection for ConfigConnection..");

					String query = "SELECT SystemConfig.* FROM SystemConfig, SSMapping WHERE SSMapping.serviceid=? AND SSMapping.systemid=SystemConfig.systemid";

					/*	query.append("SELECT SystemConfig.* ");
					query.append(" FROM SystemConfig, SSMapping ");
					query.append(" WHERE SSMapping.serviceid='" + strServiceId
						+ "' AND SSMapping.systemid=SystemConfig.systemid ");
					 */
					EBWLogger.logDebug(this,
							"Query for getting System Information is:" + query);
					//	rs = stat.executeQuery(query.toString());
					con = getConfigConnection();
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, strServiceId);
					rs = pstmt.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();

					EBWLogger.logDebug(this,
					"Starting while loop to load systeminfo");
					while (rs.next()) 
					{
						for (int i = 1; i <= rsmd.getColumnCount(); i++)
							systemInfo.put(rsmd.getColumnName(i).toUpperCase(), rs
									.getString(i));
					}
					EBWLogger.logDebug(this, "Loaded systeminfo into Cache");
				}
				systemInfoCache.put(strServiceId, systemInfo);

			} 
			else
				systemInfo = (LinkedHashMap) systemInfoCache.get(strServiceId);

			EBWLogger.trace(this, "Finished gettting System Information for "
					+ systemInfo.get("system_type"));
			EBWLogger.trace(this, "Finish SystemInformation End Time "
					+ (new Date()).getTime());

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			EBWLogger.logError(this, "SQLException caught while getting the system information for service id  :"+serviceId+" The error message is "
					+ sqle.getCause().getMessage());
		} 
		finally {
			try {

				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if(con!=null) {
					con.close();
				}
			} catch (SQLException sqle) {
				EBWLogger.logError(this, "SQLException caught while getting the system information for service id  :"+serviceId+" The error message is "
						+ sqle.getCause().getMessage());
			}
		}
		return systemInfo;
	}

	/**
	 * This method will fetch the service information passing LinkedHashMap
	 * parameter & returns LinkedHashMap
	 * 
	 * @param params
	 * @return LinkedHashMap
	 * @throws Exception
	 */

	public LinkedHashMap getServiceInfo(LinkedHashMap params) throws Exception {
		EBWLogger.trace(this, "Getting ServiceInformation for ..." + params);

		//Statement stat = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		//StringBuffer query = new StringBuffer();
		LinkedHashMap serviceInfo = null;
		String query = null;

		String serviceId = (String) params.get("serviceId".toUpperCase());

		//	EBWLogger.logDebug(this, "serviceInfoCache" + serviceInfoCache);

		if (serviceInfoCache!=null && !serviceInfoCache.containsKey(serviceId)) {
			try {
				EBWLogger.logDebug(this,
						"ServiceInfo Not found in Cache for service :"
						+ serviceId);
				EBWLogger.logDebug(this, "Connection in for ConfigService is :"
						+ serviceConnection);
				query = "select * from ServiceDefMaster";

				//	stat = getConfigConnection().createStatement();


				//		EBWLogger.logDebug(this, "Statement is :" + query);

				//	query.append("select * from ServiceDefMaster");

				if (serviceInfoCache != null && serviceInfoCache.size() > 0)
				{
					query = "select * from ServiceDefMaster where ServiceId=?";
				}
				//query.append(" where ServiceId='" + serviceId + "'"); // Add
				// the
				// new
				// serviceId
				// from
				// DB
				// to
				// cache.


				EBWLogger.logDebug(this, "ServiceDefMaster query is :"
						+ query.toString());
				con = getConfigConnection();
				pstmt = con.prepareStatement(query);

				if (serviceInfoCache != null && serviceInfoCache.size() > 0)
				{
					pstmt.setString(1, serviceId);
				}

				//rs = stat.executeQuery(query.toString());

				rs = pstmt.executeQuery();

				if (rs != null && serviceInfo == null) {// Loading all the
					// services into cache
					// for the first time.
					EBWLogger.logDebug(this, "rs is :" + rs);
					ResultSetMetaData rsmd = rs.getMetaData();

					while (rs.next()) {
						LinkedHashMap newServiceInfo = new LinkedHashMap();
						String columnName = null;
						String serviceDefVal = null;
						String newServiceId = null;
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							columnName = rsmd.getColumnName(i).toUpperCase();
							serviceDefVal = rs.getString(i);
							if (columnName.equals("SERVICEID"))
								newServiceId = serviceDefVal;
							newServiceInfo.put(columnName, serviceDefVal);
						}
						serviceInfoCache.put(newServiceId, newServiceInfo);
					}
					serviceInfo = (LinkedHashMap) serviceInfoCache
					.get(serviceId);
				} else {

					EBWLogger.logDebug(this, "Service not Found for Service:"
							+ serviceId);
					EBWLogger.logError(this,
							"ServiceDefinition Not found for service id :"
							+ serviceId);
				}
			} catch (Exception e) {
				e.printStackTrace();
				EBWLogger.logError(this,
						" Caught an exception of type "+e+ "while getting Service information for service id :"
						+ serviceId);
				try
				{
					if (serviceConnection != null)
						((java.sql.Connection) serviceConnection).close();
				} catch (SQLException sqle) {
					sqle.printStackTrace();
					EBWLogger.logError(this, " Caught an SQLException while getting service information for service id: "+serviceId+" The exception message is"
							+ sqle.getCause().getMessage());
				}
			}
			finally {
				try {

					if (pstmt != null) {
						pstmt.close();
						pstmt = null;
					}
					if (rs != null) {
						rs.close();
						rs = null;
					}
					if(con!=null) {
						con.close();
					}
				} catch (SQLException sqle) {
					EBWLogger.logError(this, " Caught an SQLException while getting service information for service id: "+serviceId+" The exception message is"
							+ sqle.getCause().getMessage());
				}
			}

		} else if(serviceInfoCache!=null) {
			try {
				EBWLogger.logDebug(this,
						"Getting serviceInfo from Cache for service :"
						+ serviceId);
				serviceInfo = (LinkedHashMap) serviceInfoCache.get(serviceId);
			} catch (Exception e) {
				e.printStackTrace();
				EBWLogger.logError(this,
						"ServiceDefinition Not found for ServiceInfo :"
						+ serviceId);
				if (serviceConnection != null)
					((java.sql.Connection) serviceConnection).close();
			}
		}

		EBWLogger.trace(this, "Finished getting service information. ");
		return serviceInfo;
	}

	/**
	 * This method can be used by classes external to Service Object which needs
	 * service information for particular service. This function will return the
	 * service information which is already cached by the Framework.
	 * 
	 * @param serviceId -
	 *            String representing service ID for which the service row
	 *            detail is required.
	 * @return - Returns LinkedHashMap having servicedefmaster columnnames as
	 *         key and its corresponding row value as value.
	 */
	public LinkedHashMap getSvcConfigInfo(String serviceId) throws Exception {
		LinkedHashMap map = new LinkedHashMap();
		map.put("SERVICEID", serviceId);
		return getServiceInfo(map);
	}

	/**
	 * This method is used to get a Connection through Configuration Connection.
	 * 
	 * @return java.sql.Connection
	 * @throws Exception
	 */

	private java.sql.Connection getConfigConnection() throws Exception {
		LinkedHashMap configSystemInfo = new LinkedHashMap();
		try {
			configSystemInfo.put("datasource_name".toUpperCase(),
					PropertyFileReader.getProperty("datasource_name"));
			configSystemInfo.put("base_protocol".toUpperCase(),
					PropertyFileReader.getProperty("base_protocol"));
			configSystemInfo.put("ip_address".toUpperCase(), PropertyFileReader
					.getProperty("ip_address"));
			configSystemInfo.put("port_no".toUpperCase(), PropertyFileReader
					.getProperty("port_no"));
			configSystemInfo.put("system_sub_type".toUpperCase(),
					PropertyFileReader.getProperty("system_sub_type"));

			EBWLogger.logDebug("EBWServiceFactory", "configSystemInfo Map is "
					+ configSystemInfo);
			EBWLogger.logDebug("EBWServiceFactory", "System Sub Type is : "
					+ PropertyFileReader.getProperty("system_sub_type"));
			String configConnType = null;
			configConnType = PropertyFileReader.getProperty("ebwConfigType");
			return (java.sql.Connection) ((DatabaseConnection) EBWConnectionFactory
					.create(configConnType)).connect(configSystemInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * This method is used to close the Connection & return an object.
	 */
	public Object close() {
		try {
			if (serviceConnection != null)
				((java.sql.Connection) this.serviceConnection).close();

		} catch (SQLException sqle) {
			new EbwException(this, sqle).printEbwException();
		}
		return "0";
	}
}





///*
//* Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
//* This software is the confidential and proprietary information of
//* Tata Consultancy Services ("Confidential Information"). You shall not
//* disclose such Confidential Information and shall use it only in
//* accordance with the terms of the license agreement you entered into
//* with Tata Consultancy Services.
//* 
//* This class is used to retrieve Service configuration information
//* by creating and using a local connection. Local Connection can be
//* a database, MQ, File etc based on the settings in ebw.properties file. 
//* 
//*/


//package com.tcs.ebw.serverside.services;

//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedHashMap;

//import com.tcs.ebw.common.util.EBWLogger;
//import com.tcs.ebw.common.util.PropertyFileReader;
//import com.tcs.ebw.exception.EbwException;
//import com.tcs.ebw.serverside.connections.DatabaseConnection;
//import com.tcs.ebw.serverside.exception.ServiceNotFoundException;
//import com.tcs.ebw.serverside.factory.EBWConnectionFactory;
//import com.tcs.ebw.serverside.factory.IEBWService;
//import com.tcs.ebw.serverside.services.EBWAbstractService;

//public class ConfigService extends EBWAbstractService{

//private static HashMap systemInfoCache= new HashMap();
//private static HashMap serviceInfoCache= new HashMap();

///**
//* This method is used to get system information from configuration
//* database using local connection and populate in Hashmap and return
//* @param params - Contains service id for reading configuration information.  
//* @return		 - Returns a HashMap containing all the system information for
//* 				   a particular service id.
//*/
//public LinkedHashMap getSystemInfo(LinkedHashMap params) throws SQLException, Exception{
//EBWLogger.trace(this,"Getting SystemInformation for "+params);
//EBWLogger.trace(this, "Getting SystemInformation Start Time " + (new Date()).getTime());
//LinkedHashMap systemInfo = new LinkedHashMap();
//String serviceId = (String)params.get("serviceId".toUpperCase());
//Statement stat=null;
//ResultSet rs=null; 
//try{


//StringBuffer query = new StringBuffer();
//String strServiceId =null;

//if(serviceId.indexOf("_")==-1)
//strServiceId = PropertyFileReader.getProperty("DEFAULT_SYSTEM_ID");
//else
//strServiceId = serviceId.substring(0,serviceId.indexOf("_"));

//if(!systemInfoCache.containsKey(strServiceId)){
//System.out.println("Getting ConfigConnection..");
//stat = getConfigConnection().createStatement();
//System.out.println("Got connection for ConfigConnection..");
//query.append("SELECT SystemConfig.* ");
//query.append(" FROM SystemConfig, SSMapping ");
//query.append(" WHERE SSMapping.serviceid='"+strServiceId+"' AND SSMapping.systemid=SystemConfig.systemid ");					

//EBWLogger.logDebug(this,"Query for getting System Information is:"+query);
//rs = stat.executeQuery(query.toString());
//ResultSetMetaData rsmd = rs.getMetaData();

//EBWLogger.logDebug(this,"Starting while loop to load systeminfo");
//while(rs.next()){
//for(int i=1;i<=rsmd.getColumnCount();i++)
//systemInfo.put(rsmd.getColumnName(i).toUpperCase(),rs.getString(i));
//}
//EBWLogger.logDebug(this,"Loaded systeminfo into Cache");
//systemInfoCache.put(strServiceId,systemInfo);

//}
//else
//systemInfo = (LinkedHashMap)systemInfoCache.get(strServiceId);

//EBWLogger.trace(this,"Finished gettting System Information for "+systemInfo.get("system_type"));
//EBWLogger.trace(this, "Finish SystemInformation End Time " + (new Date()).getTime());
//return systemInfo;
//}catch(SQLException sqle){
//sqle.printStackTrace();
//throw new EbwException(this,sqle);
//}
//finally{
//try{
//if(rs!=null){
//rs.close();
//rs=null;
//}
//if(stat!=null){
//stat.close();
//stat=null;
//}
//}catch(SQLException sqle){
//EBWLogger.logError(this,"SQLException :"+sqle.getCause().getMessage());
//}
//}
//}

///**
//* This method will fetch the service information passing LinkedHashMap parameter & returns LinkedHashMap
//* @param params
//* @return LinkedHashMap
//* @throws Exception 
//*/

//public LinkedHashMap getServiceInfo(LinkedHashMap params) throws Exception{
//EBWLogger.trace(this,"Getting ServiceInformation for ..."+params);

//Statement stat=null;
//ResultSet rs=null;
//StringBuffer query = new StringBuffer();
//LinkedHashMap serviceInfo=null;

//String serviceId = (String)params.get("serviceId".toUpperCase());
//EBWLogger.logDebug(this,"serviceInfoCache" + serviceInfoCache);
//try{
//if(!serviceInfoCache.containsKey(serviceId)){
//EBWLogger.logDebug(this,"ServiceInfo Not found in Cache for service :"+serviceId);
//EBWLogger.logDebug(this,"Connection in for ConfigService is :"+serviceConnection);
//stat = getConfigConnection().createStatement();
//EBWLogger.logDebug(this,"Statement is :"+stat);

//query.append("select * from ServiceDefMaster");

//if(serviceInfoCache!=null && serviceInfoCache.size()>0)
//query.append(" where ServiceId='"+serviceId+"'"); // Add the new serviceId from DB to cache.

//EBWLogger.logDebug(this,"ServiceDefMaster query is :"+query.toString());

//rs = stat.executeQuery(query.toString());


//if(rs !=null && serviceInfo==null) {//Loading all the services into cache for the first time.
//EBWLogger.logDebug(this,"rs is :"+rs);
//ResultSetMetaData rsmd = rs.getMetaData();									

//while(rs.next()){
//LinkedHashMap newServiceInfo= new LinkedHashMap();				
//String columnName = null;
//String serviceDefVal = null;
//String newServiceId =null;
//for(int i=1;i<=rsmd.getColumnCount();i++){
//columnName = rsmd.getColumnName(i).toUpperCase();
//serviceDefVal = rs.getString(i);
//if(columnName.equals("SERVICEID"))
//newServiceId = serviceDefVal;
//newServiceInfo.put(columnName,serviceDefVal);
//}
//serviceInfoCache.put(newServiceId,newServiceInfo); 							
//}
//serviceInfo = (LinkedHashMap)serviceInfoCache.get(serviceId);								
//}
//else {

//EBWLogger.logDebug(this, "Service not Found for Service:"+serviceId);
//EBWLogger.logError(this,"ServiceDefinition Not found for ServiceInfo :"+serviceInfo+" or ServiceId is empty");
//}
//}
//else{
//EBWLogger.logDebug(this,"Getting serviceInfo from Cache for service :"+serviceId);
//serviceInfo = (LinkedHashMap)serviceInfoCache.get(serviceId);
//}

//EBWLogger.trace(this,"Finished getting service information. ");

//}catch(Exception e){
//e.printStackTrace();		   
//EBWLogger.logError(this,"ServiceDefinition Not found for ServiceInfo :"+serviceInfo+" or ServiceId is empty");
//}finally{
//try{
//if(rs!=null){
//rs.close();
//rs=null;
//}
//if(stat!=null){
//stat.close();
//stat=null;
//}
//if(serviceConnection!=null)
//((java.sql.Connection)serviceConnection).close();
//}catch(SQLException sqle){
//sqle.printStackTrace();
//EBWLogger.logError(this,"SQLException :"+sqle.getCause().getMessage());
//}
//}
//return serviceInfo;
//}

///**
//* This method can be used by classes external to Service Object which needs service information
//* for particular service. This function will return the service information which is  already 
//* cached by the Framework.
//* @param serviceId - String representing service ID for which the service row detail is required.
//* @return  - Returns LinkedHashMap having servicedefmaster columnnames as key and its corresponding
//* 			  row value as value.
//*/
//public LinkedHashMap getSvcConfigInfo(String serviceId) throws Exception{
//LinkedHashMap map = new LinkedHashMap();
//map.put("SERVICEID", serviceId);
//return getServiceInfo(map);
//}

///**
//* This method is used to get a Connection through Configuration Connection. 
//* @return java.sql.Connection
//* @throws Exception
//*/

//private java.sql.Connection getConfigConnection() throws Exception{
//LinkedHashMap configSystemInfo = new LinkedHashMap();
//try{
//configSystemInfo.put("datasource_name".toUpperCase(),PropertyFileReader.getProperty("datasource_name"));
//configSystemInfo.put("base_protocol".toUpperCase(),PropertyFileReader.getProperty("base_protocol"));
//configSystemInfo.put("ip_address".toUpperCase(),PropertyFileReader.getProperty("ip_address"));
//configSystemInfo.put("port_no".toUpperCase(),PropertyFileReader.getProperty("port_no"));
//configSystemInfo.put("system_sub_type".toUpperCase(),PropertyFileReader.getProperty("system_sub_type"));

//EBWLogger.logDebug("EBWServiceFactory","configSystemInfo Map is "+configSystemInfo);
//EBWLogger.logDebug("EBWServiceFactory","System Sub Type is : "+PropertyFileReader.getProperty("system_sub_type"));
//String configConnType =null;
//configConnType = PropertyFileReader.getProperty("ebwConfigType");
//return (java.sql.Connection)((DatabaseConnection)EBWConnectionFactory.create(configConnType)).connect(configSystemInfo);
//}catch(Exception e){
//e.printStackTrace();
//throw e;
//}
//}

///**
//* This method is used to close the Connection & return an object.
//*/
//public Object close() {
//try{
//if(serviceConnection!=null)
//((java.sql.Connection)this.serviceConnection).close();

//}catch(SQLException sqle){
//new EbwException(this,sqle).printEbwException();
//}
//return "0";
//}

//}
