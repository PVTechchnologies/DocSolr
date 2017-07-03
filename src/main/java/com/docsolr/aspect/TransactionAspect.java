package com.docsolr.aspect;

import org.aspectj.lang.annotation.Aspect;

/**
 * 
 * @author Rajkiran
 *
 */
@Aspect
public class TransactionAspect {/*
	
	@Autowired
	IAccessService accessServiceImpl;
	
	@Before("execution(* com.maverick.dao.common.Impl.GenericDAOImpl..*(..))"
			+ " || execution(* com.maverick.dao.ChallengeDAO..*(..))")
	public void isAccessUpdateOnThisEntity(JoinPoint joinPoint){
		System.out.println("before update : "+joinPoint.getSignature().getName());
		if(RequestContextHolder.getRequestAttributes() != null){
			UserEntity currentUser = CommonUtil.getCurrentSessionUser();
			if(currentUser != null && currentUser.getProfiles() !=null && Access.STANDARDUSER.getValue().equalsIgnoreCase(currentUser.getProfiles().getProfilename())){
				try {
					if(joinPoint.getArgs().length > 0){
						boolean hasAccess = true;
						String entityClass = Access.childEntityMap.get(joinPoint.getArgs()[0].getClass().getSimpleName()) == null ?
							joinPoint.getArgs()[0].getClass().getSimpleName() : Access.childEntityMap.get(joinPoint.getArgs()[0].getClass().getSimpleName());
						if(Access.globalDefalutAccessEntityMap.get(entityClass) == null ){
							if(Access.CREATE.getMethodSet().contains(joinPoint.getSignature().getName())){
								hasAccess = accessServiceImpl.allowInsert(currentUser.getProfiles(),new Module(entityClass,null));
							}else if(Access.DELETE.getMethodSet().contains(joinPoint.getSignature().getName())){
								hasAccess = accessServiceImpl.allowDelete(currentUser.getProfiles(),new Module(entityClass,null));
							}else if(Access.UPDATE.getMethodSet().contains(joinPoint.getSignature().getName())){
								hasAccess = accessServiceImpl.allowUpdate(currentUser.getProfiles(),new Module(entityClass,null));
							}
						}
						if(!hasAccess){
							throw new org.springframework.security.access.AccessDeniedException("403");
						}
					}
				} catch (AccessDeniedException ade){
					throw ade;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
*/}
