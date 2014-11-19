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
	
	public List<MCQData> getExamPapers(int class_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from MCQData where class_id = : class_id";
		
		List<MCQData> listOfPapers=new ArrayList<MCQData>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("class_id", class_id);  
			queryResultList = query.list();
			transaction.commit();
			Iterator itr= queryResultList.iterator();
			
			while(itr.hasNext()){
				MCQData entry= (MCQData)itr.next();
				listOfPapers.add(entry);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return listOfPapers;
	}
	
	public List<MCQDetails> getAllExamPapersDetails(int class_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from MCQData where class_id = :class_id";
		
		List<MCQDetails> listOfExamPapers=new ArrayList<MCQDetails>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("class_id", class_id);  
			queryResultList = query.list();
			transaction.commit();
			Iterator itr= queryResultList.iterator();
			DivisionDB divisionDB=new DivisionDB();
			SubjectDb subjectDb= new SubjectDb();
			TeacherDB teacherDB= new TeacherDB();
			while(itr.hasNext()){
				MCQData entry= (MCQData)itr.next();
				MCQDetails mcqDetails=new MCQDetails();
				mcqDetails.setExam_id(entry.getExam_id());
				mcqDetails.setDivision(divisionDB.retriveByID(entry.getDiv_id()));
				mcqDetails.setTeacher(teacherDB.getTeacherDetailsFromID(entry.getTeacher_id()));
				mcqDetails.setClass_id(class_id);
				mcqDetails.setSubject(subjectDb.retrive(entry.getSubject_id()));
				mcqDetails.setCreationDate(entry.getCreate_date());
				mcqDetails.setUpload_path(entry.getUpload_path());
				listOfExamPapers.add(mcqDetails);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return listOfExamPapers;
	}
	
	public MCQData getMCQPaperFromClass(int exam_id, int class_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from MCQData where exam_id = :exam_id and class_id = :class_id";
		MCQData mcqData=null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("exam_id", exam_id);
			query.setInteger("class_id", class_id);  
			queryResultList = query.list();
			transaction.commit();
			if(queryResultList.size()>0){
				mcqData=(MCQData) queryResultList.get(0);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return mcqData;
	}
	
	/*
	 * Search Exams based on date,batch,teacher and division 
	 * Parameter set as they are useful for query
	 * */
	public List<MCQData> searchExam(int class_id,int subject,int teacher,int division,String startDate,String endDate){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String SPACE = " ";
		String QUERY = "from MCQData where class_id = :class_id";
		//String queryString="from MCQData where class_id = :class_id and subject_id = :subject and div_id = :division and teacher_id = :teacher and (create_date between :startDate and :endDate)";
		
		if(subject>0){
			QUERY+= SPACE + "and subject_id = :subject";
		}
		
		if (teacher>0) {
			QUERY+= SPACE + "and div_id = :division";
		}
		
		if (division>0) {
			QUERY+= SPACE+ "and teacher_id = :teacher";
		}
		
		QUERY +=SPACE+"and (create_date between :startDate and :endDate)";
		
		List<MCQData> listOfExamPapers=new ArrayList<MCQData>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(QUERY);

			if(subject>0){
				query.setInteger("subject", subject);
			}
			
			if (teacher>0) {
				query.setInteger("teacher", teacher);
			}
			
			if (division>0) {
				query.setInteger("division", division);	
			}
			
			
			if (null!=startDate && startDate.trim().length()!=0) {
				query.setString("startDate", startDate+" 00:00:00");
			}else{
				query.setString("startDate", "%");
			}
				
			
			if (null!=endDate && endDate.trim().length()!=0) {
				query.setString("endDate", endDate+" 23:59:59");
			}else{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateTodays = dateFormat.format(new Date());
				query.setString("endDate", dateTodays);
			}
			
			query.setInteger("class_id", class_id);
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
