package com.corex.exam;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.google.gson.JsonObject;
import com.transaction.exams.ExamTransaction;
import com.user.UserBean;

public class GenerateExamSaveAction extends BaseAction{

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		JsonObject jsonObject;
		jsonObject = (JsonObject) request.getSession().getAttribute("examsaveobject");
		ExamTransaction examTransaction = new ExamTransaction(); 
		String examname = jsonObject.get("examname").getAsString();
		int instituteId = jsonObject.get("instituteId").getAsInt();
		int subject = jsonObject.get("subject").getAsInt();
		int division = jsonObject.get("division").getAsInt();
		int totalMarks = jsonObject.get("totalMarks").getAsInt();
		int passingmarks = jsonObject.get("passingmarks").getAsInt();
		int creatorId = jsonObject.get("creatorId").getAsInt();
		String batch = jsonObject.get("batch").getAsString();
		String questionIdString = jsonObject.get("questionIdString").getAsString();
		String answerIdString = jsonObject.get("answerIdString").getAsString();
		int examHour = jsonObject.get("examHour").getAsInt();
		int examMinute = jsonObject.get("examMinute").getAsInt();
		//examTransaction.saveExam(examname, instituteId, subject, division,totalMarks , passingmarks, creatorId,batch , questionIdString, answerIdString,examHour,examMinute);
		return SUCCESS;
	}

}
