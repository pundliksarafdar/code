package com.classapp.db.exambean;

import java.io.Serializable;
import java.util.List;

public class Option implements Serializable{
	List<String> imageFiles;
	String options;
	boolean selected;
	boolean answer;
	
	public List<String> getImageFiles() {
		return imageFiles;
	}

	public void setImageFiles(List<String> imageFiles) {
		this.imageFiles = imageFiles;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isAnswer() {
		return answer;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}

	
		
}
