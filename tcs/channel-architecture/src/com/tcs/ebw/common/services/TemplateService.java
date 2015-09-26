/*
 * Created on Jul 19, 2006
 *
 */
package com.tcs.ebw.common.services;

import java.util.Vector;
import java.util.ArrayList;
import java.lang.reflect.Method;

import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.StringUtil;
import com.tcs.ebw.common.transferobject.TemplateTO;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.common.formbean.TemplateListForm;
/**
 * @author TCS
 */
public class TemplateService  {
	private String templateName;
	private String publicFlag;

	public void saveAsTemplate(UserPrincipal objUserPrincipal, 
	        EbwForm objEbwForm, String templateInfo) throws Exception {
	    
		EBWLogger.logDebug(this,"Calling the SaveTemplate  :" + templateInfo);
		String screenType = "R";
		
		Vector templateNameList = null;
		String userID = (String) objUserPrincipal.getUsruserid();
		String userGrpID = (String) objUserPrincipal.getUsrgrpid();
		String qlTask =objEbwForm.getScreenName();		
		String tptScreenstate = objEbwForm.getState(); // for GIC search template
		
		TemplateTO objTemplate = new TemplateTO();
		objTemplate.setTptUserID((String) objUserPrincipal.getUsruserid());
		objTemplate.setTptGropuID((String) objUserPrincipal.getUsrgrpid());

		String str1[];
		String str2[];
		str1 = templateInfo.split("#");
		System.out.println("Spilted Head based on #" + str1);
		for (int i = 0; i < str1.length; i++) {
			str2 = str1[i].split("=");
			EBWLogger.logDebug(this,"Spilted Head based on " + str2);
			if (str2[0].equals("templateName")) {
				this.templateName = str2[1];
				EBWLogger.logDebug(this,"Template Name is" + this.templateName);
			}
			if (str2[0].equals("publicFlag")) {
				this.publicFlag = str2[1];
				EBWLogger.logDebug(this,"PublicFlag is Name is" + this.publicFlag);
			}
		}
		
		objTemplate.setTptTemplateName(templateName);
		//Checks the Tempname is aleready present or not
		EBWLogger.logDebug(this,"Search Template ");
		Object objParams[] = { objTemplate, new Boolean(true)};
		Class clsParamTypes[] = { Object.class,  Boolean.class};
		try {
			//Service Call.....
			IEBWService objService = EBWServiceFactory.create("searchTemplateName");
			templateNameList = (Vector) objService.execute(clsParamTypes,objParams);
			EBWLogger.logDebug(this,"templateNameList:" + templateNameList.toString());
			if (!templateNameList.isEmpty()) {
				//throw new Exception(this,new Exception("Template Name is already exist"));
				throw new Exception("errors.TemplateError");
			}
		} catch (Exception e) {
			EBWLogger.logDebug(this,"TemplateService Template List ");
			e.printStackTrace();
			//throw new EbwException(this,new Exception("System Problem!!Template is not stored."));
			throw new Exception("errors.TemplateError");
		}
		
		objTemplate.setTptPublicFlag(publicFlag);
		objTemplate.setTptQlTask(qlTask);
		objTemplate.setTptScreenType(screenType);
	//	objTemplate.setTptScreenstate(tptScreenstate); // for GIC search template
		//Newly added code//
		objTemplate.setTptTemplate(objEbwForm.toString());
		//objTemplate.setTptTemplate(objEbwForm.toString().replaceAll("null",""));
		//End Newly added code
		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams);
		EBWLogger.trace(this, "After Prepopulate Service Param Type :" + clsParamTypes);
		EBWLogger.logDebug(this,"TemplateTO values:" + objTemplate.toString());
		try {
			//Service Call.....
			EBWLogger.logDebug(this,"Insert servive Call is executed");
			IEBWService objService = EBWServiceFactory.create("insertTemplate");
			objService.execute(clsParamTypes, objParams);
			EBWLogger.logDebug(this,"TemplateService:Template is Saved");
		} catch (Exception e) {
			e.printStackTrace();
			throw new EbwException(this,new Exception("System Problem!!Template is not stored."));
			//throw new Exception("Template is not stored.");
		}
	}

	// Delete Option in Search Template
	
	        
	public void deleteTemplate(UserPrincipal objUserPrincipal,EbwForm objEbwForm,String templateName)throws Exception{
		  System.out.println("**33333333333333333333333  deleteTemplate in Tempalte Service  11111111111111");
		  	Class cls = null;
		  	//String templateName=((TemplateListForm)objEbwForm).getSelectedTemplateName();
		  	System.out.println("*templateName**********"+templateName);
			String userID = (String) objUserPrincipal.getUsruserid();
			String userGrpID = (String) objUserPrincipal.getUsrgrpid();
			String qlTask =objEbwForm.getScreenName();		
			String tptScreenstate = objEbwForm.getState();
			String screenType = "R";
			
			
	    TemplateTO objTemplate = new TemplateTO();
		objTemplate.setTptUserID((String) objUserPrincipal.getUsruserid());
		objTemplate.setTptGropuID((String) objUserPrincipal.getUsrgrpid());
		
	    objTemplate.setTptTemplateName(templateName);
		objTemplate.setTptPublicFlag(publicFlag);
		objTemplate.setTptQlTask(qlTask);
		objTemplate.setTptScreenType(screenType);
	//	objTemplate.setTptScreenstate(tptScreenstate); // for GIC search template
		objTemplate.setTptTemplate(objEbwForm.toString());
	   
	    EBWLogger.logDebug(this,"Template Service template name is :" + objTemplate.getTptTemplateName());
	    Object objParams[] = {
	        objTemplate, new Boolean(true)
	    };
	    Class clsParamTypes[] = {
	        java.lang.Object.class, java.lang.Boolean.class
	    };
	   // ArrayList resultArray = null;
	    String result;
	    try
	    {
		    IEBWService objService = EBWServiceFactory.create("deleteTemplate");
		    System.out.println("BEFORE......deleteTemplate..");
		    //resultArray = (ArrayList)objService.execute(clsParamTypes, objParams);
		    objService.execute(clsParamTypes, objParams);
		    System.out.println("AFTER......deleteTemplate..");
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    	throw new EbwException(this,new Exception("System Problem!!Template is not stored."));
	    }
		// return objEbwForm;
	}
	
	
	public EbwForm openTemplate(String templateName, EbwForm edwFormObj)throws Exception{
	    Class cls = null;
	    String formBeanString = "hai";
	    String idenValueString[] = (String[])null;
	    String indenStr = null;
	    String valueStr = null;
	    String tempStrArray1[] = (String[])null;
	    String tempStrArray2[] = (String[])null;
	    String tempStr[] = (String[])null;
	    String methodName = null;
	    ArrayList arrList1 = null;
	    ArrayList dIDataArrList = null;
	    String[]  dIDataStringArr=(String[])null;
           
    TemplateTO objTemplate = new TemplateTO();
    objTemplate.setTptTemplateName(templateName);
    EBWLogger.logDebug(this,"Template Service template name is :" + objTemplate.getTptTemplateName());
    Object objParams[] = {
        objTemplate, new Boolean(true)
    };
    Class clsParamTypes[] = {
        java.lang.Object.class, java.lang.Boolean.class
    };
    ArrayList resultArray = null;
    try
    {
	    IEBWService objService = EBWServiceFactory.create("getTemplate");
	    resultArray = (ArrayList)objService.execute(clsParamTypes, objParams);
    }
    catch(Exception e)
    {
    	throw new EbwException(this,new Exception("System Problem!!Template is not stored."));
    }
    EBWLogger.logDebug(this,"resultArray" + resultArray.toString());
	if(resultArray!=null && resultArray.size()>1){
		ArrayList rowArray = (ArrayList)resultArray.get(1);
		EBWLogger.logDebug(this,"rowArray" + rowArray.toString());
		formBeanString = (String)rowArray.get(0);
		//formBeanString = ((String)rowArray.get(0)).replaceAll("null","");
		EBWLogger.logDebug(this,"Template Service array value is " + resultArray.toString());
		EBWLogger.logDebug(this,"Template Service formBeanString value is " + formBeanString);
		cls = edwFormObj.getClass();
		idenValueString = formBeanString.split("\r\n");
		Class paramType[] = new Class[1];
		Object params[] = new Object[1];
		try
		{
		for(int i = 0; i < idenValueString.length; i++)
		{
			tempStr = idenValueString[i].split("=");
			if(tempStr.length > 1)
			{
				EBWLogger.logDebug(this,"tempStr :" + tempStr.toString());
				for(int m = 0; m < tempStr.length; m++)
				{
					if(m == 0)
					{
						indenStr = tempStr[m].trim();
						EBWLogger.logDebug(this,"indenStr :" + indenStr.toString());
					}
					if(m == 1)
					{ 
						valueStr = tempStr[m].trim();
						EBWLogger.logDebug(this,"valueStr :" + valueStr.toString());
					}
				}

				if(valueStr != null && valueStr.indexOf("~~") > -1)
				{	
					System.out.println("tempStrArray1--"+tempStrArray1);
					tempStrArray1 = valueStr.split("~~");
					dIDataArrList = new ArrayList();
					for(int j = 0; j < tempStrArray1.length; j++)
					{
						EBWLogger.logDebug(this,"tempStrArray1" + tempStrArray1[j]);
						tempStrArray2 = tempStrArray1[j].split("##");
						arrList1 = new ArrayList();
						for(int k = 0; k < tempStrArray2.length; k++)
							arrList1.add(tempStrArray2[k]);

						dIDataArrList.add(arrList1);
						EBWLogger.logDebug(this,"ArrayList" + dIDataArrList.toString());
					}

					paramType[0] = Class.forName("java.util.ArrayList");
					params[0] = dIDataArrList;
				} else if(valueStr != null && valueStr.indexOf("#") > -1){
					
					valueStr=valueStr.substring(0,valueStr.lastIndexOf("#"));
					
					tempStrArray1 = valueStr.split("#");
					
					dIDataStringArr=new String[tempStrArray1.length];
					 for(int j = 0; j < tempStrArray1.length; j++)
						{
							EBWLogger.logDebug(this,"tempStrArray1" + tempStrArray1[j]);
							 dIDataStringArr[j]=tempStrArray1[j];
						 }
					 
					 EBWLogger.logDebug(this,"String Array" + dIDataStringArr.toString());
					 
					 System.out.println("String Array Class " + dIDataStringArr.getClass());
					//paramType[0] = Class.forName("java.lang.String[]");
					
					paramType[0] = Class.forName(dIDataStringArr.getClass().getName());
					params[0] = dIDataStringArr;
				} else
				{
					paramType[0] = Class.forName("java.lang.String");
					params[0] = valueStr;
				}
				methodName = "set" + StringUtil.initCaps(indenStr);
				EBWLogger.logDebug(this,"Method Name " + methodName);
				Method method = cls.getMethod(methodName, paramType);
				method.invoke(edwFormObj, params);
			} else
			{
				methodName = "set" + StringUtil.initCaps(tempStr[0].trim());
				if(methodName.indexOf("TableData") > -1)
					paramType[0] = Class.forName("java.util.ArrayList");
				else
					paramType[0] = Class.forName("java.lang.String");
				params[0] = null;
				EBWLogger.logDebug(this,"Method Name " + methodName);
				Method method = cls.getMethod(methodName, paramType);
				method.invoke(edwFormObj, params);
			}
			EBWLogger.logDebug(this,"Method is invoked for null parameter");

		}
		}
		catch(Exception e)
		{
			EBWLogger.logDebug(this, "this is a old template");
		}
	}else{
		edwFormObj.setAction("search");
	}
    return edwFormObj;
}

	
	/*public EbwForm openTemplate(String templateName, EbwForm edwFormObj) throws Exception{
		Class cls = null;
		String formBeanString;
		String idenValueString[]=null;
		String indenStr=null;
		String valueStr=null;
		String tempStr[]=null;
		String methodName=null;
		
		TemplateTO objTemplate = new TemplateTO();
		objTemplate.setTptTemplateName(templateName);
		Object objParams[] = {objTemplate, new Boolean(true)};
		Class clsParamTypes[] = { Object.class, Boolean.class };
		IEBWService objService = EBWServiceFactory.create("getTemplate");
		ArrayList objList =(ArrayList) objService.execute(clsParamTypes, objParams);
		if (objList.size() > 1) {
		    objList.remove(0);
		} 
		if (objList.size() == 0 || objList.get(0) == null) {
		    throw new Exception ("Template is not retrived.");
		} else {
		    formBeanString = (String) ((ArrayList)objList.get(0)).get(0);
		}
		System.out.println ("formBeanString : " + formBeanString);
		//formBeanString = getTestString();
		cls=edwFormObj.getClass();
		idenValueString = formBeanString.split("\n");
		System.out.println ("Len :" + idenValueString.length);
		Class paramType[]=new Class[1];
		Object params[] = new Object[1];
		try {
			for (int i = 0; i <idenValueString.length; i++) {
				tempStr=idenValueString[i].split("=");
				System.out.println ("Temp Str : " + tempStr + " : " + i + ":" + idenValueString.length);
				indenStr=tempStr[0].trim();
				valueStr=tempStr[1].trim();
				methodName = "set" + StringUtil.initCaps(indenStr);
				System.out.println("Method Name " + methodName + ":" + valueStr);
				paramType[0] =Class.forName("java.lang.String");
				params[0]=valueStr;
				Method method = cls.getMethod(methodName, paramType);
				method.invoke(edwFormObj, params);
				System.out.println("Method is invoked" );
			}
		} catch (Exception exc) {
		    exc.printStackTrace();
		}
		return edwFormObj;
	}*/
	
	/*public String getStringB(){
		StringBuffer strB = new StringBuffer();
		strB.append("action=getxcasdfasdfvgfAction()\r\n");
		strB.append("state=getState()\r\n");
		strB.append("exportType=getExportType()\r\n");
		strB.append("screenName=getScreenName()\r\n");
		strB.append("retainData=getRetainData()\r\n");
		strB.append("cancelFlag=getCancelFlag()\r\n");
		strB.append("paginationIndex=getPaginationIndex()\r\n");
		strB.append("txnPwdValid=getTxnPwdValid()");
		strB.append("chartDetail=getChartDetail()\r\n");
		strB.append("paymentdate=paymentdate\r\n");
		strB.append("intref1=intref1\r\n");
		strB.append("intref=intref\r\n");
		strB.append("interrefvalue=interrefvalue\r\n");
		strB.append("debitacc=debitacc\r\n");
		strB.append("consolidatepaymentvalue=consolidatepaymentvalue\r\n");
		strB.append("urgentpaymentvalue=urgentpaymentvalue\r\n");
		strB.append("recurringpaymentvalue=recurringpaymentvalue\r\n");
		strB.append("consolidatecheck=consolidatecheck\r\n");
		strB.append("internalreference=internalreference\r\n");
		strB.append("paymentdatevalue=paymentdatevalue\r\n");
		strB.append("debtitaccvalue=debtitaccvalue\r\n");
		strB.append("daily=daily\r\n");
		strB.append("weekly=weekly\r\n");
		strB.append("monthly=monthly\r\n");
		strB.append("monthend=monthend\r\n");
		strB.append("beneficiarysave=beneficiarysave\r\n");
		strB.append("beneficiary1=beneficiary1\r\n");
		strB.append("beneficiary=beneficiary\r\n");
		strB.append("account=account\r\n");
		strB.append("bank=bank\r\n");
		strB.append("swiftcode=swiftcode\r\n");
	
		return strB.toString();
		 StringBuffer strB = new StringBuffer();
		    strB.append("eqtransdataTableData=col1#col2#col3#col4~1#2#3#4~55#66#77#88\r\n");
		    strB.append("secsymbl=Garware Shipping Corporation\r\n");
		    strB.append("lmtcurr=INR\r\n");
		    strB.append("bidvalue=24\r\n");
		    strB.append("ordertype = Market\r\n");
		    strB.append("currency=array=sdsd\r\n");
		    strB.append("trdpricetxt = 24\r\n");
		    strB.append("trigpricecurrency = INR\r\n");
		    strB.append("trigprice = 24\r\n");
		    strB.append("trdqnty = 34\r\n");
		    strB.append("discqnty = \r\n");
		    strB.append("markettype = Normal Cash\r\n");
		    strB.append("deliveryterms = Non-Delivery\r\n");
		    strB.append("orderterms = Today\r\n");
		    strB.append("ordervaldt = \r\n");
		    strB.append("fromPortfolio = Trade Portfolio\r\n");
		    strB.append("cashaccount = SB-1111-BR-07\r\n");
		    strB.append("securityAccount = IN805693421\r\n");
		    strB.append("assettype = Equity\r\n");
		    return strB.toString();
	}*/
	
	/*public static void main (String str[]) throws Exception {
	    TemplateService objService = new TemplateService();
	    com.tcs.ebw.trade.formbean.TradeEquityBuyForm objForm = new com.tcs.ebw.trade.formbean.TradeEquityBuyForm(); 
	    System.out.println ("1 : " + objForm.toString());
	    objForm = (com.tcs.ebw.trade.formbean.TradeEquityBuyForm) objService.openTemplate("tttttttttttttt123", objForm);
	    System.out.println ("2 : " + objForm.toString());
	    /*EbwForm obj = new EbwForm();
	    System.out.println ("1 : " + obj.toString());
	    obj = objService.openTemplate("", obj);
	    System.out.println ("2 : " + obj.toString());
	}
	
	private String getTestString() {
	    StringBuffer strB = new StringBuffer();
	    strB.append("action=Testing\r\n");
	    strB.append("exchng = BSE\r\n");
	    strB.append("secsymbl = Garware Shipping Corporation\r\n");
	    strB.append("lmtcurr = INR\r\n");
	    strB.append("bidvalue = 24\r\n");
	    strB.append("ordertype = Market\r\n");
	    strB.append("trdpricetxt = 24\r\n");
	    strB.append("trigpricecurrency = INR\r\n");
	    strB.append("trigprice = 24\r\n");
	    strB.append("trdqnty = 34\r\n");
	    strB.append("discqnty = \r\n");
	    strB.append("markettype = Normal Cash\r\n");
	    strB.append("deliveryterms = Non-Delivery\r\n");
	    strB.append("orderterms = Today\r\n");
	    strB.append("ordervaldt = \r\n");
	    strB.append("fromPortfolio = Trade Portfolio\r\n");
	    strB.append("cashaccount = SB-1111-BR-07\r\n");
	    strB.append("securityAccount = IN805693421\r\n");
	    strB.append("assettype = Equity\r\n");
	    return strB.toString();
	}*/
}

