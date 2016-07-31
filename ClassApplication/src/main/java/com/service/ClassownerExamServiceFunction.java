package com.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.classapp.db.question.Questionbank;
import com.config.Constants;
import com.transaction.questionbank.QuestionBankTransaction;

public class ClassownerExamServiceFunction {
	String EXAM_IMAGE_FOLDER = "examImage";
	String SUBJECTIVE = "subjective";
	String OBJECTIVE = "objective";
	String PARAGRAPH = "paragraph";
	String PARAGRAPH_QUESTION = "paragraphQuestions";
	String QUESTION = "question";
	String OPTION = "option";
	
	
	public Questionbank getQuestion(int regId,int que_id,int inst_id,int sub_id,int div_id){
		QuestionBankTransaction questionBankTransaction = new QuestionBankTransaction();
		Questionbank questionbank = questionBankTransaction.getQuestion(que_id, inst_id, sub_id, div_id);
		
		List<String>primaryImage = getPrimaryImage(regId, que_id, questionbank.getQue_type(),div_id,sub_id);
		HashMap<Integer, List<String>> secImages = getSecondaryImage(regId, que_id, questionbank.getQue_type(),div_id,sub_id);
		questionbank.setPrimaryImage(primaryImage);
		questionbank.setSecondaryImage(secImages);
		return questionbank;
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
}
