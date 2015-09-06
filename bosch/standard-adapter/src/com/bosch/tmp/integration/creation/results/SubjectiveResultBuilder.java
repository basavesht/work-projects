package com.bosch.tmp.integration.creation.results;

import java.io.StringWriter;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.hl7.v2xml.Assembly;
import org.hl7.v2xml.CE1CONTENT;
import org.hl7.v2xml.CE2CONTENT;
import org.hl7.v2xml.CE3CONTENT;
import org.hl7.v2xml.ClinicalCoding;
import org.hl7.v2xml.CollectionType;
import org.hl7.v2xml.DeviceManufacturer;
import org.hl7.v2xml.DeviceModel;
import org.hl7.v2xml.DeviceSerialNumber;
import org.hl7.v2xml.Header;
import org.hl7.v2xml.InValidID;
import org.hl7.v2xml.Mode;
import org.hl7.v2xml.MultipleQuestionResponseID;
import org.hl7.v2xml.ObjectFactory;
import org.hl7.v2xml.ObservationResultStatus;
import org.hl7.v2xml.PatientTimeZone;
import org.hl7.v2xml.Program;
import org.hl7.v2xml.ProgramID;
import org.hl7.v2xml.ProgramName;
import org.hl7.v2xml.Question;
import org.hl7.v2xml.QuestionAspect;
import org.hl7.v2xml.QuestionCategory;
import org.hl7.v2xml.QuestionID;
import org.hl7.v2xml.QuestionText;
import org.hl7.v2xml.QuestionType;
import org.hl7.v2xml.QuestionVersion;
import org.hl7.v2xml.Response;
import org.hl7.v2xml.ResponseID;
import org.hl7.v2xml.ResponseRisk;
import org.hl7.v2xml.ResponseSequenceNumber;
import org.hl7.v2xml.ResponseTime;
import org.hl7.v2xml.ResponseValue;
import org.hl7.v2xml.Risk;
import org.hl7.v2xml.RiskSummary;
import org.hl7.v2xml.RiskSummaryCategory;
import org.hl7.v2xml.RiskSummaryRisk;
import org.hl7.v2xml.RiskValue;
import org.hl7.v2xml.Session;
import org.hl7.v2xml.SessionID;
import org.hl7.v2xml.SessionName;
import org.hl7.v2xml.SessionReceivedDate;
import org.hl7.v2xml.SessionResponse;
import org.hl7.v2xml.SessionResponseDate;
import org.hl7.v2xml.SessionSentDate;
import org.hl7.v2xml.Survey;
import org.hl7.v2xml.SurveyGroupName;
import org.hl7.v2xml.SurveyID;
import org.hl7.v2xml.SurveyInterface;
import org.hl7.v2xml.SurveyName;
import org.hl7.v2xml.SurveySentDate;
import org.hl7.v2xml.SurveyVersion;
import org.hl7.v2xml.TS1CONTENT;
import org.hl7.v2xml.TimeZone;
import org.hl7.v2xml.UnitOfMeasure;
import org.hl7.v2xml.UnitOfMeasureType;
import org.hl7.v2xml.VID1CONTENT;
import org.hl7.v2xml.Value;
import org.hl7.v2xml.VitalSignID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosch.th.patientrecord.AssessedVitalSign;
import com.bosch.th.patientrecord.MeterMeasurement;
import com.bosch.th.patientrecord.QuestionResponse;
import com.bosch.th.patientrecord.SurveyResponse;
import com.bosch.th.patientrecord.VitalSignValue;
import com.bosch.tmp.integration.util.ConfigKeyEnum;
import com.bosch.tmp.integration.util.Utils;
import com.bosch.tmp.integration.validation.ConfigurationLoader;

/**
 * This class constructs the subjective results which will be put in the OBX.5
 *
 * @author pab3kor
 *
 */

public class SubjectiveResultBuilder {

	public static final Logger logger = LoggerFactory
			.getLogger(SubjectiveResultBuilder.class);

