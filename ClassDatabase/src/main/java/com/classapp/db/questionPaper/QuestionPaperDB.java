package com.classapp.db.questionPaper;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.question.Questionbank;
import com.classapp.persistence.HibernateUtil;

public class QuestionPaperDB {

	public int save(QuestionPaper questionPaper) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(questionPaper);
		transaction.commit();
		System.out.println("Quesbank ID:-"+questionPaper.getPaper_id());
		if(session!=null){
			session.close();
		}
		return  questionPaper.getPaper_id();
	}
	
	public List<QuestionPaper> getQuestionPaperList(int div_id,int inst_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(QuestionPaper.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		List<QuestionPaper> questionPaperList = criteria.list();
		if(session!=null){
			session.close();
		}
		return questionPaperList;
	}
	
	public boolean deleteQuestion(int paper_id,int inst_id,int div_id) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("delete from QuestionPaper where  inst_id = :inst_id and div_id=:div_id and sub_id=:sub_id and paper_id = :paper_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("paper_id", paper_id);
		transaction.commit();
		query.executeUpdate();
		}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  true;

	}
	
	public boolean verifyPaperName(int div_id,int inst_id,String paper_description) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(QuestionPaper.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("paper_description", paper_description);
		criteria.add(criterion);
		List<QuestionPaper> questionPaperList = criteria.list();
		if(session!=null){
			session.close();
		}
		if(questionPaperList.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean verifyUpdatePaperName(int div_id,int inst_id,String paper_description,int paper_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(QuestionPaper.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("paper_description", paper_description);
		criteria.add(criterion);
		criterion = Restrictions.eq("paper_id", paper_id);
		criteria.add(criterion);
		List<QuestionPaper> questionPaperList = criteria.list();
		if(session!=null){
			session.close();
		}
		if(questionPaperList.size()>0){
			return true;
		}
		return false;
	}
}
