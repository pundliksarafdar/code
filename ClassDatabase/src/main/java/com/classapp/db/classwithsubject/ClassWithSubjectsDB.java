package com.classapp.db.classwithsubject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.subject.SubjectDb;
import com.classapp.persistence.HibernateUtil;
import com.classapp.db.subject.Subject;

public class ClassWithSubjectsDB {
	
	public String updateDb(ClassWithSubjects classWithSubjects){
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(classWithSubjects);
			transaction.commit();
		}catch(Exception e){
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
	
	public List<Subject> getSubjects(int classId){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="select sub_id from ClassWithSubjects where class_id = :class_id";
		
		List<Subject> listOfSubject=new ArrayList<Subject>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("class_id", classId);  
			queryResultList = query.list();
			transaction.commit();
			Iterator itr= queryResultList.iterator();
			SubjectDb subjectDb= new SubjectDb();
			List<String> subjectIdList = new ArrayList<String>();
			
			while(itr.hasNext()){
				Integer subjectId= (Integer)itr.next();
				subjectIdList.add(subjectId.toString());
			}
			if(subjectIdList.size()>0)
			{
			listOfSubject =subjectDb.retrive(subjectIdList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return listOfSubject;
	}
	

	public List<Subject> getSubjects(List<String> classId){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="select sub_id from ClassWithSubjects where class_id = :class_id";
		
		List<Subject> listOfSubject=new ArrayList<Subject>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameterList("class_id", classId);  
			queryResultList = query.list();
			transaction.commit();
			Iterator itr= queryResultList.iterator();
			SubjectDb subjectDb= new SubjectDb();
			while(itr.hasNext()){
				Integer entry= (Integer)itr.next();
				if(null!=subjectDb.retrive(entry)){
					listOfSubject.add(subjectDb.retrive(entry));
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return listOfSubject;
	}

}
