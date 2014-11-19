package com.classapp.db.batch.division;

import java.io.Serializable;

public class ClassDivision implements Serializable{
int class_id;
int div_id;
public int getClass_id() {
	return class_id;
}
public void setClass_id(int class_id) {
	this.class_id = class_id;
}
public int getDiv_id() {
	return div_id;
}
public void setDiv_id(int div_id) {
	this.div_id = div_id;
}


}
