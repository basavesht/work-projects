package com.bosch.pat.dao;

import java.util.List;

import com.bosch.pat.entity.PatTask;
import com.bosch.pat.exception.PATDataException;

public interface TaskDao {
	public void createTask (PatTask task) throws PATDataException ;
	public PatTask updateTask (PatTask task) throws PATDataException ;
	public PatTask findTaskById (Long taskId) throws PATDataException ;
	public List<PatTask> findTaskByPatId (Long patientId,Integer acntId) throws PATDataException ;
	public List<PatTask> findTaskByPatIdAndStatus (Long patientId,Integer acntId, List<Integer> statuses) throws PATDataException ;
	public List<Object> findNextPossibleTaskDueDateByPatId (Long patientId,Integer acntId, Integer status) throws PATDataException ; //Applicable only for recurring instances..
	public List<Object> findTaskByPatIdAndStatusNative (Long patientId,Integer acntId, List<Integer> statuses) throws PATDataException ;
}
