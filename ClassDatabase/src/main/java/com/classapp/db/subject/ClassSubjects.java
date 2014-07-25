package com.classapp.db.subject;

import java.io.Serializable;

public class ClassSubjects implements Serializable{
int class_id;
int sub_id;
public int getClass_id() {
	return class_id;
}
public void setClass_id(int class_id) {
	this.class_id = class_id;
}
public int getSub_id() {
	return sub_id;
}
public void setSub_id(int sub_id) {
	this.sub_id = sub_id;
}


}
