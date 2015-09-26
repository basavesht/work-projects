package com.tcs.ebw.taglib;
import com.tcs.ebw.taglib.TreeDataInterface;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.common.util.StringUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletConfig;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.InputStream;


public class EbwTree extends TagSupport
{
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  Object[] treeData;
  Object formobj=null;
  TreeDataInterface td;
  String formName,name;
  Class c;
  InputStream stream;
  
  private String ajaxCall;
  private String[] hiddenArray;  
  private String parentString,childString,displayString,nodeIdString;
  private Method ParentMethod;
  private Method ChildMethod;
  private Method DisplayMethod;
  private Method NodeIDMethod;
  private Method [] HiddenMethod;
  private int hiddenArrayLength=0; 
  private StringBuffer outputBuffer=null;
  
  private HashMap<Object,Object> m_colNodeParentElement = new HashMap<Object,Object>();
  private HashMap<Object,Object> m_colNodeParentId = new HashMap<Object,Object>();

   public EbwTree () 
	{
    }
   
   public EbwTree(TreeDataInterface xtd)
    {
	  td=xtd;
    }

   public void setFormName (String xformName) 
	{
	  formName = xformName;
    }

   public String getFormName () 
	{
      return formName;
    }

   public void setName (String xName) 
	{
	  name = xName;
    }

