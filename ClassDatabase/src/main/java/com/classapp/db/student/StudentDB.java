package com.classapp.db.student;

import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.batch.Batch;
import com.classapp.db.batch.BatchDB;
import com.classapp.db.batch.division.Division;
import com.classapp.db.batch.division.DivisionDB;
import com.classapp.db.batch.division.DivisionData;
import com.classapp.db.register.RegisterBean;
import com.classapp.persistence.HibernateUtil;

public class StudentDB {
	
	public String updateDb(Student student){
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(student);
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
	
	public List<Student> getStudents(int class_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from Student where class_id = : class_id";
		
		List<Student> listOfStudent=new ArrayList<Student>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("class_id", class_id);  
			queryResultList = query.list();
			transaction.commit();
			Iterator itr= queryResultList.iterator();
			
			while(itr.hasNext()){
				Student entry= (Student)itr.next();
				listOfStudent.add(entry);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return listOfStudent;
	}
	
	public List<StudentDetails> getAllStudentsDetails(int class_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from Student where class_id = :class_id";
		
		List<StudentDetails> listOfStudent=new ArrayList<StudentDetails>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("class_id", class_id);  
			queryResultList = query.list();
			transaction.commit();
			Iterator itr= queryResultList.iterator();
			DivisionDB divisionDB=new DivisionDB();
			BatchDB batchDB= new BatchDB();
			while(itr.hasNext()){
				Student entry= (Student)itr.next();
				StudentDetails studentDetails=new StudentDetails();
				studentDetails.setStudentId(entry.getStudent_id());
				studentDetails.setDivision(divisionDB.retriveByID(entry.getDiv_id()));
				studentDetails.setStudentUserBean(getStudentDetailsFromID(entry.getStudent_id()));
				
				String[] batchids=entry.getBatch_id().split(",");
				List<Batch> batches= new ArrayList<Batch>();
				for (String batchId : batchids) {
					Batch batch=batchDB.getBatchFromID(Integer.parseInt(batchId),class_id,entry.getDiv_id());
					if(batch!=null){
						batches.add(batch);
					}
				}
				studentDetails.setBatches(batches);
				listOfStudent.add(studentDetails);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return listOfStudent;
	}
	
	
	
	public boolean isStudentExistsInClass(int student_id, int class_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		boolean isExists=false;
		String queryString="from Student where student_id = :student_id and class_id = :class_id";
		
		List<Student> listOfStudent=new ArrayList<Student>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("student_id", student_id);
			query.setInteger("class_id", class_id);  
			queryResultList = query.list();
			transaction.commit();
			if(queryResultList.size()>0){
				isExists=true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return isExists;
	}
	
	public Student getStudentDetailsFromClass(int student_id, int class_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="from Student where student_id = :student_id and class_id = :class_id";
		Student student=null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("student_id", student_id);
			query.setInteger("class_id", class_id);  
			queryResultList = query.list();
			transaction.commit();
			if(queryResultList.size()>0){
				student=(Student) queryResultList.get(0);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return student;
	}
	
	public boolean isStudentExists(String studentLoginName){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		boolean isExists=false;
		String queryString="from RegisterBean where loginName = :loginName";
		
		List<Student> listOfStudent=new ArrayList<Student>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setString("loginName", studentLoginName);
			queryResultList = query.list();
			transaction.commit();
			if(queryResultList.size()>0){
				isExists=true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return isExists;
	}
	
	public RegisterBean getStudent(String studentLoginName){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		boolean isExists=false;
		String queryString="from RegisterBean where loginName = :loginName";
		
		List<RegisterBean> listOfStudent=new ArrayList<RegisterBean>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setString("loginName", studentLoginName);
			queryResultList = query.list();
			transaction.commit();
			if(queryResultList.size()==1){
				return (RegisterBean) queryResultList.get(0);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return null;
	}
	
	
	
	public RegisterBean getStudentDetailsFromID(int student_id){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		boolean isExists=false;
		String queryString="from RegisterBean where regId = :regId";
		
		List<RegisterBean> listOfStudent=new ArrayList<RegisterBean>();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("regId", student_id);
			queryResultList = query.list();
			transaction.commit();
			if(queryResultList.size()==1){
				return (RegisterBean) queryResultList.get(0);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return null;
	}
	
	
	public String[] getAssignedBatcheIds(String studentLoginName){
		Session session = null;
		Transaction transaction = null;
		List queryResultList=null;
		String queryString="regId from RegisterBean where loginName = :loginName";
		int student_id=0;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setString("loginName", studentLoginName);
			queryResultList = query.list();
			
			if(queryResultList.size()==1){
				 student_id=(Integer)queryResultList.get(0);
			}
			queryString="from Student where student_id = :student_id";
			query = session.createQuery(queryString);
			query.setInteger("student_id", student_id);
			
			queryResultList = query.list();
			transaction.commit();
			if(queryResultList.size()==1){
				 Student student=(Student)queryResultList.get(0);
				 String[]batch_ids= student.getBatch_id().split(",");
				 return batch_ids;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return null;
	}

	public boolean deleteStudent(int student_id, int class_id) {
		
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		String queryString="from Student where student_id = :student_id and class_id = :class_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("student_id", student_id);
			query.setInteger("class_id", class_id);  
					
				Student student=(Student)query.uniqueResult();
				if(student!=null){
					session.delete(student);
					status=true;					
				}
			
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return status;
	}
	
	public List<Student> getStudentinfo(int regID) {

		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List<Student> list=null;
		String queryString="from Student where student_id = :student_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("student_id", regID);
								
				list=query.list();
			
			
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return list;
	}
	
	public Student getclassStudent(int studentID,int classID) {

		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List<Student> list=null;
		String queryString="from Student where student_id = :student_id and class_id=:class_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("student_id", studentID);
			query.setInteger("class_id", classID);
								
				list=query.list();
			if(list!=null)
			{
				return list.get(0);
			}
			
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return null;
	}
	
	public List getStudentrelatedtoBatch(String batchname,int inst_id,int div_id) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List list=null;
		String queryString="from Student where (batch_id like :batch_id1 or batch_id like :batch_id2 or batch_id like :batch_id3 or batch_id = :batch_id4) and class_id=:class_id and div_id=:div_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("batch_id1", batchname+",%");
			query.setParameter("batch_id2","%,"+batchname+",%");	
			query.setParameter("batch_id3", "%,"+batchname);
			query.setParameter("batch_id4", batchname);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
				list=query.list();
			if(list!=null)
			{
				return list;
			}
			
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return null;
	}
	
	public void removebatchfromstudentlist(Student student) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List<Student> list=null;
		String queryString="from Student where student_id=:student_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("student_id", student.getStudent_id());
			list=query.list();
			/*if(list!=null)
			{
				return list;
			}
			*/
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	public Integer getStudentcountrelatedtobatch(String batchname,int inst_id,int div_id) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List<Long> list=null;
		String queryString="select count(student_id) from Student where (batch_id like :batch_id1 or batch_id like :batch_id2 or batch_id like :batch_id3 or batch_id = :batch_id4) and class_id=:class_id and div_id=:div_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("batch_id1", batchname+",%");
			query.setParameter("batch_id2","%,"+batchname+",%");	
			query.setParameter("batch_id3", "%,"+batchname);
			query.setParameter("batch_id4", batchname);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
			list=(List<Long>)query.list();
			if(list!=null)
			{
				return list.get(0).intValue();
			}
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return 0;
	}
	
	public Integer getunallocatedStudentcount(int inst_id) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List<Long> list=null;
		String queryString="select count(student_id) from Student where batch_id=:batch_id  and class_id=:class_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("batch_id", "");
			query.setParameter("class_id", inst_id);
			list=(List<Long>)query.list();
			if(list!=null)
			{
				return list.get(0).intValue();
			}
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return 0;
	}
	
	public List getStudentIDSrelatedtoBatch(String batchname,int inst_id,int div_id) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List list=null;
		String queryString="from Student where (batch_id like :batch_id1 or batch_id like :batch_id2 or batch_id like :batch_id3 or batch_id = :batch_id4) and class_id=:class_id and div_id=:div_id order by student_id asc";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("batch_id1", batchname+",%");
			query.setParameter("batch_id2","%,"+batchname+",%");	
			query.setParameter("batch_id3", "%,"+batchname);
			query.setParameter("batch_id4", batchname);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
				list=query.list();
			if(list!=null)
			{
				return list;
			}
			
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return null;
	}
	
	public List getUnallocatedStudentIDs(int inst_id) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List list=null;
		String queryString="select student_id from Student where (batch_id =:batch_id) and class_id=:class_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("batch_id", "");
			query.setParameter("class_id", inst_id);
				list=query.list();
			if(list!=null)
			{
				return list;
			}
			
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return null;
	}
	
	public boolean deletestudentrelatedtoclass(int classid) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List list=null;
		String queryString="DELETE from Student where div_id=:div_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("div_id", classid);
			query.executeUpdate();
			transaction.commit();
		}catch(Exception e){
				e.printStackTrace();
			}finally{
				session.close();
			}
		return true;
	}
	
	public Integer getStudentCount(int regID) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List<Long> list=new ArrayList<Long>();
		String queryString="select count(*) from Student where class_id=:class_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("class_id", regID);
			
				list=(List<Long>) query.list();
			if(list!=null)
			{
				return list.get(0).intValue();
			}
			
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return list.get(0).intValue();
	}
	
	public Student getStudentByStudentID(String studentID,int class_id) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List<Student> list=new ArrayList<Student>();
		String queryString=" from Student where student_id=(select regId from RegisterBean where loginName=:studentID) and class_id=:class_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("studentID", studentID);
			query.setParameter("class_id", class_id);
				list= query.list();
			if(list.size()>0){
				return list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return null;
	}
	
	public Student getStudentByStudentID(int studentID,int class_id) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List<Student> list=new ArrayList<Student>();
		String queryString=" from Student where student_id=:studentID and class_id=:class_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("studentID", studentID);
			query.setParameter("class_id", class_id);
				list= query.list();
			if(list.size()>0){
				return list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return null;
	}
	
	public List<Integer>  getStudentsFromBatches(int class_id,int div_id,String batchids) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List<Integer> list=new ArrayList<Integer>();
		String queryString=" select student_id from Student where class_id=:class_id and div_id=:div_id";
		if(!"-1".equals(batchids) && !"".equals(batchids)){
			String batchidsarr[]=batchids.split(",");
			for (int i = 0; i < batchidsarr.length; i++) {
				if(i==0){
					queryString=queryString+" and ((batch_id like :batch_id"+i+"a or batch_id like :batch_id"+i+"b or batch_id like :batch_id"+i+"c or batch_id = :batch_id"+i+"d)";
				}else{
					queryString=queryString+"or (batch_id like :batch_id"+i+"a or batch_id like :batch_id"+i+"b or batch_id like :batch_id"+i+"c or batch_id = :batch_id"+i+"d)";
				}
			}
			queryString=queryString+") order by student_id";
		}
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("div_id", div_id);
			query.setParameter("class_id", class_id);
			if(!"-1".equals(batchids) && !"".equals(batchids)){
				String batchidsarr[]=batchids.split(",");
				for (int i = 0; i < batchidsarr.length; i++) {
					query.setParameter("batch_id"+i+"a", batchidsarr[i].trim()+",%");
					query.setParameter("batch_id"+i+"b","%,"+batchidsarr[i].trim()+",%");	
					query.setParameter("batch_id"+i+"c", "%,"+batchidsarr[i].trim());
					query.setParameter("batch_id"+i+"d", batchidsarr[i].trim());
			}
			}
				list= query.list();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return list;
	}
	
	public List<Student> getStudentByStudentIDs(List<Integer> studentID,int class_id) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List<Student> list=new ArrayList<Student>();
		String queryString=" from Student where student_id in :studentID and class_id=:class_id order by student_id asc";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameterList("studentID", studentID);
			query.setParameter("class_id", class_id);
				list= query.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return list;
	}
	
	
	public List getStudentrelatedtoBatchForAttendance(String batchname,int inst_id,int div_id) {
		Session session = null;
		boolean status=false;
		Transaction transaction = null;
		List list=null;
		String queryString="Select reg.fname,reg.lname, reg.regId from Student std,RegisterBean reg " +
				"where (std.batch_id like :batch_id1 or std.batch_id like :batch_id2 or std.batch_id like :batch_id3 or std.batch_id = :batch_id4) " +
				"and std.class_id=:class_id and std.div_id=:div_id and reg.regId = std.student_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("batch_id1", batchname+",%");
			query.setParameter("batch_id2","%,"+batchname+",%");	
			query.setParameter("batch_id3", "%,"+batchname);
			query.setParameter("batch_id4", batchname);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
				list=query.list();
			if(list!=null)
			{
				return list;
			}
			
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return null;
	}
	
}
