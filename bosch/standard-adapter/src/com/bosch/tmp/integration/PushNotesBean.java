package com.bosch.tmp.integration;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.bosch.th.integration.notes.PushNotes;
import com.bosch.th.integration.PushNotesPortType;
import com.bosch.tmp.integration.persistence.NotesPersister;

/**
 * PushNotesBean process the notes persisted in PushNotes queue (active-mq)
 * sent from CL task service via CIA web service end-point.
 *
 * @author bea2kor
 */
@Service
public class PushNotesBean implements PushNotesPortType {

	public static final Logger logger = LoggerFactory.getLogger(PushNotesBean.class);

    @Autowired
    private NotesPersister notesPersister;
    
	/**
	 * Holds the JAXB context corresponding to each HL7 message.
	 * It behaves like a cache for storing HL7 message JAXB context.
	 * JAXB Context will be initialized only once and the instance will be put back into cache.
	 */
	private static Map<String,JAXBContext> messageContext = new HashMap<String,JAXBContext>();

	@Override
	public void pushNotes(PushNotes pushNotesObj) {
		logger.debug("Operation currently not supported directly with HTTP binding.");
		try {
			notesPersister.persistNotes(pushNotesObj);
		}
		catch (Exception e) {
            logger.error("Exception encountered while persisting pushNotes object/entity into CDA.");
			e.printStackTrace();
		}
	}

	/**
	 * Work-flow for processing push notes message from the JMS queue defined for pushNotes operation.
	 * PushNotes operation is currently binded using a JMS protocol.
	 * @param pushNotesJMSMessage
	 */
	public synchronized void processPushNotesQueue(String pushNotesJMSMessage)
	{
		logger.debug("Notes message from PushNotes Queue is :" + pushNotesJMSMessage);
		Unmarshaller unmarshaller = null;
		try
		{
			logger.debug("Unmarshalling message from queue");
			unmarshaller = getJAXBUnMarshaller("PushNotes", "com.bosch.th.integration.notes");
			PushNotes pushNotes = (PushNotes)unmarshaller.unmarshal(new ByteArrayInputStream(pushNotesJMSMessage.getBytes()));

			logger.debug("Processing noes persister workflow");
			pushNotes(pushNotes);
		}
		catch (Exception ex) {
			logger.error("Exception encountered while processing messages from JMS queue.");
			ex.printStackTrace();
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
	private Unmarshaller getJAXBUnMarshaller(String key, String classPackageName)
	{
		Unmarshaller unmarshaller = null;
		try
		{
			JAXBContext context = null;
			if(messageContext!=null && messageContext.containsKey(key)) {
				context = messageContext.get(key);
			}
			else {
				context = JAXBContext.newInstance(classPackageName);
				if(context!=null){
					logger.debug("Creating a new " + key + " message context..");
					messageContext.put(key, context);
				}
			}

			unmarshaller = context.createUnmarshaller();
		}
		catch (Exception e) {
			logger.error("Exception occurred while marshalling object to xml in HL7 Message Persister.");
			e.printStackTrace();
		}
		return unmarshaller ;
	}
}
