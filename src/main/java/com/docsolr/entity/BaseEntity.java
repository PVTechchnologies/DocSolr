package com.docsolr.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.log4j.Logger;

@MappedSuperclass
public class BaseEntity implements Serializable,Cloneable{
	/*
	  `ID` INT NOT NULL ,
	  `CREATED_BY_ID` INT NOT NULL ,
	  `CREATED_TIME` TIMESTAMP NOT NULL ,
	  `UPDATED_BY_ID` INT NULL ,
	  `UPDATED_TIME` TIMESTAMP NULL ,
	  
    */
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final static Logger logger = Logger.getLogger(BaseEntity.class);	
    
    /**
     * Each entity must have a id that is a primary key.
     */
	
    protected Long id;
    
    protected Long createdById;
    
    protected Timestamp createDate;
    
    protected Long updatedById;
    
    protected Timestamp updatedDate;
    
   
	protected boolean active = false;
    
    public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public BaseEntity() {
		super();
	}


	public BaseEntity(Long id) {
		super();
		this.id = id;
	}


	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(updatable=false)
	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	@Column(updatable=false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		if(createDate!=null)
		this.createDate =new java.sql.Timestamp( createDate.getTime());
	}

	public Long getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Long updatedById) {
		this.updatedById = updatedById;
	}
	
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		if(updatedDate!=null)
		this.updatedDate = new Timestamp(updatedDate.getTime());
	}
	@PrePersist
	protected void onCreate() {
		createDate = new Timestamp(new Date().getTime());
		updatedDate = createDate;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedDate = new Timestamp(new Date().getTime());
	}
}
