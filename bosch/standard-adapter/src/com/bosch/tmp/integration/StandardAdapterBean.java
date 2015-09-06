package com.bosch.tmp.integration;

import javax.xml.ws.WebServiceContext;
import org.springframework.stereotype.Service;
import com.bosch.th.integration.StandardAdapterPortType;
import com.bosch.th.integration.PushResultsPortType;
import com.bosch.tmp.integration.creation.Creator;
import com.bosch.tmp.integration.creation.CreatorFactory;
import com.bosch.tmp.integration.persistence.HL7Message;
import com.bosch.tmp.integration.util.AckRequestCodeEnum;
import com.bosch.tmp.integration.persistence.HL7MessagePersister;
import com.bosch.tmp.integration.persistence.ResultPersister;
import com.bosch.tmp.integration.persistence.SessionResultPersister;
import com.bosch.tmp.integration.process.Processor;
import com.bosch.tmp.integration.process.ProcessorFactory;
import com.bosch.tmp.integration.util.AckTypeEnum;
import com.bosch.tmp.integration.util.MessageUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.handler.MessageContext;
import org.hl7.v2xml.MSHCONTENT;
import org.xml.sax.SAXParseException;
import com.bosch.tmp.integration.util.Error;
import com.bosch.tmp.integration.util.MessageConverter;
import com.bosch.tmp.integration.validation.CDAValidationErrorHandler;
import com.bosch.tmp.integration.validation.ConfigurationLoader;
import com.bosch.tmp.integration.validation.CustomerId;
import com.bosch.tmp.integration.validation.Validator;
import com.bosch.tmp.integration.validation.ValidatorFactory;
import java.io.ByteArrayInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.hl7.v2xml.BHSCONTENT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * StandardAdapterBean has the business logic for all the operations defined in
 * StandardAdapterEndpoint and PushResultsEndpoint web services.
 *
 * @author gtk1pal
 */
@Service
public class StandardAdapterBean implements StandardAdapterPortType, PushResultsPortType
{

    /**
     * logging runtime information using log4j.
     */
    public static final Logger logger = LoggerFactory.getLogger(StandardAdapterBean.class);

    // Get customer ID from configuration
    private static CustomerId customerId = (CustomerId) ConfigurationLoader.getCustomerId() == null ? CustomerId.SA : (CustomerId) ConfigurationLoader.
            getCustomerId();


    // The wsContext cannot be injected here, but needs to be retrieved from the endpoint.
    // The wsContext provides the ESB group ID and ESB message ID to identify
    // raw data that's already or will be inserted by JAX-WS handler into database.
    private WebServiceContext wsContext;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private ProcessorFactory processorFactory;

    @Autowired
    private ResultPersister resultPersister ;

    @Autowired
    private SessionResultPersister sessionResultPersister;

    public void setWsContext(WebServiceContext wsContext)
    {
        this.wsContext = wsContext;
    }

    /**
     * This method has business logic to process A04 and A08 hl7 messages.
     * @param part1 ADT_A01
     * @return ACK
     */
    public org.hl7.v2xml.ACKCONTENT adtA01Operation(org.hl7.v2xml.ADTA01CONTENT part1)
    {
        logger.debug("Invoking generic operation from adtA01Operation");
        return (org.hl7.v2xml.ACKCONTENT) genericOperation("adtA01Operation", part1);
    }

    /**
     * This method has business logic to process A03 hl7 message.
     * @param part1 ADT_A03
     * @return ACK
     */
    public org.hl7.v2xml.ACKCONTENT adtA03Operation(org.hl7.v2xml.ADTA03CONTENT part1)
    {
        logger.debug("Invoking generic operation from adtA03Operation");
        return (org.hl7.v2xml.ACKCONTENT) genericOperation("adtA03Operation", part1);
    }

    /**
     * This method has business logic to process A31 hl7 messages.
     * @param part1 ADT_A05
     * @return ACK
     */
    public org.hl7.v2xml.ACKCONTENT adtA05Operation(org.hl7.v2xml.ADTA05CONTENT part1)
    {
        logger.debug("Invoking generic operation from adtA05Operation");
        return (org.hl7.v2xml.ACKCONTENT) genericOperation("adtA05Operation", part1);
    }

