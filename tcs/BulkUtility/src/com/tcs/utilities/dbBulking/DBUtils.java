package com.tcs.utilities.dbBulking;

import java.sql.DriverManager;
import java.sql.Connection;

/**
 * @author 224703
 *
 */
public class DBUtils {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws Exception 
	 */
	public Connection getConnection() throws Exception
	{
		Connection conn = null;
		try 
		{
			String driverName = "com.ibm.db2.jcc.DB2Driver";
			String url = "jdbc:db2://172.19.98.148:50005/FDNADB:currentSchema=BULKSCHEMA;";
			String user = "db2admin";
			String password = "sec2010@Password";
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Got connection"+conn);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return conn;
	}
}
