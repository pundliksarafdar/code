package com.corex.iservice;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.classapp.schedule.Scheduledata;
import com.corex.requestbean.NotesFileRequest;
import com.corex.requestbean.NotesListRequest;
import com.corex.requestbean.StudentBatchRequest;
import com.corex.requestbean.WeeklyScheduleRequest;
import com.corex.responsebean.WeeklyScheduleResponse;

import com.corex.requestbean.IRequest;
import com.corex.responsebean.IResponse;
import com.corex.responsebean.LoginResponse;
import com.corex.responsebean.NotesFileResponse;
import com.corex.responsebean.NotesListResponse;
import com.corex.responsebean.ScheduleResponse;
import com.corex.responsebean.StudentBatchResponse;
import com.corex.responsebean.StudentsInstituteResponse;
@Path("/corex")
public interface IService {
	
	@GET
	@Path("/test")
	public String test();
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(IRequest request);

	@POST
	@Path("/getscheduledate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScheduleByDate(IRequest request);

	@POST
	//@Path("/data/{WeeklyScheduleBean}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	@Path("/getweeklyscheduledate")
	public Response getWeeklyScheduleByDate(WeeklyScheduleRequest request);
	
	@POST
	//@Path("/data/{WeeklyScheduleBean}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	@Path("/getnoteslist")
	public Response getNotesList(NotesListRequest request);
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/getnotesfile")
	public Response getNotesFile(NotesFileRequest request);
	
	@GET
	//@Consumes(MediaType.TEXT_PLAIN)
	@Produces("application/json")
	@Path("/getinstitutes")
	public Response getAllInstitutes(@QueryParam("student_id")String student_id);

	@POST
	//@Path("/data/{WeeklyScheduleBean}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	@Path("/getstudentbatch")
	public Response getStudentBatchList(StudentBatchRequest request);
	
	@GET
	//@Path("/data/{WeeklyScheduleBean}")
	//@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	@Path("/getstudenttodaysschedule")
	public Response getStudentTodaysSchedule(@QueryParam("student_id")String student_id);
}
