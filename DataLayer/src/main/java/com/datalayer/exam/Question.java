package com.datalayer.exam;

import java.util.HashMap;

public interface Question {
	public String getQuestion() ;
	public void setQuestion(String question) ;
	
	public double getMarks();
	public void setMarks(double marks);
	
	public String getCorrectAnswer();
	public void setCorrectAnswer(String correctAnswer);
	public HashMap<String, String> getOptions();
	public void setOptions(HashMap<String, String> options);
	public void setQuestionNumber(int questionNumber);
	public int getQuestionNumber();
}
