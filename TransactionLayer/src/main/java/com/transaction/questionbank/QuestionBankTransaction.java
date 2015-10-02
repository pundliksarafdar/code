package com.transaction.questionbank;

import java.util.List;

import com.classapp.db.question.Questionbank;
import com.classapp.db.question.QuestionbankDB;

public class QuestionBankTransaction {

	public int getNextQuestionID(int inst_id,int sub_id,int div_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getNextQuestionID(inst_id, div_id, sub_id);
	}
	
	public boolean saveQuestion(Questionbank questionbank) {
		QuestionbankDB db=new QuestionbankDB();
		db.saveQuestion(questionbank);
		return true;
	}
	
	public List<Integer> getDistinctQuestionMarks(int sub_id,int div_id,int inst_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getdistinctQuestionMarks(sub_id, inst_id, div_id);
	}
	
	public List<Integer> getDistinctQuestionRep(int sub_id,int div_id,int inst_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getdistinctQuestionRep(sub_id, inst_id, div_id);
	}
	
	public List<Questionbank> getSearchedQuestions(int rep,String compexam_id,int marks,int sub_id,int inst_id,int div_id,int currentPage,int topic_id) {
		QuestionbankDB db=new QuestionbankDB();
		return db.getSearchedQuestion(rep, compexam_id, marks, sub_id, inst_id, div_id,currentPage,topic_id);
	}
	
	public int getTotalSearchedQuestionCount(int rep,String compexam_id,int marks,int sub_id,int inst_id,int div_id,int topic_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getSearchedQuestionCount(rep, compexam_id, marks, sub_id, inst_id, div_id,topic_id);
	}
	
	public boolean deleteQuestion(int que_id,int inst_id,int sub_id,int div_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		questionbankDB.deleteQuestion(que_id, inst_id, sub_id, div_id);
		return true;
	}
	
	public Questionbank getQuestion(int que_id,int inst_id,int sub_id,int div_id) {
	QuestionbankDB questionbankDB=new QuestionbankDB();
	return questionbankDB.getQuestion(que_id, inst_id, sub_id, div_id);
	}
	
	public List<Integer> getQuestionMarks(int inst_id,int sub_id,int div_id,List<Integer> que_id) {
		QuestionbankDB db=new QuestionbankDB();
		return db.getQuestionMarks(inst_id, sub_id, div_id, que_id);
	}
	public List<Integer> getQuestionrelatedtoTopics(int sub_id,int inst_id,int div_id,int topic_id){
		QuestionbankDB db=new QuestionbankDB();
		return db.getQuestionrelatedtoTopics(sub_id, inst_id, div_id, topic_id);
	}
	
	public boolean ExamQuestionStatus(List<Integer> que_id,int inst_id,int sub_id,int div_id) {
		QuestionbankDB db=new QuestionbankDB();
		return db.ExamQuestionStatus(que_id, inst_id, sub_id, div_id);
	}
		
		public boolean deleteQuestionList(List<Integer> que_id,int inst_id,int sub_id,int div_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.deleteQuestionList(que_id, inst_id, sub_id, div_id);
	}
}
