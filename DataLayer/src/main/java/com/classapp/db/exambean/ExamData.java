package com.classapp.db.exambean;

import java.io.Serializable;
import java.util.List;

public class ExamData implements Serializable {
	List<QuestionData> questionDatas;
	private int timeToSolve;
	private int totalMarks;

	public List<QuestionData> getQuestionDatas() {
		return questionDatas;
	}

	public void setQuestionDatas(List<QuestionData> questionDatas) {
		this.questionDatas = questionDatas;
	}

	public int getTimeToSolve() {
		return timeToSolve;
	}

	public void setTimeToSolve(int timeToSolve) {
		this.timeToSolve = timeToSolve;
	}

	public int getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(int totalMarks) {
		this.totalMarks = totalMarks;
	}
	
	

}
