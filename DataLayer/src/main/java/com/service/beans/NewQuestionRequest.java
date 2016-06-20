package com.service.beans;

import java.util.List;

public class NewQuestionRequest {
	QuestionPaperStructure questionPaperStructure;
	List<GenerateQuestionPaperServicebean> generateQuestionPaperServicebeanList;
	List<Integer> currentIds;
	public List<Integer> getCurrentIds() {
		return currentIds;
	}
	public void setCurrentIds(List<Integer> currentIds) {
		this.currentIds = currentIds;
	}
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
