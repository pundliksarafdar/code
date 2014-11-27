package com.admin.misc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
*/
import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Teacher.Teacher;
import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.db.batch.Batch;
import com.classapp.db.batch.BatchDB;
import com.classapp.db.batch.BatchDetails;
import com.classapp.db.batch.division.ClassDivision;
import com.classapp.db.batch.division.Division;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentData;
import com.classapp.db.student.StudentDetails;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Subjects;
import com.classapp.persistence.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.helper.BatchHelperBean;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.register.RegisterTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.teacher.TeaherTransaction;
import com.transaction.student.StudentTransaction;
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
			System.out.println("divisionName:"+divisionName);
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
			
			batch.setSub_id(subjects);
			
			if(batchTransactions.addUpdateDb(batch)){
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
			respObject.addProperty(STATUS, "success");
			com.classapp.db.subject.Subject subject = new com.classapp.db.subject.Subject();
			/*subject.setRegId(regId);*/
			subject.setSubjectName(subjectName);
			SubjectTransaction subjectTransaction = new SubjectTransaction();
			if(subjectTransaction.addUpdateSubjectToDb(subject,regId)){
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
		}*/else if(Constants.SEARCH_STUDENT.equals(methodToCall)){
			
			String studentLoginName=req.getParameter("studentLgName");
			if(studentLoginName.equals("")){
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "Login name can not be blank! Please enter login name of Student. ");											
			}else{
			List<StudentDetails> students=(List<StudentDetails>) req.getSession().getAttribute(Constants.STUDENT_LIST);
			
			req.getSession().setAttribute("studentSearchResult", null);
			boolean found=false;
			for (StudentDetails student :students) {
				if(student.getStudentUserBean().getLoginName().equals(studentLoginName)){
					req.getSession().setAttribute("studentSearchResult", student);
					req.getSession().setAttribute("studentSearchResultBatch", student);
					respObject.addProperty("studentId", student.getStudentId());
					respObject.addProperty("studentFname", student.getStudentUserBean().getFname());
					respObject.addProperty("studentLname", student.getStudentUserBean().getLname());
					found=true;
					break;
				}
			}
			
			if(!found){
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "Student with login name "+studentLoginName+" does not exists in class!");
			}else{
				respObject.addProperty(STATUS, "success");				
			}
			}
			//printWriter.write(respObject.toString());
			
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
			String divisionName=req.getParameter("divisionName");
			int divId= divisionTransactions.getDivisionIDByName(divisionName);
			Student student = validateStudent(studentLoginName, batchIds,divId,
					regId, printWriter);
			if (student != null) {
				if (studentTransaction.addUpdateDb(student)) {
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

			String divisionName = req.getParameter("divisionName");
			List<Batch> batches= batchTransactions.getAllBatchesOfDivision(divisionName, regId); 
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
					respObject.addProperty("batchIds", batchIds);
					respObject.addProperty("batchNames", batchNames);
				} else {
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "No batches found!");
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
			Student student=studentData.getStudentDetailsFromClass(studentId, regId);
			String batchIds=req.getParameter("batchIds");			
			boolean validStudent=validateStudentBatch(student,batchIds,regId, printWriter);
			if(validStudent){
				student.setBatch_id(batchIds);
				if(studentTransaction.updateStudentDb(student)){
					respObject.addProperty(STATUS, "success");
					respObject.addProperty(MESSAGE, "Successfully updated student.");
				}else{
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Error while updating the student with Id="+studentId+"!");
				}			
				//printWriter.write(respObject.toString());
			}
		}else if("deleteStudent".equals(methodToCall)){
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
			
			int deletStudentId=Integer.parseInt(req.getParameter("deleteStudentId"));										
			
				if(studentTransaction.deleteStudent(deletStudentId, regId)){
					respObject.addProperty(STATUS, "success");
					respObject.addProperty(MESSAGE, "Student Successfully deleted .");
				}else{
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Student does not exists in class!");
				}
				
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
				
				if(teacher!=null){
					teacher.setSub_ids(sub_Ids);
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
					
					String teacherLoginName=req.getParameter("teacherLgName");
					if(teacherLoginName.equals("")){
						respObject.addProperty(STATUS, "error");
						respObject.addProperty(MESSAGE, "Login name can not be blank! Please enter login name of teacher");											
					}else{
					List<TeacherDetails> teachers=(List<TeacherDetails>) req.getSession().getAttribute(Constants.TEACHER_LIST);
					
					req.getSession().setAttribute("teacherSearchResultSubjects", null);
					boolean found=false;
					for (TeacherDetails teacher :teachers) {
						if(teacher.getTeacherBean().getLoginName().equals(teacherLoginName)){
							req.getSession().setAttribute("teacherSearchResult", teacher);
							req.getSession().setAttribute("teacherSearchResultSubjects", teacher);
							respObject.addProperty("teacherId", teacher.getTeacherId());
							respObject.addProperty("teacherFname", teacher.getTeacherBean().getFname());
							respObject.addProperty("teacherLname", teacher.getTeacherBean().getLname());
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
			String teacherID = req.getParameter("teacherID");
			TeaherTransaction teaherTransaction=new TeaherTransaction();
			String stat=teaherTransaction.addTeacher(teacherID,regId,subjects);
			if("added".equals(stat))
			{
				respObject.addProperty(STATUS, "success");
			}else if("exists".equals(stat)){
				respObject.addProperty(MESSAGE, " Teacher is already exist");
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
			BatchTransactions batchTransactions=new BatchTransactions();
			List<Subjects> Batchsubjects=(List<Subjects>)batchTransactions.getBatcheSubject(batchID);
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
			List<Integer> list=teaherTransaction.getSubjectTeacher(subname,regId);
			String firstname="";
			String lastname="";
			String teacherid="";
			if(list!=null && list.size()>0)
			{
			RegisterTransaction registerTransaction=new RegisterTransaction();
			List<RegisterBean> teacherNames=registerTransaction.getTeacherName(list);
			int i=0;
			
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
			Date date=new Date(Integer.parseInt(dat[2])-1900, Integer.parseInt(dat[0])-1,Integer.parseInt(dat[1]));
			dateList.add(date);
				}
			}
			ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
			String exists=scheduleTransaction.isExistsLecture(regId+"", batchID, subList, teacherList, stList, edList, dateList);
			String teacherbusy="";
			String lectureexists="";
			if(exists.equals(""))
			{
			scheduleTransaction.addLecture(regId+"", batchID, subList, teacherList, stList, edList, dateList);
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
		   System.out.println(line);
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
			
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			boolean status=divisionTransactions.addDivision(division, regId);
			if(status)
			{
				respObject.addProperty(STATUS, "error");
			}else{
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
			
			String batchname=	req.getParameter("batchname");
			String date=req.getParameter("date");
			String dateString[]=date.split("/");
			Date date2=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[0])-1,Integer.parseInt( dateString[1]));
			ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
			List<Schedule> list=scheduleTransaction.getSchedule(Integer.parseInt(batchname),date2);
			SubjectTransaction subjectTransaction=new SubjectTransaction();
			List<String> subjectList= subjectTransaction.getScheduleSubject(list);
			RegisterTransaction registerTransaction=new RegisterTransaction();
			List<RegisterBean> teacherlist=registerTransaction.getScheduleTeacher(list);
			
			String subjects="";
			String tfirstname="";
			String tlastname="";
			String starttime="";
			String endtime="";
			String dates="";
			int counter=0;
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
					dates=list.get(counter).getDate()+"";
				}else{
					subjects=subjects+","+subjectList.get(counter);
					tfirstname=tfirstname+","+teacherlist.get(counter).getFname();
					tlastname=tlastname+","+teacherlist.get(counter).getLname();
					starttime=starttime+","+formattedstarttime;
					endtime=endtime+","+formattedendtime;
					dates=dates+","+list.get(counter).getDate();
					
				}
				counter++;
				
			}
			
			respObject.addProperty("subjects", subjects);
			respObject.addProperty("firstname", tfirstname);
			respObject.addProperty("lastname", tlastname);
			respObject.addProperty("starttime", starttime);
			respObject.addProperty("endtime", endtime);
			respObject.addProperty("dates", dates);
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
			BatchTransactions batchTransactions=new BatchTransactions();
			List<Subjects> Batchsubjects=(List<Subjects>)batchTransactions.getBatcheSubject(batchID);
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
			Date date2=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[0])-1,Integer.parseInt( dateString[1]));
			ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
			List<Schedule> list=scheduleTransaction.getSchedule(Integer.parseInt(batchname),date2);
			SubjectTransaction subjectTransaction=new SubjectTransaction();
			List<String> subjectList= subjectTransaction.getScheduleSubject(list);
			RegisterTransaction registerTransaction=new RegisterTransaction();
			List<RegisterBean> teacherlist=registerTransaction.getScheduleTeacher(list);
			TeaherTransaction teaherTransaction=new TeaherTransaction();
			List<List<RegisterBean>> teacherlists=teaherTransaction.getScheduleTeacher(list, regId);
			
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
				}else{
					TeacherFirstNames=TeacherFirstNames+","+teacherlists.get(counter).get(innercounter).getFname();
					TeacherlastNames=TeacherlastNames+","+teacherlists.get(counter).get(innercounter).getLname();
					Teacherids=Teacherids+","+teacherlists.get(counter).get(innercounter).getRegId();
				}
				innercounter++;
			}
			SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/YYYY");
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
			Date date=new Date(Integer.parseInt(dat[2])-1900, Integer.parseInt(dat[0])-1,Integer.parseInt(dat[1]));
			dateList.add(date);
			}
			ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
			String exists=scheduleTransaction.isTeacherUnavailable(regId+"", batchID, subList, teacherList, stList, edList, dateList);
			String teacherbusy="";
			String lectureexists="";
			if(exists.equals(""))
			{
			scheduleTransaction.updateLecture(regId+"", batchID, subList, teacherList, stList, edList, dateList,scheduleidsList);
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
			respObject.addProperty(STATUS, "success");	
			
		}
		else if(Constants.SEARCH_BATCH.equals(methodToCall)){
			
			String batchName=req.getParameter("batchName");
			if(batchName.equals("")){
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "batch name can not be blank! Please enter batch name of Student. ");											
			}else{
			List<BatchDetails> batches=(List<BatchDetails>) req.getSession().getAttribute(com.config.Constants.BATCHES_LIST);
			
			req.getSession().setAttribute("batchSearchResult", null);
			boolean found=false;
			for (BatchDetails batch :batches) {
				if(batch.getBatch().getBatch_name().equals(batchName)){
					req.getSession().setAttribute("batchSearchResult", batch);
					req.getSession().setAttribute("batchSearchResultBatch", batch);
					respObject.addProperty("batchId", batch.getBatch().getBatch_id());
					respObject.addProperty("batchName", batch.getBatch().getBatch_name());
					found=true;
					break;
				}
			}
			
			if(!found){
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "Batch with batch name "+batchName+" does not exists in class!");
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
			
			int batchId=Integer.parseInt(req.getParameter("batchId"));
			
			Batch batch=batchTransactions.getBatch(batchId);
			String sub_Ids=req.getParameter("subIds");			
			
			if(batch!=null){
				batch.setSub_id(sub_Ids);
				if(batchTransactions.addUpdateDb(batch)){
					respObject.addProperty(STATUS, "success");
					respObject.addProperty(MESSAGE, "Successfully updated batch.");
				}else{
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Error while updating the batch with Id="+batchId+"!");
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
			
			int deleteBatchId=Integer.parseInt(req.getParameter("batchId"));										
			Batch batch=batchTransactions.getBatch(deleteBatchId);
				if(batchTransactions.deleteBatch(batch)){
					respObject.addProperty(STATUS, "success");
					respObject.addProperty(MESSAGE, "Batch Successfully deleted .");
				}else{
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Batch does not exists in class!");
				}
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
				String dateString[]=date.split("/");
				//Date date2=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[0])-1,Integer.parseInt( dateString[1]));
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
			respObject.addProperty("batchnames", batchnames);	
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
			Date scheduledate=new Date(Integer.parseInt(dateString[2])-1900,Integer.parseInt( dateString[0])-1,Integer.parseInt( dateString[1]));
			ScheduleTransaction  scheduleTransaction=new ScheduleTransaction();
			List<Schedule> schedules= scheduleTransaction.getTeachersSchedule(Integer.parseInt(classid), regId,scheduledate);
			BatchTransactions batchTransactions=new BatchTransactions();
			List<Batch> batchs =batchTransactions.getTeachersBatch(schedules);
			SubjectTransaction subjectTransaction=new SubjectTransaction();
			List<String> subjects=subjectTransaction.getScheduleSubject(schedules);
			int counter=0;
			
			String starttime="";
			String endtime="";
			String subject="";
			String batch="";
			
			while(counter<schedules.size()){
				if(counter==0)
				{
					starttime=schedules.get(counter).getStart_time().toString();
					endtime=schedules.get(counter).getEnd_time().toString();
					subject=subjects.get(counter);
					batch=batchs.get(counter).getBatch_name();
				}else{
					starttime=starttime+","+ schedules.get(counter).getStart_time().toString();
					endtime=endtime+","+ schedules.get(counter).getEnd_time().toString();
					subject=subject+","+subjects.get(counter);
					batch=batch+","+ batchs.get(counter).getBatch_name();
				}
				counter++;
			}
			
			respObject.addProperty("batch", batch);
			respObject.addProperty("starttime", starttime);
			respObject.addProperty("endtime", endtime);
			respObject.addProperty("subject", subject);
			
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
		scheduleTransaction.deleteSchedule(Integer.parseInt(scheduleid));
			respObject.addProperty(STATUS, "success");
}
		
		
		printWriter.write(respObject.toString());
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
		
		return temphour+":"+tempminute+" "+ampm;
	}
	
	private Student validateStudent(String studentLoginName, String batches, int divId,
			int regId, PrintWriter writer) {
		JsonObject respObject = new JsonObject();
	//	respObject.addProperty(STATUS, "error");
		int studentId = -1;
		RegisterBean studentBean = studentData.getStudent(studentLoginName);

		if (studentBean != null && batches != null) {
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
	
/*	public void test() {
		try {
			InputStream input = new BufferedInputStream(
			        new FileInputStream("D:/test.xlsx"));
		//	POIFSFileSystem fileSystem=new POIFSFileSystem(input);
			XSSFWorkbook workbook=new XSSFWorkbook(input);
			XSSFSheet sheet=workbook.getSheetAt(0);
			Iterator rows = sheet.rowIterator(); 
			while(rows.hasNext())
			{
				XSSFRow row=(XSSFRow) rows.next();
				Iterator cells = row.cellIterator();
				while(cells.hasNext())
				{
					XSSFCell cell=(XSSFCell) cells.next();
					System.out.println(cell.getStringCellValue()+"");
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
