package com.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
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
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import com.classapp.db.Notes.Notes;
import com.classapp.db.Teacher.Teacher;
import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.db.attendance.Attendance;
import com.classapp.db.batch.Batch;
import com.classapp.db.batch.BatchDetails;
import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.Exam;
import com.classapp.db.exam.Exam_Paper;
import com.classapp.db.fees.BatchFees;
import com.classapp.db.fees.Fees;
import com.classapp.db.header.Header;
import com.classapp.db.printFees.PrintFees;
import com.classapp.db.printFees.PrintFeesDb;
import com.classapp.db.question.Questionbank;
import com.classapp.db.questionPaper.QuestionPaper;
import com.classapp.db.register.AdditionalFormFieldBeanDl;
import com.classapp.db.register.AdditionalStudentInfoBean;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDetails;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Subjects;
import com.classapp.db.subject.Topics;
import com.classapp.logger.AppLogger;
import com.classapp.login.UserStatic;
import com.config.Constants;
import com.excel.student.StudentExcelData;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.helper.BatchHelperBean;
import com.helper.TeacherHelperBean;
import com.miscfunction.MiscFunction;
import com.service.beans.AddBatchBean;
import com.service.beans.AttendanceScheduleServiceBean;
import com.service.beans.BatchFeesDistributionServiceBean;
import com.service.beans.BatchServiceBean;
import com.service.beans.BatchStudentExamMarks;
import com.service.beans.ClassOwnerNotificationBean;
import com.service.beans.ClassownerSettingsNotification;
import com.service.beans.EditQuestionPaper;
import com.service.beans.ExamSubject;
import com.service.beans.ExamWiseStudentDetails;
import com.service.beans.FeeStructure;
import com.service.beans.GenerateQuestionPaperResponse;
import com.service.beans.MonthlyScheduleServiceBean;
import com.service.beans.NewQuestionRequest;
import com.service.beans.ObjectiveExamBean;
import com.service.beans.ObjectiveOptions;
import com.service.beans.OnlineExamPaperSubjects;
import com.service.beans.ParaQuestionBean;
import com.service.beans.PrintDetailResponce;
import com.service.beans.ProgressCardServiceBean;
import com.service.beans.QuestionPaperData;
import com.service.beans.QuestionPaperFileElement;
import com.service.beans.QuestionPaperFileObject;
import com.service.beans.QuestionPaperPattern;
import com.service.beans.QuestionPaperStructure;
import com.service.beans.SendAcademicAlertAttendanceBean;
import com.service.beans.SendAcademicAlertFeeDueBean;
import com.service.beans.SendAcademicAlertProgressCardBean;
import com.service.beans.SendNotificationMesssageBean;
import com.service.beans.StudentData;
import com.service.beans.StudentDetailAttendanceData;
import com.service.beans.StudentExcelUploadBean;
import com.service.beans.StudentFeesServiceBean;
import com.service.beans.StudentRegisterServiceBean;
import com.service.beans.StudentSubjectMarks;
import com.service.beans.Student_Fees;
import com.service.beans.Student_Fees_Transaction;
import com.service.beans.SubjectiveExamBean;
import com.service.beans.SubjectsWithTopics;
import com.service.helper.NotificationServiceHelper;
import com.tranaction.header.HeaderTransaction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.attendance.AttendanceTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.certificate.CertificateTransaction;
import com.transaction.classownersettingtransaction.ClassownerSettingstransaction;
import com.transaction.exams.ExamTransaction;
import com.transaction.fee.FeesTransaction;
import com.transaction.image.ImageTransactions;
import com.transaction.institutestats.InstituteStatTransaction;
import com.transaction.notes.NotesTransaction;
import com.transaction.pattentransaction.QuestionPaperPatternTransaction;
import com.transaction.questionbank.ExcelFileTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.transaction.register.AdditionalFormFieldTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.transaction.teacher.TeaherTransaction;
import com.user.UserBean;
import com.util.ClassAppUtil;
import com.util.CryptoException;
import com.util.CryptoUtils;

@Path("/customuserservice") 
public class CustomUserService extends ServiceBase {
	private static final String UPLOADED_FILE_PATH = "C:"+File.separatorChar+"imageuploaded"+File.separatorChar;
	@Context
	private HttpServletRequest request;
	
	@POST
	@Path("/addClass")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addClass(Division division){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		division.setInstitute_id(userBean.getInst_id());
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		boolean status=divisionTransactions.addDivision(division, userBean.getInst_id());
		if(status == true){
			return Response.status(200).entity(false).build();
		}
		List<Division>	list=divisionTransactions.getAllDivisions(userBean.getInst_id());
		return Response.status(200).entity(list).build();
	}
	
	@POST
	@Path("/editClass")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editClass(Division division){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		division.setInstitute_id(userBean.getInst_id());
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		if(divisionTransactions.updateClass(division)){
			return Response.status(200).entity(false).build();
		}
		return Response.status(200).entity(true).build();
	}
	
	@DELETE
	@Path("/deleteDivision/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDivision(@PathParam("division") Integer division) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		divisionTransactions.deletedivision(userBean.getInst_id(),division);
		return Response.status(Response.Status.OK).build();
	}
	
	@GET
	@Path("/allInstituteSubjects")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getallInstituteSubjects(){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subjects> subjectList =  subjectTransaction.getAllClassSubjects(userBean.getInst_id());
		return Response.status(Status.OK).entity(subjectList).build();
	}
	
	@POST
	@Path("/addSubject")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSubject(Subject subject){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		subject.setInstitute_id(userBean.getInst_id());
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		if(subjectTransaction.addSubjectToDb(subject)){
			List<Subjects> subjects=subjectTransaction.getAllClassSubjects(userBean.getInst_id());
			return Response.status(200).entity(subjects).build();
		}
		return Response.status(200).entity(false).build();
	}
	
	@POST
	@Path("/updateSubject")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSubject(Subject subject){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		subject.setInstitute_id(userBean.getInst_id());
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		Boolean status=subjectTransaction.modifySubject(subject);
		return Response.status(200).entity(status).build();
	}
	
	@POST
	@Path("/updateCombineSubject")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCombineSubject(Subject subject){
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		subject.setInstitute_id(userBean.getInst_id());
		subject.setSub_type("1");
		boolean status = subjectTransaction.modifySubject(subject);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@DELETE
	@Path("/deleteSubject/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubject(@PathParam("subject") Integer sub_id) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		subjectTransaction.deleteSubject(userBean.getInst_id(),sub_id);
		List<Subjects> subjects=subjectTransaction.getAllClassSubjects(userBean.getInst_id());
		return Response.status(Response.Status.OK).entity(subjects).build();
	}
	
	@GET
	@Path("/getDivisionsTopics/{subject}/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubject(@PathParam("subject") Integer sub_id,@PathParam("division") Integer div_id) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List<Topics> topics=subjectTransaction.getTopics(userBean.getInst_id(), sub_id, div_id);
		return Response.status(Response.Status.OK).entity(topics).build();
	}
	
	@POST
	@Path("/addTopic")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addTopic(Topics topics) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		boolean status=false;
		status=	subjectTransaction.isTopicExists(userBean.getInst_id(), topics.getSub_id(), topics.getDiv_id(),topics.getTopic_name());
		if(status==true){
			return Response.status(Response.Status.OK).entity(false).build();	
		}else{
			subjectTransaction.addTopic(userBean.getInst_id(), topics.getSub_id(), topics.getDiv_id(),topics.getTopic_name());
			return Response.status(Response.Status.OK).entity(true).build();
		}
		
	}
	
