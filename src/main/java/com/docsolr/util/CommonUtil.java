package com.docsolr.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.docsolr.entity.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
public class CommonUtil {

	/**
	 * to get current spring security session
	 * @return
	 */
	public static HttpSession getCurrentSession() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true);
	}
	
	public static Object returnSuccess(Object data){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", true);
		map.put("data", data);
		map.put("errorMessage", "");
		return map;
	}
	
	public static void returnSuccess(HttpServletResponse response, Object data){
		Object obj  = returnSuccess(data);
        
        ObjectMapper objectMapper = new ObjectMapper();
        try {
			response.getOutputStream().write(objectMapper.writeValueAsBytes(obj));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object returnError(Exception exception,String errorMessage){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", false);
		map.put("data", "");
		if(StringUtils.isEmpty(errorMessage) && exception != null){
			errorMessage = exception.getLocalizedMessage();
		}
		map.put("errorMessage", errorMessage);
		return map;
	}
	
	
	public static void returnError(HttpServletResponse response, int statusCode, Exception exception, String error){
		Object obj  = returnError(exception, error);
        
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(statusCode);
        try {
			response.getOutputStream().write(objectMapper.writeValueAsBytes(obj));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Rajkiran
	 * @return
	 */
	public static Users getCurrentSessionUser(){
		HttpSession httpSession = getCurrentSession();
		Users	currentUser = null;
		if(httpSession != null) {
			//get current user
			Object object = httpSession.getAttribute("user");
			if (object != null) {
				currentUser = (Users) object;
				
			}
		}
		return currentUser;
	}
}
