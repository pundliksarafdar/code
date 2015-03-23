package com.classapp.db.urlaccess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Schedule.ScheduleDB;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDetails;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.SubjectDb;
import com.classapp.persistence.HibernateUtil;


public class URLAccessDB {

	public List<PathAccess> getURLAndAccess() {
		Transaction transaction = null;
		Session session = null;
		List<PathAccess> list = null;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session
					.createQuery("from PathAccess");
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return list;
	}
	

	public boolean setURLAndAccess(PathAccess pathAccess) {
		Transaction transaction = null;
		Session session = null;
		List<PathAccess> list = null;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(pathAccess);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return true;
	}
	
	public boolean setURLAccessBatch(List<PathAccess> listPathAccesses){
		boolean result = false;
		Session session = HibernateUtil.getSessionfactory().openSession();
		Transaction tx = session.beginTransaction();
		
		int count = 0;
		for(PathAccess pathAccess:listPathAccesses) {
			session.saveOrUpdate(pathAccess);
			/*
			if( ++count % 50 == 0 ) { // Same as the JDBC batch size
		        //flush a batch of inserts and release memory:
		        session.flush();
		        session.clear();
		    }*/
		}
		tx.commit();
		session.close();
		return result;
	}
	
	public static void main(String[] args) {
		List<PathAccess> listPathAccesses = new ArrayList<PathAccess>();
		
		for (int i = 10; i < 17; i++) {
			PathAccess access = new PathAccess();
			access.setAccess("0,1,2,3");
			//access.setId(i);
			access.setPaths("path"+i);
			listPathAccesses.add(access);
		}
		URLAccessDB urlAccessDB = new URLAccessDB();
		urlAccessDB.setURLAccessBatch(listPathAccesses);
	}
	
	public boolean isAcessible(String action,int role){
		boolean result = false;
		Transaction transaction = null;
		Session session = null;
		List<PathAccess> list = null;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session
					.createQuery("from PathAccess where paths =:action and access like :role");
			query.setString("action", action);
			query.setString("role", "%"+role+"%");
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		
		if(null!=list && list.size()<1){
			result = true;
		}
		return result;
	}
}
