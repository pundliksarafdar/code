package com.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.service.beans.ClassOwnerNotificationBean;
import com.service.beans.ClassownerSettingsNotification;

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
		return Response.ok().entity(classownerSettingsNotification).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response save(ClassOwnerNotificationBean classOwnerNotificationBean){
		classOwnerNotificationBean = new ClassOwnerNotificationBean();
		return Response.ok().build();
	}
}
