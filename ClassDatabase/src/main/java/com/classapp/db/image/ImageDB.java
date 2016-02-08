package com.classapp.db.image;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.Feedbacks.Feedback;
import com.classapp.db.exam.Exam;
import com.classapp.persistence.HibernateUtil;

public class ImageDB {
	public void save(Image image) {
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(image);
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
	
	public static void main(String[] args) {
		
	}
	
}