	/**
	 * Holds the JAXB context corresponding to each HL7 message.
	 * It behaves like a cache for storing HL7 message JAXB context.
	 * JAXB Context will be initialized only once and the instance will be put back into cache.
	 */
	private static Map<String,JAXBContext> messageContext = new HashMap<String,JAXBContext>();

	/**
	 * This method gets the Assembly object and marshals it, which will be put inside the OBX.5
	 */
	public String buildSubjectiveResults(SurveyResponse surveyResponse) throws JAXBException {

		String key = "SUBJ_ASSEMBLY";
		//get the assembly object
		Assembly assembly = buildSubjectiveAssembly(surveyResponse);

		ObjectFactory objFactory = new ObjectFactory();
		StringWriter marshalledString = new StringWriter();
		Marshaller objMarshaller = null;

		try {
			JAXBContext context = null;
			if(messageContext!=null && messageContext.containsKey(key)) {
				context = messageContext.get(key);
			}
			else {
				context = JAXBContext.newInstance(objFactory.createAssembly()
						.getClass().getPackage().getName());
				if(context!=null){
					logger.debug("Creating a new " + key + " message context..");
					messageContext.put(key, context);
				}
			}
			objMarshaller = context.createMarshaller();
			objMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			objMarshaller.marshal(objFactory.createAssembly(assembly),
					marshalledString);
		} catch (JAXBException e) {
			logger.debug("Exception while marshalling ", e);
            throw new JAXBException("Exception occured while marshalling the assembly element ", e);
		}
		logger.debug("OBX.5 Marshalled data " + marshalledString.toString());
		return marshalledString.toString();
	}



	/**
	 * This method creates the Subjective results. It process the surveyResponse
	 * object returned by the result service and constructs the OBX.5 element to
	 * hold the entire session information.
	 *
	 * @param surveyResponse
	 * @throws JAXBException
	 */
	public Assembly buildSubjectiveAssembly(SurveyResponse surveyResponse)  {

		Assembly assembly = new Assembly();

		Session session = new Session();

		session.setHeader(buildSessionHeader(surveyResponse));

		session.setProgram(buildProgram(surveyResponse));

		session.setSurvey(buildSurvey(surveyResponse));

		session.setRiskSummary(buildRiskSummary(surveyResponse));

		SessionResponse sessionResponse = new SessionResponse();
		sessionResponse
		.setSurveyInterface(buildSurveyInterface(surveyResponse));
		List<Response> respList = sessionResponse.getResponse();
		int count = 0;
        //sorting the questions to maintain the same order in which they are answered by the patient
		Collections.sort (surveyResponse.getQuestionResponse(),
				new Comparator<QuestionResponse> () {
								@Override
								public int compare(QuestionResponse o1, QuestionResponse o2) {
									return (int) (o1.getQuestionId()-o2.getQuestionId());
								}
						}
				);

		for (QuestionResponse qResponse : surveyResponse.getQuestionResponse()) {

			// building the well being questions
			if (qResponse.getMeterMeasurement().size() == 0) {
				buildQuestionAnswer(respList, qResponse, surveyResponse);
			}

			for (MeterMeasurement meterMeasurement : qResponse
					.getMeterMeasurement()) {

				for (AssessedVitalSign vital : meterMeasurement.getVitalSign()) {

					for (VitalSignValue vitalSignValue : vital
							.getNumericValue()) {
						Response response = new Response();
						ResponseID respID = new ResponseID();
						respID.setValue(surveyResponse.getId() + "_"
								+ vital.getId() + "_" + count);
						response.setResponseID(respID);
						ResponseSequenceNumber respSequenceNumber = new ResponseSequenceNumber();
						respSequenceNumber.setValue("" + count);
						response.setResponseSequenceNumber(respSequenceNumber);

						response.setResponseRisk(buildResponseRisk(qResponse));

						response.setQuestion(buildQuestion(qResponse,
								surveyResponse, vital));

						List<ResponseValue> responseValues = response
								.getResponseValue();
						responseValues.add(buildResponseValue(qResponse,
								vitalSignValue, vital,meterMeasurement,surveyResponse));

						ResponseTime responseTime = new ResponseTime();
						TS1CONTENT ts1 = new TS1CONTENT();
						ts1.setValue(Utils.convertXMLGregorianCalendarToString(vitalSignValue.getCollectionTime()));
						responseTime.setTS1(ts1);
						response.setResponseTime(responseTime);

						respList.add(response);
						count++;
					}

				}
			}

		}
		session.setSessionResponse(sessionResponse);

		assembly.setSession(session);

		//marshallAssembly("SUBJ_ASSEMBLY",assembly);

		return assembly;

	}

