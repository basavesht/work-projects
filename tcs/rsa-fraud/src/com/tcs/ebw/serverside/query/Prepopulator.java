/**
 * Prepopulator
 * 
 * Version 1.0
 * 
 * Date 26/12/2005
 * 
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 *
 */

package com.tcs.ebw.serverside.query;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.Method;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.transferobject.EBWTransferObject;
import com.tcs.ebw.common.util.StringUtil;
import com.tcs.ebw.exception.EbwException;
import java.util.*;

/**
 * Prepopulator class is used to prepopulate any TransferObject to be sent to 
 * its related table. It will be prepopulating the attributes of the 
 * according to the detail given in the table DefaultCriteria. 
 * It has a method called setTransferObject which takes up 
 * TransferObject as its argument which is the object which needs to be
 * populated. 
 * 
 * @author      tcs
 * @since       1.0
 
 */
public class Prepopulator {
	/**
	 * Class Reference 
	 */
	//private static Class  cls;	      
	
	//private static String  sequence="";
	
	/**
	 * Method reference holding method name
	 */
	//private static Method method;    
	
	/**
	 *  Values of Condition Clause of SQL Query
	 */
	//private static String where[];  
	
	/**
	 * ClassName of this class
	 */
	//private static String className; 
	
	/**
	 * SelectQuery is used for fetching records from DB
	 */
	//private static String SELECTQUERY;
	
	/**
	 * LookUpQuery is used to fetch record from DB based on DefaultType LOOKUP
	 */
	//private static String LOOKUPQUERY; 
	
	/**
	 * SequenceQuery is  for fetching from DB based on DefaultType Sequence
	 */		
	//private static String SEQUENCEQUERY;
	
	/**
	 * UpdateSequence is for updating IDGENERATOR based on DefaultType Sequence
	 */	
	//private static String UPDATESEQUENCE;
	
	/**
	 * Attribute is attribute of TO
	 */	
	private static final  String  ATTRIBUTE = "Attribute";
	
	/**
	 * AttributeType is the dataType of the attrbute
	 */	
	private static final String ATTRIBUTETYPE = "AttributeType";
	
	/**
	 * DefaultType is the defaultType of the attribute in DefaultMaster
	 */
	private static final String DEFAULTTYPE = "DefaultType";
	
	/**
	 * DefaultValue is the defaultValue of the attribute in DefaultMaster
	 */		
	private static final String DEFAULTVALUE = "DefaultValue";
	
	/**
	 * Predefined is the DefaultType of the attribute
	 */
	
	private static final String PREDEFINED = "Predefined";
	
	/**
	 * Sequence is is the DefaultType of the attribute
	 */
	private static final String SEQUENCE = "Sequence";
	
	/*
	 * Sequence is is the DefaultType of the attribute,db2 default type
	 */
	private static final String SEQUENCENUM = "Sequencenum";
	
	/**
	 * Lookup is the DefaultType of the attribute
	 */	
	private static final String LOOKUP = "Lookup";
	
	/**
	 * LookupId is the ID based on LOOKUP
	 */	
	private static final String LOOKUPID = "LookupId";
	
	/**
	 * Prefix is the attribute of IDGENERATOR
	 */	
	private static final String PREFIX = "Prefix";
	
	/**
	 *CurrentValue is the attribute of IDGENERATOR
	 */
	private static final String CURRENTVALUE = "CurrentValue";
	
	/**
	 * IDLength is the attribute of IDGENERATOR
	 */
	private static final String IDLENGTH = "IDLength";
	
	/**
	 * varaible to check whether the variable used in the
	 * clauses in SQL Statement is numeric type.
	 */
	private static final String VARCHAR = "varchar";
	
	/**
	 * varaible to check whether the variable used in the
	 * clauses in SQL Statement is numeric type.
	 */
	private static final String NUMERIC = "numeric";
	
	/**
	 * varaible to check whether the variable used in the
	 * clauses in SQL Statement is numeric type.
	 */
	private static final String DATE = "date";
	/** 
	 * variable used while invoking set method on an Object
	 * thru reflection
	 */
	private static final String SET = "set";
	/**
	 * variable used while invoking get method on an Object
	 * thru reflection 
	 */	
	private static final String GET = "get";
	
