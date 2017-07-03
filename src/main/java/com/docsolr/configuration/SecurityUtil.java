package com.docsolr.configuration;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.docsolr.dto.LoggedInUser;
import com.docsolr.entity.Account;
import com.docsolr.entity.Users;
public class SecurityUtil {
	
	
	public static UserDetails getCurrentUser() {
		UserDetails userDetails = null;
		//get security principle from spring
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			userDetails = (UserDetails)principal;
		} 
		return userDetails;
	}
	
	/**
	 * to convert user entity into spring based user
	 * @param user
	 * @return
	 */
	public static UserDetails adaptUserDetailsFromUser(Users user) {
		LoggedInUser userDetails = null;
		if (user != null) {
			userDetails = new LoggedInUser();
			userDetails.setId(user.getId());
			userDetails.setPassword(user.getPassword());
			userDetails.setAccountNonExpired(true);
			userDetails.setAccountNonLocked(true);
			userDetails.setCredentialsNonExpired(true);
			userDetails.setEnabled(user.isEnabled());
			userDetails.setAccount(user.getAccount());
			userDetails.setUsername(user.getEmail());
			userDetails.setAuthorities(user.getAuthorities());
		}
		return userDetails;
	}
	

}
