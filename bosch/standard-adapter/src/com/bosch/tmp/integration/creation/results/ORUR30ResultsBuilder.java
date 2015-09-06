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
import org.hl7.v2xml.ORUR30BATCHCONTENT;
import org.hl7.v2xml.ORUR30CONTENT;
import org.hl7.v2xml.ORUR30ORDERCONTENT;
import org.hl7.v2xml.ORUR30PATIENTINFORMATIONCONTENT;
import org.hl7.v2xml.ORUR30RESULTSCONTENT;
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
 * ORU R30 Response builder ...
 * @author BEA2KOR
 *
 */
public class ORUR30ResultsBuilder extends AbstractResultsBuilder
{
	public static final Logger logger = LoggerFactory.getLogger(ORUR30ResultsBuilder.class);

	public ORUR30ResultsBuilder()
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
	 * Logic to build ORU_R30 based on the trigger event from input request...
		1.Build BHS.
		2.Build ORU_R30 based on the max count of ORU_R30 per batch.
			- Each ORU_R30 contains MSH,PATIENT_INFORMATION,ORDER.
			- Each ORDER contains ORC,OBR,OBX.
	 */
	@Override
	public Object buildMessage(Object... data) throws CreationException
	{
		Map<String,Object> resultsDataMap = (Map<String,Object>)data[0];
		Object resultsBatchResponse = null;
		try
		{
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

				//Set the batch response type to ORU_R30 ..
				batchResponseType = ORUBatchResponseMessageType.ORU_R30;

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

				//Construct ORU_R30...
				resultsBatchResponse = buildORUR30BatchContent(resultsDataMap);
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
	 * Constructs ORU_R30 Batch ..
	 * @param resultsDataMap
	 * @return
	 * @throws ResultsCreationException
	 */
	private ORUR30BATCHCONTENT buildORUR30BatchContent(Map<String,Object> resultsDataMap) throws ResultsCreationException
	{
		ORUR30BATCHCONTENT oruR30BatchResponse = new ORUR30BATCHCONTENT();;
		try
		{
			boolean isResultsLastRequestBatch = false;

			//Check for negative acknowledgment if present in the results data map and construct negative ORU response and return ...
			ACKCONTENT negativeResponseAck = null;
			if(resultsDataMap!=null){
				if(!resultsDataMap.isEmpty()) {
					if(resultsDataMap.containsKey("Negative_Response_Ack")){
						negativeResponseAck = (ACKCONTENT)resultsDataMap.get("Negative_Response_Ack");
						if(negativeResponseAck!=null){
							oruR30BatchResponse = createNegativeORUR30Response(negativeResponseAck,requestMsg);
							return oruR30BatchResponse;
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

				//Construct ORU R30 Batch...
				if(patientResults!=null && !patientResults.isEmpty())
				{
					//Construct ORU R30 for each individual patient taking in consideration the corresponding sessions/adhoc/notes details..
					for(PatientResultsContent clinicalResult : patientResults){
						buildORUR30Content(clinicalResult,oruR30BatchResponse.getORUR30().size());
						if(isAllowedORUBatchLimitExceeded){
							break;
						}
					}

					//Iterate over ORU_R30 content list to construct ORU_R30 Batch...
					Iterator<ORUR30CONTENT> oruR30Iterator = (oruR30ContentList!=null) ? oruR30ContentList.iterator() : null;
					while (oruR30Iterator!=null && oruR30Iterator.hasNext())
					{
						oruR30BatchResponse.getORUR30().add(oruR30Iterator.next());

						//Check for the allowed ORU_R30 message per batch limit...
						if(!oruR30BatchResponse.getORUR30().isEmpty() && (oruR30BatchResponse.getORUR30().size() == allowedNumberOfORUPerBatch
								|| !oruR30Iterator.hasNext()))
						{
							//Check for last request batch ....
							if(!isMoreResultChunksAvailable && !oruR30Iterator.hasNext()){
								isResultsLastRequestBatch = true;
								logger.debug("Results last batch for the QVR_Request");
							}
							break;
						}
					}

					//Reset the ORUR30 Content for a particular patient...
					oruR30ContentList.clear();
				}
				else {
					isResultsLastRequestBatch = true;
				}

				//Adding the BHS and BTS...
				oruR30BatchResponse.setBHS(buildBatchHeader(isResultsLastRequestBatch));
				oruR30BatchResponse.setBTS(buildBTS(oruR30BatchResponse.getORUR30().size()));
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
		return oruR30BatchResponse;
	}

	/**
	 * Construct ORU_R30 Content Lists.
	 * Each PatientResultsContent corresponds to a single patient entity. All the sessions/adhocs taken by that patient will be part of single clinicalResult entity..
	 * @param clinicalResult
	 * @param ORU_BATCH_SIZE
	 * @return
	 * @throws ResultsCreationException
	 */
	private List<ORUR30CONTENT> buildORUR30Content(PatientResultsContent clinicalResult,int ORU_BATCH_SIZE) throws ResultsCreationException
	{
		try
		{
			//Message Header ...
			int messageControlNumber = ORU_BATCH_SIZE + 1;
			MSHCONTENT messageHeader = buildMessageHeader(messageControlNumber);

			//Patient Information...
			PIDCONTENT patientIdentification = buildPatientIndentification(clinicalResult.getIntegrationPatient());
			ORUR30PATIENTINFORMATIONCONTENT patientIdentificationContent = buildORUR30PatientIdentificationContent(patientIdentification);

			//Construct a new ORU's with the Observation Order/Notes..
			List<ORUR30ORDERCONTENT> observationOrder = buildORUR30OrderGroup(clinicalResult);
			List<NTECONTENT> patientNotes = buildPatientNotes(clinicalResult.getPatientNotes());
			if((observationOrder!=null && !observationOrder.isEmpty())
					|| (patientNotes!=null && !patientNotes.isEmpty())) {
				enrichORUR30Content(observationOrder,patientNotes,messageHeader,patientIdentificationContent);
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
		return oruR30ContentList;
	}

	/**
	 * Update Observation Order and Patient Notes within a new ORUR30
	 * @param observationOrder
	 * @param patientNotes
	 * @param messageHeader
	 * @param patientIdentificationContent
	 * @throws ResultsCreationException
	 */
	private void enrichORUR30Content(List<ORUR30ORDERCONTENT> observationOrder,List<NTECONTENT> patientNotes,MSHCONTENT messageHeader,ORUR30PATIENTINFORMATIONCONTENT patientIdentificationContent) throws ResultsCreationException
	{
		try
		{
			//Iterators for clinical observations and clinical notes...
			Iterator<ORUR30ORDERCONTENT> orderIterator = (observationOrder!=null) ? observationOrder.iterator() : null;
			Iterator<NTECONTENT> notesIterator = (patientNotes!=null) ? patientNotes.iterator() : null;

			//Clinical Observations and Notes flag set based on the corresponding iterator..
			boolean isClinicalOrderExists = (orderIterator!=null) ? orderIterator.hasNext() : false;
			boolean isPatientNotesExists = (notesIterator!=null) ? notesIterator.hasNext() : false;

			//Processing logic...
			while (isClinicalOrderExists || isPatientNotesExists)
			{
				//Construct allowed number of Observation order group...
				List<ORUR30ORDERCONTENT> observationOrderPerORUList = new ArrayList<ORUR30ORDERCONTENT>();
				while(orderIterator!=null && orderIterator.hasNext())
				{
					observationOrderPerORUList.add(orderIterator.next());
					if(!observationOrderPerORUList.isEmpty() && (observationOrderPerORUList.size() == allowedNumberOfOrderPerORU
							|| !orderIterator.hasNext()))
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
					if(!notesPerORUList.isEmpty() && (notesPerORUList.size() == allowedNumberOfPatientNotesPerORU
							|| !notesIterator.hasNext()))
					{
						if(!notesIterator.hasNext()){
							isPatientNotesExists = false;
						}
						break;
					}
				}

				//Add the patient notes and clinical order to the ORUR30CONTENT..
				if(!observationOrderPerORUList.isEmpty() || !notesPerORUList.isEmpty())
				{
					//Construct a new instance of ORUR30CONTENT..
					ORUR30CONTENT oruR30ContentObj = new ORUR30CONTENT();
					oruR30ContentObj.setMSH(messageHeader);
					oruR30ContentObj.setORUR30PATIENTINFORMATION(patientIdentificationContent);

					//Enrich ORUR30CONTENT with Observation Order...
					if(!observationOrderPerORUList.isEmpty()) {
						oruR30ContentObj.getORUR30ORDER().addAll(observationOrderPerORUList);
						observationOrderPerORUList.clear();
					}

					//Enrich ORUR30CONTENT with Patient notes...
					if(!notesPerORUList.isEmpty()){
						oruR30ContentObj.getNTE().addAll(notesPerORUList);
						notesPerORUList.clear();
					}

					oruR30ContentList.add(oruR30ContentObj);
				}

				//Check for the allowed ORU_R30 message per batch limit (Only greater than should be checked)...
				if(!oruR30ContentList.isEmpty() && oruR30ContentList.size() > allowedNumberOfORUPerBatch){
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
	 * Construct ORDER (Order Group) Content for ORU_R30.
	 * @param clinicalResult
	 * @return
	 * @throws ResultsCreationException
	 */
	private List<ORUR30ORDERCONTENT> buildORUR30OrderGroup(PatientResultsContent clinicalResult) throws ResultsCreationException
	{
		List<ORUR30ORDERCONTENT> oruR30OrderContentList = new ArrayList<ORUR30ORDERCONTENT>();
		try
		{
			//Clinical ObservationOrder details...
			ORCCONTENT commonOrder = buildCommonOrder();
			List<ClinicalObservationOrder> clinicalObservationOrderList = buildClinicalObservationOrderList(clinicalResult);

			//Construct ORUORDERCONTENT..
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
					List<ORUR30RESULTSCONTENT> oruR30ResultsContentList = new ArrayList<ORUR30RESULTSCONTENT>();
					while(observationIterator.hasNext())
					{
						ORUR30RESULTSCONTENT resultInfo = new ORUR30RESULTSCONTENT();
						resultInfo.setOBX(observationIterator.next());
						oruR30ResultsContentList.add(resultInfo);

						//Construct a new ORU ORDER List. Each ORDER contains a list of observation request (OBX) as per the allowed capacity associated with an OBR and ORC...
						if(!oruR30ResultsContentList.isEmpty() && (oruR30ResultsContentList.size() == allowedNumberOfObservationPerOrder
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

					//Add the clinical session notes and clinical observations to the ORUR30ORDERCONTENT..
					if(!oruR30ResultsContentList.isEmpty() || !notesPerORDERList.isEmpty())
					{
						//Construct a new instance of ORUR30ORDERCONTENT..
						ORUR30ORDERCONTENT oruR30OrderContentObj = new ORUR30ORDERCONTENT();
						oruR30OrderContentObj.setORC(commonOrder);
						oruR30OrderContentObj.setOBR(observationRequest);

						//Enrich ORUR30ORDERCONTENT with Clinical Observations ...
						if(!oruR30ResultsContentList.isEmpty()) {
							oruR30OrderContentObj.getORUR30RESULTS().addAll(oruR30ResultsContentList);
							oruR30ResultsContentList.clear();
						}

						//Enrich ORUR30ORDERCONTENT with Clinical Session notes...
						if(!notesPerORDERList.isEmpty()) {
							oruR30OrderContentObj.getNTE().addAll(notesPerORDERList);
							notesPerORDERList.clear();
						}

						oruR30OrderContentList.add(oruR30OrderContentObj);
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
		return oruR30OrderContentList;
	}

	/**
	 * Construct ORUR30PATIENTINFORMATIONCONTENT for the each PID..
	 * @param patientIdentification
	 * @return
	 */
	private ORUR30PATIENTINFORMATIONCONTENT buildORUR30PatientIdentificationContent(PIDCONTENT patientIdentification)
	{
		ORUR30PATIENTINFORMATIONCONTENT patientIdentificationContent = new ORUR30PATIENTINFORMATIONCONTENT();
		patientIdentificationContent.setPID(patientIdentification);
		return patientIdentificationContent;
	}

	/**
	 * Create negative acknowledgment for ORU_R30 in case of processing error..
	 * @param negativeResponseAck
	 * @param requestMsg
	 * @return
	 */
	private ORUR30BATCHCONTENT createNegativeORUR30Response(ACKCONTENT negativeResponseAck,Object requestMsg) {
		ORUR30BATCHCONTENT oruR30BatchResponse = new ORUR30BATCHCONTENT();
		if(requestMsg!=null && requestMsg instanceof QVRQ17CONTENT){
			oruR30BatchResponse.setBHS(buildBatchHeader(false));
			oruR30BatchResponse.setBTS(buildBTS(oruR30BatchResponse.getORUR30().size()));
			oruR30BatchResponse.getACK().add(negativeResponseAck);
		}
		return oruR30BatchResponse;
	}

	private Boolean isMoreResultChunksAvailable = Boolean.FALSE;
	private Boolean isAllowedORUBatchLimitExceeded = Boolean.FALSE;
	private List<ORUR30CONTENT> oruR30ContentList = new ArrayList<ORUR30CONTENT>();
}
