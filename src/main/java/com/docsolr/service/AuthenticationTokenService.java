package com.docsolr.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.docsolr.configuration.SecurityUtil;
import com.docsolr.dao.AuthenticationTokenDao;
import com.docsolr.entity.AuthenticationToken;
import com.docsolr.entity.Users;
import com.docsolr.entity.AuthenticationToken.TokenType;



public class AuthenticationTokenService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>{

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	AuthenticationTokenDao tokenRepo;
	
	@Autowired
	UserService userService;

	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		
		String principal = (String) token.getPrincipal();
		
		AuthenticationToken authenticationToken = tokenRepo.findByToken(principal);
		
		if(authenticationToken == null){
			throw new UsernameNotFoundException("Invalid Token");
		}
		
		//CurrentUser currentUser = ApplicationContextProvider.getApplicationContext().getBean(CurrentUser.class);
		
		
		try{
			authenticationToken.validate(TokenType.AUTHENTICATION);
		}
		catch(Exception e){
			throw new UsernameNotFoundException(e.getMessage());
		}
		
		Users userEntity = userService.findByUsername(authenticationToken.getAccountId(), authenticationToken.getUsername());
		UserDetails user = SecurityUtil.adaptUserDetailsFromUser(userEntity);
		if(userEntity == null){
			//tokenRepo.delete(authenticationToken);
			throw new UsernameNotFoundException("No such user exists");
		}
		
		System.out.println("===AuthenticationTokenService=====loadUserDetails==" + userEntity.toString());
		
		//currentUser.setUser(userEntity);
		
		return user;
	}
}

