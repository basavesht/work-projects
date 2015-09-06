package com.tcs.ebw.common.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Set;
import com.tcs.ebw.serverside.query.EBWStatement;

//import com.tcs.ebw.CA.formbean.CorporateActionViewForm;
//import com.tcs.ebw.CA.transferobject.CAViewNDBTO;

public class CallTableHeader {
	
	/**
     * Constructor of CallTableHeader     
     */
    public CallTableHeader()
    {
    }
	
	
	/**
	 * ResourceBundle object to hold the properties configured
	 */
	
	 
	/*private static String key = null;
	private static String generatedKey = "";
	private static String generatedValue = "";
	private static String generatedQueryKey = "";
	private static String generatedQuery = "";
	private static Object key1 = null;
	private static Object value = null;*/
	private static HashMap numberMap = new HashMap();
	
	public static int getIndex(String strQuery,String strColumn){
		System.out.println("CallTableHeader : Inside getIndex : queryname:" + strQuery);
		System.out.println("CallTableHeader : Inside getIndex : Column:" + strColumn);
		int col;
		String key=strQuery+"."+ strColumn;
		if(numberMap.containsKey(key)){
			//System.out.println("Already Existing : No function Call");
			col=Integer.valueOf(numberMap.get(key).toString()).intValue();
			System.out.println("Already Existing : column Value :" + col);
		}else {
			//System.out.println("Function Call");
			col=createEBWHashmap(strQuery,strColumn);
			System.out.println("Function Call: column Value" +  col);
		}
		System.out.println("CallTableHeader : Final Column Number :" + col);
		return col;
				
	}
	
	public static int createEBWHashmap(String strQuery,String strColumn)	{	
		System.out.println("CallTableHeader : Inside createEBWHashmap column :" + strColumn + "query :" + strQuery);
		EBWLogger.trace("GenStatFile",	"Starting method createHashmap");
		HashMap queryMap=new HashMap();		
		queryMap=EBWStatement.getMap();
		String key=strQuery + "." + strColumn;
		String keyQuery=strQuery+".TOColumnMap";
				
		String toColumnMapVal=(String)queryMap.get(keyQuery);
		toColumnMapVal=toColumnMapVal.substring(1,toColumnMapVal.length()-1);
		String[] strHeaderArray=toColumnMapVal.split(",");
		
		for(int i=0;i<strHeaderArray.length;i++){
			String strKey=strHeaderArray[i].substring(strHeaderArray[i].indexOf("=")+1,strHeaderArray[i].length());
			strKey=strQuery+"." + strKey;
			numberMap.put(strKey,new Integer(i));			
		}
			
		int retStr=Integer.valueOf(numberMap.get(key).toString()).intValue();
		System.out.println("CallTableHeader : Final Output :" + retStr);
		return retStr;
	}
	
/*public static void createHeaderColMap()	{	
		HashMap linkedHash = PropertyFileReader.getProperties("Statement");
				
		Set s = linkedHash.entrySet();
		Iterator k = s.iterator();
		while (k.hasNext()) {
			Map.Entry obj = (Map.Entry) k.next();
			key1 = obj.getKey();
			value = obj.getValue();
			if (key1.toString().indexOf(".TOColumnMap") > -1) {
				generatedKey = key1.toString();
				generatedValue = value.toString();
				generatedKey=generatedKey.substring(0,generatedKey.indexOf("."));
														
				String toColumnMapVal=generatedValue.substring(1,generatedValue.length()-1);
				//System.out.println("valQuery after is :" + toColumnMapVal);
				String[] strHeaderArray=toColumnMapVal.split(",");
				
				for(int i=0;i<strHeaderArray.length;i++){
					String strKey=strHeaderArray[i].substring(strHeaderArray[i].indexOf("="),strHeaderArray[i].length());
					//System.out.println("strKey is :" + strKey); 
					numberMap.put(generatedKey+"." + strKey,i);
					
				}				
			}				
		}		
		
	}*/

	
	
	
//	public static void main(String args[]){
//        System.out.println("Inside Main ");
//        //createHashmap("getAvailableOptions");
//        int ret=createEBWHashmap("getAvailableOptions","DEO_OPTN_TYP");
//        System.out.println("Return String : " + ret);
//        
//		
//	}

}
