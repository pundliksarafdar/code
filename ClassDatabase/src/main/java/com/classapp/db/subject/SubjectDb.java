package com.classapp.db.subject;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.batch.Batch;
import com.classapp.db.user.UserBean;
import com.classapp.persistence.HibernateUtil;
import com.google.gson.Gson;

public class SubjectDb {

	public String updateDb(Subjects subjects){
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(subjects);
			transaction.commit();
		}catch(Exception e){
			status = "1";
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return status;
	}
	
	public List<Subjects> getSubject(int queryType,String queryStr,String[] params,String[] paramValues){
		Session session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery(queryStr);
		
		//Get all subject of regId
		if(0==queryType){
			query.setParameter(params[0],Integer.parseInt(paramValues[0]));
		}
		return query.list();
	}
	
}
