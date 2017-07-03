package com.docsolr.security.rest;

import java.io.Serializable;

public class RestAuthenticationDetails implements Serializable {
	
	private static final long serialVersionUID = -4318794418544028559L;
	
	private String username; 
	private Long accountId;
	
	private String token;
	
	public RestAuthenticationDetails(String username, Long accountId){
		this.username = username;
		this.accountId = accountId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	
	

}