	/**
	 * value used as ZERO in steade of 0.
	 */	
	private static final String ZERO = "0";
	

	/**
	 * vairable used to check whether the field of the 
	 * TransferObject class is of type java.lang.String 
	 * 
	 */
	private static final String STRING = "java.lang.String";
	
	private static HashMap defValueMap = new HashMap();
	
	/**
	 * PrepredStatemet reference is PreCompiled Statement used for fetching record from DB
	 */	
	//private static PreparedStatement pStmt=null; 
		
	/**
	 * This function is used to fetch the data from DefaultMaster and 
	 * put it in  a ArrayList.	 
	 * @param transferObj the object which needs to be filled 
	 * throws SQLException
	 */
		
	
	public static  ArrayList LoadTODefValues(final EBWTransferObject transferObj,Connection con) throws Exception{
		EBWLogger.trace("Prepopulator :","Starting Method LoadTODefValues with EBWTransferObject is: "+transferObj);
		ArrayList defValueList=null;		
		try{
			
			String className = transferObj.getClass().getName();
			
			
			if(defValueMap==null || (defValueMap!=null && defValueMap.get(className)==null)){
			String SELECTQUERY = "Select * from DefaultMaster " +	"where Class=?";
			 
			
			 String where[]  = new String[]{className};
				for(int i=0;i<where.length;i++){
					EBWLogger.logDebug("Prepopulator","Value of WHERE Clause is :"+where.length);	
				}						
				//PreparedStatement pStmt = con.prepareStatement(SELECTQUERY);
				ResultSet rs = executeQuery(SELECTQUERY,where,false,con);
				EBWLogger.trace("Prepopulator :","resultset from  prepop "+rs);
				
				defValueList=new ArrayList();
				if(rs!=null){
				while(rs.next()){				 
				 String attributeName =  rs.getString(ATTRIBUTE);
				 String defaultValue =   rs.getString(DEFAULTVALUE);				 				 	
				 String defaultType =    rs.getString(DEFAULTTYPE);				
				 String attributeType = rs.getString(ATTRIBUTETYPE);			
				 
				 EBWLogger.logDebug("Prepopulator","Vlaue of attributeName is :"+attributeName);
				 EBWLogger.logDebug("Prepopulator","Vlaue of defaultValue is :"+defaultValue);
				 EBWLogger.logDebug("Prepopulator","Vlaue of defaultType is :"+defaultType);
				 EBWLogger.logDebug("Prepopulator","Vlaue of attributeType is :"+attributeType);
				 
				 defValueList.add(attributeName);
				 defValueList.add(defaultValue);
				 defValueList.add(defaultType);
				 defValueList.add(attributeType);				 	
				 }
				ArrayList cacheArrayList = new ArrayList();
				 
				cacheArrayList = (ArrayList)defValueList.clone();
				
				defValueMap.put(className, cacheArrayList);

					 rs.close();
				     rs=null;
				
				 	// pStmt.close();
				   //  pStmt=null;

				}
				
					
//				 if(con!=null)
//				 con.close();
			}else{
				if(defValueMap.get(className)!=null)
					defValueList=(ArrayList)defValueMap.get(className);
				}
		}catch(Exception e){
			e.printStackTrace();
			
		}
		EBWLogger.trace("Prepopulator :","Exit from  LoadTODefValues ");		
		return defValueList;
	}
	
		
	/**
	 * The transferObj is the Object on which the unfilled attributes
	 * needs to be filled by calling up their corresponding setter methods
	 * @param attribute Attribute is passed as parameter to fetch data from DefaultMaster
	 * @param transferObj the object which needs to be filled
	 * throws SQLException
	 */
	/*
	public static void fillTransferObject(final EBWTransferObject transferObj, String attribute,Connection con)throws Exception{
		
	    	EBWLogger.trace("Prepopulator :","Starting Method fillTransferObject with EBWTransferObject :"+ transferObj+",Attribute :"+attribute+")");	   
	    	String SELECTQUERY = "Select * from DefaultMaster " +"where Class=? And Attribute=?";			
		    
			String LOOKUPQUERY = "Select * from LookupMaster " +"where DomainId=? and Description=?";
			
			String SEQUENCEQUERY = "SELECT * FROM IDGenerator where TOClass=? AND Attribute=?";
			
			String UPDATESEQUENCE = "Update IDGenerator set currentvalue=? " +"where TOClass=? AND Attribute=?";
			
			String className = transferObj.getClass().getName();
		
		    EBWLogger.logDebug("Prepopulator Fill TO " , "TO Class  to be Prepopulated " + className);		    		  	    				    	

			String where[]  = new String[]{className,attribute};

			EBWLogger.logDebug("Preopopulator - FillTO", "Executing the Select Query to retrieve information for Prepopulation");
			
			ResultSet rs = executeQuery(SELECTQUERY,where,false,con);
			
			EBWLogger.logDebug("Preopopulator - FillTO", "Finished Executing the Select Query to retrieve information for Prepopulation");
			
			where = null;
			
			if(rs!=null){
				while(rs.next()){			
					String attributeName =  rs.getString(ATTRIBUTE);
					String defaultType =    rs.getString(DEFAULTTYPE);
					String defaultValue =   rs.getString(DEFAULTVALUE);
					String attributeType = rs.getString(ATTRIBUTETYPE);
					EBWLogger.logDebug("Prepopulator-FillTO","Attribute Name "+ attributeName);
					EBWLogger.logDebug("Prepopulator-FillTO","Defualt Type(Predefined or Sequence or Lookup)" + defaultType);
					EBWLogger.logDebug("Prepopulator-FillTO","Defualt Value if any "+ defaultValue);
					EBWLogger.logDebug("Prepopulator-FillTO","Attribute Type(java.lang.Double or java.lang.String or java.util.Date) "+ attributeType);
					if(defaultType.equalsIgnoreCase(PREDEFINED)){					
						Object value = invokeTOMethod(transferObj,GET+StringUtil.initCaps(attributeName),null,attributeType,con,className);
						if(value==null){						
							String methodName  = SET + StringUtil.initCaps(attributeName);
							EBWLogger.logDebug("Prepopulator-FillTO","MethodName Invoked to set the Predefined value "+ methodName);
							invokeTOMethod(transferObj,methodName, defaultValue,attributeType,con,className);
							
						}
					}else if(defaultType.equalsIgnoreCase(SEQUENCE)){
						
						Object value = invokeTOMethod(transferObj,GET+StringUtil.initCaps(attributeName),null,attributeType,con,className);
						if(value==null){							
							String methodName  = SET + StringUtil.initCaps(attributeName);							
							EBWLogger.logDebug("Prepopulator-FillTO","MethodName Invoked to set the Predefined value "+ methodName);							
							String sequence = getSequence(attributeName, className,con,SEQUENCEQUERY,UPDATESEQUENCE);							
							EBWLogger.logDebug("Prepopulator-FillTO","Generated Sequence "+ sequence);							
							invokeTOMethod(transferObj,methodName,sequence,attributeType,con,className);
							
						}
					}else if(defaultType.equalsIgnoreCase(SEQUENCENUM)){
						
						Object value = invokeTOMethod(transferObj,GET+StringUtil.initCaps(attributeName),null,attributeType,con,className);
						if(value==null){							
							where = new String[0];
							String sequence="";
							String methodName  = SET + StringUtil.initCaps(attributeName);							
							EBWLogger.logDebug("Prepopulator-FillTO","MethodName Invoked to set the Predefined value "+ methodName);
							String query = "select nextval for "+attributeName+"_SEQ from sysibm.sysdummy1";
							synchronized (sequence) {
							ResultSet rset = executeQuery(query, where,false, con);
							if(rset.next()){
								sequence = rset.getString(1);
							}
							
							//String sequence = getSequence(attributeName, className,con,SEQUENCEQUERY,UPDATESEQUENCE);							
							EBWLogger.logDebug("Prepopulator-FillTO","Generated Sequence "+ sequence);							
							invokeTOMethod(transferObj,methodName,sequence,attributeType,con,className);
							//Thread.sleep(2000);
							}
							
						}
					}else if(defaultType.equalsIgnoreCase(LOOKUP)){											
							Object value = invokeTOMethod(transferObj,GET+StringUtil.initCaps(attributeName),null,attributeType,con,className);													
							if(value!=null){																					
							String methodName  = GET + StringUtil.initCaps(attributeName);							
							EBWLogger.logDebug("Prepopulator-FillTO","MethodName Invoked to set the Lookup value "+ methodName);							
							Object description =  invokeTOMethod(transferObj,methodName,null,attributeType,con,className);							
							EBWLogger.logDebug("Prepopulator-FillTO","Description to be LookedUp " + description);							
							String desc = null;							
							if(description.getClass().getName().equalsIgnoreCase(STRING)){
								desc = (String) description;
							}else{
								String stringDesc = String.valueOf(description);
								desc = String.valueOf((int)Double.parseDouble(stringDesc));
							}
							where = new String[]{defaultValue,desc};
							ResultSet resultSet = executeQuery(LOOKUPQUERY,where,false,con);
							where = null;
							String lookupId = null;
							while(resultSet.next()){
								lookupId = resultSet.getString(LOOKUPID);
							}
							EBWLogger.logDebug("Prepopulator-FillTO","Lookup id for the Description is  "+ lookupId);
							
							String mName = SET + StringUtil.initCaps(attributeName);
							
							invokeTOMethod(transferObj,mName,lookupId,attributeType,con,className);
							resultSet.close();
							resultSet=null;						
							EBWLogger.logDebug("Prepopulator :", "Exiting from fillTransferObject ");
						}
					}
					
				}
			}
		
			if(rs!=null){
			    rs.close();
			    rs=null;			   
			   }
			EBWLogger.trace("Prepulator :","Resultset closed and nullified and Connection closed.. in outer loop");
		
	}		
	*/
	/**
	 * The transferObj is the Object on which the unfilled attributes
	 * needs to be filled by calling up their corresponding setter methods
	 * @param defaultArr ArrayList from DefaultMaster is passed as parameter
	 * @param transferObj the object which needs to be filled
	 * throws SQLException
	 */
	
