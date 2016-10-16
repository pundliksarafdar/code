package com.classapp.db.init;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

public class InitDb {
	public void initDb(){
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		
		//Queries run here
		session.createSQLQuery("").executeUpdate();
		transaction.commit();
		if(session!=null){
			session.close();
		}
	}
}
