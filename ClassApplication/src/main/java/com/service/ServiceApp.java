package com.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class ServiceApp extends Application{
private Set<Object> singletons = new HashSet<Object>();
    
	public ServiceApp() {
        singletons.add(new CorexServiceApi());
        singletons.add(new ClassownerServiceImpl());
        singletons.add(new FeeServiceImpl());
        singletons.add(new TimeTableServiceImpl());
        singletons.add(new StudentMarksImpl());
        singletons.add(new ClassownerExamService());
        singletons.add(new CustomUserService());
    }
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
