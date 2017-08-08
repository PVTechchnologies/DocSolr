package com.docsolr.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Yadav
 *
 */
@Entity
@Table(name="salesforcesetup")

public class SalesforceSetup extends BaseEntity {

	private Account account;
	
	private String salesforceOrgId ;
	
	private String salesforceOrgType ;
	
	private Users user ;
	
	private String crawlFrequency ;

	@ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "accountId")
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "userId")
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getSalesforceOrgId() {
		return salesforceOrgId;
	}

	public void setSalesforceOrgId(String salesforceOrgId) {
		this.salesforceOrgId = salesforceOrgId;
	}

	public String getSalesforceOrgType() {
		return salesforceOrgType;
	}

	public void setSalesforceOrgType(String salesforceOrgType) {
		this.salesforceOrgType = salesforceOrgType;
	}


	public String getCrawlFrequency() {
		return crawlFrequency;
	}

	public void setCrawlFrequency(String crawlFrequency) {
		this.crawlFrequency = crawlFrequency;
	}
	
}
