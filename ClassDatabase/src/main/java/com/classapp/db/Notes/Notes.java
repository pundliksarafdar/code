package com.classapp.db.Notes;

import java.sql.Timestamp;

public class Notes {
int notesid;
String notespath;
int classid;
int divid;
int subid;
String name;
String batch;
Timestamp time;
int addedby;

public int getAddedby() {
	return addedby;
}
public void setAddedby(int addedby) {
	this.addedby = addedby;
}
public Timestamp getTime() {
	return time;
}
public void setTime(Timestamp time) {
	this.time = time;
}
public String getBatch() {
	return batch;
}
public void setBatch(String batch) {
	this.batch = batch;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getNotesid() {
	return notesid;
}
public void setNotesid(int notesid) {
	this.notesid = notesid;
}
public String getNotespath() {
	return notespath;
}
public void setNotespath(String notespath) {
	this.notespath = notespath;
}
public int getClassid() {
	return classid;
}
public void setClassid(int classid) {
	this.classid = classid;
}
public int getDivid() {
	return divid;
}
public void setDivid(int divid) {
	this.divid = divid;
}
public int getSubid() {
	return subid;
}
public void setSubid(int subid) {
	this.subid = subid;
}



}
