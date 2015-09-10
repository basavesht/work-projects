package com.bosch.pat.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bosch.pat.dao.TaskDao;
import com.bosch.pat.entity.PatTask;
import com.bosch.pat.exception.PATDataException;

@Repository
public class TaskDaoImpl extends GenericDaoImpl<Long, PatTask> implements TaskDao 
{
	@Override
	public void createTask(PatTask task) throws PATDataException {
		persist(task);
	}

	@Override
	public PatTask updateTask(PatTask task) throws PATDataException {
		return merge(task);
	}

	@Override
	public PatTask findTaskById(Long taskId) throws PATDataException {
		return findById(taskId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PatTask> findTaskByPatId(Long patientId, Integer acntId) throws PATDataException {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("acntId",acntId);
		params.put("patId",patientId);
		return (List<PatTask>)findByNamedQueryAndNamedParams("PatTask.findTaskByPatId", params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findNextPossibleTaskDueDateByPatId(Long patientId,Integer acntId, Integer status) throws PATDataException {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("acntId",acntId);
		params.put("patId",patientId);
		params.put("status",status);
		return (List<Object>)findByNamedQueryAndNamedParams("PatTask.findNextPossibleTaskDueDateByPatId", params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PatTask> findTaskByPatIdAndStatus(Long patientId, Integer acntId, List<Integer> statuses) throws PATDataException {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("acntId",acntId);
		params.put("patId",patientId);
		params.put("statuses",statuses);
		return (List<PatTask>)findByNamedQueryAndNamedParams("PatTask.findTaskByPatIdAndStatus", params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findTaskByPatIdAndStatusNative(Long patientId, Integer acntId, List<Integer> statuses) throws PATDataException {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("acntId",acntId);
		params.put("patId",patientId);
		params.put("statuses",statuses);
		return (List<Object>)findByNamedQueryAndNamedParams("PatTaskNative.findTaskByPatIdAndStatus", params);
	}
}
