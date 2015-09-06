package com.tcs.ebw.taglib;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.tcs.ebw.taglib.EbwTableHelper;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.StringUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class EbwTableComponent {
	public static StringBuffer getHrefHidden(String value, TableColAttrObj colAttrObj,int row,String tablName,String formName,ResourceBundle formRB){
		StringBuffer hrefHiddenStr = new StringBuffer();
		
		if(colAttrObj!=null) {
			String colname = (colAttrObj.getColName()).toUpperCase();
			//String displayMsg = StringUtil.initCaps(((colAttrObj.getColName()).toLowerCase()));
			String displayMsg = getBundleString(colname,colname,formRB,formName,tablName);
			String tagContent =  colAttrObj.getFieldAttribute();
			if(colAttrObj.getComponentType().equalsIgnoreCase("HrefHiddenEnable")){
				hrefHiddenStr.append("<input type=\"Hidden\" name=\""+colname+"\" value=\"\"> <a name=\""+colname+"_"+row+"\"  href=\"#\" "+tagContent+">"+displayMsg+"</a>");
			}else{
				if((value !=null && value.length()>0) && (colAttrObj.getComponentType().equalsIgnoreCase("HrefHidden"))){
					//if(Integer.valueOf(value).intValue()==0)
						if(value.equalsIgnoreCase("n") || (value.equalsIgnoreCase("0") && Integer.parseInt(value)  == 0))
							hrefHiddenStr.append("<input type=\"Hidden\" name=\""+colname+"\" value=\"\">" );
						//else if(Integer.valueOf(value).intValue()==1)
						else if(value.equalsIgnoreCase("y") || (value.equalsIgnoreCase("1") && Integer.parseInt(value) == 1))
							hrefHiddenStr.append("<input type=\"Hidden\" name=\""+colname+"\" value=\"\"> <a name=\""+colname+"_"+row+"\"  href=\"#\" "+tagContent+">"+displayMsg+"</a>");
				}
			}
		}
		
		return hrefHiddenStr;
	}
	
	
	private static String getBundleString(String key, String value,ResourceBundle formRB,String formname,String tblName) {
			String strMsg = value;
			try {
					if (formRB!=null) {
					    if (key != null) {
					        strMsg = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + tblName + "." + key);
					    } else {
					        strMsg = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + tblName);
					    }
					}
				}catch (Exception exc) {
			    
			    if((key.indexOf("NoDataFoundMsg") == -1))
			        strMsg="";
			    
				//System.out.println ("Resource bundle value not found for " + key);
				//exc.printStackTrace();
			}
			return strMsg;
	}
		
	
	public String getImageValue(String data,String imageList){
		
		String val=null;
		//added by Nithya
		if(imageList.indexOf("onclick")!=-1){
			EBWLogger.logDebug(this,"Inside colorcoded imageList :"+imageList);
			imageList=imageList.substring(0,imageList.indexOf("onclick"));
			
			if(imageList.endsWith(","))
				imageList = imageList.substring(0,imageList.lastIndexOf(","));
			
		}
		String[] imgArr=imageList.split(",");
		HashMap imgMap=new HashMap();
		for(int i=0;i<imgArr.length;i++){
			String[] splitval=imgArr[i].split("=");
			imgMap.put(splitval[0],splitval[1]);
		}
		val=(String)imgMap.get("image"+data);
		
		return val;
	}
	
	public static String enableCompOnValue(LinkedHashMap dataValue,String fieldAttr,ArrayList rowData,int titles) {
		  String formattedColData = new String();
		  	java.util.HashMap attrMap=new java.util.HashMap();
		  	
		 ArrayList colNames = new ArrayList();
			if(fieldAttr.indexOf("~")>-1){
				String[] dependentColPosVal = fieldAttr.split("~");
			for(int t=0;t<dependentColPosVal.length;t++){
				
				if(dependentColPosVal[t].trim().indexOf("dependentColPos")>-1){
						attrMap.put(dependentColPosVal[t].substring(dependentColPosVal[t].indexOf("dependentColPos"),dependentColPosVal[t].indexOf("=")),dependentColPosVal[t].substring(dependentColPosVal[t].indexOf("=")+1,dependentColPosVal[t].indexOf(",")));
					
					
					if(dependentColPosVal[t].trim().indexOf("conditionData")>-1){
						String condval = dependentColPosVal[t].substring(dependentColPosVal[t].indexOf("conditionData"));
						attrMap.put(condval.substring(condval.indexOf("conditionData"),condval.indexOf("=")),condval.substring(condval.indexOf("=")+1,condval.length()));
					}
						Object[] colnames = dataValue.keySet().toArray();
						for(int i=0;i<colnames.length;i++)
						{
							colNames.add(colnames[i]);
						}
						//int colpos=Integer.parseInt(attrMap.get("dependentColPos").toString());
						int colpos=colNames.indexOf((attrMap.get("dependentColPos").toString()));
						if(rowData!=null && colpos!=-1 && (String)rowData.get(colpos)!=null){
							String condData = (String)rowData.get(colpos);
							if(condData!=null && condData.length()>0 && (String)attrMap.get("conditionData") !=null && condData.equalsIgnoreCase((String)attrMap.get("conditionData")))
								formattedColData = "disabled=\"true\"";
						}
						
				}
					
			}
			}
		  return formattedColData.toString();  
		}
	
	public  String getConditionalRowCSSString(ArrayList dataValue,String conRowCssAtrr,ArrayList rowData) {
		  String rowCssVal = "";
		  		  	
			ArrayList arr = new ArrayList();
			Object[] str = ((((LinkedHashMap)dataValue.get(0)).keySet()).toArray());
			for(int j=0;j<str.length;j++)
				arr.add(str[j]);			
			ArrayList colNames = arr;
			//ArrayList colNames = dataValue;
				
			if(conRowCssAtrr.indexOf(",")>-1){
				String[] conRowCssVal = conRowCssAtrr.split(",");
				
				int colpos = colNames.indexOf(conRowCssVal[0]); // 0th element is the col name
				String dataFrmAttr = conRowCssVal[1]; // 1st element is the condition data
				String cssName = conRowCssVal[2]; // 2nd element is the CSS for that row
				
				String dataFrmarr = (String)rowData.get(colpos);
				if(dataFrmarr!=null && dataFrmarr.length()>0 && (dataFrmarr.equalsIgnoreCase(dataFrmAttr) || dataFrmAttr.indexOf(dataFrmarr)!=-1))
					rowCssVal = cssName;			
			}
	
		  return rowCssVal;  
		}
	
	public  String getConditionalcolCSSString(ArrayList dataValue,String fieldAttr,String rowData) {
		  String rowCssVal = "";
		  	java.util.HashMap attrMap=new java.util.HashMap();	
		  	
			ArrayList arr = new ArrayList();
			Object[] str = ((((LinkedHashMap)dataValue.get(0)).keySet()).toArray());
			for(int j=0;j<str.length;j++)
				arr.add(str[j]);			
			ArrayList colNames = arr;
			//ArrayList colNames = dataValue;
				
			if(fieldAttr.indexOf("~")>-1){
				String[] dependentColPosVal = fieldAttr.split("~");
			for(int t=0;t<dependentColPosVal.length;t++){
				
				if(dependentColPosVal[t].trim().indexOf("dependentColPos")>-1){
						attrMap.put(dependentColPosVal[t].substring(dependentColPosVal[t].indexOf("dependentColPos"),dependentColPosVal[t].indexOf("=")),dependentColPosVal[t].substring(dependentColPosVal[t].indexOf("=")+1,dependentColPosVal[t].indexOf(",")));
					if(dependentColPosVal[t].trim().indexOf("conditionData")>-1){
						String condval = dependentColPosVal[t].substring(dependentColPosVal[t].indexOf("conditionData"));
						attrMap.put(condval.substring(condval.indexOf("conditionData"),condval.indexOf("=")),condval.substring(condval.indexOf("=")+1,condval.indexOf(",")));
					}
					if(dependentColPosVal[t].trim().indexOf("style")>-1){
						String styleval = dependentColPosVal[t].substring(dependentColPosVal[t].indexOf("style"));
						attrMap.put(styleval.substring(styleval.indexOf("style"),styleval.indexOf("=")),styleval.substring(styleval.indexOf("=")+1,styleval.indexOf(",")));
					}
					
					String[] data = rowData.split("~");						
					int colpos=colNames.indexOf((attrMap.get("dependentColPos").toString()));
						if(colpos!=-1 && data[colpos]!=null){
							String condData = (String)data[colpos];	
							if(condData!=null && condData.length()>0 && (String)attrMap.get("conditionData") !=null && (attrMap.get("conditionData").toString().indexOf(condData)!=-1)){
								rowCssVal = (String)attrMap.get("style");		
							}
						}				
				 }					
			   }
			}
	
		  return rowCssVal;  
		}
	
	public static void main(String[] args){
		TableColAttrObj colAttrObj = new TableColAttrObj();
		ArrayList a = new ArrayList();
		a.add("We");a.add("are");a.add("inr");a.add("TCS");
		colAttrObj.setColName("REMARKS");
		 LinkedHashMap hm=new LinkedHashMap();
	        hm.put("Product", "Product");
	        hm.put("Clear Balance", "Clear Balance");
	        hm.put("Clear Balance INR", "Clear Balance INR");
	        hm.put("INTEREST", "INTEREST");
		int row=1;
		colAttrObj.setFieldAttribute("disabled=\"true\"style=\"width:60px\" class=\"numbertextboxcontent\"~dependentColPos=Clear Balance INR,conditionData=INR");
		String value = "1";
		//int value=1;
		//ResourceBundle rb = 
		String result = EbwTableComponent.enableCompOnValue(hm,colAttrObj.getFieldAttribute(),a,0);
		System.out.println("REsult :"+result.toString()+" size=="+result.length());
	}

}
