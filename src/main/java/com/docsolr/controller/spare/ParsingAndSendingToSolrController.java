package com.docsolr.controller.spare;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ParsingAndSendingToSolrController {

	public void xmlParser(String json) throws ParserConfigurationException, TransformerException, JsonIOException,
			JsonSyntaxException, XMLStreamException, IOException, SolrServerException {

		List<String> list = new ArrayList<String>();

		StringWriter stringWriter = new StringWriter();
		XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();

		// root element
		/*
		 * Element addElement = doc.createElement("add");
		 * doc.appendChild(addElement);
		 */

		xMLStreamWriter.writeStartDocument();
		xMLStreamWriter.writeStartElement("add");
		try {
			Collection<SolrInputDocument> batch = new ArrayList<SolrInputDocument>();
			JSONArray parentArray = new JSONArray(json);
			if (parentArray != null && parentArray.length() > 0) {
				for (int i = 0; i < parentArray.length(); i++) {
					JSONArray childJsonArray = parentArray.optJSONArray(i);
					if (childJsonArray != null && childJsonArray.length() > 0) {
						for (int j = 0; j < childJsonArray.length(); j++) {
							JSONObject OutputObject = new JSONObject();
							JSONObject josnObject = childJsonArray.getJSONObject(j);
							JSONArray innerChildJsonArray = josnObject.getJSONArray("children");
							if (innerChildJsonArray != null && innerChildJsonArray.length() > 0) {
								xMLStreamWriter.writeStartElement("doc");
								/*
								 * Element docelement =
								 * doc.createElement("doc");
								 * addElement.appendChild(docelement);
								 */
								list = new ArrayList<String>();
								boolean isIdFieldExist = false;
								boolean isFieldExist = false;
								for (int z = 0; z < innerChildJsonArray.length(); z++) {
									String value = "";
									JSONObject innerOutputObject = new JSONObject();
									JSONObject childJosnObject = innerChildJsonArray.getJSONObject(z);
									JSONObject forName = childJosnObject.getJSONObject("name");
									String name = forName.getString("localPart");
									name = name.toLowerCase();
									if (childJosnObject.has("value")) {

										value = childJosnObject.getString("value");
										innerOutputObject.put("name", name);
										innerOutputObject.put("text", value);
										if (list.isEmpty()) {
											list.add(name);

											xMLStreamWriter.writeStartElement("field");
											xMLStreamWriter.writeAttribute("name", name);
											xMLStreamWriter.writeCharacters(value);
											xMLStreamWriter.writeEndElement();

											/*
											 * Element fieldname =
											 * doc.createElement("field"); Attr
											 * attr =
											 * doc.createAttribute("name");
											 * attr.setValue(name);
											 * fieldname.setAttributeNode(attr);
											 * fieldname.appendChild(doc.
											 * createTextNode(value));
											 * docelement.appendChild(fieldname)
											 * ;
											 */
										} else if (list.contains(name) && name.equalsIgnoreCase("id")) {
											isIdFieldExist = true;
										} else if (list.contains(name)) {
											/*
											 * Element fieldname =
											 * doc.createElement("field"); Attr
											 * attr =
											 * doc.createAttribute("name");
											 * attr.setValue(name+"__c");
											 * fieldname.setAttributeNode(attr);
											 * fieldname.appendChild(doc.
											 * createTextNode(value));
											 * docelement.appendChild(fieldname)
											 * ;
											 */

											xMLStreamWriter.writeStartElement("field");
											xMLStreamWriter.writeAttribute("name", name + "__c");
											xMLStreamWriter.writeCharacters(value);
											xMLStreamWriter.writeEndElement();

										} else {
											list.add(name);
											/*
											 * Element fieldname =
											 * doc.createElement("field"); Attr
											 * attr =
											 * doc.createAttribute("name");
											 * attr.setValue(name);
											 * fieldname.setAttributeNode(attr);
											 * fieldname.appendChild(doc.
											 * createTextNode(value));
											 * docelement.appendChild(fieldname)
											 * ;
											 */
											xMLStreamWriter.writeStartElement("field");
											xMLStreamWriter.writeAttribute("name", name);
											xMLStreamWriter.writeCharacters(value);
											xMLStreamWriter.writeEndElement();

										}

									}
								}

								xMLStreamWriter.writeEndElement();
							}

						}
					}
				}

				xMLStreamWriter.writeEndDocument();
				xMLStreamWriter.flush();
				xMLStreamWriter.close();
				String xmlString = stringWriter.getBuffer().toString();
				stringWriter.close();
				System.out.println(xmlString);
				/* UpdatingDocument(xmlString); */

				/*
				 * // write the content into xml file TransformerFactory
				 * transformerFactory = TransformerFactory.newInstance();
				 * Transformer transformer =
				 * transformerFactory.newTransformer(); DOMSource source = new
				 * DOMSource(doc); StreamResult result = new StreamResult(new
				 * File("d:\\cars.xml")); transformer.transform(source, result);
				 * 
				 * // Output to console for testing StreamResult consoleResult =
				 * new StreamResult(System.out); transformer.transform(source,
				 * consoleResult);
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	
	}
	
	/*
	 * @RequestMapping (value = "/sendrecord", method = RequestMethod.GET)
	 * 
	 * @ResponseBody public int sendSolrData() { String strURL =
	 * "http://132.148.68.21:8983/solr/Dummydata/update?commit=true";
	 * 
	 * String strXMLFilename = "d:\\cars.xml"; File input = new
	 * File(strXMLFilename); PostMethod post = new PostMethod(strURL); try {
	 * post.setRequestEntity(new InputStreamRequestEntity( new
	 * FileInputStream(input), input.length()));
	 * post.setRequestHeader("Content-type",
	 * "text/xml; charset=ISO-8859-1"); HttpClient httpclient = new
	 * HttpClient();
	 * 
	 * int result = httpclient.executeMethod(post);
	 * System.out.println("Response status code: " + result);
	 * System.out.println("Response body: ");
	 * System.out.println(post.getResponseBodyAsString()); return result; }
	 * catch (IOException e) { e.printStackTrace(); }catch (Exception e) {
	 * e.printStackTrace(); } finally { post.releaseConnection(); } return
	 * 0; }
	 */

	/*JAVA SCRIPT*/
	 
		/* $scope.postXml = function() {
			 $http.get("sendrecord").then(function(response) {
				 $scope.apiRecordData = response.data;
				 if( $scope.apiRecordData==200)
					 {
					 	$scope.result = "Data Posted Successfully" ;
					 }
				 else{
					 $scope.result = "Error while posting Data" 
				 }
			 });
		 }*/
		
		 
	/*Write to file*/
	public void Parsing(String json) throws ParserConfigurationException {

		List<String> list = new ArrayList<String>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();

		// root element

		Element addElement = doc.createElement("add");
		doc.appendChild(addElement);

		try {
			Collection<SolrInputDocument> batch = new ArrayList<SolrInputDocument>();
			JSONArray parentArray = new JSONArray(json);
			if (parentArray != null && parentArray.length() > 0) {
				for (int i = 0; i < parentArray.length(); i++) {
					JSONArray childJsonArray = parentArray.optJSONArray(i);
					if (childJsonArray != null && childJsonArray.length() > 0) {
						for (int j = 0; j < childJsonArray.length(); j++) {
							JSONObject OutputObject = new JSONObject();
							JSONObject josnObject = childJsonArray.getJSONObject(j);
							JSONArray innerChildJsonArray = josnObject.getJSONArray("children");
							if (innerChildJsonArray != null && innerChildJsonArray.length() > 0) {

								Element docelement = doc.createElement("doc");
								addElement.appendChild(docelement);

								list = new ArrayList<String>();
								boolean isIdFieldExist = false;
								boolean isFieldExist = false;
								for (int z = 0; z < innerChildJsonArray.length(); z++) {
									String value = "";
									JSONObject innerOutputObject = new JSONObject();
									JSONObject childJosnObject = innerChildJsonArray.getJSONObject(z);
									JSONObject forName = childJosnObject.getJSONObject("name");
									String name = forName.getString("localPart");
									name = name.toLowerCase();
									if (childJosnObject.has("value")) {

										value = childJosnObject.getString("value");
										innerOutputObject.put("name", name);
										innerOutputObject.put("text", value);
										if (list.isEmpty()) {
											list.add(name);

											Element fieldname = doc.createElement("field");
											Attr attr = doc.createAttribute("name");
											attr.setValue(name);
											fieldname.setAttributeNode(attr);
											fieldname.appendChild(doc.createTextNode(value));
											docelement.appendChild(fieldname);

										} else if (list.contains(name) && name.equalsIgnoreCase("id")) {
											isIdFieldExist = true;
										} else if (list.contains(name)) {

											Element fieldname = doc.createElement("field");
											Attr attr = doc.createAttribute("name");
											attr.setValue(name + "__c");
											fieldname.setAttributeNode(attr);
											fieldname.appendChild(doc.createTextNode(value));
											docelement.appendChild(fieldname);

										} else {
											list.add(name);

											Element fieldname = doc.createElement("field");
											Attr attr = doc.createAttribute("name");
											attr.setValue(name);
											fieldname.setAttributeNode(attr);
											fieldname.appendChild(doc.createTextNode(value));
											docelement.appendChild(fieldname);

										}

									}
								}

							}

						}
					}
				}

				// write the content into xml file 
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("d:\\cars.xml"));
				transformer.transform(source, result);

				// Output to console for testing 
				StreamResult consoleResult = new StreamResult(System.out);
				transformer.transform(source, consoleResult);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	
	}
}
