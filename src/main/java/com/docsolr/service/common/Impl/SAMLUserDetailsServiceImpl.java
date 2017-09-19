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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.impl.AudienceBuilder;
import org.opensaml.saml2.core.impl.AudienceRestrictionBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
		// build input stream using certificate file
		String userID = credential.getNameID().getValue();
		
		LOG.info(userID + " is logged in");
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		authorities.add(authority);

		// In a real scenario, this implementation has to locate user in a arbitrary
		// dataStore based on information present in the SAMLCredential and
		// returns such a date in a form of application specific UserDetails object.
		//return new User(userID, "<abc123>", true, true, true, true, authorities);
		try {
			Assertion assertionResponse = credential.getAuthenticationAssertion();
			System.out.println(XMLHelper.nodeToString(SAMLUtil.marshallMessage(assertionResponse)));
			Audience audience = (new AudienceBuilder().buildObject());
	        audience.setAudienceURI("https://login.salesforce.com");
	        AudienceRestriction audienceRestriction = (new AudienceRestrictionBuilder().buildObject());
	        audienceRestriction.getAudiences().add(audience);
			assertionResponse.getConditions().getAudienceRestrictions().add(audienceRestriction);
			
			Issuer issuer = (new IssuerBuilder().buildObject());
	        issuer.setValue("3MVG9d8..z.hDcPI7LXhdvf8ZG_XRZiJ8G5BvJyQzlOGQVLo0DaxZZ4cDNvIyk5v2UEWpb798vnP5HHRrqBM9");        
			assertionResponse.setIssuer(issuer);
			
	        SubjectConfirmation subjectConfirmation = assertionResponse.getSubject().getSubjectConfirmations().get(0);
	        subjectConfirmation.getSubjectConfirmationData().setRecipient("https://login.salesforce.com/services/oauth2/token");
	        assertionResponse.getAttributeStatements().clear();
	        String newAssertion = XMLHelper.nodeToString(SAMLUtil.marshallMessage(assertionResponse));
	        System.out.println("After deleting " +newAssertion );
	        String encodedRequestMessage = new String(Base64.encodeBytes(newAssertion.getBytes(), Base64.DONT_BREAK_LINES));
	        String assertion_type = "grant_type= urn:ietf:params:oauth:grant-type:saml2-bearer";
	        String requestBody= assertion_type+"&assertion=" + encodedRequestMessage 
	        		+"&format=json";
	        System.out.println(requestBody);
//	        HttpClient client = new HttpClient();
//	        PostMethod postMethod = new PostMethod("https://login.salesforce.com/services/oauth2/token");
//	        String assertion_type = "grant_type= urn:ietf:params:oauth:grant-type:saml2-bearer";
//	        String requestBody= assertion_type+"&assertion=" + encodedRequestMessage;
//	        StringRequestEntity entity = new StringRequestEntity(requestBody, "application/x-www-form-urlencoded", null);
//	        System.out.println(requestBody);
//	        postMethod.setRequestEntity(entity);
//	        int a = client.executeMethod(postMethod);
//	        String reponse = postMethod.getResponseBodyAsString();
//	        System.out.println(reponse);
	        URL obj = new URL("https://login.salesforce.com/services/oauth2/token");
	        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
	        connection.setRequestMethod("POST");;
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        
	        connection.setDoOutput(true);
	  
	        OutputStream output = null;
	        try {
	             output = connection.getOutputStream();
	             output.write(requestBody.getBytes("UTF-8"));
	        } finally {
	             if (output != null) try { output.close(); } catch (IOException e) {
	                 e.printStackTrace();
	             }
	        }
	           
	        int responseCode = connection.getResponseCode();
	        System.out.println("HTTP response code: " + responseCode);
	        InputStream is;
	        if (connection.getResponseCode() >= 400) {
	            is = connection.getErrorStream();            
	        } else {
	            is = connection.getInputStream();
	        }
	        
	        StringBuilder result = new StringBuilder();

	        BufferedReader in = new BufferedReader(new InputStreamReader(is));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) { 
	            result.append(inputLine);
	        }
	        in.close();
	        
	        String resultString = result.toString();
	        System.out.println ("Response body: " + resultString);

		} catch(Exception e1){
			e1.printStackTrace();
		}
		return "/recieveZip";
	}
	
}