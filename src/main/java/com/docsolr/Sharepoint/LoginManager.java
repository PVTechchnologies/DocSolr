package com.docsolr.Sharepoint;


import java.io.*;
import java.net.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.*;

public class LoginManager {
	private final String sts = "https://login.microsoftonline.com/extSTS.srf";
	private final String loginContextPath = "/_forms/default.aspx?wa=wsignin1.0";
	private final String sharepointContext = "https://pgangparia.sharepoint.com";
	private final String reqXML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><s:Header><a:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue</a:Action><a:ReplyTo><a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address></a:ReplyTo><a:To s:mustUnderstand=\"1\">https://login.microsoftonline.com/extSTS.srf</a:To><o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"><o:UsernameToken><o:Username>sharepoint@pgangparia.onmicrosoft.com</o:Username><o:Password>123@topcoder</o:Password></o:UsernameToken></o:Security></s:Header><s:Body><t:RequestSecurityToken xmlns:t=\"http://schemas.xmlsoap.org/ws/2005/02/trust\"><wsp:AppliesTo xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\"><a:EndpointReference><a:Address>https://pgangparia.sharepoint.com</a:Address></a:EndpointReference></wsp:AppliesTo><t:KeyType>http://schemas.xmlsoap.org/ws/2005/05/identity/NoProofKey</t:KeyType><t:RequestType>http://schemas.xmlsoap.org/ws/2005/02/trust/Issue</t:RequestType><t:TokenType>urn:oasis:names:tc:SAML:1.0:assertion</t:TokenType></t:RequestSecurityToken></s:Body></s:Envelope>";
	private String generateSAML() {
		String saml = reqXML
				.replace("[username]", "sharepoint@pgangparia.onmicrosoft.com");
		saml = saml.replace("[password]", "123@topcoder");
		saml = saml.replace("[endpoint]", String.format("https://%s.sharepoint.com/_forms/default.aspx?wa=wsignin1.0", sharepointContext));
		return saml;
	}
	public String login() {
		String token;
		Configuration config =null;
		SessionFactory SF =null ;
		Session session = null ;
		Transaction tr;
		try {
			config = new Configuration();
			config.configure("hibernate.cfg.xml");
			SF = config.buildSessionFactory();
			session = SF.openSession();
			tr = session.beginTransaction();

			token = requestToken();
			System.out.println("token-->"+token);
			String cookie = submitToken(token);
			System.out.println("cookie-->"+cookie);
			String formDigestValue = getDigestAuth("","",cookie);
			System.out.println(formDigestValue);

			SharePointCallout.getALlSharePointSites(token,cookie,formDigestValue,session,"sharepoint@pgangparia.onmicrosoft.com");
			//SharePointCallout.getAllFilesFoldersFromSite("https://pgangparia.sharepoint.com/sites/SPSite/SpSiteCustomSubSite", token, cookie, formDigestValue, session);
			//SharePointCallout.getAllFilesFromSite("", token, cookie, formDigestValue, session);
			//SharePointCallout.getAllFilesInfo("", token, cookie, formDigestValue);
			//getAllFilesInfo
			//SharePointCallout.camlQueryToFetchFolder("", token, cookie, formDigestValue);
			tr.commit();
			System.out.println("commited succesffully");
			return formDigestValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.identityHashCode("Exeption --> "+e.getMessage());
			e.printStackTrace();
		}finally{
			session.close();
			session.clear();
			SF.close();
		}
		return "";
	}

	private String requestToken() throws IOException  {
		String token = "";
		Writer wout = null;
		OutputStream out =  null;
		try{
			String saml = generateSAML();
			URL u = new URL(sts);
			URLConnection uc = u.openConnection();
			HttpURLConnection connection = (HttpURLConnection) uc;
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.addRequestProperty("Content-Type","text/xml; charset=utf-8");
			out = connection.getOutputStream();
			wout = new OutputStreamWriter(out);
			wout.write(saml);
			InputStream in = connection.getInputStream();
			int c;
			StringBuilder sb = new StringBuilder("");
			while ((c = in.read()) != -1)
				sb.append((char) (c));
			in.close();
			String result = sb.toString();
			token = extractToken(result);
		}catch(Exception e){
			System.out.println("Exceptio is--> "+e.getMessage());
		}finally{
			wout.flush();
			out.close();

		}
		return token;
	}
	private String extractToken(String result) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		//http://stackoverflow.com/questions/773012/getting-xml-node-text-value-with-java-dom
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new InputSource(new StringReader(result)));
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();
		String token = xp.evaluate("//BinarySecurityToken/text()", document.getDocumentElement());
		return token;
	}
	
	private String submitToken(String token) throws IOException {
		String cookie = "";
		Writer wout = null;
		OutputStream out =  null;
		InputStream in  = null;
		try{
			String url = String.format("https://%s.sharepoint.com%s", sharepointContext, loginContextPath);
			url = "https://pgangparia.sharepoint.com/_forms/default.aspx?wa=wsignin1.0";
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			HttpURLConnection connection = (HttpURLConnection) uc;
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.addRequestProperty("Accept", "application/x-www-form-urlencoded");
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
			connection.addRequestProperty("Content-Type","text/xml; charset=utf-8");
			connection.setInstanceFollowRedirects(false);
			out = connection.getOutputStream();
			wout = new OutputStreamWriter(out);
			wout.write(token);
			in = connection.getInputStream();
			int status = connection.getResponseCode();
			if (status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM) {
				String location = connection.getHeaderField("Location");
				//System.out.println("--location-->"+location);
			}
			for (int i=0; ; i++) {
				String headerName = connection.getHeaderFieldKey(i);
				String headerValue = connection.getHeaderField(i);
				//System.out.println("header: " + headerName + " : " + headerValue);
				if (headerName == null && headerValue == null) {
					// No more headers
					break;
				}
				if (headerName == null) {
					// The header value contains the server's HTTP version
				}else{
					if( headerName.equals("Set-Cookie") && (headerValue.contains("rtFa") ||  headerValue.contains("FedAuth")))
						cookie += headerValue + ";";
				}
			}
			int c;
			StringBuilder sb = new StringBuilder("");
			while ((c = in.read()) != -1)
				sb.append((char) (c));
		}catch(Exception e){

		}finally{
			in.close();
			wout.flush();
			wout.close();
		}
		return cookie;
	}

	public  String getDigestAuth(String host, String token, String cookie) throws IOException{
		String requestDigestXml = "";
		token = "";
		String url = "https://pgangparia.sharepoint.com/_api/contextinfo";
		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Accept","application/json; odata=verbose");
		connection.addRequestProperty("Cookie",cookie);
		connection.setRequestProperty("Content-Length", "0");
		connection.setDoOutput(true);
		OutputStream out = connection.getOutputStream();
		Writer wout = new OutputStreamWriter(out);
		wout.write(token);
		String line;
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				JSONObject lineRes = new JSONObject(line);
				JSONObject array = lineRes.getJSONObject("d");
				JSONObject contextObj = array.getJSONObject("GetContextWebInformation");
				requestDigestXml = contextObj.getString("FormDigestValue");
			}
		}catch(Exception e){
			System.out.println("Exception is -->"+e.getMessage());
		}finally{
			wout.flush();
			wout.close();
		}
		return requestDigestXml;
	}

	public static void main(String [] args) {
		//System.out.println("--requestDigestXml--> " + new LoginManager2().login());
		new LoginManager().login();
	}
}