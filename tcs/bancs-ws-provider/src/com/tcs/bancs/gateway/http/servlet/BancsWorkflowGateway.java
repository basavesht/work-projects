package com.tcs.bancs.gateway.http.servlet;

import java.io.FileNotFoundException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sun.xml.ws.transport.http.servlet.WSServlet;
import com.tcs.bancs.config.handler.WorkflowHandlerConfig;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;

public class BancsWorkflowGateway extends WSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -888580220847111058L;
	private static final Logger logger = Logger.getLogger(BancsWorkflowGateway.class);

	public BancsWorkflowGateway(){
		workflowHandlerConfig = null;
	}

	public void init(ServletConfig config) throws ServletException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("init(ServletConfig) - start");
		}
		this.context = config.getServletContext();
		servletConfig = config;

		super.init(config);
		String configFile = servletConfig.getInitParameter("workflowHandlerConfig");
		configFile = context.getRealPath(configFile);
		workflowHandlerConfig = WorkflowHandlerConfig.getInstance();
		try {
			workflowHandlerConfig.parse(configFile);
			servletConfig.getServletContext().setAttribute("handlerConfig", workflowHandlerConfig);
		}
		catch (FileNotFoundException e) {
			throw new ServletException("WebService config file not found ..",e);
		} 
		catch (ConfigXMLParsingException e) {
			throw new ServletException("WebService config file(XML) parsing exception ..",e);
		} 

		if (logger.isDebugEnabled()) {
			logger.debug("init(ServletConfig) - end");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			checkConfig(servletConfig);
		} catch (ConfigXMLParsingException e) {
			e.printStackTrace();
		}
		super.doPost(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			checkConfig(servletConfig);
		} catch (ConfigXMLParsingException e) {
			e.printStackTrace();
		}
		super.doGet(request, response);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			checkConfig(servletConfig);
		} catch (ConfigXMLParsingException e) {
			e.printStackTrace();
		}
		super.doPut(request, response);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			checkConfig(servletConfig);
		} catch (ConfigXMLParsingException e) {
			e.printStackTrace();
		}
		super.doDelete(request, response);
	}

	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			checkConfig(servletConfig);
		} catch (ConfigXMLParsingException e) {
			e.printStackTrace();
		}	
		super.doHead(request, response);
	}

	public void checkConfig(ServletConfig servletConfig) throws ConfigXMLParsingException 
	{
		if(workflowHandlerConfig == null){
			String configFile = servletConfig.getInitParameter("workflowHandlerConfig");
			configFile = context.getRealPath(configFile);
			workflowHandlerConfig = WorkflowHandlerConfig.getInstance();
			try {
				workflowHandlerConfig.parse(configFile);
				servletConfig.getServletContext().setAttribute("handlerConfig", workflowHandlerConfig);
			} catch (FileNotFoundException e) {
				throw new ConfigXMLParsingException("WebService config file not found ..",e);
			} catch (ConfigXMLParsingException e) {
				throw new ConfigXMLParsingException("WebService config file(XML) parsing exception ..",e);
			} 
		}
		else {
			try {
				workflowHandlerConfig.checkNew();
				servletConfig.getServletContext().setAttribute("handlerConfig", workflowHandlerConfig);
			} catch (FileNotFoundException e) {
				throw new ConfigXMLParsingException("WebService config file not found ..",e);
			} catch (ConfigXMLParsingException e) {
				throw new ConfigXMLParsingException("WebService config file(XML) parsing exception ..",e);
			} 
		}
	}

	private WorkflowHandlerConfig workflowHandlerConfig = null;
	private static ServletConfig servletConfig = null;
	private ServletContext context = null;
}
