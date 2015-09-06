package com.tcs.ebw.taglib;

import com.tcs.ebw.codegen.beans.ComboboxData;
import com.tcs.ebw.codegen.beans.ExcelForm;
import com.tcs.ebw.common.util.*;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.taglib.*;
//import com.tcs.ebw.serverside.services.TableManipulator;
import java.io.PrintStream;
import java.io.*;
import java.lang.*;
import java.lang.reflect.Method;
import java.lang.reflect.*;
import java.util.*;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;

//import com.tcs.ebw.custody.formbean.*;

// Referenced classes of package com.tcs.ebw.taglib:
//            DataInterface, TableColAttrObj

public class EbwReportTable extends TagSupport
{
	
	StringBuffer out;
	private Object attrObj;
	
	
	ArrayList headerFunctionAL;
	ArrayList secondHeaderFunctionAL;
	ArrayList thirdHeaderFunctionAL;
	ArrayList secondHeaderSumAL;
	
	int secondHeaderSumALPosition ;
	int hFCount;
	int f3Count;
	int f2Count;
	boolean rowFlag;
	ArrayList colHeaderAL;
	ArrayList dispColIndx;
	String  displayCol= new String();
	String className = new String();
	String  currency = new String();
	String  constdisplayCol;
	
	String[] constDBCol;
	String[] constDsplCol;

	String[] dispCol;
	String[] testCol;
	StringBuffer stBuff;
	Integer iLevel;
	Object tmFields;
	int level = 0;
    public EbwReportTable(ArrayList tableResultAL)
    {
       
    }

    public EbwReportTable()
    {
	  //System.out.println ("Inside constructor");
      out= new StringBuffer();
	  tableHeader = null;
	  rowFlag = false;
	  f3Count = 0;
	  f2Count=0;
	  secondHeaderSumALPosition = 0;
	  secondHeaderSumAL = new ArrayList();
	  colHeaderAL = new ArrayList();
	  dispColIndx = new ArrayList();
	  constdisplayCol = new String();
	 
    }

