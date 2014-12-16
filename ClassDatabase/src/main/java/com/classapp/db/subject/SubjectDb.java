package com.classapp.db.subject;

import java.util.List;
import com.classapp.db.subject.Subject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.Schedule.Schedule;
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
	public String updateDb(Subject subjects){
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
	
	/*public String updateclasssubDb(ClassSubjects subjects){
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
	}*/
	
public List getSubjects(List subids) {
		
		Session session = null;
		Transaction transaction = null;
		List batchList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Subjects where subjectId in :subids");
			query.setParameterList("subids", subids);
			batchList = query.list();
			
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
		return batchList;
	}

public List getSubjectID(String subname) {
	
	Session session = null;
	Transaction transaction = null;
	List subidList = null;
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("select subjectId from Subjects where  subjectName = :subname");
		query.setParameter("subname", subname);
		subidList = query.list();
		
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
	return subidList;
}
	
public String getschedulesubject(int subjectid) {
	Session session = null;
	Transaction transaction = null;
	List<String> subidList = null;
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("select subjectName from Subjects where  subjectId = :subjectCode");
		query.setParameter("subjectCode", subjectid);
		subidList = query.list();
		
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}
	return subidList.get(0);
}
	
	public List<Subject> getSubject1(int queryType,String queryStr,String[] params,String[] paramValues){
		Query query =null;
		Session session =null;
		try{
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		query = session.createQuery(queryStr);
		
		//Get all subject of regId
		if(0==queryType){
			query.setParameter(params[0],paramValues[0]);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			
		}
		return query.list();
	}
	
	/*public List<ClassSubjects> getAllClassSubjectcodes(int queryType,String queryStr,String[] params,String[] paramValues){
		Query query =null;
		Session session =null;
		try{
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		query = session.createQuery(queryStr);
		
		//Get all subject of regId
		if(0==queryType){
			query.setParameter(params[0],Integer.parseInt(paramValues[0]));
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(null!=session){
				session.close();
			}
		}
		return query.list();
	}
	
	public List<ClassSubjects> getClassSubject(int queryType,String queryStr,String[] params,String[] paramValues){
		Query query =null;
		Session session =null;
		try{
				session = HibernateUtil.getSessionfactory().openSession();
				session.beginTransaction();
				query = session.createQuery(queryStr);
				
				//Get all subject of regId
				if(0==queryType){
					query.setParameter(params[0],Integer.parseInt(paramValues[0]));
					query.setParameter(params[1],Integer.parseInt(paramValues[1]));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				
			}
		return query.list();
	}*/
	public List<Subject> getSubject(int queryType,String queryStr,String[] params,String[] paramValues){
		Query query =null;
		Session session =null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			session.beginTransaction();
			query = session.createQuery(queryStr);
			
			//Get all subject of regId
			if(0==queryType){
				query.setParameter(params[0],Integer.parseInt(paramValues[0]));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(null!=session){
				session.close();
			}
		}
		return query.list();
	}
	
	public Subject retrive(int sub_id){
		
			Session session = null;
			Transaction transaction = null;
			Object queryResult=null;
			String queryString="from Subject where sub_id = :sub_id";
			try{
				session = HibernateUtil.getSessionfactory().openSession();
				transaction = session.beginTransaction();
				Query query = session.createQuery(queryString);
				query.setInteger("sub_id", sub_id);  
				queryResult = query.uniqueResult();
				transaction.commit();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
			
			return (Subject)queryResult;
		}
	
	public List<Subject> retrivesublist(int institute_id){
		
		Session session = null;
		Transaction transaction = null;
		List<Subject> queryResult=null;
		String queryString="from Subject where institute_id in :institute_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("institute_id", institute_id);  
			queryResult = query.list();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return queryResult;
	}
	
	public Boolean modifySubject(Subject subject ) {
		Session session = null;
		Transaction transaction = null;
		List<Subject> queryResult=null;
		String queryString="from Subject where subjectName = :subjectName and institute_id=:institute_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("subjectName", subject.getSubjectName()); 
			query.setParameter("institute_id", subject.getInstitute_id()); 
			queryResult = query.list();
			if(queryResult.size()>0){
				return false;
			}else {
			session.saveOrUpdate(subject);	
			}
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return true;
		
	}
	
	public boolean deleteSubject(int subid) {
		Session session = null;
		Transaction transaction = null;
		List<Subject> queryResult=null;
		String queryString="delete Subject where subjectId = :subjectId";
	//	String queryString1="delete ClassSubjects where sub_id = :subjectId";
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
		/*	Query query = session.createQuery(queryString1);
			query.setParameter("subjectId", subid);  
			 query.executeUpdate();
		*/	Query query = session.createQuery(queryString);
			query.setParameter("subjectId", subid);
			 query.executeUpdate();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return true;
	}
}
