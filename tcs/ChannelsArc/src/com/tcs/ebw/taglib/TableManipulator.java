/**
 * 
 */
package com.tcs.ebw.taglib;
import java.util.*;
import java.lang.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
//import java.sql.*;

//import com.tcs.ebw.serverside.query.QueryExecutor;


/**
 * @author 193332
 *
 */

public class TableManipulator {

	/**
	 * @param args
	 */
	
	 int			level;
	 int[]		dbColIndxAL	;
	 Object[]		dbColValAL ;
	 ArrayList	dbColumnNameAL;
	 ArrayList 	headerAL;
	 ArrayList 	returnArrAL;
	 ArrayList 	colOpAL;
	 ArrayList 	prevIndx;
	 ArrayList  rowColOpIndxAL;	
	 String xColOpStr;
	 String[] xColOpStrArray;
	 ArrayList 	dataRowAL;
	 String rowOpStr ;
	 
	 String prevRowSum;
		 		//String rowOP = new String();
	 Integer iLevel ;
	 String columnOp5 ;
	 String columnOp4;
	 String columnOp3;
	 String columnOp2 ;
	 String columnOp1;
	 ArrayList headers;
	 Field fieldLevel;
	 Field fieldColumnOps;
	 Field fieldHeaders;
	 Field fieldXColOp;
	 Object tmFields;
	 LinkedHashMap icolumnOps;



	public TableManipulator() {
		headers					= new ArrayList();
		dbColumnNameAL			= new ArrayList();
		headerAL				= new ArrayList();
		returnArrAL				= new ArrayList();
		colOpAL					= new ArrayList();
		prevIndx 				= new ArrayList();
		rowColOpIndxAL			= new ArrayList();
		dataRowAL				= new ArrayList();
		xColOpStr				= new String();
		rowOpStr				= new String();
		tmFields				= new Object();
		icolumnOps				= new LinkedHashMap();
		columnOp5 = new String();
		columnOp4 = new String();
		columnOp3 = new String();
		columnOp2 = new String();
		columnOp1 = new String();
	}
	
	
	/**
	 * @for creating Header Values as "Header:DB Value"
	 */


	//public ArrayList getManipulatedData(ArrayList arraylist1,String keyVal){
	//public ArrayList getManipulatedData(ArrayList arraylist1,Class staticClass,Object[] rowHeadStr){
		
