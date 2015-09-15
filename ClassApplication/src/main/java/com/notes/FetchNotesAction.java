package com.notes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.classapp.db.Notes.Notes;
import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.notes.NotesTransaction;
import com.user.UserBean;

public class FetchNotesAction extends BaseAction{
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		/*String subject=(String) request.getParameter("subject");
		String division=(String) request.getParameter("division");
		NotesTransaction notesTransaction=new NotesTransaction();
		List<Notes> noteslist =notesTransaction.getNotesPath(Integer.parseInt(division), Integer.parseInt(subject), userBean.getRegId());
		request.setAttribute("noteslist", noteslist);
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		List list=subjectTransaction.getAllClassSubjects(userBean.getRegId());
		request.setAttribute("subjects", list);
		DivisionTransactions divisionTransactions=new DivisionTransactions();
		List<Division> divisions=divisionTransactions.getAllDivisions(userBean.getRegId());
		request.setAttribute("divisions", divisions);
		*/
		return SUCCESS;
	}
}
