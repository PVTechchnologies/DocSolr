package com.docsolr.Sharepoint;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docsolr.entity.SiteFileInfo;
import com.docsolr.entity.SiteFolder;
import com.docsolr.entity.SiteInfo;
import com.docsolr.entity.SiteLibrary;
import com.docsolr.service.common.GenericService;
import com.docsolr.util.CommonUtil;



/**
 * @author gangparia
 *
 */
@Service
public class SharePointCallout {

	@Autowired
	GenericService<SiteInfo> siteInfoService;
	
	@Autowired
	GenericService<SiteLibrary> siteLibraryService;
	
	@Autowired
	GenericService<SiteFolder> siteFolderService;
	
	@Autowired
	GenericService<SiteFileInfo> sitefileinfoService;
	
	@Autowired
	LoginManager loginObj;
	
	
	@Autowired
	DataService dataService;
	
	
	public void fecthSharePointData(){
		LoginManager.LoginDetail logDetail = loginObj.login();
		DataService.DataWrapper dataWrap = dataService.getSiteInfoMap();
		getALlSharePointSites(logDetail,dataWrap);
		System.out.println("commited succesffully");
		
	}
	
	// in use to get all sites information
	public  String getALlSharePointSites(LoginManager.LoginDetail logDetail, DataService.DataWrapper dataWrap )  {
		String endPoint = "https://pgangparia.sharepoint.com/_api/search/query?querytext=%27contentclass:sts_site%27&amp;Key=SPWebUrl";
		Map<String,SiteInfo> siteInfoMap = dataWrap.siteInfoMap;
		try{

			HttpURLConnection connection = CommonUtil.getConnectionForGetRequest(endPoint, logDetail.formDigestValue, logDetail.cookie);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			List<SiteInfo> sites = new ArrayList<SiteInfo>();
			List<SiteInfo> subSites = new ArrayList<SiteInfo>();
			while ((line = reader.readLine()) != null) {
				JSONObject lineRes = new JSONObject(line);
				JSONObject tableRows = lineRes.getJSONObject("d").getJSONObject("query").getJSONObject("PrimaryQueryResult").getJSONObject("RelevantResults").getJSONObject("Table").getJSONObject("Rows");
				JSONArray array1 = (JSONArray) tableRows.get("results");
				for (int i = 0; i < array1.length(); i++) {
					JSONObject jsonObject1 = (JSONObject) array1.get(i);
					JSONObject tableCells = jsonObject1.getJSONObject("Cells");
					JSONArray array2 = (JSONArray) tableCells.get("results");
					for (int j = 0; j < array2.length(); j++) {
						JSONObject jsonObject2 = (JSONObject) array2.get(j);
						if(jsonObject2.getString("Key").equals("SPWebUrl")){
							String res = jsonObject2.has("Value") && !jsonObject2.isNull("Value") ? jsonObject2.getString("Value") : null;
							if(res!=null){
								
								String siteURL = res;
								URL sitObj = new URL(siteURL);
								String sitePath = sitObj.getPath();
								String siteName="/";
								if(!sitePath.isEmpty()){
									String[] resList = sitePath.split("/sites/");
									if(resList.length > 1){
										siteName = resList[1];
									}
								}
								SiteInfo provider = new SiteInfo();
								if(siteInfoMap.containsKey(siteName))
									provider = siteInfoMap.get(siteName);
								
								provider.setSiteName(siteName);
								provider.setSiteURL(siteURL);
								siteInfoService.saveUpdateEntity(provider);
								/*if(provider.getId() ==null)
									siteInfoService.saveEntity(provider);
								else
									siteInfoService.updateEntity(provider);*/
								sites.add(provider);
								subSites.addAll( getAllSubSites (res,provider.getId(), logDetail, dataWrap));
							}
						}
					}
				}
			}
			sites.addAll(subSites);
			for(int i=0; i<sites.size();i++){
				getAllFilesFoldersFromSite(sites.get(i).getSiteURL(),sites.get(i).getId(), logDetail, dataWrap);
			}
			connection.disconnect();
			System.out.println("--sites--> "+sites);
		}catch(Exception e){
			System.out.println("Exceptin is -->"+e.getMessage()+e.getLocalizedMessage()+e.toString());
			e.printStackTrace();
			//CommonUtil.generateLog(e.getMessage(), 0,  e.getLocalizedMessage(), session);
		}finally{
			
		}
		return "";
	}

