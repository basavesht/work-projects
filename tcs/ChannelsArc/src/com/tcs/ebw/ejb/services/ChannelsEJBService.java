package com.tcs.ebw.ejb.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.ejb.EJBObject;

import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.ejb.connections.EjbConnection;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.transferobject.SecurityCheckTO;

public class ChannelsEJBService implements IEBWService
{

	public String serviceid;
	private EBWAppContext context;

	public ChannelsEJBService(String serviceid) 
	{
		this.serviceid=serviceid;

	}

	public Object execute(Class[] paramType, Object[] params) throws Exception {


		EBWLogger.logDebug(this," Entering into the execute method of ChannelsEJBService ");

		String ejbConfigFileName =PropertyFileReader.getProperty("EjbConfiguarationFileName");

		String ejbJndiName=PropertyFileReader.getPropertyKeyValue(ejbConfigFileName,"EJB_JNDI_NAME");
		String contextFactory=PropertyFileReader.getPropertyKeyValue(ejbConfigFileName, "EJB_CONTEXT_FACTORY");	
		String ejbConnUrl=PropertyFileReader.getPropertyKeyValue(ejbConfigFileName, "EJB_CONN_URL");
		String ejbMethodName=PropertyFileReader.getPropertyKeyValue(ejbConfigFileName, "EJB_METHOD_NAME");	

		EJBObject remote = EjbConnection.getRemoteEJBConnection(ejbConnUrl, ejbJndiName, contextFactory);

		EBWLogger.logDebug(this," The ejb connection object is "+remote);

		HashMap map = new HashMap();
//		code added by Dinesh on 25-Jan-10 for UUID security check for access to business calls
		//if(!(params[0] instanceof com.tcs.ebw.transferobject.SecurityCheckTO))
		//{
		SecurityCheckTO toObj = new SecurityCheckTO();
		toObj.setSessionId(getAppContext().getUserPrincipal().getSessionId());
		toObj.setSecurityTokenId(getAppContext().getUserPrincipal().getSecurityTokenId());
		toObj.setUuid(getAppContext().getUserPrincipal().getUuid());

		map.put("CLASS", paramType);
		map.put("OBJECT", params);
		map.put("SERVICEID", serviceid);
		map.put("SECTOOBJ", toObj);
		//}
		//end
		//EBWLogger.logDebug(this," The service information hashmap is "+map);

		Method m =  remote.getClass().getDeclaredMethod(ejbMethodName,HashMap.class);


		Object objOutput = null;

		try
		{
			objOutput=m.invoke(remote,map);
		}
		catch(InvocationTargetException ite)
		{
			//EBWLogger.logDebug(this," caught exception in ChannelsEJBService class" );
			ite.printStackTrace();
		}

		return objOutput;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}






	public Object close() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



	public EBWAppContext getAppContext() {
		// TODO Auto-generated method stub
		return context;
	}

	public Object getCashDenominationHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isErrorOverride() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAppContext(EBWAppContext appContext) {
		// TODO Auto-generated method stub
		this.context = appContext;

	}

	public void setCashDenominationHandler(Object cashDenominationHandler) {
		// TODO Auto-generated method stub

	}

	public Object setConnection(Object connectionObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setErrorOverride(boolean errorOverride) {
		// TODO Auto-generated method stub

	}

	public void setServiceInfo(LinkedHashMap serviceInfo)
	{

	}



}
