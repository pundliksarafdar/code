package com.classapp.db.subject;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

public class SubjectDb {

	public String updateDb(Subjects subjects,int classid){
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(subjects);
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
	
	
	public String updateclasssubDb(ClassSubjects subjects){
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(subjects);
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
	
public List getSubjects(List subids) {
		
		Session session = null;
		Transaction transaction = null;
		List batchList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select subjectName from Subjects where subjectCode in :subids");
			query.setParameterList("subids", subids);
			batchList = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return batchList;
	}

public List getSubjectID(String subname) {
	
	Session session = null;
	Transaction transaction = null;
	List subidList = null;
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("select subjectCode from Subjects where  subjectName = :subname");
		query.setParameter("subname", subname);
		subidList = query.list();
		
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}
	return subidList;
}
	
	
	public List<Subjects> getSubject(int queryType,String queryStr,String[] params,String[] paramValues){
		Session session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery(queryStr);
		
		//Get all subject of regId
		if(0==queryType){
			query.setParameter(params[0],paramValues[0]);
		}
		return query.list();
	}
	
	public List<ClassSubjects> getAllClassSubjectcodes(int queryType,String queryStr,String[] params,String[] paramValues){
		Session session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery(queryStr);
		
		//Get all subject of regId
		if(0==queryType){
			query.setParameter(params[0],Integer.parseInt(paramValues[0]));
		}
		return query.list();
	}
	
	public List<ClassSubjects> getClassSubject(int queryType,String queryStr,String[] params,String[] paramValues){
		Session session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery(queryStr);
		
		//Get all subject of regId
		if(0==queryType){
			query.setParameter(params[0],Integer.parseInt(paramValues[0]));
			query.setParameter(params[1],Integer.parseInt(paramValues[1]));
		}
		return query.list();
	}
	
}
