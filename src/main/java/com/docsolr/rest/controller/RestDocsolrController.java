package com.docsolr.rest.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.docsolr.dto.Status;
import com.docsolr.dto.UserVO;
import com.docsolr.entity.Users;
import com.docsolr.service.common.GenericService;

/**
 * @author Rajkiran Dewara
 *
 */
@RestController
@RequestMapping("/api")
public class RestDocsolrController {

	@Autowired
	public GenericService<Users> userService;

	static final Logger logger = Logger.getLogger(RestDocsolrController.class);
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public  Status createNewUser(@RequestBody UserVO user) {
		try {
			Users users = new Users(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), true);
			userService.saveEntity(users);
			return new Status(1, "Added Successfully !");
		} catch (Exception e) {
			// e.printStackTrace();
			return new Status(0, e.toString());
		}

	}
	
  }
	

