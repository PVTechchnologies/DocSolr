package com.docsolr.common.dao.Impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.docsolr.common.dao.GenericDAO;
import com.docsolr.interceptors.AccessInterceptor;

/**
 * GenericDAOImpl Class implements basic CRUD methods from GenericDAO<T>
 * interface sessionfactory is autowired to get hibernate session.
 * 
 * @author rajkiran.dewara
 * 
 * @param <T>
 */
@Repository
public class GenericDAOImpl<T> implements GenericDAO<T> {
	@Autowired
	SessionFactory sessionFactory;

	Logger logger = Logger.getLogger(GenericDAOImpl.class);

	@Override
	public T saveEntity(T entity)
	{
		logger.info("saveEntity CALLED");
		Session session = null;

		try
		{
			session = sessionFactory.openSession();
			session.save(entity);
			session.flush();
		}
		catch (Exception ex)
		{
			logger.error("Exception occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return entity;
	}


	@Override
	public T saveUpdateEntity(T entity)
	{
		logger.info("saveUpdateEntity CALLED");
		Session session = null;

		try
		{
			session = sessionFactory.openSession();
			session.saveOrUpdate(entity);
			session.flush();
		}
		catch (Exception ex)
		{
			logger.error("Exception occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return entity;
	}

	@Override
	public T updateEntity(T entity)
	{
		logger.info("updateEntity CALLED");
		Session session = null;

		try
		{
			session = sessionFactory.openSession();
			session.update(entity);
			session.flush();
		}
		catch (Exception ex)
		{
			logger.error("EXCEPTION OCCURED->");
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return entity;
	}




	@Override
	public void deleteEntity(T entity)
	{
		logger.info("deleteEntity CALLED");
		Session session = null;

		try
		{
			session = sessionFactory.openSession();
			session.delete(entity);
			session.flush();
		}
		catch (Exception ex)
		{
			logger.error("Exception Occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findEntityById(Class<T> clazz, Serializable id)
	{
		logger.info("findEntityById CALLED");
		Session session = null;
		T t = null;
		try
		{
			session = sessionFactory.openSession();
			t = (T) session.get(clazz, id);
		}
		catch (Exception ex)
		{
			logger.error("Exception Occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAllEntity(Class<T> clazz)
	{

		logger.info("findAllEntity CALLED");
		List<T> listt = null;
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			listt = session.createCriteria(clazz).list();
		}
		catch (Exception ex)
		{
			logger.error("Exception occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return listt;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<T> findLimitedEntity(Class<T> clazz,int fetchSize,Map<String,?> restrictionMap, Map<String,Map<String,?>> restrictionChildEntityMap)
	{
		logger.info("findLimitedEntity CALLED");
		List<T> listt = null;
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(clazz);
			if(fetchSize!=0 && restrictionMap!=null && !restrictionMap.isEmpty())
			{
				listt = session.createCriteria(clazz).add(Restrictions.allEq(restrictionMap)).setMaxResults(fetchSize).list();
			}
			else if(restrictionChildEntityMap!=null && !restrictionChildEntityMap.isEmpty())
			{
				if(restrictionMap!=null && !restrictionMap.isEmpty()){
					criteria.add(Restrictions.allEq(restrictionMap));
				}
				Map<String,?> restrictionChildMap = null;
				for (Entry<String, Map<String, ?>> childMap : restrictionChildEntityMap.entrySet()) {
					criteria.createCriteria(childMap.getKey()).add(Restrictions.allEq(childMap.getValue()));
				}
				listt = criteria.list();
			}
			else if(restrictionMap!=null && !restrictionMap.isEmpty()){
				listt = criteria.add(Restrictions.allEq(restrictionMap)).list();
			}
			else if(fetchSize!=0)
			{
				listt = session.createCriteria(clazz).setFetchSize(fetchSize).list();
			}
			else
			{
				listt = session.createCriteria(clazz).list();
			}
		}
		catch (Exception ex)
		{
			logger.error("Exception Occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return listt;
	}

	@Override
	public List<T> findEntityByRestriction(Class<T> clazz,Map<String,?> restrictionMap)
	{
		logger.info("findLimitedEntity CALLED");
		List<T> listt = null;
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(clazz);
			if(restrictionMap!=null && !restrictionMap.isEmpty()){
				listt = criteria.add(Restrictions.allEq(restrictionMap)).list();
			}else{
				listt = session.createCriteria(clazz).list();
			}
		}
		catch (Exception ex)
		{
			logger.error("Exception Occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return listt;
	}
	
	@Override
	public List<T> saveBatchEntity(Class<T> clazz, List<T> listOfEntity)
	{
		logger.info("saveBatchEntity CALLED");
		Session session = null;
		try{
			session = sessionFactory.openSession();
			for (T entity : listOfEntity) {
				session.save(entity);
			}
			session.flush();
		}
		catch (Exception ex)
		{
			logger.error("Exception occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return listOfEntity;
	}
	
	@Override
	public List<T> updateBatchEntity(Class<T> clazz, List<T> listOfEntity)
	{
		logger.info("updateBatchEntity CALLED");
		Session session = null;
		try{
			session = sessionFactory.openSession();
			for (T entity : listOfEntity) {
				session.update(entity);
			}
			session.flush();
		}
		catch (Exception ex)
		{
			logger.error("Exception occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return listOfEntity;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findEntityByFk(Class<T> clazz,Class<T> fkclazz)
	{
		logger.info("findEntityByFk CALLED");
		List<T> listt = null;
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			listt = session.createCriteria(clazz)
				    .list();
		}
		catch (Exception ex)
		{
			logger.error("Exception Occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return listt;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public T findUniqueEntity(Class<T> clazz, List<Criterion> restrictions) {
		logger.info("findUniqueEntity CALLED");
		T entity = null;
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(clazz);
			Iterator<Criterion> iterator = restrictions.iterator();
			while (iterator.hasNext()) {
				criteria.add(iterator.next());
			}
			entity = (T)criteria.uniqueResult();
		}
		catch (Exception ex)
		{
			logger.error("Exception occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return entity;
	}
	
	/**
	 * @author Rajkiran
	 * @param columnKey
	 * @param columnValue
	 * @param tableName
	 * @return
	 * 
	 */
	public Map<String, String> getMasterKeyValueMap(String groupName) {
		logger.info("getListOfMap CALLED");
		Session session = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String,String> result = new HashMap<String,String>();
		try {
			session = sessionFactory.openSession();
			String HQL_QUERY = "select new map(value,name) from Mastervalues " ;
			if(groupName != null && !groupName.isEmpty()){
				HQL_QUERY = HQL_QUERY + " where groupid in (select id from Mastergroup where name =:groupName)";
			}
			Query query = session.createQuery(HQL_QUERY);
			if(groupName != null && !groupName.isEmpty()){
				query.setParameter("groupName", groupName);
			}
			list = query.list();
			for (Map<String,String> row : list) {
				result.put(row.get("0"),row.get("1"));
			}
		} catch (HibernateException he) {
			logger.error("Exception occured->", he);
			he.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}
	
	/**
	 * @author Rajkiran
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findEntityByAliasCriteriaQuery(Class<T> clazz,String aliasName, Map<String,?> childPropertyEntityAliasMap, Map<String,?> restrictionMap)
	{
		// user, projectInfoUser
		// projectInfoUser.id, 1
		logger.info("findEntityByAliasCriteriaQuery CALLED");
		List<T> listt = null;
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(clazz);
			for (Entry<String,?> map : childPropertyEntityAliasMap.entrySet()) {
				criteria.createAlias(map.getKey(), (String) map.getValue());
			}
			criteria.add(Restrictions.allEq(restrictionMap));
			listt = criteria.list();
		}
		catch (Exception ex)
		{
			logger.error("Exception Occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return listt;
	}
	
	
	/**
	 * @author Rajkiran
	 */
	@Override
	public List<T> getEnityListByQuery(String tableName, Map<String,?> whereClause) {
		logger.info("getEnityListByQuery CALLED");
		Session session = null;
		List<T> list = new ArrayList<T>();
		Map<String,String> result = new HashMap<String,String>();
		try {
			session = sessionFactory.openSession();
			String HQL_QUERY = "from " + tableName ;
			int i=0;
			for (Entry<String,?> map : whereClause.entrySet()) {
				if(i == 0){
					HQL_QUERY = HQL_QUERY + " where " +map.getKey() +" = " +map.getValue();
					i++;
				}else{
					HQL_QUERY = HQL_QUERY + " and " +map.getKey() +" = " +map.getValue();
				}
			}
			Query query = session.createQuery(HQL_QUERY);
			list = query.list();
		} catch (HibernateException he) {
			logger.error("Exception occured->", he);
			he.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}
	
	
	/**
	 * @author Rajkiran
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAllEntityByProjectedColumn(Class clazz, String projectedColumn,Map<String,?> restrictionMap)
	{

		logger.info("findAllEntityByProjectedColumn CALLED");
		List<T> listt = null;
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(clazz);
			if(projectedColumn != null && !projectedColumn.isEmpty()){
				//projected column list
				ProjectionList p1 = Projections.projectionList();
				p1.add(Projections.property(projectedColumn));
				criteria.setProjection(p1);
			}
			//if there is any restriction
			if(restrictionMap != null && ! restrictionMap.isEmpty()){
				criteria.add(Restrictions.allEq(restrictionMap));
			}
			
			listt = criteria.list();
		}
		catch (Exception ex)
		{
			logger.error("Exception occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return listt;
	}
	
	
	/**
	 * @author Rajkiran
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findEntityByListOfValue(Class<T> clazz, String propertyName, List values)
	{
		logger.info("findEntityByListOfValue CALLED");
		Session session = null;
		List<T> listt = null;
		try
		{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(clazz);
			criteria.add(Restrictions.in(propertyName, values));
			listt = criteria.list();
		}
		catch (Exception ex)
		{
			logger.error("Exception Occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return listt;
	}
	
	
	/**
	 * @author Rajkiran
	 */
	@Override
	public Map<Long, String> getKeyValueMap(String tablename, String keycolumn, String valuecolumn) {
		logger.info("getKeyValueMap CALLED");
		Session session = null;
		List<Map<Long, String>> list = new ArrayList<Map<Long, String>>();
		Map<Long,String> result = new HashMap<Long,String>();
		try {
			session = sessionFactory.openSession();
			String HQL_QUERY = "select new map("+keycolumn+","+valuecolumn+") from "+tablename  ;
			Query query = session.createQuery(HQL_QUERY);
			list = query.list();
			for (Map<Long,String> row : list) {
				String key = String.valueOf(row.get("0"));
				Long longKey = Long.parseLong(key);
				result.put(longKey,row.get("1"));
			}
		} catch (HibernateException he) {
			logger.error("Exception occured->", he);
			he.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}
}
