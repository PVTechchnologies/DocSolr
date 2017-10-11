package com.docsolr.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.docsolr.service.common.Impl.DocsolrParser;
import com.docsolr.service.common.Impl.GiveParserInstance;



/**
 * Handles requests for the application file upload requests
 */
@Controller
public class FileUploadController {
	
	GiveParserInstance GPI;

	@Autowired
	DocsolrParser docsolr;
	
	private static final Logger logger = LoggerFactory
			.getLogger(FileUploadController.class);

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object>  uploadFileHandler(@RequestParam("file") MultipartFile file) {
		Map<String, Object> meta = null;
		if (!file.isEmpty()) {
			
				try {
					
				meta = docsolr.parser(file);
				
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TikaException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		return meta;
	}

}