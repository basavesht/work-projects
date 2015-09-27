package com.tcs.ebw.ejb.utility;

/* This class is for performing the  database transactions 
 * such as commit and rollback .
 * @author 197479
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.util.MissingResourceException;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;

public class TransactionManager 
{

	public static void doCommit(Connection conn) throws Exception
	{
		
		try
		{
		
			boolean tranFlag = false;
		
		
			try
			{
				PropertyFileReader.getProperty("isEJBManagedTransaction");
				tranFlag = true;
			}
			catch(MissingResourceException msre)
			{
				EBWLogger.logDebug("com.tcs.ebw.ejb.utility.TransactonManager","Caught missing resourse exception for the key \"isEJBManagedTransaction\"");
			
			}
		
			if(!tranFlag)
			{
				conn.commit();
			}
		}
		catch(SQLException sqlexe)
		{
			EBWLogger.logDebug("com.tcs.ebw.ejb.utility.TransactonManager"," Caught sql exception inside the doCommit method");
		}
		
	}
	
	public static void doRollback(Connection conn)throws Exception
	{
		try
		{
		
			boolean tranFlag = false;
		
		
			try
			{
				PropertyFileReader.getProperty("isEJBManagedTransaction");
				tranFlag = true;
			}
			catch(MissingResourceException msre)
			{
				EBWLogger.logDebug("com.tcs.ebw.ejb.utility.TransactonManager","Caught missing resourse exception for the key \"isEJBManagedTransaction\"");
			
			}
		
			if(!tranFlag)
			{
				conn.rollback();
			}
		}
		catch(SQLException sqlexe)
		{
			EBWLogger.logDebug("com.tcs.ebw.ejb.utility.TransactonManager"," Caught sql exception inside the doRollback method");
		}
	}
	
	
}
