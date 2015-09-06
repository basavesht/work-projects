package com.bosch.tmp.integration.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLUtils 
{
	private static final Logger logger = Logger.getLogger(XMLUtils.class);

	public static Document parse(String xmlDoc) throws ConfigXMLParsingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("parse(InputStream) - start");
		}
		try 
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
			dbf.setValidating(false);
			dbf.setNamespaceAware(false);

			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new ErrorHandler() {
				public void error(SAXParseException e) throws SAXParseException {
					if (logger.isDebugEnabled()) {
						logger.debug("$ErrorHandler.error(SAXParseException) - start");
					}
					throw e;
				}

				public void fatalError(SAXParseException e) throws SAXException {
					if (logger.isDebugEnabled()) {
						logger.debug("$ErrorHandler.fatalError(SAXParseException) - start");
					}
					throw e;
				}

				public void warning(SAXParseException e) throws SAXException {
				}
			});

			// convert String into InputStream
			InputStream is = new ByteArrayInputStream(xmlDoc.getBytes());
			Document doc = db.parse(is);

			if (logger.isDebugEnabled()) {
				logger.debug("parse(InputStream) - end"); 
			}
			return doc;
		} catch (FileNotFoundException e) {
			logger.error("parse(InputStream)", e); 
			throw new ConfigXMLParsingException(e);
		} catch (SAXException e) {
			logger.error("parse(InputStream)", e); 
			throw new ConfigXMLParsingException(e);
		} catch (ParserConfigurationException e) {
			logger.error("parse(InputStream)", e); 
			throw new ConfigXMLParsingException(e);
		} catch (IOException e) {
			logger.error("parse(InputStream)", e); 
			throw new ConfigXMLParsingException(e);
		}
	}
	public static String getAttribute(Node node, String attribute) 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getAttribute(Node, String) - start"); 
		}

		String returnString = (node.getAttributes() != null && node.getAttributes().getNamedItem(attribute) != null && node.getAttributes().getNamedItem(attribute).getNodeValue() != null && node.getAttributes().getNamedItem(attribute).getNodeValue().length() > 0) ? node.getAttributes().getNamedItem(attribute).getNodeValue() : null;
		if (logger.isDebugEnabled()) {
			logger.debug("getAttribute(Node, String) - end");
		}
		return	returnString;
	}
	
	public static String updateAckRequestCode(String rawDuplicateResponse,String ackRequestType)
	{
		String hl7MessageRawMessage = rawDuplicateResponse;
		ByteArrayOutputStream messageByteStream = new ByteArrayOutputStream();
		try 
		{
			Document payload = XMLUtils.parse((String)rawDuplicateResponse);
			NodeList nodes_0 = payload.getChildNodes();
			int nodes_length = nodes_0.getLength();
			for(int i = 0 ; i < nodes_length; i++)
			{
			    Node child = nodes_0.item(i);
			    if (child.getNodeName().equals("ns2:ACK"))
			    {
			        NodeList nodes_11 = child.getChildNodes();
			        int nodes_length11 = nodes_11.getLength();
			        for(int m = 0 ; m < nodes_length11; m++)
			        {
			            Node child11 = nodes_11.item(m);
			            if (child11.getNodeName().equals("ns2:MSH"))
			            {
			                NodeList nodes_1 = child11.getChildNodes();
			                int nodes_length1 = nodes_1.getLength();
			                for(int j = 0 ; j < nodes_length1; j++)
			                {
			                    Node child1 = nodes_1.item(j);
			                    if (child1.getNodeName().equals("ns2:MSH.16")) {
			                        child1.getFirstChild().setNodeValue(ackRequestType);
			                        break;
			                    }
			                }
			            }
			        }
			    }
			}

			// Initializing new Transformer and converting Document into String
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StreamResult streamResult = new StreamResult(messageByteStream);
			transformer.transform(new DOMSource(payload), streamResult);
			hl7MessageRawMessage = messageByteStream.toString();
			
		} 
		catch (Exception e) {
			logger.error("Could not update the Message ACK code in the raw message obtained from HL7Message table.");
		}
		return hl7MessageRawMessage;
	}
}
