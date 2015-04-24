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

public class DisplayNotesAction extends BaseAction{
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		String notesid=(String) request.getParameter("notesid");
		NotesTransaction notesTransaction=new NotesTransaction();
		String path=notesTransaction.getNotepathById(Integer.parseInt(notesid));
		if(userBean.getRegId()!=null)
		{
		String path1=	ServletActionContext.getServletContext().getRealPath("/"+path);
		File file = new File(ServletActionContext.getServletContext().getRealPath("/"+path));
        response.setHeader("Content-Type", ServletActionContext.getServletContext().getMimeType(file.getName()));
        response.setHeader("Content-Length", String.valueOf(file.length()));
       
        try {
			Files.copy(file.toPath(), response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return SUCCESS;
	}
}
