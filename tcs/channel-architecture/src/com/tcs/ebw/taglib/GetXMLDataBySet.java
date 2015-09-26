/**
 * 
 */
package com.tcs.ebw.taglib;

import java.io.File;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.StringUtil;


@SuppressWarnings("serial")
public class GetXMLDataBySet extends TagSupport
{
	private boolean htmlOutput = false;
	private StringBuffer outputBuffer;
	private String displayType;
	private String fileName;
	private String strQAFileName;
	private ResourceBundle bundle;
	private String setCounter;
	private Object attrObj = null;
	private String formname=null;
	private String name=null;	
	
	public String getDisplayType() 
	{
		return displayType;
	}

	public void setDisplayType(String displayType) 
	{
		this.displayType = displayType;
	}

	public String getSetCounter() 
	{
		return setCounter;
	}

	public void setSetCounter(String setCounter) 
	{
		this.setCounter = setCounter;
	}

	public GetXMLDataBySet()
	{
		this.htmlOutput = false;
		outputBuffer = null;
		displayType=null;
		fileName=null;
		//Get the resource Bundles for the tag's property file & the locale-specific resources 
		// from ComponentTypeResource.properties
		bundle = ResourceBundle.getBundle(TagLibConstants.RESOURCE_BUNDLE_FILE_MODECOMPONENT);
		
		// Setting Filename Values from Property file...
		if(fileName==null || fileName.equals(""))
		{
			EBWLogger.trace(this, "the File Name not specified by the user");
			fileName= bundle.getString("XMLQASet.fileName");
			strQAFileName=bundle.getString("XMLQA.fileName");
			System.out.println(" fileName : "+fileName+" strQAFileName : "+strQAFileName);
		}
	}
	
	public GetXMLDataBySet(boolean htmlOutput)
	{
		displayType=null;
		fileName=null;
		this.htmlOutput = htmlOutput;
		outputBuffer = null;
	}