	public ArrayList getManipulatedData(ArrayList arraylist1,Object[] rowHeadStr,Integer headerLevel,ArrayList displayHeaders,LinkedHashMap columnOp,String addColOpStr,String prevRowSumboo){
			//public ArrayList getManipulatedData(ArrayList arraylist1,Class staticClass){	 
			//System.out.println(" XXXXXXXXXXXXXXXXXXXXXXXX  Inside TableManipulator XXXXXXXXXXXXX");
			
/*
		
try{
			fieldLevel = staticClass.getDeclaredField("level");
			fieldColumnOps = staticClass.getDeclaredField("columnOps");
			fieldHeaders = staticClass.getDeclaredField("headers");
			fieldXColOp = staticClass.getDeclaredField("xColOp");
		} catch(Exception e) {
			System.out.println("Exception:::"+e);
		}
		 	fieldLevel.setAccessible(true);
		 	fieldColumnOps.setAccessible(true);
		 	fieldHeaders.setAccessible(true);
		 	fieldXColOp.setAccessible(true);
				 
		try{
			tmFields = (Object)staticClass.newInstance() ;
		} catch(Exception e) {
			System.out.println("Exception:::"+e);
		}
	
		try{
			iLevel = new Integer(fieldLevel.get(tmFields).toString());
			icolumnOps = (LinkedHashMap)fieldColumnOps.get(tmFields);
			headers = (ArrayList)fieldHeaders.get(tmFields);
		} catch(Exception e) {
			System.out.println("Exception:::"+e);
		}
			*/
		try{
			iLevel = headerLevel;
			icolumnOps = (LinkedHashMap)columnOp;
			headers = (ArrayList)displayHeaders;
			prevRowSum = prevRowSumboo;
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception:::"+e);
		}	 

		ArrayList iTempAL = new ArrayList();
		for(int t = 0 ; t < rowHeadStr.length ; t++){
			iTempAL.add(t,(rowHeadStr[t].toString()));
		}
		arraylist1.add(0,iTempAL);
		//System.out.println("after adding col arraylist1:::--------------- "+arraylist1);
		//iLevel = tmFields.level;
		//LinkedHashMap icolumnOps = (LinkedHashMap)tmFields.columnOps; 
		//headers = (ArrayList)tmFields.headers;
		//System.out.println("::::::::::::headers:::----->"+headers);
		int icolumnOpsSize = icolumnOps.size();
		//xColOpStr = tmFields.xColOp.toString();	
		try{
			//xColOpStr = (String)fieldXColOp.get(tmFields);
			//xColOpStr = "Sum:Sum";
			xColOpStr = addColOpStr;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		}
		//System.out.println("xColOpStr:::--------------------->"+xColOpStr);
			
		if(!xColOpStr.equals("") || !xColOpStr.equals(null)){
			xColOpStrArray = new String[4];
			xColOpStrArray  = xColOpStr.split(":");
		}
	
		level	= iLevel.intValue();
		ArrayList idbColumnNameAL = (ArrayList)arraylist1.get(0);
		returnMap(idbColumnNameAL,icolumnOps);
		//System.out.println("columnOp1------> "+columnOp1);
		//getFirstHeader(arraylist1,keyVal);
		getFirstHeader(arraylist1);
		boolean flag = true;
		Object headerVal = new Object();
		int	arraylist1Size = arraylist1.size();
		
		
		
		
		
				
		for(int x = 2 ; x < arraylist1Size ; x++){
			
			ArrayList dbColumnValAL = (ArrayList)arraylist1.get(x);
			
			int hdSize = dbColIndxAL.length;
			//System.out.println(":::::::::::::::::flag:::----->"+flag);
			if(flag){
				ArrayList arayL2 = new ArrayList();
				ArrayList tempArayL2 = new ArrayList();
				ArrayList prevArray = new ArrayList();
				int itNo=0;
				
				//System.out.println(":::::::::::::::::prevArray:::----->"+prevArray);
				//System.out.println("dataRowAL:::----->"+dataRowAL);
				//String element=prevArray.get(0).toString();
				arayL2.add("DR");
				arayL2.addAll((ArrayList)arraylist1.get(x-1));
				
				if(prevRowSum!=null && prevRowSum!="" && !prevRowSum.equals(""))
				{
					if(x==2)
					{
					itNo=x;
					System.out.println("x==2");
					tempArayL2 = getSumOnPrevRowData(arayL2,arayL2,itNo);
					}
					else
					{
						System.out.println("x!=2");
						prevArray=((ArrayList)dataRowAL.get(dataRowAL.size()-1));
						 tempArayL2 = getSumOnPrevRowData(arayL2,prevArray,itNo);
					}
					
				}
				 else
				 {
				//System.out.println(":::::::::::::::::TARUN Muthu:::----->"+arayL2);
				tempArayL2 = getOpOnRowData(arayL2);
				 }
				returnArrAL.add(tempArayL2);
				//System.out.println(":::::::::::::::::returnArrAL:::----->"+returnArrAL);
			}
			//System.out.println("hdSize:::::::::"+hdSize);
			int mark = 0;
					
			boolean oFlag = false;
			for(int h = (hdSize-1);h >= 0 ; h-- ){
				int hdrIndx = dbColIndxAL[h];
			//	System.out.println("dbColumnValAL.get(hdrIndx)---------:::::::::"+dbColumnValAL.get(hdrIndx));
			//	System.out.println("dbColValAL[h]------------:::::::::"+dbColValAL[h]);
			
				if(!(dbColumnValAL.get(hdrIndx).equals(dbColValAL[h]))){
					mark = h;
					oFlag = true;
				}
				
			}
			if(oFlag){
				int colOpIndx = hdSize-1;
//				System.out.println("colOpIndx------------:::::::::"+colOpIndx);
//				System.out.println("mark------------:::::::::"+mark);
				for(int w = mark;w < hdSize; w++ ){
					int prevIndxVal = Integer.parseInt(prevIndx.get(colOpIndx).toString());
					//System.out.println("prevIndxVal:::::::::"+prevIndxVal);
					getOpOnData(x-1,prevIndxVal,colOpIndx,arraylist1);
					prevIndx.set(colOpIndx,new Integer(x));
					colOpIndx--;	
				}
			}
				
			int hMark = 0;
			boolean hFlag = false;
			for(int h = 0;h < hdSize; h++ ){
				int hdrIndx = dbColIndxAL[h];
				
				if(!(dbColumnValAL.get(hdrIndx).equals(dbColValAL[h]))){
					hMark = h;
					hFlag = true;
					break;
				}
				
			}
				
			if(hFlag){
				int colOpIndx = hdSize-1;
				for(int w = hMark;w < hdSize; w++ ){
					String hdr = headerAL.get(w).toString();
					int hdrIndx = dbColIndxAL[w];
					headerVal = dbColumnValAL.get(hdrIndx);
					ArrayList hdValAL = new ArrayList();
					String hdVal = new String();
					hdVal = "H"+(w+1);
					hdValAL.add(hdVal);
					hdValAL.add((hdr+":"+dbColumnValAL.get(hdrIndx)));
					returnArrAL.add(hdValAL);
					dbColValAL[w] = headerVal;
					prevIndx.set(w,new Integer(x));
					if(w == (hdSize-1)){				
						flag = true;				
					} 	
				}
			}
		}
			
		ArrayList arayL1 = new ArrayList();
		ArrayList tempArayL1 = new ArrayList();
		ArrayList prevArray=new ArrayList();
		arayL1.add("DR");
		arayL1.addAll((ArrayList)arraylist1.get((arraylist1Size-1)));
		if(prevRowSum!=null && prevRowSum!="" && !prevRowSum.equals(""))
		{
				prevArray=((ArrayList)dataRowAL.get(dataRowAL.size()-1));
				tempArayL1 = getSumOnPrevRowData(arayL1,prevArray,0);
			
			
		}
	else
	{
		tempArayL1 = getOpOnRowData(arayL1);
	}
		returnArrAL.add(tempArayL1);
			
		for(int h = ((dbColIndxAL.length)-1);h >=0 ; h-- ){
			int colOpIndx = h;
			int prevIndxVal = Integer.parseInt(prevIndx.get(h).toString());
			getOpOnData((arraylist1Size-1),prevIndxVal,colOpIndx,arraylist1);
		}
		//System.out.println("dataRowAL:::----->"+dataRowAL);
		if(!xColOpStr.equals("") || !xColOpStr.equals(null)){
//			System.out.println("dbColumnNameAL   Lucky---------:::::::::"+dbColumnNameAL);
			dataRowAL.add(0,dbColumnNameAL);	
			getOpOnXCol(dataRowAL);	
			dataRowAL.remove(0);
		}
//		System.out.println("returnArrAL:::----->"+returnArrAL);
		return returnArrAL;
	}

		
	/**
	 * @Gettign Header for the First Row
	 */
	//public void getFirstHeader(ArrayList arraylist1,String keyVal){
	public void getFirstHeader(ArrayList arraylist1){
		//System.out.println("arraylist1:::--------------------->"+arraylist1);
		//String levelStr = keyVal+".level";
		//level			= Integer.parseInt(params.getString(levelStr));
		//level			= iLevel.intValue();
		//	System.out.println("level:::11111--------------------->"+level);
		dbColIndxAL		= new int[level];
		dbColValAL 		= new Object[level];
		//String rowOp = keyVal+".rowOp";
		//String rowOpStr =(String)params.getString(rowOp);
		//System.out.println("rowOpStr:::--------------------->"+rowOpStr);
		//String xColOp = keyVal+".xColOp";
		//xColOpStr = (String)params.getString(xColOp);
		//xColOpStr = tmFields.xColOp.toString();
		//System.out.println("xColOpStr:::--------------------->"+xColOpStr);
		//xColOpStrArray = new String[4];
/*
				try{
					xColOpStrArray  = xColOpStr.split(":");
						
				}catch(Exception e){	System.out.println("Exception:::"+e);	}

*/



		// for(int i = 0; i < level ; i++){
			 //prevIndx.add(new Integer(1));
			//ArrayList tempColOpAL = new ArrayList();
			//String header = keyVal+".header"+i;
			//String columnOp = keyVal+".columnOp"+(i+1);
			
			//String colOpStr =(String)params.getString(columnOp);
		headerAL.addAll(headers);
//		System.out.println("headerAL:::--------------------->"+headerAL);
		headerAL.remove(0);
//		System.out.println("headerAL:::--------------------->"+headerAL);

			//headerAL.add((String)params.getString(header));
			//System.out.println("headerAL:::--------------------->"+headerAL);
			/*
				try{
					StringTokenizer colOpST = new StringTokenizer(colOpStr,",");
						while(colOpST.hasMoreTokens()){
							tempColOpAL.add(colOpST.nextToken());
						}
				}catch(Exception e){	System.out.println("Exception:::"+e);	}
*/
		for(int i = 1; i <= level ; i++){
			prevIndx.add(new Integer(1));
			ArrayList tempColOpAL = new ArrayList();
//			System.out.println("headerAL:::--------------------->"+headerAL);
			switch(i){
				case 1:
					try{
						StringTokenizer colOpST1 = new StringTokenizer(columnOp1,",");
						while(colOpST1.hasMoreTokens()){
							tempColOpAL.add(colOpST1.nextToken());
						}
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("Exception:::"+e);	}
					break;
				case 2:
					try{
						StringTokenizer colOpST2 = new StringTokenizer(columnOp2,",");
						while(colOpST2.hasMoreTokens()){
							tempColOpAL.add(colOpST2.nextToken());
						}
					}catch(Exception e){	
						e.printStackTrace();
						System.out.println("Exception:::"+e);	}
					break;
				case 3:
					try{
						StringTokenizer colOpST3 = new StringTokenizer(columnOp3,",");
						while(colOpST3.hasMoreTokens()){
							tempColOpAL.add(colOpST3.nextToken());
						}
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("Exception:::"+e);	}
					break;
				case 4:
					try{
						StringTokenizer colOpST4 = new StringTokenizer(columnOp4,",");
						while(colOpST4.hasMoreTokens()){
							tempColOpAL.add(colOpST4.nextToken());
						}
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("Exception:::"+e);	}
					break;
				case 5:
					try{
						StringTokenizer colOpST5 = new StringTokenizer(columnOp5,",");
						while(colOpST5.hasMoreTokens()){
							tempColOpAL.add(colOpST5.nextToken());
						}
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("Exception:::"+e);	}
					break;
				default:
					break;
			}
//			System.out.println("tempColOpAL:::--------------------->"+tempColOpAL);
			colOpAL.add(tempColOpAL);
		}
		//System.out.println("tempColOpAL:::--------------------->"+tempColOpAL);
		//sortList(tempColOpAL);
		//System.out.println("tempColOpAL:::--------------------->"+tempColOpAL);
		//colOpAL.add(tempColOpAL);
//		System.out.println("colOpAL:::--------------------->"+colOpAL);
		//}
		dbColumnNameAL = (ArrayList)arraylist1.get(0);
		ArrayList fColAL = new ArrayList();
		fColAL.add("CL");
		//fColAL.addAll(dbColumnNameAL);
		//returnArrAL.add(fColAL);
		ArrayList dbDataValAL = (ArrayList)arraylist1.get(1);
		//System.out.println("dbDataValAL:::--------------------->"+dbDataValAL);
		int dbColSize = dbColumnNameAL.size();
		
		String[] rowColArray = new String[dbColSize];
		String[] rowStrArray = new String[2];
		
		try{
		//	System.out.println("::::::::::::::rowOpStr:::::::::"+rowOpStr);
			rowStrArray = rowOpStr.split(":");
			rowColOpIndxAL.add((String)rowStrArray[0]);
			String rowColStr = (String)rowStrArray[1];
			rowColArray = rowColStr.split(";");
			for(int t =0;t < rowColArray.length; t++){
				String rowColName = (String)rowColArray[t];
				for(int a =0;a < dbColSize; a++){
					String dbColName = (String)dbColumnNameAL.get(a);
					if(rowColName.equalsIgnoreCase(dbColName))
						rowColOpIndxAL.add(new Integer(a));
				}
			}


		}catch(Exception e){	
			e.printStackTrace();
			System.out.println("Exception:::"+e);	}		
		dbColumnNameAL.add((String)rowStrArray[0]);
		fColAL.addAll(dbColumnNameAL);
		returnArrAL.add(fColAL);

	
	for(int a =0;a < level; a++){
			String hdr = headerAL.get(a).toString();
//			System.out.println("hdr -- "+hdr);
//			System.out.println("dbColumnNameAL -- "+dbColumnNameAL);
			int indxDbColVal = dbColumnNameAL.indexOf(hdr);
//			System.out.println("indxDbColVal -- "+indxDbColVal);
//			System.out.println("dbDataValAL -- "+dbDataValAL);
			dbColIndxAL[a] = indxDbColVal;
			dbColValAL[a] = dbDataValAL.get(indxDbColVal);
			ArrayList hValAL = new ArrayList();
			
			String hVal = "H"+(a+1);
			hValAL.add(hVal);
			hValAL.add((hdr+":"+dbDataValAL.get(indxDbColVal)));
			returnArrAL.add(hValAL);
		}
	}
	
		 

