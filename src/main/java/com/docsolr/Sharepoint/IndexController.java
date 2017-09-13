package com.docsolr.Sharepoint;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.docsolr.entity.SiteInfo;



@Controller   
public class IndexController {  
  
	 @RequestMapping( "/Sites")
    public String  helloWorld(ModelMap model) {  
  
        String message = "WELCOME SPRING MVC";  
        System.out.println("message --> "+message);
        //new DataService().insertInfo();
        model.addAttribute("message", "DocSolr Search Engine");
        /*model.addAttribute("siteList", new DataService().querySiteInfoRecords());
        model.addAttribute("siteFolderMap", new DataService().getSiteFolders1());*/
        model.addAttribute("siteList", new DataService().getSites());
        model.addAttribute("siteLibraryMap",new DataService().getSiteLibrariesMap());
        model.addAttribute("siteFolderMap", new DataService().getSiteFoldersMap());
        model.addAttribute("siteFileMap",new DataService().getSiteFilesMap());
        //List<SiteInfo> sites = new ArrayList<SiteInfo>();  
        //sites.add()
        
        return "Sites"; 
    } 
	 
	 @RequestMapping(value = "/", method = RequestMethod.GET)
	 public String printWelcome(ModelMap model) {

		 model.addAttribute("message", "Spring 3 MVC Hello World");
		 return "hello";

	 }
}
