package com.docsolr.util;

import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.docsolr.dto.LoggedInUser;
import com.docsolr.entity.Users;
/**
 * util class for security based methods
 * @author Yadav
 *
 */
public class SecurityUtil {
	/**
	 * to get current logged in user name
	 * @return
	 */
	public static String getCurrentUserName() {
		String username = null;
		//get security principle from spring
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}
		return username;
	}
	
	public static Users getSessionUser(){
		return (Users) CommonUtil.getCurrentSession().getAttribute("user");
	}
	
	
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
	//??		userDetails.setAccountId(user.getAccountId());
			userDetails.setUsername(user.getEmail());
			userDetails.setAuthorities(user.getAuthorities());
		}
		return userDetails;
	}
	
	/**
	 * to signin user manually 
	 * @param user
	 * @return
	 */
	public static Authentication signInUser(Users user) {
		//convert user into user detail
		UserDetails userDetails = adaptUserDetailsFromUser(user);
		//create authentication
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				userDetails, user.getPassword(), userDetails.getAuthorities());
		//get security context
		SecurityContext securityContext = SecurityContextHolder.getContext();
		//set authentication
	    securityContext.setAuthentication(authentication);
	    //get session and set context in session
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true);
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
		session.setAttribute("user", user);
		return authentication;
	}

}
