package com.docsolr.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.docsolr.entity.AuthenticationToken;
import com.docsolr.entity.UserAuthority;
@Repository
public class AuthenticationTokenDao {

	protected final static Logger logger = Logger.getLogger(AuthenticationTokenDao.class);
	
	@Autowired
	protected SessionFactory factory;
	
	public AuthenticationToken findByToken(String token){
		List<Criterion> restrictions = new ArrayList<Criterion>();
		
		restrictions.add(Restrictions.eq("token", token));
		
		return find(AuthenticationToken.class, restrictions);
	}


	/**
	 * @param class1
	 * @param restrictions
	 * @return
	 */
	private AuthenticationToken find(Class<AuthenticationToken> class1, List<Criterion> restrictions) {
		Session session = factory.openSession();
		
		Criteria criteria = session.createCriteria(class1);
		Iterator<Criterion> iterator = restrictions.iterator();
		
		while (iterator.hasNext()) {
			criteria.add(iterator.next());
		}
		
		criteria.add(Restrictions.eq("deleted", false));
		
		AuthenticationToken object = (AuthenticationToken) criteria.uniqueResult();
		session.close();
		
		return object;
	}
	
}
