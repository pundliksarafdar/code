package com.admin.misc;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.classapp.db.Feedbacks.Feedback;
import com.classapp.db.Notes.Notes;
import com.classapp.db.Teacher.Teacher;
import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.db.advertisement.*;
import com.classapp.db.batch.Batch;
import com.classapp.db.batch.BatchDetails;
import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.Exam;
import com.classapp.db.institutestats.InstituteStats;
import com.classapp.db.notificationpkg.Notification;
import com.classapp.db.question.Questionbank;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.Schedule.Schedule;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentData;
import com.classapp.db.student.StudentDetails;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Subjects;
import com.classapp.db.subject.Topics;
import com.classapp.logger.AppLogger;
import com.classapp.login.UserStatic;
import com.classapp.notification.GeneralNotification;
import com.classapp.notification.GeneralNotification.NOTIFICATION_KEYS;
import com.classapp.persistence.Constants;
import com.classapp.servicetable.ServiceMap;
import com.datalayer.exam.ParagraphQuestion;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.helper.BatchHelperBean;
import com.helper.TeacherHelperBean;
import com.mails.AllMail;
import com.service.beans.ParaQuestionBean;
import com.threadrunner.ReEvaluateThreadRunner;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.advertisetransaction.AdvertiseTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.feedback.feedbackTransaction;
import com.transaction.institutestats.InstituteStatTransaction;
import com.transaction.notes.NotesTransaction;
import com.transaction.notification.NotificationGlobalTransation;
import com.transaction.notification.NotificationTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.teacher.TeaherTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class ClassOwnerServlet extends HttpServlet{
	private static String STATUS = "status";
	private static String MESSAGE = "message";
	private StudentTransaction studentTransaction;
	private StudentData studentData;
	private TeacherTransaction teacherTransaction;
	private DivisionTransactions divisionTransactions;
	private BatchTransactions batchTransactions;
	@Override
	public void init() throws ServletException {
				studentData= new StudentData();
				studentTransaction= new StudentTransaction();
				teacherTransaction= new TeacherTransaction();
				divisionTransactions=new DivisionTransactions();
				batchTransactions= new BatchTransactions();
	};
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		PrintWriter printWriter = resp.getWriter();
		JsonObject respObject = new JsonObject();
		respObject.addProperty(STATUS, "error");
		
		String methodToCall = (String) req.getParameter("methodToCall");
		if("addBatch".equals(methodToCall)){
			Integer regId = null;
			String subjects=req.getParameter("subjectname");
			if(!req.getParameter("regId").equals("")){
				regId = Integer.parseInt(req.getParameter("regId"));
			}else{
				UserBean userBean = (UserBean) req.getSession().getAttribute("user");
				if(0 == userBean.getRole() || !"".equals(regId)){
					if(null == regId){
						regId = userBean.getRegId();
					}
				}else{
					regId = userBean.getRegId();
				}
			}
			String batchName = (String) req.getParameter("batchName");
			/*
			 * Task Id:11 Added a code to create division
			 */
			String divisionName=(String)req.getParameter("divisionName");	
			//divisionName="div2";	
			AppLogger.logger("divisionName:"+divisionName);
			Division division=new Division();
			division.setDivisionName(divisionName);
				
			Batch batch = new Batch();
			batch.setBatch_name(batchName);
			batch.setClass_id(regId);
			batch.setDiv_id(Integer.parseInt(divisionName));
			
			if(subjects.equals("")){
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "No subjects selected!");
			}
			int batchId=batchTransactions.getNextBatchID(regId, Integer.parseInt(divisionName));
			batch.setSub_id(subjects);
			batch.setBatch_id(batchId);
			if(batchTransactions.isBatchExist(batch)){
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "Batch already exists.");
			}else if(batchTransactions.addUpdateDb(batch)){
				BatchHelperBean batchHelperBean= new BatchHelperBean(regId);
				batchHelperBean.setBatchDetailsList();
				List<BatchDetails> batchList = new ArrayList<BatchDetails>();
				batchList = batchHelperBean.getBatchDetailsList();
				Gson gson=new Gson();
				String allbatches=gson.toJson(batchList);
				respObject.addProperty("allbatches", allbatches);
				respObject.addProperty(STATUS, "success");
				respObject.addProperty(MESSAGE, "Successfully added batch!");
			}else{
				respObject.addProperty(STATUS, "error");
				
			}
			//printWriter.write(respObject.toString());
		}else if(Constants.ADD_SUBJECT.equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String subjectName = (String) req.getParameter("subjectName");
			String combinationSub = (String) req.getParameter("combinationSub");
			String subIds = (String) req.getParameter("subIds");
			com.classapp.db.subject.Subject subject = new com.classapp.db.subject.Subject();
			/*subject.setRegId(regId);*/
			subject.setSubjectName(subjectName);
			subject.setInstitute_id(regId);
			SubjectTransaction subjectTransaction = new SubjectTransaction();
			if(subjectTransaction.addUpdateSubjectToDb(subject,regId,combinationSub,subIds)){
				List<Subjects> subjects=subjectTransaction.getAllClassSubjects(regId);
				Gson gson=new Gson();
				String json=gson.toJson(subjects);
				JsonElement jsonElement = gson.toJsonTree(subjects);
				respObject.add("subjects", jsonElement);
				respObject.addProperty(STATUS, "success");
			}else{
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, " Subject is already exist");
			}
			//printWriter.write(respObject.toString());
		}/*else if("deleteBatch".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String batchName = req.getParameter("batchName");
			Batch batch = new Batch();
			batch.setClass_id(regId);
			batch.setBatch_name(batchName);
			if(batchTransactions.deleteBatch(batch)){
				respObject.addProperty(STATUS, "success");
			}else{
				respObject.addProperty(STATUS, "error");
			}
			printWriter.write(respObject.toString());
		}*/else if(Constants.STUDENT_DETAILS.equals(methodToCall)){
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			String studentId=req.getParameter("studentId");
			RegisterTransaction registerTransaction=new RegisterTransaction();
			RegisterBean studentBean=registerTransaction.getregistereduser(Integer.parseInt(studentId));
			studentBean.setLoginPass("");
			StudentTransaction studentTransaction=new StudentTransaction();
			Student student = studentTransaction.getStudentByStudentID(Integer.parseInt(studentId), userBean.getRegId());
			List<Batch> batchList = batchTransactions.getBatchRelatedtoDivision(student.getDiv_id());
			Division division=divisionTransactions.getDidvisionByID(student.getDiv_id());
			String batcharray[] = student.getBatch_id().split(",");
			List<Batch> studentBatchList = new ArrayList<Batch>();
 			for (int i = 0; i < batcharray.length; i++) {
				for (int j = 0; j < batchList.size(); j++) {
					if(Integer.parseInt(batcharray[i]) == batchList.get(j).getBatch_id()){
						studentBatchList.add(batchList.get(j));
						break;
					}
				}
				
			}
			StudentDetails studentDetails=new StudentDetails();
			studentDetails.setStudentUserBean(studentBean);
			studentDetails.setStudent(student);
			studentDetails.setBatches(studentBatchList);
			studentDetails.setDivision(division);
			Gson gson = new Gson();
			JsonElement jsonElement = gson.toJsonTree(studentDetails);
			respObject.add("studentDetails", jsonElement);
			respObject.addProperty(STATUS, "success");
			
 		}else if (Constants.ADD_STUDENT.equals(methodToCall)) {
			Integer regId = null;
			if (!req.getParameter("regId").equals("")) {
				regId = Integer.parseInt(req.getParameter("regId"));
			} else {
				UserBean userBean = (UserBean) req.getSession().getAttribute(
						"user");
				if (0 == userBean.getRole() || !"".equals(regId)) {
					if (null == regId) {
						regId = userBean.getRegId();
					}
				} else {
					regId = userBean.getRegId();
				}
			}

			String studentLoginName = req.getParameter("studentLgName");
			String batchIds = req.getParameter("batchIds");
			String divisionId=req.getParameter("divisionId");
			//int divId= divisionTransactions.getDivisionIDByName(divisionName);
			int divId= Integer.parseInt(divisionId);
			Student student = validateStudent(studentLoginName, batchIds,divId,
					regId, printWriter);
			InstituteStatTransaction statTransaction=new InstituteStatTransaction();
			InstituteStats instituteStats=statTransaction.getStats(regId);
			if(instituteStats.getAvail_ids()>0){
			if (student != null) {
				
				if (studentTransaction.addUpdateDb(student)) {	
					statTransaction.increaseUsedStudentIds(regId);
					respObject.addProperty(STATUS, "success");
					respObject.addProperty(MESSAGE,
							"Successfully addded student.");
				} else {
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Student with login name "
							+ studentLoginName + " already exists in class!");
				}

				//printWriter.write(respObject.toString());
			}else{
				respObject.addProperty(MESSAGE,
						"Unable to find student with login name : "
								+ studentLoginName);
			}
			}else{
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "You dont have IDs to add,Please increase your limit!");
			}
		}  else if (Constants.FETCH_BATCHES.equals(methodToCall)) {
			Integer regId = null;
			if (!req.getParameter("regId").equals("")) {
				regId = Integer.parseInt(req.getParameter("regId"));
			} else {
				UserBean userBean = (UserBean) req.getSession().getAttribute(
						"user");
				if (0 == userBean.getRole() || !"".equals(regId)) {
					if (null == regId) {
						regId = userBean.getRegId();
					}
				} else {
					regId = userBean.getRegId();
				}
			}

			String divisionId = req.getParameter("divisionId");
			List<Batch> batches= batchTransactions.getAllBatchesOfDivision(Integer.parseInt(divisionId), regId); 
			String batchIds="";
			String batchNames="";
			
			for (Batch batch : batches) {
				if(batchIds.equals("")){
					batchIds=batch.getBatch_id()+"";
				}else{
					batchIds=batchIds+","+batch.getBatch_id()+"";
				}
				
				if(batchNames.equals("")){
					batchNames=batch.getBatch_name();
				}else{
					batchNames=batchNames+","+batch.getBatch_name();
				}
			}
			
			if (batches != null) {
				if (batches.size()>0) {
					respObject.addProperty(STATUS, "success");
					respObject.addProperty(MESSAGE,
							"Batches fetched successfully.");
					Gson gson=new Gson();
				JsonElement jsonElement=gson.toJsonTree(batches);
					respObject.add("batches", jsonElement);
					//respObject.addProperty("batchNames", batchNames);
				} else {
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "No batches found");
				}				
			}
			//printWriter.write(respObject.toString());
		}else if(Constants.UPDATE_STUDENT.equalsIgnoreCase(methodToCall)){
			Integer regId = null;
			if(!req.getParameter("regId").equals("")){
				regId = Integer.parseInt(req.getParameter("regId"));
			}else{
				UserBean userBean = (UserBean) req.getSession().getAttribute("user");
				if(0 == userBean.getRole() || !"".equals(regId)){
					if(null == regId){
						regId = userBean.getRegId();
					}
				}else{
					regId = userBean.getRegId();
				}
			}
			int studentId=Integer.parseInt(req.getParameter("studentId"));
			int div=Integer.parseInt(req.getParameter("div"));
			Student student=studentData.getStudentDetailsFromClass(studentId, regId);			
			String batchIds=req.getParameter("batchIds");			
			student.setBatch_id(batchIds);
			student.setDiv_id(div);
//			boolean validStudent=validateStudentBatch(student,batchIds,regId, printWriter);
			
				student.setBatch_id(batchIds);
				if(studentTransaction.updateStudentDb(student)){
					respObject.addProperty(STATUS, "success");
					respObject.addProperty(MESSAGE, "Successfully updated student.");
				}else{
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Error while updating the student with Id="+studentId+"!");
				}			
				//printWriter.write(respObject.toString());
			
		}else if("deleteStudent".equals(methodToCall)){
			Integer regId = null;
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(!req.getParameter("regId").equals("")){
				regId = Integer.parseInt(req.getParameter("regId"));
			}else{
				
				if(0 == userBean.getRole() || !"".equals(regId)){
					if(null == regId){
						regId = userBean.getRegId();
					}
				}else{
					regId = userBean.getRegId();
				}
			}
			
			int deletStudentId=Integer.parseInt(req.getParameter("studentId"));										
			
				
			/*if(studentTransaction.deleteStudent(deletStudentId, regId)){
					StudentMarksTransaction marksTransaction=new StudentMarksTransaction();
					marksTransaction.deleteStudentMarksrelatedtostudentID(regId, deletStudentId);
					InstituteStatTransaction instituteStatTransaction=new InstituteStatTransaction();
					instituteStatTransaction.increaseUsedStudentIds(regId);
					if("disabled".equals(userBean.getInst_status())){
						RegisterTransaction registerTransaction=new RegisterTransaction();
						InstituteStats instituteStats=instituteStatTransaction.getStats(regId);
						if( (instituteStats.getAlloc_ids()>=instituteStats.getUsed_ids()) && (instituteStats.getAlloc_memory()>=instituteStats.getUsed_memory()))
						{
							registerTransaction.updateInstituteStatus(regId, "enabled");
						}
					}
					respObject.addProperty(STATUS, "success");
					respObject.addProperty(MESSAGE, "Student Successfully deleted .");
				}else{
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Student does not exists in class!");
				}*/
				//printWriter.write(respObject.toString());
			}else if(Constants.UPDATE_TEACHER.equalsIgnoreCase(methodToCall)){
				Integer regId = null;
				if(!req.getParameter("regId").equals("")){
					regId = Integer.parseInt(req.getParameter("regId"));
				}else{
					UserBean userBean = (UserBean) req.getSession().getAttribute("user");
					if(0 == userBean.getRole() || !"".equals(regId)){
						if(null == regId){
							regId = userBean.getRegId();
						}
					}else{
						regId = userBean.getRegId();
					}
				}
				
				int teacherId=Integer.parseInt(req.getParameter("teacherId"));
				Teacher teacher=teacherTransaction.getTeacher(teacherId, regId);
				String sub_Ids=req.getParameter("subIds");			
				String suffix=req.getParameter("suffix");
				if(teacher!=null){
					teacher.setSub_ids(sub_Ids);
					teacher.setSuffix(suffix);
					if(teacherTransaction.updateTeacher(teacher)){
						respObject.addProperty(STATUS, "success");
						respObject.addProperty(MESSAGE, "Successfully updated teacher.");
					}else{
						respObject.addProperty(STATUS, "error");
						respObject.addProperty(MESSAGE, "Error while updating the teacher with Id="+teacherId+"!");
					}
					
					//printWriter.write(respObject.toString());
				}
			}else if(Constants.DELETE_TEACHER.equals(methodToCall)){
				Integer regId = null;
				if(!req.getParameter("regId").equals("")){
					regId = Integer.parseInt(req.getParameter("regId"));
				}else{
					UserBean userBean = (UserBean) req.getSession().getAttribute("user");
					if(0 == userBean.getRole() || !"".equals(regId)){
						if(null == regId){
							regId = userBean.getRegId();
						}
					}else{
						regId = userBean.getRegId();
					}
				}
				
				int deletTeacherId=Integer.parseInt(req.getParameter("teacherId"));										
				
					if(teacherTransaction.deleteTeacher(deletTeacherId, regId)){
						respObject.addProperty(STATUS, "success");
						respObject.addProperty(MESSAGE, "Teacher Successfully deleted .");
					}else{
						respObject.addProperty(STATUS, "error");
						respObject.addProperty(MESSAGE, "Teacher does not exists in class!");
					}
					
					//printWriter.write(respObject.toString());
				}else if(Constants.SEARCH_TEACHER.equals(methodToCall)){
					UserBean userBean = (UserBean) req.getSession().getAttribute("user");
					String teacherLoginName=req.getParameter("teacherLgName");
					if(teacherLoginName.equals("")){
						respObject.addProperty(STATUS, "error");
						respObject.addProperty(MESSAGE, "Login name can not be blank! Please enter login name of teacher");											
					}else{
					List<TeacherDetails> teachers=(List<TeacherDetails>) req.getSession().getAttribute(Constants.TEACHER_LIST);
					SubjectTransaction subjectTransaction=new SubjectTransaction();
					List<Subjects> list=subjectTransaction.getAllClassSubjects(userBean.getRegId());
					req.getSession().setAttribute("teacherSearchResultSubjects", null);
					String allsubjectname="";
					String allsubjectIds="";
					if(list!=null){
						for (int i = 0; i < list.size(); i++) {
							if(i==0){
								allsubjectname=list.get(i).getSubjectName();
								allsubjectIds=list.get(i).getSubjectId()+"";
							}else{
								allsubjectname=allsubjectname+","+list.get(i).getSubjectName();
								allsubjectIds=allsubjectIds+","+list.get(i).getSubjectId()+"";
							}
						}
					}
					boolean found=false;
					for (TeacherDetails teacher :teachers) {
						if(teacher.getTeacherBean().getLoginName().equals(teacherLoginName)){
							req.getSession().setAttribute("teacherSearchResult", teacher);
							req.getSession().setAttribute("teacherSearchResultSubjects", teacher);
							respObject.addProperty("teacherId", teacher.getTeacherId());
							respObject.addProperty("teacherFname", teacher.getTeacherBean().getFname());
							respObject.addProperty("teacherLname", teacher.getTeacherBean().getLname());
							respObject.addProperty("teacherssubjectname", teacher.getSubjectNames());
							respObject.addProperty("teacherssubjectIds", teacher.getSubjectIds());
							respObject.addProperty("teachersloginname", teacher.getTeacherBean().getLoginName());
							respObject.addProperty("allsubjectname", allsubjectname);
							respObject.addProperty("allsubjectIds", allsubjectIds);
							respObject.addProperty("teacherssuffix", teacher.getSuffix());
							found=true;
							break;
						}
					}
					
					if(!found){
						respObject.addProperty(STATUS, "error");
						respObject.addProperty(MESSAGE, "Teacher with login name "+teacherLoginName+" does not exists in class!");
					}else{
						respObject.addProperty(STATUS, "success");				
					}
					}
				//	printWriter.write(respObject.toString());
					
				}else if("addTeacher".equals(methodToCall)){
			Integer regId = null;
			String subjects=req.getParameter("subjects");
			String suffix=req.getParameter("suffix");
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			int teacherID = Integer.parseInt(req.getParameter("teacherID"));
			TeaherTransaction teaherTransaction=new TeaherTransaction();
			String stat=teaherTransaction.addTeacher(teacherID,regId,subjects,suffix);
			if("added".equals(stat))
			{
				respObject.addProperty(STATUS, "success");
			}else if("exists".equals(stat)){
				respObject.addProperty(MESSAGE, " Teacher is already added");
				respObject.addProperty(STATUS, "error");
			}else if("false".equals(stat)){
				respObject.addProperty(MESSAGE, "Invalid Teacher ID");
				respObject.addProperty(STATUS, "error");
			}
		}else if("fetchBatchSubject".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
				
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String batchID=req.getParameter("batchName");
			String batchdivision=req.getParameter("batchdivision");
			String institute=req.getParameter("institute");
			BatchTransactions batchTransactions=new BatchTransactions();
			List<Subjects> Batchsubjects=null;
			if(userBean.getRole()==3){
				Student student=(Student) studentTransaction.getclassStudent(userBean.getRegId(), Integer.parseInt(institute));
				Batchsubjects=(List<Subjects>)batchTransactions.getBatcheSubject(batchID,Integer.parseInt(institute),student.getDiv_id());
			}else{
				Batchsubjects=(List<Subjects>)batchTransactions.getBatcheSubject(batchID,userBean.getRegId(),Integer.parseInt(batchdivision));
			}
			
			if(Batchsubjects!=null){
			String subjectnames="";
			String subjectids="";
			for(int i=0;i<Batchsubjects.size();i++)
			{
				if(i==0)
				{
					subjectnames=Batchsubjects.get(i).getSubjectName();
					subjectids=Batchsubjects.get(i).getSubjectId()+"";
				}else{
					subjectnames=subjectnames+","+Batchsubjects.get(i).getSubjectName();
					subjectids=subjectids+","+Batchsubjects.get(i).getSubjectId();
					
				}
				
				
			}
			
			respObject.addProperty("Batchsubjects", subjectnames);
			respObject.addProperty("BatchsubjectsIds", subjectids);
			respObject.addProperty("subjectstatus", "");
			}else{
			respObject.addProperty("subjectstatus", "notavailable");
			}
			respObject.addProperty(STATUS, "success");
			respObject.remove(STATUS);
			/*String teacherID = req.getParameter("teacherID");
			TeaherTransaction teaherTransaction=new TeaherTransaction();
			String stat=teaherTransaction.addTeacher(teacherID,regId);
			if("added".equals(stat))
			{
				respObject.addProperty(STATUS, "success");
			}else if("exists".equals(stat)){
				respObject.addProperty(MESSAGE, " Teacher is already exist");
				respObject.addProperty(STATUS, "error");
			}else if("false".equals(stat)){
				respObject.addProperty(MESSAGE, "Invalid Teacher ID");
				respObject.addProperty(STATUS, "error");
			}*/
			
			
		}else if("fetchSubjectTeacher".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String subname=req.getParameter("subname");
			TeaherTransaction teaherTransaction=new TeaherTransaction();
			List<Teacher> list=teaherTransaction.getSubjectTeacher(subname,regId);
			List teacheridlist=new ArrayList();
			int index=0;
			while (list.size()>index) {
				
				teacheridlist.add(list.get(index).getUser_id());
				index++;	
			}
			String firstname="";
			String lastname="";
			String teacherid="";
			String suffixs="";
			if(list!=null && list.size()>0)
			{
			RegisterTransaction registerTransaction=new RegisterTransaction();
			List<RegisterBean> teacherNames=registerTransaction.getTeacherName(teacheridlist);
			int i=0;
			for ( i = 0; i < list.size(); i++) {
				if(i==0){
					if(list.get(i).getSuffix()==null){
						suffixs="";
					}else{
					suffixs=list.get(i).getSuffix();
					}
				}else{
					if(list.get(i).getSuffix()==null){
						suffixs=suffixs+","+"";
					}else{
					suffixs=suffixs+","+list.get(i).getSuffix();
					}
				}
				
			}
			
			for(i=0;i<teacherNames.size();i++)
			{
				if(i==0)
				{
				firstname=teacherNames.get(i).getFname();
				}else{
					firstname=firstname+","+teacherNames.get(i).getFname();
				}
			}
			
			for(i=0;i<teacherNames.size();i++)
			{
				if(i==0)
				{
					lastname=teacherNames.get(i).getLname();
				}else{
				lastname=lastname+","+teacherNames.get(i).getLname();
				}
			}
			
			for(i=0;i<teacherNames.size();i++)
			{
				if(i==0)
				{
					teacherid=teacherNames.get(i).getRegId()+"";
				}else{
				teacherid=teacherid+","+teacherNames.get(i).getRegId();
				}
			}
		}
			respObject.addProperty("firstname", firstname);
			respObject.addProperty("lastname", lastname);
			respObject.addProperty("teacherid", teacherid);
			respObject.addProperty("suffix", suffixs);
			respObject.addProperty(STATUS, "success");
			
			
		}else if("addLectures".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String subjects= req.getParameter("subjects");
			String teachers=req.getParameter("teachers");
			String starttimes=req.getParameter("starttimes");
			String endtimes=req.getParameter("endtimes");
			String dates=req.getParameter("dates");
			String batchID=req.getParameter("batchID");
			String batchdivision=req.getParameter("batchdivision");
			if(batchID==null)
			{
				batchID=req.getParameter("batchname");
			}
			SimpleDateFormat dateFormat=new SimpleDateFormat();
			
			String subject[]=subjects.split(",");
			List subList=new ArrayList();
			int i=0;
			for(i=0;i<subject.length;i++)
			{
				if(!subject[i].equals("undefined"))
				{
				subList.add(subject[i]);
				}
			}
			
			String teacher[]=teachers.split(",");
			List teacherList=new ArrayList();
			for(i=0;i<teacher.length;i++)
			{
				if(!teacher[i].equals("undefined"))
				{
				teacherList.add(teacher[i]);
				}
			}
			
			List stList =new ArrayList();
			String ststring[]=starttimes.split(",");
			for(i=0;i<ststring.length;i++)
			{
				if(!ststring[i].equals("undefined"))
				{
			String stimes[]=ststring[i].split(" ");
			String stime[]=stimes[0].split(":");
			String starttime="";
			Time stTime;
			if(stimes[1].equals("PM") && !stime[0].equals("12"))
			{
				starttime=(Integer.parseInt(stime[0])+12)+"";
				stTime=new Time(Integer.parseInt(starttime), Integer.parseInt(stime[1]), 00);
			}else if(stimes[1].equals("AM") && stime[0].equals("12")){
				starttime=00+"";
				stTime=new Time(Integer.parseInt(starttime), Integer.parseInt(stime[1]), 00);
			}			
			else{
				stTime=new Time(Integer.parseInt(stime[0]), Integer.parseInt(stime[1]), 00);
			}
			stList.add(stTime);
				}
			}
			
			List edList=new ArrayList();
			String endString[]=endtimes.split(",");
			for(i=0;i<endString.length;i++)
			{
				if(!endString[i].equals("undefined"))
				{
			String etimes[]=endString[i].split(" ");
			String etime[]=etimes[0].split(":");; 
			String endtime="";
			Time edTime;
			if(etimes[1].equals("PM") && !etime[0].equals("12"))
			{				
				endtime=(Integer.parseInt(etime[0])+12)+"";
				edTime=new Time(Integer.parseInt(endtime), Integer.parseInt(etime[1]), 00);
			}else if(etimes[1].equals("AM") && etime[0].equals("12")){
				
				endtime=00+"";
				edTime=new Time(Integer.parseInt(endtime), Integer.parseInt(etime[1]), 00);
			}else{
				
				edTime=new Time(Integer.parseInt(etime[0]), Integer.parseInt(etime[1]), 00);
			}
			edList.add(edTime);
				}
			}
			
			List dateList=new ArrayList();
			String dateString[]=dates.split(",");
			for(i=0;i<dateString.length;i++)
			{
				if(!dateString[i].equals("undefined"))
				{
			String[] dat=dateString[i].split("/");
			Date date=new Date(Integer.parseInt(dat[2])-1900, Integer.parseInt(dat[1])-1,Integer.parseInt(dat[0]));
			dateList.add(date);
				}
			}
			String teacherbusy="";
			String lectureexists="";
			if(validatetime(stList, edList,dateList)){
			ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
			String exists=scheduleTransaction.isExistsLecture(regId+"", batchID, subList, teacherList, stList, edList, dateList,Integer.parseInt(batchdivision));
			
			if(exists.equals(""))
			{
			scheduleTransaction.addLecture(regId+"", batchID, subList, teacherList, stList, edList, dateList,Integer.parseInt(batchdivision));
			Batch batch=batchTransactions.getBatch(Integer.parseInt(batchID),userBean.getRegId(),Integer.parseInt(batchdivision));
			NotificationGlobalTransation notificationGlobalTransation=new NotificationGlobalTransation();
			notificationGlobalTransation.sendAddLectureNotification(batch.getBatch_name(),batchID,Integer.parseInt(batchdivision),regId);
			}else{
				String error[]= exists.split(",");
				for(i=0;i<error.length;i++)
				{
					if(error[i].contains("teacher"))
					{
						if(teacherbusy=="")
						{
						teacherbusy=error[i];
						}else{
							teacherbusy=teacherbusy+","+error[i];
						}
					}
					if(error[i].contains("lecture"))
					{
						if(lectureexists==""){
							lectureexists=error[i];
						}else{
							lectureexists=lectureexists+","+error[i];
						}
					}
				}
				
			}
			respObject.addProperty("time", "");
			}else{
				respObject.addProperty("time", "timeoverlapped");
			}
			respObject.addProperty("overlappedtimeids", overlappedtimeids);
			respObject.addProperty("teacher", teacherbusy);
			respObject.addProperty("lecture", lectureexists);
			
			respObject.addProperty(STATUS, "success");	
			
		}else if("addnotes".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		String path=	req.getParameter("notes");
		File file=new File(path);
		 BufferedReader br = new BufferedReader(new FileReader(path));
		 String line = null;
		 while ((line = br.readLine()) != null) {
		   AppLogger.logger(line);
		 }
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			
			
		}
		else if("addclass".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			
			String classname=	req.getParameter("classname");
			String stream=	req.getParameter("stream");
			Division division=new Division();
			division.setDivisionName(classname);
			division.setStream(stream);
			division.setInstitute_id(regId);
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			boolean status=divisionTransactions.addDivision(division, regId);
			if(status)
			{
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "Class already exist");
			}else{
				List<Division>	list=divisionTransactions.getAllDivisions(userBean.getRegId());
				Gson gson=new Gson();
				String allclasses=gson.toJson(list);
				JsonElement jsonElement = gson.toJsonTree(list);
				respObject.add("allclasses", jsonElement);
				respObject.addProperty(STATUS, "success");
			}
		}
		else if("getschedule".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
				
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			int studentclass=0;
			if(userBean.getRole()==3){
				studentclass=Integer.parseInt(req.getParameter("classid"));
			}
			String batchname=	req.getParameter("batchname");
			String batchdivision=req.getParameter("batchdivision");
			String date=req.getParameter("date");
			String dateString[]=date.split("/");
			Date date2=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[1])-1,Integer.parseInt( dateString[0]));
			ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
			List<Schedule> list=null;
			if(userBean.getRole()==3){
			StudentTransaction studentTransaction=new StudentTransaction();
			Student student=studentTransaction.getclassStudent(userBean.getRegId(), studentclass);
			list=scheduleTransaction.getSchedule(Integer.parseInt(batchname),date2,studentclass,student.getDiv_id());
			}else{
			list=scheduleTransaction.getSchedule(Integer.parseInt(batchname),date2,userBean.getRegId(),Integer.parseInt(batchdivision));
			}
			SubjectTransaction subjectTransaction=new SubjectTransaction();
			List<String> subjectList= subjectTransaction.getScheduleSubject(list);
			RegisterTransaction registerTransaction=new RegisterTransaction();
			List<RegisterBean> teacherlist=registerTransaction.getScheduleTeacher(list);
			TeacherTransaction teacherTransaction=new TeacherTransaction();
			List<String> prefixs=new ArrayList<String>();
			if(userBean.getRole()==3){
				prefixs=teacherTransaction.getTeachersPrefix(list, studentclass);
			}else{
				prefixs=teacherTransaction.getTeachersPrefix(list, regId);
			}
			
			String subjects="";
			String tfirstname="";
			String tlastname="";
			String starttime="";
			String endtime="";
			String dates="";
			String prefix="";
			
			int counter=0;
			
			while (prefixs.size()>counter) {
				if (counter==0) {
					if(prefixs.get(counter)!=null)
					{
					prefix=prefixs.get(counter);
					}
				}else{
					prefix=prefix+","+prefixs.get(counter);
				}
				counter++;
			}
			counter=0;
			while(counter<list.size())
			{
				int starthour=list.get(counter).getStart_time().getHours();
				int startminute=list.get(counter).getStart_time().getMinutes();
				int endthour=list.get(counter).getEnd_time().getHours();
				int endtminute=list.get(counter).getEnd_time().getMinutes();
				String formattedstarttime=getFormattedTime(starthour, startminute);
				String formattedendtime=getFormattedTime(endthour, endtminute);
				if(counter==0)
				{
					subjects=subjectList.get(counter);
					tfirstname=teacherlist.get(counter).getFname();
					tlastname=teacherlist.get(counter).getLname();
					starttime=formattedstarttime;
					endtime=formattedendtime;
					dates=new SimpleDateFormat("dd-MM-yyyy").format(list.get(counter).getDate());
				}else{
					subjects=subjects+","+subjectList.get(counter);
					tfirstname=tfirstname+","+teacherlist.get(counter).getFname();
					tlastname=tlastname+","+teacherlist.get(counter).getLname();
					starttime=starttime+","+formattedstarttime;
					endtime=endtime+","+formattedendtime;
					dates=dates+","+new SimpleDateFormat("dd-MM-yyyy").format(list.get(counter).getDate());
					
				}
				counter++;
				
			}
			
			respObject.addProperty("subjects", subjects);
			respObject.addProperty("firstname", tfirstname);
			respObject.addProperty("lastname", tlastname);
			respObject.addProperty("starttime", starttime);
			respObject.addProperty("endtime", endtime);
			respObject.addProperty("dates", dates);
			respObject.addProperty("prefix", prefix);
			respObject.addProperty(STATUS, "success");
		}else if("geteditschedule".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String batchID=req.getParameter("batchname");
			String batchdivision=req.getParameter("batchdivision");
			BatchTransactions batchTransactions=new BatchTransactions();
			List<Subjects> Batchsubjects=(List<Subjects>)batchTransactions.getBatcheSubject(batchID,userBean.getRegId(),Integer.parseInt(batchdivision));
			String subjectnames="";
			String subjectids="";
			for(int i=0;i<Batchsubjects.size();i++)
			{
				if(i==0)
				{
					subjectnames=Batchsubjects.get(i).getSubjectName();
					subjectids=Batchsubjects.get(i).getSubjectId()+"";
				}else{
					subjectnames=subjectnames+","+Batchsubjects.get(i).getSubjectName();
					subjectids=subjectids+","+Batchsubjects.get(i).getSubjectId();
					
				}
				
				
			}
		
			
			String batchname=	req.getParameter("batchname");
			String date=req.getParameter("date");
			String dateString[]=date.split("/");
			Date date2=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[1])-1,Integer.parseInt( dateString[0]));
			ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
			List<Schedule> list=scheduleTransaction.getSchedule(Integer.parseInt(batchname),date2,userBean.getRegId(),Integer.parseInt(batchdivision));
			SubjectTransaction subjectTransaction=new SubjectTransaction();
			List<String> subjectList= subjectTransaction.getScheduleSubject(list);
			RegisterTransaction registerTransaction=new RegisterTransaction();
			List<RegisterBean> teacherlist=registerTransaction.getScheduleTeacher(list);
			TeaherTransaction teaherTransaction=new TeaherTransaction();
			List<List<RegisterBean>> teacherlists=teaherTransaction.getScheduleTeacher(list, regId);
			List<List<String>> suffix=teaherTransaction.getScheduleTeacherSuffix(list, regId);
			String subjects="";
			String tfirstname="";
			String tlastname="";
			String starttime="";
			String endtime="";
			String dates="";
			String teacherids="";
			String TeacherFirstNames="";
			String TeacherlastNames="";
			String Teacherids="";
			String allFirstnames="";
			String alllastnames="";
			String allIds="";
			String scheduleids="";
			String teacherssuffix="";
			String allSuffix="";
			int counter=0;
			while(counter<list.size())
			{
			int innercounter=0;
			while(teacherlists.get(counter).size()>innercounter)
			{
				
				if(innercounter==0)
				{
					TeacherFirstNames=teacherlists.get(counter).get(innercounter).getFname();
					TeacherlastNames=teacherlists.get(counter).get(innercounter).getLname();
					Teacherids=teacherlists.get(counter).get(innercounter).getRegId()+"";
					teacherssuffix=suffix.get(counter).get(innercounter);
				}else{
					TeacherFirstNames=TeacherFirstNames+","+teacherlists.get(counter).get(innercounter).getFname();
					TeacherlastNames=TeacherlastNames+","+teacherlists.get(counter).get(innercounter).getLname();
					Teacherids=Teacherids+","+teacherlists.get(counter).get(innercounter).getRegId();
					teacherssuffix=teacherssuffix+","+suffix.get(counter).get(innercounter);
				}
				innercounter++;
			}
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			int starthour=list.get(counter).getStart_time().getHours();
			int startminute=list.get(counter).getStart_time().getMinutes();
			int endthour=list.get(counter).getEnd_time().getHours();
			int endtminute=list.get(counter).getEnd_time().getMinutes();
			String formattedstarttime=getFormattedTime(starthour, startminute);
			String formattedendtime=getFormattedTime(endthour, endtminute);
				if(counter==0)
				{
					subjects=subjectList.get(counter);
					tfirstname=teacherlist.get(counter).getFname();
					tlastname=teacherlist.get(counter).getLname();
					starttime=formattedstarttime;
					endtime=formattedendtime;
					dates=sdf.format(list.get(counter).getDate())+"";
					teacherids=list.get(counter).getTeacher_id()+"";
					allFirstnames=TeacherFirstNames;
					alllastnames=TeacherlastNames;
					allIds=Teacherids;
					scheduleids=list.get(counter).getSchedule_id()+"";
					allSuffix=teacherssuffix;
				}else{
					subjects=subjects+","+subjectList.get(counter);
					tfirstname=tfirstname+","+teacherlist.get(counter).getFname();
					tlastname=tlastname+","+teacherlist.get(counter).getLname();
					starttime=starttime+","+formattedstarttime;
					endtime=endtime+","+formattedendtime;
					dates=dates+","+sdf.format(list.get(counter).getDate());
					teacherids=teacherids+","+list.get(counter).getTeacher_id()+"";
					allFirstnames=allFirstnames+"/"+TeacherFirstNames;
					alllastnames=alllastnames+"/"+TeacherlastNames;
					allIds=allIds+"/"+Teacherids;
					scheduleids=scheduleids+","+list.get(counter).getSchedule_id()+"";
					allSuffix=allSuffix+"/"+teacherssuffix;
				}
				
				counter++;
				
			}
			respObject.addProperty("scheduleids",scheduleids);
			respObject.addProperty("subjects", subjects);
			respObject.addProperty("firstname", tfirstname);
			respObject.addProperty("lastname", tlastname);
			respObject.addProperty("starttime", starttime);
			respObject.addProperty("endtime", endtime);
			respObject.addProperty("dates", dates);
			respObject.addProperty("teacherids", teacherids);
			respObject.addProperty("allteachersfirstname", allFirstnames);
			respObject.addProperty("allteacherlastname", alllastnames);
			respObject.addProperty("allteacherids", allIds);
			respObject.addProperty("allSuffix", allSuffix);
			respObject.addProperty(STATUS, "success");
		
			respObject.addProperty("Batchsubjects", subjectnames);
			respObject.addProperty("BatchsubjectsIds", subjectids);
			respObject.addProperty(STATUS, "success");
			respObject.remove(STATUS);
		
		}else if("updateLectures".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String subjects= req.getParameter("subjects");
			String teachers=req.getParameter("teachers");
			String starttimes=req.getParameter("starttimes");
			String endtimes=req.getParameter("endtimes");
			String dates=req.getParameter("dates");
			String batchID=req.getParameter("batchID");
			String schrduleid=req.getParameter("schrduleid");
			String batchdivision=req.getParameter("batchdivision");
			if(batchID==null)
			{
				batchID=req.getParameter("batchname");
			}
			SimpleDateFormat dateFormat=new SimpleDateFormat();
			
			String scheduleids[]=schrduleid.split(",");
			List scheduleidsList=new ArrayList();
			int i=0;
			for(i=0;i<scheduleids.length;i++)
			{
				scheduleidsList.add(scheduleids[i]);
			}
			
			String subject[]=subjects.split(",");
			List subList=new ArrayList();
			i=0;
			for(i=0;i<subject.length;i++)
			{
				subList.add(subject[i]);
			}
			
			String teacher[]=teachers.split(",");
			List teacherList=new ArrayList();
			for(i=0;i<teacher.length;i++)
			{
				teacherList.add(teacher[i]);
			}
			
			List stList =new ArrayList();
			String ststring[]=starttimes.split(",");
			for(i=0;i<ststring.length;i++)
			{
			String stimes[]=ststring[i].split(" ");
			String stime[]=stimes[0].split(":");
			String starttime="";
			Time stTime;
			if(stimes[1].equals("PM") && !stime[0].equals("12"))
			{
				starttime=(Integer.parseInt(stime[0])+12)+"";
				stTime=new Time(Integer.parseInt(starttime), Integer.parseInt(stime[1]), 00);
			}else if(stimes[1].equals("AM") && stime[0].equals("12")){
				starttime=00+"";
				stTime=new Time(Integer.parseInt(starttime), Integer.parseInt(stime[1]), 00);
			}			
			else{
				stTime=new Time(Integer.parseInt(stime[0]), Integer.parseInt(stime[1]), 00);
			}
			stList.add(stTime);
			}
			
			List edList=new ArrayList();
			String endString[]=endtimes.split(",");
			for(i=0;i<endString.length;i++)
			{
			String etimes[]=endString[i].split(" ");
			String etime[]=etimes[0].split(":");; 
			String endtime="";
			Time edTime;
			if(etimes[1].equals("PM") && !etime[0].equals("12"))
			{				
				endtime=(Integer.parseInt(etime[0])+12)+"";
				edTime=new Time(Integer.parseInt(endtime), Integer.parseInt(etime[1]), 00);
			}else if(etimes[1].equals("AM") && etime[0].equals("12")){
				
				endtime=00+"";
				edTime=new Time(Integer.parseInt(endtime), Integer.parseInt(etime[1]), 00);
			}else{
				
				edTime=new Time(Integer.parseInt(etime[0]), Integer.parseInt(etime[1]), 00);
			}
			edList.add(edTime);
			}
			
			List dateList=new ArrayList();
			String dateString[]=dates.split(",");
			for(i=0;i<dateString.length;i++)
			{
			String[] dat=dateString[i].split("/");
			Date date=new Date(Integer.parseInt(dat[2])-1900, Integer.parseInt(dat[1])-1,Integer.parseInt(dat[0]));
			dateList.add(date);
			}
			String teacherbusy="";
			String lectureexists="";
			if(validatetime(stList, edList, dateList)){
			ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
			String exists=scheduleTransaction.isTeacherUnavailable(regId+"", batchID, subList, teacherList, stList, edList, dateList,scheduleidsList,Integer.parseInt(batchdivision));
			
			if(exists.equals(""))
			{
			scheduleTransaction.updateLecture(regId+"", batchID, subList, teacherList, stList, edList, dateList,scheduleidsList,Integer.parseInt(batchdivision));
			BatchTransactions batchTransactions=new BatchTransactions();
			Batch batch=batchTransactions.getBatch(Integer.parseInt(batchID),userBean.getRegId(),Integer.parseInt(batchdivision));
			NotificationGlobalTransation notificationGlobalTransation=new NotificationGlobalTransation();
			notificationGlobalTransation.sendUpdateLectureNotification(batch.getBatch_name(),batchID,Integer.parseInt(batchdivision),regId);
			}else{
				String error[]= exists.split(",");
				for(i=0;i<error.length;i++)
				{
					if(error[i].contains("teacher"))
					{
						if(teacherbusy=="")
						{
						teacherbusy=error[i];
						}else{
							teacherbusy=teacherbusy+","+error[i];
						}
					}
					if(error[i].contains("lecture"))
					{
						if(lectureexists==""){
							lectureexists=error[i];
						}else{
							lectureexists=lectureexists+","+error[i];
						}
					}
				}
				
			}
			
			respObject.addProperty("teacher", teacherbusy);
			respObject.addProperty("lecture", lectureexists);
			respObject.addProperty("time", "");
			}else{
				respObject.addProperty("time", "time");
			}
			respObject.addProperty("overlappedtimeids", overlappedtimeids);
			respObject.addProperty("teacher", teacherbusy);
			respObject.addProperty("lecture", lectureexists);
			respObject.addProperty(STATUS, "success");	
			
		}
		else if(Constants.SEARCH_BATCH.equals(methodToCall)){
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			String batchName=req.getParameter("batchName");
			String divID=req.getParameter("divID");
			if(batchName.equals("")){
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "batch name can not be blank! Please enter batch name of Student. ");											
			}else{
			List<BatchDetails> batches=(List<BatchDetails>) req.getSession().getAttribute(com.config.Constants.BATCHES_LIST);
			SubjectTransaction subjectTransaction=new SubjectTransaction();
			List<Subjects> list=subjectTransaction.getAllClassSubjects(userBean.getRegId());
			
			String allsubjectname="";
			String allsubjectIds="";
			if(list!=null){
				for (int i = 0; i < list.size(); i++) {
					if(i==0){
						allsubjectname=list.get(i).getSubjectName();
						allsubjectIds=list.get(i).getSubjectId()+"";
					}else{
						allsubjectname=allsubjectname+","+list.get(i).getSubjectName();
						allsubjectIds=allsubjectIds+","+list.get(i).getSubjectId()+"";
					}
				}
			}
			req.getSession().setAttribute("batchSearchResult", null);
			boolean found=false;
			for (BatchDetails batch :batches) {
				if(divID!="" && divID!=null){
				if(batch.getBatch().getBatch_name().equalsIgnoreCase(batchName) && batch.getBatch().getDiv_id()==Integer.parseInt(divID)){
					
					req.getSession().setAttribute("batchSearchResult", batch);
					req.getSession().setAttribute("batchSearchResultBatch", batch);
					respObject.addProperty("batchId", batch.getBatch().getBatch_id());
					respObject.addProperty("batchName", batch.getBatch().getBatch_name());
					respObject.addProperty("batchdivisionname", batch.getDivision().getDivisionName());
					respObject.addProperty("batchdivisionID", batch.getDivision().getDivId());
					respObject.addProperty("batchdivisionstream", batch.getDivision().getStream());
					respObject.addProperty("batchsubjectIds", batch.getSubjectIds());
					respObject.addProperty("batchsubjectnames", batch.getSubjectNames());
					respObject.addProperty("allsubjectname", allsubjectname);
					respObject.addProperty("allsubjectIds", allsubjectIds);
					found=true;
					break;
				}
				}else{
					if(batch.getBatch().getBatch_name().equalsIgnoreCase(batchName)){
						
						req.getSession().setAttribute("batchSearchResult", batch);
						req.getSession().setAttribute("batchSearchResultBatch", batch);
						respObject.addProperty("batchId", batch.getBatch().getBatch_id());
						respObject.addProperty("batchName", batch.getBatch().getBatch_name());
						respObject.addProperty("batchdivisionname", batch.getDivision().getDivisionName());
						respObject.addProperty("batchdivisionID", batch.getDivision().getDivId());
						respObject.addProperty("batchdivisionstream", batch.getDivision().getStream());
						respObject.addProperty("batchsubjectIds", batch.getSubjectIds());
						respObject.addProperty("batchsubjectnames", batch.getSubjectNames());
						respObject.addProperty("allsubjectname", allsubjectname);
						respObject.addProperty("allsubjectIds", allsubjectIds);
						found=true;
						break;
					}	
				}
			}
			
			if(!found){
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "Batch does not exists in class!");
			}else{
				respObject.addProperty(STATUS, "success");				
			}
			}
			//printWriter.write(respObject.toString());
			
		}else if(Constants.UPDATE_BATCH.equalsIgnoreCase(methodToCall)){
			Integer regId = null;
			if(!req.getParameter("regId").equals("")){
				regId = Integer.parseInt(req.getParameter("regId"));
			}else{
				UserBean userBean = (UserBean) req.getSession().getAttribute("user");
				if(0 == userBean.getRole() || !"".equals(regId)){
					if(null == regId){
						regId = userBean.getRegId();
					}
				}else{
					regId = userBean.getRegId();
				}
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			int batchId=Integer.parseInt(req.getParameter("batchId"));
			String batchName=req.getParameter("batchName");
			int batchdivisionid=Integer.parseInt(req.getParameter("batchdivisionid"));
			Batch batch=batchTransactions.getBatch(batchId,userBean.getRegId(),batchdivisionid);
			String sub_Ids=req.getParameter("subIds");			
			
			if(batch!=null){
				ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
				scheduleTransaction.deleteschedulerelatedtobatchsubject(batch, sub_Ids);
				batch.setSub_id(sub_Ids);
				batch.setBatch_name(batchName);
				if(!batchTransactions.isUpdatedBatchExist(batch)){
				if(batchTransactions.addUpdateDb(batch)){
					BatchHelperBean batchHelperBean= new BatchHelperBean(regId);
					batchHelperBean.setBatchDetailsList();
					List<BatchDetails> batchList = new ArrayList<BatchDetails>();
					batchList = batchHelperBean.getBatchDetailsList();
					Gson gson=new Gson();
					String allbatches=gson.toJson(batchList);
					respObject.addProperty("allbatches", allbatches);
					respObject.addProperty(STATUS, "success");
					respObject.addProperty(MESSAGE, "Successfully updated batch.");
				}else{
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Error while updating the batch with Id="+batchId+"!");
				}
				}else{
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Batch name already Exists!");
				}
			//	printWriter.write(respObject.toString());
			}
		}else if(Constants.DELETE_BATCH.equals(methodToCall)){
			Integer regId = null;
			if(!req.getParameter("regId").equals("")){
				regId = Integer.parseInt(req.getParameter("regId"));
			}else{
				UserBean userBean = (UserBean) req.getSession().getAttribute("user");
				if(0 == userBean.getRole() || !"".equals(regId)){
					if(null == regId){
						regId = userBean.getRegId();
					}
				}else{
					regId = userBean.getRegId();
				}
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			int deleteBatchId=Integer.parseInt(req.getParameter("batchId"));
			int batchdivisionid=Integer.parseInt(req.getParameter("batchdivisionid"));
			Batch batch=batchTransactions.getBatch(deleteBatchId,userBean.getRegId(),batchdivisionid);
			ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
			//scheduleTransaction.deleteSchedulerelatedoBatch(batch);
			StudentTransaction studentTransaction=new StudentTransaction();
			studentTransaction.removeBatchFromstudentslist(deleteBatchId+"",userBean.getRegId(),batchdivisionid);
			NotesTransaction notesTransaction=new NotesTransaction();
			notesTransaction.removebatchfromnotes(userBean.getRegId(), batchdivisionid, deleteBatchId+"");
			ExamTransaction examTransaction=new ExamTransaction();
				/*if(batchTransactions.deleteBatch(batch)){
					BatchHelperBean batchHelperBean= new BatchHelperBean(regId);
					batchHelperBean.setBatchDetailsList();
					List<BatchDetails> batchList = new ArrayList<BatchDetails>();
					batchList = batchHelperBean.getBatchDetailsList();
					Gson gson=new Gson();
					String allbatches=gson.toJson(batchList);
					respObject.addProperty("allbatches", allbatches);
					respObject.addProperty(STATUS, "success");
					respObject.addProperty(MESSAGE, "Batch Successfully deleted .");
				}else{
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Batch does not exists in class!");
				}*/
		}else if("getstudentbatch".equals(methodToCall)){
			Integer regId = null;
			
				UserBean userBean = (UserBean) req.getSession().getAttribute("user");
				if(0 == userBean.getRole() || !"".equals(regId)){
					if(null == regId){
						regId = userBean.getRegId();
					}
				}else{
					regId = userBean.getRegId();
				}
				
				String classid=req.getParameter("classid");
				String date=req.getParameter("date");
			//	String dateString[]=date.split("/");
				//Date date2=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[0])-1,Integer.parseInt( dateString[1]));
				Student student= studentTransaction.getclassStudent(regId,Integer.parseInt(classid));		
				BatchTransactions batchTransactions=new BatchTransactions();
				ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
				if(!student.getBatch_id().equals("")){
				int counter =0;
				String batchids[]=student.getBatch_id().split(",");
				String batchnames="";
				String batchid="";
				while(batchids.length>counter)
				{
					Batch batch=batchTransactions.getBatch(Integer.parseInt(batchids[counter]),Integer.parseInt(classid),student.getDiv_id());
					if(counter==0)
					{
						batchnames=batch.getBatch_name();
						batchid=batchids[counter];
						
					}else{
						batchnames=batchnames+","+batch.getBatch_name();
						batchid=batchid+","+batchids[counter];
					}
					counter++;	
				}
				respObject.addProperty("batchids", batchid);
			respObject.addProperty("batchnames", batchnames);
			respObject.addProperty("nobatch", "");
				}else{
					respObject.addProperty("nobatch", "nobatch");
				}
			respObject.addProperty(STATUS, "success");
		}else if("getteacherschedule".equals(methodToCall)){
			Integer regId = null;
			
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			
			String classid=req.getParameter("classid");
			String date=req.getParameter("date");
			String dateString[]=date.split("/");
			Date scheduledate=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[1])-1,Integer.parseInt( dateString[0]));
			ScheduleTransaction  scheduleTransaction=new ScheduleTransaction();
			/*List<Schedule> schedules= scheduleTransaction.getTeachersSchedule(Integer.parseInt(classid), regId,scheduledate);
			BatchTransactions batchTransactions=new BatchTransactions();
			List<Batch> batchs =batchTransactions.getTeachersBatch(schedules);
			SubjectTransaction subjectTransaction=new SubjectTransaction();
			List<String> subjects=subjectTransaction.getScheduleSubject(schedules);
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			List<Division> divisions=divisionTransactions.getAllDivisions(Integer.parseInt(classid));
			int counter=0;
			
			String starttime="";
			String endtime="";
			String subject="";
			String batch="";
			String division="";
			DateFormat sdf = new SimpleDateFormat("kk:mm");
			DateFormat f2 = new SimpleDateFormat("h:mma");
			while(counter<schedules.size()){
				try {
					java.util.Date start=sdf.parse(schedules.get(counter).getStart_time().toString());
					java.util.Date end=sdf.parse(schedules.get(counter).getEnd_time().toString());
			
				if(counter==0)
				{
					starttime=f2.format(start).toUpperCase();
					endtime=f2.format(end).toUpperCase();
					subject=subjects.get(counter);
					batch=batchs.get(counter).getBatch_name();
					for (int i = 0; i < divisions.size(); i++) {
						if(divisions.get(i).getDivId()==schedules.get(counter).getDiv_id()){
							division=divisions.get(i).getDivisionName();
							break;
						}
					}
				}else{
					starttime=starttime+","+ f2.format(start).toUpperCase();
					endtime=endtime+","+ f2.format(end).toUpperCase();
					subject=subject+","+subjects.get(counter);
					batch=batch+","+ batchs.get(counter).getBatch_name();
					for (int i = 0; i < divisions.size(); i++) {
						if(divisions.get(i).getDivId()==schedules.get(counter).getDiv_id()){
							division=division+","+divisions.get(i).getDivisionName();
							break;
						}
					}
				}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      	
				counter++;
			}
			
			respObject.addProperty("batch", batch);
			respObject.addProperty("starttime", starttime);
			respObject.addProperty("endtime", endtime);
			respObject.addProperty("subject", subject);
			respObject.addProperty("division", division);*/
			
			/*String dateString[]=date.split("/");
			Date date2=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[0])-1,Integer.parseInt( dateString[1]));
			Student student= studentTransaction.getclassStudent(regId,Integer.parseInt(classid));		
			BatchTransactions batchTransactions=new BatchTransactions();
			ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
			int counter =0;
			String batchids[]=student.getBatch_id().split(",");
			String batchnames="";
			String batchid="";
			while(batchids.length>counter)
			{
				Batch batch=batchTransactions.getBatch(Integer.parseInt(batchids[counter]));
				if(counter==0)
				{
					batchnames=batch.getBatch_name();
					batchid=batchids[counter];
					
				}else{
					batchnames=batchnames+","+batch.getBatch_name();
					batchid=batchid+","+batchids[counter];
				}
				counter++;	
			}
			respObject.addProperty("batchids", batchid);
		respObject.addProperty("batchnames", batchnames);*/	
		respObject.addProperty(STATUS, "success");
	}else if("deleteschedule".equals(methodToCall)){
		Integer regId = null;
		
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		if(0 == userBean.getRole() || !"".equals(regId)){
			if(null == regId){
				regId = userBean.getRegId();
			}
		}else{
			regId = userBean.getRegId();
		}
		
		String scheduleid=req.getParameter("scheduleid");
		ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
		/*scheduleTransaction.deleteSchedule(Integer.parseInt(scheduleid),userBean.getRegId());*/
			respObject.addProperty(STATUS, "success");
}else if("getstudentsrelatedtobatch".equals(methodToCall)){
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	String batchID=req.getParameter("batchID");
	String pagenumber=req.getParameter("pagenumber");
	String batchdivision=req.getParameter("divisionID");
	StudentTransaction studentTransaction=new StudentTransaction();
	int count=0;
	if("".equals(batchID)){
		count=studentTransaction.getunallocatedStudentcount(userBean.getRegId());
	}else{
		count=studentTransaction.getStudentscountrelatedtobatch(batchID,userBean.getRegId(),Integer.parseInt(batchdivision));
	}
	
	//Taking data from database
	/*int resultPerPage = Integer.parseInt(ServiceMap.getSystemParam(Constants.SERVICE_PAGINATION, "resultsperpage"));*/
	if(count>0){
		List<Student> students=new ArrayList();
		if("".equals(batchID)){
			students=studentTransaction.getUnallocatedStudentIDs(userBean.getRegId());
		}else{
			students=studentTransaction.getStudentsrelatedtobatch(batchID,userBean.getRegId(),Integer.parseInt(batchdivision));
		}
		List<Integer> studentIDsList = new ArrayList<Integer>();
		for (int i = 0; i < students.size(); i++) {
			studentIDsList.add(students.get(i).getStudent_id());
		}
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> registerBeans= registerTransaction.getStudentsInfo(studentIDsList);
		BatchTransactions batchTransactions=new BatchTransactions();
		List<Batch> batchList = batchTransactions.getBatchRelatedtoDivision(Integer.parseInt(batchdivision));
		List<StudentDetails> studentDetailsList = new ArrayList<StudentDetails>();
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		Division division = divisionTransactions.getDidvisionByID(Integer.parseInt(batchdivision));
		if(students != null){
			for (int i = 0; i < students.size(); i++) {
				StudentDetails studentDetails=new StudentDetails();
				registerBeans.get(i).setLoginPass("");
				studentDetails.setStudentUserBean(registerBeans.get(i));
				studentDetails.setDivision(division);
				
				String rollnBatch = students.get(i).getBatchIdNRoll();
				if(null!=rollnBatch){
					try{
					JsonParser jsonParser = new JsonParser();
					JsonObject jsonObject = jsonParser.parse(rollnBatch).getAsJsonObject();
					if(jsonObject.has(batchID)){
						int rollNo = jsonObject.get(batchID).getAsInt();
						studentDetails.setRollNo(rollNo);
					}
					}catch(Exception e){
						studentDetails.setRollNo(0);	
					}
				}
				String batchIDArray[] = students.get(i).getBatch_id().split(",");
				List<Batch> studentBatchList = new ArrayList<Batch>();
				if(batchIDArray.length > 1){
					for (int j = 0; j < batchIDArray.length; j++) {
							for (int k = 0; k < batchList.size(); k++) {
								if(Integer.parseInt(batchIDArray[j]) ==  batchList.get(k).getBatch_id()){
									studentBatchList.add(batchList.get(k));
									break;
								}
							}	
					}
				}else{
					for (int k = 0; k < batchList.size(); k++) {
						if(Integer.parseInt(batchIDArray[0]) ==  batchList.get(k).getBatch_id()){
							studentBatchList.add(batchList.get(k));
							break;
						}
					}
				}
				studentDetails.setBatches(studentBatchList);
				studentDetailsList.add(studentDetails);
			}
		}
		Gson gson=new Gson();
		JsonElement jsonElement = gson.toJsonTree(studentDetailsList);
		respObject.add("studentList", jsonElement);
	}else{
		Gson gson=new Gson();
		JsonElement jsonElement = gson.toJsonTree(new StudentDetails());
		respObject.add("studentList", jsonElement);
		/*respObject.addProperty("studentids", "");
		respObject.addProperty("count", "");
		respObject.addProperty("remain", "");
		respObject.addProperty("pages", "");*/
	}
	respObject.addProperty(STATUS, "success");
}else if("modifysubject".equals(methodToCall)){
	
	String subjectname=req.getParameter("subjectname");
	String subjectid=req.getParameter("subjectid");
	Subject subject=new Subject();
	subject.setSubjectId(Integer.parseInt(subjectid));
	subject.setSubjectName(subjectname);
	Integer regId = null;
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	regId=userBean.getRegId();
	subject.setInstitute_id(regId);
	SubjectTransaction subjectTransaction=new SubjectTransaction();
	Boolean status=subjectTransaction.modifySubject(subject);
	if(status==false){
		List<Subjects> subjects=subjectTransaction.getAllClassSubjects(regId);
		Gson gson=new Gson();
		String json=gson.toJson(subjects);
		respObject.addProperty("subjects", json);
		respObject.addProperty("added", "false");
	}else{
		respObject.addProperty("added", "true");
	}
	respObject.addProperty(STATUS, "success");
}else if("deletesubject".equals(methodToCall)){
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	UserStatic userStatic=userBean.getUserStatic();
	String subjectid=req.getParameter("subjectid");
	Subject subject=new Subject();
	subject.setSubjectId(Integer.parseInt(subjectid));
	BatchTransactions transactions=new BatchTransactions();
//	boolean batchstatus= transactions.deletesubjectfrombatch(subjectid);
	TeacherTransaction teacherTransaction=new TeacherTransaction();
//	teacherTransaction.deletesubjectfromteacherlist(subjectid);
	ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
	//scheduleTransaction.deleteschedulerelatedsubject(Integer.parseInt(subjectid));
	NotesTransaction notesTransaction=new NotesTransaction();
//	notesTransaction.deleteNotesRelatedToSubject(Integer.parseInt(subjectid));
	StudentMarksTransaction marksTransaction=new StudentMarksTransaction();
//	marksTransaction.deleteStudentMarksrelatedtosubject(Integer.parseInt(subjectid));
	ExamTransaction examTransaction=new ExamTransaction();
	//examTransaction.deleteExamrelatedtosubject(Integer.parseInt(subjectid));
	SubjectTransaction subjectTransaction=new SubjectTransaction();
	subjectTransaction.deleteTopicsrelatedToSubject(userBean.getRegId(), Integer.parseInt(subjectid));
	QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
	//List<Integer> ques_ids=bankTransaction.getQuestionrelatedtoSubject(userBean.getRegId(), Integer.parseInt(subjectid));
	String path=userStatic.getExamPath()+File.separator+subjectid;
	FileUtils.deleteDirectory(new File(userStatic.getExamPath()+File.separator+subjectid));
	FileUtils.deleteDirectory(new File(userStatic.getNotesPath()+File.separator+subjectid));
	bankTransaction.deleteQuestionrelatedtoSubject(userBean.getRegId(), Integer.parseInt(subjectid));
//	subjectTransaction.deleteSubject(Integer.parseInt(subjectid));
	List<Subjects> subjects=subjectTransaction.getAllClassSubjects(userBean.getRegId());
	Gson gson=new Gson();
	String json=gson.toJson(subjects);
	respObject.addProperty("subjects", json);
	respObject.addProperty(STATUS, "success");
}else if("modifyclass".equals(methodToCall)){
	String classname=req.getParameter("classname");
	String stream=req.getParameter("stream");
	String classid=req.getParameter("classid");
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	Division division=new Division();
	division.setDivId(Integer.parseInt(classid));
	division.setDivisionName(classname);
	division.setStream(stream);
	division.setInstitute_id(userBean.getRegId());
	DivisionTransactions divisionTransactions=new DivisionTransactions();
	if(divisionTransactions.updateClass(division)){
		respObject.addProperty("updated", "false");
	}else{
		List<Division>	list=divisionTransactions.getAllDivisions(userBean.getRegId());
		Gson gson=new Gson();
		String allclasses=gson.toJson(list);
		respObject.addProperty("allclasses", allclasses);
		respObject.addProperty("updated", "true");
	}
	respObject.addProperty(STATUS, "success");

}else if("deleteclass".equals(methodToCall)){
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	String classid=req.getParameter("classid");
	UserStatic userStatic=userBean.getUserStatic();
	ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
	scheduleTransaction.deleteschedulerelatedtoclass(Integer.parseInt(classid));
	StudentTransaction studentTransaction=new StudentTransaction();
	studentTransaction.removebatchfromstudentlist(Integer.parseInt(classid));
	NotesTransaction notesTransaction=new NotesTransaction();
	//notesTransaction.deleteNotesRelatedToDivision(Integer.parseInt(classid));
	StudentMarksTransaction marksTransaction=new StudentMarksTransaction();
	//marksTransaction.deleteStudentMarksrelatedtodivision(Integer.parseInt(classid));
	ExamTransaction examTransaction=new ExamTransaction();
	//examTransaction.deleteExamrelatedtodivision(Integer.parseInt(classid));
	BatchTransactions batchTransactions=new BatchTransactions();
	//batchTransactions.deletebatchrelatdtoclass(Integer.parseInt(classid));
	SubjectTransaction subjectTransaction=new SubjectTransaction();
	subjectTransaction.deleteTopicsrelatedToDivision(userBean.getRegId(), Integer.parseInt(classid));
	QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
	//List<Integer> ques_ids=bankTransaction.getQuestionrelatedtoSubject(userBean.getRegId(), Integer.parseInt(subjectid));
	List<Subjects> list=subjectTransaction.getAllClassSubjects(userBean.getRegId()); 
	if(list!=null){
	for (int i = 0; i < list.size(); i++) {
		String path=userStatic.getExamPath()+File.separator+list.get(i).getSubjectId()+File.separator+classid;
		FileUtils.deleteDirectory(new File(userStatic.getExamPath()+File.separator+list.get(i).getSubjectId()+File.separator+classid));
		FileUtils.deleteDirectory(new File(userStatic.getExamPath()+File.separator+list.get(i).getSubjectId()+File.separator+classid));
	}
	}
	bankTransaction.deleteQuestionrelatedtoClass(userBean.getRegId(), Integer.parseInt(classid));
	DivisionTransactions divisionTransactions=new DivisionTransactions();
	//divisionTransactions.deletedivision(Integer.parseInt(classid));
	List<Division>	classlist=divisionTransactions.getAllDivisions(userBean.getRegId());
	Gson gson=new Gson();
	String allclasses=gson.toJson(classlist);
	respObject.addProperty("allclasses", allclasses);
	respObject.addProperty(STATUS, "success");
}else if("forgotpassword".equals(methodToCall)){
	String email=req.getParameter("email");
	String mobile=req.getParameter("mobile");
	RegisterTransaction registerTransaction=new RegisterTransaction();
	if(registerTransaction.isEmailAndMobileValid(email, mobile)){
		String pass=registerTransaction.getPassword(email, mobile);
	
		HashMap<String, String> dataMap = new HashMap();
		dataMap.put("password", pass);
		dataMap.put("HEADER", "Password Recovery!!!");
		dataMap.put("classLink", "http://jbdev-mycorex.rhcloud.com/login");
		AllMail allMail = new AllMail();
		allMail.sendMail(email, dataMap, "forgotPassHtml.html", "Your Password");
	/*	
		// Common variables
	String to = email;//change accordingly

    // Sender's email ID needs to be mentioned
    String from = "mycorex2015@gmail.com";//change accordingly
    final String username = "mycorex2015";//change accordingly
    final String password = "corex2015";//change accordingly

    // Assuming you are sending email through relay.jangosmtp.net
    String host = "smtp.gmail.com";

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", "587");

    // Get the Session object.
    Session session = Session.getInstance(props,
    new javax.mail.Authenticator() {
       protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
       }
    });

    try {
    	try {
			
    		InternetAddress internetAddress=new InternetAddress(to);
        	internetAddress.validate();
		} catch (AddressException e) {
			// TODO: handle exception
			AppLogger.logger("Invalid Address");
		}
    	
       // Create a default MimeMessage object.
       Message message = new MimeMessage(session);

       // Set From: header field of the header.
       message.setFrom(new InternetAddress(from));

       // Set To: header field of the header.
       message.setRecipients(Message.RecipientType.TO,
       InternetAddress.parse(to));

       // Set Subject: header field
       message.setSubject("Forgot Password");

       // Now set the actual message
       message.setText("Hello, Your Password is "
          + pass);

       	
       // Send message
     Transport.send(message);

       AppLogger.logger("Sent message successfully....");

    } catch (MessagingException e) {
    	AppLogger.logger("Invalid Internet Address");
          throw new RuntimeException(e);
    }
	*/
	respObject.addProperty(STATUS, "success");
	}else{
		respObject.addProperty(STATUS, "invalid");
	}
}else if("activation".equals(methodToCall)){
	String code=req.getParameter("code");
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	int regID=userBean.getRegId();
	RegisterTransaction registerTransaction=new RegisterTransaction();
	if(registerTransaction.ActivationCodeValidation(regID, code)){
		registerTransaction.removeActivationCode(regID);
		userBean.setActivationcode("");
		respObject.addProperty(STATUS, "success");	
	}else{
		respObject.addProperty(STATUS, "fail");
	}
}else if("resendActivation".equals(methodToCall)){
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	AllMail allMail = new AllMail();
	HashMap<String, String> hashMap = new  HashMap();
	hashMap.put("HEADER", "Activation Code");
	hashMap.put("classLink", "http://jbdev-mycorex.rhcloud.com/login");
	hashMap.put("ACTIVATION_CODE", userBean.getActivationcode());	
	hashMap.put("NAME", userBean.getFirstname());
	boolean result = allMail.sendMail(userBean.getEmail(), hashMap, "registerSuccess.html","ClassApp - Resend Activation");
	if(result)
		respObject.addProperty(STATUS, "success");
	else
		respObject.addProperty(STATUS, "error");
	
}else if("resetpassword".equals(methodToCall)){
	String password=req.getParameter("password");
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	int regID=userBean.getRegId();
	RegisterTransaction registerTransaction=new RegisterTransaction();
	registerTransaction.resetpassword(regID, password);
	userBean.setStatus("");
	respObject.addProperty(STATUS, "success");
	
}else if("addfeedback".equals(methodToCall)){
	String name=req.getParameter("name");
	String email=req.getParameter("email");
	String comment=req.getParameter("comment");
	Feedback feedback=new Feedback();
	feedback.setEmail(email);
	feedback.setFeedback(comment);
	feedback.setName(name);
	feedbackTransaction feedbackTransaction=new feedbackTransaction();
	feedbackTransaction.addFeedback(feedback);
	respObject.addProperty(STATUS, "success");
	
}else if("getbatches".equals(methodToCall)){
	String division=req.getParameter("division");
	BatchTransactions batchTransactions=new BatchTransactions();
	List<Batch> batchs=batchTransactions.getBatchRelatedtoDivision(Integer.parseInt(division));
	StringBuilder batchids=new StringBuilder();
	StringBuilder batchnames=new StringBuilder();
	int i=0;
	if(batchs.size()>0){
	while(batchs.size()>i){
		batchids.append(batchs.get(i).getBatch_id()+",");
		batchnames.append(batchs.get(i).getBatch_name()+",");
		i++;
	}
	batchnames.deleteCharAt(batchnames.length()-1);
	batchids.deleteCharAt(batchids.length()-1);
	}else{
		batchnames.append("");
		batchids.append("");
	}
	respObject.addProperty("batchnames", batchnames.toString());
	respObject.addProperty("batchids", batchids.toString());
	respObject.addProperty(STATUS, "success");
	
}else if("validatenotesname".equals(methodToCall)){
	String[] notes=req.getParameter("notes").split(",");
	String[] notesrowid=req.getParameter("notesrowid").split(",");
	String filesize=req.getParameter("filesize");
	InstituteStatTransaction instituteStatTransaction=new InstituteStatTransaction();
	String overlappedIds="";
	for (int i = 0; i < notesrowid.length; i++) {
		for (int j = i+1; j < notesrowid.length; j++) {
			if(notes[i].trim().equalsIgnoreCase(notes[j])){
				if("".equals(overlappedIds)){
					overlappedIds=notesrowid[i]+","+notesrowid[j];
				}else{
					overlappedIds=overlappedIds+"/"+notesrowid[i]+","+notesrowid[j];
				}
			}
		}
		
	}
	String notesnamestatus="";
	if("".equals(overlappedIds)){
	String institute=req.getParameter("institute");
	int division=Integer.parseInt(req.getParameter("division"));
	int subject=Integer.parseInt(req.getParameter("subject"));
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	NotesTransaction notesTransaction=new NotesTransaction();
	int inst_id=userBean.getRegId();
	if(institute!="" && institute !=null){
		inst_id=Integer.parseInt(institute);
	}
	InstituteStats instituteStats=instituteStatTransaction.getStats(inst_id);
	if(instituteStats.getAvail_memory()>0 && (instituteStats.getUsed_memory()+Double.parseDouble(filesize))<=instituteStats.getAlloc_memory())
	{
		for (int i = 0; i < notes.length; i++) {
		
	
	boolean flag=notesTransaction.validatenotesname(notes[i].trim(), inst_id,division,subject);
	if(flag){
		if("".equals(notesnamestatus)){
		notesnamestatus=notesrowid[i];
		}else{
			notesnamestatus=notesnamestatus+","+notesrowid[i];
			
		}
	}
	}
		respObject.addProperty("allocmemory", "");
	}else{
		respObject.addProperty("allocmemory", "exceeded");
	}
	}
	respObject.addProperty("notesnamestatus", notesnamestatus);
		respObject.addProperty("overlappedIds", overlappedIds);
	
	respObject.addProperty(STATUS, "success");
	
}else if("getsubjectsanddivisions".equals(methodToCall)){
	String classid=req.getParameter("classes");
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	TeacherTransaction teacherTransaction=new TeacherTransaction();
	List<Subject> subjectlist=teacherTransaction.getTeacherSubject(userBean.getRegId(), Integer.parseInt(classid));
	DivisionTransactions divisionTransactions=new DivisionTransactions();
	List<Division> divisions=divisionTransactions.getAllDivisions(Integer.parseInt(classid));
	StringBuilder subjectnames=new StringBuilder();
	StringBuilder subjectsids=new StringBuilder();
	StringBuilder divisionnames=new StringBuilder();
	StringBuilder divisionids=new StringBuilder();
	int i=0;
	if(subjectlist.size()>0){
		while(subjectlist.size()>i){
			subjectnames.append(subjectlist.get(i).getSubjectName()+",");
			subjectsids.append(subjectlist.get(i).getSubjectId()+",");
			i++;
		}
		subjectnames.deleteCharAt(subjectnames.length()-1);
		subjectsids.deleteCharAt(subjectsids.length()-1);
		}else{
			subjectnames.append("");
			subjectsids.append("");
		}
		i=0;
		if(divisions.size()>0){
			while(divisions.size()>i){
				divisionnames.append(divisions.get(i).getDivisionName()+" "+divisions.get(i).getStream()+",");
				divisionids.append(divisions.get(i).getDivId()+",");
				i++;
			}
			divisionnames.deleteCharAt(divisionnames.length()-1);
			divisionids.deleteCharAt(divisionids.length()-1);
			}else{
				divisionnames.append("");
				divisionids.append("");
			}
	
		respObject.addProperty("subjectnames", subjectnames.toString());
		respObject.addProperty("subjectids", subjectsids.toString());
		respObject.addProperty("divisionnames", divisionnames.toString());
		respObject.addProperty("divisionids", divisionids.toString());
	respObject.addProperty(STATUS, "success");
	
}else if("fetchnotes".equals(methodToCall)){
	String subject=(String) req.getParameter("subject");
	String division=(String) req.getParameter("division");
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	NotesTransaction notesTransaction=new NotesTransaction();
	List<Notes> noteslist =notesTransaction.getNotesPath(Integer.parseInt(division), Integer.parseInt(subject), userBean.getRegId());
	StringBuilder notesname=new StringBuilder();
	StringBuilder notesids=new StringBuilder();
	StringBuilder notespaths=new StringBuilder();
	
	int i=0;
	if(noteslist.size()>0){
		while(noteslist.size()>i){
			notesname.append(noteslist.get(i).getName()+",");
			notesids.append(noteslist.get(i).getNotesid()+",");
			notespaths.append(noteslist.get(i).getNotespath()+"");
			i++;
		}
		notesname.deleteCharAt(notesname.length()-1);
		notesids.deleteCharAt(notesids.length()-1);
		notespaths.deleteCharAt(notespaths.length()-1);
		}else{
			notesname.append("");
			notesids.append("");
			notespaths.append("");
		}
	respObject.addProperty("notesnames", notesname.toString());
	respObject.addProperty("notesids", notesids.toString());
	respObject.addProperty("notespaths", notespaths.toString());
	respObject.addProperty(STATUS, "success");
	
}else if("getstudentnotes".equals(methodToCall)){
	String subject=(String) req.getParameter("subject");
	String classid=(String) req.getParameter("classid");
	String batch=(String) req.getParameter("batch");
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	NotesTransaction notesTransaction=new NotesTransaction();
	List<Notes> noteslist =notesTransaction.getStudentNotesPath(batch, Integer.parseInt(subject), Integer.parseInt(classid),1);
	StringBuilder notesname=new StringBuilder();
	StringBuilder notesids=new StringBuilder();
	StringBuilder notespaths=new StringBuilder();
	
	int i=0;
	if(noteslist.size()>0){
		while(noteslist.size()>i){
			notesname.append(noteslist.get(i).getName()+",");
			notesids.append(noteslist.get(i).getNotesid()+",");
			notespaths.append(noteslist.get(i).getNotespath()+"");
			i++;
		}
		notesname.deleteCharAt(notesname.length()-1);
		notesids.deleteCharAt(notesids.length()-1);
		notespaths.deleteCharAt(notespaths.length()-1);
		}else{
			notesname.append("");
			notesids.append("");
			notespaths.append("");
		}
	respObject.addProperty("notesnames", notesname.toString());
	respObject.addProperty("notesids", notesids.toString());
	respObject.addProperty("notespaths", notespaths.toString());
	respObject.addProperty(STATUS, "success");
	
}else if("fetchteachernotes".equals(methodToCall)){
	/*String subject=(String) req.getParameter("subject");
	String division=(String) req.getParameter("division");
	String classid=(String) req.getParameter("classes");
	
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	NotesTransaction notesTransaction=new NotesTransaction();
	List<Notes> noteslist =notesTransaction.getNotesPath(Integer.parseInt(division), Integer.parseInt(subject), Integer.parseInt(classid));
	StringBuilder notesname=new StringBuilder();
	StringBuilder notesids=new StringBuilder();
	StringBuilder notespaths=new StringBuilder();
	StringBuilder addedbyteacher=new StringBuilder();
	int i=0;
	if(noteslist.size()>0){
		while(noteslist.size()>i){
			notesname.append(noteslist.get(i).getName()+",");
			notesids.append(noteslist.get(i).getNotesid()+",");
			notespaths.append(noteslist.get(i).getNotespath()+",");
			if(userBean.getRegId()==noteslist.get(i).getAddedby()){
				addedbyteacher.append("true,");
			}else{
				addedbyteacher.append("false,");
			}
			
			i++;
		}
		notesname.deleteCharAt(notesname.length()-1);
		notesids.deleteCharAt(notesids.length()-1);
		notespaths.deleteCharAt(notespaths.length()-1);
		addedbyteacher.deleteCharAt(addedbyteacher.length()-1);
		}else{
			notesname.append("");
			notesids.append("");
			notespaths.append("");
			addedbyteacher.append("");
		}
	respObject.addProperty("notesnames", notesname.toString());
	respObject.addProperty("notesids", notesids.toString());
	respObject.addProperty("notespaths", notespaths.toString());
	respObject.addProperty("addedbyteacher", addedbyteacher.toString());
	respObject.addProperty(STATUS, "success");*/
	
}else if("editnotesinformation".equals(methodToCall)){
	String notesid=(String) req.getParameter("notesid");
	String institute=(String) req.getParameter("institute");
	String division=(String) req.getParameter("division");
	String subject=(String) req.getParameter("subject");
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	NotesTransaction notesTransaction=new NotesTransaction();
	Notes notes=null;
	if(userBean.getRole()==2){
		notes=notesTransaction.getNotesById(Integer.parseInt(notesid),Integer.parseInt(institute),Integer.parseInt(subject),Integer.parseInt(division));
	}else{
		notes=notesTransaction.getNotesById(Integer.parseInt(notesid),userBean.getRegId(),Integer.parseInt(subject),Integer.parseInt(division));
	}
	BatchTransactions batchTransactions=new BatchTransactions();
	List<Batch> list=new ArrayList<Batch>();
	if(institute!=null && !"".equals(institute)){		
		list=batchTransactions.getbachesrelatedtodivandsubject(subject, Integer.parseInt(division), Integer.parseInt(institute));
	}else{
	list=batchTransactions.getbachesrelatedtodivandsubject(subject, Integer.parseInt(division), userBean.getRegId());
	}
	StringBuilder allbatchnames=new StringBuilder();
	StringBuilder allbatchids=new StringBuilder();
	
	
	int i=0;
	if(list.size()>0){
		while(list.size()>i){
			allbatchnames.append(list.get(i).getBatch_name()+",");
			allbatchids.append(list.get(i).getBatch_id()+",");
			
			i++;
		}
		allbatchnames.deleteCharAt(allbatchnames.length()-1);
		allbatchids.deleteCharAt(allbatchids.length()-1);
		
		}else{
			allbatchnames.append("");
			allbatchids.append("");
		}
	respObject.addProperty("allbatchnames", allbatchnames.toString());
	respObject.addProperty("allbatchids", allbatchids.toString());
	respObject.addProperty("notesname", notes.getName());
	respObject.addProperty("notesbatches", notes.getBatch());
	respObject.addProperty(STATUS, "success");
	
}else if("updatenotes".equals(methodToCall)){
	/*String notesid=(String) req.getParameter("notesid");
	String batchids=(String) req.getParameter("batchids");
	String notesname=(String) req.getParameter("notesname");
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	NotesTransaction notesTransaction=new NotesTransaction();
	
	if(!notesTransaction.validatenotesnamebyID(notesname, userBean.getRegId(), Integer.parseInt(notesid))){
	notesTransaction.updatenotes(notesname, Integer.parseInt(notesid), batchids,1,1,1);
	}else{
		respObject.addProperty("duplicate", "true");
	}
	respObject.addProperty(STATUS, "success");*/
	
}else if("deletenotes".equals(methodToCall)){
	String notesid=(String) req.getParameter("notesid");
	NotesTransaction notesTransaction=new NotesTransaction();
	String path=notesTransaction.getNotepathById(Integer.parseInt(notesid),1,1,1);
	String realpath=	req.getSession().getServletContext().getRealPath("/"+path);
	File file = new File(realpath);
	  file.delete();
	//  notesTransaction.deleteNotes(Integer.parseInt(notesid),1,1,1);
	respObject.addProperty(STATUS, "success");
	
}else if("sendmessage".equals(methodToCall)){
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	String message=(String) req.getParameter("message");
	String batch=(String) req.getParameter("batch");
	String date=(String) req.getParameter("date");
	String batchname=(String) req.getParameter("batchname");
	String dateString[]=date.split("/");
	Date msgdate=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[0])-1,Integer.parseInt( dateString[1]));
	NotificationGlobalTransation notificationGlobalTransation=new NotificationGlobalTransation();
	String batcharr[]=batch.split(",");
	Notification notification=new Notification();
	if(batcharr.length>1){
		notification.setBatch("ALL");
	}else{
	notification.setBatch(batch.split("_")[0]);
	notification.setDiv_id(Integer.parseInt(batch.split("_")[1]));
	}
	notification.setInstitute_id(userBean.getRegId());
	notification.setMessage(message);
	notification.setMsg_date(msgdate);
	notification.setBatch_name(batchname);
	NotificationTransaction transaction=new NotificationTransaction();
	transaction.add(notification);		
	int i=0;
	/*while(i<batcharr.length){*/
		notificationGlobalTransation.sendMessage(message, notification);
	/*	i++;
	}*/
	respObject.addProperty(STATUS, "success");
	
	}else if("getsubjectofbatch".equalsIgnoreCase(methodToCall)){
		respObject.addProperty(STATUS, "success");
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId = null;
		if(0 == userBean.getRole() || !"".equals(regId)){
			if(null == regId){
				regId = userBean.getRegId();
			}
		}else{
			regId = userBean.getRegId();
		}
		String batchId = req.getParameter("batchId");
		List<com.datalayer.subject.Subject> subjectName = subjectTransaction.getSubjectsOfBatch(Integer.parseInt(batchId));
		Gson gson = new Gson();
		respObject.addProperty("subjectofbatch", gson.toJson(subjectName));
	}else if("getSubjectOfDivision".equalsIgnoreCase(methodToCall)){
		respObject.addProperty(STATUS, "success");
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId = null;
		if(0 == userBean.getRole() || !"".equals(regId)){
			if(null == regId){
				regId = userBean.getRegId();
			}
		}else{
			regId = userBean.getRegId();
		}
		String divisionId = req.getParameter("divisionId");
	String institute=	req.getParameter("institute");
	if(userBean.getRole()==2){
		regId=Integer.parseInt(institute);
	}
	
		List<Subject> subjects = subjectTransaction.getSubjectRelatedToDiv(Integer.parseInt(divisionId), regId);
		TeacherTransaction teacherTransaction=new TeacherTransaction();
		List<Subject> list=new ArrayList<Subject>();
		if(userBean.getRole()==2){
			list= teacherTransaction.getTeacherSubject(userBean.getRegId(), regId);
		}
		StringBuilder subjectids=new StringBuilder();
		StringBuilder subjectnames=new StringBuilder();
		int i=0;
		if(null!=subjects && subjects.size()>0){
			if(userBean.getRole()==2){
				while(subjects.size()>i){
					Subject subject = subjects.get(i);
					if (list!=null) {
						int j=0;
						while (j<list.size()) {
						
							if(list.get(j).getSubjectId()==subject.getSubjectId())
							{
								subjectids.append(subject.getSubjectId()+",");
								subjectnames.append(subject.getSubjectName()+",");
							}
						j++;
						}
					}
					
					i++;
				}
			}else{
		while(subjects.size()>i){
			Subject subject = subjects.get(i);
			subjectids.append(subject.getSubjectId()+",");
			subjectnames.append(subject.getSubjectName()+",");
			i++;
		}
		}
		subjectnames.deleteCharAt(subjectnames.length()-1);
		subjectids.deleteCharAt(subjectids.length()-1);
		Gson gson=new Gson();
		JsonElement jsonElement = gson.toJsonTree(subjects);
		respObject.add("subjectJson",jsonElement);
		respObject.addProperty(STATUS, "success");
		}else{
			subjectnames.append("");
			subjectids.append("");
			respObject.addProperty(STATUS, "error");
		}
		
		respObject.addProperty("subjectnames", subjectnames.toString());
		respObject.addProperty("subjectids", subjectids.toString());
		
	}else if("getSubjectOfDivisionForExam".equalsIgnoreCase(methodToCall)){
		respObject.addProperty(STATUS, "success");
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId = null;
		if(0 == userBean.getRole() || !"".equals(regId)){
			if(null == regId){
				regId = userBean.getRegId();
			}
		}else{
			regId = userBean.getRegId();
		}
		String divisionId = req.getParameter("divisionId");
	String institute=	req.getParameter("institute");
	if(userBean.getRole()==2){
		regId=Integer.parseInt(institute);
	}
	
		List<Subject> subjects = subjectTransaction.getSubjectRelatedToDivForExam(Integer.parseInt(divisionId), regId);
		TeacherTransaction teacherTransaction=new TeacherTransaction();
		List<Subject> list=new ArrayList<Subject>();
		if(userBean.getRole()==2){
			list= teacherTransaction.getTeacherSubject(userBean.getRegId(), regId);
		}
		StringBuilder subjectids=new StringBuilder();
		StringBuilder subjectnames=new StringBuilder();
		StringBuilder subjectType=new StringBuilder();
		int i=0;
		if(null!=subjects && subjects.size()>0){
			if(userBean.getRole()==2){
				while(subjects.size()>i){
					Subject subject = subjects.get(i);
					if (list!=null) {
						int j=0;
						while (j<list.size()) {
						
							if(list.get(j).getSubjectId()==subject.getSubjectId())
							{
								subjectids.append(subject.getSubjectId()+",");
								subjectnames.append(subject.getSubjectName()+",");
								subjectType.append(subject.getSub_type()+",");
							}
						j++;
						}
					}
					
					i++;
				}
			}else{
		while(subjects.size()>i){
			Subject subject = subjects.get(i);
			subjectids.append(subject.getSubjectId()+",");
			subjectnames.append(subject.getSubjectName()+",");
			subjectType.append(subject.getSub_type()+",");
			i++;
		}
		}
		subjectnames.deleteCharAt(subjectnames.length()-1);
		subjectids.deleteCharAt(subjectids.length()-1);
		subjectType.deleteCharAt(subjectType.length()-1);
		Gson gson=new Gson();
		JsonElement jsonElement = gson.toJsonTree(subjects);
		respObject.add("subjectJson",jsonElement);
		respObject.addProperty(STATUS, "success");
		}else{
			subjectnames.append("");
			subjectids.append("");
			subjectType.append("");
			respObject.addProperty(STATUS, "error");
		}
		
		respObject.addProperty("subjectnames", subjectnames.toString());
		respObject.addProperty("subjectids", subjectids.toString());
		respObject.addProperty("subjectType", subjectType.toString());
		
	}else if("getBatchesByDivisionNSubject".equalsIgnoreCase(methodToCall)){
		respObject.addProperty(STATUS, "success");
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId = null;
		if(0 == userBean.getRole() || !"".equals(regId)){
			if(null == regId){
				regId = userBean.getRegId();
			}
		}else{
			regId = userBean.getRegId();
		}
		String divisionId = req.getParameter("divisionId");
		String subjectid = req.getParameter("subjectId");
		String institute = req.getParameter("institute");
		if(userBean.getRole()==2){
			regId=Integer.parseInt(institute);
		}
		BatchTransactions batchTransactions = new BatchTransactions();
		List<Batch> list = batchTransactions.getbachesrelatedtodivandsubject(subjectid, Integer.parseInt(divisionId), regId);
		Gson gson = new Gson();
		respObject.addProperty("batchlist", gson.toJson(list));
	}else if("getDivisionsTopics".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId=userBean.getRegId();;
		String divisionId = req.getParameter("divisionID");
		String subjectid = req.getParameter("subID");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List<Topics> topics=subjectTransaction.getTopics(userBean.getRegId(), Integer.parseInt(subjectid), Integer.parseInt(divisionId));
		String topic_names="";
		String topic_ids="";
		if(topics!=null){
		for (int i = 0; i < topics.size(); i++) {
			if(i==0){
				topic_names=topics.get(i).getTopic_name();
				topic_ids=topics.get(i).getTopic_id()+"";
			}else{
				topic_names=topic_names+","+topics.get(i).getTopic_name();
				topic_ids=topic_ids+","+topics.get(i).getTopic_id();
			}
		}
		}
		
		respObject.addProperty("topic_ids",topic_ids);
		respObject.addProperty("topic_names", topic_names);
		respObject.addProperty(STATUS, "success");
		if(topics !=null){
			if(topics.size()>0){
			Gson gson = new Gson();
			JsonElement jsonElement = gson.toJsonTree(topics);
			respObject.add("topicJson",jsonElement);
		}else{
			respObject.addProperty(STATUS, "error");
		}
		}
	}else if("deleteTopics".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		UserStatic userStatic=userBean.getUserStatic();
		Integer regId=userBean.getRegId();;
		String divisionId = req.getParameter("divisionID");
		String subjectid = req.getParameter("subID");
		String topicid=req.getParameter("topicid");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
		List<Integer> quesids=bankTransaction.getQuestionrelatedtoTopics(Integer.parseInt(subjectid), userBean.getRegId(), Integer.parseInt(divisionId), Integer.parseInt(topicid));
		ExamTransaction examTransaction=new ExamTransaction();
		String examname="";
		if(quesids!=null){
			
	List<Integer> examQuesIds=new ArrayList<Integer>();
	List<Integer> nonExamQuesIds=new ArrayList<Integer>();
		//for (int j = 0; j < quesids.size(); j++) {	
		/*List<Exam> list=examTransaction.isQuestionRelatedToTopicAvailableInExam(userBean.getRegId(), Integer.parseInt(subjectid), Integer.parseInt(divisionId), quesids);
		if(list!=null){
			
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < quesids.size(); j++) {
					boolean flag= false;
					String quesarr[]=list.get(i).getQue_ids().split(",");
					for (int k = 0; k < quesarr.length; k++) {
					if(quesarr[k].trim().equals((quesids.get(j)+""))){
						flag=true;
						break;
					}
					}
					if(flag==true){
						examQuesIds.add(quesids.get(j));
					}else{
						nonExamQuesIds.add(quesids.get(j));
					}
				}
				}
			if(nonExamQuesIds!=null){
				for (int i = 0; i < nonExamQuesIds.size(); i++) {
					String questionPath = userStatic.getExamPath()+File.separator+subjectid+File.separator+divisionId+File.separator+nonExamQuesIds.get(i);
					//uploadedMarks = (Integer) request.getSession().getAttribute("uploadedMarks");
					File file = new File(questionPath);
					if(file.exists()){
						try {
							delete(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			bankTransaction.deleteQuestionList(nonExamQuesIds, userBean.getRegId(), Integer.parseInt(subjectid), Integer.parseInt(divisionId));
			bankTransaction.ExamQuestionStatus(examQuesIds,  userBean.getRegId(), Integer.parseInt(subjectid), Integer.parseInt(divisionId));
			}else{*/
				for (int i = 0; i < quesids.size(); i++) {
					String questionPath = userStatic.getExamPath()+File.separator+subjectid+File.separator+divisionId+File.separator+quesids.get(i);
					//uploadedMarks = (Integer) request.getSession().getAttribute("uploadedMarks");
					File file = new File(questionPath);
					if(file.exists()){
						try {
							delete(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				bankTransaction.deleteQuestionList(quesids, userBean.getRegId(), Integer.parseInt(subjectid), Integer.parseInt(divisionId));
		//	}
		}
		subjectTransaction.deleteTopics(userBean.getRegId(), Integer.parseInt(subjectid), Integer.parseInt(divisionId),Integer.parseInt(topicid));
		respObject.addProperty(STATUS, "success");
	}else if("addTopic".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId=userBean.getRegId();;
		String divisionId = req.getParameter("divisionID");
		String subjectid = req.getParameter("subID");
		String topicname=req.getParameter("topicname");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		boolean status=false;
		status=	subjectTransaction.isTopicExists(userBean.getRegId(), Integer.parseInt(subjectid), Integer.parseInt(divisionId),topicname);
		if(status==true){
			respObject.addProperty("topicexists", "true");
		}else{
			subjectTransaction.addTopic(userBean.getRegId(), Integer.parseInt(subjectid), Integer.parseInt(divisionId),topicname);
			respObject.addProperty("topicexists", "");
		}
		respObject.addProperty(STATUS, "success");
	}else if("TopicExists".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId=userBean.getRegId();;
		String divisionId = req.getParameter("divisionID");
		String subjectid = req.getParameter("subID");
		String topicname=req.getParameter("topicname").trim();
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		
		boolean status=false;
		if(!topicname.equals("")){	
		status=	subjectTransaction.isTopicExists(userBean.getRegId(), Integer.parseInt(subjectid), Integer.parseInt(divisionId),topicname);
		if(status==true){
			respObject.addProperty("topicexists", "true");
		}else{
			respObject.addProperty("topicexists", "");
		}
		}else{
			respObject.addProperty("topicexists", "blank");
		}
		
		respObject.addProperty(STATUS, "success");
	}else if("edittopic".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId=userBean.getRegId();;
		String divisionId = req.getParameter("divisionID");
		String subjectid = req.getParameter("subID");
		String topicname=req.getParameter("topicname");
		String topicid=req.getParameter("edittopicid");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		boolean status=false;
		status=	subjectTransaction.isEditTopicExists(userBean.getRegId(), Integer.parseInt(subjectid), Integer.parseInt(divisionId),topicname,Integer.parseInt(topicid));
		if(status==true){
			respObject.addProperty("topicexists", "true");
		}else{
			subjectTransaction.updateTopic(userBean.getRegId(), Integer.parseInt(subjectid), Integer.parseInt(divisionId),topicname,Integer.parseInt(topicid));
			respObject.addProperty("topicexists", "");
		}
		respObject.addProperty(STATUS, "success");
	}else if("isStudentAttemptedExam".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId=userBean.getRegId();;
		String divisionId = req.getParameter("division");
		String subjectid = req.getParameter("subject");
		String examID=req.getParameter("examID");
		String institute=req.getParameter("institute");
		StudentMarksTransaction marksTransaction=new StudentMarksTransaction();
		if(marksTransaction.isExamSolvedByStudent(Integer.parseInt(examID), Integer.parseInt(institute), Integer.parseInt(subjectid), Integer.parseInt(divisionId), userBean.getRegId()))
		{
			respObject.addProperty("examsolved", "yes");
		}else{
			respObject.addProperty("examsolved", "no");
		}
		respObject.addProperty(STATUS, "success");
	}else if("isQuestionAvailableInExam".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId=userBean.getRegId();;
		String divisionId = req.getParameter("division");
		String subjectid = req.getParameter("subject");
		String questionNumber=req.getParameter("questionNumber");
		String institute=req.getParameter("institute");
		int inst_id=userBean.getRegId();
		if(!"".equals(institute) && institute!=null){
			inst_id=Integer.parseInt(institute);
		}
		ExamTransaction examTransaction=new ExamTransaction();
		/*List<Exam> list=examTransaction.isQuestionAvailableInExam(inst_id, Integer.parseInt(subjectid), Integer.parseInt(divisionId), questionNumber);
		if(list!=null){
			if(list.size()>0){
			String examname="";
			for (int i = 0; i < list.size(); i++) {
				if(i==0){
					examname=list.get(i).getExam_name();
				}else{
					examname=examname+","+list.get(i).getExam_name();
				}
			}
			respObject.addProperty("examnames", examname);
			respObject.addProperty("quesstatus", "Y");
			}else{
				respObject.addProperty("quesstatus", "");
			}
		}else{
			respObject.addProperty("quesstatus", "");
		}*/
		respObject.addProperty(STATUS, "success");
	}else if("reevaluate".equalsIgnoreCase(methodToCall)){
		ReEvaluateThreadRunner evaluateThreadRunner = new ReEvaluateThreadRunner();
	}else if("getgeneralnotification".equalsIgnoreCase(methodToCall)){
		List<GeneralNotification>notifications = new ArrayList<GeneralNotification>();
		
		for(int indexNotification = 0;indexNotification<5;indexNotification++){
			GeneralNotification generalNotification = new GeneralNotification();
			generalNotification.setNotificationDate(new java.util.Date());
			generalNotification.setNotificationTitle("Notification title");
			generalNotification.setNotificationMessage("have notification");
			generalNotification.setKeys(NOTIFICATION_KEYS.REEVALUATION);
			notifications.add(generalNotification);
		}
		Gson gson = new Gson();
		respObject.addProperty("notifications",gson.toJson(notifications));
		respObject.addProperty(STATUS, "success");
	}else if("removeaddedquestioninexam".equalsIgnoreCase(methodToCall)){
		req.getSession().setAttribute("questionsIds",null);
		respObject.addProperty(STATUS, "success");
	}else if("getweeklyschedule".equals(methodToCall)){
		Integer regId = null;
		try{
			regId = Integer.parseInt(req.getParameter("regId"));
		}catch(Exception e){
			e.printStackTrace();
			
		}
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	
		if(0 == userBean.getRole() || !"".equals(regId)){
			if(null == regId){
				regId = userBean.getRegId();
			}
		}else{
			regId = userBean.getRegId();
		}
		int studentclass=0;
		if(userBean.getRole()==3){
			studentclass=Integer.parseInt(req.getParameter("classid"));
		}
		String batchname=	req.getParameter("batchname");
		String batchdivision=req.getParameter("batchdivision");
		String date=req.getParameter("date");
		String dateString[]=date.split("/");
		Date date2=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[1])-1,Integer.parseInt( dateString[0]));
		ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
		List<Schedule> list=null;
		if(userBean.getRole()==3){
		StudentTransaction studentTransaction=new StudentTransaction();
		Student student=studentTransaction.getclassStudent(userBean.getRegId(), studentclass);
		list=scheduleTransaction.getWeeklySchedule(Integer.parseInt(batchname),date2,studentclass,student.getDiv_id());
		}else{
		list=scheduleTransaction.getWeeklySchedule(Integer.parseInt(batchname),date2,userBean.getRegId(),Integer.parseInt(batchdivision));
		}
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List<String> subjectList= subjectTransaction.getScheduleSubject(list);
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> teacherlist=registerTransaction.getScheduleTeacher(list);
		TeacherTransaction teacherTransaction=new TeacherTransaction();
		List<String> prefixs=new ArrayList<String>();
		if(userBean.getRole()==3){
			prefixs=teacherTransaction.getTeachersPrefix(list, studentclass);
		}else{
			prefixs=teacherTransaction.getTeachersPrefix(list, regId);
		}
		
		String subjects="";
		String tfirstname="";
		String tlastname="";
		String starttime="";
		String endtime="";
		String dates="";
		String prefix="";
		String alldates="";
		
		int counter=0;
		
		while (prefixs.size()>counter) {
			if (counter==0) {
				if(prefixs.get(counter)!=null)
				{
				prefix=prefixs.get(counter);
				}
			}else{
				prefix=prefix+","+prefixs.get(counter);
			}
			counter++;
		}
		counter=0;

		Calendar cal=Calendar.getInstance();
		
		for (int i = 0; i < 7; i++) {
			cal.set(date2.getYear()+1900, date2.getMonth(), date2.getDate());
			cal.add(Calendar.DATE, i);
			Date enddate=new Date(cal.getTimeInMillis());	
			if(i==0){
			dates=new SimpleDateFormat("dd-MM-yyyy").format(enddate);
			}else{
				dates=dates+","+new SimpleDateFormat("dd-MM-yyyy").format(enddate);
			}
			}
		DateFormat sdf = new SimpleDateFormat("kk:mm");
		DateFormat f2 = new SimpleDateFormat("h:mma");
		while(counter<list.size())
		{
			int starthour=list.get(counter).getStart_time().getHours();
			int startminute=list.get(counter).getStart_time().getMinutes();
			int endthour=list.get(counter).getEnd_time().getHours();
			int endtminute=list.get(counter).getEnd_time().getMinutes();
			String formattedstarttime=getFormattedTime(starthour, startminute);
			String formattedendtime=getFormattedTime(endthour, endtminute);
			if(counter==0)
			{
				subjects=subjectList.get(counter);
				tfirstname=teacherlist.get(counter).getFname();
				tlastname=teacherlist.get(counter).getLname();
				starttime=formattedstarttime;
				endtime=formattedendtime;
				
				alldates=new SimpleDateFormat("dd-MM-yyyy").format(list.get(counter).getDate());
			}else{
				subjects=subjects+","+subjectList.get(counter);
				tfirstname=tfirstname+","+teacherlist.get(counter).getFname();
				tlastname=tlastname+","+teacherlist.get(counter).getLname();
				starttime=starttime+","+formattedstarttime;
				endtime=endtime+","+formattedendtime;
				alldates=alldates+","+new SimpleDateFormat("dd-MM-yyyy").format(list.get(counter).getDate());
				
			}
			counter++;
			
		}
		
		
		respObject.addProperty("alldates", alldates);
		respObject.addProperty("subjects", subjects);
		respObject.addProperty("firstname", tfirstname);
		respObject.addProperty("lastname", tlastname);
		respObject.addProperty("starttime", starttime);
		respObject.addProperty("endtime", endtime);
		respObject.addProperty("dates", dates);
		respObject.addProperty("prefix", prefix);
		respObject.addProperty(STATUS, "success");
	}else if("getteacherweeklyschedule".equals(methodToCall)){
		Integer regId = null;
		
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		if(0 == userBean.getRole() || !"".equals(regId)){
			if(null == regId){
				regId = userBean.getRegId();
			}
		}else{
			regId = userBean.getRegId();
		}
		
		String classid=req.getParameter("classid");
		String date=req.getParameter("date");
		String dateString[]=date.split("/");
		Date scheduledate=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[1])-1,Integer.parseInt( dateString[0]));
		ScheduleTransaction  scheduleTransaction=new ScheduleTransaction();
		List<Schedule> schedules= scheduleTransaction.getTeachersWeeklySchedule(Integer.parseInt(classid), regId,scheduledate);
		BatchTransactions batchTransactions=new BatchTransactions();
		List<Batch> batchs =batchTransactions.getTeachersBatch(schedules);
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List<String> subjects=subjectTransaction.getScheduleSubject(schedules);
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		List<Division> divisions=divisionTransactions.getAllDivisions(Integer.parseInt(classid));
		int counter=0;
		
		String starttime="";
		String endtime="";
		String subject="";
		String batch="";
		String dates="";
		String alldates="";
		String division="";
		Calendar cal=Calendar.getInstance();
		
		for (int i = 0; i < 7; i++) {
			cal.set(scheduledate.getYear()+1900, scheduledate.getMonth(), scheduledate.getDate());
			cal.add(Calendar.DATE, i);
			Date enddate=new Date(cal.getTimeInMillis());	
			if(i==0){
			dates=new SimpleDateFormat("dd-MM-yyyy").format(enddate);
			}else{
				dates=dates+","+new SimpleDateFormat("dd-MM-yyyy").format(enddate);
			}
			}
		while(counter<schedules.size()){
			int starthour=schedules.get(counter).getStart_time().getHours();
			int startminute=schedules.get(counter).getStart_time().getMinutes();
			int endthour=schedules.get(counter).getEnd_time().getHours();
			int endtminute=schedules.get(counter).getEnd_time().getMinutes();
			String formattedstarttime=getFormattedTime(starthour, startminute);
			String formattedendtime=getFormattedTime(endthour, endtminute);
			if(counter==0)
			{
				starttime=formattedstarttime;
				endtime=formattedendtime;
				subject=subjects.get(counter);
				batch=batchs.get(counter).getBatch_name();
				//dates=new SimpleDateFormat("dd-MM-yyyy").format(schedules.get(counter).getDate());
				alldates=new SimpleDateFormat("dd-MM-yyyy").format(schedules.get(counter).getDate());
				for (int i = 0; i < divisions.size(); i++) {
					if(divisions.get(i).getDivId()==schedules.get(counter).getDiv_id())
					{
						division=divisions.get(i).getDivisionName()+" "+divisions.get(i).getStream();
					}
				}
			}else{
				starttime=starttime+","+formattedstarttime;
				endtime=endtime+","+ formattedendtime;
				subject=subject+","+subjects.get(counter);
				batch=batch+","+ batchs.get(counter).getBatch_name();
				/*if(!dates.contains(new SimpleDateFormat("dd-MM-yyyy").format(schedules.get(counter).getDate()))){
					dates=dates+","+new SimpleDateFormat("dd-MM-yyyy").format(schedules.get(counter).getDate());
					}*/
					alldates=alldates+","+new SimpleDateFormat("dd-MM-yyyy").format(schedules.get(counter).getDate());
					for (int i = 0; i < divisions.size(); i++) {
						if(divisions.get(i).getDivId()==schedules.get(counter).getDiv_id())
						{
							division=division+","+divisions.get(i).getDivisionName()+" "+divisions.get(i).getStream();
						}
					}
			}
			counter++;
		}
		
		respObject.addProperty("batch", batch);
		respObject.addProperty("starttime", starttime);
		respObject.addProperty("endtime", endtime);
		respObject.addProperty("subject", subject);
		respObject.addProperty("alldates", alldates);
		respObject.addProperty("dates", dates);
		respObject.addProperty("division", division);
		/*String dateString[]=date.split("/");
		Date date2=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[0])-1,Integer.parseInt( dateString[1]));
		Student student= studentTransaction.getclassStudent(regId,Integer.parseInt(classid));		
		BatchTransactions batchTransactions=new BatchTransactions();
		ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
		int counter =0;
		String batchids[]=student.getBatch_id().split(",");
		String batchnames="";
		String batchid="";
		while(batchids.length>counter)
		{
			Batch batch=batchTransactions.getBatch(Integer.parseInt(batchids[counter]));
			if(counter==0)
			{
				batchnames=batch.getBatch_name();
				batchid=batchids[counter];
				
			}else{
				batchnames=batchnames+","+batch.getBatch_name();
				batchid=batchid+","+batchids[counter];
			}
			counter++;	
		}
		respObject.addProperty("batchids", batchid);
	respObject.addProperty("batchnames", batchnames);*/	
	respObject.addProperty(STATUS, "success");
}else if("getDivisionBatches".equalsIgnoreCase(methodToCall)){
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	Integer regId=userBean.getRegId();;
	String divisionId = req.getParameter("divisionID");
	BatchTransactions batchTransactions=new BatchTransactions();
	List<Batch> list =batchTransactions.getBatchRelatedtoDivision(Integer.parseInt(divisionId));
	String batchnames="";
	String batchids="";
	if(list!=null){
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				if(i==0){
					batchnames=list.get(i).getBatch_name();
					batchids=list.get(i).getBatch_id()+"";
				}else{
					batchnames=batchnames+","+list.get(i).getBatch_name();
					batchids=batchids+","+list.get(i).getBatch_id();
				}
			}
		}
	}
	respObject.addProperty("batchnames", batchnames);
	respObject.addProperty("batchids", batchids);
	respObject.addProperty(STATUS, "success");
}else if("sendteachermessage".equals(methodToCall)){
	UserBean userBean = (UserBean) req.getSession().getAttribute("user");
	String message=(String) req.getParameter("message");
	/*String batch=(String) req.getParameter("batch");
	String date=(String) req.getParameter("date");
	String batchname=(String) req.getParameter("batchname");
	String dateString[]=date.split("/");
	Date msgdate=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[0])-1,Integer.parseInt( dateString[1]));*/
	NotificationGlobalTransation notificationGlobalTransation=new NotificationGlobalTransation();
	/*String batcharr[]=batch.split(",");
	
	if(batcharr.length>1){
		notification.setBatch("ALL");
	}else{
	notification.setBatch(batch.split("_")[0]);
	notification.setDiv_id(Integer.parseInt(batch.split("_")[1]));
	}*/
	Calendar cal=Calendar.getInstance();
	cal.setTime(new java.util.Date());
	Notification notification=new Notification();
	notification.setInstitute_id(userBean.getRegId());
	notification.setMessage(message);
	notification.setMsg_date(cal.getTime());
	notification.setBatch_name("Teachers");
	notification.setRole(2);
	NotificationTransaction transaction=new NotificationTransaction();
	transaction.add(notification);		
	int i=0;
	/*while(i<batcharr.length){
		notificationGlobalTransation.sendMessage(message, batcharr[i]);
		i++;
	}*/
	respObject.addProperty(STATUS, "success");
	
	}else if("isTopicRelatedQuestionAvailableInExam".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId=userBean.getRegId();;
		String divisionId = req.getParameter("division");
		String subjectid = req.getParameter("subject");
	//	String questionNumber=req.getParameter("questionNumber");
		String topic_id=req.getParameter("topicid");
		//String institute=req.getParameter("institute");
		int inst_id=userBean.getRegId();
		/*if(!"".equals(institute)){
			inst_id=Integer.parseInt(institute);
		}*/
		QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
		List<Integer> quesids=bankTransaction.getQuestionrelatedtoTopics(Integer.parseInt(subjectid), inst_id, Integer.parseInt(divisionId), Integer.parseInt(topic_id));
		ExamTransaction examTransaction=new ExamTransaction();
		String examname="";
		if(quesids!=null){
		//for (int j = 0; j < quesids.size(); j++) {	
		/*List<Exam> list=examTransaction.isQuestionRelatedToTopicAvailableInExam(inst_id, Integer.parseInt(subjectid), Integer.parseInt(divisionId), quesids);
		if(list!=null){
			
			for (int i = 0; i < list.size(); i++) {
				if("".equals(examname)){
					examname=list.get(i).getExam_name();
				}else{
					examname=examname+","+list.get(i).getExam_name();
				}
			}
			respObject.addProperty("examnames", examname);
			respObject.addProperty("quesstatus", "Y");
		}*/
		//	}
		}if("".equals(examname)){
			respObject.addProperty("quesstatus", "");
		}
		respObject.addProperty(STATUS, "success");
	}else if("getAdvertiseAvailability".equalsIgnoreCase(methodToCall)){
		//UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		//Integer regId=userBean.getRegId();;
		String advdate = req.getParameter("advdate");
		AdvertiseTransaction advertiseTransaction=new AdvertiseTransaction();
		String bookingID=ClassOwnerServlet.getBookigID();
		/*Advertisement advertisement=new Advertisement();
		advertisement.setBooking_id("123");
		advertisement.setFirst_name("suraj");
		advertisement.setLast_name("Ankush");
		advertisement.setEmail("sankush@gmail.com");
		advertisement.setAdvdate(new Date(new java.util.Date().getTime()));
		advertisement.setImage(new byte[(int) new File("C:/codebase/git/code/ClassApplication/src/main/webapp/images/2.png").length()]);
		advertiseTransaction.save(advertisement);*/
		String datearr[]=advdate.split("/");
		int count=advertiseTransaction.getCount(new Date(new Date(Integer.parseInt(datearr[2])-1900, Integer.parseInt(datearr[1])-1,Integer.parseInt(datearr[0])).getTime()));
		if(count>10){
			respObject.addProperty("availability", "no");
		}else{
			respObject.addProperty("availability", "yes");
		}
		respObject.addProperty(STATUS, "success");
	}else if("getInstituteStats".equalsIgnoreCase(methodToCall)){
		String instituteID = req.getParameter("instituteID");
		RegisterTransaction registerTransaction=new RegisterTransaction();
		RegisterBean bean=registerTransaction.getInstitute(instituteID);
		if(bean!=null){
		InstituteStatTransaction instituteStatTransaction=new InstituteStatTransaction();
		InstituteStats instituteStats=instituteStatTransaction.getStats(bean.getRegId());
		respObject.addProperty("classname",bean.getClassName());
		respObject.addProperty("classowner",bean.getFname()+" "+bean.getLname());
		respObject.addProperty("noofstudentIds",instituteStats.getAlloc_ids());
		respObject.addProperty("noofstudentIdsused",instituteStats.getUsed_ids());
		respObject.addProperty("memoryspace",instituteStats.getAlloc_memory());
		respObject.addProperty("memoryspaceused",instituteStats.getUsed_memory());
		if(bean.getInst_status()!=null){
		respObject.addProperty("inststatus",bean.getInst_status());
		}else{
			respObject.addProperty("inststatus","Enabled");
		}
		respObject.addProperty("found","");
		}else{
			respObject.addProperty("found","notfound");	
		}
		respObject.addProperty(STATUS, "success");
	}else if("addmemory".equalsIgnoreCase(methodToCall)){
		String instituteID = req.getParameter("instituteID");
		String memoryspace = req.getParameter("memoryspace");
		RegisterTransaction registerTransaction=new RegisterTransaction();
		RegisterBean bean=registerTransaction.getInstitute(instituteID);
		if(bean!=null){
			InstituteStatTransaction instituteStatTransaction=new InstituteStatTransaction();
			instituteStatTransaction.updateMemoryLimit(bean.getRegId(),Double.parseDouble(memoryspace));
			registerTransaction.updateRenewalDates(bean.getRegId());
			if("disabled".equals(bean.getInst_status())){
				InstituteStats instituteStats=instituteStatTransaction.getStats(bean.getRegId());
				if( (instituteStats.getAlloc_ids()>=instituteStats.getUsed_ids()) && (instituteStats.getAlloc_memory()>=instituteStats.getUsed_memory()))
				{
					registerTransaction.updateInstituteStatus(bean.getRegId(), "enabled");
				}
			}
		}	
		respObject.addProperty(STATUS, "success");
	}else if("addids".equalsIgnoreCase(methodToCall)){
		String instituteID = req.getParameter("instituteID");
		String noofids = req.getParameter("noofids");
		RegisterTransaction registerTransaction=new RegisterTransaction();
		RegisterBean bean=registerTransaction.getInstitute(instituteID);
		if(bean!=null){
			InstituteStatTransaction instituteStatTransaction=new InstituteStatTransaction();
			instituteStatTransaction.updateStudentIdLimit(bean.getRegId(),Integer.parseInt(noofids));
			registerTransaction.updateRenewalDates(bean.getRegId());
			if("disabled".equals(bean.getInst_status())){
				InstituteStats instituteStats=instituteStatTransaction.getStats(bean.getRegId());
				if( (instituteStats.getAlloc_ids()>=instituteStats.getUsed_ids()) && (instituteStats.getAlloc_memory()>=instituteStats.getUsed_memory()))
				{
					registerTransaction.updateInstituteStatus(bean.getRegId(), "enabled");
				}
			}
		}	
		respObject.addProperty(STATUS, "success");
	}else if("validateQuestionAvailibility".equalsIgnoreCase(methodToCall)){
		ExamTransaction examTransaction = new ExamTransaction();
		int sub_id = Integer.parseInt(req.getParameter("sub_id"));
		int div_id = Integer.parseInt(req.getParameter("div_id"));
		int marks = Integer.parseInt(req.getParameter("marks"));
		int count = Integer.parseInt(req.getParameter("count"));
		int maximumRepeatation = Integer.parseInt(req.getParameter("maximumRepeatation"));
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId=userBean.getRegId();;
		int inst_id = regId;
		/*boolean isQuestionAvailable = examTransaction.isQuestionsAvailable(sub_id, inst_id, div_id, marks, count, maximumRepeatation);
		respObject.addProperty("available", isQuestionAvailable);*/
		respObject.addProperty(STATUS, "success");
	}else if("navigatepage".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		int pageno = Integer.parseInt(req.getParameter("pageno"));
		Notes notes=(Notes) req.getSession().getAttribute("notes");
		NotesTransaction notesTransaction=new NotesTransaction();
		String filename=notesTransaction.getNotepathById(notes.getNotesid(),notes.getInst_id(),notes.getSubid(),notes.getDivid());
		UserStatic userStatic = userBean.getUserStatic();
		String storagePath = com.config.Constants.STORAGE_PATH+File.separator+notes.getInst_id();
		userStatic.setStorageSpace(storagePath);
		String path=userStatic.getNotesPath()+File.separator+notes.getDivid()+File.separator+notes.getSubid()+File.separator+filename;
		File file = new File(path);
		String base64="";
        try {
        	PDDocument document = PDDocument.load(new File(path));
			PDPageTree pdPages =  document.getPages();
			PDFRenderer pdfRenderer = new PDFRenderer(document);
				ByteArrayOutputStream stream=new ByteArrayOutputStream();
					BufferedImage bim = pdfRenderer.renderImageWithDPI(pageno, 100, ImageType.RGB);
				  req.getSession().setAttribute("notesimage_"+pageno, bim);
				     ImageIO.write(bim, "png", stream);
				     stream.flush();
				     byte b [] = stream.toByteArray();
				   	base64= javax.xml.bind.DatatypeConverter.printBase64Binary(b);
				    	stream.close();
				document.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        respObject.addProperty("base64", base64);
		respObject.addProperty(STATUS, "success");
	}else if("addTeacherSearch".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		String username=req.getParameter("username");
		String email=req.getParameter("email");
		RegisterTransaction registerTransaction=new RegisterTransaction();
		RegisterBean registerBean=registerTransaction.getRegisteredTeacher(username, email,userBean.getRegId());
		if (registerBean!=null) {
			respObject.addProperty("firstname",registerBean.getFname());
			respObject.addProperty("lastname",registerBean.getLname());
			respObject.addProperty("teacherID",registerBean.getRegId());
			respObject.addProperty(STATUS,"available");
		}else{
			respObject.addProperty(STATUS,"notavailable");
		}
		
	}else if("getAllBatches".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		BatchHelperBean batchHelperBean= new BatchHelperBean(userBean.getRegId());
		batchHelperBean.setBatchDetailsList();
		List<BatchDetails> batchList = new ArrayList<BatchDetails>();
		batchList = batchHelperBean.getBatchDetailsList();
		 req.getSession().setAttribute("instituteBatches", batchList);
		 Gson gson=new Gson();
		 JsonElement jsonElement = gson.toJsonTree(batchList);
		 respObject.add("instituteBatches", jsonElement);
		 respObject.addProperty(STATUS,"success");
	}else if("getAllSubjects".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List<Subjects> subjects=subjectTransaction.getAllClassSubjects(userBean.getRegId());
		 req.getSession().setAttribute("instituteSubjects", subjects);
		 Gson gson=new Gson();
		 JsonElement jsonElement = gson.toJsonTree(subjects);
		 respObject.add("instituteSubjects", jsonElement);
		 respObject.addProperty(STATUS,"success");
	}else if("getAllTeachers".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		TeacherHelperBean teacherHelperBean= new TeacherHelperBean();
		teacherHelperBean.setClass_id(userBean.getRegId());
		List<TeacherDetails> teacherList = teacherHelperBean.getTeachers();
		 req.getSession().setAttribute("instituteTeachers", teacherList);
		 Gson gson=new Gson();
		 JsonElement jsonElement = gson.toJsonTree(teacherList);
		 respObject.add("instituteTeachers", jsonElement);
		 respObject.addProperty(STATUS,"success");
	}else if("getStudentByLoginID".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		String classfloorID=req.getParameter("classfloorID");
		RegisterTransaction registerTransaction=new RegisterTransaction();
		RegisterBean registerBean=registerTransaction.getRegisteredUserByLoginID(classfloorID);
		if(registerBean!=null){
			StudentTransaction studentTransaction=new StudentTransaction();
			Student student=studentTransaction.getStudentByStudentID(classfloorID, userBean.getRegId());
			if(student==null){
			respObject.addProperty("firstname", registerBean.getFname());
			respObject.addProperty("lastname", registerBean.getLname());
			respObject.addProperty("email", registerBean.getEmail());
			respObject.addProperty("regID", registerBean.getRegId());
			respObject.addProperty("phone", registerBean.getPhone1());
			respObject.addProperty(STATUS,"valid");
			}else{
				respObject.addProperty(STATUS,"exists");
			}
		}else{
			respObject.addProperty(STATUS,"invalid");	
		}		 
	}else if("addStudentByID".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		String studentID=req.getParameter("studentID");
		String divisionId=req.getParameter("divisionId");
		String batchIDs=req.getParameter("batchIDs");
		String parentFname=req.getParameter("parentFname");
		String parentLname=req.getParameter("parentLname");
		String parentPhone=req.getParameter("parentPhone");
		String parentEmail=req.getParameter("parentEmail");
		StudentTransaction studentTransaction=new StudentTransaction();
		Student student=new Student();
		student.setBatch_id(batchIDs);
		student.setClass_id(userBean.getRegId());
		student.setDiv_id(Integer.parseInt(divisionId));
		student.setParentEmail(parentEmail);
		student.setParentFname(parentFname);
		student.setParentLname(parentLname);
		student.setParentPhone(parentPhone);
		student.setStudent_id(Integer.parseInt(studentID));
		studentTransaction.addUpdateDb(student);
		respObject.addProperty(STATUS,"success");
	}else if("addStudentByManually".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		String divisionId=req.getParameter("divisionId");
		String batchIDs=req.getParameter("batchIDs");
		String parentFname=req.getParameter("parentFname");
		String parentLname=req.getParameter("parentLname");
		String parentPhone=req.getParameter("parentPhone");
		String parentEmail=req.getParameter("parentEmail");
		String studentFname=req.getParameter("studentFname");
		String studentLname=req.getParameter("studentLname");
		String studentPhone=req.getParameter("studentPhone");
		String studentEmail=req.getParameter("studentEmail");
		String dob=req.getParameter("dob");
		String address=req.getParameter("address");
		String city=req.getParameter("city");
		String state=req.getParameter("state");
		RegisterTransaction registerTransaction=new RegisterTransaction();
		RegisterBean registerBean=new RegisterBean();
		registerBean.setFname(studentFname);
		registerBean.setMname(parentFname);
		registerBean.setLname(studentLname);
		registerBean.setPhone1(studentPhone);
		registerBean.setEmail(studentEmail);
		registerBean.setRole(3);
		registerBean.setCity(city);
		registerBean.setState(state);
		registerBean.setAddr1(address);
		registerBean.setDob(dob.replace("-", ""));
		registerBean.setCountry("INDIA");
		registerBean.setClassName("");
		String username="";
		String phone="";
		if(!"".equals(studentPhone) && null != studentPhone){
			phone=studentPhone;
		}else{
			phone=parentPhone;
		}
		int counter=1;
		switch (counter) {
		case 1:
			username=(studentFname.charAt(0))+""+(studentLname.charAt(0))+""+phone;
			if(registerTransaction.isUserExits(username)){
				counter++;
			}else{
			break;
			}
		case 2:
		    username=studentFname.charAt(0)+""+parentFname.charAt(0)+""+studentLname.charAt(0)+""+phone;
			if(registerTransaction.isUserExits(username)){
				counter++;
			}else{
			break;
			}
		case 3:
		    username=studentFname+""+phone;
			if(registerTransaction.isUserExits(username)){
				counter++;
			}else{
			break;
			}
		}
		registerBean.setLoginName(username);
		registerBean.setLoginPass(new java.util.Date().getTime()+"");
		registerTransaction.registerUser(registerBean);
		registerBean=registerTransaction.getRegisteredUserByLoginID(username);
		StudentTransaction studentTransaction=new StudentTransaction();
		Student student=new Student();
		student.setBatch_id(batchIDs);
		student.setClass_id(userBean.getRegId());
		student.setDiv_id(Integer.parseInt(divisionId));
		student.setParentEmail(parentEmail);
		student.setParentFname(parentFname);
		student.setParentLname(parentLname);
		student.setParentPhone(parentPhone);
		student.setStudent_id(registerBean.getRegId());
		studentTransaction.addUpdateDb(student);
		respObject.addProperty(STATUS,"success");
	}else if("fetchNamesForSuggestion".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		RegisterTransaction registerTransaction = new RegisterTransaction();
		List<String> namesList = registerTransaction.getNamesForSuggestion(userBean.getRegId());
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(namesList);
		respObject.add("names",jsonElement);
		respObject.addProperty(STATUS,"success");
	}else if("getStudentByName".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		String studentName = req.getParameter("studentName");
		String nameArray[] = studentName.split(" ");
		String fname =nameArray[0];
		String lname = "";
		if(nameArray.length > 1){
			lname = nameArray[1];
		}
		RegisterTransaction registerTransaction = new RegisterTransaction();
		List<RegisterBean> namesList = registerTransaction.getStudentByName(userBean.getRegId(), fname, lname);
		List<Integer> studentIDList = new ArrayList<Integer>();
		if (namesList != null) {
			for (int i = 0; i < namesList.size(); i++) {
				studentIDList.add(namesList.get(i).getRegId());
			}
		}
		StudentTransaction studentTransaction = new StudentTransaction();
		List<Student> studentsList = studentTransaction.getStudentByStudentIDs(studentIDList, userBean.getRegId());
		BatchTransactions batchTransactions=new BatchTransactions();
		List<Batch> batchList = batchTransactions.getAllBatches(userBean.getRegId());
		List<StudentDetails> studentDetailsList = new ArrayList<StudentDetails>();
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		List<Division> division = divisionTransactions.getAllDivisions(userBean.getRegId());
		if(studentsList != null){
			for (int i = 0; i < studentsList.size(); i++) {
				StudentDetails studentDetails=new StudentDetails();
				namesList.get(i).setLoginPass("");
				studentDetails.setStudentUserBean(namesList.get(i));
				boolean flag = false;
				for (int j = 0; j < division.size(); j++) {
					if(studentsList.get(i).getDiv_id() == division.get(j).getDivId())
					{
						studentDetails.setDivision(division.get(j));
						flag = true;
						break;
					}
				}
				
				/*if(flag = false){
					
				}*/
				String batchIDArray[] = studentsList.get(i).getBatch_id().split(",");
				List<Batch> studentBatchList = new ArrayList<Batch>();
				if(batchIDArray.length > 1){
					for (int j = 0; j < batchIDArray.length; j++) {
							for (int k = 0; k < batchList.size(); k++) {
								if(Integer.parseInt(batchIDArray[j]) ==  batchList.get(k).getBatch_id() 
										&& studentsList.get(i).getDiv_id() == batchList.get(k).getDiv_id()){
									studentBatchList.add(batchList.get(k));
									break;
								}
							}	
					}
				}else{
					if(!"".equals(batchIDArray[0]) ){
					for (int k = 0; k < batchList.size(); k++) {
						if(Integer.parseInt(batchIDArray[0]) ==  batchList.get(k).getBatch_id() 
								&& studentsList.get(i).getDiv_id() == batchList.get(k).getDiv_id()){
							studentBatchList.add(batchList.get(k));
							break;
						}
					}
					}
				}
				studentDetails.setBatches(studentBatchList);
				studentDetailsList.add(studentDetails);
			}
		}
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(studentDetailsList);
		respObject.add("studentList", jsonElement);
		respObject.addProperty(STATUS,"success");
	}else if("getAllClasses".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		List<Division> divisionList = divisionTransactions.getAllDivisions(userBean.getRegId());
		 Gson gson=new Gson();
		 JsonElement jsonElement = gson.toJsonTree(divisionList);
		 respObject.add("instituteClasses", jsonElement);
		 respObject.addProperty(STATUS,"success");
	}else if("getSearchedQuestions".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		int inst_id = userBean.getRegId();
		String division = req.getParameter("division");
		String subject = req.getParameter("subject");
		String questionType = req.getParameter("questionType");
		int currentPage = Integer.parseInt(req.getParameter("currentPage"));
		int marks = Integer.parseInt(req.getParameter("marks"));
		int topic = Integer.parseInt(req.getParameter("topic"));
		String institute = req.getParameter("inst_id");
		String deleteStatus = req.getParameter("deleteStatus");
		if(!"".equals(institute) && institute!=null){
			inst_id = Integer.parseInt(institute);
		}
		int totalPages=0;
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		int totalCount=bankTransaction.getTotalSearchedQuestionCount(marks, Integer.parseInt(subject), inst_id, Integer.parseInt(division),topic,questionType);
		if(currentPage==0 || "Y".equals(deleteStatus)){
		if(totalCount>0){
			int remainder=totalCount%50;
			totalPages=totalCount/50;
			if(remainder>0){
				totalPages++;
			}	
		}
		if(currentPage == 0){
		currentPage=1;
		}
		
		if(currentPage > totalPages){
			currentPage=totalPages;
			}
		}
		List<Questionbank> questionbanks=bankTransaction.getSearchedQuestions(marks, Integer.parseInt(subject), inst_id, Integer.parseInt(division),currentPage,topic,questionType);
		List<Integer> marksList=bankTransaction.getDistinctQuestionMarks(Integer.parseInt(subject), Integer.parseInt(division), inst_id,questionType);
		List<ParaQuestionBean> paragraphQuestionList=new ArrayList<ParaQuestionBean>();
		if("3".equals(questionType)){
			UserStatic userStatic = userBean.getUserStatic();
			for (int i = 0; i < questionbanks.size(); i++) {
				//String questionPath=userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionbanks.get(i).getQue_id();
				String questionPath=com.config.Constants.STORAGE_PATH+File.separatorChar+inst_id+File.separatorChar+"exam"+File.separatorChar+"paragraph"+File.separatorChar+ division +File.separatorChar + subject +File.separatorChar+questionbanks.get(i).getQue_id();
				ParaQuestionBean paraQuestionBean=(ParaQuestionBean) readObject(new File(questionPath));
				paragraphQuestionList.add(paraQuestionBean);
			}
		}
		
		Gson gson=new Gson();
		 JsonElement jsonElement = gson.toJsonTree(questionbanks);
		 JsonElement jsonMarksElement = gson.toJsonTree(marksList);
		 JsonElement jsonParagraphQuestion = gson.toJsonTree(paragraphQuestionList);
		 respObject.add("questionList", jsonElement);
		 respObject.addProperty("totalPages",totalPages);
		 respObject.addProperty("currentPage",currentPage);
		 respObject.add("marks", jsonMarksElement);
		 respObject.add("paragraphQuestion", jsonParagraphQuestion);
		respObject.addProperty(STATUS,"success");
	}else if("deleteQuestions".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		int inst_id = userBean.getRegId();
		String division = req.getParameter("division");
		String subject = req.getParameter("subject");
		int questionNumber = Integer.parseInt(req.getParameter("questionNumber"));
		String quesstatus = req.getParameter("quesstatus");
		String questionType = req.getParameter("questionType");
		if("".equals(quesstatus)){
			if("3".equals(questionType)){
			UserStatic userStatic = userBean.getUserStatic();
			String questionPath = userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionNumber;
			//uploadedMarks = (Integer) request.getSession().getAttribute("uploadedMarks");
			File file = new File(questionPath);
			if(file.exists()){
				try {
					delete(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
			bankTransaction.deleteQuestion(questionNumber, inst_id, Integer.parseInt(subject), Integer.parseInt(division));
			}else{
				QuestionBankTransaction questionBankTransaction=new QuestionBankTransaction();
				boolean updateStatus=questionBankTransaction.updateDeleteQuestionStatus(questionNumber, inst_id,Integer.parseInt(subject), Integer.parseInt(division));
		
			}
	}else if("customUserValidateNotesName".equals(methodToCall)){
		String[] notes=req.getParameter("notes").split(",");
		String[] notesrowid=req.getParameter("notesrowid").split(",");
		String filesize=req.getParameter("filesize");
		InstituteStatTransaction instituteStatTransaction=new InstituteStatTransaction();
		String overlappedIds="";
		for (int i = 0; i < notesrowid.length; i++) {
			for (int j = i+1; j < notesrowid.length; j++) {
				if(notes[i].trim().equalsIgnoreCase(notes[j])){
					if("".equals(overlappedIds)){
						overlappedIds=notesrowid[i]+","+notesrowid[j];
					}else{
						overlappedIds=overlappedIds+"/"+notesrowid[i]+","+notesrowid[j];
					}
				}
			}
			
		}
		String notesnamestatus="";
		if("".equals(overlappedIds)){
		String institute=req.getParameter("institute");
		int division=Integer.parseInt(req.getParameter("division"));
		int subject=Integer.parseInt(req.getParameter("subject"));
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		NotesTransaction notesTransaction=new NotesTransaction();
		int inst_id=userBean.getInst_id();
		if(institute!="" && institute !=null){
			inst_id=Integer.parseInt(institute);
		}
		InstituteStats instituteStats=instituteStatTransaction.getStats(inst_id);
		if(instituteStats.getAvail_memory()>0 && (instituteStats.getUsed_memory()+Double.parseDouble(filesize))<=instituteStats.getAlloc_memory())
		{
			for (int i = 0; i < notes.length; i++) {
			
		
		boolean flag=notesTransaction.validatenotesname(notes[i].trim(), inst_id,division,subject);
		if(flag){
			if("".equals(notesnamestatus)){
			notesnamestatus=notesrowid[i];
			}else{
				notesnamestatus=notesnamestatus+","+notesrowid[i];
				
			}
		}
		}
			respObject.addProperty("allocmemory", "");
		}else{
			respObject.addProperty("allocmemory", "exceeded");
		}
		}
		respObject.addProperty("notesnamestatus", notesnamestatus);
			respObject.addProperty("overlappedIds", overlappedIds);
		
		respObject.addProperty(STATUS, "success");
		
	}else if("customUsergetDivisionsTopics".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		Integer regId=userBean.getRegId();;
		String divisionId = req.getParameter("divisionID");
		String subjectid = req.getParameter("subID");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List<Topics> topics=subjectTransaction.getTopics(userBean.getInst_id(), Integer.parseInt(subjectid), Integer.parseInt(divisionId));
		String topic_names="";
		String topic_ids="";
		if(topics!=null){
		for (int i = 0; i < topics.size(); i++) {
			if(i==0){
				topic_names=topics.get(i).getTopic_name();
				topic_ids=topics.get(i).getTopic_id()+"";
			}else{
				topic_names=topic_names+","+topics.get(i).getTopic_name();
				topic_ids=topic_ids+","+topics.get(i).getTopic_id();
			}
		}
		}
		
		respObject.addProperty("topic_ids",topic_ids);
		respObject.addProperty("topic_names", topic_names);
		respObject.addProperty(STATUS, "success");
		if(topics !=null){
			if(topics.size()>0){
			Gson gson = new Gson();
			JsonElement jsonElement = gson.toJsonTree(topics);
			respObject.add("topicJson",jsonElement);
		}else{
			respObject.addProperty(STATUS, "error");
		}
		}
	}else if("customUserFetchSubjectTeacher".equals(methodToCall)){
		Integer regId = null;
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		regId = userBean.getInst_id();
		String subname=req.getParameter("subname");
		TeaherTransaction teaherTransaction=new TeaherTransaction();
		List<Teacher> list=teaherTransaction.getSubjectTeacher(subname,regId);
		List teacheridlist=new ArrayList();
		int index=0;
		while (list.size()>index) {
			
			teacheridlist.add(list.get(index).getUser_id());
			index++;	
		}
		String firstname="";
		String lastname="";
		String teacherid="";
		String suffixs="";
		if(list!=null && list.size()>0)
		{
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> teacherNames=registerTransaction.getTeacherName(teacheridlist);
		int i=0;
		for ( i = 0; i < list.size(); i++) {
			if(i==0){
				if(list.get(i).getSuffix()==null){
					suffixs="";
				}else{
				suffixs=list.get(i).getSuffix();
				}
			}else{
				if(list.get(i).getSuffix()==null){
					suffixs=suffixs+","+"";
				}else{
				suffixs=suffixs+","+list.get(i).getSuffix();
				}
			}
			
		}
		
		for(i=0;i<teacherNames.size();i++)
		{
			if(i==0)
			{
			firstname=teacherNames.get(i).getFname();
			}else{
				firstname=firstname+","+teacherNames.get(i).getFname();
			}
		}
		
		for(i=0;i<teacherNames.size();i++)
		{
			if(i==0)
			{
				lastname=teacherNames.get(i).getLname();
			}else{
			lastname=lastname+","+teacherNames.get(i).getLname();
			}
		}
		
		for(i=0;i<teacherNames.size();i++)
		{
			if(i==0)
			{
				teacherid=teacherNames.get(i).getRegId()+"";
			}else{
			teacherid=teacherid+","+teacherNames.get(i).getRegId();
			}
		}
	}
		respObject.addProperty("firstname", firstname);
		respObject.addProperty("lastname", lastname);
		respObject.addProperty("teacherid", teacherid);
		respObject.addProperty("suffix", suffixs);
		respObject.addProperty(STATUS, "success");
		
		
	}else if("getCustomUserSearchedQuestions".equalsIgnoreCase(methodToCall)){
		UserBean userBean = (UserBean) req.getSession().getAttribute("user");
		int inst_id = userBean.getInst_id();
		String division = req.getParameter("division");
		String subject = req.getParameter("subject");
		String questionType = req.getParameter("questionType");
		int currentPage = Integer.parseInt(req.getParameter("currentPage"));
		int marks = Integer.parseInt(req.getParameter("marks"));
		int topic = Integer.parseInt(req.getParameter("topic"));
		String institute = req.getParameter("inst_id");
		String deleteStatus = req.getParameter("deleteStatus");
		if(!"".equals(institute) && institute!=null){
			inst_id = Integer.parseInt(institute);
		}
		int totalPages=0;
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		int totalCount=bankTransaction.getTotalSearchedQuestionCount(marks, Integer.parseInt(subject), inst_id, Integer.parseInt(division),topic,questionType);
		if(currentPage==0 || "Y".equals(deleteStatus)){
		if(totalCount>0){
			int remainder=totalCount%50;
			totalPages=totalCount/50;
			if(remainder>0){
				totalPages++;
			}	
		}
		if(currentPage == 0){
		currentPage=1;
		}
		
		if(currentPage > totalPages){
			currentPage=totalPages;
			}
		}
		List<Questionbank> questionbanks=bankTransaction.getSearchedQuestions(marks, Integer.parseInt(subject), inst_id, Integer.parseInt(division),currentPage,topic,questionType);
		List<Integer> marksList=bankTransaction.getDistinctQuestionMarks(Integer.parseInt(subject), Integer.parseInt(division), inst_id,questionType);
		List<ParaQuestionBean> paragraphQuestionList=new ArrayList<ParaQuestionBean>();
		if("3".equals(questionType)){
			UserStatic userStatic = userBean.getUserStatic();
			for (int i = 0; i < questionbanks.size(); i++) {
				//String questionPath=userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionbanks.get(i).getQue_id();
				String questionPath=com.config.Constants.STORAGE_PATH+File.separatorChar+inst_id+File.separatorChar+"exam"+File.separatorChar+"paragraph"+File.separatorChar+ division +File.separatorChar + subject +File.separatorChar+questionbanks.get(i).getQue_id();
				ParaQuestionBean paraQuestionBean=(ParaQuestionBean) readObject(new File(questionPath));
				paragraphQuestionList.add(paraQuestionBean);
			}
		}
		
		Gson gson=new Gson();
		 JsonElement jsonElement = gson.toJsonTree(questionbanks);
		 JsonElement jsonMarksElement = gson.toJsonTree(marksList);
		 JsonElement jsonParagraphQuestion = gson.toJsonTree(paragraphQuestionList);
		 respObject.add("questionList", jsonElement);
		 respObject.addProperty("totalPages",totalPages);
		 respObject.addProperty("currentPage",currentPage);
		 respObject.add("marks", jsonMarksElement);
		 respObject.add("paragraphQuestion", jsonParagraphQuestion);
		respObject.addProperty(STATUS,"success");
	}
		printWriter.write(respObject.toString());
	}
	
	private Object readObject(File file) {
		Object object = null;
		FileInputStream fin = null;
		ObjectInputStream objectInputStream = null;
		try{
			fin = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(fin);
			object =  objectInputStream.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=objectInputStream)try {objectInputStream.close();} catch (IOException e) {e.printStackTrace();}
			if(null!=fin)try {fin.close();} catch (IOException e) {e.printStackTrace();}
		}
		return object;
	}
	
	public String getFormattedTime(int hour,int minute) {
		int temphour=hour;
		int tempminute=minute;
		String ampm="";
		if(hour==12)
		{
			ampm="PM";
		}else if (hour>12) {
			temphour=hour-12;
			ampm="PM";
		}else{
			ampm="AM";
		}
		if(minute<10){
			return temphour+":0"+tempminute+" "+ampm;
		}
		return temphour+":"+tempminute+" "+ampm;
	}
	
	private Student validateStudent(String studentLoginName, String batches, int divId,
			int regId, PrintWriter writer) {
		JsonObject respObject = new JsonObject();
	//	respObject.addProperty(STATUS, "error");
		int studentId = -1;
		RegisterBean studentBean = studentData.getStudent(studentLoginName);

		if (studentBean != null && batches != null && studentBean.getRole()==3) {
			String[] selectedBatches = batches.split(",");

			if (selectedBatches.length == 0
					|| (selectedBatches.length == 1 && selectedBatches[0]
							.equals(""))) {
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE,
						"Please select atleast one batch for student :"
								+ studentLoginName);
				writer.write(respObject.toString());
				return null;
			}
			Student student = new Student();
			student.setStudent_id(studentBean.getRegId());
			student.setDiv_id(divId);
			student.setClass_id(regId);
			student.setBatch_id(batches);
			return student;			
		} else {
			//respObject.addProperty(STATUS, "error");
			respObject.addProperty(MESSAGE,
					"Unable to find student with login name : "
							+ studentLoginName);
		}
		//writer.write(respObject.toString());
		return null;

	}
	
	private boolean validateStudentBatch(Student student, String batches,int regId, PrintWriter writer ){
		JsonObject respObject = new JsonObject();
		respObject.addProperty(STATUS, "error");
				
		if(batches!=null){
			BatchHelperBean batchHelper=new BatchHelperBean();
			batchHelper.setClass_id(regId);
			List<com.classapp.db.batch.Batch> batchList=batchHelper.getBatches();
			String[] selectedBatches=batches.split(",");
			
			if(selectedBatches.length==0 ||(selectedBatches.length==1 && selectedBatches[0].equals(""))){
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "Please select atleast one batch for student Id :"+student.getStudent_id());
				writer.write(respObject.toString());
				return false;
			}
			List<String> listOfInvalidBatches=new ArrayList<String>();
			int count=0;
			int sizeOfSelectedBatches=selectedBatches.length;
			int studentDivId=0;
			studentDivId=student.getDiv_id();
			if((Integer)studentDivId!=null) {
				for (int i = 0; i < selectedBatches.length; i++) {
					for(com.classapp.db.batch.Batch batch:batchList){
						
						if((batch.getBatch_id()==Integer.parseInt(selectedBatches[i]))&& batch.getDiv_id()==studentDivId){
							count++;
						}
						if((batch.getBatch_id()==Integer.parseInt(selectedBatches[i]))&& batch.getDiv_id()!=studentDivId){
							listOfInvalidBatches.add(batch.getBatch_name());
						}
					}
				}			
				
			}
			if(sizeOfSelectedBatches==count){
				return true;
			}else if(listOfInvalidBatches.size()>0){
				StringBuilder builder= new StringBuilder("Student with student id "+student.getStudent_id()+" can not assigned to batches: ");
				for (String invalidBatch : listOfInvalidBatches) {
					builder.append(invalidBatch+",");
				}
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, builder.toString().substring(0, builder.toString().length()-1));
			}
		}
		writer.write(respObject.toString());
		return false;
	}
	
	public boolean validatetime(List<Time> starttime,List<Time> endtime,List<Date> dates) {
		boolean flag =true;
		
		for (int i = 0; i < starttime.size(); i++) {
			if(starttime.get(i).getTime()>endtime.get(i).getTime())
			{
				return false;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");

			for (int j = 0; j < starttime.size(); j++) {
				if(i!=j)
				{
				if(starttime.get(i).getTime() >= starttime.get(j).getTime() && starttime.get(i).getTime()<endtime.get(j).getTime() && dates.get(i).getTime()==dates.get(j).getTime()){
					if("".equals(overlappedtimeids)){
						overlappedtimeids=sdf.format(starttime.get(i))+","+sdf.format(endtime.get(i));
					}else{
						overlappedtimeids=overlappedtimeids+"/"+sdf.format(starttime.get(i))+","+sdf.format(endtime.get(i));
					}
					flag= false;
				}else if(endtime.get(i).getTime() > starttime.get(j).getTime() && endtime.get(i).getTime()<=endtime.get(j).getTime() && dates.get(i).getTime()==dates.get(j).getTime())
				{
					if("".equals(overlappedtimeids)){
						overlappedtimeids=sdf.format(starttime.get(i))+","+sdf.format(endtime.get(i));;
					}else{
						overlappedtimeids=overlappedtimeids+"/"+sdf.format(starttime.get(i))+","+sdf.format(endtime.get(i));
					}
					flag= false;
				}else if(starttime.get(i).getTime()<starttime.get(j).getTime() && endtime.get(i).getTime()>endtime.get(j).getTime() && dates.get(i).getTime()==dates.get(j).getTime()){
					if("".equals(overlappedtimeids)){
						overlappedtimeids=sdf.format(starttime.get(i))+","+sdf.format(endtime.get(i));;
					}else{
						overlappedtimeids=overlappedtimeids+"/"+sdf.format(starttime.get(i))+","+sdf.format(endtime.get(i));
					}
					flag= false;
				}
			}
			}
		}
		return flag;
	}

	public static String getBookigID() {
		return new java.util.Date().getTime()+"";
	}
	
	public static void delete(File file)
	    	throws IOException{
	 
	    	if(file.isDirectory()){
	 
	    		//directory is empty, then delete it
	    		if(file.list().length==0){
	 
	    		   file.delete();
	    		   AppLogger.logger("Directory is deleted : " 
	                                                 + file.getAbsolutePath());
	 
	    		}else{
	 
	    		   //list all the directory contents
	        	   String files[] = file.list();
	 
	        	   for (String temp : files) {
	        	      //construct the file structure
	        	      File fileDelete = new File(file, temp);
	 
	        	      //recursive delete
	        	     delete(fileDelete);
	        	   }
	 
	        	   //check the directory again, if empty then delete it
	        	   if(file.list().length==0){
	           	     file.delete();
	        	     AppLogger.logger("Directory is deleted : " 
	                                                  + file.getAbsolutePath());
	        	   }
	    		}
	 
	    	}else{
	    		//if file, then delete it
	    		file.delete();
	    		AppLogger.logger("File is deleted : " + file.getAbsolutePath());
	    	}
	    }
	String overlappedtimeids="";
}
