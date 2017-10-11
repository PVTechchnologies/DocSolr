package com.docsolr.Sharepoint;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller   
public class IndexController {  
  
	
	@Autowired
	SharePointCallout callout;
	
	@Autowired
	DataService dataService;
	
	@RequestMapping(value = "/SiteUrl", method = RequestMethod.GET)
	@ResponseBody 
    public Map<String,Object>  helloWorld() {  
  
        String message = "WELCOME SPRING MVC";  
        System.out.println("message --> "+message);
      /*  model.addAttribute("message", "DocSolr Search Engine");*/
        callout.fecthSharePointData();
        //dataService.queryEntityByFields();
        //dataService.getSites();
        //l.login(); //need to be run this method from batch to get files from sharepoint
       Map<String,Object> sharepointdata = new HashMap<>();
       sharepointdata.put("siteList", dataService.getSites());
       sharepointdata.put("siteLibraryMap", dataService.getSiteLibrariesMap());
       sharepointdata.put("siteFolderMap", dataService.getSiteFoldersMap());
       sharepointdata.put("siteFileMap", dataService.getSiteFilesMap());
        /*model.addAttribute("siteList",sharepointdata.get("siteList") );
        model.addAttribute("siteLibraryMap",sharepointdata.get("siteLibraryMap"));
        model.addAttribute("siteFolderMap", sharepointdata.get("siteFolderMap"));
        model.addAttribute("siteFileMap",sharepointdata.get("siteFileMap"));*/
        return sharepointdata; 
    } 
	 
	/* @RequestMapping(value = "/", method = RequestMethod.GET)
	 public String printWelcome(ModelMap model) {

		 model.addAttribute("message", "Spring 3 MVC Hello World");
		 return "hello";

	 }*/
}