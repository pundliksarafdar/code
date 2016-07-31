package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.service.beans.SendAcademicAlertAttendanceBean;
import com.service.beans.SendAcademicAlertFeeDueBean;
import com.service.beans.SendAcademicAlertProgressCardBean;
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
	
	@POST @Path("/sendAcademicAlerts/feeDue") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response sendAcademicAlerts(SendAcademicAlertFeeDueBean bean){
		NotificationServiceHelper helper = new NotificationServiceHelper();
		HashMap statusMap = helper.sendFeesDue(bean, getRegId());
		return Response.status(Status.OK).entity(statusMap).build();
	}
	
	@POST @Path("/sendAcademicAlerts/attendace") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response sendAcademicAlertsAttendance(SendAcademicAlertAttendanceBean bean){
		NotificationServiceHelper helper = new NotificationServiceHelper();
		HashMap statusMap = helper.sendAttendance(bean, getRegId());
		return Response.status(Status.OK).entity(statusMap).build();
	}
	
	@POST @Path("/sendAcademicAlerts/progressCard") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response sendAcademicAlertsProgressCard(SendAcademicAlertProgressCardBean bean){
		NotificationServiceHelper helper = new NotificationServiceHelper();
		HashMap statusMap = helper.sendProgressCard(bean, getRegId());
		return Response.status(Status.OK).entity(statusMap).build();
	}
	
	@POST @Path("/sendFeesDue/{div_id}/{batch_id}/{sms}/{email}/{parent}/{student}") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response sendFeesDue( String studentIds,@PathParam("div_id")int div_id,@PathParam("batch_id")int batch_id,
			@PathParam("sms") boolean sms,@PathParam("email") boolean email,@PathParam("parent") boolean parent,@PathParam("student") boolean student){
		NotificationServiceHelper helper = new NotificationServiceHelper();
		List<Integer> studntIdList = Stream.of(studentIds.split(","))
		        .map(Integer::parseInt)
		        .collect(Collectors.toList());
		HashMap statusMap = helper.sendFeesDue(getRegId(), div_id, batch_id, studntIdList,email,sms,parent,student);
		return Response.status(Status.OK).entity(statusMap).build();
	}
}
