package com.bosch.tmp.integration.persistence;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is a JAX-WS Logical Handler to persist raw incoming and outgoing HL7 messages.
 *
 * @author gtk1pal
 */
@Transactional
public class HL7MessageRawLogicalHandler implements LogicalHandler<LogicalMessageContext>
{
	public static final Logger logger = LoggerFactory.getLogger(HL7MessageRawLogicalHandler.class);

	@PersistenceContext
	transient EntityManager entityManager;

	@Override
	public void close(MessageContext context){
        
	}

	@Override
	public boolean handleFault(LogicalMessageContext context) {
		processMessage(context);
		return true;
	}

	@Override
	public boolean handleMessage(LogicalMessageContext context) {
		processMessage(context);
		return true;
	}

	private void processMessage(LogicalMessageContext context)
	{
		try
		{
			Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			String esbGroupId = (String) context.get("org.glassfish.openesb.messaging.groupid");
			String esbMessageId = (String) context.get("org.glassfish.openesb.messaging.messageid");

			//If esbMessageId is null (not deployed in ESB environment), use the current timestamp.
			if (esbMessageId == null) {
				esbMessageId = Long.toString(new Date().getTime());
				context.put("org.glassfish.openesb.messaging.messageid", esbMessageId);
				context.setScope("org.glassfish.openesb.messaging.messageid", Scope.APPLICATION);
			}

			logIncomingOutgoingMessage(context);

			HL7Message hl7Message;
			boolean isNew = true;
			if (!outboundProperty.booleanValue()) {
				hl7Message = new HL7Message();
				hl7Message.setIsIncoming(true);
			}
			else {
				hl7Message = HL7Message.findHL7MessageByEsbIdsAndDirection(esbGroupId, esbMessageId, false);
				if (hl7Message == null) {
					hl7Message = HL7Message.findHL7MessageByNullRawMessageAndDirection(false);
					if (hl7Message == null) {
						hl7Message = new HL7Message();
					}
					else {
						isNew = false;
					}
				}
				else {
					isNew = false;
				}
			}
			hl7Message.setEsbGroupId(esbGroupId);
			hl7Message.setEsbMessageId(esbMessageId);

			if (isNew) {
				entityManager.persist(hl7Message);
			}
			else {
				entityManager.merge(hl7Message);
			}

			logger.debug("HL7 Message ID: " + hl7Message.getId());
		}
		catch (Exception e) {
			logger.debug("Exception in handler: ", e);
		}
	}

	private void logIncomingOutgoingMessage(LogicalMessageContext context)
	{
		try
		{
			Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

			if (outboundProperty.booleanValue()) {
				logger.debug("\nOutgoing message:");
			}
			else {
				logger.debug("\nIncoming message:");
			}

			//Extracting and logging the PayLoad...
			LogicalMessage message = context.getMessage();
			DOMSource payload = (DOMSource) message.getPayload();
			ByteArrayOutputStream messageByteStream = new ByteArrayOutputStream();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StreamResult streamResult = new StreamResult(messageByteStream);
			transformer.transform(payload, streamResult);

			logger.debug(messageByteStream.toString());
			logger.debug("\n");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
