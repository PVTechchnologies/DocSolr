package com.docsolr.entity;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthentication implements Authentication {
	
	private static final long serialVersionUID = 1L;
	
	private final Users user;
	private boolean authenticated = true;

	public UserAuthentication(Users user) {
		this.user = user;
	}

	
	
	public Users getCurrentUser(){
		return user;
	}

	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	public String getName() {
		return user.getEmail();
	}



	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getAuthorities()
	 */
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}



	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getCredentials()
	 */
	public Object getCredentials() {
		return user.getPassword();
	}



	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getDetails()
	 */
	public Object getDetails() {
		return user;
	}



	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getPrincipal()
	 */
	public Object getPrincipal() {
		return user;
	}



	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#isAuthenticated()
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}



	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#setAuthenticated(boolean)
	 */
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authenticated = authenticated;
		
	}
}

