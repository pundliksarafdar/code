package com.classapp.db.feature;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

public class FeatureDb {
	
	public boolean saveFeature(Feature feature){
		boolean successful = true;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(feature);
			transaction.commit();
		}catch(Exception e){
			successful = false;
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return successful;
	}
}
