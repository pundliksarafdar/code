package com.classapp.db.subject;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.exam.Exam;
import com.classapp.db.student.StudentMarks;
import com.classapp.persistence.HibernateUtil;

public class StudentMarksDB {
	public boolean saveStudentMarks(StudentMarks studentMarks) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(studentMarks);
		transaction.commit();
		session.close();
		return  true;

	}
	
	public List<StudentMarks> getStudentMarks(int inst_id,int student_id) {
		StudentMarks studentMarks=new StudentMarks();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(StudentMarks.class);
		Criterion criterion = Restrictions.eq("student_id", student_id);
		criteria.add(criterion);
		if(inst_id!=-1){
		criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		}
		List<StudentMarks> marks = criteria.list();
		transaction.commit();
		session.close();
		return  marks;
	}
	
	
	public boolean isExamSolvedByStudent(int exam_id,int inst_id,int sub_id,int div_id,int student_id) {
		StudentMarks studentMarks=new StudentMarks();
		studentMarks.setExam_id(exam_id);
		studentMarks.setInst_id(inst_id);
		studentMarks.setDiv_id(div_id);
		studentMarks.setSub_id(sub_id);
		studentMarks.setStudent_id(student_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		studentMarks=(StudentMarks) session.get(StudentMarks.class,studentMarks);
		transaction.commit();
		session.close();
		if(studentMarks==null){
		return false;
		}
		
		return  true;
	}
	
	public List<StudentMarks> getStudentMarksList(int inst_id,int student_id,int sub_id,int div_id) {
		StudentMarks studentMarks=new StudentMarks();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(StudentMarks.class);
		Criterion criterion = Restrictions.eq("student_id", student_id);
		criteria.add(criterion);
		if(inst_id!=-1){
		criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		}
		if(sub_id!=-1){
			criterion = Restrictions.eq("sub_id", sub_id);
			criteria.add(criterion);
			}
		if(div_id!=-1){
			criterion = Restrictions.eq("div_id", div_id);
			criteria.add(criterion);
			}
		
		List<StudentMarks> marks = criteria.list();
		transaction.commit();
		session.close();
		return  marks;
	}
	
	public List<StudentMarks> getStudentMarks(int inst_id,List<Integer> student_id,int sub_id,int div_id,int currentPage,int examID) {
		StudentMarks studentMarks=new StudentMarks();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(StudentMarks.class).addOrder(Order.desc("marks"));
		Criterion criterion = Restrictions.in("student_id", student_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("exam_id", examID);
		criteria.add(criterion);
		if(inst_id!=-1){
		criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		}
		if(sub_id!=-1){
			criterion = Restrictions.eq("sub_id", sub_id);
			criteria.add(criterion);
			}
		if(div_id!=-1){
			criterion = Restrictions.eq("div_id", div_id);
			criteria.add(criterion);
			}
		if (currentPage!=1 && currentPage!=0) {
			criteria.setFirstResult((currentPage-1)*100);
		}
		criteria.setMaxResults(100);
		List<StudentMarks> marks = criteria.list();
		transaction.commit();
		session.close();
		return  marks;
	}
	
	public int getStudentMarksCount(int inst_id,List<Integer> student_id,int sub_id,int div_id,int examID) {
		StudentMarks studentMarks=new StudentMarks();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(StudentMarks.class).setProjection(Projections.rowCount());
		Criterion criterion = Restrictions.in("student_id", student_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("exam_id", examID);
		criteria.add(criterion);
		if(inst_id!=-1){
		criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		}
		if(sub_id!=-1){
			criterion = Restrictions.eq("sub_id", sub_id);
			criteria.add(criterion);
			}
		if(div_id!=-1){
			criterion = Restrictions.eq("div_id", div_id);
			criteria.add(criterion);
			}
		
		Long count = (Long) criteria.uniqueResult();
		transaction.commit();
		session.close();
		return  count.intValue();
	}
	
	public boolean deleteStudentMarksrelatedtosubject(int sub_id) {
		Transaction transaction=null;
		Session session=null;
		StudentMarks studentMarks=new StudentMarks();
		studentMarks.setSub_id(sub_id);
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.delete(studentMarks);
		transaction.commit();
		session.close();
		return  true;

	}
	
	public boolean deleteStudentMarksrelatedtodivision(int div_id) {
		Transaction transaction=null;
		Session session=null;
		StudentMarks studentMarks=new StudentMarks();
		studentMarks.setDiv_id(div_id);
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.delete(studentMarks);
		transaction.commit();
		session.close();
		return  true;

	}
	
	public boolean deleteStudentMarksrelatedtoexam(int inst_id,int div_id,int sub_id,int exam_ID) {
		Transaction transaction=null;
		Session session=null;
		StudentMarks studentMarks=new StudentMarks();
		studentMarks.setExam_id(exam_ID);
		studentMarks.setInst_id(inst_id);
		studentMarks.setSub_id(sub_id);
		studentMarks.setDiv_id(div_id);
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.delete(studentMarks);
		transaction.commit();
		session.close();
		return  true;

	}
	
	public boolean deleteStudentMarksrelatedtostudentID(int inst_id,int student_id) {
		Transaction transaction=null;
		Session session=null;
		StudentMarks studentMarks=new StudentMarks();
		studentMarks.setInst_id(inst_id);
		studentMarks.setStudent_id(student_id);
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.delete(studentMarks);
		transaction.commit();
		session.close();
		return  true;

	}
	
	public StudentMarks getStudentExamMark(int inst_id,int student_id,int sub_id,int div_id,int examID) {
		StudentMarks studentMarks=new StudentMarks();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(StudentMarks.class).addOrder(Order.desc("marks"));
		Criterion criterion = Restrictions.eq("student_id", student_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("exam_id", examID);
		criteria.add(criterion);
		if(inst_id!=-1){
		criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		}
		if(sub_id!=-1){
			criterion = Restrictions.eq("sub_id", sub_id);
			criteria.add(criterion);
			}
		if(div_id!=-1){
			criterion = Restrictions.eq("div_id", div_id);
			criteria.add(criterion);
			}
		
		List<StudentMarks> marks = criteria.list();
		transaction.commit();
		session.close();
		if(marks!=null){
			return marks.get(0);
		}
		return  null;
	}
	
	public List getStudentExamMarks(int inst_id, int div_id,int batch_id, int exam_id) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = "select reg.fname,reg.lname,std.marks,sub.subjectName,sub.subjectId,reg.regId from StudentMarks std,RegisterBean reg,Subject sub"
							+ " where reg.regId=std.student_id and sub.subjectId = std.sub_id and sub.institute_id = std.inst_id and "
							+ "std.inst_id = :inst_id and std.div_id = :div_id and std.batch_id = :batch_id and std.exam_id = :exam_id"
							+ " order by reg.regId,sub.subjectId";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", batch_id);
			query.setParameter("exam_id", exam_id);
			list = query.list();
		
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return list;
}
	
	public List getStudentExamSubjectMarks(int inst_id, int div_id,int batch_id, int sub_id) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = "select reg.fname,reg.lname,std.marks,sub.subjectId,exm.exam_id,reg.regId from StudentMarks std,RegisterBean reg,Subject sub,Exam exm"
							+ " where reg.regId=std.student_id and sub.subjectId = std.sub_id and sub.institute_id = std.inst_id and "
							+ "std.inst_id = :inst_id and std.div_id = :div_id and std.batch_id = :batch_id and std.sub_id = :sub_id and exm.exam_id = std.exam_id and "
							+ "exm.inst_id = std.inst_id order by reg.regId,sub.subjectId";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", batch_id);
			query.setParameter("sub_id", sub_id);
			list = query.list();
		
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return list;
}
	
	
	
	
}
