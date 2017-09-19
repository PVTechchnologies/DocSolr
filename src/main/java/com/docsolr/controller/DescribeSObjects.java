package com.docsolr.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docsolr.dto.SalesforceMetadataTree;
import com.docsolr.entity.SalesforceSetupDetail;
import com.docsolr.entity.Users;
import com.docsolr.service.common.GenericService;
import com.docsolr.util.CommonUtil;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.PicklistEntry;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

@Controller
public class DescribeSObjects {
	
	@Autowired
	public GenericService<SalesforceSetupDetail> salesforceSetupDetail;
	
	 public PartnerConnection partnerConnection ;
	 
	  @RequestMapping(value = "/recieveObjects", method = RequestMethod.GET)
  	@ResponseBody     
  	public Object getFieldData() throws RemoteException, Exception{
	        if (login()) {
	            
	        	// Add calls to the methods in this class.
	        	String[] result = describeGlobalSample();
	        	Object strJsonObj=describeSObjectsSample(result);
	    		return strJsonObj;	
	        }
	        else
	        {
	        	return null;
	        }
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

	public Object describeSObjectsSample(String[] objects) {
		
		String resultSb=null;
		Users users=new Users();
    	users = CommonUtil.getCurrentSessionUser();
    	
    	
		
		List<String> ObjectList = new ArrayList<String>(Arrays.asList(objects));
		List<String> FieldsList = new ArrayList<String>();
		
		Map<String, SalesforceSetupDetail> tableData = new HashMap<>();
		tableData = salesforceSetupDetail.getKeyValueMapString("SalesforceSetupDetail", "salesforceObjectApiName", "SalesforceSetupDetail", " Where createdById="+users.getId());

		String[]batchArray=new String[100];
    	List<String> listOfStrings=new ArrayList<>();
    /*	List<int[]> ObjectArrayList = new ArrayList<int[]>();*/
    	for(int j=0;j<objects.length;j++){
    		listOfStrings.add(objects[j]);
    		if(listOfStrings.size()==99 || j==objects.length-1)
    		{
    			batchArray = listOfStrings.toArray(new String[listOfStrings.size()]);
    			listOfStrings=new ArrayList<>();
    		
		try {    	
				// Call describeSObjectResults and pass it an array with
				// the names of the objects to describe.
	    
				DescribeSObjectResult[] describeSObjectResults = partnerConnection
						.describeSObjects(batchArray);
	
				// Iterate through the list of describe sObject results
				for (int i = 0; i < describeSObjectResults.length; i++) {
					DescribeSObjectResult desObj = describeSObjectResults[i];
					// Get the name of the sObject
					String objectName = desObj.getName();
					System.out.println("sObject name: " + objectName);
					// For each described sObject, get the fields
					Field[] fields = desObj.getFields();
					StringBuilder sb = new StringBuilder();
					// Get some other properties
					if (desObj.getActivateable())
						System.out.println("\tActivateable");
	
					// Iterate through the fields to get properties for each field
					for (int z = 0; z < fields.length; z++) {
						Field field = fields[z];
						System.out.println("\tField: " + field.getName());
						sb.append(field.getName()).append(",");
					}
					
					resultSb = sb.deleteCharAt(sb.length() - 1).toString();
		    		FieldsList.add(resultSb);

					
				}
			
				
					}catch (ConnectionException ce) {
		    			ce.printStackTrace();
		    			return null;
		    		}
	    		} 
	    	}
    		List<SalesforceMetadataTree> treeMapDataList = new ArrayList<>();
			treeMapDataList=metaDataList(ObjectList,FieldsList,tableData,treeMapDataList);
			return treeMapDataList;
			
		
	}
	
	public List<SalesforceMetadataTree> metaDataList(List<String> objectArray,List<String> fieldArray, Map<String, SalesforceSetupDetail> hashTable,List<SalesforceMetadataTree> treeMapDataList) {

		Map<String, String[]> treeMap = new HashMap<>();
		
		
		for (int i=0 ;i < objectArray.size(); i++) {

			Map map = new HashMap();
			map.put(objectArray.get(i), fieldArray.get(i));
			
			// Traversing Map
			Set set = map.entrySet();// Converting to Set so that we can
										// traverse
			Iterator itr = set.iterator();
			while (itr.hasNext()) {
				boolean ischecked = false;
				SalesforceMetadataTree mtc = null;
				List<SalesforceMetadataTree> mtca = new ArrayList<SalesforceMetadataTree>();
				// Converting to Map.Entry so that we can get key and value
				// separately
				Map.Entry entry = (Map.Entry) itr.next();
				System.out.println(entry.getKey() + " " + entry.getValue());
				String[] sarray = entry.getValue().toString().split(",");
				treeMap.put(entry.getKey().toString(), sarray);
				SalesforceMetadataTree child = null;
				SalesforceSetupDetail colvalues = hashTable.get(entry.getKey().toString());
				for (int k = 0; k < sarray.length; k++) {
					child = new SalesforceMetadataTree();
					child.setName(sarray[k]);
					if (colvalues != null && colvalues.getSalesforceFields().contains(sarray[k])) {
						child.setChecked(true);
						ischecked=true;
					} else {
						child.setChecked(false);
					}
					mtca.add(child);
				}
				
				mtc = new SalesforceMetadataTree();
				mtc.setName(entry.getKey().toString());
				if(!ischecked)
				{
					mtc.setChecked(false);
				}else
				{
					mtc.setChecked(true);
				}
				
				mtc.setChildren(mtca);
			
				if(colvalues!= null){
					mtc.setId(colvalues.getId());
				}
				treeMapDataList.add(mtc);
			}

		}
		System.out.println(treeMapDataList);
		return treeMapDataList;

	}
	
	public String[] describeGlobalSample() {
		
		List<String> listOfStrings=new ArrayList<>();
		String[] arr = null;
	    try {
	        // Make the describeGlobal() call
	        DescribeGlobalResult describeGlobalResult = 
	            partnerConnection.describeGlobal();
	        
	        // Get the sObjects from the describe global result
	        DescribeGlobalSObjectResult[] sobjectResults = 
	            describeGlobalResult.getSobjects();
	       
	        // Write the name of each sObject to the console
	        listOfStrings.add("MyCustomObject__c");
	        for (int i = 0; i < sobjectResults.length; i++) {
	        	listOfStrings.add(sobjectResults[i].getName());
	          System.out.println(sobjectResults[i].getName());
	        }
	        
	        arr = listOfStrings.toArray(new String[listOfStrings.size()]);
	    	return arr;
	    } catch (ConnectionException ce) {
	        ce.printStackTrace();
	    	return arr;
	    }
	}
	
	/*public void createBatch(String[] objects ){
		describeSObjectsSample(result);
    	String[]batchArray=new String[100];
    	List<String> listOfStrings=new ArrayList<>();
    	List<int[]> ObjectArrayList = new ArrayList<int[]>();
    	for(int i=0;i<result.length;i++){
    		listOfStrings.add(result[i]);
    		if(listOfStrings.size()==99)
    		{
    			batchArray = listOfStrings.toArray(new String[listOfStrings.size()]);
    			listOfStrings=new ArrayList<>();
    			
    		}
    	}
	}*/
}
