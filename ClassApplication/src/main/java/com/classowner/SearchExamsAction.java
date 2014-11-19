package com.classowner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.config.Constants;
import com.user.UserBean;

public class SearchExamsAction extends BaseAction{

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		
		List<String> batchList = new ArrayList<String>();
		List<String> subjectList = new ArrayList<String>();
		List<String> teacherList = new ArrayList<String>();
		
		batchList.add("Batch1");
		batchList.add("Batch2");
		batchList.add("Batch3");
		
		subjectList.add("Subject1");
		subjectList.add("Subject2");
		subjectList.add("Subject3");
		
		teacherList.add("teacher1");
		teacherList.add("teacher2");
		teacherList.add("teacher3");
		
		request.setAttribute(Constants.TEACHER_LIST, teacherList);
		request.setAttribute(Constants.BATCH_LIST, batchList);
		request.setAttribute(Constants.SUBJECT_LIST, subjectList);
		
		request.getSession().removeAttribute(Constants.EXAM_DATA);
		return SUCCESS;
	}
	
}