	/**
	 * @ getting Coloumn Name & Function name
	 */

	public void getOpOnData(int n,int prevIndx,int colOpIndx,ArrayList arraylist1){
			
ArrayList  sumOfColOp= new ArrayList();
		ArrayList  avgOfColOp= new ArrayList();
		ArrayList  MinOfColOp= new ArrayList();
		ArrayList  MaxOfColOp= new ArrayList();
		ArrayList specColOp = (ArrayList)colOpAL.get(colOpIndx);
		specColOp = (ArrayList)getOrderedAL(specColOp);
					
		if(!specColOp.isEmpty()){
			String fnHeader = new String();
			
			fnHeader = "FN"+(colOpIndx+1);
			ArrayList fnArrAL = new ArrayList();
			fnArrAL.add(fnHeader);
			
			String opStr =  new String();
			String colStr = new String();
			for(int x = 0; x < specColOp.size() ; x++){
				String colOps = new String();
				colOps = specColOp.get(x).toString();
				
				StringTokenizer st2 = new StringTokenizer(colOps,":");
						
				while(st2.hasMoreTokens()){
					opStr = st2.nextToken();
					colStr = st2.nextToken();
				}
										
				if(opStr.equalsIgnoreCase("sum")){
					sumOfColOp = (ArrayList)getSumOfData(n,prevIndx,colStr,arraylist1);
					fnArrAL.add(sumOfColOp);
				} else if(opStr.equalsIgnoreCase("avg")) {
					avgOfColOp = (ArrayList)getAvgOfData(n,prevIndx,colStr,arraylist1);
					fnArrAL.add(avgOfColOp);
				}else if(opStr.equalsIgnoreCase("min")) {
					MinOfColOp = (ArrayList)getMinOfData(n,prevIndx,colStr,arraylist1);
					fnArrAL.add(MinOfColOp);
				}else if(opStr.equalsIgnoreCase("max")) {
					MaxOfColOp = (ArrayList)getMaxOfData(n,prevIndx,colStr,arraylist1);
					fnArrAL.add(MaxOfColOp);
				}else {
					
				}
			}
			returnArrAL.add(fnArrAL);
		}
	}

		   

	/**
	 * @ getting Operation On Row Data
	 */
	 
	public ArrayList getOpOnRowData(ArrayList arayL2){
		
		ArrayList returnSumArray  = new ArrayList();
		returnSumArray = arayL2;
		dataRowAL.add(arayL2);
		double returnVal = 0.0d;
		int destIndx=0;
		if(prevRowSum!=null && prevRowSum!="" && !prevRowSum.equals(""))
		{
		//int destIndx=Integer.parseInt(rowColOpIndxAL.get(3).toString());
			destIndx=Integer.parseInt(rowColOpIndxAL.get(rowColOpIndxAL.size()-1).toString());
		}
		String opStr = (String)rowColOpIndxAL.get(0);
		if(opStr.equalsIgnoreCase("sum")){
         //returnSumArray = getSumOnRowData(arayL2);
			returnVal = getSumOnRowData(arayL2);
		}else{
			
		}
		returnSumArray.add((new BigDecimal(returnVal).toString()));
		if(prevRowSum!=null && prevRowSum!="" && !prevRowSum.equals(""))
		{
			returnSumArray.add(destIndx+1, (new BigDecimal(returnVal).toString()));
		}
	
	return returnSumArray;
	}
	

	/**
	 * @ This Method is used to perform Operation On Additionally Added Column Data
	 */

	public void getOpOnXCol(ArrayList arraylist1){
		
		ArrayList fnArrAL = new ArrayList();
		fnArrAL.add("X");
		ArrayList returnVal = new ArrayList();
//		System.out.println("::::::rowOpStr:::::::getOpOnXCol:::::::"+rowOpStr);
		String tempXColOpStr1 = new String();
		String tempXColOpStr2 = new String();
		try{
			StringTokenizer xColOpST = new StringTokenizer(rowOpStr,":");
			while(xColOpST.hasMoreTokens()){
				tempXColOpStr1 = (String)xColOpST.nextToken();
				tempXColOpStr2 = (String)xColOpST.nextToken();
			}
		}catch(Exception e){	e.printStackTrace();
		System.out.println("Exception:::"+e);	}
		String xOpStr = (String)xColOpStrArray[0];
		String xColStr = (String)xColOpStrArray[0];
		
		if(xOpStr.equalsIgnoreCase("sum")){
			//returnVal = (ArrayList)getSumOnXColData(arraylist1,xColStr);
			returnVal = (ArrayList)getSumOnXColData(arraylist1,tempXColOpStr2);
			fnArrAL.add(returnVal);
		} else if(xOpStr.equalsIgnoreCase("avg")){
			//returnVal = (ArrayList)getAvgOnXColData(arraylist1,xColStr);
			returnVal = (ArrayList)getAvgOnXColData(arraylist1,tempXColOpStr2);
			fnArrAL.add(returnVal);
		}
		returnArrAL.add(fnArrAL);
	}
	
		  

