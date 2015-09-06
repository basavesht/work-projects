package com.bosch.tmp.integration.persistence;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.hl7.v2xml.ACKBATCHCONTENT;
import org.hl7.v2xml.ACKCONTENT;
import org.hl7.v2xml.ADTA01CONTENT;
import org.hl7.v2xml.ADTA03CONTENT;
import org.hl7.v2xml.ADTA05CONTENT;
import org.hl7.v2xml.ORUR01BATCHCONTENT;
import org.hl7.v2xml.ORUR30BATCHCONTENT;
import org.hl7.v2xml.ObjectFactory;
import org.hl7.v2xml.QVRQ17CONTENT;
import org.hl7.v2xml.UACCONTENT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosch.tmp.integration.util.AckRequestCodeEnum;
import com.bosch.tmp.integration.util.AckResponseCodeEnum;
import com.bosch.tmp.integration.util.HL7MessageProcessingIdEnum;
import com.bosch.tmp.integration.util.MessageUtils;
import com.bosch.tmp.integration.util.Utils;

/**
 * This class is used as a helper to persist HL7 messages.
 * It contains business logic to map the actual HL7 messages into
 * the representative HL7Message class instances, which in turn
 * will be persisted into database.
 * There is one instance of this class per HL7 operation request.
 * The same instance will be used to persist both the incoming and
 * outgoing messages (always in pair).
 *
 * @author gtk1pal
 */
public class HL7MessagePersister
{
	public static final Logger logger = LoggerFactory.getLogger(HL7MessagePersister.class);

	@PersistenceContext
	transient EntityManager entityManager;

	/**
	 * Holds the JAXB context corresponding to each HL7 message.
	 * It behaves like a cache for storing HL7 message JAXB context.
	 * JAXB Context will be initialized only once and the instance will be put back into cache.
	 */
	private static Map<String,JAXBContext> messageContext = new HashMap<String,JAXBContext>();

	/**
	 * This is the ESB's normalized message id (String type).
	 * Used in conjunction with the esbGroupId to uniquely identify an ESB message.
	 * In case of ESB is not used, time-stamp would be assigned to this field.
	 */
	private String esbMessageId;

	/**
	 * This is the ESB's normalized message group id (String type).
	 * Used in conjunction with the esbMessageId to uniquely identify an ESB message.
	 * In case of ESB is not used, this field would be null.
	 */
	private String esbGroupId;

	/**
	 * This flag is used to indicate whether a new entry should be created in the database or not,
	 * depending on whether the message is incoming or outgoing.
	 */
	private boolean isNewMessage;

	/**
	 * This is the request message, which should be associated to the response message.
	 * This is needed when a HL7MessagePersister for a particular request message should be created in a different thread
	 * (like in the asynchronous processing case) in order to associate the response message to the request message.
	 */
	private HL7Message requestMessage;

	/**
	 * This flag is used to indicate whether the request (first) message is incoming or outgoing.
	 */
	private boolean incomingFirst = true;

	/**
	 * This stores the password as received in the incoming request.
	 * This is required to remove the password element before persisting HL7 raw message..
	 */
	private String password;

    /**
	 * Default Constructor of this class.
	 */
	public HL7MessagePersister() {

	}

	/**
	 * Constructor of this class.
	 * @param esbGroupId
	 * @param esbMessageId
	 */
	public HL7MessagePersister(String esbGroupId, String esbMessageId) {
		this.esbGroupId = esbGroupId;
		this.esbMessageId = esbMessageId;
	}

	public void setEsbGroupId(String esbGroupId) {
		this.esbGroupId = esbGroupId;
	}

	public void setEsbMessageId(String esbMessageId) {
		this.esbMessageId = esbMessageId;
	}

	public void setRequestMessage(HL7Message requestMessage) {
		this.requestMessage = requestMessage;
	}

	public void setIncomingFirst(boolean incomingFirst) {
		this.incomingFirst = incomingFirst;
	}

