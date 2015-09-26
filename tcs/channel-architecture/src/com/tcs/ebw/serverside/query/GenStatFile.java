/**
 * 
 */
package com.tcs.ebw.serverside.query;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.FileUtil;  
import com.tcs.ebw.common.util.PropertyFileReader;

/**
 * @author TCS
 *
 */
public class GenStatFile {	

	/** Variabel used to store the propertyfile name 
	 *  where in the DB Connection related properties
	 *  are defined.
    */
	private static  final String DB = "ebw";
		
	/**
	 * ResourceBundle object to hold the properties configured
	 */
	private static ResourceBundle resBundle = ResourceBundle.getBundle(DB);
	
	/**
	 * Varibale used to store the Home location of the Java class  
	 * to be generated
	 */
	private static final   String EBWStatementHOME=resBundle.getString("EBWStatementHOME");;
	
	/**
	 * Varibale used to store the Home location of the application 
	 * where in all the package are mounted
	 */	
	private  static final String HOME=EBWStatementHOME+"src\\com\\tcs\\ebw\\serverside\\query\\EBWStatement.java";
	/**
	 * 
	 */
	public GenStatFile() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			try {
		
			
			// The path of the statement.properties file is from properties file as done in QueryExecutor
					
			printHeader();
			printHashmap();	
			printFooter();
			System.out.println( "out:"+sbOutput.toString());
			FileUtil.writeToFile(HOME ,sbOutput.toString(), false);
		} catch (FileNotFoundException e) {
			System.out.println("Generation Failed : File Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Generation Failed : IO Exception");
			e.printStackTrace();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Generation Failed : Exception");
			e.printStackTrace();
		} 
		
	}
	
	private static void printHeader()
	{
		sbOutput.append(head);
		sbOutput.append(function_start);
	}
	
	private static void printFooter()
	{
		sbOutput.append(function_end);
		sbOutput.append(foot);
	}
	
	private static void printHashmap()
	{
	
		EBWLogger.trace("GenStatFile",	"Starting method printHashmap");
		resBundle = ResourceBundle.getBundle("Statement");
		if (resBundle != null)
		{
			enum1 = resBundle.getKeys();
			while (enum1.hasMoreElements()) {
				key = (String) enum1.nextElement();
				sbOutput.append("\t\t\t" + "map.put(\""  + key + "\",\"" + resBundle.getString(key) + "\");");
				sbOutput.append("\r\n");
			}						
		}else
			System.out.println("Could not read the resource bundle");
		
		//Added Extra Operations
		HashMap linkedHash = PropertyFileReader.getProperties("Statement");
		Set s = linkedHash.entrySet();
		Iterator k = s.iterator();
		while (k.hasNext()) {
			Map.Entry obj = (Map.Entry) k.next();
			key1 = obj.getKey();
			value = obj.getValue();
			if (key1.toString().indexOf(".Query") > -1) {
				generatedQueryKey = key1.toString();
				arrQueryKey.add(generatedQueryKey);
				generatedQuery = value.toString();
				arrQuery.add(generatedQuery);
			} else if (key1.toString().indexOf(".SortKeys") > -1 || key1.toString().indexOf(".PgnUniqueKeys") > -1) {
				generatedKey = key1.toString();
				generatedValue = value.toString();
				arr.add(generatedKey);
				map1.put(generatedKey, generatedValue);
			}
			if(key1.toString().indexOf("_page.Query") > -1) {
				pgnQuery.add(generatedQuery);
			}
		}		
		
		Iterator queryIterKey = arrQueryKey.iterator();
		int countQueryKey = 0;
		String[] queryStrArrKey = new String[50];
		while (queryIterKey.hasNext()) {
			queryStrArrKey[countQueryKey] = queryIterKey.next().toString();
			countQueryKey++;
		}
		
		
		Iterator queryIter = arrQuery.iterator();
		int countQuery = 0;
		String[] queryStrArr = new String[50];
		while (queryIter.hasNext()) {
			queryStrArr[countQuery] = queryIter.next().toString();
			countQuery++;
		}

		//for pgn
		Iterator pgnIter = pgnQuery.iterator();
		int countQueries = 0;
		String[] queryStrArray = new String[50];
		while (pgnIter.hasNext()) {
			queryStrArray[countQueries] = pgnIter.next().toString();
			countQueries++;
		}
		
		Iterator p = arr.iterator();
		String generateValue1 = "";
		String generateValue2 = "";
		int count = 0;
		String[] col = new String[20];
		while (p.hasNext()) {
			col[count] = p.next().toString();
			//System.out.println(" queries for which keys to be cald :"+col[count]);
			count++;
		}
		
		//Start of the loop based on the Query
		
		
			String regStr = "";	
			//Start of for loop Based on RestartColKey & PgnUniqueKeys
			for (int q = 0; q < count; q++) {
				String queryName =  col[q].substring(0,col[q].indexOf("."))+"_page.Query";
				String t1 = map1.get(col[q]).toString();
				//Start  of RestartColKey
				if ((col[q].toString()).indexOf(".SortKeys") > -1 || (col[q].toString()).indexOf(".PgnUniqueKeys") > -1) {
					String[] hashSplit = t1.split("#");
					String pgnVal = null;
					pgnVal = calPgnVal(hashSplit,col[q],resBundle.getString(queryName));
					if (pgnVal !=null && !pgnVal.equals(""))						
						sbOutput.append("\t\t\t" + "map.put(\""  + col[q] + "Id" + "\",\"" + pgnVal + "\");");
					
					/*ArrayList regList = new ArrayList();
					if (t1.indexOf("#") > -1) {
						String[] hashSplit = t1.split("#");
						// Start of for loop
						for (int i = 0; i < hashSplit.length; i++) {						
							String colSplitValue = hashSplit[i];
							String[] colonSplit = colSplitValue.split(":");						
							regStr=multiForLoop(regList,col[q],queryStrArr[queryVal],colonSplit);	
							System.out.println("regStr :"+regStr);
						}// End of for loop							
					} else {						
						generateValue1 = (String) map1.get(col[q]);
						String[] colonSplit = generateValue1.split(":");					
							regStr=multiForLoop(regList,col[q],queryStrArr[queryVal],colonSplit);					
					}	
					*/
				}		
				//End of RestartColKey

				//Start of PgnUniqueKeys
				/*if (col[q].toString().indexOf(".PgnUniqueKeys") > -1) {							
					String[] hashSplit = t2.split("#");
					String pgnVal = null;
					System.out.println("query before methd :"+resBundle.getString(queryName)); 
					pgnVal = calPgnVal(hashSplit,col[q],resBundle.getString(queryName));
					if (pgnVal !=null && !pgnVal.equals(""))						
						sbOutput.append("\t\t\t" + "map.put(\""  + col[q] + "Id" + "\",\"" + pgnVal + "\");");
					
					/*ArrayList regList = new ArrayList();
					if (t2.indexOf("#") > -1) {
						String[] hashSplit = t2.split("#");
						// Start of for loop
						for (int i = 0; i < hashSplit.length; i++) {						
							String colSplitValue = hashSplit[i];
							String[] colonSplit = colSplitValue.split(":");						
							regStr=multiForLoop(regList,col[q],queryStrArr[queryVal],colonSplit);							
						}// End of for loops							
					} else {					
						generateValue2 = (String) map1.get(col[q]);
						String[] colonSplit = generateValue2.split(":");						
						regStr=multiForLoop(regList,col[q],queryStrArr[queryVal],colonSplit);					
					}
					
					
				
				}*///End of PgnUniqueKeys	
				/*if (!regStr.equals(""))						
					sbOutput.append("\t\t\t" + "map.put(\""  + col[q] + "Id" + "\",\"" + regStr + "\");");*/
					//sbOutput.append("\r\n");
			}//End of for loop				
		//End of outer for loop
		
		EBWLogger.trace("GenStatFile",	"Exiting from method printHashmap");

	}
	
	/******************************************/
	private static String calPgnVal(String[] hashSplit,String key,String query){
		String result = null;
		result = "";
		String strQuery = new String();
		strQuery = query;
		String colName = null;
		strQuery = strQuery.substring((strQuery.toUpperCase()).indexOf("SELECT")+("SELECT").length(),(strQuery.toUpperCase()).indexOf(" FROM"));
		while(strQuery!=null && strQuery.indexOf("  ")>-1){
			strQuery = strQuery.replaceAll("  "," ");
		}
		System.out.println(":strQuery is :"+strQuery);
		
		String[] colArr = strQuery.split(",");
		ArrayList li = new ArrayList();
		
		/*
		 * the position of the sort and pgn keys are calculated, by spliiting the substring of the query (Starts from select and ends with where) by "," separeator.
		 * In case if the "," separated contains a stringlike this "TotalUninsQty-coalesce(TotalInstQty" and "0) as TotalUninsQty"
		 * It will be considered as two headers . but it is wrong. the header is the second string which comes after "as".
		 * The following code performs the above said calculations to form the header arraylist
		 *  
		 */
		for(int m=0;m<colArr.length;m++){
			String arrElement = colArr[m].trim();
			if(arrElement!=null && arrElement.length()>0 ){
				if(!(arrElement.indexOf("-")>-1) || !(arrElement.indexOf("(")>-1)){
					if(arrElement.indexOf(" ")>-1){
						String[] headerStrArr = arrElement.split(" ");
						for(int k=0;k<headerStrArr.length;k++){
							if(headerStrArr[k]!=null && headerStrArr[k].equalsIgnoreCase("as")){
								String colNm = headerStrArr[k+1]; 
								li.add(colNm);
							}	
						}
					}else
						li.add(arrElement);
				}
			}
		}
		System.out.println("li.size :"+li.size());
		
		for(int i=0;i<hashSplit.length;i++){
			String colSplitValue = hashSplit[i];
			String[] colonSplit = colSplitValue.split(":");	
			if (colonSplit[0].indexOf(" ") > -1) 
				colName = colonSplit[0].substring(0, colonSplit[0].indexOf(" "));
			else
				colName = colonSplit[0];
		
			for(int j=0;j<li.size();j++){
				String colInQuery = (String)li.get(j);
				if(colName.equalsIgnoreCase(colInQuery)){
					result = result+j+":";
					break;
				}
			}
		}
		/*if(result!=null && result.length()>0 && result.indexOf(";")>0)
			result = result.substring(0,result.lastIndexOf(":"));*/
		return result;
	}
	
	/******************************************/
	
	
	private static String multiForLoop(ArrayList regList,String colValue,String query,String[] colonSplit){
		String regStr="";
		int result=-1;
		String colName = null;		
			if (colonSplit[0].indexOf(" ") > -1) {
				colName = colonSplit[0].substring(0, colonSplit[0].indexOf(" "));
			} else {
				colName = colonSplit[0];
			}
			if (query.toUpperCase().indexOf(colName.toUpperCase() + ",") != -1) {			
				int indexCol = query.indexOf(colName);
				HashMap map = new HashMap();
				map.put(colValue + "Id", new Integer(indexCol));
				result = Integer.parseInt(map.get(colValue + "Id").toString());
				regList.add(new Integer(result));
				Iterator z = regList.iterator();
				StringBuffer stringbuffer = new StringBuffer();
				while (z.hasNext()) {
					String a = z.next().toString();
					stringbuffer.append(a);
					stringbuffer.append("#");
				}
				stringbuffer.deleteCharAt(stringbuffer.toString().length() - 1);
				regStr = stringbuffer.toString();
			}	
		return regStr;
	}
	
	private static StringBuffer sbOutput = new StringBuffer("\r\n");
	private static String key = null;
	private static Enumeration enum1 = null; 
	private static String generatedKey = "";
	private static String generatedValue = "";
	private static String generatedQueryKey = "";
	private static String generatedQuery = "";
	private static Object key1 = null;
	private static Object value = null;
	private static ArrayList arr = new ArrayList();
	private static ArrayList arrQuery = new ArrayList();
	private static ArrayList pgnQuery = new ArrayList();
	private static ArrayList arrQueryKey = new ArrayList();
	private static HashMap map1 = new HashMap();


	private static final String head =
	"package com.tcs.ebw.serverside.query;"	+ "\r\n"  + "\r\n" + 
	"import java.util.HashMap;" + "\r\n" + "\r\n" + 
	"public class EBWStatement {" + "\r\n\r\n"  +
	"\t" + "public EBWStatement() {" + "\r\n" + 
	"\t" + "}" + "\r\n\r\n"; 			

	private static final String foot = 
	"	/**" + "\r\n\r\n" + 
	"	 * @return map" + "\r\n" + 
	"	 */" + "\r\n" + 
	"	public static HashMap getMap() {" + "\r\n" + 
	"		return populateHashmap() ;" + "\r\n" + 
	"	}" + "\r\n\r\n" +
	"	/**" + "\r\n" + 
	"	 * @param map the map to set" + "\r\n" + 
	"	 */" + "\r\n" + 
	"	public static void setMap(HashMap map) {" + "\r\n" + 
	"		EBWStatement.map = map;" + "\r\n" + 
	"	}" + "\r\n" + "\r\n" + 
	"\t" + "static HashMap map=new HashMap();" + "\r\n\r\n" + 
	"}";
	
	private static final String function_start =
		"\t" + "private static HashMap populateHashmap() {" + "\r\n" + 
		"\t\t" + "if(map.isEmpty())" + 
		"\t\t" + "{" + "\r\n" ;

	private static final String function_end =
	"\t\t" + "}" + "\r\n\r\n" + 
	"\t\t" + "return map;" + "\r\n" +  
	"\t" + "}" + "\r\n";
	
 }
