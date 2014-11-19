package com.classapp.db.exambean;

import java.io.Serializable;
import java.util.List;

public class Description implements Serializable{
	private String description;
	private List<String> images;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	
}