	public static void fillTransferObject(final EBWTransferObject transferObj, ArrayList defaultArr,Connection con)throws Exception{
		
		EBWLogger.trace("Prepopulator :","Starting Method fillTransferObject with EBWTransferObject :"+ transferObj+",Attribute :"+defaultArr.toString()+")");	   
			String SELECTQUERY = "Select * from DefaultMaster " +"where Class=?";
			
			String LOOKUPQUERY = "Select * from LookupMaster " +"where DomainId=? and Description=?";					
			
			String SEQUENCEQUERY = "SELECT * FROM IDGenerator where TOClass=? AND Attribute=?";
			
			String UPDATESEQUENCE = "Update IDGenerator set currentvalue=? " + "where TOClass=? AND Attribute=?";
			
			String className = transferObj.getClass().getName();
		
		    EBWLogger.logDebug("Prepopulator Fill TO :" , "TO Class  to be Prepopulated " + className);		    		   
			String where[]  = new String[]{className};
			
			for(int i=0;i<where.length;i++){
				EBWLogger.logDebug("Prepopulator","Value of WHERE Clause is :"+where.length);	
			}						
			where = null;		
					
			if(defaultArr!=null){
				for(Iterator iter1=defaultArr.iterator();iter1.hasNext();)
				{		 					 					 					 
					 String attributeName = (String)  iter1.next();
					 String defaultValue=   (String)  iter1.next();
					 String   defaultType= 	(String)  iter1.next();
					 String attributeType =	(String)  iter1.next();										
					EBWLogger.logDebug("Prepopulator-FillTO","Attribute Name :"+ attributeName);
					EBWLogger.logDebug("Prepopulator-FillTO","Defualt Type(Predefined or Sequence or Lookup) :" + defaultType);
					EBWLogger.logDebug("Prepopulator-FillTO","Defualt Value if any :"+ defaultValue);
					EBWLogger.logDebug("Prepopulator-FillTO","Attribute Type(java.lang.Double or java.lang.String or java.util.Date) :"+ attributeType);
					if(defaultType.equalsIgnoreCase(PREDEFINED)){												
						Object value = invokeTOMethod(transferObj,GET+StringUtil.initCaps(attributeName),null,attributeType,con,className);
						if(value==null){							
							String methodName  = SET + StringUtil.initCaps(attributeName);
							EBWLogger.logDebug("Prepopulator-FillTO","MethodName Invoked to set the Predefined value :"+ methodName);
							invokeTOMethod(transferObj,methodName, defaultValue,attributeType,con,className);
						}
					}else if(defaultType.equalsIgnoreCase(SEQUENCE)){						
						
						Object value = invokeTOMethod(transferObj,GET+StringUtil.initCaps(attributeName),null,attributeType,con,className);
						if(value==null){							
							String methodName  = SET + StringUtil.initCaps(attributeName);							
							EBWLogger.logDebug("Prepopulator-FillTO","MethodName Invoked to set the Sequence value :"+ methodName);							
							String sequence = getSequence(attributeName, className,con,SEQUENCEQUERY,UPDATESEQUENCE);							
							EBWLogger.logDebug("Prepopulator-FillTO","Generated Sequence :"+ sequence);							
							invokeTOMethod(transferObj,methodName,sequence,attributeType,con,className);

						}  
					}else if(defaultType.equalsIgnoreCase(SEQUENCENUM)){
						
						Object value = invokeTOMethod(transferObj,GET+StringUtil.initCaps(attributeName),null,attributeType,con,className);
						if(value==null){
							String sequence="";
							where = new String[0];
							String methodName  = SET + StringUtil.initCaps(attributeName);							
							EBWLogger.logDebug("Prepopulator-FillTO","MethodName Invoked to set the Predefined value "+ methodName);
							String query = "select nextval for "+attributeName+"_SEQ from sysibm.sysdummy1";
							//Thread.sleep(2000);
							//synchronized (sequence) {							
							ResultSet rset = executeQuery(query, where,false, con);
							if(rset.next()){
								sequence = rset.getString(1);								
							}
							
							//String sequence = getSequence(attributeName, className,con,SEQUENCEQUERY,UPDATESEQUENCE);							
							EBWLogger.logDebug("Prepopulator-FillTO","Generated Sequence "+ sequence);							
							invokeTOMethod(transferObj,methodName,sequence,attributeType,con,className);
							//Thread.sleep(2000);
							//}
						}
					}else if(defaultType.equalsIgnoreCase(LOOKUP)){					
						Object value = invokeTOMethod(transferObj,GET+StringUtil.initCaps(attributeName),null,attributeType,con,className);														
						if(value!=null){																				
							String methodName  = GET + StringUtil.initCaps(attributeName);							
							EBWLogger.logDebug("Prepopulator-FillTO","MethodName Invoked to set the Lookup value :"+ methodName);							
							Object description =  invokeTOMethod(transferObj,methodName,null,attributeType,con,className);							
							EBWLogger.logDebug("Prepopulator-FillTO","Description to be LookedUp :" + description);							
							String desc = null;
							
							if(description.getClass().getName().equalsIgnoreCase(STRING)){
								desc = (String) description;
							}else{
								String stringDesc = String.valueOf(description);
								desc = String.valueOf((int)Double.parseDouble(stringDesc));
							}
							where = new String[]{defaultValue,desc};
							ResultSet resultSet = executeQuery(LOOKUPQUERY,where,false,con);
							where = null;
							String lookupId = null;
							while(resultSet.next()){
								lookupId = resultSet.getString(LOOKUPID);
							}
							EBWLogger.logDebug("Prepopulator-FillTO","Lookup id for the Description is  :"+ lookupId);														
							String mName = SET + StringUtil.initCaps(attributeName);							
							invokeTOMethod(transferObj,mName,lookupId,attributeType,con,className);
							resultSet.close();
							resultSet=null;							
						}
					}					
				}
			}					
			EBWLogger.trace("Prepopulator :" , "Exiting from fillTransferObject ");
	}
	