	@POST
	@Path("/editTopic")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editTopic(Topics topics) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		boolean status=false;
		status=	subjectTransaction.isEditTopicExists(userBean.getInst_id(), topics.getSub_id(), topics.getDiv_id(),topics.getTopic_name(),topics.getTopic_id());
		if(status==true){
			return Response.status(Response.Status.OK).entity(false).build();	
		}else{
			subjectTransaction.updateTopic(userBean.getInst_id(), topics.getSub_id(), topics.getDiv_id(),topics.getTopic_name(),topics.getTopic_id());
			return Response.status(Response.Status.OK).entity(true).build();	
		}
		
	}
	
	@DELETE
	@Path("/deleteSubjectTopic/{division}/{subject}/{topic}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubjectTopic(@PathParam("division") Integer div_id,
			@PathParam("subject") Integer sub_id,@PathParam("topic") Integer topic_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		subjectTransaction.deleteTopics(userBean.getInst_id(), sub_id, div_id, topic_id);
		return Response.status(Response.Status.OK).build();
	}
	
	@GET
	@Path("/getAllBatches")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBatches() {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		BatchHelperBean batchHelperBean= new BatchHelperBean(userBean.getInst_id());
		batchHelperBean.setBatchDetailsList();
		List<BatchDetails> batchList = new ArrayList<BatchDetails>();
		batchList = batchHelperBean.getBatchDetailsList();
		return Response.status(Response.Status.OK).entity(batchList).build();	
	}
	
	@POST
	@Path("/addBatch")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addBatch(Batch batch) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		BatchTransactions batchTransactions = new BatchTransactions();
		batch.setClass_id(userBean.getInst_id());
		int batchId=batchTransactions.getNextBatchID(batch.getClass_id(), batch.getDiv_id());
		batch.setBatch_id(batchId);
		if(batchTransactions.isBatchExist(batch)){
			return Response.status(Response.Status.OK).entity(false).build();		
		}else{
		batchTransactions.addUpdateDb(batch);
		BatchHelperBean batchHelperBean= new BatchHelperBean(userBean.getInst_id());
		batchHelperBean.setBatchDetailsList();
		List<BatchDetails> batchList = new ArrayList<BatchDetails>();
		batchList = batchHelperBean.getBatchDetailsList();
		return Response.status(Response.Status.OK).entity(batchList).build();	
		}
		
	}
	
	@PUT
	@Path("/updateBatch")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateBatch(Batch batch) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		BatchTransactions batchTransactions = new BatchTransactions();
		batch.setClass_id(userBean.getInst_id());
		ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
		scheduleTransaction.deleteschedulerelatedtobatchsubject(batch, batch.getSub_id());
		if(!batchTransactions.isUpdatedBatchExist(batch)){
			batchTransactions.addUpdateDb(batch);
			BatchHelperBean batchHelperBean= new BatchHelperBean(userBean.getInst_id());
			batchHelperBean.setBatchDetailsList();
			List<BatchDetails> batchList = new ArrayList<BatchDetails>();
			batchList = batchHelperBean.getBatchDetailsList();
			return Response.status(Response.Status.OK).entity(batchList).build();	
		}else{
			return Response.status(Response.Status.OK).entity(false).build();	
		}		
	}
	
	@DELETE
	@Path("/deleteBatch/{division}/{batch}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteBatch(@PathParam("division") Integer div_id,@PathParam("batch") Integer batch_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		BatchTransactions batchTransactions = new BatchTransactions();
		batchTransactions.deleteBatch(userBean.getInst_id(), div_id, batch_id);
		return Response.status(Response.Status.OK).build();
	}
	
	@GET
	@Path("/getBatches/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBatchs(@PathParam("division")int division){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		BatchTransactions transactions = new BatchTransactions();
		List<Batch>batches = transactions.getAllBatchesOfDivision(division, userBean.getInst_id());
		return Response.status(Status.OK).entity(batches).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getBatchFees/{div_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBatchFees(@PathParam("div_id")int div_id,List<Integer> batchIdList) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		FeesTransaction feesTransaction = new FeesTransaction();
		List<BatchFees> batchFeesList = feesTransaction.getBatchFeesList(userBean.getInst_id(), div_id, batchIdList);
		return Response.status(Status.OK).entity(batchFeesList).build();
	}
	
	@GET
	@Path("/getStudentByLoginID/{classfloorID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentByLoginID(@PathParam("classfloorID")String classfloorID){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		RegisterTransaction registerTransaction=new RegisterTransaction();
		RegisterBean registerBean=registerTransaction.getRegisteredUserByLoginID(classfloorID);
		if(registerBean != null){
		StudentTransaction studentTransaction=new StudentTransaction();
		Student student=studentTransaction.getStudentByStudentID(classfloorID, userBean.getInst_id());
		if(student==null){
			registerBean.setLoginPass("");
			return Response.status(Status.OK).entity(registerBean).build();	
		}else{
		return Response.status(Status.OK).entity(false).build();
		}
		}else{
			registerBean = new RegisterBean();
			registerBean.setRegId(-1);
			return Response.status(Status.OK).entity(registerBean).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/formField")
	public Response getClassownerSettings(){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AdditionalFormFieldTransaction transaction = new AdditionalFormFieldTransaction();
		AdditionalFormFieldBeanDl bean = transaction.getAdditionalFormFieldBean(userBean.getInst_id());
		return Response.accepted(bean).build();
	}
	
	@POST
	@Path("/addStudentByManually/{division}/{batch}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response addStudentByManually(StudentRegisterServiceBean serviceBean,@PathParam("division")int division,@PathParam("batch")String batch ) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		FeesTransaction feesTransaction = new FeesTransaction();
		RegisterTransaction registerTransaction = new RegisterTransaction();
		InstituteStatTransaction statTransaction = new InstituteStatTransaction();
		boolean status  = true;
		if(statTransaction.isIDsAvailable(userBean.getInst_id(), 1)){
		if(!"".equals(serviceBean.getRegisterBean().getEmail()) && registerTransaction.isEmailExists(serviceBean.getRegisterBean().getEmail())){
			return Response.status(Status.OK).entity("email").build();
		}else{
		int student_id = registerTransaction.registerStudentManually(userBean.getInst_id(),serviceBean.getRegisterBean(), serviceBean.getStudent(),division,batch);
		statTransaction.increaseUsedStudentIds(userBean.getInst_id());
		for (Iterator iterator = serviceBean.getStudent_FeesList().iterator(); iterator
				.hasNext();) {
			Student_Fees student_Fees = (Student_Fees) iterator.next();
			student_Fees.setStudent_id(student_id);
		}
	  status = feesTransaction.saveStudentBatchFees(userBean.getInst_id(), serviceBean.getStudent_FeesList());
		if(status){
			NotificationServiceHelper helper = new NotificationServiceHelper();
			helper.sendFeesPaymentNotification(userBean.getInst_id(), serviceBean.getStudent_FeesList());
			List<Integer> list= new ArrayList<Integer>();
			list.add(student_id);
			if(!"".equals(serviceBean.getRegisterBean().getEmail())){
			helper.sendManualRegistrationNotification(userBean.getInst_id(), list);
			}
			
			StudentTransaction studentTransaction = new StudentTransaction();
			studentTransaction.saveStudentAdditionalInfo(serviceBean);
			
		}
		}
		}else{
			return Response.status(Status.OK).entity("ID").build();
		}
		return Response.status(Status.OK).entity("").build();
	}
	
	@POST
	@Path("/addStudentByID")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addStudentByID(StudentRegisterServiceBean serviceBean) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		FeesTransaction feesTransaction = new FeesTransaction();
		StudentTransaction studentTransaction = new StudentTransaction();
		InstituteStatTransaction statTransaction = new InstituteStatTransaction();
		boolean status  = true;
		if(statTransaction.isIDsAvailable(userBean.getInst_id(), 1)){
		status = studentTransaction.addStudentByID(userBean.getInst_id(),serviceBean.getStudent());
		if(status){
			studentTransaction.saveStudentAdditionalInfo(serviceBean);
		}
		statTransaction.increaseUsedStudentIds(userBean.getInst_id());
		status = feesTransaction.saveStudentBatchFees(userBean.getInst_id(), serviceBean.getStudent_FeesList());
		if(status){
			NotificationServiceHelper helper = new NotificationServiceHelper();
			//helper.sendFeesPaymentNotification(userBean.getInst_id(), serviceBean.getStudent_FeesList());
		}
		}else{
			status = false;
		}
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/addExcelFile/{fileId}")
	@Produces("application/json")
	public Response addExcelFile(@PathParam("fileId") String fileId) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExcelFileTransaction excelTransactions = new ExcelFileTransaction(Constants.STORAGE_PATH);
		String path=excelTransactions.copyExcelFile(userBean.getInst_id()+fileId,userBean.getInst_id());
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("fileid", path);
		return Response.status(200).entity(jsonObject.toString()).build();
	}
	
	@POST
    @Path("/upload/student/xls/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadStudentExcelFile(StudentExcelUploadBean studentFileBean) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
        System.out.println("Start uploadStudentExcelFile:");
        String response = "";
        int regId = userBean.getInst_id();
        HashMap<String, ArrayList<String>> invalidStudentResponseMap = null;
        StudentExcelData studentData = new StudentExcelData(studentFileBean.getFileName());
        System.out.println("Loading data..");
        int noOfStudents = studentData.loadStudents(regId, studentFileBean.getDivId(), studentFileBean.getBatchId());
        InstituteStatTransaction statTransaction = new InstituteStatTransaction();
        if(statTransaction.isIDsAvailable(userBean.getInst_id(), noOfStudents)){
        System.out.println("Loading completed..\n Processing the data..");
        studentData.processData(regId, studentFileBean.getDivId(), studentFileBean.getBatchId());
        System.out.println("Processing completed..");
        invalidStudentResponseMap = studentData.getInvalidStudentResponseMap();
        response = "Students added successfully";
        Set<Entry<Integer, Boolean>>  entrySet = studentData.getSuccessStudentMap().entrySet();
        ArrayList<Integer> studentIds = new ArrayList<Integer>();
        ArrayList<String> addedStudentResponseList = new ArrayList<String>();
        int successCount = 0;
        for (Entry<Integer, Boolean> entry : entrySet) {
            if (entry.getValue()) {
                response = "Student with student ID=" + entry.getKey() + " is added successfully";
                studentIds.add(entry.getKey());
                ++successCount;
            } else {
                response = "Unable to add student with student ID=" + entry.getKey();
            }
            addedStudentResponseList.add(response);
        }
        statTransaction.increaseUsedStudentIds(regId, noOfStudents);
        NotificationServiceHelper notificationServiceHelper = new NotificationServiceHelper();
        if(noOfStudents > 0){
        notificationServiceHelper.sendManualRegistrationNotification(regId, studentIds);
        notificationServiceHelper.getStudentForFeesPaymentNotification(regId, studentIds);
        }
        ArrayList<String> suuccessCountList = new ArrayList<String>();
        suuccessCountList.add("" + successCount + " students added successfully.");
        invalidStudentResponseMap.put("SUCCESS", suuccessCountList);
        invalidStudentResponseMap.put("addedStudentsResponse", addedStudentResponseList);
        invalidStudentResponseMap.put("IDERROR", null);
        }else{
        	invalidStudentResponseMap = new HashMap<String, ArrayList<String>>();
        	ArrayList<String> list = new ArrayList<String>();
        	list.add("Sufficient student ID's not available");
        	invalidStudentResponseMap.put("IDERROR", list);
        }
        return Response.status((Response.Status)Response.Status.OK).entity((Object)invalidStudentResponseMap).build();
    }
	
	@GET
	@Path("/getNamesForSuggestion")
	@Produces("application/json")
	public Response getNamesForSuggestion() {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		RegisterTransaction registerTransaction = new RegisterTransaction();
		List<String> namesList = registerTransaction.getNamesForSuggestion(userBean.getInst_id());
		return Response.status(200).entity(namesList).build();
	}
	
	@GET
	@Path("/getAllClasses")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllClasses(){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		List<Division> divisionList = divisionTransactions.getAllDivisions(userBean.getInst_id());
		return Response.status(Status.OK).entity(divisionList).build();
	}
	
	@GET
	@Path("/getStudentsRelatedtoBatch/{division}/{batch}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsRelatedtoBatch(@PathParam("division") int div_id,@PathParam("batch") String batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		List<StudentDetails> studentDetailsList = new ArrayList<StudentDetails>();
		int count=0;
		StudentTransaction studentTransaction=new StudentTransaction();
		count=studentTransaction.getStudentscountrelatedtobatch(batch_id,userBean.getInst_id(),div_id);
		if(count>0){
			List<Student> students=new ArrayList();
			if("".equals(batch_id)){
				students=studentTransaction.getUnallocatedStudentIDs(userBean.getInst_id());
			}else{
				students=studentTransaction.getStudentsrelatedtobatch(batch_id,userBean.getInst_id(),div_id);
			}
			List<Integer> studentIDsList = new ArrayList<Integer>();
			for (int i = 0; i < students.size(); i++) {
				studentIDsList.add(students.get(i).getStudent_id());
			}
			RegisterTransaction registerTransaction=new RegisterTransaction();
			List<RegisterBean> registerBeans= registerTransaction.getStudentsInfo(studentIDsList);
			BatchTransactions batchTransactions=new BatchTransactions();
			List<Batch> batchList = batchTransactions.getBatchRelatedtoDivision(div_id);
			
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			Division division = divisionTransactions.getDidvisionByID(div_id);
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
						if(jsonObject.has(batch_id)){
							int rollNo = jsonObject.get(batch_id).getAsInt();
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
		}
		return Response.status(Status.OK).entity(studentDetailsList).build();
	}
	
	@GET
	@Path("/getStudentByName/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentByName(@PathParam("name") String studentName){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		List<StudentDetails> studentDetailsList = new ArrayList<StudentDetails>();
		String nameArray[] = studentName.split(" ");
		String fname =nameArray[0];
		String lname = "";
		if(nameArray.length > 1){
			lname = nameArray[1];
		}
		RegisterTransaction registerTransaction = new RegisterTransaction();
		List<RegisterBean> namesList = registerTransaction.getStudentByName(userBean.getInst_id(), fname, lname);
		List<Integer> studentIDList = new ArrayList<Integer>();
		if (namesList != null) {
			for (int i = 0; i < namesList.size(); i++) {
				studentIDList.add(namesList.get(i).getRegId());
			}
		}
		StudentTransaction studentTransaction = new StudentTransaction();
		List<Student> studentsList = studentTransaction.getStudentByStudentIDs(studentIDList, userBean.getInst_id());
		BatchTransactions batchTransactions=new BatchTransactions();
		List<Batch> batchList = batchTransactions.getAllBatches(userBean.getInst_id());
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		List<Division> division = divisionTransactions.getAllDivisions(userBean.getInst_id());
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
		return Response.status(Status.OK).entity(studentDetailsList).build();
	}
	
	@POST
	@Path("/updateStudent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateStudent(Student student){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		StudentTransaction studentTransaction = new StudentTransaction();
		Student DBstudent = studentTransaction.getStudentByStudentID(student.getStudent_id(), userBean.getInst_id());
		DBstudent.setBatch_id(student.getBatch_id());
		DBstudent.setDiv_id(student.getDiv_id());
		studentTransaction.updateStudentDb(DBstudent);
		return Response.status(Status.OK).entity(true).build();
	}
	
	@DELETE
	@Path("/deleteStudent/{student}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteStudent(@PathParam("student") Integer student_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		StudentTransaction studentTransaction = new StudentTransaction();
		studentTransaction.deleteStudent(student_id,userBean.getInst_id());
		InstituteStatTransaction statTransaction = new  InstituteStatTransaction();
		statTransaction.decreaseUsedStudentIds(userBean.getInst_id());	
		return Response.status(Response.Status.OK).build();
	}
	
	@GET
	@Path("/getStudentDetails/{student_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentDetails(@PathParam("student_id")int student_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		RegisterTransaction registerTransaction=new RegisterTransaction();
		com.classapp.db.register.RegisterBean studentBean=registerTransaction.getregistereduser(student_id);
		if(!"M".equals(studentBean.getStatus()) && !"E".equals(studentBean.getStatus())){
		studentBean.setLoginPass("");
		}
		StudentTransaction studentTransaction=new StudentTransaction();
		com.classapp.db.student.Student student = studentTransaction.getStudentByStudentID(student_id, userBean.getInst_id());
		BatchTransactions batchTransactions = new BatchTransactions();
		List<Batch> batchList = batchTransactions.getBatchRelatedtoDivision(student.getDiv_id());
		DivisionTransactions divisionTransactions = new DivisionTransactions();
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
		StudentMarksTransaction marksTransaction = new  StudentMarksTransaction();
		ExamWiseStudentDetails examWiseStudentDetails = marksTransaction.getStudentDetailedMarks(userBean.getInst_id(), student_id);
		studentDetails.setExamWiseStudentDetails(examWiseStudentDetails);
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		StudentDetailAttendanceData attendanceData  = attendanceTransaction.getStudentYearlyAttendance(userBean.getInst_id(), student_id);
		studentDetails.setAttendanceData(attendanceData);
		FeesTransaction feesTransaction = new FeesTransaction();
		StudentFeesServiceBean feesServiceBean = feesTransaction.getStudentFees(userBean.getInst_id(), student_id);
		studentDetails.setFeesServiceBean(feesServiceBean);
		
		AdditionalStudentInfoBean bean1= studentTransaction.getAdditionalStudentInfoBean_(student_id, userBean.getInst_id());
		Type type = new TypeToken<HashMap<String, String>>(){}.getType();
		Map<String, String> retMap = new Gson().fromJson(bean1.getStudentData(),type);
		studentDetails.setAdditionalStudentInfoBean((HashMap<String, String>)retMap);
		studentDetails.setInstStudentId(bean1.getInstStudentId());
		
		AdditionalFormFieldTransaction transaction = new AdditionalFormFieldTransaction();
		AdditionalFormFieldBeanDl bean = transaction.getAdditionalFormFieldBean(userBean.getInst_id());
		studentDetails.setAdditionalFormFieldBeanDl(bean);
		return Response.status(Status.OK).entity(studentDetails).build();
	}
	
	@GET
	@Path("/getCertificateForPrint/{cert_id}/{student_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response getCertificateForPrint(@PathParam("cert_id")int cert_id,@PathParam("student_id")int student_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		CertificateTransaction certificateTransaction = new CertificateTransaction(Constants.STORAGE_PATH);
		String fileData = certificateTransaction.getPrintCertificateData(userBean.getInst_id(), cert_id,student_id);
		return Response.status(200).entity(fileData).build();
	}
	
	@GET
	@Path("/searchTeacher/{username}/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchTeacher(@PathParam("username")String username,@PathParam("email")String email){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		RegisterTransaction registerTransaction=new RegisterTransaction();
		RegisterBean registerBean=registerTransaction.getRegisteredTeacher(username, email);
		if(registerBean != null){
		registerBean.setLoginPass("");
		}
		return Response.status(Status.OK).entity(registerBean).build();
	}
	
	@POST
	@Path("/addTeacher/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addTeacher(Teacher teacher){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		teacher.setClass_id(userBean.getInst_id());
		TeaherTransaction teaherTransaction=new TeaherTransaction();
		String stat=teaherTransaction.addTeacher(teacher.getUser_id(),userBean.getInst_id(),teacher.getSub_ids(),teacher.getSuffix());
		if("added".equals(stat))
		{
			return Response.status(Status.OK).entity(true).build();
		}else{
			return Response.status(Status.OK).entity(false).build();
		}
		
	}
	
	@GET
	@Path("/getAllTeachers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTeachers(){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		TeacherHelperBean teacherHelperBean= new TeacherHelperBean();
		teacherHelperBean.setClass_id(userBean.getInst_id());
		List<TeacherDetails> teacherList = teacherHelperBean.getTeachers();
		return Response.status(Status.OK).entity(teacherList).build();
	}
	
	@PUT
	@Path("/updateTeacher/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateTeacher(Teacher teacher){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		TeacherTransaction teaherTransaction=new TeacherTransaction();
		Teacher DBteacher=teaherTransaction.getTeacher(teacher.getUser_id(), userBean.getInst_id());
		if(DBteacher!=null){
			DBteacher.setSub_ids(teacher.getSub_ids());
			DBteacher.setSuffix(teacher.getSuffix());
			if(teaherTransaction.updateTeacher(DBteacher)){
				return Response.status(Status.OK).entity(true).build();
			}else{
				return Response.status(Status.OK).entity(false).build();
			}	
	}
		return Response.status(Status.OK).entity(false).build();
	}
	
	@DELETE
	@Path("/deleteTeacher/{teacher}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTeacher(@PathParam("teacher") Integer teacher_id) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		TeacherTransaction teacherTransaction = new TeacherTransaction();
		teacherTransaction.deleteTeacher(teacher_id, userBean.getInst_id());
		return Response.status(Response.Status.OK).build();
	}
	
	@GET
	@Path("/getSubjectOfDivision/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubjectOfDivision(@PathParam("division") Integer division) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subject> subjects = subjectTransaction.getSubjectRelatedToDiv(division, userBean.getInst_id());
		return Response.status(Status.OK).entity(subjects).build();
	}
	
	@GET
	@Path("/getBatchesByDivisionNSubject/{division}/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBatchesByDivisionNSubject(@PathParam("division") Integer division,@PathParam("subject") String subject) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		BatchTransactions batchTransactions = new BatchTransactions();
		List<Batch> list = batchTransactions.getbachesrelatedtodivandsubject(subject, division, userBean.getInst_id());
		return Response.status(Status.OK).entity(list).build();
	}
	
	@GET
	@Path("/getNotes/{division}/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotes(@PathParam("division")int division,@PathParam("subject")int subject){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		NotesTransaction notesTransaction=new NotesTransaction();
		List<Notes> noteslist =notesTransaction.getNotesPath(division,subject, userBean.getInst_id());
		return Response.status(Status.OK).entity(noteslist).build();
	}
	
	@PUT
	@Path("/updateNotes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNotes(Notes notes){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
	    UserStatic userStatic = userBean.getUserStatic();
	    String storagePath = Constants.STORAGE_PATH+File.separator+userBean.getInst_id();
		userStatic.setStorageSpace(storagePath);
	    String destPath=  userStatic.getNotesPath()+File.separator+notes.getDivid()+File.separator+notes.getSubid();
		NotesTransaction notesTransaction=new NotesTransaction();
		Notes oldNotes = notesTransaction.getNotesById(notes.getNotesid(), userBean.getInst_id(), notes.getSubid(), notes.getDivid());
		int extentionStart = oldNotes.getNotespath().lastIndexOf(".");
		notes.setNotespath(notes.getName()+oldNotes.getNotespath().substring(extentionStart));
		boolean status = notesTransaction.updatenotes(notes.getName(),notes.getNotespath(), notes.getNotesid(), notes.getBatch(), userBean.getInst_id(), notes.getDivid(), notes.getSubid());
		if(status == false){
			File oldFile = new File(destPath+ File.separatorChar + oldNotes.getNotespath());
			File newFile = new File(destPath+ File.separatorChar + notes.getNotespath());	
			oldFile.renameTo(newFile);
		}
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/deleteNotes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNotes(Notes notes){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		UserStatic userStatic = userBean.getUserStatic();
	    String storagePath = Constants.STORAGE_PATH+File.separator+userBean.getInst_id();
		userStatic.setStorageSpace(storagePath);
	    String notesPath=  userStatic.getNotesPath()+File.separator+notes.getDivid()+File.separator+notes.getSubid();
		NotesTransaction notesTransaction=new NotesTransaction();
		notesTransaction.deleteNotes( notes.getNotesid(), userBean.getInst_id(), notes.getDivid(), notes.getSubid(),notesPath);
		return Response.status(Status.OK).entity(true).build();
	}
	@GET
	@Path("/getCombineSubjects/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCombineSubjects(@PathParam("subject")int subject){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subject> subjectList =  subjectTransaction.getCombineSubjects(userBean.getInst_id(), subject);
		return Response.status(Status.OK).entity(subjectList).build();
	}
	
	@POST
	@Path("/addQuestionPaperPattern")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response addQuestionPaperPattern(QuestionPaperPattern examPattern){
		Gson gson = new Gson();
		System.out.println(gson.toJson(examPattern));
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		UserStatic userStatic = userBean.getUserStatic();
	    String storagePath = Constants.STORAGE_PATH+File.separator+userBean.getInst_id();
		userStatic.setStorageSpace(storagePath);
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id());
		int  pattern_id= patternTransaction.saveQuestionPaperPattern(examPattern,getRegId());
		InstituteStatTransaction instituteStatTransaction = new InstituteStatTransaction();
		if(pattern_id == 0){
			return Response.status(Status.OK).entity("name").build();
		}else{
			boolean patternStatus = instituteStatTransaction.updateStorageSpace(userBean.getInst_id(), userBean.getUserStatic().getStorageSpace());
			if(patternStatus == false){
				patternTransaction.deleteQuestionPaperPattern(examPattern.getClass_id(), pattern_id);
				return Response.status(Status.OK).entity("memory").build();
			}
			
		}
		return Response.status(Status.OK).entity("").build();
	}
	
	@GET
	@Path("/getSubjectsAndTopicsForExam/{division}/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubjectsAndTopicsForExam(@PathParam("division") String division,@PathParam("subject") int subject_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id());
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		Map<Integer, SubjectsWithTopics> map = new HashMap<Integer,SubjectsWithTopics>();
		Subject subject = subjectTransaction.getSubject(subject_id);
		if("1".equals(subject.getSub_type())){
			String subjectArray[] = subject.getCom_subjects().split(",");
			for (String string : subjectArray) {
				Subject combineSubjects = subjectTransaction.getSubject( Integer.parseInt(string));
				List<Topics> topics = subjectTransaction.getTopics(userBean.getInst_id(), Integer.parseInt(string), Integer.parseInt(division));
				SubjectsWithTopics subjectsWithTopics = new SubjectsWithTopics();
				subjectsWithTopics.setSubject(combineSubjects);
				subjectsWithTopics.setTopics(topics);
				map.put(Integer.parseInt(string), subjectsWithTopics);
			}
			
		}else{
		List<Topics> topics = subjectTransaction.getTopics(userBean.getInst_id(), subject.getSubjectId(), Integer.parseInt(division));
		SubjectsWithTopics subjectsWithTopics = new SubjectsWithTopics();
		subjectsWithTopics.setSubject(subject);
		subjectsWithTopics.setTopics(topics);
		map.put(subject.getSubjectId(), subjectsWithTopics);
		}
		return Response.status(Status.OK).entity(map).build();
	}
	
	@GET
	@Path("/searchQuestionPaperPattern/{division}/{subject}/{patternType}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchQuestionPaperPattern(@PathParam("division") String division,@PathParam("subject") int subject,@PathParam("patternType") String patternType){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id());
		List<QuestionPaperPattern> questionPaperPatternList = patternTransaction.getQuestionPaperPatternList(Integer.parseInt(division),subject,patternType);
		return Response.status(Status.OK).entity(questionPaperPatternList).build();
	}
	
	@GET
	@Path("/getQuestionPaperPattern/{division}/{patternid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionPaperPattern(@PathParam("division") String division,@PathParam("patternid") String patternId){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id());
		QuestionPaperPattern questionPaperPattern = patternTransaction.getQuestionPaperPattern(Integer.parseInt(division), Integer.parseInt(patternId));
		return Response.status(Status.OK).entity(questionPaperPattern).build();
	}
	
	@POST
	@Path("/updateQuestionPaperPattern")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateQuestionPaperPattern(QuestionPaperPattern examPattern){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id());
		boolean patternStatus= patternTransaction.updateQuestionPaperPattern(examPattern,getRegId());
		if(patternStatus == false){
			return Response.status(Status.OK).entity(patternStatus).build();
		}
		return Response.status(Status.OK).entity(patternStatus).build();
	}
	
	@DELETE
	@Path("/deleteQuestionPaperPattern/{division}/{patternid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteQuestionPaperPattern(@PathParam("division") String division,@PathParam("patternid") String patternId){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id());
		boolean status = patternTransaction.deleteQuestionPaperPattern(Integer.parseInt(division), Integer.parseInt(patternId));
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getSubjectOfDivisionForExam/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubjectOfDivisionForExam(@PathParam("division") Integer division) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subject> subjects = subjectTransaction.getSubjectRelatedToDivForExam(division, userBean.getInst_id());
		return Response.status(Status.OK).entity(subjects).build();
	}
	
	@POST
	@Path("/generateQuestionPaper/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response generateQuestionPaper(@PathParam("division") String division,List<QuestionPaperStructure> paperStructure){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id(),userBean.getUserStatic().getExamPath());
		GenerateQuestionPaperResponse questionPaperResponse=patternTransaction.generateQuestionPaper(Integer.parseInt(division), paperStructure);
		return Response.status(Status.OK).entity(questionPaperResponse).build();
	}
	
	@POST
	@Path("/generateQuestionPaperSpecific/{division}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateQuestionPaperSpecific(@PathParam("division") String division,NewQuestionRequest newQuestionRequest){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id(),userBean.getUserStatic().getExamPath());
		GenerateQuestionPaperResponse questionPaperResponse=patternTransaction.generateQuestionPaperSpecific(Integer.parseInt(division), newQuestionRequest);
		return Response.status(Status.OK).entity(questionPaperResponse).build();
	}
	
	@POST
	@Path("/getQuestionList/{division}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionList(@PathParam("division") String division,NewQuestionRequest newQuestionRequest){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id(),userBean.getUserStatic().getExamPath());
		List<QuestionPaperData> questionPaperDataList = patternTransaction.getQuestionList(Integer.parseInt(division), newQuestionRequest);
		return Response.status(Status.OK).entity(questionPaperDataList).build();
	}
	
	@POST
	@Path("/saveQuestionPaper/{patternId}/{questionPaperName}/{divisionId}/{subject}/{compSubjectIds}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveQuestionPaper(@PathParam("patternId") String patternId,@PathParam("questionPaperName") String questionPaperName,
			@PathParam("divisionId") String divisionId,@PathParam("subject") int subject,@PathParam("compSubjectIds")String compSubjectIds
			,Map<String, QuestionPaperFileElement>questionAndItem){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id());
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		QuestionPaperPattern questionPaperPattern = patternTransaction.getQuestionPaperPattern(Integer.parseInt(divisionId), Integer.parseInt(patternId));
		List questionPaperPatternList = questionPaperPattern.getQuestionPaperStructure();
		
		/*Form element to save*/
		QuestionPaperFileObject fileObject = new QuestionPaperFileObject();
		fileObject.setClass_id(questionPaperPattern.getClass_id());
		fileObject.setInst_id(questionPaperPattern.getInst_id());
		fileObject.setMarks(questionPaperPattern.getMarks());
		//fileObject.setPaper_description(questionPaperPattern.);
		fileObject.setPattern_id(questionPaperPattern.getPattern_id());
		//Description is added in question item as its too long to send
		fileObject.setPaper_description(questionPaperName);
		List<QuestionPaperFileElement> questionPaperFileElements = new ArrayList<QuestionPaperFileElement>();
		for(int index=0;index<questionPaperPatternList.size();index++){
			QuestionPaperFileElement questionPaperFileElement = new QuestionPaperFileElement();
			QuestionPaperStructure questionPaperStructure = (QuestionPaperStructure) questionPaperPatternList.get(index);
			//Check if item is exist in it or not
			String itemId = questionPaperStructure.getItem_id();
			questionPaperFileElement.setAlternate_value(questionPaperStructure.getAlternate_value());
			questionPaperFileElement.setItem_description(questionPaperStructure.getItem_description());
			questionPaperFileElement.setItem_id(questionPaperStructure.getItem_id());
			questionPaperFileElement.setItem_marks(questionPaperStructure.getItem_marks());
			questionPaperFileElement.setItem_no(questionPaperStructure.getItem_no());
			questionPaperFileElement.setItem_type(questionPaperStructure.getItem_type());
			questionPaperFileElement.setParent_id(questionPaperStructure.getParent_id());
			//questionPaperFileElement.setQues_no(questionPaperStructure.get);
			questionPaperFileElement.setQuestion_topic(questionPaperStructure.getQuestion_topic());
			questionPaperFileElement.setQuestion_type(questionPaperStructure.getQuestion_type());
			questionPaperFileElement.setSubject_id(questionPaperStructure.getSubject_id());
			questionPaperFileElements.add(questionPaperFileElement);
			
			if(questionAndItem.containsKey(itemId)){
				QuestionPaperFileElement questionpaper= questionAndItem.get(itemId);
				questionPaperFileElement.setSubject_id(questionpaper.getSubject_id());
				questionPaperFileElement.setQuestion_topic(questionpaper.getQuestion_topic());
				questionPaperFileElement.setQues_no(questionpaper.getQues_no());
			}
		}
		fileObject.setQuestionPaperFileElementList(questionPaperFileElements);
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		boolean status = patternTransaction.saveQuestionPaper(fileObject, getRegId(),subject,compSubjectIds);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getQuestionPaperList/{division}/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionPaperList(@PathParam("division") String division,@PathParam("subject") int subject){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id(),userBean.getUserStatic().getExamPath());
		List<QuestionPaper> questionPaperList = patternTransaction.getQuestionPaperList(Integer.parseInt(division),subject);
		return Response.status(Status.OK).entity(questionPaperList).build();
	}
	
	@GET
	@Path("/getQuestionPaper/{division}/{paper_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionPaper(@PathParam("division") String division,@PathParam("paper_id") String paper_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id(),userBean.getUserStatic().getExamPath());
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		patternTransaction.setStoragePath(Constants.STORAGE_PATH);
		EditQuestionPaper fileObject = patternTransaction.getQuestionPaper(Integer.parseInt(division), Integer.parseInt(paper_id));
		return Response.status(Status.OK).entity(fileObject).build();
	}
	
	@POST
	@Path("/updateQuestionPaper/{patternId}/{questionPaperName}/{divisionId}/{paperId}/{compSubjectIds}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateQuestionPaper(@PathParam("patternId") String patternId,@PathParam("questionPaperName") String questionPaperName,
			@PathParam("divisionId") String divisionId,@PathParam("paperId") int paperId,@PathParam("compSubjectIds")String compSubjectIds
			,Map<String, QuestionPaperFileElement>questionAndItem){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id());
		QuestionPaperPattern questionPaperPattern = patternTransaction.getQuestionPaperPattern(Integer.parseInt(divisionId), Integer.parseInt(patternId));
		List questionPaperPatternList = questionPaperPattern.getQuestionPaperStructure();
		
		/*Form element to save*/
		QuestionPaperFileObject fileObject = new QuestionPaperFileObject();
		fileObject.setClass_id(questionPaperPattern.getClass_id());
		fileObject.setInst_id(questionPaperPattern.getInst_id());
		fileObject.setMarks(questionPaperPattern.getMarks());
		//fileObject.setPaper_description(questionPaperPattern.);
		fileObject.setPattern_id(questionPaperPattern.getPattern_id());
		//Description is added in question item as its too long to send
		fileObject.setPaper_description(questionPaperName);
		List<QuestionPaperFileElement> questionPaperFileElements = new ArrayList<QuestionPaperFileElement>();
		for(int index=0;index<questionPaperPatternList.size();index++){
			QuestionPaperFileElement questionPaperFileElement = new QuestionPaperFileElement();
			QuestionPaperStructure questionPaperStructure = (QuestionPaperStructure) questionPaperPatternList.get(index);
			//Check if item is exist in it or not
			String itemId = questionPaperStructure.getItem_id();
			questionPaperFileElement.setAlternate_value(questionPaperStructure.getAlternate_value());
			questionPaperFileElement.setItem_description(questionPaperStructure.getItem_description());
			questionPaperFileElement.setItem_id(questionPaperStructure.getItem_id());
			questionPaperFileElement.setItem_marks(questionPaperStructure.getItem_marks());
			questionPaperFileElement.setItem_no(questionPaperStructure.getItem_no());
			questionPaperFileElement.setItem_type(questionPaperStructure.getItem_type());
			questionPaperFileElement.setParent_id(questionPaperStructure.getParent_id());
			//questionPaperFileElement.setQues_no(questionPaperStructure.get);
			questionPaperFileElement.setQuestion_topic(questionPaperStructure.getQuestion_topic());
			questionPaperFileElement.setQuestion_type(questionPaperStructure.getQuestion_type());
			questionPaperFileElement.setSubject_id(questionPaperStructure.getSubject_id());
			questionPaperFileElements.add(questionPaperFileElement);
			
			if(questionAndItem.containsKey(itemId)){
				QuestionPaperFileElement questionpaper= questionAndItem.get(itemId);
				questionPaperFileElement.setSubject_id(questionpaper.getSubject_id());
				questionPaperFileElement.setQuestion_topic(questionpaper.getQuestion_topic());
				questionPaperFileElement.setQues_no(questionpaper.getQues_no());
			}
		}
		
		fileObject.setQuestionPaperFileElementList(questionPaperFileElements);
		fileObject.setPaper_id(paperId);
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		boolean status = patternTransaction.updateQuestionPaper(fileObject, getRegId(),compSubjectIds);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@DELETE
	@Path("/deleteQuestionPaper/{division}/{paper_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteQuestionPaper(@PathParam("division") String division,@PathParam("paper_id") String paper_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id(),userBean.getUserStatic().getExamPath());
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		boolean status = patternTransaction.deleteQuestionPaper(Integer.parseInt(division), Integer.parseInt(paper_id));
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getBatchSubjects/{division}/{batch}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBatchSubjects(@PathParam("division") int division,@PathParam("batch") String batch){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		BatchTransactions batchTransactions=new BatchTransactions();
		List<Subjects> Batchsubjects=(List<Subjects>)batchTransactions.getBatcheSubject(batch,userBean.getInst_id(),division);
		return Response.status(Status.OK).entity(Batchsubjects).build();
	}
	
	@POST
	@Path("/saveExamPaper/{examName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response saveExamPaper(@PathParam("examName") String exam,List<Exam_Paper> exam_PaperList){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		String msg = examTransaction.saveExamPaper(userBean.getInst_id(), exam, exam_PaperList, userBean.getRegId());
		return Response.status(Status.OK).entity(msg).build();
	}
	
	@GET
	@Path("/getHeader/{header_id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getHeader(@PathParam("header_id") String header_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		HeaderTransaction headerTransaction = new HeaderTransaction(userBean.getUserStatic().getHeaderPath());
		String header = headerTransaction.getHeader(header_id, userBean.getInst_id(), userBean.getUserStatic().getHeaderPath());
		return Response.status(Status.OK).entity(header).build();
	}
	
	@POST
	@Path("/getExam/{division}/{exam_id}/{batch_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExam(@PathParam("division") String division,@PathParam("exam_id") String exam_id,@PathParam("batch_id") int batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		List<Exam_Paper> exam_PaperList = examTransaction.getExamPapers(Integer.parseInt(division), userBean.getInst_id(), Integer.parseInt(exam_id),batch_id);
		return Response.status(Status.OK).entity(exam_PaperList).build();
	}
	
	@GET
	@Path("/getExamQuestionPaperList/{division}/{exam_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExamQuestionPaperList(@PathParam("division")int division,@PathParam("exam_id")int exam_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id(),userBean.getUserStatic().getExamPath());
		List<QuestionPaper> questionPaperList = patternTransaction.getQuestionPaperListRelatedToExam(division, exam_id);

		return Response.status(Status.OK).entity(questionPaperList).build();
	}
	
	@POST
	@Path("/getExamList/{division}/{batch_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExamList(@PathParam("division") String division,@PathParam("batch_id") int batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		List<Exam> examList = examTransaction.getExamList(Integer.parseInt(division), userBean.getInst_id(),batch_id);
		Gson gson = new Gson();
		String jsonElement = gson.toJson(examList);
		return Response.status(Status.OK).entity(jsonElement).build();
	}
	
	@POST
	@Path("/updateExamPaper/{examID}/{division}/{batch_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateExamPaper(@PathParam("examID") String examID,List<Exam_Paper> exam_PaperList,@PathParam("division") String division,@PathParam("batch_id") int batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		boolean status = examTransaction.updateExamPapers(Integer.parseInt(division), userBean.getInst_id(), Integer.parseInt(examID), exam_PaperList,userBean.getRegId(),batch_id);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@DELETE
	@Path("/exam/{exam_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeExam(@PathParam("exam_id")int examId){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		examTransaction.deleteExam(userBean.getInst_id(), examId);
		return Response.ok().build();
	}
	
	@GET
	@Path("/getOnlineExamList/{divId}/{batchId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOnlineExamList(
			@PathParam("divId")int div_id,@PathParam("batchId")int batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		List<Exam> examsList = examTransaction.getOnlineExamList(userBean.getInst_id(), div_id, batch_id);
		return Response.status(Status.OK).entity(examsList).build();
	}
	
	@GET
	@Path("/getOnlineExamSubjectList/{divId}/{batchId}/{exam}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOnlineExamSubjectList(
			@PathParam("divId")int div_id,@PathParam("batchId")int batch_id,@PathParam("exam")int exam_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		List<OnlineExamPaperSubjects> paperSubjectList = examTransaction.getOnlineExamSubjectList(userBean.getInst_id(), div_id, batch_id, exam_id);
		return Response.status(Status.OK).entity(paperSubjectList).build();
	}
	
	@GET
	@Path("/questionPaperAvailability/{paper_id}/{div_id}/{sub_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkQuestionPaperAvailability(@PathParam("paper_id")int paper_id,@PathParam("div_id")int div_id,@PathParam("sub_id")int sub_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),userBean.getInst_id(),userBean.getUserStatic().getExamPath());
		boolean status = patternTransaction.checkQuestionPaperAvailability(paper_id, userBean.getInst_id(), div_id, sub_id);

		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/getExamSubjects/{divId}/{batchId}/{exam_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExamSubjects(
			@PathParam("divId")int div_id,
			@PathParam("batchId")int batch_id,@PathParam("exam_id")int exam_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		List<ExamSubject> subjectList = new ArrayList<ExamSubject>();
		subjectList = examTransaction.getExamSubjects(userBean.getInst_id(), div_id, batch_id, exam_id);
		return Response.status(Status.OK).entity(subjectList).build();
	}
	
	@POST
	@Path("/getStudentForMarksFill/{divId}/{batchId}/{exam}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentForMarksFill(
			@PathParam("divId")int div_id,@PathParam("exam")int exam_id,
			@PathParam("batchId")String batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentData> studentDatas = studentTransaction.getStudentForExamMarks(batch_id, userBean.getInst_id(), div_id,exam_id);
		return Response.status(Status.OK).entity(studentDatas).build();
	}
	
	@POST
	@Path("/saveStudentMarks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStudentMarks(List<StudentData> studentDataList){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		StudentTransaction studentTransaction = new StudentTransaction();
		boolean status = studentTransaction.saveStudentMarks(studentDataList);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/getStudentMarksForUpdate/{divId}/{batchId}/{exam_id}/{sub_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentMarksForUpdate(
			@PathParam("divId")int div_id,@PathParam("exam_id")int exam_id,@PathParam("sub_id")int sub_id,
			@PathParam("batchId")String batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentData> studentDatas = studentTransaction.getStudentForExamMarksUpdate(batch_id, userBean.getInst_id(), div_id, exam_id, sub_id);
		return Response.status(Status.OK).entity(studentDatas).build();
	}
	
	@GET
	@Path("/getStudentExamMarks/{div_id}/{batch_id}/{exam_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentExamMarks(@PathParam("div_id") int div_id,@PathParam("batch_id") int batch_id,
										@PathParam("exam_id") int exam_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		BatchStudentExamMarks batchStudentExamMarks  = marksTransaction.getStudentExamMarks(userBean.getInst_id(), div_id, batch_id, exam_id);
		return Response.status(200).entity(batchStudentExamMarks).build();
	}
	
	@GET
	@Path("/getStudentExamSubjectMarks/{div_id}/{batch_id}/{sub_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentExamSubjectMarks(@PathParam("div_id") int div_id,@PathParam("batch_id") int batch_id,
										@PathParam("sub_id") int sub_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		StudentSubjectMarks studentSubjectMarks = marksTransaction.getStudentExamSubjectMarks(userBean.getInst_id(), div_id, batch_id, sub_id);
		return Response.status(200).entity(studentSubjectMarks).build();
	}
	
	@GET
	@Path("/getStudentForProgressCard/{divId}/{batchId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentForProgressCard(
			@PathParam("divId")int div_id,@PathParam("batchId")String batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentData> studentDatas = studentTransaction.getStudentForProgress(batch_id, userBean.getInst_id(), div_id);
		return Response.status(Status.OK).entity(studentDatas).build();
	}
	
	@GET
	@Path("/getStudentProgressCard/{div_id}/{batch_id}/{exam_ids}/{student_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentProgressCard(@PathParam("div_id") int div_id,@PathParam("batch_id") int batch_id,
										@PathParam("exam_ids") String exam_ids,@PathParam("student_id") int student_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		ProgressCardServiceBean progressCardServiceBean = marksTransaction.getStudentProgressCard(userBean.getInst_id(), div_id, batch_id, exam_ids, student_id);
		return Response.status(200).entity(progressCardServiceBean).build();
	}
	
	@GET
	@Path("/getScheduleForAttendance/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSchedule(@PathParam("batch") Integer batch,
			@PathParam("division") Integer division,@PathParam("date")long date) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List<AttendanceScheduleServiceBean> scheduleList = attendanceTransaction.getScheduleForAttendance(batch, new Date(date), userBean.getInst_id(), division);
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getStudentsForAttendance/{division}/{batch}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsForAttendance(@PathParam("batch") String batch,
			@PathParam("division") Integer division) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentForAttendance(batch, userBean.getInst_id(), division);
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@POST
	@Path("/saveStudentAttendance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStudentAttendance(List<Attendance> attendanceList) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		boolean status = attendanceTransaction.save(attendanceList,userBean.getInst_id());
		return Response.status(Response.Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/saveCommonDayStudentAttendance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveCommonDayStudentAttendance(List<Attendance> attendanceList) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		boolean status = attendanceTransaction.saveCommonDayAttendance(userBean.getInst_id(), attendanceList);
		return Response.status(Response.Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getScheduleForUpdateAttendance/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScheduleForUpdateAttendance(@PathParam("batch") Integer batch,
			@PathParam("division") Integer division,@PathParam("date")long date) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List<AttendanceScheduleServiceBean> scheduleList = attendanceTransaction.getScheduleForUpdateAttendance(batch, new Date(date), userBean.getInst_id(), division);
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getStudentsForAttendanceUpdate/{division}/{batch}/{sub_id}/{schedule_id}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsForAttendanceUpdate(@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("sub_id") Integer sub_id,@PathParam("schedule_id") Integer schedule_id,
			@PathParam("date") long date) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentForAttendanceUpdate(batch, userBean.getInst_id(), division, sub_id,schedule_id, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@POST
	@Path("/updateCommonDayStudentAttendance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCommonDayStudentAttendance(List<Attendance> attendanceList) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		boolean status = attendanceTransaction.updateCommonDayAttendance(userBean.getInst_id(), attendanceList);
		return Response.status(Response.Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/updateStudentAttendance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateStudentAttendance(List<Attendance> attendanceList) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		boolean status = attendanceTransaction.updateAttendance(attendanceList,userBean.getInst_id());
		return Response.status(Response.Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getStudentsDailyAttendance/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsDailyAttendance(@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("date") long date) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentsDailyAttendance(batch, userBean.getInst_id(), division, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@GET
	@Path("/getStudentsWeeklyAttendance/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsWeeklyAttendance(@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("date") long date) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentsWeeklyAttendance(batch, userBean.getInst_id(), division, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@GET
	@Path("/getStudentsMonthlyAttendance/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsMonthlyAttendance(@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("date") long date) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentsMonthlyAttendance(batch, userBean.getInst_id(), division, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@POST
	@Path("/saveFeeStructre")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveFeeStrucure(FeeStructure feeStructrure){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		feeStructrure.getFees().setInst_id(userBean.getInst_id());
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.saveFeeStructure(feeStructrure.getFees(), feeStructrure.getFeesStructureList());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getAllFeeStructre")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFeeStrucure(){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		FeesTransaction feesTransaction = new FeesTransaction();
		List<Fees> feesList = feesTransaction.getAllFees(userBean.getInst_id());
		return Response.status(Status.OK).entity(feesList).build();
	}
	
	@GET
	@Path("/getFeeStructre/{fees_ID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFeeStrucure(@PathParam("fees_ID") String fees_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		FeesTransaction feesTransaction = new FeesTransaction();
		FeeStructure feeStructureList = feesTransaction.getFeeStructurelist(userBean.getInst_id(), Integer.parseInt(fees_id));
		return Response.status(Status.OK).entity(feeStructureList).build();
	}
	
	@POST
	@Path("/updateFeeStructre")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFeeStrucure(FeeStructure feeStructrure){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		feeStructrure.getFees().setInst_id(userBean.getInst_id());
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.updateFeeStructure(feeStructrure.getFees(), feeStructrure.getFeesStructureList());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@DELETE
	@Path("/deleteFeeStructre/{fees_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteFeeStrucure(@PathParam("fees_id") String fees_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.deleteFees(userBean.getInst_id(), Integer.parseInt(fees_id));
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getInstituteBatch/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInstituteBatch(@PathParam("division") String division){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		FeesTransaction feesTransaction = new FeesTransaction();
		List<BatchServiceBean> serviceBeanList = feesTransaction.getInstituteBatch(Integer.parseInt(division), userBean.getInst_id());
		return Response.status(Status.OK).entity(serviceBeanList).build();
	}
	
	@GET
	@Path("/getBatchFeesDistribution/{division}/{batchID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBatchFeesDistribution(@PathParam("division") String division,@PathParam("batchID") String batchID){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		FeesTransaction feesTransaction = new FeesTransaction();
		BatchFeesDistributionServiceBean serviceBean = feesTransaction.getBatchFeesDistribution(userBean.getInst_id(), Integer.parseInt(division),Integer.parseInt(batchID));
		return Response.status(Status.OK).entity(serviceBean).build();
	}
	
	@POST
	@Path("/saveBatchFeesDistribution")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveBatchFeesDistribution(BatchFeesDistributionServiceBean serviceBean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.saveBatchFeesDistribution(serviceBean,userBean.getInst_id());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/updateBatchFeesDistribution")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateBatchFeesDistribution(BatchFeesDistributionServiceBean serviceBean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		FeesTransaction feesTransaction = new FeesTransaction();
		boolean status = feesTransaction.updateBatchFeesDistribution(serviceBean,userBean.getInst_id());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getAllBatchStudentsFees/{div_id}/{batch_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBatchStudentsFees(@PathParam("div_id")int div_id,@PathParam("batch_id")int batch_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		FeesTransaction feesTransaction = new FeesTransaction();
		List list = feesTransaction.getAllBatchStudentsFees(userBean.getInst_id(), div_id, batch_id);
		return Response.status(Status.OK).entity(list).build();
	}
	
	@POST
	@Path("/saveStudentBatchFeesTransaction")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStudentBatchFeesTransaction(Student_Fees_Transaction serviceFees_Transaction) throws IOException{
		FeesTransaction feesTransaction = new FeesTransaction();
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		boolean status = feesTransaction.saveStudentBatchFeesTransaction(userBean.getInst_id(), serviceFees_Transaction);
		if(status){
			NotificationServiceHelper helper = new NotificationServiceHelper();
			helper.sendFeesPaymentNotification(userBean.getInst_id(), serviceFees_Transaction);
			
			PrintDetailResponce printDetailResponce = feesTransaction.saveFees(serviceFees_Transaction, "path", "type");
			printDetailResponce.setInstituteName(getUserBean().getClassName());
			printDetailResponce.setAddress(getUserBean().getAddr1()+","+getUserBean().getCity());
			printDetailResponce.setFeesPayingNow(serviceFees_Transaction.getAmt_paid());
			DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		    
	    	sdf.setTimeZone(TimeZone.getTimeZone("IST"));
	    	java.util.Date date = new java.util.Date();
	    	printDetailResponce.setTodaysDate(sdf.format(date));
		    
	    	/***********Amount paid in *******/
	    	String amountInWords = ClassAppUtil.convert((long)serviceFees_Transaction.getAmt_paid());
	    	printDetailResponce.setAmmountInWords(amountInWords+" rupees only");
			MustacheFactory mf = new DefaultMustacheFactory();
			ServletContext context = MiscFunction.getServletContext();
			String fullPath = context.getRealPath("/WEB-INF/classes/htmlfile/feereceipt.tmpl");
			File f = new File(fullPath);
			if(!f.exists()){
				f.mkdirs();
			}
			String receiptPath = getUserBean().getUserStatic().getFeesReceiptPath();
			File receiptFile = new File(receiptPath);
			if(!receiptFile.exists()){
				receiptFile.mkdirs();
			}
			String fileName = receiptPath+File.separator+printDetailResponce.getFeeReceiptNumber()+".html";
			Mustache mustache = mf.compile(new InputStreamReader(new FileInputStream(f),Charset.forName("UTF-8")),f.getName());
			FileWriter fileWriter = new FileWriter(fileName);
			mustache.execute(fileWriter, printDetailResponce).flush();
			fileWriter.close();
			/*******************Encrypt file*******************/
			try {
				CryptoUtils.encrypt(fileName);
	        } catch (CryptoException ex) {
	            ex.printStackTrace();
	        }
	        
		}
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/feeReceipt/{divId}/{batchId}/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFeeReceiptsData(@PathParam("divId")int div_id,
			@PathParam("batchId")int batchId,
			@PathParam("studentId")int studentId){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		PrintFeesDb printFeesDb = new PrintFeesDb();
		List<PrintFees> printFees = printFeesDb.getFeesDetails(studentId, userBean.getInst_id());
		return Response.accepted(printFees).build();
	}
	
	@POST @Path("/sendFeesDue/{div_id}/{batch_id}/{sms}/{email}/{parent}/{student}") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response sendFeesDue( String studentIds,@PathParam("div_id")int div_id,@PathParam("batch_id")int batch_id,
			@PathParam("sms") boolean sms,@PathParam("email") boolean email,@PathParam("parent") boolean parent,@PathParam("student") boolean student){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		NotificationServiceHelper helper = new NotificationServiceHelper();
		List<Integer> studntIdList = Stream.of(studentIds.split(","))
		        .map(Integer::parseInt)
		        .collect(Collectors.toList());
		HashMap statusMap = helper.sendFeesDue(userBean.getInst_id(), div_id, batch_id, studntIdList,email,sms,parent,student);
		return Response.status(Status.OK).entity(statusMap).build();
	}
	
	@POST @Path("/sendTextTeacher") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response sendTextTeacher(SendNotificationMesssageBean bean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		NotificationServiceHelper helper = new NotificationServiceHelper();
		boolean sms = false;
		boolean email = false;
		for(String messageType:bean.getMessageType()){
			if(messageType.equals("email")){
				email = true;
			}
			if(messageType.equals("sms")){
				sms = true;
			}
		}
		List<Integer> list =Arrays.asList(bean.getTeacher());
		HashMap statusMap = helper.sendTextTeacher(userBean.getInst_id(),email,sms,bean.getMessage(),list);
		return Response.status(Status.OK).entity(statusMap).build();
	}
	
	@POST @Path("/sendText") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response sendText(SendNotificationMesssageBean bean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		NotificationServiceHelper helper = new NotificationServiceHelper();
		int div_id = Integer.parseInt(bean.getDivisionSelect());
		int batch_id = Integer.parseInt(bean.getBatchSelect());
		boolean sms = false;
		boolean email = false;
		boolean parent =  false;
		boolean student = false;
		for(String messageType:bean.getMessageTypeTOST()){
			if(messageType.equals("email")){
				email = true;
			}
			if(messageType.equals("sms")){
				sms = true;
			}
		}
		for(String messageType:bean.getSendTo()){
			if(messageType.equals("parent")){
				parent = true;
			}
			if(messageType.equals("student")){
				student = true;
			}
		}
		HashMap statusMap = helper.sendText(userBean.getInst_id(), div_id, batch_id,email,sms,parent,student,bean.getMessage());
		return Response.status(Status.OK).entity(statusMap).build();
	}
	
	@POST @Path("/sendAcademicAlerts/feeDue") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response sendAcademicAlerts(SendAcademicAlertFeeDueBean bean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		NotificationServiceHelper helper = new NotificationServiceHelper();
		HashMap statusMap = helper.sendFeesDue(bean, userBean.getInst_id());
		return Response.status(Status.OK).entity(statusMap).build();
	}
	
	@POST @Path("/sendAcademicAlerts/attendace") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response sendAcademicAlertsAttendance(SendAcademicAlertAttendanceBean bean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		NotificationServiceHelper helper = new NotificationServiceHelper();
		HashMap statusMap = helper.sendAttendance(bean, userBean.getInst_id());
		return Response.status(Status.OK).entity(statusMap).build();
	}
	
	@POST @Path("/sendAcademicAlerts/progressCard") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public Response sendAcademicAlertsProgressCard(SendAcademicAlertProgressCardBean bean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		NotificationServiceHelper helper = new NotificationServiceHelper();
		HashMap statusMap = helper.sendProgressCard(bean, userBean.getInst_id());
		return Response.status(Status.OK).entity(statusMap).build();
	}
	
	@GET
	@Path("/setting")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getsetting(){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ClassownerSettingsNotification classownerSettingsNotification = new ClassownerSettingsNotification();
		ClassownerSettingstransaction classownerSettingstransaction = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = classownerSettingstransaction.getSettings(userBean.getInst_id());
		return Response.ok().entity(settings).build();
	}
	
	@POST
	@Path("/setting")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveSetting(ClassOwnerNotificationBean classOwnerNotificationBean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ClassownerSettingstransaction classownerSettingstransaction = new ClassownerSettingstransaction();
		ClassownerSettingsNotification classownerSettingsNotification = classownerSettingstransaction.getSettings(userBean.getInst_id());
		boolean isEmailEnabled = classownerSettingsNotification.getInstituteStats().isEmailAccess();
		boolean isSmsEnabled = classownerSettingsNotification.getInstituteStats().isSmsAccess();
		if(isEmailEnabled || isSmsEnabled){
			classownerSettingstransaction.saveSettings(classOwnerNotificationBean, userBean.getInst_id());
			return Response.ok().build();
		}else{
			HashMap<String, String>map = new HashMap<String,String>();
			map.put("NOACCESS", "No access to email and sms");
			return Response.status(Status.BAD_REQUEST).entity(map).build();
		}
		
		
	}
	
	@GET
	@Path("/header")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllHeader(){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		HeaderTransaction headerTransaction = new HeaderTransaction(getUserBean().getUserStatic().getHeaderPath());
		List<Header> header = headerTransaction.getHeaderList(userBean.getInst_id());
		return Response.status(Status.OK).entity(header).build();
	}
	
	@POST
	@Path("/saveHeader/{headerName}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces("application/json")
	public Response saveHeader(String headerElement,
			@PathParam("headerName")String headerName) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		HeaderTransaction headerTransaction = new HeaderTransaction(Constants.STORAGE_PATH);
		headerTransaction.saveHeader(headerName, headerElement, userBean.getInst_id());
		return Response.status(200).entity(true).build();
	}
	
	@DELETE
	@Path("/header/{header_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteHeader(@PathParam("header_id") String header_id){
		System.out.println("Deleting....."+header_id);
		return Response.ok().build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/formField")
	public Response saveClassowner(HashMap<String, String> formFields){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		AdditionalFormFieldTransaction transaction = new AdditionalFormFieldTransaction();
		transaction.saveAdditionalFormField(formFields, userBean.getInst_id());
		return Response.accepted(formFields).build();
	}
	
	@GET
	@Path("/getScheduleForMonth/{batch}/{division}/{startDate}/{endDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScheduleForMonth(@PathParam("batch") int batch,
			@PathParam("division") int division,@PathParam("startDate") long startDate,@PathParam("endDate") long endDate) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		List<MonthlyScheduleServiceBean> scheduleList = new ArrayList<MonthlyScheduleServiceBean>();
		if(endDate == 0){
			scheduleList = scheduleTransaction.getMonthSchedule(batch, new Date(startDate), userBean.getInst_id(), division);
		}else{
			scheduleList = scheduleTransaction.getMonthSchedule(batch, new Date(startDate),new Date(endDate), userBean.getInst_id(), division);
		}
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@POST
	@Path("/schedule")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSchedule(com.service.beans.Schedule schedule) {
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		HashMap<String, String>map = new HashMap<String, String>();
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		String msg = scheduleTransaction.addSchedule(schedule, userBean.getInst_id());
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
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		HashMap<String, String>map = new HashMap<String, String>();
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		String msg = scheduleTransaction.updateSchedule(schedule, userBean.getInst_id());
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
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		int count = scheduleTransaction.deleteSchedule(schedule_id, userBean.getInst_id(),div_id,batch_id,date,grp_id);
		return Response.status(Response.Status.OK).entity(count).build();
	}
	
	@POST
	@Path("/saveCerificateTemplate/{certificateDesc}/{header_id}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces("application/json")
	public Response saveCerificateTemplate(String certificateBody,@PathParam("certificateDesc")String certificateDesc,
			@PathParam("header_id")String header_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		CertificateTransaction certificateTransaction = new CertificateTransaction(Constants.STORAGE_PATH);
		boolean status = certificateTransaction.saveCertificate(userBean.getInst_id(), certificateDesc, certificateBody, header_id);
		return Response.status(200).entity(status).build();
	}
	
	@GET
	@Path("/getCerificateTemplate/{cert_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response getCerificateTemplate(@PathParam("cert_id")int cert_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		CertificateTransaction certificateTransaction = new CertificateTransaction(Constants.STORAGE_PATH);
		String fileData = certificateTransaction.getCertificate(userBean.getInst_id(), cert_id);
		return Response.status(200).entity(fileData).build();
	}
	
	@POST
	@Path("/updateCerificateTemplate/{certificateDesc}/{header_id}/{cert_id}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces("application/json")
	public Response updateCerificateTemplate(String certificateBody,@PathParam("certificateDesc")String certificateDesc,
			@PathParam("header_id")String header_id,@PathParam("cert_id")int cert_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		CertificateTransaction certificateTransaction = new CertificateTransaction(Constants.STORAGE_PATH);
		boolean status = certificateTransaction.updateCertificate(userBean.getInst_id(), certificateDesc, certificateBody, header_id,cert_id);
		return Response.status(200).entity(status).build();
	}
	
	@GET
	@Path("/deleteCerificateTemplate/{cert_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCerificateTemplate(@PathParam("cert_id")int cert_id) {
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		CertificateTransaction certificateTransaction = new CertificateTransaction(Constants.STORAGE_PATH);
	    certificateTransaction.deleteCertificate(userBean.getInst_id(), cert_id);
		return Response.status(200).entity(true).build();
	}
	
	@POST
	@Path("/saveSubjective")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveSubjectiveExam(SubjectiveExamBean subjectiveExamBean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		InstituteStatTransaction statTransaction = new InstituteStatTransaction();
		boolean status = statTransaction.storageSpaceAvailabilityCheckForSubjective(userBean.getInst_id(), subjectiveExamBean.getImages(), "save", 0, Constants.STORAGE_PATH,Integer.parseInt(subjectiveExamBean.getClassId()),subjectiveExamBean.getSubjectId());
		if(status){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(Integer.parseInt(subjectiveExamBean.getClassId()));
		questionbank.setAdded_by(getRegId());
		questionbank.setExam_rep("1");
		questionbank.setInst_id(userBean.getInst_id());
		questionbank.setMarks(subjectiveExamBean.getMarks());
		questionbank.setQue_id(0);
		questionbank.setRep(0);
		questionbank.setSub_id(subjectiveExamBean.getSubjectId());
		questionbank.setTopic_id(subjectiveExamBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_text(subjectiveExamBean.getQuestion());
		questionbank.setQue_type(subjectiveExamBean.getQuestionType());
		int questionId = bankTransaction.saveQuestion(questionbank);
		if(questionId >= 0){
			ImageTransactions imageTransactions = new ImageTransactions(Constants.STORAGE_PATH);
			imageTransactions.saveQuestionImage(subjectiveExamBean.getImages(), questionId, userBean.getInst_id(),Integer.parseInt(subjectiveExamBean.getClassId()),subjectiveExamBean.getSubjectId());
		}
		statTransaction.updateStorageSpace(userBean.getInst_id(), Constants.STORAGE_PATH+"/"+userBean.getInst_id());
		}else{
			return Response.status(Status.OK).entity("memory").build();
		}
		return Response.status(Status.OK).entity("").build();
	}
	
	@POST
	@Path("/saveObjective")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveObjectiveExam(ObjectiveExamBean objectiveExamBean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		HashMap<Integer, List<String>> optionImages = new HashMap<Integer, List<String>>();
		List<ObjectiveOptions> list =  objectiveExamBean.getOptions();
		int optionsSize = list.size();
		for(int index=0;index<optionsSize;index++){
			optionImages.put(index, list.get(index).getOptionImage());
		}
		InstituteStatTransaction statTransaction = new InstituteStatTransaction();
		boolean status = statTransaction.storageSpaceAvailabilityCheckForObjective(userBean.getInst_id(), objectiveExamBean.getImages(),optionImages, "save", objectiveExamBean.getQuestionId(), Constants.STORAGE_PATH,Integer.parseInt(objectiveExamBean.getClassId()),objectiveExamBean.getSubjectId());
		if(status){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(Integer.parseInt(objectiveExamBean.getClassId()));
		questionbank.setExam_rep("1");
		questionbank.setInst_id(userBean.getInst_id());
		questionbank.setAdded_by(getRegId());
		questionbank.setMarks(objectiveExamBean.getMarks());
		questionbank.setQue_id(0);
		questionbank.setRep(0);
		questionbank.setSub_id(objectiveExamBean.getSubjectId());
		questionbank.setTopic_id(objectiveExamBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_text(objectiveExamBean.getQuestion());
		questionbank.setQue_type(objectiveExamBean.getQuestionType());
		
		List<ObjectiveOptions> objectiveOptions = objectiveExamBean.getOptions();
		List<Integer>correctOptionId = new ArrayList<Integer>();
		int optionLength = objectiveOptions.size();
		for(int index=0;index<optionLength;index++){
			if(objectiveOptions.get(index).isOptionCorrect()){
				correctOptionId.add(index);
			}
			try {
				Method method = questionbank.getClass().getMethod("setOpt_"+(index+1),String.class);
				method.invoke(questionbank,objectiveOptions.get(index).getOptionText());
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		String correctOption = StringUtils.join(correctOptionId,",");
		questionbank.setAns_id(correctOption);
		int questionId = bankTransaction.saveQuestion(questionbank);
		if(questionId >= 0){
			ImageTransactions imageTransactions = new ImageTransactions(Constants.STORAGE_PATH);
			imageTransactions.saveObjectiveQuestionImage(objectiveExamBean.getImages(), questionId, userBean.getInst_id(),Integer.parseInt(objectiveExamBean.getClassId()),objectiveExamBean.getSubjectId());
			imageTransactions.saveOptionImage(optionImages, questionId, userBean.getInst_id(),Integer.parseInt(objectiveExamBean.getClassId()),objectiveExamBean.getSubjectId());
		}
		statTransaction.updateStorageSpace(userBean.getInst_id(), Constants.STORAGE_PATH+"/"+userBean.getInst_id());
		}else{
			
			return Response.status(Status.OK).entity("memory").build();
		}
		return Response.status(Status.OK).entity("").build();
	}
	
	@POST
	@Path("/paraquestion")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveParaExam(ParaQuestionBean paraQuestionBean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		InstituteStatTransaction statTransaction = new InstituteStatTransaction();
		boolean memoryStatus = statTransaction.storageSpaceAvailabilityCheckForParagraph(userBean.getInst_id(), paraQuestionBean, Constants.STORAGE_PATH, "save",0);
		if(memoryStatus){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(paraQuestionBean.getClassId());
		questionbank.setExam_rep("1");
		questionbank.setInst_id(userBean.getInst_id());
		questionbank.setMarks(paraQuestionBean.getMarks());
		questionbank.setSub_id(paraQuestionBean.getSubjectId());
		questionbank.setTopic_id(paraQuestionBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_type(paraQuestionBean.getQuestionType());
		questionbank.setAdded_by(getRegId());
		
		//This is reuired to create folder with inst id in storage folder; 
		paraQuestionBean.setInstId(userBean.getInst_id());
		int questionNumber=  bankTransaction.saveQuestion(questionbank);
		bankTransaction.saveParaObject(Constants.STORAGE_PATH,paraQuestionBean, questionNumber);
		statTransaction.updateStorageSpace(userBean.getInst_id(), Constants.STORAGE_PATH+"/"+userBean.getInst_id());
		}else{
			
			return Response.status(Status.OK).entity("memory").build();
		}
		return Response.status(Status.OK).entity("").build();
		/*
		UserStatic userStatic = getUserBean().getUserStatic();
		String examPath = userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionNumber;
		File file = new File(examPath);
		
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
			try {file.createNewFile();} catch (IOException e) {	e.printStackTrace();}
		}
		writeObject(examPath, paraQuestionBean);
		*/
		
	}
	
	@GET
	@Path("/paraquestion/{que_id}/{sub_id}/{div_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestion(@PathParam("que_id")int que_id,
			@PathParam("sub_id")int sub_id,
			@PathParam("div_id")int div_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		int inst_id = userBean.getInst_id();
		ClassownerExamServiceFunction classownerExamServiceFunction = new ClassownerExamServiceFunction();
		Questionbank questionbank = classownerExamServiceFunction.getQuestion(inst_id,que_id, inst_id, sub_id, div_id);
		
		if(questionbank.getQue_type().equals("3")){
			String questionPath=com.config.Constants.STORAGE_PATH+File.separatorChar+inst_id+File.separatorChar+"exam"+File.separatorChar+"paragraph"+File.separatorChar+div_id+File.separatorChar+sub_id+File.separatorChar+questionbank.getQue_id();
			ParaQuestionBean paraQuestionBean = (ParaQuestionBean) readObject(new File(questionPath));
			
			return Response.ok(paraQuestionBean).build();
		}
		
		return Response.ok(questionbank).build();
	}
	
	@PUT
	@Path("/saveSubjective")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editSubjectiveExam(SubjectiveExamBean subjectiveExamBean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		InstituteStatTransaction statTransaction = new InstituteStatTransaction();
		boolean status = statTransaction.storageSpaceAvailabilityCheckForSubjective(userBean.getInst_id(), subjectiveExamBean.getImages(), "edit", subjectiveExamBean.getQuestionId(), Constants.STORAGE_PATH,Integer.parseInt(subjectiveExamBean.getClassId()),subjectiveExamBean.getSubjectId());
		if(status){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(Integer.parseInt(subjectiveExamBean.getClassId()));
		questionbank.setAdded_by(getRegId());
		questionbank.setExam_rep("1");
		questionbank.setQue_id(subjectiveExamBean.getQuestionId());
		questionbank.setInst_id(userBean.getInst_id());
		questionbank.setMarks(subjectiveExamBean.getMarks());
		questionbank.setRep(0);
		questionbank.setSub_id(subjectiveExamBean.getSubjectId());
		questionbank.setTopic_id(subjectiveExamBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_text(subjectiveExamBean.getQuestion());
		questionbank.setQue_type(subjectiveExamBean.getQuestionType());
		
		boolean success = bankTransaction.updateSubjectiveQuestion(subjectiveExamBean.getQuestionId(), userBean.getInst_id(), subjectiveExamBean.getSubjectId(),
				Integer.parseInt(subjectiveExamBean.getClassId()), subjectiveExamBean.getQuestion(),
				subjectiveExamBean.getMarks(),subjectiveExamBean.getTopicId());
		if(success){
			ImageTransactions imageTransactions = new ImageTransactions(Constants.STORAGE_PATH);
			imageTransactions.saveQuestionImage(subjectiveExamBean.getImages(), subjectiveExamBean.getQuestionId(), userBean.getInst_id(),Integer.parseInt(subjectiveExamBean.getClassId()),subjectiveExamBean.getSubjectId());
		}
		statTransaction.updateStorageSpace(userBean.getInst_id(), Constants.STORAGE_PATH+"/"+userBean.getInst_id());
		}else{
			return Response.status(Status.OK).entity("memory").build();
		}
		return Response.status(Status.OK).entity("").build();
	}
	
	@PUT
	@Path("/saveObjective")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editObjectiveExam(ObjectiveExamBean objectiveExamBean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		HashMap<Integer, List<String>> optionImagesMap = new HashMap<Integer, List<String>>();
		List<ObjectiveOptions> optionList =  objectiveExamBean.getOptions();
		int optionListSize = optionList.size();
		for(int index=0;index<optionListSize;index++){
			optionImagesMap.put(index, optionList.get(index).getOptionImage());
		}
		InstituteStatTransaction statTransaction = new InstituteStatTransaction();
		boolean memoryStatus = statTransaction.storageSpaceAvailabilityCheckForObjective(userBean.getInst_id(), objectiveExamBean.getImages(),optionImagesMap, "edit", objectiveExamBean.getQuestionId(), Constants.STORAGE_PATH,Integer.parseInt(objectiveExamBean.getClassId()),objectiveExamBean.getSubjectId());
		if(memoryStatus){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(Integer.parseInt(objectiveExamBean.getClassId()));
		questionbank.setExam_rep("1");
		questionbank.setInst_id(userBean.getInst_id());
		questionbank.setAdded_by(getRegId());
		questionbank.setMarks(objectiveExamBean.getMarks());
		questionbank.setQue_id(objectiveExamBean.getQuestionId());
		questionbank.setRep(0);
		questionbank.setSub_id(objectiveExamBean.getSubjectId());
		questionbank.setTopic_id(objectiveExamBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_text(objectiveExamBean.getQuestion());
		questionbank.setQue_type(objectiveExamBean.getQuestionType());
		
		List<ObjectiveOptions> objectiveOptions = objectiveExamBean.getOptions();
		List<Integer>correctOptionId = new ArrayList<Integer>();
		int optionLength = objectiveOptions.size();
		for(int index=0;index<optionLength;index++){
			if(objectiveOptions.get(index).isOptionCorrect()){
				correctOptionId.add(index);
			}
			try {
				Method method = questionbank.getClass().getMethod("setOpt_"+(index+1),String.class);
				method.invoke(questionbank,objectiveOptions.get(index).getOptionText());
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		String correctOption = StringUtils.join(correctOptionId,",");
		questionbank.setAns_id(correctOption);
		
		boolean status = bankTransaction.updateObjectiveQuestion(questionbank);
		
		//This contains previous id of options
		List<Integer>prevImageLocation = new ArrayList<Integer>();
		
		//This contains current id of options
		List<Integer>currentImageLocation = new ArrayList<Integer>();
				
		if(status){
			ImageTransactions imageTransactions = new ImageTransactions(Constants.STORAGE_PATH);
			imageTransactions.saveObjectiveQuestionImage(objectiveExamBean.getImages(), questionbank.getQue_id(), userBean.getInst_id(),Integer.parseInt(objectiveExamBean.getClassId()),objectiveExamBean.getSubjectId());
			
			HashMap<Integer, List<String>> optionImages = new HashMap<Integer, List<String>>();
			List<ObjectiveOptions> list =  objectiveExamBean.getOptions();
			int optionsSize = list.size();
			for(int index=0;index<optionsSize;index++){
				/*This folders needs to rename*/
				if(index != list.get(index).getPreviousId()){
					currentImageLocation.add(index);
					prevImageLocation.add(list.get(index).getPreviousId());
				}
				optionImages.put(index, list.get(index).getOptionImage());
			}
			imageTransactions.renameFolders(prevImageLocation, currentImageLocation, questionbank.getQue_id(), userBean.getInst_id(),Integer.parseInt(objectiveExamBean.getClassId()),objectiveExamBean.getSubjectId());
			imageTransactions.saveOptionImage(optionImages, questionbank.getQue_id(), userBean.getInst_id(),Integer.parseInt(objectiveExamBean.getClassId()),objectiveExamBean.getSubjectId());
		}
		statTransaction.updateStorageSpace(userBean.getInst_id(), Constants.STORAGE_PATH+"/"+userBean.getInst_id());
		}else{
			
			return Response.status(Status.OK).entity("memory").build();
		}
		return Response.status(Status.OK).entity("").build();
		
	}
	
	@PUT
	@Path("/paraquestion/{queId}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateParaExam(@PathParam("queId")int queId,
			ParaQuestionBean paraQuestionBean){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		InstituteStatTransaction statTransaction = new InstituteStatTransaction();
		boolean memoryStatus = statTransaction.storageSpaceAvailabilityCheckForParagraph(userBean.getInst_id(), paraQuestionBean, Constants.STORAGE_PATH, "edit",queId);
		if(memoryStatus){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(paraQuestionBean.getClassId());
		questionbank.setExam_rep("1");
		questionbank.setInst_id(userBean.getInst_id());
		questionbank.setMarks(paraQuestionBean.getMarks());
		questionbank.setSub_id(paraQuestionBean.getSubjectId());
		questionbank.setTopic_id(paraQuestionBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_type(paraQuestionBean.getQuestionType());
		questionbank.setAdded_by(getRegId());
		questionbank.setQue_id(queId);
		//This is reuired to create folder with inst id in storage folder; 
		paraQuestionBean.setInstId(userBean.getInst_id());
		bankTransaction.updateParagraphQuestion(queId,
				userBean.getInst_id(),
				paraQuestionBean.getSubjectId(),
				paraQuestionBean.getClassId(),
				paraQuestionBean.getMarks(),paraQuestionBean.getTopicId());
		bankTransaction.saveParaObject(Constants.STORAGE_PATH,paraQuestionBean, queId);
		statTransaction.updateStorageSpace(userBean.getInst_id(), Constants.STORAGE_PATH+"/"+userBean.getInst_id());
		}else{
			
			return Response.status(Status.OK).entity("memory").build();
		}
		return Response.status(Status.OK).entity("").build();
		/*
		UserStatic userStatic = getUserBean().getUserStatic();
		String examPath = userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionNumber;
		File file = new File(examPath);
		
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
			try {file.createNewFile();} catch (IOException e) {	e.printStackTrace();}
		}
		writeObject(examPath, paraQuestionBean);
		*/

	}
	
	@DELETE
	@Path("/deleteQuestion/{que_id}/{sub_id}/{div_id}/{questionType}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteQuestion(@PathParam("que_id")int que_id,
			@PathParam("sub_id")int sub_id,
			@PathParam("div_id")int div_id,@PathParam("questionType")String questionType){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		int inst_id = userBean.getInst_id();
		UserStatic userStatic = userBean.getUserStatic();
		if("3".equals(questionType)){
			
			String questionPath = userStatic.getExamPath()+File.separator+div_id+File.separator+sub_id+File.separator+que_id;
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
		String imgPath = "";
		if("1".equals(questionType)){
			imgPath = userStatic.getStorageSpace() + File.separatorChar + inst_id + File.separatorChar+ "examImage" + File.separatorChar + "subjective" + File.separatorChar + div_id + File.separatorChar + sub_id + File.separatorChar + que_id ;
		}else if("2".equals(questionType)){
			imgPath = userStatic.getStorageSpace() + File.separatorChar + inst_id + File.separatorChar+ "examImage" + File.separatorChar + "objective" + File.separatorChar + div_id + File.separatorChar + sub_id + File.separatorChar + que_id ;
		}else if("3".equals(questionType)){
			imgPath = userStatic.getStorageSpace() + File.separatorChar + inst_id + File.separatorChar+ "examImage" + File.separatorChar + "paragraph" + File.separatorChar + div_id + File.separatorChar + sub_id + File.separatorChar + que_id ;
		}
		File file = new File(imgPath);
		if(file.exists()){
			try {
				delete(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			QuestionBankTransaction bankTransaction=new QuestionBankTransaction();
			bankTransaction.deleteQuestion(que_id, inst_id, sub_id, div_id);
		
		return Response.ok(true).build();
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
}
