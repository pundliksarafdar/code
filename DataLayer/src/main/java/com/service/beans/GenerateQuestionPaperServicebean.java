package com.service.beans;

import java.util.List;

public class GenerateQuestionPaperServicebean {
	int subject_id;
	int topic_id;
	int marks;
	String question_type;
	List<Integer> question_ids;
	int count;
	int question_PickUpCounter;
	public int getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}
	public int getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(int topic_id) {
		this.topic_id = topic_id;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public String getQuestion_type() {
		return question_type;
	}
	public void setQuestion_type(String question_type) {
		this.question_type = question_type;
	}
	public List<Integer> getQuestion_ids() {
		return question_ids;
	}
	public void setQuestion_ids(List<Integer> question_ids) {
		this.question_ids = question_ids;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getQuestion_PickUpCounter() {
		return question_PickUpCounter;
	}
	public void setQuestion_PickUpCounter(int question_PickUpCounter) {
		this.question_PickUpCounter = question_PickUpCounter;
	}
	
	
}
