package com.docsolr.web.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class CorsFilter extends OncePerRequestFilter  {
    static final String ORIGIN = "Origin";

    @Override
    @SuppressWarnings("rawtypes")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
		Enumeration headdd = request.getHeaderNames();
        
        String dddd = "Content-Type,X-TENANT-ID,X-AUTH-TOKEN";
        
        while(headdd.hasMoreElements()){
        	dddd += ("," + headdd.nextElement());	
        }
        
        /*if(dddd.startsWith(",")){
        	dddd = dddd.substring(1);
        }*/
        
        
        response.setHeader("Access-Control-Allow-Origin", "*");//* or origin as u prefer
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", dddd);
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        
        if (request.getMethod().equals("OPTIONS")) {
            try {
                response.getWriter().print("OK");
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
        	filterChain.doFilter(request, response);
        }
    }

}