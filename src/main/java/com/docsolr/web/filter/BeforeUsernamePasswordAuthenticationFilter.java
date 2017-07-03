/**
 * 
 */
package com.docsolr.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Rajkiran Dewara
 *
 */
public class BeforeUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	   //~ Static fields/initializers =====================================================================================

	   //~ Constructors ===================================================================================================

	   private boolean postOnly = true;
	   public BeforeUsernamePasswordAuthenticationFilter() {
		   super();
	   }

	   //~ Methods ========================================================================================================

	   public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
	        if (postOnly && !request.getMethod().equals("POST")) {
	            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
	        }

	        String username = obtainUsername(request);
	        String password = obtainPassword(request);

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
	        request.getSession().setAttribute("redirectUrl",request.getParameter("redirectUrl"));
	        return this.getAuthenticationManager().authenticate(authRequest);
	    }


	
	
}
