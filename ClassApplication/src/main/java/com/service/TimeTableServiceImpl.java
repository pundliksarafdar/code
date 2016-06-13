package com.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.batch.Batch;
import com.google.gson.JsonObject;
import com.service.beans.MonthlyScheduleServiceBean;
import com.service.beans.ScheduleBean;
import com.serviceinterface.TimeTableServiceApi;
import com.transaction.batch.BatchTransactions;
import com.transaction.schedule.ScheduleTransaction;

@Path("/schedule")
public class TimeTableServiceImpl extends ServiceBase{

	@Context
	private HttpServletRequest request;

	@GET
	@Path("/test")
	@AuthorizeRole(role=AuthorizeRole.ROLE.all)
	public Response serviceOn() {
		getRegId();
		return Response.status(200).entity("Service OK sc!..").build();
	}
	
	@GET
	@Path("/getScheduleForAttendance/{batch}/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSchedule(@PathParam("batch") Integer batch,
			@PathParam("division") Integer division) {
		// TODO Auto-generated method stub
		Long requestDate = Long.parseLong(request.getParameter("date"));
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		java.util.Date date = new  java.util.Date();
		List<Schedule> scheduleList = scheduleTransaction.getSchedule(batch, new Date(requestDate), getRegId(), division);
		for (Schedule schedule : scheduleList) {
			System.out.println(schedule.toString());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getScheduleForMonth/{batch}/{division}/{startDate}/{endDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScheduleForMonth(@PathParam("batch") int batch,
			@PathParam("division") int division,@PathParam("startDate") long startDate,@PathParam("endDate") long endDate) {
		// TODO Auto-generated method stub
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<MonthlyScheduleServiceBean> scheduleList = new ArrayList<MonthlyScheduleServiceBean>();
		if(endDate == 0){
			scheduleList = scheduleTransaction.getMonthSchedule(batch, new Date(startDate), getRegId(), division);
		}else{
			scheduleList = scheduleTransaction.getMonthSchedule(batch, new Date(startDate),new Date(endDate), getRegId(), division);
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	
	@POST
	@Path("/schedule")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSchedule(com.service.beans.Schedule schedule) {
		// TODO Auto-generated method stub
		HashMap<String, String>map = new HashMap<String, String>();
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		String msg = scheduleTransaction.addSchedule(schedule, getRegId());
		map.put("message", msg);
		if(!"".equals(msg)){
			return Response.status(Response.Status.BAD_REQUEST).entity(map).build();
		}
		return Response.status(Response.Status.OK).entity(map).build();
	}
	
	@PUT
	@Path("/schedule")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSchedule(com.service.beans.Schedule schedule) {
		// TODO Auto-generated method stub
		HashMap<String, String>map = new HashMap<String, String>();
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		String msg = scheduleTransaction.updateSchedule(schedule, getRegId());
		map.put("message", msg);
		if(!"".equals(msg)){
			return Response.status(Response.Status.BAD_REQUEST).entity(map).build();
		}
		return Response.status(Response.Status.OK).entity(map).build();
	}
	
	@DELETE
	@Path("/schedule/{div_id}/{batch_id}/{schedule_id}/{date}/{grp_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSchedule(@PathParam("div_id")int div_id,@PathParam("batch_id")int batch_id,
			@PathParam("schedule_id")int schedule_id,@PathParam("date")Date date,@PathParam("grp_id")int grp_id) {
		// TODO Auto-generated method stub
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteSchedule(schedule_id, getRegId(),div_id,batch_id,date,grp_id);
		return Response.status(Response.Status.OK).entity(count).build();
	}
	

	/*@POST
	@Path("/addScheduleObject/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addScheduleObject(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		if (scheduleBean.getStart_time().compareTo(scheduleBean.getEnd_time()) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		if (scheduleBean.getStartDate().compareTo(scheduleBean.getEndDate()) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		HashMap<String, String> resultMap = scheduleTransaction.addSchedule(scheduleBean.getClass_id(), scheduleBean.getBatch_id(), scheduleBean.getSub_id(), scheduleBean.getTeacher_id(), scheduleBean.getStart_time(),
				scheduleBean.getEnd_time(), scheduleBean.getStartDate(), scheduleBean.getEndDate(), scheduleBean.getDiv_id(), scheduleBean.getIsWeekly());
		System.out.println("Add Schedule Execution result : " + resultMap);
		JsonObject jsonObject = new JsonObject();
		
		int count=0;
		for (Entry<String, String> result : resultMap.entrySet()) {
			String counter=""+(count++);
			jsonObject.addProperty(counter,result.getKey()+":"+result.getValue());
		}
		return Response.status(Response.Status.OK).entity(jsonObject.getAsString()).build();
	}

	@POST
	@Path("/addScheduleObjectByDays/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addScheduleObjectByDays(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		if (scheduleBean.getStart_time().compareTo(scheduleBean.getEnd_time()) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		if (scheduleBean.getStartDate().compareTo(scheduleBean.getEndDate()) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		HashMap<String, String> resultMap = scheduleTransaction.addScheduleByDays(scheduleBean.getClass_id(), scheduleBean.getBatch_id(), scheduleBean.getSub_id(), scheduleBean.getTeacher_id(),
				scheduleBean.getStart_time(), scheduleBean.getEnd_time(), scheduleBean.getStartDate(), scheduleBean.getEndDate(), scheduleBean.getDiv_id(), scheduleBean.getMonday(), scheduleBean.getTuesday(), 
				scheduleBean.getWednesday(), scheduleBean.getThursday(), scheduleBean.getFriday(), scheduleBean.getSaturday(),
				scheduleBean.getSunday());
		System.out.println("Add Schedule Execution result : " + resultMap);
		JsonObject jsonObject = new JsonObject();
		int count=0;
		for (Entry<String, String> result : resultMap.entrySet()) {
			String counter=""+(count++);
			jsonObject.addProperty(counter,result.getKey()+":"+result.getValue());
		}
		return Response.status(Response.Status.OK).entity(jsonObject.getAsString()).build();
	}

	@POST
	@Path("/addSchedule/{classid}/{batch}/{division}/{subject}/{teacher}/{startTime}/{endTime}/{startDate}/{endDate}/{weekly}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSchedule(@PathParam("classid") Integer classid, @PathParam("batch") Integer batch,
			@PathParam("division") Integer divId, @PathParam("subject") Integer subject,
			@PathParam("teacher") Integer teacher, @PathParam("startTime") Time startTime,
			@PathParam("endTime") Time endTime, @PathParam("startDate") Date startDate,
			@PathParam("endDate") Date endDate, @PathParam("weekly") Boolean weekly) {
		if (startTime.compareTo(endTime) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		if (startDate.compareTo(endDate) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		HashMap<String, String> resultMap = scheduleTransaction.addSchedule(classid, batch, subject, teacher, startTime,
				endTime, startDate, endDate, divId, weekly);
		System.out.println("Add Schedule Execution result : " + resultMap);
		JsonObject jsonObject = new JsonObject();
		
		int count=0;
		for (Entry<String, String> result : resultMap.entrySet()) {
			String counter=""+(count++);
			jsonObject.addProperty(counter,result.getKey()+":"+result.getValue());
		}
		return Response.status(Response.Status.OK).entity(jsonObject.getAsString()).build();
	}

	@POST
	@Path("/addScheduleByDays/{classid}/{batch}/{division}/{subject}/{teacher}/{startTime}/{endTime}/{startDate}/{endDate}/{monday}/{tuesday}/{wednesday}/{thursday}/{friday}/{saturday}/{sunday}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addScheduleByDays(@PathParam("classid") Integer classid, @PathParam("batch") Integer batch,
			@PathParam("division") Integer divId, @PathParam("subject") Integer subject,
			@PathParam("teacher") Integer teacher, @PathParam("startTime") Time startTime,
			@PathParam("endTime") Time endTime, @PathParam("startDate") Date startDate,
			@PathParam("endDate") Date endDate, @PathParam("monday") Boolean monday,
			@PathParam("tuesday") Boolean tuesday, @PathParam("wednesday") Boolean wednesday,
			@PathParam("thursday") Boolean thursday, @PathParam("friday") Boolean friday,
			@PathParam("saturday") Boolean saturday, @PathParam("sunday") Boolean sunday) {

		if (startTime.compareTo(endTime) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		if (startDate.compareTo(endDate) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		HashMap<String, String> resultMap = scheduleTransaction.addScheduleByDays(classid, batch, subject, teacher,
				startTime, endTime, startDate, endDate, divId, monday, tuesday, wednesday, thursday, friday, saturday,
				sunday);
		System.out.println("Add Schedule Execution result : " + resultMap);
		JsonObject jsonObject = new JsonObject();
		int count=0;
		for (Entry<String, String> result : resultMap.entrySet()) {
			String counter=""+(count++);
			jsonObject.addProperty(counter,result.getKey()+":"+result.getValue());
		}
		return Response.status(Response.Status.OK).entity(jsonObject.getAsString()).build();
	}
	
	@GET
	@Path("/getSchedule/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScheduleObject(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<Schedule> scheduleList = scheduleTransaction.getSchedule(scheduleBean.getBatch_id(), scheduleBean.getStartDate(), scheduleBean.getClass_id(), scheduleBean.getDiv_id());
		for (Schedule schedule : scheduleList) {
			System.out.println(schedule.toString());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getSchedule/{classid}/{batch}/{date}/{division}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSchedule(@PathParam("classid") Integer classid, @PathParam("batch") Integer batch,
			@PathParam("division") Integer division, @PathParam("date") Date date) {
		// TODO Auto-generated method stub
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<Schedule> scheduleList = scheduleTransaction.getSchedule(batch, date, classid, division);
		for (Schedule schedule : scheduleList) {
			System.out.println(schedule.toString());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getWeeklyScheduleObject/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeeklyScheduleObject(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		// TODO Auto-generated method stub
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<Schedule> scheduleList = scheduleTransaction.getWeeklySchedule(scheduleBean.getStartDate(), scheduleBean.getClass_id(), scheduleBean.getDiv_id());
		for (Schedule schedule : scheduleList) {
			System.out.println(schedule.toString());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}

	@GET
	@Path("/getWeeklySchedule/{classid}/{date}/{division}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeeklySchedule(@PathParam("classid") Integer classid, @PathParam("division") Integer division,
			@PathParam("date") Date date) {
		// TODO Auto-generated method stub
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<Schedule> scheduleList = scheduleTransaction.getWeeklySchedule(date, classid, division);
		for (Schedule schedule : scheduleList) {
			System.out.println(schedule.toString());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}

	@GET
	@Path("/getWeeklyScheduleObjectOfBatch/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeeklyScheduleObjectOfBatch(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		// TODO Auto-generated method stub
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<Schedule> scheduleList = scheduleTransaction.getWeeklySchedule(scheduleBean.getBatch_id(), scheduleBean.getStartDate(), scheduleBean.getClass_id(), scheduleBean.getDiv_id());
		for (Schedule schedule : scheduleList) {
			System.out.println(schedule.toString());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getWeeklyScheduleOfBatch/{classid}/{date}/{division}/{batchId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeeklyScheduleOfBatch(@PathParam("classid") Integer classid,
			@PathParam("division") Integer division, @PathParam("date") Date date,
			@PathParam("batchId") Integer batchId) {
		// TODO Auto-generated method stub
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<Schedule> scheduleList = scheduleTransaction.getWeeklySchedule(batchId, date, classid, division);
		for (Schedule schedule : scheduleList) {
			System.out.println(schedule.toString());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}

	@GET
	@Path("/getScheduleObjectOfTeacher/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScheduleObjectOfTeacher(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		// TODO Auto-generated method stub
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<Schedule> scheduleList = scheduleTransaction.getTeachersSchedule(scheduleBean.getClass_id(), scheduleBean.getTeacher_id(), scheduleBean.getStartDate());
		for (Schedule schedule : scheduleList) {
			System.out.println(schedule.toString());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getScheduleOfTeacher/{classid}/{date}/{teacherId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScheduleOfTeacher(@PathParam("classid") Integer classid, @PathParam("date") Date date,
			@PathParam("teacherId") Integer teacherId) {
		// TODO Auto-generated method stub
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<Schedule> scheduleList = scheduleTransaction.getTeachersSchedule(classid, teacherId, date);
		for (Schedule schedule : scheduleList) {
			System.out.println(schedule.toString());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getWeeklyScheduleObjectOfTeacher/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeeklyScheduleObjectOfTeacher(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<Schedule> scheduleList = scheduleTransaction.getTeachersWeeklySchedule(scheduleBean.getClass_id(), scheduleBean.getTeacher_id(), scheduleBean.getStartDate());
		for (Schedule schedule : scheduleList) {
			System.out.println(schedule.toString());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}

	@GET
	@Path("/getWeeklyScheduleOfTeacher/{classid}/{date}/{teacherId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeeklyScheduleOfTeacher(@PathParam("classid") Integer classid, @PathParam("date") Date date,
			@PathParam("teacherId") Integer teacherId) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<Schedule> scheduleList = scheduleTransaction.getTeachersWeeklySchedule(classid, teacherId, date);
		for (Schedule schedule : scheduleList) {
			System.out.println(schedule.toString());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}

	@POST
	@Path("/updateScheduleObject/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateScheduleObject(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		if (scheduleBean.getStart_time().compareTo(scheduleBean.getEnd_time()) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}

		Date today = new Date(new java.util.Date().getTime());
		if (today.compareTo(scheduleBean.getStartDate()) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}

		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();

		String result = scheduleTransaction.updateSchedule(scheduleBean.getClass_id(), scheduleBean.getBatch_id(), scheduleBean.getSub_id(), scheduleBean.getTeacher_id(), scheduleBean.getStart_time(), scheduleBean.getEnd_time(),
				scheduleBean.getStartDate(), scheduleBean.getDiv_id(), scheduleBean.getSchedule_id());
		System.out.println(result);
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	@POST
	@Path("/updateSchedule/{classid}/{batch}/{division}/{subject}/{teacher}/{startTime}/{endTime}/{startDate}/{scheduleId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSchedule(@PathParam("classid") Integer classid, @PathParam("batch") Integer batch,
			@PathParam("division") Integer division, @PathParam("subject") Integer subject,
			@PathParam("teacher") Integer teacher, @PathParam("startTime") Time startTime,
			@PathParam("endTime") Time endTime, @PathParam("startDate") Date startDate,
			@PathParam("scheduleId") Integer scheduleId) {
		if (startTime.compareTo(endTime) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}

		Date today = new Date(new java.util.Date().getTime());
		if (today.compareTo(startDate) > 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}

		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();

		String result = scheduleTransaction.updateSchedule(classid, batch, subject, teacher, startTime, endTime,
				startDate, division, scheduleId);
		System.out.println(result);
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	@DELETE
	@Path("/deleteScheduleObject/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteScheduleObject(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteSchedule(scheduleBean.getSchedule_id(), scheduleBean.getClass_id());
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}

	@DELETE
	@Path("/deleteSchedule/{classId}/{scheduleId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSchedule(@PathParam("classId") Integer classId, @PathParam("scheduleId") Integer scheduleId) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteSchedule(scheduleId, classId);
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}

	@DELETE
	@Path("/deleteScheduleObjectOfTeacher/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteScheduleObjectOfTeacher(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteScheduleOfTeacher(scheduleBean.getTeacher_id(), scheduleBean.getClass_id());
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}
	
	@DELETE
	@Path("/deleteScheduleOfTeacher/{classId}/{teacherId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteScheduleOfTeacher(@PathParam("classId") Integer classId,
			@PathParam("teacherId") Integer teacherId) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteScheduleOfTeacher(teacherId, classId);
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}

	@DELETE
	@Path("/deleteAllScheduleObjectOfBatch/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAllScheduleObjectOfBatch(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteAllSchedulesOfBatch(scheduleBean.getBatch_id(), scheduleBean.getDiv_id(), scheduleBean.getClass_id());
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}
	
	@DELETE
	@Path("/deleteAllSchedulesOfbatch/{classId}/{divId}/{batchId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAllSchedulesOfbatch(@PathParam("classId") Integer classId, @PathParam("divId") Integer divId,
			@PathParam("batchId") Integer batchId) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteAllSchedulesOfBatch(batchId, divId, classId);
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}

	@DELETE
	@Path("/deleteSubjectScheduleObjectOfTeacher/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubjectScheduleObjectOfTeacher(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteSubjectSchedulesOfTeacher(scheduleBean.getSchedule_id(), scheduleBean.getTeacher_id());
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}
	
	@DELETE
	@Path("/deleteSubjectScheduleOfTeacher/{teacherId}/{subjectId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubjectScheduleOfTeacher(@PathParam("teacherId") Integer teacherId,
			@PathParam("subjectId") Integer subjectId) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteSubjectSchedulesOfTeacher(subjectId, teacherId);
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}

	@DELETE
	@Path("/deleteSubjectScheduleObjectOfClass/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubjectScheduleObjectOfClass(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteSubjectSchedulesOfClass(scheduleBean.getSub_id(), scheduleBean.getClass_id());
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}
	
	
	@DELETE
	@Path("/deleteSubjectScheduleOfClass/{classId}/{subjectId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubjectScheduleOfClass(@PathParam("classId") Integer classId,
			@PathParam("subjectId") Integer subjectId) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteSubjectSchedulesOfClass(subjectId, classId);
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}
	
	@DELETE
	@Path("/deleteAllScheduleObjectsOfSubject/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAllScheduleObjectsOfSubject(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteschedulerelatedsubject(scheduleBean.getSub_id());
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}

	@DELETE
	@Path("/deleteAllSchedulesOfSubject/{subjectId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAllSchedulesOfSubject(@PathParam("subjectId") Integer subjectId) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteschedulerelatedsubject(subjectId);
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}

	@DELETE
	@Path("/deleteAllScheduleObjectsOfClass/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAllScheduleObjectsOfClass(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteschedulerelatedtoclass(scheduleBean.getClass_id());
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}
	
	@DELETE
	@Path("/deleteAllSchedulesOfClass/{classId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAllSchedulesOfClass(@PathParam("classId") Integer classId) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteschedulerelatedtoclass(classId);
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}

	@DELETE
	@Path("/deleteBatchSubjectObjectSchedule/{scheduleBean}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteBatchSubjectObjectSchedule(@PathParam("scheduleBean") ScheduleBean scheduleBean) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		BatchTransactions batchTrasaction = new BatchTransactions();
		List<Batch> batchList = batchTrasaction.getAllBatchesOfDivision(scheduleBean.getDiv_id(), scheduleBean.getClass_id());
		int count = 0;
		for (Batch batch : batchList) {
			if (batch.getBatch_id() == scheduleBean.getBatch_id()) {
				count = scheduleTransaction.deleteScheduleOfBatchForSubject(scheduleBean.getBatch_id(), scheduleBean.getSub_id(), scheduleBean.getDiv_id(), scheduleBean.getClass_id());
				break;
			}
		}
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}
	
	@DELETE
	@Path("/deleteBatchSubjectSchedule/{classId}/{batchId}/{subjectId}/{divId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteBatchSubjectSchedule(@PathParam("classId") Integer classId,
			@PathParam("batchId") Integer batchId, @PathParam("subjectId") Integer subjectId,
			@PathParam("divId") Integer divId) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		BatchTransactions batchTrasaction = new BatchTransactions();
		List<Batch> batchList = batchTrasaction.getAllBatchesOfDivision(divId, classId);
		int count = 0;
		for (Batch batch : batchList) {
			if (batch.getBatch_id() == batchId) {
				count = scheduleTransaction.deleteScheduleOfBatchForSubject(batchId, subjectId, divId, classId);
				break;
			}
		}
		System.out.println("No. of rows deleted :" + count);
		return Response.status(Response.Status.OK).entity(count).build();
	}
*/
	
}
