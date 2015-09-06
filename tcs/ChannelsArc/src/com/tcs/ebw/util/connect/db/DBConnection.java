/*
 * Created on Oct 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.util.connect.db;

import java.util.Map;
import java.util.Hashtable;

import com.tcs.ebw.util.connect.ConnectionImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * @author 152699
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DBConnection implements ConnectionImpl {
	Connection connection = null;
	ResultSet resultset = null;
	
	public DBConnection () {
	}
	
	public void connect(String driver, String url, String userName, String pwd) throws Exception {
		Class.forName(driver);
		connection = DriverManager.getConnection(url, userName, pwd);
	}
	
	public Map execute (Map mapObj) throws Exception {
		return null;
	}
	
	public Map executeQuery (Map mapObj) throws Exception {
		Hashtable map = new Hashtable();
		return null;
	}
	public void close() throws Exception {
		
	}
	
	/**
	 * @return Returns the connection.
	 */
	public Connection getConnection() {
		return connection;
	}
	/**
	 * @param connection The connection to set.
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
