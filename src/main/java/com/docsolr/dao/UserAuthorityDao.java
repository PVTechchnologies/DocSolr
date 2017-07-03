package com.docsolr.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.docsolr.entity.UserAuthority;
import com.docsolr.entity.UserAuthority.Roles;

@Repository
public class UserAuthorityDao {
	@Autowired
	protected SessionFactory factory;
	
	public UserAuthority find(Roles authority){
		
		List<Criterion> restrictions = new ArrayList<Criterion>(1);
		restrictions.add(Restrictions.eq("authority", authority.toString()));
		
		UserAuthority userAuthority = find(UserAuthority.class, restrictions);
		
		if(userAuthority == null){
			throw new RuntimeException("Invalid " + UserAuthority.class.getSimpleName() + " authority " + authority);
		}
		
		
		return userAuthority;
	}

	/**
	 * @param class1
	 * @param restrictions
	 * @return
	 */
	private UserAuthority find(Class<UserAuthority> class1, List<Criterion> restrictions) {
		Session session = factory.openSession();
		
		Criteria criteria = session.createCriteria(class1);
		Iterator<Criterion> iterator = restrictions.iterator();
		
		while (iterator.hasNext()) {
			criteria.add(iterator.next());
		}
		
		criteria.add(Restrictions.eq("deleted", false));
		
		UserAuthority object = (UserAuthority) criteria.uniqueResult();
		session.close();
		
		return object;
	}

	/*public List<UserAuthority> findAll(){
		List<UserAuthority> userAuthorities = findAllIsNotDeleted(UserAuthority.class);
		return userAuthorities;
	}*/
	
	@SuppressWarnings("unchecked")
	protected List findAll(Class clazz, long accountId, List<Criterion> criterians){
		
		Session session = factory.openSession();
		
		Criteria criteria = session.createCriteria(clazz);
		Iterator<Criterion> iterator = criterians.iterator();
		
		while (iterator.hasNext()) {
			criteria.add(iterator.next());
		}
		criteria.add(Restrictions.eq("accountId", accountId));
		criteria.add(Restrictions.eq("deleted", false));
		List objects = (List) criteria.list();
		session.close();
		
		return objects;
	}
}
