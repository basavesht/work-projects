package com.bosch.tmp.integration.validation;

import com.sun.xml.ws.developer.ValidationErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

/**
 * This class is used to handle JAXB validation errors customization.
 * It can be specified in the SchemaValidation annotation of the web service endpoint.
 *
 * @author gtk1pal
 */
public class CDAValidationErrorHandler extends ValidationErrorHandler
{
    public static final Logger logger = LoggerFactory.getLogger(CDAValidationErrorHandler.class);

    public static final String WARNING = "CDAValidationWarning";
    public static final String ERROR = "CDAValidationError";
    public static final String FATAL_ERROR = "CDAValidationFatalError";

    public void warning(SAXParseException exception) throws SAXException
    {
        logger.debug("Validation WARNING:");
        logger.debug(exception.toString());
        packet.invocationProperties.put(WARNING, exception);
    }

    public void error(SAXParseException exception) throws SAXException
    {
        logger.debug("Validation ERROR:");
        logger.debug(exception.toString());
        packet.invocationProperties.put(ERROR, exception);
        //throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException
    {
        logger.debug("Validation FATAL_ERROR:");
        logger.debug(exception.toString());
        packet.invocationProperties.put(FATAL_ERROR, exception);
        //throw exception;
    }
}