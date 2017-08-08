package com.docsolr.controller;



import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docsolr.entity.UserAuthority;
import com.docsolr.entity.UserAuthority.Roles;
import com.docsolr.entity.UserConnection;
import com.docsolr.entity.Users;
import com.docsolr.service.common.GenericService;
import com.docsolr.util.SecurityUtil;


@Controller
public class SalesforceController  {
	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String INSTANCE_URL = "INSTANCE_URL";

	// clientId is 'Consumer Key' in the Remote Access UI
	private static String clientId = "3MVG9d8..z.hDcPKJEqG974zEN__uVDhGsy1pvPoRh.91X4Kj3iyIiphkDtc7eTgPY4PkTwko_06e5MwmfJ4z";
	// clientSecret is 'Consumer Secret' in the Remote Access UI
	private static String clientSecret = "3749580945705226921";
	// This must be identical to 'Callback URL' in the Remote Access UI
	private static String redirectUri = "http://localhost:8080/docsolr/auth/salesforce/callback";
	private static String environment = "https://login.salesforce.com";
	private String authUrl = null;
	private String tokenUrl = null;
	
	@Autowired
	GenericService<UserAuthority> userAuthGenericService;
	
	@Autowired
	public GenericService<Users> userService;
	
	@Autowired
	public GenericService<UserConnection> userConnectionService;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public String home() {
		
		return "<a href=\"/auth/salesforce\">login</a>";
	}
	
	@RequestMapping(value = "/auth/salesforce" , method = RequestMethod.POST)
	public String authenticate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String accessToken = (String) request.getSession().getAttribute(ACCESS_TOKEN);
		
		if(accessToken == null) {
		
			try {
				authUrl = environment
						+ "/services/oauth2/authorize?response_type=code&client_id="
						+ clientId + "&redirect_uri="
						+ URLEncoder.encode(redirectUri, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new ServletException(e);
			}
	
			return "redirect:" + authUrl;
		} else {
			return "redirect:/auth/salesforce/callback";
		}
	}
	
	@RequestMapping(value = "/auth/salesforce/callback")
	@ResponseBody
	public void authenticateCallback(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String accessToken = (String) request.getSession().getAttribute(
				ACCESS_TOKEN);
		
		if(accessToken == null) {
			String instanceUrl = null;
			String Url = null;
			String token_type=null;
			
			
			tokenUrl = environment + "/services/oauth2/token";
			
			String code = request.getParameter("code");

			HttpClient httpclient = new HttpClient();

			PostMethod post = new PostMethod(tokenUrl);
			post.addParameter("code", code);
			post.addParameter("grant_type", "authorization_code");
			post.addParameter("client_id", clientId);
			post.addParameter("client_secret", clientSecret);
			post.addParameter("redirect_uri", redirectUri);

			try {
				httpclient.executeMethod(post);

				try {
					JSONObject authResponse = new JSONObject(
							new JSONTokener(new InputStreamReader(
									post.getResponseBodyAsStream())));
					System.out.println("Auth response: "
							+ authResponse.toString(2));

					accessToken = authResponse.getString("access_token");
					instanceUrl = authResponse.getString("instance_url");
					Url = authResponse.getString("id");
					token_type = authResponse.getString("token_type");

				     GetMethod Get=new GetMethod(Url);
				     Get.addRequestHeader("Authorization", token_type + " "+ accessToken);
				     httpclient.executeMethod(Get);
				     
				     JSONObject fullDetail = new JSONObject(
								new JSONTokener(new InputStreamReader(
										Get.getResponseBodyAsStream())));
						System.out.println("Auth response: "
								+ fullDetail.toString(2));
					
						addConnection(fullDetail,authResponse);
						
						
				} catch (JSONException e) {
					e.printStackTrace();
					throw new ServletException(e);
				}
			} finally {
				post.releaseConnection();
			}
			// Set a session attribute so that other servlets can get the access
			// token
			request.getSession().setAttribute(ACCESS_TOKEN, accessToken);
			
			// We also get the instance URL from the OAuth response, so set it
			// in the session too
			request.getSession().setAttribute(INSTANCE_URL, instanceUrl);
		}
		response.sendRedirect("/docsolr/user");
		//return new ModelAndView("redirect:/user");//new RedirectView("docsolr/user",false);
	}
	
	@RequestMapping(value = "/auth/salesforce/logout")
	@ResponseBody
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		session.removeAttribute(ACCESS_TOKEN);
		session.removeAttribute(INSTANCE_URL);
		
