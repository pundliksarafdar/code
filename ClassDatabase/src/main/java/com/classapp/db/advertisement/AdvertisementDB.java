package com.classapp.db.advertisement;

import java.sql.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.batch.Batch;
import com.classapp.db.exam.Exam;
import com.classapp.persistence.HibernateUtil;

public class AdvertisementDB {
	
	public void save(Advertisement advertisement){
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(advertisement);
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

	public void updateDb(Advertisement advertisement){
	
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(advertisement);
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
	
	public int getCount(Date date){
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Advertisement.class).setProjection(Projections.rowCount());
			Criterion criterion = Restrictions.eq("advdate", date);
			criteria.add(criterion);
			long advListCount =  (Long) criteria.uniqueResult();
			transaction.commit();
			return (int)advListCount;
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
		return 0;
		
	}

}
