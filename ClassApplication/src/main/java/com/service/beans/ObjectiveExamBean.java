package com.service.beans;

import java.util.List;

public class ObjectiveExamBean {
	String classId;
	int subjectId;
	int topicId;
	String questionType;
	String question;
	int marks;
	int questionId;
	List<String> images;
	List<ObjectiveOptions> options;
	
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getQuestion() {
		return question;
	}
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public List<ObjectiveOptions> getOptions() {
		return options;
	}
	public void setOptions(List<ObjectiveOptions> options) {
		this.options = options;
	}
	
	
}
