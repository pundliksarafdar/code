package com.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.classapp.db.register.AdditionalFormFieldBeanDl;
import com.classapp.logger.AppLogger;
import com.config.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.service.beans.ClassOwnerNotificationBean;
import com.service.beans.ClassownerSettingsNotification;
import com.transaction.classownersettingtransaction.ClassownerSettingstransaction;
import com.transaction.register.AdditionalFormFieldTransaction;
import com.user.UserBean;

@Path("ClassownerSettings")
public class ClassownerSettingsServiceImpl extends ServiceBase{
	@GET
	@Path("test")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test(){
		return Response.ok().entity("test").build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(){
		ClassownerSettingsNotification classownerSettingsNotification = new ClassownerSettingsNotification();
		ClassownerSettingstransaction classownerSettingstransaction = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = classownerSettingstransaction.getSettings(getRegId());
		return Response.ok().entity(settings).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response save(ClassOwnerNotificationBean classOwnerNotificationBean){
		ClassownerSettingstransaction classownerSettingstransaction = new ClassownerSettingstransaction();
		boolean isEmailEnabled = getUserBean().getUserStatic().getSettings().getInstituteStats().isEmailAccess();
		boolean isSmsEnabled = getUserBean().getUserStatic().getSettings().getInstituteStats().isSmsAccess();
		if(isEmailEnabled || isSmsEnabled){
			classownerSettingstransaction.saveSettings(classOwnerNotificationBean, getRegId());
			return Response.ok().build();
		}else{
			HashMap<String, String>map = new HashMap<String,String>();
			map.put("NOACCESS", "No access to email and sms");
			return Response.status(Status.BAD_REQUEST).entity(map).build();
		}
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/formField")
	public Response getClassownerSettings(){
		AdditionalFormFieldTransaction transaction = new AdditionalFormFieldTransaction();
		AdditionalFormFieldBeanDl bean = transaction.getAdditionalFormFieldBean(getRegId());
		return Response.accepted(bean).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/formField")
	public Response saveClassowner(HashMap<String, String> formFields){
		AdditionalFormFieldTransaction transaction = new AdditionalFormFieldTransaction();
		transaction.saveAdditionalFormField(formFields, getRegId());
		try {
			updateExcel();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.accepted(formFields).build();
	}
	
	 public  void updateExcel() throws FileNotFoundException {
		 UserBean userBean = getUserBean();
		 AdditionalFormFieldTransaction transaction = new AdditionalFormFieldTransaction();
			AdditionalFormFieldBeanDl bean = transaction.getAdditionalFormFieldBean(getRegId());
			if(bean != null){
		 File file = new File(Constants.STORAGE_PATH+"/SampleStudent.xls");
		FileInputStream myFile = new FileInputStream(file);
		org.apache.poi.ss.usermodel.Workbook workbook = null;

		try {

			workbook = WorkbookFactory.create(myFile);

		} catch (FileNotFoundException e) {
			AppLogger.logError(e);
		} catch (IOException e) {
			AppLogger.logError(e);
		}catch (EncryptedDocumentException e) {
			AppLogger.logError(e);
		} catch (InvalidFormatException e) {
			AppLogger.logError(e);
		}
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		if (rowIterator.hasNext()) {
			Row rowHeader = rowIterator.next();
			String str = bean.getFormField();
			 JsonParser parser = new JsonParser();
			 Object obj = parser.parse(str);
	         JsonObject object = (JsonObject)obj;
			
			/*Gson gson = new Gson();
			JsonElement jsonObj = gson.toJsonTree(str);
			JsonObject object = jsonObj.getAsJsonObject();*/
			Set<Entry<String, JsonElement>> ens = object.entrySet();
			int i = 19;
			if (ens != null) {
                // Iterate JSON Elements with Key values
                for (Entry<String, JsonElement> en : ens) {
                	Cell cell = rowHeader.getCell(i);
        			if(cell == null){
                        cell = rowHeader.createCell(i);
                    }
        			cell.setCellValue(en.getValue().toString().replace("\"", ""));
        			i++;
                   /* System.out.println(en.getKey() + " : ");
                    System.out.println(en.getValue());*/
                }
            }
			
			try {
				myFile.close();
				File f = new File(userBean.getUserStatic().getStorageSpace()+"/SampleFiles");
				if(!f.exists()){
					f.mkdir();
				}
				File oFile = new File(userBean.getUserStatic().getStorageSpace()+"/SampleFiles/SampleStudent.xls");
				
	            FileOutputStream outFile =new FileOutputStream(oFile);
				workbook.write(outFile);
				outFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			}
	}
}
