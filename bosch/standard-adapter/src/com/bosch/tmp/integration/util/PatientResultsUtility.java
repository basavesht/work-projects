package com.bosch.tmp.integration.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.v2xml.QPD9CONTENT;
import org.hl7.v2xml.QVRQ17CONTENT;
import org.hl7.v2xml.RCPCONTENT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosch.th.cia.FindNotesReply;
import com.bosch.th.cia.FindResultsReply;
import com.bosch.th.cia.Note;
import com.bosch.th.patientrecord.AssessedVitalSign;
import com.bosch.th.patientrecord.MeterMeasurement;
import com.bosch.th.patientrecord.QuestionResponse;
import com.bosch.th.patientrecord.SurveyResponse;
import com.bosch.tmp.cl.task.HeaderType;
import com.bosch.tmp.integration.businessObjects.results.ORUBatchCapacityParameters;
import com.bosch.tmp.integration.entities.Notes;
import com.bosch.tmp.integration.entities.Result;
import com.bosch.tmp.integration.entities.SessionResult;
import com.bosch.tmp.integration.process.results.PatientResultsProcessor;
import com.bosch.tmp.integration.validation.ConfigurationLoader;

/**
 * This class wraps all the utility methods for results processing.
 * Following are the use-cases defined in the results workflow..
	 1. Objective Results.
	 2. Subjective Results.
	 3. Notes.
	 4. Objective with Notes.
	 5. Subjective with Notes.
 * @author BEA2KOR
 *
 */
public class PatientResultsUtility {

	/**
	 * Private Constructor..
	 */
	private PatientResultsUtility(){

	}

	public static final Logger logger = LoggerFactory.getLogger(PatientResultsProcessor.class);

	/**
	 * Gets the results response type from QVR_Q17 request message.
	 * @param requestMsg
	 * @return
	 */
	public static String getResultResponseType(QVRQ17CONTENT requestMsg){
		String responseType = null;
		if(requestMsg!=null && requestMsg.getQPD()!=null && requestMsg.getQPD().getQPD9()!=null){
			if(!requestMsg.getQPD().getQPD9().isEmpty()){
				QPD9CONTENT responseTypeContent = requestMsg.getQPD().getQPD9().get(0);
				responseType = (responseTypeContent!=null && responseTypeContent.getCX1()!=null && !responseTypeContent.getCX1().getValue().trim().equals(""))?responseTypeContent.getCX1().getValue():null;
			}
		}
		return responseType;
	}

	public static boolean isORUPerBatchLimitReached(int count, int limit){
		return false;
	}

	public static boolean isOBRPerOrderLimitReached(int count, int limit){
		return false;
	}

	public static boolean isOBXPerObservationRequestLimitReached(int count, int limit){
		return false;
	}


	/**
	 * Calculate the maximum allowed capacity of ORU Batch based on configuration/request/maximum value configured.
	 * @param resultsDataMap
	 * @return
	 */
	public static Integer getORUBatchAllowedCapacity(Map<String,Object> resultsDataMap)
	{
		//Default it to the constants...
		int numberOfORUPerBatch = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_ORU_PER_BATCH.getLimit();
		int numberOfOrderPerORU = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_ORDER_PER_ORU.getLimit();
		int numberOfObservationPerOrder = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_OBSERVATION_PER_ORDER.getLimit();
		Integer oruBatchAllowedObervationCapacity = ORUBatchSegmentLimitsEnum.ORU_BATCH_ALLOWED_CAPACITY.getLimit();
		QVRQ17CONTENT requestMsg = null;
		try
		{
			//Get the request message from the map..
			if(resultsDataMap!=null){
				if(!resultsDataMap.isEmpty()) {
					if(resultsDataMap.containsKey("QVR_Q17Request")){
						requestMsg = (QVRQ17CONTENT)resultsDataMap.get("QVR_Q17Request");
					}
				}
			}

			//Get the number of ORU's to be considered based on the input request and max limit set...
			if(requestMsg!=null) {
				RCPCONTENT requestMsgRCP = (RCPCONTENT) requestMsg.getRCP();
				numberOfORUPerBatch = getNumberOfORUPerBatch(requestMsgRCP);
			}
			numberOfOrderPerORU = NumberOfOrderPerORU();
			numberOfObservationPerOrder = geNumberOfObservationPerOrder();

			//Total calculated number of observations per batch..
			oruBatchAllowedObervationCapacity = (numberOfORUPerBatch)*(numberOfOrderPerORU*numberOfObservationPerOrder);
		}
		catch (Exception e) {
			logger.debug("Error during processing getORUBatchAllowedCapacity");
		}
		return oruBatchAllowedObervationCapacity;
	}

