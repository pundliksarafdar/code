package com.service.beans;

import java.util.List;

public class EditQuestionPaper {
	QuestionPaperEditFileObject fileObject;
	List<GenerateQuestionPaperServicebean> generateQuestionPaperServicebeanList;
	public QuestionPaperEditFileObject getFileObject() {
		return fileObject;
	}
	public void setFileObject(QuestionPaperEditFileObject fileObject) {
		this.fileObject = fileObject;
	}
	public List<GenerateQuestionPaperServicebean> getGenerateQuestionPaperServicebeanList() {
		return generateQuestionPaperServicebeanList;
	}
	public void setGenerateQuestionPaperServicebeanList(
			List<GenerateQuestionPaperServicebean> generateQuestionPaperServicebeanList) {
		this.generateQuestionPaperServicebeanList = generateQuestionPaperServicebeanList;
	}
	
}
