package com.serviceinterface;

import java.sql.Date;
import java.sql.Time;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public interface TimeTableServiceApi {
	@GET
	@Path("/test")
	public Response serviceOn();
	
	@POST
	@Path("/addSchedule/{classid}/{batch}/{division}/{subject}/{teacher}/{startTime}/{endTime}/{startDate}/{endDate}/{weekly}/")
	public Response addSchedule(@PathParam("classid")Integer classid,
	@PathParam("batch")Integer batch,
	@PathParam("division")Integer divId,
	@PathParam("subject")Integer subject,
	@PathParam("teacher")Integer teacher,
	@PathParam("startTime")Time startTime,
	@PathParam("endTime")Time endTime,
	@PathParam("startDate")Date startDate,
	@PathParam("endDate")Date endDate,
	@PathParam("weekly")Boolean weekly);
	
	@POST
	@Path("/addScheduleByDays/{classid}/{batch}/{division}/{subject}/{teacher}/{startTime}/{endTime}/{startDate}/{endDate}/{monday}/{tuesday}/{wednesday}/{thursday}/{friday}/{saturday}/{sunday}")
	public Response addScheduleByDays(@PathParam("classid")Integer classid,
	@PathParam("batch")Integer batch,
	@PathParam("division")Integer divId,
	@PathParam("subject")Integer subject,
	@PathParam("teacher")Integer teacher,
	@PathParam("startTime")Time startTime,
	@PathParam("endTime")Time endTime,
	@PathParam("startDate")Date startDate,
	@PathParam("endDate")Date endDate,
	@PathParam("monday")Boolean monday,
	@PathParam("tuesday")Boolean tuesday,
	@PathParam("wednesday")Boolean wednesday,
	@PathParam("thursday")Boolean thursday,
	@PathParam("friday")Boolean friday,
	@PathParam("saturday")Boolean saturday,
	@PathParam("sunday")Boolean sunday);
		
	@GET
	@Path("/getSchedule/{classid}/{batch}/{date}/{division}/")
	public Response getSchedule(@PathParam("classid")Integer classid,
	@PathParam("batch")Integer batch,
	@PathParam("division")Integer division,
	@PathParam("date")Date date);
	
	@GET
	@Path("/getWeeklySchedule/{classid}/{date}/{division}/")
	public Response getWeeklySchedule(@PathParam("classid")Integer classid,
	@PathParam("division")Integer division,
	@PathParam("date")Date date);
	
	@GET
	@Path("/getWeeklyScheduleOfBatch/{classid}/{date}/{division}/{batchId}/")
	public Response getWeeklyScheduleOfBatch(@PathParam("classid")Integer classid,
	@PathParam("division")Integer division,
	@PathParam("date")Date date,
	@PathParam("batchId")Integer batchId);
	
	@GET
	@Path("/getScheduleOfTeacher/{classid}/{date}/{teacherId}/")
	public Response getScheduleOfTeacher(@PathParam("classid")Integer classid,
	@PathParam("date")Date date,
	@PathParam("teacherId")Integer teacherId);
	
	@GET
	@Path("/getWeeklyScheduleOfTeacher/{classid}/{date}/{teacherId}/")
	public Response getWeeklyScheduleOfTeacher(@PathParam("classid")Integer classid,
	@PathParam("date")Date date,
	@PathParam("teacherId")Integer teacherId);
	
	@POST
	@Path("/updateSchedule/{classid}/{batch}/{division}/{subject}/{teacher}/{startTime}/{endTime}/{startDate}/{scheduleId}/")
	public Response updateSchedule(@PathParam("classid")Integer classid,
			@PathParam("batch")Integer batch,
			@PathParam("division")Integer division,
			@PathParam("subject")Integer subject,
			@PathParam("teacher")Integer teacher,
			@PathParam("startTime")Time startTime,
			@PathParam("endTime")Time endTime,
			@PathParam("startDate")Date startDate,
			@PathParam("scheduleId")Integer scheduleId);
	
	@DELETE
	@Path("/deleteSchedule/{classId}/{scheduleId}/")
	public Response deleteSchedule(@PathParam("classId")Integer classId,
			@PathParam("scheduleId")Integer scheduleId);
	
	@DELETE
	@Path("/deleteScheduleOfTeacher/{classId}/{teacherId}/")
	public Response deleteScheduleOfTeacher(@PathParam("classId")Integer classId,
			@PathParam("teacherId")Integer teacherId);
	
	@DELETE
	@Path("/deleteAllSchedulesOfbatch/{classId}/{divId}/{batchId}/")
	public Response deleteAllSchedulesOfbatch(@PathParam("classId")Integer classId,
			@PathParam("divId")Integer divId,
			@PathParam("batchId")Integer batchId);
	
	@DELETE
	@Path("/deleteSubjectScheduleOfTeacher/{teacherId}/{subjectId}/")
	public Response deleteSubjectScheduleOfTeacher(@PathParam("teacherId")Integer teacherId,
			@PathParam("subjectId")Integer subjectId);
	
	@DELETE
	@Path("/deleteSubjectScheduleOfClass/{classId}/{subjectId}/")
	public Response deleteSubjectScheduleOfClass(@PathParam("classId")Integer classId,
			@PathParam("subjectId")Integer subjectId);
	
	@DELETE
	@Path("/deleteAllSchedulesOfSubject/{subjectId}/")
	public Response deleteAllSchedulesOfSubject(@PathParam("subjectId")Integer subjectId);
	
	@DELETE
	@Path("/deleteAllSchedulesOfClass/{classId}/")
	public Response deleteAllSchedulesOfClass(@PathParam("classId")Integer classId);
	
	@DELETE
	@Path("/deleteBatchSubjectSchedule/{classId}/{batchId}/{subjectId}/{divId}/")
	public Response deleteBatchSubjectSchedule(@PathParam("classId")Integer classId,
			@PathParam("batchId")Integer batchId,
			@PathParam("subjectId")Integer subjectId,
			@PathParam("divId")Integer divId);
	
	
}