    public int doEndTag()
        throws JspException {
		//out.append("</table>");
		
		try {
			JspWriter jspout = pageContext.getOut();
			jspout.println(out.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	out.delete (0,out.length());
	dispColIndx.clear();
	
	
		return 0;
    }

    public int doStartTag()
        throws JspException
    {
    try {
		
		 if(formname != null)
                attrObj = pageContext.getRequest().getAttribute(formname);
		
		            if(attrObj == null && formname != null)
                attrObj = pageContext.getSession().getAttribute(formname);
			
			
            Class formClass = Class.forName(attrObj.getClass().getName());
			//Class staticClass = Class.forName("com.tcs.ebw.serverside.services.TMFields");
			Class staticClass = Class.forName("com.tcs.ebw.taglib.TMFields");
			
			

		
appRB = ResourceBundle.getBundle("ApplicationConfig");
formRB = ResourceBundle.getBundle(this.appRB.getString("message-resources"));	
//statRB = ResourceBundle.getBundle("Statement");

//System.out.println("Asset Details Collection ----"+objPortfolioSummaryForm.getAssetDetailsCollection().getData());

formObj=(DataInterface)formClass.getMethod("get" + StringUtil.initCaps(name) + "Collection", null).invoke(attrObj, null);
System.out.println(":::::::get"+StringUtil.initCaps(name)+"Collection");
System.out.println("formObj::::::::::----->"+formClass.getMethod("get" + StringUtil.initCaps(name) + "Collection", null).getReturnType());
System.out.println(":::::::formObj.getData()::::::::::::::::::::::::"+formObj.getData());
ArrayList arValAL = (ArrayList)formObj.getData();
System.out.println(":::::::arValAL::::::::::::::::::::::::"+arValAL);
ArrayList rowHeadAL = (ArrayList)formObj.getRowHeader();
System.out.println(":::::::rowHeadAL::::::::::::::::::::::::"+rowHeadAL);
LinkedHashMap rowHead = (LinkedHashMap)rowHeadAL.get(0);
System.out.println(":::::::rowHead::::::::::::::::::::::::::"+rowHead);
Set rowHeadSet = rowHead.keySet();
System.out.println(":::::::rowHeadSet::::::::::::::::::::::::"+rowHeadSet);
Object[] rowHeadStr = (Object[])rowHeadSet.toArray();
System.out.println(":::::::rowHeadStr::::::::::::::::::::::::"+rowHeadStr);
System.out.println(":::::::rowHeadStr: Length::::::::::::::::"+rowHeadStr.length);

//ArrayList arValAL = (ArrayList)formClass.getMethod("getCorporateDetailsTableData", null).invoke(attrObj, null);
	//Object tmFields = (Object)staticClass.newInstance();
//New manipulator 
System.out.println("arValAL::::::::::----->"+arValAL);
		TableManipulator tm = new TableManipulator();
		//ArrayList tableResultAL = (ArrayList)tm.getManipulatedData(arValAL,staticClass);
		ArrayList tableResultAL = (ArrayList)tm.getManipulatedData(arValAL,staticClass,rowHeadStr);
System.out.println("tableResultAL::::::::::----->"+tableResultAL);

//Field[] zxc = (Field[])staticClass.getFields();
		Field fieldLevel = staticClass.getDeclaredField("level");
		//System.out.println("fieldLevel::::::::::----->"+fieldLevel);
		Field fieldColumnOps = staticClass.getDeclaredField("columnOps");
		//System.out.println("fieldColumnOps::::::::::----->"+fieldColumnOps);
		Field fieldHeaders = staticClass.getDeclaredField("headers");
		//System.out.println("fieldHeaders::::::::::----->"+fieldHeaders);
		Field fieldXColOp = staticClass.getDeclaredField("xColOp");
		//System.out.println("fieldXColOp::::::::::----->"+fieldXColOp);
		fieldLevel.setAccessible(true);
		fieldColumnOps.setAccessible(true);
		fieldHeaders.setAccessible(true);
	    fieldXColOp.setAccessible(true);

//System.out.println(":::::::Field::::::::::::::::"+field.get(tmFields));
/*for(int d = 0; d < zxc.length; d++){
System.out.println(":::::::zxc:::::::::::::::::::::::::::::::::::::"+zxc[d]);

field.get(tmFields)
}*/
//TMFields tmFields = TMFields.getInstance();
 try{
		  tmFields = (Object)staticClass.newInstance() ;
		} catch(Exception e) {
				 System.out.println("Exception:::"+e);
		}

		try{
		  iLevel = new Integer(fieldLevel.get(tmFields).toString());
		 
		 } catch(Exception e) {
				 System.out.println("Exception:::"+e);
		}
displayCol=(String)formClass.getMethod("getReorderCols", null).invoke(attrObj, null);
System.out.println("displayCol::::::::::----->"+displayCol);

//formObj=(DataInterface)formClass.getMethod("getCorporatedetailsCollection", null).invoke(attrObj, null);
  
formObj=(DataInterface)formClass.getMethod("get" + StringUtil.initCaps(name) + "Collection", null).invoke(attrObj, null);

  


ArrayList colattrobj=(ArrayList)formObj.getColAttrObjs();
//System.out.println("colattrobj::::::::::----->"+colattrobj);

             stBuff = new StringBuffer(); 
StringBuffer sBuffer = new StringBuffer();
int colattrobjSize = colattrobj.size();
testCol = new String[colattrobjSize+1];
constDsplCol = new String[colattrobjSize+1];
constDBCol= new String[colattrobjSize+1];
if(colattrobj != null && colattrobj.size() > 0){
             //String usercolOpStr = servicename+".rowOp";
									//String usercolOp			= (String)statRB.getString(usercolOpStr);
									//String usercolOp			= (String)tmFields.xColOp;
									String usercolOp			= (String)fieldXColOp.get(tmFields);
									//System.out.println("usercolOp::::::::::----->"+usercolOp);
		for(int d = 0; d < colattrobj.size(); d++){
									 
									String str = new String();
									
									String strChkHead = new String();
									TableColAttrObj tablecolattrobj = (TableColAttrObj)colattrobj.get(d);
									str = tablecolattrobj.getColName().toString();
	//System.out.println("str::::::::::----->"+str);
	strChkHead = 	formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + str);	
	//System.out.println("strChkHead::::::::::----->"+strChkHead);
									 constDBCol[d] = str;
									 constDsplCol[d] = strChkHead;
									 
									

									sBuffer.append(strChkHead).append(",");
									stBuff.append(str).append(",");

									

									if(d == (colattrobj.size()-1) && (!usercolOp.equals("")))	{
									String[] colOp = new String[2];
									colOp = usercolOp.split(":");
									String str1 = colOp[1];
								//	System.out.println("str1::::::::::----->"+str1);
			String str1Head = 	formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + str1);
			//System.out.println("str1Head::::::::::----->"+str1Head);
			constDBCol[d+1] = str1;
									 constDsplCol[d+1] = str1Head;
									sBuffer.append(str1Head).append(",");
									stBuff.append(str1).append(",");
									}
									
		}

	}
stBuff.deleteCharAt((stBuff.length()-1));
sBuffer.deleteCharAt((sBuffer.length()-1));

if(displayCol == null){
displayCol = "";
}


if(displayCol.equals("") || displayCol.equalsIgnoreCase("null") || displayCol == null){

	 constdisplayCol = sBuffer.toString();
	 //constdisplayCol = "EMP_SKILL,EMP_SALARY,EMP_EXP";
	 System.out.println("constdisplayCol::::::::::----->"+constdisplayCol);
	 displayCol = stBuff.toString();
	//displayCol = "EMP_SKILL,EMP_SALARY,EMP_EXP";
	// System.out.println("displayCol::::::::::----->"+displayCol);
	 testCol = constdisplayCol.split(",");
 
}
		 System.out.println("displayCol:::::::::::::::::::::::::::::::::::"+displayCol);
		dispCol = displayCol.split(",");

for(int x = 0 ; x < dispCol.length ; x++){
	for(int y = 0 ; y < constDsplCol.length ; y++){
		if(dispCol[x].equalsIgnoreCase(constDsplCol[y])){
			dispCol[x] = constDBCol[y];
			testCol[x] = constDsplCol[y];
			
		}
	}
}




	

//String userChoiceStr = servicename+".userChoice";
//String userLevelStr = servicename+".level";
		//String userChoice			= (String)statRB.getString(userChoiceStr);
		
		// userLevel			= Integer.parseInt(statRB.getString(userLevelStr));
		
		  
		  //userLevel			= 2;

		//ArrayList displayAL = (ArrayList)tmFields.headers;
		ArrayList displayAL = (ArrayList)fieldHeaders.get(tmFields);
		String userCSA = (String)displayAL.get(0);
//System.out.println("userCSA::::::::::----->"+userCSA);
			if(userCSA.equalsIgnoreCase("disp1")){
				//if(userCSA[1].equalsIgnoreCase("top")){
					
					printSeperateTableTop(tableResultAL);
			} else if(userCSA.equalsIgnoreCase("bottom")){
				//} else if(userCSA[1].equalsIgnoreCase("bottom")){
					
					printSeperateTableBottom(tableResultAL);
				}  else if(userCSA.equalsIgnoreCase("disp2")){
				
					 colHeaderAL = (ArrayList)tableResultAL.get(0);

					for (int q = 0;q<dispCol.length;q++) {


								for (int i = 0;i< colHeaderAL.size();i++) {
									if(dispCol[q].equalsIgnoreCase(colHeaderAL.get(i).toString())){
										dispColIndx.add(new Integer(i));
										
								}
							}
							
						
						}
					printSeperateTableForDisp2(tableResultAL);
				} 

		
		
		
		} catch(Exception e){
			e.printStackTrace();
		}
		return 0;
    }


	
		
public void printSeperateTableForDisp2 (ArrayList tableResultAL) throws IOException {
	//System.out.println("Inside printSeperateTableForDisp2::::::::::----->");
//System.out.println("tableResultAL::::::::::----->"+tableResultAL);
//System.out.println("dispColIndx::::::::::----->"+dispColIndx);
		TableManipulator tm1 = new TableManipulator();
			int dispSize = dispColIndx.size();
				int dispVal = Integer.parseInt(dispColIndx.get(dispSize-1).toString());
 int 	tableResultALSize = tableResultAL.size();
 headerFunctionAL = new ArrayList();
 secondHeaderFunctionAL = new ArrayList();
 thirdHeaderFunctionAL = new ArrayList();

 hFCount = 1;
 headerFunctionAL.add(tableResultAL.get(0));
String currencyColumn = (String)dispCol[(dispCol.length-1)];
System.out.println("-------currencyColumn------------->>>>>>>"+currencyColumn);
 int currencyIndex  = 0;

 
		for(int x = 0 ; x < tableResultALSize ; x++){
					ArrayList tempAL = (ArrayList)tableResultAL.get(x);
							
							String elemntAtZ = tempAL.get(0).toString();
							//String elemntAtOne = tempAL.get(1).toString();
							//if(userLevel == 2){
									
								if(elemntAtZ.startsWith("C")){
								//printColumnNamesHeading(tempAL);
								 currencyIndex  = tempAL.indexOf(currencyColumn);

								
							}
							if(elemntAtZ.startsWith("D")){
								//printRows(tempAL);
								 currency =  tempAL.get(currencyIndex-1).toString();

								
							}

								if(elemntAtZ.equalsIgnoreCase("H1")){
								
								headerFunctionAL.add(tempAL);
							}
							if(elemntAtZ.equalsIgnoreCase("FN1")){
								headerFunctionAL.add(tempAL);
								
							}
							if(elemntAtZ.equalsIgnoreCase("H2")){
								
								secondHeaderFunctionAL.add(((String)tempAL.get(1)));
								String elemntAtOne = tempAL.get(1).toString();
								String[] colHeader = new String[4];
									colHeader = elemntAtOne.split(":");
								String headerName = (String)colHeader[0];
								String headerValue = (String)colHeader[1];

								if(secondHeaderSumAL.isEmpty()){
									secondHeaderSumAL.add(headerName);
									secondHeaderSumAL.add(headerValue);
									secondHeaderSumAL.add(new Double(0.00));
									secondHeaderSumALPosition = secondHeaderSumAL.indexOf(headerValue);
								}else if(secondHeaderSumAL.contains(headerValue)){
									secondHeaderSumALPosition = secondHeaderSumAL.indexOf(headerValue);

								}else{
									secondHeaderSumAL.add(headerValue);
									secondHeaderSumAL.add(new Double(0.00));
									secondHeaderSumALPosition = secondHeaderSumAL.indexOf(headerValue);
								}

								
							}
							if(elemntAtZ.equalsIgnoreCase("H3")){
								thirdHeaderFunctionAL.add(((String)tempAL.get(1)));
secondHeaderFunctionAL.add(((String)secondHeaderFunctionAL.get((secondHeaderFunctionAL.size()-1))));
								
							}
							if(elemntAtZ.equalsIgnoreCase("FN2")){
								
								secondHeaderFunctionAL.remove((secondHeaderFunctionAL.size()-1));
								secondHeaderSumAL = (ArrayList)tm1.sumOfSecondHeader(tempAL,secondHeaderSumALPosition,secondHeaderSumAL,dispVal);
								
							}
							/*}else if(userLevel == 3){
								//if(elemntAtZ.equalsIgnoreCase("H1") || elemntAtZ.equalsIgnoreCase("H2")){
								if(elemntAtZ.equalsIgnoreCase("H1")){
								headerFunctionAL.add(tempAL);
							}
							//if(elemntAtZ.equalsIgnoreCase("FN1") || elemntAtZ.equalsIgnoreCase("FN2")){
								if(elemntAtZ.equalsIgnoreCase("FN1")){
								headerFunctionAL.add(tempAL);
								
							}
							}*/
							
		}


//System.out.println("headerFunctionAL:::----->"+headerFunctionAL);


System.out.println("secondHeaderSumAL:::::WWWWWWW::::::::::------->>>>>>"+secondHeaderSumAL);
				printReorderImage();

				printSecondTableForDisp2(secondHeaderSumAL);

				for(int x = 0 ; x < tableResultALSize ; x++){

							ArrayList tempAL = (ArrayList)tableResultAL.get(x);
							
							String elemntAtZ = tempAL.get(0).toString();
							//String elemntAtOne = tempAL.get(1).toString();
							
							if(elemntAtZ.startsWith("C")){
								//printColumnNamesHeading(tempAL);
								 //currencyIndex  = tempAL.indexOf(currencyColumn);

								
							}
							if(elemntAtZ.startsWith("D")){
								//printRows(tempAL);
								 //currency =  tempAL.get(currencyIndex-1).toString();

								
							}
							if(elemntAtZ.startsWith("X")){
								
								//printXRow(tempAL);
								
							}
							if(elemntAtZ.startsWith("H")){
								if(elemntAtZ.equalsIgnoreCase("H1")){
									printSeperateTableTopHeaderForDisp2();
								
								}else if(elemntAtZ.equalsIgnoreCase("H2")){

								}else if(elemntAtZ.equalsIgnoreCase("H3")){
									
								
								}else{
								printTopHeaderForDisp2(tempAL);
								}
							}
							if(elemntAtZ.startsWith("F")){
								if(elemntAtZ.equalsIgnoreCase("FN1")){
								}else if(elemntAtZ.equalsIgnoreCase("FN2")){

									//sumOfSecondHeader(tempAL,secondHeaderSumALPosition);
								}else{
								printOperationsForDisp2(tempAL);
								}
								
							}
							
						}

		}

	


	
		
public void printSeperateTableTop (ArrayList tableResultAL) throws IOException {
	
 int 	tableResultALSize = tableResultAL.size();
 headerFunctionAL = new ArrayList();
 hFCount = 1;
 headerFunctionAL.add(tableResultAL.get(0));
		for(int x = 1 ; x < tableResultALSize ; x++){
					ArrayList tempAL = (ArrayList)tableResultAL.get(x);
							
							String elemntAtZ = tempAL.get(0).toString();
							
							//if(userLevel == 2){
								if(elemntAtZ.equalsIgnoreCase("H1")){
								
								headerFunctionAL.add(tempAL);
							}
							if(elemntAtZ.equalsIgnoreCase("FN1")){
								headerFunctionAL.add(tempAL);
								
							}
							/*}else if(userLevel == 3){
								//if(elemntAtZ.equalsIgnoreCase("H1") || elemntAtZ.equalsIgnoreCase("H2")){
								if(elemntAtZ.equalsIgnoreCase("H1")){
								headerFunctionAL.add(tempAL);
							}
							//if(elemntAtZ.equalsIgnoreCase("FN1") || elemntAtZ.equalsIgnoreCase("FN2")){
								if(elemntAtZ.equalsIgnoreCase("FN1")){
								headerFunctionAL.add(tempAL);
								
							}
							}*/
							
		}


//System.out.println("headerFunctionAL:::----->"+headerFunctionAL);



				printReorderImage();
				for(int x = 0 ; x < tableResultALSize ; x++){

							ArrayList tempAL = (ArrayList)tableResultAL.get(x);
							
							String elemntAtZ = tempAL.get(0).toString();
							
							if(elemntAtZ.startsWith("C")){
								//printColumnNamesHeading(tempAL);
								
							}
							if(elemntAtZ.startsWith("D")){
								printRows(tempAL);
								
							}
							if(elemntAtZ.startsWith("X")){
								
								printXRow(tempAL);
								
							}
							if(elemntAtZ.startsWith("H")){
								if(elemntAtZ.equalsIgnoreCase("H1")){
									printSeperateTableTopHeader();
								
								}else{
								printTopHeader(tempAL);
								}
							}
							if(elemntAtZ.startsWith("F")){
								if(elemntAtZ.equalsIgnoreCase("FN1")){
								}else{
								printOperations(tempAL);
								}
								
							}
							
						}

		}

