package com.service.beans;

import java.util.List;

import com.classapp.db.question.Questionbank;
import com.datalayer.exam.ParagraphQuestion;

public class GenerateQuestionPaperResponse {
	List<QuestionPaperData> questionPaperDataList;
	List<GenerateQuestionPaperServicebean> questionPaperServicebeanList;
	public List<QuestionPaperData> getQuestionPaperDataList() {
		return questionPaperDataList;
	}
	public void setQuestionPaperDataList(
			List<QuestionPaperData> questionPaperDataList) {
		this.questionPaperDataList = questionPaperDataList;
	}
	public List<GenerateQuestionPaperServicebean> getQuestionPaperServicebeanList() {
		return questionPaperServicebeanList;
	}
	public void setQuestionPaperServicebeanList(
			List<GenerateQuestionPaperServicebean> questionPaperServicebeanList) {
		this.questionPaperServicebeanList = questionPaperServicebeanList;
	}
	
	
}
