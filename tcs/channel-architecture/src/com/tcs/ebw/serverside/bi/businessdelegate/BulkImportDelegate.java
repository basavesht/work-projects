/*
 * Created on Wed Jul 12 15:41:43 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.serverside.bi.businessdelegate;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import org.apache.commons.beanutils.BeanUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.common.util.StringUtil;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.bi.transferobject.DsBulkimpFilestatusTO;
//import com.tcs.ebw.serverside.exception.InvalidDataException;
import com.tcs.ebw.serverside.bi.formbean.*;

import com.tcs.wdc.core.config.IDCConfig;
import com.tcs.wdc.core.exceptions.IDCApplnException;
import com.tcs.wdc.core.service.bi.DuplicateFileCheckService;
import com.tcs.wdc.core.service.bi.FileParserService;
import com.tcs.wdc.core.util.Binary;
import com.tcs.wdc.core.util.FileUtil;
import com.tcs.wdc.core.util.GenerateHash;
import com.tcs.wdc.core.validations.BusinessFatalErrorValidator;
import com.tcs.wdc.core.xml.DOM2String;
import com.tcs.wdc.core.xml.XMLBuilder;
import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.exception.EbwException;
//import com.tcs.ebw.trade.transferobject.EbRDCBuySellReqNONDBTO;
import com.tcs.ebw.transferobject.EBWTransferObject;
import java.util.HashMap;
import com.tcs.ebw.mvc.validator.EbwForm;
//import com.tcs.ebw.CA.formbean.AcknowledgementForm;
/**
 * BulkImportAction class is using this Business Delegate. 
 */

