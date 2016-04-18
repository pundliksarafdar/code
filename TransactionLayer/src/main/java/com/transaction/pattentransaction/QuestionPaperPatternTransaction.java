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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
import com.service.beans.GenerateQuestionPaperResponse;
import com.service.beans.NewQuestionRequest;
import com.service.beans.OnlineExam;
import com.service.beans.OnlineExamPaper;
import com.service.beans.OnlineExamPaperElement;
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
	public QuestionPaperPatternTransaction(String storageURL,int inst_id) {
		this.patternStorageURL = storageURL;
		this.inst_id = inst_id;
	}
	
	public QuestionPaperPatternTransaction(String storageURL,int inst_id,String questionStorageURL) {
		this.patternStorageURL = storageURL;
		this.inst_id = inst_id;
		this.questionStorageURL = questionStorageURL;
	}

	public boolean saveQuestionPaperPattern(com.service.beans.QuestionPaperPattern paperPattern,int addedby) {
		QuestionPaperPatternDB questionPaperPatternDB = new QuestionPaperPatternDB();
		if(!questionPaperPatternDB.verifyPatterName(inst_id, paperPattern.getClass_id(), paperPattern.getPattern_name())){
		QuestionPaperPattern questionPaperPattern = new QuestionPaperPattern();
		questionPaperPattern.setDiv_id(paperPattern.getClass_id());
		questionPaperPattern.setInst_id(inst_id);
		questionPaperPattern.setMarks(paperPattern.getMarks());
		questionPaperPattern.setPattern_name(paperPattern.getPattern_name());
		questionPaperPattern.setSub_id(paperPattern.getSub_id());
		questionPaperPattern.setPattern_type(paperPattern.getPattern_type());
		questionPaperPattern.setAddedby(addedby);
		int pattern_id=questionPaperPatternDB.saveQuestionPaperPattern(questionPaperPattern);
		paperPattern.setPattern_id(pattern_id);
		String filePath = patternStorageURL+File.separator+questionPaperPattern.getDiv_id()+File.separator+pattern_id;
		writeObject(filePath, paperPattern);
		}else{
			return false;
		}
		return true;
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
	
	public List<com.service.beans.QuestionPaperPattern> getQuestionPaperPatternList(int div_id,String pattern_type) {
		QuestionPaperPatternDB paperPatternDB = new QuestionPaperPatternDB();
		List<QuestionPaperPattern> questionPaperPatternList =   paperPatternDB.getQuestionPaperPatternList(inst_id, div_id,pattern_type);
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
					 ParagraphQuestion paragraphQuestion = (ParagraphQuestion) readObject(new File(questionStorageURL+File.separator+generateQuestionPaperServicebean.getSubject_id()+File.separator+div_id+File.separator+generateQuestionPaperServicebean.getQuestion_ids().get(generateQuestionPaperServicebean.getQuestion_PickUpCounter())));
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
		
		for (Iterator iterator = generateQuestionPaperServicebeans.iterator(); iterator
				.hasNext();) {
			GenerateQuestionPaperServicebean generateQuestionPaperServicebean = (GenerateQuestionPaperServicebean) iterator
					.next();
			generateQuestionPaperServicebean.setQuestion_PickUpCounter(0);
		}
		GenerateQuestionPaperResponse generateQuestionPaperResponse = new GenerateQuestionPaperResponse();
		generateQuestionPaperResponse.setQuestionPaperDataList(questionPaperDataList);
		generateQuestionPaperResponse.setQuestionPaperServicebeanList(generateQuestionPaperServicebeans);
		return generateQuestionPaperResponse;
	}
	
	public GenerateQuestionPaperResponse generateQuestionPaperSpecific(int div_id,NewQuestionRequest newQuestionRequest) {
		GenerateQuestionPaperResponse generateQuestionPaperResponse = new GenerateQuestionPaperResponse();
		for (Iterator iterator = newQuestionRequest.getGenerateQuestionPaperServicebeanList().iterator(); iterator.hasNext();) {
			GenerateQuestionPaperServicebean generateQuestionPaperServicebean = (GenerateQuestionPaperServicebean) iterator.next();
			if(generateQuestionPaperServicebean.getSubject_id() == newQuestionRequest.getQuestionPaperStructure().getSubject_id() &&
					generateQuestionPaperServicebean.getTopic_id() == Integer.parseInt(newQuestionRequest.getQuestionPaperStructure().getQuestion_topic()) &&
					generateQuestionPaperServicebean.getQuestion_type().equals(newQuestionRequest.getQuestionPaperStructure().getQuestion_type()) &&
					generateQuestionPaperServicebean.getMarks() == newQuestionRequest.getQuestionPaperStructure().getItem_marks()){
				QuestionPaperData questionPaperData = new QuestionPaperData();
				QuestionbankDB questionbankDB = new QuestionbankDB();
				questionPaperData.setItem_id(newQuestionRequest.getQuestionPaperStructure().getItem_id());
				List<Integer> questionIdsList = questionbankDB.getQuestionsForGenerateExam(inst_id, div_id, generateQuestionPaperServicebean);
				if(questionIdsList.size() > 0){
				questionIdsList =generateRandomQuestionId(questionIdsList,1);
				generateQuestionPaperServicebean.getQuestion_ids().add(questionIdsList.get(0));
				Questionbank  questionbank = questionbankDB.getQuestion(questionIdsList.get(0), inst_id, generateQuestionPaperServicebean.getSubject_id(), div_id);	
				questionPaperData.setQuestionbank(questionbank);
				if(generateQuestionPaperServicebean.getQuestion_type().equals("3")){
					 ParagraphQuestion paragraphQuestion = (ParagraphQuestion) readObject(new File(questionStorageURL+File.separator+generateQuestionPaperServicebean.getSubject_id()+File.separator+div_id+File.separator+generateQuestionPaperServicebean.getQuestion_ids().get(generateQuestionPaperServicebean.getQuestion_PickUpCounter())));
					 questionPaperData.setParagraphQuestion(paragraphQuestion);
				  }
				questionPaperData.setDataStatus("Y");
				}else{
					questionPaperData.setDataStatus("N");
				}
				List<QuestionPaperData> paperDatasList = new ArrayList<QuestionPaperData>();
				paperDatasList.add(questionPaperData);
				generateQuestionPaperResponse.setQuestionPaperDataList(paperDatasList);
				break;
			}
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
				 ParagraphQuestion paragraphQuestion = (ParagraphQuestion) readObject(new File(questionStorageURL+File.separator+questionbank.getSub_id()+File.separator+div_id+File.separator+questionbank.getQue_id()));
				 questionPaperData.setParagraphQuestion(paragraphQuestion);
			  }
			questionPaperData.setQuestionbank(questionbank);
			questionPaperData.setItem_id(newQuestionRequest.getQuestionPaperStructure().getItem_id());
			questionPaperDataList.add(questionPaperData);
			
		}
		return questionPaperDataList;
	}
	
	public List<QuestionPaper> getQuestionPaperList(int div_id) {
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		return questionPaperDB.getQuestionPaperList(div_id, inst_id);
	}
	
	public boolean deleteQuestionPaper(int div_id,int paper_id) {
		QuestionPaperDB questionPaperDB = new QuestionPaperDB();
		File file = new File(patternStorageURL+File.separator+div_id+File.separator+paper_id);
		try {
			delete(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return questionPaperDB.deleteQuestion(paper_id, inst_id, div_id);
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
	
	public boolean saveQuestionPaper(QuestionPaperFileObject fileObject,int userID) {
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
		int paper_id =questionPaperDB.save(questionPaper);
		fileObject.setPaper_id(paper_id);
		String filePath = questionPaperStorageURL+File.separator+fileObject.getClass_id()+File.separator+paper_id;
		writeObject(filePath, fileObject);
		}
		return true;
	}
	
	public boolean updateQuestionPaper(QuestionPaperFileObject fileObject,int userID) {
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
		questionPaperDB.save(questionPaper);
		String filePath = questionPaperStorageURL+File.separator+fileObject.getClass_id()+File.separator+fileObject.getPaper_id();
		writeObject(filePath, fileObject);
		}
		return true;
	}
	
	public QuestionPaperEditFileObject getQuestionPaper(int div_id,int paper_id) {
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
		
		for (Iterator iterator = editFileElementList.iterator(); iterator
				.hasNext();) {
			QuestionPaperEditFileElement editFileElement = (QuestionPaperEditFileElement) iterator.next();
			for (Iterator iterator2 = questionbankList.iterator(); iterator2
					.hasNext();) {
				Questionbank questionbank = (Questionbank) iterator2
						.next();
				if(editFileElement.getSubject_id() == questionbank.getSub_id() && editFileElement.getQues_no() == questionbank.getQue_id()){
					editFileElement.setQuestionbank(questionbank);
					if("3".equals(questionbank.getQue_type())){
						 ParagraphQuestion paragraphQuestion = (ParagraphQuestion) readObject(new File(questionStorageURL+File.separator+questionbank.getSub_id()+File.separator+div_id+File.separator+questionbank.getQue_id()));
						 editFileElement.setParagraphQuestion(paragraphQuestion);
					}
				}
				
			}
			
		}
		editFileObject.setQuestionPaperFileElementList(editFileElementList);
		return editFileObject;
	}
	
	public OnlineExamPaper getOnlineQuestionPaper(int div_id,int paper_id) {
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
					
					questionbank.setAns_id("");
					onlineExamPaperElement.setQuestionbank(questionbank);
					if("3".equals(questionbank.getQue_type())){
						 ParagraphQuestion paragraphQuestion = (ParagraphQuestion) readObject(new File(questionStorageURL+File.separator+questionbank.getSub_id()+File.separator+div_id+File.separator+questionbank.getQue_id()));
						 onlineExamPaperElement.setParagraphQuestion(paragraphQuestion);
					}
					}
				}
				
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
			
		}
		onlineExam.setMarks(totalMarks);
		return onlineExam;
	}
	
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
		while(questionIdSet.size()<count){
			int index = random.nextInt(allQuestionId.size());
			questionIdSet.add(allQuestionId.get(index));
		}
		questionId.addAll(questionIdSet);
		return questionId;
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
