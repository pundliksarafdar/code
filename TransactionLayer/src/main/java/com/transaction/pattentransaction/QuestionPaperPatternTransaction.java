package com.transaction.pattentransaction;

import java.beans.Beans;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.TTCCLayout;

import com.classapp.db.pattern.QuestionPaperPattern;
import com.classapp.db.pattern.QuestionPaperPatternDB;
import com.classapp.db.question.Questionbank;
import com.classapp.db.question.QuestionbankDB;
import com.classapp.db.questionPaper.QuestionPaper;
import com.classapp.db.questionPaper.QuestionPaperDB;
import com.classapp.db.student.StudentMarks;
import com.classapp.logger.AppLogger;
import com.classapp.persistence.Constants;
import com.datalayer.exam.ParagraphQuestion;
import com.google.gson.JsonObject;
import com.service.beans.EditQuestionPaper;
import com.service.beans.GenerateQuestionPaperResponse;
import com.service.beans.NewQuestionRequest;
import com.service.beans.OnlineExam;
import com.service.beans.OnlineExamPaper;
import com.service.beans.OnlineExamPaperElement;
import com.service.beans.ParaQuestionBean;
import com.service.beans.OnlineExamPaperElementEvaluate;
import com.service.beans.QuestionPaperData;
import com.service.beans.GenerateQuestionPaperServicebean;
import com.service.beans.QuestionPaperEditFileElement;
import com.service.beans.QuestionPaperEditFileObject;
import com.service.beans.QuestionPaperFileElement;
import com.service.beans.QuestionPaperFileObject;
import com.service.beans.QuestionPaperStructure;
import com.transaction.studentmarks.StudentMarksTransaction;

public class QuestionPaperPatternTransaction {
	String patternStorageURL;
	int inst_id;
	String questionStorageURL;
	String questionPaperStorageURL;
	
	String EXAM_IMAGE_FOLDER = "examImage";
	String SUBJECTIVE = "subjective";
	String OBJECTIVE = "objective";
	String PARAGRAPH = "paragraph";
	String PARAGRAPH_QUESTION = "paragraphQuestions";
	String QUESTION = "question";
	String OPTION = "option";
	
	public QuestionPaperPatternTransaction(String storageURL,int inst_id) {
		this.patternStorageURL = storageURL;
		this.inst_id = inst_id;
	}
	
	public QuestionPaperPatternTransaction(String storageURL,int inst_id,String questionStorageURL) {
		this.patternStorageURL = storageURL;
		this.inst_id = inst_id;
		this.questionStorageURL = questionStorageURL;
	}

	public int saveQuestionPaperPattern(com.service.beans.QuestionPaperPattern paperPattern,int addedby) {
		QuestionPaperPatternDB questionPaperPatternDB = new QuestionPaperPatternDB();
		int pattern_id = 0;
		if(!questionPaperPatternDB.verifyPatterName(inst_id, paperPattern.getClass_id(), paperPattern.getPattern_name())){
		QuestionPaperPattern questionPaperPattern = new QuestionPaperPattern();
		questionPaperPattern.setDiv_id(paperPattern.getClass_id());
		questionPaperPattern.setInst_id(inst_id);
		questionPaperPattern.setMarks(paperPattern.getMarks());
		questionPaperPattern.setPattern_name(paperPattern.getPattern_name());
		questionPaperPattern.setSub_id(paperPattern.getSub_id());
		questionPaperPattern.setPattern_type(paperPattern.getPattern_type());
		questionPaperPattern.setAddedby(addedby);
		pattern_id=questionPaperPatternDB.saveQuestionPaperPattern(questionPaperPattern);
		paperPattern.setPattern_id(pattern_id);
		String filePath = patternStorageURL+File.separator+questionPaperPattern.getDiv_id()+File.separator+pattern_id;
		writeObject(filePath, paperPattern);
		}else{
			return pattern_id;
		}
		return pattern_id;
	}
	
	public boolean updateQuestionPaperPattern(com.service.beans.QuestionPaperPattern paperPattern,int modifiedby) {
		QuestionPaperPatternDB questionPaperPatternDB = new QuestionPaperPatternDB();
		if(!questionPaperPatternDB.verifyUpdatePatterName(inst_id, paperPattern.getClass_id(), paperPattern.getPattern_name(),paperPattern.getPattern_id())){
		QuestionPaperPattern questionPaperPattern = new QuestionPaperPattern();
		questionPaperPattern.setDiv_id(paperPattern.getClass_id());
		questionPaperPattern.setInst_id(inst_id);
		questionPaperPattern.setMarks(paperPattern.getMarks());
		questionPaperPattern.setPattern_name(paperPattern.getPattern_name());
		questionPaperPattern.setSub_id(paperPattern.getSub_id());
		questionPaperPattern.setPattern_id(paperPattern.getPattern_id());
		questionPaperPattern.setModifiedby(modifiedby);
		int pattern_id=questionPaperPatternDB.updateQuestionPaperPattern(questionPaperPattern);
		String filePath = patternStorageURL+File.separator+questionPaperPattern.getDiv_id()+File.separator+pattern_id;
		writeObject(filePath, paperPattern);
		}else{
			return false;
		}
		return true;
	}
	
	public List<com.service.beans.QuestionPaperPattern> getQuestionPaperPatternList(int div_id,int sub_id,String pattern_type) {
		QuestionPaperPatternDB paperPatternDB = new QuestionPaperPatternDB();
		List<QuestionPaperPattern> questionPaperPatternList =   paperPatternDB.getQuestionPaperPatternList(inst_id, div_id,sub_id,pattern_type);
		List<com.service.beans.QuestionPaperPattern> paperPatternList = new ArrayList<com.service.beans.QuestionPaperPattern>();
		if(questionPaperPatternList != null){
			for (int i = 0; i < questionPaperPatternList.size(); i++) {
				com.service.beans.QuestionPaperPattern paperPattern = new com.service.beans.QuestionPaperPattern();
				paperPattern.setPattern_id(questionPaperPatternList.get(i).getPattern_id());
				paperPattern.setClass_id(questionPaperPatternList.get(i).getDiv_id());
				paperPattern.setInst_id(inst_id);
				paperPattern.setMarks(questionPaperPatternList.get(i).getMarks());
				paperPattern.setPattern_name(questionPaperPatternList.get(i).getPattern_name());
				paperPatternList.add(paperPattern);
			}
		}
		return paperPatternList;
	}
	
	public com.service.beans.QuestionPaperPattern getQuestionPaperPattern(int div_id,int patternid) {
		File file = new File(patternStorageURL+File.separator+div_id+File.separator+patternid);
		com.service.beans.QuestionPaperPattern questionPaperPattern = (com.service.beans.QuestionPaperPattern) readObject(file);
		return questionPaperPattern;
	}
	
