package com.docsolr.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "authenticationtoken")
public class AuthenticationToken extends BaseEntity{
	
	public static enum TokenType {AUTHENTICATION, ACTIVATION, FORGET_PASSWORD,VERIFY_EMAIL};
	
	private static final long serialVersionUID = 7707944517067010022L;
	
	private String username;
	private Long accountId;
	
	private String token;
	
	private long expireTime;
	
	private TokenType tokenType;
	
	protected AuthenticationToken(){
		
	}
	
	public AuthenticationToken(String username, Long tenantId, TokenType tokenType ) {
		super();
		this.username = username;
		this.accountId = tenantId;
		this.expireTime = getExpiry(tokenType);
		this.tokenType = tokenType;
		
		this.token = createToken();
	}
	
	private long getExpiry( TokenType tokenType){
		Calendar calender = Calendar.getInstance();
		
		switch(tokenType){
		
			case FORGET_PASSWORD:
				calender.add(Calendar.DAY_OF_YEAR, 1);
				return calender.getTime().getTime();
			default:
				return 0L;
		}
	}
	
	
	public void validate(TokenType tokenType){
		if(this.expireTime !=0 && new Date().getTime() > this.expireTime){
			throw new RuntimeException("Token Expired");
		}
		
		if(tokenType != this.tokenType){
			throw new RuntimeException("Invalid Token");
		}
	}
	
	private String createToken() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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

	public void setAccountId(Long tenantId) {
		this.accountId = tenantId;
	}
	
	

}
