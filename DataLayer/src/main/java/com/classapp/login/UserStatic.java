package com.classapp.login;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.service.beans.ClassownerSettingsNotification;

public class UserStatic {
	private double totalStorage;
	private double usedSpace;
	private double remainingSpace;
	private double notesSpace;
	private double examSpace;
	private String storageSpace;
	private String examPath;
	private String notesPath;
	private String patternPath;
	private String questionPaperPath;
	private String headerPath;
	private Set<String> alarms;
	private ClassownerSettingsNotification settings;
	
	public double getTotalStorage() {
		return totalStorage;
	}
	public void setTotalStorage(double totalStorage) {
		this.totalStorage = totalStorage;
	}
	public double getUsedSpace() {
		return usedSpace;
	}
	public void setUsedSpace(double usedSpace) {
		this.usedSpace = usedSpace;
	}
	public double getRemainingSpace() {
		return remainingSpace;
	}
	public void setRemainingSpace(double remainingSpace) {
		this.remainingSpace = remainingSpace;
	}
	public String getStorageSpace() {
		return storageSpace;
	}
	public void setStorageSpace(String storageSpace) {
		this.storageSpace = storageSpace;
	}
	public double getNotesSpace() {
		return notesSpace;
	}
	public void setNotesSpace(double notesSpace) {
		this.notesSpace = notesSpace;
	}
	public double getExamSpace() {
		return examSpace;
	}
	public void setExamSpace(double examSpace) {
		this.examSpace = examSpace;
	}
	public Set<String> getAlarms() {
		if (null == alarms) {
			alarms = new HashSet<String>();
		}
		return alarms;
	}
	public void setAlarms(Set<String> alarms) {
		this.alarms = alarms;
	}
	public String getExamPath() {
		return this.storageSpace+File.separator+"exam";
	}
	public void setExamPath(String examPath) {
		this.examPath = examPath;
	}
	public String getNotesPath() {
		return this.storageSpace+File.separator+"notes";
	}
	public void setNotesPath(String notesPath) {
		this.notesPath = notesPath;
	}
	public String getPatternPath() {
		return this.storageSpace+File.separator+"pattern";
	}
	public void setPatternPath(String patternPath) {
		this.patternPath = patternPath;
	}
	public String getQuestionPaperPath() {
		return this.storageSpace+File.separator+"QuestionPaper";
	}
	public void setQuestionPaperPath(String questionPaperPath) {
		this.questionPaperPath = questionPaperPath;
	}
	public String getHeaderPath() {
		return this.storageSpace+File.separator+"headerFiles";
	}
	public void setHeaderPath(String headerPath) {
		this.headerPath = headerPath;
	}
	public String getNotesPath(int sub_id,int div_id) {
		return this.storageSpace+File.separator+"notes"+File.separator+sub_id+File.separator+div_id;
	}
	public ClassownerSettingsNotification getSettings() {
		return settings;
	}
	public void setSettings(ClassownerSettingsNotification settings) {
		this.settings = settings;
	}
	
	
}
