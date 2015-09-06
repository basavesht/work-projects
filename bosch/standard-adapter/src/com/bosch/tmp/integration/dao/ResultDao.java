/**
 *
 */
package com.bosch.tmp.integration.dao;

import java.util.List;

import com.bosch.tmp.integration.entities.Result;

/**
 * @author BEA2KOR
 *
 */
public interface ResultDao {

	/**
	 * Persist results (Session/Adhoc vitals).
	 * @param result
	 * @return
	 */
	public Result save(Result result);

	/**
	 * Update results entity.
	 * @param result
	 * @return
	 */
	public Result update(Result result);

	/**
	 * Find result by unique id.
	 * @param resultId
	 * @return
	 */
	public Result findById(long resultId);

	/**
	 * Find result by result id and type (VITAL/SESSION).
	 * @param resultId
	 * @param isAcknowledged
	 * @param type
	 * @return
	 */
	public List<Result> findResultsByResultId(String resultId, Boolean isAcknowledged, String type);

	/**
	 * Find Result by vital sign id and organization id.
	 * @param vitalSignId
	 * @param organizationId
	 * @param isAcknowledged
	 * @param type
	 * @return
	 */
	public List<Result> findResultsByVitalSignAndOrgId(String vitalSignId, String organizationId, Boolean isAcknowledged, List<String> type);

	/**
	 * Find result by session id.
	 * @param sessionId
	 * @param organizationId
	 * @param isAcknowledged
	 * @param type
	 * @return
	 */
	public List<Result> findResultsBySessionId(String sessionId, String organizationId, Boolean isAcknowledged, String type);

	/**
	 * Find result by organization id and type.
	 * @param organizationId
	 * @param isAcknowledged
	 * @param type
	 * @return
	 */
	public List<Result> findResultsByOrganizationId(String organizationId, Boolean isAcknowledged, String type);

	/**
	 * Find result by organization id only.
	 * @param organizationId
	 * @param isAcknowledged
	 * @return
	 */
	public List<Result> findResultsByOrganizationId(String organizationId, Boolean isAcknowledged);

	/**
	 * Find all existing results for CDA.
	 * @return
	 */
	public List<Result> findAllResults();
}
