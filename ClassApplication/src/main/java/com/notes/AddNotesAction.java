package com.notes;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.classapp.db.Notes.Notes;
import com.config.BaseAction;
import com.transaction.notes.NotesTransaction;
import com.user.UserBean;

public class AddNotesAction extends BaseAction{
	 private File myFile;
	   private String myFileContentType;
	   private String myFileFileName;
	   private String destPath;
	   private String subject;
	   private String division;
	   private String notesname;
	
	public String getNotesname() {
		return notesname;
	}
	public void setNotesname(String notesname) {
		this.notesname = notesname;
	}
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		/* Copy file to a safe location */
	      destPath = "D:/"+userBean.getRegId()+"/"+division+"/"+subject+"/"; 
	      Notes notes=new Notes();
	      try{
	     	 System.out.println("Src File name: " + myFile);
	     	 System.out.println("Dst File name: " + myFileFileName);
	     	    	 
	     	 File destFile  = new File(destPath, myFileFileName);
	    	 FileUtils.copyFile(myFile, destFile);
	    	 notes.setClassid(userBean.getRegId());
	    	 notes.setDivid(Integer.parseInt(division));
	    	 notes.setNotespath(destPath+myFileFileName);
	    	 notes.setSubid(Integer.parseInt(subject));
	    	 notes.setName(notesname);
	    	 NotesTransaction notesTransaction=new NotesTransaction();
	    	 notesTransaction.addNotes(notes);
	  
	      }catch(IOException e){
	         e.printStackTrace();
	         return ERROR;
	      }

	      return SUCCESS;
	}
	public File getMyFile() {
		return myFile;
	}
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	public String getMyFileContentType() {
		return myFileContentType;
	}
	public void setMyFileContentType(String myFileContentType) {
		this.myFileContentType = myFileContentType;
	}
	public String getMyFileFileName() {
		return myFileFileName;
	}
	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}
	public String getDestPath() {
		return destPath;
	}
	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}

}
