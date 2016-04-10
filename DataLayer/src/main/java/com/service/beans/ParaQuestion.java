package com.service.beans;

import java.io.Serializable;
import java.util.List;

public class ParaQuestion  implements Serializable{
	String questionText;
	String questionMarks;
	List<String> queImage;
	
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String getQuestionMarks() {
		return questionMarks;
	}
	public void setQuestionMarks(String questionMarks) {
		this.questionMarks = questionMarks;
	}
	public List<String> getQueImage() {
		return queImage;
	}
	public void setQueImage(List<String> queImage) {
		this.queImage = queImage;
	}
	
	
}
