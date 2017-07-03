package com.docsolr.security.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import com.docsolr.dao.AuthenticationTokenDao;
import com.docsolr.entity.AuthenticationToken;
import com.docsolr.entity.AuthenticationToken.TokenType;

public class RestSessionAuthenticationStrategy implements SessionAuthenticationStrategy{

	@Autowired
	AuthenticationTokenDao tokenRepo;
	
	@Override
	public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
		
		System.out.println("============RestSessionAuthenticationStrategy================");
		
		if(authentication.isAuthenticated()){
			
			RestAuthenticationDetails authenticationDetail = (RestAuthenticationDetails)authentication.getDetails();
			
			AuthenticationToken authenticationToken = new AuthenticationToken(authenticationDetail.getUsername(), authenticationDetail.getAccountId(), TokenType.AUTHENTICATION);
			
			try {
				//tokenRepo.save(authenticationToken);
				authenticationDetail.setToken(authenticationToken.getToken());				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
