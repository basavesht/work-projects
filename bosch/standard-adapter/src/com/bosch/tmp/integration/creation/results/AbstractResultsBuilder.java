package com.bosch.tmp.integration.creation.results;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.hl7.v2xml.BHS10CONTENT;
import org.hl7.v2xml.BHSCONTENT;
import org.hl7.v2xml.BTSCONTENT;
import org.hl7.v2xml.MSHCONTENT;
import org.hl7.v2xml.NTECONTENT;
import org.hl7.v2xml.OBRCONTENT;
import org.hl7.v2xml.OBXCONTENT;
import org.hl7.v2xml.ORCCONTENT;
import org.hl7.v2xml.PIDCONTENT;
import org.hl7.v2xml.QVRQ17CONTENT;
import org.hl7.v2xml.RCPCONTENT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosch.th.cia.Note;
import com.bosch.th.integrationpatient.AddressType;
import com.bosch.th.integrationpatient.IntegrationPatientType;
import com.bosch.th.integrationpatient.PhoneNumberType;
import com.bosch.th.patientrecord.AssessedVitalSign;
import com.bosch.th.patientrecord.Device;
import com.bosch.th.patientrecord.DeviceReportedMode;
import com.bosch.th.patientrecord.MeterMeasurement;
import com.bosch.th.patientrecord.Mode;
import com.bosch.th.patientrecord.QuestionResponse;
import com.bosch.th.patientrecord.SelfReportedMode;
import com.bosch.th.patientrecord.SurveyResponse;
import com.bosch.th.patientrecord.VitalSignValue;
import com.bosch.tmp.cl.basictypes.ExternalIdentifierType;
import com.bosch.tmp.integration.businessObjects.results.ClinicalObservation;
import com.bosch.tmp.integration.businessObjects.results.ClinicalObservationOrder;
import com.bosch.tmp.integration.businessObjects.results.ClinicalPatientNotes;
import com.bosch.tmp.integration.businessObjects.results.ClinicalSurveyNotes;
import com.bosch.tmp.integration.businessObjects.results.PatientResultsContent;
import com.bosch.tmp.integration.creation.Builder;
import com.bosch.tmp.integration.creation.CreationException;
import com.bosch.tmp.integration.persistence.ControlNumber;
import com.bosch.tmp.integration.util.ConfigKeyEnum;
import com.bosch.tmp.integration.util.MessageUtils;
import com.bosch.tmp.integration.util.ORUBatchResponseMessageType;
import com.bosch.tmp.integration.util.PatientResultsUtility;
import com.bosch.tmp.integration.util.ResultResponseTypeEnum;
import com.bosch.tmp.integration.util.SegmentUtils;
import com.bosch.tmp.integration.util.Utils;
import com.bosch.tmp.integration.validation.ConfigValue;
import com.bosch.tmp.integration.validation.ConfigurationLoader;

/**
 * Abstract Patient Results Builder ...
 * @author BEA2KOR
 *
 */
public abstract class AbstractResultsBuilder implements Builder
{
	public static final Logger logger = LoggerFactory.getLogger(AbstractResultsBuilder.class);

	/**
	 * Instance variables to store ..
	 *  - Request message (QVR_Q17).
	 *  - Push Results list.
	 *  - Number of ORU/Batch, OBR/Order, OBX/ObservationRequest.
	 */
	protected Integer allowedNumberOfORUPerBatch = 0;
	protected Integer allowedNumberOfOrderPerORU = 0;
	protected Integer allowedNumberOfObservationPerOrder = 0;
	protected Integer allowedNumberOfPatientNotesPerORU = 0;
	protected Integer allowedNumberOfSessionNotesPerOrder = 0;
	protected Object requestMsg = null;
	protected ORUBatchResponseMessageType batchResponseType = null;
	private String batchControlNumber = generateControlNumber();
	protected String resultResponseType = null;

	@Override
	public abstract Object buildMessage(Object... data) throws CreationException ;

