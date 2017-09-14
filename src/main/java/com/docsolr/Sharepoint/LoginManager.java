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
		try {
			Configuration config = new Configuration();
			config.configure("hibernate.cfg.xml");
			SessionFactory SF = config.buildSessionFactory();
			Session session = SF.openSession();
			Transaction tr = session.beginTransaction();
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
			//SharePointCallout.getAllSubSites("https://pgangparia.sharepoint.com/sites/SPSite", 0, token, cookie, formDigestValue, session);
			//'/sites/SPSite/Shared Documents/SPSite_Folder1'
			//SharePointCallout.getAllFilesInfo("https://pgangparia.sharepoint.com/sites/SPSite", 0, "/sites/SPSite/Shared Documents/SPSite_Folder1", token, cookie, formDigestValue, session);
			
			
			tr.commit();
			System.out.println("commited succesffully");
			session.close();
			SF.close();
			return formDigestValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.identityHashCode("Exeption --> "+e.getMessage());
			e.printStackTrace();
		}		
		return "";
	}

	private String requestToken() throws XPathExpressionException, SAXException,
		ParserConfigurationException, IOException {

		String saml = generateSAML();

		URL u = new URL(sts);
		URLConnection uc = u.openConnection();
		HttpURLConnection connection = (HttpURLConnection) uc;

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Content-Type","text/xml; charset=utf-8");

		OutputStream out = connection.getOutputStream();
		Writer wout = new OutputStreamWriter(out);
		wout.write(saml);

		wout.flush();
		wout.close();

		InputStream in = connection.getInputStream();
		int c;
		StringBuilder sb = new StringBuilder("");
		while ((c = in.read()) != -1)
			sb.append((char) (c));
		in.close();
		String result = sb.toString();
		//System.out.println("--result--> "+result);
		String token = extractToken(result);
		//System.out.println(token);
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
        //System.out.println(token);
        return token;
	}
	private String submitToken(String token) throws IOException {
		//String token = "t=EwBwAk6hBwAUIQT64YiMbkZQLHdw6peopUrQ0O8AAYkt43mh328r0OTpTqSVMQEWGlzlpE906mSyOfU2JgkHQCBz0VBLPKyFEYeCUUqLQ0FmodljevOEceo5L1r+aj207XYvgGl+QBOMxSuNtdbPprICB/+NhRxEynCQe2l1U84a3S20At+OsGorLHKpp1RIfjR6FGGW3ahltWwDvvkcLY5mMtvOHoQx+citNFIvXGY4zzosNgum0OXMlIz26QfODI705ICMV9wmLfbJ4xQjeRAHFrPQxdeQ3mA9tepV9zPKyeAsAmFrMb0/3GUh9GK0jk9O1+N5PZYtL4cKsOrMbGN3Z++IhoTrwLR6/8PJrZNtyKJhv/W35N66THKsKH0DZgAACDKSCSEEFKnaQAEQ+c2vlhFUJ1WBjs9puwnuOFye+J6AvcpFrCaefpBozSYZTQAwJDuHu51xUyrUhrPetgTekrM04m7q6IpqccJBFxTzd3UAkJLgFJQpcerLOFKgYMrVNWOyqEPzn9Zdjv3Xa73HGa36kOUqZeDPcBcxOtMy0I5LmV8tQ4a3Cc302hDax208/eL1fi5xqEiUE89DLEJ8w9KyIWfVUFwvs3r374t/7KJmQH55yZk3p874gNFyToHA4s+0ZuMikRyDTXeYPQ/Jz8rgIYGA+dCwDNb6x+2y26TRX9QiWYvuhcJ8V1xola+Wo6tjHJwon+8QHXLjCiOXkLUvZbjnR2X+UoAnAYNYb5YVeTBqQSO2l19VhK4o5tnHvOhnwVBM8DeGFJSeMChqS7SlPzq/39ntZtPmv9HuvFrP8801pW9KmxgXdoEB&p=";
		//System.out.println("token-->"+token);
		String url = String.format("https://%s.sharepoint.com%s", sharepointContext, loginContextPath);
		url = "https://pgangparia.sharepoint.com/_forms/default.aspx?wa=wsignin1.0";
		URL u = new URL(url);
		//System.out.println(url);
		URLConnection uc = u.openConnection();
		HttpURLConnection connection = (HttpURLConnection) uc;

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Accept", "application/x-www-form-urlencoded");
		connection.addRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
		connection.addRequestProperty("Content-Type","text/xml; charset=utf-8");
		connection.setInstanceFollowRedirects(false);

		OutputStream out = connection.getOutputStream();
		Writer wout = new OutputStreamWriter(out);

		wout.write(token);

		wout.flush();
		wout.close();
		String cookie = "";
		InputStream in = connection.getInputStream();
		int status = connection.getResponseCode();
		//System.out.println("status-->"+status);
		if (status == HttpURLConnection.HTTP_MOVED_TEMP
				  || status == HttpURLConnection.HTTP_MOVED_PERM) {
				    String location = connection.getHeaderField("Location");
				    System.out.println("--location-->"+location);
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
	        	//System.out.println("header: " + headerName + " : " + headerValue);
	        	//System.out.println("compare-->"+(headerName.trim() == "Set-Cookie")+headerName);
	        	if( headerName.equals("Set-Cookie") && (headerValue.contains("rtFa") ||  headerValue.contains("FedAuth"))){
	        		cookie += headerValue + ";";
	        	}
	        }
	    }
	    //cookie = cookie.substring(0, cookie.length() - 1);
		String headerName = connection.getHeaderField("Set-Cookie");
		//System.out.println("headerName");
		//System.out.println(headerName);
		int c;
		StringBuilder sb = new StringBuilder("");
		while ((c = in.read()) != -1)
			sb.append((char) (c));
		in.close();
		String result = sb.toString();
		//System.out.println(result);

		return cookie;
	}
	
	public  String getDigestAuth(String host, String token, String cookie) throws IOException{
		//System.out.println("in requestDigestXml");
		//System.out.println("--cookie-->"+cookie);
        String requestDigestXml = "";
        token = "";
        String url = "https://pgangparia.sharepoint.com/_api/contextinfo";
		URL obj = new URL("https://pgangparia.sharepoint.com/_api/contextinfo");
		HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Accept","application/json; odata=verbose");
		connection.addRequestProperty("Cookie",cookie);
		connection.setRequestProperty("Content-Length", "0");
		connection.setDoOutput(true);
		OutputStream out = connection.getOutputStream();
		Writer wout = new OutputStreamWriter(out);
		wout.write(token);
		wout.flush();
		wout.close();
		String line;
		try{
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		int status = connection.getResponseCode();
		//System.out.println("status-->"+status);
		while ((line = reader.readLine()) != null) {
			JSONObject lineRes = new JSONObject(line);
			//System.out.println(line);
			//System.out.println("--lineRes-->"+lineRes);
			JSONObject array = lineRes.getJSONObject("d");
			JSONObject contextObj = array.getJSONObject("GetContextWebInformation");
			//String name = (String) lineRes.get("GetContextWebInformation");
			//System.out.println("--array--> "+contextObj);
			requestDigestXml = contextObj.getString("FormDigestValue");
			//json.getJSONObject("d").getJSONObject("GetContextWebInformation").getString("FormDigestValue")
			//System.out.println("--requestDigestXml--> "+requestDigestXml);
		}
		}catch(Exception e){
			System.out.println("Exception is -->"+e.getMessage());
		}
        return requestDigestXml;
    }

	public static void main(String [] args) {
		//System.out.println("--requestDigestXml--> " + new LoginManager2().login());
		 new LoginManager().login();
	}
}