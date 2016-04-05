package com.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.classapp.notification.GeneralNotification.NOTIFICATION_KEYS;
import com.google.gson.Gson;
import com.notification.access.Notification;
import com.notification.bean.MessageDafaultInterface;
import com.service.beans.BatchStudentExamMarks;
import com.service.beans.ProgressCardMessage;
import com.service.beans.ProgressCardServiceBean;
import com.service.beans.StudentProgressCard;
import com.service.beans.StudentSubjectMarks;
import com.serviceinterface.ClassownerServiceApi;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.util.NotificationEnum;

@Path("/studentmarks") 
public class StudentMarksImpl extends ServiceBase{
	@Context
	private HttpServletRequest request;
	
	@GET
	@Path("/test")
	public Response serviceOn(){
		int regId = getRegId();
		return Response.status(200).entity("Service.."+regId).build();
	}
	
	@GET
	@Path("/getStudentExamMarks/{div_id}/{batch_id}/{exam_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentExamMarks(@PathParam("div_id") int div_id,@PathParam("batch_id") int batch_id,
										@PathParam("exam_id") int exam_id) {
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		BatchStudentExamMarks batchStudentExamMarks  = marksTransaction.getStudentExamMarks(getRegId(), div_id, batch_id, exam_id);
		return Response.status(200).entity(batchStudentExamMarks).build();
	}
	
	@GET
	@Path("/getStudentExamSubjectMarks/{div_id}/{batch_id}/{sub_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentExamSubjectMarks(@PathParam("div_id") int div_id,@PathParam("batch_id") int batch_id,
										@PathParam("sub_id") int sub_id) {
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		StudentSubjectMarks studentSubjectMarks = marksTransaction.getStudentExamSubjectMarks(getRegId(), div_id, batch_id, sub_id);
		return Response.status(200).entity(studentSubjectMarks).build();
	}
	
	@GET
	@Path("/getStudentProgressCard/{div_id}/{batch_id}/{exam_ids}/{student_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentProgressCard(@PathParam("div_id") int div_id,@PathParam("batch_id") int batch_id,
										@PathParam("exam_ids") String exam_ids,@PathParam("student_id") int student_id) {
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		ProgressCardServiceBean progressCardServiceBean = marksTransaction.getStudentProgressCard(getRegId(), div_id, batch_id, exam_ids, student_id);
		return Response.status(200).entity(progressCardServiceBean).build();
	}
	
	@GET
	@Path("/sendStudentProgressCard/{div_id}/{batch_id}/{exam_ids}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendStudentProgressCard(@PathParam("div_id") int div_id,@PathParam("batch_id") int batch_id,
										@PathParam("exam_ids") String exam_ids) {
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		List<StudentProgressCard> progressCardList = marksTransaction.sendStudentProgressCard(getRegId(), div_id, batch_id, exam_ids);
		Map<Integer, MessageDafaultInterface> map = new HashMap<Integer, MessageDafaultInterface>();
		for (StudentProgressCard studentProgressCard : progressCardList) {
			MessageDafaultInterface cardMessage = new ProgressCardMessage();
			cardMessage.setEmailObject(studentProgressCard);
			cardMessage.setEmailTemplate("progressCard.tmpl");
			map.put(studentProgressCard.getStudent_id(), cardMessage);
		}
		Notification notification = new Notification();
		notification.send(NotificationEnum.MessageCategery.PROGRESS_CARD_MANUAL, (HashMap<Integer, MessageDafaultInterface>) map, getRegId());
		StudentProgressCard progressCard = new StudentProgressCard();
		return Response.status(200).entity(progressCard).build();
	}
}
