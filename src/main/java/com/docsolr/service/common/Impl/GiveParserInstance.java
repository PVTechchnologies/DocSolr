package com.docsolr.service.common.Impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.image.ImageParser;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.springframework.stereotype.Service;

import com.docsolr.service.common.IGiveParserInstance;

@Service
public class GiveParserInstance<T> implements IGiveParserInstance<T> {

	@Override
	public T getInstance(String contentType) {
		// TODO Auto-generated stub
		AbstractParser ap = null;
		if("application/pdf".equalsIgnoreCase(contentType)){
			ap =  new PDFParser();
		}else if("application/doc".equalsIgnoreCase(contentType)){
			ap = new OOXMLParser();
		}else if("image/jpeg".equalsIgnoreCase(contentType)){
			ap = new JpegParser();
		}else if("image".indexOf(contentType) != -1){
			ap = new ImageParser();
		}
		
		return (T) ap;
	}
	
	public String getExtension(String file){
		String ext = FilenameUtils.getExtension(file); 
		return ext;
	}

}