   public String getName () 
	{
     return name;
	}
   public int doStartTag()
        throws JspException
        {
	   		EBWLogger.logDebug(this,"Entered into doStartTag Method Of Tree Taglib");
	   		try {	   			
	   			if(!getAjaxCall().equalsIgnoreCase("true"))
	   				formobj = pageContext.getRequest().getAttribute(formName);   			
	   					
				Class frmclass = formobj.getClass();
				td = (TreeDataInterface)frmclass.getMethod("get" + StringUtil.initCaps(name) + "TreeData", null).invoke(formobj, null);
			}
	   		catch (Exception e) {
	            e.printStackTrace();
	   			//EBWLogger.logDebug(e,"Error in getting Tree interface from Form");
			}
	        Object[] treeDataArray=(java.lang.Object[])(td.getDataArray());
	       
	   		Document document = constructTree(treeDataArray);
	   		
	   		try {
				//	 This following lines of code helps in transforming xml to html
				ResourceBundle file = ResourceBundle.getBundle("ebw");//reading the XSL file required for transforming from a properties
				String value = file.getString("XSLFile");
				Source xsltSource=null;
				if(!getAjaxCall().equalsIgnoreCase("true"))
					xsltSource = new StreamSource(pageContext.getServletContext().getResourceAsStream(value));
				else{					
					xsltSource=new StreamSource(stream);
				}
				TransformerFactory transfac = TransformerFactory.newInstance();
				Transformer trans = transfac.newTransformer(xsltSource);
				trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				trans.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source = new DOMSource(document);
				JspWriter jspout=null;
				if(!getAjaxCall().equalsIgnoreCase("true")){
					jspout = pageContext.getOut();
					trans.transform(source, new StreamResult(jspout));
				}else{
					StringBuffer output=new StringBuffer();
					java.io.File f=new java.io.File("output.txt");
					StreamResult result=new StreamResult(f);
					trans.transform(source,result);
					try{
						java.io.BufferedReader readFile=new java.io.BufferedReader(new java.io.FileReader("output.txt"));
						String l;
						while ((l = readFile.readLine()) != null) {
			            	output.append(l);	
			            	output.append("\r\n");
			            }
			            setOutputBuffer(output);			            
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	   		return 0;
        }
   
    
   /**
    * this method loops through the tree data content and constructs tree corresponding to that.
    * @param td
    */ 
   public Document constructTree(Object[] a_arrTreeData)
    {
	    Document document = null;   
	    if(a_arrTreeData.length!=0)   
			{
			   try  {
		             c = a_arrTreeData[0].getClass();
		             
		             //node id method
		             nodeIdString=td.getNodeIdField();
				     String NodeIdMet="get"+nodeIdString.substring(0,1).toUpperCase() + nodeIdString.substring(1, nodeIdString.length());
				     NodeIDMethod=c.getMethod(NodeIdMet,null);
		             
		             //parent Method
				     parentString=td.getParentField();
				     String ParentMet="get"+parentString.substring(0,1).toUpperCase() + parentString.substring(1, parentString.length());
				     ParentMethod=c.getMethod(ParentMet,null);

				   	 
				     //childMethod
				     childString=td.getChildField();
					 String ChildMet="get"+childString.substring(0,1).toUpperCase() + childString.substring(1, childString.length());
                     ChildMethod=c.getMethod(ChildMet,null);

					 //displayString method
                     displayString=td.getDisplayField();
					 String DisplayMet="get"+displayString.substring(0,1).toUpperCase() + displayString.substring(1, displayString.length());
                     DisplayMethod=c.getMethod(DisplayMet,null);
					 
                   
					 //hidden field methods
                     if(td.getHiddenFields()!=null) {
						 hiddenArray=td.getHiddenFields();
						 hiddenArrayLength = hiddenArray.length;
						 String [] HiddenMet=new String[hiddenArrayLength];
						 HiddenMethod=new Method[hiddenArrayLength];
						 for(int h=0;h<hiddenArrayLength;h++)
						 {
							 HiddenMet[h]="get"+hiddenArray[h].substring(0,1).toUpperCase() + hiddenArray[h].substring(1, hiddenArray[h].length());
							 HiddenMethod[h]=c.getMethod(HiddenMet[h],null);
						 }
					}
                     
                    //call method to construct dom structure for transformation
                    document = constructDOM(a_arrTreeData);
                    
                    //this following method prints xml for generated document
                    writeXMLFile(document);
 
			   }catch  (NoSuchMethodException e) 
				{
					//EBWLogger.logDebug(e,"Method not found Exception Occured");
					e.printStackTrace();
				}   
			   catch  (FactoryConfigurationError e) 
				{
					//EBWLogger.logDebug(e,"Error");
				}
			   catch  (TransformerFactoryConfigurationError e) 
				{
					//EBWLogger.logDebug(e,"Error");
				} 
			}
		
	    return document;
    }
   
   /**
    * This method helps in constructing xml correponding to tree
    * @return
    */
   public Document constructDOM(Object[] a_objTreeData)
   {
	   Document document = null;
	   String l_strParentValue = "";
	   String l_strChildValue = "";
	   String l_strNodeIdValue = "";
	   String l_strDisplayValue = "";
	   String hiddenObjects[] = null;
		if(td.getHiddenFields()!=null)  {
			 hiddenObjects = new String[hiddenArrayLength];
		}
		
	   try {
			   DocumentBuilderFactory documentBuilderFactory =  DocumentBuilderFactory.newInstance();
			   DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			   String root = "root";
			   document = documentBuilder.newDocument();
			   Element rootElement = document.createElement(root);
			   document.appendChild(rootElement);
			   
			   //parent field
			   Node item = document.createElement("ParentField");
			   Node value = document.createTextNode(parentString);
			   item.appendChild(value);
			   rootElement.appendChild(item);
				
				//child field
			   item = document.createElement("ChildField");
			   value = document.createTextNode(childString);
			   item.appendChild(value);
			   rootElement.appendChild(item);
			   
			   //Hidden Fields
			   item = document.createElement("HiddenFields");
			   if(td.getHiddenFields()!=null)  {
		             for(int m=0;m<hiddenArrayLength;m++)
		                {
                         String str = new Integer(m).toString();
						 Node item2 = document.createElement("f"+str);
                         item.appendChild(item2);
                         value = document.createTextNode(hiddenArray[m]);
				         item2.appendChild(value);
					    }
               }
			   rootElement.appendChild(item);
			   
			   for(int i = 0; i < a_objTreeData.length; i++)
			   {
				   l_strNodeIdValue=((Object)NodeIDMethod.invoke(a_objTreeData[i],null)).toString();
				   l_strParentValue=((Object)ParentMethod.invoke(a_objTreeData[i],null)).toString();
				   l_strChildValue=((Object)ChildMethod.invoke(a_objTreeData[i],null)).toString();
				   l_strDisplayValue=((Object)DisplayMethod.invoke(a_objTreeData[i],null)).toString();
				   
				   EBWLogger.logDebug(this,"Node ID Value " + l_strNodeIdValue);
				   EBWLogger.logDebug(this,"parent Value " + l_strParentValue);
				   EBWLogger.logDebug(this,"child Value " + l_strChildValue);
				   EBWLogger.logDebug(this,"....................... ");
				   
				   
				   if(hiddenArrayLength!=0)	{
					for(int l=0;l<hiddenArrayLength;l++)
						{
			              hiddenObjects[l]=((Object)HiddenMethod[l].invoke(a_objTreeData[i],null)).toString();
			            }
					}
				   
				   Element child = null;
				   String l_tmpParentId = null;
				   Element parentElement = null;
				   if(l_strParentValue.equalsIgnoreCase("0"))
				   {
					    child = document.createElement("element");
					    parentElement = rootElement;
					    parentElement.appendChild(child);
				   }else
				   {
					   parentElement = (Element)m_colNodeParentElement.get(l_strParentValue);
					   l_tmpParentId = (String)m_colNodeParentId.get(l_strParentValue);
					   child = document.createElement("element");
				   }
						
				  //displayid
				  item = document.createElement("displayId");
			      value = document.createTextNode(l_strDisplayValue);
				  item.appendChild(value);
				  child.appendChild(item);
						
				  //childId
				  item = document.createElement("childId");
				  value = document.createTextNode(l_strChildValue);
				  item.appendChild(value);
				  child.appendChild(item);
				
				  //parentid
				  item = document.createElement("parentId");
				  if(l_strParentValue.equalsIgnoreCase("0"))
				  {
					  value = document.createTextNode(l_strParentValue);
					  item.appendChild(value);
					  child.appendChild(item);
				  }else
				  {
					  value = document.createTextNode(l_tmpParentId);
					  item.appendChild(value);
					  child.appendChild(item);
				  }
						
				  //displayField
			      item = document.createElement("displayField");
				  value = document.createTextNode(l_strDisplayValue);
				  item.appendChild(value);
				  child.appendChild(item);
						
				  //adding hidden fields
				  if(hiddenArrayLength!=0) {
			          for(int j=0;j<hiddenArrayLength;j++)
			          {
						item = document.createElement(hiddenArray[j]);
			            value = document.createTextNode(hiddenObjects[j].toString());
					    item.appendChild(value);
						child.appendChild(item);
					  }
				 }
				 parentElement.appendChild(child);
    			 m_colNodeParentElement.put(l_strNodeIdValue, child);
				 m_colNodeParentId.put(l_strNodeIdValue, l_strChildValue);
			}
			   
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   
	   return document;
   }

	//function to convert back the xml from the document(memory)
	public void writeXMLFile(Document doc)
	{
		try{
			
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");

			// create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();

			 // print xml
		   System.out.println("Here's the xml:\n\n" + xmlString);
		   } catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
	public StringBuffer getTreeThruAjax(HashMap attributeMap,HttpSession sessionObject,Object formObj,InputStream stream){		
		EbwTree tree=new EbwTree();
		tree.setName((String)attributeMap.get("name"));
		tree.setFormName((String)attributeMap.get("formname"));
		tree.setAjaxCall((String)attributeMap.get("ajaxCall"));
		tree.setFormobj(formObj);
		tree.setStream(stream);
		try{
			tree.doStartTag();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("BUFFER FROM TAGLIB"+tree.getOutputBuffer());
		return tree.getOutputBuffer();		
	}

	public String getAjaxCall() {
		return ajaxCall;
	}

	public void setAjaxCall(String ajaxCall) {
		this.ajaxCall = ajaxCall;
	}

	public Object getFormobj() {
		return formobj;
	}

	public void setFormobj(Object formobj) {
		this.formobj = formobj;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}

	public StringBuffer getOutputBuffer() {
		return outputBuffer;
	}

	public void setOutputBuffer(StringBuffer outputBuffer) {
		this.outputBuffer = outputBuffer;
	}

}