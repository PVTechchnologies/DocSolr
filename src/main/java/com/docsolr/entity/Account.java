package com.docsolr.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account extends BaseEntity {

	private static final long serialVersionUID = 4861901849265550026L;
	
	private String name;
	private String url;
	public Account(){
		
	}
	public Account(Long id){
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}	

}
