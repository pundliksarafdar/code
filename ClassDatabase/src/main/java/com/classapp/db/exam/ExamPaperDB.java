package com.classapp.db.exam;

import java.util.List;

import org.hibernate.Criteria;
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
		session.close();
		return  exam_Paper.getExam_paper_id();

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
