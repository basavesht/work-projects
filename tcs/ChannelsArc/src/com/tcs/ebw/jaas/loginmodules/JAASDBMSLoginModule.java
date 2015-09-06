/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.loginmodules;

import javax.security.auth.login.*;
import javax.security.auth.Subject;
import java.io.IOException;
import java.sql.*;
import javax.security.auth.callback.*;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.tcs.ebw.jaas.utils.PropertyFileReader;

/**
 * 
 *  This class is instantiated when creating the instance of LoginContext
 *  in authenticate method. It gets instantiated implicitly by JAAS API.
 */
public class JAASDBMSLoginModule extends JAASAbstractLoginModule{

	
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
     *
     */
	/*public JAASDBMSLoginModule(){
		System.out.println("Into dbms login module.");
		initializeConnection();
		
	}
	*/
	
	/**
     * Logging output for this plug in instance.
     */
    protected Log log = LogFactory.getLog(this.getClass());
    
    /**
     * This method is for initialize a connection 
     */
	protected void initializeConnection() {
		System.out.println("Into initialize connection..");
		try{
		Class.forName(PropertyFileReader.getProperty("dbms_jdbcType"));		
		connection = DriverManager.getConnection(PropertyFileReader.getProperty("dbms_connectString"));
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
			System.out.println("Initialize of login module called");
			this.subject = subject;
			
			this.callbackHandler = callbackHandler;
			this.succeeded=false;
			this.sharedState = sharedState;
			this.options = options;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    
    /**
     * This method is being called from the authentication classes to initiate
     * the login process (authentication process). Uses callback Handlers to get
     * the credentials which are given by the user or external application. 
     */
    public boolean login() throws LoginException {
    	System.out.println("into login...");
    	
    	try{
    		initializeConnection();
    		if(callbackHandler == null) {
    		      throw new LoginException("No callback handler is available");
    		    }
    		    Callback callbacks[] = new Callback[2];
    		    callbackHandler.handle(callbacks);
    		    username = ((NameCallback)callbacks[0]).getName();
    		    password = ((NameCallback)callbacks[1]).getName();
                System.out.println("Callback return  ..."+username+" : "+password);
	    	Statement st = connection.createStatement();
	    	System.out.println("login : calling query .."+"select * from UserMaster where username='"+username+"' and password='"+password+"'");
	    	ResultSet rs = st.executeQuery("select * from UserMaster where username='"+username+"' and password='"+password+"'");
	    	
	    	int i=0;
	    	while(rs.next())i++;
	    	
	    	System.out.println("value of i is :"+ i);
	    	if(i==0) succeeded=false;
	    	else	succeeded = true;
	    	
	    	System.out.println("DBML login returning "+succeeded);
	    	return succeeded;
	    	
	    	
    	}catch(SQLException sqlex){
    		sqlex.printStackTrace();    	
    		return false;
    	}catch(IOException io){
    		io.printStackTrace();
    		return false;
    	}
    	catch(UnsupportedCallbackException uncall){
    		uncall.printStackTrace();
    		return false;
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    }     
}