	private void buildQuestionAnswer(List<Response> respList,
			QuestionResponse qResponse, SurveyResponse surveyResponse) {
		Response response = new Response();
		ResponseID respID = new ResponseID();
		respID.setValue(surveyResponse.getId() + "_"+ qResponse.getSequenceNumber());
		response.setResponseID(respID);
		ResponseSequenceNumber respSequenceNumber = new ResponseSequenceNumber();
		respSequenceNumber.setValue("" + qResponse.getSequenceNumber());
		response.setResponseSequenceNumber(respSequenceNumber);

		response.setResponseRisk(buildResponseRisk(qResponse));

		response.setQuestion(buildQuestion(qResponse,
				surveyResponse, null));

		List<ResponseValue> responseValues = response
				.getResponseValue();
		ResponseValue responseValue = new ResponseValue();
		Value value = new Value();
		value.setValue(qResponse.getResponse());
		responseValue.setValue(value);
		ResponseTime responseTime = new ResponseTime();
		TS1CONTENT ts1 = new TS1CONTENT();
		ts1.setValue(Utils.convertXMLGregorianCalendarToString(surveyResponse.getCollectionTime()));
		responseTime.setTS1(ts1);
		response.setResponseTime(responseTime);

		responseValues.add(responseValue);

		respList.add(response);
	}



	/*
	 * This method constructs the Response value object which will put as part
	 * of Response object
	 */
	private ResponseValue buildResponseValue(QuestionResponse qResponse,
			VitalSignValue vitalSignValue, AssessedVitalSign vital, MeterMeasurement meterMeasurement, SurveyResponse surveyResponse) {
		ResponseValue responseValue = new ResponseValue();
		Value value = new Value();
		value.setValue(qResponse.getResponse());
		responseValue.setValue(value);

		VitalSignID vitalSignID = new VitalSignID();
		vitalSignID.setValue(""+vitalSignValue.getId());
		responseValue.setVitalSignID(vitalSignID);

		Mode mode = new Mode();
		Map<String, String> obsMethodCustCodes =
				ConfigurationLoader.getApplicationConfig(ConfigKeyEnum.RESULTS_SUBJ_METHOD_CUSTOMER_CODE.toString());
		if (obsMethodCustCodes != null) {
			String custCode = obsMethodCustCodes.get(meterMeasurement.getMode().getClass().getSimpleName());
			if (null == custCode){
				custCode = "";
			}
			mode.setValue(custCode);
			logger.debug("Customer code for observation method code " + meterMeasurement.getMode().getClass().getSimpleName() + " is " + custCode);
		}
		else {
			mode.setValue("");
		}
		responseValue.setMode(mode);

		if(vital.isInvalid()){
			InValidID inValidID = new InValidID();
			inValidID.setValue(vital.isInvalid());
			responseValue.setInValidID(inValidID);
		}

		ObservationResultStatus observationResultStatus = new ObservationResultStatus();
		observationResultStatus.setValue(vital.isInvalid()?"E":"F");

		responseValue.setObservationResultStatus(observationResultStatus);
		ClinicalCoding clinicalCoding = new ClinicalCoding();
		CE1CONTENT ce1 = new CE1CONTENT();
		ce1.setValue((vitalSignValue.getLoincCode()==null)?"":vitalSignValue.getLoincCode());
		clinicalCoding.setCE1(ce1);
		CE2CONTENT ce2 = new CE2CONTENT();
		ce2.setValue("" + vitalSignValue.getValue());
		clinicalCoding.setCE2(ce2);
		CE3CONTENT ce3 = new CE3CONTENT();
		ce3.setValue("LOINC");
		clinicalCoding.setCE3(ce3);
		responseValue.setClinicalCoding(clinicalCoding);

		UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
		ce1 = new CE1CONTENT();
		ce1.setValue((vitalSignValue.getUnit().name()==null)?"":vitalSignValue.getUnit().name());
		unitOfMeasure.setCE1(ce1);
		responseValue.setUnitOfMeasure(unitOfMeasure);

		CollectionType collectionType = new CollectionType();
		collectionType.setValue(meterMeasurement.getCollectionType().getClass().getSimpleName());
		responseValue.setCollectionType(collectionType);

		PatientTimeZone patientTimeZone = new PatientTimeZone();
		patientTimeZone.setValue(meterMeasurement.getPatientTimezone());
		responseValue.setPatientTimeZone(patientTimeZone);

		return responseValue;
	}

