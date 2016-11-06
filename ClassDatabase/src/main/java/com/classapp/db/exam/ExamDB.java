package com.classapp.db.exam;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.internal.ast.tree.OrderByClause;

import com.classapp.db.question.Questionbank;
import com.classapp.persistence.HibernateUtil;

public class ExamDB {

	public int saveExam(Exam exam) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(exam);
		transaction.commit();
		session.close();
		return  exam.getExam_id();

	}
	
	public List<Exam> getExamList(int inst_id) {
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(Exam.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		List<Exam> examList = criteria.list();
		session.close();
		return  examList;	
	}
	
	public List<Exam> getExamList(int inst_id,List<Integer> examIds) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(Exam.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.in("exam_id", examIds);
		criteria.add(criterion);
		List<Exam> examList = criteria.list();
		session.close();
		return  examList;	
	}
	
	public boolean deleteExam(int inst_id,int exam_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			Query query = session.createQuery("delete from Exam where inst_id = :inst_id and exam_id = :exam_id ");
			query.setParameter("inst_id", inst_id);
			query.setParameter("exam_id", exam_id);
			query.executeUpdate();
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
		return  true;

	}
	
	public boolean validateExam(int inst_id,String exam_name) {
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		try{
			Query query = session.createQuery("select exam_name from Exam where inst_id = :inst_id and exam_name = :exam_name ");
			query.setParameter("inst_id", inst_id);
			query.setParameter("exam_name", exam_name);
			List list = query.list();
			if(list.size()>0){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return  false;

	}
	
}
