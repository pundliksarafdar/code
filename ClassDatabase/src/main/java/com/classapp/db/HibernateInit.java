package com.classapp.db;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

public class HibernateInit {
	public void createTablesIfNotExist(){
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		
		session.createSQLQuery("CREATE TABLE 'appl_classes_test' ('user_id' int(11) NOT NULL,'class_id' int(11) NOT NULL)").executeUpdate();
		
	}
}
