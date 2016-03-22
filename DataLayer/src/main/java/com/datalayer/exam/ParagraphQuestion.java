package com.datalayer.exam;

import java.io.Serializable;

public class ParagraphQuestion implements Serializable {
	int questionnumber;
	String paraQuestions[];
	String paraPerQuestionMarks[];
	String paragraphText;
	int questionmarks;
	public int getQuestionnumber() {
		return questionnumber;
	}
	public void setQuestionnumber(int questionnumber) {
		this.questionnumber = questionnumber;
	}
	public String[] getParaQuestions() {
		return paraQuestions;
	}
	public void setParaQuestions(String[] paraQuestions) {
		this.paraQuestions = paraQuestions;
	}
	public String[] getParaPerQuestionMarks() {
		return paraPerQuestionMarks;
	}
	public void setParaPerQuestionMarks(String[] paraPerQuestionMarks) {
		this.paraPerQuestionMarks = paraPerQuestionMarks;
	}
	public String getParagraphText() {
		return paragraphText;
	}
	public void setParagraphText(String paragraphText) {
		this.paragraphText = paragraphText;
	}
	public int getQuestionmarks() {
		return questionmarks;
	}
	public void setQuestionmarks(int questionmarks) {
		this.questionmarks = questionmarks;
	}
	
	
}
