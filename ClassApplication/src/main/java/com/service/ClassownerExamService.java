package com.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.annotations.cache.Cache;

import com.classapp.db.question.Questionbank;
import com.config.Constants;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.service.beans.ObjectiveExamBean;
import com.service.beans.ObjectiveOptions;
import com.service.beans.ParaQuestionBean;
import com.service.beans.SubjectiveExamBean;
import com.transaction.image.ImageTransactions;
import com.transaction.questionbank.QuestionBankTransaction;
import com.user.UserBean;

@Path("/classownerservice/examservice")
public class ClassownerExamService extends ServiceBase{
	@POST
	@Path("/saveSubjective")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveSubjectiveExam(SubjectiveExamBean subjectiveExamBean){
		Questionbank questionbank = new Questionbank();
		QuestionBankTransaction bankTransaction = new QuestionBankTransaction();
		questionbank.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionbank.setDiv_id(Integer.parseInt(subjectiveExamBean.getClassId()));
		questionbank.setAdded_by(getRegId());
		questionbank.setExam_rep("1");
		questionbank.setInst_id(getRegId());
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
		questionbank.setInst_id(getRegId());
		questionbank.setMarks(subjectiveExamBean.getMarks());
		questionbank.setRep(0);
		questionbank.setSub_id(subjectiveExamBean.getSubjectId());
		questionbank.setTopic_id(subjectiveExamBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_text(subjectiveExamBean.getQuestion());
		questionbank.setQue_type(subjectiveExamBean.getQuestionType());
		
		boolean success = bankTransaction.updateSubjectiveQuestion(subjectiveExamBean.getQuestionId(), getRegId(), subjectiveExamBean.getSubjectId(),
				Integer.parseInt(subjectiveExamBean.getClassId()), subjectiveExamBean.getQuestion(),
				subjectiveExamBean.getMarks(),subjectiveExamBean.getTopicId());
		if(success){
			ImageTransactions imageTransactions = new ImageTransactions(Constants.STORAGE_PATH);
			imageTransactions.saveQuestionImage(subjectiveExamBean.getImages(), subjectiveExamBean.getQuestionId(), getRegId());
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
		questionbank.setInst_id(getRegId());
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
		questionbank.setInst_id(getRegId());
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
			imageTransactions.saveObjectiveQuestionImage(objectiveExamBean.getImages(), questionbank.getQue_id(), getRegId());
			
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
			imageTransactions.renameFolders(prevImageLocation, currentImageLocation, questionbank.getQue_id(), getRegId());
			imageTransactions.saveOptionImage(optionImages, questionbank.getQue_id(), getRegId());
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
		questionbank.setInst_id(getRegId());
		questionbank.setMarks(paraQuestionBean.getMarks());
		questionbank.setSub_id(paraQuestionBean.getSubjectId());
		questionbank.setTopic_id(paraQuestionBean.getTopicId());
		questionbank.setQues_status("");
		questionbank.setQue_type(paraQuestionBean.getQuestionType());
		questionbank.setAdded_by(getRegId());
		questionbank.setQue_id(queId);
		//This is reuired to create folder with inst id in storage folder; 
		paraQuestionBean.setInstId(getRegId());
		bankTransaction.updateParagraphQuestion(queId,
				getRegId(),
				paraQuestionBean.getSubjectId(),
				paraQuestionBean.getClassId(),
				paraQuestionBean.getMarks(),paraQuestionBean.getTopicId());
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
	
	@GET
	@Path("/paraquestion/{que_id}/{sub_id}/{div_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestion(@PathParam("que_id")int que_id,
			@PathParam("sub_id")int sub_id,
			@PathParam("div_id")int div_id){
		int inst_id = getRegId();
		ClassownerExamServiceFunction classownerExamServiceFunction = new ClassownerExamServiceFunction();
		Questionbank questionbank = classownerExamServiceFunction.getQuestion(getRegId(),que_id, inst_id, sub_id, div_id);
		
		if(questionbank.getQue_type().equals("3")){
			UserBean userBean = getUserBean();
			String questionPath=com.config.Constants.STORAGE_PATH+File.separatorChar+userBean.getRegId()+File.separatorChar+"exam"+File.separatorChar+"paragraph"+File.separatorChar+questionbank.getQue_id();
			ParaQuestionBean paraQuestionBean = (ParaQuestionBean) readObject(new File(questionPath));
			
			return Response.ok(paraQuestionBean).build();
		}
		
		return Response.ok(questionbank).build();
	}
	
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	@Cache(maxAge=2300, mustRevalidate = true, noStore = true, proxyRevalidate = true, sMaxAge = 2300)
	public Response test(){
		return Response.ok("test").build();
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