	/**
	 * @ This Method is used to perform Summation Operation On Row Data
	 */
	  
public double getSumOnRowData(ArrayList arayL2){
		double	rowSum 	= 0.0d;
		for(int x = 1; x < rowColOpIndxAL.size(); x++ ){
			int rowColOpIndx = Integer.parseInt(rowColOpIndxAL.get(x).toString());
			double    colVal = 0.0d;
			try{
				if(!arayL2.get(rowColOpIndx+1).getClass().toString().equalsIgnoreCase("class java.lang.String"))
				{
				if(arayL2.get(rowColOpIndx+1) == null || ((arayL2.get(rowColOpIndx+1)).equals("null"))){
			    	colVal = 0.0;
			    }else{
			    colVal   = Double.parseDouble( arayL2.get(rowColOpIndx+1).toString());
			    }
			    
			    rowSum = rowSum + colVal;
				}
			} catch(Exception e){
				//e.printStackTrace();
				//System.out.println("Inside Exception::::::"+e);
				//colVal = 1;
				//rowSum = rowSum + colVal;
			}			
		}
		return rowSum;
	}

public ArrayList getSumOnPrevRowData(ArrayList arayL2,ArrayList prevarr,int itNo){
		System.out.println("---------getSumOnPrevRowData-----");
		System.out.println("---------prevarr-----"+prevarr);
		ArrayList returnSumArray  = new ArrayList();
		returnSumArray = arayL2;
		//dataRowAL.add(arayL2);
		double	rowSum 	= 0.0d;
		int destIndx=Integer.parseInt(rowColOpIndxAL.get(3).toString());
		if(itNo!=0)
		{
			rowSum = Double.parseDouble(prevarr.get(Integer.parseInt(rowColOpIndxAL.get(3).toString())+1).toString());
			System.out.println("Nith---rowSum=="+rowSum);
			for(int x = 1; x < rowColOpIndxAL.size()-1; x++ ){
				int rowColOpIndx = Integer.parseInt(rowColOpIndxAL.get(x).toString());
				double    colVal = 0.0d;
				try{
					//if(!arayL2.get(rowColOpIndx+1).getClass().toString().equalsIgnoreCase("class java.lang.String"))
					//{
					if(arayL2.get(rowColOpIndx+1) == null || ((arayL2.get(rowColOpIndx+1)).equals("null"))){
				    	colVal = 0.0;
				    }else{
				    colVal   = Double.parseDouble( arayL2.get(rowColOpIndx+1).toString());
				    System.out.println("nith---colVal=="+colVal);
				    }
				    
				    rowSum = rowSum + colVal;
				    System.out.println("nith2---rowSum=="+rowSum);
					//}
				} catch(Exception e){
					//e.printStackTrace();
					//System.out.println("Inside Exception::::::"+e);
					//colVal = 1;
					//rowSum = rowSum + colVal;
				}			
			}
		}
		/*ArrayList prevArrayL2=new ArrayList();
		prevArrayL2=(ArrayList)returnArrAL.get(returnArrAL.size()-1);
		System.out.println("=======prevArrayL2===="+prevArrayL2);
		String element=prevArrayL2.get(0).toString();
		if(element.startsWith("D")){*/
		//prevArrayL2.add(returnArrAL.get(returnArrAL.size()-1));
		else
		{
		rowSum = Double.parseDouble(prevarr.get(Integer.parseInt(rowColOpIndxAL.get(3).toString())+1).toString());
		System.out.println("1---rowSum=="+rowSum);
		for(int x = 1; x < rowColOpIndxAL.size()-1; x++ ){
			int rowColOpIndx = Integer.parseInt(rowColOpIndxAL.get(x).toString());
			double    colVal = 0.0d;
			try{
				//if(!arayL2.get(rowColOpIndx+1).getClass().toString().equalsIgnoreCase("class java.lang.String"))
				//{
				if(arayL2.get(rowColOpIndx+1) == null || ((arayL2.get(rowColOpIndx+1)).equals("null"))){
			    	colVal = 0.0;
			    }else{
			    colVal   = Double.parseDouble( arayL2.get(rowColOpIndx+1).toString());
			    System.out.println("1---colVal=="+colVal);
			    }
			    
			    rowSum = rowSum + colVal;
			    System.out.println("2---rowSum=="+rowSum);
				//}
			} catch(Exception e){
				//e.printStackTrace();
				//System.out.println("Inside Exception::::::"+e);
				//colVal = 1;
				//rowSum = rowSum + colVal;
			}			
		}
		}
		returnSumArray.add(destIndx+1, (new BigDecimal(rowSum).toString()));
		dataRowAL.add(returnSumArray);
		//returnSumArray.add((new BigDecimal(rowSum).toString()));
		 System.out.println("End---returnSumArray=="+returnSumArray);
		//}
		return returnSumArray;
		
	}
	


	/**
	 * @ This Method is used to perform Summation Operation On Coloumn Data
	 */
/*
	public ArrayList getSumOnXColData(ArrayList arraylist1,String colStr){
		int arraySize = arraylist1.size();
		ArrayList returnXSumArray  = new ArrayList();
		double	colSum 	= 0.0d;
		returnXSumArray.add("SUM");
		//System.out.println("colStr --------------::::::"+colStr);
		String[] colNames = colStr.split(";");
//System.out.println("returnXSumArray ------b4-------::::::"+returnXSumArray);
		returnXSumArray= (ArrayList)getArrangedAL(returnXSumArray,colNames);
//System.out.println("returnXSumArray -------AFTR-------::::::"+returnXSumArray);
		//int 	colIndex = arrL1.indexOf(colNames[0]);

		for(int q=0;q < arraySize; q++){
			ArrayList arrayRow = new ArrayList();
			arrayRow = (ArrayList)arraylist1.get(q);
			int arrayRowSize = arrayRow.size();
			
			
			
			double    colVal = 0.0d;
			
				
				try{
				     colVal   = Double.parseDouble( arrayRow.get(arrayRowSize-1).toString());
					// System.out.println(":::colVal:::::::::----"+colVal);
				    colSum = colSum + colVal;
					//System.out.println(":::colSum:::::::::----"+colSum);
				} catch(Exception e){				
						colVal = 1.0;
						colSum = colSum + colVal;
				}			
			}
			
			//System.out.println(":::colSum:::::::::----"+colSum);
			String fnStr = (new Double(colSum)).toString();

			returnXSumArray.set((returnXSumArray.size()-1),fnStr);

	return returnXSumArray;
	
	}
*/

	/**
	 * @ This Method is used to perform Summation Operation On Additionally Added Coloumn Data
	 */

	public ArrayList getSumOnXColData(ArrayList arraylist1,String tempXColOpStr2){
		ArrayList arrL1 = (ArrayList)arraylist1.get(0);
		int arraySize = arraylist1.size();
		ArrayList returnXSumArray  = new ArrayList();
		//double	colSum 	= 0.0d;
		returnXSumArray.add("SUM");
		String colStr = tempXColOpStr2.toUpperCase();
		//System.out.println("colStr --------------::::::"+colStr);
		String[] colNames = colStr.split(";");
		//System.out.println("returnXSumArray ------b4-------::::::"+returnXSumArray);
		returnXSumArray= (ArrayList)getArrangedAL(returnXSumArray,colNames);
		//System.out.println("returnXSumArray -------AFTR-------::::::"+returnXSumArray);
		//int 	colIndex = arrL1.indexOf(colNames[0]);
		for(int q=0;q < colNames.length; q++){
			//for(int q=0;q <= colNames.length; q++){
			double    colVal = 0.0d;
			double	colSum 	= 0.0d;
			int colIndex = 0;
			//System.out.println("arrL1 --------------::::::"+arrL1);
			if(q == colNames.length){
				colIndex = arrL1.indexOf("SUM");
			}else{
			    colIndex = arrL1.indexOf(colNames[q]);
			}
		 	//colIndex = arrL1.indexOf(colNames[q]);
			//System.out.println("colIndex --------------::::::"+colIndex);
			for(int x=1;x < arraySize; x++){
				ArrayList arrayL1 = (ArrayList)arraylist1.get(x);
				try{
				    colVal   = Double.parseDouble( arrayL1.get(colIndex+1).toString());
				    colSum = colSum + colVal;
				} catch(Exception e){				
				//colVal = 1;
				//colSum = colSum + colVal;
				}			
			}
			//System.out.println(":::colSum:::::::::----"+colSum);
			String fnStr = (new BigDecimal(colSum)).toString();
			if(q == colNames.length){
				returnXSumArray.set((returnXSumArray.size()-1),fnStr);
			}else{
				returnXSumArray.set((colIndex+1),fnStr);
			}
		}
//		System.out.println(":::returnXSumArray:::::::::----"+returnXSumArray);	
		return returnXSumArray;
	}


	/**
	 * @ This Method is used to perform Average Operation On Additionally Added Coloumn Data
	 */	

