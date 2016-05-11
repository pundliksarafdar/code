package com.service;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.service.beans.AttendanceScheduleServiceBean;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.attendance.AttendanceTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.teacher.TeacherTransaction;

@Path("/commonDelete")
public class DeleteServiceImpl extends ServiceBase {
	@Context
	private HttpServletRequest request;

	@GET
	@Path("/test")
	public Response serviceOn() {
		return Response.status(200).entity("Service OK sc!..").build();
	}
	
	@DELETE
	@Path("/deleteDivision/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDivision(@PathParam("division") Integer division) {
		// TODO Auto-generated method stub
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		divisionTransactions.deletedivision(getRegId(),division);
		return Response.status(Response.Status.OK).build();
	}
	
	@DELETE
	@Path("/deleteSubject/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubject(@PathParam("subject") Integer sub_id) {
		// TODO Auto-generated method stub
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		subjectTransaction.deleteSubject(getRegId(),sub_id);
		return Response.status(Response.Status.OK).build();
	}
	
	@DELETE
	@Path("/deleteTeacher/{teacher}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTeacher(@PathParam("teacher") Integer teacher_id) {
		// TODO Auto-generated method stub
		TeacherTransaction teacherTransaction = new TeacherTransaction();
		teacherTransaction.deleteTeacher(teacher_id, getRegId());
		return Response.status(Response.Status.OK).build();
	}
	
	@DELETE
	@Path("/deleteBatch/{division}/{batch}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteBatch(@PathParam("division") Integer div_id,@PathParam("batch") Integer batch_id) {
		BatchTransactions batchTransactions = new BatchTransactions();
		batchTransactions.deleteBatch(getRegId(), div_id, batch_id);
		return Response.status(Response.Status.OK).build();
	}
	
	@DELETE
	@Path("/deleteExam/{exam}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteExam(@PathParam("exam") Integer exam_id) {
		ExamTransaction examTransaction =  new ExamTransaction();
		examTransaction.deleteExam(getRegId(), exam_id);
		return Response.status(Response.Status.OK).build();
	}
	
	@DELETE
	@Path("/deleteStudent/{student}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteStudent(@PathParam("student") Integer student_id) {
		StudentTransaction studentTransaction = new StudentTransaction();
		studentTransaction.deleteStudent(student_id, getRegId());
		return Response.status(Response.Status.OK).build();
	}
	
	@DELETE
	@Path("/deleteSubjectTopic/{division}/{subject}/{topic}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubjectTopic(@PathParam("division") Integer div_id,
			@PathParam("subject") Integer sub_id,@PathParam("topic") Integer topic_id) {
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		subjectTransaction.deleteTopics(getRegId(), sub_id, div_id, topic_id);
		return Response.status(Response.Status.OK).build();
	}
}
