package com.docsolr.service.common.Impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.DublinCore;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;


@Service
public class DocsolrParser {
	@Autowired
	GiveParserInstance<AbstractParser> abparser;
	private @Value("#{solrProperties['TARGET_SCHEMA_URL']}") String urlString;
	
	 public Map<String, Object> parser( MultipartFile file) throws IOException, TikaException {
	      Map<String, Object> response= new HashMap<>();
		  byte[] bytes = file.getBytes();
		  InputStream myInputStream = new ByteArrayInputStream(bytes); 
		  
		  
	      //detecting the file type
	      BodyContentHandler handler = new BodyContentHandler();
	      Metadata metadata = new Metadata();
	      
	      //FileInputStream inputstream = new FileInputStream(new File("C:\\Users\\IBM_ADMIN\\Downloads\\Statement_Jan 2018.pdf"));
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
	      indexDocument(metadata, file.getOriginalFilename(),handler);
	      return response;
	   }
	 
	 private void indexDocument(Metadata meta, String pathfilename, BodyContentHandler handler)  {
		 	SolrClient solr = new HttpSolrClient.Builder(urlString).build();  
		 	UUID guid = java.util.UUID.randomUUID();
			String docid = guid.toString();
			//Dublin Core metadata (partial set)
	        String doctitle = meta.get(DublinCore.TITLE);
	        String doccreator = meta.get(DublinCore.CREATOR); 
	        
	        //other metadata
	        String docurl = pathfilename; //document url
	        
	        //content
	        String doccontent = handler.toString();
			try {
				SolrInputDocument doc = new SolrInputDocument();
				//solr.deleteByQuery("*");        
		         
			      //Saving the document 
			     // solr.commit(); 
				doc.addField("id", docid);
				
				//map metadata fields to default schema
				//location: path\solr-4.7.2\example\solr\collection1\conf\schema.xml
				
				//Dublin Core
				//thought: schema could be modified to use Dublin Core
				doc.addField("title", doctitle);
				doc.addField("author", doccreator);

				//other metadata
				doc.addField("url", docurl);
				
				//content (and text)
				//per schema, the content field is not indexed by default, used for returning and highlighting document content
				//the schema "copyField" command automatically copies this to the "text" field which is indexed
				doc.addField("content", doccontent);
				
				//indexing
				//when a field is indexed, like "text", Solr will handle tokenization, stemming, removal of stopwords etc, per the schema defn
				
				//add to index
				solr.add(doc);	
				solr.commit();
			} 
			catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}	
}