		public void printSeperateTableBottom (ArrayList tableResultAL) throws IOException {
				int 	tableResultALSize = tableResultAL.size();
				printReorderImage();
				for(int x = 0 ; x < tableResultALSize ; x++){

							ArrayList tempAL = (ArrayList)tableResultAL.get(x);
							
							String elemntAtZ = tempAL.get(0).toString();
							
							if(elemntAtZ.startsWith("C")){
								printColumnNamesHeading(tempAL);
								
							}
							if(elemntAtZ.startsWith("D")){
								printRows(tempAL);
								
							}
							if(elemntAtZ.startsWith("H")){
								
								printHeader(tempAL);
							}
							if(elemntAtZ.startsWith("F")){
								
								printOperations(tempAL);
								
							}
							if(elemntAtZ.startsWith("X")){
								
								printXRow(tempAL);
								
							}
							
						}
		}


public void printSeperateTableTopHeaderForDisp2() throws IOException {
int	pCount = 2;
ArrayList hFValAL = new ArrayList();
ArrayList colAL = new ArrayList();
colAL =(ArrayList)headerFunctionAL.get(0);

System.out.println("headerFunctionAL:::----->"+headerFunctionAL);
if(hFCount != 1){
out.append("</table>");
//dispColIndx.clear();
}
out.append("<table name=\"TableTopHeader\" id=\"TableTopHeader\"  width=\"100%\" border=\"0\" class=\"layoutRepTable\">");
	
out.append("<tr>");
for(int x = hFCount ; x < headerFunctionAL.size() ; x++){
		hFValAL = (ArrayList)headerFunctionAL.get(x);
if(pCount != 0){
	String hIndx = (String)hFValAL.get(0);
			if(hIndx.startsWith("H")){
				String hVal = (String)hFValAL.get(1);
				String[] colHeader = new String[4];
				colHeader = hVal.split(":");
					for (int z = 0;z<colHeader.length;z++) {
						for (int q = 0;q<constDBCol.length;q++) {
								
									if(constDBCol[q].equalsIgnoreCase(colHeader[z])){
									
										colHeader[z]=constDsplCol[q];
										
								}
							}
							
						
						}
				
				out.append("<tr>");
				out.append ("<td class=\"H1lable\"><B><u>"+colHeader[1]+"</u></B></td>");
				out.append("</tr>");
				

			}

		if(hIndx.startsWith("F")){


	for (int q = 1;q<hFValAL.size();q++) {
				ArrayList fValAL = new ArrayList();
				
				fValAL = (ArrayList)hFValAL.get(q);
				out.append("<tr>");
			for (int i = 0;i< fValAL.size();i++) {
				String fStr = fValAL.get(i).toString();
				
				if(!fStr.equalsIgnoreCase("Null")){
								if(i==0){
									//out.append ("<td><B>"+fStr+" of -</B>");
								}else{
								String colSuperName =(String)formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + (constDsplCol[i-1].toString()));								
								out.append ("<td>"+colSuperName+" : "+fStr+"</td>");
							}
				}
			}
			out.append("</td>");
			out.append("</tr>");
			
		}
		
	
	}
	
	pCount--;

	
}else{ 
	hFCount = x;
	
	break;
	}
	//hFCount = x-1;

}
out.append("</tr>");
out.append("</table>");
printColumnTableForDisp2(colAL);
}