	public ArrayList getAvgOnXColData(ArrayList arraylist1,String tempXColOpStr2){
		
		ArrayList arrL1 = (ArrayList)arraylist1.get(0);
		int arraySize = arraylist1.size();
		ArrayList returnXAvgArray  = new ArrayList();
		//double	colSum 	= 0.0d;
		returnXAvgArray.add("AVG");
		String colStr = tempXColOpStr2.toUpperCase();
		String[] colNames = colStr.split(";");

		returnXAvgArray= (ArrayList)getArrangedAL(returnXAvgArray,colNames);

		for(int q=0;q <= colNames.length; q++){
			int 	avgCount 		= 	0;
			double	XColAvg 		= 0.0d;
			double    colVal = 0.0d;
			double	colSum 	= 0.0d;
			//int 	colIndex = arrL1.indexOf(colNames[0]);
			int colIndex = 0;
			if(q == colNames.length){
				colIndex = arrL1.indexOf("SUM");
			}else{
			    colIndex = arrL1.indexOf(colNames[q]);
			}				
			for(int x=1;x < arraySize; x++){
				ArrayList arrayL1 = (ArrayList)arraylist1.get(x);
				try{
				    colVal   = Double.parseDouble( arrayL1.get(colIndex+1).toString());
				    colSum = colSum + colVal;
				} catch(Exception e){				
					//colVal = 1;
					//colSum = colSum + colVal;
				}			
				avgCount++;
			}
			//System.out.println("Average COunt:::::::::----"+avgCount);
			XColAvg = colSum/avgCount;
			String fnStr = (new BigDecimal(XColAvg)).toString();
			if(q == colNames.length){
				returnXAvgArray.set((returnXAvgArray.size()-1),fnStr);
			}else{
			    returnXAvgArray.set((colIndex+1),fnStr);
			}
		}
		//returnXAvgArray.set((returnXAvgArray.size()-1),fnStr);
		return returnXAvgArray;
	}
	
	
/*
	public ArrayList getAvgOnXColData(ArrayList arraylist1,String colStr){
		
		
		int arraySize = arraylist1.size();
		ArrayList returnXAvgArray  = new ArrayList();
		double	colSum 	= 0.0d;
		returnXAvgArray.add("AVG");
		String[] colNames = colStr.split(";");

		returnXAvgArray= (ArrayList)getArrangedAL(returnXAvgArray,colNames);
		int 	avgCount 		= 	0;
		double	XColAvg 		= 0.0d;
		//int 	colIndex = arrL1.indexOf(colNames[0]);

		for(int q=0;q < arraySize; q++){
			ArrayList arrayRow = new ArrayList();
			arrayRow = (ArrayList)arraylist1.get(q);
			int arrayRowSize = arrayRow.size();
			
			
			
			double    colVal = 0.0d;
			
				
				try{
				     colVal   = Double.parseDouble( arrayRow.get(arrayRowSize-1).toString());
				    colSum = colSum + colVal;
				} catch(Exception e){				
						colVal = 1.0;
						colSum = colSum + colVal;
				}	
				avgCount++;
			}
			//System.out.println("Average COunt:::::::::----"+avgCount);
			XColAvg = colSum/avgCount;
			String fnStr = (new Double(XColAvg)).toString();

			returnXAvgArray.set((returnXAvgArray.size()-1),fnStr);

	return returnXAvgArray;
	
	}
*/




	/**
	 * @ This Method is used to perform Summation Operation On Coloumn Data
	 */

	public ArrayList getSumOfData(int val,int prevIndx,String colStr,ArrayList arraylist1){
		
		ArrayList arrL1 = (ArrayList)arraylist1.get(0);
		//System.out.println("arrL1 -------:::::: "+arrL1);
		//System.out.println("prevIndx -------:::::: "+prevIndx);
		//System.out.println("val -------:::::: "+val);
		ArrayList returnSumArray  = new ArrayList();
		//System.out.println("colStr -------getSumOfData-------::::::"+colStr);
		String[] colNames = colStr.split(";");
		returnSumArray.add("Sum");
		//System.out.println("returnSumArray -------getSumOfData-------::::::"+returnSumArray);
		returnSumArray= (ArrayList)getArrangedAL(returnSumArray,colNames);
		//System.out.println("returnSumArray -------getSumOfData-------::::::"+returnSumArray);
		for(int q=0;q < colNames.length; q++){
			int 	colIndex = arrL1.indexOf(colNames[q]);
			double	colSum 	= 0.0d;
			double    colVal = 0.0d;
			for(int xx = prevIndx ; xx <= val ; xx++){
				ArrayList arrayL1 = (ArrayList)arraylist1.get(xx);
				try{
				    colVal   = Double.parseDouble( arrayL1.get(colIndex).toString());
				    colSum = colSum + colVal;
				    
				} catch(Exception e){				
						//colVal = 1;
						//colSum = colSum + colVal;
				}			
			}
			
			//String fnStr = (new Double(colSum)).toString();
			String fnStr = (new BigDecimal(colSum)).toString();
			returnSumArray.set((colIndex+1),fnStr);
//			returnSumArray.set((colIndex+1),new Double(colSum));
		}		
		return returnSumArray;
	}


	/**
	 * @ This Method is used to perform Average Operation On Coloumn Data
	 */

	public ArrayList getAvgOfData(int val,int prevIndx,String colStr,ArrayList arraylist1){
		
		ArrayList arrL1 = (ArrayList)arraylist1.get(0);
		ArrayList returnAvgArray  = new ArrayList();
		String[] colNames = colStr.split(";");
		returnAvgArray.add("Avg");
		returnAvgArray= (ArrayList)getArrangedAL(returnAvgArray,colNames);
		
		for(int q=0;q < colNames.length; q++){
			int 	colIndex 	= arrL1.indexOf(colNames[q]);
			double	colAvg 		= 0.0d;
			double  colVal 		= 0.0d;
			double  colSum 		= 0.0d;
			int 	avgDiv 		= 	0;
		
			for(int xx = prevIndx ; xx <= val ; xx++){
				ArrayList arrayL1 = (ArrayList)arraylist1.get(xx);
				try{
				    colVal   = Double.parseDouble( arrayL1.get(colIndex).toString());
				    colSum = colSum + colVal;
				} catch(Exception e){				
					//	colVal = 1;
						//colSum = colSum + colVal;
				}
				avgDiv++;
			}
			colAvg = colSum/avgDiv;
			String fnStr = (new BigDecimal(colAvg)).toString();
			returnAvgArray.set((colIndex+1),fnStr);
		}
		return returnAvgArray;
	}


	/**
	 * @ This Method is used to perform Minimum arithmatic Operation On Coloumn Data
	 */

	public ArrayList getMinOfData(int val,int prevIndx,String colStr,ArrayList arraylist1){
		
		ArrayList arrL1 = (ArrayList)arraylist1.get(0);
		ArrayList returnMinArray  = new ArrayList();
		String[] colNames = colStr.split(";");
		returnMinArray.add("Min");
		returnMinArray= (ArrayList)getArrangedAL(returnMinArray,colNames);
		
		for(int q=0;q < colNames.length; q++){
			int 	colIndex = arrL1.indexOf(colNames[q]);
			double	minVal 	= 0.0d;
			double    colVal = 0.0d;
				
			for(int xx = prevIndx ; xx <= val ; xx++){
				ArrayList arrayL1 = (ArrayList)arraylist1.get(xx);
				if(xx == prevIndx){
				minVal = Double.parseDouble( arrayL1.get(colIndex).toString());
				}
				try{
				    colVal   = Double.parseDouble( arrayL1.get(colIndex).toString());
				    if(colVal <= minVal){
				       	minVal = colVal;
				    }
				} catch(Exception e){				
				}			
			}
			String fnStr = (new BigDecimal(minVal)).toString();
			returnMinArray.set((colIndex+1),fnStr);
		}		
		return returnMinArray;
	}


	/**
	 * @ This Method is used to perform Maximum arithmatic Operation On Coloumn Data
	 */
	
	public ArrayList getMaxOfData(int val,int prevIndx,String colStr,ArrayList arraylist1){
		
		ArrayList arrL1 = (ArrayList)arraylist1.get(0);
		ArrayList returnMaxArray  = new ArrayList();
		String[] colNames = colStr.split(";");
		returnMaxArray.add("Max");
		returnMaxArray= (ArrayList)getArrangedAL(returnMaxArray,colNames);
		
		for(int q=0;q < colNames.length; q++){
			int 	colIndex = arrL1.indexOf(colNames[q]);
			double	maxVal 	= 0.0d;
			double    colVal = 0.0d;
				
			for(int xx = prevIndx ; xx <= val ; xx++){
				ArrayList arrayL1 = (ArrayList)arraylist1.get(xx);
				if(xx == prevIndx){
					maxVal = Double.parseDouble( arrayL1.get(colIndex).toString());
					}
				try{
					colVal   = Double.parseDouble( arrayL1.get(colIndex).toString());
				    if(colVal >= maxVal){
				       	maxVal = colVal;
				    }
				} catch(Exception e){				
					
				}			
			}			
			String fnStr = (new BigDecimal(maxVal)).toString();
			returnMaxArray.set((colIndex+1),fnStr);
		}		
		return returnMaxArray;
	
	}
	

