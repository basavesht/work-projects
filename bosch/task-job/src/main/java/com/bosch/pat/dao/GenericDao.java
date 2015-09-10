package com.bosch.pat.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.springframework.dao.DataAccessException;

public interface GenericDao <PK, T> {

	/**
	 * Persist the newInstance object into database.
	 * @param entity
	 */
	public void persist(T entity) throws DataAccessException ;

	/**
	 * Save changes made to a persistent object.	
	 * @param entity
	 */
	public T merge(T entity) throws DataAccessException;

	/**
	 * Remove an object from persistent storage in the database
	 * @param entity
	 */
	public void remove(T entity) throws DataAccessException;

	/**
	 * Retrieve an object that was previously persisted to the database using
	 * the indicated id as primary key
	 * @param id
	 * @return
	 */
	public T findById(PK id) throws DataAccessException;

	/**
	 * Retrieve an object by passing the query string.
	 * @param queryString
	 * @return
	 */
	public List<?> find(String queryString) throws DataAccessException;

	/**
	 * Retrieve an object by passing the query string along with query parameters as an array.
	 * @param queryString
	 * @param values
	 * @return
	 */
	public List<?> find(String queryString, Object... values) throws DataAccessException;

	/**
	 * Retrieve an object by passing the query name
	 * @param queryName
	 * @return
	 */
	public List<?> findByNamedQuery(String queryName) throws DataAccessException;

	/**
	 * Retrieve an object by passing the query name and the query parameters as an array.
	 * @param queryName
	 * @param values
	 * @return
	 */
	public List<?> findByNamedQuery(String queryName, Object... values) throws DataAccessException;

	/**
	 * Retrieve an object by passing the query name and the query parameters in a Map.
	 * Map contains the entries with key as query parameters name and value as query parameters value.
	 * @param queryName
	 * @param params
	 * @return
	 */
	public List<?> findByNamedQueryAndNamedParams(String queryName, Map<String,?> params)throws DataAccessException;
	
	
	/**
	 * Retrieve the count of the records matching the query condition .
	 * Map contains the entries with key as query parameters name and value as query parameters value.
	 * @param queryName
	 * @param params
	 * @return
	 */
	public Integer count(String queryName, Map<String,?> params) throws DataAccessException;

	/**
	 * Retrieve an object by passing a list of criteria to be set on the entity.
	 * @param criterion
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> findByCriteria(Criterion... criterion)throws DataAccessException;

	/**
	 * Checks if the Entity Manager contains the entity instance. 
	 * @param entity
	 * @return
	 */
	public boolean contains(T entity) throws DataAccessException;

	/**
	 * Flush out the 
	 */
	public void flush() throws DataAccessException;

	
	/**
	 * @param queryName
	 * @param dto
	 * @return
	 * @throws DataAccessException
	 */
	List<?> findByNamedQuery(String queryName, Class dto, Map<String, ?> params)
			throws DataAccessException;

	List<?> findByNativeQuery(String queryName, Map<String, ?> params)
			throws DataAccessException;

	/**
	 * 
	 * @param queryName
	 * @param values
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> findByNamedNativeQuery(String queryName, Object... values) throws DataAccessException;
	
	
	/**
	 * To remove record from database
	 * @param queryName
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public int removeByNamedQueryAndNamedParams(String queryName, Map<String, ?> params) throws DataAccessException ;

	List<?> findByNamedNativeQuery(String queryName, Map<String, ?> params)
			throws DataAccessException;

}