public void printSecondTableForDisp2 (ArrayList secondHeaderSumAL) throws IOException {

	String strValue = new String("Value");
	String displayColumnName = new String();

displayColumnName = secondHeaderSumAL.get(0).toString();
	out.append("<table name=\"SeperateTable\" id=\"SeperateTable\"  width=\"50%\" border=\"1\" class=\"columnheader\" >");
	
	out.append("<tr>");
	//out.append ("<td class=\"tableheader\"></td>");
	for (int q = 0;q<dispCol.length;q++) {


			
				if(dispCol[q].equalsIgnoreCase(displayColumnName)){
					
				out.append ("<td class=\"tableheader\">"+testCol[(colHeaderAL.indexOf(displayColumnName)-1)]+"</td>");
				break;
			}
	}
					//out.append ("<td class=\"tableheader\">"+secondHeaderSumAL.get(0)+"</td>");
				out.append ("<td class=\"tableheader\">"+strValue+"</td>");
		
	out.append("</tr>");
	





	boolean rFlag = true;

	boolean myFlag = true;
	//out.append ("<td class=\"tableData\"></td>");
	for (int i = 1;i<secondHeaderSumAL.size();i++) {
		
			if(myFlag){
className="tableData";
}else{
className="tableData2";
}
				if(rFlag){
				
				out.append ("<tr>");
				System.out.println("Class Name of the Object:Inside IF::----->"+secondHeaderSumAL.get(i).getClass());
					if((secondHeaderSumAL.get(i).getClass().toString()).equalsIgnoreCase("class java.lang.String")){
					
				out.append ("<td class="+className+">"+secondHeaderSumAL.get(i)+"</td>");
					}else {
				out.append ("<td class="+className+" align=\"right\">"+secondHeaderSumAL.get(i)+"</td>");
					}
					rFlag = false;
			
			
		
	
	//out.append ("</tr></tr>");
}else{
	System.out.println("Class Name of the Object:::----->"+secondHeaderSumAL.get(i).getClass());
		
						if((secondHeaderSumAL.get(i).getClass().toString()).equalsIgnoreCase("class java.lang.String")){
						String tempStrr1 = currency+" "+(String)secondHeaderSumAL.get(i);
					out.append ("<td class="+className+">"+tempStrr1+"</td>");
						}else {
							String tempStrr2 =  currency+" "+(String)secondHeaderSumAL.get(i);
					out.append ("<td class="+className+" align=\"right\">"+tempStrr2+"</td>");
						}
				
				
			
		rFlag = true;
		out.append ("</tr>");
		if(myFlag){
myFlag = false;
}else{
myFlag = true;
}

}

			


}
out.append("</table>");

}


