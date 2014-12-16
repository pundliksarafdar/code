package com.classapp.db.subject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

public class GetSubject {
	
	public List getSubjects(String subname,int regID){
		Session session = null;
		Transaction transaction = null;
		List SubList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("Select sub.subjectId from Subjects sub where sub.subjectName =:subname and sub.institute_id=:class_id");
			query.setParameter("subname", subname);
			query.setParameter("class_id", regID);
			SubList = query.list();
			
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
		
		return SubList;
		
	}
	public List<Subject> getSubjects(int regId){
		List<com.classapp.db.subject.Subject> subjectList = null;
		List<Subject> subjects = new ArrayList<Subject>();
		String[] params = {"regId"};
		String[] paramsValue = {regId+""};
		SubjectDb subjectDb = new SubjectDb();
		String getAllSubjectsQuery = "from Subjects where regId =:regId";
		subjectList = subjectDb.getSubject(0,getAllSubjectsQuery, params, paramsValue);
		try {
			for(int i=0;i<subjectList.size();i++){
				Subject subject = new Subject();
				BeanUtils.copyProperties(subject, subjectList.get(i));
				subjects.add(subject);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return subjects;
	}
	/*public List getAllClassSubjectcodes(String suggestion){
		List subjectList = null;
		List<String> subjects = new ArrayList<String>();
		String[] params = {"regId"};
		String[] paramsValue = {suggestion};
		SubjectDb subjectDb = new SubjectDb();
		String getAllSubjectsQuery = "select sub_id from ClassSubjects where class_id = :regId";
		subjectList = subjectDb.getAllClassSubjectcodes(0,getAllSubjectsQuery, params, paramsValue);
		
		try {
			for(int i=0;i<subjectList.size();i++){
				String subject="";
				BeanUtils.copyProperties(subject, "");
				
			}
			subjects.add("Chemistry");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		subjects.add("Chemistry");
		subjects.add("Physics");
		return subjectList;
	}*/
	
	
	public List getAllClassSubjectsNames(int regID){
		Session session = null;
		Transaction transaction = null;
		List SubList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Subjects where institute_id = :institute_id");
			query.setParameter("institute_id", regID);
			SubList = query.list();
			
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
		
		return SubList;
	}
		public boolean isSubjectExists(int regId,String subject){
		boolean isSubjectExist =false;
		List<Subject> subjectsList = getSubjects(regId);
		
		for (int i = 0; i < subjectsList.size(); i++) {
			Subject subjects = subjectsList.get(i);
			if(subjects.getSubjectName().equalsIgnoreCase(subject)){
				isSubjectExist = true;
				break;
			}else{
				isSubjectExist = false;
			}
		}
		return isSubjectExist;
	}
	
	public String isSubjectExists(String subject,int regID){
		String isSubjectExist ="false";
		List subjectsList = getSubjects(subject,regID);
		
		/*for (int i = 0; i < subjectsList.size(); i++) {
			Subject subjects = subjectsList.get(i);
			if(subjects.getSubjectName().equalsIgnoreCase(subject)){
				isSubjectExist = true;
				break;
			}else{
				isSubjectExist = false;
			}
		}*/
		if(subjectsList.size()>0)
		{
			isSubjectExist="true";
		}
		return isSubjectExist;
	}
	
	public List getSubjectid(String subname,int classid){
		Session session = null;
		Transaction transaction = null;
		List SubList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Subjects where subjectName =:subname and institute_id=:class_id");
			query.setParameter("subname", subname);
			query.setParameter("class_id", classid);
			SubList = query.list();
			
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
		
		return SubList;
		
	}
}
