package com.tcs.bancs.config.executor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tcs.bancs.config.executor.attributes.ServiceHandlerAttributes;
import com.tcs.bancs.config.executor.attributes.SrvcMethod;
import com.tcs.bancs.helpers.ObservableConfiguration;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.helpers.xml.XMLUtils;

public class WorkflowExecutorConfig extends ObservableConfiguration 
{
	private static final Logger logger = Logger.getLogger(WorkflowExecutorConfig.class);
	private List<ServiceHandlerAttributes> serviceHandlers = new ArrayList<ServiceHandlerAttributes>();
	private ServiceHandlerAttributes handler = null;
	private static WorkflowExecutorConfig instance  = null;

	//Singleton class..
	private WorkflowExecutorConfig(){

	}

	//Single Instance creation..
	public static WorkflowExecutorConfig getInstance()
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String, boolean) - start"); 
		}

		if (instance == null){
			instance = new WorkflowExecutorConfig();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String, boolean) - end"); 
		}
		return instance;
	}

	@Override
	public void parseImpl(String configFileName) throws FileNotFoundException,ConfigXMLParsingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("parseImpl(String) - start");
		}

		InputStream is = null;
		try
		{
			is = new FileInputStream(configFileName);
			Document configDoc = XMLUtils.parse(is);
			NodeList nodes = configDoc.getElementsByTagName("WORKFLOW_EXECUTOR").item(0).getChildNodes();
			int nodes_length = nodes.getLength();
			for(int i = 0 ; i < nodes_length; i++)
			{
				Node child = nodes.item(i);
				if (child.getNodeName().equals("SERVICE")) 
				{
					handler = new ServiceHandlerAttributes();
					handler.setService_id(XMLUtils.getAttribute(child,"ID"));

					NodeList nodes_1 = child.getChildNodes();
					int nodes_length_1 = nodes_1.getLength();
					for(int j = 0 ; j < nodes_length_1; j++)
					{
						Node child_1 = nodes_1.item(j);
						if (child_1.getNodeName().equals("CLASS")) 
						{
							handler.setService_class(child_1.getFirstChild().getNodeValue());
						}
						else if (child_1.getNodeName().equals("METHOD")) 
						{
							SrvcMethod method = new SrvcMethod();

							NodeList nodes_2 = child_1.getChildNodes();
							int nodes_length_2 = nodes_2.getLength();
							for(int k = 0 ; k < nodes_length_2; k++)
							{
								Node child_2 = nodes_2.item(k);
								if (child_2.getNodeName().equals("NAME")){
									method.setName(child_2.getFirstChild().getNodeValue());
								}
								else if (child_2.getNodeName().equals("METHOD-PARAMS"))
								{
									List<Class> methodParams = new ArrayList<Class>();
									NodeList nodes_3 = child_2.getChildNodes();
									int nodes_length_3 = nodes_3.getLength();
									for(int l = 0 ; l < nodes_length_3; l++)
									{
										Node child_3 = nodes_3.item(l);
										if (child_3.getNodeName().equals("METHOD-PARAM")) 
										{
											String method_param = child_3.getFirstChild().getNodeValue();
											try {
												methodParams.add(Class.forName(method_param));
											} 
											catch (ClassNotFoundException e) {
												throw new ConfigXMLParsingException("METHOD-PARAM " + method_param + " is not proper", e);		
											}
										}
									}
									method.setMethodParams(toClassArray(methodParams.toArray()));
								}
							}
							handler.setService_method(method);
						}
					}
					validateServiceHandler(handler);
					serviceHandlers.add(handler);
				}
			}
		}
		catch (IOException e) {
			logger.error("WorkflowExecutorConfig(String)", e);
			throw new ConfigXMLParsingException("IOException received", e);		
		}
		finally
		{
			try {
				if (is !=null) 
					is.close();
			} 
			catch (IOException e) {
				logger.warn("WorkflowExecutorConfig(String)- exception ignored", e); 
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("parseImpl(String) - end");
		}
	}

	private void validateServiceHandler(ServiceHandlerAttributes handler) throws ConfigXMLParsingException {
		assertConfigForTag(handler==null,"SERVICE");
		assertConfigForAttribute(handler.getService_id()==null,"SERVICE","ID");
		assertConfigForChildTag(handler.getService_class()==null,"CLASS","SERVICE");
		assertConfigForChildTag(handler.getService_method()==null,"METHOD","SERVICE");
		assertConfigForChildTag(handler.getService_method().getName()==null,"METHOD","NAME");
		assertConfigForChildTag(handler.getService_method().getMethodParams()==null,"METHOD","METHOD-PARAMS");
	}

	private void assertConfigForTag(boolean raiseError, String tagName) throws ConfigXMLParsingException {
		if(raiseError) {
			throw new ConfigXMLParsingException("XML tag '" + tagName + "' must be specified in config XML");
		}
	}

	private void assertConfigForChildTag(boolean raiseError,String childTag,String parentTag) throws ConfigXMLParsingException {
		if(raiseError) {
			throw new ConfigXMLParsingException("XML Child tag '" + childTag + "' must be specified under parent Tag '"+ parentTag + "' in config XML");
		}
	}

	private void assertConfigForAttribute(boolean raiseError, String tagName, String attributeName) throws ConfigXMLParsingException {		
		if(raiseError) {
			throw new ConfigXMLParsingException("XML attribute '" + attributeName + "' must be specified for tag '" + tagName +"' in config XML");
		}
	}

	private Class[] toClassArray(Object[] methodParams){
		int count = 0;
		Class[] classParams = new Class[methodParams.length];
		for(Object param : methodParams) {
			classParams[count] = (Class)param ;
			count++;
		}
		return classParams;
	}

	public List<ServiceHandlerAttributes> getServiceHandlers() {
		return serviceHandlers;
	}

	public void setServiceHandlers(List<ServiceHandlerAttributes> serviceHandlers) {
		this.serviceHandlers = serviceHandlers;
	}

	public static void main(String args[]) throws FileNotFoundException, ConfigXMLParsingException{
		WorkflowExecutorConfig config = WorkflowExecutorConfig.getInstance();
		config.parseImpl("./WEB-INF/workflow-executor.xml");
	}

}