		return "redirect:/docsolrlogin";
	}
	
	@RequestMapping(value = "/accounts")
	@ResponseBody
	public String accounts(HttpServletRequest request) throws ServletException{
		HttpSession session = request.getSession();
		
		String accessToken = (String)session.getAttribute(ACCESS_TOKEN);
		String instanceUrl = (String)session.getAttribute(INSTANCE_URL);
		
		if (accessToken == null) {
			return "<a href=\"/logout\">Log out</a> | Error - no access token";
		}
		
		StringBuffer writer = new StringBuffer();

		writer.append("We have an access token: " + accessToken + "<br />"
				+ "Using instance " + instanceUrl + "<br /><br />");

		HttpClient httpclient = new HttpClient();
		GetMethod get = new GetMethod(instanceUrl
				+ "/services/data/v20.0/query");

		// set the token in the header
		get.setRequestHeader("Authorization", "OAuth " + accessToken);

		// set the SOQL as a query param
		NameValuePair[] params = new NameValuePair[1];

		params[0] = new NameValuePair("q",
				"SELECT Id, Name from Account LIMIT 100");
		get.setQueryString(params);

		try {
			httpclient.executeMethod(get);
			if (get.getStatusCode() == HttpStatus.SC_OK) {
				// Now lets use the standard java json classes to work with the
				// results
				try {
					JSONObject response = new JSONObject(
							new JSONTokener(new InputStreamReader(
									get.getResponseBodyAsStream())));
					System.out.println("Query response: "
							+ response.toString(2));

					writer.append(response.getString("totalSize")
							+ " record(s) returned<br /><br />");

					JSONArray results = response.getJSONArray("records");

					for (int i = 0; i < results.length(); i++) {
						writer.append(results.getJSONObject(i).getString("Id")
								+ ", "
								+ results.getJSONObject(i).getString("Name")
								+ "<br />");
					}
					writer.append("<br />");
				} catch (JSONException e) {
					e.printStackTrace();
					throw new ServletException(e);
				} catch (IOException e) {
					e.printStackTrace();
					throw new ServletException(e);
				}
			}
		} catch (HttpException e) {
			e.printStackTrace();
			throw new ServletException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServletException(e);
		} finally {
			get.releaseConnection();
		}
		
		return writer.toString();
	}
	
	
	public void addConnection( JSONObject fullDetail,JSONObject response) {
		
		String fname=fullDetail.getString("first_name");
		String lname=fullDetail.getString("last_name");
		String mail=fullDetail.getString("email");
	
		Users user=new Users();
		UserConnection userConnection=new UserConnection();
		
		if(mail!=null)
		{
			/*For user table*/
			
			Map restrictionMap  = new HashMap();
			restrictionMap.put("email", mail);
				
			List<Users> users = userService.findEntityByRestriction(Users.class, restrictionMap);
			if(users != null && users.size() == 1){
				user = users.get(0);
			}else{
				user = null;
			}
			if(user == null)
			{
			 user = new Users(fname, lname, mail,null, true,true);
			 user.setSocialSalesforce(true);
			 UserAuthority userAuthority = new UserAuthority(Roles.ROLE_USER);
				Map authRestrictionMap = new HashMap();
				authRestrictionMap.put("authority", userAuthority.getAuthority());
				List<UserAuthority> userAuthorityList = userAuthGenericService.findLimitedEntity(UserAuthority.class, 0, authRestrictionMap, null);
				if(!userAuthorityList.isEmpty()){
					Set<UserAuthority> setOfAuthority = new HashSet<UserAuthority>();
					setOfAuthority.add(userAuthorityList.get(0));
					user.setAuthorities(setOfAuthority);
					user.setEnabled(true);
				}
				userService.saveEntity(user);
			}
			
			
			/*For User Connection Table*/
			
			
			restrictionMap  = new HashMap();
			restrictionMap.put("userId", mail);
			
			List<UserConnection> connections= userConnectionService.findEntityByRestriction(UserConnection.class, restrictionMap);
			if(connections != null && connections.size() == 1){
				userConnection = connections.get(0);
			}else{
				userConnection = null;
			}
			
			if(userConnection == null)
			{
			userConnection = new UserConnection(mail,null, null, fullDetail.getString("display_name"),null,null,null,null,null,null,response.getString("access_token"),null);
			
			userConnectionService.saveEntity(userConnection);
			}
			
		}
		SecurityUtil.signInUser(user);
	}
	
}