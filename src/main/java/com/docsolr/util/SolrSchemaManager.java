package com.docsolr.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.NoOpResponseParser;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SolrSchemaManager {


    private SolrClient solrClient;
    
    private static ObjectMapper om = new ObjectMapper();
    
    private static Map<String, String> dataTypeMapping = new HashMap<String, String>();
    static {
        dataTypeMapping.put("integer", "int");
    }

    private List<String> fieldBlackList = new ArrayList<String>();

    @Autowired
    public SolrSchemaManager(@Value("#{solrProperties['TARGET_SCHEMA_URL']}") String urlString) {
    	if(urlString.contains("2181")){
    		solrClient = new CloudSolrClient.Builder().withZkHost(urlString).build();
    	}else{
    		solrClient = new HttpSolrClient.Builder(urlString).build();
    	}       
        fieldBlackList.add("_text_");
        fieldBlackList.add("_version_");
        fieldBlackList.add("id");
    }
    
    public SolrSchemaManager(SolrClient _solrClient) {
        solrClient = _solrClient;
        fieldBlackList.add("_text_");
        fieldBlackList.add("_version_");
        fieldBlackList.add("id");
    }


    public void addField(String name, String type, boolean multivalue)  throws IOException, SolrServerException {
        String mappedType = dataTypeMapping.get(type);
        if(mappedType == null) {
            mappedType = type;
        }

        Map<String, Object> fieldAttributes = new HashMap();
        fieldAttributes.put("name", name);
        fieldAttributes.put("type", mappedType);
        fieldAttributes.put("stored", true);
        fieldAttributes.put("indexed", true);
        fieldAttributes.put("multiValued", multivalue);
        fieldAttributes.put("required", false);

        SchemaRequest.AddField schemaRequest = new SchemaRequest.AddField(fieldAttributes);
        SchemaResponse.UpdateResponse response =  schemaRequest.process(solrClient);
        System.out.println(response);
    }

    public void addField(String name)  throws IOException, SolrServerException {
        addField(name, "string",false);

    }

    public void deleteField(String name) throws IOException, SolrServerException {
        if(fieldBlackList.contains(name)) {
            return;
        }
        SchemaRequest.DeleteField deleteFieldRequest = new SchemaRequest.DeleteField(name);
        SchemaResponse.UpdateResponse deleteFieldResponse = deleteFieldRequest.process(solrClient);


    }

    public List<String> getAllFields() throws IOException, SolrServerException {
        List<String> allFields = new ArrayList<String>();
        SchemaRequest.Fields listFields = new SchemaRequest.Fields();
        if(solrClient instanceof CloudSolrClient){
        	CloudSolrClient cloudSolr = (CloudSolrClient) solrClient;
			cloudSolr.setDefaultCollection("newcollection");
        	NamedList<Object> objs = cloudSolr.request(listFields);
    	    JsonNode collectionsJsonNode = om.readTree((String) objs.get("response")).get("fields");
    	    for (Iterator<JsonNode> i = collectionsJsonNode.iterator(); i.hasNext(); ) {
    	        String fields = i.next().textValue();
    	        if (!allFields.contains(fields)) {
    	        	allFields.add(fields);
    	        }
    	    }
        }else{
	        SchemaResponse.FieldsResponse fieldsResponse = listFields.process(solrClient);
	        List<Map<String,Object>> solrFields = fieldsResponse.getFields();
	        for(Map<String,Object> field : solrFields) {
	            allFields.add((String) field.get("name"));
	        }
        }
        return allFields;
    }

    public void deleteAllFields() throws IOException, SolrServerException {
        for(String fieldName: getAllFields()) {
            deleteField(fieldName);
        }
    }
    
    public  List<String> getCollections(CloudSolrClient solrClient) throws SolrServerException, IOException {
	    List<String> collections = new ArrayList<>();

	    NoOpResponseParser responseParser = new NoOpResponseParser();
	    responseParser.setWriterType("json");

	    solrClient.setParser(responseParser);

	    CollectionAdminRequest collectionAdminRequest = new CollectionAdminRequest.List();
	    try{
	    NamedList<Object> collectionAdminResponse = solrClient.request(collectionAdminRequest);

	    JsonNode collectionsJsonNode = om.readTree((String) collectionAdminResponse.get("response")).get("collections");

	    for (Iterator<JsonNode> i = collectionsJsonNode.iterator(); i.hasNext(); ) {
	        String collection = i.next().textValue();
	        if (!collections.contains(collection)) {
	            collections.add(collection);
	        }
	    }
	    }catch(Exception e){
	    	collections = null;
	    }
	    return collections;
	}
}