public void printColumnTableForDisp2 (ArrayList xcolNames) throws IOException {

	int siz = xcolNames.size();
	

	out.append("<table name="+name+" id="+name+"  width=\"100%\" border=\"0\" class=\"columnheader\" >");
	
	out.append("<tr>");
	//out.append ("<td class=\"tableheader\"></td>");
	for (int q = 0;q<dispCol.length;q++) {


			for (int i = 0;i< siz;i++) {
				if(dispCol[q].equalsIgnoreCase(xcolNames.get(i).toString())){
					//dispColIndx.add(new Integer(i));
					//out.append ("<td class=\"tableheader\">"+dispCol[q]+"</td>");
				out.append ("<td class=\"tableheader\">"+testCol[i-1]+"</td>");
			}
		}
		
	
	}
	out.append("</tr>");
	
}



public void printTopHeaderForDisp2(ArrayList tempAL) throws IOException{
		
	
	
	//className="layoutMaxTable";
	//className="tableTitle";
	String hName = (String)tempAL.get(0);
	
	if(hName.equalsIgnoreCase("H1")){
		className="H1lable";
	}else if(hName.equalsIgnoreCase("H2")){
		className="H2lable";
	}else if(hName.equalsIgnoreCase("H3")){
		className="H3lable";
	}


		String hVal = (String)tempAL.get(1);
	String[] colHeader = new String[4];
	colHeader = hVal.split(":");
		for (int z = 0;z<colHeader.length;z++) {
			for (int q = 0;q<constDBCol.length;q++) {
					
						if(constDBCol[q].equalsIgnoreCase(colHeader[z])){
						
							colHeader[z]=constDsplCol[q];
							
					}
				}
				
			
			}
out.append ("<tr>");
	out.append ("<td class="+className+"><B>"+colHeader[1]+"</B></td>");

	
}



