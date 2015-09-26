package com.tcs.bancs.interceptors.logger;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import com.tcs.bancs.config.handler.WorkflowHandlerConfig;

public class RequestLogger implements SOAPHandler<SOAPMessageContext> {

	public void close(MessageContext context) {

	}

	public boolean handleFault(SOAPMessageContext context) {
		return false;
	}

	public boolean handleMessage(SOAPMessageContext context) 
	{
		try {
			logServletContext(context);
			logMessageDirection(context);
			logSOAPMessage(context);
			logSOAPFault(context);
		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	private void logServletContext(SOAPMessageContext context){
		Boolean isResponse = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(!isResponse){
			ServletContext servletContext = (ServletContext)context.get(MessageContext.SERVLET_CONTEXT);
			WorkflowHandlerConfig config = (WorkflowHandlerConfig)servletContext.getAttribute("handlerConfig");
			System.out.println("Workflow-Handler :\r\n" + config.toString());
		}
	}
	
	private void logMessageDirection(SOAPMessageContext context){
		Boolean messageProperty = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		System.out.println("Message Direction :\r\n" + messageProperty);
	}

	private void logSOAPMessage(SOAPMessageContext context) throws SOAPException, IOException {
		SOAPMessage message = context.getMessage();
		System.out.println("SOAP Message :\r\n" );
		message.writeTo(System.out);
	}

	private void logSOAPFault(SOAPMessageContext context) throws SOAPException, IOException  {
		Boolean isResponse = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(isResponse){
			SOAPMessage message = context.getMessage();
			SOAPBody soapBody = message.getSOAPPart().getEnvelope().getBody();
			SOAPFault soapFault = soapBody.getFault();
			if(soapFault!=null){
				System.out.println("SOAP Fault :\r\n" + soapFault.getFaultString());
			}
		}
	}
}
