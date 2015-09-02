package com.exam;

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
import com.transaction.notification.NotificationGlobalTransation;
import com.user.UserBean;

public class SubjectChooseAction extends BaseAction{
	   private String successaction;
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
	
	public String getSuccessaction() {
		return successaction;
	}
	public void setSuccessaction(String successaction) {
		this.successaction = successaction;
	}
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		     
		return SUCCESS;
	}
	
}