public void printOperationsForDisp2 (ArrayList xOpValues) throws IOException{
	int len = xOpValues.size();
		
		

	//className="layoutMaxTable";
	String fnName = (String)xOpValues.get(0);
	//className="layoutMaxTable";
	if(fnName.equalsIgnoreCase("FN1")){
		className="f3Table";
	}else if(fnName.equalsIgnoreCase("FN2")){
		className="f1Table";
	}else if(fnName.equalsIgnoreCase("FN3")){
		className="f2Table";
	}	
	

	boolean markFlag = false;
	for(int x = 1; x < len ; x++){
			ArrayList opArray = (ArrayList)xOpValues.get(x);

			for(int z = 0; z < dispColIndx.size() ; z++){
				
				for(int d = 1; d < opArray.size() ; d++){
						String colOps = opArray.get(d).toString();
						if(d == Integer.parseInt(dispColIndx.get(z).toString())){
							
							if(colOps.equalsIgnoreCase("Null")){
								
								
							}else{
								markFlag = true;
							}
						}
				}
				
			}



if(markFlag){

			//out.append ("<tr>");
				if(fnName.equalsIgnoreCase("FN3")){
								String tempStr = (String)(secondHeaderFunctionAL.get(f3Count).toString());
								String[] tempStrHeader = new String[4];
								tempStrHeader = tempStr.split(":");
								//String tempStr2 = tempStrHeader[1]+" "+colOps;
								out.append ("<td class="+className+" align=\"right\">"+tempStrHeader[1]+"</td>");
								//f3Flag = false;
								f3Count++;
								} 
			//out.append ("<td class="+className+" align=\"left\">"+opArray.get(0)+"</td>");
			//out.append ("<td class="+className+"></td>");
		//	System.out.println("secondHeaderFunctionAL::Inside method:----->"+secondHeaderFunctionAL);
		//	System.out.println("dispColIndx::Inside method:----->"+dispColIndx);
		//	System.out.println("opArray:::----->"+opArray);
		//				System.out.println("thirdHeaderFunctionAL::Inside method:----->"+thirdHeaderFunctionAL);
			boolean f2Flag = true;
			for(int z = 0; z < dispColIndx.size() ; z++){
				
				for(int d = 1; d < opArray.size() ; d++){
						String colOps = opArray.get(d).toString();


						if(d == Integer.parseInt(dispColIndx.get(z).toString())){
							
							
							if(colOps.equalsIgnoreCase("Null")){
								//out.append ("<td class="+className+"></td>");
							}else{
								
								if(fnName.equalsIgnoreCase("FN3") && f2Flag){
									//if(fnName.equalsIgnoreCase("FN3")){
								String temp3Str = (String)(thirdHeaderFunctionAL.get(f2Count).toString());
								String[] temp3StrHeader = new String[4];
								temp3StrHeader = temp3Str.split(":");
								String temp3Str2 = temp3StrHeader[1]+" "+colOps;
								out.append ("<td class="+className+" align=\"right\">"+temp3Str2+"</td>");
								f2Flag = false;
								f2Count++;
								} else {
									String tempStrr = currency+" "+colOps;
								out.append ("<td class="+className+" align=\"right\">"+tempStrr+"</td>");

								}
							}
						}
				}
				
			}
			
}
			
		}
		out.append ("<tr><td></td></tr>");
			
}



public void printSeperateTableTopHeader () throws IOException {
int	pCount = 2;
ArrayList hFValAL = new ArrayList();
ArrayList colAL = new ArrayList();
colAL =(ArrayList)headerFunctionAL.get(0);

/*
if(userLevel == 2){
	pCount = 2;
}

if(userLevel == 3){
	//pCount = 4;
	pCount = 2;
}
if(userLevel == 1){
	//pCount = 4;
	pCount = 2;
}
*/
//System.out.println("hFCount:::>"+hFCount);
if(hFCount != 1){
out.append("</table>");
out.append("</div>");
dispColIndx.clear();
}
out.append("<div border=\"1\" class=\"layoutMaxTable2\">");
out.append("<table name=\"TableTopHeader\" id=\"TableTopHeader\"  width=\"100%\" border=\"0\" class=\"layoutRepTable\">");
	
out.append("<tr>");
for(int x = hFCount ; x < headerFunctionAL.size() ; x++){
		hFValAL = (ArrayList)headerFunctionAL.get(x);
if(pCount != 0){
	String hIndx = (String)hFValAL.get(0);
			if(hIndx.startsWith("H")){
				String hVal = (String)hFValAL.get(1);
				String[] colHeader = new String[4];
				colHeader = hVal.split(":");
					for (int z = 0;z<colHeader.length;z++) {
						for (int q = 0;q<constDBCol.length;q++) {
								
									if(constDBCol[q].equalsIgnoreCase(colHeader[z])){
									
										colHeader[z]=constDsplCol[q];
										
								}
							}
							
						
						}
				
				out.append("<tr>");
				out.append ("<td class=\"H1lable\"><B>"+colHeader[0]+": "+colHeader[1]+"</B></td>");
				out.append("</tr>");
				

			}

		if(hIndx.startsWith("F")){


	for (int q = 1;q<hFValAL.size();q++) {
				ArrayList fValAL = new ArrayList();
				String functionName = new String();
				fValAL = (ArrayList)hFValAL.get(q);
				out.append("<tr>");
			for (int i = 0;i< fValAL.size();i++) {
				String fStr = fValAL.get(i).toString();
				
		        if(!fStr.equalsIgnoreCase("Null")){
								if(i==0){
									//out.append ("<td><B>"+fStr+" of -</B>");
										functionName = fStr.toUpperCase();
								}else{
																
								out.append ("<td class=\"H1text\">"+functionName+" "+constDsplCol[i-1]+" : "+fStr+"</td>");
							}
				}
			}
			out.append("</td>");
			out.append("</tr>");
			
		}
		
	
	}
	
	pCount--;

	
}else{ 
	hFCount = x;
	
	break;
	}
	//hFCount = x-1;

}
out.append("</tr>");
out.append("</table>");
printColumnTable(colAL);
}

