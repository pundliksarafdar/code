package com.classapp.db.notice;

import java.sql.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.logger.AppLogger;
import com.classapp.persistence.HibernateUtil;

public class NoticeDB {
	public Boolean saveStudentNotice(StudentNotice notice) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.save(notice);
		transaction.commit();
		session.close();
		return  true;
	
	}
	
	public Boolean saveStaffNotice(StaffNotice notice) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.save(notice);
		transaction.commit();
		session.close();
		return  true;
	
	}
	
	public List<StudentNotice> getStudentNotice(int inst_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from StudentNotice where inst_id = :inst_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("inst_id", inst_id);  
			queryResultList = query.list();
			transaction.commit();
			
		}catch(Exception e){
			AppLogger.logError(e);
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return queryResultList;
	}
	
	public List<StaffNotice> getStaffNotice(int inst_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from StaffNotice where inst_id = :inst_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("inst_id", inst_id);  
			queryResultList = query.list();
			transaction.commit();
			
		}catch(Exception e){
			AppLogger.logError(e);
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return queryResultList;
	}
	
	public boolean deleteStudentNotice(int inst_id,int notice_id){
		Session session = null;
		Transaction transaction = null;
		String queryString="delete from StudentNotice where inst_id = :inst_id and notice_id = :notice_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("inst_id", inst_id);
			query.setInteger("notice_id", notice_id);
			query.executeUpdate();
			transaction.commit();
			
		}catch(Exception e){
			AppLogger.logError(e);
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return true;
	}
	
	public boolean deleteStaffNotice(int inst_id,int notice_id){
		Session session = null;
		Transaction transaction = null;
		String queryString="delete from StaffNotice where inst_id = :inst_id and notice_id = :notice_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("inst_id", inst_id);
			query.setInteger("notice_id", notice_id);
			query.executeUpdate();
			transaction.commit();
			
		}catch(Exception e){
			AppLogger.logError(e);
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return true;
	}
	
	public List<StudentNotice> getStudentNotice(int inst_id,Date date){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from StudentNotice where inst_id = :inst_id and start_date <= :date and end_date >= :date";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("inst_id", inst_id); 
			query.setParameter("date", date);
			queryResultList = query.list();
			transaction.commit();
			
		}catch(Exception e){
			AppLogger.logError(e);
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return queryResultList;
	}
	
	public List<StaffNotice> getStaffNotice(int inst_id,Date date){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from StaffNotice where inst_id = :inst_id and start_date <= :date and end_date >= :date";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("inst_id", inst_id);  
			query.setParameter("date", date);
			queryResultList = query.list();
			transaction.commit();
			
		}catch(Exception e){
			AppLogger.logError(e);
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return queryResultList;
	}
	
	public List<StaffNotice> getStaffNotice(int inst_id,Date date,String role){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from StaffNotice where inst_id = :inst_id and start_date <= :date and end_date >= :date and (role = :role1 "
				+ "or role like :role2 or role like :role3 or role like :role4 or role = 0)";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("inst_id", inst_id);  
			query.setParameter("date", date);
			query.setParameter("role1", role);
			query.setParameter("role2", role+",%");
			query.setParameter("role3", "%,"+role+",%");
			query.setParameter("role4", "%,"+role);
			queryResultList = query.list();
			transaction.commit();
			
		}catch(Exception e){
			AppLogger.logError(e);
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return queryResultList;
	}
	
	public List<StudentNotice> getStudentNotice(int inst_id,Date date,int div_id,String batch_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from StudentNotice where inst_id = :inst_id and start_date <= :date and end_date >= :date and (div_id = 0 "
				+ "or div_id = :div_id) and ( batch_id = '' or ";
		String [] batchArray = batch_id.split(",");
		int i = 1;
		for (String string : batchArray) {
			queryString = queryString + " batch_id = :batch_id"+i+++" or batch_id like :batch_id"+i+++" or batch_id like :batch_id"+i+++" or batch_id like :batch_id"+i+++"";
		}
		queryString = queryString + ")";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("inst_id", inst_id);  
			query.setParameter("date", date);
			query.setParameter("div_id", div_id);
			i = 1;
			for (String string : batchArray) {
				query.setParameter("batch_id"+i++, string);
				query.setParameter("batch_id"+i++, string+",%");
				query.setParameter("batch_id"+i++, "%,"+string+",%");
				query.setParameter("batch_id"+i++, "%,"+string);
			}
			queryResultList = query.list();
			transaction.commit();
			
		}catch(Exception e){
			AppLogger.logError(e);
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return queryResultList;
	}
}
