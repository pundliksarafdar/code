package com.classowner;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.exams.MCQDetails;
import com.config.BaseAction;
import com.config.Constants;
import com.helper.MCQDataHelper;
import com.user.UserBean;

public class UploadExamsAction extends BaseAction{
	String actionname,examname,exammarks;
	int uploadedMarks;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		if (null==actionname) {
			return SUCCESS;
		}else if(actionname.equalsIgnoreCase("submitmarks")){
			request.getSession().setAttribute("examname", examname);
			request.getSession().setAttribute("exammarks", exammarks);
			return "startuploadingexam";
		}else{
			return ERROR;
		}
	}
	public String getActionname() {
		return actionname;
	}
	public void setActionname(String actionname) {
		this.actionname = actionname;
	}
	public String getExamname() {
		return examname;
	}
	public void setExamname(String examname) {
		this.examname = examname;
	}
	public String getExammarks() {
		return exammarks;
	}
	public void setExammarks(String exammarks) {
		this.exammarks = exammarks;
	}
	public int getUploadedMarks() {
		return uploadedMarks;
	}
	public void setUploadedMarks(int uploadedMarks) {
		this.uploadedMarks = uploadedMarks;
	}
	
	
}
