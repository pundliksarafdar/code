
package com.helper;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.exams.MCQDetails;
import com.transaction.exams.ExamTransaction;


public class MCQDataHelper {
	private int class_id;
	private List<MCQDetails> listOfMCQs= new  ArrayList<MCQDetails>();
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	/*
	public List<MCQDetails> getListOfMCQs() {

		ExamTransaction examTransaction=new ExamTransaction(class_id);
		return examTransaction.getAllExamPapersDetailsFromID();
	}*/
}
