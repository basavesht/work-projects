package com.bosch.tmp.integration.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bosch.tmp.integration.entities.Notes;
import com.bosch.tmp.integration.entities.Result;
import com.bosch.tmp.integration.entities.SessionResult;

public class ResultsTestDataUtil {

	/**
	 * load unacked objective result id's..
	 * @return
	 */
	public static List<Result> loadUnackedAdhocResults()
	{
		List<Result> unackedObjectiveResults = new ArrayList<Result>();

		//Meter measurement id = 1645;
		List<String> resultIds = Arrays.asList("2527","2526");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(1645L);
			unackedObjectiveResults.add(result);
		}

		//Meter measurement id = 1644;
		resultIds = Arrays.asList("2525");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(1644L);
			unackedObjectiveResults.add(result);
		}

		//Meter measurement id = 1643;
		resultIds = Arrays.asList("2524","2523");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(1643L);
			unackedObjectiveResults.add(result);
		}

		//Meter measurement id = 845;
		resultIds = Arrays.asList("1363");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(845L);
			unackedObjectiveResults.add(result);
		}

		//Meter measurement id = 844;
		resultIds = Arrays.asList("1362","1361");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(844L);
			unackedObjectiveResults.add(result);
		}

		//Meter measurement id = 766;
		resultIds = Arrays.asList("1293");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(766L);
			unackedObjectiveResults.add(result);
		}

		//Meter measurement id = 775;
		resultIds = Arrays.asList("1292");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(775L);
			unackedObjectiveResults.add(result);
		}

		//Meter measurement id = 773;
		resultIds = Arrays.asList("1290");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(773L);
			unackedObjectiveResults.add(result);
		}


		//Meter measurement id = 272;
		resultIds = Arrays.asList("505","504");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(272L);
			unackedObjectiveResults.add(result);
		}


		//Meter measurement id = 271;
		resultIds = Arrays.asList("503");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(271L);
			unackedObjectiveResults.add(result);
		}

		//Meter measurement id = 270;
		resultIds = Arrays.asList("502","501");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(270L);
			unackedObjectiveResults.add(result);
		}

		//Meter measurement id = 267;
		resultIds = Arrays.asList("497");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(267L);
			unackedObjectiveResults.add(result);
		}

		//Meter measurement id = 265;
		resultIds = Arrays.asList("495");
		for (String resultId : resultIds){
			Result result = new Result();
			result.setType("VITAL_SIGN");
			result.setVitalSignId(resultId);
			result.setResultId(265L);
			unackedObjectiveResults.add(result);
		}
		return unackedObjectiveResults;
	}

	public static List<Notes> loadUnackedNotes() {
		return null;
	}

	public static List<SessionResult> loadUnackedSessions() {
		return null;
	}

	public static List<Result> loadUnackedResults() {
		return new ArrayList<Result>();
	}
}
