package com.classowner;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.MCQData;
import com.transaction.exams.ExamTransaction;
import com.user.UserBean;

public class ViewAllExamsAction extends BaseAction{

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		/*ExamTransaction examTransaction = new ExamTransaction();
		List<MCQData> examData = examTransaction.getExamData(userBean.getRegId().toString());
		request.setAttribute(Constants.MCQS_LIST, examData);*/
		return SUCCESS;
	}
}
