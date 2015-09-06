package com.bosch.tmp.integration.process.results;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.v2xml.QVRQ17CONTENT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bosch.th.cia.FindNotesReply;
import com.bosch.th.cia.FindResultsReply;
import com.bosch.th.cia.Note;
import com.bosch.th.externalorganization.OrganizationType;
import com.bosch.th.integrationpatient.IntegrationPatientType;
import com.bosch.th.patientrecord.AssessedVitalSign;
import com.bosch.th.patientrecord.MeterMeasurement;
import com.bosch.th.patientrecord.QuestionResponse;
import com.bosch.th.patientrecord.SurveyResponse;
import com.bosch.th.patientrecord.VitalSignValue;
import com.bosch.tmp.cl.results.ResultId;
import com.bosch.tmp.cl.results.ResultsType;
import com.bosch.tmp.cl.task.EqualsFilterType;
import com.bosch.tmp.cl.task.HeaderType;
import com.bosch.tmp.cl.task.NotesPayloadType;
import com.bosch.tmp.cl.task.PatientNotesPayloadType;
import com.bosch.tmp.cl.task.SearchableFieldNameType;
import com.bosch.tmp.cl.task.SurveyNotesPayloadType;
import com.bosch.tmp.integration.businessObjects.results.ClinicalSurveyNotes;
import com.bosch.tmp.integration.businessObjects.results.ORUBatchCapacityParameters;
import com.bosch.tmp.integration.businessObjects.results.PatientResultsContent;
import com.bosch.tmp.integration.creation.CreationException;
import com.bosch.tmp.integration.creation.CreatorFactory;
import com.bosch.tmp.integration.creation.results.ResultsCreationException;
import com.bosch.tmp.integration.dao.NotesDao;
import com.bosch.tmp.integration.dao.ResultDao;
import com.bosch.tmp.integration.dao.SessionResultDao;
import com.bosch.tmp.integration.entities.Notes;
import com.bosch.tmp.integration.entities.Result;
import com.bosch.tmp.integration.entities.SessionResult;
import com.bosch.tmp.integration.process.ProcessException;
import com.bosch.tmp.integration.serviceReference.cia.CIAService;
import com.bosch.tmp.integration.util.ConfigKeyEnum;
import com.bosch.tmp.integration.util.Error;
import com.bosch.tmp.integration.util.MessageUtils;
import com.bosch.tmp.integration.util.PatientResultsUtility;
import com.bosch.tmp.integration.util.ResultResponseTypeEnum;
import com.bosch.tmp.integration.util.ResultTypeEnum;
import com.bosch.tmp.integration.util.Utils;
import com.bosch.tmp.integration.validation.ConfigurationLoader;
import com.bosch.tmp.integration.validation.CustomerId;

/**
 * PatientResultBean implementation class..
 * @author BEA2KOR
 */
@Component
public class ResultsWorkflowHandler
{
	public static final Logger logger = LoggerFactory.getLogger(ResultsWorkflowHandler.class);

	@Autowired
	private CIAService ciaServiceProxy;

	@Autowired
	private ResultDao resultDao;

	@Autowired
	private SessionResultDao sessionResultDao;

	@Autowired
	private NotesDao notesDao;

	/**
	 * Constructor for instantiating PatientResultBeanImpl with the requestMsg.
	 * @param resultsRequestMsg
	 */
	public ResultsWorkflowHandler(){

	}

