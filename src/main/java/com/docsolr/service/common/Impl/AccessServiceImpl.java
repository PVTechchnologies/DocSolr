package com.docsolr.service.common.Impl;


import org.springframework.stereotype.Service;

import com.docsolr.entity.admin.Profiles;
import com.docsolr.service.common.IAccessService;
import com.fasterxml.jackson.databind.Module;

/**
 * 
 * @author Rajkiran
 *
 */
@Service
public class AccessServiceImpl implements IAccessService {

	/* (non-Javadoc)
	 * @see com.docsolr.service.common.IAccessService#allowView(com.docsolr.entity.Profiles, com.fasterxml.jackson.databind.Module)
	 */
	@Override
	public boolean allowView(Profiles userType, Module module) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.docsolr.service.common.IAccessService#allowInsert(com.docsolr.entity.Profiles, com.fasterxml.jackson.databind.Module)
	 */
	@Override
	public boolean allowInsert(Profiles userType, Module module) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.docsolr.service.common.IAccessService#allowUpdate(com.docsolr.entity.Profiles, com.fasterxml.jackson.databind.Module)
	 */
	@Override
	public boolean allowUpdate(Profiles userType, Module module) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.docsolr.service.common.IAccessService#allowDelete(com.docsolr.entity.Profiles, com.fasterxml.jackson.databind.Module)
	 */
	@Override
	public boolean allowDelete(Profiles userType, Module module) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}/*

	@Autowired
	GenericService<ModulePermissions> genericService;

	@Autowired
	AccessDAO accessDAOImpl;
	
	*//**
	 * @author Rajkiran
	 * @param profile
	 * @param module
	 * @return
	 * @throws Exception
	 *//*
	public ModulePermissions getByProfileAndModule(Profiles profile, Module module) throws Exception {
		List<ModulePermissions> listOfAccess = getAcl(profile.getId());
		for (ModulePermissions modulePermissions : listOfAccess) {
			if(module.getEntityclass() != null){
				if(module.getEntityclass().equalsIgnoreCase(modulePermissions.getModule().getEntityclass())){
					return modulePermissions;
				}
			}else if(module.getUrl() != null){
				if(module.getUrl().equalsIgnoreCase(modulePermissions.getModule().getUrl())){
					return modulePermissions;
				}
			}
		}
		return new ModulePermissions();
		
	}
	
	public List<ModulePermissions> getAcl(Long profileId){
		return accessDAOImpl.getModulePermissions(profileId);
	}

	public boolean allowView(Profiles userType, Module module) throws Exception {
		return this.getByProfileAndModule(userType, module).isRead();
	}

	public boolean allowInsert(Profiles userType, Module module) throws Exception {
		return this.getByProfileAndModule(userType, module).isCreate();
	}

	public boolean allowUpdate(Profiles userType, Module module) throws Exception {
		return this.getByProfileAndModule(userType, module).isUpdate();
	}

	public boolean allowDelete(Profiles userType, Module module) throws Exception {
		return this.getByProfileAndModule(userType, module).isDelete();
	}
	
*/}