	/**
	 * @ This Method is used to Sort Columns wrt to DBColumn 
	 */

	public ArrayList getOrderedAL(ArrayList specColOp){
		
		String str1 = new String();
		String str2 = new String();
				
		for(int a=0; a<specColOp.size();a++){
			String colOps = specColOp.get(a).toString();
			//System.out.println("colOps::::::::-------"+colOps);
			StringTokenizer st2 = new StringTokenizer(colOps,":");
			//System.out.println("st2::::::::-------"+st2);
			while(st2.hasMoreTokens()){
				 str1 = st2.nextToken();
//System.out.println("str1::::::::-------"+str1);
				 str2 = st2.nextToken();
//System.out.println("str2::::::::-------"+str2);
			}
		String[] colSplit = str2.split(";");						
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(str1).append(":");
					
		for(int p = 0; p < dbColumnNameAL.size();p++){
			String colValue = new String();
			colValue = (String)dbColumnNameAL.get(p);
			
				for(int y = 0; y < colSplit.length;y++){
					if(colValue.equalsIgnoreCase(colSplit[y])){
						sbuffer.append(colValue).append(";");						
					}
				}
		}		
		sbuffer.deleteCharAt((sbuffer.length()-1));
		specColOp.set(a,sbuffer);
		}
	return specColOp;
	}



	/**
	 * @ This Method is use to arrange ArrayList with Non Operating Column as Null
	 */
	
	public ArrayList getArrangedAL(ArrayList returnSumArray,String[] colNames){
		
		for(int x=0;x < dbColumnNameAL.size(); x++){	
			boolean gFlag = false;
		
			for(int q=0;q < colNames.length; q++){		
				if(!((colNames[q].equals(dbColumnNameAL.get(x))))){
					gFlag = true;
				}else{
					gFlag = false;
					break;					
				}
			}
				if(gFlag){
					returnSumArray.add("Null");
				}else{
					returnSumArray.add(dbColumnNameAL.get(x));
				}
			gFlag = false;
		}
		return returnSumArray;
	}



	/**
	 * @ This is a general method can be called on any arraylist to sort its element alphabatically.
	 */

