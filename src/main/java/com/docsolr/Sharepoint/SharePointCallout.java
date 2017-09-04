package com.docsolr.Sharepoint;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.docsolr.entity.SiteFolders;
import com.docsolr.entity.SiteInfo;

import  org.apache.http.client.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @author gangparia
 *
 */
public class SharePointCallout {


	
	public static String getALlSharePointSites(String token, String cookie,String formDigestValue,Session session, String userName )  {
		String endPoint = "https://pgangparia.sharepoint.com/_api/search/query?querytext=%27contentclass:sts_site%27&amp;Key=SPWebUrl";
		try{
			URL obj = new URL(endPoint);
			HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
			connection.setRequestMethod( "GET" );
			connection.setRequestProperty("Accept", "application/json;odata=verbose");
			connection.setRequestProperty("Content-Type", "application/json;odata=verbose");
			connection.setRequestProperty("X-RequestDigest",formDigestValue);
			connection.setRequestProperty("Cookie", cookie);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			 List<String> sites = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				JSONObject lineRes = new JSONObject(line);
		        JSONObject tableRows = lineRes.getJSONObject("d").getJSONObject("query").getJSONObject("PrimaryQueryResult").getJSONObject("RelevantResults").getJSONObject("Table").getJSONObject("Rows");
		        JSONArray array1 = (JSONArray) tableRows.get("results");
		        for (int i = 0; i < array1.length(); i++) {
		         
		        	JSONObject jsonObject1 = (JSONObject) array1.get(i);
		        	JSONObject tableCells = jsonObject1.getJSONObject("Cells");
		            //System.out.println(i+"-------------------------------> "+jsonObject1);
		            JSONArray array2 = (JSONArray) tableCells.get("results");
		            for (int j = 0; j < array2.length(); j++) {
		            	JSONObject jsonObject2 = (JSONObject) array2.get(j);
		            	System.out.println(j+"--> "+jsonObject2);
		            	if(jsonObject2.getString("Key").equals("SPWebUrl")){
		            		System.out.println(j+"----------yooooooooooooo---------------> "+jsonObject2);
		            		String res = jsonObject2.has("Value") && !jsonObject2.isNull("Value") ? jsonObject2.getString("Value") : null;
		            		if(res!=null){
		            			SiteInfo provider = new SiteInfo();
		            			URL sitObj = new URL(res);
		            			String sitePath = sitObj.getPath();
		            			String siteName="/";
		            			if(!sitePath.isEmpty()){
		            				String[] resList = sitePath.split("/sites/");
		            				if(resList.length > 1){
		            					siteName = resList[1];
		            				}
		            			}
		            			provider.setSiteName(siteName);
		            			provider.setSiteURL(res);
		            			//provider.setSiteId(sitObj.getPath()+userName);
		            			System.out.println(provider);
		            			System.out.println(sitObj);
		            			session.save(provider);
		            			sites.add(res);
		            		}
		            	}
		            }
		            
		        }
			}
			System.out.println("--sites--> "+sites);
			for(int i=0; i<sites.size();i++){
				getAllFilesFoldersFromSite(sites.get(i),token,cookie,formDigestValue,session);
			}
			
		}catch(Exception e){
			System.out.println("Exceptin is -->"+e.getMessage());
		}

		return "";
	}

	public static void getAllFilesFoldersFromSite(String siteURL,String token, String cookie,String formDigestValue,Session session ){
		
		try{
			URL obj = new URL(siteURL+"/_api/web/Folders?$expand=Files,Folders/Files,Folders/Folders/Files$value");

			HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
			connection.setRequestMethod( "GET" );
			connection.setRequestProperty("Accept", "application/json;odata=verbose");
			connection.setRequestProperty("Content-Type", "application/json;odata=verbose");
			connection.setRequestProperty("X-RequestDigest",formDigestValue);
			connection.setRequestProperty("Cookie", cookie);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			List<String> sites = new ArrayList<String>();
			
			while ((line = reader.readLine()) != null) {
				JSONObject lineRes = new JSONObject(line);
				System.out.println("--lineRes--> "+lineRes);
				JSONArray array2 =  (JSONArray)  lineRes.getJSONObject("d").get("results");
				  for (int j = 0; j < array2.length(); j++) {
					  
					  JSONObject jsonObject2 = (JSONObject) array2.get(j);
					  System.out.println(jsonObject2.get("ItemCount"));
					  System.out.println(jsonObject2.get("ServerRelativeUrl"));
					  SiteFolders provider = new SiteFolders();
					  String sitePath = obj.getPath();
					  String siteName="/";
	          			if(!sitePath.isEmpty()){
	          				String[] resList = sitePath.split("/sites/");
	          				if(resList.length > 1){
	          					siteName = resList[1].split("/")[0];
	          				}
	          			}
					  provider.setSiteName(siteName);
					  provider.setHostURL(obj.getHost());
					  provider.setUniqueId((String)jsonObject2.get("UniqueId"));
					  provider.setServerRelativeURL((String)jsonObject2.get("ServerRelativeUrl"));
					  provider.setItemCount(((int)jsonObject2.get("ItemCount")));
					  Date timeCreated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse((String)jsonObject2.get("TimeCreated"));
					  Date timeModified =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse((String)jsonObject2.get("TimeLastModified"));
					  provider.setTimeCreated(timeCreated);
					  provider.setTimeLastModified(timeModified);
					  session.save(provider);
				  }
			}
		

			//getAllFilesInfo("","","","");
		}catch(Exception e){
			System.out.println("Exceptin is -->"+e.getMessage());
		}

	}
	
	
