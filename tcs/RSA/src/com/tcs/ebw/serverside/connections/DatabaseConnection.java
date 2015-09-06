//Source File Name:   DatabaseConnection.java
package com.tcs.ebw.serverside.connections;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.serverside.exception.ClusterUrlException;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.serverside.exception.*;
import com.tcs.ebw.serverside.factory.IEBWConnection;
import java.sql.Connection;
import java.util.*;
import javax.naming.*;
import javax.sql.DataSource;

public class DatabaseConnection implements IEBWConnection
{
	private int ds_try=0;

	public DatabaseConnection()
	{
		dbConnection = null;
		dbConnectionCache = new HashMap();
		systemInfo = null;
	}


	public Object connect(LinkedHashMap systemInfo)
	throws Exception
	{
		EBWLogger.trace(this, "Entered DatabaseConnection.connect(params)");
		EBWLogger.trace(this, "systeminfo : " + systemInfo);
		//EBWLogger.trace(this, "Start Time " + (new Date()).getTime());
		this.systemInfo = systemInfo;
		String system_sub_type = (String)systemInfo.get("SYSTEM_SUB_TYPE");
		datasource_name = (String)systemInfo.get("DATASOURCE_NAME");
		try
		{
			dbConnection = getConnectionFromDSCache(system_sub_type, datasource_name);
		}
		catch(DataSourceCreationException e)
		{
			throw e;
		}
		//EBWLogger.trace(this, "End Time " + (new Date()).getTime());
		EBWLogger.trace(this, "Finished DatabaseConnection.connect(params)");
		return dbConnection;
	}

	public Object disconnect()
	throws Exception
	{
		if(dbConnection != null)
			dbConnection.close();
		return null;
	}

	private Connection getConnectionFromDSCache(String system_sub_type, String datasource_name)
	throws Exception
	{
		EBWLogger.trace(this, "Into getConnectionFromDSCache");
		EBWLogger.trace(this, "Param1 : system_sub_type =" + system_sub_type);
		EBWLogger.trace(this, "Param2 : datasource_name =" + datasource_name);
		//EBWLogger.trace(this, "Start Time " + (new Date()).getTime());
		Connection conn = null;
		DataSource ds=null;
		try
		{
			ds = getDatasourceFromCache(system_sub_type, datasource_name);
			//EBWLogger.trace(this, "Time between DS and Connection :" + (new Date()).getTime());

			//Get the property to enable the connection leak trace on...
			boolean connectionTraceOn = Boolean.parseBoolean(System.getProperty("bancs.channels.connectionTrace", "false")); // default value as false
			if(connectionTraceOn) {
				conn = new MyConnection(ds.getConnection()); 
			}
			else {
				conn = ds.getConnection(); 
			}
		}
		catch(DataSourceCreationException e)
		{
			EBWLogger.logDebug(this, "Datasource did not return Connection.. so trying 1 more time. ");
			// Try one more time..
			// First remove Datasource from Cache.. GetDatasource again and put in cache.
			if(dsCache.containsKey(datasource_name) && ds_try==0){
				dsCache.remove(datasource_name);
				ctxCache.remove(datasource_name);
				ds_try+=1;
				ds=getDatasourceFromCache(system_sub_type, datasource_name);

				//Get the property to enable the connection leak trace on...
				boolean connectionTraceOn = Boolean.parseBoolean(System.getProperty("bancs.channels.connectionTrace", "false")); // default value as false
				if(connectionTraceOn) {
					conn = new MyConnection(ds.getConnection()); 
				}
				else {
					conn = ds.getConnection(); 
				}
			}
			else{
				EBWLogger.logDebug(this,"Tried getting DS for second time, but did not get Connection");
				ds_try=0;
				throw new EbwException(this, new DataSourceCreationException(datasource_name));
			}
		}
		//EBWLogger.trace(this, "End Time " + (new Date()).getTime());
		EBWLogger.trace(this, "Finished getConnectionFromDSCache");
		return conn;
	}

	private DataSource getDatasourceFromCache(String system_sub_type, String datasource_name)
	throws Exception
	{
		EBWLogger.trace(this, "Into getDatasourceFromCache");
		//EBWLogger.trace(this, "Param1 : system_sub_type =" + system_sub_type);
		//EBWLogger.trace(this, "Param2 : datasource_name =" + datasource_name);
		//EBWLogger.trace(this, "Start Time " + (new Date()).getTime());
		DataSource ds = null;
		try
		{
			if(dsCache.get(datasource_name) == null)
			{

				Context serverctx = (Context)getInitialContextFromChache(system_sub_type,datasource_name);
				EBWLogger.logDebug(this,"InitiaContext created for getting datasource"+datasource_name);

				if(system_sub_type != null && system_sub_type.equalsIgnoreCase("WEBSERVER")){

					String WSdatasource_name = "java:/comp/env/"+datasource_name;
					ds = (DataSource)serverctx.lookup(WSdatasource_name);
					EBWLogger.logDebug(this,"Datasource created from inialcontext for webserver");
				}else{
					ds = (DataSource)serverctx.lookup(datasource_name);
					EBWLogger.logDebug(this,"Datasource created for datasource name:"+datasource_name);
				}

				dsCache.put(datasource_name, ds);

			} else

				ds = (DataSource)dsCache.get(datasource_name);
		}
		catch(DataSourceCreationException e)
		{
			throw new EbwException(this, new DataSourceCreationException(datasource_name));
		}
		//EBWLogger.trace(this, "End Time " + (new Date()).getTime());
		EBWLogger.trace(this, "Finished getDatasourceFromCache");
		return ds;
	}

