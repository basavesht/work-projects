/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.serverside.jaas.loginmodules;

import javax.security.auth.login.*;
import javax.security.auth.Subject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.sql.*;
import javax.security.auth.callback.*;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.MissingResourceException;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.common.util.StringUtil;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.security.EBWSecurity;
import com.tcs.ebw.serverside.connections.DatabaseConnection;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.query.QueryExecutor;
import com.tcs.ebw.serverside.exception.JAASLoginConnNotFoundException;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;

import org.apache.commons.codec.binary.Base64;

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
     */
	/*public JAASDBMSLoginModule(){
		System.out.println("Into dbms login module.");
		initializeConnection();
		
	}
	*/
	
	/**
     * Logging output for this plug in instance.
     */
    protected Log logger = LogFactory.getLog(this.getClass());
    
    /**
     * This method initializes the connection to the Database where the JAAS information is stored.
     * The connection information is read from a property file.
     * @throws com.tcs.ebw.serverside.exception.EbwException
     */
	protected void initializeConnection() throws Exception {
	    EBWLogger.trace(this,"Into initialize connection..");
	    EBWLogger.trace(this,"Start Time "+new java.util.Date().getTime());
		
		
		    DatabaseConnection dbconn = new DatabaseConnection();
		    LinkedHashMap params = new LinkedHashMap();
		    params.put("datasource_name".toUpperCase(),PropertyFileReader.getProperty("jaas_datasource_name"));
		    params.put("system_sub_type".toUpperCase(),PropertyFileReader.getProperty("jaas_system_sub_type"));
		    params.put("port_no".toUpperCase(),PropertyFileReader.getProperty("jaas_port_no"));
		    params.put("base_protocol".toUpperCase(),PropertyFileReader.getProperty("jaas_base_protocol"));
		    params.put("ip_address".toUpperCase(),PropertyFileReader.getProperty("jaas_ip_address"));
		    
		    try{
		        connection = (Connection)dbconn.connect(params);
		    }catch(Exception e){
		        //new EbwException(this,new JAASLoginConnNotFoundException(params)).getExceptionMessage();
		        e.printStackTrace();
		        throw new EbwException(this,new JAASLoginConnNotFoundException(params));
		    }
		    
		    dbconn=null;
		    EBWLogger.trace(this,"End Time "+new java.util.Date().getTime());
		    EBWLogger.trace(this,"Finished initialize connection..");
	}
    
	
	
	
    /**
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
	 */
	public void initialize(Subject subject,CallbackHandler callbackHandler, Map sharedState,Map options) {
		EBWLogger.trace(this,"Initialize of login module called");
		try{
			this.subject = subject;
			this.callbackHandler = callbackHandler;
			this.succeeded=false;
			this.sharedState = sharedState;
			this.options = options;
		}catch(Exception e){
			e.printStackTrace();
			EBWLogger.logError(this,e.getMessage());
		}
	}
	
    
    /**
     * This method is being called from the authentication classes to initiate
     * the login process (authentication process). Uses callback Handlers to get
     * the credentials which are given by the user or external application.
     * @throws UnsupportedCallbackException 
     * @throws SQLException
     * @throws IOException 
     */
    public boolean login() throws LoginException {
    	EBWLogger.logDebug(this,"into login...1");
    	Statement st =null;
    	ResultSet rs = null;
    	String extuserid=null;
    	String extsessionid=null;
    	String groupId =null; 
    	try{
    		
    		// below line of code is commented as connection should be taken only through factory 
    		//initializeConnection();
    		
    		
    		if(callbackHandler == null) {
    		      throw new LoginException("No callback handler is available");
    		    }
    		    //Callback callbacks[] = new Callback[2];
    			Callback callbacks[] = new Callback[3];
    		    callbackHandler.handle(callbacks);
    		    username =new String(((NameCallback)callbacks[0]).getName());
    		    password = new String(((PasswordCallback)callbacks[1]).getPassword());
    		    
    		   try{
    			   
    			   
    		    miscinfo=new String(((NameCallback)callbacks[2]).getName());
    		    String temp[] = miscinfo.split(" ");
    		    groupId = temp[0];
    		    
    		    //System.out.println(" group id getting from the screen is "+groupId);
    		    EBWLogger.logDebug(this,"group id getting from screen is "+groupId);
    		    
    		   }catch(Exception e){
    			   EBWLogger.logDebug(this,"'miscinfo' not configured. Hence continuing..."+e.getMessage());
    			   e.printStackTrace();
    		   }
                    EBWLogger.logDebug(this,"Callback return  ..."+username+" : "+password);
                    EBWLogger.logDebug(this,"Misc info is :"+miscinfo);
    		    
    		    EBWSecurity security = new EBWSecurity();
    		    
    		    //--------------------------CODE FOR Decryption of Credentials---------------------------
    		   try{
	    		   if(PropertyFileReader.getProperty("isDecryptionReqd").toUpperCase().equals("TRUE"))
	    		   {
//	    			   String getURLparams=PropertyFileReader.getProperty("MISC_INFO_KEY");
//	   				   String temp[]=getURLparams.split(",");
	    			
	    			   
	    			   security.setSecretKey(security.getSecretKey());
	    			   String temp[]=miscinfo.split(" ");
	    			   extsessionid=temp[0];
	    			   extuserid=temp[1];
	    			   
	   				   extuserid =new String(security.decryptSymmetric(Base64.decodeBase64(extuserid.getBytes())));
	   				   EBWLogger.logDebug(this,"EXTUSERID-"+extuserid);
                     
	   				   // String usernameDecrypted = new String(security.decryptSymmetric(Base64.decodeBase64(username.getBytes())));
                       // username = usernameDecrypted;
                   
	   				    username =new String(security.decryptSymmetric(Base64.decodeBase64(username.getBytes())));
                        EBWLogger.logDebug(this,"USERID is- "+username);
                        
	    			   //Decrypting any extra information sent as the part of Login URL
	    			   if (!(extsessionid==null || extsessionid.equals(""))){
	    				   
	    				   extsessionid=new String(security.decryptSymmetric(Base64.decodeBase64(extsessionid.getBytes())));
		    			   EBWLogger.logDebug(this,"EXTSESSION-"+extsessionid);   				   
	    	
	    			   }
	    			   EBWLogger.logDebug(this,"extsessionid:"+extsessionid);
	    			   EBWLogger.logDebug(this,"EXTUSERID:"+extuserid);
	    			   
	    		   	}
	    	     }catch(MissingResourceException mre){
    			   EBWLogger.logDebug(this, "Key - 'isDecryptionReqd' is not present ,hence continuing without decryption");
    			   extuserid=" ";
    			   extsessionid=" ";
    		     }
    		    //---------------------------------Decryption code ends---------------------------------
                
    		    
	    	     
    		    if(userPrincipal ==null)
        		    userPrincipal = new UserPrincipal(username);
                
	    	EBWLogger.logDebug(this,"UnEncrypted password is :"+password);
	    	String encPwd = new String(security.computeHash(password.getBytes(),"MD5"));
	    	StringBuffer qryString = new StringBuffer();
	    	
	    	QueryExecutor queryExecutor = new QueryExecutor();
	    	queryExecutor.setConnection(connection);
	    	
	    	try{
	    	if (extuserid!=null || !extuserid.equals(""))
	    		{
	    		EBWLogger.logDebug(this,"Populating userprincipal with 'extuserid' - "+extuserid);
	    		extuserid = extuserid.trim();
	    		userPrincipal.setExtuserid(extuserid);
	    		}
	    	
	    	if (extsessionid!=null || !extsessionid.equals(""))
    		{
    		EBWLogger.logDebug(this,"Populating userprincipal with 'extsessionid' - "+extsessionid);
    		extsessionid = extsessionid.trim();
    		userPrincipal.setExtsessionid(extsessionid);
    		}
	    	
	    	}catch(NullPointerException npe){
	    		EBWLogger.logDebug(this, "miscinfo is 'null'...Continuing...");
	    	}
	    	userPrincipal.setUsrpwd(encPwd);   
	    	 username=username.trim();
             EBWLogger.logDebug(this,"username tructed length is :"+username.length());
	    	
             userPrincipal.setUsruserid(username);
	    	EBWLogger.logDebug(this,"Username set in userPrincipal is :"+userPrincipal.getUsruserid()+", length is :"+username.length());
            EBWLogger.logDebug(this,"User name before setting in User principal is :"+username);
	    	try{
	    		if(PropertyFileReader.getProperty("GroupId")!=null && PropertyFileReader.getProperty("GroupId")!="" )
	    		{
	    			EBWLogger.logDebug(this," group id taking from properties file "+PropertyFileReader.getProperty("GroupId"));
	    			userPrincipal.setUsrgrpid(PropertyFileReader.getProperty("GroupId"));
	    		}
	    		else
	    		{
	    			EBWLogger.logDebug(this,"Group id taking from the screen "+groupId);
	    			userPrincipal.setUsrgrpid(groupId);
	    		}
	    	    
	    	}catch(Exception e){
	    	    EBWLogger.trace(this, "No Default Entity ID / Group Id configured in ebw.properties file");
	    	    userPrincipal.setUsrgrpid(groupId);
	    	}
	    	EBWLogger.logDebug(this,"login : calling query .."+qryString.toString());
	    	
	    	
	    	Class clsParamTypes[] = {Object.class,Boolean.class};
	    	Object objParams[] = {userPrincipal, Boolean.valueOf(false)};
	    	
	    	EBWLogger.logDebug(this," calling the login service getAuthenticationDetails");
	    	
	    	IEBWService service = EBWServiceFactory.create("getAuthenticationDetails");
	    	
	    	ArrayList objOutput = (ArrayList) service.execute(clsParamTypes, objParams);
	    	
	    	//ArrayList objOutput = (ArrayList)queryExecutor.executeQuery("getAuthenticationDetails",userPrincipal);
	    	
	    	EBWLogger.logDebug(this,"Output from login query .."+objOutput);
	    	//ArrayList fanums = new ArrayList();
	    	int i=0;
	    	
	    	
	    	/*rs = st.executeQuery(qryString.toString());
	    	while(rs.next()){
	    	    i++;
	    	    System.out.println("Setting principal values");
	    	    userPrincipal.setUsruserid(rs.getString(1));
	    	    fanums.add((String)rs.getString(2));
	    	    userPrincipal.setUsrgrpid((String)rs.getString(3));
	    	    EBWLogger.logDebug(this,"UserID : "+userPrincipal.getUsruserid());
	    	}
	    	*/
	    	int objOutputSize =objOutput.size();
	    	if(objOutputSize>0){
	    	    ArrayList headerRow = (ArrayList)objOutput.get(0); // These header names are setter methods for principal
	    	    //Start from first row, cos zero'th row contains header
	    	    for(int k=1;k<objOutputSize;k++){
	    	        i++;
		    	    ArrayList firstRow = (ArrayList)objOutput.get(k);
		    	    EBWLogger.logDebug(this,"First row val is :"+firstRow);
		    	    //userPrincipal.setUsrgrpid((String)firstRow.get(2));

					for(int j=0;j<headerRow.size();j++){
						String headerval = (String)headerRow.get(j);
						EBWLogger.logDebug(this,"Header val is :"+headerval);
						String val = (String)firstRow.get(j);
						//if(j==1)fanums.add(val);// First Col is username,2nd col is fanum. This is RJ specific.
						Class[] cls = {String.class};
						if(val.toLowerCase().indexOf("date")==-1){ // Currently handles only datatypes otherthan date type
						    try{
								Method m = userPrincipal.getClass().getDeclaredMethod("set"+StringUtil.initCaps(headerval.toLowerCase()),cls);
								Object obj[] = {val};
								m.invoke(userPrincipal,obj);
							
						    }catch(Exception e){
						        EBWLogger.logError(this,"Error in setting attribute value in UserPrincipal for method"+"set"+StringUtil.initCaps(headerval.toLowerCase()));
						    e.printStackTrace();
						    }
						}
					}
	    	    }
	    	}
	    	
	    	String tmpuserid = userPrincipal.getUsruserid();
	    	
	    /*	if( fanums!=null && ( fanums.contains("ALLFA") || fanums.contains("FAALL") )){
	    	    EBWLogger.logDebug(this,"RIS USER ");
	    	    //userPrincipal.setUsruserid("%");
	    	    userPrincipal.setUsrusername(tmpuserid);
	    	    objOutput = (ArrayList)queryExecutor.executeQuery("getAuthenticationDetailsRIS",userPrincipal);
	    	    fanums = new ArrayList();
	    	    if(objOutput!=null)
	    	        for(int h=1;h<objOutput.size();h++){
	    	            ArrayList datarow =(ArrayList)objOutput.get(h);
	    	            String data = (String)datarow.get(1);
	    	            EBWLogger.logDebug(this,"FA Numbers from Query Output - data :"+data);
	    	            if(!( data.trim().equalsIgnoreCase("ALLFA") || data.trim().equalsIgnoreCase("FAALL"))){
	    	                EBWLogger.logDebug(this,"Adding FA Numbers :"+data+", length :"+data.length());
	    	                fanums.add((String)datarow.get(1));
	    	            }
	    	        }
	    	        
	    	        This block has been moved to LogonAction class
	    	}*/
	    	userPrincipal.setName(tmpuserid);
	    	
	    	EBWLogger.logDebug(this,"Userprincipal userid is :"+userPrincipal.getUsruserid());
	    	//EBWLogger.logDebug(this,"Fanumbers got from JAAS login are :"+fanums);
	    	
	    	//userPrincipal.setFanums(fanums);
	    	EBWLogger.logDebug(this,"value of i is :"+ i);
	    	if(i==0) succeeded=false;
	    	else{	succeeded = true;
	    	}
	    	
	    	EBWLogger.logDebug(this,"login returning "+succeeded);
	    	EBWLogger.trace(this,"ResultSet closed , Statement Closed and Connection Nullified after login query execution");
	    	return succeeded;
	    	
	    	
    	}catch(SQLException sqlex){
    		sqlex.printStackTrace();
    		EBWLogger.logError(this,"SQL Error in Login - Error Code :"+sqlex.getErrorCode()+" Msg :"+sqlex.getMessage());
    		return false;
    	}catch(IOException io){
    		io.printStackTrace();
    		EBWLogger.logError(this,"IO Error in Login :"+io.getMessage());
    		return false;
    	}
    	catch(UnsupportedCallbackException uncall){
    		uncall.printStackTrace();
    		EBWLogger.logDebug(this,"CallbackException in login :"+uncall.getMessage());
    		return false;
    	}catch(Exception e){
    		e.printStackTrace();
    		EBWLogger.logDebug(this,"Unknown Error while Login :"+e.getMessage());
    		return false;
    	}
    	finally{
    	    try{
	    		if(rs!=null){
	    	        rs.close();
			    	rs=null;
	    		}
	    		if(st!=null){
			    	st.close();
			    	st=null;
	    		}
	    		if(connection!=null){
			    	connection.close();
			    	connection=null;
    	    	}
    	    }catch(SQLException sqle){
    	        EBWLogger.logError(this,"SQLException :"+sqle.getCause().getMessage());
    	    }
    	}
    }
   
}
