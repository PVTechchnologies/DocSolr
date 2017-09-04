package com.docsolr.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="siteFolders")
public class SiteFolders  implements Serializable  {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="uniqueId")
	private String uniqueId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="timeLastModified")
	private Date timeLastModified;
	
	@Column(name="timeCreated")
	private Date timeCreated;
	
	@Column(name="itemCount")
	private int itemCount;
	
	@Column(name="serverRelativeURL")
	private String serverRelativeURL;
	
	@Column(name="siteName")
	private String siteName;
	
	@Column(name="hostURL")
	private String hostURL;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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