public static void getAllFilesInfo(String siteURL,String token, String cookie,String formDigestValue){
		
		try{
			URL obj = new URL("https://pgangparia.sharepoint.com/sites/SPSite/_layouts/15/WopiFrame.aspx?sourcedoc=%7B901BD8C2-006A-410F-A3C4-B87F0A8681A1%7D&file=Spsite%20word%20doc%201.docx&action=default");
			HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
			connection.setRequestMethod( "GET" );
			connection.setRequestProperty("Accept", "application/json;odata=verbose");
			connection.setRequestProperty("Content-Type", "application/json;odata=verbose");
			connection.setRequestProperty("X-RequestDigest",formDigestValue);
			connection.setRequestProperty("Cookie", cookie);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			List<String> sites = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				JSONObject lineRes = new JSONObject(line);
				System.out.println("--lineRes--> "+lineRes);
				JSONArray array2 =  (JSONArray)  lineRes.getJSONObject("d").get("results");
				  for (int j = 0; j < array2.length(); j++) {
					  JSONObject jsonObject2 = (JSONObject) array2.get(j);
					  System.out.println(jsonObject2.get("ItemCount"));
					  System.out.println(jsonObject2.get("ServerRelativeUrl"));
					  
				  }
			}

			
		}catch(Exception e){
			System.out.println("Exceptin is -->"+e.getMessage());
		}

	}
	
	
	
	
	
	
	// HTTP GET request
	public static String sendGet() throws Exception {

		/*HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet("http://mkyong.com");
			HttpResponse response = client.execute(request);

			//get all headers
			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				System.out.println("Key : " + header.getName()
				      + " ,Value : " + header.getValue());
			}

			//get header by 'key'
			String server = response.getFirstHeader("Server").getValue();


			String url = "https://pgangparia.sharepoint.com/_vti_bin/client.svc";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			//con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Authorization", "Bearer");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer resp = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				resp.append(inputLine);
			}
			in.close();

			//print result
			System.out.println(response.toString());*/
		try {

			URL obj = new URL("https://pgangparia.sharepoint.com/_vti_bin/client.svc");
			//URL obj = new URL("https://ihg.sharepoint.com/_vti_bin/client.svc");
			URLConnection connection = obj.openConnection();
			connection.setRequestProperty("Authorization", "Bearer");
			Map<String, List<String>> map = connection.getHeaderFields();

			System.out.println("Printing All Response Header for URL: " + obj.toString() + "\n");
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}

			System.out.println("\nGet Response Header By Key ...\n");
			List<String> contentLength = map.get("WWW-Authenticate");
			if (contentLength == null) {
				System.out.println("'Content-Length' doesn't present in Header!");
			}else {
				for (String header : contentLength) {
					System.out.println("Content-Lenght: " + header);
					String realmStr = header.split(",")[0].split(" ")[1].split("=")[1];
					realmStr = realmStr.replace("\"", "").trim();
					System.out.println("realmStr-->" + realmStr);
					return realmStr;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return "";
	}


	public static String getAccessToken(String realm) throws Exception {
		String acsAuth2Url ="https://accounts.accesscontrol.windows.net/"+realm+"/tokens/OAuth/2";
		URL obj = new URL(acsAuth2Url);
		HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
		connection.setRequestProperty("Authorization", "Bearer");
		String grant_type = "client_credentials";
		String client_id = "8c5df13d-0e10-42a2-b0f0-7356f6f43496@"+realm;
		String client_secret = "S0t+VJaf3neNiacB89xsAeHW1XO8Ok6TvCo5zNiw9K4=";
		String resource = "00000003-0000-0ff1-ce00-000000000000/pgangparia.sharepoint.com@"+realm;
		/*String client_id = "505e7c48-b53d-4776-af46-90aeb0da80bf@"+realm;
		String client_secret = "jGIRXsUOZQ+F51zog4AMfaaznMab6O4LVqwKLL06oeo=";
		String resource = "00000003-0000-0ff1-ce00-000000000000/ihg.sharepoint.com@"+realm;*/



		grant_type = URLEncoder.encode(grant_type, "UTF-8");
		client_secret = URLEncoder.encode(client_secret, "UTF-8");
		client_id = URLEncoder.encode(client_id, "UTF-8");
		resource = URLEncoder.encode(resource, "UTF-8");
		//connection.setMethod('POST');
		//connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
		//connection.setRequestProperty("Content-Length",""+body.length());      
		//connection.setRequestMethod("POST");

		//String urlParameters  = "param1=a&param2=b&param3=c";
		String urlParameters = "grant_type="+grant_type+"&client_id="+client_id+"&resource="+resource+"&client_secret="+client_secret+"&scope="+resource;
		byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		String request        = acsAuth2Url;
		URL    url            = new URL( request );
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		conn.setRequestProperty( "charset", "utf-8");
		conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		conn.setUseCaches( false );
		try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
			wr.write( postData );
		}
		/*OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		writer.write(postData);
		writer.flush();*/

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String access_token = "" ;
		while ((line = reader.readLine()) != null) {
			JSONObject lineRes = new JSONObject(line);
			System.out.println(line);
			System.out.println("--lineRes-->"+lineRes);

			access_token = lineRes.getString("access_token");
			System.out.println(access_token);
		}
		//writer.close();
		reader.close();         

		return access_token;
	}

	public static String getSharePointFilesANDFolders(String accessToken) throws Exception {
		String endPoint = "https://pgangparia.sharepoint.com/_api/web/Folders?$expand=Folders,Files";
		URL obj = new URL(endPoint);
		HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
		connection.setRequestMethod( "GET" );
		connection.setRequestProperty("Accept", "application/json;odata=verbose");
		connection.setRequestProperty("Content-Type", "application/json;odata=verbose");
		connection.setRequestProperty("Authorization","Bearer "+ accessToken);
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while ((line = reader.readLine()) != null) {
			JSONObject lineRes = new JSONObject(line);
			System.out.println(line);
			System.out.println("--lineRes-->"+lineRes);

			//access_token = lineRes.getString("access_token");
			//System.out.println(access_token);
		}

		return "";
	}

	
	
	
	
	public static void basichAuthenicationSharepoint() throws Exception, IOException{
		/*CredentialsProvider credsProvider = new BasicCredentialsProvider();
		    credsProvider.setCredentials(
		            new AuthScope(AuthScope.ANY),
		            new NTCredentials("username", "password", "https://hostname", "domain"));
		    CloseableHttpClient httpclient = HttpClients.custom()
		            .setDefaultCredentialsProvider(credsProvider)
		            .build();
		    try {
		        HttpGet httpget = new HttpGet("http://hostname/_api/web/lists");

		        System.out.println("Executing request " + httpget.getRequestLine());
		        CloseableHttpResponse response = httpclient.execute(httpget);
		        try {
		            System.out.println("----------------------------------------");
		            System.out.println(response.getStatusLine());
		            EntityUtils.consume(response.getEntity());
		        } finally {
		            response.close();
		        }
		    } finally {
		        httpclient.close();
		    }	*/
		
		//URL fileToDownload = new URL("https://pgangparia.sharepoint.com/_api/web/lists");
        //URI FileDL = new URI(fileToDownload.toString());


        //File downloadedFile = new File(this.localDownloadPath + filePath);
        //if (downloadedFile.canWrite() == false) downloadedFile.setWritable(true);
        
		/*BufferedReader br = null;
		String retval = new String();
		final HttpClient client = new HttpClient();
		client.getParams().setAuthenticationPreemptive(true);
		 DefaultHttpClient httpclient = new DefaultHttpClient();
		//client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("sharepoint@pgangparia.onmicrosoft.com","123@topcoder"));
		httpclient.getCredentialsProvider().setCredentials(
                 new AuthScope("localhost", 443),
                 new UsernamePasswordCredentials("sharepoint@pgangparia.onmicrosoft.com", "123@topcoder"));
		HttpGet httpget = new HttpGet("https://pgangparia.sharepoint.com/_api/web/lists");
		 HttpResponse response = httpclient.execute(httpget);
         HttpEntity entity = response.getEntity();

         System.out.println("----------------------------------------");
         System.out.println(response.getStatusLine());
         if (entity != null) {
             System.out.println("Response content length: " + entity.getContentLength());
         }*/
		System.out.println("Executing request 0" );
         
         CredentialsProvider credsProvider = new BasicCredentialsProvider();
         credsProvider.setCredentials(
                 new AuthScope(AuthScope.ANY),
                 new NTCredentials("sharepoint@pgangparia.onmicrosoft.com", "123@topcoder", "", "https://pgangparia.sharepoint.com"));
         CloseableHttpClient httpclient = HttpClients.custom()
                 .setDefaultCredentialsProvider(credsProvider)
                 .build();
         try {
        	 System.out.println("Executing request " );
             HttpGet httpget = new HttpGet("https://pgangparia.sharepoint.com/_api/web/lists");

             System.out.println("Executing request " + httpget.getRequestLine());
             CloseableHttpResponse response = httpclient.execute(httpget);
             try {
                 System.out.println("----------------------------------------");
                 System.out.println(response.getStatusLine());
                 EntityUtils.consume(response.getEntity());
             } catch(Exception e){
            	 
            	 System.out.println("----------------------------------------"+e.getMessage());
             }finally {
            	 System.out.println("----------------------------------------finalyy");
                 response.close();
             }
         } finally {
             httpclient.close();
         }
         //Get the Digestvalue.
         CredentialsProvider provider = new BasicCredentialsProvider();
         provider.setCredentials(AuthScope.ANY, new  NTCredentials("sharepoint@pgangparia.onmicrosoft.com", "123@topcoder", "", "https://pgangparia.sharepoint.com"));
         HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
         //provider.Credentials = System.Net.CredentialCache.DefaultNetworkCredentials;

         HttpPost httpPost = new HttpPost( "https://pgangparia.sharepoint.com/_api/contextinfo");
         httpPost.addHeader("Accept", "application/json;odata=verbose");
         httpPost.addHeader("content-type", "application/json;odata=verbose");
         httpPost.addHeader("X-ClientService-ClientTag", "SDK-JAVA");
         HttpResponse response = client.execute(httpPost);

         byte[] content = EntityUtils.toByteArray(response.getEntity());
         String jsonString = new String(content, "UTF-8"); 
         System.out.println("--response-->"+response);
         JSONObject json = new JSONObject(jsonString);
         String FormDigestValue = json.getJSONObject("d").getJSONObject("GetContextWebInformation").getString("FormDigestValue");
         System.out.println(FormDigestValue);
         
		/*
		final GetMethod myMethod = new GetMethod("https://login.microsoftonline.com");

		int returnCode = client.executeMethod(myMethod);

		if (returnCode != HttpStatus.SC_OK) {
			System.err.println("Method passed: " + myMethod.getStatusLine());
		} 
		else {

			System.out.println("buffer : ");
			br = new BufferedReader(new InputStreamReader(myMethod.getResponseBodyAsStream()));
			String readLine;
			while (((readLine = br.readLine()) != null)) {
				System.err.println(readLine);
				retval = retval.concat(readLine);
			}

		}
		try {
			HttpGet httpget = new HttpGet("https://pgangparia.sharepoint.com/_api/web/lists");
			final GetMethod myMethod1 = new GetMethod("https://pgangparia.sharepoint.com/_api/web/lists");
			client.executeMethod(myMethod1);
			System.out.println("buffer : ");
			br = new BufferedReader(new InputStreamReader(myMethod1.getResponseBodyAsStream()));
			String readLine;
			while (((readLine = br.readLine()) != null)) {
				System.err.println(readLine);
				retval = retval.concat(readLine);
			}
			//System.out.println("Executing request " + httpget.getRequestLine());
			//CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				System.out.println("----------------------------------------");
				//System.out.println(response.getStatusLine());
				//  EntityUtils.consume(response.getEntity());
			} finally {
				///response.close();
			}
		} finally {
			//httpclient.close();
		}	*/

	}

	public static void getAllSites(String accessToken) throws Exception{
		//String relativeUri = "/sites/dpmqa/Records/30888";
		String endpoint = "https://pgangparia.sharepoint.com/_api/search/query?querytext=\'contentclass:sts_site\'";

		URL obj = new URL(endpoint);
		HttpURLConnection connection = (HttpURLConnection)obj.openConnection();

		connection.setRequestMethod( "GET" );
		connection.setRequestProperty("Accept", "application/json;odata=verbose");
		connection.setRequestProperty("Content-Type", "application/json;odata=verbose");
		connection.setRequestProperty("Authorization","Bearer "+ accessToken);
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while ((line = reader.readLine()) != null) {
			JSONObject lineRes = new JSONObject(line);
			System.out.println(line);
			System.out.println("--lineRes-->"+lineRes);

			//access_token = lineRes.getString("access_token");
			//System.out.println(access_token);
		}
	}

	public static String getBinaryToken(String tokenRequestXml, String username, String password, String host, String endpointurl) throws Exception{
		String  token = "";
		tokenRequestXml = "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"> <s:Header> <a:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue</a:Action> <a:ReplyTo> <a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address> </a:ReplyTo> <a:To s:mustUnderstand=\"1\">https://login.microsoftonline.com/extSTS.srf</a:To> <o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"> <o:UsernameToken> <o:Username>sharepoint@pgangparia.onmicrosoft.com</o:Username> <o:Password>123@topcoder</o:Password> </o:UsernameToken> </o:Security> </s:Header> <s:Body> <t:RequestSecurityToken xmlns:t=\"http://schemas.xmlsoap.org/ws/2005/02/trust\"> <wsp:AppliesTo xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\"> <a:EndpointReference> <a:Address>https://pgangparia.sharepoint.com/Spdemo/_api/web/lists</a:Address> </a:EndpointReference> </wsp:AppliesTo> <t:KeyType>http://schemas.xmlsoap.org/ws/2005/05/identity/NoProofKey</t:KeyType> <t:RequestType>http://schemas.xmlsoap.org/ws/2005/02/trust/Issue</t:RequestType> <t:TokenType>urn:oasis:names:tc:SAML:1.0:assertion</t:TokenType> </t:RequestSecurityToken> </s:Body> </s:Envelope>";
		endpointurl = "https://login.microsoftonline.com/extSTS.srf";
		URL obj = new URL(endpointurl);
		HttpURLConnection conn = (HttpURLConnection)obj.openConnection();
		conn.setConnectTimeout(1000000);
		conn.setDoOutput( true );
		// set read timeout.
		conn.setReadTimeout(1000000);
		conn.setRequestProperty("Content-Length", ""+tokenRequestXml.length());
		conn.setRequestMethod("POST");
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(tokenRequestXml);
		wr.flush();

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		while ((line = reader.readLine()) != null) {
			System.out.println("--lineRes-->"+line);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			InputSource is;
			try {
				builder = factory.newDocumentBuilder();
				is = new InputSource(new StringReader(line));
				Document doc =  builder.parse(is);
				NodeList list = doc.getElementsByTagName("wsse:BinarySecurityToken");
				//token = list.item(0).getTextContent();
				System.out.println("list-->"+ token);

			} catch (ParserConfigurationException e) {
			} catch (SAXException e) {
			} catch (IOException e) {
			}
		}

		return token;
	} 
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
	
	public static void sharePointCall() throws Exception {
		String urlStr = "https://pgangparia.sharepoint.com/_api/web/lists";
			String domain = ""; // May also be referred as realm
			String userName = "sharepoint@pgangparia.onmicrosoft.com";
			String password = "123@topcoder";		

			String responseText = getAuthenticatedResponse(urlStr, domain, userName, password);

		    System.out.println("response: " + responseText);
	}
	
	private static String getAuthenticatedResponse(final String urlStr, final String domain, final String userName, final String password) throws IOException {

	    StringBuilder response = new StringBuilder();

		Authenticator.setDefault(new Authenticator() {
	        @Override
	        public PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(domain + "\\" + userName, password.toCharArray());
	        }
	    });

	    URL urlRequest = new URL(urlStr);
	    HttpURLConnection conn = (HttpURLConnection) urlRequest.openConnection();
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    conn.setRequestMethod("GET");

	    InputStream stream = conn.getInputStream();
	    BufferedReader in = new BufferedReader(new InputStreamReader(stream));
	    String str = "";
	    while ((str = in.readLine()) != null) {
	        response.append(str);
	    }
	    in.close();		

	    return response.toString();
	}
	
	public static String getCookie(String host, String token) throws Exception
	{
		String cookie = "";
		/*try {
			URL obj = new URL("https://pgangparia.sharepoint.com/_forms/default.aspx?wa=wsignin1.0");
			HttpURLConnection conn = (HttpURLConnection)obj.openConnection();
			//conn.setRequestProperty("Content-Length", ""+tokenRequestXml.length());
			///conn.setRequestMethod( "POST" );
				//conn.setConnectTimeout(1000000);
				//conn.setDoOutput( true );
				// set read timeout.
				//conn.setReadTimeout(1000000);
				//conn.setRequestProperty("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
				//conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
				//conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				//conn.setRequestProperty("Content-Length",""+token.length());
			conn.setUseCaches(false);
			conn.setDoOutput( true );
			conn.setDoInput(true);
			conn.setRequestProperty("Host", "https://pgangparia.sharepoint.com");
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,;q=0.8");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			conn.setRequestProperty("Referer", "https://pgangparia.sharepoint.com/_forms/default.aspx?wa=wsignin1.0");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			 conn.setRequestProperty("Content-Length", Integer.toString(token.length()));
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(token);
			wr.flush();
			int responseCode = conn.getResponseCode();
			System.out.println("cookie list-->"+ responseCode);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((line = reader.readLine()) != null) {
				System.out.println("cookie list-->"+ line);
			}
		} catch (IOException e) {
			System.out.println("************* IOException *************\n "+ e);
			throw e;
		} catch (Exception e) {
			System.out.println("************* MalformedByteSequenceException *************\n "+e);
			throw e;
		} finally {
			//if( is != null)	is.close();
		}*/
		URL url;
	    HttpURLConnection conn = null;        
	    try{
	        //Create connection

	        url = new URL("https://pgangparia.sharepoint.com/_forms/default.aspx?wa=wsignin1.0");
	        conn = (HttpURLConnection)url.openConnection();

	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", 
	                   "application/x-www-form-urlencoded");
	        //connection.setRequestProperty("Content-Language", "en-US"); 
	        //connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
	        conn.setRequestProperty("Content-Length", Integer.toString(token.length()));

	        conn.setUseCaches(false);
	        conn.setDoInput(true);
	        conn.setDoOutput(true);

	        System.out.println("hwllo");
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(token);
			wr.flush();
			System.out.println("hwllo");
			String line;
			String cookiesHeader = conn.getHeaderField("Set-Cookie");
	        System.out.println(cookiesHeader);

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			System.out.println("hwllo 3");
			while ((line = reader.readLine()) != null) {
				System.out.println("hwllo"+line);
			}
	        //get response
	        /*InputStream is = connection.getInputStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        String line;
	        StringBuffer response = new StringBuffer();

	        String cookiesHeader = connection.getHeaderField("Set-Cookie");
	        System.out.println(cookiesHeader);

	        while((line = br.readLine()) != null){
	            response.append(line);
	            response.append('\n');

	        }
	        System.out.println(response.toString());*/
	        //br.close();
	 
	        return "";
	    }catch(Exception e){
	        System.out.println("Unable to full create connection");
	        e.printStackTrace();
	        return null;
	    }finally {

	          if(conn != null) {
	        	  //conn.disconnect(); 
	          }
	    }


		/*HttpRequest requestCookie = new HttpRequest();
	        requestCookie.setEndpoint(host+"/_forms/default.aspx?wa=wsignin1.0");
	        requestCookie.setHeader("Content-Type", "application/x-www-form-urlencoded");
	        //requestCookie.setHeader('User-Agent','Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)');
	        requestCookie.setMethod("POST");
	        requestCookie.setBody(token);
	        requestCookie.setHeader('Content-Length',String.valueof(token.length()));
	        System.debug('.........2nst call for requestCookie........'+requestCookie);

	        HttpResponse responseCookie = new HttpResponse();
	        Http httpCookie = new Http();
	        responseCookie = httpCookie.send(requestCookie);
	        System.debug('.........2nst call for responseCookie...code.....'+responseCookie.getStatusCode()+'.......Status........'+responseCookie.getStatus());
	        System.debug('.........2nst call for responseCookie.....'+responseCookie.getHeaderKeys());
	        string location = responseCookie.getHeader('Location');
	        if(responseCookie.getStatus() == 'MovedPermanently')
	        { 
	            HttpRequest reqMovedPermanently = new HttpRequest();
	            reqMovedPermanently.setHeader('Content-Type', 'application/x-www-form-urlencoded');
	            reqMovedPermanently.setMethod('POST');
	            reqMovedPermanently.setEndpoint(host+'/_forms/default.aspx?wa=wsignin1.0');
	            reqMovedPermanently.setBody(token);
	            reqMovedPermanently.setHeader('Content-Length',String.valueof(token.length()));
	            reqMovedPermanently.setHeader('Location', location);
	            reqMovedPermanently.setHeader('User-Agent','Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)');
	            HttpResponse responseMovedPermanently = new HttpResponse();
	            Http httpMovedPermanently = new Http();
	            responseMovedPermanently = httpMovedPermanently.send(reqMovedPermanently);
	            System.debug('.........22nd call for responseMovedPermanently...code.....'+responseMovedPermanently.getStatusCode()+'.......Status........'+responseMovedPermanently.getStatus());
	            System.debug('.........22nd call for responseMovedPermanently.....'+responseMovedPermanently.getBody());
	            cookie = responseMovedPermanently.getHeader('Set-Cookie');
	            //System.debug('.........22nd call for requestCookie..if....'+cookie);
	        }
	        else
	        { 
	            cookie = responseCookie.getHeader('Set-Cookie');
	            //System.debug('.........22nd call for requestCookie..else....'+cookie);
	        }*/


		//return "";
	}
	
	private static String submitToken(String token) throws IOException {
		//String token = "t=EwBwAk6hBwAUIQT64YiMbkZQLHdw6peopUrQ0O8AAYkt43mh328r0OTpTqSVMQEWGlzlpE906mSyOfU2JgkHQCBz0VBLPKyFEYeCUUqLQ0FmodljevOEceo5L1r+aj207XYvgGl+QBOMxSuNtdbPprICB/+NhRxEynCQe2l1U84a3S20At+OsGorLHKpp1RIfjR6FGGW3ahltWwDvvkcLY5mMtvOHoQx+citNFIvXGY4zzosNgum0OXMlIz26QfODI705ICMV9wmLfbJ4xQjeRAHFrPQxdeQ3mA9tepV9zPKyeAsAmFrMb0/3GUh9GK0jk9O1+N5PZYtL4cKsOrMbGN3Z++IhoTrwLR6/8PJrZNtyKJhv/W35N66THKsKH0DZgAACDKSCSEEFKnaQAEQ+c2vlhFUJ1WBjs9puwnuOFye+J6AvcpFrCaefpBozSYZTQAwJDuHu51xUyrUhrPetgTekrM04m7q6IpqccJBFxTzd3UAkJLgFJQpcerLOFKgYMrVNWOyqEPzn9Zdjv3Xa73HGa36kOUqZeDPcBcxOtMy0I5LmV8tQ4a3Cc302hDax208/eL1fi5xqEiUE89DLEJ8w9KyIWfVUFwvs3r374t/7KJmQH55yZk3p874gNFyToHA4s+0ZuMikRyDTXeYPQ/Jz8rgIYGA+dCwDNb6x+2y26TRX9QiWYvuhcJ8V1xola+Wo6tjHJwon+8QHXLjCiOXkLUvZbjnR2X+UoAnAYNYb5YVeTBqQSO2l19VhK4o5tnHvOhnwVBM8DeGFJSeMChqS7SlPzq/39ntZtPmv9HuvFrP8801pW9KmxgXdoEB&p=";
		// http://cafeconleche.org/books/xmljava/chapters/ch03s05.html
		String url = "http://pgangparia.sharepoint.com" + "/_forms/default.aspx?wa=wsignin1.0";
		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		HttpURLConnection connection = (HttpURLConnection) uc;

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Accept", "application/x-www-form-urlencoded");
		connection.addRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
		// http://stackoverflow.com/questions/12294274/mobile-app-for-sharepoint/12295224#12295224
		// connection.addRequestProperty("SOAPAction", sts);
		connection.addRequestProperty("Content-Type",
				"text/xml; charset=utf-8");
		// connection.addRequestProperty("Expect", "100-continue");
		// connection.addRequestProperty("Connection", "Keep-Alive");
		// connection.addRequestProperty("Content-Length", saml.length() +
		// "");

		OutputStream out = connection.getOutputStream();
		Writer wout = new OutputStreamWriter(out);

		wout.write(token);

		wout.flush();
		wout.close();

		InputStream in = connection.getInputStream();
		//http://www.exampledepot.com/egs/java.net/GetHeaders.html
		
	    for (int i=0; ; i++) {
	        String headerName = connection.getHeaderFieldKey(i);
	        String headerValue = connection.getHeaderField(i);
	        System.out.println("header: " + headerName + " : " + headerValue);
	        if (headerName == null && headerValue == null) {
	            // No more headers
	            break;
	        }
	        if (headerName == null) {
	            // The header value contains the server's HTTP version
	        }
	    }
		String headerName = connection.getHeaderField("set-cookie");
		System.out.println("headerName");
		System.out.println(headerName);
		int c;
		StringBuilder sb = new StringBuilder("");
		while ((c = in.read()) != -1)
			sb.append((char) (c));
		in.close();
		String result = sb.toString();
		System.out.println(result);

		return headerName;
	}

	
	public static String getFilesForFolder(String acessToken) throws Exception {
		String relativeUri = "/sites/dpmqa/Records/30888";
		String endpoint = "https://ihg.sharepoint.com/sites/dpmqa/_api/web/getfolderbyserverrelativeurl(\'"+relativeUri+"\')/files?$expand=ListItemAllFields/FieldValuesAsText&$select=Title,Name,ServerRelativeUrl,UniqueId,dpmDocType,Current_x0020_PIP";

		URL obj = new URL(endpoint);
		HttpURLConnection connection = (HttpURLConnection)obj.openConnection();

		//String endPoint = 'https://ihg.sharepoint.com/sites/dpmqa/_api/web/getfolderbyserverrelativeurl(\'/sites/dpmqa/Records/'+projectId+'\')/files?$select=Title,Name,ServerRelativeUrl,UniqueId';

		connection.setRequestMethod( "GET" );
		connection.setRequestProperty("Accept", "application/json;odata=verbose");
		connection.setRequestProperty("Content-Type", "application/json;odata=verbose");
		connection.setRequestProperty("Authorization","Bearer "+ acessToken);
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while ((line = reader.readLine()) != null) {
			JSONObject lineRes = new JSONObject(line);
			System.out.println(line);
			System.out.println("--lineRes-->"+lineRes);

			//access_token = lineRes.getString("access_token");
			//System.out.println(access_token);
		}

		return "";
	}

	public static void fetchRealm(String domainName){
		/*HttpRequest httpRequestObject = new HttpRequest();
		  httpRequestObject.setEndPoint("https://"+domainName+"/_vti_bin/client.svc");
		  httpRequestObject.setmethod("GET");
		  httpRequestObject.setHeader("Authorization", "Bearer");
		  Http http = new Http();
		  HttpResponse httpResponse ;
		  if(!test.isRunningTest())
		  httpResponse = http.send(httpRequestObject);
		  String header = httpResponse.getHeader("WWW-Authenticate");*/
		//String realm = header.split(',')[0].split(' ')[1].split('=')[1];
		//realm = realm.removeStart('"').removeEnd('"');
		System.out.println("status");
		try{
			URL url = new URL("https://pgangparia.sharepoint.com/_vti_bin/client.svc");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			con.setRequestProperty("Content-Type", "application/json");
			int status = con.getResponseCode();
			System.out.println(status);

		}catch(Exception e){
			System.out.println(e.getMessage());
		}

		//return "";
	}



	public static void getRealm(){
		System.out.println("hello");
		/*CredentialsProvider credsProvider = (CredentialsProvider) new BasfhttpClienticCredentialsProvider();
	    credsProvider.setCredentials(
	            new AuthScope(AuthScope.ANY),
	            new NTCredentials("username", "password", "https://hostname", "domain"));
	    CloseableHttpClient httpclient = HttpClients.custom()
	            .setDefaultCredentialsProvider((org.apache.http.client.CredentialsProvider) credsProvider)
	            .build();
	    try {
	        HttpGet httpget = new HttpGet("http://hostname/_api/web/lists");

	        System.out.println("Executing request " + httpget.getRequestLine());
	        CloseableHttpResponse response = httpclient.execute(httpget);
	        try {
	            System.out.println("----------------------------------------");
	            System.out.println(response.getStatusLine());
	            EntityUtils.consume(response.getEntity());
	        } finally {
	            response.close();
	        }
	    } finally {
	        httpclient.close();
	    }*/

		/*HttpClient client = factory.getHttpClient(); //or any method to get a client instance
		Credentials credentials = new UsernamePasswordCredentials("username", "password");
		client.getState().setCredentials(AuthScope.ANY, credentials);*/

	}





	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//fetchRealm("tecst");
		try{
			//String realm = sendGet();
			//System.out.println("--realm in main method--->"+realm);
			//String accessToken = getAccessToken(realm);
			//System.out.println("--accessToken-->"+accessToken);
			//getFilesForFolder(accessToken);
			//getSharePointFilesANDFolders(accessToken);
			//getAllSites(accessToken);
			//basichAuthenicationSharepoint();
			//String accessToken = getBinaryToken("", "sharepoint@pgangparia.onmicrosoft.com","123@topcoder","","");
			//String cookie = submitToken(accessToken);
			//System.out.println("cookie-->"+accessToken);
			//getSharePointFilesANDFolders(accessToken);
			//getCookie("",accessToken);
			//sharePointCall();
			//LoginManager l = new LoginManager();
			//String cookie = l.login();
			//System.out.println("cookie-->"+cookie);
			//String digestToken = l.getDigestAuth("","",cookie);
			//System.out.println("digestToken-->"+digestToken);
			//System.out.println("--requestDigestXml--> " + new LoginManager2().login());
			new LoginManager().login();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
