/*
 * Created on Tue Dec 12 17:19:09 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.common.util;

import com.tcs.ebw.common.util.EBWLogger;

public class SplitSelectedId{
	
public static String[] SplitRow(String[] selectedRow){
		String delim = "~";
		String[] strRowData=selectedRow[0].split("~");
		return strRowData;
}

public static String SplitStrValStd(String[] completeStr, int indexno)
{
	String delim = "~";
	return SplitStrVal(completeStr, delim, indexno);

}

/**
 *	Single row selection scenario
**/
public static String SplitStrVal(String[] completeStr, String delim, int indexno)
{
	int arrIndx=0;
	String substr="";
	int count=completeStr[arrIndx].length();
	try{
		String objArr[]=completeStr[arrIndx].split(delim);
		//objArr=completeStr[arrIndx].split(delim);		
		for(int i=0;i<objArr.length;i++)
			System.out.println("objArr[" +i+"] is :" + objArr[i]);
		
		if(indexno < 0 || indexno > count){	
			EBWLogger.trace("com.tcs.ebw.common.util.SplitSelectedId  ","the index number passed is "+ indexno);
			System.out.println("SplitSelectedId :the index number passed is "+ indexno);
		}else {
			substr=objArr[indexno];
		}
		
	}catch(ArrayIndexOutOfBoundsException ex){
			ex.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
	System.out.println("Value for index :" +indexno+ "is:" +substr);
	return substr;
}


	public static void main(String args[]){
		String str[]={"abcd~pqrs~wrw~fdf"};
		String Val = SplitSelectedId.SplitStrValStd(str,1);
		System.out.println("Received Value is " + Val);
		System.out.println("SplitRow :" + SplitRow(str)[0]);
	}
}