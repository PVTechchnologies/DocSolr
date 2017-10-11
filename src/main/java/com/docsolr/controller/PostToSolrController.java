package com.docsolr.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class PostToSolrController {

	
		@RequestMapping (value = "/sendrecord", method = RequestMethod.GET)
	 	@ResponseBody     
	 	public int sendSolrData() {
		    String strURL = "http://132.148.68.21:8983/solr/Dummydata/update?commit=true";
		    
		    String strXMLFilename = "d:\\cars.xml";
		    File input = new File(strXMLFilename);
		    PostMethod post = new PostMethod(strURL);
		    try {
		        post.setRequestEntity(new InputStreamRequestEntity(
		                new FileInputStream(input), input.length()));
		        post.setRequestHeader("Content-type",
		                "text/xml; charset=ISO-8859-1");
		        HttpClient httpclient = new HttpClient();

		        int result = httpclient.executeMethod(post);
		        System.out.println("Response status code: " + result);
		        System.out.println("Response body: ");
		        System.out.println(post.getResponseBodyAsString());
		        return result;
		    } catch (IOException e) {
		        e.printStackTrace();
		    }catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        post.releaseConnection();
		    }
		    return 0;
		}
		
		@RequestMapping (value = "/SolrSearch", method = RequestMethod.POST)
		@ResponseBody
		public Object SolrJSearcher (@RequestBody String jsonReqData,HttpSession session, HttpServletRequest req) throws SolrServerException, IOException  {
				
				JSONObject searchdata =new JSONObject(jsonReqData);
				String data = searchdata.getString("name");
			
				String urlString = "http://132.148.68.21:8983/solr/Dummydata"; 
		        SolrClient client = new HttpSolrClient.Builder(urlString).build();

		        SolrQuery query = new SolrQuery();
		        

		        query.setQuery(data);
		
		        query.set("defType", "edismax");
		        query.set("qf", "id _root_ _text_ _version_ billing_address" 
		        		+" billingcity billingstreet cloud docType email__c" 
		        		+" masterrecordid name objectType parentid type" 
		        		+" type__c zipcode");
		      
		        query.setStart(0);
		       

		        QueryResponse response = client.query(query);
		        SolrDocumentList results = response.getResults();
		       /* for (int i = 0; i < results.size(); ++i) {
		            System.out.println(results.get(i));
		        }*/
		        
		        Gson gson = new Gson(); 
		        String json = gson.toJson(response.getResults());
		        System.out.println(json);
		        
		        return json;
		    }
		
		}
	


