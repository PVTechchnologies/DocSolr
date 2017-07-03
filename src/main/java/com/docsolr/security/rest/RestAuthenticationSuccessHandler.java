package com.docsolr.security.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.docsolr.entity.Users;
import com.docsolr.service.UserService;
import com.docsolr.util.CommonUtil;

public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
	
	@Autowired
	UserService userService;
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		System.out.println("=========authentication=======" + authentication);
		
		RestAuthenticationDetails authenticationDetails = (RestAuthenticationDetails) authentication.getDetails();
		
		Users userEntity = userService.findByUsername(authenticationDetails.getAccountId(), authenticationDetails.getUsername());
		
		Map map = new HashMap<>();
    	
    	map.put("username", authentication.getName());
    	map.put("lastname", userEntity.getLastName());
    	map.put("firstname", userEntity.getFirstName());
    	map.put(AUTH_HEADER_NAME, authenticationDetails.getToken());
    	
    	
    	
    	CommonUtil.returnSuccess(response, map);
    	
    	
    }

}