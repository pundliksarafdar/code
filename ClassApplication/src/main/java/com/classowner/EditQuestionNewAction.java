package com.classowner;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.user.UserBean;

public class EditQuestionNewAction extends BaseAction{
	int division;
	int subject;
	int topicId;
	int questiontype;
	int questionNumber;
	
	@Override
	public String performBaseAction(UserBean userBean, HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		String questionType;
		switch (questiontype) { 
		case 1:questionType = "subjective";
			break;
		case 2:questionType = "objective";
			break;
		case 3:questionType = "paragraph";
			break;
		default:questionType = "none";
			break;
		}
		return questionType;
	}
	public int getDivision() {
		return division;
	}
	public void setDivision(int division) {
		this.division = division;
	}
	public int getSubject() {
		return subject;
	}
	public void setSubject(int subject) {
		this.subject = subject;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public int getQuestiontype() {
		return questiontype;
	}
	public void setQuestiontype(int questiontype) {
		this.questiontype = questiontype;
	}
	public int getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	
}
