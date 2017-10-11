package com.docsolr.service.common.Impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.solr.common.util.Hash;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;


@Service
public class DocsolrParser {
	@Autowired
	GiveParserInstance<AbstractParser> abparser;
	
	
	 public Map<String, Object> parser( MultipartFile file) throws IOException, TikaException {
	      Map<String, Object> response= new HashMap<>();
		  byte[] bytes = file.getBytes();
		  InputStream myInputStream = new ByteArrayInputStream(bytes); 
		  
		  
	      //detecting the file type
	      BodyContentHandler handler = new BodyContentHandler();
	      Metadata metadata = new Metadata();
	      //FileInputStream inputstream = new FileInputStream(new File("C:/Users/Yadav/Desktop/demo.docx"));
	      ParseContext pcontext = new ParseContext();
	      
	      AbstractParser  genericparser = abparser.getInstance(file.getContentType()); 
	      try {
	    	  genericparser.parse(myInputStream, handler, metadata,pcontext);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      System.out.println("Contents of the document:" + handler.toString());
	      System.out.println("Metadata of the document:");
	      String[] metadataNames = metadata.names();
	      response.put("Contents", handler.toString());
	      response.put("Meta", metadataNames);
	      for(String name : metadataNames) {
	         System.out.println(name + ": " + metadata.get(name));
	      }
	      return response;
	   }
}