	/**
	 * Creates a map with key as the resultId and value as complete ResultId entity...
	 * @param resultIdList
	 * @return
	 */
	public static Map<String,Result> convertToVitalsMap(List<Result> results){
		Map<String,Result> resultIdMap = new HashMap<String,Result>();
		for(Result resultId : results){
			if(resultId!=null) {
				resultIdMap.put(resultId.getVitalSignId(), resultId);
			}
		}
		return resultIdMap;
	}

	/**
	 * Creates a map with key as the resultId and value as complete ResultId entity...
	 * @param resultIdList
	 * @return
	 */
	public static Map<Long,Result> convertToResultsMap(List<Result> results){
		Map<Long,Result> resultIdMap = new HashMap<Long,Result>();
		for(Result resultId : results){
			if(resultId!=null) {
				resultIdMap.put(resultId.getSessionId(), resultId);
			}
		}
		return resultIdMap;
	}

	/**
	 * Creates a map with key as the sessionId and value as complete Session_Result entity...
	 * @param resultIdList
	 * @return
	 */
	public static Map<Long,SessionResult> convertToSessionsMap(List<SessionResult> sessions){
		Map<Long,SessionResult> sesssionIdMap = new HashMap<Long,SessionResult>();
		for(SessionResult sessionId : sessions){
			if(sessionId!=null) {
				sesssionIdMap.put(sessionId.getSessionId(), sessionId);
			}
		}
		return sesssionIdMap;
	}

	/**
	 * Creates a map with key as the resultId and value as complete ResultId entity...
	 * @param resultIdList
	 * @return
	 */
	public static Map<Long,Notes> convertToNotesMap(List<Notes> notes){
		Map<Long,Notes> noteIdMap = new HashMap<Long,Notes>();
		for(Notes note : notes){
			if(note!=null) {
				noteIdMap.put(note.getNotesId(), note);
			}
		}
		return noteIdMap;
	}

	/**
	 * Determining max number of ORU's per batch...
	 * @param requestMsgRCP
	 * @return
	 */
	public static Integer getNumberOfORUPerBatch(RCPCONTENT requestMsgRCP)
	{
		int numberOfORUPerBatch = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_ORU_PER_BATCH.getLimit();
		try
		{
			try {
				numberOfORUPerBatch = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.RESULTS_COUNT_PER_MESSAGE.toString()));
			}
			catch (Exception e1) {
				logger.debug("Processing with the default values due to an exception...");
			}

			//Get the number of ORU's to be considered based on the input request and configured value set...
			if(requestMsgRCP!=null)
			{
				//Considering the smaller value between the configured value and request data ...
				int numberOfORUPerBatchFromRequest = getORUBatchLimitFromRequest(requestMsgRCP);
				try {
					if((numberOfORUPerBatchFromRequest != 0) && (numberOfORUPerBatchFromRequest < numberOfORUPerBatch)) {
						numberOfORUPerBatch = numberOfORUPerBatchFromRequest;
					}
				}
				catch (Exception e){
					logger.debug("Wrong RCP value is being sent in the Q17, Hence using the default value of RCP. i.e, 25");
				}
			}

