package com.transaction.image;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.image.Image;
import com.classapp.db.image.ImageDB;
import com.classapp.utils.Constants;
import com.service.beans.ImageListBean;

public class ImageTransactions {
	String storageURL;
	String logoImageUrl;
	String LOGO_FOLDER = "imageLogo";
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
}
