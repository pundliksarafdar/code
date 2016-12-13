package com.corex.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.corex.service.AttendanceService;
import com.corex.service.InstituteService;
import com.corex.service.Service;

public class AppClass extends Application{
	private Set<Object> singletons = new HashSet<Object>();
    
	public AppClass() {
		singletons.add(new Service());
		singletons.add(new InstituteService());
		singletons.add(new AttendanceService());
    }
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
