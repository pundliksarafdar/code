package com.classapp.db.pattern;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.exam.Exam;
import com.classapp.db.question.Questionbank;
import com.classapp.persistence.HibernateUtil;

public class QuestionPaperPatternDB {
	public int saveQuestionPaperPattern(QuestionPaperPattern questionPaperPattern) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(questionPaperPattern);
		transaction.commit();
		if(session!=null){
			session.close();
		}
		return  questionPaperPattern.getPattern_id();

	}
	
	public boolean verifyPatterName(int inst_id,int div_id,String pattern_name) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(QuestionPaperPattern.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("pattern_name", pattern_name);
		criteria.add(criterion);
		List<QuestionPaperPattern> questionPaperPatternList = criteria.list();
		if(questionPaperPatternList != null){
			if(questionPaperPatternList.size()>0){
				return true;
			}
		}
		if(session!=null){
			session.close();
		}
		return  false;

	}
	
	public boolean verifyUpdatePatterName(int inst_id,int div_id,String pattern_name,int pattern_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(QuestionPaperPattern.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("pattern_name", pattern_name);
		criteria.add(criterion);
		criterion = Restrictions.ne("pattern_id", pattern_id);
		criteria.add(criterion);
		List<QuestionPaperPattern> questionPaperPatternList = criteria.list();
		if(questionPaperPatternList != null){
			if(questionPaperPatternList.size()>0){
				return true;
			}
		}
		if(session!=null){
			session.close();
		}
		return  false;

	}
	
	
	public List<QuestionPaperPattern> getQuestionPaperPatternList(int inst_id,int div_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(QuestionPaperPattern.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		List<QuestionPaperPattern> questionPaperPatternList = criteria.list();
		if(session!=null){
			session.close();
		}
		return  questionPaperPatternList;

	}
	
	public boolean deleteQuestionPaperPatternList(int inst_id,int div_id,int pattern_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from QuestionPaperPattern where inst_id = :inst_id and div_id = :div_id and pattern_id = :pattern_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("pattern_id", pattern_id);
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
}
