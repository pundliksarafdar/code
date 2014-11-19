package com.exam;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.MCQData;
import com.transaction.exams.ExamTransaction;
import com.user.UserBean;

public class SearchExamResultAction extends BaseAction {
	private int divisionId, teacherId, subjectId;
	private String startDate, endDate;

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		ExamTransaction examTransaction = new ExamTransaction();
		List<MCQData> exams = examTransaction.searchExam(userBean.getRegId(), subjectId, teacherId, divisionId,
				startDate, endDate);
		request.setAttribute(Constants.EXAM_SEARCH_RESULT, exams);
		
		/*Setting to session as result will be useful for View exam action*/
		request.getSession().setAttribute(Constants.EXAM_SEARCH_RESULT, exams);
		return SUCCESS;
	}

	public int getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(int divisionId) {
		this.divisionId = divisionId;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