			//Checking for max limit. If the number calculated exceeds the maximum limit set, then consider the max limit to handle any performance..
			if (numberOfORUPerBatch > ORUBatchSegmentLimitsEnum.MAX_NUM_OF_ORU_PER_BATCH.getLimit()){
				numberOfORUPerBatch = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_ORU_PER_BATCH.getLimit();
			}
		}
		catch (Exception e) {
			logger.debug("Error during processing getORUBatchAllowedCapacity - Setting the value to default max limit");
		}
		return numberOfORUPerBatch;
	}

	/**
	 * Determining number of OBR to be present in ORUBatch ....
	 * @return
	 */
	public static Integer NumberOfOrderPerORU()
	{
		int numberOfOrderPerORU = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_ORDER_PER_ORU.getLimit();
		try
		{
			try {
				numberOfOrderPerORU = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.NUMBER_OF_OBR_SEGMENTS.toString()));
			}
			catch (Exception e1) {
				logger.debug("getNumberOfORUPerOrder() : Processing with the default values due to an exception...");
			}

			//Checking for max limit. If the number configured exceeds the maximum limit set, then consider the max limit to handle any performance..
			if (numberOfOrderPerORU > ORUBatchSegmentLimitsEnum.MAX_NUM_OF_ORDER_PER_ORU.getLimit()){
				numberOfOrderPerORU = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_ORDER_PER_ORU.getLimit();
			}
		}
		catch (Exception e) {
			logger.debug("getNumberOfORUPerOrder() : Processing with the default values due to an exception...");
		}
		return numberOfOrderPerORU;
	}

	/**
	 * Determining the number of OBX's to be present in ORUBatch..
	 * @return
	 */
	public static Integer geNumberOfObservationPerOrder()
	{
		int numberOfObservationPerOrder = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_OBSERVATION_PER_ORDER.getLimit();
		try
		{
			try {
				numberOfObservationPerOrder = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.NUMBER_OF_OBX_SEGMENTS.toString()));
			}
			catch (Exception e1) {
				logger.debug("Processing with the default values due to an exception...");
			}

			//Checking for max limit. If the number configured exceeds the maximum limit set, then consider the max limit to handle any performance..
			if (numberOfObservationPerOrder > ORUBatchSegmentLimitsEnum.MAX_NUM_OF_OBSERVATION_PER_ORDER.getLimit()){
				numberOfObservationPerOrder = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_OBSERVATION_PER_ORDER.getLimit();
			}
		}
		catch (Exception e) {
			logger.debug("Error during processing getORUBatchAllowedCapacity");
		}
		return numberOfObservationPerOrder;
	}

	/**
	 * Get number of ORU Batch limit from the request RCP content..
	 * @param requestMsgRCP
	 * @return
	 */
	public static Integer getORUBatchLimitFromRequest(RCPCONTENT requestMsgRCP)
	{
		int numberOfORUPerBatchFromRequest = 0;
		if (null != requestMsgRCP && null != requestMsgRCP.getRCP2() && null != requestMsgRCP.getRCP2().getCQ1())
		{
			String value = requestMsgRCP.getRCP2().getCQ1().getValue();
			if (null != value && value.length() > 0)
			{
				try {
					numberOfORUPerBatchFromRequest = Integer.parseInt(value);
				}
				catch(Exception e){
					numberOfORUPerBatchFromRequest = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_ORU_PER_BATCH.getLimit();
				}
			}
		}
		return numberOfORUPerBatchFromRequest;
	}

	/**
	 * Returns the ORU Batch response message HL7 message type..
	 * @return
	 */
	public static String getORUBatchResponseHL7MessageType(){
		String messageType = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.ORU_BATCH_RESPONSE_HL7_MESSAGE_TYPE.toString());
		try {
			messageType  =	messageType == null || messageType.trim().equals("") ? ORUBatchResponseMessageType.ORU_R30.getBatchMessageType(): messageType;
		} catch (Exception e) {
			messageType = ORUBatchResponseMessageType.ORU_R30.getBatchMessageType();
		}
		return messageType;
	}

	/**
	 * Number of NTE Per ORU...
	 * @return
	 */
	public static Integer getNumberOfNTEPerORU()
	{
		int numberOfNTEPerPatient = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_NTE_PER_ORU.getLimit();
		try
		{
			try {
				numberOfNTEPerPatient = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.NUMBER_OF_NTE_PER_PATIENT.toString()));
			}
			catch (Exception e1) {
				logger.debug("Processing with the default values due to an exception...");
			}

			//Checking for max limit. If the number configured exceeds the maximum limit set, then consider the max limit to handle any performance..
			if (numberOfNTEPerPatient > ORUBatchSegmentLimitsEnum.MAX_NUM_OF_NTE_PER_ORU.getLimit()){
				numberOfNTEPerPatient = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_NTE_PER_ORU.getLimit();
			}
		}
		catch (Exception e) {
			logger.debug("Error during processing getORUBatchAllowedCapacity");
		}
		return numberOfNTEPerPatient;
	}

	/**
	 * Number of NTE Per Order...
	 * @return
	 */
	public static Integer getNumberOfNTEPerOrder()
	{
		int numberOfNTEPerOrder = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_NTE_PER_ORDER.getLimit();
		try
		{
			try {
				numberOfNTEPerOrder = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.NUMBER_OF_NTE_PER_SESSION.toString()));
			}
			catch (Exception e1) {
				logger.debug("Processing with the default values due to an exception...");
			}

			//Checking for max limit. If the number configured exceeds the maximum limit set, then consider the max limit to handle any performance..
			if (numberOfNTEPerOrder > ORUBatchSegmentLimitsEnum.MAX_NUM_OF_NTE_PER_ORDER.getLimit()){
				numberOfNTEPerOrder = ORUBatchSegmentLimitsEnum.MAX_NUM_OF_NTE_PER_ORDER.getLimit();
			}
		}
		catch (Exception e) {
			logger.debug("Error during processing getORUBatchAllowedCapacity");
		}
		return numberOfNTEPerOrder;
	}


	/**
	 * Calculate and check the ORU batch capacity based on the allowed number of OBX's and the number of OBX's returned per chunk from Result service..
	 * @param resultsDataMap
	 * @param reply
	 * @return
	 */
	public static boolean isORUMaxCapacityReached(String resultResponseType,ORUBatchCapacityParameters batchCapacity,FindResultsReply reply)
	{
		boolean isMaxCapacityReached = false;
		Integer resultsBatchCurrentCapacity = batchCapacity.getResultsBatchCurrentCapacity();
		Integer resultsBatchMaxCapacity = batchCapacity.getResultsBatchMaxCapacity();
		try
		{
			//HB3/T400 sessions..
			if(reply!=null && reply.getSurveyResponse()!=null && !reply.getSurveyResponse().isEmpty())
			{
				if(resultResponseType!=null && (resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.OBJECTIVE.responseType()) || resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.OBJECTIVE_NOTES.responseType()))){
					//For HB3 Session. Only vital related information present within the session will be mapped against a separate OBX.
					for (SurveyResponse surveyResponse : reply.getSurveyResponse()){
						for (QuestionResponse questionResponse : surveyResponse.getQuestionResponse()){
							for (MeterMeasurement meterMeasurement : questionResponse.getMeterMeasurement()){
								for (AssessedVitalSign assesVitalSign : meterMeasurement.getVitalSign()){
									if (assesVitalSign != null && assesVitalSign.getId() != null){
										resultsBatchCurrentCapacity ++ ;
									}
								}
							}
						}
					}
				}
				else if(resultResponseType!=null && (resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE.responseType()) || resultResponseType.equalsIgnoreCase(ResultResponseTypeEnum.SUBJECTIVE_NOTES.responseType()))){
					//For HB3/T400 sessions in case of subjective. Complete session information will be embedded within a single OBX.
					if(reply.getSurveyResponse()!=null && !reply.getSurveyResponse().isEmpty()) {
						resultsBatchCurrentCapacity = + reply.getSurveyResponse().size();
					}
				}
			}

			//For T400 adhoc vitals. All the T400 reported vitals which will be not part of any session will mapped against a separate OBX.
			if(reply!=null && reply.getMeterMeasurement()!=null && !reply.getMeterMeasurement().isEmpty())
			{
				for(MeterMeasurement meterMeasurement : reply.getMeterMeasurement()){
					for (AssessedVitalSign assesVitalSign : meterMeasurement.getVitalSign()){
						if (assesVitalSign != null && assesVitalSign.getId() != null){
							resultsBatchCurrentCapacity ++ ;
						}
					}
				}
			}

			logger.info("ORU Current filled capacity (as per chunk) :" + resultsBatchCurrentCapacity);
			logger.info("ORU Max capacity :" + resultsBatchMaxCapacity);

			//Compare Current and Max Batch Capacity...
			if(resultsBatchCurrentCapacity == resultsBatchMaxCapacity || resultsBatchCurrentCapacity > resultsBatchMaxCapacity){
				isMaxCapacityReached = true;
			}

			//Set it back to the batchCapacity required to compute for the next batch...
			batchCapacity.setResultsBatchCurrentCapacity(resultsBatchCurrentCapacity);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return isMaxCapacityReached;
	}

	/**
	 * Check if the maximum allowed capacity for notes is reached ...
	 * @param batchCapacity
	 * @param findNotes
	 * @return
	 */
	public static boolean isORUNotesCapacityReached(ORUBatchCapacityParameters batchCapacity, FindNotesReply findNotes)
	{
		boolean isMaxCapacityReached = false;
		Integer notesCurrentCapacity = batchCapacity.getNotesCurrentCapacity();
		Integer notesMaxCapacity = batchCapacity.getNotesMaxCapacity();
		try
		{
			//Patient/Session notes..
			if(findNotes!=null && findNotes.getNote()!=null && !findNotes.getNote().isEmpty())
			{
				for (Note note : findNotes.getNote())
				{
					//Extract notes header...
					HeaderType noteDetails = note.getHeader();
					if(noteDetails!=null && noteDetails.getId()!=null){
						notesCurrentCapacity ++ ;
					}
				}
			}

			logger.info("ORU NOTES Current filled capacity (as per chunk) :" + notesCurrentCapacity);
			logger.info("ORU NOTES Max capacity :" + notesMaxCapacity);

			//Compare Current and Max Batch Capacity...
			if(notesCurrentCapacity == notesMaxCapacity || notesCurrentCapacity > notesMaxCapacity){
				isMaxCapacityReached = true;
			}

			//Set it back to the batchCapacity required to compute for the next batch...
			batchCapacity.setNotesCurrentCapacity(notesCurrentCapacity);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return isMaxCapacityReached;
	}

	/**
	 * Maximum allowed capacity for notes. It includes both Patient and Session notes.
	 * This method gives maximum allowed capacity of notes per patient or ORU..
	 * @return
	 */
	public static Integer getORUNotesAllowedCapacity() {
		return ORUBatchSegmentLimitsEnum.getORUNotesAllowedCapacity();
	}
}
