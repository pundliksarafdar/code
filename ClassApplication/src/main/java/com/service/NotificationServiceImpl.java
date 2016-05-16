package com.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.service.beans.SendNotificationMesssageBean;

@Path("/notification")
public class NotificationServiceImpl extends ServiceBase{
	@GET @Path("/test") @Produces(MediaType.APPLICATION_JSON)
	public Response test(){
		return Response.ok("test").build();
	}
	
	@POST @Path("/send") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response send(SendNotificationMesssageBean bean){
		return Response.ok("test").build();
	}
}
