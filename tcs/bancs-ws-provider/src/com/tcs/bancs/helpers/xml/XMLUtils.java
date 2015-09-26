package com.tcs.bancs.helpers.xml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLUtils 
{
	private static final Logger logger = Logger.getLogger(XMLUtils.class);

	public static Document parse(InputStream xmlDoc) throws ConfigXMLParsingException 
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

			InputSource is = new InputSource();
			is.setByteStream(xmlDoc);
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

}
