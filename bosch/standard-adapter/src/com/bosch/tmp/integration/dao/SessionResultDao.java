package com.bosch.tmp.integration.dao;

import java.util.List;

import com.bosch.tmp.integration.entities.SessionResult;

public interface SessionResultDao
{
	/**
	 * Persist session (HB3/T400).
	 * @param session
	 * @return
	 */
	public SessionResult save(SessionResult session);

	/**
	 * Update session.
	 * @param session
	 * @return
	 */
	public SessionResult update(SessionResult session);

	/**
	 * Find session by unique id.
	 * @param sessionId
	 * @return
	 */
	public SessionResult findById(long sessionId);

	/**
	 * Find session by organization id.
	 * @param organizationId
	 * @param isAcknowledged
	 * @return
	 */
	public  List<SessionResult> findSessionResultsByOrganizationId(String organizationId, Boolean isAcknowledged);

	/**
	 * Find session by session id.
	 * @param sessionId
	 * @param isAcknowledged
	 * @return
	 */
	public  List<SessionResult> findSessionResultBySessionId(String sessionId, Boolean isAcknowledged);

	/**
	 * Find session by session id and organization id.
	 * @param sessionId
	 * @param organizationId
	 * @return
	 */
	public  List<SessionResult> findSessionResultBySessionIdAndOrgId(String sessionId, String organizationId);
}