	public void sortList(ArrayList tempColOpAL){
		
	    Collections.sort(tempColOpAL);
	}
	
/**
 *  This ReturnMap Method is used to Map arithmatic operation specific to perticular Headers.Currently
 * it is supporting only upto 5 level of Headers.
 */		
	
	
	public void	returnMap(ArrayList idbColumnNameAL,LinkedHashMap icolumnOps){

	//	System.out.println("::::::idbColumnNameAL::::::::::::::"+idbColumnNameAL);
	//	System.out.println("::::::icolumnOps::::::::::::::"+icolumnOps);
		ArrayList cOpAL5 = new ArrayList();
		ArrayList cOpAL4 = new ArrayList();
		ArrayList cOpAL3 = new ArrayList();
		ArrayList cOpAL2 = new ArrayList();
		ArrayList cOpAL1 = new ArrayList();
		ArrayList cOpAL = new ArrayList();
		int colCount = 1;

		for(int i = 0 ; i < idbColumnNameAL.size(); i++){
			//String iColName = (String)idbColumnNameAL.get(i).toString().toLowerCase();
			String iColName = (String)idbColumnNameAL.get(i).toString();
			//System.out.println("::::::iColName::::::::::::::"+iColName);
			//System.out.println("::::::icolumnOps::::::::::::::"+icolumnOps);
			String colMapVal = (String)icolumnOps.get(iColName);
			//System.out.println("::::::colMapVal::::::::::::::"+colMapVal);
			if(colMapVal!=null)
			{
			if(!colMapVal.equals("")){
				StringTokenizer st1 = new StringTokenizer(colMapVal,":");
				String str1 = new String();
				String str2 = new String();
				while(st1.hasMoreTokens()){
					str1 = st1.nextToken();
					str2 = st1.nextToken();
				}
				if(colCount ==1){
					rowOpStr = str2+":"+ iColName;
				} else {
					rowOpStr = rowOpStr+";"+iColName;	
				}
				int headerCount = 1;
//				System.out.println("level:::--------------------->"+level);
				
				if(level >= 1){
					try{
						StringTokenizer st2 = new StringTokenizer(str1,",");
						while(st2.hasMoreTokens()){
							String strr1 = st2.nextToken();
							StringTokenizer st3 = new StringTokenizer(strr1,";");
							while(st3.hasMoreTokens()){
								String strrr1 = st3.nextToken();
								// System.out.println("::::::strrr1::::::::::::::"+strrr1);
								//System.out.println("::::::iColName::::::::::::::"+iColName);
								String opCol = strrr1 +":"+iColName;
								// System.out.println("::::::opCol::::::::::::::"+opCol);
		
								switch(headerCount){
									case 1:
										cOpAL1.add(opCol);
										break;
									case 2:
										cOpAL2.add(opCol);
										break;
									case 3:
										cOpAL3.add(opCol);
										break;
									case 4:
										cOpAL4.add(opCol);
										break;
									case 5:
										cOpAL5.add(opCol);
										break;
									default:
										break;
								}
							}
							headerCount++;
						}
					} catch(Exception e) {
						e.printStackTrace();
						System.out.println("Exception:::"+e);
					}
				}else{
					String opCol = new String();
					try{
						StringTokenizer st3 = new StringTokenizer(str1,";");
						while(st3.hasMoreTokens()){
							String strrr1 = st3.nextToken();
							opCol = strrr1 +":"+iColName;
							switch(headerCount){
								case 1:
									cOpAL1.add(opCol);
									break;
								case 2:
									cOpAL2.add(opCol);
									break;
								case 3:
									cOpAL3.add(opCol);
									break;
								case 4:
									cOpAL4.add(opCol);
									break;
								case 5:
									cOpAL5.add(opCol);
									break;
								default:
									break;
								}

						}
				 }catch(Exception e){
					opCol = str1 +":"+iColName;
					switch(headerCount){
						case 1:
							cOpAL1.add(opCol);
							break;
						case 2:
							cOpAL2.add(opCol);
							break;
						case 3:
							cOpAL3.add(opCol);
							break;
						case 4:
							cOpAL4.add(opCol);
							break;
						case 5:
							cOpAL5.add(opCol);
							break;
						default:
							break;
					}
					System.out.println("Exception in Tablemanipulator Class::::------"+e);
				 }
				 headerCount++;

				}
				colCount++;
			 }
		   }
		}
		//if(!columnOp1.equals("") || !columnOp1.equals("null"))

		if(!cOpAL1.isEmpty()){
			//System.out.println(":::::cOpAL1:::::::------------>"+cOpAL1);
			sortList(cOpAL1);
			cOpAL.add(cOpAL1);
		}
		if(!cOpAL2.isEmpty()){
			//System.out.println(":::::cOpAL2:::::::------------>"+cOpAL2);
			sortList(cOpAL2);
			cOpAL.add(cOpAL2);
		}
		if(!cOpAL3.isEmpty()){
			//System.out.println(":::::cOpAL3:::::::------------>"+cOpAL3);
			sortList(cOpAL3);
			cOpAL.add(cOpAL3);
		}
		if(!cOpAL4.isEmpty()){
			sortList(cOpAL4);
			cOpAL.add(cOpAL4);
		}
		if(!cOpAL5.isEmpty()){
			sortList(cOpAL5);
			cOpAL.add(cOpAL5);
		}
		//System.out.println(":::::cOpAL:::::::------------>"+cOpAL);

		for(int x =0; x < cOpAL.size(); x++){
			ArrayList testAL= (ArrayList)cOpAL.get(x);
			int place = (int)x+1;
			ArrayList tempOpAL = new ArrayList();
			for(int y =1; y <= testAL.size(); y++){
				String testStr = (String)testAL.get(y-1);
				switch(place){
					case 1:
						if(y == 1){
							columnOp1 = columnOp1+testStr;
							StringTokenizer cOpst1 = new StringTokenizer(testStr,":");
							String cOpStr1 = new String();
							String cOpStr2 = new String();
							while(cOpst1.hasMoreTokens()){
								cOpStr1 = cOpst1.nextToken();
								cOpStr2 = cOpst1.nextToken();
							}
							tempOpAL.add(cOpStr1);
						}else{
							StringTokenizer cOpst2 = new StringTokenizer(testStr,":");
							String cOpStrr1 = new String();
							String cOpStrr2 = new String();
							while(cOpst2.hasMoreTokens()){
								cOpStrr1 = cOpst2.nextToken();
								cOpStrr2 = cOpst2.nextToken();
							}
							if(tempOpAL.contains(cOpStrr1)){
								columnOp1 = columnOp1 + ";" +cOpStrr2;
							}else{
								columnOp1 = columnOp1 + ","+testStr;
							}
							tempOpAL.add(cOpStrr1);
						}
						break;
					case 2:
						if(y == 1){
							columnOp2 = columnOp2+testStr;
							StringTokenizer cOpst1 = new StringTokenizer(testStr,":");
							String cOpStr1 = new String();
							String cOpStr2 = new String();
							while(cOpst1.hasMoreTokens()){
								cOpStr1 = cOpst1.nextToken();
								cOpStr2 = cOpst1.nextToken();
							}
							tempOpAL.add(cOpStr1);
						}else{
							StringTokenizer cOpst2 = new StringTokenizer(testStr,":");
							String cOpStrr1 = new String();
							String cOpStrr2 = new String();
							while(cOpst2.hasMoreTokens()){
								cOpStrr1 = cOpst2.nextToken();
								cOpStrr2 = cOpst2.nextToken();
							}
							if(tempOpAL.contains(cOpStrr1)){
								columnOp2 = columnOp2 + ";" +cOpStrr2;
							}else{
								columnOp2 = columnOp2 + ","+testStr;
							}
							tempOpAL.add(cOpStrr1);
						}
						break;
					case 3:
						if(y == 1){
							columnOp3 = columnOp3+testStr;
							StringTokenizer cOpst1 = new StringTokenizer(testStr,":");
							String cOpStr1 = new String();
							String cOpStr2 = new String();
							while(cOpst1.hasMoreTokens()){
								cOpStr1 = cOpst1.nextToken();
								cOpStr2 = cOpst1.nextToken();
							}
							tempOpAL.add(cOpStr1);
						}else{
							StringTokenizer cOpst2 = new StringTokenizer(testStr,":");
							String cOpStrr1 = new String();
							String cOpStrr2 = new String();
							while(cOpst2.hasMoreTokens()){
								cOpStrr1 = cOpst2.nextToken();
								cOpStrr2 = cOpst2.nextToken();
							}
							if(tempOpAL.contains(cOpStrr1)){
								columnOp3 = columnOp3 + ";" +cOpStrr2;
							}else{
								columnOp3 = columnOp3 + ","+testStr;
							}
							tempOpAL.add(cOpStrr1);
						}
						break;
					case 4:
						if(y == 1){
							columnOp4 = columnOp4+testStr;
							StringTokenizer cOpst1 = new StringTokenizer(testStr,":");
							String cOpStr1 = new String();
							String cOpStr2 = new String();
							while(cOpst1.hasMoreTokens()){
								cOpStr1 = cOpst1.nextToken();
								cOpStr2 = cOpst1.nextToken();
							}
							tempOpAL.add(cOpStr1);
						}else{
							StringTokenizer cOpst2 = new StringTokenizer(testStr,":");
							String cOpStrr1 = new String();
							String cOpStrr2 = new String();
							while(cOpst2.hasMoreTokens()){
								cOpStrr1 = cOpst2.nextToken();
								cOpStrr2 = cOpst2.nextToken();
							}
							if(tempOpAL.contains(cOpStrr1)){
								columnOp4 = columnOp4 + ";" +cOpStrr2;
							}else{
								columnOp4 = columnOp4 + ","+testStr;
							}
							tempOpAL.add(cOpStrr1);
						}
						break;
					case 5:
						if(y == 1){
							columnOp5 = columnOp5+testStr;
							StringTokenizer cOpst1 = new StringTokenizer(testStr,":");
							String cOpStr1 = new String();
							String cOpStr2 = new String();
							while(cOpst1.hasMoreTokens()){
								cOpStr1 = cOpst1.nextToken();
								cOpStr2 = cOpst1.nextToken();
							}
							tempOpAL.add(cOpStr1);
						}else{
							StringTokenizer cOpst2 = new StringTokenizer(testStr,":");
							String cOpStrr1 = new String();
							String cOpStrr2 = new String();
							while(cOpst2.hasMoreTokens()){
								cOpStrr1 = cOpst2.nextToken();
								cOpStrr2 = cOpst2.nextToken();
							}
							if(tempOpAL.contains(cOpStrr1)){
								columnOp5 = columnOp5 + ";" +cOpStrr2;
							}else{
								columnOp5 = columnOp5 + ","+testStr;
							}
							tempOpAL.add(cOpStrr1);
						}
						break;
					default:
						break;
				}
			}
		}
	}
	


/**
 * This method has been called from the EbwReport Class for Display Type "disp2"
 * (Customized for Portfolio Screen of Custody)
 * 
 * 
 */
	//public ArrayList sumOfSecondHeader (ArrayList tempAL,int secondHeaderSumALPosition,ArrayList secondHeaderSumAL,int dispVal) {
/*	public ArrayList sumOfSecondHeader (ArrayList tempAL,int secondHeaderSumALPosition,ArrayList secondHeaderSumAL,ArrayList dispVal,ArrayList falseHeaderColAL, ArrayList currHeaderColAL) {	
		ArrayList rowAL = (ArrayList)tempAL.get(1);
		int size =  secondHeaderSumAL.size();
	//	System.out.println("rowAL::::::::::----->"+rowAL);
	//	System.out.println("secondHeaderSumALPosition::::::::::----->"+secondHeaderSumALPosition);
	//	System.out.println("secondHeaderSumAL::::::::::----->"+secondHeaderSumAL);
	//	System.out.println("dispVal::::::::::----->"+dispVal);
		//int dispSize = dispColIndx.size();
		//int dispVal = Integer.parseInt(dispColIndx.get(dispSize-1).toString());
		double	Sum 	= 0.0d;
		boolean flag1=false,flag2=false;
		for(int i=1;i<dispVal.size();i++){
		try{	   

		//	System.out.println("::::::::: 1;;;;"+secondHeaderSumAL.get((secondHeaderSumALPosition+i)).toString());
            
			String ss=secondHeaderSumAL.get((secondHeaderSumALPosition+i)).toString();
			if(falseHeaderColAL!=null ||currHeaderColAL!=null)
           {
               if(ss.startsWith("$") || ss.startsWith("*") ){
				ss=ss.substring(1);				
			} 
           }
				
			//double    colValSecondHeader = Double.parseDouble(secondHeaderSumAL.get((secondHeaderSumALPosition+1)).toString());
			double    colValSecondHeader = Double.parseDouble(ss);
			//double    colValrowAL = Double.parseDouble(rowAL.get(dispVal).toString());
		//	System.out.println("colValSecondHeader::::::::::----->"+colValSecondHeader);
			int dispValPosition = Integer.parseInt(dispVal.get(i).toString());
			double    colValrowAL = Double.parseDouble(rowAL.get(dispValPosition).toString());
		//	System.out.println("colValrowAL::::::::::----->"+colValrowAL);
			Sum = colValrowAL + colValSecondHeader;
			if(falseHeaderColAL!=null ||  currHeaderColAL!=null )
           {
				for(int j=0;j<falseHeaderColAL.size();j++){
				//	System.out.println(":::::"+dispVal.get(i)+"::::::::::"+falseHeaderColAL.get(j));
							if(dispVal.get(i).equals(falseHeaderColAL.get(j))){
								flag1=true;
							//	System.out.println("::::::::: Equal value   ::::::::" );
							}
				}	
				for(int j=0;j<currHeaderColAL.size();j++){
					//	System.out.println(":::::"+dispVal.get(i)+"::::::::::"+falseHeaderColAL.get(j));
								if(dispVal.get(i).equals(currHeaderColAL.get(j))){
									flag2=true;
								//	System.out.println("::::::::: Equal value   ::::::::" );
								}
					}
		}			
			//System.out.println("Sum::::::::::----->"+Sum);
		} catch(Exception e){				
		//	System.out.println("Exception Inside sumOfSecondHeader::::::::::----->"+e);
		}			
		String fnStr = (new BigDecimal(Sum)).toString();
		//secondHeaderSumAL.set((secondHeaderSumALPosition+1),fnStr);
	//	System.out.println("fnStr:::::before:::::----->"+fnStr);
		if(falseHeaderColAL!=null || currHeaderColAL!=null )
           {
        if(flag1==true){
			flag1=false;
			fnStr="$"+fnStr;
		//	System.out.println("fnStr:::::after:::::----->"+fnStr);
		}
		if(flag2==true){
			flag2=false;
			fnStr="*"+fnStr;
		//	System.out.println("fnStr:::::after:::::----->"+fnStr);
		}	
       }				

		secondHeaderSumAL.set((secondHeaderSumALPosition+i),fnStr);
	}
		return secondHeaderSumAL;
	}*/
	
