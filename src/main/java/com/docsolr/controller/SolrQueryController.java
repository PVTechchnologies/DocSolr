package com.docsolr.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docsolr.entity.Users;
import com.docsolr.util.CommonUtil;
import com.docsolr.util.SolrSchemaManager;
import com.google.gson.Gson;

@Controller
public class SolrQueryController {
	
	@Autowired
	public SolrSchemaManager solrSchemaManager;

	private @Value("#{solrProperties['TARGET_SCHEMA_URL']}") String urlString;
	
	@RequestMapping(value = "/SolrSearch", method = RequestMethod.POST)
	@ResponseBody
	public Object SolrJSearcher(@RequestBody String jsonReqData, HttpSession session, HttpServletRequest req)
			throws SolrServerException, IOException {

		JSONObject searchdata = new JSONObject(jsonReqData);
		String data = searchdata.getString("name");
		SolrClient client;
		Users users = new Users();
		users = CommonUtil.getCurrentSessionUser();
		SolrQuery query = new SolrQuery();
		if(urlString.contains("2181")){
			client =  new CloudSolrClient.Builder().withZkHost(urlString).build();
			query.setParam("collection", "newcollection");
			query.setParam("shards", users.getFirstName());
		}else{
			client = new HttpSolrClient.Builder(urlString).build();
		}
		query.setQuery(data);
	
		query.set("defType", "edismax");
		//query.set("qf",
		//		"id _root_ _text_ _version_ billing_address" + " billingcity billingstreet cloud docType email__c"
		//				+ " masterrecordid name objectType parentid type billingstate" + " description type__c zipcode" + " content filetype");
		query.set("fl","* score");
		String fieldNames = String.join(" ", solrSchemaManager.getAllFields());
		//query.setFields(fieldNames);
		query.set("qf",	fieldNames);
					
		query.setStart(0);

		QueryResponse response = client.query(query);
		// SolrDocumentList results = response.getResults();

		Gson gson = new Gson();
		String json = gson.toJson(response.getResults());
		System.out.println(json);

		String result = json.replace("\\n"," ").replace("[\"","\"").replace("\"]", "\"");
		System.out.println(result);
	
		return result;
	}
	
	

}
