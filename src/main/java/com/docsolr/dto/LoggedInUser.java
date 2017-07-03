package com.docsolr.dto;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetails;

import com.docsolr.entity.Account;
import com.docsolr.entity.UserAuthority;

public class LoggedInUser implements Serializable, UserDetails  {
	
	private static final long serialVersionUID = 1L;
	private long id;
	private String password;
	private String username;
	
	//below fields are implemented from the userDetails entity
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	private Collection <UserAuthority> authorities;
	private Account account;
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	public Account getAccount() {
		return account;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the accountNonExpired
	 */
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}
	/**
	 * @param accountNonExpired the accountNonExpired to set
	 */
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	/**
	 * @return the accountNonLocked
	 */
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	/**
	 * @param accountNonLocked the accountNonLocked to set
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	/**
	 * @return the credentialsNonExpired
	 */
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	/**
	 * @param credentialsNonExpired the credentialsNonExpired to set
	 */
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/**
	 * @return the authorities
	 */
	public Collection<UserAuthority> getAuthorities() {
		return authorities;
	}
	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(Collection<UserAuthority> authorities) {
		this.authorities = authorities;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
