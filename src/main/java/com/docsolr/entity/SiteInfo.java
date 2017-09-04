package com.docsolr.entity;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name="siteinfo")
public class SiteInfo  implements Serializable  {

	
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="siteName")
	private String siteName;
	
	@Column(name="siteURL")
	private String siteURL;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteURL() {
		return siteURL;
	}
	public void setSiteURL(String siteURL) {
		this.siteURL = siteURL;
	}


}