	/**
	 * Get the RCP value from the input request. If this value is less than the configured results count per batch and "maxResultsPerBatch"
	 * then set this value as the "resultsPerBatch_limit".
	 * If configured number of OBR's per ORU_R30/R01 exceeds the max limit defined "maxNumberOfOBRs",
	 * then set this value as "numberOfOBRs_limit".
	 * If configured number of OBX's per OBR exceeds the max limit defined "maxNumberOfOBXs",
	 * then set this value as "numberOfOBXs_limit".
	 */
	protected void setClinicalResponseLimit()
	{
		//Set the number of ORU's/OBR's/OBX's to be considered based on the input request and max limit set...
		if(requestMsg!=null && requestMsg instanceof QVRQ17CONTENT) {
			RCPCONTENT requestMsgRCP = (RCPCONTENT)((QVRQ17CONTENT)requestMsg).getRCP();
			allowedNumberOfORUPerBatch = PatientResultsUtility.getNumberOfORUPerBatch(requestMsgRCP);
		}
		allowedNumberOfOrderPerORU = PatientResultsUtility.NumberOfOrderPerORU();
		allowedNumberOfObservationPerOrder = PatientResultsUtility.geNumberOfObservationPerOrder();
		allowedNumberOfPatientNotesPerORU = PatientResultsUtility.getNumberOfNTEPerORU();
		allowedNumberOfSessionNotesPerOrder = PatientResultsUtility.getNumberOfNTEPerOrder();

		logger.debug("Allowed number of ORU per ORU BATCH calcualted : " + allowedNumberOfORUPerBatch);
		logger.debug("Allowed number of ORDER per ORU calcualted : " + allowedNumberOfOrderPerORU);
		logger.debug("Allowed number of Observation per ORDER calcualted : " + allowedNumberOfObservationPerOrder);
		logger.debug("Allowed number of Patient Notes per ORU calcualted : " + allowedNumberOfPatientNotesPerORU);
		logger.debug("Allowed number of Session Notes per ORDER calcualted : " + allowedNumberOfSessionNotesPerOrder);
	}


