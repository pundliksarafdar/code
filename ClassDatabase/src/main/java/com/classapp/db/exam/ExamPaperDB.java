package com.classapp.db.exam;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.classapp.persistence.HibernateUtil;

public class ExamPaperDB {
	public int saveExamPaper(Exam_Paper exam_Paper) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(exam_Paper);
		transaction.commit();
		if(session!=null){
			session.close();
		}
		return  exam_Paper.getExam_paper_id();

	}
	
	public boolean deleteExamPaper(Exam_Paper exam_Paper) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Exam_Paper where inst_id = :inst_id and div_id = :div_id and exam_id = :exam_id and exam_paper_id = :exam_paper_id ");
			query.setParameter("inst_id", exam_Paper.getInst_id());
			query.setParameter("div_id", exam_Paper.getDiv_id());
			query.setParameter("exam_id", exam_Paper.getExam_id());
			query.setParameter("exam_paper_id", exam_Paper.getExam_paper_id());
			query.executeUpdate();
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
	
	public boolean updateExamPaper(Exam_Paper exam_Paper) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update Exam_Paper set marks = :marks , duration = :duration," +
					"header_id = :header_id, question_paper_id = :question_paper_id,modified_by = :modified_by," +
					"modified_dt = :modified_dt  where inst_id = :inst_id and div_id = :div_id and exam_id = :exam_id and exam_paper_id = :exam_paper_id ");
			query.setParameter("inst_id", exam_Paper.getInst_id());
			query.setParameter("div_id", exam_Paper.getDiv_id());
			query.setParameter("exam_id", exam_Paper.getExam_id());
			query.setParameter("exam_paper_id", exam_Paper.getExam_paper_id());
			query.setParameter("marks", exam_Paper.getMarks());
			query.setParameter("duration", exam_Paper.getDuration());
			query.setParameter("header_id", exam_Paper.getHeader_id());
			query.setParameter("question_paper_id", exam_Paper.getQuestion_paper_id());
			query.setParameter("modified_by", exam_Paper.getModified_by());
			query.setParameter("modified_dt", exam_Paper.getModified_dt());
			query.executeUpdate();
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
	
	public List<Integer> getDistinctExams(int inst_id,int div_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(Exam_Paper.class).setProjection(
			    Projections.distinct(Projections.projectionList().add(Projections.property("exam_id"), "exam_id")));
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		 criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		List<Integer> examList = criteria.list();
		transaction.commit();
		session.close();
		return  examList;	
	}
	
	public List<Exam_Paper> getExamPapers(int inst_id,int div_id,int exam_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(Exam_Paper.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("exam_id", exam_id);
		criteria.add(criterion);
		List<Exam_Paper> examList = criteria.list();
		transaction.commit();
		session.close();
		return  examList;	
	}

}
