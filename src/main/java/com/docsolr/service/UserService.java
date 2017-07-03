package com.docsolr.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.docsolr.dao.UserAuthorityDao;
import com.docsolr.dao.UserDAO;
import com.docsolr.dto.UserVO;
import com.docsolr.entity.UserAuthority;
import com.docsolr.entity.UserConnection;
import com.docsolr.entity.Users;
import com.docsolr.entity.UserAuthority.Roles;
import com.docsolr.service.common.GenericService;
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserService {

	static Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	GenericService genericService;
	
	@Autowired
	private UserAuthorityDao userAuthorityDao;
	
	private @Value("#{deployProperties['application.url']}") String applicationURL;

	public Users findUserByUrlPathOrId(String urlPath) throws Exception {
		return null;//userDAO.findUserByUrlPathOrId(urlPath);
	}
	
	
	/**
	 * find user by username in database
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public Users findUserByUsername(String userName) throws Exception {
		return findUserByUsername(null, userName);
	}
	
	/**
	 * find user by username in database
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public Users findUserByUsername(Long accountId, String userName) throws Exception {
		try {
			return userDAO.findUserByUsername(accountId, userName);
		} catch (Exception e) {
			logger.error("Error on finding user for username" + userName);
			throw e;
		}
	}
	
	/**
	 * find user by username in database
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public Users findByUsername(Long accountId, String userName) {
		try {
			return userDAO.findUserByUsername(accountId, userName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Users createUser(Long accountId, UserVO userVO, Roles authority) throws Exception {
		Users createUser = new Users();
		createUser.setPassword(userVO.getPassword());
		createUser.setEnabled(true);
		
		updateUserProperties(accountId, userVO, authority, createUser);
		createUser = (Users) genericService.saveEntity(createUser);
		return createUser;
	}


	private void updateUserProperties(Long accountId, UserVO userVO, Roles authority, Users createUser) {
		createUser.setEmail(userVO.getEmail());
		createUser.setFirstName(userVO.getFirstName());
		createUser.setLastName(userVO.getLastName());
		Set<UserAuthority> authorities = new HashSet<>();
		authorities.add(userAuthorityDao.find(authority));
		createUser.setAuthorities(authorities);
	}
	
	public Users createOrUpdateUser(Long accountId, UserVO userVO, Roles authority) throws Exception {
		Users createUser = (Users) genericService.findEntityById(Users.class, userVO.getUserId());
		if(createUser!=null){
			updateUserProperties(accountId, userVO, authority, createUser);
			createUser = (Users) genericService.saveEntity(createUser);
		}
		return createUser;
	}
	
	public void createOrUpdateUser(Users Users) throws Exception {
		genericService.saveEntity(Users);
	}
	
	public UserVO getUserVO(Users Users){
		UserVO userVO = new UserVO(Users.getId(), Users.getEmail(), null, 
				Users.getFirstName(),Users.getLastName());
		return userVO;
	}
}