	public int doStartTag() throws JspException
	{		
		try
		{			
			attrObj=(com.tcs.ebw.mvc.validator.EbwForm)pageContext.getRequest().getAttribute(formname);			
			Class formClass = Class.forName(attrObj.getClass().getName());
						
			String QASetLength=null;
			String strSetId=null;
			String strNextSet=null;
			String strNestedSet=null;
			String strSetQId=null;
			String strQuestionID=null;
			String strNested=null;
			String strNestedQIDs=null;			
			String tmpSetId=null;
			
			File file;
			JspWriter out = pageContext.getOut();
			
			if(!htmlOutput)
			{
				out=pageContext.getOut();
			}
			// Pagewise display for getting	Set Counter
			setCounter = (String) formClass.getMethod("get" + StringUtil.initCaps(name+"Counter"), null).invoke(attrObj, null);
		    System.out.println("Form obj : "+setCounter);
		    if(setCounter==null || setCounter.equals(""))
		    {
		    	setCounter="0";
		    	System.out.println("It is empty.........................................");
		    }
						
			
			
		    //XML File Parsing...
			file= new File(fileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			
			//File Parsing..
			File QAFile = new File(strQAFileName);
			DocumentBuilderFactory dbfQA = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbQA = dbfQA.newDocumentBuilder();
			Document docQA = dbQA.parse(QAFile);
			docQA.getDocumentElement().normalize();
						
			// Setting displayType Values from Property file...
			if(displayType==null || displayType.equals(""))
			{
				EBWLogger.trace(this, "the display Type not specified by the user");
				displayType= bundle.getString("XMLQA.displayType");
				System.out.println(" displayType "+displayType);
			}
						
			int intSetCounter=Integer.parseInt(setCounter);
			
			//Reading the Values from XML file.. QuestionID, Question, AnswerID, Answer, Nested, NextQuestionID
			NodeList questionIDNode= doc.getElementsByTagName("Set");
			NodeList nodeSetId= doc.getElementsByTagName("SetId");
			NodeList nodeNextSet= doc.getElementsByTagName("NextSet");
			NodeList nodeNestedSet= doc.getElementsByTagName("NestedSet");
			NodeList nodeSetQId= doc.getElementsByTagName("SetQId");
						
			System.out.println(" Reading XML"+questionIDNode.getLength());			
			QASetLength=String.valueOf(questionIDNode.getLength());
			//Reading Values...
			tmpSetId=nodeSetId.item(intSetCounter).getChildNodes().item(0).getNodeValue();
			
			if(strSetId==null)
			{
				strSetId=nodeSetId.item(intSetCounter).getChildNodes().item(0).getNodeValue();
				System.out.println(" strSetId Value "+strSetId);				
				
				strNextSet=nodeNextSet.item(intSetCounter).getChildNodes().item(0).getNodeValue();
				System.out.println(" NextSet Value "+strNextSet);					
				
				strNestedSet=nodeNestedSet.item(intSetCounter).getChildNodes().item(0).getNodeValue();
				System.out.println(" NestedSet Value "+strNestedSet);
			}
			else
			{
				strSetId+=","+nodeSetId.item(intSetCounter).getChildNodes().item(0).getNodeValue();
				System.out.println(" strSetId Value "+strSetId);				
				
				strNextSet+=","+nodeNextSet.item(intSetCounter).getChildNodes().item(0).getNodeValue();
				System.out.println(" NextSet Value "+strNextSet);					
				
				strNestedSet+=","+nodeNestedSet.item(intSetCounter).getChildNodes().item(0).getNodeValue();
				System.out.println(" NestedSet Value "+strNestedSet);
			}
			
			for(int setQIdCounter=0;setQIdCounter<nodeSetQId.getLength();setQIdCounter++)				
			{
				strSetQId=nodeSetQId.item(setQIdCounter).getChildNodes().item(0).getNodeValue();
				NodeList nodeQuestionID= doc.getElementsByTagName("QuestionID");
				NodeList nodeNested= doc.getElementsByTagName("Nested");
				NodeList nodeNestedQIDs= doc.getElementsByTagName("NestedQIDs");
				
				//If SetID is same then set QASet Values...
				if(strSetQId.equals(tmpSetId))
				{
					if(strQuestionID==null)
					{
						strQuestionID=nodeQuestionID.item(setQIdCounter).getChildNodes().item(0).getNodeValue();
						System.out.println(" QuestionID Value "+strQuestionID);
						
						strNested=nodeNested.item(setQIdCounter).getChildNodes().item(0).getNodeValue();
						System.out.println(" Nested Value "+strNested);
						
						strNestedQIDs=nodeNestedQIDs.item(setQIdCounter).getChildNodes().item(0).getNodeValue();
						System.out.println(" NestedQIDs Value "+strNestedQIDs);
					}

					else
					{
						strQuestionID+=":"+nodeQuestionID.item(setQIdCounter).getChildNodes().item(0).getNodeValue();
						System.out.println(" QuestionID Value "+strQuestionID);
						
						strNested+=":"+nodeNested.item(setQIdCounter).getChildNodes().item(0).getNodeValue();
						System.out.println(" Nested Value "+strNested);
						
						strNestedQIDs+=":"+nodeNestedQIDs.item(setQIdCounter).getChildNodes().item(0).getNodeValue();
						System.out.println(" NestedQIDs Value "+strNestedQIDs);
					}
				}
			}
			
			System.out.println("Before OutputBuffer");
			outputBuffer=new StringBuffer();
			outputBuffer.append((new StringBuilder("\n\n <input type=\"hidden\" name=\""+name+"Counter\" value=\"\" id=\""+name+"Counter\">")));
			outputBuffer.append((new StringBuilder("\n\n <script type=\"text/javascript\">")));
			outputBuffer.append((new StringBuilder("\n setDisplayType('"+displayType+"');")));
			outputBuffer.append((new StringBuilder("\n setQASetsLength('"+QASetLength+"');")));
			outputBuffer.append((new StringBuilder("\n setCounter('"+name+"Counter');")));
			outputBuffer.append((new StringBuilder("\n setCurrentQASet('"+strSetId+"');")));
			outputBuffer.append((new StringBuilder("\n </script>")));			
			out.print(outputBuffer);
			
			//Set values of All questions in Set...
			setJSF(docQA, strSetId, strNextSet, strNestedSet, strQuestionID, strNested, strNestedQIDs);
			
			
			System.out.println(" Output Completed");			

			
		} catch (Exception exc) 
		{
	        System.out.println (" Error in doStartTag : " + exc.getStackTrace());
		}   
		return 0;
	}
	
	public void setJSF(Document docQA,String strSetId,String strNextSet,String strNestedSet,String strQuestionID,String strNested,String strNestedQIDs)
	{
		try
		{
			//Set separated by ":" 
			// QuestionID, QuestionValue, AnswerQID, AnswerValue, NextQID, NestedQID, NextSet, CurrentSet, NestedSet			
			
			//Other Required Variables..
			String tmpQID=null;
			String tmpQValue=null;
			String tmpConditional=null;
			String strNodeName=null;
			short nodeType;

			//Parameters Passed..
			String[] tmpSetQID=strQuestionID.split(":");

			//Setting Variables for JS..
			String strAnsValue=null;
			String strAnsQID=null;
			String strAnsNextQID=null;
			String strQID=null;
			String strQValue=null;
			String strConditional=null;
			String tmpCurrentSet=strSetId;
			String tmpNextSet=strNextSet;
			String tmpNestedSet=strNestedSet;
			//DisplaySet dispSet=new DisplaySet();
			
			
			
			//Loop till tmpSetQID length..
			for(int qcounter=0;qcounter<tmpSetQID.length;qcounter++)
			{			
				NodeList questionNode= docQA.getElementsByTagName("Question");			
				
				for(int questionCounter=0;questionCounter<questionNode.getLength();questionCounter++)
				{
					NodeList nodeQuestionID= docQA.getElementsByTagName("QuestionID");
					tmpQID=nodeQuestionID.item(questionCounter).getChildNodes().item(0).getNodeValue();
					
					if(tmpQID.equals(tmpSetQID[qcounter]))
					{
						System.out.println("tmpQID " + tmpQID + " tmpSetQID[qcounter] " + tmpSetQID[qcounter]);
						
						NodeList nodeQuestionValue= docQA.getElementsByTagName("QuestionValue");						
						tmpQValue=nodeQuestionValue.item(questionCounter).getChildNodes().item(0).getNodeValue();
						
						NodeList nodeConditional= docQA.getElementsByTagName("Conditional");
						tmpConditional=nodeConditional.item(questionCounter).getChildNodes().item(0).getNodeValue();
						
						NodeList strAnsTagNode=questionNode.item(questionCounter).getChildNodes().item(7).getChildNodes();						
						
						for(int ansTagCounter=0;ansTagCounter<strAnsTagNode.getLength();ansTagCounter++)
						{
							nodeType=strAnsTagNode.item(ansTagCounter).getNodeType();
							if(nodeType!=Node.TEXT_NODE)
							{
								NodeList strAnsNode=strAnsTagNode.item(ansTagCounter).getChildNodes();
								
								for(int ansCounter=0;ansCounter<strAnsNode.getLength();ansCounter++)
								{
									nodeType=strAnsNode.item(ansCounter).getNodeType();
									
									if(nodeType!=Node.TEXT_NODE)
									{
										strNodeName=strAnsNode.item(ansCounter).getNodeName();
										if(strNodeName.equals("AnswerQID"))
										{
											if(strAnsQID==null)
											{
												strAnsQID=strAnsNode.item(ansCounter).getChildNodes().item(0).getNodeValue();												
											}
											else if(strAnsQID.charAt(strAnsQID.length()-1)=='~')
											{
												strAnsQID+=strAnsNode.item(ansCounter).getChildNodes().item(0).getNodeValue();
											}
											else
											{
												strAnsQID+=":"+strAnsNode.item(ansCounter).getChildNodes().item(0).getNodeValue();
											}
										}
										if(strNodeName.equals("AnswerValue"))
										{
											if(strAnsValue==null)
											{
												strAnsValue=strAnsNode.item(ansCounter).getChildNodes().item(0).getNodeValue();												
											}
											else if(strAnsValue.charAt(strAnsValue.length()-1)=='~')
											{
												strAnsValue+=strAnsNode.item(ansCounter).getChildNodes().item(0).getNodeValue();
											}
											else
											{
												strAnsValue+=":"+strAnsNode.item(ansCounter).getChildNodes().item(0).getNodeValue();
											}
										}
										if(strNodeName.equals("NextQuestionID"))
										{
											if(strAnsNextQID==null)
											{
												strAnsNextQID=strAnsNode.item(ansCounter).getChildNodes().item(0).getNodeValue();												
											}
											else if(strAnsNextQID.charAt(strAnsNextQID.length()-1)=='~')
											{
												strAnsNextQID+=strAnsNode.item(ansCounter).getChildNodes().item(0).getNodeValue();
											}
											else
											{
												strAnsNextQID+=":"+strAnsNode.item(ansCounter).getChildNodes().item(0).getNodeValue();												
											}
										}
									}
								}
							}
						}

						if(strQID!=null)
						{
							strQID+=":"+tmpQID;
						}
						else
						{
							strQID=tmpQID;
						}
						
						if(strQValue!=null)
						{
							strQValue+=":"+tmpQValue;
						}
						else
						{
							strQValue=tmpQValue;
						}

						if(strConditional!=null)
						{
							strConditional+=":"+tmpConditional;
						}
						else
						{
							strConditional=tmpConditional;
						}

						strAnsQID+="~";
						strAnsValue+="~";
						strAnsNextQID+="~";

						System.out.println("--------------------------------------------------------------------");
						System.out.println(" currentSet "+tmpCurrentSet+" \nNested Set "+tmpNextSet+" \nNext Set "+tmpNestedSet);
						System.out.println(" strQID "+strQID+" \nstrQValue "+strQValue+" \nstrAnsQID "+strAnsQID+" \nstrAnsValue "+strAnsValue+" \nstrAnsNextQID "+strAnsNextQID+" \nstrNestedQIDs "+strNestedQIDs);
						System.out.println(" Qusetion End.....");
						System.out.println("--------------------------------------------------------------------");
					}
				}
			}
			
			displayDetails(tmpCurrentSet, tmpNestedSet, tmpNextSet, strQID, strQValue, strConditional, strAnsQID, strAnsValue, strAnsNextQID, strNestedQIDs, strNested);

		} catch (Exception exc)
		{
	        System.out.println (" Error setJSF : " + exc.getMessage()+ exc.getStackTrace());
		}
	}
		
	public void displayDetails(String tmpCurrentSet,String tmpNestedSet,String tmpNextSet,String strQID,String strQValue,String strConditional,String strAnsQID,String strAnsValue,String strAnsNextQID,String strNestedQIDs,String strNested)
	{
		try
		{
			JspWriter out = pageContext.getOut();
			StringBuffer outputBuffer = new StringBuffer();
			
			outputBuffer.append((new StringBuilder("\n <table width=\"100%\" border=0  class=\"answerTable\">")));
			outputBuffer.append((new StringBuilder("\n<tr>")));
			outputBuffer.append((new StringBuilder("\n<td valign=\"top\"  class=\"QAnsSet\">")));
	
			outputBuffer.append((new StringBuilder("\n<table>")));
			outputBuffer.append((new StringBuilder("\n<tr>")));
			outputBuffer.append((new StringBuilder("\n<td  class=\"BarTitleBlueLeft\">Your Answers</td>")));
			outputBuffer.append((new StringBuilder("\n</tr>")));
			outputBuffer.append((new StringBuilder("\n<tr >")));
			outputBuffer.append((new StringBuilder("\n<td>")));
			outputBuffer.append((new StringBuilder("\n<div id=\"divResponse\">\n\n\n\n")));
			
			out.print(outputBuffer);
			outputBuffer=null;
			
			displayResponse(strQID, strQValue,tmpCurrentSet);
			
			outputBuffer = new StringBuffer();
			outputBuffer.append((new StringBuilder("\n</div>")));
			outputBuffer.append((new StringBuilder("\n</td>")));
			outputBuffer.append((new StringBuilder("\n</tr>")));
			outputBuffer.append((new StringBuilder("\n</table>")));
	
			outputBuffer.append((new StringBuilder("\n</td><td valign=\"top\" width=\"80%\">")));
	
			outputBuffer.append((new StringBuilder("\n<table class=\"answerTable\">")));
			outputBuffer.append((new StringBuilder("\n<tr>")));
			outputBuffer.append((new StringBuilder("\n<td class=\"MoreSpaceBarTitleBlue\">Borrowing Advice</td>")));
			outputBuffer.append((new StringBuilder("\n</tr>")));
			outputBuffer.append((new StringBuilder("\n<tr>")));
			outputBuffer.append((new StringBuilder("\n<td class=\"MoreSpaceSliderBox\">\n\n\n\n\n")));
			
			out.print(outputBuffer);
			outputBuffer=null;
			
			displayRightDetails(strQID, strQValue,tmpCurrentSet, strNested, strAnsValue,  strAnsNextQID,  strNestedQIDs, strConditional, tmpNextSet);
			
			outputBuffer = new StringBuffer();						
			outputBuffer.append((new StringBuilder("\n</td>")));
			outputBuffer.append((new StringBuilder("\n</tr>")));
			outputBuffer.append((new StringBuilder("\n</table>")));
	
			outputBuffer.append((new StringBuilder("\n</td>")));
			outputBuffer.append((new StringBuilder("\n</tr>")));
			outputBuffer.append((new StringBuilder("\n</table>")));
			out.print(outputBuffer);
		}
		catch(Exception e)
		{
			System.out.println("Error in displayDetails Set "+e.toString());
		}
	}
	
	
	public void displayLeftTable(String questionID, String questionValue, String currentSet)
	{
		try
		{
			JspWriter out = pageContext.getOut();
			StringBuffer outputBuffer = new StringBuffer();
			
			System.out.print("In displayLeftTable QIDm QValue, currentSet : "+questionID+questionValue+currentSet);
			
			outputBuffer.append((new StringBuilder("\n <div id=\"response" + currentSet + questionID+"\" style=\"display:none;postion:absolute\">")));
			outputBuffer.append((new StringBuilder("\n <table width=\"100% \">")));
			outputBuffer.append((new StringBuilder("\n <tr><td>")));
			outputBuffer.append((new StringBuilder("\n <label class=\"BlackFontLabel\">Q. " + questionValue + "</label>")));
			outputBuffer.append((new StringBuilder("\n </td></tr>")));
			outputBuffer.append((new StringBuilder("\n <tr><td>")));
			outputBuffer.append((new StringBuilder("\n <input id=\"response"+ currentSet + questionID +"_value\" name=\""+name+"\" class=\"BlueInfo\" readonly=\"true\">")));
			outputBuffer.append((new StringBuilder("\n </td></tr>")));
			outputBuffer.append((new StringBuilder("\n </table>")));
			outputBuffer.append((new StringBuilder("\n </div>")));
		
			out.print(outputBuffer);
		}
		catch(Exception e)
		{
			System.out.println("Error in displayLeftTable Set "+e.toString());
		}
	}
	
	public void displayResponse(String paramQID,String paramQValue, String paramCurrentSet)
	{
		String[] tmpQID = paramQID.split(":");
		String[] tmpQValue = paramQValue.split(":");

		String currentQID = null;
		String currentQValue = null;
		
		for (int count = 0; count < tmpQID.length; count++)
		{
			currentQID = tmpQID[count];
			currentQValue = tmpQValue[count];
			System.out.println("In displayResponseBefore Call Left Table QIDm QValue, currentSet : "+ paramQID + paramQValue + paramCurrentSet);
			displayLeftTable(currentQID, currentQValue, paramCurrentSet);
		}
	}
	
	public void displayRightDetails(String paramQID,String paramQValue, String paramCurrentSet,String paramNested,String paramAnswerValue, String paramNextQIDs, String paramNestedQIDs,String paramConditional,String paramNextQASet)
	{
		try 
		{			
			System.out.println("In displayRightDetails QIDm QValue, currentSet paramNextQASet +paramAnswerValue: "+ paramQID + paramQValue + paramCurrentSet + paramNextQASet +paramAnswerValue);
			JspWriter out = pageContext.getOut();
			
			StringBuffer outputBuffer = new StringBuffer();
			String[] tmpXMLQuestionId = paramQID.split(":");
			String[] tmpQValue = paramQValue.split(":");
			String[] tmpNested = paramNested.split(":");
			String[] tmpAnsValue = paramAnswerValue.split("~");
			String[] tmpNextQIDs = paramNextQIDs.split("~");
			String[] tmpNestedQIDs = paramNestedQIDs.split(":");
			String[] tmpConditional = paramConditional.split(":");
			
			System.out.println("**********************");
			System.out.println("paramConditional "+paramConditional);
			System.out.println("paramNested "+paramNested);
			System.out.println("**********************");

			String currentQID = null;
			String currentQuestionValue = null;
			String currentDisplayBlock = "block";
			String currentAnswerValue = null;
			String currentNextQID = null;
			String dispType = null;
			String QusetionClass = "BlakFontLabel";
			String setEnd = "N";
			String currentSet = paramCurrentSet;
			String currentNextSet = paramNextQASet;// next setId here again xmlobj function  call this is mentioned in set details
			String divSetID = "QASet"+currentSet;//current setID this is mentioned in set details
			String currentNestedQIDs=null;//this is mentioned in set details
	
			outputBuffer.append((new StringBuilder("\n <table align=\"right\" width=800 border=0><tr><td >")));
			out.print(outputBuffer);
			
			for(int count=0;count<tmpXMLQuestionId.length;count++)
			{
				System.out.println("In displayRightDetails inside for: "+tmpXMLQuestionId[count]);
				String tmpQuestionId = tmpXMLQuestionId[count];
	
				if(tmpQuestionId.equals(tmpXMLQuestionId[count]))
				{
					System.out.println("In displayRightDetails tmpNested[count] + tmpConditional[count]: "+tmpNested[count] + tmpConditional[count]);
					currentQID = tmpQuestionId;
					currentQuestionValue = tmpQValue[count];
					currentAnswerValue = tmpAnsValue[count];
					currentNextQID = tmpNextQIDs[count];
					currentNestedQIDs = tmpNestedQIDs[count];//this is mentioned in set details
	
					if(tmpNested[count].equals("N") && tmpConditional[count].equals("N"))
					{
						currentDisplayBlock = "block";

						outputBuffer = null;
						outputBuffer =  new StringBuffer();
						outputBuffer.append((new StringBuilder("\n\n <script type=\"text/javascript\">")));
						outputBuffer.append((new StringBuilder("\n displayedQuestions('"+currentQID+"');")));					
						outputBuffer.append((new StringBuilder("\n </script>")));
						out.print(outputBuffer);
					}
					else
					{
						currentDisplayBlock = "none";
					}
	
					if(currentAnswerValue.equals("TextBox"))
					{
						dispType = "TextBox";
					}
					else
					{
						dispType = displayType;
					}
	
					/*if(dispType=="Button" && count!=0)
					{
						currentDisplayBlock="none";
					}*/
					
					outputBuffer=null;
					outputBuffer = new StringBuffer();
	
					displayRightTable(currentSet, currentQID, currentQuestionValue, currentDisplayBlock, currentAnswerValue, currentNextQID, dispType, QusetionClass, setEnd, currentNextSet, divSetID, currentNestedQIDs);
					
					outputBuffer.append((new StringBuilder("\n </td></tr><tr><td>")));
					out.print(outputBuffer);
					
				}
			}
			outputBuffer=null;
			outputBuffer = new StringBuffer();
			outputBuffer.append((new StringBuilder("\n </td></tr></table>")));
		
			out.print(outputBuffer);
		}
		catch(Exception e)
		{
			System.out.println("Error in displayRightDetails Set "+e.toString());
		}
	}
	
	public void displayRightTable(String currentSet,String currentQID,String currentQuestionValue,String currentDisplayBlock,String currentAnswerValue,String currentNextQID,String paramDispType,String QusetionClass,String setEnd,String paramcurrentNextSet,String divSetID,String currentNestedQIDs)
	{
		System.out.println("in displayRightTable Set currentQuestionValue "+currentQID+currentQuestionValue);
		JspWriter out = pageContext.getOut();
		if(!htmlOutput)
		{
			out=pageContext.getOut();
		}
		StringBuffer outputBuffer = new StringBuffer();
		String divID;
		String answerValue = currentAnswerValue;
		String dispType = paramDispType;
		String[] nextQID = currentNextQID.split(":");
		//alert(" nextQID "+nextQID[0]);
		String display = currentDisplayBlock;
		//String answer;
		String QClass = QusetionClass;
		//String currentSet = divSetID;
		String currentNextSet = paramcurrentNextSet;
		String currentNestedQID = currentNestedQIDs;
		String tdWidth=null;
		//String str="";

		divID="QAid"+currentSet+currentQID;

		outputBuffer.append((new StringBuilder("\n <div id=\""+divID+"\" style=\"display:"+display+"\">")));
		outputBuffer.append((new StringBuilder("\n <table align=\"right\" width=\"800\" border=0>")));
		outputBuffer.append((new StringBuilder("\n <tr><td class=\""+QClass+"\" valign=\"top\"")));

		if(dispType.equals("DragSlider") || dispType.equals("ComboBox") || dispType.equals("TextBox"))
		{
			tdWidth=" width=\"300\"";
			outputBuffer.append((new StringBuilder(tdWidth)));
		}
		outputBuffer.append((new StringBuilder(">")));

		if(currentQuestionValue != "")
		{
			outputBuffer.append((new StringBuilder("\n Q. ")));
		}

		outputBuffer.append((new StringBuilder(currentQuestionValue+"</td>")));

		//If DisplayType is textbox
		if(dispType.equals("TextBox"))
		{
			outputBuffer.append((new StringBuilder("\n <td width=\"100\">")));
			outputBuffer.append((new StringBuilder("\n <input id=\"txt"+currentQID+"\" type=\"text\" class=\"NestedTextbox\">")));
			outputBuffer.append((new StringBuilder("\n </td><td width=\"100\">")));
			outputBuffer.append((new StringBuilder("\n <input id=\"btn"+currentQID+"\" type=button class=\"NestedButton\" name=\"btn"+ currentQID +"\" value=\"save\"")));
			outputBuffer.append((new StringBuilder("\n  onclick=\"sendOverResponse('" + currentQID + "',document.getElementById('txt" + currentQID +"').value,null);"+"checkNestedMultipleQuestions('" + currentNestedQID + "');checkAnswered('" + currentQID + "');checkNestedSet('"+currentNextSet+ "');\">")));
			outputBuffer.append((new StringBuilder("\n </td>")));
			outputBuffer.append((new StringBuilder("\n <td></td>")));
		}

		//If DisplayType is button
		else if(dispType.equals("Button"))
		{
			String[] values = answerValue.split(":");
			//int width = 100/values.length-1;

			outputBuffer.append((new StringBuilder("\n <tr><td>")));
			outputBuffer.append((new StringBuilder("\n <table width=\"65%\" ><tr>")));

			for(int counter = 0;counter<values.length;counter++)
			{
				String onClick = "sendOverResponse('" + currentQID + "','" + values[counter] + "',null);checkNestedQuestions('"+ values[counter] +"','"+ values[counter] +"','"+ nextQID[counter] + "');"+"checkNestedMultipleQuestions('"+ currentNestedQID + "');checkAnswered('" + currentQID+ "');checkNestedSet('"+ currentNextSet + "');";

				outputBuffer.append((new StringBuilder("\n <td>")));
				outputBuffer.append((new StringBuilder("\n <input type=\"button\" value=\"" + values[counter] + "\"")));
				outputBuffer.append((new StringBuilder("\n  onclick=\""+onClick+"\"")));
				outputBuffer.append((new StringBuilder("\n  class=\"buttonclass\" ")));
				outputBuffer.append((new StringBuilder("\n ></td>")));
			}
			outputBuffer.append((new StringBuilder("\n </tr></table>")));
		}

		//If DisplayType is Radio
		else if(dispType.equals("Radio"))
		{
			String[] values = answerValue.split(":");
			String radioName = "rad-"+currentQID;

			outputBuffer.append((new StringBuilder("\n <tr><td>")));
			outputBuffer.append((new StringBuilder("\n <table width=\"100%\"><tr>")));

			for(int counter=0;counter<values.length;counter++)
			{
				int width = 100/values.length-1;
				String onClick = "sendOverResponse('"+ currentQID +"','" + values[counter] + "',null);"+"checkNestedQuestions('"+ values[counter] +"','"+ values[counter] +"','"+ nextQID[counter] +"','"+divSetID+"');checkNestedMultipleQuestions('"+ currentNestedQID +"');checkAnswered('" + currentQID+ "');checkNestedSet('"+currentNextSet+ "');";
				String radioId = "rad-"+currentQID+"-"+values[counter];

				outputBuffer.append((new StringBuilder("\n <td width = \""+width+"%\">")));
				outputBuffer.append((new StringBuilder("\n <input type = \"radio\" value = \""+values[counter]+"\" ")));
				outputBuffer.append((new StringBuilder("  id = \""+radioId+"\" ")));
				outputBuffer.append((new StringBuilder("  name = \""+radioName+"\" ")));
				outputBuffer.append((new StringBuilder("  onclick = \""+onClick+"\"")));
				outputBuffer.append((new StringBuilder("  class=\"radStyleClass\" >"+values[counter]+"</td>")));
			}
			outputBuffer.append((new StringBuilder("\n</tr></table></td>")));
		}

		//If DisplayType is ComboBox
		else if(dispType.equals("ComboBox"))
		{
			String[] values=answerValue.split(":");
			String comboName = "cbo-"+currentQID;
			String strOnChange= "forComboCNQ('" + currentNextQID + "','" + comboName + "','" + currentQID + "','" + currentNextSet + "','" + currentNestedQID + "');";

			outputBuffer.append((new StringBuilder("\n <td>")));//width = \"" + width + "% \"
			outputBuffer.append((new StringBuilder("\n <select id=\""+ comboName + "\" name=\"" + comboName + "\" size=1 class=\"cboClass \"")));
			outputBuffer.append((new StringBuilder("  onChange=\"" + strOnChange + "\">")));
			outputBuffer.append((new StringBuilder("\n <option>-- Select --</option>")));

			for(int counter=0;counter<values.length;counter++)
			{
				outputBuffer.append((new StringBuilder("\n <option value=\""+values[counter]+"\">"+ values[counter] +"</option>")));
			}

			outputBuffer.append((new StringBuilder("\n </td>")));
		//	outputBuffer.append((new StringBuilder("\n <td width=\"40%\"></td>";
		}

		//If DisplayType is dragslider
		else if(dispType.equals("DragSlider"))
		{
			String sliderid = ""+currentQID;
			String slider_activevalue = "slider"+sliderid+"_activeValue";
			String topconst = "0";
			String	bottomconst = "450";
			String	bg = "slider-"+sliderid;
			String[] values=null;
			String keyincr=null;

			if(answerValue != null && answerValue.length() > 0 && answerValue!="Numeric")
			{
				values = answerValue.split(":");
			}

			if(values!=null && answerValue!="Numeric")
			{
				keyincr = ""+500/values.length;
			}
			else if(answerValue.equals("Numeric"))
			{
				keyincr = "1";
			}

			outputBuffer.append((new StringBuilder("\n <td width = \"500\">")));
			outputBuffer.append((new StringBuilder("\n <table width = \"100%\"><tr>")));
			outputBuffer.append((new StringBuilder("\n <td>")));
			outputBuffer.append((new StringBuilder("\n <div id = \""+bg+"-bg\" class=sliderbg >")));
			outputBuffer.append((new StringBuilder("\n <table width=100% cellpadding=0 cellspacing=0>")));
			outputBuffer.append((new StringBuilder("\n <tr><td height=8></td></tr>")));
			outputBuffer.append((new StringBuilder("\n <tr>")));

			if(answerValue != null && answerValue.length() > 0 && answerValue!="Numeric")
			{
				for(int counter=0;counter<values.length;counter++)
				{
					int width=100/values.length;
					outputBuffer.append((new StringBuilder("\n <td class = \"sliderOption\" width = \""+width+"%\">")));
					outputBuffer.append((new StringBuilder( values[counter]+"</td>")));
				}
			}

			else if(answerValue.equals("Numeric"))
			{
				outputBuffer.append((new StringBuilder("\n <td class = \"sliderOption\" width=\"100%\"></td>")));
			}

			outputBuffer.append((new StringBuilder("\n </tr>")));
			outputBuffer.append((new StringBuilder("\n </table>")));
			outputBuffer.append((new StringBuilder("\n <div id = \""+bg+"-thumb\" class=sliderthumb >")));
			outputBuffer.append((new StringBuilder("\n <table cellpadding=0 cellspacing=0 valign=bottom >")));

			if(answerValue != null && answerValue.length() > 0 && answerValue!="Numeric")
			{
				outputBuffer.append((new StringBuilder("\n <tr><td><img class=\"slideImg\" src=\"../assets/greySliderTL.gif\" ")));
				outputBuffer.append((new StringBuilder("\n onload=\"loadinit("+topconst+","+bottomconst+","+keyincr+",'"+bg+"','"+ sliderid+"','"+answerValue+"','"+currentNextQID+"','"+setEnd+"','"+currentNextSet+"','"+divSetID+"','"+currentNestedQID+"')\"></td>")));
			}
			else if(answerValue.equals("Numeric"))
			{
				outputBuffer.append((new StringBuilder("\n <tr><td><img class=\"slideImg\" src=\"../assets/greySliderTL.gif\" ")));
				outputBuffer.append((new StringBuilder("\n onload=\"loadinitnumeric("+topconst+","+bottomconst+","+keyincr+",'"+bg+"','"+ sliderid+"','"+nextQID+"','"+setEnd+"','"+currentNextSet+"','" + divSetID +"','" + currentNestedQID+"')\"></td>")));
			}

			if(Integer.parseInt(keyincr)<50)
			{
				outputBuffer.append((new StringBuilder("\n <td class=\"sliderTC\"  width=\"50\"></td>")));
			}
			else
			{
				outputBuffer.append((new StringBuilder("\n <td class=\"sliderTC\"  width=\""+keyincr+"\"></td>")));
			}

			outputBuffer.append((new StringBuilder("\n <td><img class=\"slideImg\" src=\"../assets/greySliderTR.gif\"></td>")));
			outputBuffer.append((new StringBuilder("\n </tr>")));
			outputBuffer.append((new StringBuilder("\n <tr><td><img class=\"slideImg\" src=\"../assets/greySliderL.gif\"></td>")));

			if(answerValue != null && answerValue.length() > 0 && answerValue!="Numeric")
			{
				outputBuffer.append((new StringBuilder("\n <td class=\"sliderOption\" id=\""+slider_activevalue+"\"></td>")));
			}
			else if(answerValue.equals("Numeric"))
			{
				outputBuffer.append((new StringBuilder("\n <td class=\"sliderOption\" id=\""+slider_activevalue+"\">$0k</td>")));
			}

			outputBuffer.append((new StringBuilder("\n <td><img class=\"slideImg\" src=\"../assets/greySliderR.gif\"></td>")));
			outputBuffer.append((new StringBuilder("\n </tr>")));
			outputBuffer.append((new StringBuilder("\n <tr><td><img class=\"slideImg\" src=\"../assets/greySliderBL.gif\"></td>")));
			outputBuffer.append((new StringBuilder("\n <td class=\"sliderBC\" ></td>")));
			outputBuffer.append((new StringBuilder("\n <td><img class=\"slideImg\" src=\"../assets/greySliderBR.gif\"></td>")));
			outputBuffer.append((new StringBuilder("\n </tr>")));
			outputBuffer.append((new StringBuilder("\n </table>")));
			outputBuffer.append((new StringBuilder("\n </div></div>")));
			outputBuffer.append((new StringBuilder("\n </tr></table>")));
		}

		outputBuffer.append((new StringBuilder("\n </tr></table></div>")));
		try
		{
			out.print(outputBuffer);
		}
		catch(Exception e)
		{
			System.out.println("Error in displayRightTable Set "+e.toString());
		}
	}
		
	public int doEndTag() throws JspException 
	{
		return super.doEndTag();
	}
	
	public void setFileName(String fileName)
	{
		this.fileName=fileName;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	private void destroyObjects()
    {
		displayType=null;
		outputBuffer = null;
		fileName=null;
    }
	
	public void release()
    {
        super.release();
    }

	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
