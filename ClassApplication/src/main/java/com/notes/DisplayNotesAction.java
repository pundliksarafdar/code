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
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.notes.NotesTransaction;
import com.user.UserBean;

public class DisplayNotesAction extends BaseAction{
	private String division;
	private String subject;
	private String batch;
	private String institute;
	private int notesid;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		//String notesid=(String) request.getParameter("notesid");
		NotesTransaction notesTransaction=new NotesTransaction();
		int inst_id=userBean.getRegId();
		if(!"".equals(institute) && institute!=null){
			inst_id=Integer.parseInt(institute);
		}
		String filename=notesTransaction.getNotepathById(notesid,inst_id,Integer.parseInt(subject),Integer.parseInt(division));
		UserStatic userStatic = userBean.getUserStatic();
		if(userBean.getRegId()!=null)
		{
			if(!"".equals(institute) && institute!=null){
				String storagePath = Constants.STORAGE_PATH+File.separator+institute;
				userStatic.setStorageSpace(storagePath);
			}
		String path=userStatic.getNotesPath()+File.separator+subject+File.separator+division+File.separator+filename;
		File file = new File(path);
        response.setHeader("Content-Type", ServletActionContext.getServletContext().getMimeType(file.getName()));
        response.setHeader("Content-Length", String.valueOf(file.length()));
       
        try {
			Files.copy(file.toPath(), response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return null;
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
	public int getNotesid() {
		return notesid;
	}
	public void setNotesid(int notesid) {
		this.notesid = notesid;
	}
	
}
