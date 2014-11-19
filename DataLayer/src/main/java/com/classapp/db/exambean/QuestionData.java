package com.classapp.db.exambean;

import java.io.Serializable;
import java.util.List;

public class QuestionData implements Serializable{
	private Question question;
	private boolean solved;
	private List<Option> options;
	private Description description;
	
	private int marks; 
	private boolean multipleChoice;
	
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public List<Option> getOptions() {
		return options;
	}
	public void setOptions(List<Option> options) {
		this.options = options;
	}
	public Description getDescription() {
		return description;
	}
	public void setDescription(Description description) {
		this.description = description;
	}
	public boolean isMultipleChoice() {
		return multipleChoice;
	}
	public void setMultipleChoice(boolean multipleChoice) {
		this.multipleChoice = multipleChoice;
	}
	public boolean isSolved() {
		return solved;
	}
	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
	
	
}
