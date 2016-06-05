package com.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.classapp.db.Notes.Notes;
import com.classapp.db.Schedule.Schedule;
import com.classapp.db.attendance.Attendance;
import com.classapp.db.batch.Batch;
import com.classapp.db.exam.Exam;
import com.classapp.db.question.Questionbank;
import com.classapp.db.questionPaper.QuestionPaper;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Subjects;
import com.classapp.db.subject.Topics;
import com.classapp.login.UserStatic;
import com.config.Constants;
import com.google.gson.Gson;
import com.service.beans.AttendanceScheduleServiceBean;
import com.service.beans.BatchStudentExamMarks;
import com.service.beans.ExamSubject;
import com.service.beans.GenerateQuestionPaperResponse;
import com.service.beans.MonthlyScheduleServiceBean;
import com.service.beans.NewQuestionRequest;
import com.service.beans.ObjectiveExamBean;
import com.service.beans.ObjectiveOptions;
import com.service.beans.ParaQuestionBean;
import com.service.beans.QuestionPaperData;
import com.service.beans.QuestionPaperEditFileObject;
import com.service.beans.QuestionPaperFileElement;
import com.service.beans.QuestionPaperFileObject;
import com.service.beans.QuestionPaperPattern;
import com.service.beans.QuestionPaperStructure;
import com.service.beans.StudentData;
import com.service.beans.StudentSubjectMarks;
import com.service.beans.SubjectiveExamBean;
import com.service.beans.SubjectsWithTopics;
import com.service.beans.TeacherDivisionsAndSubjects;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.attendance.AttendanceTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.image.ImageTransactions;
import com.transaction.notes.NotesTransaction;
import com.transaction.pattentransaction.QuestionPaperPatternTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

@Path("/teacher") 
public class TeacherServiceImpl extends ServiceBase {
	@Context
	private HttpServletRequest request;
	@GET
	@Path("/getDivisionAndSubjects/{inst_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentExamMarks(@PathParam("inst_id") int inst_id) {
		TeacherTransaction teacherTransaction = new TeacherTransaction();
		TeacherDivisionsAndSubjects teacherDivisionsAndSubjects = teacherTransaction.getDivisionsAndSubjects(inst_id, getRegId());
		return Response.status(200).entity(teacherDivisionsAndSubjects).build();
	}
	
	@GET
	@Path("/getSubjectOfDivision/{inst_id}/{div_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubjectOfDivision(@PathParam("inst_id") int inst_id,@PathParam("div_id") int div_id) {
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subject> subjects = subjectTransaction.getSubjectRelatedToDiv(div_id, inst_id);
		TeacherTransaction teacherTransaction=new TeacherTransaction();
		List<Subject> list=new ArrayList<Subject>();
		list= teacherTransaction.getTeacherSubject(getRegId(), inst_id);
		List<Subject> subjectsList = new ArrayList<Subject>();
		if(subjects != null){
		for (Subject subject : subjects) {
			for (Subject teachersSubject : list) {
				if(teachersSubject.getSubjectId() == subject.getSubjectId()){
					subjectsList.add(teachersSubject);
					break;
				}
			}
		}
		}
			
			return Response.status(200).entity(subjectsList).build();
	}
	
	@GET
	@Path("/getSubjectOfInstitutesDivision/{inst_id}/{div_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubjectOfInstitutesDivision(@PathParam("inst_id") int inst_id,@PathParam("div_id") int div_id) {
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subject> subjects = subjectTransaction.getSubjectRelatedToDiv(div_id, inst_id);
		return Response.status(200).entity(subjects).build();
	}
	
	@GET
	@Path("/getDivisionsTopics/{inst_id}/{div_id}/{sub_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDivisionsTopics(@PathParam("inst_id") int inst_id,@PathParam("div_id") int div_id,
										@PathParam("sub_id") int sub_id) {
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List<Topics> topics=subjectTransaction.getTopics(inst_id, sub_id,div_id);

			
			return Response.status(200).entity(topics).build();
	}
	
