package com.docsolr.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.docsolr.service.common.Impl.DocsolrParser;
import com.docsolr.service.common.Impl.GiveParserInstance;
import com.google.common.io.Files;

import jdk.internal.org.xml.sax.SAXException;




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
/*
	public static void main(String[] args) throws IOException, TransformerConfigurationException, SAXException,
	TikaException, org.xml.sax.SAXException {
	byte[] file = Files.toByteArray(new File("C:/Users/Yadav/Desktop/OoPdfFormExample.pdf"));
	AutoDetectParser tikaParser = new AutoDetectParser();
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
	TransformerHandler handler = factory.newTransformerHandler();
	handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
	
	handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
	handler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	handler.setResult(new StreamResult(out));
	ExpandedTitleContentHandler handler1 = new ExpandedTitleContentHandler(handler);
	tikaParser.parse(new ByteArrayInputStream(file), handler1, new Metadata());
	System.out.println(new String(out.toByteArray(), "UTF-8"));
	}*/
	
 
	

}

	
