package com.service.beans;

import java.util.List;

public class NewQuestionRequest {
	QuestionPaperStructure questionPaperStructure;
	List<GenerateQuestionPaperServicebean> generateQuestionPaperServicebeanList;
	public QuestionPaperStructure getQuestionPaperStructure() {
		return questionPaperStructure;
	}
	public void setQuestionPaperStructure(
			QuestionPaperStructure questionPaperStructure) {
		this.questionPaperStructure = questionPaperStructure;
	}
	public List<GenerateQuestionPaperServicebean> getGenerateQuestionPaperServicebeanList() {
		return generateQuestionPaperServicebeanList;
	}
	public void setGenerateQuestionPaperServicebeanList(
			List<GenerateQuestionPaperServicebean> generateQuestionPaperServicebeanList) {
		this.generateQuestionPaperServicebeanList = generateQuestionPaperServicebeanList;
	}
	
}
