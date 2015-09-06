package com.bosch.tmp.integration.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.th.integration.PushResults;
import com.bosch.th.patientrecord.SurveyResponse;
import com.bosch.tmp.integration.dao.SessionResultDao;
import com.bosch.tmp.integration.entities.SessionResult;
import com.bosch.tmp.integration.util.ConfigKeyEnum;
import com.bosch.tmp.integration.util.MessageUtils;
import com.bosch.tmp.integration.util.Utils;
import com.bosch.tmp.integration.validation.ConfigurationLoader;

/**
 * @author BEA2KOR
 *
 */
@Service
public class SessionResultPersister
{
	public static final Logger logger = LoggerFactory.getLogger(SessionResultPersister.class);

	@Autowired
	private SessionResultDao sessionResultDao;

	public SessionResultPersister(){
	}

	/**
	 * Persist Session results into CDA..
	 * @param pushResults
	 * @return
	 */
	public List<SessionResult> persist(PushResults pushResults)
	{
		logger.info("Persisting Session details...");

		//Null check..
		if (pushResults == null) {
			logger.debug("Session to be persisted is null.");
			return null;
		}

		//SessionResult Persistence...
		List<SessionResult> sessionResults = null;
		try {
			sessionResults = createSessionResults(pushResults);
			for (SessionResult session : sessionResults){
				sessionResultDao.save(session);
			}
		}
		catch (Exception e) {
			logger.error("Exception occurs when persisting results: ", e);
			return sessionResults;
		}

		logger.info("Persistence done.");
		return sessionResults;
	}

	/**
	 * Persist the session information from the PushResults ( Result service response ) to
	 * SESSION_RESULT table ...
	 * @param pushResults
	 * @return
	 */
	private List<SessionResult> createSessionResults(PushResults pushResults)
	{
		List<SessionResult> sessionResults = new ArrayList<SessionResult>();

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
			return sessionResults;
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
			return sessionResults;
		}

		//Extract the session information..
		List<SurveyResponse> surveyResponse = pushResults.getSurveyResponse();
		if (surveyResponse == null || surveyResponse.size() == 0){
			logger.debug("No Survey Response found in pushed results.");
		}

		//Create SessionResult entity instance for ever session in the survey response...
		for (SurveyResponse clinicalSession : surveyResponse) {
			Long sessionId = clinicalSession.getId();
			SessionResult sessionResult = new SessionResult();
			sessionResult.setSessionId(sessionId);
			sessionResult.setPatientId(patientId);
			sessionResult.setIsAcknowledged(Boolean.FALSE);
			sessionResult.setOrganizationId(organizationId);
			sessionResult.setResponseTimestamp(Utils.convertXMLGregorianCalendarToDate(clinicalSession.getResponseDate()));
			sessionResult.setRecievedTimestamp(Utils.convertXMLGregorianCalendarToDate(clinicalSession.getReceivedDate()));
			sessionResults.add(sessionResult);
		}

		return sessionResults;
	}

	/**
	 * Method to persist acknowledgment info for a SessionResult in the DB.
	 * @param sessionResults
	 */
	public void persist(List<SessionResult> sessionResults) {
		for (SessionResult sessionResult : sessionResults) {
			logger.debug("Persisting acknowledgment information for a Session Result with ID: " + sessionResult.getSessionId());
			sessionResult.setIsAcknowledged(Boolean.TRUE);
			sessionResultDao.update(sessionResult);
		}
	}
}
