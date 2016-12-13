package com.corex.service;

import java.sql.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.classapp.db.attendance.Attendance;
import com.service.beans.AttendanceScheduleServiceBean;
import com.transaction.attendance.AttendanceTransaction;

@Path("/corex/attendance")
@Produces(MediaType.APPLICATION_JSON)
public class AttendanceService extends MobileServiceBase{
	@GET
	@Path("/test")
	public Response test(){
		return Response.ok("test").build();
	}
	
	@GET
	@Path("/scheduleForUpdateAttendance/{division}/{batch}/{date}")
	public Response getScheduleForAttendace(@PathParam("batch") Integer batch,
			@PathParam("division") Integer division,@PathParam("date")long date,
			@QueryParam("instituteId")int instituteId){
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		if(instituteId==0){
			instituteId = getRegId();
		}
		List<AttendanceScheduleServiceBean> scheduleList = attendanceTransaction.getScheduleForAttendance(batch, new Date(date), instituteId, division);
		return Response.ok(scheduleList).build();
	}
	
	@GET
	@Path("/studentsForAttendance/{division}/{batch}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsForAttendance(@PathParam("batch") String batch,
			@PathParam("division") Integer division,@QueryParam("instituteId")int instituteId) {
		if(instituteId==0){
			instituteId = getRegId();
		}
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentForAttendance(batch, instituteId, division);
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@POST
	@Path("/saveStudentAttendance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStudentAttendance(@QueryParam("instituteId")int instituteId,List<Attendance> attendanceList) {
		if(instituteId==0){
			instituteId = getRegId();
		}
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		boolean status = attendanceTransaction.save(attendanceList,instituteId);
		return Response.status(Response.Status.OK).entity(status).build();
	}
}
