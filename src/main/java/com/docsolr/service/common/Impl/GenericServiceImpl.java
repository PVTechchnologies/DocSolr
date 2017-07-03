package com.docsolr.service.common.Impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docsolr.common.dao.GenericDAO;
import com.docsolr.service.common.GenericService;

/**
 * 
 * @author rajkiran.dewara
 *
 * @param <T>
 */
@Service
@Transactional
public class GenericServiceImpl<T> implements GenericService<T> {

	Logger logger = Logger.getLogger(GenericServiceImpl.class);

	@Autowired
	GenericDAO<T> genericDAOImpl;

	@Override
	public T saveEntity(T entity)
	{
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.saveEntity(entity);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}


	@Override
	public T updateEntity(T entity)
	{
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.updateEntity(entity);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}

	@Override
	public void deleteEntity(T entity)
	{
		logger.info("CALLED");
		try
		{
			genericDAOImpl.deleteEntity(entity);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}

	@Override
	public List<T> findAllEntity(Class clazz)
	{
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.findAllEntity(clazz);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}


	@Override
	public T findEntityById(Class clazz, Serializable id)
	{
		logger.info("CALLED");
		try
		{
			return (T) genericDAOImpl.findEntityById(clazz, id);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}

	@Override
	public List<T> findLimitedEntity(Class<T> clazz,int fetchSize,Map<String,?> restrictionMap,Map<String,Map<String,?>> restrictionChildEntityMap)
	{
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.findLimitedEntity(clazz, fetchSize,restrictionMap,restrictionChildEntityMap);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}
	@Override
	public List<T> findEntityByRestriction(Class<T> clazz,Map<String,?> restrictionMap){
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.findEntityByRestriction(clazz, restrictionMap);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}
	@Override
	public T getByProfileAndModule(Class clazzone,Class clazztwo, Serializable idone, Serializable idtwo)
	{
		logger.info("CALLED");
		try
		{
			return null;//genericDAOImpl.findLimitedEntity(clazz, fetchSize,restrictionMap);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}
	
	public List<T> saveBatchEntity(Class<T> clazz, List<T> listOfEntity){
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.saveBatchEntity(clazz,listOfEntity);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}
	
	public List<T> updateBatchEntity(Class<T> clazz, List<T> listOfEntity){
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.updateBatchEntity(clazz,listOfEntity);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}
	
	@Override
	public List<T> findEntityByFk(Class<T> clazz,Class<T> fkclazz)
	{
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.findEntityByFk(clazz,fkclazz);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}
	
	@Override
	public T saveUpdateEntity(T entity)
	{
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.saveUpdateEntity(entity);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}
	
	@Override
	public List<T> findEntityByAliasCriteriaQuery(Class<T> clazz,String aliasName, Map<String,?> childPropertyEntityMap, Map<String,?> restrictionMap){
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.findEntityByAliasCriteriaQuery(clazz, aliasName, childPropertyEntityMap, restrictionMap);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}
	
	@Override
	public List<T> getEnityListByQuery(String tableName, Map<String,?> whereClause) {
	
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.getEnityListByQuery(tableName, whereClause);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}
	
	@Override
	public List<T> findAllEntityByProjectedColumn(Class clazz, String projectedColumn,Map<String,?> restrictionMap){
		
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.findAllEntityByProjectedColumn(clazz, projectedColumn, restrictionMap);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
		
	}
	
	@Override
	public List<T> findEntityByListOfValue(Class<T> clazz, String propertyName, List values){
		logger.info("CALLED");
		try
		{
			return genericDAOImpl.findEntityByListOfValue(clazz, propertyName, values);
		}
		catch (Exception ex)
		{
			logger.error("Exception Ocured->"+ex);
			throw ex;
		}
	}

}