    /**
     * This method has business logic to process QVR_Q17 hl7 message.
     * @param part1 QVR_Q17
     * @return ORU_R30 BATCH
     */
    public org.hl7.v2xml.ORUR30BATCHCONTENT qvrQ17Operation(org.hl7.v2xml.QVRQ17CONTENT part1)
    {
        logger.debug("Invoking generic operation from qvrQ17Operation");
        return (org.hl7.v2xml.ORUR30BATCHCONTENT) genericOperation("qvrQ17Operation", part1);
    }

    /**
     * This method has business logic to process QVR_Q17 hl7 message.
     * @param part1 QVR_Q17
     * @return ORU_R01 BATCH
     */
    public org.hl7.v2xml.ORUR01BATCHCONTENT qvrQ17ORUR01Operation(org.hl7.v2xml.QVRQ17CONTENT part1)
    {
        logger.debug("Invoking generic operation from qvrQ17ORUR01Operation");
        return (org.hl7.v2xml.ORUR01BATCHCONTENT) genericOperation("qvrQ17ORUR01Operation", part1);
    }

    /**
     * This method has business logic to process ACK_BATCH hl7 message.
     * @param part1 ACK_BATCH
     * @return ACK
     */
    public org.hl7.v2xml.ACKCONTENT ackBATCHOperation(org.hl7.v2xml.ACKBATCHCONTENT part1)
    {
        logger.debug("Invoking generic operation from ackBATCHOperation");
        return (org.hl7.v2xml.ACKCONTENT) genericOperation("ackBATCHOperation", part1);
    }

    public void pushResultsOperation(String jmsMessage)
    {
        logger.debug("message from queue is" + jmsMessage);
        try
        {
            logger.debug("Unmarshalling message from queue");

            // create a JAXBContext for the package
            JAXBContext ctx = JAXBContext.newInstance("com.bosch.th.integration");

            // create an unmarshaller
            Unmarshaller unmarshaller = ctx.createUnmarshaller();

            // unmarshal the xml bytes to Java object
            com.bosch.th.integration.PushResults pushResults = (com.bosch.th.integration.PushResults) unmarshaller.
                    unmarshal(new ByteArrayInputStream(jmsMessage.getBytes()));

            pushResults(pushResults);
        }
       catch (Exception ex)
        {
        	ex.printStackTrace();
            System.out.println(ex.toString());
        }
    }

