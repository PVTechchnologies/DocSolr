package com.docsolr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.docsolr.configuration.SecurityUtil;
import com.docsolr.entity.Users;
import com.docsolr.service.common.GenericService;
/**
 * spring user detail service implementation
 * action j_spring_security_check
 * spring-security.xml
 * <beans:property name="defaultTargetUrl" value="/signin" />
 * /login#/login
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserService service;
	
	@Autowired
	private GenericService<Users> genericServiceImpl;
	/**
	 * override load user to load user from application database
	 */
	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException, DataAccessException {
		UserDetails userDetails =null;
		try {
			//Account currentTenant = ApplicationContextProvider.getApplicationContext().getBean(CurrentUser.class).getAccount();
			
			/*SecurityContextHolder.getContext().getAuthentication().getPrincipal();*/
			Users user = null;
			Map<String,String> restrictionMap  = new HashMap<String,String>();
			restrictionMap.put("email", arg0);
			List<Users> users = genericServiceImpl.findEntityByRestriction(Users.class, restrictionMap);
			if(users != null && users.size() == 1){
				user = users.get(0);
			}else{
				user = null;
			}
			/*if(currentTenant == null){
				user = service.findUserByUsername(null, arg0);
			}
			else{
				System.out.println("====currentTenant id=========" + currentTenant.getId());
				
				System.out.println("====currentTenant id=========" + arg0);
				user = service.findUserByUsername(currentTenant.getId(), arg0);
				
				System.out.println("==========user=========== " + user);
				
			}*/
			//fetch user from DAO
			
			if (user == null){
				throw new NoResultException("Invalid username or password");
			}
			
			//convert into spring user details
			userDetails = SecurityUtil.adaptUserDetailsFromUser(user);
		} catch (NoResultException e) {
			throw new UsernameNotFoundException(e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userDetails;
	}
}
