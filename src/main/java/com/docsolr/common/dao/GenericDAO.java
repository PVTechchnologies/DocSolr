package com.docsolr.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.docsolr.entity.BaseEntity;

/**
 * 
 * @author rajkiran.dewara
 *
 * @param <T>
 */
public interface GenericDAO<T> {
	T saveEntity(T entity);
	T updateEntity(T entity);
	void deleteEntity(T entity);

	public T findEntityById(Class<T> clazz, Serializable id);
	public List<T> findAllEntity(Class<T> clazz);
	public List<T> findLimitedEntity(Class<T> clazz,int fetchSize,Map<String,?> restrictionMap,Map<String,Map<String,?>> restrictionChildEntityMap);
	public List<T> saveBatchEntity(Class<T> clazz, List<T> listOfEntity);
	public List<T> updateBatchEntity(Class<T> clazz, List<T> listOfEntity);
	public List<T> findEntityByFk(Class<T> clazz, Class<T> fkclazz);
	public T saveUpdateEntity(T entity);
	public T findUniqueEntity(Class<T> clazz, List<Criterion> restrictions);
	public Map<String, String> getMasterKeyValueMap(String groupName);
	public List<T> findEntityByAliasCriteriaQuery(Class<T> clazz,String aliasName, Map<String,?> childPropertyEntityMap, Map<String,?> restrictionMap);
	public List<T> getEnityListByQuery(String tableName, Map<String,?> whereClause);
	public List<T> findAllEntityByProjectedColumn(Class clazz, String projectedColumn,Map<String,?> restrictionMap);
	public List<T> findEntityByListOfValue(Class<T> clazz, String propertyName, List values);
	public Map<Long, String> getKeyValueMap(String tablename, String keycolumn, String valuecolumn,String whereClause);
	public List<T> findEntityByRestriction(Class<T> clazz,Map<String,?> restrictionMap);
	public List<T> saveUpdateBatchEntity(Class<T> clazz, List<T> listOfEntity);
	public Map<String, T> getKeyValueMapString(String tablename, String keycolumn, String valuecolumn,String whereClause);
}
