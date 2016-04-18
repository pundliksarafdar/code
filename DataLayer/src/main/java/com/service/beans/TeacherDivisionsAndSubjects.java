package com.service.beans;
import java.util.List;

import com.datalayer.subject.Subject;

public class TeacherDivisionsAndSubjects {
	List<Subject>  subjectList;
	List<Division> divisionList;
	public List<Subject> getSubjectList() {
		return subjectList;
	}
	public void setSubjectList(List<Subject> subjectList) {
		this.subjectList = subjectList;
	}
	public List<Division> getDivisionList() {
		return divisionList;
	}
	public void setDivisionList(List<Division> divisionList) {
		this.divisionList = divisionList;
	}
	
}
