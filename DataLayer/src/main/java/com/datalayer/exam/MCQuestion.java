package com.datalayer.exam;

import java.util.HashMap;
/**
 * This is a bean class which represnt a stand question with correct option and max 10 options
 * @author amit_meshram
 *
 */
public class MCQuestion implements Question{
	
	String correctAnswer;// This contains correct option numbers. If question have multiple correct answers, then this should be ', separated string'
	HashMap<String, String> options;// This is number of available options. only 10 options are allowed for a question.
	double marks;
	String question;
	int questionNumber;
	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	public MCQuestion(String correctAns) {
		
		this.correctAnswer=correctAns;
		this.marks=0.0;
		this.options=new HashMap<String, String>();
	}
	
	public MCQuestion(int questionNumber, String correctAns, String question, double marks) {
		this.correctAnswer=correctAns;
		this.marks=marks;
		this.question=question;
		this.options=new HashMap<String, String>();
		this.questionNumber=questionNumber;
	}
	
	public MCQuestion(String correctAns, String question, HashMap<String, String> optionMap) {
		
		this.correctAnswer=correctAns;
		this.marks=0.0;
		this.options=optionMap;
		this.question=question;
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
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	public HashMap<String, String> getOptions() {
		return options;
	}
	public void setOptions(HashMap<String, String> options) {
		this.options = options;
	}
	@Override
	public String toString() {
		return "MCQuestion [questionNumber="+questionNumber+",question=" + question  +", correctAnswer=" + correctAnswer + ", options=" + options
				+", marks=" + marks + "]";
	}	
}
