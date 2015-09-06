package com.bosch.tmp.integration.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bosch.tmp.integration.entities.SessionResult;

@Repository
public class SessionResultDaoImpl extends JpaDaoSupport implements SessionResultDao
{
	/**
	 * Sets the entity manager factory.
	 * @param entityManagerFactory the factory
	 */
	@Autowired
	public SessionResultDaoImpl(final EntityManagerFactory entityManagerFactory) {
		super();
		super.setEntityManagerFactory(entityManagerFactory);
	}

	@Transactional
	@Override
	public SessionResult save(SessionResult session) {
		getJpaTemplate().persist(session);
		return session;
	}

	@Transactional
	@Override
	public SessionResult update(SessionResult session) {
		return getJpaTemplate().merge(session);
	}

	@Override
	public SessionResult findById(long id) {
		return getJpaTemplate().find(SessionResult.class, id);
	}

	@Override
	public List<SessionResult> findSessionResultsByOrganizationId(String organizationId, Boolean isAcknowledged) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("organizationId", organizationId);
		params.put("isAcknowledged", isAcknowledged);
		return getJpaTemplate().findByNamedQueryAndNamedParams("SessionResult.findSessionResultsByOrganizationId", params);
	}

	@Override
	public List<SessionResult> findSessionResultBySessionId(String sessionId,Boolean isAcknowledged) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", Long.parseLong(sessionId));
		params.put("isAcknowledged", isAcknowledged);
		return getJpaTemplate().findByNamedQueryAndNamedParams("SessionResult.findSessionResultBySessionId", params);
	}

	@Override
	public List<SessionResult> findSessionResultBySessionIdAndOrgId(String sessionId, String organizationId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", Long.parseLong(sessionId));
		params.put("organizationId", organizationId);
		return getJpaTemplate().findByNamedQueryAndNamedParams("SessionResult.findSessionResultBySessionIdAndOrgId", params);
	}

}
