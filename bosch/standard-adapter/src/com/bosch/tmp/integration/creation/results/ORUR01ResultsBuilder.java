package com.bosch.tmp.integration.creation.results;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hl7.v2xml.ACKCONTENT;
import org.hl7.v2xml.MSHCONTENT;
import org.hl7.v2xml.NTECONTENT;
import org.hl7.v2xml.OBRCONTENT;
import org.hl7.v2xml.OBXCONTENT;
import org.hl7.v2xml.ORCCONTENT;
import org.hl7.v2xml.ORUR01BATCHCONTENT;
import org.hl7.v2xml.ORUR01CONTENT;
import org.hl7.v2xml.ORUR01OBSERVATIONCONTENT;
import org.hl7.v2xml.ORUR01ORDEROBSERVATIONCONTENT;
import org.hl7.v2xml.ORUR01PATIENTCONTENT;
import org.hl7.v2xml.ORUR01PATIENTRESULTCONTENT;
import org.hl7.v2xml.PIDCONTENT;
import org.hl7.v2xml.QVRQ17CONTENT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosch.tmp.integration.businessObjects.results.ClinicalObservationOrder;
import com.bosch.tmp.integration.businessObjects.results.PatientResultsContent;
import com.bosch.tmp.integration.creation.CreationException;
import com.bosch.tmp.integration.util.ConfigKeyEnum;
import com.bosch.tmp.integration.util.ORUBatchResponseMessageType;
import com.bosch.tmp.integration.util.PatientResultsUtility;
import com.bosch.tmp.integration.validation.ConfigurationLoader;

/**
 * ORU R01 Response builder ...
 * @author BEA2KOR
 *
 */
public class ORUR01ResultsBuilder extends AbstractResultsBuilder
{
	public static final Logger logger = LoggerFactory.getLogger(ORUR01ResultsBuilder.class);

