package com.docsolr.entity;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "SiteFileInfo")
public class SiteFileInfo extends BaseEntity{
	


	private String fileName;
	private String fileRelativeURL;
	private String siteURL;
	private Date fileCreatedDate;
	private Date fileLastModifiedDate;
	
	
	public Date getFileLastModifiedDate() {
		return fileLastModifiedDate;
	}
	public void setFileLastModifiedDate(Date fileLastModifiedDate) {
		this.fileLastModifiedDate = fileLastModifiedDate;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileRelativeURL() {
		return fileRelativeURL;
	}
	public void setFileRelativeURL(String fileRelativeURL) {
		this.fileRelativeURL = fileRelativeURL;
	}
	public String getSiteURL() {
		return siteURL;
	}
	public void setSiteURL(String siteURL) {
		this.siteURL = siteURL;
	}
	
	
	public Date getFileCreatedDate() {
		return fileCreatedDate;
	}
	public void setFileCreatedDate(Date fileCreatedDate) {
		this.fileCreatedDate = fileCreatedDate;
	}
	


}
