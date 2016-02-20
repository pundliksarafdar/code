package com.service.beans;

import java.util.List;

import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Topics;

public class SubjectsWithTopics {
	Subject subject;
	List<Topics> topics;
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public List<Topics> getTopics() {
		return topics;
	}
	public void setTopics(List<Topics> topics) {
		this.topics = topics;
	}
	
}
