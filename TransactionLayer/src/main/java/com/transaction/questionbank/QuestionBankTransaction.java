package com.transaction.questionbank;

import java.util.List;

import com.classapp.db.question.Questionbank;
import com.classapp.db.question.QuestionbankDB;

public class QuestionBankTransaction {

	public int getNextQuestionID(int inst_id,int sub_id,int div_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getNextQuestionID(inst_id, div_id, sub_id);
	}
	
	public int saveQuestion(Questionbank questionbank) {
		QuestionbankDB db=new QuestionbankDB();
		return db.saveQuestion(questionbank);
		
	}
	
	public List<Integer> getDistinctQuestionMarks(int sub_id,int div_id,int inst_id,String ques_type) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getdistinctQuestionMarks(sub_id, inst_id, div_id,ques_type);
	}
	
	public List<Integer> getDistinctQuestionRep(int sub_id,int div_id,int inst_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getdistinctQuestionRep(sub_id, inst_id, div_id);
	}
	
	public List<Questionbank> getSearchedQuestions(int marks,int sub_id,int inst_id,int div_id,int currentPage,int topic_id,String quesType) {
		QuestionbankDB db=new QuestionbankDB();
		return db.getSearchedQuestion( marks, sub_id, inst_id, div_id,currentPage,topic_id,quesType);
	}
	
	public int getTotalSearchedQuestionCount(int marks,int sub_id,int inst_id,int div_id,int topic_id,String quesType) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		return questionbankDB.getSearchedQuestionCount(marks, sub_id, inst_id, div_id,topic_id,quesType);
	}
	
	public boolean deleteQuestion(int que_id,int inst_id,int sub_id,int div_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		questionbankDB.deleteQuestion(que_id, inst_id, sub_id, div_id);
		return true;
	}
	
	public boolean updateDeleteQuestionStatus(int que_id,int inst_id,int sub_id,int div_id) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		questionbankDB.updateDeleteQuestionStatus(que_id, inst_id, sub_id, div_id);
		return true;
	}
	
	public boolean updateSubjectiveQuestion(int que_id,int inst_id,int sub_id,int div_id, String que_text, int marks) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		questionbankDB.updateSubjectiveQuestion(que_id, inst_id, sub_id, div_id, que_text, marks);
		return true;
	}
	
	public boolean updateObjectiveQuestion(Questionbank questionbank) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		questionbankDB.updateObjectiveQuestion(questionbank);
		return true;
	}
	
	public boolean updateParagraphQuestion(int que_id,int inst_id,int sub_id,int div_id, int marks) {
		QuestionbankDB questionbankDB=new QuestionbankDB();
		questionbankDB.updateParagraphQuestion(que_id, inst_id, sub_id, div_id, marks);
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
		
		public List<Integer> getDisabledQuestions(List<Integer> que_id,int inst_id,int sub_id,int div_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.getDisabledQuestions(que_id, inst_id, sub_id, div_id);
	}
		public List<Integer> getQuestionrelatedtoSubject(int inst_id,int sub_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.getQuestionrelatedtoSubject(sub_id, inst_id);
	}
		
		public List<Integer> getQuestionrelatedtoClass(int inst_id,int div_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.getQuestionrelatedtoClass(inst_id, div_id);
	}
		
		public boolean deleteQuestionrelatedtoClass(int inst_id,int div_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.deleteQuestionrelatedtoClass(inst_id, div_id);
	}
		
		public boolean deleteQuestionrelatedtoSubject(int inst_id,int sub_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.deleteQuestionrelatedtosubject(sub_id, inst_id);
	}
		
		public boolean updateQuestionrelatedtoTopic(int inst_id,int sub_id,int div_id,int topic_id) {
			QuestionbankDB db=new QuestionbankDB();
			return db.updateQuestionrelatedtotopic(inst_id, sub_id, div_id, topic_id);
	}
		
}