	/*
	 * This method constructs the SurveyInterface object which will put as part
	 * of SessionResponse object
	 */
	private SurveyInterface buildSurveyInterface(SurveyResponse surveyResponse) {
		SurveyInterface surveyInterface = new SurveyInterface();
		DeviceManufacturer deviceManufacturer = new DeviceManufacturer();
		deviceManufacturer.setValue(surveyResponse.getPatientInterface()
				.getManufacturer());
		surveyInterface.setDeviceManufacturer(deviceManufacturer);
		DeviceModel deviceModel = new DeviceModel();
		deviceModel.setValue(surveyResponse.getPatientInterface().getModel());
		surveyInterface.setDeviceModel(deviceModel);
		DeviceSerialNumber deviceSerialNumber = new DeviceSerialNumber();
		deviceSerialNumber.setValue(surveyResponse.getPatientInterface()
				.getSerialNumber());
		surveyInterface.setDeviceSerialNumber(deviceSerialNumber);
		return surveyInterface;
	}

	/*
	 * This method constructs the Question object which will put as part of
	 * Response object
	 */
	private Question buildQuestion(QuestionResponse qResponse,
			SurveyResponse surveyResponse, AssessedVitalSign vital) {
		Question question = new Question();
		QuestionID questionID = new QuestionID();
		questionID.setValue("" + qResponse.getQuestionId());
		question.setQuestionID(questionID);

		QuestionText questionText = new QuestionText();
		questionText.setValue(qResponse.getQuestion());
		question.setQuestionText(questionText);

		QuestionVersion questionVersion = new QuestionVersion();
		VID1CONTENT vid1 = new VID1CONTENT();
		vid1.setValue(qResponse.getQuestionVersion());
		questionVersion.setVID1(vid1);
		question.setQuestionVersion(questionVersion);

		QuestionCategory questionCategory = new QuestionCategory();
		CE1CONTENT ce1 = new CE1CONTENT();
		ce1.setValue((vital==null)?"":vital.getType().name());
		questionCategory.setCE1(ce1);

		CE2CONTENT ce2 = new CE2CONTENT();
		ce2.setValue(qResponse.getQuestionCategory().name());
		questionCategory.setCE2(ce2);
		question.setQuestionCategory(questionCategory);

		QuestionType questionType = new QuestionType();
		questionType.setValue(qResponse.getQuestionType().name());
		question.setQuestionType(questionType);

		/*
		 * List<QuestionVariable> questionVariables = question
		 * .getQuestionVariable(); for (SurveyVariable sv :
		 * surveyResponse.getSurveyVariables()) { QuestionVariable qv = new
		 * QuestionVariable(); VariableKey variableKey = new VariableKey();
		 * variableKey.setValue(sv.getKey()); qv.setVariableKey(variableKey);
		 * VariableValue variableValue = new VariableValue(); Value value = new
		 * Value(); value.setValue(sv.getValue());
		 * variableValue.setValue(value); qv.setVariableValue(variableValue);
		 * questionVariables.add(qv); }
		 */

		QuestionAspect questionAspect = new QuestionAspect();
		ce2 = new CE2CONTENT();
		ce2.setValue(qResponse.getQuestionAspect());
		questionAspect.setCE2(ce2);
		// TODO need to add CE1
		question.setQuestionAspect(questionAspect);

		ClinicalCoding clinicalCoding = new ClinicalCoding();
		ce1 = new CE1CONTENT();
		ce1.setValue((qResponse.getLoinc()==null)?"":qResponse.getLoinc());
		clinicalCoding.setCE1(ce1);
		ce2 = new CE2CONTENT();
		ce2.setValue("");
		clinicalCoding.setCE2(ce2);
		CE3CONTENT ce3 = new CE3CONTENT();
		ce3.setValue("LOINC");
		clinicalCoding.setCE3(ce3);
		question.setClinicalCoding(clinicalCoding);

		if (null != qResponse.getUnit()) {
			UnitOfMeasureType unitOfMeasureType = new UnitOfMeasureType();
			unitOfMeasureType.setValue(qResponse.getUnit().name());
			question.setUnitOfMeasureType(unitOfMeasureType);
		}

		// TODO Multiple question response Id to be added
		MultipleQuestionResponseID multipleQuestionResponseID = new MultipleQuestionResponseID();
		multipleQuestionResponseID.setValue((qResponse.getResponseId()==null)?"":""+qResponse.getResponseId());

		question.setMultipleQuestionResponseID(multipleQuestionResponseID);
		return question;
	}

