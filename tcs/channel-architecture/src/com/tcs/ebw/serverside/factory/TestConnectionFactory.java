/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.serverside.factory;

import java.math.BigDecimal;
import java.util.HashMap;

import com.tcs.ebw.common.util.FileUtil;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.security.EBWSecurity;
//import com.tcs.ebw.taglib.EbwTableHelper;
//import com.tcs.ebw.trade.transferobject.BusinessPartnerTO;
//import com.tcs.ebw.trade.transferobject.EbRdcBuysellReqTO;
//import com.tcs.ebw.transferobject.WbfeInboxMessagesTO;
//import com.tcs.ebw.transferobject.WbfeOutboxMessagesTO;


public class TestConnectionFactory {

	/*
	 *	Testing this class in standalone mode
	 */
	public static void main(String args[]){
		
		try{
	    //IEBWService serviceType = EBWServiceFactory.create("ORDER_SellOrderService");
		  //IEBWService serviceType = EBWServiceFactory.create("SYSADM_DatabaseService");
		 // IEBWService serviceType = EBWServiceFactory.create("QUARTZQDN_getBPList");
		//-----latest    IEBWService serviceType = EBWServiceFactory.create("TransPasswordService");
	    //IEBWService serviceType = EBWServiceFactory.create("SYSADM_getfapValtable");
		//IEBWService serviceType = EBWServiceFactory.create("setDap");			
		//IEBWService serviceType = EBWServiceFactory.create("WSSellOrderService");
	    
	    //IEBWService serviceType = EBWServiceFactory.create("HttpSellOrderService");
	   //IEBWService serviceType = EBWServiceFactory.create("HelloWorld");
		//DatabaseService serviceType = (DatabaseService)new EBWServiceFactory().getInstance().create("getSellOrderInfo");
		/*LinkedHashMap params = new LinkedHashMap();
		params.put("SellOrderId","S0001");
		params.put("orderDt",c);
		params.put("exchangeCurrency","USD");
		params.put("exchangeRate","45");
		params.put("baseCurrency","INR");
		params.put("itemDescription","Item1");
		*/
		
	    /*
	     * 
	     *DsProgramsTO dsprogTO = new DsProgramsTO();
	    dsprogTO.setUpgprogapplarea("I");
	    dsprogTO.setUpgprogramid("RDCCHBH01S");
	    
		SellOrderTO sellOrderTO = new SellOrderTO();
		sellOrderTO.setSellOrderId("S1114");
		sellOrderTO.setSellOrderDt(new java.util.Date("01/01/2006"));
		sellOrderTO.setExchangeCurrency("USD");
		sellOrderTO.setExchangeRate(45.00);
		sellOrderTO.setBaseCurrency("INR");
		sellOrderTO.setItemDescription("Item2");
		
		
		SellOrderTO sellOrderTO2 = new SellOrderTO();
		sellOrderTO2.setSellOrderId("S2223");
		sellOrderTO2.setSellOrderDt(new Date("01-JAN-2006"));
		sellOrderTO2.setExchangeCurrency("USD");
		sellOrderTO2.setExchangeRate(45.00);
		sellOrderTO2.setBaseCurrency("INR");
		sellOrderTO2.setItemDescription("Item2");
		*/
		  
//		WbfeOutboxMessagesTO wbfeOutboxMessagesTO = new WbfeOutboxMessagesTO();
//		wbfeOutboxMessagesTO.setOutsendgrpid("ABB2");
//		wbfeOutboxMessagesTO.setOutsendusrid("EUROGM2");
//		wbfeOutboxMessagesTO.setOutrecpgrpid("G0002");
//		wbfeOutboxMessagesTO.setOutrecpusrid("asd2");
//		wbfeOutboxMessagesTO.setOutallrecpusrid("asdd2");
//		wbfeOutboxMessagesTO.setOutsubject("subject2");
//		//wbfeOutboxMessagesTO.setOutattachflag("Y");
//		wbfeOutboxMessagesTO.setOutmailrefno("asd01232");
//		wbfeOutboxMessagesTO.setOutnewmessage("Message2");
		
		
		//outsendgrpid,outsendusrid,outrecpgrpid,outrecpusrid,outallrecpusrid,
		//outsubject,outmailrefno,outnewmessage
		
//		WbfeInboxMessagesTO wbfeInboxMessagesTO = new WbfeInboxMessagesTO();
//		wbfeInboxMessagesTO.setInbsendgrpid("ABB2");
//		wbfeInboxMessagesTO.setInbsendusrid("EUROGM2");
//		wbfeInboxMessagesTO.setInbrecpgrpid("G0002");
//		wbfeInboxMessagesTO.setInbrecpusrid("USER2");
//		wbfeInboxMessagesTO.setInbsubject("subject2");
//		wbfeInboxMessagesTO.setInbattachflag("Y2");
//		wbfeInboxMessagesTO.setInbnewmessage("Message2");
		
		/*EmployeeTO employeeTO = new EmployeeTO();
		employeeTO.setEmpNo("152820");
		employeeTO.setDepartment("CC");
		employeeTO.setDesignation("Analyst");
		employeeTO.setDoj("01/01/2004");
		employeeTO.setSalary("3030");
		*/
		
		
		
		com.tcs.ebw.transferobject.DsDapTO dsDapTO = new com.tcs.ebw.transferobject.DsDapTO();
		dsDapTO.setUdpgrpid("GR0014");
		dsDapTO.setUdpdapid("DP0001");
		dsDapTO.setUdpdescription("DAp desc");
		
		com.tcs.ebw.transferobject.DsDapAcctCdTO dsDapAcctCdTO = new com.tcs.ebw.transferobject.DsDapAcctCdTO();
		dsDapAcctCdTO.setUdadapid("DP0001");
		dsDapAcctCdTO.setUdagrpid("GR0013");
		dsDapAcctCdTO.setUdafieldname("BO Country");
//		dsDapAcctCdTO.setBp_id(new Double(12.00));
		
		
		
		
		//StringBuffer orderId = new StringBuffer();
		//orderId.append("S0001");
		 
		// String stateids[] = {"ORDER_setSellOrder","ORDER_setSellOrder"};
		/* String stateids[] = {"insertMessageInfo","insertInboxInfo"};
		Class paramTypesObj[] = {String[].class ,com.tcs.ebw.transferobject.EBWTransferObject[].class,Boolean.class};
		com.tcs.ebw.transferobject.EBWTransferObject[] tos = {wbfeOutboxMessagesTO,wbfeInboxMessagesTO};
		Object params[] = {stateids,tos,new Boolean(false)};
		*/
		
//		BusinessPartnerTO bpTO = new BusinessPartnerTO();
//		bpTO.setBpId(new Double(12.00));

		//For quartzDB service testing
		//Class paramTypesObj[] = {com.tcs.ebw.transferobject.EBWTransferObject.class};
		Class paramTypesObj[] = {String.class,HashMap.class};
		
		//com.tcs.ebw.transferobject.EBWTransferObject[] tos = {sellOrderTO,sellOrderTO2};
		//Object params[] = {bpTO};
		HashMap paramsObj = new HashMap();
		paramsObj.put("usruserid","RDCSUPER1");
		paramsObj.put("usrtranspwd",new String(new EBWSecurity().computeHash("RDCTRANS".getBytes(),"SHA1")));
		Object params[] = {"validateTransPwd",paramsObj};
		
		//*** For WebServices Testing. ****
		//Class paramTypesObj[] = {com.tcs.ebw.transferobject.EBWTransferObject.class};
		
		//Object params[] = {"<branchrequest><parameters><loginid>watersona</loginid><passwd>E8|B1|6E|13|91|33|1A|4D|</passwd><qz_servicename>sh_SEET0562_Get</qz_servicename></parameters></branchrequest>"};
		
		StringBuffer comsString = new StringBuffer();
		
		//comsString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><branchrequest><parameters><loginid>watersona</loginid><passwd>E8|B1|6E|13|91|33|1A|4D|</passwd><qz_servicename>sh_SEET0562_Get</qz_servicename></parameters></branchrequest>");
		//Woking *****
		//comsString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><branchrequest><parameters><qz_servicename>sh_SEET0562_Get</qz_servicename></parameters></branchrequest>");
		
		//comsString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><header><userid>SYSGBATZ</userid><groupid>BRANCH_GROUP</groupid><entity>GBATZ</entity><passwordtoken>2065545896</passwordtoken><languageid/><entitydate/><application>QZPARAM</application><servicename>QuartzReferenceData</servicename><taskname>SwiftSearch</taskname><sessionid>cx37Av-fSTcodMCcHTfFqmy</sessionid><userlocation>172.19.30.72</userlocation></header><parameters><bank_name_textbox></bank_name_textbox><bic_code1></bic_code1><location_code>0</location_code><location_code1></location_code1><bank_name>%</bank_name><bank_code>0</bank_code><land_code>0</land_code><bank_code1></bank_code1><bic_code>0</bic_code><re_bickey>0</re_bickey><gotocaller>no</gotocaller><land_code1></land_code1><re_originofkey>0</re_originofkey></parameters></request>");
		
		
		
		/*comsString.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
		comsString.append("<COMSPRCSINFO ID=\"EBSN\" ><BUSINESSINFO  ELEMENTCOUNT=\"0\" MAXSEVERITY=\"0\"                  INFOCOUNT=\"0\"  WARNINGCOUNT=\"0\"  ERRORCOUNT=\"0\"  SEVEREERRORCOUNT=\"0\"                  CRITICALERRORCOUNT=\"0\" ></BUSINESSINFO><OUTPUT>");
		comsString.append("<VECTOR ID=\"6\"><ELEMENT ID=\"1\"><CLASS ID=\"158017\"><ENTITY_CODE>GBAZA</ENTITY_CODE><UserID>227</UserID></CLASS></ELEMENT><ELEMENT ID=\"2\"><CLASS ID=\"158017\"><ENTITY_CODE>GBAZACOJNB</ENTITY_CODE><UserID>227</UserID></CLASS></ELEMENT></VECTOR></OUTPUT></COMSPRCSINFO>");
		*/
		
		/* Latest tested .. Failed due to application level service failure.
		 */ 
		//comsString.append("<EXTRNLINTRFC ID=\"1\" SVCID=\"sh_BrnchService\"><INPUT ID=\"1\"><CLASS ID=\"279800\">");
		//comsString.append("<LOGIN_ID>SYSGBATZ</LOGIN_ID><ENTITY_CODE>GBATZ</ENTITY_CODE><CHANNEL_TYPE>9</CHANNEL_TYPE><MAC_ON_CHAR_1>sh_QZET1101Ftch</MAC_ON_CHAR_1>");
		//comsString.append("<MAC_ON_CHAR_2>2065545896</MAC_ON_CHAR_2><MAC_ON_NUM_3>101</MAC_ON_NUM_3></CLASS></INPUT><INPUT ID=\"2\"><CLASS ID=\"156509\"><NAME>%</NAME><BANK_CODE>0</BANK_CODE>");
		//comsString.append("<LOCATION>0</LOCATION>D_CODE>0</LAND_CODE>");
		//comsString.append("<BIC_CODE>0</BIC_CODE></CLASS></INPUT><INPUT ID=\"3\"><CLASS ID=\"158213\"><BIC_KEY>0</BIC_KEY></CLASS></INPUT><OUTPUT><CLASS NAME=\"xlDArray\"/></OUTPUT></EXTRNLINTRFC>");
		
		
		/**
		 * Working - For swift directory list.  
		 *
		comsString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		comsString.append("<request><header><userid>SYSADMIN</userid><groupid>BRANCH_GROUP</groupid>");
		comsString.append("<entity>G</entity><passwordtoken>1297457889</passwordtoken><application>QZPARAM</application><servicename>QuartzReferenceData</servicename><taskname>SwiftSearch</taskname><sessionid>CJyuiPkhJSpHpbZU5aNZ6cb</sessionid>");
		comsString.append("<userlocation>172.19.30.80</userlocation></header><parameters><bank_name_textbox></bank_name_textbox><bic_code1></bic_code1><location_code>0</location_code><location_code1></location_code1><bank_name>%</bank_name>");
		comsString.append("<bank_code>0</bank_code><land_code>0</land_code><bank_code1></bank_code1><bic_code>0</bic_code><re_bickey>0</re_bickey><gotocaller>no</gotocaller><land_code1></land_code1><re_originofkey>0</re_originofkey></parameters></request>");
		*/
		
		/** Working - For Account list 
		 * 
		 */
		comsString.append("<request><header><userid>SYSGBAZA</userid><groupid>BRANCH_GROUP</groupid><entity>GBAZACOJNB</entity><passwordtoken>102701858</passwordtoken><application>AM</application><servicename>QuartzReferenceData</servicename><taskname>AccountSearch</taskname><sessionid>_KFpC4A5bpXJdlYsUmZnx1C</sessionid><userlocation>172.19.30.80</userlocation></header><parameters><opngdt></opngdt><accmgr></accmgr><accuse>7</accuse><iban></iban><posid></posid><opngdt1></opngdt1><stat></stat><accname></accname><re_subacid>0</re_subacid><refaccno></refaccno><re_posid>0</re_posid><bsplcode></bsplcode><bpid></bpid><re_postype>0</re_postype><curr>ZAR</curr></parameters></request>");
		
		/** 
		 * Working - For Acct Entry
		 */
		//comsString.append("<request><header><userid>SYSGBAZA</userid><groupid>BRANCH_GROUP</groupid><entity>GBAZACOJNB</entity><passwordtoken>102701858</passwordtoken><application>AM</application><servicename>QuartzTransactionService</servicename><taskname>AccountCreateRelease</taskname><sessionid>_KFpC4A5bpXJdlYsUmZnx1C</sessionid><userlocation>172.19.30.80</userlocation></header><parameters><requestdata><clfs>4</clfs><bsplcode>879</bsplcode><ewl>5</ewl><refaccno>2222222228</refaccno><netamt>2</netamt><h_off_bs_ind>2</h_off_bs_ind><actype>1</actype><acntusg>7</acntusg><acccurr>ZAR</acccurr><instrmnt_id>127</instrmnt_id><bpid>406</bpid><lngpref>2</lngpref><opendt1>20060125</opendt1><suspendintflg>2</suspendintflg><h_cr_frzn_flag>2</h_cr_frzn_flag><h_drmnt_flag>2</h_drmnt_flag><h_atmtc_open_flag>2</h_atmtc_open_flag><h_frzn_flag>2</h_frzn_flag><h_incmplt_flag>2</h_incmplt_flag><h_rclcltn_flag>2</h_rclcltn_flag><withholtax>2</withholtax><h_check_digit>2</h_check_digit><h_spl_frzn_flag>2</h_spl_frzn_flag></requestdata></parameters></request>");
		
		/**
		 * Webservice request - SOAP format
		 */
		//comsString.append("<HelloWorld xmlns=\"http://tempuri.org/\" />");
		//Object params[] ={comsString.toString()};
		
		
		
		//System.out.println("Response from WebService is "+serviceType.executeQuery(sellOrderTO));
		
		/**
		 * For Webservices call - Quartz - Barclays
		 */
		//System.out.println("Response from execute is "+serviceType.execute(paramTypesObj,params));
		
		//System.out.println("After execute of sellorders "+serviceType.execute(paramTypesObj,params));
		//java.util.PropertyResourceBundle propRes = (PropertyResourceBundle)PropertyResourceBundle.getBundle("statement");
		
		
		//System.out.println("After execute of sellorder and employeeinfo:"+serviceType.execute(paramTypesObj,params));
		//System.out.println("After execute of sellorder and employeeinfo:"+serviceType.execute(paramTypesObj,params));
		
		
		//System.out.println("After execute order dt:"+params[1]);
		
		
		//--- latest serviceType.close();
		
		//ComboboxData comboData = new ComboboxData();
		//HashMap hashParams = new HashMap();
		//hashParams.put("orderid","S0016");
		//hashParams.put("cgrgrpid","ACC");
		
		//System.out.println("Result1 is :"+comboData.getComboBoxData("getCountryRef",hashParams));
		
		//System.out.println("Result2 is :"+comboData.getComboBoxData("getSellOrderCombo",hashParams));
		
		/*com.tcs.ebw.taglib.EbwTableHelper helper = new EbwTableHelper("test");
		
		FileUtil.writeObjectToFile("d:\\arun\\helper.class", helper,false);
		*/
		
//		EbRdcBuysellReqTO buysellto = new EbRdcBuysellReqTO();
//	    buysellto.setRefid("MFB000000000721");
		
	    /*IEBWService serviceType = EBWServiceFactory.create("getBuySellDetails");
	    Class paramTypes[] = {EbRdcBuysellReqTO.class};
	    Object paramObjs[] = {buysellto};
	    //serviceType.execute(paramTypes,paramObjs);
	    */
	    new TestConnectionFactory().printClass();
		}catch(Exception e){
		    e.printStackTrace();
		    
		    //new EbwException(new TestConnectionFactory(),e);
		}
	}
	
	private Object  printClass(){
	    System.out.println("this.getClass()"+this.getClass());
	    System.out.println("this.getClass().getName()"+this.getClass().getName());
	    return new String("12");
	       
	}
}
