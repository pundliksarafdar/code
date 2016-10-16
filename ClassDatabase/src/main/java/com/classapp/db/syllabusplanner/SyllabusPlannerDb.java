package com.classapp.db.syllabusplanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;

import com.classapp.db.fees.Fees;
import com.classapp.persistence.HibernateUtil;
import com.service.beans.SyllabusBean;
import com.service.beans.SyllabusFilterBean;

import javassist.expr.Instanceof;

public class SyllabusPlannerDb {
	public boolean saveSyllabusPlanner(SyllabusBean syllabusBean) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(syllabusBean);
		transaction.commit();
		if(session!=null){
			session.close();
		}
		return  true;

	}
	
	public List<SyllabusBean> getSyllabus(int year,int month,int instId,int classId,int subId,List<String> batchId,int regId){
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(SyllabusBean.class);
		Criterion criterion = Restrictions.sqlRestriction("YEAR(date) = ?",year,IntegerType.INSTANCE);
		criteria.add(criterion);
		criterion = Restrictions.sqlRestriction("MONTH(date) = ?",month,IntegerType.INSTANCE);
		criteria.add(criterion);
		criterion = Restrictions.eq("instId", instId);
		criteria.add(criterion);
		criterion = Restrictions.eq("classId", classId);
		criteria.add(criterion);
		criterion = Restrictions.eq("subjectId", subId);
		criteria.add(criterion);
		
		criterion = Restrictions.eq("teacherId", regId);
		criteria.add(criterion);
		criteria.addOrder(Order.asc("date"));
		List<SyllabusBean> syllabusBeans = criteria.list();
		if(session!=null){session.close();}
		return syllabusBeans;
	}
	
	public List<SyllabusBean> getSyllabus(int year,int month,int day,int instId,List<Integer> classId,List<Integer> subId,List<Integer> batchId,List<Integer> teacherId){
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(SyllabusBean.class);
		Criterion criterion = Restrictions.sqlRestriction("YEAR(date) = ?",year,IntegerType.INSTANCE);
		criteria.add(criterion);
		criterion = Restrictions.sqlRestriction("MONTH(date) = ?",month,IntegerType.INSTANCE);
		criteria.add(criterion);
		criterion = Restrictions.sqlRestriction("DAY(date) = ?",day,IntegerType.INSTANCE);
		criteria.add(criterion);
		criterion = Restrictions.eq("instId", instId);
		criteria.add(criterion);
		
		if(null!=classId && !classId.isEmpty()){
			criterion = Restrictions.in("classId", classId);
			criteria.add(criterion);
		}
		if(null!=subId && !subId.isEmpty()){
			criterion = Restrictions.in("subjectId", subId);
			criteria.add(criterion);
		}
		if(null!=teacherId && !teacherId.isEmpty()){
			criterion = Restrictions.in("teacherId", teacherId);
			criteria.add(criterion);
		}
		criteria.addOrder(Order.asc("date"));
		List<SyllabusBean> syllabusBeans = criteria.list();
		if(session!=null){session.close();}
		return syllabusBeans;
	}
	public List<SyllabusBean> getSyllabus(long id,int instId,int classId,int subId,int teacherId){
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(SyllabusBean.class);
		Criterion criterion = Restrictions.eq("instId", instId);
		criteria.add(criterion);
		criterion = Restrictions.eq("classId", classId);
		criteria.add(criterion);
		criterion = Restrictions.eq("subjectId", subId);
		criteria.add(criterion);
		criterion = Restrictions.eq("teacherId", teacherId);
		criteria.add(criterion);
		criterion = Restrictions.eq("id", id);
		criteria.add(criterion);
		List<SyllabusBean> syllabusBeans = criteria.list();
		if(session!=null){session.close();}
		return syllabusBeans;
	}
	public boolean updateSyllabus(long id,int instId,int classId,int subId,int regId,String syllabus,Date date){
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		List<SyllabusBean> syllabusBeans = syllabusPlannerDb.getSyllabus(id, instId, classId, subId, regId);
		if(syllabusBeans.size()<1){
			return false;
		}else{
			SyllabusBean syllabusBean = new SyllabusBean();
			syllabusBean = syllabusBeans.get(0);
			syllabusBean.setSyllabus(syllabus);
			syllabusBean.setDate(date);
			syllabusPlannerDb.saveSyllabusPlanner(syllabusBean);
		}
		return true;
		
	}
	
	
	public boolean deleteSyllabus(long id,int instId,int classId,int subId,int teacherId){
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		List<SyllabusBean> syllabusBeans = syllabusPlannerDb.getSyllabus(id, instId, classId, subId, teacherId);
		if(syllabusBeans.size()<1){
			return false;
		}
		
		SyllabusBean syllabusBean = syllabusBeans.get(0);
		session.delete(syllabusBean);
		transaction.commit();
		if(session!=null){session.close();}
		return true;
	}
	
	public boolean deleteSyllabus(SyllabusBean syllabusBean){
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.delete(syllabusBean);
		transaction.commit();
		if(session!=null){session.close();}
		return true;
	}
	
	public boolean setStatus(SyllabusBean syllabusBean){
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		List<SyllabusBean> syllabusBeans = syllabusPlannerDb.getSyllabus(syllabusBean.getId(), syllabusBean.getInstId(), syllabusBean.getClassId(), syllabusBean.getSubjectId(), syllabusBean.getTeacherId());
		SyllabusBean syllabusBean2 = syllabusBeans.get(0);
		syllabusBean2.setStatus(syllabusBean.getStatus());
		syllabusPlannerDb.saveSyllabusPlanner(syllabusBean2);
		return true;
		
	}
	
	public List<SyllabusBean> getSyllabusFilter(int instId,List<Integer> classId,List<Integer> subId,List<Integer> teacherId){
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(SyllabusBean.class);
		Criterion criterion = Restrictions.eq("instId", instId);
		criteria.add(criterion);
		if(classId!=null && !classId.isEmpty()){
			criterion = Restrictions.in("classId", classId);
			criteria.add(criterion);
		}
		if(subId!=null && !subId.isEmpty()){
			criterion = Restrictions.in("subjectId", subId);
			criteria.add(criterion);
		}
		if(teacherId!=null && !teacherId.isEmpty()){
			criterion = Restrictions.in("teacherId", teacherId);
			criteria.add(criterion);
		}
		List<SyllabusBean> syllabusBeans = criteria.list();
		if(session!=null){session.close();}
		return syllabusBeans;
	}
	
	public void getFilteredResult(){
		SyllabusPlannerDb db = new SyllabusPlannerDb();
		List<SyllabusBean> list = db.getSyllabusFilter(190, null, null, null);
		Set<Integer>classIds = new TreeSet<Integer>();
		Set<Integer>subjectIds = new TreeSet<Integer>();
		Set<Integer>batchIds = new TreeSet<Integer>();
		Set<Integer>teacherIds = new TreeSet<Integer>();
		for(SyllabusBean syllabusBean:list){
			int classId = syllabusBean.getClassId();
			classIds.add(classId);
			
			int subjectId = syllabusBean.getSubjectId();
			subjectIds.add(subjectId);
			
			/*
			int batchIds = syllabusBean.getBatchId();
			classIds.add(batchIds);
			*/
			
			int teacherId = syllabusBean.getTeacherId();
			teacherIds.add(teacherId);
		}
	}
	
	public List<SyllabusFilterBean> getSyllabusDb(int instituteId){
		String mysqlStr = "select distinct 'teacher' as type,rTable.reg_id as id,concat(rTable.fname,\" \",rTable.lname) as name from regtable rTable,syllabustable sTable "
				+"where rTable.reg_id = sTable.teacherId and sTable.instId=:instituteId "
				+"union select 'division',d.div_id,concat(d.div_name,\" \",d.stream) from division d,syllabustable sTable "
		+"where d.div_id=sTable.classId  and institute_Id=:instituteId "
					+"union select 'subject',sub_id,sub_name from subject sub,syllabustable sTable "
		+"where sub.sub_id=sTable.subjectId and institute_Id=:instituteId "
					+"union select 'batch',batch_id,batch_name from batch bat,syllabustable sTable "
			+"where bat.batch_id=sTable.batchId and class_Id=:instituteId";

		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Query query = session.createSQLQuery(mysqlStr);
		query.setParameter("instituteId", instituteId).setResultTransformer(Transformers.aliasToBean(SyllabusFilterBean.class));
		List list = query.list();
		if(session!=null){session.close();}
		return list;
	}
	
		public void saveTest() {
		SyllabusPlannerDb plannerDb = new SyllabusPlannerDb();
		SyllabusBean syllabusBean = new SyllabusBean();
		syllabusBean.setInstId(190);
		syllabusBean.setBatchId("3");
		syllabusBean.setClassId(2);
		syllabusBean.setDate(new Date());
		syllabusBean.setSubjectId(3);
		syllabusBean.setSyllabus("Syllanus");
		syllabusBean.setTeacherId(1);
		plannerDb.saveSyllabusPlanner(syllabusBean);
	}
	
	public static void main(String[] args) {
		SyllabusPlannerDb db = new SyllabusPlannerDb();
	
	}
}
