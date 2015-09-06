
package com.bosch.tmp.integration.process;

import com.bosch.tmp.integration.creation.CreationException;
import com.bosch.tmp.integration.serviceReference.cia.CIAService;
import com.bosch.tmp.integration.util.AckTypeEnum;
import com.bosch.tmp.integration.util.Error;
import com.bosch.tmp.integration.util.MessageUtils;
import com.bosch.tmp.integration.validation.ConfigurationLoader;
import com.bosch.tmp.integration.validation.CustomerId;
import com.bosch.th.externalorganization.OrganizationType;
import com.bosch.tmp.cl.basictypes.ExternalIdentifierType;
import com.bosch.th.integrationpatient.AddressType;
import com.bosch.th.integrationpatient.Enrollment;
import com.bosch.tmp.cl.basictypes.IdentifierType;
import com.bosch.th.integrationpatient.IntegrationPatientType;
import com.bosch.th.integrationpatient.PhoneNumberType;
import com.bosch.th.integrationpatient.StatusType;
import com.bosch.tmp.integration.creation.CreatorFactory;
import com.bosch.tmp.integration.dao.NotesDao;
import com.bosch.tmp.integration.dao.ResultDao;
import com.bosch.tmp.integration.dao.SessionResultDao;
import com.bosch.tmp.integration.entities.Result;
import com.bosch.tmp.integration.entities.SessionResult;
import com.bosch.tmp.integration.entities.Notes;
import com.bosch.tmp.integration.persistence.ResultPersister;
import com.bosch.tmp.integration.persistence.SessionResultPersister;
import com.bosch.tmp.integration.persistence.NotesPersister;
import com.bosch.tmp.integration.util.AckResponseCodeEnum;
import com.bosch.tmp.integration.util.ConfigKeyEnum;
import com.bosch.tmp.integration.util.ResultTypeEnum;
import com.bosch.tmp.integration.util.Utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hl7.v2xml.ACKBATCHCONTENT;
import org.hl7.v2xml.ACKCONTENT;
import org.hl7.v2xml.ADTA01CONTENT;
import org.hl7.v2xml.ADTA03CONTENT;
import org.hl7.v2xml.ADTA05CONTENT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import org.hl7.v2xml.BHSCONTENT;

/**
 * This class has the implementation to process an incoming msg once the validation is successful.
 * Appropriate methods from the CIA service are invoked.
 *
 * @author gtk1pal
 * @author tis1pal
 */
@Service
public class DefaultProcessor implements Processor
{

	public static final Logger logger = LoggerFactory.getLogger(DefaultProcessor.class);

	@Autowired
	private CIAService ciaServiceProxy;

	@Autowired
	private SessionResultDao sessionResultDao;

	@Autowired
	private ResultDao resultDao;

    @Autowired
    private ResultPersister resultPersister ;

    @Autowired
    private SessionResultPersister sessionPersister;

    @Autowired
	private NotesDao notesDao;

    @Autowired
    private NotesPersister  notesPersister;

	public CIAService getCiaServiceProxy() {
		return ciaServiceProxy;
	}

	public void setCiaServiceProxy(CIAService ciaServiceProxy) {
		this.ciaServiceProxy = ciaServiceProxy;
	}

	protected AckTypeEnum ackType;

	private CustomerId customerId;

	public void setAckType(AckTypeEnum ackType)
	{
		this.ackType = ackType;
	}

	private Error error = null;

	@Override
	public void setErrorObject(Error error) {
		this.error = error;

	}

	public Object processMessage(String methodName, Object message) throws ProcessException
	{
		// If the message is valid and passes all validations then the CIA service is invoked
		if (error.hasErrors())
		{
			throw new ProcessException("Cannot process message due to at least one validation error.");
		}

		try
		{
			if (message instanceof ADTA01CONTENT)
			{
				return processADTA01((ADTA01CONTENT) message);
			}
			else if (message instanceof ADTA03CONTENT)
			{
				return processADTA03((ADTA03CONTENT) message);
			}
			else if (message instanceof ADTA05CONTENT)
			{
				return processADTA05((ADTA05CONTENT) message);
			}
			else if (message instanceof ACKBATCHCONTENT)
			{
				return processACKBATCH((ACKBATCHCONTENT) message);
			}
			/* COMPLETE CODE IS RE-DESIGNED AND MOVED TO RESULT PROCESSOR....
            else if (message instanceof QVRQ17CONTENT)
            {
                return processQVRQ17((QVRQ17CONTENT) message);
            }*/
		}
		catch (Exception ex)
		{
			logger.error("Exception occurred in processMessage of default processor", ex);
			throw new ProcessException("Exception occurred in processMessage of default processor", ex);
		}
		return null;
	}

	public ACKCONTENT processADTA01(ADTA01CONTENT message) throws ProcessException
	{
		// Update the ciaService with user details from the UAC segment.
		// Service is dynamically updated with user details from the UAC segment on every request.
		getCiaServiceProxy().updateServiceWithUserDetails(message,error);
		try
		{
			String triggerEvent = MessageUtils.getTriggerEvent(message);
			if (triggerEvent != null && triggerEvent.equals("A04"))
			{
				logger.info("**********Processing A04 message**********");
				return processAdmission(message);

			}
			else if (triggerEvent != null && triggerEvent.equals("A08"))
			{
				logger.info("**********Processing A08 Demographic Update message**********");
				return processUpdate(message);
			}
		}
		catch (Exception ex)
		{
			logger.error("Exception occurred while processing ADT_A01 message", ex);
			throw new ProcessException("Exception occurred while processing ADT_A01 message", ex);
		}
		return null;
	}

	public ACKCONTENT processADTA03(ADTA03CONTENT message) throws ProcessException
	{
		// Update the ciaService with user details from the UAC segment.
		// Service is dynamically updated with user details from the UAC segment on every request.
		getCiaServiceProxy().updateServiceWithUserDetails(message, error);
		try
		{
			String triggerEvent = MessageUtils.getTriggerEvent(message);
			if (triggerEvent != null && triggerEvent.equals("A03"))
			{
				logger.info("**********Processing A03 Patient Pause message**********");
				return processPatientPause(message);
			}
		}
		catch (Exception ex)
		{
			logger.error((ex.getMessage()));
			throw new ProcessException("Exception occurred while processing ADT_A03 message", ex);
		}
		return null;
	}

	public ACKCONTENT processADTA05(ADTA05CONTENT message) throws ProcessException
	{
		// Update the ciaService with user details from the UAC segment.
		// Service is dynamically updated with user details from the UAC segment on every request.
		getCiaServiceProxy().updateServiceWithUserDetails(message, error);
		try
		{
			String triggerEvent = MessageUtils.getTriggerEvent(message);
			if (triggerEvent != null && triggerEvent.equals("A31"))
			{
				logger.info("**processUpdate********Processing A31 Demographic Update message**********");
				return processUpdate(message);
			}
		}
		catch (Exception ex)
		{
			logger.error((ex.getMessage()));
			throw new ProcessException("Exception occurred while processing ADT_A31 message", ex);
		}
		return null;
	}

	public ACKCONTENT processACKBATCH(ACKBATCHCONTENT message) throws ProcessException
	{
		try
		{
			logger.info("**********Processing ACKBATCH for results message**********");
			return processAcknowledgments(message);
		}
		catch (Exception ex)
		{
			logger.error((ex.getMessage()));
			throw new ProcessException("Exception occurred while processing ACKBATCH message", ex);
		}
	}

	/*  COMPLETE CODE IS RE-DESIGNED AND MOVED TO RESULT PROCESSOR....
 	public ORUR30BATCHCONTENT processQVRQ17(QVRQ17CONTENT message) throws ProcessException
    {

        // Update the ciaService with user details from the UAC segment.
        // Service is dynamically updated with user details from the UAC segment on every request.
    	getCiaServiceProxy().updateServiceWithUserDetails(message);
        try
        {
            String triggerEvent = MessageUtils.getTriggerEvent(message);
            if (triggerEvent != null && triggerEvent.equals("Q17"))
            {
                logger.info("**********Processing Q17 Results Query message**********");
                return processResultsRequest(message);
            }
        }
        catch (Exception ex)
        {
            logger.error("Exception occurred while processing QVR_Q17 message", ex);
            throw new ProcessException("Exception occurred while processing QVR_Q17 message", ex);
        }
        return null;
    }*/

	private ACKCONTENT processAdmission(ADTA01CONTENT message) throws ProcessException
	{
		// Find signup org in the TH+ system given external org id in the incoming message.
		// Verify if the signup org is found and if its mapped to an internal org and if it is integrated.
		OrganizationType signupOrg = findAndVerifySignupOrganization(message);

		// Find the patients by identifiers and filter them by location and integration style.
		List<ExternalIdentifierType> identifiersList = null;
		try
		{
			identifiersList = MessageUtils.getIdentifiers(message);
		}
		catch (Exception ex)
		{
			identifiersList = null;
			logger.error("Exception occurred while getting external identifiers from incoming message.");
			throw new ProcessException("Exception occurred while getting external identifiers from incoming message. " + ex.
					getMessage());
		}

		//List<IntegrationPatientType> unfilteredPatientsList = findPatients(identifiersList);
		//get Internal Organization
		List<IntegrationPatientType> unfilteredPatientsList = getCiaServiceProxy().findPatientsByIdInOrganization(identifiersList, MessageUtils.getInternalOrganizationList(signupOrg).
				longValue(), error);

		logger.debug("Processing unfiltered patients list");
		List<IntegrationPatientType> patientsListByLocation = filterPatientsByLocation(unfilteredPatientsList, signupOrg);
		List<IntegrationPatientType> patientsListByIntegrationStyle = filterPatientsByIntegrationStyle(
				patientsListByLocation);

		// Create a patient object and populate it with info from incoming message
		logger.debug("Populate new patient object with information from incoming message.");
		IntegrationPatientType messagePatient = mapMessagePatient(null, message, identifiersList, signupOrg);

		//Check for Paused patient processing...
		if (!unfilteredPatientsList.isEmpty()) {
			for (IntegrationPatientType existingPatient : unfilteredPatientsList){
				if (existingPatient.isIsIntegration() && StatusType.PAUSED.toString().equalsIgnoreCase(existingPatient.getStatus().toString())) {
					String patId = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.toString());
					if (MessageUtils.getExistingPatIdentifier(existingPatient,patId).equalsIgnoreCase(MessageUtils.getMessagePatIdentifier(identifiersList,patId))){
						messagePatient = updateExistingPatientWithMessagePatient(messagePatient, existingPatient,message);
					}
					break;
				}
			}
		}

