package com.docsolr.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

	public class MSOfficeParse {

	   public static void main(final String[] args) throws IOException, TikaException {
	      
	      //detecting the file type
	      BodyContentHandler handler = new BodyContentHandler();
	      Metadata metadata = new Metadata();
	      FileInputStream inputstream = new FileInputStream(new File("C:/Users/Yadav/Desktop/demo.docx"));
	      ParseContext pcontext = new ParseContext();
	      
	      //OOXml parser
	      OOXMLParser  msofficeparser = new OOXMLParser (); 
	      try {
			msofficeparser.parse(inputstream, handler, metadata,pcontext);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      System.out.println("Contents of the document:" + handler.toString());
	      System.out.println("Metadata of the document:");
	      String[] metadataNames = metadata.names();
	      
	      for(String name : metadataNames) {
	         System.out.println(name + ": " + metadata.get(name));
	      }
	   }
	}

