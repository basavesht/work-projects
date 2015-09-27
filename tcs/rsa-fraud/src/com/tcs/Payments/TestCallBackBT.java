package com.tcs.Payments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.tcs.Payments.EAITO.MS_BOTTOM_LINE_ASYNC_IN;
import com.tcs.Payments.EAITO.MS_BOTTOM_LINE_ASYNC_OUT;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.serverside.services.BottomLineProcessor;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class TestCallBackBT {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException 
	{
		Connection con = getConnection();
		MS_BOTTOM_LINE_ASYNC_OUT responseOut = new MS_BOTTOM_LINE_ASYNC_OUT();
		ServiceContext context = new ServiceContext();
		context.setConnection(con);
		try 
		{
			//Dummy Input...
			MS_BOTTOM_LINE_ASYNC_IN asynchBTRequest = new MS_BOTTOM_LINE_ASYNC_IN();
			asynchBTRequest.setJOB_STATUS("Success");
			asynchBTRequest.setSPOOL_JOB_NAME("17083_1234");
			asynchBTRequest.setDESCRIPTION("Callback Print");

			//BT Processor...
			BottomLineProcessor callBackBTObj = new BottomLineProcessor();
			callBackBTObj.setConnection(con);
			responseOut = callBackBTObj.processAsynchBTRequest(asynchBTRequest);

			//Output Response....
			System.out.println("Response Out"+responseOut.toString());
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
		finally {

		}
	}
	/**
	 * Simple get connection...
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException
	{
		Connection con = null;
		try
		{
			if(con == null)
			{
				String driverName = "com.ibm.db2.jcc.DB2Driver";
				String url = "jdbc:db2://172.19.98.148:50001/MSDEV:currentSchema=PAYPERF;";
				String user = "db2dev";
				String password = "DB2@2009";
				Class.forName(driverName);
				con = DriverManager.getConnection(url, user, password);
				System.out.println("Test Connection succeeded : "+con);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Test Connection failed");
		}
		return con;
	}
}
