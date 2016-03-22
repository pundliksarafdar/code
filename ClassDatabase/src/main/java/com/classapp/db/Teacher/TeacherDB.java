package com.classapp.db.Teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.register.RegisterBean;
import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Schedule.ScheduleDB;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDetails;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.SubjectDb;
import com.classapp.persistence.HibernateUtil;


public class TeacherDB {

	public Boolean isTeacherRegistered(int teacherID) {
		Transaction transaction = null;
		Session session = null;
		List<RegisterBean> list = null;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session
					.createQuery("from RegisterBean where regId=:teacherID and role=:role");
			query.setParameter("teacherID", teacherID);
			query.setParameter("role", 2);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public Boolean isTeacherExists(int teacherID,int regid) {
		Transaction transaction=null;
		Session session=null;
		List<RegisterBean> list=null;
		List<Teacher> list2=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
			Teacher teacher=new Teacher();
			teacher.setClass_id(regid);
			teacher.setUser_id(teacherID);
			Query query=session.createQuery("from Teacher where user_id=:userid and class_id=:regid");
			query.setParameter("userid", teacherID);
			query.setParameter("regid", regid);
			list2=query.list();
				
			if (list2.size()>0) {
				return true;
			}else{
				
				return false;
			}
		
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return false;
	}
	
	
	public Boolean add(int teacherIID,int regid,String subjects,String suffix) {
		Transaction transaction=null;
		Session session=null;
		List<RegisterBean> list=null;
		List<Teacher> list2=null;
		try{
			session=HibernateUtil.getSessionfactory().openSession();
			transaction=session.beginTransaction();
			
			//	RegisterBean bean=list.get(0);
				Teacher teacher=new Teacher();
				teacher.setClass_id(regid);
				teacher.setUser_id(teacherIID);
				teacher.setSub_ids(subjects);
				teacher.setSuffix(suffix);
				session.save(teacher);
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
	
	public List getSubjectTeacher(String subid) {
		Transaction transaction = null;
		Session session = null;
		List<RegisterBean> list = null;
		List<Teacher> list2 = null;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session
					.createQuery("select user_id from Teacher where sub_ids like :sub_ids");
			query.setParameter("sub_ids", "%," + subid + ",%");
			list = query.list();
			// session.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}

		return list;
	}

	public List getSubjectTeacher(String subid,int regId) {
		Transaction transaction=null;
		Session session=null;
		List list=null;
		List<Teacher> list2=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query=session.createQuery("from Teacher where (sub_ids like :sub_id1 or sub_ids like :sub_id2 or sub_ids like :sub_id3 or sub_ids=:sub_id4 ) and class_id=:regId");
		query.setParameter("sub_id1", "%,"+subid+",%");
		query.setParameter("sub_id2", subid+",%");
		query.setParameter("sub_id3", "%,"+subid);
		query.setParameter("sub_id4", subid);
		query.setParameter("regId", regId);
		list=query.list();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
		return list;
	}
	
	public List<TeacherDetails> getAllTeachersFromClass(int class_id){
		ArrayList<TeacherDetails> teachers=new ArrayList<TeacherDetails>();
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from Teacher where class_id=:class_id";
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("class_id", class_id);  
			queryResultList = query.list();
			transaction.commit();
			Iterator itr= queryResultList.iterator();
			
			while(itr.hasNext()){
				Teacher entry= (Teacher)itr.next();
				TeacherDetails teacherDetails= new TeacherDetails();
				teacherDetails.setTeacherId(entry.getUser_id());
				teacherDetails.setSubjectIds(entry.getSub_ids());
				teacherDetails.setTeacherBean(getTeacherDetailsFromID(entry.getUser_id()));
				if(entry.getSuffix()!=null){
				teacherDetails.setSuffix(entry.getSuffix());
				}else{
					teacherDetails.setSuffix("");	
				}
				if(!entry.getSub_ids().equals("")){
				teacherDetails.setSubjects(getAssignedSubjects(entry.getSub_ids().split(",")));
				}else{
					teacherDetails.setSubjects(new ArrayList<Subject>());
				}
				teachers.add(teacherDetails);
			}
			return teachers;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return null;
	}
	
	public RegisterBean getTeacherDetailsFromID(int teacher_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		
		String queryString="from RegisterBean where regId = :regId";
			
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("regId", teacher_id);
			queryResultList = query.list();
			transaction.commit();
			if(queryResultList.size()==1){
				return (RegisterBean) queryResultList.get(0);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return null;
	}
	
	public boolean updateDb(Teacher teacher){
		boolean status=true;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(teacher);
			transaction.commit();
		}catch(Exception e){
			status = false;
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
	
	public boolean deleteTeacher(int user_id, int class_id) {
		
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		String queryString="from Teacher where user_id = :user_id and class_id = :class_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("user_id", user_id);
			query.setInteger("class_id", class_id);  
					
				Teacher teacher=(Teacher)query.uniqueResult();
				if(teacher!=null){
					ScheduleDB db=new ScheduleDB();
					db.deleteSchedulerelatedtoteacher(user_id, class_id);
					session.delete(teacher);
					status=true;					
				}
			
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return status;
	}
	
public Teacher getTeacher(int user_id, int class_id) {
		
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		String queryString="from Teacher where user_id = :user_id and class_id=:class_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("user_id", user_id);
			query.setInteger("class_id", class_id);  
					
				Teacher teacher=(Teacher)query.uniqueResult();
				if(teacher!=null){
					return teacher;				
				}
			
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return null;
	}
	
	private List<Subject> getAssignedSubjects(String[] sub_ids){
		ArrayList<Subject> subjects=new ArrayList<Subject>();
		SubjectDb subjectDb=new SubjectDb();
		for (String sub_Id : sub_ids) {
			Subject subject=subjectDb.retrive(Integer.parseInt(sub_Id));
			subjects.add(subject);
		}
		return subjects;
	}
	
	public List getTeachersClass(int regID){
		
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List classids=null;
		String queryString="select class_id from Teacher where user_id = :user_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("user_id", regID);
				classids=query.list();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return classids;
	}
	
	public List<Teacher> getteacherrelatedtosubject(String subid) {
		
		Transaction transaction=null;
		Session session=null;
		List<Teacher> list=new ArrayList<Teacher>();
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query=session.createQuery(" from Teacher where (sub_ids like :sub_id1 or sub_ids like :sub_id2 or sub_ids like :sub_id3 or sub_ids = :sub_id4)");
		query.setParameter("sub_id1", "%,"+subid+",%");
		query.setParameter("sub_id2", subid+",%");
		query.setParameter("sub_id3", "%,"+subid);
		query.setParameter("sub_id4", subid);
	//	query.setParameter("regId", regId);
		list=query.list();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
		return list;
	}
	
	public String getTeachersPrefix(int teacherID,int regID){
		Transaction transaction=null;
		Session session=null;
		List<String> list=new ArrayList<String>();
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query=session.createQuery("select suffix from Teacher where user_id=:teacherID and class_id=:regID");
		query.setParameter("teacherID", teacherID);
		query.setParameter("regID", regID);
		
	//	query.setParameter("regId", regId);
		list=query.list();
		if(list.size()>0){
			return list.get(0);
		}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
		return "";
	}
	
	public Integer getTeacherCount(int regID){
		Transaction transaction=null;
		Session session=null;
		List<Long> list=new ArrayList<Long>();
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query=session.createQuery("select count(*) from Teacher where class_id=:class_id");
		query.setParameter("class_id", regID);
		list=query.list();
		if(list.size()>0){
			return list.get(0).intValue();
		}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
		return list.get(0).intValue();
	}
	
	public List<Subject> getTeacherSubjects(int teacherID,int classID){
		Transaction transaction=null;
		Session session=null;
		List<Subject> list=new ArrayList<Subject>();
		List<String> subjectids=new ArrayList<String>();
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query=session.createQuery("select sub_ids from Teacher where user_id=:teacher_id and class_id=:class_id");
		query.setParameter("class_id", classID);
		query.setParameter("teacher_id", teacherID);
		subjectids=query.list();
		List<Integer> subids=new ArrayList<Integer>();
		if(subjectids.size()>0){
			String[] ids= subjectids.get(0).split(",");
			int i=0;
			while(i<ids.length){
				subids.add(Integer.parseInt(ids[i]));
				i++;
			}
			query=session.createQuery("from Subject where subjectId in :subjectId");
			query.setParameterList("subjectId", subids);
			list=query.list();
		}
		
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
		return list;
	}
	
}