		// Process the filtered patients list and either admit, unpause or re-enroll message patient.
		IntegrationPatientType admitReply = processPatients(patientsListByLocation, patientsListByIntegrationStyle,messagePatient, message);

		// Create acknowledgement.
		ACKCONTENT appAck = null;
		try
		{
			// Create an Application Ack for the incoming message
			appAck = (ACKCONTENT) CreatorFactory.getCreator(getCustomerId()).createMessage("ACK", this.ackType, message, error);
		}
		catch (CreationException ex)
		{
			logger.error("Exception occurred while initializing creator to create app ack.");
			throw new ProcessException("Exception occurred while initializing creator to create app ack. " + ex.
					getMessage());
		}
		finally
		{
			patientsListByLocation = null;
			patientsListByIntegrationStyle = null;
		}

		return appAck;
	}

	private ACKCONTENT processUpdate(Object message) throws ProcessException
	{
		// Find signup org in the TH+ system given external org id in the incoming message.
		// Verify if the signup org is found and if its mapped to an internal org and if it is integrated.
		OrganizationType signupOrg = findAndVerifySignupOrganization(message);
		// Get patient's pat id from the incoming message.
		List<ExternalIdentifierType> identifiersList = new ArrayList<ExternalIdentifierType>();
		try
		{
			if (message instanceof ADTA01CONTENT)
			{
				ADTA01CONTENT msg = (ADTA01CONTENT) message;
				identifiersList = MessageUtils.getIdentifiers(msg);
			}
			else if (message instanceof ADTA05CONTENT)
			{
				ADTA05CONTENT msg = (ADTA05CONTENT) message;
				identifiersList = MessageUtils.getIdentifiers(msg);
			}
		}
		catch (Exception ex)
		{
			identifiersList = null;
			logger.error("Exception occurred while getting external pat id from incoming message.", ex);
			throw new ProcessException("Exception occurred while getting external pat id from incoming message. " + ex.
					getMessage());
		}


		// Create a patient object and populate it with info from incoming message
		logger.debug("Populate new patient object with information from incoming message.");
		IntegrationPatientType messagePatient = mapMessagePatient(null, message, identifiersList, signupOrg);

		// Find the patients by identifiers and filter them by location.
		List<IntegrationPatientType> unfilteredPatientsList = getCiaServiceProxy().findPatientsByIdInOrganization(identifiersList, MessageUtils.getInternalOrganizationList(signupOrg).
				longValue(), error);

		logger.debug("Processing unfiltered patients list");
		List<IntegrationPatientType> patientsListByLocation = filterPatientsByLocation(unfilteredPatientsList, signupOrg);

		// If no patients matching pat id are found, then reject the message.
		if (patientsListByLocation.isEmpty())
		{
			unfilteredPatientsList = null;
			patientsListByLocation = null;
			logger.error("Cannot find patient in TH+ system with the specified Pat Id");
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_FOUND.toString()));
			throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
		}

		//Forbid the request to Update for non-integrated patient .....
		forbidNonIntegratedPatientRequest(identifiersList,patientsListByLocation,error);

		// Check if the patient in the incoming message is unique in signup organization.
		// Two patient records with same SSN and different Pat Id are not unique. Duplicate SSN error should be returned.
		// Only one record with a Pat Id and/or SSN Id should exist within an organization.
		logger.debug("Start checking uniqueness of patient Pat Id and SSN.");
		checkPatientUniqueness(messagePatient, patientsListByLocation, new ArrayList<IntegrationPatientType>());
		logger.debug("End checking uniqueness of patient Pat Id and SSN - PATIENT IS UNIQUE.");

		// Process filtered patients list and update patient demographics.
		IntegrationPatientType updatePatientDemographicsReply = null;

		for (IntegrationPatientType existingPatient : patientsListByLocation)
		{
			logger.debug("Update the existing patient with non null information from the incoming message.");
			existingPatient = updateExistingPatientWithMessagePatient(messagePatient, existingPatient, message);
			// CIA's updatePatientDemographics accepts a list, as there could be multiple patient records matching pat Id.
			// All these records needs to be updated with the same information.
			// Especially in case of VA.
			List<IntegrationPatientType> existingPatientsToUpdate = new ArrayList<IntegrationPatientType>();
			existingPatientsToUpdate.add(existingPatient);
			try
			{
				logger.info("Invoking CIA's updatePatientDemographics operation to update existing patient in internal org Id: " + messagePatient.
						getInternalOrganizationId());
				updatePatientDemographicsReply = getCiaServiceProxy().updatePatientDemographics(existingPatientsToUpdate, error);
				patientsListByLocation = null;
				unfilteredPatientsList = null;
				break;
			}
			catch (Exception ex)
			{
				patientsListByLocation = null;
				unfilteredPatientsList = null;
				logger.error("Exception returned by CIA updatePatientDemographics operation.");
				throw new ProcessException("Exception returned by CIA updatePatientDemographics operation. " + ex.
						getMessage());
			}

		}

		// Create acknowledgement.
		ACKCONTENT appAck = null;
		try
		{
			// Create an Application Ack for the incoming message
			appAck = (ACKCONTENT) CreatorFactory.getCreator(getCustomerId()).createMessage("ACK", this.ackType,
					message, error);
		}
		catch (CreationException ex)
		{
			logger.error("Exception occurred while initializing creator to create app ack.");
			throw new ProcessException("Exception occurred while initializing creator to create app ack. " + ex.
					getMessage());
		}

		return appAck;
	}

	private ACKCONTENT processPatientPause(ADTA03CONTENT message) throws ProcessException
	{
		// Find signup org in the TH+ system given external org id in the incoming message.
		// Verify if the signup org is found and if its mapped to an internal org and if it is integrated.
		OrganizationType signupOrg = findAndVerifySignupOrganization(message);

		// Get patient's pat id from the incoming message.
		List<ExternalIdentifierType> identifiersList = new ArrayList<ExternalIdentifierType>();
		try
		{
			identifiersList.add(MessageUtils.getPatId(message));
		}
		catch (Exception ex)
		{
			identifiersList = null;
			logger.error("Exception occurred while getting external pat id from incoming message.");
			throw new ProcessException("Exception occurred while getting external pat id from incoming message. " + ex.
					getMessage());
		}

		// Create a patient object and populate it with info from incoming message
		logger.debug("Populate new patient object with information from incoming message.");
		//        IntegrationPatientType messagePatient = mapMessagePatient(message, identifiersList, signupOrg);

		// Find the patients by identifiers and filter them by location.
		List<IntegrationPatientType> unfilteredPatientsList = getCiaServiceProxy().findPatientsByIdInOrganization(identifiersList, MessageUtils.getInternalOrganizationList(signupOrg).
				longValue(), error);
		logger.debug("Processing unfiltered patients list");
		List<IntegrationPatientType> patientsListByLocation = filterPatientsByLocation(unfilteredPatientsList, signupOrg);

		// If no patients matching pat id are found, then reject the message.
		if (patientsListByLocation.isEmpty())
		{
			unfilteredPatientsList = null;
			patientsListByLocation = null;
			logger.error("Cannot find patient in TH+ system with the specified Pat Id");
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_FOUND.toString()));
			throw new ProcessException("SA Process error: " + error.getError().getFaultInternalMessage());

		}

		//Forbid the request to Pause for Non-Integrated patient .....
		forbidNonIntegratedPatientRequest(identifiersList,patientsListByLocation,error);

		// Process filtered patients list and pause the message patient.
		IntegrationPatientType pausePatientReply = null;

		// If the found patient is enrolled then pause the patient otherwise reject as the patient is already paused.
		// In Paused or disenrolled states, patient is already paused.
		for (IntegrationPatientType existingPatient : patientsListByLocation)
		{
			if (StatusType.ENROLLED.value().equalsIgnoreCase(existingPatient.getStatus().value()))
			{
				try
				{
					// If the patient is unpaused, pause date needs to be set. This is required by Patient service.
					// TODO for TH+2.0 release, SA adds current timestamp. But for later releases this date needs to come in the incoming message.
					// Current TimeStamp should be used only when this date is missing in the incoming message.
					logger.info("Mapping message using details from existing patient and message patient.");
					IntegrationPatientType messagePatient = mapMessagePatient(existingPatient, message, identifiersList,
							signupOrg);
					logger.info("Setting patient pause date.");
					XMLGregorianCalendar pauseStartDate = null;
					if (null == pauseStartDate)
					{
						try
						{
							pauseStartDate = Utils.convertDateToXMLGregorianCalendar(new Date());
						}
						catch (DatatypeConfigurationException ex)
						{
							logger.error("Exception while converting Pause Date..", ex);
						}
					}
					messagePatient.setEffectiveStateChangeDate(pauseStartDate);

					logger.info("Invoking CIA's pause patient operation to pause a new patient in internal org Id: " + messagePatient.
							getInternalOrganizationId());
					pausePatientReply = getCiaServiceProxy().pausePatient(messagePatient, error);
					patientsListByLocation = null;
					unfilteredPatientsList = null;
					break;
				}
				catch (Exception ex)
				{
					patientsListByLocation = null;
					unfilteredPatientsList = null;
					logger.error("Exception returned while setting pause date or by CIA pausePatient operation.");
					//Todo:add proper error
					//Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_ALREADY_PAUSED.toString()));
					throw new ProcessException("Exception returned while setting pause date by CIA pausePatient operation. " + ex.
							getMessage());
				}
			}
			else
			{
				patientsListByLocation = null;
				unfilteredPatientsList = null;
				logger.error("Patient in TH+ system with the specified Pat Id is already paused");
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_ALREADY_PAUSED.toString()));
				throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
			}
		}


		// Create acknowledgement.
		ACKCONTENT appAck = null;
		try
		{
			// Create an Application Ack for the incoming message
			appAck = (ACKCONTENT) CreatorFactory.getCreator(getCustomerId()).createMessage("ACK", this.ackType,
					message, error);
		}
		catch (CreationException ex)
		{
			logger.error("Exception occurred while initializing creator to create app ack.");
			throw new ProcessException("Exception occurred while initializing creator to create app ack. " + ex.
					getMessage());
		}

		return appAck;
	}

	/**
	 * Method to process the filtered patients list and perform appropriate action of admission, re-admission, unpause or reject.
	 * @param patientsList
	 * @return IntegrationPatientType response patient
	 * @throws ProcessException
	 */
	private IntegrationPatientType processPatients(List<IntegrationPatientType> patientsListByLocation,
			List<IntegrationPatientType> patientsListByIntegrationStyle,
			IntegrationPatientType messagePatient, Object message) throws ProcessException
			{
		// patientsListByIntegrationStyle is empty. No patients matching Pat Id or SSN are found.
		// Admit a new patient in signup org.
		if (patientsListByIntegrationStyle.isEmpty())
		{
			try
			{
				//INT-687---A01/A04 for create patient DOB < current Date
				//for unpause/readmit/update/pause patient DOB < enrollDate
				if (messagePatient.getBirthDate() != null)
				{
					//IF DOB is greater than Current Date throw procss Exception
					long currentTimestamp = System.currentTimeMillis();
					long dateEntered = messagePatient.getBirthDate().toGregorianCalendar().getTimeInMillis();
					long getRidOfTime = 1000 * 60 * 60 * 24;
					long dateEnteredWithoutTime = dateEntered / getRidOfTime;
					long currentTimestampWithoutTime = currentTimestamp / getRidOfTime;
					if (dateEnteredWithoutTime >= currentTimestampWithoutTime)
					{
						logger.error("Birth Date cannot be greater than Current Date");
						error.getErrorList().add(
								MessageUtils.createError(ConfigKeyEnum.DOB_CURRENTDATE_ERROR.toString()));
						throw new ProcessException("Birth Date cannot be greater than Current Date");
					}
				}
				logger.info("Invoking CIA's admit patient operation to admit a new patient with internal org Id: " + messagePatient.
						getInternalOrganizationId());
				return getCiaServiceProxy().admitPatient(messagePatient, error);
			}
			catch (Exception ex)
			{
				logger.error("Exception returned by CIA admitPatient operation.");
				throw new ProcessException("Exception returned by CIA admitPatient operation. " + ex.getMessage());
			}
		}
		else
		{
			// Check if the patient in the incoming message is unique in signup organization.
			// Two patient records with same SSN and different Pat Id are not unique. Duplicate SSN error should be returned.
			// Only one record with a Pat Id and/or SSN Id should exist within an organization.
			checkPatientUniqueness(messagePatient, patientsListByLocation, patientsListByIntegrationStyle);

			IntegrationPatientType existingPatient = patientsListByIntegrationStyle.get(0);

			existingPatient = updateExistingPatientWithMessagePatient(messagePatient, existingPatient, message);
			// If the patient is paused, unpause date needs to be set. This is required by Patient service.
			// TODO for TH+2.0 release, SA adds current timestamp. But for later releases this date needs to come in the incoming message.
			// Current TimeStamp should be used only when this date is missing in the incoming message.
			if (StatusType.PAUSED.toString().equalsIgnoreCase(existingPatient.getStatus().toString()))
			{
				try
				{
					existingPatient.setEffectiveStateChangeDate(Utils.convertStringToXMLGregorianCalendar(Utils.
							createCurrentTimestamp(new String())));
				}
				catch (Exception ex)
				{
					logger.error("Exception occurred while converting string date to XMLGregorianCalendar Type.");
					throw new ProcessException("Exception occurred while converting string date to XMLGregorianCalendar Type. " + ex.
							getMessage());
				}
			}

			if (existingPatient.isIsIntegration())
			{
				// Process Integrated Patient
				logger.debug("Processing integrated patient.");
				return processIntegratedPatient(existingPatient);
			}
			else
			{
				// Process Non-Integrated Patient
				logger.debug("Processing non integrated patient.");
				//Adding PatID to the existing Patient Information.
				try
				{
					existingPatient.getIdentifier().getExternalIdentifier().add(MessageUtils.getPatId(message));
					existingPatient.setIsIntegration(true);
				}
				catch (Exception ex)
				{
					logger.error("Exception occurred while adding the PAT ID For non integrated patient.");
					throw new ProcessException("Exception occurred while adding the PAT ID For non integrated patient. " + ex.
							getMessage());
				}
				return processNonIntegratedPatient(existingPatient);
			}
		}
			}

	/**
	 * Method to process an integrated patient and perform appropriate action of re-admission or unpause or reject.
	 * @param patient existing patient
	 * @return IntegrationPatientType response patient
	 * @throws ProcessException
	 */
	private IntegrationPatientType processIntegratedPatient(IntegrationPatientType messagePatient) throws
	ProcessException
	{
		StatusType statusType = messagePatient.getStatus();
		switch (statusType)
		{
		case ENROLLED:
			// If the existing patient with PS id is already enrolled in the signup org, then do nothing and reject the message.
			logger.error("Patient with patient internal id is already enrolled and unpaused in signup organization:  " + messagePatient.
					getIdentifier().getInternalIdentifier());
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_ALREADY_EXISTS.toString()));
			throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
		default:
			try
			{
				logger.info("Invoking CIA admitPatient operation to Unpause or Re-enroll a patient. " + messagePatient.
						getIdentifier().getInternalIdentifier());
				return getCiaServiceProxy().admitPatient(messagePatient, error);
			}
			catch (Exception ex)
			{
				logger.error("Exception returned by CIA admitPatient operation.");
				throw new ProcessException("Exception returned by CIA admitPatient operation. " + ex.getMessage());
			}
		}
	}

	/**
	 * Method to process a non integrated patient and perform appropriate action of integration, admission, re-admission, unpause or reject.
	 * @param patient existing patient
	 * @return IntegrationPatientType response patient
	 * @throws ProcessException
	 */
	private IntegrationPatientType processNonIntegratedPatient(IntegrationPatientType messagePatient) throws
	ProcessException
	{
		try
		{
			logger.info("Invoking CIA admitPatient operation to integrate a non integrated patient with internal pat Id: " + messagePatient.
					getIdentifier().getInternalIdentifier());
			return getCiaServiceProxy().admitPatient(messagePatient, error);
		}
		catch (Exception ex)
		{
			logger.error("Exception returned by CIA admitPatient operation.");
			throw new ProcessException("Exception returned by CIA admitPatient operation. " + ex.getMessage());
		}
	}

	/*  COMPLETE CODE IS RE-DESIGNED AND MOVED TO RESULT PROCESSOR.... */

	/**
	 * Method to process results request QVR_Q17 and return ORU_R30 response message.
	 * @param message QVR_Q17 incoming message
	 * @return ORU_R30 response message
	 *
	 * NOTE: For TH+2.0 release - When a QVR_Q17 message is received, by default it is treated as an unbounded query
	 * and only unacked session related vitals are sent back in response message.
	 *//*
    private ORUR30BATCHCONTENT processResultsRequest(QVRQ17CONTENT message) throws ProcessException
    {
        // Find signup org in the TH+ system given external org id in the incoming message.
        // Verify if the results org is found and if its mapped to an internal org and if it is integrated.
        OrganizationType resultsOrg = findAndVerifySignupOrganization(message);

        // Get the internal org id of the found results org id.
        String resultsOrgInternalId = resultsOrg.getInternalOrganization().get(0).getOrganizationIdentifier();

        // Get a list of unacked results belonging to patients within resultsOrg.
        List<Result> sessionUnackedResults = new ArrayList<Result>();
        sessionUnackedResults = Result.findResultsByOrganizationId(resultsOrgInternalId, Boolean.FALSE,
                ResultTypeEnum.SESSION_VITAL_SIGN.toString());
        List<Result> adhocUnackedResults = new ArrayList<Result>();
        adhocUnackedResults = Result.findResultsByOrganizationId(resultsOrgInternalId, Boolean.FALSE,
                ResultTypeEnum.VITAL_SIGN.toString());
        // Create ORU_R30_BATCH results response message.
        ORUR30BATCHCONTENT resultsResponse = null;
        if (sessionUnackedResults.size() == 0 && adhocUnackedResults.size() == 0)
        {
            try
            {
                resultsResponse = (ORUR30BATCHCONTENT) CreatorFactory.getCreator(getCustomerId()).createMessage(
                        "ORU_R30",
                        null, message.getMSH(),message.getRCP());
            }
            catch (CreationException ex)
            {
                logger.error("Exception occurred while initializing creator to create results response message.");
                throw new ProcessException("Exception occurred while initializing creator to create results response message. " + ex.
                        getMessage());
            }
            return resultsResponse;
        }

        //TODO:Do the below operation only if sessionUnackedResults are not empty
        List<ResultId> sessionResultsToQuery = new ArrayList<ResultId>();
        if (!sessionUnackedResults.isEmpty())
        {
            // Create a list of ResultId types with session id as ResultId and type as Session.
            //This list needs to be passed to CIA and then to Results service to query for results (sessions) by session ids.
            //This set tracks unique session ids.
            //Do we need this
            Set<Long> uniqueResultIds = new HashSet<Long>();
            for (Result result : sessionUnackedResults)
            {
                // NOTE: For TH+ 2.0 release only session related vitals (objective results) are sent back to the customer EMR.
                if (result.getType().equalsIgnoreCase(ResultTypeEnum.SESSION_VITAL_SIGN.toString()))
                {
                    ResultId resultId = new ResultId();
                    resultId.setId(result.getSessionId());
                    resultId.setType(ResultsType.SESSION);
                    // If resultId with specified session Id already exists in the list then do not add it to the list again.
                    // This is to avoid querying Results service multiple times for the same session.
                    if (uniqueResultIds.add(resultId.getId()))
                    {
                        sessionResultsToQuery.add(resultId);
                    }
                }
            }

            // Once the unique ResultId objects are created, this set is of no use. Clear the set to free the memory.
            //Todo swetha
            uniqueResultIds.clear();
        }
        List<ResultId> adhocResultsToQuery = new ArrayList<ResultId>();
        if (!adhocUnackedResults.isEmpty())
        {
            Set<Long> uniqueResultIds = new HashSet<Long>();
            for (Result result : adhocUnackedResults)
            {
                if (result.getType().equalsIgnoreCase(ResultTypeEnum.VITAL_SIGN.toString()))
                {
                    ResultId resultId = new ResultId();
                    resultId.setId(result.getResultId());
                    resultId.setType(ResultsType.METER_MEASUREMENT);
                    if (uniqueResultIds.add(resultId.getId()))
                    {
                        adhocResultsToQuery.add(resultId);
                    }
                }
            }
            uniqueResultIds.clear();
        }
        Long internalOrgId = null;
        if (resultsOrgInternalId != null && !resultsOrgInternalId.trim().equals(""))
        {
            internalOrgId = Long.parseLong(resultsOrgInternalId);
        }
        List<PushResults> filteredUnackedResults = new ArrayList<PushResults>();
        // Invoking findSessionResults and findAdhocResults operation which inturn invokes findResults operation of CIA.
        // It also creates an IdFilter by passing the list of result ids to be queried.
        if (!sessionResultsToQuery.isEmpty())
        {
            List<SurveyResponse> surveyResponse = findSessionResults(sessionResultsToQuery);
            // Convert unackedResults list to a map
            Map<Long, Result> sessionUnackedResultsMap = createUnackedResultsMap(sessionUnackedResults);

            // Filter the unacked results from the list of results returned by the results service matching session ids.
            List<PushResults> filteredSessionUnackedResults = filterSessionUnacknowledgedResults(surveyResponse,
                    sessionUnackedResultsMap,
                    internalOrgId);
            filteredUnackedResults.addAll(filteredSessionUnackedResults);
            filteredSessionUnackedResults = null;
            sessionUnackedResultsMap = null;
        }
        if (!adhocResultsToQuery.isEmpty())
        {
            List<MeterMeasurement> meterMeasurements = findAdhocResults(adhocResultsToQuery);
            // Convert unackedResults list to a map
            Map<Long, Result> adhocUnackedResultsMap = createUnackedResultsMap(adhocUnackedResults);
            // Filter the unacked results from the list of results returned by the results service matching Result ids.
            List<PushResults> filteredAdhocUnackedResults = filterAdhocUnacknowledgedResults(meterMeasurements,
                    adhocUnackedResultsMap,
                    internalOrgId);
            filteredUnackedResults.addAll(filteredAdhocUnackedResults);
            filteredAdhocUnackedResults = null;
            adhocUnackedResultsMap = null;

        }
        // Create ORU_R30_BATCH results response message.
        //ORUR30BATCHCONTENT resultsResponse = null;
        try
        {
            // Create an ORU_R30_BATCH results response message for the incoming QVR_Q17 message
            resultsResponse = (ORUR30BATCHCONTENT) CreatorFactory.getCreator(getCustomerId()).createMessage("ORU_R30",
                    filteredUnackedResults, message.getMSH(),message.getRCP());
        }
        catch (CreationException ex)
        {
            logger.error("Exception occurred while initializing creator to create results response message.");
            throw new ProcessException("Exception occurred while initializing creator to create results response message. " + ex.
                    getMessage());
        }

        return resultsResponse;
    }
	  */
	/**
	 * Method to ack results in the SA DB based on Vital Sign Id.
	 * @param ackBatch
	 * @return ack
	 */
	public ACKCONTENT processAcknowledgments(ACKBATCHCONTENT message) throws ProcessException
	{
		BHSCONTENT bhsContent = message.getBHS();
		if(bhsContent!=null && bhsContent.getBHS4()!=null && bhsContent.getBHS4().getValue()!=null)
		{
			OrganizationType signupOrg = findAndVerifySignupOrganization(message);
			List<ACKCONTENT> ackList = message.getACK();
			for (ACKCONTENT ackContent : ackList)
			{
				if(ackContent != null && ackContent.getMSH()!=null && ackContent.getMSH().getMSH4()!=null &&
						ackContent.getMSH().getMSH4().getHD1()!=null && ackContent.getMSH().getMSH4().getHD1().getValue()!=null &&
						ackContent.getMSH().getMSH4().getHD1().getValue().equals(bhsContent.getBHS4().getValue()))
				{
					String organizationId = MessageUtils.getInternalOrganizationList(signupOrg).toString();
					if (ackContent != null && ackContent.getMSA() != null)
					{
						if (ackContent.getMSA().getMSA1() != null && ackContent.getMSA().getMSA1().getValue().equalsIgnoreCase(AckResponseCodeEnum.AA.toString()))
						{
							String resultId = ackContent.getMSA().getMSA2().getValue();
							if(resultId!=null) {
								if(resultId.contains("_V")){
									resultId = resultId.replace("_V", "");
									List<Result> unacknowledgedResults = resultDao.findResultsByVitalSignAndOrgId(resultId, organizationId, Boolean.FALSE, Arrays.asList(ResultTypeEnum.SESSION_VITAL_SIGN.toString(),ResultTypeEnum.VITAL_SIGN.toString()));
									resultPersister.persist(unacknowledgedResults);
									logger.info("Unacked Results with Result Id: " + resultId + " in the SA DB have been updated with ack info.");
								}
								else if(resultId.contains("_S")){
									resultId = resultId.replace("_S", "");
									List<SessionResult> unacknowledgedSessions = sessionResultDao.findSessionResultBySessionId(resultId, Boolean.FALSE);
									sessionPersister.persist(unacknowledgedSessions);

									//Acknowledge all the vitals corresponding to the acknowledged session...
									List<Result> unacknowledgedResults = resultDao.findResultsBySessionId(resultId, organizationId, Boolean.FALSE, ResultTypeEnum.SESSION_VITAL_SIGN.toString());
									resultPersister.persist(unacknowledgedResults);

									logger.info("Unacked Sessions with Session Id: " + resultId + " in the SA DB have been updated with ack info.");

								}else if(resultId.contains("_N")){
                                    resultId = resultId.replace("_N", "");
                                    List<Notes> unacknowledgedNotes = notesDao.findNotesByNoteIdAndOrgId(Long.parseLong(resultId), organizationId);
                                    notesPersister.acknowledgeNotes(unacknowledgedNotes);
                                    logger.info("Unacked Notes with Notes Id: " + resultId + " in the SA DB have been updated with ack info.");
                                }
							}
						}
					}
				}
			}
		}

		// Create response acknowledgment....
		ACKCONTENT appAck = null;
		try {
			appAck = (ACKCONTENT) CreatorFactory.getCreator(getCustomerId()).createMessage("ACK", this.ackType, message, error);
		}
		catch (CreationException ex) {
			logger.error("Exception occurred while initializing creator to create app ack.");
			throw new ProcessException("Exception occurred while initializing creator to create app ack. " + ex.getMessage());
		}

		return appAck;
	}

	/**
	 * Method to filter patients by location.
	 * @param patients list
	 * @return list of filtered patients
	 */
	private List<IntegrationPatientType> filterPatientsByLocation(List<IntegrationPatientType> patientsList,
			OrganizationType signupOrg) throws ProcessException
			{
		logger.debug("Filtering out patients that do not belong to the signup organization");
		String signupOrgInternalId = signupOrg.getInternalOrganization().get(0).getOrganizationIdentifier();
		logger.debug("Patient signup Organization Internal Id: " + signupOrgInternalId);
		List<IntegrationPatientType> filteredPatientsByLocation = new ArrayList<IntegrationPatientType>();

		for (IntegrationPatientType patient : patientsList)
		{
			// Process patients that belong to accounts other than signup and add to the new filtered list
			if (!(signupOrgInternalId.equalsIgnoreCase(patient.getInternalOrganizationId())))
			{
				logger.debug("Patient does not exist in signup organization with internal Id: " + patient.
						getInternalOrganizationId());
				logger.debug("Adding the patient existing in organization other than signup to a list");
				// Add patients that belong to organizations other than signup to a new list
				filteredPatientsByLocation.add(patient);
			}
		}
		logger.info("Filtering out patients that do not belong to the signup organization");
		// Remove patients in new list (filteredPatientsByLocation) from the patients list
		patientsList.removeAll(filteredPatientsByLocation);
		return patientsList;
			}

	/**
	 * Method to filter patients by Integration Style. Integrated and non-integrated patients are separated.
	 * @param patientsList
	 * @return list of patients filtered by Integration Style
	 */
	private List<IntegrationPatientType> filterPatientsByIntegrationStyle(List<IntegrationPatientType> patientsList)
	{
		logger.debug("Filtering patients by Integration Style");
		List<IntegrationPatientType> integratedPatientsList = new ArrayList<IntegrationPatientType>();
		List<IntegrationPatientType> nonIntegratedPatientsList = new ArrayList<IntegrationPatientType>();

		for (IntegrationPatientType patient : patientsList)
		{
			if (patient.isIsIntegration())
			{
				integratedPatientsList.add(patient);
			}
			else
			{
				nonIntegratedPatientsList.add(patient);
			}
		}

		// Integrated patients has priority over non integrated patients
		if (!integratedPatientsList.isEmpty())
		{
			logger.debug("Returning integrated patients list");
			return integratedPatientsList;
		}
		else
		{
			logger.debug("Returning non integrated patients list");
			return nonIntegratedPatientsList;
		}
	}

	/**
	 * Method to filter patients by PatId. Patients with Pat id and SSN are separated.
	 * @param patientsList
	 * @return list of patients filtered by PatId
	 */
	private List<IntegrationPatientType> filterPatientsByPatId(List<IntegrationPatientType> patientsList,
			IntegrationPatientType messagePatient)
			{
		logger.debug("Filtering patients by Identifiers");
		List<IntegrationPatientType> patientsListByPatId = new ArrayList<IntegrationPatientType>();
		String identifierName = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.toString());
		String messagePatId = getIdentifierValueByName(messagePatient, identifierName);

		for (IntegrationPatientType existingPatient : patientsList)
		{
			String existingPatId = getIdentifierValueByName(existingPatient, identifierName);

			if (messagePatId.equalsIgnoreCase(existingPatId))
			{
				patientsListByPatId.add(existingPatient);
			}
		}
		return patientsListByPatId;
			}

	private List<IntegrationPatientType> filterPatientsBySSN(List<IntegrationPatientType> patientsList,
			IntegrationPatientType messagePatient)
			{
		logger.debug("Filtering patients by Identifiers");
		List<IntegrationPatientType> patientsListByPatId = new ArrayList<IntegrationPatientType>();
		String identifierName = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.SSN.toString());
		String messagessn = getIdentifierValueByName(messagePatient, identifierName);

		for (IntegrationPatientType existingPatient : patientsList)
		{
			String existingPatId = getIdentifierValueByName(existingPatient, identifierName);

			if (existingPatId != null && messagessn.equalsIgnoreCase(existingPatId))
			{
				patientsListByPatId.add(existingPatient);
			}
		}
		return patientsListByPatId;
			}

	/**
	 * Method to filter patients by SSN.
	 * @param patientsList
	 * @return list of patients filtered by SSN
	 */
	private List<IntegrationPatientType> filterPatientsBySSN(List<IntegrationPatientType> patientsList,
			List<IntegrationPatientType> patientsListByPatId, IntegrationPatientType messagePatient)
			{
		logger.debug("Filtering patients by SSN");
		List<IntegrationPatientType> list = new ArrayList<IntegrationPatientType>();
		list.addAll(patientsList);
		list.removeAll(patientsListByPatId);
		return filterPatientsBySSN(list, messagePatient);
			}

	/*  COMPLETE CODE IS RE-DESIGNED AND MOVED TO RESULT PROCESSOR....
  	private List<PushResults> filterSessionUnacknowledgedResults(List<SurveyResponse> sessions,
            Map<Long, Result> unackedResults, Long internalOrgId) throws ProcessException
    {
        // This map stores patients internal id as key and PushResults as value.
        // PushResults object holds patient's unacked results and patient information.
        // FindResultsReply holds results related to different patients.
        // The patient to which the current result belongs to is searched for in the map and the results are added to PushResults object.
        Map<String, PushResults> resultsToReport = new HashMap<String, PushResults>();
        List<PushResults> pushResultList = new ArrayList<PushResults>();
        if (sessions == null || sessions.size() == 0)
        {
            logger.debug("No sessions in findResultsReply. No vital sign results to report back to the customer.");
            pushResultList.addAll(resultsToReport.values());
            return pushResultList;
        }

        for (SurveyResponse session : sessions)
        {
            String patientId = null;
            Long sessionId = session.getId();
            List<QuestionResponse> unackedResponses = new ArrayList<QuestionResponse>();
            List<QuestionResponse> responses = session.getQuestionResponse();
            if (responses == null || responses.size() == 0)
            {
                logger.debug(
                        "No question responses in session " + sessionId + ". No vital sign results to report back to the customer.");
                continue;
            }
            for (QuestionResponse response : responses)
            {
                // unackedMeasurements list stores all the unacked session related vitals.
                List<MeterMeasurement> unackedMeasurements = new ArrayList<MeterMeasurement>();
                List<MeterMeasurement> measurements = response.getMeterMeasurement();
                if (measurements == null || measurements.size() == 0)
                {
                    continue;
                }
                for (MeterMeasurement measurement : measurements)
                {
                    Long resultId = measurement.getId();
                    logger.info("Meter Measurement to report back exists with id" +resultId);
                    Result unackedResult = unackedResults.get(resultId);

                    if (unackedResult != null)
                    {
                        if (unackedResult.getSessionId().equals(sessionId) && unackedResult.getResultId().equals(
                                resultId))
                        {
                            unackedMeasurements.add(measurement);

                            // A single session can only belong to one patient.
                            // If the patientId is assigned once, there is no need to update it on all the other unacked results.
                            // TODO: we many need to consider changing it to BTP Id instaed of patient Id since we get BTP_ID as
                            // part of each seesion then we don't need to look for extracting patient id.
                            if (null == patientId)
                            {
                                patientId = unackedResult.getPatientId();
                                logger.info("Patient Id for which results exist are" +patientId);
                            }
                            break;

                        }
                    }
                }

                if (!unackedMeasurements.isEmpty())
                {
                    // Clear all the MeterMeasurements from the current response and add the unacked measurements.
                    // response.getMeterMeasurement().clear();
                    measurements.clear();
                    // response.getMeterMeasurement().addAll(unackedMeasurements);
                    measurements.addAll(unackedMeasurements);

                    // Add the current response to the unackdResponses list.
                    unackedResponses.add(response);
                }
            }

            // Only add the current session to resultsToReport list if it has any unacked results to report.
            if (!unackedResponses.isEmpty())
            {
                //session.getQuestionResponse().clear();
                responses.clear();
                //session.getQuestionResponse().addAll(unackedResponses);
                responses.addAll(unackedResponses);

                // If the current patient does not exist in the map, add the patient and instantiate PushResults object for the patient.
                if (!resultsToReport.containsKey(patientId))
                {
                    resultsToReport.put(patientId, new PushResults());
                }
                resultsToReport.get(patientId).getSurveyResponse().add(session);
            }
        }
        // Add patient information to PushResults object
        // Find patients by external Id
        Set<String> patientExternalIds = resultsToReport.keySet();
        List<ExternalIdentifierType> externalIds = new ArrayList<ExternalIdentifierType>();
        List<IntegrationPatientType> allPatients = new ArrayList<IntegrationPatientType>();
        String idName = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.toString());
        for (String patientExternalId : patientExternalIds)
        {
            ExternalIdentifierType externalId = new ExternalIdentifierType();
            externalId.setName(idName);
            externalId.setValue(patientExternalId);
            externalIds.add(externalId);
            List<IntegrationPatientType> patients = findPatientsByIdInOrganization(externalIds, internalOrgId);
            //This will return the same patient record twice one for SSN and the other for PS-ID
            //So adding the first record will avoid duplicates.
            allPatients.addAll(patients);
            patients.clear();
        }
        //List<IntegrationPatientType> patients = findPatients(externalIds,internalOrgId);
        Map<String, PushResults> enrichedResults = new HashMap<String, PushResults>();
        for (IntegrationPatientType patient : allPatients)
        {
            String idvalue = getIdentifierValueByName(patient, idName);
            if (resultsToReport.containsKey(idvalue))
            {
                PushResults pushResults = resultsToReport.get(idvalue);
                pushResults.setIntegrationPatient(patient);
                enrichedResults.put(idvalue, pushResults);
                resultsToReport.remove(idvalue);
            }
        }
        allPatients.clear();
        allPatients = null;
        for (PushResults result : enrichedResults.values())
        {
            pushResultList.add(result);
        }
        resultsToReport.clear();
        resultsToReport = null;
        enrichedResults.clear();
        enrichedResults = null;
        return pushResultList;

    }

    private List<PushResults> filterAdhocUnacknowledgedResults(List<MeterMeasurement> meterMeasurements,
            Map<Long, Result> unackedResults, Long internalOrgId) throws ProcessException
    {
        // This map stores patients internal id as key and PushResults as value.
        // PushResults object holds patient's unacked results and patient information.
        // FindResultsReply holds results related to different patients.
        // The patient to which the current result belongs to is searched for in the map and the results are added to PushResults object.
        Map<String, PushResults> resultsToReport = new HashMap<String, PushResults>();
        List<PushResults> pushResultList = new ArrayList<PushResults>();
        if (meterMeasurements == null || meterMeasurements.size() == 0)
        {
            logger.debug("No vital sign results to report back to the customer.");
        }
        List<MeterMeasurement> unackedMeasurements = new ArrayList<MeterMeasurement>();

        for (MeterMeasurement measurement : meterMeasurements)
        {
            Long resultId = measurement.getId();
            String patientId = null;
            Result unackedResult = unackedResults.get(resultId);
            if (unackedResult != null)
            {
                if (unackedResult.getResultId().equals(
                        resultId))
                {
                    unackedMeasurements.add(measurement);

                    // A single measurement can only belong to one patient.
                    // If the patientId is assigned once, there is no need to update it on all the other unacked results.
                    if (null == patientId)
                    {
                        patientId = unackedResult.getPatientId();
                    }
                    // break;
                }
            }

            if (!unackedMeasurements.isEmpty())
            {
                // If the current patient does not exist in the map, add the patient and instantiate PushResults object for the patient.
                if (!resultsToReport.containsKey(patientId))
                {
                    resultsToReport.put(patientId, new PushResults());
                }
                resultsToReport.get(patientId).getMeterMeasurement().add(measurement);
            }
        }

        // Add patient information to PushResults object
        // Find patients by external Id
        Set<String> patientExternalIds = resultsToReport.keySet();
        List<ExternalIdentifierType> externalIds = new ArrayList<ExternalIdentifierType>();
         List<IntegrationPatientType> allPatients = new ArrayList<IntegrationPatientType>();
        String idName = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.toString());
        for (String patientExternalId : patientExternalIds)
        {
            ExternalIdentifierType externalId = new ExternalIdentifierType();
            externalId.setName(idName);
            externalId.setValue(patientExternalId);
            externalIds.add(externalId);

             List<IntegrationPatientType> patients = findPatientsByIdInOrganization(externalIds, internalOrgId);
            //This will return the same patient record twice one for SSN and the other for PS-ID
            //So adding the first record will avoid duplicates.
            allPatients.addAll(patients);
            patients.clear();
        }
       // List<IntegrationPatientType> patients = findPatientsByIdInOrganization(externalIds, internalOrgId);
        Map<String, PushResults> enrichedResults = new HashMap<String, PushResults>();
        for (IntegrationPatientType patient : allPatients)
        {
            String idvalue = getIdentifierValueByName(patient, idName);

            if (resultsToReport.containsKey(idvalue))
            {
                //resultsToReport.get(getIdentifierValueByName(patient, idName)).setIntegrationPatient(patient);
                PushResults pushResults = resultsToReport.get(idvalue);
                pushResults.setIntegrationPatient(patient);
                enrichedResults.put(idvalue, pushResults);
                resultsToReport.remove(idvalue);
            }
        }

        for (PushResults result : enrichedResults.values())
        {
            pushResultList.add(result);
        }
        allPatients.clear();
        allPatients = null;
        resultsToReport.clear();
        enrichedResults.clear();
//        pushResultList.addAll(resultsToReport.values());
//        return (PushResults) resultsToReport.values();
        return pushResultList;
    }*/

	/**
	 * Method to map patient info from message to a new IntegrationPatientType object.
	 * @param message incoming message
	 * @param identifiers from message
	 * @return patient object
	 */
	private IntegrationPatientType mapMessagePatient(IntegrationPatientType existingPatient, Object message,
			List<ExternalIdentifierType> identifiers,
			OrganizationType signupOrg) throws ProcessException
			{
		try
		{
			IntegrationPatientType patient = new IntegrationPatientType();
			// isIntegration property is always set to true as this is a message sent through integration app
			// Patient service needs this property to differentiate between different apps invoking it
			patient.setIsIntegration(true);
			// Set patient identifiers
			IdentifierType identifierType = new IdentifierType();
			identifierType.getExternalIdentifier().addAll(identifiers);
			//identifierType.setInternalIdentifier(MessageUtils.getInternalOrganizationList(signupOrg));
			if (existingPatient != null)
			{
				logger.debug("existing patient is not null");
				identifierType.setInternalIdentifier(existingPatient.getIdentifier().getInternalIdentifier());
				identifierType.setVersionCount(existingPatient.getIdentifier().getVersionCount());
			}
			patient.setIdentifier(identifierType);
			// Set patient DOB
			patient.setBirthDate(MessageUtils.getPatientBirthDate(message));
			// Set patient name
			patient.setFirstName(MessageUtils.getPatientFirstName(message));
			patient.setMiddleInitial(MessageUtils.getPatientMiddleName(message));
			patient.setLastName(MessageUtils.getPatientLastName(message));
			// Set patient gender
			patient.setGender(MessageUtils.getPatientGender(message));
			// Set patient address
			patient.getAddress().add(MessageUtils.getPatientAddress(message));
			// Set patient phone number
			patient.getPhoneNumber().add(MessageUtils.getPatientPhoneNumber(message));
			// Set signup organization
			patient.setInternalOrganizationId(
					signupOrg.getInternalOrganization().get(0).getOrganizationIdentifier());
			patient.setExternalOrganizationId(signupOrg.getExternalOrganization().getOrganizationIdentifier());

			return patient;
		}
		catch (Exception ex)
		{
			logger.error("Exception occurred while populating message patient object", ex);
			throw new ProcessException("Exception occurred while populating message patient object", ex);
		}
			}

	/**
	 * Method to check if the patient in incoming message is unique.
	 * @param messagePatient
	 * @param patientsListByLocation
	 * @throws ProcessException
	 */
	private void checkPatientUniqueness(IntegrationPatientType messagePatient,
			List<IntegrationPatientType> patientsListByLocation,
			List<IntegrationPatientType> patientsListByIntegrationStyle) throws ProcessException
			{
		List<IntegrationPatientType> patientsListByPatId = filterPatientsByPatId(patientsListByLocation, messagePatient);
		List<IntegrationPatientType> patientsListBySSN = filterPatientsBySSN(patientsListByLocation, patientsListByPatId,
				messagePatient);
		//List<IntegrationPatientType> patientsListBySSN = filterPatientsBySSN(patientsListByLocation, messagePatient);
		if ((null != patientsListByIntegrationStyle && patientsListByIntegrationStyle.isEmpty()) &&
				(null != patientsListByPatId && patientsListByPatId.isEmpty()))
		{
			logger.error("patient with Pat Id does not exist.");
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_FOUND.toString()));
			throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
		}
		if (patientsListByPatId.size() > 1)
		{
			// If more than one patient with same Pat ID exist, then reject the message.
			logger.error("Another patient with same Pat Id already exists.");
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_ALREADY_EXISTS.toString()));
			throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
		}
		if (patientsListBySSN.size() > 1)
		{
			// If more than one patient with same SSN exist, then reject the message.
			logger.error("Another patient with same SSN already exists.");
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.DUPLICATE_SSN.toString()));
			throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
		}
		// PatientsListByPatId list is empty signifies a new patient, but with the SSN list being greater than 1 signifies for duplicate SSN.
		if (!patientsListBySSN.isEmpty())
		{
			IntegrationPatientType existingPatient = patientsListBySSN.get(0);
			if (existingPatient.isIsIntegration() && patientsListByPatId.isEmpty())
			{
				logger.error("Another patient with same SSN already exists.");
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.DUPLICATE_SSN.toString()));
				throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
			}
			//Not sure if this ever happens
			//if the patient is not integrated and trying to integrate the patient with the existing patient Identifier
			//then reject the message.
			if (!existingPatient.isIsIntegration() && !patientsListByPatId.isEmpty())
			{
				// If the existingPatient is integrated, then SSN is assigned to another patient in the signupOrg.
				// No need to compare the pat ids as the 2 lists patientsListByPatId and patientsListBySSN should hold unique patient objects.
				// If the existingPatient is non integrated, then check if a patient is already found with pat id.
				logger.error("Another patient with same SSN already exists.");
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.DUPLICATE_SSN.toString()));
				throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
			}
		}
			}

	/**
	 * This method returns identifier value of a patient given patient and identifier name.
	 * @param patient
	 * @param identifierName
	 * @return identifier value
	 */
	private String getIdentifierValueByName(IntegrationPatientType patient, String identifierName)
	{
		List<ExternalIdentifierType> externalIDs = patient.getIdentifier().getExternalIdentifier();

		for (ExternalIdentifierType id : externalIDs)
		{
			if (id != null && null != id.getName() && id.getName().equalsIgnoreCase(identifierName))
			{
				return id.getValue();
			}
		}
		return null;
	}

	/**
	 * Method to find and verify signup organization
	 * @param incoming message
	 * @return signup org
	 */
	private OrganizationType findAndVerifySignupOrganization(Object message) throws ProcessException
	{
		// Find the signup organization and validate it.
		String orgId = null;
		try
		{
			orgId = MessageUtils.getOrganizationId(message);
		}
		catch (Exception ex)
		{
			logger.error("Exception occurred while getting org Id from incoming message.");
			throw new ProcessException(
					"Exception occurred while getting org Id from incoming message. " + ex.getMessage());
		}

		// Find orgs in the TH+ system matching the org id and the customer id.
		List<OrganizationType> organizations = getCiaServiceProxy().getExternalOrganizations(orgId, getCustomerId().value(),error);
		OrganizationType signupOrg = null;

		// If the returned list is empty, the org with org id doesnot exist in the system.
		if (organizations.isEmpty())
		{
			logger.error("Cannot find Organization in TH+ system with the org Id:  " + orgId);
			error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.UNKNOWN_ORGANIZATION.toString()));
			throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
		}
		// Unlike VA for SA complete org id is passed as there is no concept of sub orgs mapping to an org.
		// Ideally a single org maching the ord id should be returned.
		// Assuming the above the first org object will be used.
		for (OrganizationType organization : organizations)
		{

			if (organization.getInternalOrganization().isEmpty())
			{
				logger.error("Organization is not mapped to any internal organization:  " + orgId);
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.ORGANIZATION_NOT_MAPPED.toString()));
				throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
			}
			else if (!organization.getInternalOrganization().get(0).isIsIntegrated())
			{
				logger.error("Organization is not integrated:  " + orgId);
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.ORGANIZATION_NOT_INTEGRATED.toString()));
				throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
			}
			else
			{
				signupOrg = organization;
				logger.info("Signup Organization - Internal Org Id: " + organization.getInternalOrganization().get(0).
						getOrganizationIdentifier());
				break;
			}
		}
		return signupOrg;
	}

	/**
	 * Method to get the customer Id from the config file.
	 * If no customer Id is configured then SA (Standard Adapter) is used as default.
	 * @return CustomerId
	 */
	private CustomerId getCustomerId()
	{
		if (customerId == null)
		{
			customerId = (CustomerId) ConfigurationLoader.getCustomerId();
			// Get the customer Id from the config file.
			return customerId == null ? CustomerId.SA : customerId;
		}
		else
		{
			// Return already initialised customerId value.
			return customerId;
		}
	}

	/**
	 * Method to update existing patient with info in message patient.
	 * If info in fields is null, existing patient will not change
	 * Only not null fields will be updated during demographic updates (A08/A31)
	 * @param messagePatient
	 * @param existingPatient
	 * @return existingPatient
	 */
	private IntegrationPatientType updateExistingPatientWithMessagePatient(IntegrationPatientType messagePatient,
			IntegrationPatientType existingPatient, Object message) throws ProcessException
			{
		try
		{
			if (messagePatient.getBirthDate() != null)
			{
				//IF DOB is greater then Enroll Date throw procss Exception
				long enrollDate = getEnrollDate(existingPatient);
				long dateEntered = messagePatient.getBirthDate().toGregorianCalendar().getTimeInMillis();
				long getRidOfTime = 1000 * 60 * 60 * 24;
				long dateEnteredWithoutTime = dateEntered / getRidOfTime;
				if(dateEnteredWithoutTime > enrollDate ){
					logger.error("Birth Date cannot be greater then Enroll Date");
					error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.DOB_ERROR.toString()));
					throw new ProcessException("Birth Date cannot be greater then Enroll Date");
				}else{
					existingPatient.setBirthDate(messagePatient.getBirthDate());
				}
			}
			if (messagePatient.getFirstName() != null)
			{
				existingPatient.setFirstName(messagePatient.getFirstName().trim().isEmpty()?"":messagePatient.getFirstName());
			}
			if (messagePatient.getLastName() != null)
			{
				if (messagePatient.getLastName() != null && (!messagePatient.getLastName().trim().isEmpty()))
				{
					existingPatient.setLastName(messagePatient.getLastName());
				}
				else
				{
					existingPatient.setLastName(existingPatient.getLastName());
				}
			}
			if (messagePatient.getMiddleInitial() != null)
			{
				existingPatient.setMiddleInitial(messagePatient.getMiddleInitial().trim().isEmpty()?"":messagePatient.getMiddleInitial());
			}

			String gender = MessageUtils.getGenderInformationFromMessage(message);
			//INT-580 <urn:PID.8></urn:PID.8> then retain the existing value in the database if exist
			if (gender == null)
			{
				existingPatient.setGender(existingPatient.getGender());
			}
			else
			{
				if (messagePatient.getGender() != null)
				{
					existingPatient.setGender(messagePatient.getGender());
				}
				else
				{ //INT-580 <urn:PID.8>""</urn:PID.8> then update the database with null
					existingPatient.setGender(null);
				}
			}

			//Address Details ..
			if (messagePatient.getAddress() != null && !messagePatient.getAddress().isEmpty())
			{
				AddressType address = messagePatient.getAddress().get(0);
				AddressType existingPatientAddress = existingPatient.getAddress().get(0);
				if (address != null)
				{
					if (address.getCity() != null) {
						existingPatientAddress.setCity(address.getCity().trim().isEmpty()?"":address.getCity());
					}
					if (address.getCountry() != null) {
						existingPatientAddress.setCountry(address.getCountry().trim().isEmpty()?"":address.getCountry());
					}
					if (address.getState() != null) {
						existingPatientAddress.setState(address.getState().trim().isEmpty()?"":address.getState());
					}
					if (address.getZipCode() != null) {
						existingPatientAddress.setZipCode(address.getZipCode().trim().isEmpty()?"":address.getZipCode());
					}
					if (address.getStreet() != null && !address.getStreet().isEmpty()) {
						if (address.getStreet().get(0) != null) {
							existingPatientAddress.getStreet().set(0, address.getStreet().get(0).trim().isEmpty()?"":address.getStreet().get(0));
						}
						if (address.getStreet().get(1) != null) {
							existingPatientAddress.getStreet().set(1, address.getStreet().get(1).trim().isEmpty()?"":address.getStreet().get(1));
						}
					}
					existingPatient.getAddress().add(0, existingPatientAddress);
				}
			}

			//Phone Number...
			if (messagePatient.getPhoneNumber() != null && !messagePatient.getPhoneNumber().isEmpty())
			{
				PhoneNumberType phoneNumber = messagePatient.getPhoneNumber().get(0);
				if (phoneNumber != null && phoneNumber.getNumber() != null)
				{
					if (!existingPatient.getPhoneNumber().isEmpty()) {
						existingPatient.getPhoneNumber().get(0).setNumber(phoneNumber.getNumber().trim().isEmpty()?"":phoneNumber.getNumber());
					}
					else  {
						existingPatient.getPhoneNumber().add(phoneNumber);
					}
				}
			}

			// Check for the config parameter "UPDATE_PATIENT_IDENTIFIERS_LIST" value, for allowing an udpate for A04 message (i.e UnPause/Re-Enrollment). If its true allow update, else not.
			String triggerEvent = MessageUtils.getTriggerEvent(message);
			boolean allowSSNUpdate = true;
			if (triggerEvent != null && triggerEvent.equals("A04"))
			{
				allowSSNUpdate = false;
				//Patient Identifiers list ...
				Map<String, String> identifiersMap = ConfigurationLoader.getApplicationConfig(ConfigKeyEnum.UPDATE_PATIENT_IDENTIFIERS_LIST.toString());
				if (!identifiersMap.isEmpty()) {
					if (identifiersMap.containsKey(ConfigKeyEnum.SSN.toString())) {
						allowSSNUpdate = Boolean.valueOf((String) identifiersMap.get(ConfigKeyEnum.SSN.toString()));
					}
				}
			}

			//Allow update for A08/A31 message ..
			if(allowSSNUpdate) {
				String ssnId = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.SSN.toString());
				String ssnValue = getIdentifierValueByName(messagePatient, ssnId);
				if (ssnValue != null && !ssnValue.trim().isEmpty()) {
					List<ExternalIdentifierType> externalIDs = existingPatient.getIdentifier().getExternalIdentifier();
					for (ExternalIdentifierType id : externalIDs) {
						if (id.getName().equalsIgnoreCase(ssnId)) {
							id.setValue(ssnValue);
							break;
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			logger.error("Exception occurred while updateExistingPatient With MessagePatient", ex);
			throw new ProcessException("Exception occurred while update ExistingPatient With  MessagePatient", ex);
		}
		return existingPatient;
			}

	/*   COMPLETE CODE MOVED TO RESULT PROCESSOR...*/
	/**
	 * Method to create a map of resultId and Result object given a list of Result objects.
	 * @param unackedResults
	 * @return map
	 *//*
    private Map<Long, Result> createUnackedResultsMap(List<Result> unackedResults)
    {
        Map<Long, Result> unackedResultsMap = new HashMap<Long, Result>();
        for (Result unackedResult : unackedResults)
        {
            unackedResultsMap.put(unackedResult.getResultId(), unackedResult);
        }
        return unackedResultsMap;
    }*/

	/****************************************Methods to invoke CIA operations****************************************/
	/*    COMPLETE CODE MOVED TO CIA SERVICE PROXY FOR BETTER REFACTORING.... CIA Operations needs to be invoked thorough CIA service proxy..
	 * *//**
	 * Method to invoke CIA's getExternalOrganizations operation.
	 * @param orgId external org Id
	 * @return list of organizations matching external orgId
	 * @throws Exception
	 *//*
    private List<OrganizationType> getExternalOrganizations(String orgId, String customerId) throws ProcessException
    {
        GetExternalOrganizationsReply getExternalOrganizationsReply = new GetExternalOrganizationsReply();
        GetExternalOrganizations getExternalOrganizations = new GetExternalOrganizations();
        getExternalOrganizations.setOrganizationIdentifier(orgId);
        getExternalOrganizations.setOrganizationType(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.ORGANIZATION_TYPE.
                toString()));
        try
        {
            logger.info(
                    "Invoking getExternalOrganizations operation from CIA with Customer Id: " + customerId + "and Organization Id: " + orgId);
            getExternalOrganizationsReply = getCiaServiceProxy().getExternalOrganizations(getExternalOrganizations);
        }
        catch (RemoteAccessException re)
        {
            logger.error(" RemoteAccessException : ", re);
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
            throw new ProcessException(
                    "User Authentication Failed : " + Error.getError().getFaultInternalMessage());
        }
        catch (UserNotAuthorizedFault ex)
        {
            logger.error(" UserNotAuthorizedFault : ", ex);
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
            throw new ProcessException("User is not authorized to get external organizations given org id and customer id" + Error.
                    getError().
                    getFaultInternalMessage());
        }
        catch (ResourceNotFoundFault ex)
        {
            logger.error(" ResourceNotFoundFault : ", ex);
            throw new ProcessException(
                    "Resource is not found by the search string, org id", ex);
        }

        return getExternalOrganizationsReply.getExternalOrganization();
    }

	  *//**
	  * Method to invoke CIA's findPatients operation.
	  * @param identifiersList
	  * @return list of patients matching identifiers in the identifierList
	  * @throws Exception
	  *//*
    private List<IntegrationPatientType> findPatients(List<ExternalIdentifierType> identifiersList,
            Long internalIdentifier) throws
            ProcessException
    {
        GetPatientsReply getPatientsReply = new GetPatientsReply();
        GetPatients getPatients = new GetPatients();
        IdentifierType identifierType = new IdentifierType();
        identifierType.getExternalIdentifier().addAll(identifiersList);
        identifierType.setInternalIdentifier(internalIdentifier);
        getPatients.setIntegrationPatient(identifierType);
        try
        {
            logger.debug("Invoking CIA's findPatients operation");
            getPatientsReply = ciaService.getPatients(getPatients);
        }
        catch (RemoteAccessException re)
        {
            logger.error(" RemoteAccessException : ", re);
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
            throw new ProcessException(
                    "User Authentication Failed : " + Error.getError().getFaultInternalMessage());
        }
        catch (UserNotAuthorizedFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
            throw new ProcessException("User is not authorized to find patients in the system" + Error.getError().
                    getFaultInternalMessage());
        }
        return getPatientsReply.getIntegrationPatient();
    }

	   *//**
	   * Method to invoke CIA's findPatients operation.
	   * @param identifiersList
	   * @return list of patients matching identifiers in the identifierList
	   * @throws Exception
	   *//*
    private List<IntegrationPatientType> findPatients(List<ExternalIdentifierType> identifiersList) throws
            ProcessException
    {
        GetPatientsReply getPatientsReply = new GetPatientsReply();
        GetPatients getPatients = new GetPatients();
        IdentifierType identifierType = new IdentifierType();
        identifierType.getExternalIdentifier().addAll(identifiersList);
        getPatients.setIntegrationPatient(identifierType);
        try
        {
            logger.debug("Invoking CIA's findPatients operation");
            getPatientsReply = ciaService.getPatients(getPatients);
        }
        catch (RemoteAccessException re)
        {
            logger.error(" RemoteAccessException : ", re);
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
            throw new ProcessException(
                    "User Authentication Failed : " + Error.getError().getFaultInternalMessage());
        }
        catch (UserNotAuthorizedFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
            throw new ProcessException("User is not authorized to find patients in the system" + Error.getError().
                    getFaultInternalMessage());
        }
        return getPatientsReply.getIntegrationPatient();
    }

	    *//**
	    * Method to invoke CIA's findPatientsByIdInOrganization operation.
	    * @param identifiersList,orgID
	    * @return list of patients matching identifiers in the identifierList and the Organization
	    * @throws Exception
	    *//*
    private List<IntegrationPatientType> findPatientsByIdInOrganization(List<ExternalIdentifierType> identifiersList,
            long orgID) throws
            ProcessException
    {
        FindPatientsByIdInOrganizationReply findPatientsReply = new FindPatientsByIdInOrganizationReply();
        FindPatientsByIdInOrganization findPatients = new FindPatientsByIdInOrganization();
        IdentifierType identifierType = new IdentifierType();
        identifierType.getExternalIdentifier().addAll(identifiersList);
        identifierType.setInternalIdentifier(new Long(orgID));
        findPatients.setIntegrationPatient(identifierType);
        findPatients.setOrganizationId(orgID);
        try
        {
            logger.debug("Invoking CIA's findPatientsByIdInOrganization operation");
            findPatientsReply = ciaService.findPatientsByIdInOrganization(findPatients);
        }
        catch (RemoteAccessException re)
        {
            logger.error(" RemoteAccessException : ", re);
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
            throw new ProcessException(
                    "User Authentication Failed : " + Error.getError().getFaultInternalMessage());
        }
        catch (UserNotAuthorizedFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
            throw new ProcessException("User is not authorized to find patients in the system" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (InsufficientDataFault inSufficent)
        {
            logger.error(inSufficent.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
            throw new ProcessException("Insufficient Data to find patients in the system" + Error.getError().
                    getFaultInternalMessage());

        }
        catch (NoSuchPatientFault nosuchPatient)
        {
            logger.error(nosuchPatient.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_FOUND.toString()));
            throw new ProcessException("Patient Not Found in the system" + Error.getError().
                    getFaultInternalMessage());

        }
         if (logger.isDebugEnabled())
            {
                doMarshal(findPatientsReply);
            }
        return findPatientsReply.getIntegrationPatient();
    }

	     *//**
	     * Method to invoke CIA's admitPatient operation.
	     * This method is used to admit a new patient, unpause or re-enroll an existing patient.
	     * @param messagePatient
	     * @return AdmitReply object
	     * @throws Exception
	     *//*
    private IntegrationPatientType admitPatient(IntegrationPatientType messagePatient) throws ProcessException,
            NoSuchUserFault, PatientNotActiveFault, UserNotActiveFault, UserNotAssignableFault, DuplicatePatientFault,
            InvalidDataFault, PatientAlreadyEnrolledFault, RemoteServiceFault, PatientNotPausedFault
    {
        AdmitPatientReply admitPatientReply = new AdmitPatientReply();
        AdmitPatient admitPatient = new AdmitPatient();
        admitPatient.setIntegrationPatient(messagePatient);

        try
        {

            logger.debug("Invoking CIA's admit operation");
            if (logger.isDebugEnabled())
            {
                doMarshal(admitPatient);
            }

            admitPatientReply = ciaService.admitPatient(admitPatient);

            if (logger.isDebugEnabled())
            {
                doMarshal(admitPatientReply);
            }
        }
        catch (RemoteServiceFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
            throw new ProcessException("Remote Service Error: " + Error.getError().
                    getFaultInternalMessage());
        }
        catch (NoSuchPatientFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_FOUND.toString()));
            throw new ProcessException("Patient with external ids is not found in the system: " + Error.getError().
                    getFaultInternalMessage());
        }
        catch (ResourceNotFoundFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.RESOURCE_NOT_FOUND.toString()));
            throw new ProcessException("Required resource to admit/update patient is not found" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (InsufficientDataFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
            throw new ProcessException("Data provided to admit/update patient is insufficient" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (OptimisticLockingFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.OPTIMISTIC_LOCKING.toString()));
            throw new ProcessException("Another thread is trying to operate on the same DB record" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (UserNotAuthorizedFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
            throw new ProcessException("User is not authorized to admit/update the patient" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (OrganizationNotActiveFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.ORGANIZATION_NOT_ACTIVE.toString()));
            throw new ProcessException("Patient's signup organization is not active", ex);
        }
        return admitPatientReply.getIntegrationPatient();
    }

	      *//**
	      * Method to invoke CIA's pausePatient operation.
	      * @param messagePatient
	      * @return paused patient
	      *//*
    private IntegrationPatientType pausePatient(IntegrationPatientType messagePatient) throws ProcessException,
            PatientNotActiveFault
    {
        PausePatientReply pausePatientReply = new PausePatientReply();

        try
        {
            logger.debug("Invoking CIA's pause patient operation");
            PausePatient pausePatient = new PausePatient();
            pausePatient.setIntegrationPatient(messagePatient);
            pausePatientReply = ciaService.pausePatient(pausePatient);
        }
        catch (RemoteServiceFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
            throw new ProcessException("Remote Service Error: " + Error.getError().
                    getFaultInternalMessage());
        }
        catch (NoSuchPatientFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_FOUND.toString()));
            throw new ProcessException("Patient with external ids is not found in the system: " + Error.getError().
                    getFaultInternalMessage());
        }
        catch (ResourceNotFoundFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.RESOURCE_NOT_FOUND.toString()));
            throw new ProcessException("Required resource to admit/update patient is not found" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (InsufficientDataFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
            throw new ProcessException("Data provided to admit/update patient is insufficient" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (OptimisticLockingFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.OPTIMISTIC_LOCKING.toString()));
            throw new ProcessException("Another thread is trying to operate on the same DB record" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (UserNotAuthorizedFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
            throw new ProcessException("User is not authorized to admit/update the patient" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (InvalidDataFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
            throw new ProcessException("The data provided to pause a patient is invalid" + Error.getError().
                    getFaultInternalMessage());
        }
        return pausePatientReply.getIntegrationPatient();
    }

	       *//**
	       * Method to invoke CIA's updatePatientDemographics operation.
	       * @param messagePatient
	       * @return UpdatePatientDemographicsReply object
	       * @throws Exception
	       *//*
    private IntegrationPatientType updatePatientDemographics(List<IntegrationPatientType> existingPatientsList) throws
            ProcessException, DuplicatePatientFault, InvalidDataFault
    {
        UpdatePatientDemographicsReply updatePatientDemographicsReply = new UpdatePatientDemographicsReply();
        UpdatePatientDemographics updatePatientDemographics = new UpdatePatientDemographics();
        updatePatientDemographics.getIntegrationPatient().addAll(existingPatientsList);

        try
        {
            logger.debug("Invoking CIA's updatePatientDemographics operation");

            updatePatientDemographicsReply = ciaService.updatePatientDemographics(updatePatientDemographics);
        }
        catch (RemoteServiceFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
            throw new ProcessException("Remote Service Error: " + Error.getError().
                    getFaultInternalMessage());
        }
        catch (NoSuchPatientFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_FOUND.toString()));
            throw new ProcessException("Patient with external ids is not found in the system: " + Error.getError().
                    getFaultInternalMessage());
        }
        catch (DuplicatePatientFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.DUPLICATE_SSN.toString()));
            throw new ProcessException("Patient with same SSN already exists " + Error.getError().getFaultInternalMessage());
        }
        catch (ResourceNotFoundFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.RESOURCE_NOT_FOUND.toString()));
            throw new ProcessException("Required resource to admit/update patient is not found" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (InsufficientDataFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
            throw new ProcessException("Data provided to admit/update patient is insufficient" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (OptimisticLockingFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.OPTIMISTIC_LOCKING.toString()));
            throw new ProcessException("Another thread is trying to operate on the same DB record" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (UserNotAuthorizedFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
            throw new ProcessException("User is not authorized to admit/update the patient" + Error.getError().
                    getFaultInternalMessage());
        }

        return updatePatientDemographicsReply.getIntegrationPatient();
    }

	        *//**
	        * Method to invoke CIA's findResults operation.
	        * @param list of resultIds to query
	        * @return list of results
	        * @throws ProcessException
	        *//*
    private List<SurveyResponse> findSessionResults(List<ResultId> resultsToQuery) throws ProcessException
    {
        List<SurveyResponse> surveyResponse = new ArrayList<SurveyResponse>();

        FindResultsReply findResultsReply = new FindResultsReply();
        FindResults findResults = new FindResults();

        IdFilter idFilter = new IdFilter();
        idFilter.getId().addAll(resultsToQuery);
        findResults.setFilter(idFilter);

        try
        {
            logger.debug("Invoking CIA's findResults operation");
            findResultsReply = ciaService.findResults(findResults);
            Long totalChunks = findResultsReply.getChunkInfo().getTotalChunks();
            for (int i = 0; i < totalChunks; i++)
            {
                if (i > 0)
                {
                    QueryInfo queryInfo = new QueryInfo();
                    queryInfo.setLastSeenItemId(findResultsReply.getChunkInfo().getLastSeenItemId());
                    findResults.setQueryInfo(queryInfo);
                    findResultsReply = ciaService.findResults(findResults);
                }
                surveyResponse.addAll(findResultsReply.getSurveyResponse());
            }

        }
        catch (RemoteAccessException re)
        {
            logger.error(" RemoteAccessException : ", re);
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
            throw new ProcessException(
                    "User Authentication Failed : " + Error.getError().getFaultInternalMessage());
        }
        catch (UserNotAuthorizedFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
            throw new ProcessException("User is not authorized to query results in the system" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (InvalidFilterFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
            throw new ProcessException("The filter to find results provided is invalid" + Error.getError().
                    getFaultInternalMessage());
        }
        catch (InvalidNextChunkFault ex)
        {
            logger.error(ex.getMessage());
            Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
            throw new ProcessException("Invalid next chunk error" + Error.getError().getFaultInternalMessage());
        }
        return surveyResponse;
    }

	         *//**
	         * Method to invoke CIA's findResults operation.
	         * @param list of resultIds to query
	         * @return list of results
	         * @throws ProcessException
	         *//*
    private List<MeterMeasurement> findAdhocResults(List<ResultId> resultsToQuery) throws ProcessException
    {
        List<MeterMeasurement> meterMeasurements = new ArrayList<MeterMeasurement>();
        if (null != resultsToQuery)
        {
            FindResultsReply findResultsReply = new FindResultsReply();
            FindResults findResults = new FindResults();
            IdFilter idFilter = new IdFilter();
            idFilter.getId().addAll(resultsToQuery);
            findResults.setFilter(idFilter);
            try
            {
                logger.debug("Invoking CIA's findResults operation");
                findResultsReply = ciaService.findResults(findResults);
                Long totalChunks = findResultsReply.getChunkInfo().getTotalChunks();
                for (int i = 0; i < totalChunks; i++)
                {
                    if (i > 0)
                    {
                        QueryInfo queryInfo = new QueryInfo();
                        queryInfo.setLastSeenItemId(findResultsReply.getChunkInfo().getLastSeenItemId());
                        findResults.setQueryInfo(queryInfo);
                        findResultsReply = ciaService.findResults(findResults);
                    }
                    List<MeterMeasurement> replyMeterMeasurements = findResultsReply.getMeterMeasurement();
                    for (MeterMeasurement meter : replyMeterMeasurements)
                    {
                        meterMeasurements.add(meter);
                    }
                }
            }
            catch (RemoteAccessException re)
            {
                logger.error(" RemoteAccessException : ", re);
                Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.REMOTE_SERVICE_FAILED.toString()));
                throw new ProcessException(
                        "User Authentication Failed : " + Error.getError().getFaultInternalMessage());
            }
            catch (UserNotAuthorizedFault ex)
            {
                logger.error(ex.getMessage());
                Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.USER_NOT_AUTHORIZED.toString()));
                throw new ProcessException("User is not authorized to query results in the system" + Error.getError().
                        getFaultInternalMessage());
            }
            catch (InvalidFilterFault ex)
            {
                logger.error(ex.getMessage());
                Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
                throw new ProcessException("The filter to find results provided is invalid" + Error.getError().
                        getFaultInternalMessage());
            }
            catch (InvalidNextChunkFault ex)
            {
                logger.error(ex.getMessage());
                Error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.INSUFFICIENT_DATA.toString()));
                throw new ProcessException("Invalid next chunk error" + Error.getError().getFaultInternalMessage());
            }
        }
        return meterMeasurements;
    }

    private void doMarshal(Object obj)
    {
        logger.info("Marshalling the request...");
        JAXBContext jc = null;
        try
        {
            jc = JAXBContext.newInstance("com.bosch.th.cia");
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            m.marshal(obj, stringWriter);
            logger.info(stringWriter.toString());
        }
        catch (JAXBException ex)
        {
            logger.error("Caught Exception while marshalling the request!!", ex);
        }
    }*/

	private long getEnrollDate(IntegrationPatientType existingPatient){
		List<Enrollment> enrollmentList =  existingPatient.getEnrollmentHistoryList();
		long enrollDateWithoutTime =-1;
		if(enrollmentList!=null && !enrollmentList.isEmpty()){
			//get the first enrolled history
			Enrollment enrollment = enrollmentList.get(0);
			if(enrollment!=null && enrollment.getAuditInfo()!= null
					&& enrollment.getAuditInfo().getTimestamp()!=null){
				long enrollDate = enrollment.getAuditInfo().getTimestamp().toGregorianCalendar().getTimeInMillis();
				long getRidOfTime = 1000 * 60 * 60 * 24;
				enrollDateWithoutTime = enrollDate / getRidOfTime;
			}
		}
		return enrollDateWithoutTime;

	}

	/**
	 * Check if the existing patient is integrated or not . If not then throw "PATIENT_NOT_INTEGRATED" exception.
	 * @param identifiersList
	 * @param patientDetails
	 * @throws ProcessException
	 */
	private void forbidNonIntegratedPatientRequest(List<ExternalIdentifierType> identifiersList, List<IntegrationPatientType> patientDetails, Error error ) throws ProcessException
	{
		try {
			boolean isPatientIntegrated = false;
			if(patientDetails!=null && !patientDetails.isEmpty()){
				if(identifiersList!=null && !identifiersList.isEmpty()){
					for(IntegrationPatientType existingPatient : patientDetails){
						String patId = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.toString());
						if (MessageUtils.getExistingPatIdentifier(existingPatient,patId).equalsIgnoreCase(MessageUtils.getMessagePatIdentifier(identifiersList,patId))){
							isPatientIntegrated = existingPatient.isIsIntegration();
						}
					}
				}
			}

			if(!isPatientIntegrated){
				logger.error("Existing patient is not Integrated...");
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.PATIENT_NOT_INTEGRATED.toString()));
				throw new ProcessException("SA Process error: " + error.getError().getFaultInternalMessage());
			}
		}
		catch (Exception e) {
			throw new ProcessException("Exception occured while processing 'forbidNonIntegratedPatientRequest' request...");
		}
	}
}
