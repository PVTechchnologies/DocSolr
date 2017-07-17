package com.docsolr.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docsolr.dto.UserVO;
import com.docsolr.entity.Account;
import com.docsolr.entity.UserAuthority;
import com.docsolr.entity.UserAuthority.Roles;
import com.docsolr.entity.Users;
import com.docsolr.service.common.GenericService;
import com.docsolr.util.CommonUtil;


@Controller
public class RegistrationController {

	@Autowired
	public GenericService<Users> userService;
	
	@Autowired
	GenericService<UserAuthority> userAuthGenericService;
	
	@Autowired
	GenericService<Account> accountService;
	
	@RequestMapping("/signup")
	public String signnup(HttpSession session)
	{
		
		return "login/registration";
	}
	
	@RequestMapping("/docsolrlogin")
	public String login(HttpSession session)
	{
		return "login/docsolrlogin";
	}
	
	 @RequestMapping(value = "/addUser", method = RequestMethod.POST)
	 @ResponseBody
	 public String addUser(@ModelAttribute("newUserSignup")UserVO userVo,  ModelMap model) {
			      
		 /*model.addAttribute("name", user.getName());*/
		 Users user = new Users(userVo.getFirstName(), userVo.getLastName(), userVo.getEmail(), userVo.getPassword(), true,true);
		 UserAuthority userAuthority = new UserAuthority(Roles.ROLE_USER);
			Map authRestrictionMap = new HashMap();
			authRestrictionMap.put("authority", userAuthority.getAuthority());
			List<UserAuthority> userAuthorityList = userAuthGenericService.findLimitedEntity(UserAuthority.class, 0, authRestrictionMap, null);
			if(!userAuthorityList.isEmpty()){
				Set<UserAuthority> setOfAuthority = new HashSet<UserAuthority>();
				setOfAuthority.add(userAuthorityList.get(0));
				user.setAuthorities(setOfAuthority);
				user.setEnabled(true);
			}
			userService.saveEntity(user);
			return "result";
	 }
	 
	 
	// @RequestMapping(value = "/list", method = RequestMethod.GET)
	 @RequestMapping(value = "/populatePersonData", method = RequestMethod.GET)
		@ResponseBody
		public Object getCorporates() throws Exception {
			List<Account> list = null;
			try {
				 list = accountService.findAllEntity(Account.class);
				 return CommonUtil.returnSuccess(list);
			} catch (AccessDeniedException ade) {
				throw ade;
			} catch (RuntimeException e) {
				e.printStackTrace();
				return CommonUtil.returnError(e, null);
			}
		}
	 
}
