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

import com.classapp.db.Notes.Notes;
import com.classapp.db.schedule.Schedule;
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
import com.transaction.batch.BatchTransactions;
import com.transaction.notes.NotesTransaction;
import com.transaction.notification.NotificationTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.teacher.TeacherTransaction;

public class Service implements IService {
	@GET
	@Path("/test")
	public String test() {

		return "Service is On";
	}

	@Override
	@POST
	@Path("/login")
	public Response login(IRequest request) {
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setCode("000", "success");
		com.tranaction.login.login login = new login();
		LoginBeanMobile beanMobile = request.getLoginBeanMobile();
		UserBean userBean = new UserBean();
		login.loadBean(userBean, beanMobile);
		if(null == userBean.getUsername()){
			loginResponse.setCode("401", "unauthorised");
		}else{
		loginResponse.setUserBean(userBean);
		
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		HashMap<String, List> studentData = scheduleTransaction.getStudentData(userBean
				.getRegId());
		
		loginResponse.setStudentScheduleData(studentData);
		}
		return Response.ok().entity(loginResponse).build();
	}

	@Override
	@POST
	@Path("/getscheduledate")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getScheduleByDate(IRequest request) {
		ScheduleResponse response = new ScheduleResponse();
		List<com.classapp.schedule.Schedule> scheduleData = null;
		HashMap<String, List> studentData = null;

		Object dateObj =  request.getObject();
		String date = (String) dateObj;
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		LoginBeanMobile beanMobile = request.getLoginBeanMobile();
		UserBean userBean = new UserBean();
		com.tranaction.login.login login = new login();
		login.loadBean(userBean, beanMobile);
		
		if(null!=userBean.getRole()){
		if (userBean.getRole() == 3) {
			if (null != userBean) {
				scheduleData = scheduleTransaction.getScheduleForDate(
						userBean.getRegId(), date);
			}
			studentData = scheduleTransaction.getStudentData(userBean
					.getRegId());

			ScheduleBean scheduleBean = new ScheduleBean();
			//scheduleBean.setSchedule(scheduleData);
			scheduleBean.setStudentScheduleData(studentData);

			response.setCode("000", "success");
			response.setScheduleBean(scheduleBean);
		} else {
			response.setCode("002", "Invalid role");
		}
		}else{
			response.setCode("001", "Invalid credentials");
		}
		return Response.ok().entity(response).build();
	}
	
	@Override
	@POST
	//@Path("/data/{WeeklyScheduleBean}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	@Path("/getweeklyscheduledate")
	public Response getWeeklyScheduleByDate(WeeklyScheduleRequest weeklyschedulerequest) {
		System.out.println(weeklyschedulerequest);
		
		TeacherTransaction teacherTransaction=new TeacherTransaction();
		List<TeacherDetails> teacherList =teacherTransaction.getAllTeachersFromClass(weeklyschedulerequest.getInst_id());
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List<Subjects> subjectList= subjectTransaction.getAllClassSubjects(weeklyschedulerequest.getInst_id());
		StudentTransaction studentTransaction=new StudentTransaction();
		Student student=studentTransaction.getclassStudent(weeklyschedulerequest.getRegID(), weeklyschedulerequest.getInst_id());
		ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
		List<Schedule> scheduleList=new ArrayList<Schedule>();
			if(weeklyschedulerequest.getBatch_ID()==0){
				scheduleList=scheduleTransaction.getWeeklySchedule(new Date(weeklyschedulerequest.getDate()), weeklyschedulerequest.getInst_id(), student.getDiv_id());
			}else{
				scheduleList=scheduleTransaction.getWeeklySchedule(weeklyschedulerequest.getBatch_ID(), new Date(weeklyschedulerequest.getDate()), weeklyschedulerequest.getInst_id(), student.getDiv_id());
			}	
		BatchTransactions batchTransactions=new BatchTransactions();
		List<Batch> batchList= batchTransactions.getAllBatchesOfDivision(student.getDiv_id(), weeklyschedulerequest.getInst_id());
		DateFormat sdf = new SimpleDateFormat("kk:mm");
		DateFormat f2 = new SimpleDateFormat("h:mma");
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<WeeklyScheduleResponse> weeklyScheduleResponseList=new ArrayList<WeeklyScheduleResponse>();
		if(scheduleList!=null){
			if(scheduleList.size()>0){
				try{
				for (int i = 0; i < scheduleList.size(); i++) {
					WeeklyScheduleResponse weeklyScheduleResponse =new WeeklyScheduleResponse();
					java.util.Date start=sdf.parse(scheduleList.get(i).getStart_time().toString());
					java.util.Date end=sdf.parse(scheduleList.get(i).getEnd_time().toString());
					String starttime=f2.format(start).toUpperCase();
					String endtime=f2.format(end).toUpperCase();
					weeklyScheduleResponse.setScheduledate(new SimpleDateFormat("dd-MM-yyyy").format(scheduleList.get(i).getDate()));
					for (int j = 0; j < subjectList.size(); j++) {
						if(subjectList.get(j).getSubjectId().intValue()==scheduleList.get(i).getSub_id()){
							weeklyScheduleResponse.setSubject(subjectList.get(j).getSubjectName());
							break;
						}
					}
					
					for (int j = 0; j < teacherList.size(); j++) {
						if(teacherList.get(j).getTeacherId()==scheduleList.get(i).getTeacher_id()){
							//RegisterBean registerBean=registerTransaction.getregistereduser(teacherList.get(j).getUser_id());
							weeklyScheduleResponse.setTeacherName(teacherList.get(j).getTeacherBean().getFname()+" "+teacherList.get(j).getTeacherBean().getLname()+" "+teacherList.get(j).getSuffix());
							break;
						}
					}
					
					for (int j = 0; j < batchList.size(); j++) {
						if(batchList.get(j).getBatch_id()==scheduleList.get(i).getBatch_id()){
							//RegisterBean registerBean=registerTransaction.getregistereduser(teacherList.get(j).getUser_id());
							weeklyScheduleResponse.setBatchName(batchList.get(j).getBatch_name());
							break;
						}
					}
					weeklyScheduleResponse.setTime(starttime+":"+endtime);
					weeklyScheduleResponseList.add(weeklyScheduleResponse);
				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		return Response.ok().entity(weeklyScheduleResponseList).build();
	}

	@Override
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
					notesListResponse.setInst_id(noteList.get(i).getClassid());
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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
