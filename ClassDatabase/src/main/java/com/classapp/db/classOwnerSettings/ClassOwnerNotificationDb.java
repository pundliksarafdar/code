package com.classapp.db.classOwnerSettings;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
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
	
	public List<ContactDetailBean> getContactDetailStudentParent(List<Integer> studentId,int instId) {
		List<ContactDetailBean> contacts=new ArrayList<ContactDetailBean>();
		ListCustomToString listCustomToString = new ListCustomToString();
		listCustomToString.addAll(studentId);
		Transaction transaction=null;
		Session session = null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("select s.parentPhone,s.parentEmail,r.phone1,r.email,r.regId from Student s,RegisterBean r where s.student_id=r.regId and (s.student_id) in :studentIds and s.class_id = :classId");
		
		query.setParameterList("studentIds", listCustomToString);
		query.setParameter("classId", instId);
		List list = query.list();
		
		for(Object object:list){
			Object[] objects = (Object[]) object;
			ContactDetailBean contactDetailBean = new ContactDetailBean();
			if(null!=objects[0] && !"".equals(objects[0])){
				contactDetailBean.setParentPhone(Long.parseLong((String)objects[0]));
			}
			contactDetailBean.setParentEmail((String)objects[1]);
			if(null!=objects[2] && !"".equals(objects[2])){
				contactDetailBean.setStudentPhone(Long.parseLong((String) objects[2]));
			}
			contactDetailBean.setStudentEmail((String)objects[3]);
			contactDetailBean.setStudentId(((Number)objects[4]).intValue());
			contacts.add(contactDetailBean);
		}
		
		System.out.println(contacts);
		transaction.commit();
		session.close();
		return  contacts;
	}
	
	public List<ClassOwnerNotificationBean> getInstitutesForDailyAttendance(String day){
		Transaction transaction=null;
		Session session=null;
		List resultList = null;
		day = "\""+day.substring(0,3)+"\":true";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			String queryString="from ClassOwnerNotificationBean "
					+ "where weeklyRecurrence like :day and (smsAttendanceDaily = true or emailAttendanceDaily = true)";
			Query query = session.createQuery(queryString);
			query.setParameter("day", "%"+day+"%");
			resultList =query.list();
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
		return resultList;
	}
	
	public List<ClassOwnerNotificationBean> getInstitutesForWeeklyAttendance(){
		Transaction transaction=null;
		Session session=null;
		List resultList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			String queryString="from ClassOwnerNotificationBean where  smsAttendanceWeekly = true or emailAttendanceWeekly = true";
			Query query = session.createQuery(queryString);
			resultList =query.list();
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
		return resultList;
	}
	
	public List<ClassOwnerNotificationBean> getInstitutesForMonthlyAttendance(){
		Transaction transaction=null;
		Session session=null;
		List resultList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			String queryString="from ClassOwnerNotificationBean where  smsAttendanceMonthly = true or emailAttendanceMonthly = true";
			Query query = session.createQuery(queryString);
			resultList =query.list();
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
		return resultList;
	}
	
	public List<ClassOwnerNotificationBean> getInstitutesForFeesDueNotifications(Date date){
		Transaction transaction=null;
		Session session=null;
		List resultList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			String queryString="from ClassOwnerNotificationBean "
					+ "where paymentDueDate like :date and (smsPaymentDue = true or emailPaymentDue = true)";
			Query query = session.createQuery(queryString);
			query.setParameter("date", date);
			resultList =query.list();
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
		return resultList;
	}
	
	public class ListCustomToString<E> extends ArrayList<E>{
		/*
		@Override
		public String toString() {
			Iterator<E> it = iterator();
	        if (! it.hasNext())
	            return "()";

	        StringBuilder sb = new StringBuilder();
	        sb.append('(');
	        for (;;) {
	            E e = it.next();
	            sb.append(e == this ? "(this Collection)" : e);
	            if (! it.hasNext())
	                return sb.append(')').toString();
	            sb.append(',').append(' ');
	        }
		}
		*/
	};
}
