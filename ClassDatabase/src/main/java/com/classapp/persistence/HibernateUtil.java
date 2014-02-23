package com.classapp.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private HibernateUtil(){
	}
	private static final SessionFactory SESSION_FACTORY = buildSessioFactory();
	
	private static SessionFactory buildSessioFactory(){
		
		try{
			return new Configuration().configure().buildSessionFactory();
		}catch(Exception ex){
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static SessionFactory getSessionfactory() {
		return SESSION_FACTORY;
	}
	
	public static void shutdown(){
		getSessionfactory().close();
	}
}