	public ArrayList sumOfSecondHeader (ArrayList tempAL,int secondHeaderSumALPosition,ArrayList secondHeaderSumAL,ArrayList dispVal,String curr, String constant,String str) {	
		ArrayList rowAL = (ArrayList)tempAL.get(1);
		int size =  secondHeaderSumAL.size();
		//System.out.println("rowAL::::::::::----->"+rowAL);
		//System.out.println("secondHeaderSumALPosition::::::::::----->"+secondHeaderSumALPosition);
		//System.out.println("secondHeaderSumAL::::::::::----->"+secondHeaderSumAL);
		//System.out.println("dispVal::::::::::----->"+dispVal);
		//int dispSize = dispColIndx.size();
		//int dispVal = Integer.parseInt(dispColIndx.get(dispSize-1).toString());
		double	Sum 	= 0.0d;
		boolean flagc=true;
		for(int i=1;i<dispVal.size();i++){
		try{
			
			//double    colValSecondHeader = Double.parseDouble(secondHeaderSumAL.get((secondHeaderSumALPosition+1)).toString());
			double    colValSecondHeader = Double.parseDouble(secondHeaderSumAL.get((secondHeaderSumALPosition+i)).toString());
			//double    colValrowAL = Double.parseDouble(rowAL.get(dispVal).toString());
			//System.out.println("colValSecondHeader::::::::::----->"+colValSecondHeader);
			int dispValPosition = Integer.parseInt(dispVal.get(i).toString());
			double    colValrowAL = Double.parseDouble(rowAL.get(dispValPosition).toString());
			if(curr!=null && curr!="" && constant!=null &&  constant!="" &&  str!=null && str!=""){
			//	System.out.println("::::  Enter into loop::");
				if(!curr.equalsIgnoreCase(str)){
				if(flagc){
			//		System.out.println("::::  Before  colValrowAL::"+colValrowAL);
					colValrowAL=getFXrate(colValrowAL,constant);
			//		System.out.println("::::  After   colValrowAL::"+colValrowAL);
					flagc=false;
				}
					
					
				}
			//	System.out.println("::::  Exit from Loop  ::");
			}
		//	System.out.println("colValrowAL::::::::::----->"+colValrowAL);
			Sum = colValrowAL + colValSecondHeader;
		//	System.out.println("Sum::::::::::----->"+Sum);
		} catch(Exception e){				
			System.out.println("Exception Inside sumOfSecondHeader::::::::::----->"+e);
		}			
		String fnStr = (new BigDecimal(Sum)).toString();
		//secondHeaderSumAL.set((secondHeaderSumALPosition+1),fnStr);
		secondHeaderSumAL.set((secondHeaderSumALPosition+i),fnStr);
		}
		return secondHeaderSumAL;
	}


	
	public double getFXrate(double d,String constant)
	{
		//double d=Double.parseDouble(fStr);
		double e=Double.parseDouble(constant);
		 double f=0.0;
		if(e!=0)
		 f=(double)(d/e);
//		else
//		 System.out.println("::::::HERE CONSTANT IS ZERO...::::::::::::::d"+d);
		//System.out.println("::::::::::::::::::::e"+e);
		//System.out.println("::::::::::::::::::::f"+f);
		//String s=new String();
		//s=s+f;
		//System.out.println("::::::::::::::::::::s"+s);
		return f;
		
	}
	public double getFXrates(double val,double constant)
	{
		double returnvalue=0.0;
		if(constant==0.0)
		{
		//	System.out.println("CONSTANT IS ZERO::::   "+constant);
			returnvalue=val;
			
		}
		else{
			returnvalue=(val*constant);
		}
		return returnvalue;
	}
	
	
	public Object getManipulatedDataForExport(ArrayList arraylist1,Object[] rowHeadStr,Integer headerLevel,ArrayList displayHeaders,LinkedHashMap columnOp,String addColOpStr,String prevRowSumboo)
	throws Exception{
		ArrayList dataAfterOp = getManipulatedData(arraylist1,rowHeadStr,headerLevel,displayHeaders,columnOp,addColOpStr,prevRowSumboo);
		String fnname = "FN"+headerLevel;
		System.out.println("fnname="+fnname);
		Object opObj = null;
		Class collectionCls = Class.forName("java.util.ArrayList");
		Object collectionObj = null;
		collectionObj = (ArrayList) collectionCls.newInstance();
		ArrayList dataFnRow = null;
		ArrayList tableResultSetALEx = new ArrayList();
		
		for(int i=0;i<dataAfterOp.size();i++){
			//if (dataFnRow == null)
			//	dataFnRow = new ArrayList();
			ArrayList tempAL = (ArrayList)dataAfterOp.get(i);
			String elemntAtZ = tempAL.get(0).toString();
			if(elemntAtZ.startsWith("C"))
			{
				dataFnRow = new ArrayList();
				for(int w=1;w<tempAL.size()-1;w++)
					dataFnRow.add(tempAL.get(w).toString());
				System.out.println("dataFnRow Col Names="+dataFnRow);
				((ArrayList) collectionObj).add(dataFnRow);
			}
			ArrayList fnRow = new ArrayList();
			
			ArrayList dataRow = new ArrayList();
			
			//ArrayList dataFnRow = new ArrayList();
			
			if(elemntAtZ.startsWith("F"))
			{	
				//if (dataFnRow == null)
					dataFnRow = new ArrayList();
				fnRow = (ArrayList)tempAL.get(1);
				
				if(elemntAtZ.equalsIgnoreCase(fnname)){
					System.out.println("fnRow="+fnRow);
					dataRow = (ArrayList)dataAfterOp.get(i-1);
					System.out.println("dataRow="+dataRow);
					for(int j=1;j<fnRow.size()-1;j++){
						String element = fnRow.get(j).toString();
						if(element.equalsIgnoreCase("Null"))
							dataFnRow.add(dataRow.get(j).toString());
						else
							dataFnRow.add(fnRow.get(j).toString());
						}
					System.out.println("dataFnRow="+dataFnRow);
					//tableResultSetALEx.add(dataFnRow);
					((ArrayList) collectionObj).add(dataFnRow);
				}
			}
		//	dataFnRow = null;
			
		}
		opObj = collectionObj;
		System.out.println("opObj="+opObj);
		return opObj;
	
	}
	
	
		public ArrayList getManipulatedData(ArrayList arValAL, Class staticClass,	        Object[] rowHeadStr) { // TODO Auto-generated method stub	        return null;	    }	    public ArrayList sumOfSecondHeader(ArrayList tempAL,	        int secondHeaderSumALPosition, ArrayList secondHeaderSumAL, int dispVal) { // TODO Auto-generated method stub	        return null;	    }
	
	
}
	


	


