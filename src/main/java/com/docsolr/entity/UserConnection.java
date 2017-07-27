package com.docsolr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="userconnection")
@Inheritance(strategy=InheritanceType.JOINED)
public class UserConnection{
	public UserConnection(){
		
	}
	 
	public UserConnection(String userId, String providerId, String providerUserId, String displayName,
			String profileUrl, String imageUrl, String secret, String refreshToken, Long expireTime, Integer rank,
			String accessToken, String email) {
		super();
		this.userId = userId;
		this.providerId = providerId;
		this.providerUserId = providerUserId;
		this.displayName = displayName;
		this.profileUrl = profileUrl;
		this.imageUrl = imageUrl;
		this.secret = secret;
		this.refreshToken = refreshToken;
		this.expireTime = expireTime;
		this.rank = rank;
		this.accessToken = accessToken;
		this.email = email;
	}

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	private String userId;
	
	private String providerId;
	
	private String providerUserId;
	
	private String displayName;
	
	private String profileUrl;
	
	private String imageUrl;
	
	private String secret;
	
	private String refreshToken;
	
	private Long expireTime;
	
	private Integer rank;
	
	private String accessToken;
	
	private String email;
	
	@Id
	@Column(name="userId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name="providerId")
	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	@Column(name="providerUserId")
	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	@Column(name="displayName")
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name="profileUrl")
	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	@Column(name="imageUrl")
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Column(name="secret")
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Column(name="refreshToken")
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Column(name="expireTime")
	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	@Column(name="rank")
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Column(name="accessToken")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if(email != null){
			this.userId = email;
		}
		this.email = email;
	}
}
