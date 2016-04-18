package com.service;

import java.io.File;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import com.classapp.db.question.Questionbank;
import com.classapp.db.questionPaper.QuestionPaper;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Topics;
import com.config.Constants;
import com.google.gson.Gson;
import com.service.beans.GenerateQuestionPaperResponse;
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
import com.service.beans.SubjectiveExamBean;
import com.service.beans.SubjectsWithTopics;
import com.service.beans.TeacherDivisionsAndSubjects;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.image.ImageTransactions;
import com.transaction.pattentransaction.QuestionPaperPatternTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
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
		for (Subject subject : subjects) {
			for (Subject teachersSubject : list) {
				if(teachersSubject.getSubjectId() == subject.getSubjectId()){
					subjectsList.add(teachersSubject);
					break;
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
			imageTransactions.saveQuestionImage(subjectiveExamBean.getImages(), questionId, getRegId());
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
			imageTransactions.saveObjectiveQuestionImage(objectiveExamBean.getImages(), questionId, getRegId());
			
			HashMap<Integer, List<String>> optionImages = new HashMap<Integer, List<String>>();
			List<ObjectiveOptions> list =  objectiveExamBean.getOptions();
			int optionsSize = list.size();
			for(int index=0;index<optionsSize;index++){
				optionImages.put(index, list.get(index).getOptionImage());
			}
			imageTransactions.saveOptionImage(optionImages, questionId, getRegId());
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
		questionbank.setInst_id(getRegId());
		questionbank.setMarks(paraQuestionBean.getMarks());
		questionbank.setSub_id(paraQuestionBean.getSubjectId());
		questionbank.setTopic_id(paraQuestionBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_type(paraQuestionBean.getQuestionType());
		questionbank.setAdded_by(getRegId());
		
		//This is reuired to create folder with inst id in storage folder; 
		paraQuestionBean.setInstId(getRegId());
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

}