	/*
	 * This method constructs the ResponseRisk object which will put as part of
	 * Response object
	 */
	private ResponseRisk buildResponseRisk(QuestionResponse qResponse) {
		ResponseRisk responseRisk = new ResponseRisk();
		RiskValue riskValue = new RiskValue();
		Risk risk = new Risk();
		CE1CONTENT ce1 = new CE1CONTENT();
		ce1.setValue("");
		risk.setCE1(ce1);

		CE2CONTENT ce2 = new CE2CONTENT();
		ce2.setValue(qResponse.getRisk().name());
		risk.setCE2(ce2);

		riskValue.setRisk(risk);
		responseRisk.setRiskValue(riskValue);

		return responseRisk;
	}

	/*
	 * This method constructs the SurveyResponse object which will put as part
	 * of Session object
	 */
	private RiskSummary buildRiskSummary(SurveyResponse surveyResponse) {
		List<com.bosch.th.patientrecord.RiskAssessment.Risk> riskList = surveyResponse
				.getRiskAssessment().getRisk();
		RiskSummary riskSummary = new RiskSummary();
		List<RiskSummaryRisk> rskList = riskSummary.getRiskSummaryRisk();
		for (com.bosch.th.patientrecord.RiskAssessment.Risk risk : riskList) {
			RiskSummaryRisk riskSummaryRisk = new RiskSummaryRisk();
			RiskSummaryCategory riskSummaryCategory = new RiskSummaryCategory();
			CE1CONTENT ce1 = new CE1CONTENT();
			ce1.setValue("");
			riskSummaryCategory.setCE1(ce1);
			CE2CONTENT ce2 = new CE2CONTENT();
			ce2.setValue(risk.getCategory().name());
			riskSummaryCategory.setCE2(ce2);
			riskSummaryRisk.setRiskSummaryCategory(riskSummaryCategory);

			RiskValue riskValue = new RiskValue();
			Risk riskVal = new Risk();
			ce1 = new CE1CONTENT();
			ce1.setValue("");
			riskVal.setCE1(ce1);

			ce2 = new CE2CONTENT();
			ce2.setValue(risk.getLevel().name());
			riskVal.setCE2(ce2);
			riskValue.setRisk(riskVal);
			riskSummaryRisk.setRiskValue(riskValue);
			rskList.add(riskSummaryRisk);
		}
		return riskSummary;
	}

