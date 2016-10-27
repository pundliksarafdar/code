package com.classapp.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {
	private HibernateUtil(){
	}
	private static final SessionFactory SESSION_FACTORY = buildSessioFactory();
	private static ServiceRegistry serviceRegistry;
	
	private static SessionFactory buildSessioFactory(){
		Configuration configuration = new Configuration();
	    configuration.configure();
	    serviceRegistry = new ServiceRegistryBuilder().applySettings(
	            configuration.getProperties()). buildServiceRegistry();
	    SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	    
	    return sessionFactory;
	}
	
	
	public static SessionFactory getSessionfactory() {
		if(SESSION_FACTORY.isClosed()){
			System.out.println("Session closed...........................................................");
		}
		
		return SESSION_FACTORY;
	}
	
	public static void shutdown(){
		getSessionfactory().close();
	}
}
