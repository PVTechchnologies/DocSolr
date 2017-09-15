package com.docsolr.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	        	
	        	Object strJsonObj=describeSObjectsSample();
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

	public Object describeSObjectsSample() {
		
		
		List<String> StandardObjectList = new ArrayList<String>(Arrays.asList("account", "contact", "lead"));
		List<String> StandardFieldsList = new ArrayList<String>();

		/*List<String> CustomObjectList = new ArrayList<String>(Arrays.asList("MyCustomObjects"));
		List<String> CustomFieldsList = new ArrayList<String>();*/
		
		try {
			String resultSb=null;
			Users users=new Users();
	    	users = CommonUtil.getCurrentSessionUser();
	    	
			Map<String, SalesforceSetupDetail> tableData = new HashMap<>();
        	tableData = salesforceSetupDetail.getKeyValueMapString("SalesforceSetupDetail", "salesforceObjectApiName", "SalesforceSetupDetail", " Where createdById="+users.getId());
			// Call describeSObjectResults and pass it an array with
			// the names of the objects to describe.
			DescribeSObjectResult[] describeSObjectResults = partnerConnection
					.describeSObjects(new String[] {"account", "contact", "lead" });

			// Iterate through the list of describe sObject results
			for (int i = 0; i < describeSObjectResults.length; i++) {
				DescribeSObjectResult desObj = describeSObjectResults[i];
				// Get the name of the sObject
				String objectName = desObj.getName();
				System.out.println("sObject name: " + objectName);
				StringBuilder sb = new StringBuilder();
				// For each described sObject, get the fields
				Field[] fields = desObj.getFields();

				// Get some other properties
				if (desObj.getActivateable())
					System.out.println("\tActivateable");

				// Iterate through the fields to get properties for each field
				for (int j = 0; j < fields.length; j++) {
					Field field = fields[j];
					System.out.println("\tField: " + field.getName());
					sb.append(field.getName()).append(",");
				}
				resultSb = sb.deleteCharAt(sb.length() - 1).toString();
	
				StandardFieldsList.add(resultSb);
			}
			

			List<SalesforceMetadataTree> treeMapDataList = new ArrayList<>();
			/*treeMapDataList=metaDataList(CustomObjectList,CustomFieldsList,tableData,treeMapDataList,"Custom");*/
			treeMapDataList=metaDataList(StandardObjectList,StandardFieldsList,tableData,treeMapDataList,"Standard");
			return treeMapDataList;
			
		} catch (ConnectionException ce) {
			ce.printStackTrace();
			return null;
		}
	}
	
	public List<SalesforceMetadataTree> metaDataList(List<String> objectArray,List<String> fieldArray, Map<String, SalesforceSetupDetail> hashTable,List<SalesforceMetadataTree> treeMapDataList,String type) {

		Map<String, String[]> treeMap = new HashMap<>();
		
		/*int i=0 ;
		if(!type.equalsIgnoreCase("Custom")){
			i=1;
		}*/
		for (int i=0 ;i < objectArray.size(); i++) {

			Map map = new HashMap();
			// Adding elements to map
			if(type.equalsIgnoreCase("Custom"))
			{
				
				map.put(objectArray.get(i), fieldArray.get(i));
			}
			else
			{
				
				map.put(objectArray.get(i), fieldArray.get(i));
				
			}
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
}
