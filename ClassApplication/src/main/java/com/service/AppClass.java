package com.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class AppClass extends Application{
	private Set<Object> singletons = new HashSet<Object>();
    
	public AppClass() {
		singletons.add(new AdminToClassService());
		singletons.add(new AttendanceImlp());
		singletons.add(new ClassownerServiceImpl());
		singletons.add(new CorexServiceApi());
		singletons.add(new FeeServiceImpl());
		singletons.add(new TimeTableServiceImpl());
		singletons.add(new ClassownerSettingsServiceImpl());
		singletons.add(new StudentMarksImpl());
		singletons.add(new ClassownerExamService());
		singletons.add(new DeleteServiceImpl());
		singletons.add(new TeacherServiceImpl());
		singletons.add(new StudentService());
		singletons.add(new NotificationServiceImpl());
    }
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
