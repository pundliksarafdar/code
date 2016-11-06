package com.classapp.db.notificationpkg;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.register.RegisterBean;
import com.classapp.logger.AppLogger;
import com.classapp.persistence.HibernateUtil;

public class NotificationDB {
	public Boolean add(Notification notification) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.save(notification);
		transaction.commit();
		if(session!=null){
			session.close();
		}
		return  true;
	
	}
	
	public List<Notification> getMessageforStudent(int instituteid,List<String> batchids,int div_id) {
		Session session=null;
		List<Notification> notifications = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery("from Notification where  institute_id = :institute_id and ((batch in :batch and div_id=:div_id) or batch='ALL')");
			query.setParameter("institute_id", instituteid);
			query.setParameterList("batch", batchids);
			query.setParameter("div_id", div_id);
			notifications = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
			
	return notifications;
	}
	
	public List<Notification> getMessageforTeachers(int instituteid) {
		Session session=null;
		List<Notification> notifications = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery("from Notification where  institute_id = :institute_id and role=2");
			query.setParameter("institute_id", instituteid);
			notifications = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
			
	return notifications;
	}
	
	public List<Notification> getMessageforOwner(int instituteid) {
		Session session=null;
		List<Notification> notifications = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery("from Notification where  institute_id = :institute_id");
			query.setParameter("institute_id", instituteid);
			notifications = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
			
	return notifications;
	}
	
	public void deleteNotifications() {
		Transaction transaction=null;
		Session session=null;
		List<Notification> notifications = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Notification where  msg_date < CURRENT_DATE-7");
			int noofrowsdeleted=query.executeUpdate();
			transaction.commit();
			AppLogger.logger("No of notifications deleted="+noofrowsdeleted);
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
	
	public Boolean add(Institute_MsgLog msgLog) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.save(msgLog);
		transaction.commit();
		if(session!=null){
			session.close();
		}
		return  true;
	
	}
}
