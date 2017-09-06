package com.docsolr.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docsolr.entity.SalesforceSetupDetail;
import com.docsolr.entity.Users;
import com.docsolr.service.common.GenericService;
import com.docsolr.util.CommonUtil;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.bind.XmlObject;


@Controller
public class PartnerQueryController {

		@Autowired
		public GenericService<SalesforceSetupDetail> salesforceSetupDetail;
	
	    public PartnerConnection partnerConnection ;
		
	    private static BufferedReader reader =
	        new BufferedReader(new InputStreamReader(System.in));
	    
	    @RequestMapping(value = "/recieveRecord", method = RequestMethod.GET)
    	@ResponseBody     
    	public Object getFieldData() throws RemoteException, Exception{
	        if (login()) {
	            
	        	// Add calls to the methods in this class.
	            
	        	Object strJsonObj=querySample();
	        	return strJsonObj;
	        }
	        else
	        {
	        	return null;
	        }
	    } 
	    
	    private String getUserInput(String prompt) {
	        String result = "";
	        try {
	          System.out.print(prompt);
	          result = reader.readLine();
	        } catch (IOException ioe) {
	          ioe.printStackTrace();
	        }
	        return result;
	    }
	    
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

	    
	    public Object querySample() throws ConnectionException {    
	    	 SObject[] records=null;
	    	 Set<Long> setOfID = new HashSet<Long>();
	    	 Users users=new Users();
	    	 users = CommonUtil.getCurrentSessionUser();
	    	 
	    	Map<String, SalesforceSetupDetail> tableData = new HashMap<>();
	        tableData = salesforceSetupDetail.getKeyValueMapString("SalesforceSetupDetail", "salesforceObjectApiName", "SalesforceSetupDetail", " Where createdById="+users.getId());
	       
	        List<String> query = new ArrayList<String>();
	        for (Map.Entry<String, SalesforceSetupDetail> entrySet:tableData.entrySet()) {
	        	SalesforceSetupDetail ssd = entrySet.getValue();
				query.add("Select id,"+ssd.getSalesforceFields()+" from "+entrySet.getKey());
				setOfID.add(ssd.getId());
				
			}
	        
	       
        	for(int j=0;j<query.size();j++)
        	{
        		String soqlQuery=query.get(j);
        	
	        try {
	            // Set query batch size
	            partnerConnection.setQueryOptions(250);
	            
	            // Make the query call and get the query results
	             
	            QueryResult qr = partnerConnection.query(soqlQuery);
	            
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
	        } catch(ConnectionException ce) {
	            ce.printStackTrace();
	        }
	    
	        System.out.println("\nQuery execution completed.");     
	    }
        	Set<String> ids = new HashSet<String>();
        	for (SObject so : records) {
        		Iterator<XmlObject> sxml =so.getChildren(); 
        		while(sxml.hasNext()){
        			System.out.println("===");
        			XmlObject xobj = sxml.next();
        			if(xobj.getName().toString().equalsIgnoreCase("{urn:sobject.partner.soap.sforce.com}Id")){
        				System.out.println(xobj.getName().toString());
        				ids.add(xobj.getValue().toString());
        			}
        		}
			}
       
        	getAttachment(ids);
        	System.out.println(ids);
	        return records;
	  
	    }
	    
	   private void getAttachment(Set<String> setOfId) throws ConnectionException{
		   partnerConnection.setQueryOptions(250);
		   
		   
		   StringBuilder result = new StringBuilder();
		   
		   for(String string : setOfId) {
		        result.append("\'"+string+"\'");
		        result.append(",");
		    }
		 
		   result.deleteCharAt(result.length() - 1);
		   System.out.println(result);
		   
		   String soqlQuery="SELECT Id, body FROM Attachment Where ParentID IN("+ result +")";
		   
		   QueryResult qr = partnerConnection.query(soqlQuery);
		   SObject[] records=null; 
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
           
           Set<String> ids = new HashSet<String>();
       		for (SObject so : records) {
       		Iterator<XmlObject> sxml =so.getChildren(); 
       		while(sxml.hasNext()){
       			System.out.println("===");
       			XmlObject xobj = sxml.next();
       			if(xobj.getName().toString().equalsIgnoreCase("{urn:sobject.partner.soap.sforce.com}Body")){
       				System.out.println(xobj.getName().toString());
       				ids.add(xobj.getValue().toString());
       			}
       		}
			}
       		System.out.println(ids);
		   System.out.println("attachment >>>>>>..."+records);
	
	    }
	  
	}


