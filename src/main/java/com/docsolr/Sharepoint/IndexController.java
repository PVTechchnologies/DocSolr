package com.docsolr.Sharepoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller   
public class IndexController {  
  
	@Autowired
	LoginManager l;
	
	@Autowired
	DataService dataService;
	
	 @RequestMapping( "/Sites")
    public String  helloWorld(ModelMap model) {  
  
        String message = "WELCOME SPRING MVC";  
        System.out.println("message --> "+message);
        model.addAttribute("message", "DocSolr Search Engine");
        dataService.getSites();
        //l.login(); //need to be run this method from batch to get files from sharepoint
        model.addAttribute("siteList", dataService.getSites());
        model.addAttribute("siteLibraryMap",dataService.getSiteLibrariesMap());
        model.addAttribute("siteFolderMap", dataService.getSiteFoldersMap());
        model.addAttribute("siteFileMap",dataService.getSiteFilesMap());
   
        
        return "Sites"; 
    } 
	 
	 @RequestMapping(value = "/", method = RequestMethod.GET)
	 public String printWelcome(ModelMap model) {

		 model.addAttribute("message", "Spring 3 MVC Hello World");
		 return "hello";

	 }
}
