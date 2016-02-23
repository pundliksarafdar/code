package com.classapp.db.header;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.Feedbacks.Feedback;
import com.classapp.db.exam.Exam;
import com.classapp.persistence.HibernateUtil;

public class HeaderDB {
	public void save(String headerId,String headerName,int instId) {
		Header header = new Header();
		header.setHeader_id(headerId);
		header.setHeader_name(headerName);
		header.setInst_id(instId);
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(header);
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
