/*
 * Created on Dec 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.serverside.jaas.utils;

import java.sql.*;
/**
 * @author 152820
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CheckLDAPBind {

	public boolean checkLDAP(String username,String password) throws SQLException, ClassNotFoundException, Exception{
		
			String jdbcType = PropertyFileReader.getProperty("ldap_jdbcType");
			String hostName = PropertyFileReader.getProperty("ldap_hostName");
			String port 	= PropertyFileReader.getProperty("ldap_port");
			//username = PropertyFileReader.getProperty("ldap_username");
			//password = PropertyFileReader.getProperty("ldap_password");
			String baseDN   = PropertyFileReader.getProperty("ldap_baseDN");
			boolean succeeded=false;
			Class.forName(jdbcType);
			//String ldapConnectString =   "jdbc:ldap://172.19.24.206:389/o=India.com?SEARCH_SCOPE:=subTreeScope";
			String ldapConnectString =   "jdbc:ldap://"+hostName+":"+port+"/"+baseDN+"?SEARCH_SCOPE:=subTreeScope";
			
			System.out.println("Connect String is :"+ldapConnectString);
			java.sql.Connection con;
			con = DriverManager.getConnection(ldapConnectString,username,password);
			Statement stmt = con.createStatement();
			System.out.println("Connected successfully...");
			/**
			 * Syntax - 
			 * SELECT attribute1 [, attribute2, attributeN] 
			 * FROM [objectScope|oneLevelScope|subTreeScope;]baseDN
			 * [WHERE where clause]
			 * 
			 */
			String sql = "SELECT dn,sn FROM "+baseDN+" where sn="+username;
			
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			while (rs.next()) {
				for(int i=1;i<=rsmd.getColumnCount();i++)
					System.out.println(rsmd.getColumnName(i) + " = "+rs.getString(i));
				succeeded=true;
			}
			return succeeded;
	}
	
	public static void main(String args[]){
		try{
		System.out.println("Result is : "+new CheckLDAPBind().checkLDAP("cn=wpsadmin","wpsadmin"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
 
}
