package com.docsolr.entity;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name="SiteInfo")
public class SiteInfo  extends BaseEntity {

	
	
	

	private String siteName;
	
	private long parentSiteId;

	public long getParentSiteId() {
		return parentSiteId;
	}
	public void setParentSiteId(long parentSiteId) {
		this.parentSiteId = parentSiteId;
	}
	private String siteURL;

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
