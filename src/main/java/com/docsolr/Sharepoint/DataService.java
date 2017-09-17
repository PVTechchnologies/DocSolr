package com.docsolr.Sharepoint;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docsolr.entity.SiteFileInfo;
import com.docsolr.entity.SiteFolder;
import com.docsolr.entity.SiteInfo;
import com.docsolr.entity.SiteLibrary;
import com.docsolr.service.common.GenericService;
import com.docsolr.util.CommonUtil;




@Service
public class DataService {
	String hibernatePropsFilePath = "hibernate.cfg.xml";

	@Autowired
	GenericService<SiteInfo> siteInfoService;

	@Autowired
	GenericService<SiteLibrary> siteLibraryService;

	@Autowired
	GenericService<SiteFolder> siteFolderService;

	@Autowired
	GenericService<SiteFileInfo> siteFileInfoService;

	

	public List<SiteInfo> getSites(){
		List<SiteInfo> sites = new ArrayList<SiteInfo>();
		//Session sess = CommonUtil.geSession();
		List<SiteInfo> dpList = new ArrayList<SiteInfo>();
		//dpList = sess.createQuery("from SiteInfo ").list();
		Map<String,String> dummmyMap = new HashMap<String,String>();
		dpList =  siteInfoService.getEnityListByQuery("SiteInfo", dummmyMap);
		//dpList =  siteInfoService.findAllEntity(getClass());
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteInfo dp = (SiteInfo) obj;
			//siteMap.put(dp.getId(), dp);
			sites.add(dp);
		}
		System.out.println(sites.size());
		return sites;
	}
	
	
	public Map<Long,SiteInfo> getSiteMap(){
		Map<Long,SiteInfo> siteMap = new HashMap<Long,SiteInfo>();
		Session sess = CommonUtil.geSession();
		List<SiteInfo> dpList = new ArrayList<SiteInfo>();
		//dpList = sess.createQuery("from SiteInfo ").list();
		dpList =  siteInfoService.getEnityListByQuery("SiteInfo", new HashMap<String,String>());
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteInfo dp = (SiteInfo) obj;
			siteMap.put(dp.getId(), dp);
		}
		return siteMap;
	}
	public Map<Long,List<SiteLibrary>> getSiteLibrariesMap(){
		Map<Long,List<SiteLibrary>> siteLibraryMap = new HashMap<Long,List<SiteLibrary>>();
		//Session sess = CommonUtil.geSession();
		List<SiteLibrary> dpList = new ArrayList<SiteLibrary>();
		//dpList = sess.createQuery("from SiteLibrary ").list();
		dpList = siteLibraryService.getEnityListByQuery("SiteLibrary", new HashMap<String,String>());
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteLibrary dp = (SiteLibrary) obj;
			if(siteLibraryMap.containsKey(dp.getSiteId()))
				siteLibraryMap.get(dp.getSiteId()).add(dp);
			else{ 
				List<SiteLibrary> dpList1 = new ArrayList<SiteLibrary>();
				dpList1.add(dp);
				siteLibraryMap.put(dp.getSiteId(), dpList1);
			}
		}
		return siteLibraryMap;
	}
	
	
	public Map<Long,List<SiteFolder>> getSiteFoldersMap(){
		Map<Long,List<SiteFolder>> siteFolderMap = new HashMap<Long,List<SiteFolder>>();
		//Session sess = CommonUtil.geSession();
		List<SiteFolder> dpList = new ArrayList<SiteFolder>();
		//dpList = sess.createQuery("from SiteFolder ").list();
		dpList = siteFolderService.getEnityListByQuery("SiteFolder", new HashMap<String,String>());
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteFolder dp = (SiteFolder) obj;
			if(siteFolderMap.containsKey(dp.getSiteLibraryId()))
				siteFolderMap.get(dp.getSiteLibraryId()).add(dp);
			else{
				List<SiteFolder> dpList1 = new ArrayList<SiteFolder>();
				dpList1.add(dp);
				siteFolderMap.put((long)dp.getSiteLibraryId(), dpList1);
			}
		}
		return siteFolderMap;
	}
	

	public Map<Long,List<SiteFileInfo>> getSiteFilesMap(){
		Map<Long,List<SiteFileInfo>> siteFileMap = new HashMap<Long,List<SiteFileInfo>>();
		//Session sess = CommonUtil.geSession();
		List<SiteFileInfo> dpList = new ArrayList<SiteFileInfo>();
		//dpList = sess.createQuery("from SiteFileInfo ").list();
		dpList = siteFileInfoService.getEnityListByQuery("SiteFileInfo", new HashMap<String,String>());
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteFileInfo dp = (SiteFileInfo) obj;
			if(siteFileMap.containsKey(dp.getFolderId()))
				siteFileMap.get(dp.getFolderId()).add(dp);
			else{
				List<SiteFileInfo> dpList1 = new ArrayList<SiteFileInfo>();
				dpList1.add(dp);
				siteFileMap.put((long)dp.getFolderId(), dpList1);
			}
		}
		return siteFileMap;
	}
	
	
	public DataWrapper getSiteInfoMap(){
		DataWrapper dataWrap = new DataWrapper();
		Map<String,SiteInfo> siteInfoMap = new HashMap<String,SiteInfo>();
		List<SiteInfo> dpList = new ArrayList<SiteInfo>();
		dpList = siteInfoService.getEnityListByQuery("SiteInfo", new HashMap<String,String>());
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteInfo dp = (SiteInfo) obj;
			dp.setActive(true);
			siteInfoService.updateEntity(dp);
			siteInfoMap.put(dp.getSiteName(), dp);
		}	
		dataWrap.siteInfoMap = siteInfoMap;
		getSiteLibraryMap(dataWrap);
		return dataWrap;
	}
	
	public DataWrapper getSiteLibraryMap(DataWrapper dataWrap){
		Map<String,SiteLibrary> siteLibraryMap = new HashMap<String,SiteLibrary>();
		List<SiteLibrary> dpList = new ArrayList<SiteLibrary>();
		dpList = siteLibraryService.getEnityListByQuery("SiteLibrary", new HashMap<String,String>());
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteLibrary dp = (SiteLibrary) obj;
			dp.setActive(true);
			siteLibraryService.updateEntity(dp);
			siteLibraryMap.put(dp.getGuid(), dp);
		}	
		dataWrap.siteLibraryMap = siteLibraryMap;
		getSiteFolderMap(dataWrap);
		return dataWrap;
	}
	
	public DataWrapper getSiteFolderMap(DataWrapper dataWrap){
		Map<String,SiteFolder> siteFolderMap = new HashMap<String,SiteFolder>();
		List<SiteFolder> dpList = new ArrayList<SiteFolder>();
		dpList = siteFolderService.getEnityListByQuery("SiteFolder", new HashMap<String,String>());
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteFolder dp = (SiteFolder) obj;
			//siteFolderService.updateEntity(dp);
			siteFolderMap.put(dp.getUniqueId(), dp);
		}	
		dataWrap.siteFolderMap = siteFolderMap;
		getSiteFileMap(dataWrap);
		return dataWrap;
	}
	
	public DataWrapper getSiteFileMap(DataWrapper dataWrap){
		Map<String,SiteFileInfo> siteFileInfoMap = new HashMap<String,SiteFileInfo>();
		List<SiteFileInfo> dpList = new ArrayList<SiteFileInfo>();
		dpList = siteFileInfoService.getEnityListByQuery("SiteFileInfo", new HashMap<String,String>());
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteFileInfo  dp = (SiteFileInfo) obj;
			siteFileInfoService.updateEntity(dp);
			siteFileInfoMap.put(dp.getUniqueId(), dp);
		}	
		dataWrap.siteFileInfoMap = siteFileInfoMap;
		return dataWrap;
	}
	
	class DataWrapper{
		Map<String,SiteInfo> siteInfoMap;
		Map<String,SiteLibrary> siteLibraryMap;
		Map<String, SiteFolder> siteFolderMap;
		Map<String, SiteFileInfo> siteFileInfoMap;
	}

	/*public void updateInfo(){
		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		SessionFactory sf = config.buildSessionFactory();
		Session session = sf.openSession();
		final Transaction transaction = session.beginTransaction();
		try {
			Object obj = session.load(SiteInfo.class, new Integer(1234));
			SiteInfo dp = (SiteInfo) obj;
			dp.setSiteName("pankaj@sharepoint.com");
			transaction.commit();
		} catch (Exception ex) {
			// Log the exception here
			transaction.rollback();
			throw ex;
		}
		finally {
			//transaction.commit();
			session.close();
			sf.close();
		}



	}*/

}


