package com.tcs.ebw.serverside.listeners;

import javax.servlet.http.*;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import java.util.LinkedHashMap;

public class EBWHttpSessionListener implements HttpSessionListener{
	
	
	
	public void sessionCreated(HttpSessionEvent arg0) {
		EBWLogger.logDebug(this,"Entered Method sessionDestroyed"+arg0.getSession().getId());
		
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
//		EBWLogger.logDebug(this,"Entered Method sessionDestroyed");
		try{
//			
//			com.tcs.ebw.CA.transferobject.EbwLoginInfoNDBTO ebwLoginInfoNDBTO = new com.tcs.ebw.CA.transferobject.EbwLoginInfoNDBTO();
//			ebwLoginInfoNDBTO.setUSRSESSIONID(arg0.getSession().getId());
//	
//			Object objParams[]={ebwLoginInfoNDBTO,new Boolean(false)};
//			Class clsParamTypes[]={Object.class,Boolean.class};
//			
//			IEBWService objService = EBWServiceFactory.create("deleteLoginInfo");
//			Object objOutput = objService.execute(clsParamTypes, objParams);
			
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
		EBWLogger.logDebug(this,"Finished Method sessionDestroyed");
	}
	

}