public void printColumnTable (ArrayList xcolNames) throws IOException {

	int siz = xcolNames.size();
	

	out.append("<table name="+name+" id="+name+"  width=\"100%\" border=\"0\" class=\"columnheader\" >");
	
	out.append("<tr>");
	out.append ("<td class=\"tableheader\"></td>");
	for (int q = 0;q<dispCol.length;q++) {


			for (int i = 1;i< siz;i++) {
				if(dispCol[q].equalsIgnoreCase(xcolNames.get(i).toString())){
					dispColIndx.add(new Integer(i));
					
				out.append ("<td class=\"tableheader\">"+testCol[q]+"</td>");
			}
		}
		
	
	}
	out.append("</tr>");
	
}


public void printReorderImage () throws IOException {
	out.append("<table width=\"100%\" class=\"grouptable\"><tr><td class=\"tableTitle\"></td><td align=\"right\" class=\"tablechecklink\" colspan=\"6\">");
	out.append("<a href=\"#\" onClick=\"popupwindow=window.open('popup.jsp?dis=false&tablename="+name+"&colValue="+constdisplayCol+"','popupwindow','resizable=no,width=500,height=150,status=yes')\"><img src=\"../images/reorder.gif\" border=\"0\" alt=\"Reorder Columns\"></a>&nbsp;&nbsp;");
	out.append("</td>");
	out.append("</table>");
}


public void printColumnNamesHeading (ArrayList xcolNames) throws IOException {
	int siz = xcolNames.size();
	

	out.append("<table name="+name+" id="+name+"  width=\"100%\" border=\"0\" class=\"columnheader\">");
	
	out.append("<tr>");
	out.append ("<td class=\"tableheader\"></td>");
	for (int q = 0;q<dispCol.length;q++) {


			for (int i = 1;i< siz;i++) {
				if(dispCol[q].equalsIgnoreCase(xcolNames.get(i).toString())){
					dispColIndx.add(new Integer(i));
				out.append ("<td class=\"tableheader\">"+testCol[q]+"</td>");
			}
		}
		
	
	}
	out.append("</tr>");
	
}


public void printTopHeader(ArrayList tempAL) throws IOException{
		
	
	
	//className="layoutMaxTable";
	//className="tableTitle";
	String hName = (String)tempAL.get(0);
	
	if(hName.equalsIgnoreCase("H1")){
		className="H1lable";		
	}else if(hName.equalsIgnoreCase("H2")){
		className="H2lable";
	}else if(hName.equalsIgnoreCase("H3")){
		className="H3lable";
	}


		String hVal = (String)tempAL.get(1);
	String[] colHeader = new String[4];
	colHeader = hVal.split(":");
		for (int z = 0;z<colHeader.length;z++) {
			for (int q = 0;q<constDBCol.length;q++) {
					
						if(constDBCol[q].equalsIgnoreCase(colHeader[z])){
						
							colHeader[z]=constDsplCol[q];
							
					}
				}
				
			
			}
out.append ("<tr>");
	out.append ("<td class="+className+"><B>"+colHeader[0]+": "+colHeader[1]+"</B></td>");

	
}

public void printHeader(ArrayList tempAL) throws IOException{
		
	
	
	//className="layoutMaxTable";
	//className="tableTitle";
	String hName = (String)tempAL.get(0);
	
	if(hName.equalsIgnoreCase("H1")){
		className="H1title";
		//out.append ("<div border=\"1\" class=\"layoutMaxTable2\">");
	}else if(hName.equalsIgnoreCase("H2")){
		className="H2title";
	}else if(hName.equalsIgnoreCase("H3")){
		className="H3title";
	}


		String hVal = (String)tempAL.get(1);
	String[] colHeader = new String[4];
	colHeader = hVal.split(":");
		for (int z = 0;z<colHeader.length;z++) {
			for (int q = 0;q<constDBCol.length;q++) {
					
						if(constDBCol[q].equalsIgnoreCase(colHeader[z])){
						
							colHeader[z]=constDsplCol[q];
							
					}
				}
				
			
			}

	out.append ("<tr>");
	out.append ("<td width=\"100%\" colspan=\"10\">");
	out.append ("<table width=\"100%\" class=\"grouptable\"><tr>");
	
	out.append ("<td class="+className+">"+colHeader[0]+": "+colHeader[1]+"</td>");
	//out.append ("<td><B><U>"+colHeader[0]+": "+colHeader[1]+"</U></B></td>");
	out.append ("</tr>");
	out.append ("</table>");
	out.append ("</td></tr>");
	
	
}


