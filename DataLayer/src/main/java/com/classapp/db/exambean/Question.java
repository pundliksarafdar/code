package com.classapp.db.exambean;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable{
	private String question;
	private List<String> image;
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public List<String> getImage() {
		return image;
	}
	public void setImage(List<String> image) {
		this.image = image;
	}
	
	
}