	public boolean deleteQuestionPaperPattern(int div_id,int patternid) {
		QuestionPaperPatternDB paperPatternDB = new QuestionPaperPatternDB();
		paperPatternDB.deleteQuestionPaperPatternList(inst_id, div_id, patternid);
		File file = new File(patternStorageURL+File.separator+div_id+File.separator+patternid);
		try {
			delete(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean deleteQuestionPaperPatternRelatedToClass(int inst_id,int div_id) {
		QuestionPaperPatternDB paperPatternDB = new QuestionPaperPatternDB();
		paperPatternDB.deleteQuestionPaperPatternRelatedToClass(inst_id, div_id);
		File file = new File(patternStorageURL+File.separator+div_id);
		try {
			delete(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public GenerateQuestionPaperResponse generateQuestionPaper(int div_id,List<QuestionPaperStructure> questionPaperPattern) {
		List<GenerateQuestionPaperServicebean> generateQuestionPaperServicebeans = new ArrayList<GenerateQuestionPaperServicebean>();
		for (int i = 0; i < questionPaperPattern.size(); i++) {
			boolean flag = false;
			QuestionPaperStructure paperStructure = questionPaperPattern.get(i);
			if(paperStructure.getItem_type().equals("Question")){
			GenerateQuestionPaperServicebean questionPaperServicebean = new GenerateQuestionPaperServicebean();
			questionPaperServicebean.setMarks(paperStructure.getItem_marks());
			questionPaperServicebean.setQuestion_type(paperStructure.getQuestion_type());
			questionPaperServicebean.setSubject_id(paperStructure.getSubject_id());
			if(paperStructure.getQuestion_topic() != null && !"".equals(paperStructure.getQuestion_topic())){
			questionPaperServicebean.setTopic_id(Integer.parseInt(paperStructure.getQuestion_topic()));
			}
			for (Iterator iterator = generateQuestionPaperServicebeans
					.iterator(); iterator.hasNext();) {
				GenerateQuestionPaperServicebean generateQuestionPaperServicebean = (GenerateQuestionPaperServicebean) iterator
						.next();
				if(generateQuestionPaperServicebean.getSubject_id() == paperStructure.getSubject_id() 
						&& generateQuestionPaperServicebean.getTopic_id() == Integer.parseInt(paperStructure.getQuestion_topic())
						&& generateQuestionPaperServicebean.getQuestion_type().equals(paperStructure.getQuestion_type()) 
						&& generateQuestionPaperServicebean.getMarks() == paperStructure.getItem_marks()){
					generateQuestionPaperServicebean.setCount(generateQuestionPaperServicebean.getCount()+1);
					flag = true;
					break;
				}
				
			}
			if(flag == true){
				continue;
			}
			questionPaperServicebean.setCount(1);
			generateQuestionPaperServicebeans.add(questionPaperServicebean);
			}
		}
		QuestionbankDB questionbankDB = new QuestionbankDB();
		generateQuestionPaperServicebeans=questionbankDB.getQuestionsForGenerateExam(inst_id, div_id, generateQuestionPaperServicebeans);
		for (Iterator iterator = generateQuestionPaperServicebeans.iterator(); iterator
				.hasNext();) {
			GenerateQuestionPaperServicebean generateQuestionPaperServicebean = (GenerateQuestionPaperServicebean) iterator
					.next();
			if(generateQuestionPaperServicebean.getQuestion_ids().size()>0){
			generateQuestionPaperServicebean.setQuestion_ids(generateRandomQuestionId(generateQuestionPaperServicebean.getQuestion_ids(),generateQuestionPaperServicebean.getCount()));
			}
		}
		List<QuestionPaperData> questionPaperDataList = new ArrayList<QuestionPaperData>();
		for (int i = 0; i < questionPaperPattern.size(); i++) {
			boolean dataFlag = false;
			if(questionPaperPattern.get(i).getItem_type().equals("Question")){
			for (Iterator iterator = generateQuestionPaperServicebeans.iterator(); iterator
					.hasNext();) {
				GenerateQuestionPaperServicebean generateQuestionPaperServicebean = (GenerateQuestionPaperServicebean) iterator
						.next();
				if(questionPaperPattern.get(i).getSubject_id() == generateQuestionPaperServicebean.getSubject_id() &&
					questionPaperPattern.get(i).getItem_marks() == generateQuestionPaperServicebean.getMarks() &&
					questionPaperPattern.get(i).getQuestion_type().equals(generateQuestionPaperServicebean.getQuestion_type()) &&
					Integer.parseInt(questionPaperPattern.get(i).getQuestion_topic()) == generateQuestionPaperServicebean.getTopic_id()){
					if( generateQuestionPaperServicebean.getQuestion_ids().size() > generateQuestionPaperServicebean.getQuestion_PickUpCounter() ){
					QuestionPaperData generateQuestionPaperResponse = new QuestionPaperData();
				  com.classapp.db.question.Questionbank questionbank =  questionbankDB.getQuestion(generateQuestionPaperServicebean.getQuestion_ids().get(generateQuestionPaperServicebean.getQuestion_PickUpCounter()), inst_id, generateQuestionPaperServicebean.getSubject_id(), div_id);
				  generateQuestionPaperResponse.setQuestionbank(questionbank);
				  generateQuestionPaperResponse.setItem_id(questionPaperPattern.get(i).getItem_id());
				  if(questionPaperPattern.get(i).getQuestion_type().equals("3")){
					 ParaQuestionBean paraQuestionBean = (ParaQuestionBean) readObject(new File(questionStorageURL+File.separator+"paragraph"+File.separator+div_id+File.separator+generateQuestionPaperServicebean.getSubject_id()+File.separator+generateQuestionPaperServicebean.getQuestion_ids().get(generateQuestionPaperServicebean.getQuestion_PickUpCounter())));
					 ParagraphQuestion paragraphQuestion = new ParagraphQuestion();
					 if(paraQuestionBean != null){
						 paragraphQuestion.setParagraphText(paraQuestionBean.getParagraph());
					 }
					 generateQuestionPaperResponse.setParagraphQuestion(paragraphQuestion);
				  }
				  generateQuestionPaperServicebean.setQuestion_PickUpCounter(generateQuestionPaperServicebean.getQuestion_PickUpCounter()+1);
				  generateQuestionPaperResponse.setDataStatus("Y");
				  questionPaperDataList.add(generateQuestionPaperResponse);
				  dataFlag = true;
				  break;
				}else{
					break;
				}
				}
			}
			if(dataFlag == false){
				QuestionPaperData generateQuestionPaperResponse = new QuestionPaperData();
				generateQuestionPaperResponse.setItem_id(questionPaperPattern.get(i).getItem_id());
				generateQuestionPaperResponse.setDataStatus("N");
				questionPaperDataList.add(generateQuestionPaperResponse);
			}
			}
		}
		List<GenerateQuestionPaperServicebean> servicebeanList = new ArrayList<GenerateQuestionPaperServicebean>();
		for (Iterator iterator = generateQuestionPaperServicebeans.iterator(); iterator
				.hasNext();) {
			GenerateQuestionPaperServicebean generateQuestionPaperServicebean = (GenerateQuestionPaperServicebean) iterator
					.next();
			boolean innerFlag = false;
			generateQuestionPaperServicebean.setQuestion_PickUpCounter(0);
			for (Iterator iterator2 = servicebeanList.iterator(); iterator2.hasNext();) {
				GenerateQuestionPaperServicebean bean = (GenerateQuestionPaperServicebean) iterator2
						.next();
				if(generateQuestionPaperServicebean.getSubject_id() == bean.getSubject_id() && generateQuestionPaperServicebean.getMarks() == bean.getMarks()){
					List<Integer> list = bean.getQuestion_ids();
					list.addAll(generateQuestionPaperServicebean.getQuestion_ids());
					bean.setQuestion_ids(list);
					innerFlag = true;
					break;
 				}
			}
			if(innerFlag == false){
				generateQuestionPaperServicebean.setTopic_id(-1);
				servicebeanList.add(generateQuestionPaperServicebean);
			}
		}
		GenerateQuestionPaperResponse generateQuestionPaperResponse = new GenerateQuestionPaperResponse();
		generateQuestionPaperResponse.setQuestionPaperDataList(questionPaperDataList);
		generateQuestionPaperResponse.setQuestionPaperServicebeanList(servicebeanList);
		return generateQuestionPaperResponse;
	}
	
	public GenerateQuestionPaperResponse generateQuestionPaperSpecific(int div_id,NewQuestionRequest newQuestionRequest) {
		GenerateQuestionPaperResponse generateQuestionPaperResponse = new GenerateQuestionPaperResponse();
		boolean flag = false;
		if(null == newQuestionRequest.getGenerateQuestionPaperServicebeanList()){
			return generateQuestionPaperResponse;
		}
		for (Iterator iterator = newQuestionRequest.getGenerateQuestionPaperServicebeanList().iterator(); iterator.hasNext();) {
			GenerateQuestionPaperServicebean generateQuestionPaperServicebean = (GenerateQuestionPaperServicebean) iterator.next();
			if(generateQuestionPaperServicebean.getSubject_id() == newQuestionRequest.getQuestionPaperStructure().getSubject_id() &&
					generateQuestionPaperServicebean.getQuestion_type().equals(newQuestionRequest.getQuestionPaperStructure().getQuestion_type()) &&
					generateQuestionPaperServicebean.getMarks() == newQuestionRequest.getQuestionPaperStructure().getItem_marks()){
				QuestionPaperData questionPaperData = new QuestionPaperData();
				QuestionbankDB questionbankDB = new QuestionbankDB();
				questionPaperData.setItem_id(newQuestionRequest.getQuestionPaperStructure().getItem_id());
				generateQuestionPaperServicebean.setTopic_id(Integer.parseInt(newQuestionRequest.getQuestionPaperStructure().getQuestion_topic()));
				List<Integer> questionIdsList = questionbankDB.getQuestionsForGenerateExam(inst_id, div_id, generateQuestionPaperServicebean);
				if(questionIdsList.size() > 0){
				questionIdsList =generateRandomQuestionId(questionIdsList,1);
				generateQuestionPaperServicebean.getQuestion_ids().add(questionIdsList.get(0));
				Questionbank  questionbank = questionbankDB.getQuestion(questionIdsList.get(0), inst_id, generateQuestionPaperServicebean.getSubject_id(), div_id);	
				questionPaperData.setQuestionbank(questionbank);
				if(generateQuestionPaperServicebean.getQuestion_type().equals("3")){
					 ParaQuestionBean paraQuestionBean = (ParaQuestionBean) readObject(new File(questionStorageURL+File.separator+"paragraph"+File.separator+div_id+File.separator+generateQuestionPaperServicebean.getSubject_id()+File.separator+generateQuestionPaperServicebean.getQuestion_ids().get(generateQuestionPaperServicebean.getQuestion_PickUpCounter())));
					 ParagraphQuestion paragraphQuestion = new ParagraphQuestion();
					 if(paraQuestionBean != null){
						 paragraphQuestion.setParagraphText(paraQuestionBean.getParagraph());
					 }
					 questionPaperData.setParagraphQuestion(paragraphQuestion);
				  }
				questionPaperData.setDataStatus("Y");
				}else if(generateQuestionPaperServicebean.getQuestion_ids().size() > 0 && newQuestionRequest.getCurrentIds().size() > 0){
					generateQuestionPaperServicebean.setQuestion_ids(newQuestionRequest.getCurrentIds());
					questionIdsList = questionbankDB.getQuestionsForGenerateExam(inst_id, div_id, generateQuestionPaperServicebean);
					if(questionIdsList.size() > 0){
						questionIdsList =generateRandomQuestionId(questionIdsList,1);
						generateQuestionPaperServicebean.getQuestion_ids().add(questionIdsList.get(0));
						Questionbank  questionbank = questionbankDB.getQuestion(questionIdsList.get(0), inst_id, generateQuestionPaperServicebean.getSubject_id(), div_id);	
						questionPaperData.setQuestionbank(questionbank);
						if(generateQuestionPaperServicebean.getQuestion_type().equals("3")){
							ParaQuestionBean paraQuestionBean = (ParaQuestionBean) readObject(new File(questionStorageURL+File.separator+"paragraph"+File.separator+div_id+File.separator+generateQuestionPaperServicebean.getSubject_id()+File.separator+questionbank.getQue_id()));
							 ParagraphQuestion paragraphQuestion = new ParagraphQuestion();
							 if(paraQuestionBean != null){
								 paragraphQuestion.setParagraphText(paraQuestionBean.getParagraph());
							 }
							 questionPaperData.setParagraphQuestion(paragraphQuestion);
						  }
						questionPaperData.setDataStatus("Y");
					}
					else{
						questionPaperData.setDataStatus("MN");
					}
				}else{
					questionPaperData.setDataStatus("N");
				}
				List<QuestionPaperData> paperDatasList = new ArrayList<QuestionPaperData>();
				paperDatasList.add(questionPaperData);
				generateQuestionPaperResponse.setQuestionPaperDataList(paperDatasList);
				generateQuestionPaperServicebean.setTopic_id(-1);
				flag = true;
				break;
			}
		}
		if(flag == false){
			QuestionPaperData questionPaperData = new QuestionPaperData();
			QuestionbankDB questionbankDB = new QuestionbankDB();
			GenerateQuestionPaperServicebean generateQuestionPaperServicebean = new GenerateQuestionPaperServicebean();
			questionPaperData.setItem_id(newQuestionRequest.getQuestionPaperStructure().getItem_id());
			generateQuestionPaperServicebean.setCount(1);
			generateQuestionPaperServicebean.setMarks(newQuestionRequest.getQuestionPaperStructure().getItem_marks());
			List<Integer> Ids = new ArrayList<Integer>();
			for (Iterator iterator = newQuestionRequest.getGenerateQuestionPaperServicebeanList().iterator(); iterator.hasNext();) {
				GenerateQuestionPaperServicebean servicebean = (GenerateQuestionPaperServicebean) iterator.next();
				if(servicebean.getSubject_id() == newQuestionRequest.getQuestionPaperStructure().getSubject_id() &&
				   servicebean.getQuestion_type().equals(newQuestionRequest.getQuestionPaperStructure().getQuestion_type()) &&
				   servicebean.getMarks() == newQuestionRequest.getQuestionPaperStructure().getItem_marks()){
					Ids.addAll(servicebean.getQuestion_ids());
				}
			}
			generateQuestionPaperServicebean.setQuestion_ids(Ids);
			generateQuestionPaperServicebean.setQuestion_PickUpCounter(0);
			generateQuestionPaperServicebean.setQuestion_type(newQuestionRequest.getQuestionPaperStructure().getQuestion_type());
			generateQuestionPaperServicebean.setSubject_id(newQuestionRequest.getQuestionPaperStructure().getSubject_id());
			if(newQuestionRequest.getQuestionPaperStructure().getQuestion_topic() != null && !"".equals(newQuestionRequest.getQuestionPaperStructure().getQuestion_topic())){
			generateQuestionPaperServicebean.setTopic_id(Integer.parseInt(newQuestionRequest.getQuestionPaperStructure().getQuestion_topic()));
			}
			List<Integer> questionIdsList = questionbankDB.getQuestionsForGenerateExam(inst_id, div_id, generateQuestionPaperServicebean);
			if(questionIdsList.size() > 0){
				questionIdsList =generateRandomQuestionId(questionIdsList,1);
				generateQuestionPaperServicebean.getQuestion_ids().add(questionIdsList.get(0));
				Questionbank  questionbank = questionbankDB.getQuestion(questionIdsList.get(0), inst_id, generateQuestionPaperServicebean.getSubject_id(), div_id);	
				questionPaperData.setQuestionbank(questionbank);
				if(generateQuestionPaperServicebean.getQuestion_type().equals("3")){
					ParaQuestionBean paraQuestionBean = (ParaQuestionBean) readObject(new File(questionStorageURL+File.separator+"paragraph"+File.separator+div_id+File.separator+questionbank.getQue_id()));
					 ParagraphQuestion paragraphQuestion = new ParagraphQuestion();
					 if(paraQuestionBean != null){
						 paragraphQuestion.setParagraphText(paraQuestionBean.getParagraph());
					 }
					 questionPaperData.setParagraphQuestion(paragraphQuestion);
				  }
				questionPaperData.setDataStatus("Y");
				}else{
					questionPaperData.setDataStatus("N");
				}
			List<QuestionPaperData> paperDatasList = new ArrayList<QuestionPaperData>();
			paperDatasList.add(questionPaperData);
			generateQuestionPaperResponse.setQuestionPaperDataList(paperDatasList);
			newQuestionRequest.getGenerateQuestionPaperServicebeanList().add(generateQuestionPaperServicebean);
		}
		generateQuestionPaperResponse.setQuestionPaperServicebeanList(newQuestionRequest.getGenerateQuestionPaperServicebeanList());
		return generateQuestionPaperResponse;
	}
	
	public List<QuestionPaperData> getQuestionList(int div_id,NewQuestionRequest newQuestionRequest) {
		QuestionbankDB questionbankDB = new QuestionbankDB();
		List<Questionbank> questionbankList = questionbankDB.getQuestionBankList(inst_id, div_id, newQuestionRequest.getQuestionPaperStructure().getSubject_id(), Integer.parseInt(newQuestionRequest.getQuestionPaperStructure().getQuestion_topic()), newQuestionRequest.getQuestionPaperStructure().getQuestion_type(), newQuestionRequest.getQuestionPaperStructure().getItem_marks());
		List<QuestionPaperData> questionPaperDataList = new ArrayList<QuestionPaperData>();
		for (Iterator iterator = questionbankList.iterator(); iterator
				.hasNext();) {
			Questionbank questionbank = (Questionbank) iterator
					.next();
			QuestionPaperData questionPaperData = new QuestionPaperData();
			if(questionbank.getQue_type().equals("3")){
				ParaQuestionBean paraQuestionBean = (ParaQuestionBean) readObject(new File(questionStorageURL+File.separator+"paragraph"+File.separator+div_id+File.separator+questionbank.getSub_id()+File.separator+questionbank.getQue_id()));
				 ParagraphQuestion paragraphQuestion = new ParagraphQuestion();
				 if(paraQuestionBean != null){
					 paragraphQuestion.setParagraphText(paraQuestionBean.getParagraph());
				 }
				 questionPaperData.setParagraphQuestion(paragraphQuestion);
			  }
			questionPaperData.setQuestionbank(questionbank);
			questionPaperData.setItem_id(newQuestionRequest.getQuestionPaperStructure().getItem_id());
			questionPaperDataList.add(questionPaperData);
			
		}
		return questionPaperDataList;
	}
	
	public List<QuestionPaper> getQuestionPaperList(int div_id,int subject) {
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		return questionPaperDB.getQuestionPaperList(div_id, inst_id,subject);
	}
	
	public List<QuestionPaper> getQuestionPaperListRelatedToExam(int div_id,int paper_id) {
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		return questionPaperDB.getQuestionPaperListRelatedToExam(paper_id, inst_id,div_id);
	}
	
	public boolean checkQuestionPaperAvailability(int paper_id,int inst_id,int div_id,int sub_id) {
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		return questionPaperDB.checkQuestionPaperAvailability(paper_id, inst_id,div_id,sub_id);
	}
	
	public boolean deleteQuestionPaper(int div_id,int paper_id) {
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		File file = new File(questionPaperStorageURL+File.separator+div_id+File.separator+paper_id);
		try {
			delete(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return questionPaperDB.deleteQuestionPaper(paper_id, inst_id, div_id);
	}
	
	public boolean deleteQuestionPaperRelatedToClass(int inst_id,int div_id) {
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		File file = new File(patternStorageURL+File.separator+div_id);
		try {
			delete(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return questionPaperDB.deleteQuestionPaperRelatedToClass(inst_id, div_id);
	}
	
	public boolean saveQuestionPaper(QuestionPaperFileObject fileObject,int userID,int subject,String compSubjectIds) {
		boolean success = false;
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		if(questionPaperDB.verifyPaperName(fileObject.getClass_id(), inst_id, fileObject.getPaper_description())){
			return false;
		}else{
		QuestionPaper questionPaper = new QuestionPaper();
		questionPaper.setDiv_id(fileObject.getClass_id());
		questionPaper.setInst_id(inst_id);
		questionPaper.setMarks(fileObject.getMarks());
		questionPaper.setPaper_description(fileObject.getPaper_description());
		questionPaper.setCreated_by(userID);
		questionPaper.setCreated_dt(new Date(new java.util.Date().getTime()));
		questionPaper.setSub_id(subject);
		questionPaper.setCompSubjectIds(compSubjectIds);
		int paper_id =questionPaperDB.save(questionPaper);
		fileObject.setPaper_id(paper_id);
		if(paper_id !=-1 ){
			String filePath = questionPaperStorageURL+File.separator+fileObject.getClass_id()+File.separator+paper_id;
			writeObject(filePath, fileObject);
			success = true;
		}
		}
		
		return success;
	}
	
	public boolean updateQuestionPaper(QuestionPaperFileObject fileObject,int userID,String compSubjectIds) {
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		if(questionPaperDB.verifyUpdatePaperName(fileObject.getClass_id(), inst_id, fileObject.getPaper_description(),fileObject.getPaper_id())){
			return false;
		}else{
		QuestionPaper questionPaper = new QuestionPaper();
		questionPaper.setDiv_id(fileObject.getClass_id());
		questionPaper.setInst_id(inst_id);
		questionPaper.setMarks(fileObject.getMarks());
		questionPaper.setPaper_description(fileObject.getPaper_description());
		questionPaper.setModified_by(userID);
		questionPaper.setModified_dt(new Date(new java.util.Date().getTime()));
		questionPaper.setPaper_id(fileObject.getPaper_id());
		questionPaper.setCompSubjectIds(compSubjectIds);
		questionPaperDB.updateQuestionPaper(questionPaper);
		fileObject.setInst_id(inst_id);
		String filePath = questionPaperStorageURL+File.separator+fileObject.getClass_id()+File.separator+fileObject.getPaper_id();
		writeObject(filePath, fileObject);
		}
		return true;
	}
	
	public EditQuestionPaper getQuestionPaper(int div_id,int paper_id) {
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		File file = new File(questionPaperStorageURL+File.separator+div_id+File.separator+paper_id);
		QuestionPaperFileObject fileObject = (QuestionPaperFileObject) readObject(file);
		List<QuestionPaperFileElement> fileElements=fileObject.getQuestionPaperFileElementList();
		List<List<Integer>> list = new ArrayListOverride<List<Integer>>();
		for (Iterator iterator = fileElements.iterator(); iterator.hasNext();) {
			QuestionPaperFileElement questionPaperFileElement = (QuestionPaperFileElement) iterator.next();
			List<Integer> IdList = new ArrayListOverride<Integer>();
			IdList.add(div_id);
			IdList.add(questionPaperFileElement.getSubject_id());
			IdList.add(questionPaperFileElement.getQues_no());
			list.add(IdList);
		}
		QuestionbankDB questionbankDB = new QuestionbankDB();
		List<Questionbank> questionbankList = questionbankDB.getQuestionBankList(list,inst_id);
		QuestionPaperEditFileObject editFileObject = new QuestionPaperEditFileObject();
		editFileObject.setClass_id(fileObject.getClass_id());
		editFileObject.setInst_id(fileObject.getInst_id());
		editFileObject.setMarks(fileObject.getMarks());
		editFileObject.setPaper_description(fileObject.getPaper_description());
		editFileObject.setPaper_id(fileObject.getPaper_id());
		editFileObject.setPattern_id(fileObject.getPattern_id());
		List<QuestionPaperEditFileElement> editFileElementList = new ArrayList<QuestionPaperEditFileElement>();
		for (Iterator iterator = fileElements.iterator(); iterator
				.hasNext();) {
			QuestionPaperFileElement fileElement = (QuestionPaperFileElement) iterator.next();
			QuestionPaperEditFileElement editFileElement = new QuestionPaperEditFileElement();
			//editFileElement.setAlternate_value(alternate_value)
			try {
				BeanUtils.copyProperties(editFileElement, fileElement);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			editFileElementList.add(editFileElement);
		}
		List<GenerateQuestionPaperServicebean> generateQuestionPaperServicebeans = new ArrayList<GenerateQuestionPaperServicebean>();
		for (Iterator iterator = editFileElementList.iterator(); iterator
				.hasNext();) {
			boolean flag = false;
			QuestionPaperEditFileElement editFileElement = (QuestionPaperEditFileElement) iterator.next();
			for (Iterator iterator2 = questionbankList.iterator(); iterator2
					.hasNext();) {
				Questionbank questionbank = (Questionbank) iterator2
						.next();
				
				/************Load exam image******/
				
				if(editFileElement.getSubject_id() == questionbank.getSub_id() && editFileElement.getQues_no() == questionbank.getQue_id()){
					List<String>primaryImage = getPrimaryImage(this.inst_id, questionbank.getQue_id(), questionbank.getQue_type(),div_id,questionbank.getSub_id());
					questionbank.setPrimaryImage(primaryImage);
					
					HashMap<Integer, List<String>>secondaryImage = getSecondaryImage(this.inst_id, questionbank.getQue_id(), questionbank.getQue_type(),div_id,questionbank.getSub_id());
					questionbank.setSecondaryImage(secondaryImage);
					
					editFileElement.setQuestionbank(questionbank);
					if("3".equals(questionbank.getQue_type())){
						 ParaQuestionBean paraQuestionBean = (ParaQuestionBean) readObject(new File(questionStorageURL+File.separator+"paragraph"+File.separator+div_id+File.separator+questionbank.getSub_id()+File.separator+questionbank.getQue_id()));
						 ParagraphQuestion paragraphQuestion = new ParagraphQuestion();
						 if(paraQuestionBean != null){
							 paragraphQuestion.setParagraphText(paraQuestionBean.getParagraph()); 
						 }
						 editFileElement.setParagraphQuestion(paragraphQuestion);
					}
				}
				
			}
			GenerateQuestionPaperServicebean questionPaperServicebean = new GenerateQuestionPaperServicebean();
			questionPaperServicebean.setMarks(editFileElement.getItem_marks());
			questionPaperServicebean.setQuestion_type(editFileElement.getQuestion_type());
			questionPaperServicebean.setSubject_id(editFileElement.getSubject_id());
			if(editFileElement.getQuestion_topic() != null && !"".equals(editFileElement.getQuestion_topic())){
			questionPaperServicebean.setTopic_id(Integer.parseInt(editFileElement.getQuestion_topic()));
			}
			for (Iterator innerIterator = generateQuestionPaperServicebeans
					.iterator(); innerIterator.hasNext();) {
				GenerateQuestionPaperServicebean generateQuestionPaperServicebean = (GenerateQuestionPaperServicebean) innerIterator
						.next();
				if(generateQuestionPaperServicebean.getSubject_id() == editFileElement.getSubject_id() 
						/*&& generateQuestionPaperServicebean.getTopic_id() == Integer.parseInt(editFileElement.getQuestion_topic())*/
						&& generateQuestionPaperServicebean.getQuestion_type().equals(editFileElement.getQuestion_type()) 
						&& generateQuestionPaperServicebean.getMarks() == editFileElement.getItem_marks()){
					List<Integer> Ids = new ArrayList<Integer>();
					Ids =  generateQuestionPaperServicebean.getQuestion_ids();
					Ids.add(editFileElement.getQues_no());
					generateQuestionPaperServicebean.setCount(generateQuestionPaperServicebean.getCount()+1);
					generateQuestionPaperServicebean.setQuestion_ids(Ids);
					flag = true;
					break;
				}
				
			}
			if(flag == false){
			List<Integer> Ids = new ArrayList<Integer>();
			Ids.add(editFileElement.getQues_no());
			questionPaperServicebean.setQuestion_ids(Ids);
			questionPaperServicebean.setCount(1);
			generateQuestionPaperServicebeans.add(questionPaperServicebean);
			}
			
		}
		editFileObject.setQuestionPaperFileElementList(editFileElementList);
		EditQuestionPaper editQuestionPaper = new EditQuestionPaper();
		editQuestionPaper.setFileObject(editFileObject);
		editQuestionPaper.setGenerateQuestionPaperServicebeanList(generateQuestionPaperServicebeans);
		return editQuestionPaper;
	}
	
	public OnlineExamPaper getOnlineQuestionPaper(int div_id,int paper_id,int instId) {
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		File file = new File(this.questionPaperStorageURL+File.separator+div_id+File.separator+paper_id);
		QuestionPaperFileObject fileObject = (QuestionPaperFileObject) readObject(file);
		List<QuestionPaperFileElement> fileElementsUnsorted=fileObject.getQuestionPaperFileElementList();
		
		List<QuestionPaperFileElement> fileElements = makeTree(fileElementsUnsorted);
		List<List<Integer>> list = new ArrayListOverride<List<Integer>>();
		for (Iterator iterator = fileElements.iterator(); iterator.hasNext();) {
			QuestionPaperFileElement questionPaperFileElement = (QuestionPaperFileElement) iterator.next();
			List<Integer> IdList = new ArrayListOverride<Integer>();
			IdList.add(div_id);
			IdList.add(questionPaperFileElement.getSubject_id());
			IdList.add(questionPaperFileElement.getQues_no());
			list.add(IdList);
		}
		QuestionbankDB questionbankDB = new QuestionbankDB();
		List<Questionbank> questionbankList = questionbankDB.getQuestionBankList(list,inst_id);
		OnlineExamPaper onlineExamPaper = new OnlineExamPaper();
		onlineExamPaper.setClass_id(fileObject.getClass_id());
		onlineExamPaper.setInst_id(fileObject.getInst_id());
		onlineExamPaper.setMarks(fileObject.getMarks());
		onlineExamPaper.setPaper_description(fileObject.getPaper_description());
		onlineExamPaper.setPaper_id(fileObject.getPaper_id());
		onlineExamPaper.setPattern_id(fileObject.getPattern_id());
		List<OnlineExamPaperElement> onlineExamPaperElementList = new ArrayList<OnlineExamPaperElement>();
		for (Iterator iterator = fileElements.iterator(); iterator
				.hasNext();) {
			QuestionPaperFileElement fileElement = (QuestionPaperFileElement) iterator.next();
			OnlineExamPaperElement onlineExamPaperElement = new OnlineExamPaperElement();
			//editFileElement.setAlternate_value(alternate_value)
			try {
				BeanUtils.copyProperties(onlineExamPaperElement, fileElement);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			onlineExamPaperElementList.add(onlineExamPaperElement);
		}
		
		for (Iterator iterator = onlineExamPaperElementList.iterator(); iterator
				.hasNext();) {
			OnlineExamPaperElement onlineExamPaperElement = (OnlineExamPaperElement) iterator.next();
			for (Iterator iterator2 = questionbankList.iterator(); iterator2
					.hasNext();) {
				Questionbank questionbank = (Questionbank) iterator2
						.next();
				if(onlineExamPaperElement.getSubject_id() == questionbank.getSub_id() && onlineExamPaperElement.getQues_no() == questionbank.getQue_id()){
					if(questionbank.getAns_id() != null && !"".equals(questionbank.getAns_id())){
						if(questionbank.getAns_id().split(",").length>1){
							onlineExamPaperElement.setAns_type("multiple");
						}else{
							onlineExamPaperElement.setAns_type("single");
						}
					
					onlineExamPaperElement.setQuestionbank(questionbank);
					if("3".equals(questionbank.getQue_type())){
						 ParagraphQuestion paragraphQuestion = (ParagraphQuestion) readObject(new File(questionStorageURL+File.separator+questionbank.getSub_id()+File.separator+div_id+File.separator+questionbank.getQue_id()));
						 onlineExamPaperElement.setParagraphQuestion(paragraphQuestion);
					}
					}
				}
				
				List<String>primaryImage = getPrimaryImage(instId, questionbank.getQue_id(), questionbank.getQue_type(),div_id,questionbank.getSub_id());
				questionbank.setPrimaryImage(primaryImage);
				
				HashMap<Integer, List<String>>secondaryImage = getSecondaryImage(instId, questionbank.getQue_id(), questionbank.getQue_type(),div_id,questionbank.getSub_id());
				questionbank.setSecondaryImage(secondaryImage);
				
			}
			
		}
		onlineExamPaper.setOnlineExamPaperElementList(onlineExamPaperElementList);
		return onlineExamPaper;
	}
	
	public OnlineExam getOnlineQuestionPaperMarks(int div_id,int paper_id,String answers,OnlineExam onlineExam) {
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		File file = new File(questionPaperStorageURL+File.separator+div_id+File.separator+paper_id);
		QuestionPaperFileObject fileObject = (QuestionPaperFileObject) readObject(file);
		List<QuestionPaperFileElement> fileElements=fileObject.getQuestionPaperFileElementList();
		List<List<Integer>> list = new ArrayListOverride<List<Integer>>();
		for (Iterator iterator = fileElements.iterator(); iterator.hasNext();) {
			QuestionPaperFileElement questionPaperFileElement = (QuestionPaperFileElement) iterator.next();
			List<Integer> IdList = new ArrayListOverride<Integer>();
			IdList.add(div_id);
			IdList.add(questionPaperFileElement.getSubject_id());
			IdList.add(questionPaperFileElement.getQues_no());
			list.add(IdList);
		}
		QuestionbankDB questionbankDB = new QuestionbankDB();
		List<Questionbank> questionbankList = questionbankDB.getQuestionBankList(list,inst_id);
		OnlineExamPaper onlineExamPaper = new OnlineExamPaper();
		onlineExamPaper.setClass_id(fileObject.getClass_id());
		onlineExamPaper.setInst_id(fileObject.getInst_id());
		onlineExamPaper.setMarks(fileObject.getMarks());
		onlineExamPaper.setPaper_description(fileObject.getPaper_description());
		onlineExamPaper.setPaper_id(fileObject.getPaper_id());
		onlineExamPaper.setPattern_id(fileObject.getPattern_id());
		List<OnlineExamPaperElement> onlineExamPaperElementList = new ArrayList<OnlineExamPaperElement>();
		for (Iterator iterator = fileElements.iterator(); iterator
				.hasNext();) {
			QuestionPaperFileElement fileElement = (QuestionPaperFileElement) iterator.next();
			OnlineExamPaperElement onlineExamPaperElement = new OnlineExamPaperElement();
			//editFileElement.setAlternate_value(alternate_value)
			try {
				BeanUtils.copyProperties(onlineExamPaperElement, fileElement);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			onlineExamPaperElementList.add(onlineExamPaperElement);
		}
		String[] ansArray = answers.split("/");
		int counter = 0;
		int totalMarks = 0;
		for (Iterator iterator = onlineExamPaperElementList.iterator(); iterator
				.hasNext();) {
			OnlineExamPaperElement onlineExamPaperElement = (OnlineExamPaperElement) iterator.next();
			if("Question".equals(onlineExamPaperElement.getItem_type()) ){
			for (Iterator iterator2 = questionbankList.iterator(); iterator2
					.hasNext();) {
				Questionbank questionbank = (Questionbank) iterator2
						.next();
				if(onlineExamPaperElement.getSubject_id() == questionbank.getSub_id() && onlineExamPaperElement.getQues_no() == questionbank.getQue_id()){
					if(questionbank.getAns_id() != null && !"".equals(questionbank.getAns_id())){
						if(questionbank.getAns_id().replace(" ", "").equals(ansArray[counter].replace(" ", ""))){
						totalMarks = totalMarks + questionbank.getMarks();
					}
						counter++;
					}
				}
				
			}
			}else{
				counter++;
			}
		}
		onlineExam.setMarks(totalMarks);
		return onlineExam;
	}
	
	/*public List<OnlineExamPaperElement> getAlinedExam(List<OnlineExamPaperElement>) {
		
	}*/
	
	class ArrayListOverride<E> extends ArrayList<E>{
		@Override
		public String toString() {
			Iterator<E> it = iterator();
	        if (! it.hasNext())
	            return "()";

	        StringBuilder sb = new StringBuilder();
	        sb.append('(');
	        for (;;) {
	            E e = it.next();
	            sb.append(e == this ? "(this Collection)" : e);
	            if (! it.hasNext())
	                return sb.append(')').toString();
	            sb.append(',').append(' ');
	        }
	    }

		}
	
		
	
	private void writeObject(String filePath,Object questionData){
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			File file = new File(filePath);
			if(!file.exists() || file.isDirectory()){
				if(file.isDirectory()){
					delete(file);
				}
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			fout = new FileOutputStream(filePath);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(questionData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null!=oos)try {oos.close();} catch (IOException e) {e.printStackTrace();}
			if(null!=fout)try {fout.close();} catch (IOException e) {e.printStackTrace();}
		}
		
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
	
	private List<Integer> generateRandomQuestionId(List<Integer> allQuestionId,int count){
		
		List<Integer> questionId = new ArrayList<Integer>();
		Set<Integer> questionIdSet = new HashSet<Integer>();
		Random random = new Random();
		while(questionIdSet.size()<count && allQuestionId.size()>questionIdSet.size()){
			int index = random.nextInt(allQuestionId.size());
			questionIdSet.add(allQuestionId.get(index));
		}
		questionId.addAll(questionIdSet);
		return questionId;
	}
	
	private List<QuestionPaperFileElement> formTreeForQuestions(List<QuestionPaperFileElement> questionPaperFileElements){
		List<QuestionPaperFileElement> questionsCopy = new ArrayList<QuestionPaperFileElement>();
		questionsCopy.addAll(questionPaperFileElements);
		
		
		return questionPaperFileElements;
	}
	
	public List<QuestionPaperFileElement> makeTree(List<QuestionPaperFileElement> nodes){
		List<QuestionPaperFileElement> rootQuestionPaperFileElements = new ArrayList<QuestionPaperFileElement>();
		for(QuestionPaperFileElement node:nodes){
			if(node.getParent_id() == null || node.getParent_id().equals("undefined")){
				rootQuestionPaperFileElements.add(node);
			}
		}
		nodes.removeAll(rootQuestionPaperFileElements);
		
		List<QuestionPaperFileElement>sortedTreeList = new ArrayList<QuestionPaperFileElement>();
		for(QuestionPaperFileElement node:rootQuestionPaperFileElements){
			sortedTreeList.add(node);
			List<QuestionPaperFileElement>cQuestionPaperFileElement = getChildQuestionPaperFileElements(node, nodes);
			sortedTreeList.addAll(cQuestionPaperFileElement);
		}
		System.out.println("Sorted list...."+sortedTreeList);
		return sortedTreeList;
	}
	
	public List<QuestionPaperFileElement> getChildQuestionPaperFileElements(QuestionPaperFileElement rootQuestionPaperFileElement,List<QuestionPaperFileElement>nodes){
		List<QuestionPaperFileElement> childQuestionPaperFileElements = new ArrayList<QuestionPaperFileElement>();
		boolean found = false;
		for(QuestionPaperFileElement node:nodes){
			if(rootQuestionPaperFileElement.getItem_id().equals(node.getParent_id())){
				List<QuestionPaperFileElement> childQuestionPaperFileElementsFromRecursion = 
						getChildQuestionPaperFileElements(node, nodes);
				if(childQuestionPaperFileElementsFromRecursion.size() == 0){
					childQuestionPaperFileElements.add(node);
				}else{
					childQuestionPaperFileElements.add(node);
					childQuestionPaperFileElements.addAll(childQuestionPaperFileElementsFromRecursion);
				}
				
			}
		}
		return childQuestionPaperFileElements;
	}
	
	public HashMap<String, Integer> evaluteExam(Map<String,List<String>> examMap,int division,int questionPaperId,int regId){
		
		int marks = 0;
		int totalMarks = 0;
		OnlineExamPaper onlineExamPaper = this.getOnlineQuestionPaper(division,questionPaperId,regId);
		List<OnlineExamPaperElement> examPaperElements = onlineExamPaper.getOnlineExamPaperElementList();
		
		List<OnlineExamPaperElementEvaluate>examPaperElementEvaluates = new ArrayList<OnlineExamPaperElementEvaluate>();
		//AlternateMap to store alternate question marks
		//alternateTotalMarksMap to store total marks
		HashMap<Integer, Integer>alternateMap = new HashMap<Integer,Integer>();
		HashMap<Integer, Integer>alternateTotalMarksMap = new HashMap<Integer,Integer>();
		for(OnlineExamPaperElement onlineExamPaperElement:examPaperElements){
			OnlineExamPaperElementEvaluate onlineExamPaperElementEvaluate = new OnlineExamPaperElementEvaluate();
			try {
				BeanUtils.copyProperties(onlineExamPaperElementEvaluate, onlineExamPaperElement);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int queNo = onlineExamPaperElement.getQues_no();
			List<String> list = (List) examMap.get(queNo+"");
			Questionbank questionBank = onlineExamPaperElement.getQuestionbank();
			int alternateValue = onlineExamPaperElement.getAlternate_value();
			//If question bank is null then its not a question
			if(null!=list && null!=questionBank){
				if(alternateValue!=0 && !alternateTotalMarksMap.containsKey(alternateValue)){
					alternateTotalMarksMap.put(alternateValue, questionBank.getMarks());
				}else{
					totalMarks = totalMarks +questionBank.getMarks();
				}
				for(String l:list){
					
					if(questionBank.getAns_id().contains(l)){
						System.out.println(questionBank.getQue_text()+"::::::Answer is correct");
						onlineExamPaperElementEvaluate.setObtainedMarks(questionBank.getMarks());
						if(alternateValue!=0){
							if(!alternateMap.containsKey(alternateValue)|| alternateMap.get(alternateValue)<questionBank.getMarks()){
								alternateMap.put(alternateValue, questionBank.getMarks());
								//alternateTotalMarksMap.put(alternateValue, questionBank.getMarks());
							}
							
						}else{
							marks = marks + questionBank.getMarks();
						}
					}else{
						System.out.println(questionBank.getQue_text()+":::::::Answer is wrong");
						
					}
				}
				
			}
			//onlineExamPaperElementEvaluate
			examPaperElementEvaluates.add(onlineExamPaperElementEvaluate);
			
		}
		System.out.println(examPaperElementEvaluates);
		int m = getChildMark(examPaperElementEvaluates,0);
		System.out.println("Makrs...................."+m);
		 
		//Add all "OR" question marks
		
		Iterator it = alternateMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        marks = marks + (Integer)pair.getValue(); 
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    /*
	    Iterator itTotalMarks = alternateTotalMarksMap.entrySet().iterator();
	    while (itTotalMarks.hasNext()) {
	        Map.Entry pair = (Map.Entry)itTotalMarks.next();
	        totalMarks = totalMarks + (Integer)pair.getValue(); 
	        itTotalMarks.remove(); // avoids a ConcurrentModificationException
	    }
	    */
		
		HashMap<String, Integer>marksObj = new HashMap<String, Integer>();
		marksObj.put("marks", marks);
		marksObj.put("totalmarks", totalMarks);
		return marksObj;
	}
	
	public int getChildMark(List<OnlineExamPaperElementEvaluate>elementEvaluates,int index){
		int marks = 0;
		OnlineExamPaperElementEvaluate onlineExamPaperElementEvaluate = elementEvaluates.get(index);
		if(null != onlineExamPaperElementEvaluate.getQuestionbank()){
			if(onlineExamPaperElementEvaluate.getAlternate_value()!=0){
			do{
				if(index>=elementEvaluates.size()){
					break;
				}
				onlineExamPaperElementEvaluate = elementEvaluates.get(index);
				marks =  onlineExamPaperElementEvaluate.getObtainedMarks();
				//Break loop if you got a correct answer
				if(marks!=0){
					break;
				}
				index++;
			}while(onlineExamPaperElementEvaluate.getAlternate_value()!=0);
			}else{
				marks = onlineExamPaperElementEvaluate.getObtainedMarks();
			}
		}else{
			index++;
			//if marks is not a question then its a instruction and can have children 
			marks = getChildMark(elementEvaluates, index);
		}
		return marks;
	}
	
	public List<String> getPrimaryImage(int regId,int questionId,String questionType,int div_id,int sub_id){
		List<String> primaryImages = new ArrayList<String>();
		String primaryImageDest = "";
		if(questionType.equals("1")){
			primaryImageDest = Constants.STORAGE_PATH + File.separatorChar + regId + File.separatorChar+ EXAM_IMAGE_FOLDER + File.separatorChar + SUBJECTIVE + File.separatorChar + div_id + File.separatorChar + sub_id + File.separatorChar + questionId;
		}else 
		if(questionType.equals("2")){
			primaryImageDest = Constants.STORAGE_PATH + File.separatorChar + regId + File.separatorChar+ EXAM_IMAGE_FOLDER + File.separatorChar + OBJECTIVE + File.separatorChar + div_id + File.separatorChar + sub_id + File.separatorChar  + questionId + File.separatorChar + QUESTION ;
		}else 
		if(questionType.equals("3")){
			primaryImageDest = Constants.STORAGE_PATH + File.separatorChar + regId + File.separatorChar+ EXAM_IMAGE_FOLDER + File.separatorChar + PARAGRAPH + File.separatorChar + div_id + File.separatorChar + sub_id + File.separatorChar + questionId + File.separatorChar + PARAGRAPH;
		}
		
		if(null!=primaryImageDest && primaryImageDest.trim().length()>0){
			File folder = new File(primaryImageDest);
			
			if(null==folder || !folder.exists()){
				return primaryImages;
			}
			File[] images = folder.listFiles();
			
			if(null==images || !(images.length != 0)){
				return primaryImages;
			}
			
			for(File image:images){
				if(image.isFile()){
					if(questionType.equals("1")){
						primaryImages.add(EXAM_IMAGE_FOLDER+"_"+SUBJECTIVE+"_"+div_id+"_"+sub_id+"_"+questionId+"_"+image.getName());
					}else if(questionType.equals("2")){
						primaryImages.add(EXAM_IMAGE_FOLDER+"_"+OBJECTIVE+"_"+div_id+"_"+sub_id+"_"+questionId+"_"+QUESTION+"_"+image.getName());
					}else if(questionType.equals("3")){
						primaryImages.add(EXAM_IMAGE_FOLDER+"_"+PARAGRAPH+"_"+div_id+"_"+sub_id+"_"+questionId+"_"+PARAGRAPH+"_"+image.getName());
					}
				}
			}
		}
		return primaryImages;
	}
	
	public HashMap<Integer, List<String>> getSecondaryImage(int regId,int questionId,String questionType,int div_id,int sub_id){
		HashMap<Integer, List<String>> secImages = new HashMap<Integer, List<String>>();
		String secImageDest = "";
		if(questionType.equals("2")){
			secImageDest = Constants.STORAGE_PATH + File.separatorChar + regId + File.separatorChar+ EXAM_IMAGE_FOLDER + File.separatorChar + OBJECTIVE + File.separatorChar + div_id + File.separatorChar + sub_id  + File.separatorChar + questionId + File.separatorChar + OPTION;
		}else 
		if(questionType.equals("3")){
			secImageDest = Constants.STORAGE_PATH + File.separatorChar + regId + File.separatorChar+ EXAM_IMAGE_FOLDER + File.separatorChar + PARAGRAPH + File.separatorChar + div_id + File.separatorChar + sub_id + File.separatorChar + questionId + File.separatorChar + PARAGRAPH_QUESTION;
		}
		
		if(null!=secImageDest && secImageDest.trim().length()>0){
			File optionsImageFolder = new File(secImageDest);
			if(optionsImageFolder!=null && optionsImageFolder.exists()){
				File[] optionImageFolder = optionsImageFolder.listFiles();
				for(int index=0;index<optionImageFolder.length;index++){
					String optionImageFolderName = EXAM_IMAGE_FOLDER+"_"+OBJECTIVE+"_"+div_id+"_"+sub_id+"_"+questionId+"_"+OPTION+"_"+ optionImageFolder[index].getName();
					File[] imageFiles = optionImageFolder[index].listFiles();
					List<String> imageList = new ArrayList<String>();
					for(int indexFile=0;indexFile<imageFiles.length;indexFile++){
						imageList.add(optionImageFolderName+"_"+imageFiles[indexFile].getName());
					}
					secImages.put(Integer.parseInt(optionImageFolder[index].getName()),imageList);
				}
			}
		}
		return secImages;
	}

	
	public void setStoragePath(String storagePath){
		Constants.STORAGE_PATH = storagePath;
	}

	public String getPatternStorageURL() {
		return patternStorageURL;
	}

	public void setPatternStorageURL(String patternStorageURL) {
		this.patternStorageURL = patternStorageURL;
	}

	public int getInst_id() {
		return inst_id;
	}

	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}

	public String getQuestionStorageURL() {
		return questionStorageURL;
	}

	public void setQuestionStorageURL(String questionStorageURL) {
		this.questionStorageURL = questionStorageURL;
	}

	public String getQuestionPaperStorageURL() {
		return questionPaperStorageURL;
	}

	public void setQuestionPaperStorageURL(String questionPaperStorageURL) {
		this.questionPaperStorageURL = questionPaperStorageURL;
	}
	
	
}
