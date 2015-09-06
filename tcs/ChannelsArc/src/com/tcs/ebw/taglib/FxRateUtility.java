package com.tcs.ebw.taglib;

import java.util.ArrayList;
import java.util.HashMap;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;

public class FxRateUtility {
	
	private String toCurrency;
	private String frmCurrency;
	private String dateString;
	
	public FxRateUtility(){
	}
	
	public FxRateUtility(String frmCur,String toCur ,String dateString){
		System.out.println("NITHYA inside FxRateUtility contructor");
		this.frmCurrency = frmCur;
		this.toCurrency = toCur;
		this.dateString = dateString;
		
	}
	
	public String getFxRateConstantFromDB()
	{
		String constVal = "0";
		try{
			IEBWService service = EBWServiceFactory.create("getFxRateConstant");
	    	HashMap param = new HashMap();
	    	
	    	param.put("FRMCURRENCY",this.frmCurrency);
	    	param.put("TOCURRENCY",this.toCurrency);
	    	
	    	//param.put("DATE",this.dateString);
	    	
	    	
	    	
	    	Class cls [] ={Object.class, Boolean.class};
	    	Object objParams[] = {param,new Boolean(false)};	    	
	    	ArrayList objOutput = (ArrayList) service.execute(cls,objParams);
	    	//System.out.println("ReorderCols fetched from the DB :"+objOutput);
	    	System.out.println("NITHYA inside FxRateUtility objOutput="+objOutput);
	    	ArrayList objOutputVal = (ArrayList)objOutput.get(1);
	    	if(objOutput !=null ){
	    		constVal = objOutputVal.get(0).toString();
	    	  	}
    	
    	}catch(Exception e){
    		System.out.println("Error when getting reorder data from DB:"+e);
    	}
    	EBWLogger.trace(this,"Exited from getReorderInfoBeforeInsert with reordecols :"+constVal);
    	System.out.println("NITHYA inside FxRateUtility constVal="+constVal);
		return constVal;
	}
	

	
	public static void main(String[] args) {
		
		FxRateUtility fxRateUtility = new FxRateUtility("GBP","IDR","");
		String d = fxRateUtility.getFxRateConstantFromDB();
		System.out.println("d in main = "+d);
	}

}