	public ORUR01ResultsBuilder()
	{
		// Get the following values from ConfigurationLoader.
		try
		{
			this.allowedNumberOfORUPerBatch = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.RESULTS_COUNT_PER_MESSAGE.toString()));
			this.allowedNumberOfOrderPerORU = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.NUMBER_OF_OBR_SEGMENTS.toString()));
			this.allowedNumberOfObservationPerOrder = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.NUMBER_OF_OBX_SEGMENTS.toString()));
			this.allowedNumberOfPatientNotesPerORU = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.NUMBER_OF_NTE_PER_PATIENT.toString()));
			this.allowedNumberOfSessionNotesPerOrder = Integer.parseInt(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.NUMBER_OF_NTE_PER_SESSION.toString()));
		}
		catch (Exception ex) {
			// Ignore the exceptions and process with the default values..
		}
	}

	/**
	 * Logic to build ORU_R01 based on the trigger event from input request...
		1.Build BHS.
		2.Build ORU_R01 based on the max count of ORU_R01 per batch.
			- Each ORU_R01 contains MSH,PATIENT_INFORMATION,ORDER.
			- Each ORDER contains ORC,OBR,OBX.
	 * @return
	 */
	@Override
	public Object buildMessage(Object... data) throws CreationException
	{
		Map<String,Object> resultsDataMap = (Map<String,Object>)data[0];
		Object resultsBatchResponse = null;
		try {
			if(!resultsDataMap.isEmpty())
			{
				//Set the request message from the map..
				if(resultsDataMap!=null){
					if(!resultsDataMap.isEmpty()) {
						if(resultsDataMap.containsKey("QVR_Q17Request")){
							requestMsg = (QVRQ17CONTENT)resultsDataMap.get("QVR_Q17Request");
						}
					}
				}

				//Set the Result Response type.
				resultResponseType = PatientResultsUtility.getResultResponseType((QVRQ17CONTENT)requestMsg);

				//Set the batch response type to ORU_R01 ..
				batchResponseType = ORUBatchResponseMessageType.ORU_R01;

				//Obtain the Results chunks availability flag from the request...
				if(resultsDataMap!=null){
					if(!resultsDataMap.isEmpty()) {
						if(resultsDataMap.containsKey("isMoreResultChunksAvailable")){
							isMoreResultChunksAvailable = (Boolean)resultsDataMap.get("isMoreResultChunksAvailable");
						}
					}
				}

				//Setting the limit for ORU Batch response..
				setClinicalResponseLimit();

				//Construct ORU_R01...
				resultsBatchResponse = buildORUR01BatchContent(resultsDataMap);
			}
		}
		catch (ResultsCreationException resultsCreateException) {
			logger.error("ResultsCreationException occurred in ORU Batch Result creation...", resultsCreateException);
			throw resultsCreateException ;
		}
		catch (Exception ex) {
			logger.error("Exception occurred in ORU Batch Result creation...", ex);
			throw new ResultsCreationException("Exception occurred in ORU Batch Result creation...", ex);
		}
		return resultsBatchResponse;
	}

	/**
	 * Constructs ORU_R01 Batch ..
	 * @param resultsDataMap
	 * @return
	 * @throws ResultsCreationException
	 */
	private ORUR01BATCHCONTENT buildORUR01BatchContent(Map<String,Object> resultsDataMap) throws ResultsCreationException
	{
		ORUR01BATCHCONTENT oruR01BatchResponse = new ORUR01BATCHCONTENT();
		try
		{
			boolean isResultsLastRequestBatch = false;

			//Check for negative acknowledgment if present in the results data map and construct negative ORU response and return ...
			ACKCONTENT negativeResponseAck = null;
			if(resultsDataMap!=null){
				if(!resultsDataMap.isEmpty()) {
					if(resultsDataMap.containsKey("Negative_Response_Ack")) {
						negativeResponseAck = (ACKCONTENT)resultsDataMap.get("Negative_Response_Ack");
						if(negativeResponseAck!=null){
							oruR01BatchResponse = createNegativeORUR01Response(negativeResponseAck,requestMsg);
							return oruR01BatchResponse;
						}
					}
				}
			}

			//Processing work-flow...
			if(requestMsg!=null && requestMsg instanceof QVRQ17CONTENT)
			{
				//Set the results response (PatientResultsContent) from the map..
				List<PatientResultsContent> patientResults = new ArrayList<PatientResultsContent>();
				if(resultsDataMap!=null){
					if(!resultsDataMap.isEmpty()) {
						if(resultsDataMap.containsKey("Unacked_Result_Responses")){
							patientResults = (List<PatientResultsContent>)resultsDataMap.get("Unacked_Result_Responses");
						}
					}
				}

				//Construct ORU R01 Batch ...
				if(patientResults!=null && !patientResults.isEmpty())
				{
					//Construct ORU R01 for each individual patient taking in consideration the corresponding sessions/adhoc/notes details..
					for(PatientResultsContent clinicalResult : patientResults){
						buildORUR01Content(clinicalResult,oruR01BatchResponse.getORUR01().size());
						if(isAllowedORUBatchLimitExceeded){
							break;
						}
					}

					//Iterate over ORU_R01 content list to construct ORU_R01 Batch...
					Iterator<ORUR01CONTENT> oruR01Iterator = (oruR01ContentList!=null) ? oruR01ContentList.iterator() : null;
					while (oruR01Iterator!=null && oruR01Iterator.hasNext())
					{
						oruR01BatchResponse.getORUR01().add(oruR01Iterator.next());

						//Check for the allowed ORU_R01 message per batch limit...
						if(!oruR01BatchResponse.getORUR01().isEmpty() && (oruR01BatchResponse.getORUR01().size() == allowedNumberOfORUPerBatch
								|| !oruR01Iterator.hasNext()))
						{
							//Check for last request batch ....
							if(!isMoreResultChunksAvailable && !oruR01Iterator.hasNext()){
								isResultsLastRequestBatch = true;
								logger.debug("Results last batch for the QVR_Request");
							}
							break;
						}
					}

					//Reset the ORUR01 Content for a particular patient...
					oruR01ContentList.clear();
				}
				else {
					isResultsLastRequestBatch = true;
				}

				//Adding the BHS and BTS...
				oruR01BatchResponse.setBHS(buildBatchHeader(isResultsLastRequestBatch));
				oruR01BatchResponse.setBTS(buildBTS(oruR01BatchResponse.getORUR01().size()));
			}
		}
		catch (ResultsCreationException resultsCreateException) {
			logger.error("ResultsCreationException encountered during ORU Batch Content building for multiple ORU's...", resultsCreateException);
			throw resultsCreateException ;
		}
		catch (Exception ex) {
			logger.error("Exception encountered during ORU Batch Content building for multiple ORU's...", ex);
			throw new ResultsCreationException("Runtime exception encountered during ORU Batch Content building for multiple ORU's...", ex);
		}
		return oruR01BatchResponse;
	}

	/**
	 * Construct ORU_R01 Content Lists.
	 * Each PatientResultsContent corresponds to a single patient entity. All the sessions/adhocs taken by that patient will be part of single clinicalResult entity..
	 * @param clinicalResult
	 * @param ORU_BATCH_SIZE
	 * @return
	 * @throws ResultsCreationException
	 */
	private List<ORUR01CONTENT> buildORUR01Content(PatientResultsContent clinicalResult,int ORU_BATCH_SIZE) throws ResultsCreationException
	{
		try
		{
			//Message Header ...
			int messageControlNumber = ORU_BATCH_SIZE + 1;
			MSHCONTENT messageHeader = buildMessageHeader(messageControlNumber);

			//Patient Information...
			PIDCONTENT patientIdentification = buildPatientIndentification(clinicalResult.getIntegrationPatient());

			//Construct a new ORU's with the Observation Order/Notes..
			List<ORUR01ORDEROBSERVATIONCONTENT> observationOrder = buildORUR01OrderObservationGroup(clinicalResult);
			List<NTECONTENT> patientNotes = buildPatientNotes(clinicalResult.getPatientNotes());
			if((observationOrder!=null && !observationOrder.isEmpty())
					|| (patientNotes!=null && !patientNotes.isEmpty())) {
				enrichORUR01Content(observationOrder,patientNotes,messageHeader,patientIdentification);
			}
		}
		catch (ResultsCreationException resultsCreateException) {
			logger.error("ResultsCreationException during ORU Content building for a single patient ORU Batch...", resultsCreateException);
			throw resultsCreateException ;
		}
		catch (Exception ex) {
			logger.error("Exception during ORU Content building for a single patient ORU Batch...", ex);
			throw new ResultsCreationException("Exception during ORU Content building for a single patient ORU Batch...", ex);
		}
		return oruR01ContentList;
	}

	/**
	 * Update Observation Order within a new ORUR01.
	 * @param observationOrder
	 * @param patientNotes
	 * @param messageHeader
	 * @param patientContent
	 * @throws ResultsCreationException
	 */
	private void enrichORUR01Content(List<ORUR01ORDEROBSERVATIONCONTENT> observationOrder,List<NTECONTENT> patientNotes, MSHCONTENT messageHeader,PIDCONTENT patientContent) throws ResultsCreationException
	{
		try
		{
			//Iterators for clinical observations and clinical notes...
			Iterator<ORUR01ORDEROBSERVATIONCONTENT> orderIterator = (observationOrder!=null) ? observationOrder.iterator() : null;
			Iterator<NTECONTENT> notesIterator = (patientNotes!=null) ? patientNotes.iterator() : null;

			//Clinical Observations and Notes flag set based on the corresponding iterator..
			boolean isClinicalOrderExists = (orderIterator!=null) ? orderIterator.hasNext() : false;
			boolean isPatientNotesExists = (notesIterator!=null) ? notesIterator.hasNext() : false;

			//Processing logic...
			while (isClinicalOrderExists || isPatientNotesExists)
			{
				//Construct allowed number of Observation order group...
				List<ORUR01ORDEROBSERVATIONCONTENT> observationOrderPerORUList = new ArrayList<ORUR01ORDEROBSERVATIONCONTENT>();
				while(orderIterator!=null && orderIterator.hasNext())
				{
					observationOrderPerORUList.add(orderIterator.next());

					//Construct a new ORU_R01 message. Check for allowed OBR (ORU_ORDER) per ORU_R01 message...
					if(!observationOrderPerORUList.isEmpty() && (observationOrderPerORUList.size() == allowedNumberOfOrderPerORU || !orderIterator.hasNext()))
					{
						if(!orderIterator.hasNext()){
							isClinicalOrderExists = false;
						}
						break;
					}
				}

				//Construct allowed number of Patient notes...
				List<NTECONTENT> notesPerORUList = new ArrayList<NTECONTENT>();
				while(notesIterator!=null && notesIterator.hasNext())
				{
					notesPerORUList.add(notesIterator.next());
					if(!notesPerORUList.isEmpty() && (notesPerORUList.size() == allowedNumberOfPatientNotesPerORU || !notesIterator.hasNext()))
					{
						if(!notesIterator.hasNext()){
							isPatientNotesExists = false;
						}
						break;
					}
				}

				//Add the patient notes and clinical order to the ORUR01CONTENT..
				if(!observationOrderPerORUList.isEmpty() || !notesPerORUList.isEmpty())
				{
					//Construct a new instance of ORUR01CONTENT..
					ORUR01CONTENT oruR01ContentObj = new ORUR01CONTENT();
					oruR01ContentObj.setMSH(messageHeader);
					oruR01ContentObj.getORUR01PATIENTRESULT().add(buildORUR01PatientResultContent(patientContent,notesPerORUList,observationOrderPerORUList));

					//Add to the final ORU R01 list..
					oruR01ContentList.add(oruR01ContentObj);

					//Reset the reference list..
					observationOrderPerORUList.clear();
					notesPerORUList.clear();
				}

				//Check for the allowed ORU_R01 message per batch limit (Only greater than should be checked)...
				if(!oruR01ContentList.isEmpty() && oruR01ContentList.size() > allowedNumberOfORUPerBatch){
					isAllowedORUBatchLimitExceeded = true;
					break;
				}
			}
		}
		catch (Exception ex) {
			logger.error("Exception encountered during ORU enrichment with the other result content for an ORU...", ex);
			throw new ResultsCreationException("Exception encountered during ORU enrichment with the other result content for an ORU...", ex);
		}
	}

	/**
	 * Construct ORDER (Order Group) Content for ORU_R01.
	 * @param clinicalResult
	 * @return
	 * @throws ResultsCreationException
	 */
	private List<ORUR01ORDEROBSERVATIONCONTENT> buildORUR01OrderObservationGroup(PatientResultsContent clinicalResult) throws ResultsCreationException
	{
		//Data Variables..
		List<ORUR01ORDEROBSERVATIONCONTENT> oruR01OrderContentList = new ArrayList<ORUR01ORDEROBSERVATIONCONTENT>();

		try {
			//Clinical Order/Observation details...
			ORCCONTENT commonOrder = buildCommonOrder();
			List<ClinicalObservationOrder> clinicalObservationOrderList = buildClinicalObservationOrderList(clinicalResult);

			for(ClinicalObservationOrder clinicalOrder : clinicalObservationOrderList)
			{
				//Extract Observation request information...
				OBRCONTENT observationRequest = clinicalOrder.getObservationRequest();

				//Iterators for clinical observations and clinical notes...
				Iterator<OBXCONTENT> observationIterator = clinicalOrder.getClinicalObservations().iterator();
				Iterator<NTECONTENT> notesIterator = clinicalOrder.getClinicalNotes().iterator();

				//Clinical Observations and Notes flag set based on the corresponding iterator..
				boolean isClinicalObservationExists = observationIterator.hasNext();
				boolean isClinicalNotesExists = notesIterator.hasNext();

				//Processing logic...
				while (isClinicalObservationExists || isClinicalNotesExists)
				{
					//Construct allowed number of Clinical Observations...
					List<ORUR01OBSERVATIONCONTENT> oruR01ResultsContentList = new ArrayList<ORUR01OBSERVATIONCONTENT>();
					while(observationIterator.hasNext())
					{
						ORUR01OBSERVATIONCONTENT resultInfo = new ORUR01OBSERVATIONCONTENT();
						resultInfo.setOBX(observationIterator.next());
						oruR01ResultsContentList.add(resultInfo);

						//Construct a new ORU ORDER List. Each ORDER contains a list of observation request (OBX) as per the allowed capacity associated with an OBR and ORC...
						if(!oruR01ResultsContentList.isEmpty() && (oruR01ResultsContentList.size() == allowedNumberOfObservationPerOrder
								|| !observationIterator.hasNext()))
						{
							if(!observationIterator.hasNext()){
								isClinicalObservationExists = false;
							}
							break;
						}
					}

					//Construct allowed number of Session notes...
					List<NTECONTENT> notesPerORDERList = new ArrayList<NTECONTENT>();
					while(notesIterator.hasNext())
					{
						notesPerORDERList.add(notesIterator.next());
						if(!notesPerORDERList.isEmpty() && (notesPerORDERList.size() == allowedNumberOfSessionNotesPerOrder
								|| !notesIterator.hasNext()))
						{
							if(!notesIterator.hasNext()){
								isClinicalNotesExists = false;
							}
							break;
						}
					}

					//Add the clinical session notes and clinical observations to the ORUR01ORDEROBSERVATIONCONTENT..
					if(!oruR01ResultsContentList.isEmpty() || !notesPerORDERList.isEmpty())
					{
						//Construct a new instance of ORUR01ORDEROBSERVATIONCONTENT..
						ORUR01ORDEROBSERVATIONCONTENT oruR01OrderContentObj = new ORUR01ORDEROBSERVATIONCONTENT();
						oruR01OrderContentObj.setORC(commonOrder);
						oruR01OrderContentObj.setOBR(observationRequest);

						//Enrich ORUR01ORDEROBSERVATIONCONTENT with Clinical Observations ...
						if(!oruR01ResultsContentList.isEmpty()) {
							oruR01OrderContentObj.getORUR01OBSERVATION().addAll(oruR01ResultsContentList);
							oruR01ResultsContentList.clear();
						}

						//Enrich ORUR01ORDEROBSERVATIONCONTENT with Clinical Session notes...
						if(!notesPerORDERList.isEmpty()) {
							oruR01OrderContentObj.getNTE().addAll(notesPerORDERList);
							notesPerORDERList.clear();
						}

						oruR01OrderContentList.add(oruR01OrderContentObj);
					}
				}
			}
		}
		catch (ResultsCreationException resultsCreateException) {
			logger.error("ResultsCreationException during ORU ORDER Content building for a single ORU...", resultsCreateException);
			throw resultsCreateException ;
		}
		catch (Exception ex) {
			logger.error("Exception encountered during ORU ORDER Content building for a single ORU...", ex);
			throw new ResultsCreationException("Exception encountered during ORU ORDER Content building for a single ORU...", ex);
		}
		return oruR01OrderContentList;
	}

	/**
	 * Constructs a new Patient Result Content for ORU_R01 message.
	 * @param patientInfo
	 * @param notesPerORUList
	 * @param observationOrderList
	 * @return
	 */
	private ORUR01PATIENTRESULTCONTENT buildORUR01PatientResultContent(PIDCONTENT patientInfo, List<NTECONTENT> notesPerORUList, List<ORUR01ORDEROBSERVATIONCONTENT> observationOrderList)
	{
		//Build ORURO1 Patient Content along with Notes if present...
		ORUR01PATIENTCONTENT patientContent = new ORUR01PATIENTCONTENT();
		patientContent.setPID(patientInfo);
		if(notesPerORUList!=null) {
			patientContent.getNTE().addAll(notesPerORUList);
		}

		//Build ORUR01 PATIENT Result Content .....
		ORUR01PATIENTRESULTCONTENT patientResultContent = new ORUR01PATIENTRESULTCONTENT();
		if(observationOrderList!=null) {
			patientResultContent.getORUR01ORDEROBSERVATION().addAll(observationOrderList);
		}
		patientResultContent.setORUR01PATIENT(patientContent);

		return patientResultContent;
	}

	/**
	 * Create negative acknowledgment for ORU_R01 in case of processing error.
	 * @param negativeResponseAck
	 * @param requestMsg
	 * @return
	 */
	private ORUR01BATCHCONTENT createNegativeORUR01Response(ACKCONTENT negativeResponseAck,Object requestMsg) {
		ORUR01BATCHCONTENT oruR01BatchResponse = new ORUR01BATCHCONTENT();
		if(requestMsg!=null && requestMsg instanceof QVRQ17CONTENT){
			oruR01BatchResponse.setBHS(buildBatchHeader(false));
			oruR01BatchResponse.setBTS(buildBTS(oruR01BatchResponse.getORUR01().size()));
			oruR01BatchResponse.getACK().add(negativeResponseAck);
		}
		return oruR01BatchResponse;
	}

	private Boolean isMoreResultChunksAvailable = Boolean.FALSE;
	private Boolean isAllowedORUBatchLimitExceeded = Boolean.FALSE;
	private List<ORUR01CONTENT> oruR01ContentList = new ArrayList<ORUR01CONTENT>();
}
