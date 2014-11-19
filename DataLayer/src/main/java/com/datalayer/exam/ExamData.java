package com.datalayer.exam;

import java.util.List;

public class ExamData {
	private List<QuestionData> questionDatas;
	private int totalMarks;
	public List<QuestionData> getQuestionDatas() {
		return questionDatas;
	}
	public void setQuestionDatas(List<QuestionData> questionDatas) {
		this.questionDatas = questionDatas;
	}
	public int getTotalMarks() {
		return totalMarks;
	}
	public void setTotalMarks(int totalMarks) {
		this.totalMarks = totalMarks;
	}
	
	
}
