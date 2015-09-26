/*
 * Created on Jan 12, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.serverside.query;

import java.sql.Connection;
import java.text.SimpleDateFormat;

//import com.tcs.ebw.trade.transferobject.EbRDCBuySellReqNONDBTO;
import com.tcs.ebw.transferobject.EmployeeTO;
import com.tcs.ebw.transferobject.EBWTransferObject;
import com.tcs.ebw.transferobject.EmployeeTO;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.tcs.ebw.transferobject.SellOrderTO;
//import com.tcs.ebw.transferobject.OrdersTO;
//import com.tcs.ebw.transferobject.EbRoleNDBTO;
import java.util.ResourceBundle;
import com.tcs.ebw.common.util.ConvertionUtil;
import java.util.Date;

/**
 * @author ashokvijayakumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SQLClient {
	
	public static void main(String args[]) {
		ResourceBundle queryRB = ResourceBundle.getBundle("Statement");
		try{
			
			
//			Hashtable properties = new Hashtable();
//			//Get the connection iformation from property File.
//			properties.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
//			properties.put(Context.PROVIDER_URL,"jnp://172.19.31.94:1099");
//			properties.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
//			InitialContext ctx = new InitialContext(properties);
//			DataSource ds = (DataSource) ctx.lookup("ebwOradb1");
//			Connection serviceConnection = ds.getConnection();
			QueryExecutor queryExecutor = new QueryExecutor();
//			queryExecutor.setConnection(serviceConnection);
//			SellOrderTO sellOrderTO = new SellOrderTO();
//			OrdersTO ordersTO = new OrdersTO();
//			
//			ordersTO.setActunit(new Double(3.0));
//			ordersTO.setStatus(new Double(0));
//			ordersTO.setAssettype(new Double(2.0));
//			ordersTO.setValidity(new Double(0));
//			EbRDCBuySellReqNONDBTO toObj = new EbRDCBuySellReqNONDBTO();
//			toObj.setAssetname("fine");
//			toObj.setAmount(new Double("15.0"));
//			toObj.setCommission(new Double("18.0"));
//			toObj.setTotalamt(new Double("15.0"));
//			toObj.setAccamt(new Double("15.0"));
//			toObj.setAccno("152012");
//			toObj.setAsset("Building");
//			toObj.setPortfolio("Building");
//			toObj.setCurrency("INR");
//			toObj.setAssettype(new Double("15.0"));
			
//		    sellOrderTO.setSellOrderId("S0144");
//		    Date orderDt = ConvertionUtil.convertToDate("01/02/2006");
//			sellOrderTO.setSellOrderDt("01-JAN-2006");
//			sellOrderTO.setExchangeCurrency("INR");
//			sellOrderTO.setExchangeRate("1.5");
//			sellOrderTO.setBaseCurrency("EUROR");
//			sellOrderTO.setItemDescription("okkva");
//			queryExecutor.execute("setSellOrder", sellOrderTO);
			//Date sellOrderDate = new Date(format.format(date));

			
//			Date date = new Date();
//			date.setDate(10);
//			date.setMonth(2);
//			date.setYear(2006);
//			System.out.println(date);
//			System.out.println(ConvertionUtil.convertToDate("10/02/20005"));
			//sellOrderTO.setOrderDt(ConvertionUtil.convertToDate("10/02/2005"));
//			System.out.println("Date "+ sellOrderTO.getOrderDt());
			//sellOrderTO.setBaseCurrency("EURO");
//			 sellOrderTO.setBaseCurrency("USD");
//			sellOrderTO.setExchangeRate(new Double("12"));
//			sellOrderTO.setItemDescription("Central Processing Unit");
//			queryExecutor.execute("setSellOrder", sellOrderTO);
//						int startTime = (int)System.currentTimeMillis()*1000;
//						SellOrderTO OrderTO = (SellOrderTO)queryExecutor.executeQuery("getSellOrder", null);
//						int endTime = (int)System.currentTimeMillis()*1000;
//						System.out.println("Total Time Taken in seconds"+ (endTime-startTime));
//						  System.out.println("SellOrderTO SEllORder Id  "+ OrderTO.getSellOrderId());
//						   System.out.println("SellOrderTO SEllORder Id  "+ OrderTO.getOrderDt());
//						   System.out.println("SellOrderTO SEllORder Id  "+ OrderTO.getBaseCurrency());
//						   System.out.println("SellOrderTO SEllORder Id  "+ OrderTO.getExchangeCurrency());
//						   System.out.println("SellOrderTO SEllORder Id  "+ OrderTO.getExchangeRate());
//						   System.out.println("SellOrderTO SEllORder Id  "+ OrderTO.getItemDescription());
			
			
//			int startTime = (int)System.currentTimeMillis()*1000;
			java.util.Vector resultList = null;
			
//			    resultList = (java.util.Vector)queryExecutor.executeQuery("setMutualFundBuyOrder", toObj);
////			
////			int endTime = (int)System.currentTimeMillis()*1000;
////			System.out.println("Total Time Taken in seconds"+ (endTime-startTime));
////			System.out.println("Number of rows returned " + resultList.size()); 
//			for(int i=0;i<resultList.size();i++){
//				System.out.println("Role Type Combo "+ resultList.get(i));
//			}			
//			//			
			//			JdbcExecutor jdbcExecutor = ExecutorFactory.createJdbcExecutor();
			//			String query = jdbcExecutor.getQueryString(queryRB.getString("getSellOrder.Query"), null);
			//			String fileName = "SellOrderEntry.sqlj";
			//			SQLJExecutor sqljExecutor = new SQLJExecutor();
			//			String packageName =  new SQLClient().getClass().getName();
			//			System.out.println("Package Name " + packageName);
			//			String url = new SQLClient().getClass().getResource(packageName).toString();
			//			System.out.println("URL  "+ url);
			//			sqljExecutor.creatSQLJFile(packageName+ fileName,query);
			
			//			Runtime.getRuntime().exec("javac ");
			//			Hashtable properties = new Hashtable();
			//			//Get the connection iformation from property File.
			//			properties.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
			//			properties.put(Context.PROVIDER_URL,"jnp://172.19.31.94:1099");
			//			properties.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			//			InitialContext ctx = new InitialContext(properties);
			//            DataSource ds = (DataSource) ctx.lookup("ebwOradb1");
			//			Connection serviceConnection = ds.getConnection();
			//			JdbcExecutor jdbcExecutor =(JdbcExecutor) ExecutorFactory.createQueryExecutor();
			//			jdbcExecutor.setConnection(serviceConnection);
			//			SellOrderTO sellOrderTO = new SellOrderTO();
			//			sellOrderTO.setSellOrderId("S0001");
			//			SellOrderTO soTO[] = (SellOrderTO[]) jdbcExecutor.executeQuery("getSellOrder",sellOrderTO);
			//			System.out.println("SellorderTOlength "+ soTO.length);
			//			for(int i=0;i<soTO.length;i++){
			//				System.out.println("SellOrderId " + soTO[i].getBaseCurrency());
			//			}
			//System.out.println("SellOrderId " + soTO.getSellOrderId());
			//			EmployeeTO employeeTO = new EmployeeTO();
			//			employeeTO.setEmpNo("152825");
			//			employeeTO.setDesignation("Information Technology Analyst");
			//			employeeTO.setDepartment("CC");
			//			employeeTO.setDoj("11/12/2004");
			//			employeeTO.setSalary("12000");
			//			Object object = queryExecutor.executeQuery("getEmployeeInfo", employeeTO);
			//			EmployeeTO employTO = (EmployeeTO)object;
			//			System.out.println("Employee Number " + employTO.getEmpNo());
			//			System.out.println("Employee Designation "+ employTO.getDesignation());
			//			System.out.println("Employee Departement " +employTO.getDepartment());
			//			EmployeeTO employTO[] = (EmployeeTO[]) object;
			//			for(int i=0;i<employTO.length;i++){
			//				System.out.println("Employee Number " + employTO[i].getEmpNo());
			//				System.out.println("Employee Designation "+ employTO[i].getDesignation());
			//				System.out.println("Employee Departement " +employTO[i].getDepartment());
			//			}
			//Object[] data = new Object[]{"Assistant Systems Engineer", "Computer Consultancy", "152993"};
			//			Object object = queryExecutor.executeQuery("getEmpInfo", data);
			//queryExecutor.execute("updateEmployeeInfo",data);
			
			//			Object[] results = (Object[])object;
			//			for(int i=0;i<results.length;i++){
			//				System.out.println("Results " + results[i]);
			//			}
			//			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
