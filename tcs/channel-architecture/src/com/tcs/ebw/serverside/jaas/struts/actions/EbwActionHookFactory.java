package com.tcs.ebw.serverside.jaas.struts.actions;

import com.tcs.ebw.common.util.EBWLogger;

public class EbwActionHookFactory 
{
	public static EbwActionHookInterface getHookInstance(String hookClassName) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		EBWLogger.logDebug(EbwActionHookFactory.class, " Into EbwActionHookFactory class getHookInstance() method ");
		EbwActionHookInterface object = null;
		object  = (EbwActionHookInterface)Class.forName(hookClassName).newInstance();
		return object;
	}
}
