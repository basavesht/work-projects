/*

 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.

 * This software is the confidential and proprietary information of 

 * Tata Consultancy Services ("Confidential Information"). You shall not

 * disclose such Confidential Information and shall use it only in

 * accordance with the terms of the license agreement you entered into

 * with Tata Consultancy Services.

 */

package com.tcs.ebw.serverside.services;

import org.apache.log4j.Logger;



import java.net.InetAddress;
import java.sql.Connection;

import java.sql.SQLException;

import java.sql.PreparedStatement;

import java.util.HashMap;

import java.util.HashSet;

import java.util.LinkedHashMap;

import java.util.Set;



import com.tcs.ebw.common.util.EBWLogger;

import com.tcs.ebw.serverside.query.QueryExecutor;

import com.tcs.ebw.serverside.services.EBWAbstractService;

import com.tcs.ebw.transferobject.EBWTransferObject;

import com.tcs.ebw.exception.EbwException;



/**

 *

 * This is the service delegating class which implements 

 * the EBWconnectionType interface and gives generalized

 * implementation for all the methods. This class will 

 * inturn instantiate the appropriate business logic class.

 * This class acts as an interface for eBankworks front end

 * and serverside framework.

 */

public class DatabaseService extends  EBWAbstractService{
	/**
	 * Logger for this class
	 */
	private static final Logger performanceLogger = Logger.getLogger("MSPerformanceLogger"); //$NON-NLS-1$

	

	/**

	 * Statement object used to execute the queries against the database connection

	 * established by connectdbConfig or connect methods.

	 */

	private PreparedStatement st;

	

	/**

	 * Set boolean value as false

	 */

	private Boolean boolFalse = new Boolean(false);

	

	/**

	 * Set boolean value as true

	 */

	private Boolean boolTrue = new Boolean(false);

	

	/**

	 * Variable is the eference of QueryExecutor

	 */

	private QueryExecutor queryExecutor;		

	

	/**

	 * Reference of Sql Connection

	 */

	private java.sql.Connection con = null;



	/**

	 * Static boolean variable for DB Commit operation  

	 */

	static boolean DB_AUTO_COMMIT;

	

	/**

	 * Constructor of DatabaseService

	 *

	 */

	public DatabaseService()

	{



		queryExecutor = new QueryExecutor();

		

		

		// the below code is commented as it never executes and if executes it will throw a nullPointer excp as conn will be always null here

		

		/*

		if(DB_AUTO_COMMIT)

		try {

			 (con).setAutoCommit(false);

		}

		catch(Exception excp){

			try{

		        excp.printStackTrace();

		        con.rollback();

//		        throw excp;	//Should it throw ebwException??

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

//				throw sql2;   // Should it throw ebwException of excep + sql2

			}

		}

		 */

	}	



	static {



		DB_AUTO_COMMIT = false;





	}



	

	/**

	 * This method is used to execute queries which dont need any TO or parameter

	 * to be passed.

	 * 

	 * 

	 * @param statementid - String representing statement name as mentioned in statement property file.

	 * @return Return datatype will be as specified in statement.properties file.

	 * @throws SQLException

	 * @throws Exception

	 */

	public Object executeQuery(String statementid) throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 1");

