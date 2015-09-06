package com.bosch.tmp.integration.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bosch.tmp.integration.entities.Result;

@Repository
public class ResultDaoImpl extends JpaDaoSupport implements ResultDao
{

	/**
	 * Sets the entity manager factory.
	 * @param entityManagerFactory the factory
	 */
	@Autowired
	public ResultDaoImpl(final EntityManagerFactory entityManagerFactory) {
		super();
		super.setEntityManagerFactory(entityManagerFactory);
	}

	@Transactional
	@Override
	public Result save(Result result){
		getJpaTemplate().persist(result);
		return result;
	}

	@Transactional
	@Override
	public Result update(Result result){
		return getJpaTemplate().merge(result);
	}

	@Override
	public Result findById(long resultId){
		return getJpaTemplate().find(Result.class, resultId);
	}

	@Override
	public List<Result> findResultsByResultId(String resultId,Boolean isAcknowledged, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resultId", Long.parseLong(resultId));
		params.put("isAcknowledged", isAcknowledged);
		params.put("type", type);
		return getJpaTemplate().findByNamedQueryAndNamedParams("Result.findResultsByResultId", params);
	}

	@Override
	public List<Result> findResultsByVitalSignAndOrgId(String vitalSignId,String organizationId, Boolean isAcknowledged, List<String> type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("vitalSignId", vitalSignId);
		params.put("organizationId", organizationId);
		params.put("isAcknowledged", isAcknowledged);
		params.put("type", type);
		return getJpaTemplate().findByNamedQueryAndNamedParams("Result.findResultsByVitalSignAndOrgId", params);
	}

	@Override
	public List<Result> findResultsBySessionId(String sessionId,String organizationId, Boolean isAcknowledged, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", Long.parseLong(sessionId));
		params.put("organizationId", organizationId);
		params.put("isAcknowledged", isAcknowledged);
		params.put("type", type);
		return getJpaTemplate().findByNamedQueryAndNamedParams("Result.findResultsBySessionId", params);
	}

	@Override
	public List<Result> findResultsByOrganizationId(String organizationId,Boolean isAcknowledged, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("organizationId", organizationId);
		params.put("isAcknowledged", isAcknowledged);
		params.put("type", type);
		return getJpaTemplate().findByNamedQueryAndNamedParams("Result.findResultsByOrganizationIdAndType", params);
	}

	@Override
	public List<Result> findResultsByOrganizationId(String organizationId,Boolean isAcknowledged) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("organizationId", organizationId);
		params.put("isAcknowledged", isAcknowledged);
		return getJpaTemplate().findByNamedQueryAndNamedParams("Result.findResultsByOrganizationId", params);
	}

	@Override
	public List<Result> findAllResults() {
		return getJpaTemplate().findByNamedQuery("Result.findAllResults");
	}

}
