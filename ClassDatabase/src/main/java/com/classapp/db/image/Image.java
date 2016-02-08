package com.classapp.db.image;

public class Image {
	String imageid;
	
	/*
	 * Image type will be logo image, examimage,watermar image etc 
	 */
	String imagetype;
	String imagename;
	public String getImageid() {
		return imageid;
	}
	public void setImageid(String imageid) {
		this.imageid = imageid;
	}
	public String getImagetype() {
		return imagetype;
	}
	public void setImagetype(String imagetype) {
		this.imagetype = imagetype;
	}
	public String getImagename() {
		return imagename;
	}
	public void setImagename(String imagename) {
		this.imagename = imagename;
	}
	
	
}
