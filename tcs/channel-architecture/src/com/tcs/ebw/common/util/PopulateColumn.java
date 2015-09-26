/*
 * Created on Tue Dec 12 17:19:09 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.common.util;
import com.tcs.ebw.common.util.StringUtil;
import java.lang.reflect.*;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.taglib.DataInterface;
import com.tcs.ebw.taglib.TagLibConstants;
import com.tcs.ebw.common.util.EBWLogger;
//import com.tcs.ebw.CA.formbean.*;
//import com.tcs.ebw.CA.transferobject.*;

public class PopulateColumn {

/**
 *	Single row selection scenario
**/
public static void populateFormData(EbwForm objSourceForm,String[] strRowData,String[] fieldsArray,int[] indexArray)  throws Exception {

        System.out.println("Inside populateFormData ");
		try{
        int fieldsArraylength=fieldsArray.length;
        
		
        Class classForm= objSourceForm.getClass();
		Class paramType[]=new Class[1];
		paramType[0] =Class.forName("java.lang.String");
		Method method;
		String methodName=null;
		
		/** Code to get the
		 * Resource bundle for getting values from EbwTableResourceBundle
		 */
		ResourceBundle ebwTableRb = null;
		try {
			ebwTableRb = ResourceBundle.getBundle(TagLibConstants.RESOURCE_BUNDLE_FILE_EBWTABLE);
		} catch (Exception exc) {
			System.out.println ("Resource Bundle Not found...");
		}
		
		String decFormat=null;
		String inFormat=null;
		String seperator=null;
		String cDecimal=null;
		String datatype="BigDecimal";
		int dec=0;
		int informat=0;
		
		try {
		 decFormat=ebwTableRb.getString("decFormat");
		 inFormat=ebwTableRb.getString("inFormat");
		 seperator=ebwTableRb.getString("seperator");
		 cDecimal=ebwTableRb.getString("cDecimal");
		 dec=Integer.parseInt(decFormat);
		 informat=Integer.parseInt(inFormat);
		}catch(NumberFormatException e){
			EBWLogger.logDebug("PopulateColumn","NumberFormatException :" + e);
		}catch(Exception e){
			EBWLogger.logDebug("PopulateColumn","ResourceBudle key not found :" + e);
		}
								
		char sep=seperator.charAt(0);
		char cDec=cDecimal.charAt(0);
		
		/** End **/
	   //try {
		
		
		

       for(int i=0;i<fieldsArraylength;i++) {
		   String attribute=fieldsArray[i];
		   String strData=strRowData[indexArray[i]];
		  // System.out.println("strData Value is :" + strData);
            if (strData.equals("null")) {
				strData=" ";
			}else if (attribute.toLowerCase().indexOf("date")!=-1) {
				strData=ConvertionUtil.convertToAppDateStr(strData);
			} else if (attribute.toLowerCase().indexOf("qty")!=-1) {
				strData=ConvertionUtil.ConvertToUserNumberFormat(strData,dec,sep,cDec,informat,datatype);
			}
           	
			Object params[]={strData};
			methodName = "set" + StringUtil.initCaps(fieldsArray[i]);
			method = classForm.getMethod(methodName, paramType);
			method.invoke(objSourceForm, params);
			
        }
		} catch(Exception e){
			EBWLogger.logDebug("PopulateColumn","NoSuchMethodException:" + e);
			throw e;
			
		}
		
	
  } 

public static void setPercentage(EbwForm objSourceForm,Object to,String[] toFieldsArray,String[] formFieldsArray) throws Exception {
	
	Class classForm= objSourceForm.getClass();
	Class classTO= to.getClass();
	
	Class paramType[]=new Class[1];
	paramType[0] =Class.forName("java.lang.String");
	Method method;
	String methodName=null;
	Object TOObjMethod=null;
	int fieldsArraylength=toFieldsArray.length;
	
	for(int i=0;i<fieldsArraylength;i++) {		  
		 TOObjMethod = classForm.getMethod("get" + StringUtil.initCaps(formFieldsArray[i]), null).invoke(objSourceForm, null);
		// System.out.println("TOObjMethod 1--" + TOObjMethod);
		 if(TOObjMethod!=null)			 
			 TOObjMethod=TOObjMethod.toString().toUpperCase()+"%";
		 else 
			 TOObjMethod=""+"%";
		 //TOObjMethod=TOObjMethod.toString().toUpperCase()+"%";
		 
		 //System.out.println("TOObjMethod 2--" + TOObjMethod);
     	 Object params[]={TOObjMethod}; 
     	 //System.out.println("params:" + params.length );
     	 methodName = "set" + StringUtil.initCaps(toFieldsArray[i]);
       	 method = classTO.getMethod(methodName, paramType);
       	 //System.out.println("method:" + method);
       	 method.invoke(to,params);	
       	
     }	
}

	/*public static void main(String args[]){
        System.out.println("Inside Main ");
        PopulateColumn pc=new PopulateColumn();
        CorporateActionViewForm objCAView=new CorporateActionViewForm();
        CAViewNDBTO objCAViewNDBTO=new CAViewNDBTO();
        objCAView.setClientaccnum("34535");
        objCAView.setSecurityid("");
        String[] toFieldsArray={"ISR_SFA_FND_ACCT_ID","DPT_SEC_ID"};
        String[] formFieldsArray={"clientaccnum","securityid"};
        try{
        pc.setPercentage(objCAView, objCAViewNDBTO, toFieldsArray, formFieldsArray);
        } catch (Exception e){
        	System.out.println("e--" + e);
        }
        
              
        try{
            Double val=Double.parseDouble("-140835");
        	String st=ConvertionUtil.ConvertToUserNumberFormat("-140835",4,',','.',4,"Amount");
        	System.out.println("st--" + st);
        } catch (Exception e){
        	System.out.println("e--" + e);	
        }		
		
	}*/
}