package com.docsolr.interceptors;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class AuditableInterceptor extends EmptyInterceptor{

	private static final long serialVersionUID = 7618578890944004277L;
	
	@Override
	public boolean onFlushDirty(
			Object entity, 
			Serializable id, 
			Object[] currentState, 
			Object[] previousState, 
			String[] propertyNames, 
			Type[] types) {
		
		if (entity instanceof Auditable) {
            for (int i = 0; i < propertyNames.length; i++) {
                if ("dateCreated".equals(propertyNames[i])) {
                    currentState[i] = new Date();
                    return true;
                }
            }
        }
		
		return false;
	}

    @Override	
    public boolean onSave(Object entity,
            Serializable id,
            Object[] state,
            String[] propertyNames,
            Type[] types) {

        if (entity instanceof Auditable) {
            for (int i = 0; i < propertyNames.length; i++) {
                if ("dateCreated".equals(propertyNames[i])) {
                    state[i] = new Date();
                    return true;
                }
            }
        }
        return false;
    }

}
