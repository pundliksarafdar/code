package com.service.beans;

import java.util.List;

import com.classowner.SendMessageAction;
import com.serviceinterface.IAcademicAlert;

public class SendAcademicAlertProgressCardBean extends IAcademicAlert{
	List<Integer>examList;

	public List<Integer> getExamList() {
		return examList;
	}

	public void setExamList(List<Integer> examList) {
		this.examList = examList;
	}
	
}
