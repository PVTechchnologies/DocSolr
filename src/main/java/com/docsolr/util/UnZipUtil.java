package com.docsolr.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONObject;
import org.json.XML;

public class UnZipUtil {
	
	public String unZipIt(byte[] zipFile) throws IOException {

		InputStream myInputStream = new ByteArrayInputStream(zipFile);

		ZipInputStream zis = new ZipInputStream(myInputStream);

		ZipEntry entry;
		
		StringBuilder s = new StringBuilder();
		String str;
		byte[] buffer = new byte[1024];
		int read = 0;
		while ((entry = zis.getNextEntry())!= null) {
		      while ((read = zis.read(buffer, 0, 1024)) >= 0) {
		           s.append(new String(buffer, 0, read));
		      }
		}
		str=s.toString();
		JSONObject jsonObj = XML.toJSONObject(str);
	    System.out.println(jsonObj);
	    return jsonObj.toString();
	}
}
			
