package com.classapp.db.subject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Subjects;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.batch.Batch;
import com.classapp.db.exam.Exam;
import com.classapp.db.fees.Fees;
import com.classapp.db.Schedule.Schedule;
import com.classapp.persistence.HibernateUtil;

public class SubjectDb {

	public int updateDb(Subjects subjects,int classid){
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(subjects);
			transaction.commit();
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
		return subjects.getSubjectId();
	}
	
	public String updateDb(Subject subjects){
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(subjects);
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
		List subjectList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Subjects where subjectId in :subids");
			query.setParameterList("subids", subids);
			subjectList = query.list();
			
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
		return subjectList;
	}

public List<Subject> getSubjectList(List subids) {
	
	Session session = null;
	Transaction transaction = null;
	List subjectList = null;
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("from Subject where subjectId in :subids");
		query.setParameterList("subids", subids);
		subjectList = query.list();
		
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
	return subjectList;
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
		
	}finally{
		if(null!=session){
			session.close();
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
		}finally{
			if(null!=session){
				session.close();
			}
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
			if(null!=session){
				session.close();
			}	
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
			Object queryResult=null;
			String queryString="from Subject where sub_id = :sub_id";
			try{
				session = HibernateUtil.getSessionfactory().openSession();
				Query query = session.createQuery(queryString);
				query.setInteger("sub_id", sub_id);  
				queryResult = query.uniqueResult();
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
		List<Subject> queryResult=null;
		String queryString="from Subject where institute_id in :institute_id order by subjectName asc";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery(queryString);
			query.setParameter("institute_id", institute_id);  
			queryResult = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}	
		
		return queryResult;
	}
	
public Subject getSubject(int institute_id,int sub_id){
		
		Session session = null;
		Transaction transaction = null;
		List<Subject> queryResult=null;
		String queryString="from Subject where institute_id = :institute_id and subjectId = :subjectId";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("institute_id", institute_id);  
			query.setParameter("subjectId", sub_id); 
			queryResult = query.list();
			if(queryResult != null){
				if(queryResult.size() > 0){
					return queryResult.get(0);
				}
			}
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}	
		
		return null;
	}
	
public List<Subject> recentlyaddedsubfirst(int institute_id){
		
		Session session = null;
		Transaction transaction = null;
		List<Subject> queryResult=null;
		String queryString="from Subject where institute_id in :institute_id order by subjectId desc";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("institute_id", institute_id);  
			queryResult = query.list();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}	
		