	// in use --> get all subsites
	public  List<SiteInfo> getAllSubSites(String siteURL,long parentSiteId, LoginManager.LoginDetail logDetail,  DataService.DataWrapper dataWrap ){
		Map<String,SiteInfo> siteInfoMap = dataWrap.siteInfoMap;
		List<SiteInfo> subSites = new ArrayList<SiteInfo>();
		String endPoint = siteURL+"/_api/web/webs/?$select=title,ServerRelativeUrl";
		try{
			URL obj = new URL(endPoint);
			HttpURLConnection connection = CommonUtil.getConnectionForGetRequest(endPoint, logDetail.formDigestValue, logDetail.cookie);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				JSONObject lineRes = new JSONObject(line);
				//System.out.println("--lineRes--> "+lineRes);
				JSONArray array2 =  (JSONArray)  lineRes.getJSONObject("d").get("results");
				for (int j = 0; j < array2.length(); j++) {
					JSONObject jsonObject2 = (JSONObject) array2.get(j);
					//String subSiteURL = obj.getProtocol()+"://"+ obj.getHost()+jsonObject2.get("ServerRelativeUrl");
					
					String subSiteURL = obj.getProtocol()+"://"+ obj.getHost()+jsonObject2.get("ServerRelativeUrl");
					URL sitObj = new URL(subSiteURL);
					String sitePath = sitObj.getPath();
					String siteName="/";
					//System.out.println("--sitePath-->"+sitePath);
					if(!sitePath.isEmpty() && sitePath.contains("sites")){
						String[] resList = sitePath.split("/sites/");
						System.out.println("----reslit---> "+resList[resList.length-1]);
						if(resList.length > 1){
							siteName = resList[resList.length-1];
							
						}
					}else{
						siteName = sitePath.substring(1,sitePath.length());
					}
					SiteInfo provider = new SiteInfo();
					if(siteInfoMap.containsKey(siteName))
						provider = siteInfoMap.get(siteName);
					provider.setSiteName(siteName);
					provider.setSiteURL(subSiteURL);
					provider.setParentSiteId(parentSiteId);
					siteInfoService.saveUpdateEntity(provider);
					/*if(provider.getId() == null)
						siteInfoService.saveEntity(provider);
					else
						siteInfoService.updateEntity(provider);*/
					subSites.add(provider);
				}
			}
			connection.disconnect();
		}catch(Exception e){
			System.out.println("Exceptin is- -->"+e.getMessage());
			//CommonUtil.generateLog(e.getMessage(), 0,  e.getLocalizedMessage(), session);
		}finally{
			
		}
		return subSites;
	}

	// in use --> get all site, subsite main folders/library
	public  void getAllFilesFoldersFromSite(String siteURL,Long siteId, LoginManager.LoginDetail logDetail, DataService.DataWrapper dataWrap){
		Map<String,SiteLibrary> siteLibraryMap = dataWrap.siteLibraryMap;
		try{
			String endPointURL = siteURL+"/_api/Web/Lists/?$filter=Hidden%20eq%20false";
			//System.out.println("----endPointURL--->"+endPointURL);
			URL obj = new URL(endPointURL);
			HttpURLConnection connection = CommonUtil.getConnectionForGetRequest(endPointURL, logDetail.formDigestValue, logDetail.cookie);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			List<String> sites = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				JSONObject lineRes = new JSONObject(line);
				//System.out.println("--lineRes--> "+lineRes);
				JSONArray array2 =  (JSONArray)  lineRes.getJSONObject("d").get("results");
				for (int j = 0; j < array2.length(); j++) {
					JSONObject jsonObject2 = (JSONObject) array2.get(j);
					if(((int)jsonObject2.get("ItemCount"))>0){
						
						String sitePath = obj.getPath();
						String siteName="/";
						System.out.println("--sitePath-->"+sitePath);
						if(!sitePath.isEmpty()){
							String[] resList = sitePath.split("/sites/");
							if(resList.length > 1){
								siteName = resList[1];
							}
						}
						SiteLibrary provider = new SiteLibrary();
						if(siteLibraryMap.containsKey((String)jsonObject2.get("Id")))
							provider = siteLibraryMap.get((String)jsonObject2.get("Id"));
						provider.setSiteURL(siteURL);
						provider.setSiteId(siteId);
						provider.setSiteName(siteName);
						provider.setHostURL(obj.getHost());
						provider.setGuid((String)jsonObject2.get("Id"));
						provider.setServerRelativeURL((String)jsonObject2.get("Title"));
						provider.setItemCount(((int)jsonObject2.get("ItemCount")));
						Date timeCreated = CommonUtil.convertStringToDate((String)jsonObject2.get("Created"));
						Date timeModified = CommonUtil.convertStringToDate((String)jsonObject2.get("LastItemModifiedDate"));
						provider.setTimeCreated(timeCreated);
						provider.setTimeLastModified(timeModified);
						siteLibraryService.saveUpdateEntity(provider);
						/*if(provider.getId() == null)
							siteLibraryService.saveEntity(provider);
						else
							siteLibraryService.updateEntity(provider);*/
						getAllFilesFromSite(siteURL,provider.getId(),provider.getServerRelativeURL(),logDetail, dataWrap);
					}
				}
			}
			connection.disconnect();
		}catch(Exception e){
			System.out.println("Exceptin is ------->"+e.getMessage());
			//CommonUtil.generateLog(e.getMessage(), 0,  e.getLocalizedMessage(), session);
		}finally{
			
		}

	}

	// in use -- > use to get all folder info from site menu folders
	public  void getAllFilesFromSite(String siteURL,Long siteLibraryId, String serverRelativeUrl, LoginManager.LoginDetail logDetail,  DataService.DataWrapper dataWrap){
		Map<String, SiteFolder> siteFolderMap = dataWrap.siteFolderMap;
		try{
			String[] paths = serverRelativeUrl.split("/");
			serverRelativeUrl = paths[paths.length - 1];
			serverRelativeUrl = CommonUtil.replaceSpace(serverRelativeUrl);
			String  endPointURL =siteURL+"/_api/web/Lists/GetByTitle('"+serverRelativeUrl+"')/Items?$expand=Folder&$select=Title,File";
			URL obj = new URL(endPointURL);
			HttpURLConnection connection = CommonUtil.getConnectionForGetRequest(endPointURL, logDetail.formDigestValue, logDetail.cookie);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			List<String> sites = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				JSONObject lineRes = new JSONObject(line);
				System.out.println("--linres--> "+lineRes);
				JSONArray array2 =  (JSONArray)  lineRes.getJSONObject("d").get("results");
				for (int j = 0; j < array2.length(); j++) {
					JSONObject jsonObject2 = (JSONObject) array2.get(j);
					JSONObject folderObj  = (JSONObject) jsonObject2.get("Folder");
					if(folderObj.has("ItemCount") && ((int)folderObj.get("ItemCount"))>0){
						
						String sitePath = obj.getPath();
						String siteName="/";
						if(!sitePath.isEmpty()){
							String[] resList = sitePath.split("/sites/");
							if(resList.length > 1)
								siteName = resList[1].split("/")[0];
						}
						SiteFolder provider = new SiteFolder();
						System.out.println("----getting i---uniqued----id0----------------");
						if(siteFolderMap.containsKey((String)folderObj.get("UniqueId")))
							provider = siteFolderMap.get((String)folderObj.get("UniqueId"));
						provider.setSiteName(siteName);
						provider.setHostURL(obj.getHost());
						provider.setUniqueId((String)folderObj.get("UniqueId"));
						//provider.setGuid((String)folderObj.get("UniqueId"));
						provider.setServerRelativeURL((String)folderObj.get("ServerRelativeUrl"));
						provider.setItemCount(((int)folderObj.get("ItemCount")));
						provider.setSiteLibraryId(siteLibraryId);
						Date timeCreated = CommonUtil.convertStringToDate((String)folderObj.get("TimeCreated"));
						Date timeModified = CommonUtil.convertStringToDate((String)folderObj.get("TimeLastModified"));
						provider.setTimeCreated(timeCreated);
						provider.setTimeLastModified(timeModified);
						siteFolderService.saveUpdateEntity(provider);
						/*if(provider.getId() ==null)
							siteFolderService.saveEntity(provider);
						else
							siteFolderService.updateEntity(provider);*/
						getAllFilesInfo(siteURL,provider.getId(),provider.getServerRelativeURL(), logDetail, dataWrap);
					}
				}
			}
			connection.disconnect();
		}catch(Exception e){
			System.out.println("Exceptin is--- -->"+e.getMessage());
			//CommonUtil.generateLog(e.getMessage(), 0,  e.getLocalizedMessage(), session);
		}finally{
			
		}

	}

	// in use -->  used to get all files from a folder
	public  void getAllFilesInfo(String siteURL, long folderId, String serverRelativeUrl, LoginManager.LoginDetail logDetail, DataService.DataWrapper dataWrap ){
		Map<String, SiteFileInfo> siteFileInfoMap = dataWrap.siteFileInfoMap;
		try{
			serverRelativeUrl = CommonUtil.replaceSpace(serverRelativeUrl);
			String endPointURL = siteURL+"/_api/web/getfolderbyserverrelativeurl('"+serverRelativeUrl+"')?$expand=Files";
			URL obj = new URL(endPointURL);
			HttpURLConnection connection = CommonUtil.getConnectionForGetRequest(endPointURL, logDetail.formDigestValue, logDetail.cookie);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			List<String> sites = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				JSONObject lineRes = new JSONObject(line);
				System.out.println("--lineRes--> "+lineRes);
				JSONObject jsonObj =  (JSONObject)  lineRes.getJSONObject("d");
				getAllFolderFiles(jsonObj,folderId,siteURL, siteFileInfoMap);
			}
			connection.disconnect();
		}catch(Exception e){
			System.out.println("Exceptin is - -->"+e.getMessage()+e.getLocalizedMessage()+e.getCause()+e.getStackTrace());
			//CommonUtil.generateLog(e.getMessage(), 0,  e.getLocalizedMessage(), session);
		}finally{
			
		}
	}

	// in use
	public  List<SiteFileInfo> getAllFolderFiles(JSONObject  resObj,long folderId, String siteURL,  Map<String, SiteFileInfo> siteFileInfoMap ){
		List<SiteFileInfo>  files = new ArrayList<SiteFileInfo>();
		try{
			JSONObject  fileObj =  (JSONObject)  resObj.get("Files");
			JSONArray fileArray =  (JSONArray)  fileObj.get("results");
			for(int j = 0; j < fileArray.length(); j++){
				JSONObject jsonObject2 = (JSONObject) fileArray.get(j);
				if(jsonObject2.has("ServerRelativeUrl")){
					SiteFileInfo file = new SiteFileInfo();
					file.setSiteURL(siteURL);
					String serverRelativeURL = (String)jsonObject2.get("ServerRelativeUrl");
					if(serverRelativeURL.length()>255){
						System.out.println("---serverRelativeURL---->"+serverRelativeURL);
						serverRelativeURL = serverRelativeURL.substring(0, 254);
					}
					file.setFileRelativeURL(serverRelativeURL);
					String fileName = (String)jsonObject2.get("Name");
					System.out.println("---fileName---->"+fileName);
					if(fileName.length()>255)
						fileName = fileName.substring(0, 244);
					if(siteFileInfoMap.containsKey((String)jsonObject2.get("UniqueId")))
						file = siteFileInfoMap.get((String)jsonObject2.get("UniqueId"));
					file.setName(fileName);
					file.setFolderId(folderId);
					file.setUniqueId((String)jsonObject2.get("UniqueId"));
					file.setFileCreatedDate(CommonUtil.convertStringToDate((String)jsonObject2.get("TimeCreated")));
					file.setFileLastModifiedDate(CommonUtil.convertStringToDate((String)jsonObject2.get("TimeLastModified")));
					sitefileinfoService.saveUpdateEntity(file);
					/*if(file.getId() ==null)
						sitefileinfoService.saveEntity(file);
					else
						sitefileinfoService.updateEntity(file);*/
				}

			}
		}catch(Exception e){
			System.out.println("Exceptin is -->"+e.getMessage());
			//CommonUtil.generateLog(e.getMessage(), 0,  e.getLocalizedMessage(), session);
		}finally{
			
		}
		return files;
	}

}