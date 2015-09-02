package com.datalayer.exam;

import java.util.List;

public class QuestionSearchRequest {
	private int marks;
	private int count;
	private int maximumRepeatation = -1;
	
	//Defaults to -1 can identify that exam search is not happend for this class
	private long availiblityCount = -1;
	private List<Integer> questionId;
	
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getMaximumRepeatation() {
		return maximumRepeatation;
	}
	public void setMaximumRepeatation(int maximumRepeatation) {
		this.maximumRepeatation = maximumRepeatation;
	}
	public long getAvailiblityCount() {
		return availiblityCount;
	}
	public void setAvailiblityCount(long availiblityCount) {
		this.availiblityCount = availiblityCount;
	}
	public List<Integer> getQuestionId() {
		return questionId;
	}
	public void setQuestionId(List<Integer> questionId) {
		this.questionId = questionId;
	}
	
	@Override
	public String toString() {
		
		return this.questionId.toString();
	}
}
