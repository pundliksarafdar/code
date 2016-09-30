package com.service;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.attendance.Attendance;
import com.service.beans.AttendanceScheduleServiceBean;
import com.transaction.attendance.AttendanceTransaction;
import com.transaction.schedule.ScheduleTransaction;

@Path("/attendance")
public class AttendanceImlp extends ServiceBase{
	@Context
	private HttpServletRequest request;

	@GET
	@Path("/test")
	public Response serviceOn() {
		return Response.status(200).entity("Service OK sc!..").build();
	}
	
	@GET
	@Path("/getScheduleForAttendance/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSchedule(@PathParam("batch") Integer batch,
			@PathParam("division") Integer division,@PathParam("date")long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List<AttendanceScheduleServiceBean> scheduleList = attendanceTransaction.getScheduleForAttendance(batch, new Date(date), getRegId(), division);
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getScheduleForUpdateAttendance/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScheduleForUpdateAttendance(@PathParam("batch") Integer batch,
			@PathParam("division") Integer division,@PathParam("date")long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List<AttendanceScheduleServiceBean> scheduleList = attendanceTransaction.getScheduleForUpdateAttendance(batch, new Date(date), getRegId(), division);
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getStudentsForAttendance/{division}/{batch}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsForAttendance(@PathParam("batch") String batch,
			@PathParam("division") Integer division) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentForAttendance(batch, getRegId(), division);
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@POST
	@Path("/saveStudentAttendance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStudentAttendance(List<Attendance> attendanceList) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		boolean status = attendanceTransaction.save(attendanceList,getRegId());
		return Response.status(Response.Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getStudentsForAttendanceUpdate/{division}/{batch}/{sub_id}/{schedule_id}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsForAttendanceUpdate(@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("sub_id") Integer sub_id,@PathParam("schedule_id") Integer schedule_id,
			@PathParam("date") long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentForAttendanceUpdate(batch, getRegId(), division, sub_id,schedule_id, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@POST
	@Path("/updateStudentAttendance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateStudentAttendance(List<Attendance> attendanceList) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		boolean status = attendanceTransaction.updateAttendance(attendanceList,getRegId());
		return Response.status(Response.Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getStudentsDailyAttendance/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsDailyAttendance(@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("date") long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentsDailyAttendance(batch, getRegId(), division, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@GET
	@Path("/getStudentsMonthlyAttendance/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsMonthlyAttendance(@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("date") long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentsMonthlyAttendance(batch, getRegId(), division, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@GET
	@Path("/getStudentsWeeklyAttendance/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsWeeklyAttendance(@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("date") long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentsWeeklyAttendance(batch, getRegId(), division, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@POST
	@Path("/saveCommonDayStudentAttendance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveCommonDayStudentAttendance(List<Attendance> attendanceList) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		boolean status = attendanceTransaction.saveCommonDayAttendance(getRegId(), attendanceList);
		return Response.status(Response.Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/updateCommonDayStudentAttendance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCommonDayStudentAttendance(List<Attendance> attendanceList) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		boolean status = attendanceTransaction.updateCommonDayAttendance(getRegId(), attendanceList);
		return Response.status(Response.Status.OK).entity(status).build();
	}
}
