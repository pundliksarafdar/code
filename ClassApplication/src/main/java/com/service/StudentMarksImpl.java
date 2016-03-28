package com.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.service.beans.BatchStudentExamMarks;
import com.service.beans.StudentSubjectMarks;
import com.serviceinterface.ClassownerServiceApi;
import com.transaction.studentmarks.StudentMarksTransaction;

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
}
