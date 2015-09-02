package com.corex.exam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.datalayer.exam.QuestionSearchRequest;
import com.user.UserBean;

public class GenerateExamAction extends BaseAction{

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		
		return SUCCESS;
	}
	
	public List<QuestionSearchRequest> getAvailibility(List<QuestionSearchRequest> questionSearchRequestList){
		return questionSearchRequestList;
	}
	
	private List<QuestionSearchRequest> generateExam(List<QuestionSearchRequest> questionSearchRequestList) throws Exception{
		
		questionSearchRequestList = getAvailibility(questionSearchRequestList);
		for (QuestionSearchRequest questionSearchRequest:questionSearchRequestList) {
			if(questionSearchRequest.getAvailiblityCount()<0 || questionSearchRequest.getCount()>questionSearchRequest.getAvailiblityCount()){
				throw new Exception("Please check availibility first"+this.getClass()); 
			}
			
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

		try {
			List<QuestionSearchRequest> list = action.generateExam(questionSearchRequests);
			for(QuestionSearchRequest questionSearchRequest2 : list){
				System.out.println(questionSearchRequest2.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
