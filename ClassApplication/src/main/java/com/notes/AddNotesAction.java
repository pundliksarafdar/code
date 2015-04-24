package com.notes;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.management.loading.PrivateClassLoader;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

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
	   private String batch;
	   private String validforbatch;
	   private String allbatches;
	   private String classes;
	   
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public String getAllbatches() {
		return allbatches;
	}
	public void setAllbatches(String allbatches) {
		this.allbatches = allbatches;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getValidforbatch() {
		return validforbatch;
	}
	public void setValidforbatch(String validforbatch) {
		this.validforbatch = validforbatch;
	}
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
	    //  destPath = "D:/"+userBean.getRegId()+"/"+division+"/"+subject+"/"; 
	      Notes notes=new Notes();
	      destPath=  ServletActionContext.getServletContext().getRealPath("/"+userBean.getRegId())+"/Notes/"+division+"/"+subject+"/";
	      String DBPAth=userBean.getRegId()+"/Notes/"+division+"/"+subject+"/";
	      if(userBean.getRole()==2){
	    	  destPath=  ServletActionContext.getServletContext().getRealPath("/"+classes)+"/Notes/"+division+"/"+subject+"/";
		       DBPAth=classes+"/Notes/"+division+"/"+subject+"/";
	      }
	      
	      
	      try{
	     	 System.out.println("Src File name: " + myFile);
	     	 System.out.println("Dst File name: " + myFileFileName);
	     	    	 
	     	 File destFile  = new File(destPath, myFileFileName);
	    	 FileUtils.copyFile(myFile, destFile);
	    	 if(userBean.getRole()==2){
	    		 notes.setClassid(Integer.parseInt(classes));
	    	 }else{
	    	 notes.setClassid(userBean.getRegId());
	    	 }
	    	 notes.setDivid(Integer.parseInt(division));
	    	 notes.setNotespath(DBPAth+myFileFileName);
	    	 notes.setSubid(Integer.parseInt(subject));
	    	 notes.setName(notesname);
	    	 notes.setAddedby(userBean.getRegId());
	    	 if(validforbatch.equals("all")){
	    		 batch=allbatches;
	    	 }
	    	 notes.setBatch(batch);
	    	 notes.setTime(new Timestamp(new Date().getTime()));
	    	 NotesTransaction notesTransaction=new NotesTransaction();
	    	 notesTransaction.addNotes(notes);
	  
	      }catch(IOException e){
	         e.printStackTrace();
	         return ERROR;
	      }
	      if(userBean.getRole()==2){
	    	  return "teachernotes";
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
