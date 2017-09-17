package com.docsolr.Sharepoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Service
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
	
	public LoginDetail login() {
		String token;
		try {
			LoginDetail logDetail = new LoginDetail();
			logDetail.token = requestToken();
			System.out.println("token-->"+logDetail.token);
			logDetail.cookie = submitToken(logDetail.token);
			System.out.println("cookie-->"+logDetail.cookie);
			logDetail.formDigestValue = getDigestAuth("","",logDetail.cookie);
			System.out.println(logDetail.formDigestValue);
			logDetail.userName = "sharepoint@pgangparia.onmicrosoft.com";
			//callout.getALlSharePointSites(token,cookie,formDigestValue,"sharepoint@pgangparia.onmicrosoft.com");
			
			return logDetail;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.identityHashCode("Exeption --> "+e.getMessage());
			e.printStackTrace();
		}		
		return null;
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
			JSONObject array = lineRes.getJSONObject("d");
			JSONObject contextObj = array.getJSONObject("GetContextWebInformation");
			requestDigestXml = contextObj.getString("FormDigestValue");
		}
		}catch(Exception e){
			System.out.println("Exception is -->"+e.getMessage());
		}
        return requestDigestXml;
    }

	class LoginDetail{
		String token;
		String cookie;
		String formDigestValue;
		String userName;
	}
}