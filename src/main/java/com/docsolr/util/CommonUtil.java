package com.docsolr.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.docsolr.entity.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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
	public static Session geSession(){
		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		SessionFactory SF = config.buildSessionFactory();
		Session sess = SF.openSession();
		return sess;
		
	}
	
	
	public static Date convertStringToDate(String dateString) throws ParseException{
		Date dateRec = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateString);
		return dateRec;
	}
	public static String replaceSpace(String str) {
		String[] words = str.split(" ");
		StringBuilder sentence = new StringBuilder(words[0]);

		for (int i = 1; i < words.length; ++i) {
			sentence.append("%20");
			sentence.append(words[i]);
		}

		return sentence.toString();
	} 
	
	public static HttpURLConnection getConnectionForGetRequest(String endpointURL,String formDigestValue, String cookie) throws IOException{
		URL obj = new URL(endpointURL);
		HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
		connection.setRequestMethod( "GET" );
		connection.setRequestProperty("Accept", "application/json;odata=verbose");
		connection.setRequestProperty("Content-Type", "application/json;odata=verbose");
		connection.setRequestProperty("X-RequestDigest",formDigestValue);
		connection.setRequestProperty("Cookie", cookie);
		
		return connection;
	}
}