	/*
	 * This method constructs the Survey object which will put as part of
	 * Session object
	 */
	private Survey buildSurvey(SurveyResponse surveyResponse) {
		Survey survey = new Survey();
		SurveyID surveyID = new SurveyID();
		surveyID.setValue("" + surveyResponse.getId());
		survey.setSurveyID(surveyID);
		SurveyName surveyName = new SurveyName();
		surveyName.setValue(surveyResponse.getSurveyName());
		survey.setSurveyName(surveyName);
		SurveyVersion surveyVersion = new SurveyVersion();
		VID1CONTENT vid1 = new VID1CONTENT();
		vid1.setValue(surveyResponse.getSurveyVersion());
		surveyVersion.setVID1(vid1);
		survey.setSurveyVersion(surveyVersion);
		SurveyGroupName surveyGroupName = new SurveyGroupName();
		surveyGroupName.setValue(surveyResponse.getSurveyGroupName());
		survey.setSurveyGroupName(surveyGroupName);
		SurveySentDate surveySentDate = new SurveySentDate();
		TS1CONTENT ts1 = new TS1CONTENT();
		ts1.setValue(Utils.convertXMLGregorianCalendarToString(surveyResponse.getSentDate()));
		surveySentDate.setTS1(ts1);
		survey.setSurveySentDate(surveySentDate);

		return survey;
	}

	/*
	 * This method constructs the Program object which will put as part of
	 * Session object
	 */
	private Program buildProgram(SurveyResponse surveyResponse) {
		Program program = new Program();

		ProgramID programID = new ProgramID();
		programID.setValue("" + surveyResponse.getProgramId());
		program.setProgramID(programID);

		ProgramName programName = new ProgramName();
		programName.setValue(surveyResponse.getProgramName());
		program.setProgramName(programName);

		return program;
	}

	/*
	 * This method constructs the Header object which will put as part of
	 * Session object
	 */
	private Header buildSessionHeader(SurveyResponse surveyResponse) {
		Header header = new Header();
		SessionName sessionName = new SessionName();
		sessionName.setValue(surveyResponse.getSurveyName());
		header.setSessionName(sessionName);

		SessionID sessionId = new SessionID();
		sessionId.setValue("" + surveyResponse.getId());
		header.setSessionID(sessionId);

		SessionReceivedDate sessionReceivedDate = new SessionReceivedDate();
		TS1CONTENT ts1 = new TS1CONTENT();
		ts1.setValue(Utils.convertXMLGregorianCalendarToString(surveyResponse.getReceivedDate()));
		sessionReceivedDate.setTS1(ts1);
		header.setSessionReceivedDate(sessionReceivedDate);

		SessionResponseDate sessionResponseDate = new SessionResponseDate();
		ts1 = new TS1CONTENT();
		ts1.setValue(Utils.convertXMLGregorianCalendarToString(surveyResponse.getResponseDate()));
		sessionResponseDate.setTS1(ts1);
		header.setSessionResponseDate(sessionResponseDate);

		SessionSentDate sentDate = new SessionSentDate();
		ts1 = new TS1CONTENT();
		ts1.setValue(Utils.convertXMLGregorianCalendarToString(surveyResponse.getSentDate()));
		sentDate.setTS1(ts1);
		header.setSessionSentDate(sentDate);

		TimeZone patientTimeZone = new TimeZone();
		patientTimeZone.setValue(surveyResponse.getPatientTimezone());
		header.setTimeZone(patientTimeZone);

		return header;

	}

}
