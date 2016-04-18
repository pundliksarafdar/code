package com.notes;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.register.RegisterTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class AddTeacherNotesOptionAction extends BaseAction{
	private String division;
	private String subject;
	private String batch;
	private String institute;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		String notes=(String) request.getParameter("notesadded");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		TeacherTransaction teacherTransaction=new TeacherTransaction();
		List classids=teacherTransaction.getTeachersClass(userBean.getRegId());
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List classes=registerTransaction.getTeachersInstitutes(classids);
		request.setAttribute("classes", classes);
		
		request.setAttribute("notes", notes);
		
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
	public String getInstitute() {
		return institute;
	}
	public void setInstitute(String institute) {
		this.institute = institute;
	}
	
	

}