	/**
	 * Process QVR_Q17 message request based on the respective response type.
	 * Following are the use-cases defined in the results work-flow..
		 1. Objective Results.
		 2. Subjective Results.
		 3. Notes.
		 4. Objective with Notes.
		 5. Subjective with Notes.
	 * @param requestMsg
	 * @param oruBatchResponseMessageType
	 * @return
	 * @throws ResultsProcessException
	 * @throws ResultsCreationException
	 */
	public Object processQ17Event(QVRQ17CONTENT requestMsg, String oruBatchResponseMessageType) throws ResultsProcessException, ResultsCreationException
	{
		Object responseMsg = null;
		this.requestMsg = requestMsg;
		this.oruBatchResponseMessageType = oruBatchResponseMessageType;
		try
		{
			//Validate the request...
			if(requestMsg == null){
				return null;
			}

			//Assign it to the instance variable and add the results to a map..
			resultsDataMap.put("QVR_Q17Request",requestMsg);

			/*
			 * Update the ciaService with user details from the UAC segment.
			 * Service is dynamically updated with user details from the UAC segment on every request.
			 */
			getCiaServiceProxy().updateServiceWithUserDetails(requestMsg, error);

			//Process based on the response type and get result responses..
			resultResponseType = PatientResultsUtility.getResultResponseType(requestMsg);
			if(resultResponseType!=null && !resultResponseType.trim().equals("")
					&& Arrays.asList(ResultResponseTypeEnum.values()).contains(ResultResponseTypeEnum.findByResponseValue(resultResponseType))){
				if(resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE.responseType()) || resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.OBJECTIVE.responseType())){
					processUnAckedResults();
				}
				else if(resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.NOTES.responseType())){
					processUnAckedNotes();
				}
				else if(resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.OBJECTIVE_NOTES.responseType()) || resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE_NOTES.responseType())){
					processUnAckedResults();
					processUnAckedNotes();
				}
			}
			else {
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.RESULT_TYPE_NOT_FOUND.toString()));
				throw new ResultsProcessException("Result type not found in the request.. ");
			}

			//Add it to the results map..
			resultsDataMap.put("Unacked_Result_Responses", new ArrayList<PatientResultsContent>(patientResultsMap.values()));
			logger.debug("Number of patient results content created in this request : " + patientResultsMap.size());

			//Construct ORU-Response message...
			responseMsg = constructHL7ORUBatchResponse();
		}
		catch (ResultsProcessException resultsProcessException) {
			logger.error("Processing of QVR_Q17 event failed.. ", resultsProcessException);
			throw resultsProcessException ;
		}
		catch (ResultsCreationException resultsCreationException) {
			logger.error("Creation of ORU Batch result response failed.. ", resultsCreationException);
			throw resultsCreationException ;
		}
		catch (ProcessException processException) {
			logger.error("Processing of QVR_Q17 event failed.. ", processException);
			throw new ResultsProcessException("Processing of QVR_Q17 event failed.. ", processException);
		}
		return responseMsg;
	}

	/**
	 * Request work-flow for results. Results includes unAcked results (HB3 + T400) from RESULT Table and unAcked adhoc vitals from T400.
	 	- If the response type in the input request is "SUBJ/SUBNTE", then get all unacknowledged unique session id's from SESSION_RESULT CDA[List]. Else no. - Subjective sessions list..
		- Also, Get all unacknowledged unique session id's from RESULT table[List] - Objective sessions list..
		- Generate a unique list of session id's from the above 2 list. [List].
	 	- Invoke result service through CIA to get all the result responses for unique session id's obtained above.
	    - Put the subjective session list and objective session list along with the results response for the unique session id's into a map required for outgoing message construction..
	 * @throws ResultsProcessException
	 */
	private void processUnAckedResults() throws ResultsProcessException
	{
		try
		{
			//Get internal organization Id ...
			organizationId = getInternalOrganizationId();

			//Get all UnAcknowledged session vital id's (HB3) from RESULT/SESSION_RESULT table in CDA.
			List<ResultId> unAckedSubjectiveSessionIds = null;
			List<ResultId> unAckedObjectiveSessionIds = null;
			if(resultResponseType!=null){
				if(resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE.responseType()) || resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE_NOTES.responseType())){
					unAckedSubjectiveSessionIds = getUnAckedSubjectiveSessionIds();
				}
				unAckedObjectiveSessionIds = getUnAckedObjectiveSessionIds();
			}

			//Get Unique list of UnAcknowledged sessions from both subjective/objectives list obtained above...
			List<ResultId> unAckedUniqueSessionIdsToQuery = filterUnAckedSessionIds(unAckedSubjectiveSessionIds,unAckedObjectiveSessionIds);

			//Add and process all UnAcknowledged session/adhoc vitals through CIA to Results service...
			if(!unAckedUniqueSessionIdsToQuery.isEmpty()){
				getSessionResultsResponse(unAckedUniqueSessionIdsToQuery);
			}

			//Get all UnAcknowledged adhoc vital id's (T400) from RESULT table in CDA.
			List<ResultId> unAckedAdhocResultIdsToQuery = getUnAckedAdhocResultIds();

			//Add and process all UnAcknowledged session/adhoc vitals through CIA to Results service...
			if(!unAckedAdhocResultIdsToQuery.isEmpty()){
				getAdhocResultsResponse(unAckedAdhocResultIdsToQuery);
			}
		}
		catch (ResultsProcessException processException) {
			logger.error("Unable to construct patient results content for the unacked results/session id ...", processException);
			throw processException;
		}
	}

	/**
	 * Get all the UnAcknowledged sessions from CDA database (SESSION_RESULTS Table).
	 * Construct corresponding ResultId object for each vital id obtained.
	 * @return
	 * @throws ResultsProcessException
	 */
	private List<ResultId> getUnAckedSubjectiveSessionIds() throws ResultsProcessException
	{
		List<ResultId> unackedSubjectiveSessionsIds = new ArrayList<ResultId>();
		try
		{
			List<SessionResult> unackedSubjectiveSessionsResults = new ArrayList<SessionResult>();
			unackedSubjectiveSessionsResults = getSessionResultDao().findSessionResultsByOrganizationId(organizationId,Boolean.FALSE);
			if(unackedSubjectiveSessionsResults!=null && !unackedSubjectiveSessionsResults.isEmpty())
			{
				Set<Long> uniqueUnAckedSessionIds = new HashSet<Long>();
				for (SessionResult subbjectiveSessionResult : unackedSubjectiveSessionsResults)
				{
					//Creating a ResultId instance...
					ResultId resultId = new ResultId();
					resultId.setId(subbjectiveSessionResult.getSessionId());
					resultId.setType(ResultsType.SESSION);

					//Check for any duplicate before adding...
					if (uniqueUnAckedSessionIds.add(resultId.getId())){
						unackedSubjectiveSessionsIds.add(resultId);
					}
				}

				//Add it to the results map..
				resultsDataMap.put("Unacked_Subjective_Session_Ids", PatientResultsUtility.convertToSessionsMap(unackedSubjectiveSessionsResults));
                logger.debug("Number of unacked subjective sessions retrieved from SESSION_RESULT entity : " + unackedSubjectiveSessionsIds.size());
			}
		}
		catch (Exception e) {
			logger.error("Exception encountered while fetching unacked session id's from SESSION_RESULT entity..", e);
			throw new ResultsProcessException("Exception encountered while fetching unacked session id's from SESSION_RESULT entity..", e);
		}
		finally {

		}
		return unackedSubjectiveSessionsIds;
	}

	/**
	 * Get all the UnAcknowledged sessions from CDA database (RESULTS Table).
	 * Construct corresponding ResultId object for each vital id obtained.
	 * @return
	 * @throws ResultsProcessException
	 */
	private List<ResultId> getUnAckedObjectiveSessionIds() throws ResultsProcessException
	{
		List<ResultId> unackedObjectiveSessionsResultIds = new ArrayList<ResultId>();
		try
		{
			List<Result> unackedObjectiveSessionsResults = new ArrayList<Result>();
			unackedObjectiveSessionsResults = getResultDao().findResultsByOrganizationId(organizationId,Boolean.FALSE,ResultTypeEnum.SESSION_VITAL_SIGN.toString());
			if(unackedObjectiveSessionsResults!=null && !unackedObjectiveSessionsResults.isEmpty())
			{
				Set<Long> uniqueUnAckedResultIds = new HashSet<Long>();
				for (Result objectiveSessionResult : unackedObjectiveSessionsResults)
				{
					if (objectiveSessionResult.getType().equalsIgnoreCase(ResultTypeEnum.SESSION_VITAL_SIGN.toString()))
					{
						//Creating a ResultId instance...
						ResultId resultId = new ResultId();
						resultId.setId(objectiveSessionResult.getSessionId());
						resultId.setType(ResultsType.SESSION);

						//Check for any duplicate before adding...
						if (uniqueUnAckedResultIds.add(resultId.getId())){
							unackedObjectiveSessionsResultIds.add(resultId);
						}
					}
				}

				//Add it to the results map..
				resultsDataMap.put("Unacked_Objective_Session_Ids", PatientResultsUtility.convertToResultsMap(unackedObjectiveSessionsResults));
				resultsDataMap.put("Unacked_Objective_Session_Vital_Ids", PatientResultsUtility.convertToVitalsMap(unackedObjectiveSessionsResults));
                logger.debug("Number of unacked Objective sessions retrieved from RESULT entity : " + unackedObjectiveSessionsResultIds.size());
			}
		}
		catch (Exception e) {
			logger.error("Exception encountered while fetching unacked session id's from RESULT entity..", e);
			throw new ResultsProcessException("Exception encountered while fetching unacked session id's from RESULT entity..", e);
		}
		finally {

		}
		return unackedObjectiveSessionsResultIds;
	}

	/**
	 * Get all the UnAcknowledged adhoc vital id's (T400) from CDA database (RESULTS Table).
	 * Result Type - SESSION
	 * Construct corresponding ResultId object for each vital id obtained.
	 * @return
	 * @throws ResultsProcessException
	 */
	private List<ResultId> getUnAckedAdhocResultIds() throws ResultsProcessException
	{
		List<ResultId> unAckedAdhocResultIds = new ArrayList<ResultId>();
		try
		{
			List<Result> unAckedAdhocResults = new ArrayList<Result>();
			unAckedAdhocResults = getResultDao().findResultsByOrganizationId(organizationId, Boolean.FALSE,ResultTypeEnum.VITAL_SIGN.toString());
			if(unAckedAdhocResults!=null && !unAckedAdhocResults.isEmpty())
			{
				Set<Long> uniqueUnAckedResultIds = new HashSet<Long>();
				for (Result adhocResult : unAckedAdhocResults)
				{
					if (adhocResult.getType().equalsIgnoreCase(ResultTypeEnum.VITAL_SIGN.toString()))
					{
						//Creating a ResultId instance...
						ResultId resultId = new ResultId();
						resultId.setId(adhocResult.getResultId());
						resultId.setType(ResultsType.METER_MEASUREMENT);

						//Check for any duplicate before adding...
						if (!uniqueUnAckedResultIds.contains(resultId.getId())){
							unAckedAdhocResultIds.add(resultId);
						}
					}
				}

				//Add it to the results map..
				resultsDataMap.put("Unacked_Adhoc_Result_Ids", PatientResultsUtility.convertToVitalsMap(unAckedAdhocResults));
			}
			logger.debug("Number of unacked Adhocs results retrieved from RESULT entity : " + unAckedAdhocResultIds.size());
		}
		catch (Exception e) {
			logger.error("Exception encountered while fetching unacked Result id's from RESULT entity..", e);
			throw new ResultsProcessException("Exception encountered while fetching Result session id's from RESULT entity..", e);
		}
		finally {

		}
		return unAckedAdhocResultIds;
	}

	/**
	 * Get a unique list of unacked session ids from the subjective/objective list if not empty.
	 * @param unAckedSubjectiveSessions
	 * @param unAckedObjectiveSessions
	 * @return
	 */
	private List<ResultId> filterUnAckedSessionIds(List<ResultId> unAckedSubjectiveSessions,List<ResultId> unAckedObjectiveSessions)
	{
		Map<Long,ResultId> uniqueUnackedSessions = new HashMap<Long,ResultId>();
		try
		{
			if(unAckedSubjectiveSessions!=null && !unAckedSubjectiveSessions.isEmpty()) {
				for (ResultId unAckedSubjectionSession : unAckedSubjectiveSessions){
					uniqueUnackedSessions.put(unAckedSubjectionSession.getId(), unAckedSubjectionSession);
				}
			}
			if(unAckedObjectiveSessions!=null && !unAckedObjectiveSessions.isEmpty()) {
				for (ResultId unAckedObjectiveSession : unAckedObjectiveSessions){
					uniqueUnackedSessions.put(unAckedObjectiveSession.getId(), unAckedObjectiveSession);
				}
			}
		}
		finally {

		}
		return new ArrayList<ResultId>(uniqueUnackedSessions.values());
	}

	/**
	 * Invoke Result service through CIA for all the unique unacknowledged session Id's...
	 * Construct PushResult object for each session response received.
	 * @param unAckedUniqueSessionsToQuery
	 * @throws ResultsProcessException
	 */
	private void getSessionResultsResponse(List<ResultId> unAckedUniqueSessionsToQuery) throws ResultsProcessException
	{
		FindResultsReply findResults = new FindResultsReply();
		if (!unAckedUniqueSessionsToQuery.isEmpty())
		{
			FindResultsReply findResultsChunk = new FindResultsReply();

			//Get first set of Results along with the Chunks info...
			findResultsChunk = getCiaServiceProxy().findResults(unAckedUniqueSessionsToQuery, error);
			if(findResultsChunk!=null)
			{
				//Add the first chunk data to findResults instance...
				findResults.getSurveyResponse().addAll(findResultsChunk.getSurveyResponse());
				findResults.getIntegrationPatient().addAll(findResultsChunk.getIntegrationPatient());

				//Get the totals chunks from the find results reply...
				Long totalChunks = findResultsChunk.getChunkInfo().getTotalChunks();
				if(totalChunks > 1)
				{
					//Get the ORU Batch Max capacity..
					batchCapacity.setResultsBatchMaxCapacity(PatientResultsUtility.getORUBatchAllowedCapacity(resultsDataMap));

					//Get the request message from the map..
					QVRQ17CONTENT requestMsg = null;
					if(resultsDataMap!=null){
						if(!resultsDataMap.isEmpty()) {
							if(resultsDataMap.containsKey("QVR_Q17Request")){
								requestMsg = (QVRQ17CONTENT)resultsDataMap.get("QVR_Q17Request");
							}
						}
					}

					//Get the Result Response type.
					String resultResponseType = PatientResultsUtility.getResultResponseType(requestMsg);

					//Invoke FindResults for the next set of chunks if present..
					for (int i = 0; i < totalChunks; i++)
					{
						if (i > 0)
						{
							//Check the ORU Max limit and then invoke service for the next chunk if still capacity exists.
							if(!PatientResultsUtility.isORUMaxCapacityReached(resultResponseType,batchCapacity,findResults)){
								findResultsChunk = getCiaServiceProxy().findNextChunkResults(unAckedUniqueSessionsToQuery,findResultsChunk.getChunkInfo().getLastSeenItemId(),error);
								findResults.getSurveyResponse().addAll(findResultsChunk.getSurveyResponse());
								findResults.getIntegrationPatient().addAll(findResultsChunk.getIntegrationPatient());
							}
							else {
								//In case ORU Max limit is reached but the chunks still exists, then set this flag required for ORU construction....
								resultsDataMap.put("isMoreResultChunksAvailable", Boolean.TRUE);
								break;
							}
						}
					}
				}
				constructPatientResultsResponse(findResults);
			}
			else {
				logger.error("CIA results service returned NULL");
				throw new ResultsProcessException("CIA results service returned NULL");
			}
		}
	}

	/**
	 * Invoke Result service through CIA for all the unacknowledged result Id's...
	 * Construct PushResult object for each ad-hoc result response received.
	 * @param unAckedAdhocResultIdsToQuery
	 * @throws ResultsProcessException
	 */
	private void getAdhocResultsResponse(List<ResultId> unAckedAdhocResultIdsToQuery) throws ResultsProcessException
	{
		FindResultsReply findResults = new FindResultsReply();
		if (!unAckedAdhocResultIdsToQuery.isEmpty())
		{
			FindResultsReply findResultsChunk = new FindResultsReply();

			//Get first set of Results along with the Chunks info...
			findResultsChunk = getCiaServiceProxy().findResults(unAckedAdhocResultIdsToQuery, error);
			if(findResultsChunk!=null)
			{
				//Add the first chunk data to findResults instance...
				findResults.getMeterMeasurement().addAll(findResultsChunk.getMeterMeasurement());
				findResults.getIntegrationPatient().addAll(findResultsChunk.getIntegrationPatient());

				//Get the totals chunks from the find results reply...
				Long totalChunks = findResultsChunk.getChunkInfo().getTotalChunks();
				if(totalChunks > 1)
				{
					//Get the ORU Batch Max capacity..
					batchCapacity.setResultsBatchMaxCapacity(PatientResultsUtility.getORUBatchAllowedCapacity(resultsDataMap));

					//Get the request message from the map..
					QVRQ17CONTENT requestMsg = null;
					if(resultsDataMap!=null){
						if(!resultsDataMap.isEmpty()) {
							if(resultsDataMap.containsKey("QVR_Q17Request")){
								requestMsg = (QVRQ17CONTENT)resultsDataMap.get("QVR_Q17Request");
							}
						}
					}

					//Get the Result Response type.
					String resultResponseType = PatientResultsUtility.getResultResponseType(requestMsg);

					//Invoke FindResults for the next set of chunks if present..
					for (int i = 0; i < totalChunks; i++)
					{
						if (i > 0)
						{
							//Check the ORU Max limit and then invoke service for the next chunk if still capacity exists.
							if(!PatientResultsUtility.isORUMaxCapacityReached(resultResponseType,batchCapacity,findResultsChunk)){
								findResultsChunk = getCiaServiceProxy().findNextChunkResults(unAckedAdhocResultIdsToQuery,findResultsChunk.getChunkInfo().getLastSeenItemId(),error);
								findResults.getMeterMeasurement().addAll(findResultsChunk.getMeterMeasurement());
								findResults.getIntegrationPatient().addAll(findResultsChunk.getIntegrationPatient());
							}
							else {
								break;
							}
						}
					}
				}
				constructPatientResultsResponse(findResults);
			}
			else {
				logger.error("CIA results service returned NULL");
				throw new ResultsProcessException("CIA results service returned NULL");
			}
		}
	}

	/**
	 * Patient Results response construction..
	 * It contains a list of PatientResultsContent. Each patient details and session data will be added to single PushResult instance..
	 * @param findResultsReply
	 * @throws ResultsProcessException
	 */
	private void constructPatientResultsResponse(FindResultsReply findResultsReply) throws ResultsProcessException
	{
		if(resultResponseType!=null){
			if(resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE.responseType()) || resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE_NOTES.responseType())){
				constructPatientSubjectiveResults(findResultsReply);
			}
			else if(resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.OBJECTIVE.responseType()) || resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.OBJECTIVE_NOTES.responseType())){
				constructPatientObjectiveResults(findResultsReply);
			}
		}
	}

	/**
	 * Construct the Patient Objective results instance..
	 * @param findResultsReply
	 * @throws ResultsProcessException
	 */
	private void constructPatientObjectiveResults(FindResultsReply findResultsReply)  throws ResultsProcessException
	{
		try
		{
			//Get UnAcked objective session id's Map from the resultsDataMap..
			Map<Long,Result> unAckedObjectiveSessionIds = null;
			if(resultsDataMap!=null){
				if(!resultsDataMap.isEmpty()) {
					if(resultsDataMap.containsKey("Unacked_Objective_Session_Ids")){
						unAckedObjectiveSessionIds = (Map<Long,Result>)resultsDataMap.get("Unacked_Objective_Session_Ids");
					}
				}
			}

			//Get the UnAcked objective session vital id's message from the resultsDataMap ....
			Map<String,Result> unAckedObjectiveSessionVitalIds = null;
			if(resultsDataMap!=null){
				if(!resultsDataMap.isEmpty()) {
					if(resultsDataMap.containsKey("Unacked_Objective_Session_Vital_Ids")){
						unAckedObjectiveSessionVitalIds = (Map<String,Result>)resultsDataMap.get("Unacked_Objective_Session_Vital_Ids");
					}
				}
			}

			//Clinical sessions from the CL Results service reply...
			if (findResultsReply!=null && findResultsReply.getSurveyResponse()!=null && !findResultsReply.getSurveyResponse().isEmpty())
			{
				//Filter the session already acknowledged...
				for(SurveyResponse clinicalSession : findResultsReply.getSurveyResponse())
				{
					String patientBTPId = clinicalSession.getBtpId();
					if(unAckedObjectiveSessionIds!=null && unAckedObjectiveSessionIds.containsKey(clinicalSession.getId()))
					{
						List<QuestionResponse> unAckedQuestionResponseList = new ArrayList<QuestionResponse>();
						for (QuestionResponse questionResponse : clinicalSession.getQuestionResponse())
						{
							List<MeterMeasurement> unAckedMetermeasurementList = new ArrayList<MeterMeasurement>();
							for(MeterMeasurement meterMeasurement : questionResponse.getMeterMeasurement())
							{
								//Filter the ad-hoc vitals already acknowledged...
								List<AssessedVitalSign> unAckedAssessedVitalSignList = new ArrayList<AssessedVitalSign>();
								for (AssessedVitalSign assessedVitalSign : meterMeasurement.getVitalSign())
								{
									if (assessedVitalSign != null && assessedVitalSign.getId() != null)
									{
										//Remove all the acked vitals and consider only the un-acked vitals..
										List<VitalSignValue> unackedVitalSigns = new ArrayList<VitalSignValue>();
										for (VitalSignValue vitalSignValue : assessedVitalSign.getNumericValue()){
											if(unAckedObjectiveSessionVitalIds.containsKey(vitalSignValue.getId())){
												unackedVitalSigns.add(vitalSignValue);
											}
										}

										//Clears all the vital sign value and add only unAcked vital sign values to AssessedVitalSign as obtained above..
										assessedVitalSign.getNumericValue().clear();
										if(!unackedVitalSigns.isEmpty()){
											assessedVitalSign.getNumericValue().addAll(unackedVitalSigns);
											unAckedAssessedVitalSignList.add(assessedVitalSign);
										}
									}
								}

								//Clears all the Vitals and add only Unacked Vitals to MeterMeasurement as obtained above..
								meterMeasurement.getVitalSign().clear();
								if(!unAckedAssessedVitalSignList.isEmpty()){
									meterMeasurement.getVitalSign().addAll(unAckedAssessedVitalSignList);
									unAckedMetermeasurementList.add(meterMeasurement);
								}
							}

							//Clears all the Meter Measurement and add only Unacked Meter Measurement to Question Response as obtained above..
							questionResponse.getMeterMeasurement().clear();
							if(!unAckedMetermeasurementList.isEmpty()){
								questionResponse.getMeterMeasurement().addAll(unAckedMetermeasurementList);
								unAckedQuestionResponseList.add(questionResponse);
							}
						}

						//Clears all the Question responses and add only Unacked Question response to Clinical Session as obtained above..
						clinicalSession.getQuestionResponse().clear();
						if(!unAckedQuestionResponseList.isEmpty())
						{
							clinicalSession.getQuestionResponse().addAll(unAckedQuestionResponseList);

							//Construct PatientResultsContent for this meter measurement...
							if (!patientResultsMap.containsKey(patientBTPId)){
								patientResultsMap.put(patientBTPId, new PatientResultsContent());
							}
							patientResultsMap.get(patientBTPId).getSurveyResponse().add(clinicalSession);
						}
					}
				}
			}
			else {
				logger.debug("No sessions in the results to report back ...");
			}

			//Adhoc vitals from the results reply...
			if (findResultsReply!=null && findResultsReply.getMeterMeasurement()!=null && !findResultsReply.getMeterMeasurement().isEmpty()){
				constructPatientAdocResults(findResultsReply.getMeterMeasurement());
			}
			else {
				logger.debug("No adhoc vital sign in the results to report back ...");
			}

			//Enriching the patient information....
			if(!patientResultsMap.isEmpty()) {
				enrichPatientInformation(findResultsReply.getIntegrationPatient());
			}
		}
		catch (ResultsProcessException e){
			logger.error("Unable to construct patient results content for the unacked session id for Objective results...",e);
			throw new ResultsProcessException("Unable to construct patient results content for the unacked session id for Objective results...",e);
		}
		catch (Exception ex) {
			logger.error("Exception occurred in Patient Results content construction processor for Objective results.....", ex);
			throw new ResultsProcessException("Exception occurred in Patient Results content construction processor for Objective results.....", ex);
		}
	}

	/**
	 * This map stores patients internal id as key and PatientResultsContent as value.
        PatientResultsContent object holds patient's unacked results and patient information.
        FindResultsReply holds results related to different patients.
        The patient to which the current result belongs to is searched for in the map and the results are added to PatientResultsContent object.
	 * @param findResultsReply
	 * @throws ResultsProcessException
	 */
	private void constructPatientSubjectiveResults(FindResultsReply findResultsReply) throws ResultsProcessException
	{
		try
		{
			//Get UnAcked subjective session id's Map from the resultsDataMap..
			Map<Long,SessionResult> unAckedSubjectiveSessionIds = null;
			if(resultsDataMap!=null){
				if(!resultsDataMap.isEmpty()) {
					if(resultsDataMap.containsKey("Unacked_Subjective_Session_Ids")){
						unAckedSubjectiveSessionIds = (Map<Long,SessionResult>)resultsDataMap.get("Unacked_Subjective_Session_Ids");
					}
				}
			}

			//Get UnAcked objective session id's Map from the resultsDataMap..
			Map<Long,Result> unAckedObjectiveSessionIds = null;
			if(resultsDataMap!=null){
				if(!resultsDataMap.isEmpty()) {
					if(resultsDataMap.containsKey("Unacked_Objective_Session_Ids")){
						unAckedObjectiveSessionIds = (Map<Long,Result>)resultsDataMap.get("Unacked_Objective_Session_Ids");
					}
				}
			}

			//Clinical sessions from the CL Results service reply if present...
			if (findResultsReply!=null && findResultsReply.getSurveyResponse()!=null && !findResultsReply.getSurveyResponse().isEmpty())
			{
				for(SurveyResponse clinicalSession : findResultsReply.getSurveyResponse())
				{
					String patientBTPId = clinicalSession.getBtpId();
					if((unAckedSubjectiveSessionIds!= null && unAckedSubjectiveSessionIds.containsKey(clinicalSession.getId()))
							|| (unAckedObjectiveSessionIds!=null && unAckedObjectiveSessionIds.containsKey(clinicalSession.getId()))){

						//Construct PatientResultsContent for this meter measurement...
						if (!patientResultsMap.containsKey(patientBTPId)){
							patientResultsMap.put(patientBTPId, new PatientResultsContent());
						}
						patientResultsMap.get(patientBTPId).getSurveyResponse().add(clinicalSession);
					}
				}
			}
			else {
				logger.debug("No sessions in the results to report back ...");
			}

			//Adhoc vitals from the results reply if present...
			if (findResultsReply!=null && findResultsReply.getMeterMeasurement()!=null && !findResultsReply.getMeterMeasurement().isEmpty()){
				constructPatientAdocResults(findResultsReply.getMeterMeasurement());
			}
			else {
				logger.debug("No adhoc vital sign in the results to report back ...");
			}

			//Enriching the patient information....
			if(!patientResultsMap.isEmpty()) {
				enrichPatientInformation(findResultsReply.getIntegrationPatient());
			}
		}
		catch (ResultsProcessException e){
			logger.error("Unable to construct patient results content for the unacked session id for Subjective results...",e);
			throw new ResultsProcessException("Unable to construct patient results content for the unacked session id for Subjective results...",e);
		}
		catch (Exception ex) {
			logger.error("Exception occurred in Patient Results content construction processor for Subjective results...", ex);
			throw new ResultsProcessException("Exception occurred in Patient Results content construction processor for Subjective results...", ex);
		}
	}

	/**
	 * Construct PatientResultsContent instances for T400 reported adhoc vitals from the results reply..
	 * @param meterMeasurementList
	 * @throws ResultsProcessException
	 */
	private void constructPatientAdocResults(List<MeterMeasurement> meterMeasurementList) throws ResultsProcessException
	{
		try
		{
			//Get the request message from the map..
			Map<String,Result> unAckedAdhocVitalIds = null;
			if(resultsDataMap!=null){
				if(!resultsDataMap.isEmpty()) {
					if(resultsDataMap.containsKey("Unacked_Adhoc_Result_Ids")){
						unAckedAdhocVitalIds = (Map<String,Result>)resultsDataMap.get("Unacked_Adhoc_Result_Ids");
					}
				}
			}

			//Filter the ad-hoc vitals already acknowledged...
			if (unAckedAdhocVitalIds!=null && !unAckedAdhocVitalIds.isEmpty())
			{
				if (meterMeasurementList!=null && !meterMeasurementList.isEmpty())
				{
					for(MeterMeasurement meterMeasurement : meterMeasurementList)
					{
						String patientBTPId = meterMeasurement.getBtpId();
						List<AssessedVitalSign> unAckedAssessedVitalSignList = new ArrayList<AssessedVitalSign>();
						for (AssessedVitalSign assessedVitalSign : meterMeasurement.getVitalSign())
						{
							if (assessedVitalSign != null && assessedVitalSign.getId() != null)
							{
								//Remove all the acked vitals and consider only the un-acked vitals..
								List<VitalSignValue> unackedVitalSigns = new ArrayList<VitalSignValue>();
								for (VitalSignValue vitalSignValue : assessedVitalSign.getNumericValue()){
									if(unAckedAdhocVitalIds.containsKey(vitalSignValue.getId())){
										unackedVitalSigns.add(vitalSignValue);
									}
								}

								//Clears all the vital sign value and add only unAcked vital sign values to AssessedVitalSign as obtained above..
								assessedVitalSign.getNumericValue().clear();
								if(!unackedVitalSigns.isEmpty()){
									assessedVitalSign.getNumericValue().addAll(unackedVitalSigns);
									unAckedAssessedVitalSignList.add(assessedVitalSign);
								}
							}
						}

						//Clears all the Vitals and add only Unacked Vitals to MeterMeasurement as obtained above..
						meterMeasurement.getVitalSign().clear();
						if(!unAckedAssessedVitalSignList.isEmpty())
						{
							meterMeasurement.getVitalSign().addAll(unAckedAssessedVitalSignList);

							//Construct PatientResultsContent for this meter measurement...
							if (!patientResultsMap.containsKey(patientBTPId)){
								patientResultsMap.put(patientBTPId, new PatientResultsContent());
							}
							patientResultsMap.get(patientBTPId).getMeterMeasurement().add(meterMeasurement);
						}
					}
				}
			}
		}
		catch (Exception ex) {
			logger.error("Exception occurred in Patient Results content construction processor for Adhoc results.....", ex);
			throw new ResultsProcessException("Exception occurred in Patient Results content construction processor for Adhoc results.....", ex);
		}
	}

	/**
	 * Request work-flow for Only notes.
	 * It includes both patient and session notes.
	 * All the unacked notes will be sent in the ORU batch response.
	 * @throws ResultsProcessException
	 */
	private void processUnAckedNotes() throws ResultsProcessException
	{
		try
		{
			//Get internal organization Id ...
			if(organizationId == null) {
				organizationId = getInternalOrganizationId();
			}

			//Get all UnAcknowledged session vital id's (HB3) from RESULT/SESSION_RESULT table in CDA.
			List<EqualsFilterType> unAckedNoteIdsToQuery = null;
			if(resultResponseType!=null){
				unAckedNoteIdsToQuery = getUnAckedNoteId();
			}

			//Add and process all UnAcknowledged session/adhoc vitals through CIA to Results service...
			if(!unAckedNoteIdsToQuery.isEmpty()){
				getNotesResponse(unAckedNoteIdsToQuery);
			}
		}
		catch (ResultsProcessException processException) {
			logger.error("Unable to construct notes content for the unacked note id ...", processException);
			throw processException;
		}
	}

	/**
	 * Get all the UnAcknowledged Notes from CDA database (NOTES Table).
	 * Construct corresponding EqualsFilterType object for each note id obtained.
	 * @return
	 * @throws ResultsProcessException
	 */
	private List<EqualsFilterType> getUnAckedNoteId() throws ResultsProcessException
	{
		List<EqualsFilterType> unAckedNoteFilters = new ArrayList<EqualsFilterType>();
		try
		{
			List<Notes> unAckedNotes = new ArrayList<Notes>();
			unAckedNotes = getNotesDao().findNotesByOrganizationId(organizationId,Boolean.FALSE);
			if(unAckedNotes!=null && !unAckedNotes.isEmpty())
			{
				Set<Long> uniqueUnAckedNoteIds = new HashSet<Long>();
				for (Notes note : unAckedNotes)
				{
					//Creating a Note filer instance with the note id..
					EqualsFilterType noteFilter = new EqualsFilterType();
					noteFilter.setFieldName(SearchableFieldNameType.ID);
					noteFilter.setValue(Long.toString(note.getNotesId()));

					//Check for any duplicate before adding...
					if (uniqueUnAckedNoteIds.add(note.getId())){
						unAckedNoteFilters.add(noteFilter);
					}
				}

				//Add it to the results map..
				resultsDataMap.put("Unacked_Note_Ids", PatientResultsUtility.convertToNotesMap(unAckedNotes));
                logger.debug("Number of unacked Notes retrieved from NOTES table : " + unAckedNotes.size());
			}
		}
		catch (Exception e) {
			logger.error("Exception encountered while fetching unacked notes from NOTES entity..", e);
			throw new ResultsProcessException("Exception encountered while fetching unacked notes from NOTES entity..", e);
		}
		finally {

		}
		return unAckedNoteFilters;
	}

	/**
	 * Process all the unacked notes, by invoking Notes service through CIA.
	 * @param unAckedNoteIdsToQuery
	 * @throws ResultsProcessException
	 */
	private void getNotesResponse(List<EqualsFilterType> unAckedNoteIdsToQuery) throws ResultsProcessException
	{
		FindNotesReply findNotes = new FindNotesReply();
		if (!unAckedNoteIdsToQuery.isEmpty())
		{
			FindNotesReply findNotesChunk = new FindNotesReply();

			//Get first set of Results along with the Chunks info...
			findNotesChunk = getCiaServiceProxy().findNotes(unAckedNoteIdsToQuery,error);
			if(findNotesChunk!=null)
			{
				//Add the first chunk data to findNotes instance...
				findNotes.getNote().addAll(findNotesChunk.getNote());
				findNotes.getIntegrationPatient().addAll(findNotesChunk.getIntegrationPatient());

				//Get the total page number from the find notes reply...
				int totalChunks = findNotesChunk.getPageInfo().getTotalPages();
				if(totalChunks > 1)
				{
					//Get the ORU Batch Max capacity..
					batchCapacity.setNotesMaxCapacity(PatientResultsUtility.getORUNotesAllowedCapacity());

					//Invoke FindResults for the next set of chunks if present..
					for (int i = 0; i < totalChunks; i++)
					{
						if (i > 0)
						{
							//Check the ORU Max limit and then invoke service for the next chunk if still capacity exists.
							if(!PatientResultsUtility.isORUNotesCapacityReached(batchCapacity,findNotesChunk)){
								findNotesChunk = getCiaServiceProxy().findNextPageNotes(unAckedNoteIdsToQuery,findNotesChunk.getPageInfo().getPageNumber(),findNotesChunk.getPageInfo().getPageSize(),error);
								findNotes.getNote().addAll(findNotesChunk.getNote());
								findNotes.getIntegrationPatient().addAll(findNotesChunk.getIntegrationPatient());
							}
							else {
								break;
							}
						}
					}
				}
				constructNotesResponse(findNotes);
			}
			else {
				logger.error("CIA results service returned NULL");
				throw new ResultsProcessException("CIA results service returned NULL");
			}
		}
	}

	/**
	 * Construct notes response object to build ORU batch. Here notes will be filtered based on patient notes and session notes and accordingly
	 * will put into the map for further processing for ORU construction.
	 * @param findNotes
	 * @throws ResultsProcessException
	 */
	private void constructNotesResponse(FindNotesReply findNotes) throws ResultsProcessException
	{
		try
		{
			//Get the request message from the map..
			Map<Long,Notes> unAckedNoteIds = null;
			if(resultsDataMap!=null){
				if(!resultsDataMap.isEmpty()) {
					if(resultsDataMap.containsKey("Unacked_Note_Ids")){
						unAckedNoteIds = (Map<Long,Notes>)resultsDataMap.get("Unacked_Note_Ids");
					}
				}
			}

			if (unAckedNoteIds!=null && !unAckedNoteIds.isEmpty())
			{
				if (findNotes!=null && !findNotes.getNote().isEmpty())
				{
					//Create/Update PatientResultContent...
					for(Note note : findNotes.getNote())
					{
						//Extract notes header and type...
						HeaderType noteDetails = note.getHeader();
						NotesPayloadType noteType = note.getPayload();
						if(noteType == null || !(noteType instanceof PatientNotesPayloadType || noteType instanceof SurveyNotesPayloadType)){
							logger.error("Note type is unknown or not available for the Note id :" + noteDetails.getId());
							break;
						}

						//Patient/Survey notes..
						String patientBTPId = null;
						if(noteType instanceof SurveyNotesPayloadType)
						{
							//Extract the Note type and related patient BTP id..
							SurveyNotesPayloadType surveyNote = (SurveyNotesPayloadType)noteType;
							patientBTPId = surveyNote.getPatient().getBtpId();
							Long surveyId = surveyNote.getSourceId();

							//Construct PatientResultsContent for the unique patient, if not present.
							if (!patientResultsMap.containsKey(patientBTPId)){
								patientResultsMap.put(patientBTPId, new PatientResultsContent());
							}

							//Construct ClinicalSurveyNotes instance for each unique session only. If session id is already added, then add it to the same..
							Map<Long, ClinicalSurveyNotes> clinicalSurveyNotes = patientResultsMap.get(patientBTPId).getSurveyNotes();
							if(clinicalSurveyNotes!=null){
								if(clinicalSurveyNotes.containsKey(surveyId)){
									clinicalSurveyNotes.get(surveyId).getNotes().add(note);
								}
								else
								{
									//Get the Survey received and response date from SESSION entity ...
									List<SessionResult> survey = getSessionResultDao().findSessionResultBySessionIdAndOrgId(Long.toString(surveyId),organizationId);
									if(survey == null || survey.isEmpty()){
										logger.debug("Survey details not found in CDA-system. Ignoring the associated note");
										break;
									}

									//Create the first survey note instance...
									ClinicalSurveyNotes clinicalSurveyNote = new ClinicalSurveyNotes();
									clinicalSurveyNote.setSurveyId(surveyId);
									clinicalSurveyNote.setSurveyRecievedDate(Utils.convertStringToXMLGregorianCalendar(survey.get(0).getRecievedTimestamp().toString()));
									clinicalSurveyNote.setSurveyResponseDate(Utils.convertStringToXMLGregorianCalendar(survey.get(0).getResponseTimestamp().toString()));
									clinicalSurveyNote.getNotes().add(note);
									clinicalSurveyNotes.put(surveyId,clinicalSurveyNote);
								}
							}
						}
						else if(noteType instanceof PatientNotesPayloadType)
						{
							//Extract the Note type and related patient BTP id..
							PatientNotesPayloadType patientNote = (PatientNotesPayloadType)noteType;
							patientBTPId = patientNote.getPatient().getBtpId();

							//Construct PatientResultsContent for the unique patient, if not present.
							if (!patientResultsMap.containsKey(patientBTPId)){
								patientResultsMap.put(patientBTPId, new PatientResultsContent());
							}

							//Add all the Patient notes...
							patientResultsMap.get(patientBTPId).getPatientNotes().getNotes().add(note);
						}
					}

					//Enriching the patient information....
					if(!patientResultsMap.isEmpty()) {
						enrichPatientInformation(findNotes.getIntegrationPatient());
					}
				}
			}
		}
		catch (Exception ex) {
			logger.error("Exception occurred in Notes content construction for ORU building ...", ex);
			throw new ResultsProcessException("Exception occurred in Notes content construction for ORU building ..", ex);
		}
	}

	/**
	 * Enrich integrated patient information based on the patient id defined as key inside a map.
	 * @param integrationPatientList
	 * @throws ResultsProcessException
	 */
	private void enrichPatientInformation(List<IntegrationPatientType> integrationPatientList) throws ResultsProcessException
	{
		try
		{
			//Enrich patient information based on the BTPID, only if not exists...
			if(!patientResultsMap.isEmpty()){
				for(IntegrationPatientType integratedPatient : integrationPatientList){
					String integratedPatientBTPId = integratedPatient.getBtpId();
					if(patientResultsMap.containsKey(integratedPatientBTPId)){
						if(patientResultsMap.get(integratedPatientBTPId).getIntegrationPatient()==null) {
							patientResultsMap.get(integratedPatientBTPId).setIntegrationPatient(integratedPatient);
						}
					}
				}
			}
			logger.debug("List of BTP id's found for results processing in this QVR_Q17 request : " + patientResultsMap.keySet().toArray().toString());
		}
		catch (Exception ex) {
			logger.error("Exception occurred during patient details enrichment for results content..", ex);
			throw new ResultsProcessException("Exception occurred during patient details enrichment for results content..", ex);
		}
	}

	/**
	 * Construct the response message based on trigger event.
	 * @return
	 * @throws ResultsCreationException
	 */
	private Object constructHL7ORUBatchResponse() throws ResultsCreationException
	{
		Object responseBatchMessage = null;
		try
		{
			responseBatchMessage = CreatorFactory.getCreator(getCustomerId()).createMessage(oruBatchResponseMessageType,resultsDataMap);
		}
		catch (CreationException e) {
			throw new ResultsCreationException(oruBatchResponseMessageType + "  results batch creation failed during results processing... ",e);
		}
		catch (Exception ex) {
			logger.error("Exception occurred during "+ oruBatchResponseMessageType + "results batch creation ", ex);
			throw new ResultsCreationException("Exception occurred during "+ oruBatchResponseMessageType + "results batch creation ", ex);
		}
		return responseBatchMessage;
	}


	/**
	 * Verify the sign-up organization in the TH+ system given external organization id from the incoming message.
	 * Extract internal organization Id from the returned organization service response.
	 * @return
	 * @throws ResultsProcessException
	 */
	private String getInternalOrganizationId() throws ResultsProcessException
	{
		String internalOrgId = null;
		OrganizationType signupOrganization = null;
		try
		{
			//Find and Verify external organization...
			String externalOrgId = MessageUtils.getOrganizationId(requestMsg);
			List<OrganizationType> organizations = getCiaServiceProxy().getExternalOrganizations(externalOrgId, getCustomerId().value(), error);
			if (organizations.isEmpty()){
				logger.error("Cannot find Organization in TH+ system with the org Id:  " + externalOrgId);
				error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.UNKNOWN_ORGANIZATION.toString()));
				throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
			}

			//Extract the organizations..
			for (OrganizationType organization : organizations){
				if (organization.getInternalOrganization().isEmpty()){
					logger.error("Organization is not mapped to any internal organization:  " + externalOrgId);
					error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.ORGANIZATION_NOT_MAPPED.toString()));
					throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
				}
				else if (!organization.getInternalOrganization().get(0).isIsIntegrated()){
					logger.error("Organization is not integrated:  " + externalOrgId);
					error.getErrorList().add(MessageUtils.createError(ConfigKeyEnum.ORGANIZATION_NOT_INTEGRATED.toString()));
					throw new ProcessException("SA Process Error: " + error.getError().getFaultInternalMessage());
				}
				else {
					signupOrganization = organization;
					logger.info("Signup Organization - Internal Org Id: " + organization.getInternalOrganization().get(0).getOrganizationIdentifier());
					break;
				}
			}
			internalOrgId = MessageUtils.getInternalOrganizationList(signupOrganization).toString();
			logger.debug("Internal Organization Id found for this QVR_Q17 request : " + internalOrgId);
		}
		catch (ProcessException e) {
			logger.error("Unable able to obtain internal organization Id for the request...", e);
			throw new ResultsProcessException("Unable able to obtain internal organization Id for the request...", e);
		}
		catch (Exception ex){
			logger.error("Exception occurred while getting org Id from incoming message.");
			throw new ResultsProcessException("Exception occurred while getting org Id from incoming message. " + ex.getMessage());
		}
		return internalOrgId;
	}

	/**
	 * Method to get the customer Id from the config file.
	 * If no customer Id is configured then SA (Standard Adapter) is used as default.
	 * @return
	 */
	public CustomerId getCustomerId(){
		CustomerId customerId = (CustomerId) ConfigurationLoader.getCustomerId();
		return customerId == null ? CustomerId.SA : customerId;
	}

	private QVRQ17CONTENT requestMsg = null;
	private String organizationId = null;
	private String resultResponseType = null;
	private String oruBatchResponseMessageType = null;
	private Map<String,Object> resultsDataMap = new HashMap<String,Object>();
	private Map<String, PatientResultsContent> patientResultsMap = new HashMap<String, PatientResultsContent>();
	private ORUBatchCapacityParameters batchCapacity = new ORUBatchCapacityParameters();
	private Error error = null;

	public CIAService getCiaServiceProxy() {
		return ciaServiceProxy;
	}
	public void setCiaServiceProxy(CIAService ciaServiceProxy) {
		this.ciaServiceProxy = ciaServiceProxy;
	}
	public ResultDao getResultDao() {
		return resultDao;
	}
	public void setResultDao(ResultDao resultDao) {
		this.resultDao = resultDao;
	}
	public SessionResultDao getSessionResultDao() {
		return sessionResultDao;
	}
	public void setSessionResultDao(SessionResultDao sessionResultDao) {
		this.sessionResultDao = sessionResultDao;
	}
	public NotesDao getNotesDao() {
		return notesDao;
	}
	public void setNotesDao(NotesDao notesDao) {
		this.notesDao = notesDao;
	}
	public Error getError() {
		return error;
	}
	public void setError(Error error) {
		this.error = error;
	}
}
