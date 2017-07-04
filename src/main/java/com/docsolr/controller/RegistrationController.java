package com.docsolr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docsolr.dto.Status;
import com.docsolr.dto.UserVO;
import com.docsolr.entity.Users;
import com.docsolr.service.common.GenericService;

@Controller
public class RegistrationController {

	@Autowired
	public GenericService<Users> userService;
	
	@RequestMapping("/signup")
	public String signnup()
	{
		return "login/registration";
	}
	@RequestMapping("/docsolrlogin")
	public String login()
	{
		return "login/docsolrlogin";
	}
	 @RequestMapping(value = "/addUser", method = RequestMethod.POST)
	 @ResponseBody
	 public String addUser(@ModelAttribute("SpringWeb")UserVO user,  ModelMap model) {
			      
		 /*model.addAttribute("name", user.getName());*/
		 Users users = new Users(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), true);
		 userService.saveEntity(users);
		 return "result";
		 
	 }
	 
	 
}