	/**
	 * Construct BHS Content for the complete ORU_R30/R01BATCH...
	 * @param isResultsLastRequestBatch
	 * @return
	 */
	protected BHSCONTENT buildBatchHeader(boolean isResultsLastRequestBatch)
	{
		MSHCONTENT incomingRequestMSH = null;
		BHSCONTENT batchHeaderObj = null;
		String referenceMessageControlNumber = null;
		String receivingFacilityCode = null;

		if(requestMsg!=null && requestMsg instanceof QVRQ17CONTENT)
		{
			incomingRequestMSH = ((QVRQ17CONTENT)requestMsg).getMSH();

			// Check for empty request...
			if (null == incomingRequestMSH){
				return null;
			}

			//Receiving facility code..
			if (null != incomingRequestMSH.getMSH4()){
				receivingFacilityCode = incomingRequestMSH.getMSH4().getHD1().getValue();
			}

			//Reference message control number from the incoming request MSH..
			if (null != incomingRequestMSH.getMSH10() && null != incomingRequestMSH.getMSH4().getHD1()){
				referenceMessageControlNumber = incomingRequestMSH.getMSH10().getValue();
			}
		}

		//Batch Header message construction...
		logger.debug("ORU Batch response type : " + batchResponseType.getBatchMessageType());
		batchHeaderObj = SegmentUtils.buildBHS(receivingFacilityCode,batchResponseType.getBatchMessageType(),batchControlNumber,referenceMessageControlNumber);

		//Check :If the allowed capacity is not reached and ORU Batch response construction is complete, then update BHS will appropriate message.....
		if(isResultsLastRequestBatch) {
			BHS10CONTENT bhsUpdateMessage = new BHS10CONTENT();
			bhsUpdateMessage.setValue(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.RESULTS_LAST_BATCH_MESSAGE.toString()));
			batchHeaderObj.setBHS10(bhsUpdateMessage);
		}
		return batchHeaderObj;
	}

	/**
	 * Construct MSH Content for each ORU_R30/R01. It contains the message header..
	 * @param messageControlNumber
	 * @return
	 * @throws ResultsCreationException
	 */
	protected MSHCONTENT buildMessageHeader(Integer messageControlNumber) throws ResultsCreationException
	{
		MSHCONTENT incomingRequestMSH = null;
		try
		{
			if(requestMsg!=null && requestMsg instanceof QVRQ17CONTENT) {
				incomingRequestMSH = ((QVRQ17CONTENT)requestMsg).getMSH();
			}

			// Check for empty request...
			if (null == incomingRequestMSH){
				return null;
			}
			incomingRequestMSH = SegmentUtils.buildMSH(incomingRequestMSH, batchResponseType.getBatchMessageType(),(batchControlNumber + "_" + messageControlNumber));
		}
		catch (Exception e) {
			logger.error("Exception encountered while building Message header for ORU Batch");
			throw new ResultsCreationException("Exception encountered while building Message header for ORU Batch",e);
		}
		return incomingRequestMSH;
	}

	/**
	 * Construct PID Content for each ORU_R30/R01. It contains the patient information
	 * as obtained from the patient service ..
	 * @param patientDetails
	 * @return
	 * @throws ResultsCreationException
	 */
	protected PIDCONTENT buildPatientIndentification(IntegrationPatientType patientDetails) throws ResultsCreationException
	{
		PIDCONTENT patientIdentificationObj = null;
		try
		{
			if(patientDetails!=null)
			{
				//Patient Identifiers...
				List<ExternalIdentifierType> patientIdentifiers = patientDetails.getIdentifier().getExternalIdentifier();
				String patientIdentifier = MessageUtils.getMessagePatIdentifier(patientIdentifiers,ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.toString()));
				String socialSecurityNumber = MessageUtils.getMessagePatIdentifier(patientIdentifiers,ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.SSN.toString()));

				//Patient Information...
				String lastName = patientDetails.getLastName();
				String firstName = patientDetails.getFirstName();
				String middleInitial = patientDetails.getMiddleInitial();
				String dob = Utils.convertXMLGregorianCalendarToString(patientDetails.getBirthDate(), ConfigurationLoader.getFieldConfigurations(null, null, "PID7").toString());
				String gender = patientDetails.getGender() != null ? patientDetails.getGender().toString() : null;

				//Patient Address Details
				String address1 = null;
				String address2 = null;
				String zipCode = null;
				String city = null;
				String state = null;
				String country = null;
				try
				{
					AddressType address = !patientDetails.getAddress().isEmpty() ? patientDetails.getAddress().get(0) : new AddressType();
					List<String> streets = address.getStreet();
					if (streets.size() == 1) {
						address1 = address.getStreet().get(0);
					}
					else if (streets.size() > 1) {
						address1 = address.getStreet().get(0);
						address2 = address.getStreet().get(1);
					}
					zipCode = address.getZipCode();
					city = address.getCity();
					state = address.getState();
					country = address.getCountry();
				}
				catch (Exception e) {
					logger.debug("Error during address feild population");
				}

				//Patient Contact Details..
				String phoneNumber = null;
				try {
					PhoneNumberType phone = !patientDetails.getPhoneNumber().isEmpty() ? patientDetails.getPhoneNumber().get(0) : new PhoneNumberType();
					phoneNumber = phone.getNumber();
				}
				catch (Exception e){
					logger.debug("Error during phone number feild population");
				}
				patientIdentificationObj = SegmentUtils.buildPID(patientIdentifier, socialSecurityNumber, lastName, firstName, middleInitial, dob, gender, address1, address2, city, state, zipCode, country, phoneNumber);
			}
		}
		catch (Exception e) {
			logger.error("Exception encountered while building Patient Identification content for ORU Batch");
			throw new ResultsCreationException("Exception encountered while building Patient Identification content for ORU Batch",e);
		}
		return patientIdentificationObj;
	}

	/**
	 * Construct ORC (Common Order) Content for ORU_R30/R01.
	 * @return
	 * @throws ResultsCreationException
	 */
	protected ORCCONTENT buildCommonOrder() throws ResultsCreationException
	{
		ORCCONTENT commonOrder = null;
		try
		{
			// Generate order control number. This is same for all the results belonging to a patient.
			String orderControlNumber = generateControlNumber();

			//Order sending facility...
			String sendingFacility = null;
			ConfigValue obj = ConfigurationLoader.getFieldById("ORC2_EI2").getConfigValue();
			if (obj != null){
				sendingFacility = obj.getValue();
			}

			commonOrder = SegmentUtils.buildORC(orderControlNumber, sendingFacility);
		}
		catch (Exception e) {
			logger.error("Exception encountered while building Common Order (ORCContent) for ORU Order");
			throw new ResultsCreationException("Exception encountered while building Common Order (ORCContent) for ORU Order",e);
		}
		return commonOrder;
	}

	/**
	 * Construct OBR (Observation Request) Content for ORU_R30/R01.
	 * The OBR segment is used in all ORU messages as a report header, and contains important information
	 * about the order being fulfilled (i.e. order number, request date/time, observation date/time,
	 * ordering provider, etc).
	 * @param responseDate
	 * @param receivedDate
	 * @return
	 * @throws ResultsCreationException
	 */
	protected OBRCONTENT buildObservationRequest(XMLGregorianCalendar responseDate, XMLGregorianCalendar receivedDate) throws ResultsCreationException
	{
		OBRCONTENT observationRequest = null;
		try
		{
			observationRequest = SegmentUtils.buildOBR(responseDate, receivedDate);
		}
		catch (Exception e) {
			logger.error("Exception encountered while building Observation Request (OBRContent) for ORU Order");
			throw new ResultsCreationException("Exception encountered while building Observation Request (OBRContent) for ORU Order",e);
		}
		return observationRequest;
	}

	/**
	 * Construct OBX (Clinical Observation - Vitals) Content for ORU_R30/R01.
	 * The OBX segment transmits the actual clinical observation results as a single observation or observation fragment.
	 * For Subjective results, complete session information including vital will be embedded within a single OBX segment.
	 * For Objective results, individual OBX will be created for each session or adhoc vitals.
	 * @param observation
	 * @return
	 * @throws ResultsCreationException
	 */
	protected OBXCONTENT buildClinicalObservation(ClinicalObservation observation) throws ResultsCreationException
	{
		// Set id is constant for all the results.
		int setId = 1;
		OBXCONTENT clinicalObservationContent = null;
		try
		{
			if(observation!=null && observation.getClinicalSessionRawXML()!=null)
			{
				//Session Unique Id...
				String observationId = observation.getObservationId();

				//Construct OBX...
				clinicalObservationContent = SegmentUtils.buildSubjectiveOBX(observation.getClinicalSessionRawXML(),observationId);
			}
			else
			{
				//Vital Value unique Id...
				String observationId = observation.getObservationId();

				//Device Details..
				Device device = observation.getDevice();

				//Vitals information (like loinc code, vital reading, unit of measure, collection time)..
				VitalSignValue vitalSignValue = observation.getVitalSignValue();
				String loincCode = vitalSignValue.getLoincCode();
				BigDecimal vitalReading = vitalSignValue.getValue();
				String unitOfMeasure = vitalSignValue.getUnit().value();
				XMLGregorianCalendar collectionTime = vitalSignValue.getCollectionTime();

				//Vital Collection mode
				Mode collectionMode = observation.getCollectionMode();
				String vitalCollectionMode = collectionMode instanceof DeviceReportedMode ? "device" : collectionMode instanceof SelfReportedMode ? "self" : "careManager";

				//Construct OBX...
				clinicalObservationContent = SegmentUtils.buildOBX(setId, loincCode, vitalReading, unitOfMeasure, collectionTime,vitalCollectionMode, device, observationId);
			}
		}
		catch (Exception ex) {
			logger.error("Exception encountered while building Clinical Observation (OBXContent) for ORU Order");
			throw new ResultsCreationException("Exception encountered while building Clinical Observation (OBXContent) for ORU Order",ex);
		}
		return clinicalObservationContent;
	}

	/**
	 * Build BTS Content...
	 * @param numOfMessages
	 * @return
	 */
	protected BTSCONTENT buildBTS(int numOfMessages){
		return SegmentUtils.buildBTS(numOfMessages);
	}

	/**
	 * Constructs list of OBXContent for each push results input.
	 * @param clinicalResult
	 * @return
	 * @throws ResultsCreationException
	 */
	protected List<ClinicalObservationOrder> buildClinicalObservationOrderList(PatientResultsContent clinicalResult) throws ResultsCreationException {
		Map<Long,ClinicalObservationOrder> clinicalObservationOrderList = new HashMap<Long,ClinicalObservationOrder>();
		buildClinicalSessionObservationList(clinicalResult, clinicalObservationOrderList);
		buildClinicalAdhocObservationList(clinicalResult, clinicalObservationOrderList);
		buildClinicalSessionNotesList(clinicalResult, clinicalObservationOrderList);
		return new ArrayList<ClinicalObservationOrder>(clinicalObservationOrderList.values());
	}

	/**
	 * Build the Clinical Session Notes List for each session.
	 * @param clinicalResult
	 * @param clinicalObservationOrderList
	 * @throws ResultsCreationException
	 */
	private void buildClinicalSessionNotesList(PatientResultsContent clinicalResult,Map<Long,ClinicalObservationOrder> clinicalObservationOrderList) throws ResultsCreationException
	{
		try
		{
			Map<Long, ClinicalSurveyNotes> surveyNotes = clinicalResult.getSurveyNotes();
			if(surveyNotes!=null)
			{
				if(resultResponseType!=null)
				{
					if(resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.NOTES.responseType())
							|| resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.OBJECTIVE_NOTES.responseType())
							|| resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE_NOTES.responseType()))
					{
						//Iterate over all the survey containing notes..
						for (Map.Entry<Long, ClinicalSurveyNotes> survey : surveyNotes.entrySet())
						{
							Long surveyId = survey.getKey(); //Session Id..
							ClinicalSurveyNotes notes = survey.getValue(); //Corresponding survey notes..

							//For Subjective results, each session will contain a single OBR and NTE ...
							for (Note note : notes.getNotes())
							{
								//New NTEContent for each note...
								NTECONTENT surveyNote = buildSessionNotes(note);

								//Adding OBR and all the linked NTE's...
								if(clinicalObservationOrderList!=null)
								{
									if(clinicalObservationOrderList.containsKey(surveyId)
											&& clinicalObservationOrderList.get(surveyId).getSessionId()!=null){
										clinicalObservationOrderList.get(surveyId).getClinicalNotes().add(surveyNote); //NTE
									}
									else {
										//New OBRContent for a new survey not currently added to the map...
										OBRCONTENT observationRequest = buildObservationRequest(notes.getSurveyResponseDate(), notes.getSurveyRecievedDate());
										//Create a new Clinical Observation Order...
										ClinicalObservationOrder clinicalOrder = new ClinicalObservationOrder();
										clinicalOrder.setObservationRequest(observationRequest); //OBR
										clinicalOrder.getClinicalNotes().add(surveyNote); //NTE
										clinicalOrder.setSessionId(surveyId.toString());
										clinicalObservationOrderList.put(surveyId,clinicalOrder);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Exception encountered while building Clinical Session Notes (NTEContent) for ORU Order");
			throw new ResultsCreationException("Exception encountered while building Clinical Session Notes (NTEContent) for ORU Order",ex);
		}
	}

	/**
	 * Constructs list of OBXContent for each push results survey response.
	 * @param clinicalResult
	 * @param clinicalObservationOrderList
	 * @throws ResultsCreationException
	 */
	protected void buildClinicalSessionObservationList(PatientResultsContent clinicalResult, Map<Long,ClinicalObservationOrder> clinicalObservationOrderList) throws ResultsCreationException
	{
		try
		{
			List<OBXCONTENT> clinicalObservations = new ArrayList<OBXCONTENT>();
			List<SurveyResponse> sessions = clinicalResult.getSurveyResponse();
			if(sessions!=null && !sessions.isEmpty())
			{
				if(resultResponseType!=null)
				{
					if(resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE.responseType()) || resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE_NOTES.responseType()))
					{
						//For Subjective results, each session will contain a single OBR and OBX ...
						for (SurveyResponse session : sessions)
						{
							//New OBRContent for each session...
							OBRCONTENT observationRequest = buildObservationRequest(session.getResponseDate(), session.getReceivedDate());

							// New OBX is created per vital sign measurement.
							ClinicalObservation observation = new ClinicalObservation();
							observation.setObservationId(session.getId().toString());
							observation.setClinicalSessionRawXML(buildSubjectiveClinicalSessionContent(session));

							OBXCONTENT clinicalObservation = buildClinicalObservation(observation);
							clinicalObservations.add(clinicalObservation);

							//Adding OBR and all the linked OBX's
							ClinicalObservationOrder clinicalOrder = new ClinicalObservationOrder();
							clinicalOrder.setObservationRequest(observationRequest); //OBR
							clinicalOrder.getClinicalObservations().addAll(clinicalObservations); //OBX
							clinicalOrder.setSessionId(session.getId().toString());
							clinicalObservationOrderList.put(session.getId(),clinicalOrder);

							//Reset the clinical observations list for a new session..
							clinicalObservations.clear();
						}
					}
					else if(resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.OBJECTIVE.responseType()) || resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.OBJECTIVE_NOTES.responseType()))
					{
						//For Objective results, each vital within a session will contain a single OBR and OBX ...
						for (SurveyResponse session : sessions)
						{
							ClinicalObservationOrder clinicalOrder = new ClinicalObservationOrder();
							OBRCONTENT observationRequest = buildObservationRequest(session.getResponseDate(), session.getReceivedDate());
							for (QuestionResponse response : session.getQuestionResponse())
							{
								for (MeterMeasurement measurement : response.getMeterMeasurement())
								{
									for (AssessedVitalSign assesVitalSign : measurement.getVitalSign())
									{
										if (assesVitalSign != null && assesVitalSign.getId() != null)
										{
											for (VitalSignValue vitalSignValue : assesVitalSign.getNumericValue())
											{
												// New OBX is created per vital sign measurement.
												ClinicalObservation observation = new ClinicalObservation();
												observation.setObservationId(vitalSignValue.getId());
												observation.setCollectionMode(measurement.getMode());
												observation.setDevice(measurement.getCollectionType().getInterface());
												observation.setVitalSignValue(vitalSignValue);

												OBXCONTENT clinicalObservation = buildClinicalObservation(observation);
												clinicalObservations.add(clinicalObservation);
											}
										}
									}
								}
							}

							//Adding OBR and all the linked OBX's
							clinicalOrder.getClinicalObservations().addAll(clinicalObservations); //OBX
							clinicalOrder.setObservationRequest(observationRequest); //OBR
							clinicalOrder.setSessionId(session.getId().toString());
							clinicalObservationOrderList.put(session.getId(),clinicalOrder);

							//Reset the clinical observations list for a new session..
							clinicalObservations.clear();
						}
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("Exception encountered while building Clinical Observation (OBXCONTENT) for Sessions (Objective/Subjective) for ORU Order");
			throw new ResultsCreationException("Exception encountered while building Clinical Observation (OBXCONTENT) for Sessions (Objective/Subjective) for ORU Order",e);
		}
	}

	/**
	 * Constructs list of OBXContent for each push results ad-hoc result.
	 * @param clinicalResult
	 * @param clinicalObservationOrderList
	 * @throws ResultsCreationException
	 */
	protected void buildClinicalAdhocObservationList(PatientResultsContent clinicalResult, Map<Long,ClinicalObservationOrder> clinicalObservationOrderList) throws ResultsCreationException
	{
		try
		{
			List<OBXCONTENT> clinicalObservations = new ArrayList<OBXCONTENT>();
			List<MeterMeasurement> meterMeasurementList = clinicalResult.getMeterMeasurement();
			if(meterMeasurementList!=null && !meterMeasurementList.isEmpty())
			{
				for (MeterMeasurement measurement : meterMeasurementList)
				{
					//Construct OBRCONTENT based on the meter received date...
					OBRCONTENT observationRequest = null;
					observationRequest = buildObservationRequest(measurement.getResponseDate(), measurement.getReceivedDate());

					//Construct OBXCONTENT..
					ClinicalObservationOrder clinicalOrder = new ClinicalObservationOrder();
					for (AssessedVitalSign assesVitalSign : measurement.getVitalSign())
					{
						if (assesVitalSign != null && assesVitalSign.getId() != null)
						{
							for (VitalSignValue vitalSignValue : assesVitalSign.getNumericValue())
							{
								//New OBX is created per vital sign measurement.
								ClinicalObservation observation = new ClinicalObservation();
								observation.setObservationId(vitalSignValue.getId());
								observation.setCollectionMode(measurement.getMode());
								observation.setDevice(measurement.getCollectionType().getInterface());
								observation.setVitalSignValue(vitalSignValue);

								OBXCONTENT clinicalObservation = buildClinicalObservation(observation);
								clinicalObservations.add(clinicalObservation);
							}
						}
					}

					//Adding OBR and all the linked OBX's
					clinicalOrder.getClinicalObservations().addAll(clinicalObservations); //OBX
					clinicalOrder.setObservationRequest(observationRequest);
					clinicalObservationOrderList.put(measurement.getId(),clinicalOrder);

					//Reset the clinical observations list for a new measurement..
					clinicalObservations.clear();
				}
			}
		} catch (Exception e) {
			logger.error("Exception encountered while building Clinical Observation (OBXCONTENT) for Adhocs(Objective/Subjective) for ORU Order");
			throw new ResultsCreationException("Exception encountered while building Clinical Observation (OBXCONTENT) for Adhocs(Objective/Subjective) for ORU Order",e);
		}
	}

	/**
	 * Construct OBX.5 for subjective sessions.
	 * @param session
	 * @return
	 * @throws ResultsCreationException
	 */
	private String buildSubjectiveClinicalSessionContent(SurveyResponse session) throws ResultsCreationException
	{
		String subjectiveRawXMLString = null;
		try {
			SubjectiveResultBuilder subjectiveSessionBuilderObj = new SubjectiveResultBuilder();
			subjectiveRawXMLString = subjectiveSessionBuilderObj.buildSubjectiveResults(session);
		}
		catch (Exception e) {
			logger.error("Exception encountered while building Subjective Clinical Observation (OBXCONTENT) for ORU Order");
			throw new ResultsCreationException("Exception encountered while building Subjective Clinical Observation (OBXCONTENT) for ORU Order",e);
		}
		return subjectiveRawXMLString;
	}

	/**
	 * Construct NTE (Patient Notes) Content for ORU_R30/R01.
	 * @param clinicalPatientNotes
	 * @return
	 * @throws ResultsCreationException
	 */
	protected List<NTECONTENT> buildPatientNotes(ClinicalPatientNotes clinicalPatientNotes) throws ResultsCreationException
	{
		List<NTECONTENT> patientNotesContent = new ArrayList<NTECONTENT>();
		try
		{
			if(clinicalPatientNotes!=null)
			{
				if(resultResponseType!=null)
				{
					if(resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.NOTES.responseType())
							|| resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.OBJECTIVE_NOTES.responseType())
							|| resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE_NOTES.responseType()))
					{
						for (Note note : clinicalPatientNotes.getNotes()) {
							patientNotesContent.add(SegmentUtils.buildNTE(note));
						}
					}
				}
			}
		}
		catch (Exception ex) {
			logger.error("Exception encountered while building Clinical Patient Notes (NTEContent) for ORU");
			throw new ResultsCreationException("Exception encountered while building Clinical Patient Notes (NTEContent) for ORU",ex);
		}
		return patientNotesContent;
	}

	/**
	 * Construct NTE (Session Notes) Content for a particular session note in ORU ORDER.
	 * @param surveyNote
	 * @return
	 * @throws ResultsCreationException
	 */
	private NTECONTENT buildSessionNotes(Note surveyNote)  throws ResultsCreationException {
		return SegmentUtils.buildNTE(surveyNote);
	}

	/**
	 * Generate batch control number.
	 * @return
	 */
	private String generateControlNumber()
	{
		// Generate control number.
		String controlNumber = null;
		try {
			ControlNumber controlNumberObj = new ControlNumber();
			controlNumberObj.persist();
			controlNumber = controlNumberObj.getId().toString();
		}
		catch (Exception ex)
		{
			logger.error("Cannot persist control number, setting it to empty string.");
			controlNumber = "";
		}
		logger.debug("Control number generated is " + batchControlNumber);
		return controlNumber;
	}
}
