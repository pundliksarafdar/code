package com.corex.exam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.Request;

import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.QuestionSearchRequest;
import com.transaction.exams.ExamTransaction;
import com.user.UserBean;

public class GenerateExamAction extends BaseAction{
	private Integer[] questioncount;
	private Integer[] marks;
	private Integer division,subject;
	private Integer[] bacth;
	private Integer passingmarks;
	private String examname;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		ExamTransaction examTransaction = new ExamTransaction();
		List<QuestionSearchRequest> questionSearchRequestList = new ArrayList();
		
		for(int index=0;index<questioncount.length;index++){
			QuestionSearchRequest questionSearchRequest = new QuestionSearchRequest();
			questionSearchRequest.setCount(questioncount[index]);
			questionSearchRequest.setMarks(marks[index]);
			questionSearchRequestList.add(questionSearchRequest);
		}
		
		List<QuestionSearchRequest> exam = null;
		List<String> answerId = new ArrayList<String>();
		boolean valid = validateCriteria(subject,division, userBean.getRegId(), questionSearchRequestList);
		if(valid){
			 exam = generateExam(subject,division, userBean.getRegId(), questionSearchRequestList);
			 String questionId = "";
			 String questionAnswerId = "";
			 //Extract questionids
			 List<List<Integer>> questionIdListsList = new ArrayList<List<Integer>>();
			 List<List<String>> questionAnswerIdListsList = new ArrayList<List<String>>();
			 for (QuestionSearchRequest questionSearchRequest:exam) {
				List<Integer> questionList = questionSearchRequest.getQuestionId();
				List<String> answersId = examTransaction.getAnswers(subject, userBean.getRegId(), division, questionList);
				questionIdListsList.add(questionList);
				questionAnswerIdListsList.add(answersId);
			}
			 
		}
		System.out.println(exam);
		//examTransaction.saveExam(examname, in, subId, divId, totalMarks, passMarks, creatorId, questionId, batchId, ansId)
		return SUCCESS;
	}
	
	public boolean validateCriteria(Integer sub_id,Integer div_id,int classId,List<QuestionSearchRequest> list){
		ExamTransaction examTransaction = new ExamTransaction();
		return examTransaction.validateSearchCriteria(sub_id, classId, div_id, list);
	}
	
	public List<QuestionSearchRequest> getQuestionId(Integer sub_id,Integer div_id,int classId,List<QuestionSearchRequest> questionSearchRequestList){
		ExamTransaction examTransaction = new ExamTransaction();
		return examTransaction.getCriteriaQuestion(sub_id, classId, div_id, questionSearchRequestList);

	}
	
	private List<QuestionSearchRequest> generateExam(Integer sub_id,Integer div_id,int classId,List<QuestionSearchRequest> questionSearchRequestList){
		questionSearchRequestList = getQuestionId(sub_id,div_id,classId,questionSearchRequestList);
		for (QuestionSearchRequest questionSearchRequest:questionSearchRequestList) {
			List<Integer> searchRequest = generateRandomQuestionId(questionSearchRequest.getQuestionId(), questionSearchRequest.getCount());
			questionSearchRequest.setQuestionId(searchRequest);
		}
		return questionSearchRequestList;
	}
	
	private List<Integer> generateRandomQuestionId(List<Integer> allQuestionId,int count){
		
		List<Integer> questionId = new ArrayList<Integer>();
		Set<Integer> questionIdSet = new HashSet<Integer>();
		
		Random random = new Random();
		while(questionIdSet.size()<count){
			int index = random.nextInt(allQuestionId.size());
			questionIdSet.add(allQuestionId.get(index));
		}
		questionId.addAll(questionIdSet);
		return questionId;
	}
	
	
	
	public Integer[] getQuestioncount() {
		return questioncount;
	}

	public void setQuestioncount(Integer[] questioncount) {
		this.questioncount = questioncount;
	}

	public Integer[] getMarks() {
		return marks;
	}

	public void setMarks(Integer[] marks) {
		this.marks = marks;
	}
	
	public Integer getDivision() {
		return division;
	}

	public void setDivision(Integer division) {
		this.division = division;
	}

	public Integer getSubject() {
		return subject;
	}

	public void setSubject(Integer subject) {
		this.subject = subject;
	}

	public Integer[] getBacth() {
		return bacth;
	}

	public void setBacth(Integer[] bacth) {
		this.bacth = bacth;
	}
	
	public Integer getPassingmarks() {
		return passingmarks;
	}

	public void setPassingmarks(Integer passingmarks) {
		this.passingmarks = passingmarks;
	}

	public String getExamname() {
		return examname;
	}

	public void setExamname(String examname) {
		this.examname = examname;
	}

	public static void main(String[] args) {
		GenerateExamAction action = new GenerateExamAction();
		/*
		List<Integer> allExamId = new ArrayList<Integer>();
		allExamId.add(1);
		allExamId.add(2);
		allExamId.add(3);
		allExamId.add(4);
		allExamId.add(5);
		allExamId.add(6);
		
		List<Integer> examIds = action.generateRandomExamId(allExamId, 3);
		System.out.println(examIds);
		*/
		
		List<QuestionSearchRequest> questionSearchRequests = new ArrayList<QuestionSearchRequest>();
		
		//5 Marks questions
		QuestionSearchRequest questionSearchRequest = new QuestionSearchRequest();
		questionSearchRequest.setMarks(5);
		
		List<Integer> allQuestionId = new ArrayList<Integer>();
		allQuestionId.add(1);
		allQuestionId.add(2);
		allQuestionId.add(3);
		allQuestionId.add(4);
		allQuestionId.add(5);
		allQuestionId.add(6);
		questionSearchRequest.setQuestionId(allQuestionId);
		
		questionSearchRequest.setAvailiblityCount(10);
		
		questionSearchRequest.setCount(5);
		
		questionSearchRequest.setMaximumRepeatation(2);
		
		questionSearchRequests.add(questionSearchRequest);
		
		//4 Marks
		questionSearchRequest = new QuestionSearchRequest();
		questionSearchRequest.setMarks(4);
		
		allQuestionId = new ArrayList<Integer>();
		allQuestionId.add(1*2);
		allQuestionId.add(2*2);
		allQuestionId.add(3*2);
		allQuestionId.add(4*2);
		allQuestionId.add(5*2);
		allQuestionId.add(6*2);
		questionSearchRequest.setQuestionId(allQuestionId);
		
		questionSearchRequest.setAvailiblityCount(10);
		
		questionSearchRequest.setCount(5);
		
		questionSearchRequest.setMaximumRepeatation(2);
		
		questionSearchRequests.add(questionSearchRequest);
		

		//3 Marks
		questionSearchRequest = new QuestionSearchRequest();
		questionSearchRequest.setMarks(3);
		
		allQuestionId = new ArrayList<Integer>();
		allQuestionId.add(1*3);
		allQuestionId.add(2*3);
		allQuestionId.add(3*3);
		allQuestionId.add(4*3);
		allQuestionId.add(5*3);
		allQuestionId.add(6*3);
		questionSearchRequest.setQuestionId(allQuestionId);
		
		questionSearchRequest.setAvailiblityCount(10);
		
		questionSearchRequest.setCount(3);
		
		questionSearchRequest.setMaximumRepeatation(2);
		
		questionSearchRequests.add(questionSearchRequest);

		/*
		try {
			List<QuestionSearchRequest> list = action.generateExam(questionSearchRequests);
			for(QuestionSearchRequest questionSearchRequest2 : list){
				System.out.println(questionSearchRequest2.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
}
