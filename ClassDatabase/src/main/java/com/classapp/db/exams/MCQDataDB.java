package com.classapp.db.exams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.Teacher.TeacherDB;
import com.classapp.db.batch.division.DivisionDB;
import com.classapp.db.student.Student;
import com.classapp.db.subject.SubjectDb;
import com.classapp.persistence.HibernateUtil;

public class MCQDataDB {
	
	
	public String updateDb(MCQData mcqData){
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(mcqData);
			transaction.commit();
		}catch(Exception e){
			status = "1";
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return status;
	}
	
	
	
	/*
	 * Search Exams based on date,batch,teacher and division 
	 * Parameter set as they are useful for query
	 * */
	public List<MCQData> searchExam(int classId,Date startDate,Date endDate,Integer divId,Boolean publish,Integer subjectId,Integer teacherId,Integer examId,String batch){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String SPACE = " ";
		String QUERY = "from MCQData where class_id = :class_id";
		
		if(subjectId!=null){
			QUERY+= SPACE + "and subject_id = :subject";
		}
		
		if (divId!=null) {
			QUERY+= SPACE + "and div_id = :division";
		}
		
		if (teacherId != null) {
			QUERY+= SPACE+ "and teacher_id = :teacher";
		}
		
		if(batch != null){
			QUERY+= SPACE+ "and batches like :batches";
		}
		
		QUERY +=SPACE+"and (create_date between :startDate and :endDate)";
		
		List<MCQData> listOfExamPapers=new ArrayList<MCQData>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(QUERY);

			if(subjectId!=null){
				query.setInteger("subject", subjectId);
			}
			
			if (teacherId!=null) {
				query.setInteger("teacher", teacherId);
			}
			
			if (divId!=null) {
				query.setInteger("division", divId);	
			}

			if(batch != null){
				query.setString("batches", batch);
			}
			
			if (null!=startDate) {
				query.setString("startDate", startDate+" 00:00:00");
			}else{
				query.setString("startDate", "%");
			}


			if (null!=endDate) {
				query.setString("endDate", endDate+" 23:59:59");
			}else{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateTodays = dateFormat.format(new Date());
				query.setString("endDate", dateTodays);
			}
			
			
			query.setInteger("class_id", classId);
			queryResultList = query.list();
			transaction.commit();
			Iterator itr= queryResultList.iterator();
			DivisionDB divisionDB=new DivisionDB();
			SubjectDb subjectDb= new SubjectDb();
			TeacherDB teacherDB= new TeacherDB();
			while(itr.hasNext()){
				MCQData mcqData= (MCQData)itr.next();
				listOfExamPapers.add(mcqData);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return listOfExamPapers;
	}
}
