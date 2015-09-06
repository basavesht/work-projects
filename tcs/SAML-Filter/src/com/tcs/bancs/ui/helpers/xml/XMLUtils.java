package com.tcs.bancs.ui.helpers.xml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
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


public class XMLUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(XMLUtils.class);

	public static Document parse(byte[] contextData, String xsdDoc) throws ContextDataParsingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("parse(byte[], String) - start"); //$NON-NLS-1$
		}

		FileReader xsdReader = null;
		try
		{
		DocumentBuilderFactory dbf =
			DocumentBuilderFactory.newInstance(); 
		dbf.setValidating(true);
		dbf.setNamespaceAware(false);
		
		dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        
        // Specify our own schema - this overrides the schemaLocation in the xml file
        dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", "file:" + xsdDoc);
        
        /***

		SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		StreamSource xsds = new StreamSource();
		try {
			xsdReader = new FileReader(xsdDoc);
			xsds.setReader(xsdReader);
		} catch (FileNotFoundException e) {
				logger.error("parse(byte[], String)", e); //$NON-NLS-1$

			throw new ContextDataParsingException("Error setting reader for:" + xsdDoc, e);
		}
		xsds.setPublicId("XSD");

		Schema schemaXSD;
		try {
			schemaXSD = schemaFactory.newSchema(xsds);
		} catch (SAXException e) {
				logger.error("parse(byte[], String)", e); //$NON-NLS-1$

			throw new ContextDataParsingException("Error setting XSD schema", e);

		}

		dbf.setSchema(schemaXSD);

		**/
        
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
				logger.error("parse(byte[], String)", e); //$NON-NLS-1$

			throw new ContextDataParsingException("Error creating DocumentBuilder", e);
		}



		db.setErrorHandler(new ErrorHandler() {
			public void error(SAXParseException e) throws SAXParseException {
					if (logger.isDebugEnabled()) {
						logger.debug("$ErrorHandler.error(SAXParseException) - start"); //$NON-NLS-1$
					}

				throw e;
			}

			public void fatalError(SAXParseException e) throws SAXException {
					if (logger.isDebugEnabled()) {
						logger.debug("$ErrorHandler.fatalError(SAXParseException) - start"); //$NON-NLS-1$
					}

				throw e;

			}

			public void warning(SAXParseException e) throws SAXException {
			}
		});

		String xmlDoc = new String(contextData);
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlDoc));
		is.setPublicId("XML");
		Document doc;
		try {
			doc = db.parse(is);
		} catch (SAXException e) {
				logger.error("parse(byte[], String)", e); //$NON-NLS-1$

			throw new ContextDataParsingException("Error parsing XML", e);
		} catch (IOException e) {
				logger.error("parse(byte[], String)", e); //$NON-NLS-1$

			throw new ContextDataParsingException("IO Exception received while parsing XML", e);
		}

			if (logger.isDebugEnabled()) {
				logger.debug("parse(byte[], String) - end"); //$NON-NLS-1$
			}
		return doc;
		}
		finally
		{
			if(xsdReader != null)
			{
				try {
					xsdReader.close();
				} catch (IOException e) {
					logger.warn("parse(byte[], String) - exception ignored", e); //$NON-NLS-1$
				}
			}
		}
	}
	public static Document parse(InputStream xmlDoc) throws ConfigXMLParsingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("parse(InputStream) - start"); //$NON-NLS-1$
		}

		try {
			DocumentBuilderFactory dbf =
				DocumentBuilderFactory.newInstance(); 
			dbf.setValidating(false);
			dbf.setNamespaceAware(false);


			DocumentBuilder db = dbf.newDocumentBuilder();



			db.setErrorHandler(new ErrorHandler() {
				public void error(SAXParseException e) throws SAXParseException {
					if (logger.isDebugEnabled()) {
						logger.debug("$ErrorHandler.error(SAXParseException) - start"); //$NON-NLS-1$
					}

					throw e;
				}

				public void fatalError(SAXParseException e) throws SAXException {
					if (logger.isDebugEnabled()) {
						logger.debug("$ErrorHandler.fatalError(SAXParseException) - start"); //$NON-NLS-1$
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
				logger.debug("parse(InputStream) - end"); //$NON-NLS-1$
			}
			return doc;
		} catch (FileNotFoundException e) {
			logger.error("parse(InputStream)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException(e);
		} catch (SAXException e) {
			logger.error("parse(InputStream)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException(e);
		} catch (ParserConfigurationException e) {
			logger.error("parse(InputStream)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException(e);
		} catch (IOException e) {
			logger.error("parse(InputStream)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException(e);
		}
	}
	public static String getAttribute(Node node, String attribute) {
		return	(node.getAttributes() != null && 
				 node.getAttributes().getNamedItem(attribute)!=null && 
				 node.getAttributes().getNamedItem(attribute).getNodeValue() !=null &&
				 node.getAttributes().getNamedItem(attribute).getNodeValue().length() > 0)
			 ?node.getAttributes().getNamedItem(attribute).getNodeValue():null;
	}

}
