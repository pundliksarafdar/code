package com.notes;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.Notes.Notes;
import com.classapp.db.batch.division.Division;
import com.classapp.db.institutestats.InstituteStats;
import com.classapp.db.register.RegisterBean;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.institutestats.InstituteStatTransaction;
import com.transaction.notes.NotesTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class DisplayNotesListAction extends BaseAction{
	
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List list=subjectTransaction.getAllClassSubjects(userBean.getRegId());
		request.setAttribute("subjects", list);
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		List<Division> divisions=divisionTransactions.getAllDivisions(userBean.getRegId());
		request.setAttribute("divisions", divisions);
		return SUCCESS;
	}	

}
