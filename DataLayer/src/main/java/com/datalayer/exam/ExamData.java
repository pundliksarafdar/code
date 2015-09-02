package com.datalayer.exam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExamData implements Serializable{
	private List<QuestionData> questionDatas;
	private int totalMarks;
	public List<QuestionData> getQuestionDatas() {
		if(null!=questionDatas)
			return questionDatas;
		else
			return new ArrayList<QuestionData>();
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
