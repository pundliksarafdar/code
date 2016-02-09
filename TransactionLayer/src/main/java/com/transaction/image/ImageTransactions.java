package com.transaction.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
	public void copyLogoImage(String imageId){
		String tempImagePath = this.storageURL + File.separatorChar+ "imageTemp" + File.separatorChar + imageId;
		String logoImagePath = this.storageURL + File.separatorChar+ LOGO_FOLDER + File.separatorChar + imageId;
		File source = new File(tempImagePath);
	    File dest = new File(logoImagePath);
		try {
			Files.copy(source.toPath(), dest.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageDB imageDB = new ImageDB();
		Image image = new Image();
		image.setImage_id("200");
		image.setImage_name("Donashe");
		image.setImage_type("typo");
		imageDB.save(image);
	}
	
	public List<ImageListBean> getImageList(Constants.IMAGE_TYPE imageType){
		List<ImageListBean> imageListBeans = new ArrayList<ImageListBean>();
		for(int i = 0;i<6;i++){
			ImageListBean imageListBean = new ImageListBean();
			imageListBean.setImageid("imageid"+i);
			imageListBean.setImagename("imagename"+i+".jpg");
			imageListBean.setImagetype("logo");
			imageListBeans.add(imageListBean);
		}
		return imageListBeans;
	}
}
