/*
 * Created on Jul 7, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.workflow;

/**
 * @author TCS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


public class WorkflowInfo {
	private String xml="";
	private Document doc=null;
	
	public WorkflowInfo(String xml){
		this.xml = xml;
		try{
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xml);
			setDocumentObject(document);
			System.out.println("Document Object created ");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public WorkflowInfo(InputStream reader){
		try{
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(reader);
			setDocumentObject(document);
			System.out.println("Document Object created ");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public String getElementValue(String elementName){
		String value="";
		NodeList nodelist =null;
		if(this.doc !=null){
			nodelist = this.doc.getElementsByTagName(elementName);
			value = nodelist.item(0).getFirstChild().getNodeValue();
		}
		else
			System.out.println("Document is null");
		
		return value;
	}
	
	
	public ArrayList getTableData(String tagName){
		
		Element element = this.doc.getDocumentElement();
		
		/** 
		 * Getting list of all the nodes that starts with the given tagName
		 * and putting it in the object nodeList
		 **/
		NodeList nodeList = element.getElementsByTagName(tagName);
		
		/** ArrayList to store the data , first index will be the header
		 * and the follwing indexes will be the values 
		 */
		ArrayList data = new ArrayList();
		
		/** 
		 * ArrayList to store the headerinformation
		 */
		ArrayList headers = null;
		
		/**
		 * Getting the childNodes of the given tagName
		 */
		
		NodeList responseChildNodes = nodeList.item(0).getChildNodes();
		
		/** Populating the ArrayList with the headers **/
		
		for(int i=0;i<responseChildNodes.getLength();i++){
			if(responseChildNodes.item(i).getNodeType()!=Node.TEXT_NODE){
				String childNodeName = responseChildNodes.item(i).getNodeName();
				if(headers==null){
					headers = new ArrayList();
				}
				headers.add(childNodeName);
			}
		}
		
		data.add(headers);
		
		headers = null;
		
		/** Populating the ArrayList with values by getting
		 * the childNodeList of all the node that starts 
		 * with the given tagName 
		 **/
		
		for(int i=0;i<nodeList.getLength();i++){
			/**
			 * Getting the childNodes of the Node given by 
			 * name tagName
			 */
			NodeList childNodeList = nodeList.item(i).getChildNodes();
			
			ArrayList values = null;		  
			/** Getting the child nodes of the nodes with 
			 * in the node - tagName
			 *  
			 **/
			for(int j=0;j<childNodeList.getLength();j++){
				/** Creating a new ArrayList for holding data of 
				 * each node - tagName 
				 */
				if(values==null){
					values = new ArrayList();
				}
				/**
				 * populating the ArrayList only if the childNodes of
				 * the node - tagName has only childrens
				 */
			   if(childNodeList.item(j).hasChildNodes()){
				 values.add(childNodeList.item(j).getChildNodes().item(0).getNodeValue());
				}
			   /**
			    * To add null values for the childNodes of the node - tagName
			    * which does not have children and which is not type text.
			    */
				if(!childNodeList.item(j).hasChildNodes() && childNodeList.item(j).getNodeType()!=Node.TEXT_NODE){
				   values.add(null); 	
				}
	
			}
			data.add(values);
		}
		
		return data;
	}
	
	private void setDocumentObject(Document doc){
		this.doc = doc;
	}
	
	private Document getDocumentObject(){
		return this.doc;
	}
	
	public static void main(String args[])throws Exception{
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<quartz-response><errorid>170240167</errorid><ackresponse>170240167 - You Must Select The Withholding Tax Check Box As Credit Interest Is Applicable On This Account.</ackresponse><severity>1</severity><ackmessage>SUCCESS</ackmessage><comresponsedata><response><posid>897</posid></response></comresponsedata></quartz-response>");
		try{
		    WorkflowInfo domParser = new WorkflowInfo(new FileInputStream("D:\\BANCS\\BancslinkV2Web\\xml\\Workflows\\AccCheckFlow.xml"));
		    //System.out.println (domParser.getElementValue("outcome"));
			ArrayList data = domParser.getTableData("method");
			System.out.println("Table date " + data);
		}catch(FileNotFoundException fne){
			fne.printStackTrace();
		}
		
	}
}
