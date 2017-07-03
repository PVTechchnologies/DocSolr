package com.docsolr.interceptors;

import java.util.Date;

public interface Auditable {
	public void setDateCreated(Date dateCreated);
	public void setDateUpdated(Date dateUpdated);
}
