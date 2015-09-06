/**
 * 
 */
package com.tcs.Payments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.EJBException;

import com.tcs.Payments.EAITO.RSA_CALLBACK_INP;
import com.tcs.Payments.EAITO.RSA_CALLBACK_OUT;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.serverside.services.CallBackRSAProcessor;
import com.tcs.mswitch.common.channel.DBProcedureChannel;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class TestCallBackRSA {


	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException 
	{
		Connection con = getConnection();
		RSA_CALLBACK_OUT responseOut = new RSA_CALLBACK_OUT();
		ServiceContext context = new ServiceContext();
		context.setConnection(con);
		try 
		{
			//Dummy Input...
			RSA_CALLBACK_INP rsaRequest = new RSA_CALLBACK_INP();
			rsaRequest.setCASE_MNGMT_USR_ID("MSUser1");
			rsaRequest.setCASE_STATUS(MSSystemDefaults.RSA_GENUINE_CASE);
			rsaRequest.setTXN_CONF_ID("17083");
			rsaRequest.setDESCRIPTION("Genuine Case");

			//RSA Processor...
			CallBackRSAProcessor callBackRSAObj = new CallBackRSAProcessor();
			callBackRSAObj.setConnection(con);
			responseOut = callBackRSAObj.processRSARequest(rsaRequest);

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
	 * Handling Bancs and RTA (Commit and Rollback)..
	 * @param bancsConnection
	 * @param objOutput
	 * @throws Exception
	 */
	public RSA_CALLBACK_OUT responseHandler(Connection bancsConnection,ArrayList objOutput) throws Exception
	{
		ServiceContext context = new ServiceContext();
		RSA_CALLBACK_OUT rsaResponse = new RSA_CALLBACK_OUT();
		try
		{
			if(objOutput instanceof ArrayList)
			{
				ArrayList serviceReturnOut = (ArrayList)objOutput;
				if(serviceReturnOut!= null && !serviceReturnOut.isEmpty())
				{ 
					if(serviceReturnOut.get(0)!=null && serviceReturnOut.get(0) instanceof ServiceContext){
						context = (ServiceContext)serviceReturnOut.get(0);
					}
					if(serviceReturnOut.get(1)!=null && serviceReturnOut.get(1) instanceof RSA_CALLBACK_OUT){
						rsaResponse = (RSA_CALLBACK_OUT)serviceReturnOut.get(1);
					}

				}
			}
			if(context.isServiceCallSuccessful()) {			        
				bancsConnection.commit();
				if(context.isRTACommitReq()) {
					DBProcedureChannel.commit(); // commit RTA
				} 
			}
			else {
				bancsConnection.rollback();
				if(context.isRTARollbackReq()){
					DBProcedureChannel.rollback();
				}
			}
		} 
		catch(Exception ex)
		{
			try {		
				bancsConnection.rollback();		
				if(context.isRTARollbackReq()){
					DBProcedureChannel.rollback();
				}			
			} 
			catch (Exception syex){
				throw new EJBException ("Rollback failed: " + syex.getMessage());
			}		
		}
		finally {
			if (bancsConnection != null){
				bancsConnection.close();
			}	
		}
		return rsaResponse;
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
