package com.docsolr.aspect;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.docsolr.entity.BaseEntity;
import com.docsolr.entity.Users;
import com.docsolr.util.CommonUtil;

/**
 * 
 * @author Rajkiran
 * Since 30-Dec-2017
 * This Class is to behave intercepter based on the aspect used for each method.
 */

@Aspect
public class GenericAspect {
	
	
	/**
	 * @author Rajkiran
	 * @param joinPoint
	 * This method will be executed before logged in user persist any entity in database table.
	 */
	@Before("execution(* com.docsolr.common.dao.Impl.GenericDAOImpl.save*(..))")
	public void updateBaseEntityRequiredFields(JoinPoint joinPoint){
		
			List<BaseEntity> listOfBaseEntity = new ArrayList<BaseEntity>();
			//List<BasicEntity> listOfBasicEntity = new ArrayList<BasicEntity>();
			if(joinPoint.getArgs() != null && joinPoint.getArgs().length ==1 ){
				if(joinPoint.getArgs()[0] instanceof BaseEntity){
					listOfBaseEntity.add(((BaseEntity)joinPoint.getArgs()[0]));
				}/*else if(joinPoint.getArgs()[0] instanceof BasicEntity){
					listOfBasicEntity.add(((BasicEntity)joinPoint.getArgs()[0]));
				}*/
			}else if(joinPoint.getArgs() != null && joinPoint.getArgs().length ==2 ){
				listOfBaseEntity.addAll(((List<BaseEntity>)joinPoint.getArgs()[1]));
			}
			setupBaseEntity(listOfBaseEntity);
	}
	
	/**
	 * @author Rajkiran
	 * @param listOfBaseEntity
	 * @param currentUser
	 * Method to setup the base entity details.
	 */
	private void setupBaseEntity(List<BaseEntity> listOfBaseEntity){
		Users currentUser = CommonUtil.getCurrentSessionUser();
		if(!listOfBaseEntity.isEmpty()){
			for (BaseEntity baseEntity : listOfBaseEntity) {
				java.sql.Timestamp createDate = new java.sql.Timestamp(new Date().getTime());
				baseEntity.setCreateDate(createDate);
				baseEntity.setUpdatedDate(createDate);
				if(currentUser != null){
					if(baseEntity.getCreatedById() == null){
						baseEntity.setCreatedById(currentUser.getId());
					}
					baseEntity.setUpdatedById(currentUser.getId());
				}
			}
		}/*else if(!listOfBasicEntity.isEmpty()){
			for (BasicEntity baseEntity : listOfBasicEntity) {
				java.sql.Timestamp createDate = new java.sql.Timestamp(new Date().getTime());
				if(baseEntity.getDateCreated() == null){
					baseEntity.setDateCreated(createDate);
				}
				baseEntity.setDateUpdated(createDate);
			}
		}*/
		
	}
	
}
