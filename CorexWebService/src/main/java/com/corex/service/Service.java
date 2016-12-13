package com.corex.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.RequestWrapper;

import com.corex.requestbean.WeeklyScheduleRequest;
import com.corex.responsebean.WeeklyScheduleResponse;
import com.datalayer.division.InstituteData;
import com.datalayer.division.InstituteDataKey;
import com.google.gson.JsonObject;
import com.mobile.bean.SessionBean;
import com.service.beans.MonthlyScheduleServiceBean;
import com.service.beans.SyllabusFilterBean;
import com.classapp.db.Notes.Notes;
import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Teacher.Teacher;
import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.db.batch.Batch;
import com.classapp.db.notificationpkg.Notification;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.db.subject.Subjects;
import com.classapp.login.LoginBeanMobile;
import com.classapp.login.UserBean;
import com.classapp.persistence.Constants;
import com.classapp.schedule.ScheduleBean;
import com.classapp.schedule.Scheduledata;
import com.classapp.servicetable.ServiceMap;
import com.corex.annotation.AuthorizeRole;
import com.corex.annotation.AuthorizeRole.ROLE;
import com.corex.common.Common;
import com.corex.iservice.IService;
import com.corex.requestbean.IRequest;
import com.corex.requestbean.LoginBean;
import com.corex.requestbean.NotesFileRequest;
import com.corex.requestbean.NotesListRequest;
import com.corex.requestbean.StudentBatchRequest;
import com.corex.responsebean.IResponse;
import com.corex.responsebean.LoginResponse;
import com.corex.responsebean.NotesFileResponse;
import com.corex.responsebean.NotesListResponse;
import com.corex.responsebean.ScheduleResponse;
import com.corex.responsebean.StudentBatchResponse;
import com.corex.responsebean.StudentsInstituteResponse;
import com.corex.responsebean.WeeklyScheduleData;
import com.tranaction.login.login;
import com.tranaction.subject.SubjectTransaction;
import com.tranaction.syllabusplanner.SyllabusPlannerTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.notes.NotesTransaction;
import com.transaction.notification.NotificationTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.teacher.TeacherTransaction;

