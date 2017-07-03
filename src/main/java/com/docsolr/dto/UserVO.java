package com.docsolr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVO {

	private String name;

	private long userId;

	private String email;

	private String password;

	private String firstName;

	private String lastName;


	public UserVO() {
		super();
	}

	public UserVO(String name, long userId) {
		super();
		this.name = name;
		this.userId = userId;
	}

	public UserVO(Long userId, String email, String password, String firstName,
			String lastName) {
		super();
		this.userId = userId == null ? 0 : userId;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.name = firstName == null ? "" : firstName;
		this.name = this.name + lastName == null ? "" : lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
