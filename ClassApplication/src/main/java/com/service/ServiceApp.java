package com.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class ServiceApp extends Application{
private Set<Object> singletons = new HashSet<Object>();
    
	public ServiceApp() {
        singletons.add(new CorexServiceApi());
        singletons.add(new ClassownerServiceImpl());
    }
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
