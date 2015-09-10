package com.bosch.pat.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bosch.pat.dao.ConnectEraSummaryDao;
import com.bosch.pat.entity.ConnectEraSummary;
import com.bosch.pat.exception.PATDataException;

@Repository
public class ConnectEraSummaryDaoImpl extends GenericDaoImpl<Integer, ConnectEraSummary> implements ConnectEraSummaryDao {

	@Override
	public ConnectEraSummary updateMostRecentTaskDueDate(ConnectEraSummary connectEraSummary) throws PATDataException {
		return merge(connectEraSummary);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConnectEraSummary> findByCurrentConnectEraId(Integer acntId, Long patientId) throws PATDataException {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("acntId",acntId);
		params.put("patId",patientId);
		return (List<ConnectEraSummary>)findByNamedQueryAndNamedParams("ConnectEraSummary.findByCurrentConnectEraId", params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findByCurrentConnectEraIdNative(Integer acntId, Long patientId) throws PATDataException {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("acntId",acntId);
		params.put("patId",patientId);
		return (List<Object>)findByNamedQueryAndNamedParams("ConnectEraSummaryNative.findByCurrentConnectEraId", params);
	}

	@Override
	public ConnectEraSummary updateMostRecentConnectDate(
			ConnectEraSummary connectEraSummary) throws PATDataException {
		return merge(connectEraSummary);
	}
}
