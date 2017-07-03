package com.docsolr.dao;


import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.docsolr.entity.Users;

@Repository
public class UserDAO{

	protected final static Logger logger = Logger.getLogger(UserDAO.class);

	@Autowired
	protected SessionFactory factory;
	
	/**
	 * to get user from user name
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public Users findUserByUsername(Long accountId, String userName) throws Exception {
		Users entity = null;
		try {
			Session session = factory.getCurrentSession();
			Criteria criteria = session.createCriteria(Users.class);
			//add filter
			criteria.add(Restrictions.eq("email", userName).ignoreCase());
			
			if(accountId == null){
				criteria.add(Restrictions.isNull("accountId"));
			}
			else{
				criteria.add(Restrictions.eq("accountId", accountId));
			}
			
			entity = (Users) criteria.uniqueResult();
			
		} catch (HibernateException he) {
			logger.error("Failed to find user for name " + userName, he);
			throw new Exception("Failed to find user for name " + userName,he);
		}
		return entity;
	}
	
	
}
