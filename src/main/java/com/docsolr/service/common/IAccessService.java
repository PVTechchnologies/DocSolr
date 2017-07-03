package com.docsolr.service.common;

import com.docsolr.entity.admin.Profiles;
import com.fasterxml.jackson.databind.Module;

public interface IAccessService {
	public boolean allowView(Profiles userType, Module module) throws Exception;
	public boolean allowInsert(Profiles userType, Module module) throws Exception;
	public boolean allowUpdate(Profiles userType, Module module) throws Exception;
	public boolean allowDelete(Profiles userType, Module module) throws Exception;
}
