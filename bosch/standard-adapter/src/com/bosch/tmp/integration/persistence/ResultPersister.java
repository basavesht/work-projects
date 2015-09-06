package com.bosch.tmp.integration.persistence;

import com.bosch.th.integration.PushResults;
import com.bosch.th.patientrecord.AssessedVitalSign;
import com.bosch.th.patientrecord.MeterMeasurement;
import com.bosch.th.patientrecord.QuestionResponse;
import com.bosch.th.patientrecord.SurveyResponse;
import com.bosch.th.patientrecord.VitalSignValue;
import com.bosch.tmp.integration.dao.ResultDao;
import com.bosch.tmp.integration.entities.Result;
import com.bosch.tmp.integration.util.ConfigKeyEnum;
import com.bosch.tmp.integration.util.MessageUtils;
import com.bosch.tmp.integration.util.ResultTypeEnum;
import com.bosch.tmp.integration.util.Utils;
import com.bosch.tmp.integration.validation.ConfigurationLoader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used as a helper to persist Results. It contains business logic to map the Results data structure into
 * the representative Result class instances, which in turn will be persisted into database.
 *
 * @author BEA2KOR
 */
@Service
public class ResultPersister
{
	public static final Logger logger = LoggerFactory.getLogger(ResultPersister.class);

	@Autowired
	private ResultDao resultDao;

	public ResultPersister(){

	}

	/**
	 * The method to either create or update a list of Result entries in the database.
	 * @param pushResults The results from CIA to persist
	 * @return The persisted list of Results
	 */
	public List<Result> persist(PushResults pushResults)
	{
		logger.info("Persisting Results...");

		if (pushResults == null){
			logger.debug("Results to be persisted is null.");
			return null;
		}

		List<Result> results = null;
		try {
			results = createResults(pushResults);
			for (Result result : results) {
				resultDao.save(result);
			}
		}
		catch (Exception e){
			logger.error("Exception occurs when persisting results: ", e);
			return results;
		}

		logger.info("Persistence done.");
		return results;
	}

	/**
	 * The method to convert Results data structure to a representative list of Result objects
	 * to be persisted into database.
	 * @param pushResults The results to convert
	 * @return The list of Result representations of the pushResults
	 * @throws Exception Any exception thrown during conversion
	 */
	private List<Result> createResults(PushResults pushResults) throws Exception
	{
		List<Result> results = new ArrayList<Result>();

		//Extract the patient Id..
		String patientId = null;
		try
		{
			String patId = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.PAT_ID.toString());
			patientId = MessageUtils.getMessagePatIdentifier(pushResults.getIntegrationPatient().getIdentifier().getExternalIdentifier(),patId);
			if(patientId ==  null){
				throw new Exception("Patient Id is null..");
			}
		}
		catch (Exception e) {
			logger.error("Patient ID can't be retrieved from pushed results. Ignoring sessionResults.");
			return results;
		}

		//Extract the organization Id...
		String organizationId = null;
		try {
			organizationId = pushResults.getIntegrationPatient().getInternalOrganizationId();
			if (organizationId == null) {
				throw new Exception("Organization Id is null...");
			}
		} catch (Exception e) {
			logger.error("Organization ID can't be retrieved from pushed results. Ignoring sessionResults.");
			return results;
		}

		//T400 reported vitals..
		List<MeterMeasurement> adhocMeterMeasurements = pushResults.getMeterMeasurement();
		if (adhocMeterMeasurements == null || adhocMeterMeasurements.size() == 0){
			logger.debug("No adhocMeterMeasurements in pushed results.");
		}

		//Create Result entity instance for ever meter measurement vital ....
		for (MeterMeasurement measurement : adhocMeterMeasurements){
			for (AssessedVitalSign assessedVitalSign : measurement.getVitalSign()){
				if(assessedVitalSign!=null && assessedVitalSign.getId()!=null){
					for (VitalSignValue vitalSignValue : assessedVitalSign.getNumericValue())
					{
						Long resultId = measurement.getId();
						Result result = new Result();
						result.setResultId(resultId);
						result.setPatientId(patientId);
						result.setIsAcknowledged(Boolean.FALSE);
						result.setOrganizationId(organizationId);
						result.setSessionId(null);
						result.setResponseTimestamp(Utils.convertXMLGregorianCalendarToDate(measurement.getCollectionTime()));
						result.setLoincCode(vitalSignValue.getLoincCode());
						result.setProgramName(null);
						result.setType(ResultTypeEnum.VITAL_SIGN.toString());
						result.setVitalSignId(vitalSignValue.getId());
						results.add(result);
					}
				}
			}
		}

		//HB3 reported vitals..
		List<SurveyResponse> sessions = pushResults.getSurveyResponse();
		if (sessions == null || sessions.size() == 0){
			logger.debug("No sessions in pushed results.");
		}

		//Create Result entity instance for ever session vital in the survey response...
		for (SurveyResponse session : sessions)
		{
			Long sessionId = session.getId();
			List<QuestionResponse> responses = session.getQuestionResponse();
			if (responses == null || responses.size() == 0){
				logger.debug("No question responses in session " + sessionId + ". Ignoring results.");
				continue;
			}
			for (QuestionResponse response : responses){
				List<MeterMeasurement> measurements = response.getMeterMeasurement();
				if (measurements == null || measurements.size() == 0){
					logger.debug("No meter measurements in question response " + response.getResponse() +". Ignoring results.");
					continue;
				}
				for (MeterMeasurement measurement : measurements){
					for (AssessedVitalSign assessedVitalSign : measurement.getVitalSign()){
						if(assessedVitalSign!=null && assessedVitalSign.getId()!=null){
							for (VitalSignValue vitalSignValue : assessedVitalSign.getNumericValue())
							{
								Result result = new Result();
								result.setResultId(measurement.getId());
								result.setPatientId(patientId);
								result.setIsAcknowledged(Boolean.FALSE);
								result.setOrganizationId(organizationId);
								result.setSessionId(sessionId);
								result.setResponseTimestamp(Utils.convertXMLGregorianCalendarToDate(session.getResponseDate()));
								result.setLoincCode(vitalSignValue.getLoincCode());
								result.setProgramName(session.getProgramName());
								result.setType(ResultTypeEnum.SESSION_VITAL_SIGN.toString());
								result.setVitalSignId(vitalSignValue.getId());
								results.add(result);
							}
						}
					}
				}
			}
		}
		return results;
	}

	/**
	 * Method to persist acknowledgment info for a result in the DB.
	 * @param results
	 */
	public void persist(List<Result> results)
	{
		for (Result result : results){
			logger.debug("Persisting acknowledgment information for a result with ID: " + result.getResultId());
			result.setIsAcknowledged(Boolean.TRUE);
			resultDao.update(result);
		}
	}
}
