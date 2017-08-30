/*
 * Copyright 2017 DocSolr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.docsolr.service.common.Impl;

import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.security.saml.util.SAMLUtil;
import org.springframework.stereotype.Service;

@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {
	
	// Logger
	private static final Logger LOG = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

	
	public Object loadUserBySAML(SAMLCredential credential)
			throws UsernameNotFoundException {
		
		// The method is supposed to identify local account of user referenced by
		// data in the SAML assertion and return UserDetails object describing the user.
		
		String userID = credential.getNameID().getValue();
		
		Assertion mya = credential.getAuthenticationAssertion();
		//users.forEach(action);
		LOG.info(userID + " is logged in");
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		authorities.add(authority);

		// In a real scenario, this implementation has to locate user in a arbitrary
		// dataStore based on information present in the SAMLCredential and
		// returns such a date in a form of application specific UserDetails object.
		//return new User(userID, "<abc123>", true, true, true, true, authorities);
		try {
			String assertion = XMLHelper.nodeToString(SAMLUtil.marshallMessage(credential.getAuthenticationAssertion()));
			
			final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			 DefaultBootstrap.bootstrap();	
			 	XMLObject responseXmlObj = credential.getAuthenticationAssertion().getParent();
		        Response responseObj = (Response) responseXmlObj;
		 
		 
		        // Get the SAML2 Assertion part from the response
		        StringWriter rspWrt = new StringWriter();
		        XMLHelper.writeNode(responseObj.getAssertions().get(0).getDOM(), rspWrt);
		        String requestMessage = rspWrt.toString();
		 
		        // Get the Base64 encoded string of the message
		        // Then Get it prepared to send it over HTTP protocol
		        String encodedRequestMessage = Base64.encodeBytes(assertion.getBytes());
		        HttpClient client = new HttpClient();
		        PostMethod postMethod = new PostMethod("https://login.salesforce.com/services/oauth2/token");
		        String assertion_type = "urn:oasis:names:tc:SAML:2.0:profiles:SSO:browser";
		        String requestBody= "grant_type=assertion&assertion_type="+assertion_type+"&assertion=" + encodedRequestMessage;
		       // requestBody = requestBody.replaceAll("\\r|\\n", "");
		        StringRequestEntity entity = new StringRequestEntity(requestBody, "application/x-www-form-urlencoded", null);
		        System.out.println(requestBody);
		        postMethod.setRequestEntity(entity);
		        int a = client.executeMethod(postMethod);
		        String reponse = postMethod.getResponseBodyAsString();
		        System.out.println(reponse);

		} catch(Exception e1){
			e1.printStackTrace();
		}
		return "/recieveZip";
	}
	
}