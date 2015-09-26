// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 1/16/2007 2:26:26 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BulkImportAction.java

package com.tcs.ebw.serverside.bi.action;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.mvc.action.EbwAction;
import com.tcs.ebw.serverside.bi.businessdelegate.BulkImportDelegate;
import com.tcs.ebw.serverside.bi.formbean.BulkImportForm;
import com.tcs.ebw.serverside.bi.formbean.SentdataForm;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
//import com.tcs.ebw.CA.formbean.AcknowledgementForm;
import com.tcs.ebw.mvc.validator.EbwForm;

public class BulkImportAction extends EbwAction
{

    public BulkImportAction()
    {
    }

    public ActionForward performTask(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
    	EbwForm objEbwForm=new EbwForm(); 
        SentdataForm objSentdataFrm = new SentdataForm();
     //   AcknowledgementForm objAckForm=new AcknowledgementForm();
        String forwardTo = "input";
        try
       
        {
        	String state; 
            EBWLogger.trace(this, "Starting performTask()");
            BulkImportForm objBulkImportForm = (BulkImportForm)form;
   //FOR EJB CALL
            
             String serviceToCall=objBulkImportForm.getImpfiletypes();
             System.out.println("SERVICE NAME PASSED FROM ACTION IS:"+serviceToCall);
             if(serviceToCall.equals("CreateResponses"))
            	 objBulkImportForm.setState("BulkImport_EJB");
            	 
   //FOR EJB CALL ENDS
            
           String action = objBulkImportForm.getAction();
           state= objBulkImportForm.getState();
            EBWLogger.logDebug(this, "BulkImportForm Action : " + action);
            EBWLogger.logDebug(this, "BulkImportForm State  : " + state);
            if(!action.equals("INIT"))
                objBulkImportForm.setPaginationIndex("0");
            request.setAttribute("BulkImportForm", objBulkImportForm);
            com.tcs.ebw.serverside.jaas.principal.UserPrincipal objUserPrincipal = getUserPrincipal(request);
            action.equals("INIT");
            if(action.equals("btnupload"))
            {
                
                    byte abyte0[] = objBulkImportForm.impfile.getFileData();
                    javax.servlet.ServletContext servletcontext = getServlet().getServletContext();
                    String s3 = PropertyFileReader.getProperty("BulkImport_Server_Folder");
                    String s4 = objBulkImportForm.impfile.getFileName();
                    File file = new File(s3, s4);
                    FileWriter fw = new FileWriter(file, false);
                    fw.write(new String(abyte0));
                    fw.close();
                    EBWLogger.logDebug(this, "FileName before writing file :" + file);
                    EBWLogger.logDebug(this, "File written successfully");
                    objBulkImportForm.setFilename(objBulkImportForm.impfile.getFileName());
                    BulkImportDelegate objBulkImportDelegate = new BulkImportDelegate(objBulkImportForm, getUserObject(request), objUserPrincipal);
                    objEbwForm =objBulkImportDelegate.bulkImport_New_btnupload(serviceToCall);
                    
                    if(objBulkImportForm.getState().equals("BulkImport_New"))
                    {
                    	objSentdataFrm=(SentdataForm) objEbwForm;
                    	request.setAttribute("SentdataForm", objSentdataFrm);
	                    forwardTo = "Sentdata";
                    }
                    else if(objBulkImportForm.getState().equals("BulkImport_EJB"))
                    {
                    	/*objAckForm=(AcknowledgementForm)objEbwForm;
	                    request.setAttribute("AcknowledgementForm", objAckForm);*/
	                    forwardTo = "Acknowledgement";
                    }
            } else
            if(!action.equals("INIT"))
                throw new Exception("Action is not configured.");
            EBWLogger.trace(this, "Finished performTask()");
        }
        catch(SQLException sqle)
        {
            EBWLogger.logDebug(this, "SQLException in BulkImportAction");
            sqle.printStackTrace();
            objSentdataFrm.setAction("Sentdata_Errors");
           // if(sqle != null && sqle.getCause() != null)
                //objSentdataFrm.setDet(sqle.getCause().getMessage());
            	//objSentdataFrm.setDet("UNSUCCESSFUL");
           // else
                //objSentdataFrm.setDet("Error executing SQL Query using Database Service");
            	//objSentdataFrm.setDet("UNSUCCESSFUL");
            System.out.println("Inside SQL error");
            saveErrorMessage(request, sqle);
            request.setAttribute("SentdataForm", objSentdataFrm);
            forwardTo = "Sentdata";
        }
        catch(Throwable objThrow)
        {
            EBWLogger.logDebug(this, "Exception in BulkImportAction");
            objThrow.printStackTrace();
            objSentdataFrm.setAction("Sentdata_Errors");
            System.out.println("Inside objThrow");
          //  if(objThrow != null && objThrow.getCause() != null)
                //objSentdataFrm.setDet(objThrow.getCause().getMessage());
           // objSentdataFrm.setDet("UNSUCCESSFUL");
           // else
                //objSentdataFrm.setDet("NoSuchMethodException ");
            //	objSentdataFrm.setDet("UNSUCCESSFUL");
            saveErrorMessage(request, objThrow);
            request.setAttribute("SentdataForm", objSentdataFrm);
            forwardTo = "Sentdata";
        }
        return mapping.findForward(forwardTo);
    }
}