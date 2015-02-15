package com.classapp.db.Teacher;

import java.io.Serializable;

public class Teacher implements Serializable{
int user_id;
int class_id;
String suffix;
public String getSuffix() {
	return suffix;
}
public void setSuffix(String suffix) {
	this.suffix = suffix;
}
String sub_ids;
public String getSub_ids() {
	return sub_ids;
}
public void setSub_ids(String sub_ids) {
	this.sub_ids = sub_ids;
}
public int getUser_id() {
	return user_id;
}
public void setUser_id(int user_id) {
	this.user_id = user_id;
}
public int getClass_id() {
	return class_id;
}
public void setClass_id(int class_id) {
	this.class_id = class_id;
}

}
