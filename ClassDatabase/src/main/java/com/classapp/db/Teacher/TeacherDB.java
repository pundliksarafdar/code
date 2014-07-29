package com.classapp.db.Teacher;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.register.RegisterBean;
import com.classapp.persistence.HibernateUtil;

public class TeacherDB {

	public Boolean isTeacherRegistered(String teacher) {
		Transaction transaction=null;
		Session session=null;
		List<RegisterBean> list=null;
		
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query=session.createQuery("from RegisterBean where loginName=:userid and role=2");
		query.setParameter("userid", teacher);
		list=query.list();
		if(list.size()>0)
		{
		return true;
		}
		else{
			return false;
		}
	}
	
	
	public Boolean isTeacherExists(String teacherID,int regid) {
		Transaction transaction=null;
		Session session=null;
		List<RegisterBean> list=null;
		List<Teacher> list2=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query=session.createQuery("from RegisterBean where loginName=:userid and role=2");
		query.setParameter("userid", teacherID);
		list=query.list();
		if(list.size()>0)
		{
			RegisterBean bean=list.get(0);
			Teacher teacher=new Teacher();
			teacher.setClass_id(regid);
			teacher.setUser_id(bean.getRegId());
			query=session.createQuery("from Teacher where user_id=:userid and class_id=:regid");
			query.setParameter("userid", teacher.getUser_id());
			query.setParameter("regid", teacher.getClass_id());
			list2=query.list();
			if (list2.size()>0) {
				return true;
			}else{
				
				return false;
			}
		
		}
		return false;
	}
	
	
	public Boolean add(String teacherID,int regid,String subjects) {
		Transaction transaction=null;
		Session session=null;
		List<RegisterBean> list=null;
		List<Teacher> list2=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query=session.createQuery("from RegisterBean where loginName=:userid and role=2");
		query.setParameter("userid", teacherID);
		list=query.list();
		if(list.size()>0)
		{
			RegisterBean bean=list.get(0);
			Teacher teacher=new Teacher();
			teacher.setClass_id(regid);
			teacher.setUser_id(bean.getRegId());
			teacher.setSub_ids(subjects);
			session.save(teacher);
			transaction.commit();
		return  true;
		}
		return false;
	}
	
	public List getSubjectTeacher(String subid) {
		Transaction transaction=null;
		Session session=null;
		List<RegisterBean> list=null;
		List<Teacher> list2=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query=session.createQuery("select user_id from Teacher where sub_ids like :sub_ids");
		query.setParameter("sub_ids", "%,"+subid+",%");
		list=query.list();
		
		return list;
	}
}
