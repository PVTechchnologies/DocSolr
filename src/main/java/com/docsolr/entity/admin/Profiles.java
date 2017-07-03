package com.docsolr.entity.admin;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.docsolr.entity.BaseEntity;
import com.docsolr.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Rajkiran
 *
 */
@Entity
@Table(name = "profiles", uniqueConstraints = @UniqueConstraint(columnNames = "profilename"))
public class Profiles extends BaseEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String profilename;
	
	@JsonIgnore
	private Set<Users> users;
	
	public Profiles() {
	}

	public Profiles(long id, String profilename) {
		this.id = id;
		this.profilename = profilename;
	}

	@Column(name = "profilename", unique = true, nullable = false, length = 100)
	public String getProfilename() {
		return this.profilename;
	}

	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}

	@OneToMany(targetEntity = Users.class, mappedBy = "profiles")
	public Set<Users> getUsers() {
		return users;
	}

	public void setUsers(Set<Users> users) {
		this.users = users;
	}

}
