package com.docsolr.web.filter;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.GenericFilterBean;


public class ForceFullySessionClearFilter extends GenericFilterBean{

		@Override
		public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
			   HttpServletRequest request = (HttpServletRequest) req;
		       HttpServletResponse response = (HttpServletResponse) res;
		       
		       	Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // concern you
				
				
				System.out.println("===============PRE===================");
				System.out.println("==================================");
				System.out.println("==============" + auth + "========");
				System.out.println("==================================");
				System.out.println("==================================");
		       
		       
		       chain.doFilter(request, response);
		       
		       HttpSession session = request.getSession(false);
		       
		       if(session != null){
		    	   System.out.println("===========================");
		    	   
		    	   System.out.println(session);
		    	   
		    	   session.invalidate();
		       }
				auth = SecurityContextHolder.getContext().getAuthentication(); // concern you
				
				
				System.out.println("==============POST====================");
				System.out.println("==================================");
				System.out.println("==============" + auth + "========");
				System.out.println("==================================");
				System.out.println("==================================");
			    SecurityContextLogoutHandler ctxLogOut = new SecurityContextLogoutHandler(); // concern you

			    if( auth != null ){
			        ctxLogOut.logout(request, response, auth); // concern you
			    }
			
		}
	}

