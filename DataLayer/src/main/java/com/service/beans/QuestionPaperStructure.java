package com.service.beans;

import java.io.Serializable;
import java.util.List;

public class QuestionPaperStructure implements Serializable {
	String item_no;
	String item_description;
	String question_type;
	String question_topic;
	String item_type;
	int item_marks;
	int alternate_value;
	String parent_id;
	String item_id;
	int subject_id;
	
	public String getItem_no() {
		return item_no;
	}
	public void setItem_no(String item_no) {
		this.item_no = item_no;
	}
	public String getItem_description() {
		return item_description;
	}
	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}
	public String getQuestion_type() {
		return question_type;
	}
	public void setQuestion_type(String question_type) {
		this.question_type = question_type;
	}
	public String getQuestion_topic() {
		return question_topic;
	}
	public void setQuestion_topic(String question_topic) {
		this.question_topic = question_topic;
	}
	public String getItem_type() {
		return item_type;
	}
	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}
	public int getItem_marks() {
		return item_marks;
	}
	public void setItem_marks(int item_marks) {
		this.item_marks = item_marks;
	}
	public int getAlternate_value() {
		return alternate_value;
	}
	public void setAlternate_value(int alternate_value) {
		this.alternate_value = alternate_value;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public int getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}
	
}
