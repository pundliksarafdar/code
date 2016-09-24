package com.service;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.classapp.db.register.AdditionalFormFieldBeanDl;
import com.google.gson.JsonObject;
import com.service.beans.ClassOwnerNotificationBean;
import com.service.beans.ClassownerSettingsNotification;
import com.transaction.classownersettingtransaction.ClassownerSettingstransaction;
import com.transaction.register.AdditionalFormFieldTransaction;

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
		return Response.accepted(formFields).build();
	}
}
