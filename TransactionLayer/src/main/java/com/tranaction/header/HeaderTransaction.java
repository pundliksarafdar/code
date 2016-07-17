package com.tranaction.header;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.classapp.db.header.Header;
import com.classapp.db.header.HeaderDB;

public class HeaderTransaction {
	String storageURL;
	
	public HeaderTransaction(String storage) {
		this.storageURL = storage;
	}
	
	public String getStorageURL() {
		return storageURL;
	}

	public void setStorageURL(String storageURL) {
		this.storageURL = storageURL;
	}

	public boolean saveHeader(String headerName,String headerContainer,int regId){
		HeaderDB headerDB = new HeaderDB();
		String uuid = UUID.randomUUID().toString();
		String headerPath = this.storageURL + File.separatorChar+ regId +File.separatorChar+ "headerFiles" + File.separatorChar + uuid + ".html";
		File file = new File(headerPath);
		try {
			FileUtils.writeStringToFile(file, headerContainer);
			headerDB.save(uuid, headerName, regId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
				
	}
	
	public List<Header> getHeaderList(int inst_id) {
	HeaderDB headerDB = new HeaderDB();
	return headerDB.getHeaderList(inst_id);
	}
	
	public String getHeader(String header_id,int regId,String path){
		String headerPath = path + File.separatorChar + header_id + ".html";
		String header = "";
		File file = new File(headerPath);
		try {
			if(file.exists()){
				header = FileUtils.readFileToString(file);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return header;
				
	}
	
}
