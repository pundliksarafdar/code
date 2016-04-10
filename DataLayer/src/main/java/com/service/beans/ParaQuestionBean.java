package com.service.beans;

import java.io.Serializable;
import java.util.List;

public class ParaQuestionBean implements Serializable{
	int classId;
	int instId;
	int subjectId;
	int topicId;
	String questionType;
	String paragraph;
	int marks;
	List<String>images;
	List<ParaQuestion>paraQuestion;
	

	public int getInstId() {
		return instId;
	}
	public void setInstId(int instId) {
		this.instId = instId;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
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
	public String getParagraph() {
		return paragraph;
	}
	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
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
	public List<ParaQuestion> getParaQuestion() {
		return paraQuestion;
	}
	public void setParaQuestion(List<ParaQuestion> paraQuestion) {
		this.paraQuestion = paraQuestion;
	}
	
	
}
