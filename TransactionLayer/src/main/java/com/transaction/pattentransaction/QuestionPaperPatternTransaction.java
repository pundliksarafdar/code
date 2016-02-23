package com.transaction.pattentransaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.TTCCLayout;

import com.classapp.db.pattern.QuestionPaperPattern;
import com.classapp.db.pattern.QuestionPaperPatternDB;
import com.classapp.db.question.Questionbank;
import com.classapp.db.question.QuestionbankDB;
import com.classapp.logger.AppLogger;
import com.classapp.persistence.Constants;
import com.datalayer.exam.ParagraphQuestion;
import com.service.beans.GenerateQuestionPaperResponse;
import com.service.beans.NewQuestionRequest;
import com.service.beans.QuestionPaperData;
import com.service.beans.GenerateQuestionPaperServicebean;
import com.service.beans.QuestionPaperStructure;

public class QuestionPaperPatternTransaction {
	String patternStorageURL;
	int inst_id;
	String questionStorageURL;
	public QuestionPaperPatternTransaction(String storageURL,int inst_id) {
		this.patternStorageURL = storageURL;
		this.inst_id = inst_id;
	}
	
	public QuestionPaperPatternTransaction(String storageURL,int inst_id,String questionStorageURL) {
		this.patternStorageURL = storageURL;
		this.inst_id = inst_id;
		this.questionStorageURL = questionStorageURL;
	}

	public boolean saveQuestionPaperPattern(com.service.beans.QuestionPaperPattern paperPattern) {
		QuestionPaperPatternDB questionPaperPatternDB = new QuestionPaperPatternDB();
		if(!questionPaperPatternDB.verifyPatterName(inst_id, paperPattern.getClass_id(), paperPattern.getPattern_name())){
		QuestionPaperPattern questionPaperPattern = new QuestionPaperPattern();
		questionPaperPattern.setDiv_id(paperPattern.getClass_id());
		questionPaperPattern.setInst_id(inst_id);
		questionPaperPattern.setMarks(paperPattern.getMarks());
		questionPaperPattern.setPattern_name(paperPattern.getPattern_name());
		questionPaperPattern.setSub_id(paperPattern.getSub_id());
		questionPaperPattern.setPattern_type(paperPattern.getPattern_type());
		int pattern_id=questionPaperPatternDB.saveQuestionPaperPattern(questionPaperPattern);
		paperPattern.setPattern_id(pattern_id);
		String filePath = patternStorageURL+File.separator+questionPaperPattern.getDiv_id()+File.separator+pattern_id;
		writeObject(filePath, paperPattern);
		}else{
			return false;
		}
		return true;
	}
	
