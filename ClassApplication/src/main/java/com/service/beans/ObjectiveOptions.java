package com.service.beans;

import java.util.List;

public class ObjectiveOptions {
	String optionText;
	boolean optionCorrect;
	int optionId;
	List<String> optionImage;
	
	public String getOptionText() {
		return optionText;
	}
	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}
	public boolean isOptionCorrect() {
		return optionCorrect;
	}
	public void setOptionCorrect(boolean optionCorrect) {
		this.optionCorrect = optionCorrect;
	}
	public int getOptionId() {
		return optionId;
	}
	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}
	public List<String> getOptionImage() {
		return optionImage;
	}
	public void setOptionImage(List<String> optionImage) {
		this.optionImage = optionImage;
	}
	
	
}
