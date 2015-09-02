package com.classapp.db.subject;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
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
		session.save(studentMarks);
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
}
