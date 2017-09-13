package com.docsolr.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="siteFolders")
public class SiteFolders extends BaseEntity {
	
	private String uniqueId;
	private long siteLibraryId;

	private String name;
	private Date timeLastModified;
	private Date timeCreated;
	private int itemCount;
	private String serverRelativeURL;
	private String siteName;
	private String hostURL;
	
	public long getSiteLibraryId() {
		return siteLibraryId;
	}

	public void setSiteLibraryId(long siteLibraryId) {
		this.siteLibraryId = siteLibraryId;
	}

	
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
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
