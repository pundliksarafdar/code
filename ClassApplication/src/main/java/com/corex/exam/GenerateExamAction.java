package com.corex.exam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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

import com.classapp.logger.AppLogger;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.QuestionData;
import com.datalayer.exam.QuestionSearchRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.transaction.exams.ExamTransaction;
import com.user.UserBean;

public class GenerateExamAction extends BaseAction{
	private Integer[] questioncount;
	private Integer[] marks;
	private Integer division,subject;
	private String batch;
	private Integer passingmarks;
	private Integer totalMarks;
	private String examname;
	private int examHour;
	private int examMinute;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {/*
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
		boolean valid = validateCriteria(this.examname,subject,division, userBean.getRegId(), questionSearchRequestList);
		if(valid){
			 exam = generateExam(subject,division, userBean.getRegId(), questionSearchRequestList);
			 String questionId = "";
			 String questionAnswerId = "";
			 //Extract questionids
			 List<String> questionIdListsList = new ArrayList<String>();
			 List<String> questionAnswerIdListsList = new ArrayList<String>();
			 String questionIdString = "";
			 String answerIdString = "";
			 
			 for (QuestionSearchRequest questionSearchRequest:exam) {
				List<Integer> questionList = questionSearchRequest.getQuestionId();
				List<String> answersId = examTransaction.getAnswers(subject, userBean.getRegId(), division, questionList);
				questionIdListsList.add(StringUtils.join(questionList,","));
				questionAnswerIdListsList.add(StringUtils.join(answersId,"/"));
			}
			 questionIdString = StringUtils.join(questionIdListsList,",");
			 answerIdString = StringUtils.join(questionAnswerIdListsList,"/");
			 AppLogger.logger(questionIdString+"<>"+answerIdString);
			 int instituteId = userBean.getRegId();
			 int creatorId = userBean.getRegId();
			 UserStatic userStatic = userBean.getUserStatic();
			 
			 List<QuestionData> questionDataList = new ArrayList<QuestionData>();
			 for(int index = 0;index<questionIdListsList.size();index++){
				 String questionIdListsListStr = questionIdListsList.get(index);
				 String[] questionIdListsListStrList = questionIdListsListStr.split(",");
				 
				 for(int indexListStr=0;indexListStr<questionIdListsListStrList.length;indexListStr++){
					 String questionPath = userStatic.getExamPath()+File.separator+subject+File.separator+division+File.separator+questionIdListsListStrList[indexListStr];
					 QuestionData questionData=(QuestionData) readObject(new File(questionPath));
					 QuestionData questionDataForMinified = new QuestionData();
					 //Miinified object is used to put the data in page as while object is not required- minimise server load
					 questionDataForMinified.setQuestion(questionData.getQuestion());
					 questionDataForMinified.setMarks(questionData.getMarks());
					 questionDataList.add(questionDataForMinified);
				 }
			 }
			 request.setAttribute("questionData", questionDataList);
			 JsonObject jsonObject = new JsonObject();
			 jsonObject.addProperty("examname", examname);
			 jsonObject.addProperty("instituteId",instituteId);
			 jsonObject.addProperty("subject",subject);
			 jsonObject.addProperty("division",division);
			 jsonObject.addProperty("totalMarks",totalMarks);
			 jsonObject.addProperty("passingmarks",passingmarks);
			 jsonObject.addProperty("creatorId",creatorId);
			 jsonObject.addProperty("batch",batch);
			 jsonObject.addProperty("questionIdString",questionIdString);
			 jsonObject.addProperty("answerIdString",answerIdString);
			 jsonObject.addProperty("examHour",examHour);
			 jsonObject.addProperty("examMinute",examMinute);
			 
			 request.getSession().setAttribute("examsaveobject", jsonObject);
			 //examTransaction.saveExam(examname, instituteId, subject, division,totalMarks , passingmarks, creatorId,batch , questionIdString, answerIdString,examHour,examMinute);
		}
		
		
		
	*/	return SUCCESS;
	}
	
	/*private Object readObject(File file) {
		Object object = null;
		FileInputStream fin = null;
		ObjectInputStream objectInputStream = null;
		try{
			fin = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(fin);
			object =  objectInputStream.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=objectInputStream)try {objectInputStream.close();} catch (IOException e) {e.printStackTrace();}
			if(null!=fin)try {fin.close();} catch (IOException e) {e.printStackTrace();}
		}
		return object;
	}
	
	public boolean validateCriteria(String examName,Integer sub_id,Integer div_id,int classId,List<QuestionSearchRequest> list){
		if(null==examName || examName.trim().length()==0){
			return false;
		}

		ExamTransaction examTransaction = new ExamTransaction();
		boolean isExamExist = examTransaction.isExamExists(classId,examName,"");
		if(isExamExist){
			return false;
		}
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

	public String getBatch() {
		return batch;
	}

	public void setBatch(String bacth) {
		this.batch = bacth;
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

	public Integer getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(Integer totalMarks) {
		this.totalMarks = totalMarks;
	}

	public int getExamHour() {
		return examHour;
	}

	public void setExamHour(int examHour) {
		this.examHour = examHour;
	}

	public int getExamMinute() {
		return examMinute;
	}

	public void setExamMinute(int examMinute) {
		this.examMinute = examMinute;
	}*/
	
		
}
