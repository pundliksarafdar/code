package com.classapp.db.classOwnerSettings;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.institutestats.InstituteStats;
import com.classapp.persistence.HibernateUtil;

public class ClassOwnerNotificationDb {
	
	public boolean saveClassownerNotification(ClassOwnerNotificationBean bean){
		boolean successful = true;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(bean);
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
	
	public ClassOwnerNotificationBean getClassOwnerNotification(int inst_id){
		ClassOwnerNotificationBean classOwnerNotificationBean = new ClassOwnerNotificationBean();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		classOwnerNotificationBean =(ClassOwnerNotificationBean) session.get(ClassOwnerNotificationBean.class,inst_id);
		if(null!=session){session.close();}
		return classOwnerNotificationBean;
	}
}
