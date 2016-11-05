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
import com.classapp.utils.Constants.CALENDER_VIEW;
import com.classapp.utils.Constants.SYLLABUS_STATE;
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
	
	public List<SyllabusBean> getSyllabus(int year,int month,int day,int instId,List<Integer> classId,List<Integer> subId,List<Integer> batchId,List<Integer> teacherId,String view){
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(SyllabusBean.class);
		Criterion criterion = Restrictions.sqlRestriction("YEAR(date) = ?",year,IntegerType.INSTANCE);
		criteria.add(criterion);
		if(view.equalsIgnoreCase(CALENDER_VIEW.MONTH.toString()) || view.equalsIgnoreCase(CALENDER_VIEW.DAY.toString())){
			criterion = Restrictions.sqlRestriction("MONTH(date) = ?",month,IntegerType.INSTANCE);
			criteria.add(criterion);
		}
		if(view.equalsIgnoreCase(CALENDER_VIEW.DAY.toString())){
			criterion = Restrictions.sqlRestriction("DAY(date) = ?",day,IntegerType.INSTANCE);
			criteria.add(criterion);
		}
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
	public boolean updateSyllabus(long id,int instId,int classId,int subId,int regId,String syllabus,Date date,String teacherStatus){
		SyllabusPlannerDb syllabusPlannerDb = new SyllabusPlannerDb();
		List<SyllabusBean> syllabusBeans = syllabusPlannerDb.getSyllabus(id, instId, classId, subId, regId);
		if(syllabusBeans.size()<1){
			return false;
		}else{
			SyllabusBean syllabusBean = new SyllabusBean();
			syllabusBean = syllabusBeans.get(0);
			Date todayDateOnly = getTodaysDate();
			if(syllabusBean.getDate().compareTo(todayDateOnly)<0 && !SYLLABUS_STATE.EDITABLE.toString().equalsIgnoreCase(syllabusBean.getStatus())){
				return false;
			}
			syllabusBean.setSyllabus(syllabus);
			syllabusBean.setDate(date);
			syllabusBean.setTeacherStatus(teacherStatus);
			syllabusPlannerDb.saveSyllabusPlanner(syllabusBean);
		}
		return true;
		
	}
	
	public Date getTodaysDate(){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime( date );
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
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
	
	public void deleteSyllabus(Integer teacherId,Integer subjectId,Integer division,Integer batch,Integer instId){
		String mysqlStr = "delete from SyllabusBean where instId =:instituteId  and teacherId=:teacherId or teacherId=null "
				+ "and classId=:division or classId=null "
				+ "and subjectId=:subjectId or subjectId=null";
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery(mysqlStr);
		query.setParameter("instituteId", instId);
		query.setParameter("subjectId", division);
		query.setParameter("teacherId", teacherId);
		query.setParameter("division", division);
		query.executeUpdate();
		if(session!=null){session.close();}
		
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
		db.deleteSyllabus(210, null, null, null, 190);
	}
}
