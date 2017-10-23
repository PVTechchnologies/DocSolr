package com.docsolr.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.docsolr.entity.SalesforceSetupDetail;
import com.docsolr.entity.Users;
import com.docsolr.service.common.GenericService;
import com.docsolr.util.CommonUtil;
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

	public PartnerConnection partnerConnection;

	String urlString = "http://132.148.68.21:8983/solr/Dummydata";

	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

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

		metadataConfig.setServiceEndpoint("https://ap5.salesforce.com/services/Soap/u/40/00D7F000001a7Nw");
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
				query.add("Select " + ssd.getSalesforceFields() + " from " + entrySet.getKey());
			}

			/* Calling of Queries */
			for (int j = 0; j < query.size(); j++) {
				String soqlQuery = query.get(j);

				try {
					// Set query batch size
					partnerConnection.setQueryOptions(250);

					// Make the query call and get the query results

					QueryResult qr = partnerConnection.query(soqlQuery);

					/*Gson gson = new Gson();
					String json = gson.toJson(qr);
					System.out.println(json);*/
					boolean done = false;

					//int loopCount = 0;
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

			//getAttachment(ids);/* Method calling for Attachment */

			Gson gson = new Gson();
			String json = gson.toJson(recordsList);
		//	System.out.println(json);

			
			/*Calling function to parse record data and send it to Solr Server*/
			parseAndSend(json);
			

		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return recordsList;

	}

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
		int loopCount = 0;
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

		String urlString = "http://132.148.68.21:8983/solr/Dummydata";
		SolrClient solr = new HttpSolrClient.Builder(urlString).build();

		SolrInputDocument firstParentObject = new SolrInputDocument();
		SolrInputDocument docObject = null;
		/*"firstParentObject" is used for storing first object of records and rest were its child*/
		
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
										innerOutputObject.put("name", name);
										innerOutputObject.put("text", value);
										if (list.isEmpty()) {		/*when list is empty, run only for once*/ 
											list.add(name);
											if (i==0 && j == 0) {
												firstParentObject.addField(name, value);
											} else {
												docObject.addField(name, value);
											}
										} else if (list.contains(name) && name.equalsIgnoreCase("id")) { /*when id was duplicate*/
											isIdFieldExist = true;
										} else if (list.contains(name)) { /*when records key value were same*/ 
											if (i==0 && j == 0) {
												firstParentObject.addField(name + "__c", value);
											} else {
												docObject.addField(name + "__c", value);
											}

										} else {          /*run always with fair data*/
											if (i==0 && j == 0) {
												firstParentObject.addField(name, value);
											} else {
												docObject.addField(name, value);
											}
											list.add(name);
										}
									}

								} /* Z loop Ends here */

							}
							if (j != 0 || i>0)
								firstParentObject.addChildDocument(docObject);

						} /* J Loop Ends here */
					}

				} /* I Loop Ends Here */

				batch.add(firstParentObject);
				solr.add(batch);
				solr.commit();
				System.out.println("Documents Updated");
				/* Last OutPut Here */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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


	
