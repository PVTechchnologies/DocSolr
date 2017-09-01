package com.docsolr.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.docsolr.common.dao.GenericDAO;
import com.docsolr.dto.SalesforceMetadataTree;
import com.docsolr.entity.SalesforceSetupDetail;
import com.docsolr.entity.Users;
import com.docsolr.service.common.GenericService;
import com.docsolr.util.CommonUtil;
import com.docsolr.util.UnZipUtil;
import com.sforce.soap.metadata.AsyncResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.PackageTypeMembers;
import com.sforce.soap.metadata.RetrieveMessage;
import com.sforce.soap.metadata.RetrieveRequest;
import com.sforce.soap.metadata.RetrieveResult;
import com.sforce.soap.metadata.RetrieveStatus;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * @author Yadav
 *
 */
@Controller
public class SalesforceMetadataController {
	

	@Autowired
	public GenericService<SalesforceSetupDetail> salesforceSetupDetail;
	
	@Autowired
	public GenericDAO<SalesforceSetupDetail> SFSD;
	
	private UnZipUtil unZipUtil;
	
	Map<String, String[]> treeMap = new HashMap<>();
    // Binding for the metadata WSDL used for making metadata API calls
    private MetadataConnection metadataConnection;
    
    static BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));

    // one second in milliseconds
    private static final long ONE_SECOND = 1000;
    // maximum number of attempts to retrieve the results
    private static final int MAX_NUM_POLL_REQUESTS = 50; 

    // manifest file that controls which components get retrieved
    private static final String MANIFEST_FILE = "C:\\Users\\Yadav\\git\\DocSolr\\package.xml"; 

    private static final double API_VERSION = 40.0; 
    
    
    

     /*   String USERNAME = "hiteshyadav@mtxb2b.com";
        // This is only a sample. Hard coding passwords in source files is a bad practice.
        String PASSWORD = "123456789t"; */
 /*       String URL = "https://login.salesforce.com/services/Soap/u/40.0";*/
        String URL = "https://ap5.salesforce.com/services/Soap/m/40.0/00D7F000001a7Nw";
        
        @RequestMapping(value = "/recieveZip", method = RequestMethod.GET)
    	@ResponseBody        
    	public Object getXmlZip() throws RemoteException, Exception{
    		String Accesstoken = SalesforceController.acctoken;
    		createMetadataConnection(Accesstoken, URL);
    		Object strJsonObj=retrieveZip();
    		return strJsonObj;
    	}
        
    
    private Object retrieveZip() throws RemoteException, Exception
    {	
    	Users users=new Users();
    	users = CommonUtil.getCurrentSessionUser();
    	
    	String jsonobjectofmetadata;
        RetrieveRequest retrieveRequest = new RetrieveRequest();
        // The version in package.xml overrides the version in RetrieveRequest
        retrieveRequest.setApiVersion(API_VERSION);
        setUnpackaged(retrieveRequest);
       
        // Start the retrieve operation
        AsyncResult asyncResult = metadataConnection.retrieve(retrieveRequest);
        String asyncResultId = asyncResult.getId();
        
        // Wait for the retrieve to complete
        int poll = 0;
        long waitTimeMilliSecs = ONE_SECOND;
        RetrieveResult result = null;
        do {
            Thread.sleep(waitTimeMilliSecs);
            // Double the wait time for the next iteration
            waitTimeMilliSecs *= 2;
            if (poll++ > MAX_NUM_POLL_REQUESTS) {
                throw new Exception("Request timed out.  If this is a large set " +
                "of metadata components, check that the time allowed " +
                "by MAX_NUM_POLL_REQUESTS is sufficient.");
            }
            result = metadataConnection.checkRetrieveStatus(
                    asyncResultId, true);
            
            byte[] resultzip= result.getZipFile();
            unZipUtil = new UnZipUtil();
            jsonobjectofmetadata = unZipUtil.unZipIt(resultzip);
            System.out.println("Retrieve Status: " + result.getStatus());
          
            
        } while (!result.isDone());

        if (result.getStatus() == RetrieveStatus.Failed) {
            throw new Exception(result.getErrorStatusCode() + " msg: " +
                    result.getErrorMessage());
        } else if (result.getStatus() == RetrieveStatus.Succeeded) {      
            // Print out any warning messages
            StringBuilder buf = new StringBuilder();
            if (result.getMessages() != null) {
                for (RetrieveMessage rm : result.getMessages()) {
                    buf.append(rm.getFileName() + " - " + rm.getProblem());
                }
            }
            if (buf.length() > 0) {
                System.out.println("Retrieve warnings:\n" + buf);
            }    
        }
        
        
        if(users!=null)
		{
        	Map<String, SalesforceSetupDetail> tableData = new HashMap<>();
        	tableData = salesforceSetupDetail.getKeyValueMapString("SalesforceSetupDetail", "salesforceObjectApiName", "SalesforceSetupDetail", " Where createdById="+users.getId());
        	
        	ArrayList<String> CustomObjectList = new ArrayList<String>();
			ArrayList<String> StandardObjectList = new ArrayList<String>();
			ArrayList<String> CustomFieldsList = new ArrayList<String>();
			ArrayList<String> StandardFieldsList = new ArrayList<String>();

			JSONObject jobject = new JSONObject(jsonobjectofmetadata);
			JSONArray CustomArray = jobject.getJSONArray("CustomObject");
			JSONObject objectForParent = jobject.getJSONObject("Package");
			JSONObject innnerParentObj = objectForParent.getJSONObject("types");
			JSONArray standardArray = innnerParentObj.getJSONArray("members");
			for (int z = 0; z < standardArray.length(); z++) {

				StandardObjectList.add(standardArray.get(z).toString());
			}
			System.out.println(StandardObjectList);
			
			for (int i = 0; i < CustomArray.length(); i++) {
				StringBuilder sb = new StringBuilder();
				JSONObject c = CustomArray.getJSONObject(i);

				JSONArray FieldsArray = c.getJSONArray("fields");
				for (int j = 0; j < FieldsArray.length(); j++) {
					JSONObject f = FieldsArray.getJSONObject(j);
					String name = f.getString("fullName");
					sb.append(name).append(",");
				}
				String resultSb = sb.deleteCharAt(sb.length() - 1).toString();
				if (c.has("label")) {
					CustomObjectList.add(c.getString("label"));
					CustomFieldsList.add(resultSb);
				} else {
					StandardFieldsList.add(resultSb);
				}

			}

	
			/*Calling of method to create JSON for treeview*/
			
			List<SalesforceMetadataTree> treeMapDataList = new ArrayList<>();
			treeMapDataList = metaDataList(CustomObjectList,CustomFieldsList,tableData,treeMapDataList,"Custom");
			treeMapDataList = metaDataList(StandardObjectList,StandardFieldsList,tableData,treeMapDataList,"Standard");
			return treeMapDataList;
		}
        else
        {
        	return null;
        }
    }
     
    private void setUnpackaged(RetrieveRequest request) throws Exception
    {
        // Edit the path, if necessary, if your package.xml file is located elsewhere
        File unpackedManifest = new File(MANIFEST_FILE);
        System.out.println("Manifest file: " + unpackedManifest.getAbsolutePath());
        
        if (!unpackedManifest.exists() || !unpackedManifest.isFile())
            throw new Exception("Should provide a valid retrieve manifest " +
                    "for unpackaged content. " +
                    "Looking for " + unpackedManifest.getAbsolutePath());

        // Note that we populate the _package object by parsing a manifest file here.
        // You could populate the _package based on any source for your
        // particular application.
        com.sforce.soap.metadata.Package p = parsePackage(unpackedManifest);
        request.setUnpackaged(p);
    }

    private com.sforce.soap.metadata.Package parsePackage(File file) throws Exception {
        try {
            InputStream is = new FileInputStream(file);
            List<PackageTypeMembers> pd = new ArrayList<PackageTypeMembers>();
            DocumentBuilder db =
                DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Element d = db.parse(is).getDocumentElement();
            for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling()) {
                if (c instanceof Element) {
                    Element ce = (Element)c;
                    //
                    NodeList namee = ce.getElementsByTagName("name");
                    if (namee.getLength() == 0) {
                        // not
                        continue;
                    }
                    String name = namee.item(0).getTextContent();
                    NodeList m = ce.getElementsByTagName("members");
                    List<String> members = new ArrayList<String>();
                    for (int i = 0; i < m.getLength(); i++) {
                        Node mm = m.item(i);
                        members.add(mm.getTextContent());
                    }
                    PackageTypeMembers pdi = new PackageTypeMembers();
                    pdi.setName(name);
                    pdi.setMembers(members.toArray(new String[members.size()]));
                    pd.add(pdi);
                }

            }
            com.sforce.soap.metadata.Package r = new com.sforce.soap.metadata.Package();
            r.setTypes(pd.toArray(new PackageTypeMembers[pd.size()]));
            r.setVersion(API_VERSION + "");
            return r;
        } catch (ParserConfigurationException pce) {
            throw new Exception("Cannot create XML parser", pce);
        } catch (IOException ioe) {
            throw new Exception(ioe);
        } catch (SAXException se) {
            throw new Exception(se);
        }
    }
    
 
    private void createMetadataConnection(final String Accesstoken,final String loginUrl)throws ConnectionException {

    /*    final ConnectorConfig loginConfig = new ConnectorConfig();
        loginConfig.setAuthEndpoint(loginUrl);
        loginConfig.setServiceEndpoint(loginUrl);
        loginConfig.setManualLogin(true);
        PartnerConnection pc = new PartnerConnection(loginConfig);
        LoginResult loginResult = pc.login(username, password);*/

        final ConnectorConfig metadataConfig = new ConnectorConfig();
      
        metadataConfig.setServiceEndpoint(loginUrl);
        metadataConfig.setSessionId(Accesstoken);
        this.metadataConnection = new MetadataConnection(metadataConfig);
    }
    
    //The sample client application retrieves the user's login credentials.
    // Helper function for retrieving user input from the console
    String getUserInput(String prompt) {
        System.out.print(prompt);
        try {
            return rdr.readLine();
        }
        catch (IOException ex) {
            return null;
        }
    }

    

    // Method for inserting tree data in database
    
    @RequestMapping(value = "/addObjects", method = RequestMethod.POST)
	
	 public String addObjects(@RequestBody String selecteditem) {
			      
		 String key,selected,oldKey="";
		 boolean haveID =false;
		 StringBuilder result = new StringBuilder();
		 Long id=null,oldid=null,tempid=null;
		 Integer idStringvalue = null;
		 JSONArray jsonArray=new JSONArray(selecteditem);

		 
		 /*Code to detect which record needs to be delete Starts here*/
		 ArrayList<String> listdata = new ArrayList<String>();     
		 if (jsonArray != null) {
			
		    for (int i=0;i<jsonArray.length();i++){ 
		    	 JSONObject jsonObject=jsonArray.getJSONObject(i);
		    	try{
		    		   listdata.add(Integer.toString(jsonObject.getInt("idvalue")));
		    	 
		    	}
		    	catch(Exception e)
		    	{
		    		continue;
		    	}
		    } 
		 } 
		
		 Users users=new Users();
		 users = CommonUtil.getCurrentSessionUser();
		 Map<String, SalesforceSetupDetail> tableData = new HashMap<>();
     	tableData = salesforceSetupDetail.getKeyValueMapString("SalesforceSetupDetail", "salesforceObjectApiName", "SalesforceSetupDetail", " Where createdById="+users.getId());
     	
     	ArrayList<String> ListOfID = new ArrayList<String>();
     	/*Creating list of ID coming from databse starts here*/
     	Iterator<Map.Entry<String, SalesforceSetupDetail>> entries = tableData.entrySet().iterator();
     	while (entries.hasNext()) {
     	    Map.Entry<String, SalesforceSetupDetail> entry = entries.next();
     	    SalesforceSetupDetail colvalues = tableData.get(entry.getKey().toString());
     	    ListOfID.add(colvalues.getId().toString());   
     	}
		
			Collection<String> selectedList = new ArrayList(listdata);
			Collection<String> unSelectedList = new ArrayList(ListOfID);

			List<String> sourceList = new ArrayList<String>(selectedList);
			List<SalesforceSetupDetail> ssdlist = new ArrayList<SalesforceSetupDetail>();
			List<String> destinationList = new ArrayList<String>(unSelectedList);
			destinationList.removeAll(sourceList);
			for (String string : destinationList) {
				SalesforceSetupDetail ssd = new SalesforceSetupDetail();
				ssd.setId(Long.parseLong(string));
				ssdlist.add(ssd);
			}
			System.out.println(destinationList);

			if(!ssdlist.isEmpty())
			{
				SFSD.deleteBatchEntity(SalesforceSetupDetail.class, ssdlist);
			}	
		  /*End here*/
		  
		 List<SalesforceSetupDetail> listssd = new ArrayList<SalesforceSetupDetail>();
		 for(int i=0;i<jsonArray.length();i++)
		 {
			 
			 JSONObject jsonObject=jsonArray.getJSONObject(i);
			 key=jsonObject.getString("key");
			 selected=jsonObject.getString("selected");
			 idStringvalue=!jsonObject.get("idvalue").equals(null)? ((Integer)jsonObject.get("idvalue")) : null;
			 if(idStringvalue!=null)
			 {
				 id = new Long(idStringvalue.toString());
			 }
			 
			if(!key.equalsIgnoreCase(selected))/*only go inside for field name*/
			{
				
				if (!oldKey.equalsIgnoreCase(key)) {

					if (oldKey!="") {/*This will run whenever new object name occurs and store previously build String*/
						/*All objects were stored from here except last one*/
						result.deleteCharAt(result.length() - 1);
						SalesforceSetupDetail detail = new SalesforceSetupDetail(oldKey, result.toString());
						detail.setId(oldid);
						tempid=oldid;
						listssd.add(detail);
						result = new StringBuilder();
					}
				}

				
				oldid=id;
				oldKey = key;
				result.append(selected);
				result.append(",");
				
				/*This will run only for last object*/
				if (i == jsonArray.length() - 1) {
					result.deleteCharAt(result.length() - 1);
					SalesforceSetupDetail detail = new SalesforceSetupDetail(oldKey, result.toString());
					if(tempid!=oldid)
					{
						detail.setId(oldid);
					}
					listssd.add(detail);
				}
			}
			
		 }
		 salesforceSetupDetail.saveUpdateBatchEntity(SalesforceSetupDetail.class, listssd);
		 System.out.println(selecteditem);
		    return null;
	 }

    
    // Method for creating tree JSON data with checked or unchecked type.
    
	public List<SalesforceMetadataTree> metaDataList(ArrayList<String> objectArray,ArrayList<String> fieldArray, Map<String, SalesforceSetupDetail> hashTable,List<SalesforceMetadataTree> treeMapDataList,String type) {

		int i=0 ;
		if(!type.equalsIgnoreCase("Custom")){
			i=1;
		}
		for ( ;i < objectArray.size(); i++) {

			Map map = new HashMap();
			// Adding elements to map
			if(type.equalsIgnoreCase("Custom"))
			{
				
				map.put(objectArray.get(i), fieldArray.get(i));
			}
			else
			{
				
				map.put(objectArray.get(i), fieldArray.get(i-1));
				
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

