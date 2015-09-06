package com.bosch.tmp.integration.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.hl7.v2xml.ACKBATCHCONTENT;
import org.hl7.v2xml.ACKCONTENT;
import org.hl7.v2xml.ADTA01CONTENT;
import org.hl7.v2xml.ADTA03CONTENT;
import org.hl7.v2xml.ORUR30BATCHCONTENT;
import org.hl7.v2xml.QVRQ17CONTENT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author gtk1pal
 */
@Configurable
public class MessageConverter
{

    public static final Logger logger = LoggerFactory.getLogger(MessageConverter.class);

    @Autowired
    @Qualifier("unmarshaller")
    private Unmarshaller unmarshaller;

    @Autowired
    @Qualifier("marshaller")
    private Marshaller marshaller;

    /*
     * Converting Java-representation of HL7 message back to its
     * raw XML message using JAXB marshalling.
     * This is not ideal solution to persist raw XML message, but
     * could be used in case persisting raw message at the JAX-WS
     * layer doesn't work.
     */
    public String hl7ToXml(Object object) throws Exception
    {
        // the property JAXB_FORMATTED_OUTPUT specifies whether or not the
        // marshalled XML data is formatted with linefeeds and indentation
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter sw = new StringWriter();

        String objType = MessageUtils.getMessageName(object);

        // marshal the data in the Java content tree to the string writer
        marshaller.marshal(new JAXBElement(new QName("urn:hl7-org:v2xml", objType),
                object.getClass(), object), sw);

        sw.close();

        logger.debug("Marshalled raw data: " + sw);

        return sw.toString();
    }

    /*
     * Converting XML string to Java-representation of HL7 message using JAXB unmarshalling.
     */
    public Object xmlToHl7(String xml) throws Exception
    {
        logger.debug("Unmarshalling xml: " + xml);

        // unmarshal the xml bytes to Java object
        JAXBElement jaxbElement = (JAXBElement) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        return (jaxbElement == null) ? null : jaxbElement.getValue();
    }

    /*
     * Converting Java-representation of HL7 message back to its
     * raw XML message using JAXB marshalling.
     * This is not ideal solution to persist raw XML message, but
     * could be used in case persisting raw message at the JAX-WS
     * layer doesn't work.
     */
    public static String convertObjectToXml(Object object, String objType) throws Exception
    {
        // create a JAXBContext for the 'cls' class
        JAXBContext ctx = JAXBContext.newInstance(object.getClass().getPackage().getName());

        // create a marshaller
        Marshaller marshaller = ctx.createMarshaller();

        // the property JAXB_FORMATTED_OUTPUT specifies whether or not the
        // marshalled XML data is formatted with linefeeds and indentation
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter sw = new StringWriter();

        // marshal the data in the Java content tree to the string writer
        marshaller.marshal(new JAXBElement(new QName("urn:hl7-org:v2xml", objType),
                object.getClass(), object), sw);

        sw.close();

        logger.debug("Marshalled raw data: " + sw);

        return sw.toString();
    }

    /*
     * Converting XML string to Java-representation of HL7 message using JAXB unmarshalling.
     */
    public static Object convertXmlToObject(String xml, String packageName) throws Exception
    {
        logger.debug("Unmarshalling xml: " + xml);

        // create a JAXBContext for the package
        JAXBContext ctx = JAXBContext.newInstance(packageName);

        // create an unmarshaller
        Unmarshaller unmarshaller = ctx.createUnmarshaller();

        // unmarshal the xml bytes to Java object
        JAXBElement element = (JAXBElement) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));

        return element.getValue();
    }
}
