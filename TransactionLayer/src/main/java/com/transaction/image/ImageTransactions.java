package com.transaction.image;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.image.Image;
import com.classapp.db.image.ImageDB;
import com.classapp.utils.Constants;
import com.service.beans.ImageListBean;
import com.service.beans.ParaQuestion;
import com.service.beans.ParaQuestionBean;

public class ImageTransactions {
	String storageURL;
	String logoImageUrl;
	String LOGO_FOLDER = "imageLogo";
	String EXAM_IMAGE_FOLDER = "examImage";
	String SUBJECTIVE = "subjective";
	String OBJECTIVE = "objective";
	String PARAGRAPH = "paragraph";
	String PARAGRAPH_QUESTION = "paragraph_questions";
	String QUESTION = "question";
	String OPTION = "option";
	
	public ImageTransactions(String baseStorageURL) {
		this.storageURL = baseStorageURL;
	}
	public void copyLogoImage(String imageId,String imageName,int regId){
		String tempImagePath = this.storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + imageId;
		String logoImagePath = this.storageURL + File.separatorChar + regId + File.separatorChar+ LOGO_FOLDER + File.separatorChar + imageId;
		File source = new File(tempImagePath);
	    File dest = new File(logoImagePath);
	    
	    if(!dest.getParentFile().exists()){
	    	dest.getParentFile().mkdirs();
	    }
		try {
			Files.copy(source.toPath(), dest.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageDB imageDB = new ImageDB();
		Image image = new Image();
		image.setInst_id(regId);
		image.setImage_id(imageId);
		image.setImage_name(imageName);
		//Constants.IMAGE_TYPE type = Constants.IMAGE_TYPE.LOGO;
		image.setImage_type(Constants.IMAGE_TYPE.L.toString());
		imageDB.save(image);
	}
	
	public void saveQuestionImage(List<String> tempImageList,int questionId,int regId){
		String subjectiveImageDest = this.storageURL + File.separatorChar + regId + File.separatorChar+ EXAM_IMAGE_FOLDER + File.separatorChar + SUBJECTIVE + File.separatorChar + questionId;
		List<String>savedImages = new ArrayList<String>();
		for(String tempImageName:tempImageList){
			boolean isExist = saveImage(tempImageName, subjectiveImageDest+ File.separatorChar+tempImageName,regId);
			if(isExist){
				savedImages.add(tempImageName);
			}
		}
		/*Removing images from list*/
		List<String> imageNameList = getImageListInImageFolder(subjectiveImageDest);
		imageNameList.removeAll(tempImageList);
		
		/*If image is not exist in the request then remove from the folder*/
		deleteImageListInImageFolder(subjectiveImageDest, imageNameList);
	}
	
	public void saveObjectiveQuestionImage(List<String> tempImageList,int questionId,int regId){
		String subjectiveImageDest = this.storageURL + File.separatorChar + regId + File.separatorChar+ EXAM_IMAGE_FOLDER + File.separatorChar + OBJECTIVE + File.separatorChar + questionId + File.separatorChar + QUESTION ;
		List<String>savedImages = new ArrayList<String>();
		for(String tempImageName:tempImageList){
			boolean isExist = saveImage(tempImageName, subjectiveImageDest+ File.separatorChar+tempImageName,regId);
			if(isExist){
				savedImages.add(tempImageName);
			}
		}
		/*Removing images from list*/
		List<String> imageNameList = getImageListInImageFolder(subjectiveImageDest);
		imageNameList.removeAll(tempImageList);
		
		/*If image is not exist in the request then remove from the folder*/
		deleteImageListInImageFolder(subjectiveImageDest, imageNameList);
	}
	
	public void saveOptionImage(HashMap<Integer, List<String>> optionImages,int questionId,int regId){
		String objectiveImageDest = this.storageURL + File.separatorChar + regId + File.separatorChar+ EXAM_IMAGE_FOLDER + File.separatorChar + OBJECTIVE + File.separatorChar + questionId + File.separatorChar + OPTION +File.separatorChar;
		
		for (Entry<Integer, List<String>> entry : optionImages.entrySet()) {
			String optionIdPath = objectiveImageDest + entry.getKey() ;
			
			List<String> tempImageList = entry.getValue(); 
			for(String tempImageName:tempImageList){
				String imageDest = optionIdPath + File.separatorChar + tempImageName;
				saveImage(tempImageName, imageDest,regId);
			}
			/*Removing images from list*/
			List<String> imageNameList = getImageListInImageFolder(optionIdPath);
			imageNameList.removeAll(tempImageList);
			
			/*If image is not exist in the request then remove from the folder*/
			deleteImageListInImageFolder(optionIdPath, imageNameList);
		}		
	}
	
	public void renameFolders(List<Integer>prevImage,List<Integer>currentImage,int questionId,int regId){
		String objectiveImageDest = this.storageURL + File.separatorChar + regId + File.separatorChar+ EXAM_IMAGE_FOLDER + File.separatorChar + OBJECTIVE + File.separatorChar + questionId + File.separatorChar + OPTION +File.separatorChar;
		System.out.println("Renaming image.....");
		System.out.println(prevImage);
		System.out.println(currentImage);
		
		int imageFolderLength = prevImage.size();
		for(int index=0;index<imageFolderLength;index++){
			File prevOptionImageFolder = new File(objectiveImageDest+prevImage.get(index));
			File currentOptionImageFolder = new File(objectiveImageDest+currentImage.get(index));
			prevOptionImageFolder.renameTo(currentOptionImageFolder);
		}
	}
	
	public void saveParagraphImage(ParaQuestionBean paraQuestionBean,int questionId,int regId){
		//change folder option
		String paraImageDest = this.storageURL + File.separatorChar + regId + File.separatorChar+ EXAM_IMAGE_FOLDER + File.separatorChar + PARAGRAPH + File.separatorChar + questionId + File.separatorChar + PARAGRAPH +File.separatorChar;
		for(String tempImageName:paraQuestionBean.getImages()){
			saveImage(tempImageName, paraImageDest+tempImageName,regId);
		}
		
		String paraQuestionImageDest = this.storageURL + File.separatorChar + regId + File.separatorChar+ EXAM_IMAGE_FOLDER + File.separatorChar + PARAGRAPH + File.separatorChar + questionId + File.separatorChar + PARAGRAPH_QUESTION +File.separatorChar;
		int index = 0;
		for(ParaQuestion paraQuestion:paraQuestionBean.getParaQuestion()){
			String imageOptions = paraQuestionImageDest+ index + File.separatorChar; 
			index++;
			for(String paraQuestionImage:paraQuestion.getQueImage()){
				saveImage(paraQuestionImage, imageOptions+paraQuestionImage,regId);
			}
		}
	}
	
	public List<ImageListBean> getImageList(int instId,Constants.IMAGE_TYPE imageType){
		ImageDB imageDB = new ImageDB();
		List<ImageListBean> imageListBeans = new ArrayList<ImageListBean>();
		List<Image> imageList = imageDB.getImagesByType(instId, imageType.toString());
		for (Image image : imageList) {
			ImageListBean imageListBean = new ImageListBean();
			try {
				BeanUtils.copyProperties(imageListBean, image);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			imageListBeans.add(imageListBean);
		}
		return imageListBeans;
	}
	
	/*
	 if image is present in tempdirectory then it will return true else false
	 */
	private boolean saveImage(String imageFileId,String imagePath,int regId){
		String tempImagePath = this.storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + regId + imageFileId;
		
		File source = new File(tempImagePath);
	    File dest = new File(imagePath);
	    
		if(!dest.getParentFile().exists()){
	    	dest.getParentFile().mkdirs();
	    }
		
		if(source.exists()){
		try {
			Files.copy(source.toPath(), dest.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return true;
		}else{
			return false;
		}
	}
	
	private void deleteImageListInImageFolder(String destPath,List<String>imageNames){
		List<String> imageList = new ArrayList<String>();
		File file = new File(destPath);
		if(file.exists()){
			File[] files = file.listFiles();
			for (File imageFile:files) {
				if(imageNames.contains(imageFile.getName())){
					imageFile.delete();
				}
			}
		}
	}
	
	private List<String> getImageListInImageFolder(String destPath){
		List<String> imageList = new ArrayList<String>();
		File file = new File(destPath);
		if(file.exists()){
			File[] files = file.listFiles();
			for (File imageFile:files) {
				imageList.add(imageFile.getName());
			}
		}
		return imageList;
	}
}
