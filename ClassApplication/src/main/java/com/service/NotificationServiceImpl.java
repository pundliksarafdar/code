package com.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.service.beans.SendNotificationMesssageBean;
import com.service.helper.NotificationServiceHelper;
import com.user.UserBean;

@Path("/notification")
public class NotificationServiceImpl extends ServiceBase{
	@GET @Path("/test") @Produces(MediaType.APPLICATION_JSON)
	public Response test(){
		return Response.ok("test").build();
	}
	
	@POST @Path("/send") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response send(SendNotificationMesssageBean bean){
		NotificationServiceHelper helper = new NotificationServiceHelper();
		UserBean userBean = getUserBean();
		List<String> validationMessage = helper.validateAccess(bean, getRegId());
		if(validationMessage.isEmpty()){
			List<String> statusList = helper.sendMessage(bean,"Short notifiction",userBean.getClassName(),getRegId());
			return Response.ok(statusList).build();
		}else{
			return Response.status(Status.NOT_ACCEPTABLE).entity(validationMessage).build();
					
		}
	}
}
