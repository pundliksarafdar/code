package com.datalayer.exam;

import java.util.HashMap;

/**
 * This is a bean class which represnt a stand question with correct option and max 10 options
 * @author amit_meshram
 *
 */
public class SubjectiveQuestion implements Question{
	double marks;
	String question;
	int questionNumber;
	
	
	public SubjectiveQuestion(String question) {
		this.marks=0.0;		
		this.question=question;
	}
	
	public SubjectiveQuestion(int questionNumber,String question, double marks) {
		
		this.marks=marks;	
		this.question=question;	
		this.questionNumber=questionNumber;
	}	
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public double getMarks() {
		return marks;
	}
	public void setMarks(double marks) {
		this.marks = marks;
	}
	
	public String getCorrectAnswer() {
		throw new UnsupportedOperationException();
	}
	public void setCorrectAnswer(String correctAnswer) {
		throw new UnsupportedOperationException();
	}
	
	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
	
	@Override
	public String toString() {
		return "SubjectiveQuestion [questionNumber="+questionNumber+", question=" + question +", marks=" + marks +  "]";
	}

	@Override
	public HashMap<String, String> getOptions() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setOptions(HashMap<String, String> options) {
		throw new UnsupportedOperationException();		
	}
	
}
