package com.docsolr.service.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * 
 * @author Rajkiran
 *
 */
public class HtmlTemplate
{
	static Logger logger = Logger.getLogger(HtmlTemplate.class);
	public static final String MODULE = "[HtmlTamplate] #";
	
	public static String getHtmlFileData(String filename) throws IOException 
	{	
		final String METHOD = "getHtmlFileData ->";
		
		logger.debug(MODULE +METHOD+ " called !");
		logger.debug(MODULE +METHOD+ " Template File Name : " + filename);
		
		File htmlfile = null;
		String htmlsource = "";
		FileInputStream fis = null;
		int i =0;
		htmlfile = new File(filename);
		
		if(htmlfile.exists()){
			try{			    			    
				fis = new FileInputStream(htmlfile);
				int len = fis.available();
				byte b[] = new byte[len];
				i = fis.read(b);
				if(i != -1)
				
				htmlsource = new String(b);						
				htmlsource = htmlsource.replaceAll("\n","");
				htmlsource = htmlsource.replaceAll("\r","");
				
				fis.close();
			}catch(Exception e){	
			    logger.error(MODULE + "Error in Reading File: " + e,e);			
			}finally{
				if( fis!=null ){
					try{
						fis.close();
					}catch(Exception e){
						logger.error(MODULE + "Error in Closing File input Stream: " + e,e);	
					}
				}
				
			}			
			return htmlsource;
		}else{
		    logger.debug(MODULE +METHOD+ " File is not found at required place !");
			return null;
		}
	 }

}