		return queryResult;
	}
	
	public Boolean modifySubject(Subject subject ) {
		Session session = null;
		Transaction transaction = null;
		List<Subject> queryResult=null;
		String queryString="from Subject where subjectName = :subjectName and institute_id=:institute_id and subjectId != :subjectId";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("subjectName", subject.getSubjectName()); 
			query.setParameter("institute_id", subject.getInstitute_id()); 
			query.setParameter("subjectId", subject.getSubjectId()); 
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
	
	public List<Subject> getSubjectRelatedToDiv(int div_id,int inst_id) {
		Session session = null;
		Transaction transaction = null;
		List<Batch> queryResult=null;
		List<Integer> subjectIds=null;
		List<Subject> subjectlist=null;
		String queryString="from Batch where class_id = :inst_id and div_id=:div_id";
	
		try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery(queryString);
		query.setParameter("inst_id", inst_id);	
		query.setParameter("div_id", div_id);	
		queryResult=query.list();
		if(queryResult!=null){
			subjectIds=new ArrayList<Integer>();
			for (int i = 0; i < queryResult.size(); i++) {
				String ids[]=queryResult.get(i).getSub_id().split(",");
				for (int j = 0; j < ids.length; j++) {
					if(!subjectIds.contains(ids[j])){
						subjectIds.add(Integer.parseInt(ids[j]));
					}
				}
			}
			if(subjectIds.size()>0){
				subjectlist=getSubjectList(subjectIds);
				subjectIds=new ArrayList<Integer>();
				for (Iterator iterator = subjectlist.iterator(); iterator.hasNext();) {
					Subject subject = (Subject) iterator.next();
					if("1".equals(subject.getSub_type())){
						String Ids[] = subject.getCom_subjects().split(",");
						if(Ids.length > 0){
							for (String string : Ids) {
								subjectIds.add(Integer.parseInt(string));
							}
						}
					}
					if(!"1".equals(subject.getSub_type())){
					subjectIds.add(subject.getSubjectId());
					}
				}
				subjectlist=getSubjectList(subjectIds);
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return subjectlist;
	}
	
	public List<Subject> getSubjectRelatedToDivForExam(int div_id,int inst_id) {
		Session session = null;
		Transaction transaction = null;
		List<Batch> queryResult=null;
		List<Integer> subjectIds=null;
		List<Subject> subjectlist=null;
		String queryString="from Batch where class_id = :inst_id and div_id=:div_id";
	
		try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery(queryString);
		query.setParameter("inst_id", inst_id);	
		query.setParameter("div_id", div_id);	
		queryResult=query.list();
		if(queryResult!=null){
			subjectIds=new ArrayList<Integer>();
			for (int i = 0; i < queryResult.size(); i++) {
				String ids[]=queryResult.get(i).getSub_id().split(",");
				for (int j = 0; j < ids.length; j++) {
					if(!subjectIds.contains(ids[j])){
						subjectIds.add(Integer.parseInt(ids[j]));
					}
				}
			}
			if(subjectIds.size()>0){
			subjectlist=getSubjectList(subjectIds);
			subjectIds=new ArrayList<Integer>();
			for (Iterator iterator = subjectlist.iterator(); iterator.hasNext();) {
				Subject subject = (Subject) iterator.next();
				if("1".equals(subject.getSub_type())){
					String Ids[] = subject.getCom_subjects().split(",");
					if(Ids.length > 0){
						for (String string : Ids) {
							subjectIds.add(Integer.parseInt(string));
						}
					}
				}
				subjectIds.add(subject.getSubjectId());
			}
			subjectlist=getSubjectList(subjectIds);
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return subjectlist;
	}
	
	public List<Topics> GetSubjectTopics(int inst_id,int sub_id,int div_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(Topics.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		
		criterion = Restrictions.eq("sub_id", sub_id);
		criteria.add(criterion);
	
		
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
	
		
		List<Topics> topicList = criteria.list();
		transaction.commit();
		session.close();
		return  topicList;
	}
	
	public boolean deleteTopics(int inst_id,int sub_id,int div_id,int topicid) {
		Session session = null;
		Transaction transaction = null;
		List<Subject> queryResult=null;
		String queryString="delete Topics where inst_id = :inst_id and sub_id=:sub_id and div_id=:div_id"
				+ " and topic_id = :topic_id";	
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
		Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("sub_id", sub_id);
			query.setParameter("div_id", div_id);
			query.setParameter("topic_id", topicid);
			 query.executeUpdate();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return  true;
	}
	
	public boolean deleteTopicsrelatedToSubject(int inst_id,int sub_id) {
	
		Session session = null;
		Transaction transaction = null;
		List<Subject> queryResult=null;
		String queryString="delete Topics where inst_id = :inst_id and sub_id=:sub_id";	
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
		Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("sub_id", sub_id);
			 query.executeUpdate();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return  true;
	}
	
	public boolean deleteTopicsrelatedToDivision(int inst_id,int div_id) {
		Session session = null;
		Transaction transaction = null;
		List<Subject> queryResult=null;
		String queryString="delete Topics where inst_id = :inst_id and div_id=:div_id";	
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
		Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			 query.executeUpdate();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return  true;
	}
	
	public boolean saveorupdateTopic(Topics topic) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(topic);
		transaction.commit();
		session.close();
		return  true;
	}
	
	public int getNextTopicID(int inst_id,int div_id,int sub_id) {
		Transaction transaction=null;
		Session session=null;
		List<Integer> list = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select max(topic_id)+1 from Topics where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("sub_id", sub_id);
			list = query.list();
			if(list!=null){
				return list.get(0);
			}
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
	return 1;
	}
	
	public boolean isTopicExists(int inst_id,int div_id,int sub_id,String topic_name) {
		Transaction transaction=null;
		Session session=null;
		List<Integer> list = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Topics where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and topic_name=:topic_name");
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("sub_id", sub_id);
			query.setParameter("topic_name", topic_name);
			list = query.list();
			if(list!=null){
				if (list.size()>0) {
					return true;	
				}
			}
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
	return false;
	}
	
	public boolean isEditTopicExists(int inst_id,int div_id,int sub_id,String topic_name,int topicid) {
		Transaction transaction=null;
		Session session=null;
		List<Integer> list = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Topics where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and topic_name=:topic_name and topic_id != :topic_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("sub_id", sub_id);
			query.setParameter("topic_name", topic_name);
			query.setParameter("topic_id", topicid);
			
			list = query.list();
			if(list!=null){
				if (list.size()>0) {
					return true;	
				}
			}
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
	return false;
	}
	
	public List<Subject> getCompositeSubjectrelatedtoSubject(int inst_id,String subjectid) {
		Session session = null;
		Transaction transaction = null;
		List<Subject> subjectList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Subject where (com_subjects LIKE :sub_id1 OR "
											+ "com_subjects LIKE :sub_id2 OR com_subjects LIKE :sub_id3 OR "
											+ "com_subjects = :sub_id4) and institute_id = :class_id");
			query.setParameter("sub_id1", subjectid+",%");
			query.setParameter("sub_id2", "%,"+subjectid);
			query.setParameter("sub_id3", "%,"+subjectid+",%");
			query.setParameter("sub_id4", subjectid);
			query.setParameter("class_id", inst_id);
			subjectList = query.list();
			
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
		return subjectList;
	}

	public List<Subject> retrive(List<Integer> split) {
		Session session = null;
		List<Subject> subjects = new ArrayList<Subject>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Criteria criteria = session.createCriteria(Subject.class);
			Criterion criterion = Restrictions.in("subjectId",split);
			criteria.add(criterion);
			subjects = criteria.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return subjects;
	}
}