	/**
	 * The method to either create or update an HL7Message entry in the database.
	 *
	 * @param object The object to persist
	 * @return The persisted HL7Message
	 */
	public HL7Message persist(Object object)
	{
		logger.info("Persisting HL7 message...");

		if (object == null) {
			logger.debug("Object to be persisted is null.");
			return null;
		}

		HL7Message hl7message = null;
		try
		{
			//Persist HL7 message accordingly..
			hl7message = createHL7Message(object);

			if (hl7message.entityManager == null) {
				hl7message.entityManager = this.entityManager;
			}

			// Get previous HL7Message entry with same control number as message.If any, this means message is a response to the previous HL7Message.
			HL7Message prevRequestMsg = null;
			if (hl7message.getControlNumber() != null) {
				prevRequestMsg = HL7Message.findHL7MessageByControlNumber(hl7message.getControlNumber());
			}

			if (this.isNewMessage) {
				hl7message.persist();
			}
			else {
				hl7message.merge();
			}

			// Associate message as the response message of the previous HL7Message that has the same control number, if any.
			if ((prevRequestMsg != null) && (prevRequestMsg.getIsIncoming() != hl7message.getIsIncoming())) {
				logger.debug("Found previous entry with different direction with the same control number " + hl7message.getControlNumber());
				logger.debug("Associating the message with the previous entry.");
				prevRequestMsg.setResponseMessage(hl7message);
				prevRequestMsg.merge();
			}
			else {
				logger.debug("Cannot find previous entry with different direction with the same control number " + hl7message.getControlNumber());
			}

			// In case of response message:
			if (this.requestMessage != null && !hl7message.getId().equals(this.requestMessage.getId())) {
				this.requestMessage.setResponseMessage(hl7message);
				this.requestMessage.merge();
			}
		}
		catch (Exception e) {
			logger.debug("Exception occurs when persisting message: ", e);
			return null;
		}

		logger.info("Persistence done.");

		return hl7message;
	}

	/**
	 * The method to convert an actual HL7 message to a representative HL7Message class to be persisted into database ...
	 * @param object The object to convert
	 * @return The HL7Message representation of the object
	 * @throws Exception Any exception thrown during conversion
	 */
	private HL7Message createHL7Message(Object object) throws Exception
	{
		String messageType = MessageUtils.getMessageName(object);

		//Infer that it's incoming message if: - 'incomingFirst' flag is true and 'requestMessage' is null, OR 'incomingFirst' flag is false and 'requestMessage' is not null.
		boolean incoming = ((this.incomingFirst && (this.requestMessage == null)) || (!this.incomingFirst && (this.requestMessage != null)));
		this.isNewMessage = true;

		//Get the Request/Response Header Content..
		Object headerContent = MessageUtils.getHL7MessageHeader(object);

		//Get the Control number from the actual message.
		String msgControlNumber = MessageUtils.getHL7MessageControlNumber(headerContent);
		if (msgControlNumber == null || msgControlNumber.trim().equals("")) {
			throw new Exception("Message control number is missing in " + (incoming ? "incoming" : "outgoing") + " message.");
		}

		HL7Message hl7Message = null;
		try
		{
			//Setting the message directions and type based on ESB id and directions...
			if (incoming)
			{
				hl7Message = HL7Message.findHL7MessageByEsbIdsAndDirection(this.esbGroupId, this.esbMessageId, Boolean.TRUE);
				if (hl7Message == null) {
					logger.debug("Cannot find existing entry for incoming message.");
					hl7Message = new HL7Message();
					hl7Message.setIsIncoming(true);
				}
				else {
					logger.debug("Found existing entry for incoming message.");
					this.isNewMessage = false;
				}
				hl7Message.setControlNumber(msgControlNumber);
			}
			else {
				hl7Message = new HL7Message();
				hl7Message.setIsIncoming(false);
				hl7Message.setControlNumber(msgControlNumber);
			}

			//ESB group id and ESB message id...
			if (this.isNewMessage){
				hl7Message.setEsbGroupId(this.esbGroupId);
				hl7Message.setEsbMessageId(this.esbMessageId);
			}

			//HL7 raw message...
			hl7Message.setRawMessage(createHL7RawMessage(object).toCharArray());

			//HL7 Version data...
			String hl7Version = null;
			if(this.requestMessage!=null && requestMessage.getHl7Version()!=null){
				hl7Version = requestMessage.getHl7Version();
			}
			else {
				hl7Version = MessageUtils.getHL7VersionNumber(headerContent);
			}
			hl7Message.setHl7Version(hl7Version);
			hl7Message.setMpi(null);

			//Processing Id...
			try {
				hl7Message.setProcessingId(HL7MessageProcessingIdEnum.valueOf(MessageUtils.getHL7MessageProcessingId(headerContent)));
			}
			catch (Exception ex) {
				// do nothing, leave property as null.
			}

			//Message Time-stamp...
			try {
				hl7Message.setSendTimestamp(Utils.convertStringToDate(MessageUtils.getHL7MessageSentTimestamp(headerContent), null));
			}
			catch (Exception ex) {
				// do nothing, leave property as null.
			}

			//Other HL7 message parameters...
			hl7Message.setStructureType(messageType);
			hl7Message.setType(MessageUtils.getHL7MessageType(headerContent));
			hl7Message.setSendingFacilityDns(null);
			hl7Message.setSendingFacilityId(MessageUtils.getHL7MessageSendingFacility(headerContent, messageType));

			//HL7 Application ACK request code...
			try {
				hl7Message.setAppAckRequestCode(AckRequestCodeEnum.valueOf(MessageUtils.getHL7ACKRequestType(headerContent)));
			}
			catch (Exception ex) {
				// do nothing, leave property as default
			}

			//User Name...
			UACCONTENT uacContent = null;
			try {
				uacContent = (UACCONTENT) object.getClass().getMethod("getUAC").invoke(object);
				if(uacContent!=null && uacContent.getUAC2()!=null && uacContent.getUAC2().getED5()!=null && uacContent.getUAC2().getED5().getUNT2()!=null){
					hl7Message.setUserName(uacContent.getUAC2().getED5().getUNT1().getValue());
				}
			}
			catch (Exception ex) {
				// do nothing, leave property as null.
			}

			//ACK fault code/message
			if (object instanceof ACKCONTENT)
			{
				ACKCONTENT ackContent = (ACKCONTENT) object;
				if (ackContent != null && ackContent.getMSA() != null)
				{
					if (ackContent.getMSA().getMSA3() != null)
					{
						String faultMsg = ackContent.getMSA().getMSA3().getValue();
						if (faultMsg != null)
						{
							hl7Message.setAckFaultCode(faultMsg.substring(0, faultMsg.indexOf(":")));
							hl7Message.setAckFaultMessage(faultMsg.substring(faultMsg.indexOf(":") + 1));
						}
					}

					if (ackContent.getMSA().getMSA1() != null){
						hl7Message.setAckTypeCode(AckResponseCodeEnum.valueOf(ackContent.getMSA().getMSA1().getValue()));
					}
				}
			}

			//Setting the HL7 RequestMessage within HL7MessagePersister instance...
			if (this.requestMessage == null) {
				this.requestMessage = hl7Message;
			}
		}
        catch (Exception e) {
			e.printStackTrace();
		}
		return hl7Message;
	}