@Path("/corex")
@Produces(MediaType.APPLICATION_JSON)
public class Service extends MobileServiceBase{
	@GET
	@Path("/test")
	//@AuthorizeRole
	@Produces(MediaType.APPLICATION_JSON)
	public Response test() {
		int i = 1/0;
		return Response.ok().build();
	}

	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginBeanMobile loginBeanMobile) { 
			SessionBean sessionBean = new SessionBean();
			login login = new login();
			UserBean userBean = login.loginck(loginBeanMobile.getUsername(), loginBeanMobile.getPassword());
			if(userBean!=null){
				String tokenId = UUID.randomUUID().toString();
				sessionBean.setTimestamp(new java.util.Date());
				sessionBean.setToken(tokenId);
				sessionBean.setUserBean(userBean);
				Common.userMap.put(tokenId, sessionBean);
				DivisionTransactions divisionTransactions = new DivisionTransactions();
				HashMap<InstituteDataKey, List<InstituteDataKey>> division = divisionTransactions.getAllBatches(userBean);
				
				HashMap authBean = new HashMap();
				authBean.put("authtoken", tokenId);
				authBean.put("role", userBean.getRole());
				authBean.put("instituteData", division);
				return Response.ok(authBean).build();
			}else{
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	}
	
	@GET
	@Path("view/mobile/{yyyymmdd}")
	@AuthorizeRole
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getAllPlannedSyllabusForMobileMonth(@PathParam("yyyymmdd") String yyyymmdd,
			@QueryParam("division")List<Integer> classId,
			@QueryParam("subject")List<Integer> subId,
			@QueryParam("batchId")List<Integer> batchId,
			@QueryParam("teacher")List<Integer> teacherId,
			@QueryParam("view")String view) {
		SyllabusPlannerTransaction transaction = new SyllabusPlannerTransaction();
		List<com.mobile.bean.SyllabusBean> syllabusBeans = transaction.getSyllabusBeanForMobileDb(yyyymmdd, getRegId(), classId, subId, batchId, teacherId,view);
		return Response.ok(syllabusBeans).build();
	}
	
	@POST
	@Path("mobile/changeStatus/{yyyymmdd}")
	@AuthorizeRole
	public Response setPlannedSyallbusStatus(@PathParam("yyyymmdd") String yyyymmdd,
			@QueryParam("id")Long id,
			@QueryParam("classId")int classId,
			@QueryParam("subjectId")int subId,
			@QueryParam("batchId")int batchId,
			@QueryParam("teacherId")int teacherId,
			@QueryParam("status")String status) {
		SyllabusPlannerTransaction transaction = new SyllabusPlannerTransaction();
		boolean res = transaction.setPlannedSyllabusStatus(yyyymmdd, id, getRegId(), classId, subId, batchId, teacherId, status);
		return Response.ok(res).build();
	}
	
	@GET
	@Path("mobile/getFilterResult")
	public Response getFilteredResult(){
		SyllabusPlannerTransaction syllabusPlannerTransaction = new SyllabusPlannerTransaction();
		HashMap<String, List<SyllabusFilterBean>> resultMap = syllabusPlannerTransaction.getSyllabusFilter(getRegId());
		return Response.ok(resultMap).build();
	}

	@POST
	@Path("/getscheduledate/{instituteId}/{batch}/{startDate}/{endDate}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getScheduleByDate(
			@PathParam("instituteId") int instId,
			@PathParam("batch") int batch,
			@PathParam("startDate") long startDate,
			@PathParam("endDate") long endDate) {
		StudentTransaction studentTransaction = new StudentTransaction();
		Student student = studentTransaction.getStudentByStudentID(getRegId(), instId);
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<MonthlyScheduleServiceBean> scheduleList = new ArrayList<MonthlyScheduleServiceBean>();
		if(endDate == 0){
			scheduleList = scheduleTransaction.getMonthSchedule(batch, new Date(startDate), instId, student.getDiv_id());
		}else{
			scheduleList = scheduleTransaction.getMonthSchedule(batch, new Date(startDate),new Date(endDate), instId, student.getDiv_id());
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/getnoteslist")
	public Response getNotesList(NotesListRequest request) {
		//int resultPerPage = Integer.parseInt(ServiceMap.getSystemParam(Constants.APP_PAGINATION, Constants.RESULT_PER_PAGE));
		int resultPerPage =10;
		List<NotesListResponse> notesListResponseList=new ArrayList<NotesListResponse>();
		StudentTransaction studentTransaction=new StudentTransaction();
		Student student=studentTransaction.getclassStudent(request.getStudent_id(), request.getInst_id());
		NotesTransaction notesTransaction=new NotesTransaction();
		int total_pages=notesTransaction.getNotescount(student.getDiv_id(), request.getSub_id(), request.getInst_id(),request.getBatch_id());
		List<Notes> noteList=notesTransaction.getNotesPath(student.getDiv_id(), request.getSub_id(), request.getInst_id(), request.getPage_no(),request.getBatch_id());
		if(noteList!=null){
			if (noteList.size()>0) {
				for (int i = 0; i < noteList.size(); i++) {
					NotesListResponse notesListResponse=new NotesListResponse();
					notesListResponse.setDiv_id(noteList.get(i).getDivid());
					//notesListResponse.setInst_id(noteList.get(i).getClassid());
					notesListResponse.setNotes_id(noteList.get(i).getNotesid());
					notesListResponse.setNotes_name(noteList.get(i).getName());
					notesListResponse.setSub_id(noteList.get(i).getSubid());
					notesListResponse.setTotal_pages(total_pages);
					notesListResponseList.add(notesListResponse);
				}
			}
		}
		return Response.ok().entity(notesListResponseList).build();
	}

	@POST
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/getnotesfile")
	public Response getNotesFile(NotesFileRequest request) {
		NotesTransaction notesTransaction=new NotesTransaction();
		String filename=notesTransaction.getNotepathById(request.getNotes_id(),request.getInst_id(),request.getSub_id(),request.getDiv_id());
		String basePath=Constants.STORAGE_PATH;
		String storagePath = basePath+File.separator+request.getInst_id()+File.separator+"notes";
		String filepath=storagePath+File.separator+request.getSub_id()+File.separator+request.getDiv_id()+File.separator+filename;
		File file=new File(filepath);
		NotesFileResponse fileResponse=new NotesFileResponse();
		fileResponse.setFile(file);
		java.nio.file.Path path=file.toPath();
		byte[] docStream={};
		try {
			docStream = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return Response
		            .ok(docStream, MediaType.APPLICATION_OCTET_STREAM).build();
		//return Response.ok().entity(fileResponse).build();
	}

	@GET
	//@Consumes(MediaType.TEXT_PLAIN)
	@Produces("application/json")
	@Path("/getinstitutes")
	public Response getAllInstitutes(@QueryParam("student_id") String id) {
		List<StudentsInstituteResponse> studentsInstituteResponseList=new ArrayList<StudentsInstituteResponse>();
		int student_id=Integer.parseInt(id);
		StudentTransaction studentTransaction=new StudentTransaction();
		List<Student> studentList=studentTransaction.getStudent(student_id);
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> registerBeanList=registerTransaction.getclassNames(studentList);
		
		if(registerBeanList!=null){
			if(registerBeanList.size()>0){
				for (int i = 0; i < registerBeanList.size(); i++) {
					StudentsInstituteResponse studentsInstituteResponse=new StudentsInstituteResponse();
					studentsInstituteResponse.setInst_id(registerBeanList.get(i).getRegId());
					studentsInstituteResponse.setInst_name(registerBeanList.get(i).getClassName());
					studentsInstituteResponseList.add(studentsInstituteResponse);
				}
			}
		}
		return Response.ok().entity(studentsInstituteResponseList).build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/getstudentbatch")
	public Response getStudentBatchList(
			StudentBatchRequest request) {
		StudentTransaction studentTransaction=new StudentTransaction();
		Student student=studentTransaction.getclassStudent(request.getStudent_id(), request.getInst_id());
		BatchTransactions batchTransactions=new BatchTransactions();
		String batchids[]=student.getBatch_id().split(",");
		int counter=0;
		List<StudentBatchResponse> studentBatchResponseList=new ArrayList<StudentBatchResponse>();
		while(batchids.length>counter)
		{
			Batch batch=batchTransactions.getBatch(Integer.parseInt(batchids[counter]),request.getInst_id(),student.getDiv_id());
			StudentBatchResponse studentBatchResponse=new StudentBatchResponse();
			studentBatchResponse.setBatch_id(batch.getBatch_id());
			studentBatchResponse.setBatch_name(batch.getBatch_name());
			studentBatchResponseList.add(studentBatchResponse);
			counter++;	
		}
		return Response.ok().entity(studentBatchResponseList).build();
	}

	@GET
	//@Consumes("application/json")
	@Produces("application/json")
	@Path("/getstudenttodaysschedule")
	public Response getStudentTodaysSchedule(
			@QueryParam("student_id") String student_id) {
		StudentTransaction studentTransaction=new StudentTransaction();
		List<Student> list=studentTransaction.getStudent(Integer.parseInt(student_id));
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> beans= registerTransaction.getclassNames(list);
		NotificationTransaction notificationTransaction=new NotificationTransaction();
		List<Notification> notifications=new ArrayList<Notification>();
		for (int i = 0; i < list.size(); i++) {
			List<Notification> notificationsList=  notificationTransaction.getMessageforStudent(list.get(i));
			if(notificationsList!=null){
				for (int j = 0; j < notificationsList.size(); j++) {
					notifications.add(notificationsList.get(j));
				}
			}
		}
		ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
		List<Scheduledata> scheduledatas=scheduleTransaction.gettodaysSchedule(list);
		Map<String, List<Scheduledata>> map=new HashMap<String, List<Scheduledata>>();
		if(scheduledatas!=null){
			if(scheduledatas.size()>0){
			for (int i = 0; i < beans.size(); i++) {
				List<Scheduledata> data=new ArrayList<Scheduledata>();
				for (int j = 0; j < scheduledatas.size(); j++) {
					if(beans.get(i).getRegId()==scheduledatas.get(j).getInst_id()){
						scheduledatas.get(j).setInst_name(beans.get(i).getClassName());
						data.add(scheduledatas.get(j));
					}
				}
				if(data.size()>0){
				map.put(beans.get(i).getClassName(),data);
				}
			}
			}
		}
		return Response.ok().entity(map).build();
		
	}
}