	/**
	 * The getSequence method is used for fetching the updated sequence from IDGenerator
	 * @param attributeName AttributeName is passed as parameter
	 * @param toClassName the transfer object  is passed as parameter
	 * throws SQLException
	 */
	
	
	private static String getSequence(String attributeName, String toClassName,Connection con,String SEQUENCEQUERY,String UPDATESEQUENCE)throws Exception{
		EBWLogger.trace("Prepopulator :","Starting Method  getSequence with AttributeName :"+attributeName+",TO Name :"+toClassName);
		String where[] = null;
		where=new String[]{toClassName,attributeName};
		ResultSet resultSet = executeQuery(SEQUENCEQUERY,where,false,con);
		where = null;
		String updatedValue = null;
		try{
		while(resultSet.next()){	
			EBWLogger.trace("Prepopulator :","inside resultset");
			String prefix = resultSet.getString(PREFIX);
			EBWLogger.trace("Prepopulator :","inside prefix"+prefix);
			String currentValue = resultSet.getString(CURRENTVALUE);
			EBWLogger.trace("Prepopulator :","inside currentValue "+currentValue);
			int configLength = resultSet.getInt(IDLENGTH);
			EBWLogger.trace("Prepopulator :","inside configLength " +configLength);
			if(prefix==null || configLength==0){
				updatedValue = String.valueOf(Integer.parseInt(currentValue) + 1) ;
				where = new String[]{updatedValue, toClassName.trim(),attributeName.trim()};
				executeQuery(UPDATESEQUENCE, where, true,con);
				EBWLogger.trace("Prepopulator :", "update query executed successfully");
				EBWLogger.trace("Prepopulator :", "details "+resultSet);
				
			}else{
				EBWLogger.trace("Prepopulator :","inside resultset else loop");
				String newValue = String.valueOf(Integer.parseInt(currentValue) + 1) ;
				where = new String[]{newValue, toClassName.trim(),attributeName.trim()};
				executeQuery(UPDATESEQUENCE, where, true,con);
				updatedValue = prefix + String.valueOf(newValue);
				int idLength = prefix.length() + newValue.length();
				StringBuffer sequence = null;
				for(int i=0;i<configLength-idLength;i++){
					if(sequence==null){
						sequence= new StringBuffer(prefix);
					}
					sequence.append(ZERO);
				}
				
				if(sequence!=null){
					sequence.append(newValue);
					updatedValue = sequence.toString();
				}
			}
			EBWLogger.trace("Prepopulator :", "Exiting from resultset with resultant output :"+resultSet);
		}	}catch(Exception e){
			e.printStackTrace();
		}
		resultSet.close();		
		EBWLogger.trace("Prepopulator :", "Exiting from getSequence with resultant output :"+updatedValue);

		return updatedValue;
	}
	
	
	/**
	 * Returns a ResultSet object
	 * The query argument must specify the query to be executed.
	 * The where argument must specify the list of values to be used with
	 * the Where clause of the query to be executed
	 * The flag argument must be false to execute a Select query and must be
	 * tru to execute a update query
	 * <p>
	 * This method returns the ResultSet object with the rows if the Select
	 * Query is executed and return null referred ResultSet object when  
	 * Update Query is executed
	 * 
	 * @param query query to be exectued
	 * @param where values to be used in the where clause of the query
	 * @param flag  to identify the query type
	 * @return resultset object
	 */
	
