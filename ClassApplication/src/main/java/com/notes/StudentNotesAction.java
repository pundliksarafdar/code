package com.notes;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.Notes.Notes;
import com.classapp.db.batch.division.Division;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.config.BaseAction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.notes.NotesTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.student.StudentTransaction;
import com.user.UserBean;

public class StudentNotesAction extends BaseAction{
	int institute;
	String batch;
	int subject;
	List<Notes> noteslist;
	String notesavailable;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		/*BatchTransactions batchTransactions=new BatchTransactions();
		int regID=userBean.getRegId();
		StudentTransaction studentTransaction=new StudentTransaction();
		List<Student> list=studentTransaction.getStudent(regID);
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> beans= registerTransaction.getclassNames(list);
		request.setAttribute("Classes", beans);*/
		NotesTransaction notesTransaction=new NotesTransaction();
		noteslist =notesTransaction.getStudentNotesPath(batch, subject, institute);
		if(noteslist == null || noteslist.size()==0){
			notesavailable="no";
		}
		return SUCCESS;
	}
	public int getInstitute() {
		return institute;
	}
	public void setInstitute(int institute) {
		this.institute = institute;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public int getSubject() {
		return subject;
	}
	public void setSubject(int subject) {
		this.subject = subject;
	}
	public List<Notes> getNoteslist() {
		return noteslist;
	}
	public void setNoteslist(List<Notes> noteslist) {
		this.noteslist = noteslist;
	}
	public String getNotesavailable() {
		return notesavailable;
	}
	public void setNotesavailable(String notesavailable) {
		this.notesavailable = notesavailable;
	}
	

}
