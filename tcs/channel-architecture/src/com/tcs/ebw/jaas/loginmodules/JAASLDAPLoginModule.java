/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.loginmodules;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.login.*;

//import com.tcs.ebw.jaas.utils.CheckLDAPBind;
import com.tcs.ebw.jaas.utils.PropertyFileReader;


public class JAASLDAPLoginModule extends JAASAbstractLoginModule{
	
	/**
	 * Connection object which has the connection to an LDAP Server.
	 * The parameters for LDAP connection will be taken from the 
	 * Property file.
	 *  
	 */
	private Connection connection;
	
	/**
	 * This variable stores the jdbc connection driver.
	 */
	private String ldap_jdbcType;
	
	/**
	 * This variable stores the IP address or hostname of the LDAP Server.
	 */
	private String ldap_hostName; 
	
	/**
	 * Port variable stores the port number of the LDAP server for connection. 
	 */
	private String ldap_port;
	
	/**
	 * This variable stores the username credential required for connecting 
	 * with LDAP server.
	 *  
	 */
	private String ldap_username;
	
	/**
	 * This variable stores the password credential required for connecting 
	 * with LDAP server.
	 */
	private String ldap_password;
	
	/**
	 * This variable stores the hierarchy level of LDAP directory server from
	 * which we have to start searching. For further information on LDAP hierarchy 
	 * please refer LDAP directory server documentation. 
	 */
	private String ldap_baseDN;
	
	
	/** 
	 * This method is to abort the login process
	 * @return boolean if true,then abort from login process,else not
	 */
	public boolean abort() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
     * This method is for initialize a connection  through LDAP
     */
	protected void initializeConnection() {
		System.out.println("Into initialize connection..");
		try{
			ldap_jdbcType = PropertyFileReader.getProperty("ldap_jdbcType");
			ldap_hostName = PropertyFileReader.getProperty("ldap_hostName");
			ldap_port 	= PropertyFileReader.getProperty("ldap_port");
			username = PropertyFileReader.getProperty("ldap_username");
			password = PropertyFileReader.getProperty("ldap_password");
			ldap_baseDN   = PropertyFileReader.getProperty("ldap_baseDN");
			
			Class.forName(ldap_jdbcType);
			//String ldapConnectString =   "jdbc:ldap://172.19.24.206:389/o=India.com?SEARCH_SCOPE:=subTreeScope";
			String ldapConnectString =   "jdbc:ldap://"+ldap_hostName+":"+ldap_port+"/"+ldap_baseDN+"?SEARCH_SCOPE:=subTreeScope";
			System.out.println("Connect String is :"+ldapConnectString);
			System.out.println("Username and password "+username+"  "+password);
			
			connection = DriverManager.getConnection(ldapConnectString,username,password);
			System.out.println("Connection to LDAP Server successfull");
		}catch(SQLException sqle){
			sqle.printStackTrace();	
		}catch(ClassNotFoundException cn){
			cn.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
 
	/**
    * This method is to initialize using Subject subject,CallbackHandler callbackHandler
    * @param subject
    * @param callbackHandler
    * @param sharedState shared <code>LoginModule</code> state. <p>
    *
    * @param options options specified in the login
    *			<code>Configuration</code> for this particular
    *			<code>LoginModule</code>.
    */
	public void initialize(Subject subject,CallbackHandler callbackHandler, Map sharedState,Map options) {
		System.out.println("Inialize start..");
		try{
			/*System.out.println("Inialize start..");
			initializeConnection();
			System.out.println("Inialize over..");
			*/		
			System.out.println("Initialize of login module called");
			this.subject = subject;
			succeeded =false;
			this.callbackHandler = callbackHandler;
			//this.callbackHandler = callbackHandler;
			this.sharedState = sharedState;
			this.options = options;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    /**
     * Authenticate the user by prompting for a user name and password.
     *
     * <p>
     *
     * @return true in all cases since this <code>LoginModule</code>
     *		should not be ignored.
     *
     * @exception FailedLoginException if the authentication fails. <p>
     *
     * @exception LoginException if this <code>LoginModule</code>
     *		is unable to perform the authentication.
     */
    public boolean login() throws LoginException {
    	try{
    		initializeConnection();
    		
	    	//= new CheckLDAPBind().checkLDAP(connection);
    		Callback callbacks[] = new Callback[2];
		    callbackHandler.handle(callbacks);
		    username = ((NameCallback)callbacks[0]).getName();
		    password = ((NameCallback)callbacks[1]).getName();
		    
    		String sql = "SELECT dn,sn FROM "+ldap_baseDN+" where sn="+username;
    		System.out.println(sql);
    		//String sql = "SELECT dn,sn FROM "+baseDN;
			Statement stmt = connection.createStatement(); 
			System.out.println("Statement created");
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			while (rs.next()) {
				for(int i=1;i<=rsmd.getColumnCount();i++)
					System.out.println(rsmd.getColumnName(i) + " = "+rs.getString(i));
				succeeded=true;
			}
			
			return succeeded;
    	}catch(Exception ex){
    		ex.printStackTrace();
    		return false;
    	}
    }

   /* public static void main(String args[]){
    	try{
    		JAASLDAPLoginModule Jass = new JAASLDAPLoginModule();
    		//Jass.initialize();
    		//Jass.username = "cn=wpsadmin";
    		//Jass.password = "wpsadmin";
    		Jass.login();
    	  System.out.println("Login result :"+Jass.login());
    	  
    	  Jass.password = "ebwuser1";
    	  System.out.println("Login result :"+Jass.login());
    	}catch(LoginException le){
    		le.printStackTrace();
    	}
    }
    */
    
    
}
