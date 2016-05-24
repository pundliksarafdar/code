package com.classapp.db.exam;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.subject.Subject;
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
	
	public boolean validateExamPaper(Exam_Paper exam_Paper,int inst_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select exam_paper_id from Exam_Paper where inst_id = :inst_id and div_id = :div_id and exam_id = :exam_id and batch_id = :batch_id ");
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", exam_Paper.getDiv_id());
			query.setParameter("exam_id", exam_Paper.getExam_id());
			query.setParameter("batch_id", exam_Paper.getBatch_id());
			List list = query.list();
			if(list.size()>0){
				return true;
			}
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
		return  false;

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
	
	public List<Integer> getDistinctExams(int inst_id,int div_id,int batch_id) {
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
		 criterion = Restrictions.eq("batch_id", batch_id);
			criteria.add(criterion);
		List<Integer> examList = criteria.list();
		transaction.commit();
		session.close();
		return  examList;	
	}
	
	public List<Exam_Paper> getExamPapers(int inst_id,int div_id,int exam_id,int batch_id) {
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
		 criterion = Restrictions.eq("batch_id", batch_id);
			criteria.add(criterion);
		List<Exam_Paper> examList = criteria.list();
		transaction.commit();
		session.close();
		return  examList;	
	}
	
	public List<Subject> getExamSubjects(int inst_id,int div_id,int batch_id,int exam_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		List<Subject> list = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("Select sub.subjectName,sub.subjectId,examPaper.marks from Exam_Paper examPaper , Subject sub where sub.institute_id = examPaper.inst_id and" +
					" sub.subjectId = examPaper.sub_id and examPaper.inst_id =:inst_id and examPaper.div_id = :div_id and examPaper.batch_id = :batch_id" +
					" and examPaper.exam_id = :exam_id  order by sub.subjectId ");
			query.setParameter("inst_id",inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("exam_id", exam_id);
			query.setParameter("batch_id", batch_id);
			list = query.list();
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
		return  list;

	}
	
	public List<Subject> getSubjectsExam(int inst_id,int div_id,int batch_id,int sub_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		List<Subject> list = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("Select distinct examPaper.exam_id,exm.exam_name  from Exam_Paper examPaper , Exam exm "
					+ "where exm.inst_id = examPaper.inst_id and exm.exam_id = examPaper.exam_id and examPaper.sub_id =:sub_id and  examPaper.inst_id =:inst_id "
					+ "and examPaper.div_id=:div_id and examPaper.batch_id=:batch_id order by exm.exam_id");
			query.setParameter("inst_id",inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("sub_id", sub_id);
			query.setParameter("batch_id", batch_id);
			list = query.list();
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
		return  list;

	}
	
	public List getDistinctExamSubjects(int inst_id,int div_id,int batch_id,List<Integer> exam_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		List list = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("Select distinct sub.subjectId,sub.subjectName from Exam_Paper examPaper , Subject sub where sub.institute_id = examPaper.inst_id and" +
					" sub.subjectId = examPaper.sub_id and examPaper.inst_id =:inst_id and examPaper.div_id = :div_id and examPaper.batch_id = :batch_id" +
					" and examPaper.exam_id in :exam_id  order by sub.subjectId ");
			query.setParameter("inst_id",inst_id);
			query.setParameter("div_id", div_id);
			query.setParameterList("exam_id", exam_id);
			query.setParameter("batch_id", batch_id);
			list = query.list();
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
		return  list;

	}
	
	public List getDistinctBatchExamSubjects(int inst_id,int div_id,List<Integer> batch_id,List<Integer> exam_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		List list = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("Select distinct sub.subjectId,sub.subjectName,examPaper.batch_id from Exam_Paper examPaper , Subject sub where sub.institute_id = examPaper.inst_id and" +
					" sub.subjectId = examPaper.sub_id and examPaper.inst_id =:inst_id and examPaper.div_id = :div_id and examPaper.batch_id in :batch_id" +
					" and examPaper.exam_id in :exam_id  order by examPaper.batch_id,sub.subjectId ");
			query.setParameter("inst_id",inst_id);
			query.setParameter("div_id", div_id);
			query.setParameterList("exam_id", exam_id);
			query.setParameterList("batch_id", batch_id);
			list = query.list();
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
		return  list;

	}
	
	public List getExamSubjects(int inst_id,int div_id,int batch_id,List<Integer> exam_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		List<Subject> list = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("Select examPaper.exam_id,sub.subjectId,examPaper.marks from Exam_Paper examPaper , Subject sub where sub.institute_id = examPaper.inst_id and" +
					" sub.subjectId = examPaper.sub_id and examPaper.inst_id =:inst_id and examPaper.div_id = :div_id and examPaper.batch_id = :batch_id" +
					" and examPaper.exam_id in :exam_id  order by examPaper.exam_id, sub.subjectId ");
			query.setParameter("inst_id",inst_id);
			query.setParameter("div_id", div_id);
			query.setParameterList("exam_id", exam_id);
			query.setParameter("batch_id", batch_id);
			list = query.list();
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
		return  list;

	}
	
	public List getBatchExamSubjects(int inst_id,int div_id,List<Integer> batch_id,List<Integer> exam_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		List<Subject> list = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("Select examPaper.exam_id,sub.subjectId,examPaper.marks,examPaper.batch_id from Exam_Paper examPaper , Subject sub where sub.institute_id = examPaper.inst_id and" +
					" sub.subjectId = examPaper.sub_id and examPaper.inst_id =:inst_id and examPaper.div_id = :div_id and examPaper.batch_id in :batch_id" +
					" and examPaper.exam_id in :exam_id "
					+ " order by examPaper.exam_id, sub.subjectId ");
			query.setParameter("inst_id",inst_id);
			query.setParameter("div_id", div_id);
			query.setParameterList("exam_id", exam_id);
			query.setParameterList("batch_id", batch_id);
			list = query.list();
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
		return  list;

	}
	
	public boolean deleteExamPaperRelatedToClass(int inst_id,int div_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Exam_Paper where inst_id = :inst_id "
											+ "and div_id = :div_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
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
	
	public boolean deleteExamPaperRelatedToSubject(int inst_id,int sub_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Exam_Paper where inst_id = :inst_id "
											+ "and sub_id = :sub_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("sub_id", sub_id);
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
	
	public boolean deleteExamPaperRelatedToExam(int inst_id,int exam_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Exam_Paper where inst_id = :inst_id and exam_id = :exam_id");
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
	
	public List<Exam> getOnlineExamList(int inst_id,int div_id,int batch_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		List<Exam> examList =null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select ex from Exam ex,Exam_Paper ep "
					+ "where ex.inst_id = :inst_id and ex.exam_id = ep.exam_id and "
					+ "ex.inst_id = ep.inst_id and ep.paper_type = 2 and ep.div_id = :div_id and ep.batch_id = :batch_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", batch_id);
			examList = query.list();
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
		return  examList;

	}
	
	public List getOnlineExamSubjectList(int inst_id,int div_id,int batch_id,int exam_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		List examList =null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select ep.question_paper_id,ep.sub_id,sub.subjectName from Exam ex,Exam_Paper ep,Subject sub "
					+ "where ex.inst_id = :inst_id and ex.exam_id = ep.exam_id and "
					+ "ex.inst_id = ep.inst_id and ep.paper_type = 2 and ep.div_id = :div_id "
					+ "and ep.batch_id = :batch_id and ep.exam_id = :exam_id and sub.subjectId = ep.sub_id and "
					+ " sub.institute_id = ep.inst_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", batch_id);
			query.setParameter("exam_id", exam_id);
			examList = query.list();
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
		return  examList;

	}
	
	public List<Integer> getDistinctBatchExam(int inst_id,int div_id,List batch_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		List examList =null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select distinct ep.exam_id,ep.batch_id,ex.exam_name from Exam_Paper ep,Exam ex "
					+ " where ep.inst_id = :inst_id and ep.div_id = :div_id and ep.batch_id in :batch_id and ex.inst_id = ep.inst_id and ex.exam_id = ep.exam_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameterList("batch_id", batch_id);
			examList = query.list();
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
		return  examList;

	}
}