	private static   ResultSet executeQuery(String query,String where[],boolean flag,Connection con)throws Exception{
		
		EBWLogger.trace("Prepopulator :","Starting Method executeQuery   "+flag+query);	  
		ResultSet rs = null;
	    try {
			int wherelen = where.length;
			for (int i = 0; i < wherelen; i++)
				EBWLogger.logDebug("Prepopulator", "where[" + i + "] :" + where[i]);			

			PreparedStatement pStmt = con.prepareStatement(query);
			int j = 0;
			for (int i = 0; i < where.length; i++) {
				if (where[i] != null) {
					j = i;
					pStmt.setString(j + 1, where[i].trim());
				}
			}
			if (flag) {
				EBWLogger.trace("Prepopulator :","1");
				pStmt.execute();
			} else {
				EBWLogger.trace("Prepopulator :","2");
				rs = pStmt.executeQuery();
			}
		}

		catch (SQLException sqE) {
			if (con != null) {
				//con.rollback();
			}
			sqE.printStackTrace();
		} catch (Exception e) {
			if (con != null) {
				//con.rollback();
			}
			e.printStackTrace();
		}
		
		EBWLogger.trace("Prepopulator :", "Exit from executeQuery with ResultSet :"+rs);

		return rs;
	}
	
	private static   ResultSet executeQuery(PreparedStatement pStmt,String where[],boolean flag,Connection con)throws Exception{
		
		EBWLogger.trace("Prepopulator :","Starting Method executeQuery  ");	  
		ResultSet rs = null;
	    try {
			int wherelen = where.length;
			for (int i = 0; i < wherelen; i++)
				EBWLogger.logDebug("Prepopulator", "where[" + i + "] :" + where[i]);			

			
			int j = 0;
			for (int i = 0; i < where.length; i++) {
				if (where[i] != null) {
					j = i;
					pStmt.setString(j + 1, where[i].trim());
				}
			}
			if (flag) {
				pStmt.execute();
			} else {
				rs = pStmt.executeQuery();
			}
		}

		catch (SQLException sqE) {
			if (con != null) {
				//con.rollback();
			}
			sqE.printStackTrace();
		} catch (Exception e) {
			if (con != null) {
				//con.rollback();
			}
			e.printStackTrace();
		}
		
		EBWLogger.trace("Prepopulator :", "Exit from executeQuery with ResultSet :"+rs);

		return rs;
	}
	
