package com.docsolr.Sharepoint;



import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.service.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.docsolr.entity.SiteFileInfo;
import com.docsolr.entity.SiteFolders;
import com.docsolr.entity.SiteInfo;
import com.docsolr.entity.SiteLibrary;
import com.docsolr.util.CommonUtil;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DataService {
	String hibernatePropsFilePath = "hibernate.cfg.xml";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new DataService().insertInfo();
		//new DataInsertion().getInfo();
		//new DataInsertion().updateInfo();
		//new DataService().querySiteInfoRecords();
		//new DataInsertion().insertMultipleRecords();
		//new DataInsertion().insertInfoSiteItemDetails();
		//new DataService().getSiteFolders();
		new DataService().insertSiteFileInfo();
		URL obj;
		try {
			obj = new URL("https://pgangparia.sharepoint.com/sites/SPSite/_api/web/Folders?$expand=Files,Folders/Files,Folders/Folders/Files$value");
//			 System.out.println("protocol = " + obj.getProtocol());
//		        System.out.println("authority = " + obj.getAuthority());
//		        System.out.println("host = " + obj.getHost());
//		        System.out.println("port = " + obj.getPort());
//		        System.out.println("path = " + obj.getPath());
//		        System.out.println("query = " + obj.getQuery());
//		        System.out.println("filename = " + obj.getFile());
//		        System.out.println("ref = " + obj.getRef());
//		        System.out.println("ref = " + obj.getUserInfo());
		        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2017-08-15T16:56:21Z");
		        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		        //sdf.parse("2017-08-15T16:56:21Z");
		        
		        //String path = "/sites/SPSite/_api/web/Folders";
		        
		        //System.out.println(path.split("/sites/")[1].split("/")[0]);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Map<String, List<SiteFolders>> getSiteFolders1(){
		Map<String, List<SiteFolders>> siteFolderMap = new HashMap<String, List<SiteFolders>> ();
		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		SessionFactory SF = config.buildSessionFactory();
		Session sess = SF.openSession();
		List<SiteInfo> dpList = new ArrayList<SiteInfo>();
		dpList = sess.createQuery("from SiteFolders ").list();
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteFolders dp = (SiteFolders) obj;
			System.out.println(dp.getId() + dp.getSiteName() );
			String siteName = dp.getSiteName();
			if(siteName.length() ==1 && siteName.contains("/"))siteName = "home"; 
			System.out.println("siteName-->"+(siteName));
			if(siteFolderMap.containsKey(siteName)){
				siteFolderMap.get(siteName).add(dp);
			}else{
				List<SiteFolders> folderList = new ArrayList<SiteFolders>();
				folderList.add(dp);
				siteFolderMap.put(siteName, folderList);
			}
		}
		sess.close();
		System.out.println(siteFolderMap.values().size());
		System.out.println(siteFolderMap.keySet().size());
		return siteFolderMap;
		
	}
	

	public List<SiteInfo> getSites(){
		List<SiteInfo> sites = new ArrayList<SiteInfo>();
		Session sess = CommonUtil.geSession();
		List<SiteInfo> dpList = new ArrayList<SiteInfo>();
		dpList = sess.createQuery("from SiteInfo ").list();
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteInfo dp = (SiteInfo) obj;
			//siteMap.put(dp.getId(), dp);
			sites.add(dp);
		}
		return sites;
	}
	
	
	public Map<Long,SiteInfo> getSiteMap(){
		Map<Long,SiteInfo> siteMap = new HashMap<Long,SiteInfo>();
		Session sess = CommonUtil.geSession();
		List<SiteInfo> dpList = new ArrayList<SiteInfo>();
		dpList = sess.createQuery("from SiteInfo ").list();
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
		Session sess = CommonUtil.geSession();
		List<SiteLibrary> dpList = new ArrayList<SiteLibrary>();
		dpList = sess.createQuery("from SiteLibrary ").list();
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
	
	
	public Map<Long,List<SiteFolders>> getSiteFoldersMap(){
		Map<Long,List<SiteFolders>> siteFolderMap = new HashMap<Long,List<SiteFolders>>();
		Session sess = CommonUtil.geSession();
		List<SiteFolders> dpList = new ArrayList<SiteFolders>();
		dpList = sess.createQuery("from SiteFolders ").list();
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteFolders dp = (SiteFolders) obj;
			if(siteFolderMap.containsKey(dp.getSiteLibraryId()))
				siteFolderMap.get(dp.getSiteLibraryId()).add(dp);
			else{
				List<SiteFolders> dpList1 = new ArrayList<SiteFolders>();
				dpList1.add(dp);
				siteFolderMap.put((long)dp.getSiteLibraryId(), dpList1);
			}
		}
		return siteFolderMap;
	}
	

	public Map<Long,List<SiteFileInfo>> getSiteFilesMap(){
		Map<Long,List<SiteFileInfo>> siteFileMap = new HashMap<Long,List<SiteFileInfo>>();
		Session sess = CommonUtil.geSession();
		List<SiteFileInfo> dpList = new ArrayList<SiteFileInfo>();
		dpList = sess.createQuery("from SiteFileInfo ").list();
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
	

	public void insertInfo(){
		Configuration config = new Configuration();
		config.configure(hibernatePropsFilePath);
		SessionFactory SF = config.buildSessionFactory();
		Session sess = SF.openSession();
		SiteInfo provider = new SiteInfo();
		provider.setSiteName("testSIte");
		provider.setSiteURL("test.com");
		//provider.setSiteId("1234111");
		Transaction tr = sess.beginTransaction();
		sess.save(provider);
		System.out.println("Object saved successfully");
		tr.commit();
		sess.close();
		SF.close();

	}
	
	public void insertSiteFileInfo(){
		Configuration config = new Configuration();
		config.configure(hibernatePropsFilePath);
		SessionFactory SF = config.buildSessionFactory();
		Session sess = SF.openSession();
		SiteFileInfo provider = new SiteFileInfo();
		provider.setName("testSIte");
		provider.setSiteURL("test.com");
		//provider.setSiteId("1234111");
		Transaction tr = sess.beginTransaction();
		sess.save(provider);
		System.out.println("Object saved successfully");
		tr.commit();
		sess.close();
		SF.close();
	}
	
	public void insertInfoSiteItemDetails(){
		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		SessionFactory SF = config.buildSessionFactory();
		Session sess = SF.openSession();
		SiteFolders provider = new SiteFolders();
		provider.setSiteName("testSIte");
		provider.setHostURL("test.com");
		provider.setUniqueId("1234");
		Transaction tr = sess.beginTransaction();
		sess.save(provider);
		System.out.println("Object saved successfully");
		tr.commit();
		sess.close();
		SF.close();

	}
	/*
	public void insertMultipleRecords(){
		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		SessionFactory SF = config.buildSessionFactory();
		Session sess = SF.openSession();
		List<SiteInfo> dpList = new ArrayList<>();
		Transaction tr = sess.beginTransaction();
		SiteInfo provider = new SiteInfo();
		provider.setSiteName("testSIte12342");
		provider.setSiteURL("test.com");
		provider.setSiteId("12342111");
		sess.save(provider);
		SiteInfo provider1 = new SiteInfo();
		provider1.setSiteName("testSIte12343");
		provider1.setSiteURL("test.com");
		provider1.setSiteId("12343");
		sess.save(provider1);
		
		tr.commit();
		sess.close();
		SF.close();

		
	}

	public void getInfo(){
		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		SessionFactory SF = config.buildSessionFactory();
		Session sess = SF.openSession();
		Object obj = sess.load(SiteInfo.class, new Integer(1234));
		SiteInfo dp = (SiteInfo) obj;
		System.out.println(dp.getSiteId() + dp.getSiteName() + dp.getSiteURL());

	}*/
	
	public List<SiteInfo> querySiteInfoRecords(){
		Configuration config = new Configuration();
		config.configure("hibernate.cfg.xml");
		SessionFactory SF = config.buildSessionFactory();
		Session sess = SF.openSession();
		List<SiteInfo> dpList = new ArrayList<SiteInfo>();
		dpList = sess.createQuery("from SiteInfo ").list();
		Iterator it = dpList.iterator();
		while(it.hasNext()){
			Object obj = (Object) it.next();
			SiteInfo dp = (SiteInfo) obj;
			System.out.println(dp.getId() + dp.getSiteName() + dp.getSiteURL());
			String siteName = dp.getSiteName();
			if(siteName.length() ==1 && siteName.contains("/"))siteName = "home"; 
			dp.setSiteName(siteName);
		}
		sess.close();
		return dpList;
		
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

