package com.service.beans;

import com.classapp.db.question.Questionbank;
import com.datalayer.exam.ParagraphQuestion;

public class GenerateQuestionPaperResponse {
	String item_id;
	Questionbank questionbank;
	ParagraphQuestion paragraphQuestion;
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public Questionbank getQuestionbank() {
		return questionbank;
	}
	public void setQuestionbank(Questionbank questionbank) {
		this.questionbank = questionbank;
	}
	public ParagraphQuestion getParagraphQuestion() {
		return paragraphQuestion;
	}
	public void setParagraphQuestion(ParagraphQuestion paragraphQuestion) {
		this.paragraphQuestion = paragraphQuestion;
	}
	
}