	/**
	 * Returns Object by invoking the method on a Object The transferObj object
	 * argument is the reference to TransferObject on which the method is to be
	 * invoked.The methodName argument is the name of the method to be invoked
	 * on the object.The param argument is the values of the parameters of the
	 * methods to be invoked
	 * <p>
	 * Method always retruns the Object which is the value retruned after the
	 * method is invoked on the object.
	 * 
	 * @param transferObj
	 *            on which the method is invoked
	 * @param methodName
	 *            name of the method to be invoked
	 * @param param
	 *            parameters if any on the method to be invoked
	 * @return Object value returned after the method is ivoked
	 */
	
	private static Object invokeTOMethod(EBWTransferObject transferObj, 
	        String methodName,String param,String paramType,Connection con,String className)
	   throws Exception{
		
	    EBWLogger.trace("Prepopulator","Entering method private static Object invokeTOMethod"+" transferObj :"+transferObj+", methodName :"+methodName+", param :"+param+", paramType :"+paramType);	   	    
	    Object value[] = null;
		Class[] paramTypes = null;
		Object obj = null;
		Object paramValue = param;
		if(param!=null){
		    if(paramType!=null){
				if(paramType.equalsIgnoreCase(VARCHAR)){
					paramTypes = new Class[]{String.class};
					paramValue = new String(param);
				}else if(paramType.equalsIgnoreCase(NUMERIC)){
					paramTypes = new Class[]{Double.class};
					paramValue = new Double(param);
				}else if (paramType.equalsIgnoreCase(DATE)){
					paramTypes = new Class[]{Date.class};
					paramValue = new Date(param);
					
				}else if(paramType.equalsIgnoreCase("String[]")){
				    paramTypes = new Class[]{String[].class};
				    paramValue = new String[]{param};				  					
				}
			}else{
				paramTypes = new Class[]{String.class};
				paramValue = new String(param);
			}
			value = new Object[]{paramValue};
		}
		Class cls = Class.forName(className);
		Method method = cls.getMethod(methodName,paramTypes);
		obj = method.invoke(transferObj,value);		
		EBWLogger.trace("Prepopulator :", "Exit from invokeTOMethod  with Value got by Invoking the TO Method "+obj);

		return obj;
	}
		
}
