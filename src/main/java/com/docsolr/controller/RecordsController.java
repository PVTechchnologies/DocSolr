package com.docsolr.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.Create;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.CreateShard;
import org.apache.solr.client.solrj.request.ConfigSetAdminRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docsolr.entity.SalesforceSetupDetail;
import com.docsolr.entity.Users;
import com.docsolr.service.common.GenericService;
import com.docsolr.service.common.Impl.GiveParserInstance;
import com.docsolr.util.CommonUtil;
import com.docsolr.util.SolrSchemaManager;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.bind.XmlObject;

@Controller
public class RecordsController {

	@Autowired
	public GenericService<SalesforceSetupDetail> salesforceSetupDetail;
	
	@Autowired
	GiveParserInstance<AbstractParser> abparser;

	public PartnerConnection partnerConnection;

	 private @Value("#{solrProperties['TARGET_SCHEMA_URL']}") String urlString;
	 private @Value("#{salesforceProperties['ServiceEndpoint']}") String serviceEndpoint;
	 private @Value("#{salesforceProperties['ServiceEndpointVersion']}") String serviceEndpointVersion;
	 //TODO: This organization id needs to be dynamic in future
	 private @Value("#{salesforceProperties['ServiceOrgId']}") String serviceOrgId;
	
	@Autowired
	public SolrSchemaManager solrSchemaManager;
	
	@Autowired
	public GenericService<Users> userService;


	@RequestMapping(value = "/recieveRecord", method = RequestMethod.GET)
	@ResponseBody
	public Object getFieldData(RedirectAttributes redirectAttributes) throws RemoteException, Exception {
		if (login()) {

			// Add calls to the methods in this class
			Object strJsonObj = getFields();
			return strJsonObj;
		} else {
			return null;
		}
	}

	/* To create partner connection */

	private boolean login() throws ConnectionException {
		boolean success = false;

		String token = SalesforceController.acctoken;
		final ConnectorConfig metadataConfig = new ConnectorConfig();
		//TODO: This organization id needs to be dynamic in future
		metadataConfig.setServiceEndpoint(serviceEndpoint+serviceEndpointVersion+"/"+serviceOrgId);
		metadataConfig.setSessionId(token);
		this.partnerConnection = new PartnerConnection(metadataConfig);

		success = true;
		return success;
	}