	public boolean updateQuestionPaperPattern(com.service.beans.QuestionPaperPattern paperPattern) {
		QuestionPaperPatternDB questionPaperPatternDB = new QuestionPaperPatternDB();
		if(!questionPaperPatternDB.verifyUpdatePatterName(inst_id, paperPattern.getClass_id(), paperPattern.getPattern_name(),paperPattern.getPattern_id())){
		QuestionPaperPattern questionPaperPattern = new QuestionPaperPattern();
		questionPaperPattern.setDiv_id(paperPattern.getClass_id());
		questionPaperPattern.setInst_id(inst_id);
		questionPaperPattern.setMarks(paperPattern.getMarks());
		questionPaperPattern.setPattern_name(paperPattern.getPattern_name());
		questionPaperPattern.setSub_id(paperPattern.getSub_id());
		questionPaperPattern.setPattern_id(paperPattern.getPattern_id());
		int pattern_id=questionPaperPatternDB.saveQuestionPaperPattern(questionPaperPattern);
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
			questionPaperServicebean.setTopic_id(Integer.parseInt(paperStructure.getQuestion_topic()));
			for (Iterator iterator = generateQuestionPaperServicebeans
					.iterator(); iterator.hasNext();) {
				GenerateQuestionPaperServicebean generateQuestionPaperServicebean = (GenerateQuestionPaperServicebean) iterator
						.next();
				if(generateQuestionPaperServicebean.getSubject_id() == paperStructure.getSubject_id() && generateQuestionPaperServicebean.getTopic_id() == Integer.parseInt(paperStructure.getQuestion_topic())
						&& generateQuestionPaperServicebean.getQuestion_type().equals(paperStructure.getQuestion_type()) && generateQuestionPaperServicebean.getMarks() == paperStructure.getItem_marks()){
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
			generateQuestionPaperServicebean.setQuestion_ids(generateRandomQuestionId(generateQuestionPaperServicebean.getQuestion_ids(),generateQuestionPaperServicebean.getCount()));
			
		}
		List<QuestionPaperData> questionPaperDataList = new ArrayList<QuestionPaperData>();
		for (int i = 0; i < questionPaperPattern.size(); i++) {
			for (Iterator iterator = generateQuestionPaperServicebeans.iterator(); iterator
					.hasNext();) {
				GenerateQuestionPaperServicebean generateQuestionPaperServicebean = (GenerateQuestionPaperServicebean) iterator
						.next();
				if(questionPaperPattern.get(i).getSubject_id() == generateQuestionPaperServicebean.getSubject_id() &&
					questionPaperPattern.get(i).getItem_marks() == generateQuestionPaperServicebean.getMarks() &&
					questionPaperPattern.get(i).getQuestion_type().equals(generateQuestionPaperServicebean.getQuestion_type()) &&
					Integer.parseInt(questionPaperPattern.get(i).getQuestion_topic()) == generateQuestionPaperServicebean.getTopic_id()){
					QuestionPaperData generateQuestionPaperResponse = new QuestionPaperData();
				  com.classapp.db.question.Questionbank questionbank =  questionbankDB.getQuestion(generateQuestionPaperServicebean.getQuestion_ids().get(generateQuestionPaperServicebean.getQuestion_PickUpCounter()), inst_id, generateQuestionPaperServicebean.getSubject_id(), div_id);
				  generateQuestionPaperResponse.setQuestionbank(questionbank);
				  generateQuestionPaperResponse.setItem_id(questionPaperPattern.get(i).getItem_id());
				  if(questionPaperPattern.get(i).getQuestion_type().equals("3")){
					 ParagraphQuestion paragraphQuestion = (ParagraphQuestion) readObject(new File(questionStorageURL+File.separator+generateQuestionPaperServicebean.getSubject_id()+File.separator+div_id+File.separator+generateQuestionPaperServicebean.getQuestion_ids().get(generateQuestionPaperServicebean.getQuestion_PickUpCounter())));
					 generateQuestionPaperResponse.setParagraphQuestion(paragraphQuestion);
				  }
				  generateQuestionPaperServicebean.setQuestion_PickUpCounter(generateQuestionPaperServicebean.getQuestion_PickUpCounter()+1);
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
				GenerateQuestionPaperServicebean generateQuestionPaperService = questionbankDB.getQuestionsForGenerateExam(inst_id, div_id, generateQuestionPaperServicebean);
				generateQuestionPaperService.setQuestion_ids(generateRandomQuestionId(generateQuestionPaperService.getQuestion_ids(),1));
				if(generateQuestionPaperService.getQuestion_ids().size() > 0){
				generateQuestionPaperServicebean.getQuestion_ids().add(generateQuestionPaperService.getQuestion_ids().get(0));
				Questionbank  questionbank = questionbankDB.getQuestion(generateQuestionPaperService.getQuestion_ids().get(0), inst_id, generateQuestionPaperService.getSubject_id(), div_id);	
				questionPaperData.setQuestionbank(questionbank);
				questionPaperData.setItem_id(newQuestionRequest.getQuestionPaperStructure().getItem_id());
				if(generateQuestionPaperService.getQuestion_type().equals("3")){
					 ParagraphQuestion paragraphQuestion = (ParagraphQuestion) readObject(new File(questionStorageURL+File.separator+generateQuestionPaperServicebean.getSubject_id()+File.separator+div_id+File.separator+generateQuestionPaperServicebean.getQuestion_ids().get(generateQuestionPaperServicebean.getQuestion_PickUpCounter())));
					 questionPaperData.setParagraphQuestion(paragraphQuestion);
				  }
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
}
