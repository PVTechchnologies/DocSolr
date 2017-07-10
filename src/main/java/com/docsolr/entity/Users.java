package com.docsolr.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.docsolr.entity.admin.Profiles;

@Entity
@Table(name="users")
@Inheritance(strategy=InheritanceType.JOINED)
public class Users extends BaseEntity {
	
	 
	private static final long serialVersionUID = 1L;
	
	protected Boolean isActive =true;
	
	protected Boolean isAdmin=false;
	
	private String email;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private Set<UserAuthority> authorities = new HashSet<UserAuthority>();
	

	private Profiles profiles;
	
	private Account account;
	
	private boolean enabled;
	
	
	public Users() {
		super();
	}
	
	public Users(Long userId) {
		super();
		this.id = userId;
	}

	public Users(String firstName, String lastName,String email, String password, boolean enabled,  Boolean isActive) {
		this();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isActive = isActive;
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
	
	
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Transient
	public String getFullName(){
		return getFirstName()+" "+getLastName(); 
	}
	
	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	
	
	@Override
	public String toString() {
		return "UserEntity [email=" + email + ", password=" + password + ", firstName=" + firstName + ", lastName="
				+ lastName + "]";
	}

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "usersuserauthority", joinColumns = {
			@JoinColumn(name = "userId", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "authoritieId", nullable = false, updatable = false) })
	
	public Set<UserAuthority> getAuthorities() {
		return authorities;
	}
	
	public void setAuthorities(Set<UserAuthority> authorities) {
		this.authorities = authorities;
	}
	
	public void setEnabled(Boolean enabled) {
		if(enabled == null){
			this.enabled = true;
		}
		else{
			this.enabled = enabled;
		}
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	@ManyToOne(targetEntity = Profiles.class)
    @JoinColumn(name = "profileId")
	public Profiles getProfiles() {
		return profiles;
	}

	public void setProfiles(Profiles profiles) {
		this.profiles = profiles;
	}
	
	@ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "accountId")
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
