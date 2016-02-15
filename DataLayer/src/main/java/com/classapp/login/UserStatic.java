package com.classapp.login;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

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
	
	private Set<String> alarms;
	
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
	
	
}
