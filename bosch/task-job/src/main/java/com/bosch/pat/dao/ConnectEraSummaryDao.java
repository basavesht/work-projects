package com.bosch.pat.dao;

import java.util.List;

import com.bosch.pat.entity.ConnectEraSummary;
import com.bosch.pat.exception.PATDataException;

public interface ConnectEraSummaryDao {
	public ConnectEraSummary updateMostRecentConnectDate (ConnectEraSummary connectEraSummary) throws PATDataException ;
	public List<ConnectEraSummary> findByCurrentConnectEraId (Integer acntId, Long patientId) throws PATDataException ;
	public List<Object> findByCurrentConnectEraIdNative (Integer acntId, Long patientId) throws PATDataException ;
	public ConnectEraSummary updateMostRecentTaskDueDate (ConnectEraSummary connectEraSummary) throws PATDataException ;
}
