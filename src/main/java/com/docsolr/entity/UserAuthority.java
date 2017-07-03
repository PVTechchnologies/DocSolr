package com.docsolr.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name ="userauthority")
public class UserAuthority extends BaseEntity implements GrantedAuthority{
	
	public static enum Roles {ROLE_USER, ROLE_ADMIN, ROLE_CUSTOMER};
	
	private static final long serialVersionUID = 3686658127196915992L;
	
	private String authority;
	
	UserAuthority(){
		
	}
	
	public UserAuthority(Roles authority){
		this.authority = authority.toString();		
	}
	
	public String getAuthority() {
		return authority;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj!= null && obj instanceof UserAuthority){
			return this.authority.equals(((UserAuthority) obj).authority);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.authority.hashCode();
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	public String toString() {
		return this.authority;
	}
	
}