		Object returnObject = executeQuery(statementid, new EBWTransferObject(), boolFalse);
		elapsedTime = System.currentTimeMillis() - startTime;		
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return returnObject; 

	}

		

	

	/**

	 * This method is used for pagination passing stmtId, single TO,HashMap and isTransaction as input parameter.

	 * It returns an Object which will be TO,ArrrayList,HashMap etc..

	 * @param objTO Transfer Object(TO)  is passed as input parameter

	 * @param isTransaction boolean value which will be true or false.

	 * @return Return datatype will be as specified in statement.properties file.	

	 * @throws Exception 

	 * @throws ClassNotFoundException if specified Driver Class is not found 

	 * @throws InstantiationException if EBWTransferObject cannot be instantiated

	 * @throws NoSuchMethodException if method invoked is not found in EBWTransferObject 

	 * @throws IllegalAccessException if there is not accessrights to access the method on the EBWTransferObject

	 * @throws InvocationTargetException if the specified method is not invoked properly 

	 * @throws NoSuchFieldException if the field lookedup on the EBWTransferObject is not there 

	 * @throws SQL exception if  Connection,Statement Object is not returned properly and if

	 *         there is any problem query execution

	 */

	

	public Object executeQuery( String statementid, Object params, HashMap pgnMap,Boolean isTransaction) throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String, Object, HashMap, Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 18");

		EBWLogger.trace(this,"Starting execute method in Implementation class");

		EBWLogger.trace(this,"statementid : "+statementid);

		EBWLogger.trace(this,"EBWTransferObject : "+params);

		Object returnVal=null;

		try{



	//		queryExecutor.setConnection((java.sql.Connection)serviceConnection);

			EBWLogger.logDebug(this,"Connection set in queryExecutor");

			returnVal = queryExecutor.executeQuery(statementid,params,pgnMap);

			EBWLogger.logDebug(this,statementid+" execution over...");

			}catch(Exception excp){

			try{

		        excp.printStackTrace();

				if(isTransaction.booleanValue())

			        con.rollback();

				

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		  if(!isTransaction.booleanValue()){

			  try{

				  close();

			  }catch(Exception e){

				  EBWLogger.logDebug(this,"Connection is already closed");

			  }

		  }

		}



			EBWLogger.trace(this,"Finished with execute method of implementation class");

		elapsedTime = System.currentTimeMillis() - startTime;		
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String, Object, HashMap, Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	return returnVal;

	}

	

	

	/**

	 * This method is used to execute more than 1 statement with more than

	 * 1 TO. The first array value of statment id will be considered with 

	 * first TO array value and the same will be followed till the end of 

	 * statmentid array size.

	 *   

	 * @param statementids - String Array representing list of statement names as mentioned in statement property file.

	 * @param tos

	 * @param isTransaction 

	 *   		- If true, then all the statements are to be 

	 *        		considered and committed as 1 transaction. If any of the statement fails

	 *        		then all the statement execution will be rollbacked. <BR>

	 *        	- If false, then all the statemens will be taken and 

	 *          	executed individually and committed after each execution.

	 * @return Return datatype will be as specified in statement.properties file.

	 * @throws SQLException

	 * @throws Exception

	 */

	public Object execute(String statementids[], Object tos[] ,Boolean isTransaction) throws SQLException, Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String[], Object[], Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 2");

		EBWLogger.trace(this,"Starting execute method stateids,tos,boolean in Implementation class ");

	    EBWLogger.trace(this,"Starting method  public Object execute(String statementids[], EBWTransferObject tos[] ,Boolean isTransaction)");

	    printStringArrayValues(statementids,"execute(String statementids[], EBWTransferObject tos[] ,Boolean isTransaction)");

	    printTOArrayValues(tos,"execute(String statementids[], EBWTransferObject tos[] ,Boolean isTransaction)");

	    EBWLogger.trace(this,"isTransaction :"+isTransaction);

	    

		Object returnVal=null;

		try{

	        



		    EBWLogger.logDebug(this,"Executing for Service Id :"+(String)serviceInfo.get("ServiceId".toUpperCase()));

		    queryExecutor.execute(statementids,tos);

		    

			if (!isTransaction.booleanValue())

			    con.commit();

		        

		    EBWLogger.trace(this,"Finished with executem method of implementation class");



		}

		catch(Exception excp){

			try{

		        excp.printStackTrace();

		        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}

		}finally{

		    if(!isTransaction.booleanValue()){

		        if(statementids!=null)

		        for(int i=0;i<statementids.length;i++)

		            EBWLogger.trace(this,"Closing databaseconnection for statement id: "+statementids[i]);

		       try{ 

		    	   close();

			    }catch(Exception e){

					  EBWLogger.logDebug(this,"Connection is already closed");

				  }

		    }

		}
		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String[], Object[], Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return returnVal;

	}				

	

	/**

	 * This method is used for pagination passing TO,HashMap and isTransaction as input parameter.

	 * It returns an Object which will be TO,ArrrayList,HashMap etc..

	 * @param objTO Transfer Object(TO)  is passed as input parameter

	 * @param isTransaction boolean value which will be true or false.

	 * @return Return datatype will be as specified in statement.properties file.	

	 * @throws Exception 

	 * @throws ClassNotFoundException if specified Driver Class is not found 

	 * @throws InstantiationException if EBWTransferObject cannot be instantiated

	 * @throws NoSuchMethodException if method invoked is not found in EBWTransferObject 

	 * @throws IllegalAccessException if there is not accessrights to access the method on the EBWTransferObject

	 * @throws InvocationTargetException if the specified method is not invoked properly 

	 * @throws NoSuchFieldException if the field lookedup on the EBWTransferObject is not there 

	 * @throws SQL exception if  Connection,Statement Object is not returned properly and if

	 *         there is any problem query execution

	 */

	public Object executeQuery( Object objTO, HashMap paginationMap , Boolean isTransaction)throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(Object, HashMap, Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 13");

		EBWLogger.trace(this,"Starting execute method in Implementation class ");

		EBWLogger.trace(this,"Starting method public Object executeQuery( EBWTransferObject objTO)");

		EBWLogger.trace(this,"objTO : "+objTO);

		Object returnVal=null;

		try{

		    EBWLogger.logDebug(this,"Service Id :"+(String)serviceInfo.get("ServiceId".toUpperCase()));

			//queryExecutor.setConnection((java.sql.Connection)serviceConnection);

		    returnVal = queryExecutor.executeQuery((String)serviceInfo.get("SERVICEID"), objTO, paginationMap);

		}catch(Exception excp){

			try{

		        excp.printStackTrace();

				if(isTransaction.booleanValue())

			        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		  if(!isTransaction.booleanValue()){

		      EBWLogger.trace(this,"Closing databaseconnection for :"+(String)serviceInfo.get("ServiceId".toUpperCase()));

		    try{  

		    	close();

		    }catch(Exception e){

				  EBWLogger.logDebug(this,"Connection is already closed");

			  }

		  }

		}

		

			EBWLogger.trace(this,"Finished with executem method public Object executeQuery( EBWTransferObject objTO)");
		
		elapsedTime = System.currentTimeMillis() - startTime;		
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(Object, HashMap, Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return returnVal;

	} 

	

	

	

	/**

	 * This method can be used to execute multiple statements with multiple TOs.

	 * 

	 * @param statementids

	 * @param tos

	 * @param isTransaction - Not Application . 

	 * @return As specified in the statement properties file.

	 * @throws SQLException

	 * @throws Exception

	 */

/*	public Object[] executeQuery(String statementids[], Object tos[] , HashMap paginationMap,Boolean isTransaction)throws SQLException, Exception {

		EBWLogger.trace(this,"rkv in 9");

	    EBWLogger.trace(this,"Starting execute method stateids,tos,boolean in Implementation class ");

	    EBWLogger.trace(this,"Starting method public Object[] executeQuery(String statementids[], EBWTransferObject tos[] ,Boolean isTransaction)");

	    printStringArrayValues(statementids,"executeQuery(String statementids[], EBWTransferObject tos[] ,Boolean isTransaction)");

	    Object returnVal[]=new Object[statementids.length];

	    Set set = new HashSet();	  

	    

		try

		{

			 if(paginationMap!=null){

		    	set = paginationMap.keySet(); 

		    	String[] array = (String[])set.toArray(new String[set.size()]);

		    	for(int z=0;z<array.length;z++)

		    		EBWLogger.trace(this,"array "+z +":"+array[z]);

			    for(int i=0;i<statementids.length;i++){

			    	if((paginationMap.get(array[i])!=null)){

				    		HashMap tempMap = new HashMap();

				    		tempMap.put(array[i],paginationMap.get(array[i]));

				    		EBWLogger.trace(this,"tempMap:"+tempMap);

				    		returnVal[i] = queryExecutor.executeQuery(statementids[i], tos[i],tempMap,"twotable");

				    		EBWLogger.logDebug(this,"Executing for Service Id :"+statementids[i]);

			    	}

			    }

			 }

		}

		catch(Exception excp){

			try{

		        excp.printStackTrace();

		        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		  if(!isTransaction.booleanValue()){

		      if(statementids!=null)

			        for(int i=0;i<statementids.length;i++)

			            EBWLogger.trace(this,"Closing databaseconnection for statement id: "+statementids[i]);

		  try{   

		    close();

		  }catch(Exception e){

			  EBWLogger.logDebug(this,"Connection is already closed");

		  }

		  }

		}

		

		    EBWLogger.trace(this,"Finished with executem method of implementation class");

		 return returnVal;

	} */  // CCHECK TODO add the rollback

		

		/**

		 * This method is used to execute same query will multiple parameters.

		 * @param statementid - String representing statement name as mentioned in statement property file.

		 * @param tos

		 * @param isTransaction <BR> 

		 * 		  - If true, then all the statements are to be 

		 *        	considered and committed as 1 transaction. If any of the statement fails

		 *        	then all the statement execution will be rollbacked. <BR>

		 *        - If false, then all the statemens will be taken and 

		 *          executed individually and committed after each execution.   

		 * @return Return datatype will be as specified in statement.properties file.

		 * @throws SQLException

		 * @throws Exception

		 */

		public Object execute(String statementid, Object tos[] ,Boolean isTransaction) throws SQLException, Exception{
			long startTime = System.currentTimeMillis();		
			long elapsedTime=0; 
			if (performanceLogger.isInfoEnabled()) {
				performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String, Object[], Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}

			EBWLogger.trace(this,"rkv in 3");

			EBWLogger.trace(this,"Starting execute method stateids,tos,boolean in Implementation class ");

		    EBWLogger.trace(this,"Starting method  public Object execute(String statementids[], EBWTransferObject tos[] ,Boolean isTransaction)");

		    EBWLogger.trace(this,"statementid:"+statementid);

		    printTOArrayValues(tos,"execute(String statementid, EBWTransferObject tos[] ,Boolean isTransaction)");

		    EBWLogger.trace(this,"isTransaction :"+isTransaction);		    

			Object returnVal="Success";			

			

		    try{



			    EBWLogger.logDebug(this,"Executing for Service Id :"+(String)serviceInfo.get("ServiceId".toUpperCase()));			

			    queryExecutor.execute(statementid,tos);

				if(!isTransaction.booleanValue())

				    con.commit();			    

			    EBWLogger.trace(this,"Finished with executem method of implementation class");

			}

			catch(Exception excp){

			try{

		        excp.printStackTrace();

		        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}

			}finally{

			    if(!isTransaction.booleanValue()){

			        if(statementid==null)

				        EBWLogger.trace(this,"Closing databaseconnection for statement id: "+statementid);

			       try{ 

			    	   close();

			       }catch(Exception e){

						  EBWLogger.logDebug(this,"Connection is already closed");

					  }

			    }

			}					


		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String, Object[], Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return returnVal;

	}

		

	/**

	 * This method is used to execute multiple queries using a single parameter Object.

	 * This method can be used in case we want to use some parameter values from the 

	 * parameter object in different queries or if the values in the parameter object are

	 * shared by more than 1 query.

	 * 

	 * @param statementids - String Array representing list of statement names as mentioned in statement property file.

	 * @param tos

	 * @param isTransaction <BR> 

	 * 		  - If true, then all the statements are to be 

	 *        	considered and committed as 1 transaction. If any of the statement fails

	 *        	then all the statement execution will be rollbacked. <BR>

	 *        - If false, then all the statemens will be taken and 

	 *          executed individually and committed after each execution.   

	 * @return Return datatype will be as specified in statement.properties file.

	 * @throws SQLException

	 * @throws Exception

	 */

	public Object execute(String statementids[], Object tos,Boolean isTransaction) throws SQLException, Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String[], Object, Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 3i");

		EBWLogger.trace(this,"Starting execute method stateids,tos,boolean in Implementation class ");

	    EBWLogger.trace(this,"Starting method public Object execute(String statementids[], EBWTransferObject tos,Boolean isTransaction)");

	    printStringArrayValues(statementids,"execute(String statementids[], EBWTransferObject tos,Boolean isTransaction)");

	    EBWLogger.trace(this,"tos :"+tos);

	    EBWLogger.trace(this,"isTransaction :"+isTransaction);

	    

		Object returnVal=null;

		Object tosarray[]=null;

		if(tos instanceof EBWTransferObject){

		    tosarray = new EBWTransferObject[statementids.length-1];

		    

		    /**

		     * This prototype looks similiar to execute(statementids[],Objects[])

		     * so fill the single TO in an array equal to the size to statementids and

		     * call the previous method itself, to avoid duplication of code. 

		     */

		    for(int i=0;i<statementids.length;i++)

		        tosarray[i] = (EBWTransferObject)tos;

		    

		    execute(statementids,tosarray,isTransaction);

		}

		else if(tos instanceof java.util.HashMap){

		    tosarray = new HashMap[statementids.length-1];

		    

		    /**

		     * This prototype looks similiar to execute(statementids[],Objects[])

		     * so fill the single TO in an array equal to the size to statementids and

		     * call the previous method itself, to avoid duplication of code. 

		     */

		    for(int i=0;i<statementids.length;i++)

		        tosarray[i] = (HashMap)tos;

		    

		    execute(statementids,tosarray,isTransaction);

		}
		
		elapsedTime = System.currentTimeMillis() - startTime;
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String[], Object, Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return returnVal;

	}

		

	/**

	 * This method can be used if the serviceid stored in the service

	 * object is to be used as statementid.

	 * 

	 * @param objTO

	 * @param isTransaction <BR> 

	 * 		  - If true, then all the statements are to be 

	 *        	considered and committed as 1 transaction. If any of the statement fails

	 *        	then all the statement execution will be rollbacked. <BR>

	 *        - If false, then all the statemens will be taken and 

	 *          executed individually and committed after each execution.   

	 * @return Return datatype will be as specified in statement.properties file.

	 * @throws Exception

	 */

	

	public Object execute( Object objTO,Boolean isTransaction) throws Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(Object, Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 4");

		EBWLogger.trace(this,"Starting execute method in Implementation class");

		EBWLogger.trace(this,"Starting method public String execute( Object objTO,Boolean isTransaction)");

		EBWLogger.trace(this,"objTO : "+objTO);

		

		Object returnVal="0";					

		try{	

			returnVal=queryExecutor.execute((String)serviceInfo.get("ServiceId".toUpperCase()),objTO);

			if(!isTransaction.booleanValue())

				con.commit();

		}		

		catch(Exception excp){

			try{

		        excp.printStackTrace();

		        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		   if(!isTransaction.booleanValue()){

		    EBWLogger.trace(this,"Closing databaseconnection for statement id: "+(String)serviceInfo.get("ServiceId".toUpperCase()));

		    try{

		    	close();

		    }catch(Exception e){

				  EBWLogger.logDebug(this,"Connection is already closed");

			  }

		   }

		}

		EBWLogger.logDebug(this,(String)serviceInfo.get("ServiceId".toUpperCase())+" execution over...");

		EBWLogger.trace(this,"Finished with executiong of method public String execute( EBWTransferObject objTO)");

		
		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(Object, Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return returnVal;

	}



	

	

	public Object execute( Object objTO[],Boolean isTransaction) throws Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(Object[], Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 4");

		EBWLogger.trace(this,"Starting execute method in Implementation class");

		EBWLogger.trace(this,"Starting method public String execute( Object objTO,Boolean isTransaction)");

		EBWLogger.trace(this,"objTO : "+objTO);

		

		Object returnVal="0";					

		try{	

			returnVal=queryExecutor.execute((String)serviceInfo.get("ServiceId".toUpperCase()),objTO);

			if(!isTransaction.booleanValue())

				con.commit();

		}		

		catch(Exception excp){

			try{

		        excp.printStackTrace();

		        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		   if(!isTransaction.booleanValue()){

		    EBWLogger.trace(this,"Closing databaseconnection for statement id: "+(String)serviceInfo.get("ServiceId".toUpperCase()));

		    try{

		    	close();

		    }catch(Exception e){

				  EBWLogger.logDebug(this,"Connection is already closed");

			  }

		   }

		}

		EBWLogger.logDebug(this,(String)serviceInfo.get("ServiceId".toUpperCase())+" execution over...");

		EBWLogger.trace(this,"Finished with executiong of method public String execute( EBWTransferObject objTO)");

		
		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(Object[], Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return returnVal;

	}

	

	

	/**

	 * This method is used to execute a single statementid with single parameter object.

	 * 

	 * @param statementid - String representing statement name as mentioned in statement property file.

	 * @param objTO

	 * @param isTransaction - Not application, can be given as true always.

	 * @return Return datatype will be as specified in statement.properties file.

	 * @throws SQLException

	 * @throws Exception

	 */



	 // CHECK WHAT IS DEFAULT  -- isTransaction is false

	

	public String execute( String statementid, Object objTO) throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String, Object) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 5");

		EBWLogger.trace(this,"Starting execute method in Implementation class");

		EBWLogger.trace(this,"Starting method public String execute( String statementid, EBWTransferObject objTO)");

		EBWLogger.trace(this,"statementid :"+statementid);

		EBWLogger.trace(this,"objTO :"+objTO);

		

		String returnVal="0";

		

		try{		 

			EBWLogger.logDebug(this,"Pre Connection in execute is :"+con);

			queryExecutor.execute(statementid,objTO);

			EBWLogger.logDebug(this,"Connection in execute is :"+con);

			con.commit();



		}

		catch(Exception excp){

			try{

		        excp.printStackTrace();

		        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		    EBWLogger.trace(this,"Closing databaseconnection for statement id: "+statementid);

		    try{

		    	close();

		    }catch(Exception e){

				  EBWLogger.logDebug(this,"Connection is already closed");

			  }

		}

		EBWLogger.logDebug(this,statementid+" execution over...");

		EBWLogger.trace(this,"Finished with execute method of implementation class");

		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String, Object) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	return returnVal;

	}

	

	/**

	 * This method is used to execute a single statementid with single parameter object.

	 * @param statementid String representing statement name as mentioned in statement property file.

	 * @param objTO	

	 * @param isTransaction <BR> 

	 * 		  - If true, then all the statements are to be 

	 *        	considered and committed as 1 transaction. If any of the statement fails

	 *        	then all the statement execution will be rollbacked. <BR>

	 *        - If false, then all the statemens will be taken and 

	 *          executed individually and committed after each execution.  

	 * @return String  	 

	 * @throws SQLException

	 * @throws Exception

	 */

	

	

	public String execute( String statementid, Object objTO, Boolean isTransaction) throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String, Object, Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 6");

		EBWLogger.trace(this,"Starting execute method in Implementation class");

		EBWLogger.trace(this,"Starting method public String execute( String statementid, EBWTransferObject objTO)");

		EBWLogger.trace(this,"statementid :"+statementid);

		EBWLogger.trace(this,"objTO :"+objTO);

		

		String returnVal="0";

		

		try{		

			queryExecutor.execute(statementid,objTO);

			if (!isTransaction.booleanValue())

			    con.commit();

		}		

		catch(Exception excp){

			try{

		        excp.printStackTrace();

		        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}

		}finally{

		    if(!isTransaction.booleanValue()){

		        EBWLogger.trace(this,"Closing databaseconnection for statement id: "+statementid);

		        try{

		        	close();

		        }catch(Exception e){

					  EBWLogger.logDebug(this,"Connection is already closed");

				  }

		    }

		}

		EBWLogger.logDebug(this,statementid+" execution over...");

		EBWLogger.trace(this,"Finished with execute method of implementation class");
		
		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String, Object, Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	return returnVal;

	}

	

	/**

	 * This method is used to execute multiple statementid with multiple parameter objects.

	 * @param stateids - Statement Ids to be executed as defined in statement.properties file.

	 * @param to1 	- Transfer Object1 having form data.

	 * @param to2	- Transfer Object2 having form data.

	 * @return		- Returns number of rows affected

	 * @throws SQLException

	 * @throws Exception

	 */

	

	public String execute( String stateids[], Object to1,Object to2,Boolean isTransaction)throws Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String[], Object, Object, Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 7");

		EBWLogger.trace(this,"Starting execute [],to,to method in Implementation class");

		String returnVal="0";

			

			Object tos[] ={ to1, to2};

			execute(stateids,tos,isTransaction);

			EBWLogger.logDebug(this,stateids[0]+" execution1 over...");

			

			EBWLogger.trace(this,"Finished with execute method of implementation class");
		
		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - execute(String[], Object, Object, Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return returnVal;

	}

			

	/**

	 * This method can be used when calling execute query from custom built services extending

	 * DatabaseService class.

	 * @param statementids Statement Ids to be executed as defined in statement.properties file.

	 * @param tos Object[]  TO[] to be executed as defined in the properties file

	 * @return Object[] may be TO[] etc.. will be returned as ouput.

	 * @throws SQLException

	 * @throws Exception

	 */

	

	public Object[] executeQuery(String statementids[], Object tos[] )throws SQLException, Exception {
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String[], Object[]) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 8");

		Object[] returnObjectArray = executeQuery(statementids, tos, new Boolean(false));
		elapsedTime = System.currentTimeMillis() - startTime;
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String[], Object[]) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	    return returnObjectArray;

	}

	

	/**

	 * This method can be used to execute multiple statements with multiple TOs.

	 * 

	 * @param statementids - Statement Ids to be executed as defined in statement.properties file.

	 * @param tos

	 * @param isTransaction - Not Application . 

	 * @return As specified in the statement properties file.

	 * @throws SQLException

	 * @throws Exception

	 */

	public Object[] executeQuery(String statementids[], Object tos[] ,Boolean isTransaction)throws SQLException, Exception {
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String[], Object[], Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 9");

	    EBWLogger.trace(this,"Starting execute method stateids,tos,boolean in Implementation class ");

	    EBWLogger.trace(this,"Starting method public Object[] executeQuery(String statementids[], EBWTransferObject tos[] ,Boolean isTransaction)");

	    printStringArrayValues(statementids,"executeQuery(String statementids[], EBWTransferObject tos[] ,Boolean isTransaction)");

	    Object returnVal[]=new Object[statementids.length];



		try

		{

		    for(int i=0;i<statementids.length;i++){		

		        returnVal[i] = queryExecutor.executeQuery(statementids[i], tos[i]);

		        EBWLogger.logDebug(this,"Executing for Service Id :"+statementids[i]);

		    }

		}

		catch(Exception excp){

			try{

		        excp.printStackTrace();

		        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		  if(!isTransaction.booleanValue()){

		      if(statementids!=null)

			        for(int i=0;i<statementids.length;i++)

			            EBWLogger.trace(this,"Closing databaseconnection for statement id: "+statementids[i]);

		      try{

		        	close();

		        }catch(Exception e){

					  EBWLogger.logDebug(this,"Connection is already closed");

				  }

		  }

		}		

		    EBWLogger.trace(this,"Finished with executem method of implementation class");
		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String[], Object[], Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		 return returnVal;

	}   // CHECK TODO add the rollback



	

	

	

	

	/**

	 * This method can be used when calling execute query from custom built services extending

	 * DatabaseService class.

	 * @param  statementids[] - Statement Ids to be executed as defined in statement.properties file.

	 * @param to Transfer Object is passed as input parameter

	 * @return Object[] Transfer Object Array will be returned  which is specified in the .properties file

	 * @throws SQLException

	 * @throws Exception 

	 *  

	 */

	public Object[] executeQuery(String statementids[], Object to ) throws SQLException, Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String[], Object) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 10");

		Object[] returnObjectArray = executeQuery(statementids, to, new Boolean(false));
		elapsedTime = System.currentTimeMillis() - startTime;
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String[], Object) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	    return returnObjectArray;

	}

	

	

	/**

	 * This method is used to execute list of statements with one parameter object which

	 * can have values shared across multiple statements. 

	 * @param statementids  - String Array representing list of statement names as mentioned in statement property file. 

	 * @param to

	 * @param isTransaction <BR> 

	 * 		  - If true, then all the statements are to be 

	 *        	considered and committed as 1 transaction. If any of the statement fails

	 *        	then all the statement execution will be rollbacked. <BR>

	 *        - If false, then all the statemens will be taken and 

	 *          executed individually and committed after each execution.  

	 * @return Return datatype will be as specified in statement.properties file.

	 * @throws SQLException

	 * @throws Exception

	 */

	/*public Object[] executeQuery(String statementids[], Object to , HashMap paginationMap,Boolean isTransaction) throws SQLException, Exception{

		EBWLogger.trace(this,"rkv in 30");

		//System.out.println("came inside database service new method");

		System.out.println("Pagination Map :"+paginationMap);

	    EBWLogger.trace(this,"Starting execute method stateids,tos,boolean in Implementation class ");

	    EBWLogger.trace(this,"Starting method public Object[] executeQuery(String statementids[], EBWTransferObject to ,Boolean isTransaction)");

	    printStringArrayValues(statementids,"executeQuery(String statementids[], EBWTransferObject to ,Boolean isTransaction)");

	    EBWLogger.trace(this,"to :"+to);

	    EBWLogger.trace(this,"isTransaction :"+isTransaction);

	    Set set = new HashSet();	             

		Object returnVal[]=new Object[statementids.length];

	   try{

	        for(int i=0;i<statementids.length;i++){	       

		        returnVal[i] = queryExecutor.executeQuery(statementids[i], to);

		        EBWLogger.logDebug(this,"Executing for Service Id :"+statementids[i]);

		    }

		    

		    EBWLogger.trace(this,"Finished with executem method of implementation class");

		}

		try

		{

			 if(paginationMap!=null){

				 HashMap pgnInfoMap = (HashMap)paginationMap.get("pgnInfoMap");

				 HashMap toQueryExecutorMap = (HashMap)paginationMap.get("toQueryExecutor");

		    	set = paginationMap.keySet(); 

		    	String[] array = (String[])set.toArray(new String[set.size()]);

		    	for(int z=0;z<array.length;z++)

		    		EBWLogger.trace(this,"array "+z +":"+array[z]);

			    for(int i=0;i<statementids.length;i++){

			    	String key = statementids[i]+"_PaginationFlag";

			    	//System.out.println( "key :"+key);

			    	//System.out.println("pgnInfoMap.get(key)) :"+(String)pgnInfoMap.get(key));

			    	if(((String)pgnInfoMap.get(key)).equalsIgnoreCase("y")){

			    			returnVal[i] = queryExecutor.executeQuery(statementids[i], to,toQueryExecutorMap,"twotable");

				    		EBWLogger.logDebug(this,"Executing for Service Id :"+statementids[i]);

				    }else{

				    	returnVal[i] = queryExecutor.executeQuery(statementids[i], to);

			    	

			    	}

			    }

			 }

		}

		catch(Exception excp){

			try{

		        excp.printStackTrace();

		        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		  if(!isTransaction.booleanValue()){

		      if(statementids!=null)

			        for(int i=0;i<statementids.length;i++)

			            EBWLogger.trace(this,"Closing databaseconnection for statement id: "+statementids[i]);

		      try{

		        	close();

		        }catch(Exception e){

					  EBWLogger.logDebug(this,"Connection is already closed");

				  }

		  }

		}

		

		return returnVal;

	}

	*/

	

	/****************/

	

	public Object[] executeQuery(String statementids[], Object to ,Boolean isTransaction) throws SQLException, Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String[], Object, Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 11");

	    EBWLogger.trace(this,"Starting execute method stateids,tos,boolean in Implementation class ");

	    EBWLogger.trace(this,"Starting method public Object[] executeQuery(String statementids[], EBWTransferObject to ,Boolean isTransaction)");

	    printStringArrayValues(statementids,"executeQuery(String statementids[], EBWTransferObject to ,Boolean isTransaction)");

	    EBWLogger.trace(this,"to :"+to);

	    EBWLogger.trace(this,"isTransaction :"+isTransaction);

	                

		Object returnVal[]=new Object[statementids.length];

	   try{

	        for(int i=0;i<statementids.length;i++){	       

		        returnVal[i] = queryExecutor.executeQuery(statementids[i], to);

		        EBWLogger.logDebug(this,"Executing for Service Id :"+statementids[i]);

		    }

		    

		    EBWLogger.trace(this,"Finished with executem method of implementation class");

		}

		catch(Exception excp){

			try{

		        excp.printStackTrace();

		        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		  if(!isTransaction.booleanValue()){

		      if(statementids!=null)

			        for(int i=0;i<statementids.length;i++)

			            EBWLogger.trace(this,"Closing databaseconnection for statement id: "+statementids[i]);

		      try{

		        	close();

		        }catch(Exception e){

					  EBWLogger.logDebug(this,"Connection is already closed");

				  }

		  }

		}

		
		elapsedTime = System.currentTimeMillis() - startTime;
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String[], Object, Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return returnVal;

	}

	/*******************/

	

	/**

	 * This method can be used when calling execute query from custom built services extending

	 * DatabaseService class.

	 * @param objTO as input parameter is passed 

	 * @return Object will be returned which will be TO,ArrayList,Vector etc....

	 * @throws SQLException

	 * @throws Exception

	 * 

	 */

	public Object executeQuery( Object objTO )throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(Object) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 12");

		Object returnObject = executeQuery(objTO, new Boolean(false));
		elapsedTime = System.currentTimeMillis() - startTime;
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(Object) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	    return returnObject;

	}

	

	/**

	 * This method can be used when calling execute query from custom built services extending

	 * DatabaseService class.

	 * @param objTO TO is passed as input parameter

	 * @param isTransaction <BR> 

	 * 		  - If true, then all the statements are to be 

	 *        	considered and committed as 1 transaction. If any of the statement fails

	 *        	then all the statement execution will be rollbacked. <BR>

	 *        - If false, then all the statemens will be taken and 

	 *          executed individually and committed after each execution.  

	 * @return Return datatype will be as specified in statement.properties file.

	 * @throws SQLException

	 * @throws Exception

	 */

	

	public Object executeQuery( Object objTO, Boolean isTransaction)throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(Object, Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 13");

		EBWLogger.trace(this,"Starting execute method in Implementation class ");

		EBWLogger.trace(this,"Starting method public Object executeQuery( EBWTransferObject objTO)");

		EBWLogger.trace(this,"objTO : "+objTO);

		Object returnVal=null;

		try{

		    EBWLogger.logDebug(this,"Service Id :"+(String)serviceInfo.get("ServiceId".toUpperCase()));

			returnVal = queryExecutor.executeQuery((String)serviceInfo.get("ServiceId".toUpperCase()), objTO);

		}catch(Exception excp){

			try{

		        excp.printStackTrace();

				if(isTransaction.booleanValue())

			        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		  if(!isTransaction.booleanValue()){

		      EBWLogger.trace(this,"Closing databaseconnection for statement id: "+(String)serviceInfo.get("ServiceId".toUpperCase()));

		      try{

		        	close();

		        }catch(Exception e){

					  EBWLogger.logDebug(this,"Connection is already closed");

				  }

		  }

		}

		

			EBWLogger.trace(this,"Finished with executem method public Object executeQuery( EBWTransferObject objTO)");

		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(Object, Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return returnVal;

	} // CHECK HANDLE try & catch block

			

	

	/**

	 * This method can be used when calling execute query from custom built services extending

	 * DatabaseService class.

	 * @param statementid - String representing statement name as mentioned in statement property file.

	 * @param params

	 * @param isTransaction <BR> 

	 * 		  - If true, then all the statements are to be 

	 *        	considered and committed as 1 transaction. If any of the statement fails

	 *        	then all the statement execution will be rollbacked. <BR>

	 *        - If false, then all the statemens will be taken and 

	 *          executed individually and committed after each execution.  

	 * @return Return datatype will be as specified in statement.properties file.

	 * @throws SQLException

	 * @throws Exception

	 */

	public Object executeQuery( String statementid, HashMap params, Boolean isTransaction) throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String, HashMap, Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 15");

		EBWLogger.trace(this,"Starting execute method in Implementation class");

		EBWLogger.trace(this,"public Object executeQuery( String statementid, HashMap params");

		EBWLogger.trace(this,"statementid: "+statementid);

		EBWLogger.trace(this,"params: "+params);

		

		Object returnVal=null;

		try{			

			returnVal = queryExecutor.executeQuery(statementid,params);

			EBWLogger.logDebug(this,statementid+" execution over...");

	

		}catch(Exception excp){

			try{

		        excp.printStackTrace();

				if(isTransaction.booleanValue())

			        con.rollback();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		  if(!isTransaction.booleanValue()){

		     EBWLogger.trace(this,"Closing databaseconnection for statement id: "+statementid);

		     try{

		        	close();

		        }catch(Exception e){

					  EBWLogger.logDebug(this,"Connection is already closed");

				  }

		  }

		}

		EBWLogger.trace(this,"Finished with execute method of implementation class");
		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String, HashMap, Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	return returnVal;

	}

	

	

	/**

	 * This method can be used when calling execute query from custom built services extending

	 * DatabaseService class.

	 * @param statementid - String representing statement name as mentioned in statement property file.

	 * @param params

	 * @param isTransaction <BR> 

	 * 		  - If true, then all the statements are to be 

	 *        	considered and committed as 1 transaction. If any of the statement fails

	 *        	then all the statement execution will be rollbacked. <BR>

	 *        - If false, then all the statemens will be taken and 

	 *          executed individually and committed after each execution.  

	 * @return Return datatype will be as specified in statement.properties file.

	 * @throws SQLException

	 * @throws Exception

	 */

	public Object executeQuery( String statementid, HashMap params[], Boolean isTransaction) throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0;
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String, HashMap[], Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 16");

		EBWLogger.trace(this,"Starting execute method in Implementation class");

		EBWLogger.trace(this,"public Object executeQuery( String statementid, HashMap params");

		EBWLogger.trace(this,"statementid: "+statementid);

		EBWLogger.trace(this,"params: "+params);

		

		Object returnVal = null;

		try {



			for (int i = 0; i < params.length; i++) {

				returnVal = queryExecutor.executeQuery(statementid, params[i]);

				EBWLogger.logDebug(this, statementid + " execution over...");

			}

		} catch (Exception excp) {

			try {

				excp.printStackTrace();

				if (isTransaction.booleanValue())

					con.rollback();

				throw excp; // Should throw ebwException

			} catch (SQLException sql2) {

				sql2.printStackTrace();

				throw sql2; // Should do a throw of excep + sql2

			}



		} finally {

			if (!isTransaction.booleanValue())

				try{

		        	close();

		        }catch(Exception e){

					  EBWLogger.logDebug(this,"Connection is already closed");

				  }

		}



		EBWLogger.trace(this,"Finished with execute method of implementation class");
		
		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String, HashMap[], Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	return returnVal;

	}

	



	/**

	 * This method can be used when calling execute query from custom built services extending

	 * DatabaseService class.

	 * @param statementid which will be read from the .properties file

	 * @param params 

	 * @return Return datatype will be as specified in statement.properties file.

	 * @throws SQLException

	 * @throws Exception

	 */

	public Object executeQuery( String statementid, Object params ) throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String, Object) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 17");

		Object returnObject = executeQuery(statementid, params, new Boolean(false));
		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String, Object) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	    return returnObject;



	}

	

	/**

	 * This method can be used when calling execute query from custom built services extending

	 * DatabaseService class. 

	 * @param statementid - String representing statement name as mentioned in statement property file.

	 * @param params -object  wuill be passed as other parameter(TO etc..)

	 * @param isTransaction <BR> 

	 * 		  - If true, then all the statements are to be 

	 *        	considered and committed as 1 transaction. If any of the statement fails

	 *        	then all the statement execution will be rollbacked. <BR>

	 *        - If false, then all the statemens will be taken and 

	 *          executed individually and committed after each execution.  

	 * @return Return datatype will be as specified in statement.properties file.

	 * @throws SQLException

	 * @throws Exception

	 */

	public Object executeQuery( String statementid, Object params, Boolean isTransaction) throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String, Object, Boolean) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 18");

		EBWLogger.trace(this,"Starting execute method in Implementation class");

		EBWLogger.trace(this,"statementid : "+statementid);

		EBWLogger.trace(this,"EBWTransferObject : "+params);

		Object returnVal=null;

		try{



			EBWLogger.logDebug(this,"Connection set in queryExecutor");

			returnVal = queryExecutor.executeQuery(statementid,params);

			EBWLogger.logDebug(this,statementid+" execution over...");

			}catch(Exception excp){

			try{

		        excp.printStackTrace();

				if(isTransaction.booleanValue())

			        con.rollback();

				

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

		  if(!isTransaction.booleanValue())

			  try{

		        	close();

		        }catch(Exception e){

					  EBWLogger.logDebug(this,"Connection is already closed");

				  }

		}



			EBWLogger.trace(this,"Finished with execute method of implementation class");
		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery(String, Object, Boolean) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	return returnVal;

	}

	

	/**

	 * This method can be used when calling execute query from custom built services extending

	 * DatabaseService class.

	 * @return Object

	 * @throws SQLException

	 * @throws Exception

	 */

	public Object executeQuery() throws SQLException,Exception{
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 

		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery() - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		EBWLogger.trace(this,"rkv in 19");

		EBWLogger.trace(this,"Starting execute method in Implementation class");

		EBWLogger.trace(this,"Into Method Object executeQuery() ");

		

		Object returnVal=null;

		HashMap params = new HashMap();

		String statementid = (String)serviceInfo.get("ServiceId".toUpperCase());



		try{

			returnVal = queryExecutor.executeQuery(statementid,params);

			EBWLogger.logDebug(this,statementid+" execution over...");



		}catch(Exception excp){

			try{

		        excp.printStackTrace();

		        throw excp;	//Should throw ebwException

		    }catch(SQLException sql2){

		        sql2.printStackTrace();

				throw sql2;   // Should do a throw of excep + sql2

			}



		}finally{

			try{

	        	close();

	        }catch(Exception e){

				  EBWLogger.logDebug(this,"Connection is already closed");

			  }

		}

		EBWLogger.trace(this,"Finished with execute method of implementation class");

		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : "+getIPAddr()+" - Thread Id : " + Thread.currentThread().getId() + " - " + this.getClass().getName() + " - executeQuery() - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	return returnVal;

	}

	

	/**

	 * This method is used to print the values of Statement Ids

	 * @param statementids StatementIds are passed as parameter

     * @param methodName  Method Name is passed

	 */

		

	private void printStringArrayValues(String statementids[],String methodName){

	    if(statementids!=null){

		    for(int i=0;i< statementids.length;i++)

		        EBWLogger.trace(" DatabaseService","statementids[] :"+statementids[i]);

	    }else

	        EBWLogger.logError(" DatabaseService","Value of String Array in method "+methodName+" is NULL");

	}

	

	/**

	 * 

	 * This method is used to print the values of Statement Ids

	 * @param tos Object[] are passed as parameter which will be TO Array read from the .properties file

     * @param methodName  Method Name is passed

	 */

	

	private void printTOArrayValues(Object tos[],String methodName){

	    if(tos!=null){

	        for(int i=0;i< tos.length;i++)

		        EBWLogger.trace(this,"tos[] :"+tos[i]);

	    }

	    else

	        EBWLogger.logError(this,"Value of TO Array in method "+methodName+" is NULL");

	        

	}

	

	/**

	 * This method is to commit transaction for a db operation

	 * @throws SQLException

	 */

	

	protected void comitAllTransactions() throws SQLException{

	    if(serviceConnection!=null){

			if(serviceConnection instanceof java.sql.Connection)

			((Connection)serviceConnection).commit();

		}

	}

	

	/**

	 *   This method checks for the connection object type and calls the 

	 *   close method corresponding to that connection object.

	 */

	public Object close() throws SQLException {			

	EBWLogger.logDebug(this,"***** ***** serviceConnection: "+serviceConnection);

	EBWLogger.logDebug(this,"Service Connection closed and Nullified");

			if(serviceConnection!=null){

				if(serviceConnection instanceof java.sql.Connection){

					try{

						(con).close();

			        }catch(Exception e){

						  EBWLogger.logDebug(this,"Connection is already closed");

					  }

				EBWLogger.logDebug(this,"Service Connection closed and Nullified");

				}

			}

		return "0";

	}



	/**

	 * This method sets the connection object for the service. This connection

	 * object will be created and set prior to calling any service.

	 * 

	 * @connectionObj - Connection object which can be a java.sql.Connection in case

	 *                  of Database connection, java.net.HttpURLConnection object in

	 *  				case of Webservices etc based on the type of system and connection.

	 * 					The Connection Handler classes will type cast the Object type of 

	 * 					connection to corresponding type.

	 * 

	 */

	public Object setConnection(Object connectionObj) {

		this.serviceConnection = connectionObj;

		if(connectionObj!=null)

		{

		    con = (java.sql.Connection)connectionObj;

		    

		    if(!DB_AUTO_COMMIT)

		    {

		    	try

		    	{

		    		con.setAutoCommit(false);

		    	}

		    	catch(SQLException e)

		    	{

		    		e.printStackTrace();

		    	}

		    }

		    

			queryExecutor.setConnection(con);

		}

		return null;

	}



	public String getIPAddr(){
		String ipAddress ="";
		try{
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		}catch(Exception e){}
		return ipAddress;
	}


	public LinkedHashMap getBusinessServiceInfo(String serviceId) throws Exception{return null;}

	

	public LinkedHashMap getBusinessSystemInfo(String serviceId) throws Exception{return null;}

}

