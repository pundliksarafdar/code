package com.transaction.questionbank;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

public class ExcelFileTransaction {
	String storageURL;
	
	String EXCEL_FOLDER = "excel";
	public ExcelFileTransaction(String baseStorageURL) {
		this.storageURL = baseStorageURL;
	}
	public String copyExcelFile(String fileId,int regId){
		String tempExcelFilePath = this.storageURL + File.separatorChar+ "excelTemp" + File.separatorChar + fileId;
		String excelFilePath = this.storageURL + File.separatorChar + regId + File.separatorChar+ EXCEL_FOLDER + File.separatorChar +"_"+new Date().getTime()+ fileId;
		File source = new File(tempExcelFilePath);
	    File dest = new File(excelFilePath);
	    String filePath="";
	    if(!dest.getParentFile().exists()){
	    	dest.getParentFile().mkdirs();
	    }
		try {
			Files.copy(source.toPath(), dest.toPath());
			System.out.println("File copied to "+dest.toPath()+" from "+source.toPath());
			filePath=dest.toPath().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}
}
