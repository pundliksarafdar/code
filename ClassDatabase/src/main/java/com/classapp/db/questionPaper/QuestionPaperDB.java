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
		boolean success = true;
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			session.saveOrUpdate(questionPaper);
		}catch(Exception e){
			success = false;
		}
		transaction.commit();
		System.out.println("Quesbank ID:-"+questionPaper.getPaper_id());
		if(session!=null){
			session.close();
		}
		if(success){	return  questionPaper.getPaper_id();}
		else{	return -1;}
	}
	
	public boolean updateQuestionPaper(QuestionPaper questionPaper) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("update QuestionPaper set "
				+ " marks = :marks,paper_description = :paper_description,modified_by = :modified_by,modified_dt = :modified_dt,compSubjectIds = :compSubjectIds"
				+ "  where  inst_id = :inst_id and div_id=:div_id  and paper_id = :paper_id");
		query.setParameter("inst_id", questionPaper.getInst_id());
		query.setParameter("div_id", questionPaper.getDiv_id());
		query.setParameter("paper_id", questionPaper.getPaper_id());
		query.setParameter("marks", questionPaper.getMarks());
		query.setParameter("paper_description", questionPaper.getPaper_description());
		query.setParameter("modified_by", questionPaper.getModified_by());
		query.setParameter("modified_dt", questionPaper.getModified_dt());
		query.setParameter("compSubjectIds", questionPaper.getCompSubjectIds());
		query.executeUpdate();
		transaction.commit();
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
	
	public List<QuestionPaper> getQuestionPaperList(int div_id,int inst_id,int subject) {
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(QuestionPaper.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("sub_id", subject);
		criteria.add(criterion);
		List<QuestionPaper> questionPaperList = criteria.list();
		if(session!=null){
			session.close();
		}
		return questionPaperList;
	}
	
	public boolean deleteQuestionPaper(int paper_id,int inst_id,int div_id) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("delete from QuestionPaper where  inst_id = :inst_id and div_id=:div_id  and paper_id = :paper_id");
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
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
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
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(QuestionPaper.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("div_id", div_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("paper_description", paper_description);
		criteria.add(criterion);
		criterion = Restrictions.ne("paper_id", paper_id);
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
	
	public boolean deleteQuestionPaperRelatedToClass(int inst_id,int div_id) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("delete from QuestionPaper where  inst_id = :inst_id and div_id=:div_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
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
	
	public List<QuestionPaper> getQuestionPaperListRelatedToExam(int paper_id,int inst_id,int div_id) {
		Session session=null;
		List<QuestionPaper> questionPaperList = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery("select qp  from QuestionPaper qp,Exam_Paper ep "
				+ "where  qp.inst_id = :inst_id and qp.div_id=:div_id  and qp.inst_id = ep.inst_id "
				+ "and qp.div_id = ep.div_id and qp.paper_id = ep.question_paper_id and ep.exam_id = :paper_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("paper_id", paper_id);
		questionPaperList = query.list();
		}catch(Exception e){
		e.printStackTrace();
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  questionPaperList;

	}
	
	public boolean deleteQuestionPaperRelatedToSubject(int inst_id,int sub_id) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("delete from QuestionPaper where  inst_id = :inst_id and (sub_id=:sub_id  or "
				+ "compSubjectIds like :compSubjectIds1 or compSubjectIds like :compSubjectIds2 or compSubjectIds like :compSubjectIds3 or "
				+ "compSubjectIds like :compSubjectIds4)");
		query.setParameter("inst_id", inst_id);
		query.setParameter("sub_id", sub_id);
		query.setParameter("compSubjectIds1", sub_id+",%");
		query.setParameter("compSubjectIds2", "%,"+sub_id);
		query.setParameter("compSubjectIds3", "%,"+sub_id+",%");
		query.setParameter("compSubjectIds4", sub_id+"");
		int row = query.executeUpdate();
		transaction.commit();
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
	
	public boolean checkQuestionPaperAvailability(int paper_id,int inst_id,int div_id,int sub_id) {
		Session session=null;
		List<QuestionPaper> questionPaperList = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery("from QuestionPaper "
				+ "where  inst_id = :inst_id and div_id=:div_id "
				+ " and paper_id = :paper_id and sub_id = :sub_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("paper_id", paper_id);
		query.setParameter("sub_id", sub_id);
		questionPaperList = query.list();
		if(questionPaperList != null){
			if(questionPaperList.size() > 0){
				return true;
			}
		}
		}catch(Exception e){
		e.printStackTrace();
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  false;

	}
}