public class BulkImportDelegate extends EbwBusinessDelegate {
	EBWTransferObject bulkfileRecordsTO[];
	public EBWTransferObject bulkfileRecordTO=null;
	private String BusinessparamInputFileSpec="";
    private ArrayList businesserrors=null;
    private String xmlrequest="";
    private Map mapRequest=null;
    SentdataForm objSentdataForm=null; 
  //  AcknowledgementForm objAckForm=null;
	BulkImportForm objBulkImportForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;
	EbwForm objEbwForm=null;
	private Document requestOut =null;
	private Document businessDataXMLSpec=null;
	private int totalRec=0;
	private int currRec=0;
	private String returnStr=null;
	private String fileRefNo=null;
	private DsBulkimpFilestatusTO dbfTO;
	 	/**
	 * Constructor of BulkImportDelegate class. 
	 */ 
	public BulkImportDelegate (BulkImportForm objBulkImportForm, HashMap objUserSessionObject) {
		this.objBulkImportForm = objBulkImportForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objBulkImportForm.getRetainData(), "~");
		this.totalRec=0;
		this.currRec=0;
		this.returnStr=null;
		this.fileRefNo=null;
	}

	/**
	 * Constructor of BulkImportDelegate class. 
	 */ 
	public BulkImportDelegate (BulkImportForm objBulkImportForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
	    EBWLogger.trace(this,"Constructor of BulkImportDelegate");
		this.objBulkImportForm = objBulkImportForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objBulkImportForm.getRetainData(), "~");
		this.totalRec=0;
		this.currRec=0;
		this.returnStr=null;
		this.fileRefNo=null;
	}

	/**
	 * Method for BulkImport_New State and btnupload Event. 
	 */
	public EbwForm bulkImport_New_btnupload(String serviceName) throws SQLException, Exception {
		EBWLogger.trace(this, "Starting BulkImport_Newbtnupload()"); 
		EBWLogger.trace(this, "Service name       : insertFileInfo");
		EBWLogger.logDebug(this,"BulkImportForm Action in Delegate: " + objBulkImportForm.getAction());
		EBWLogger.logDebug(this,"BulkImportForm State  in Delegate: " + objBulkImportForm.getState());
		
        objEbwForm=new EbwForm();
		objSentdataForm = new SentdataForm();
	//	objAckForm= new AcknowledgementForm();
		
		try{
		BeanUtils.copyProperties(objSentdataForm, objBulkImportForm);

		Object objOutput = null;
		String moduleval=null;
		
//pre update status with status="SUCCESSFUL"
		objOutput = updateBulkFileStatus();
//end
		
if(validateImports(moduleval)){
        System.out.println("FILEREFNUMBER:"+fileRefNo);
		Object result = insertBulkFileRecords(serviceName);
		//for updating the status in case of a partial or a failure
		if(returnStr.equals("failure") || returnStr.equals("partial"))
			{
			dbfTO.setStatus(returnStr.toUpperCase());
			setBulkImportFailure();
			}
		
		/*if(returnStr.equals("failure"))
			{
	    System.out.println("INSERTION SUCCESSFUL");
			objSentdataForm.setState("Sentdata_New");
			objSentdataForm.setDet("SUCCESSFUL");		
			objSentdataForm.setCurrRec(currRec);
			
			
			}
	    else
	    	if(returnStr.equals("partial")){
	    	
	        /*System.out.println("SERVICE RETURNED FAILURE");
	        objSentdataForm.setDet("UNSUCCESSFUL - Error in Data Insertion");
	        setBulkImportFailure();
	        objSentdataForm.setCurrRec(currRec);
	        
	        }*/
	    	
			String strResult ="";
			if(result instanceof String[])
			{
			    for(int i=0;i<((String[])result).length;i++)
			      strResult =strResult+", "+((String[])result)[i];
			    System.out.println("RESULT RECVD: "+strResult);
			    //objSentdataForm.setDet(strResult);
			    
			}
			
	}
	else{
		    System.out.println("Error in validating import files");
		    objSentdataForm.setAction("Sentdata_Errors");
		    setBulkImportFailure();
		    objBulkImportForm.setState("BulkImport_New");
		    return objSentdataForm;
		}
		    
		EBWLogger.trace(this, "Return bean object : " + objSentdataForm);
		EBWLogger.trace(this, "Finished BulkImport_Newbtnupload()");
		System.out.println("State:"+objBulkImportForm.getState());
			if(objBulkImportForm.getState().equals("BulkImport_EJB"))
			//	return objAckForm;				return null;
			else
			{
				objBulkImportForm.setState("BulkImport_New");
				return objSentdataForm;
			}
		
		}
		catch(Exception idcexception){
		    EBWLogger.logError(this,"Error in Validation of BulkFile");
		    idcexception.printStackTrace();
	        objSentdataForm.setAction("Sentdata_Errors");
	        //setBulkImportFailure();
	        objBulkImportForm.setState("BulkImport_New");
			return objSentdataForm;
		}
	}

	
	private boolean validateImports(String moduleval) throws Exception{
	    boolean result =false;
	    try{

	    	Method toMethod=null; 
            String colValue=null;
            Class clstype=null;
            System.out.println("MODULEVAL:"+moduleval);
		    //BusinessparamInputFileSpec = PropertyFileReader.getProperty(objBulkImportForm.getImpfiletypes()+".inputspecfile");
	        IDCConfig config = IDCConfig.getInstance();
	        System.out.println("IMP FILE TYPES IS:"+objBulkImportForm.getImpfiletypes());
	        System.out.println("Getting property : "+objBulkImportForm.getImpfiletypes()+".inputspecfile"+"  from idc.properties");
	        BusinessparamInputFileSpec = config.get(objBulkImportForm.getImpfiletypes()+".inputspecfile");
	        String BusinessDataFileSpec = config.get(objBulkImportForm.getImpfiletypes()+".file");
	        if(BusinessparamInputFileSpec==null || BusinessDataFileSpec==null){
	            objSentdataForm.setDet("Input file specification or Data file Specification is not defined correctly. Please contact Application Aministrator");
		        objSentdataForm.setAction("Sentdata_Errors");
		        setBulkImportFailure();
	        }
	        String BusinessDataxml = FileUtil.readFileIncludeNewline(BusinessDataFileSpec);
	        businessDataXMLSpec = XMLBuilder.parse(BusinessDataxml);
		    
	        /** Importedfile Validation Starts here **/
	        String file = PropertyFileReader.getProperty("BulkImport_Server_Folder")+"/"+objBulkImportForm.getFilename();
	        System.out.println("About to read file from disk: "+file);
	        String fileContent=FileUtil.readFileIncludeNewline(file.toString());
	        System.out.println("filecontent length :"+fileContent.getBytes().length);
	        xmlrequest = "<parameters><filetype>"+objBulkImportForm.getImpfiletypes()+"</filetype><file>"+Binary.encode(fileContent.getBytes(),fileContent.length())+ "</file></parameters>";
	        System.out.println("Request is :"+xmlrequest);
	        
	        NodeList identifierList = ((Document)businessDataXMLSpec).getElementsByTagName("identifier");
            moduleval = identifierList.item(0).getAttributes().getNamedItem("root").getNodeValue();
            StringBuffer xmlOut = new StringBuffer();
            
            
            
            try{
	        FileParserService fps = new FileParserService();
	        requestOut = fps.doService(XMLBuilder.parse(xmlrequest),null);
	        System.out.println("requestOut:"+requestOut);
	        mapRequest = (Map) fps.getResultHash();
	       }
            catch(Exception e)
	        {
	        	System.out.println("FPSCause:"+e.getMessage());
	        	e.printStackTrace();
	        	objSentdataForm.setDet("UNSUCCESSFUL - "+e.getMessage());
	        	result=false;
	        	return result;
	        }
	        
            
            
	        EBWLogger.logDebug(this,"Map Request in Validation is "+mapRequest);
	        //xmlDomrequest = requestOut;
	        DOM2String.print(requestOut.getFirstChild(),xmlOut);
	        
	        //com.tcs.ebw.common.util.FileUtil.writeToFileAppend("d:\\Arun\\EquityBuyOrdersInput.xml",xmlOut.toString(),true);
	        EBWLogger.logDebug(this,"BulkImport Request xml is :"+xmlOut.toString());
	        
	        xmlOut.delete(0,xmlOut.toString().length());
	        System.out.println("---Starting Validation---");
	        try{
	        String validationxml = FileUtil.readFileIncludeNewline(BusinessparamInputFileSpec);
	        BusinessFatalErrorValidator bfev = new BusinessFatalErrorValidator();
	        bfev.setDoc(XMLBuilder.parse(validationxml));
	        bfev.checkBusinessFatalErrors(requestOut);
	        bfev.checkFatalErrors(requestOut,"F",null);
	        System.out.println("---End Of Validation---");
	        }catch(Exception e)
	        {
	        	System.out.println("VAlCause:"+e.getMessage());
	        	System.out.println("VAlCause:"+e.getLocalizedMessage());
	        	e.printStackTrace();
	        	objSentdataForm.setDet("UNSUCCESSFUL - "+e.getMessage());
	        	result=false;
	        	return result;
	       }
   //code for TO array generation starts-harsh (16/01/2007)

System.out.println("-----------------POPULATING TO ARRAY---------------");
		 
		 
	 
            NodeList moduleNodelist = requestOut.getElementsByTagName(moduleval);
            String stmtName = "";
            /*String stmtName[] = null;
            for(int k=0;k<stmtName.length;k++)
            	stmtName[k]= moduleNodelist.item(k).getFirstChild().getNodeName();*/
            int records = moduleNodelist.getLength();
            totalRec=records;
            System.out.println("Records is :"+records);
            bulkfileRecordsTO= new EBWTransferObject[records];
            Map inputMap = new LinkedHashMap();
            if(records==0)
                throw new EbwException(this,new NullPointerException("No_Records_to_Import"));
            for(int i=0;i<records;i++){
            	currRec=(i+1);
             bulkfileRecordTO = (EBWTransferObject)Class.forName("com.tcs.ebw."+moduleval).newInstance();
              
              if(moduleNodelist.item(i).getFirstChild()==null)
              throw new Exception("Root value "+moduleval+" in spec file does not match with root node declared in the DTD file.. Please correct and try again..");  
              stmtName= moduleNodelist.item(i).getFirstChild().getNodeName();
              System.out.println("STATEMENT NAME"+stmtName);
              
              Node inputRecord = moduleNodelist.item(i).getFirstChild();
              System.out.println("Input record is"+inputRecord.getNodeName());
              System.out.println("Input record is"+inputRecord.getNodeValue());
              inputMap = new LinkedHashMap();
              int cols = inputRecord.getChildNodes().getLength();
              System.out.println("cols:"+cols);
    		  System.out.println("Adding data for record "+i);
              for(int j=0;j<cols;j++){     
    			    //System.out.println("Node Name /Value:"+inputRecord.getChildNodes().item(j).getNodeName()+" / "+inputRecord.getChildNodes().item(j).getFirstChild().getNodeValue());
    			    String columnName = inputRecord.getChildNodes().item(j).getNodeName();
    			    colValue = inputRecord.getChildNodes().item(j).getFirstChild().getNodeValue();
    			    inputMap.put(columnName,colValue);
    			    
    			    clstype = getClassType(businessDataXMLSpec,columnName);
    			    Class paramTypes[] = {clstype};
    			    Object params[] = {colValue};
    			    
    			    if(clstype==Double.class)
    			        params[0]=new Double(colValue);
    			    if(clstype==java.util.Date.class)
    			    	params[0]=new java.util.Date(colValue);
    			    toMethod = bulkfileRecordTO.getClass().getMethod("set"+StringUtil.initCaps(columnName),paramTypes);
    			    EBWLogger.logDebug(this,"Invoking method "+toMethod.getName() +" of type "+clstype.getName()+ " with value :"+colValue);
    			    toMethod.invoke(bulkfileRecordTO,params);
    			    EBWLogger.logDebug(this,"TO Attribute "+columnName +" set in bulkfileTO as "+colValue );
    		//adding filereference number
    			    Class cls[] = {String.class};
    			    Object ob[]={fileRefNo};
    			    //System.out.println("fileRefNO:"+fileRefNo);
    			    bulkfileRecordTO.getClass().getMethod("setClrBfsfilereferenceno",cls).invoke(bulkfileRecordTO,ob);
    			   //System.out.println("FileRefNUMBER from TO:"+bulkfileRecordTO.getClass().getMethod("getClrBfsfilereferenceno",null).invoke(bulkfileRecordTO,null).toString());
    			   
    			    //adding filereference number
              }
              bulkfileRecordsTO[i]=bulkfileRecordTO;
              System.out.println("Class:"+bulkfileRecordTO.getClass().getName());
  		      System.out.println("Class:"+bulkfileRecordTO.getClass().getDeclaredMethod("getClrBfsfilereferenceno",null).getName());
  		   
              //added for file reference number
                  
                System.out.println(":"+bulkfileRecordsTO[i]);
              
                EBWLogger.logDebug(this,"Statement id created using root element is: "+"insert"+StringUtil.initCaps(stmtName));
                EBWLogger.logDebug(this,"BulkFileImport Successfully completed ");
                
            }
            
            //code for TO array generation ends-Harsh(16/01/2007)
         
            
	        //DuplicateFileCheckService dfc = new DuplicateFileCheckService();
	        
        /** Vaidation Ends here 
         * 
         */
            
            
   
	    
	    
	    
	    
	    
	    }
	    
	    catch(SQLException sqle){
	        EBWLogger.logError(this,"Error in Validation of BulkFile:SQL");
	        //objSentdataForm.setDet(sqle.getCause()+sqle.getMessage());
	        objSentdataForm.setDet("UNSUCCESSFUL - Invalid Data or Specification Files");
	        setBulkImportFailure();
	        objSentdataForm.setAction("Sentdata_Errors");
	        sqle.printStackTrace();
	       // throw sqle;
	    }catch(NullPointerException nullpointer){
	        EBWLogger.logError(this,"Error importing datafiles: Input specification file name or Data specification file name in idc.properties may be wrong..");
	        //objSentdataForm.setDet(nullpointer.getCause()+nullpointer.getMessage());
	        objSentdataForm.setAction("Sentdata_Errors");
	        objSentdataForm.setDet("UNSUCCESSFUL - Invalid Data or Specification Files");
	        setBulkImportFailure();
	        nullpointer.printStackTrace();
	        //throw nullpointer;
	    }
	    catch(Exception idcexception){
	    
	    	
	        EBWLogger.logError(this,"Error in Validation of BulkFile");
	        //objSentdataForm.setDet(idcexception.getCause()+idcexception.getMessage());
	        objSentdataForm.setDet("UNSUCCESSFUL - Invalid Data or Specification Files");
	        setBulkImportFailure();
	        objSentdataForm.setAction("Sentdata_Errors");
	      //  throw new EbwException(this,new InvalidDataException(this.objBulkImportForm.getImpfiletypes().toString()));
	//        idcexception.printStackTrace(); 
//	        throw idcexception;
	    }    
	    /*catch(Exception e){
	        EBWLogger.logError(this,"Error in Validation of BulkFile");
	        //objSentdataForm.setDet(e.getCause()+e.getMessage());
	        objSentdataForm.setAction("Sentdata_Errors");
	        e.printStackTrace();
	        throw e;
	    }*/
	    result=true;
	 
        return result;
        
	}
	
	//private Object updateBulkFileStatus(String moduleval) throws Exception{
	private Object updateBulkFileStatus() throws Exception{
	    Object outputObj=null;
	    try{
	    dbfTO= new DsBulkimpFilestatusTO();
		dbfTO.setBfsgroupid(objUserPrincipal.getUsrgrpid());
		dbfTO.setBfsfiletype(objBulkImportForm.getImpfiletypes());
		dbfTO.setCreatedby(objUserPrincipal.getUsruserid());
		dbfTO.setBfsorgfilename(objBulkImportForm.getFilename());
		dbfTO.setModifiedby(objUserPrincipal.getUsruserid());
		dbfTO.setVersionnum(new Double(1));
		//dbfTO.setBfsfiletype(objBulkImportForm.getExportType());
		dbfTO.setBfsdescription(objBulkImportForm.getDescription());
		dbfTO.setStatus("SUCCESS");
		
		
		System.out.println("Generated Hash is :"+GenerateHash.getInstance().hashCode());
		dbfTO.setBfsfilehash(String.valueOf(GenerateHash.getInstance().hashCode()));
		
		Class clsParamTypes[]={String.class, Object.class,Boolean.class};
		Object objParams[]={"insertFileInfo",dbfTO,new Boolean(false)};
		
		EBWLogger.trace(this, "Before insertFileInfo Service call Service Parameters : " + objParams); 
		EBWLogger.trace(this, "Before insertFileInfo Service Param Type : " + clsParamTypes); 
		
		EBWAppContext ebwappctx = new EBWAppContext();
		ebwappctx.setScreenAction(objBulkImportForm.getAction());
		ebwappctx.setScreenState(objBulkImportForm.getState());
		ebwappctx.setServiceId("insertFileInfo");
		ebwappctx.setUserPrincipal(objUserPrincipal);
		
		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("insertFileInfo",ebwappctx);
		outputObj = objService.execute(clsParamTypes, objParams);
		fileRefNo=dbfTO.getBfsfilereferenceno();
		EBWLogger.logDebug(this,"Reference No generated is :"+dbfTO.getBfsfilereferenceno());

		EBWLogger.logDebug(this,"Output Object is: "+outputObj);
		
		objSentdataForm.setfileRefNo("File Reference Number Generated:"+dbfTO.getBfsfilereferenceno());
		
	    }catch(SQLException e){
	        e.printStackTrace();
	        objSentdataForm.setAction("Sentdata_Errors");
	        //objSentdataForm.setDet(e.getCause()+e.getMessage());
	        objSentdataForm.setDet("UNSUCCESSFUL - Error in Connection");
	        setBulkImportFailure();
	        EBWLogger.logError(this,"Error in inserting Bulkfilestatus table");
	        //throw e;
	    }
		return outputObj;
	}
	
	private Object insertBulkFileRecords(String serviceName) throws SQLException, Exception{
	    EBWLogger.trace(this,"Staring method insertBulkFileRecords");
	    EBWLogger.trace(this,"Input xml request is :"+xmlrequest);
	    
	    Object result="";
	    int recordsAffected=0;
	    try{
	    
	    	EBWLogger.logDebug(this,"Action in insertBulkFileRecords method"+objBulkImportForm.getAction());	
			EBWLogger.logDebug(this,"State in insertBulkFileRecords method"+objBulkImportForm.getState());
			
			EBWAppContext ebwappctx = new EBWAppContext();
			ebwappctx.setScreenAction(objBulkImportForm.getAction());
			ebwappctx.setScreenState(objBulkImportForm.getState());
			ebwappctx.setServiceId(serviceName);
			ebwappctx.setUserPrincipal(objUserPrincipal);
		   EBWLogger.logDebug(this,"Executing service:"+serviceName);
//code added for EJB CALL
	
		   //RJ Specific code for adding "RJEJB_" to serviceName
		   serviceName="RJEJB_"+serviceName;
		   //end
		   
		   
		        System.out.println("-----CALLING EJB SERVICE----");
	    		Class clsParamTypes[]={String.class, String.class,Object[].class, Boolean.class};
			    Object objParams[]={"submitVotingForm","CREATE", bulkfileRecordsTO,new Boolean(true)};
			    IEBWService objService = EBWServiceFactory.create(serviceName,ebwappctx);
			    result=objService.execute(clsParamTypes, objParams);
			    objService.close();
			    System.out.println("CLASS OF RESULT:"+result.getClass().getName());
			    System.out.println("Result:"+result);
			   
			    
			    
			    //code for populating the Acknowledgemnet Form
			    
			    
			    ArrayList resultArray=(ArrayList)result;
				int res=-1;

				if(resultArray!=null && resultArray.size()>1)
					res=0;
				else 
					res=-1;

				if (res==-1){
					EBWLogger.logDebug(this,"EJB Error in Voting");
					/*objAckForm.setState("Acknowledgement_Error");*/
					returnStr="failure";
				} else {
					/*objAckForm.setAcktableTableData(resultArray);*/
					System.out.println("Setting Votingdata successfully" + resultArray);
					returnStr="partial";
				} 
				

				//code For Acknowledgement Form Ends
				



		   
//code for ejb call ENDS		    
		    /*
		     * 
		     *EBWActivityLogMasterTO activity_log_params = new EBWActivityLogMasterTO();
		    
		    if(resultOut!=null && resultOut.equalsIgnoreCase("Success"))
		        activity_log_params.setResult(resultOut);
		    else
		        activity_log_params.setResult("Failure");
		        
		    Object paramObjs[] = {activity_log_params};
		    Class paramClassType[] ={Object.class};
		    IEBWService objBusinessService = EBWServiceFactory.create("updateActivityLogMaster");
		    objBusinessService.execute(paramClassType,paramObjs);
		    EBWLogger.logDebug("EBWServiceFactory","Activity result Updated Successfully for Activity:"+activity_log_params.getActivityId());
		    
			
		    */
		    
		    EBWLogger.logDebug(this,"Service:"+serviceName+" executed Successfully");
		    EBWLogger.logDebug(this,"No of rows inserted are "+recordsAffected);
		    
	    }catch(SQLException sqle){
	        sqle.printStackTrace();
	        EBWLogger.logError(this,"SQLException -- inserting BulkFile records in Service insertBulkFileRecords");
	        objSentdataForm.setAction("Sentdata_Errors");
	        //objSentdataForm.setDet(sqle.getCause()+sqle.getMessage());
	        objSentdataForm.setDet("UNSUCCESSFUL - Error While Saving Data");
	        setBulkImportFailure();
	        //throw sqle;
	    }
	    catch(Exception e){
	        e.printStackTrace();
	        
	        EBWLogger.logError(this,"Error inserting BulkFile records in Service insertBulkFileRecords");
	        EBWLogger.logError(this,"Cause of error :"+e.getCause().getMessage());
	        objSentdataForm.setAction("Sentdata_Errors");
//objSentdataForm.setDet(e.getCause()+e.getMessage());
	        objSentdataForm.setDet("UNSUCCESSFUL - Error While Saving Data");
	        setBulkImportFailure();
	        //throw e;
	    }
	    return result;
	}
	
	//added for TO generation
	private Class getClassType(Object businessDataXMLSpec,String columnName){
        Class clsType=null;
        NodeList fieldList = ((Document)businessDataXMLSpec).getElementsByTagName("field");
        String strClsType=null;
        EBWLogger.logDebug(this,"Field length is :"+fieldList.getLength());
        for(int i=0;i<fieldList.getLength();i++){
            EBWLogger.logDebug(this,"Checking field :"+fieldList.item(i).getFirstChild().getNodeName()+" Value is :"+fieldList.item(i).getFirstChild().getNodeValue());
            if(fieldList.item(i).getFirstChild().getNodeValue().equalsIgnoreCase(columnName))
                strClsType = fieldList.item(i).getAttributes().getNamedItem("type").getNodeValue();    
        }
        
        if(strClsType!=null){
            if(strClsType.equalsIgnoreCase("string"))
                clsType=String.class;
            else if(strClsType.equalsIgnoreCase("int")|| strClsType.equalsIgnoreCase("numeric")||
                    strClsType.equalsIgnoreCase("double"))
                clsType=Double.class;
            else if (strClsType.equalsIgnoreCase("date"))
            	clsType = java.util.Date.class;
        }
        return clsType;
    }
	//added for TO generation END
	
	//added for setting status of BulkImport
	public void setBulkImportFailure()throws SQLException, Exception
	{
		try{
		System.out.println("-----CALLING updtBIStatus SERVICE----");
	
   	Class clsParamTypes[]={Object.class,Boolean.class};
	Object objParams[]={dbfTO,new Boolean(false)};
	Object result=null;
	  IEBWService objService = EBWServiceFactory.create("updtBIStatus");
	  result=objService.execute(clsParamTypes, objParams);
	  //System.out.println("RESULT OF UUPDATEBI:"+result);
	   
	   }catch(Exception e)
		{ 
	   	EBWLogger.logError(this,"Error setting the status of BulkImport");
		//throw e;
		}
	}
}
//end 