 /**
     * pushResults operation accepts and processes results sent from CIA/CL and persists required information to Results table.
     * @param part1 Results sent from CIA/CL
     */
    public void pushResults(com.bosch.th.integration.PushResults part1)
    {
        try
        {
			// Persist Result with ResultPersister (extract data from PushResults input,
			// convert it to 'Result' class).
			resultPersister.persist(part1);

			 //Session persister workflow..
			sessionResultPersister.persist(part1);
		}
        catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * This method is a wrapper of all operations for persisting the incoming and outgoing message
     * @param methodName The name of method to invoke.
     * @param message The message received.
     * @return The response message (ACK or other response messages like ORU_R30).
     */
    public Object genericOperation(String methodName, Object message)
    {
        Object response = null;
        HL7MessagePersister persister = null;
        AckTypeEnum ackType = AckTypeEnum.APP;
        String ackRequestType = AckRequestCodeEnum.AL.toString();
        Error error = new Error();
        try
        {
            String esbGroupId = null;
            String esbMessageId = null;
            if (this.wsContext != null)
            {
                MessageContext messageContext = this.wsContext.getMessageContext();
                logger.debug("Message context: " + messageContext);
                esbGroupId = (String) messageContext.get("org.glassfish.openesb.messaging.groupid");
                esbMessageId = (String) messageContext.get("org.glassfish.openesb.messaging.messageid");
                logger.debug("ESB Group ID: " + esbGroupId);
                logger.debug("ESB Message ID: " + esbMessageId);
            }

            // Persist the incoming message
            persister = new HL7MessagePersister(esbGroupId, esbMessageId);
            persister.persist(message);

            //Check for duplicate response based on message's control number (MSH10) and sending facility id (MSH4).
            response = findDuplicateResponse(message, AckTypeEnum.APP);

            // Initialize an error list to store errors during message validation. New List to be created for each generic operation..
            List<Error> errorList = new ArrayList<Error>();
            error.setErrorList(errorList);

            //Get the Request/Response Header Content and ACK type..
            Object hl7MessageHeader = MessageUtils.getHL7MessageHeader(message);
            ackRequestType = MessageUtils.getHL7ACKRequestType(hl7MessageHeader);

            // Check for JAXB validation errors (ERROR and FATAL_ERROR), if the SchemaValidation annotation on the Endpoint class is enabled. If there are errors, app-level negative ack will be returned.
            if (this.wsContext != null)
            {
                MessageContext messageContext = this.wsContext.getMessageContext();
                Object exception = messageContext.get(CDAValidationErrorHandler.FATAL_ERROR);
                if (exception instanceof SAXParseException){
                    throw (SAXParseException) exception;
                }
                exception = messageContext.get(CDAValidationErrorHandler.ERROR);
                if (exception instanceof SAXParseException){
                    throw (SAXParseException) exception;
                }
            }

            // Validate and Process the request ...
            if (response == null) {
                response = validateAndProcess(methodName, message, ackType,error);

            }
        }
        catch (Exception exception)
        {
            logger.error("Error during request processing", exception);
            response = createNegativeAck(exception, ackType, methodName, message,error);
            persister.persist(response);
        }
        finally
        {
            //Persist the response either positive or negative ack...
            persister.persist(response);

            //Hide the response in case MSH segment field 16 is set to NE..
            if (ackRequestType!=null && ackRequestType.equalsIgnoreCase(AckRequestCodeEnum.NE.toString())) {
                response = null;
            }
            error.setErrorList(null);
        }
        return response;
    }

    /**
     * This method is where validation and business logic processing are done.
     * Depending on the customer ID configured in the configuration XML, the appropriate
     * validator and processor will be invoked.
   	 * @param methodName - WS operation name to identify the response in case of ORU_R30/ORU_R01..
     * @param message The HL7 message to validate and process.
     * @param ackType The type of acknowledgment (app-level) that needs to be returned.
     * @return The response HL7 message.
     * @throws Exception Runtime exception or validation exception, which will be caught by calling function
     * and handled accordingly based on the existing Error (see method createNegativeAck()).
     */
    private Object validateAndProcess(String methodName, Object message, AckTypeEnum ackType,Error error) throws Exception
    {
    	Validator validator = ValidatorFactory.getValidator(customerId);
        validator.setErrorObject(error);
        // Validate
        validator.validateMessage(message);
        String msgType = MessageUtils.getMessageName(message);

        Processor processor = processorFactory.getProcessor(customerId,msgType);
        processor.setErrorObject(error);
        // Process
        processor.setAckType(ackType);
        Object response = processor.processMessage(methodName,message);

        return response;
    }

    /**
     * This method finds duplicate response (app-level) based on
     * message's control number (MSH10) and sending facility id (MSH4).
     *
     * @param message The input message
     * @param ackType The ack type (app-level) to be searched.
     * @return The duplicate response if any, or null.
     */
    private Object findDuplicateResponse(Object message, AckTypeEnum ackType)
    {
		String rawDuplicateResponse = null;

		// Get the control number and sending facility ID from the input message's MSH segment.
		String controlNumber = null;
		String sendFacilityId = null;
		Object requestObj;
		try {
			requestObj = MessageUtils.getHL7MessageHeader(message);
		}
		catch (Exception ex) {
			return null;
		}

		if (requestObj instanceof MSHCONTENT)
		{
			MSHCONTENT mshContent = (MSHCONTENT) requestObj;
			try {
				controlNumber = mshContent.getMSH10().getValue();
			}
			catch (NullPointerException ex) {
				// do nothing
			}

			try {
				sendFacilityId = mshContent.getMSH4().getHD1().getValue();
				logger.info("Finding app-level response of a duplicate incoming HL7 message with " + (controlNumber == null ? "null" : controlNumber) + " control number and " + (sendFacilityId == null ? "null" : sendFacilityId) + " sending facility ID.");

			}
			catch (NullPointerException ex) {
				// do nothing
			}
		}
		else if (requestObj instanceof BHSCONTENT)
		{
			BHSCONTENT bhsContent = (BHSCONTENT) requestObj;
			try {
				controlNumber = bhsContent.getBHS11().getValue();
			}
			catch (NullPointerException ex) {
				// do nothing
			}

			try {
				sendFacilityId = bhsContent.getBHS4().getValue();
				logger.info("Finding app-level response of a duplicate incoming HL7 message with " + (controlNumber == null ? "null" : controlNumber) + " control number and " + (sendFacilityId == null ? "null" : sendFacilityId) + " sending facility ID.");

			}
			catch (NullPointerException ex) {
				// do nothing
			}
		}


		try
		{
			HL7Message hl7Message = HL7Message.findDuplicateHL7MessageResponse(controlNumber, sendFacilityId);
			if (hl7Message != null) {
				rawDuplicateResponse = new String(hl7Message.getRawMessage());
			}
		}
		catch (Exception ex) {
			// do nothing
		}

		// convert xml to object
		Object duplicateResponse = null;
		if (rawDuplicateResponse != null) {
			try  {
				duplicateResponse = messageConverter.xmlToHl7(rawDuplicateResponse);
			}
			catch (Exception ex) {
				// do nothing
			}
		}

		if (duplicateResponse != null) {
			logger.info("Found response of the duplicate incoming HL7 message.");
		}
		else {
			logger.info("Response of the duplicate incoming HL7 message not found.");
		}
		return duplicateResponse;
	}

    /**
     * Create a negative acknowledgment based on the ack type and the exception passed.
     * If there is already error in the list (for example in case of validation exception),
     * the error will be used instead of creating a new one.
     * This method has to be synchronized since it might add an Error entry to the singleton Error class.
     *
     * @param ex The exception to base the ack creation on.
     * @param ackType Type of acknowledgment (app-level)
     * @param methodName Method name in case response type could be different
     * @return The ACKCONTENT object
     */
    private Object createNegativeAck(Exception ex, AckTypeEnum ackType, String methodName, Object message,Error error)
    {
        // If no error yet, create one based on the exception input.
        if (!error.hasErrors())
        {
            Error errorNew = new Error();
            errorNew.setFaultExternalCode("[" + ex.getClass().getSimpleName() + "]");
            errorNew.setFaultExternalMessage(ex.getMessage());
            error.getErrorList().add(errorNew);
        }

        return createAck(ackType, methodName, message,error);
    }

    /**
     * Create a positive or negative acknowledgment based on the type (application-level)
     * and method name if the response type could be different.
     *
     * @param ackType Type of acknowledgment (app-level)
     * @param methodName Method name in case response type could be different
     * @return The ACKCONTENT object
     */
    private Object createAck(AckTypeEnum ackType, String methodName, Object message,Error error)
    {
        Creator creator = CreatorFactory.getCreator(customerId);
        Object response = null;
        try
        {
            response = creator.createMessage("ACK", ackType, message,error);
        }
        catch (Exception e)
        {
            logger.debug("Error while creating acknowledgement: ", e);
        }

        if (methodName == null)
        {
            return response;
        }
        // in case of qvrQ17Operation, wrap the acknowledgement in a ORU_R30_BATCH message
		else if (methodName.equals("qvrQ17Operation"))
		{
			try
			{
				Map<String,Object> resultsDataMap = new HashMap<String,Object>();
				resultsDataMap.put("QVR_Q17Request", message);
				resultsDataMap.put("Negative_Response_Ack", response);
				response = creator.createMessage("ORU_R30", resultsDataMap);
			}
			catch (Exception e)
			{
				logger.debug("Error while creating ORU_R30 batch message: ", e);
			}
		}
		else if (methodName.equals("qvrQ17ORUR01Operation"))
		{
			try
			{
				Map<String,Object> resultsDataMap = new HashMap<String,Object>();
				resultsDataMap.put("QVR_Q17Request", message);
				resultsDataMap.put("Negative_Response_Ack", response);
				response = creator.createMessage("ORU_R01", resultsDataMap);
			}
			catch (Exception e)
			{
				logger.debug("Error while creating ORU_R01 batch message: ", e);
			}
		}

        return response;
    }
}


