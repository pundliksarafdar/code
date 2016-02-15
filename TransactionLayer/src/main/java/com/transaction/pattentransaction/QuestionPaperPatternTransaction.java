package com.transaction.pattentransaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.classapp.db.pattern.QuestionPaperPattern;
import com.classapp.db.pattern.QuestionPaperPatternDB;
import com.classapp.logger.AppLogger;
import com.classapp.persistence.Constants;

public class QuestionPaperPatternTransaction {
	String storageURL;
	int inst_id;
	public QuestionPaperPatternTransaction(String storageURL,int inst_id) {
		this.storageURL = storageURL;
		this.inst_id = inst_id;
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
		int pattern_id=questionPaperPatternDB.saveQuestionPaperPattern(questionPaperPattern);
		paperPattern.setPattern_id(pattern_id);
		String filePath = storageURL+File.separator+questionPaperPattern.getDiv_id()+File.separator+pattern_id;
		writeObject(filePath, paperPattern);
		}else{
			return false;
		}
		return true;
	}
	
	public List<com.service.beans.QuestionPaperPattern> getQuestionPaperPatternList(int div_id) {
		QuestionPaperPatternDB paperPatternDB = new QuestionPaperPatternDB();
		List<QuestionPaperPattern> questionPaperPatternList =   paperPatternDB.getQuestionPaperPatternList(inst_id, div_id);
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
		File file = new File(storageURL+File.separator+div_id+File.separator+patternid);
		com.service.beans.QuestionPaperPattern questionPaperPattern = (com.service.beans.QuestionPaperPattern) readObject(file);
		return questionPaperPattern;
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
}
