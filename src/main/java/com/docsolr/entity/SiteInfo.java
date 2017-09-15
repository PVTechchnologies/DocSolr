package com.docsolr.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;


@Entity
@Table(name="siteinfo")
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
