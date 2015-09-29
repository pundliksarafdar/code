package com.notes;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.Notes.Notes;
import com.classapp.db.batch.division.Division;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.notes.NotesTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class DisplayNotesListAction extends BaseAction{
	private String division;
	private String subject;
	private String batch;
	private String institute;
	List<Notes> noteslist;
	private String newbatch;
	private String actionname;
	private String notesname;
	private int notesid;
	int currentPage;
	int totalPage;
	int role;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		int inst_id=userBean.getRegId();
		if(!"".equals(institute) && institute!=null){
			inst_id=Integer.parseInt(institute);
		}
		role=userBean.getRole();
		// TODO Auto-generated method stub
		if("editnames".equals(actionname)){
			NotesTransaction notesTransaction=new NotesTransaction();
			notesTransaction.updatenotes(notesname, notesid, newbatch,inst_id,Integer.parseInt(division),Integer.parseInt(subject));
		}else if("deletenotes".equals(actionname)){
			NotesTransaction notesTransaction=new NotesTransaction();
			String name=notesTransaction.getNotepathById(notesid,inst_id,Integer.parseInt(subject),Integer.parseInt(division));
			 UserStatic userStatic = userBean.getUserStatic();
		      String filePath=  userStatic.getNotesPath()+File.separator+subject+File.separator+division+File.separator+name;
		      String DBPAth="";
		      if(userBean.getRole()==2){
					String storagePath = Constants.STORAGE_PATH+File.separator+institute;
					userStatic.setStorageSpace(storagePath);
		    	  filePath=  userStatic.getNotesPath()+File.separator+subject+File.separator+division+File.separator+name;
			      
		      }
		      
			File file = new File(filePath);
			  file.delete();
			
			notesTransaction.deleteNotes(notesid,inst_id,Integer.parseInt(division),Integer.parseInt(subject));
		}
		
		if(currentPage==0){
			currentPage++;
		}
		totalPage=0;
		NotesTransaction notesTransaction=new NotesTransaction();
		int totalCount=notesTransaction.getNotescount(Integer.parseInt(division), Integer.parseInt(subject), inst_id,batch);
		
		if(totalCount>0){
			int remainder=totalCount%2;
			totalPage=totalCount/2;
			if(remainder>0){
				totalPage++;
			}
		}
		
		if(totalPage<currentPage){
			currentPage--;
		}
		noteslist =notesTransaction.getNotesPath(Integer.parseInt(division), Integer.parseInt(subject), inst_id,currentPage,batch);
		if(!"".equals(institute) && institute!=null){
			return "teachernotes";
		}
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
	public List<Notes> getNoteslist() {
		return noteslist;
	}
	public void setNoteslist(List<Notes> noteslist) {
		this.noteslist = noteslist;
	}
	public String getActionname() {
		return actionname;
	}
	public void setActionname(String actionname) {
		this.actionname = actionname;
	}
	public String getNewbatch() {
		return newbatch;
	}
	public void setNewbatch(String newbatch) {
		this.newbatch = newbatch;
	}
	public String getNotesname() {
		return notesname;
	}
	public void setNotesname(String notesname) {
		this.notesname = notesname;
	}
	public int getNotesid() {
		return notesid;
	}
	public void setNotesid(int notesid) {
		this.notesid = notesid;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	
	

}