	/**
	 * Marshal the request/response Object to XML to store in HL7 table raw message table...
	 * @param obj
	 * @return
	 */
	private String createHL7RawMessage(Object obj)
	{
		StringWriter marshalledString = new StringWriter();
		try
		{
			ObjectFactory objFactory = new ObjectFactory();
			Marshaller objMarshaller = null;

			//Edit's the HL7 Message by removing password field before persist..
			hideSecuredDtlsBeforePersist(obj);

			if(obj instanceof ADTA01CONTENT) {
				objMarshaller = getJAXBMarshaller("ADT_A01",objFactory.createADTA01CONTENT());
				objMarshaller.marshal(objFactory.createADTA01((ADTA01CONTENT)obj), marshalledString);
			}
			else if(obj instanceof ADTA03CONTENT) {
				objMarshaller = getJAXBMarshaller("ADT_AO3",objFactory.createADTA03CONTENT());
				objMarshaller.marshal(objFactory.createADTA03((ADTA03CONTENT)obj), marshalledString);
			}
			else if(obj instanceof ADTA05CONTENT) {
				objMarshaller = getJAXBMarshaller("ADT_A05",objFactory.createADTA05CONTENT());
				objMarshaller.marshal(objFactory.createADTA05((ADTA05CONTENT)obj), marshalledString);
			}
			else if(obj instanceof QVRQ17CONTENT) {
				objMarshaller = getJAXBMarshaller("QVR_Q17",objFactory.createQVRQ17CONTENT());
				objMarshaller.marshal(objFactory.createQVRQ17((QVRQ17CONTENT)obj), marshalledString);
			}
			else if(obj instanceof ACKCONTENT) {
				objMarshaller = getJAXBMarshaller("ACK",objFactory.createACKCONTENT());
				objMarshaller.marshal(objFactory.createACK((ACKCONTENT)obj), marshalledString);
			}
			else if(obj instanceof ACKBATCHCONTENT) {
				objMarshaller = getJAXBMarshaller("ACK_BATCH",objFactory.createACKBATCHCONTENT());
				objMarshaller.marshal(objFactory.createACKBATCH((ACKBATCHCONTENT)obj), marshalledString);
			}
			else if(obj instanceof ORUR30BATCHCONTENT) {
				objMarshaller = getJAXBMarshaller("ORU_R30BATCH",objFactory.createORUR30BATCHCONTENT());
				objMarshaller.marshal(objFactory.createORUR30BATCH((ORUR30BATCHCONTENT)obj), marshalledString);
			}
			else if(obj instanceof ORUR01BATCHCONTENT) {
				objMarshaller = getJAXBMarshaller("ORU_R01BATCH",objFactory.createORUR01BATCHCONTENT());
				objMarshaller.marshal(objFactory.createORUR01BATCH((ORUR01BATCHCONTENT)obj), marshalledString);
			}

			//Roll-back the HL7 message changes if any after persisting, required for further request processing...
			unhideSecuredDtlsAfterPersist(obj);

			logger.debug("Marshalled data.... " +  marshalledString.toString());
		}
		catch (Exception e) {
			logger.error("Exception occurred while marshalling object to xml in HL7 Message Persister.");
			e.printStackTrace();
		}
		return marshalledString.toString();
	}

