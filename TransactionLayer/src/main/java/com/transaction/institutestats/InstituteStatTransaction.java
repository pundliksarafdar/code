package com.transaction.institutestats;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.classapp.db.institutestats.InstituteStats;
import com.classapp.db.institutestats.InstituteStatsDB;
import com.service.beans.ParaQuestion;
import com.service.beans.ParaQuestionBean;

public class InstituteStatTransaction {
	public void save(InstituteStats stats) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();
	instituteStatsDB.save(stats);
	}
	
	public boolean updateStudentIdLimit(int inst_id,int noOfIds) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();
	return instituteStatsDB.updateStudentIdLimit(inst_id, noOfIds);
	}
	
	public boolean updateMemoryLimit(int inst_id,double memory) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.updateMemoryLimit(inst_id, memory);
	}
	
	public boolean increaseUsedStudentIds(int inst_id) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.increaseUsedStudentIds(inst_id);	
	}
	
	public boolean decreaseUsedStudentIds(int inst_id) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.decreaseUsedStudentIds(inst_id);		
	}
	
	public boolean increaseUsedMemory(int inst_id,double memory) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.increaseUsedMemory(inst_id, memory);		
	}
	
	public boolean decreaseUsedMemory(int inst_id,double memory) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.decreaseUsedMemory(inst_id, memory);	
	}
	
	public InstituteStats getStats(int inst_id) {
	InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
	return instituteStatsDB.getStats(inst_id);
	}
	
	public boolean isIDsAvailable(int inst_id,int noOfIds) {
		InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
		InstituteStats instituteStats = instituteStatsDB.getStats(inst_id);
		if(instituteStats != null){
			if(instituteStats.getAvail_ids() >= noOfIds){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
		
		}
	
	public boolean increaseUsedStudentIds(int inst_id,int noOfIds) {
		InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
		return instituteStatsDB.increaseUsedStudentIds(inst_id,noOfIds);	
		}
	
	public boolean updateStorageSpace(int inst_id,String storagepath) {
		InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
		InstituteStats stats = instituteStatsDB.getStats(inst_id);
		double size = getFolderSize(new File(storagepath))/(1024.0*1024.0);
		if(stats.getAlloc_memory() < size ){
			return false;
		}		
		return instituteStatsDB.updateStorageSpace(inst_id, size);
		}
	
	public boolean storageSpaceAvailabilityCheckForSubjective(int inst_id,List<String> imageList,String type,int question_id,String storageURL,int div_id,int sub_id) {
		InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
		InstituteStats stats = instituteStatsDB.getStats(inst_id);
		if("save".equalsIgnoreCase(type)){
		double size =0;
		for(String tempImageName:imageList){
			String imagePath = storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + inst_id + tempImageName;
			File source = new File(imagePath);
			size = size + source.length(); 
		}
		size = size /(1024 * 1024);
		if(stats.getAlloc_memory() < (stats.getUsed_memory() + size)){
			return false;
		}
		}else{
			String subjectiveImage = storageURL + File.separatorChar + inst_id + File.separatorChar+ "examImage" + File.separatorChar + "subjective" + File.separatorChar+ div_id + File.separatorChar + sub_id + File.separatorChar + question_id;
			File oldImage = new File(subjectiveImage);
			double oldSize = 0 ;
			if(oldImage.exists()){
				oldSize = getFolderSize(oldImage)/(1024.0*1024.0);
			} 
			double size =0;
			for(String tempImageName:imageList){
				String newImagePath = storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + inst_id + tempImageName;
				File source = new File(newImagePath);
				size = size + source.length(); 
			}
			size = size /(1024 * 1024);
			if(stats.getAlloc_memory() < (stats.getUsed_memory() + size - oldSize)){
				return false;
			}

			
		}
		return true;
		}
	
	public boolean storageSpaceAvailabilityCheckForObjective(int inst_id,List<String> imageList,HashMap<Integer, List<String>> optionImages,String type,int question_id,String storageURL,int div_id,int sub_id) {
		InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
		InstituteStats stats = instituteStatsDB.getStats(inst_id);
		if("save".equalsIgnoreCase(type)){
		double size =0;
		for(String tempImageName:imageList){
			String imagePath = storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + inst_id + tempImageName;
			File source = new File(imagePath);
			size = size + source.length(); 
		}
		for (Entry<Integer, List<String>> entry : optionImages.entrySet()) {
			String optionIdPath = storageURL + entry.getKey() ;
			
			List<String> tempImageList = entry.getValue(); 
			for(String tempImageName:tempImageList){
				String newImagePath = storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + inst_id + tempImageName;
				File source = new File(newImagePath);
				size = size + source.length(); 
			}
		}
		size = size /(1024 * 1024);
		if(stats.getAlloc_memory() < (stats.getUsed_memory() + size)){
			return false;
		}
		}else{
			String subjectiveImage = storageURL + File.separatorChar + inst_id + File.separatorChar+ "examImage" + File.separatorChar + "objective" + File.separatorChar+ div_id + File.separatorChar + sub_id + File.separatorChar + question_id;
			File oldImage = new File(subjectiveImage);
			double oldSize = 0 ;
			if(oldImage.exists()){
				oldSize = getFolderSize(oldImage)/(1024.0*1024.0);
			} 
			double size =0;
			for(String tempImageName:imageList){
				String imagePath = storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + inst_id + tempImageName;
				File source = new File(imagePath);
				size = size + source.length(); 
			}
			for (Entry<Integer, List<String>> entry : optionImages.entrySet()) {
				String optionIdPath = storageURL + entry.getKey() ;
				
				List<String> tempImageList = entry.getValue(); 
				for(String tempImageName:tempImageList){
					String newImagePath = storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + inst_id + tempImageName;
					File source = new File(newImagePath);
					size = size + source.length(); 
				}
			}
			size = size /(1024 * 1024);
			if(stats.getAlloc_memory() < (stats.getUsed_memory() + size - oldSize)){
				return false;
			}
		}
		return true;
		}
	
	public boolean storageSpaceAvailabilityCheckForParagraph(int inst_id,ParaQuestionBean paraQuestionBean,String storageURL,String type,int question_id) {
		InstituteStatsDB instituteStatsDB=new InstituteStatsDB();	
		InstituteStats stats = instituteStatsDB.getStats(inst_id);
		if("save".equalsIgnoreCase(type)){
			double size =0;
			for(String tempImageName:paraQuestionBean.getImages()){
				String imagePath = storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + inst_id + tempImageName;
				File source = new File(imagePath);
				size = size + source.length(); 
			}
			for(ParaQuestion paraQuestion:paraQuestionBean.getParaQuestion()){
				for(String paraQuestionImage:paraQuestion.getQueImage()){
					String imagePath = storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + inst_id + paraQuestionImage;
					File source = new File(imagePath);
					size = size + source.length(); 
				}
			}
			size = size /(1024 * 1024);
			if(stats.getAlloc_memory() < (stats.getUsed_memory() + size )){
				return false;
			}
		}else{
			String subjectiveImage = storageURL + File.separatorChar + inst_id + File.separatorChar+ "examImage" + File.separatorChar + "paragraph" + File.separatorChar+ paraQuestionBean.getClassId() + File.separatorChar + paraQuestionBean.getSubjectId() + File.separatorChar + question_id;
			File oldImage = new File(subjectiveImage);
			double oldSize = 0 ;
			if(oldImage.exists()){
				oldSize = getFolderSize(oldImage)/(1024.0*1024.0);
			} 
			double size =0;
			for(String tempImageName:paraQuestionBean.getImages()){
				String imagePath = storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + inst_id + tempImageName;
				File source = new File(imagePath);
				size = size + source.length(); 
			}
			for(ParaQuestion paraQuestion:paraQuestionBean.getParaQuestion()){
				for(String paraQuestionImage:paraQuestion.getQueImage()){
					String imagePath = storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + inst_id + paraQuestionImage;
					File source = new File(imagePath);
					size = size + source.length(); 
				}
			}
			size = size /(1024 * 1024);
			if(stats.getAlloc_memory() < (stats.getUsed_memory() + size - oldSize)){
				return false;
			}
		}
		return true;
		}
	
	public long getFolderSize(File directory) {
	    long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += file.length();
	        else
	            length += getFolderSize(file);
	    }
	    return length;
	}
}
