package com.docsolr.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.docsolr.common.dao.GenericDAO;
import com.docsolr.dto.SalesforceMetadataTree;
import com.docsolr.entity.SalesforceSetupDetail;
import com.docsolr.entity.Users;
import com.docsolr.service.common.GenericService;
import com.docsolr.util.CommonUtil;
import com.docsolr.util.UnZipUtil;
import com.sforce.soap.metadata.AsyncResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.PackageTypeMembers;
import com.sforce.soap.metadata.RetrieveMessage;
import com.sforce.soap.metadata.RetrieveRequest;
import com.sforce.soap.metadata.RetrieveResult;
import com.sforce.soap.metadata.RetrieveStatus;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * @author Yadav
 *
 */
@Controller
public class MetadataQueryController {

	@Autowired
	public GenericService<SalesforceSetupDetail> salesforceSetupDetail;

	@Autowired
	public GenericDAO<SalesforceSetupDetail> SFSD;

	static BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));

	// Method for inserting tree data in database

	@RequestMapping(value = "/addObjects", method = RequestMethod.POST)

	public String addObjects(@RequestBody String selecteditem) {

		String key, selected, oldKey = "";
		String tempKey = "";
		boolean haveID = false;
		StringBuilder result = new StringBuilder();
		Long id = null, oldid = null, tempid = null;
		Integer idStringvalue = null;
		JSONArray jsonArray = new JSONArray(selecteditem);

		/* Code to detect which record needs to be delete Starts here */
		
		ArrayList<String> listdata = new ArrayList<String>();
		if (jsonArray != null) {

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				try {
					listdata.add(Integer.toString(jsonObject.getInt("idvalue")));

				} catch (Exception e) {
					continue;
				}
			}
		}

		Users users = new Users();
		users = CommonUtil.getCurrentSessionUser();
		Map<String, SalesforceSetupDetail> tableData = new HashMap<>();
		tableData = salesforceSetupDetail.getKeyValueMapString("SalesforceSetupDetail", "salesforceObjectApiName",
				"SalesforceSetupDetail", " Where createdById=" + users.getId());

		ArrayList<String> ListOfID = new ArrayList<String>();
		/* Creating list of ID coming from databse starts here */
		Iterator<Map.Entry<String, SalesforceSetupDetail>> entries = tableData.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, SalesforceSetupDetail> entry = entries.next();
			SalesforceSetupDetail colvalues = tableData.get(entry.getKey().toString());
			ListOfID.add(colvalues.getId().toString());
		}

		Collection<String> selectedList = new ArrayList(listdata);
		Collection<String> unSelectedList = new ArrayList(ListOfID);

		List<String> sourceList = new ArrayList<String>(selectedList);
		List<SalesforceSetupDetail> ssdlist = new ArrayList<SalesforceSetupDetail>();
		List<String> destinationList = new ArrayList<String>(unSelectedList);
		destinationList.removeAll(sourceList);
		for (String string : destinationList) {
			SalesforceSetupDetail ssd = new SalesforceSetupDetail();
			ssd.setId(Long.parseLong(string));
			ssdlist.add(ssd);
		}
		System.out.println(destinationList);

		if (!ssdlist.isEmpty()) {
			SFSD.deleteBatchEntity(SalesforceSetupDetail.class, ssdlist);
		}
		/* Deleting of record End here */

		List<SalesforceSetupDetail> listssd = new ArrayList<SalesforceSetupDetail>();
		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject jsonObject = jsonArray.getJSONObject(i);
			key = jsonObject.getString("key");
			selected = jsonObject.getString("selected");
			idStringvalue = !jsonObject.get("idvalue").equals(null) ? ((Integer) jsonObject.get("idvalue")) : null;

			if (idStringvalue != null) {
				id = new Long(idStringvalue.toString());
				tempKey = key;
			}
			if (!tempKey.equalsIgnoreCase(key) && idStringvalue == null) {
				id = null;
			}

			if (!key.equalsIgnoreCase(
					selected))/* only go inside for field name */
			{

				if (!oldKey.equalsIgnoreCase(key)) { /*Old key/id storing previous value of key/id*/

					if (oldKey != "") {/*
										 * This will run whenever new object name
										 * occurs and store previously build
										 * String
										 */
						/* All objects were stored from here except last one */
						result.deleteCharAt(result.length() - 1);
						SalesforceSetupDetail detail = new SalesforceSetupDetail(oldKey, result.toString());
						detail.setId(oldid);
						tempid = oldid;
						listssd.add(detail);
						result = new StringBuilder();
					}
				}

				oldid = id;
				oldKey = key;
				result.append(selected);
				result.append(",");

				/* This will run only for last object */
				if (i == jsonArray.length() - 1) {
					result.deleteCharAt(result.length() - 1);
					SalesforceSetupDetail detail = new SalesforceSetupDetail(oldKey, result.toString());
					if (tempid != oldid) {
						detail.setId(oldid);
					}
					listssd.add(detail);
				}
			}

		}

		salesforceSetupDetail.saveUpdateBatchEntity(SalesforceSetupDetail.class, listssd);
		System.out.println(selecteditem);

		return null;
	}

}
