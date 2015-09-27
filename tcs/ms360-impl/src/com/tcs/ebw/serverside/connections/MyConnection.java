package com.tcs.ebw.serverside.connections;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MyConnection implements Connection {

	static int count = 0;
	static FileOutputStream logStream = null;;
	private Connection conn;

	public MyConnection(Connection c){
		conn = c;
		log("Connection Leak Trace : Connection OPEN : total connection = " + (count+1) + "[" + getCallerInfo() + "]" + "ConnectionObject" + c.hashCode());
		count++;
	}

	private String indent(int count) {
		StringBuffer b = new StringBuffer();
		for(int i=0; i < count; i++){
			b.append(" ");
		}
		return b.toString();
	}

	public void clearWarnings() throws SQLException {
		conn.clearWarnings();
	}

	public void close() throws SQLException {
		conn.close();
		count--;
		log("Connection Leak Trace : Connection CLOSE  : total connection = " + (count) + "[" + getCallerInfo() + "]" + "ConnectionObject  " + conn.hashCode());
	}

	private String getCallerInfo() 
	{
		Throwable t = new Throwable();
		StackTraceElement[] st = t.getStackTrace();
		String calledFrom = "";

		//Stack Trace length to be printed in the log file...
		int stackTraceLength = 20;
		if(st.length < 20){
			stackTraceLength = st.length;
		}

		for(int i =0 ;i < stackTraceLength ; i++) {
			log("StackTrace :" + st[i] + "\n");
		}

		if(st.length > 2 ){
			calledFrom = st[2].toString();
		}
		else if(st.length > 1 ){
			calledFrom = st[1].toString();
		}
		else if(st.length > 0 ){
			calledFrom = st[0].toString();
		}
		else{
			calledFrom = "unknown location";
		}
		return calledFrom;
	}

	private void log(String string) {
		try {
			if ( logStream == null ){
				logStream = new FileOutputStream("connections.log");
			}
			logStream.write(String.format("%s%s%n", indent(count),string).getBytes());
			logStream.flush();
		} catch (IOException e) {
			System.err.println(string);
		}
	}

	public void commit() throws SQLException {
		conn.commit();
	}

	public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
		return conn.createArrayOf(arg0, arg1);
	}

	public Blob createBlob() throws SQLException {
		return conn.createBlob();
	}

	public Clob createClob() throws SQLException {
		return conn.createClob();
	}

	public NClob createNClob() throws SQLException {
		return conn.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return conn.createSQLXML();
	}

	public Statement createStatement() throws SQLException {
		return conn.createStatement();
	}

	public Statement createStatement(int arg0, int arg1) throws SQLException {
		return conn.createStatement(arg0,arg1);
	}

	public Statement createStatement(int arg0, int arg1, int arg2)
	throws SQLException {
		return conn.createStatement(arg0,arg1,arg2);
	}

	public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
		return conn.createStruct(arg0,arg1);
	}

	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}

	public String getCatalog() throws SQLException {
		return conn.getCatalog();
	}

	public Properties getClientInfo() throws SQLException {
		return conn.getClientInfo();
	}

	public String getClientInfo(String arg0) throws SQLException {
		return conn.getClientInfo(arg0);
	}

	public int getHoldability() throws SQLException {
		return conn.getHoldability();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return conn.getMetaData();
	}

	public int getTransactionIsolation() throws SQLException {
		return conn.getTransactionIsolation();
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return conn.getTypeMap();
	}

	public SQLWarning getWarnings() throws SQLException {
		return conn.getWarnings();
	}

	public boolean isClosed() throws SQLException {
		return conn.isClosed();
	}

	public boolean isReadOnly() throws SQLException {
		return conn.isReadOnly();
	}

	public boolean isValid(int arg0) throws SQLException {
		return conn.isValid(arg0);
	}

	public String nativeSQL(String arg0) throws SQLException {
		return conn.nativeSQL(arg0);
	}

	public CallableStatement prepareCall(String arg0) throws SQLException {
		return conn.prepareCall(arg0);
	}

	public CallableStatement prepareCall(String arg0, int arg1, int arg2)
	throws SQLException {
		return conn.prepareCall(arg0, arg1, arg2);
	}

	public CallableStatement prepareCall(String arg0, int arg1, int arg2,
			int arg3) throws SQLException {
		return conn.prepareCall(arg0, arg1, arg2, arg3);
	}

	public PreparedStatement prepareStatement(String arg0) throws SQLException {
		return conn.prepareStatement(arg0);
	}

	public PreparedStatement prepareStatement(String arg0, int arg1)
	throws SQLException {
		return conn.prepareStatement(arg0);
	}

	public PreparedStatement prepareStatement(String arg0, int[] arg1)
	throws SQLException {
		return conn.prepareStatement(arg0, arg1);
	}

	public PreparedStatement prepareStatement(String arg0, String[] arg1)
	throws SQLException {
		return conn.prepareStatement(arg0, arg1);
	}

	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2)
	throws SQLException {
		return conn.prepareStatement(arg0, arg1, arg2);
	}

	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2,
			int arg3) throws SQLException {
		return conn.prepareStatement(arg0, arg1, arg2, arg3);
	}

	public void releaseSavepoint(Savepoint arg0) throws SQLException {
		conn.releaseSavepoint(arg0);
	}

	public void rollback() throws SQLException {
		conn.rollback();
	}

	public void rollback(Savepoint arg0) throws SQLException {
		conn.rollback(arg0);
	}

	public void setAutoCommit(boolean arg0) throws SQLException {
		conn.setAutoCommit(arg0);
	}

	public void setCatalog(String arg0) throws SQLException {
		conn.setCatalog(arg0);
	}

	public void setClientInfo(Properties arg0) throws SQLClientInfoException {
		conn.setClientInfo(arg0);
	}

	public void setClientInfo(String arg0, String arg1)
	throws SQLClientInfoException {
		conn.setClientInfo(arg0,arg1);
	}

	public void setHoldability(int arg0) throws SQLException {
		conn.setHoldability(arg0);
	}

	public void setReadOnly(boolean arg0) throws SQLException {
		conn.setReadOnly(arg0);
	}

	public Savepoint setSavepoint() throws SQLException {
		return conn.setSavepoint();
	}

	public Savepoint setSavepoint(String arg0) throws SQLException {
		return conn.setSavepoint(arg0);
	}

	public void setTransactionIsolation(int arg0) throws SQLException {
		conn.setTransactionIsolation(arg0);
	}

	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
		conn.setTypeMap(arg0);
	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return false;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return unwrap(arg0);
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
