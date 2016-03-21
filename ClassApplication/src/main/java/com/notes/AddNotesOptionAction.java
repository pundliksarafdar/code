package com.notes;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.logger.AppLogger;
import com.config.BaseAction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class AddNotesOptionAction extends BaseAction{
	private String division;
	private String subject;
	private String batch;
	
	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	String names;
	
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		AppLogger.logger("Names"+names);
		String notes=(String) request.getParameter("notesadded");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List list=subjectTransaction.getAllClassSubjects(userBean.getRegId());
		request.setAttribute("subjects", list);
		request.setAttribute("division", request.getAttribute("divisionName"));
		request.setAttribute("subject", request.getAttribute("classownerUploadexamSubjectNameSelect"));
		request.setAttribute("batch", request.getAttribute("classownerUploadexamSelectBatchName"));
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		List<Division> divisions=divisionTransactions.getAllDivisions(userBean.getRegId());
		request.setAttribute("notes", notes);
		request.setAttribute("divisions", divisions);
		return SUCCESS;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	

}