	private Context getInitialContextFromChache(String system_sub_type,String datasource_name)
	throws NamingException, Exception
	{
		//EBWLogger.trace(this, "Into getInitialContextFromChache");
		//EBWLogger.trace(this, "Param1 : system_sub_type =" + system_sub_type);
		//EBWLogger.trace(this, "Param1 : datasource_name =" + datasource_name);
		//EBWLogger.trace(this, "Start Time " + (new Date()).getTime());
		InitialContext initCtx = null;
		Context envContext = null;
		try
		{
			if(ctxCache.get(datasource_name) == null)
			{
				EBWLogger.logDebug(this, "system_sub_type is null" + system_sub_type);
				if(system_sub_type != null && system_sub_type.equalsIgnoreCase("WEBSERVER"))
				{
					EBWLogger.logDebug(this, "Creating Initial Context for WebServer");
					initCtx = new InitialContext();
					EBWLogger.logDebug(this, "Initial Context created for WebServer");
					ctxCache.put(datasource_name, initCtx);
					EBWLogger.logDebug(this, "Lookup of java:/comp/env Over for datasource " + datasource_name);
				} else
				{
					String strContextFactory = PropertyFileReader.getProperty("JNDI_CONTEXT_FACTORY");
					if(strContextFactory != null && strContextFactory.length() > 0)
					{
						Hashtable properties = new Hashtable();
						properties.put("java.naming.factory.initial", strContextFactory);

						// code added for url framing for clustered ennvironment


						String ipaddress = (String)systemInfo.get("IP_ADDRESS");

						String portno = (String)systemInfo.get("PORT_NO");

						String url= null;
						if(ipaddress.indexOf(',')>0 && portno.indexOf(',')>0)
						{
							System.out.println(" coming into cluster configuration");
							String[] iparray = ipaddress.split(",");
							String[] portarray = portno.split(",");
							EBWLogger.logDebug(this,"The no of ip addresses are "+iparray.length);
							EBWLogger.logDebug(this,"The no of ports are "+portarray.length);

							if(iparray.length==portarray.length)
							{
								StringBuffer strb = new StringBuffer();

								for(int i=0,j=iparray.length,k=portarray.length;i<j&&i<k;i++)
								{
									strb.append(iparray[i]+":"+portarray[i]+",");
								}
								strb.deleteCharAt(strb.length()-1);

								url = strb.toString();

								EBWLogger.logDebug(this,"the url for clustered environment is "+url);
							}
							else
							{
								throw new ClusterUrlException();
							}
						}
						else
						{
							url=systemInfo.get("IP_ADDRESS") + ":" + systemInfo.get("PORT_NO");
							System.out.println("The url for single server is "+url);
						}

						// ends here


						//                    properties.put("java.naming.provider.url", systemInfo.get("BASE_PROTOCOL") + "://" + systemInfo.get("IP_ADDRESS") + ":" + systemInfo.get("PORT_NO"));


						properties.put("java.naming.provider.url", systemInfo.get("BASE_PROTOCOL") + "://" + url);


						EBWLogger.logDebug(this, "Before creating InitialContext ");
						initCtx = new InitialContext(properties);
						ctxCache.put(datasource_name, initCtx);
					} else
					{
						EBWLogger.logDebug(this,"InitialContext of the Appserver created within the same context");
						initCtx = new InitialContext();
						ctxCache.put(datasource_name, initCtx);
						//throw new EbwException(this, new JNDIContextFactoryNotSpecified(datasource_name));
						EBWLogger.logDebug(this,"No JNDIContextFactory specified so considering it as same Server Context");
					}
				}
			} else
			{
				/*if(system_sub_type != null && system_sub_type.indexOf("JNDI") == -1)
             {
                 envContext = (Context)ctxCache.get(system_sub_type);
                 return envContext;
             }
				 */
				initCtx = (InitialContext)ctxCache.get(datasource_name);
			}
		}
		catch(InitialContextCreationException e)
		{
			throw new EbwException(this, new InitialContextCreationException(system_sub_type));
		}
		//EBWLogger.trace(this, "End Time " + (new Date()).getTime());
		//EBWLogger.trace(this, "Finished getInitialContextFromChache");
		return initCtx;
	}

	public static void main(String args[])
	{
		try
		{
			LinkedHashMap props = new LinkedHashMap();
			props.put("datasource_name".toUpperCase(), "jdbc/ebwOradb");
			props.put("base_protocol".toUpperCase(), "t3");
			props.put("ip_address".toUpperCase(), "localhost");
			props.put("port_no".toUpperCase(), "1099");
			props.put("system_sub_type".toUpperCase(), "Oracle");
			DatabaseConnection dbconn = new DatabaseConnection();
			dbconn.connect(props);
			EBWLogger.logDebug("DatabaseConnection", "Connction created successfully");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	protected InitialContext ctx;
	protected Connection dbConnection;
	protected String datasource_name;
	protected HashMap dbConnectionCache;
	protected static HashMap ctxCache = new HashMap();
	protected static HashMap dsCache = new HashMap();
	protected LinkedHashMap systemInfo;

}