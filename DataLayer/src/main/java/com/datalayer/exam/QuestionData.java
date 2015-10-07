package com.datalayer.exam;

import java.io.Serializable;
import java.util.List;

public class QuestionData implements Serializable{
	private int questionNumber;
	private String question;
	private List<String> answers;
	private List<String> options;
	private List<String> questionImage;
	private List<String> answerImage;
	private int marks;
	private int optionImageCount[];
	
	//private transient int createdby;
	public int getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public List<String> getAnswers() {
		return answers;
	}
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	/*public int getCreatedby() {
		return createdby;
	}
	public void setCreatedby(int createdby) {
		this.createdby = createdby;
	}*/
	public List<String> getQuestionImage() {
		return questionImage;
	}
	public void setQuestionImage(List<String> questionImage) {
		this.questionImage = questionImage;
	}
	public List<String> getAnswerImage() {
		return answerImage;
	}
	public void setAnswerImage(List<String> answerImage) {
		this.answerImage = answerImage;
	}
	public int[] getOptionImageCount() {
		return optionImageCount;
	}
	public void setOptionImageCount(int[] optionImageCount) {
		this.optionImageCount = optionImageCount;
	}
}
