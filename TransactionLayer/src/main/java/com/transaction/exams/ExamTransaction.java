package com.transaction.exams;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.exam.CompExam;
import com.classapp.db.exam.Exam;
import com.classapp.db.exam.ExamDB;
import com.classapp.db.exams.MCQData;
import com.classapp.db.exams.MCQDataDB;
import com.classapp.db.question.QuestionbankDB;
import com.datalayer.exam.QuestionSearchRequest;
import com.datalayer.exambean.ExamData;


public class ExamTransaction {
	
	public void saveExam(ExamData examData) {
		MCQDataDB mcqDataDB = new MCQDataDB();
		MCQData mcqData = new MCQData();
		try {
			BeanUtils.copyProperties(mcqData, examData);
			mcqDataDB.updateDb(mcqData);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public List<com.datalayer.exam.MCQData> searchExamData(int classId,Date startDate,Date endDate,Integer divId,Boolean publish,Integer subjectId,Integer teacherId,Integer examId,String batch){
		MCQDataDB mcqDataDB = new MCQDataDB();
		 //mcqData = new com.datalayer.exam.MCQData();
		List<com.datalayer.exam.MCQData> mcqData = new ArrayList<com.datalayer.exam.MCQData>();
		List<MCQData> examData = mcqDataDB.searchExam(classId, startDate, endDate, divId, publish, subjectId, teacherId, examId,batch);
		for (MCQData data:examData) {
			com.datalayer.exam.MCQData mcqDataDest = new com.datalayer.exam.MCQData();
			try {
				BeanUtils.copyProperties(mcqDataDest, data);
				mcqData.add(mcqDataDest);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mcqData;
	}
	
	public List<CompExam> getAllCompExamList() {
		ExamDB db=new ExamDB();
		QuestionbankDB db2=new QuestionbankDB();
		return db.getAllCompExam();
	}
	
	public List<Exam> getExam(int inst_id,int sub_id,int div_id,String exam_status,int currentPage) {
		ExamDB db=new ExamDB();
	  return db.getExamList(inst_id, sub_id, div_id, exam_status,currentPage);
		
	}
	
	public Exam getExamToAttempt(int inst_id,int sub_id,int div_id,int examID) {
		ExamDB db=new ExamDB();
	  return db.getExam(examID, inst_id, sub_id, div_id);
		
	}
	
	public int getExamCount(int inst_id,int sub_id,int div_id,String exam_status) {
		ExamDB db=new ExamDB();
		return db.getExamListCount(inst_id, sub_id, div_id, exam_status);		
	}
	
	public boolean publishExam(int exam_id,int inst_id,int sub_id,int div_id,Timestamp start_time,Timestamp end_time) {
		ExamDB examDB=new ExamDB();
		examDB.enableExam(exam_id, inst_id, sub_id, div_id, start_time, end_time);
	return true;
	}
	
	public boolean disableExam(int exam_id,int inst_id,int sub_id,int div_id) {
		ExamDB examDB=new ExamDB();
		examDB.disableExam(exam_id, inst_id, sub_id, div_id);
	return true;
	}
	
	public List<QuestionSearchRequest> getCriteriaQuestionCount(int sub_id,int inst_id,int div_id,List<QuestionSearchRequest> list){
		QuestionbankDB questionbankDB = new QuestionbankDB();
		return questionbankDB.getCriteriaQuestionCount(sub_id, inst_id, div_id, list);
	}
	
	public List<QuestionSearchRequest> getCriteriaQuestion(int sub_id,int inst_id,int div_id,List<QuestionSearchRequest> list){
		QuestionbankDB questionbankDB = new QuestionbankDB();
		return questionbankDB.getCriteriaQuestion(sub_id, inst_id, div_id, list);
	}
	
	public boolean validateSearchCriteria(int sub_id,int inst_id,int div_id,List<QuestionSearchRequest> list){
		boolean result = true;
		List<QuestionSearchRequest> listQuestionSearchCount = getCriteriaQuestionCount(sub_id, inst_id, div_id, list);
		for(QuestionSearchRequest questionSearchRequest:listQuestionSearchCount){
			if(questionSearchRequest.getAvailiblityCount()<questionSearchRequest.getCount()){
				result = false;
			}
		}
		return result;
	}
	
	
	/**
	 * @param questionId - coma seaparatted
	 * @param batchId - coma seaparatted
	 * @param ansId - coma seaparatted
	 */
	public void saveExam(String examName,int instituteId,int subId,int divId,int totalMarks,int passMarks,int creatorId,List<Integer> questionId,List<Integer> batchId,List<Integer> ansId){
		Exam exam = new Exam();
		ExamDB examDB = new ExamDB();
		int examId = examDB.getNextExamID(instituteId, divId, subId);
		exam.setExam_id(examId);
		exam.setExam_name(examName);
		exam.setInstitute_id(instituteId);
		exam.setDiv_id(divId);
		exam.setSub_id(subId);
		exam.setTotal_marks(totalMarks);
		exam.setPass_marks(passMarks);
		exam.setCreated_by(creatorId);
		
		examDB.saveExam(exam);
	}
	
	public List<String> getAnswers(int sub_id,int inst_id,int div_id,List<Integer> que_ids){
		QuestionbankDB questionbankDB = new QuestionbankDB();
		return questionbankDB.getQuestionAnsIds(sub_id, inst_id, div_id, que_ids);
	}
	public static void main(String[] args) {
		/*
		ExamData examData = new ExamData();
		examData.setClass_id(3);
		examData.setCreate_date(new Date());
		examData.setDiv_id(28);
		examData.setPublish(false);
		examData.setSubject_id(22);
		examData.setTeacher_id(10);
		examData.setUpload_path("");
		ExamTransaction examTransaction = new ExamTransaction();
		examTransaction.saveExam(examData);
		*/
		String batch  = "2";
		String[] str = batch.split(",");
		System.out.println();
		ExamTransaction examTransaction = new ExamTransaction();
		List list = examTransaction.searchExamData(3, null, null, null, null, null, null, null,"%2%");
		System.out.println(list);
	}
}
