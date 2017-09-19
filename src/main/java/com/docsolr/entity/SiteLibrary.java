package com.docsolr.entity;



import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="sitelibrary")
public class SiteLibrary  extends BaseEntity{  



	private Long siteId;
	private String guid;
	private String name;
	private Date timeLastModified;
	private Date timeCreated;
	private int itemCount;
	private String serverRelativeURL;
	

	private String siteName;
	private String hostURL;
	private String siteURL;


	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	
	public String getSiteURL() {
		return siteURL;
	}

	public void setSiteURL(String siteURL) {
		this.siteURL = siteURL;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTimeLastModified() {
		return timeLastModified;
	}

	public void setTimeLastModified(Date timeLastModified) {
		this.timeLastModified = timeLastModified;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public String getServerRelativeURL() {
		return serverRelativeURL;
	}

	public void setServerRelativeURL(String serverRelativeURL) {
		this.serverRelativeURL = serverRelativeURL;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getHostURL() {
		return hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}



}
