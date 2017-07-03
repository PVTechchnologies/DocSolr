package com.docsolr.interceptors;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

public class AccessInterceptor extends AuditableInterceptor{


	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		super.onDelete(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		return super.onLoad(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		return super.onSave(entity, id, state, propertyNames, types);
	}

	@Override
	public void postFlush(Iterator entities) {
		super.postFlush(entities);
	}

	@Override
	public void preFlush(Iterator entities) {
		super.preFlush(entities);
	}

	@Override
	public Boolean isTransient(Object entity) {
		return super.isTransient(entity);
	}

	@Override
	public Object instantiate(String entityName, EntityMode entityMode, Serializable id) {
		return super.instantiate(entityName, entityMode, id);
	}

	@Override
	public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		return super.findDirty(entity, id, currentState, previousState, propertyNames, types);
	}

	@Override
	public String getEntityName(Object object) {
		return super.getEntityName(object);
	}

	@Override
	public Object getEntity(String entityName, Serializable id) {
		return super.getEntity(entityName, id);
	}

	@Override
	public void afterTransactionBegin(Transaction tx) {
		super.afterTransactionBegin(tx);
	}

	@Override
	public void afterTransactionCompletion(Transaction tx) {
		super.afterTransactionCompletion(tx);
	}

	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		super.beforeTransactionCompletion(tx);
	}

	@Override
	public String onPrepareStatement(String sql) {
		return super.onPrepareStatement(sql);
	}

	@Override
	public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
		super.onCollectionRemove(collection, key);
	}

	@Override
	public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
		super.onCollectionRecreate(collection, key);
	}

	@Override
	public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
		super.onCollectionUpdate(collection, key);
	}

}
