package com.bosch.pat.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.springframework.dao.DataAccessException;

import com.bosch.pat.dao.GenericDao;

/**
 * Generic DAO implementation Pattern 
 *  PK is generic type parameter representing the primary key of the entity.
 *  T is generic type class representing the entity.
 * @author BEA2KOR
 *
 * @param <PK>
 * @param <T>
 */
public class GenericDaoImpl<PK extends Serializable, T> implements GenericDao<PK, T>
{
	protected Class<T> entityClass;

	@PersistenceContext (unitName = "PATDB")
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		if(this.entityClass == null) {
			ParameterizedType parameterizedType = (ParameterizedType) (this.getClass().getGenericSuperclass());
			while (!(parameterizedType instanceof ParameterizedType)) {
				parameterizedType = (ParameterizedType) parameterizedType.getClass().getGenericSuperclass();
			}
			this.entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[1];
		}
	}

	@Override
	public void persist(final T entity) throws DataAccessException {
		entityManager.persist(entity);
	}

	@Override
	public T merge(final T entity) throws DataAccessException {
		return entityManager.merge(entity);
	}

	@Override
	public void remove(final T entity) throws DataAccessException {
		entityManager.remove(entity);
	}

	@Override
	public T findById(final PK id) throws DataAccessException {
		return entityManager.find(entityClass, id);
	}

	@Override
	public List<?> find(String queryString) throws DataAccessException {
		return find(queryString, (Object[]) null);
	}

	@Override
	public List<?> find(String queryString, Object... values) throws DataAccessException {
		Query queryObject = entityManager.createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
		return queryObject.getResultList();
	}
	
	@Override
	public List<?> findByNativeQuery(String queryName,  Map<String, ?> params) throws DataAccessException {
		Query  query =  entityManager.createNativeQuery(queryName);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.getResultList();
	}

	@Override
	public List<?> findByNamedQuery(String queryName) throws DataAccessException {
		return findByNamedQuery(queryName, (Object[]) null);
	}

	@Override
	public List<?> findByNamedQuery(String queryName,Class dto, Map<String, ?> params) throws DataAccessException {
		TypedQuery<T> typedQuery =  entityManager.createNamedQuery(queryName,  dto);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				typedQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return typedQuery.getResultList();
	}

	@Override
	public List<?> findByNamedQuery(String queryName, Object... values) throws DataAccessException {
		Query queryObject = entityManager.createNamedQuery(queryName);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
		return queryObject.getResultList();
	}

	@Override
	public List<?> findByNamedQueryAndNamedParams(String queryName, Map<String, ?> params) throws DataAccessException {
		Query queryObject = entityManager.createNamedQuery(queryName);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return queryObject.getResultList();
	}

	@Override
	public List<?> findByCriteria(Criterion... criterion) throws DataAccessException {
		Criteria criteria = (Criteria) entityManager.getCriteriaBuilder().createQuery(entityClass);
		for (Criterion c : criterion) {
			criteria.add(c);
		}
		return criteria.list();
	}

	@Override
	public boolean contains(T entity) throws DataAccessException {
		return entityManager.contains(entity);
	}

	@Override
	public void flush() throws DataAccessException {
		entityManager.flush();
	}

	@Override
	public Integer count(String queryName, Map<String, ?> params) throws DataAccessException {
		Query queryObject = entityManager.createNamedQuery(queryName);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return ((Number)queryObject.getSingleResult()).intValue();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public List<?> findByNamedNativeQuery(String queryName, Object... values) throws DataAccessException {
		Query queryObject = entityManager.createNamedQuery(queryName);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
		return queryObject.getResultList();
	}
	
	@Override
	public int removeByNamedQueryAndNamedParams(String queryName, Map<String, ?> params) throws DataAccessException {
		Query queryObject = entityManager.createNamedQuery(queryName);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return queryObject.executeUpdate();
	}
	
	@Override
	public List<?> findByNamedNativeQuery(String queryName, Map<String, ?> params) throws DataAccessException {
		Query queryObject = entityManager.createNamedQuery(queryName);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return queryObject.getResultList();
	}
}
