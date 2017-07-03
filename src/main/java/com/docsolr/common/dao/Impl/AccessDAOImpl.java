package com.docsolr.common.dao.Impl;

import org.springframework.stereotype.Repository;

import com.docsolr.common.dao.AccessDAO;
/**
 * 
 * @author Rajkiran
 *
 */
@Repository
public class AccessDAOImpl implements AccessDAO {/*
	
	Logger logger = Logger.getLogger(AccessDAOImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;

  *//**
   * @author Rajkiran
   *//*
  public List<ModulePermissions> getModulePermissions(Long profileId){
	  logger.info("saveEntity CALLED");
		Session session = null;
		List<ModulePermissions> listOfAccess = null;
		try
		{
			session = sessionFactory.openSession(new AccessInterceptor());
			Query query = session.createSQLQuery("Select mm.* from users us, profiles pf, modulepermissions mm, module ml"
					+ " where us.profileid = pf.id and mm.profileid = us.profileid and mm.profileid = pf.id  and mm.moduleid = ml.id and mm.profileid= :profileid ")
			.addEntity(ModulePermissions.class)
			.setParameter("profileid", profileId);
			listOfAccess = (java.util.ArrayList<ModulePermissions>)query.list();
		}
		catch (Exception ex)
		{
			logger.error("Exception occured->",ex);
			throw ex;
		}
		finally
		{
			if (session != null)
			{
				session.close();
			}
		}
		return listOfAccess;
  }
*/}