	/**
	 * Set back the password details into the HL7 message object after persist..
	 * @param obj
	 */
	private void unhideSecuredDtlsAfterPersist(Object obj)
	{
		if(obj!=null && (obj instanceof ADTA01CONTENT || obj instanceof ADTA03CONTENT || obj instanceof ADTA05CONTENT || obj instanceof QVRQ17CONTENT)) {
			UACCONTENT uacContent = null;
			try {
				uacContent = (UACCONTENT) obj.getClass().getMethod("getUAC").invoke(obj);
			}
			catch (Exception ex) {
				// do nothing
			}
			if(uacContent!=null && uacContent.getUAC2()!=null
					&& uacContent.getUAC2().getED5()!=null && uacContent.getUAC2().getED5().getUNT2()!=null){
				uacContent.getUAC2().getED5().getUNT2().setValue(password);
			}
		}
	}

	/**
	 * Hide the password details from the HL7 message object before persist..
	 * @param obj
	 */
	private void hideSecuredDtlsBeforePersist(Object obj)
	{
		if(obj!=null && (obj instanceof ADTA01CONTENT || obj instanceof ADTA03CONTENT || obj instanceof ADTA05CONTENT || obj instanceof QVRQ17CONTENT)) {
			UACCONTENT uacContent = null;
			try {
				uacContent = (UACCONTENT) obj.getClass().getMethod("getUAC").invoke(obj);
			}
			catch (Exception ex) {
				// do nothing
			}
			if(uacContent!=null && uacContent.getUAC2()!=null
					&& uacContent.getUAC2().getED5()!=null && uacContent.getUAC2().getED5().getUNT2()!=null && !uacContent.getUAC2().getED5().getUNT2().getValue().trim().equals("")){
				password = uacContent.getUAC2().getED5().getUNT2().getValue();
				uacContent.getUAC2().getED5().getUNT2().setValue("");
			}
		}
	}

	/**
	 * Creates a JAXB context.
     * Context will be created only once and will be stored into a static HashMap against the message name as key,
	 * since creation of JAXB is little costly. Once the context is stored into HashMap,
     * rest all request uses the same context for processing.
	 * Creation of JAXB context is thread-safe.
	 * @param key
	 * @param obj
	 * @return
	 */
	private Marshaller getJAXBMarshaller(String key, Object obj)
	{
		Marshaller objMarshaller = null;
		try
		{
			JAXBContext context = null;
			if(messageContext!=null && messageContext.containsKey(key)) {
				context = messageContext.get(key);
			}
			else {
				context = JAXBContext.newInstance(obj.getClass().getPackage().getName());
				if(context!=null){
					logger.debug("Creating a new " + key + " message context..");
					messageContext.put(key, context);
				}
			}

			objMarshaller = context.createMarshaller();
			objMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		}
		catch (Exception e) {
			logger.error("Exception occurred while marshalling object to xml in HL7 Message Persister.");
			e.printStackTrace();
		}
		return objMarshaller ;
	}
}
