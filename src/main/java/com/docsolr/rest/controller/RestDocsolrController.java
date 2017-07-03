package com.docsolr.rest.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docsolr.dto.Status;
import com.docsolr.dto.UserVO;

/**
 * @author Rajkiran Dewara
 *
 */
@Controller
@RequestMapping("/api")
public class RestDocsolrController {
	
	static final Logger logger = Logger.getLogger(RestDocsolrController.class);
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public  Status addEmployee(@RequestBody UserVO employee) {
		try {
			return new Status(1, "Added Successfully !");
		} catch (Exception e) {
			// e.printStackTrace();
			return new Status(0, e.toString());
		}

	}
	
}
