package com.datalayer.exam;

import java.util.List;

public class QuestionData {
	private Double questionNumber;
	private String question;
	private List<String> answers;
	private List<String> options;
	private int marks;
	public Double getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(Double questionNumber) {
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

	
}
