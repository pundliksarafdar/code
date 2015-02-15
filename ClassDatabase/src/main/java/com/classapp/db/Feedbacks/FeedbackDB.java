package com.classapp.db.Feedbacks;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;


public class FeedbackDB {
public void addFeedback(Feedback feedback) {
	
	Session session = null;
	Transaction transaction = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		session.saveOrUpdate(feedback);
		transaction.commit();
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
	}finally{
		if(null!=session){
			session.close();
		}
	}
	
}
}
