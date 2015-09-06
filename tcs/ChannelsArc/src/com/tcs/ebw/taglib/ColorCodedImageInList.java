package com.tcs.ebw.taglib;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.tcs.ebw.common.util.EBWLogger;
public class ColorCodedImageInList{
	public String getImageValue(String data,String imageList){
		String val=null;
		String[] imgArr=imageList.split(",");
		HashMap imgMap=new HashMap();
		for(int i=0;i<imgArr.length;i++){
			String[] splitval=imgArr[i].split("=");
			imgMap.put(splitval[0],splitval[1]);
		}
		val=(String)imgMap.get("image"+data);
		return val;
	}
	
	public static String formatDataForLink(String hrefValue,String colData, String conditionData,String fieldAttr,String colName) {
		  StringBuffer formattedColData = new StringBuffer();
		  if(conditionData.equals("2") || conditionData.equalsIgnoreCase("Y"))  { //1 or Y
			  if(hrefValue.equals(""))
				  hrefValue="#";
		    formattedColData.append("<a href=\""+hrefValue+"\" name=\""+colName+"\" "+fieldAttr+">");
		    formattedColData.append(colData);
		    formattedColData.append("</a>");
		  } 
		  else { //No formatting required
		    formattedColData.append(colData);
		  }
		  return formattedColData.toString();  
		}
	
	/*public static String formatDataForLink(String hrefValue,String colData, String conditionData,String fieldAttr, String splitKey)
	{

		  StringBuffer formattedColData = new StringBuffer();
		  String[] splitKeyName =splitKey.split("\"");
		  if(conditionData.equals(getBundleString(splitKeyName[1])))  
			{
				if(hrefValue.equals(""))
				  hrefValue="#";

				formattedColData.append("<a href=\""+hrefValue+"\""+fieldAttr+">");
				formattedColData.append(colData);
				formattedColData.append("</a>");
			} 

		  else 
		  {
				//No formatting required
				formattedColData.append(colData);
		   }

		  return formattedColData.toString();  
		}	
private static String getBundleString(String key) {
		String strMsg = "";
		try {
			ResourceBundle appRB = ResourceBundle.getBundle("ApplicationConfig");
			ResourceBundle formRB = ResourceBundle.getBundle(appRB.getString("message-resources"));
			strMsg = formRB.getString(key);
		} catch (Exception exc) {
			EBWLogger.trace(new Object(), "Entry not found in Resource Bundle");
		}
		return strMsg;
	}*/
	
	public static void main(String args[]){
		
	}
}