public void printRows (ArrayList xcolValues) throws IOException{
	int len = xcolValues.size();
	String dataValue = new String();
if(rowFlag){
	out.append ("<tr>");
	out.append ("<td class=\"tableData\"></td>");
	for (int q = 0;q<dispColIndx.size();q++) {
			for (int i = 1;i< len;i++){
				int dCIndx =Integer.parseInt(dispColIndx.get(q).toString());
				System.out.println("dCIndx-------1111111---"+dCIndx);
				if(i == dCIndx)	{
					try{
							dataValue = xcolValues.get(i).toString();
							}catch(Exception e){
							System.out.println("Exception---"+e);
							dataValue = "";
							}
						//System.out.println("dataValue-------1111111---"+dataValue);
					//if((xcolValues.get(i).getClass().toString()).equalsIgnoreCase("class java.lang.String")){
						if((dataValue.getClass().toString()).equalsIgnoreCase("class java.lang.String")){
					//System.out.println("Class Name of the Object:::----->"+xcolValues.get(i).getClass());
					
				out.append ("<td class=\"tableData\">"+dataValue+"</td>");
					}else {
						
						
				out.append ("<td class=\"tableData\" align=\"right\">"+dataValue+"</td>");
					}
			}
			
		}
		
	}
	rowFlag = false;
	out.append ("</tr></tr>");
}else{
	out.append ("<tr>");
		out.append ("<td class=\"tableData2\"></td>");
		for (int q = 0;q<dispColIndx.size();q++) {
				for (int i = 1;i< len;i++){
					int dCIndx =Integer.parseInt(dispColIndx.get(q).toString());
					System.out.println("dCIndx-------22222---"+dCIndx);
					if(i == dCIndx)	{
						try{
						dataValue = xcolValues.get(i).toString();
							}catch(Exception e){
							System.out.println("Exception---"+e);
							dataValue = "";
							}
						if((dataValue.getClass().toString()).equalsIgnoreCase("class java.lang.String")){
						//System.out.println("Class Name of the Object:::----->"+xcolValues.get(i).getClass());
						
					out.append ("<td class=\"tableData2\">"+dataValue+"</td>");
						}else {
							
						
					out.append ("<td class=\"tableData2\" align=\"right\">"+dataValue+"</td>");
						}
				}
				
			}
			
		}
		rowFlag = true;
		out.append ("</tr></tr>");

}

}




public void printXRow (ArrayList xOpValues) throws IOException{
	int len = xOpValues.size();
	//className="layoutMaxTable";
	String fnName = (String)xOpValues.get(0);
	
	 if(fnName.equalsIgnoreCase("X")){
		className="fXTable";
	}	
	

	boolean markFlag = false;
	for(int x = 1; x < len ; x++){
			ArrayList opArray = (ArrayList)xOpValues.get(x);

			for(int z = 0; z < dispColIndx.size() ; z++){
				
				for(int d = 1; d < opArray.size() ; d++){
						String colOps = opArray.get(d).toString();
						if(d == Integer.parseInt(dispColIndx.get(z).toString())){
							
							if(colOps.equalsIgnoreCase("Null")){
								
								
							}else{
								markFlag = true;
							}
						}
				}
				
			}



if(markFlag){
			out.append ("<tr><td></td></tr>");
			out.append ("<tr>");
			//out.append ("<table>");
				
			out.append ("<td class="+className+" align=\"left\">"+opArray.get(0)+"</td>");
			//out.append ("<td class="+className+"></td>");
			for(int z = 0; z < dispColIndx.size() ; z++){
				
				for(int d = 1; d < opArray.size() ; d++){
						String colOps = opArray.get(d).toString();


						if(d == Integer.parseInt(dispColIndx.get(z).toString())){
							
							if(colOps.equalsIgnoreCase("Null")){
								out.append ("<td class="+className+"></td>");
							}else{
								out.append ("<td class="+className+" align=\"right\">"+colOps+"</td>");
							}
						}
				}
				
			}
			//out.append ("</table>");
			
}
			
		}
		out.append ("</tr><tr><td></td></tr>");
}


public void printOperations (ArrayList xOpValues) throws IOException{
	int len = xOpValues.size();
		
		level			= iLevel.intValue();

	//className="layoutMaxTable";
	String fnName = (String)xOpValues.get(0);
	//className="layoutMaxTable";
	if(fnName.equalsIgnoreCase("FN1")){
		className="f3Table";
	}else if(fnName.equalsIgnoreCase("FN2")){
		className="f1Table";
	}else if(fnName.equalsIgnoreCase("FN3")){		
		className="f2Table";
	}	
	

	boolean markFlag = false;
	for(int x = 1; x < len ; x++){
			ArrayList opArray = (ArrayList)xOpValues.get(x);

			for(int z = 0; z < dispColIndx.size() ; z++){
				
				for(int d = 1; d < opArray.size() ; d++){
						String colOps = opArray.get(d).toString();
						if(d == Integer.parseInt(dispColIndx.get(z).toString())){
							
							if(colOps.equalsIgnoreCase("Null")){
								
								
							}else{
								markFlag = true;
							}
						}
				}
				
			}



if(markFlag){

			out.append ("<tr>");
				
			out.append ("<td class="+className+" align=\"left\">"+opArray.get(0)+"</td>");
			//out.append ("<td class="+className+"></td>");
			for(int z = 0; z < dispColIndx.size() ; z++){
				
				for(int d = 1; d < opArray.size() ; d++){
						String colOps = opArray.get(d).toString();


						if(d == Integer.parseInt(dispColIndx.get(z).toString())){
							
							if(colOps.equalsIgnoreCase("Null")){
								out.append ("<td class="+className+"></td>");
							}else{
								out.append ("<td class="+className+" align=\"right\">"+colOps+"</td>");
							}
						}
				}
				
			}
			
}
			
		}
		out.append ("</tr>");
		if(fnName.equalsIgnoreCase("FN1")){
		
//out.append ("</div>");
		}
		out.append ("<tr><td></td></tr>");
			
}

 public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


public String getServicename()
    {
        return servicename;
    }

    public void setServicename(String servicename)
    {
        this.servicename = servicename;
    }


	 public String getFormname()
    {
        return formname;
    }

    public void setFormname(String formname)
    {
        this.formname = formname;
    }
	
	
    private ResourceBundle formRB;
    private ResourceBundle appRB;
	//private ResourceBundle  statRB;
	private String name;
	private String servicename;
    private String formname;
	private DataInterface formObj;
	private ArrayList tableHeader;
	
}