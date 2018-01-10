package com.docsolr.controller.spare;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;  
	import java.net.*;
@Controller
public class XmlConverter {
	
	
	 	@RequestMapping(value = "/getMetadata", method = RequestMethod.GET)
		@ResponseBody        
		public String getJson() throws IOException{            
	         /*   File file = new File ("C:\\Users\\Yadav\\git\\DocSolr\\retrieveResults\\unpackaged\\objects\\Account.object");  
	            InputStream inputStream = new FileInputStream(file);  
	            StringBuilder builder =  new StringBuilder();  
	            int ptr = 0;  
	            while ((ptr = inputStream.read()) != -1 ) {  
	                builder.append((char) ptr); 
	              //  System.out.println(ptr);
	            }  

	            String xml  = builder.toString();  
	            JSONObject jsonObj = XML.toJSONObject(xml);   */
	 		
	 		 String link = "C:\\Users\\Yadav\\git\\DocSolr\\retrieveResults\\unpackaged\\objects\\Account.object";
	         BufferedReader br = new BufferedReader(new FileReader(link));
	         String line = null;  
	         String str = null;
	         while ((line = br.readLine()) != null) 
	         {   
	             str+=line;  
	         }
	         JSONObject jsonObj = XML.toJSONObject(str);
	         System.out.println(jsonObj);
	 
	        return jsonObj.toString();
	    }  
	 	

	   

	 	
	}

