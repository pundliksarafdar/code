package com.classapp.db.classwithsubject;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ClassWithSubjects implements Serializable{
	/*private CompositeKeyPK class_subject_pk;

	public CompositeKeyPK getClass_subject_pk() {
		return class_subject_pk;
	}

	public void setClass_subject_pk(CompositeKeyPK class_subject_pk) {
		this.class_subject_pk = class_subject_pk;
	}
	*/
	private int class_id;
	private int sub_id;
	
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
