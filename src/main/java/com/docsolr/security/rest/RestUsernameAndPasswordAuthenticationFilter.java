package com.docsolr.security.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.docsolr.dto.CurrentUserVO;

public class RestUsernameAndPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
   //~ Static fields/initializers =====================================================================================

   public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
   public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
   private boolean postOnly = true;

   //~ Constructors ===================================================================================================

   public RestUsernameAndPasswordAuthenticationFilter() {
	   super("/api/login");
   }

   //~ Methods ========================================================================================================

   public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
       System.out.println("======RestUsernameAndPasswordAuthenticationFilter================");
	   
	   if (postOnly && !request.getMethod().equals("POST")) {
           throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
       }
       
       InputStream ip = request.getInputStream();
       
       JSONObject json = new JSONObject(new JSONTokener(ip));
       
       String username = getValue(json, SPRING_SECURITY_FORM_USERNAME_KEY);
       String password = getValue(json, SPRING_SECURITY_FORM_PASSWORD_KEY);

       if (username == null) {
           username = "";
       }

       if (password == null) {
           password = "";
       }

       username = username.trim();

       UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

       // Allow subclasses to set the "details" property
       setDetails(request, authRequest);

       return this.getAuthenticationManager().authenticate(authRequest);
   }

   private String getValue(JSONObject json, String property) {
	   try{
		   return json.getString(property);
	   }
	   catch(Exception e){
		   return null;
	   }	
   }

   protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
	   CurrentUserVO currentTenant = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean(CurrentUserVO.class);
	  // Account  account = currentTenant.getAccount();
	   Long accountId =  1L;
	 
       authRequest.setDetails(new RestAuthenticationDetails((String)authRequest.getPrincipal(), accountId));
   }
}

