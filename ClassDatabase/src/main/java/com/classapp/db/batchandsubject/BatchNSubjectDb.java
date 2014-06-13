package com.classapp.db.batchandsubject;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;
import com.datalayer.batchnsubject.BatchAndSubject;

public class BatchNSubjectDb {
	public String addSubjectNBatch(BatchAndSubject batchAndSubject){
		com.classapp.db.batchandsubject.BatchAndSubject batchAndSubjectDb = new com.classapp.db.batchandsubject.BatchAndSubject();
		try {
			BeanUtils.copyProperties(batchAndSubjectDb, batchAndSubject);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(batchAndSubjectDb);
			transaction.commit();
		} catch (Exception e) {
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
}
