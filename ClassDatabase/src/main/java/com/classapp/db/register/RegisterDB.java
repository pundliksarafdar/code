package com.classapp.db.register;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

public class RegisterDB {
	public List getTeacherName(List TeacherIDs) {
		
		Session session = null;
		Transaction transaction = null;
		List subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId in :teacherids");
			query.setParameterList("teacherids", TeacherIDs);
			subidList = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return subidList;
	}
	
public List getTeachersClassName(List classIDs) {
		
		Session session = null;
		Transaction transaction = null;
		List subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId in :teacherids");
			query.setParameterList("teacherids", classIDs);
			subidList = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return subidList;
	}
	
	public RegisterBean getscheduleTeacher(int TeacherId) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId in :teacherids");
			query.setParameter("teacherids", TeacherId);
			subidList = query.list();
			if(subidList!=null)
			{
				subidList.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return subidList.get(0);
	}
	
	public RegisterBean getRegisterclass(int regId) {
		Session session = null;
		Transaction transaction = null;
		List<RegisterBean> subidList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from RegisterBean where  regId in :teacherids");
			query.setParameter("teacherids", regId);
			subidList = query.list();
			if(subidList!=null)
			{
				subidList.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return subidList.get(0);
	}
}