	public Object getFields() throws Exception {

		List<SObject[]> recordsList = new ArrayList<>();
		Users users = new Users();
		users = CommonUtil.getCurrentSessionUser();

		Map<String, SalesforceSetupDetail> tableData = new HashMap<>();
		tableData = salesforceSetupDetail.getKeyValueMapString("SalesforceSetupDetail", "salesforceObjectApiName",
				"SalesforceSetupDetail", " Where createdById=" + users.getId());

		try {
			/* Making list of quries from database records */
			List<String> query = new ArrayList<String>();
			for (Map.Entry<String, SalesforceSetupDetail> entrySet : tableData.entrySet()) {
				SalesforceSetupDetail ssd = entrySet.getValue();
				query.add("Select " + ssd.getSalesforceFields() + " from " + entrySet.getKey() + " limit 10");
			}

			/* Calling of Queries */
			for (int j = 0; j < query.size(); j++) {
				String soqlQuery = query.get(j);

				try {
					// Set query batch size
					partnerConnection.setQueryOptions(250);

					// Make the query call and get the query results

					QueryResult qr = partnerConnection.query(soqlQuery);

					/*
					 * Gson gson = new Gson(); String json = gson.toJson(qr);
					 * System.out.println(json);
					 */
					boolean done = false;

					// int loopCount = 0;
					// Loop through the batches of returned results
					while (!done) {

						recordsList.add(qr.getRecords());

						if (qr.isDone()) {
							done = true;
						} else {
							qr = partnerConnection.queryMore(qr.getQueryLocator());
						}
					}
				} catch (ConnectionException ce) {
					ce.printStackTrace();
				}

				System.out.println("\nQuery execution completed.");
			}

			/*
			 * Set<String> ids = new HashSet<String>(); ids=gettingSet(records);
			 */

			/* Parsing of result fetched in XML form */

			Set<String> ids = new HashSet<String>();
			for (SObject[] records : recordsList) {
				for (SObject so : records) {
					Iterator<XmlObject> sxml = so.getChildren();
					while (sxml.hasNext()) {

						XmlObject xobj = sxml.next();
						if (xobj.getName().toString().equalsIgnoreCase("{urn:sobject.partner.soap.sforce.com}Id")) {

							ids.add(xobj.getValue().toString());
						}
					}
				}
			}

			// getAttachment(ids);/* Method calling for Attachment */

			Gson gson = new Gson();
			String json = gson.toJson(recordsList);
			// System.out.println(json);

		
			/*
			 * Calling function to parse record data and send it to Solr Server
			 */
			parseAndSend(json);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return recordsList;

	}

	/**
	 * @param setOfId
	 * @throws Exception
	 */
	private void getAttachment(Set<String> setOfId) throws Exception {

		partnerConnection.setQueryOptions(250);

		StringBuilder result = new StringBuilder();

		for (String string : setOfId) {
			result.append("\'" + string + "\'");
			result.append(",");
		}

		result.deleteCharAt(result.length() - 1);

		String soqlQuery = "SELECT Id, body FROM Attachment Where ParentID IN(" + result + ")";

		QueryResult qr = partnerConnection.query(soqlQuery);
		SObject[] records = null;
		boolean done = false;
		
		// Loop through the batches of returned results
		while (!done) {
			records = qr.getRecords();

			if (qr.isDone()) {
				done = true;
			} else {
				qr = partnerConnection.queryMore(qr.getQueryLocator());
			}
		}

		/*
		 * Set<String> ids = new HashSet<String>(); ids=gettingSet(records);
		 */

		Set<String> ids = new HashSet<String>();
		for (SObject so : records) {
			Iterator<XmlObject> sxml = so.getChildren();
			while (sxml.hasNext()) {

				XmlObject xobj = sxml.next();
				if (xobj.getName().toString().equalsIgnoreCase("{urn:sobject.partner.soap.sforce.com}Body")) {

					ids.add(xobj.getValue().toString());
					System.out.println(xobj);
				}
			}
		}

	}

	public void parseAndSend(String json) throws ParserConfigurationException, TransformerException, JsonIOException,
			JsonSyntaxException, XMLStreamException, IOException, SolrServerException {

		List<String> list = new ArrayList<String>();
		Collection<SolrInputDocument> batch = new ArrayList<SolrInputDocument>();

		SolrClient solr ;
		
		if(urlString.contains("2181")){
			solr =  new CloudSolrClient.Builder().withZkHost(urlString).build();
		}else{
			solr = new HttpSolrClient.Builder(urlString).build();
		}

		SolrInputDocument firstParentObject = new SolrInputDocument();
		SolrInputDocument docObject = null;
		
		Users users = new Users();
		users = CommonUtil.getCurrentSessionUser();
		
		if(solr instanceof CloudSolrClient){
			List<String> collections = solrSchemaManager.getCollections((CloudSolrClient) solr);
			if(null == collections || !collections.contains("newcollection")){
				ConfigSetAdminRequest.List list1 = new ConfigSetAdminRequest.List();
				List<String> av=list1.process(solr).getConfigSets();
				Create create = CollectionAdminRequest.createCollectionWithImplicitRouter("newcollection", "mynewcollection", users.getFirstName(), 1);
				create.setMaxShardsPerNode(20);
				solr.request(create);
				users.setSolrShardExists(true);
				userService.saveUpdateEntity(users);
			}
			
			if(!users.isSolrShardExists()){
				CreateShard shard = CollectionAdminRequest.createShard("newcollection", users.getFirstName());
				shard.process(solr);
			}
		}	
		/*
		 * "firstParentObject" is used for storing first object of records and
		 * rest were its child
		 */

		try {

			JSONArray parentArray = new JSONArray(json);
			if (parentArray != null && parentArray.length() > 0) {
				for (int i = 0; i < parentArray.length(); i++) {
					JSONArray childJsonArray = parentArray.optJSONArray(i);
					if (childJsonArray != null && childJsonArray.length() > 0) {
						for (int j = 0; j < childJsonArray.length(); j++) {

							JSONObject josnObject = childJsonArray.getJSONObject(j);
							JSONArray innerChildJsonArray = josnObject.getJSONArray("children");

							if (innerChildJsonArray != null && innerChildJsonArray.length() > 0) {

								docObject = new SolrInputDocument();

								list = new ArrayList<String>();

								boolean isIdFieldExist = false;

								for (int z = 0; z < innerChildJsonArray.length(); z++) {
									String value = "";
									JSONObject innerOutputObject = new JSONObject();
									JSONObject childJosnObject = innerChildJsonArray.getJSONObject(z);
									JSONObject forName = childJosnObject.getJSONObject("name");
									String name = forName.getString("localPart");
									name = name.toLowerCase();
									if (childJosnObject.has("value")) {

										value = childJosnObject.getString("value");
										if(name.equals("versiondata")){
											JSONObject keyObj = (JSONObject)getKey(innerChildJsonArray, "FileType");
											String fileType = keyObj.getString("value");
											value = parseBase64(value.toString(),fileType);
											name = "content";
										}
											innerOutputObject.put("name", name);
											innerOutputObject.put("text", value);
										if (list.isEmpty()) { /*
																 * when list is
																 * empty, run
																 * only for once
																 */
											list.add(name);
											if (i == 0 && j == 0) {
												firstParentObject.addField(name, value);
											} else {
												docObject.addField(name, value);
											}
										} else if (list.contains(name) && name.equalsIgnoreCase(
												"id")) { /*
															 * when id was
															 * duplicate
															 */
											isIdFieldExist = true;
										} else if (list.contains(
												name)) { /*
															 * when records key
															 * value were same
															 */
											if (i == 0 && j == 0) {
												firstParentObject.addField(name + "__c", value);
											} else {
												docObject.addField(name + "__c", value);
											}

										} else { /* run always with fair data */
											if (i == 0 && j == 0) {
												firstParentObject.addField(name, value);
											} else {
												docObject.addField(name, value);
											}
											list.add(name);
										}
									}

								} /* Z loop Ends here */

							}
							if (j != 0 || i > 0)
								firstParentObject.addChildDocument(docObject);

						} /* J Loop Ends here */
					}

				} /* I Loop Ends Here */
				
				batch.add(firstParentObject);
				System.out.println(batch);
				List<String> existingFields = solrSchemaManager.getAllFields();
				Set<String> fieldNamesBatch = new LinkedHashSet<String>();
				for(SolrInputDocument sid:batch){
					fieldNamesBatch.addAll(sid.getFieldNames());
					if(sid.getChildDocumentCount()>0){
						List<SolrInputDocument> childDocs = sid.getChildDocuments();
						for(SolrInputDocument sidChild:childDocs){
							fieldNamesBatch.addAll(sidChild.getFieldNames());
						}
					}
				}
				for(String field:fieldNamesBatch){
					if(!existingFields.contains(field)){
						solrSchemaManager.addField(field, "string", false);
					}
				}
				if(solr instanceof CloudSolrClient){
					UpdateRequest add = new UpdateRequest();
					add.add(batch);
					add.setParam("collection", "newcollection");
					add.setParam("shard", users.getFirstName());
					add.process(solr);

					UpdateRequest commit = new UpdateRequest();
					commit.setAction(UpdateRequest.ACTION.COMMIT, true, true);
					commit.setParam("collection", "newcollection");
					commit.setParam("shard", users.getFirstName());
					commit.process(solr);
				}else {
					solr.add(batch);
					solr.commit();
				}
				System.out.println("Documents Updated");
				/* Last OutPut Here */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private String parseBase64(String data, String fileType){
		byte[] bytes = Base64.getDecoder().decode(data);
		 InputStream myInputStream = new ByteArrayInputStream(bytes);
	      BodyContentHandler handler = new BodyContentHandler();
	      Metadata metadata = new Metadata();
	      ParseContext pcontext = new ParseContext();
	      
	      AbstractParser  genericparser = abparser.getInstance(fileType); 
	      try {
	    	  genericparser.parse(myInputStream, handler, metadata,pcontext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      return handler.toString();
	}
	
	private Object getKey(JSONArray array, String key){

	    for (int i = 0; i < array.length(); i++)
	    {
	    	JSONObject childJosnObject = array.getJSONObject(i);
			JSONObject forName = childJosnObject.getJSONObject("name");
			String name = forName.getString("localPart");
	        if (key.equalsIgnoreCase(name))
	        {
	            return childJosnObject;
	        }
	    }

	    return null;
	}

	/*
	 * private Set<String> gettingSet(SObject[] records){
	 * 
	 * Boolean havebody=false;
	 * 
	 * Map<String,List<String>> idsBase64Map = new HashMap<>();
	 * 
	 * Set<String> ids = new HashSet<String>(); for (SObject so : records) {
	 * Iterator<XmlObject> sxml =so.getChildren(); while(sxml.hasNext()){
	 * System.out.println("==="); XmlObject xobj = sxml.next();
	 * if(xobj.getName().toString().equalsIgnoreCase(
	 * "{urn:sobject.partner.soap.sforce.com}Body")){
	 * System.out.println(xobj.getName().toString());
	 * ids.add(xobj.getValue().toString()); } else {
	 * if(xobj.getName().toString().equalsIgnoreCase(
	 * "{urn:sobject.partner.soap.sforce.com}Id")){
	 * 
	 * System.out.println(xobj.getName().toString());
	 * ids.add(xobj.getValue().toString()); } } } } return ids; }
	 */


	

}
