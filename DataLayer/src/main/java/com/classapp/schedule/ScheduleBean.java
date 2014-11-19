package com.classapp.schedule;

import java.util.HashMap;
import java.util.List;

public class ScheduleBean {
	List<Schedule> schedule;
	HashMap<String, List> studentScheduleData;

	public List<Schedule> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<Schedule> schedule) {
		this.schedule = schedule;
	}

	public HashMap<String, List> getStudentScheduleData() {
		return studentScheduleData;
	}

	public void setStudentScheduleData(HashMap<String, List> studentData) {
		this.studentScheduleData = studentData;
	}

	
}