	@POST
	@Path("/saveSubjective")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveSubjectiveExam(SubjectiveExamBean subjectiveExamBean){
		getUserBean().getUserStatic().getExamPath();
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(Integer.parseInt(subjectiveExamBean.getClassId()));
		questionbank.setAdded_by(getRegId());
		questionbank.setExam_rep("1");
		questionbank.setInst_id(subjectiveExamBean.getInst_id());
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
			imageTransactions.saveQuestionImage(subjectiveExamBean.getImages(), questionId, subjectiveExamBean.getInst_id());
		}
		return Response.ok().build();
	}
	
	@PUT
	@Path("/saveSubjective")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editSubjectiveExam(SubjectiveExamBean subjectiveExamBean){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(Integer.parseInt(subjectiveExamBean.getClassId()));
		questionbank.setAdded_by(getRegId());
		questionbank.setExam_rep("1");
		questionbank.setQue_id(subjectiveExamBean.getQuestionId());
		questionbank.setInst_id(subjectiveExamBean.getInst_id());
		questionbank.setMarks(subjectiveExamBean.getMarks());
		questionbank.setRep(0);
		questionbank.setSub_id(subjectiveExamBean.getSubjectId());
		questionbank.setTopic_id(subjectiveExamBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_text(subjectiveExamBean.getQuestion());
		questionbank.setQue_type(subjectiveExamBean.getQuestionType());
		
		boolean success = bankTransaction.updateSubjectiveQuestion(subjectiveExamBean.getQuestionId(), subjectiveExamBean.getInst_id(), subjectiveExamBean.getSubjectId(),
				Integer.parseInt(subjectiveExamBean.getClassId()), subjectiveExamBean.getQuestion(),
				subjectiveExamBean.getMarks());
		if(success){
			ImageTransactions imageTransactions = new ImageTransactions(Constants.STORAGE_PATH);
			imageTransactions.saveQuestionImage(subjectiveExamBean.getImages(), subjectiveExamBean.getQuestionId(), subjectiveExamBean.getInst_id());
		}
		return Response.ok().build();
	}
	
	@POST
	@Path("/saveObjective")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveObjectiveExam(ObjectiveExamBean objectiveExamBean){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(Integer.parseInt(objectiveExamBean.getClassId()));
		questionbank.setExam_rep("1");
		questionbank.setInst_id(objectiveExamBean.getInst_id());
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
			imageTransactions.saveObjectiveQuestionImage(objectiveExamBean.getImages(), questionId, objectiveExamBean.getInst_id());
			
			HashMap<Integer, List<String>> optionImages = new HashMap<Integer, List<String>>();
			List<ObjectiveOptions> list =  objectiveExamBean.getOptions();
			int optionsSize = list.size();
			for(int index=0;index<optionsSize;index++){
				optionImages.put(index, list.get(index).getOptionImage());
			}
			imageTransactions.saveOptionImage(optionImages, questionId, objectiveExamBean.getInst_id());
		}
		
		return Response.ok().build();
	}
	
	@PUT
	@Path("/saveObjective")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editObjectiveExam(ObjectiveExamBean objectiveExamBean){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(Integer.parseInt(objectiveExamBean.getClassId()));
		questionbank.setExam_rep("1");
		questionbank.setInst_id(objectiveExamBean.getInst_id());
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
			imageTransactions.saveObjectiveQuestionImage(objectiveExamBean.getImages(), questionbank.getQue_id(), objectiveExamBean.getInst_id());
			
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
			imageTransactions.renameFolders(prevImageLocation, currentImageLocation, questionbank.getQue_id(),  objectiveExamBean.getInst_id());
			imageTransactions.saveOptionImage(optionImages, questionbank.getQue_id(),  objectiveExamBean.getInst_id());
		}
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/paraquestion")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveParaExam(ParaQuestionBean paraQuestionBean){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(paraQuestionBean.getClassId());
		questionbank.setExam_rep("1");
		questionbank.setInst_id(paraQuestionBean.getInstId());
		questionbank.setMarks(paraQuestionBean.getMarks());
		questionbank.setSub_id(paraQuestionBean.getSubjectId());
		questionbank.setTopic_id(paraQuestionBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_type(paraQuestionBean.getQuestionType());
		questionbank.setAdded_by(getRegId());
		
		//This is reuired to create folder with inst id in storage folder; 
		//paraQuestionBean.setInstId(getRegId());
		int questionNumber=  bankTransaction.saveQuestion(questionbank);
		bankTransaction.saveParaObject(Constants.STORAGE_PATH,paraQuestionBean, questionNumber);
		
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
		return Response.ok().build();
	}
	
	@PUT
	@Path("/paraquestion/{queId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateParaExam(@PathParam("queId")int queId,
			ParaQuestionBean paraQuestionBean){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(paraQuestionBean.getClassId());
		questionbank.setExam_rep("1");
		questionbank.setInst_id(paraQuestionBean.getInstId());
		questionbank.setMarks(paraQuestionBean.getMarks());
		questionbank.setSub_id(paraQuestionBean.getSubjectId());
		questionbank.setTopic_id(paraQuestionBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_type(paraQuestionBean.getQuestionType());
		questionbank.setAdded_by(getRegId());
		questionbank.setQue_id(queId);
		//This is reuired to create folder with inst id in storage folder; 
		
		bankTransaction.updateParagraphQuestion(queId,
				paraQuestionBean.getInstId(),
				paraQuestionBean.getSubjectId(),
				paraQuestionBean.getClassId(),
				paraQuestionBean.getMarks());
		bankTransaction.saveParaObject(Constants.STORAGE_PATH,paraQuestionBean, queId);
		
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
		return Response.ok().build();
	}
	
	@POST
	@Path("/addQuestionPaperPattern")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addQuestionPaperPattern(QuestionPaperPattern examPattern){
		Gson gson = new Gson();
		System.out.println(gson.toJson(examPattern));
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		String storagePath = com.config.Constants.STORAGE_PATH+File.separator+examPattern.getInst_id();
		userBean.getUserStatic().setStorageSpace(storagePath);
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),examPattern.getInst_id());
		boolean patternStatus= patternTransaction.saveQuestionPaperPattern(examPattern,getRegId());
		if(patternStatus == false){
			return Response.status(Status.OK).entity(patternStatus).build();
		}
		return Response.status(Status.OK).entity(patternStatus).build();
	}
	
	@POST
	@Path("/updateQuestionPaperPattern")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateQuestionPaperPattern(QuestionPaperPattern examPattern){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		String storagePath = com.config.Constants.STORAGE_PATH+File.separator+examPattern.getInst_id();
		userBean.getUserStatic().setStorageSpace(storagePath);
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),examPattern.getInst_id());
		boolean patternStatus= patternTransaction.updateQuestionPaperPattern(examPattern,getRegId());
		if(patternStatus == false){
			return Response.status(Status.OK).entity(patternStatus).build();
		}
		return Response.status(Status.OK).entity(patternStatus).build();
	}
	
	@GET
	@Path("/getSubjectsAndTopics/{inst_id}/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubjectsAndTopics(@PathParam("inst_id") int inst_id,@PathParam("division") String division){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subject> list = subjectTransaction.getSubjectRelatedToDiv(Integer.parseInt(division), inst_id);
		Map<Integer, SubjectsWithTopics> map = new HashMap<Integer,SubjectsWithTopics>();
		if(list != null){
			if(list.size() > 0){
				
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Subject subject = (Subject) iterator.next();
					List<Topics> topics = subjectTransaction.getTopics(inst_id, subject.getSubjectId(), Integer.parseInt(division));
					SubjectsWithTopics subjectsWithTopics = new SubjectsWithTopics();
					subjectsWithTopics.setSubject(subject);
					subjectsWithTopics.setTopics(topics);
					map.put(subject.getSubjectId(), subjectsWithTopics);
				}
			}
		}
		return Response.status(Status.OK).entity(map).build();
	}
	
	@GET
	@Path("/searchQuestionPaperPattern/{inst_id}/{division}/{patternType}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchQuestionPaperPattern(@PathParam("inst_id") int inst_id,@PathParam("division") int division,@PathParam("patternType") String patternType){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		String storagePath = com.config.Constants.STORAGE_PATH+File.separator+inst_id;
		userBean.getUserStatic().setStorageSpace(storagePath);
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),inst_id);
		List<QuestionPaperPattern> questionPaperPatternList = patternTransaction.getQuestionPaperPatternList(division,patternType);
		return Response.status(Status.OK).entity(questionPaperPatternList).build();
	}
	
	@GET
	@Path("/getQuestionPaperPattern/{inst_id}/{division}/{patternid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionPaperPattern(@PathParam("inst_id") int inst_id,@PathParam("division") int division,@PathParam("patternid") String patternId){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		String storagePath = com.config.Constants.STORAGE_PATH+File.separator+inst_id;
		userBean.getUserStatic().setStorageSpace(storagePath);
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),inst_id);
		QuestionPaperPattern questionPaperPattern = patternTransaction.getQuestionPaperPattern(division, Integer.parseInt(patternId));
		return Response.status(Status.OK).entity(questionPaperPattern).build();
	}
	
	@POST
	@Path("/generateQuestionPaper/{inst_id}/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response generateQuestionPaper(@PathParam("inst_id") int inst_id,@PathParam("division") String division,List<QuestionPaperStructure> paperStructure){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		String storagePath = com.config.Constants.STORAGE_PATH+File.separator+inst_id;
		userBean.getUserStatic().setStorageSpace(storagePath);
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),inst_id,userBean.getUserStatic().getExamPath());
		GenerateQuestionPaperResponse questionPaperResponse=patternTransaction.generateQuestionPaper(Integer.parseInt(division), paperStructure);
		return Response.status(Status.OK).entity(questionPaperResponse).build();
	}
	
	@POST
	@Path("/generateQuestionPaperSpecific/{inst_id}/{division}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateQuestionPaperSpecific(@PathParam("inst_id") int inst_id,@PathParam("division") String division,NewQuestionRequest newQuestionRequest){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		String storagePath = com.config.Constants.STORAGE_PATH+File.separator+inst_id;
		userBean.getUserStatic().setStorageSpace(storagePath);
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),inst_id,userBean.getUserStatic().getExamPath());
		GenerateQuestionPaperResponse questionPaperResponse=patternTransaction.generateQuestionPaperSpecific(Integer.parseInt(division), newQuestionRequest);
		return Response.status(Status.OK).entity(questionPaperResponse).build();
	}
	
	@POST
	@Path("/getQuestionList/{inst_id}/{division}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionList(@PathParam("inst_id") int inst_id,@PathParam("division") String division,NewQuestionRequest newQuestionRequest){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		String storagePath = com.config.Constants.STORAGE_PATH+File.separator+inst_id;
		userBean.getUserStatic().setStorageSpace(storagePath);
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),inst_id,userBean.getUserStatic().getExamPath());
		List<QuestionPaperData> questionPaperDataList = patternTransaction.getQuestionList(Integer.parseInt(division), newQuestionRequest);
		return Response.status(Status.OK).entity(questionPaperDataList).build();
	}
	
	@POST
	@Path("/saveQuestionPaper/{inst_id}/{patternId}/{questionPaperName}/{divisionId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveQuestionPaper(@PathParam("inst_id") int inst_id,@PathParam("patternId") String patternId,@PathParam("questionPaperName") String questionPaperName,
			@PathParam("divisionId") String divisionId
			,Map<String, String>questionAndItem){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		String storagePath = com.config.Constants.STORAGE_PATH+File.separator+inst_id;
		userBean.getUserStatic().setStorageSpace(storagePath);
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),inst_id);
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
		fileObject.setPaper_description(questionAndItem.get("desc"));
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
				int question_number = Integer.parseInt(questionAndItem.get(itemId));
				questionPaperFileElement.setQues_no(question_number);
			}
		}
		
		fileObject.setQuestionPaperFileElementList(questionPaperFileElements);
		boolean status = patternTransaction.saveQuestionPaper(fileObject, getRegId());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getQuestionPaperList/{inst_id}/{division}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionPaperList(@PathParam("inst_id") int inst_id,@PathParam("division") String division){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		String storagePath = com.config.Constants.STORAGE_PATH+File.separator+inst_id;
		userBean.getUserStatic().setStorageSpace(storagePath);
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),inst_id,userBean.getUserStatic().getExamPath());
		List<QuestionPaper> questionPaperList = patternTransaction.getQuestionPaperList(Integer.parseInt(division));
		return Response.status(Status.OK).entity(questionPaperList).build();
	}
	
	@GET
	@Path("/getQuestionPaper/{inst_id}/{division}/{paper_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestionPaper(@PathParam("inst_id") int inst_id,@PathParam("division") String division,@PathParam("paper_id") String paper_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		String storagePath = com.config.Constants.STORAGE_PATH+File.separator+inst_id;
		userBean.getUserStatic().setStorageSpace(storagePath);
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),inst_id,userBean.getUserStatic().getExamPath());
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		QuestionPaperEditFileObject fileObject = patternTransaction.getQuestionPaper(Integer.parseInt(division), Integer.parseInt(paper_id));
		return Response.status(Status.OK).entity(fileObject).build();
	}
	
	@POST
	@Path("/getExamSubjects/{inst_id}/{divId}/{batchId}/{exam_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExamSubjects(@PathParam("inst_id") int inst_id,
			@PathParam("divId")int div_id,
			@PathParam("batchId")int batch_id,@PathParam("exam_id")int exam_id){
		ExamTransaction examTransaction = new ExamTransaction();
		List<ExamSubject> subjectList = new ArrayList<ExamSubject>();
		subjectList = examTransaction.getExamSubjects(inst_id , div_id, batch_id, exam_id);
		return Response.status(Status.OK).entity(subjectList).build();
	}
	
	@GET
	@Path("/getExamList/{inst_id}/{division}/{batch_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExamList(@PathParam("inst_id") int inst_id,@PathParam("division") String division,@PathParam("batch_id") int batch_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ExamTransaction examTransaction = new ExamTransaction();
		List<Exam> examList = examTransaction.getExamList(Integer.parseInt(division), inst_id,batch_id);
		Gson gson = new Gson();
		String jsonElement = gson.toJson(examList);
		return Response.status(Status.OK).entity(jsonElement).build();
	}
	
	@GET
	@Path("/getBatchesofDivision/{inst_id}/{div_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBatchesofDivision(@PathParam("inst_id") int inst_id,@PathParam("div_id") int div_id) {
		BatchTransactions batchTransactions = new BatchTransactions();
		List<Batch> batches= batchTransactions.getAllBatchesOfDivision(div_id, inst_id);
		return Response.status(200).entity(batches).build();
	}
	
	@GET
	@Path("/getStudentForMarksFill/{inst_id}/{divId}/{batchId}/{exam}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentForMarksFill(@PathParam("inst_id") int inst_id,
			@PathParam("divId")int div_id,@PathParam("exam")int exam_id,
			@PathParam("batchId")String batch_id){
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentData> studentDatas = studentTransaction.getStudentForExamMarks(batch_id, inst_id, div_id,exam_id);
		return Response.status(Status.OK).entity(studentDatas).build();
	}
	
	@POST
	@Path("/saveStudentMarks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStudentMarks(List<StudentData> studentDataList){
		StudentTransaction studentTransaction = new StudentTransaction();
		boolean status = studentTransaction.saveStudentMarks(studentDataList);
		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getStudentMarksForUpdate/{inst_id}/{divId}/{batchId}/{exam_id}/{sub_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentMarksForUpdate(@PathParam("inst_id") int inst_id,
			@PathParam("divId")int div_id,@PathParam("exam_id")int exam_id,@PathParam("sub_id")int sub_id,
			@PathParam("batchId")String batch_id){
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentData> studentDatas = studentTransaction.getStudentForExamMarksUpdate(batch_id, inst_id, div_id, exam_id, sub_id);
		return Response.status(Status.OK).entity(studentDatas).build();
	}
	
	@GET
	@Path("/getBatchSubjects/{inst_id}/{divId}/{batchId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBatchSubjects(@PathParam("inst_id") int inst_id,
			@PathParam("divId")int div_id,@PathParam("batchId")String batch_id){
		BatchTransactions batchTransactions=new BatchTransactions();
		List<Subjects> Batchsubjects = (List<Subjects>)batchTransactions.getBatcheSubject(batch_id,inst_id,div_id);
		return Response.status(Status.OK).entity(Batchsubjects).build();
	}
	
	@GET
	@Path("/getStudentExamMarks/{inst_id}/{div_id}/{batch_id}/{exam_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentExamMarks(@PathParam("inst_id") int inst_id,@PathParam("div_id") int div_id,@PathParam("batch_id") int batch_id,
										@PathParam("exam_id") int exam_id) {
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		BatchStudentExamMarks batchStudentExamMarks  = marksTransaction.getStudentExamMarks(inst_id, div_id, batch_id, exam_id);
		return Response.status(200).entity(batchStudentExamMarks).build();
	}
	
	@GET
	@Path("/getStudentExamSubjectMarks/{inst_id}/{div_id}/{batch_id}/{sub_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentExamSubjectMarks(@PathParam("inst_id") int inst_id,@PathParam("div_id") int div_id,@PathParam("batch_id") int batch_id,
										@PathParam("sub_id") int sub_id) {
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		StudentSubjectMarks studentSubjectMarks = marksTransaction.getStudentExamSubjectMarks(inst_id, div_id, batch_id, sub_id);
		return Response.status(200).entity(studentSubjectMarks).build();
	}
	
	@GET
	@Path("/getScheduleForAttendance/{inst_id}/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSchedule(@PathParam("inst_id") int inst_id,@PathParam("batch") Integer batch,
			@PathParam("division") Integer division,@PathParam("date")long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List<AttendanceScheduleServiceBean> scheduleList = attendanceTransaction.getScheduleForAttendance(batch, new Date(date), inst_id, division);
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getStudentsForAttendance/{inst_id}/{division}/{batch}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsForAttendance(@PathParam("inst_id") int inst_id,@PathParam("batch") String batch,
			@PathParam("division") Integer division) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentForAttendance(batch, inst_id, division);
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@POST
	@Path("/saveStudentAttendance/{inst_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStudentAttendance(@PathParam("inst_id") int inst_id,List<Attendance> attendanceList) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		boolean status = attendanceTransaction.save(attendanceList,inst_id);
		return Response.status(Response.Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getScheduleForUpdateAttendance/{inst_id}/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScheduleForUpdateAttendance(@PathParam("inst_id") int inst_id,@PathParam("batch") Integer batch,
			@PathParam("division") Integer division,@PathParam("date")long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List<AttendanceScheduleServiceBean> scheduleList = attendanceTransaction.getScheduleForUpdateAttendance(batch, new Date(date), inst_id, division);
		return Response.status(Response.Status.OK).entity(scheduleList).build();
	}
	
	@GET
	@Path("/getStudentsForAttendanceUpdate/{inst_id}/{division}/{batch}/{sub_id}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsForAttendanceUpdate(@PathParam("inst_id") int inst_id,@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("sub_id") Integer sub_id,
			@PathParam("date") long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentForAttendanceUpdate(batch, inst_id, division, sub_id, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@POST
	@Path("/updateStudentAttendance/{inst_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateStudentAttendance(@PathParam("inst_id") int inst_id,List<Attendance> attendanceList) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		boolean status = attendanceTransaction.updateAttendance(attendanceList,inst_id);
		return Response.status(Response.Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/getStudentsDailyAttendance/{inst_id}/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsDailyAttendance(@PathParam("inst_id") int inst_id,@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("date") long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentsDailyAttendance(batch, inst_id, division, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@GET
	@Path("/getStudentsWeeklyAttendance/{inst_id}/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsWeeklyAttendance(@PathParam("inst_id") int inst_id,@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("date") long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentsWeeklyAttendance(batch, inst_id, division, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@GET
	@Path("/getStudentsMonthlyAttendance/{inst_id}/{division}/{batch}/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudentsMonthlyAttendance(@PathParam("inst_id") int inst_id,@PathParam("batch") String batch,
			@PathParam("division") Integer division,@PathParam("date") long date) {
		// TODO Auto-generated method stub
		AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
		List studentList = attendanceTransaction.getStudentsMonthlyAttendance(batch, inst_id, division, new Date(date));
		return Response.status(Response.Status.OK).entity(studentList).build();
	}
	
	@POST
	@Path("/updateQuestionPaper/{inst_id}/{patternId}/{questionPaperName}/{divisionId}/{paperId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateQuestionPaper(@PathParam("inst_id") int inst_id,@PathParam("patternId") String patternId,@PathParam("questionPaperName") String questionPaperName,
			@PathParam("divisionId") String divisionId,@PathParam("paperId") int paperId
			,Map<String, String>questionAndItem){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		
		QuestionPaperPatternTransaction patternTransaction = new QuestionPaperPatternTransaction(userBean.getUserStatic().getPatternPath(),inst_id);
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
		fileObject.setPaper_description(questionAndItem.get("desc"));
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
				int question_number = Integer.parseInt(questionAndItem.get(itemId));
				questionPaperFileElement.setQues_no(question_number);
			}
		}
		
		fileObject.setQuestionPaperFileElementList(questionPaperFileElements);
		fileObject.setPaper_id(paperId);
		patternTransaction.setQuestionPaperStorageURL(userBean.getUserStatic().getQuestionPaperPath());
		boolean status = patternTransaction.updateQuestionPaper(fileObject, getRegId());
		return Response.status(Status.OK).entity(status).build();
	}
	
	@POST
	@Path("/getNotes/{inst_id}/{division}/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotes(@PathParam("inst_id")int inst_id,@PathParam("division")int division,@PathParam("subject")int subject){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		NotesTransaction notesTransaction=new NotesTransaction();
		List<Notes> noteslist =notesTransaction.getNotesPath(division,subject, inst_id);
		return Response.status(Status.OK).entity(noteslist).build();
	}
	
	@POST
	@Path("/updateNotes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNotes(Notes notes){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
	    UserStatic userStatic = userBean.getUserStatic();
	    String storagePath = Constants.STORAGE_PATH+File.separator+notes.getInst_id();
	     userStatic.setStorageSpace(storagePath);
	    String destPath=  userStatic.getNotesPath(notes.getSubid(), notes.getDivid());
		NotesTransaction notesTransaction=new NotesTransaction();
		Notes oldNotes = notesTransaction.getNotesById(notes.getNotesid(), notes.getInst_id(), notes.getSubid(), notes.getDivid());
		int extentionStart = oldNotes.getNotespath().lastIndexOf(".");
		notes.setNotespath(notes.getName()+oldNotes.getNotespath().substring(extentionStart));
		boolean status = notesTransaction.updatenotes(notes.getName(),notes.getNotespath(), notes.getNotesid(), notes.getBatch(), notes.getInst_id(), notes.getDivid(), notes.getSubid());
		if(status == false){
			File oldFile = new File(destPath+ File.separatorChar + oldNotes.getNotespath());
			File newFile = new File(destPath+ File.separatorChar + notes.getNotespath());	
			oldFile.renameTo(newFile);
		}		return Response.status(Status.OK).entity(status).build();
	}
	
	@GET
	@Path("/question/{inst_id}/{que_id}/{sub_id}/{div_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestion(@PathParam("inst_id")int inst_id,@PathParam("que_id")int que_id,
			@PathParam("sub_id")int sub_id,
			@PathParam("div_id")int div_id){
		ClassownerExamServiceFunction classownerExamServiceFunction = new ClassownerExamServiceFunction();
		Questionbank questionbank = classownerExamServiceFunction.getQuestion(inst_id,que_id, inst_id, sub_id, div_id);
		
		if(questionbank.getQue_type().equals("3")){
			UserBean userBean = getUserBean();
			String questionPath=com.config.Constants.STORAGE_PATH+File.separatorChar+inst_id+File.separatorChar+"exam"+File.separatorChar+"paragraph"+File.separatorChar+questionbank.getQue_id();
			ParaQuestionBean paraQuestionBean = (ParaQuestionBean) readObject(new File(questionPath));
			
			return Response.ok(paraQuestionBean).build();
		}
		
		return Response.ok(questionbank).build();
	}
	
	@GET
	@Path("/image/{folderPath}/{inst_id}")
	@Produces("image/jpeg")
	public byte[] showImage(@PathParam("folderPath")String folderPath,@PathParam("inst_id")int inst_id){
		String folderActualPath = folderPath.replaceAll("_", "\\"+File.separatorChar); 
		String imagefileName = Constants.STORAGE_PATH + File.separatorChar+ inst_id + File.separatorChar + folderActualPath;
		try {
			FileInputStream stream = new FileInputStream(imagefileName);
			InputStream resourceStream = new BufferedInputStream(stream);
			return IOUtils.toByteArray(resourceStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GET
	@Path("/teacherSchedule/{month}/{year}/{inst_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTeacherSchedule(@PathParam("month")int month,@PathParam("year")int year,@PathParam("inst_id")int inst_id){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		ScheduleTransaction  scheduleTransaction=new ScheduleTransaction();
		List<MonthlyScheduleServiceBean> list =scheduleTransaction.getTeachersSchedule(inst_id, userBean.getRegId(), month, year);
		return Response.status(Status.OK).entity(list).build();
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
}
