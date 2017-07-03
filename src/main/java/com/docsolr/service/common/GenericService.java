package com.docsolr.service.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author rajkiran.dewara
 *
 * @param <T>
 */
public interface GenericService<T> {
	T saveEntity(T entity);
	T updateEntity(T entity);
	void deleteEntity(T entity);

	public T findEntityById(Class clazz, Serializable id);
	public List<T> findAllEntity(Class clazz);
	public List<T> findLimitedEntity(Class<T> clazz,int fetchSize,Map<String,?> restrictionMap,Map<String,Map<String,?>> restrictionChildEntityMap);
	
	public T getByProfileAndModule(Class clazzone,Class clazztwo, Serializable idone, Serializable idtwo);
	public List<T> saveBatchEntity(Class<T> clazz, List<T> listOfEntity);
	public List<T> updateBatchEntity(Class<T> clazz, List<T> listOfEntity);
	public List<T> findEntityByFk(Class<T> clazz, Class<T> fkclazz);
	public T saveUpdateEntity(T entity);
	public List<T> findEntityByAliasCriteriaQuery(Class<T> clazz,String aliasName, Map<String,?> childPropertyEntityMap, Map<String,?> restrictionMap);
	public List<T> getEnityListByQuery(String tableName, Map<String,?> whereClause);
	public List<T> findAllEntityByProjectedColumn(Class clazz, String projectedColumn,Map<String,?> restrictionMap);
	public List<T> findEntityByListOfValue(Class<T> clazz, String propertyName, List values);
	public List<T> findEntityByRestriction(Class<T> clazz,Map<String,?> restrictionMap);
}
