package com.classapp.db.attendance;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.question.Questionbank;
import com.classapp.persistence.HibernateUtil;

public class AttendanceDB {
	public void save(Attendance attendance){
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(attendance);
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
		
	}
	
	public boolean updateAttendance(Attendance attendance) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("update Attendance set presentee = :presentee where  inst_id = :inst_id and att_date = :att_date and att_id = :att_id");
		query.setParameter("inst_id", attendance.getInst_id());
		query.setParameter("att_date", attendance.getAtt_date());
		query.setParameter("presentee", attendance.getPresentee());
		query.setParameter("att_id", attendance.getAtt_id());
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
	
	public boolean deleteAttendance(int inst_id,int div_id) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("DELETE FROM Attendance WHERE inst_id=:inst_id and div_id=:div_id");
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
		if(session!=null){
			session.close();
		}
		}
		return  true;

	}
	
	public boolean deleteAttendance(int inst_id,int div_id,int batch_id) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("DELETE FROM Attendance WHERE inst_id=:inst_id and div_id=:div_id"
										+ " and batch_id = :batch_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
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
	
	public List getStudentsDailyAttendance(String batchname,
			int inst_id, int div_id, Date date) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = "Select std.fname,std.lname, reg.regId,att.presentee,att.att_id, sch.start_time,sch.end_time,sub.subjectName,sub.subjectId,std.batchIdNRoll" +
				" from Student std,RegisterBean reg, Attendance att,Schedule sch,Subject sub "
				+ "where (std.batch_id like :batch_id1 or std.batch_id like :batch_id2 or std.batch_id like :batch_id3 or std.batch_id = :batch_id4) "
				+ "and std.class_id=:class_id and std.div_id=:div_id and reg.regId = std.student_id and att.student_id = reg.regId and "
				+ "att.div_id = std.div_id and att.batch_id = :attbatch_id and att.att_date = :date and att.att_date = sch.date and  att.schedule_id = sch.schedule_id" +
				" and att.div_id = sch.div_id and att.batch_id = sch.batch_id and std.class_id = att.inst_id and std.class_id = sch.inst_id and att.inst_id = sch.inst_id" +
				" and sub.subjectId = att.sub_id order by reg.regId,sch.start_time ";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("batch_id1", batchname + ",%");
			query.setParameter("batch_id2", "%," + batchname + ",%");
			query.setParameter("batch_id3", "%," + batchname);
			query.setParameter("batch_id4", batchname);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("attbatch_id", Integer.parseInt(batchname));
			query.setParameter("date", date);
			

			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	public List getDistinctDailyScheduleTime(int batch,
			int inst_id, int div_id, Date date) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = "Select distinct (sch.start_time),sch.end_time,sub.subjectId,sub.subjectName" +
				" from  Attendance att,Schedule sch,Subject sub "
				+ "where sch.inst_id = :inst_id and sch.div_id=:div_id  and" +
				" sch.batch_id = :batch_id and sch.date = :date and att.inst_id = sch.inst_id " +
				"and att.div_id = sch.div_id and att.batch_id = sch.batch_id and att.att_date = sch.date " +
				"and att.schedule_id = sch.schedule_id and sub.subjectId = att.sub_id  order by sch.start_time ";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("batch_id", batch);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("date", date);
			

			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public List getStudentsMonthlyPresentCount(String batchname,
			int inst_id, int div_id, Date date) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(date);
		int lastdate = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		String queryString = " SELECT std.fname,std.lname,count(att.presentee),att.att_date  ,att.student_id FROM Attendance att, Student std "
							+"where  std.student_id = att.student_id and std.class_id = att.inst_id and att.inst_id=:inst_id and att.div_id = :div_id and att.batch_id = :batch_id and att.presentee ='P' and att.att_date>=:start_date " +
							"and att.att_date<=:end_date group by att.att_date,att.student_id order by att.student_id,att.att_date";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", Integer.parseInt(batchname));
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);

			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	public List getStudentsMonthlyTotalCount(String batchname,
			int inst_id, int div_id, Date date) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(date);
		int lastdate = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		String queryString = " SELECT count(distinct schedule_id),att_date FROM Attendance "
							+"where inst_id=:inst_id and div_id = :div_id and batch_id = :batch_id and att_date>=:start_date " +
							"and att_date<=:end_date group by att_date order by att_date";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", Integer.parseInt(batchname));
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);

			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public List getStudentsWeeklyPresentCount(String batchname,
			int inst_id, int div_id, Date date) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(startDate);
		 calendar.add(calendar.DATE, 6);
		//calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		String queryString = " SELECT std.fname,std.lname,count(att.presentee),att.att_date  ,att.student_id FROM Attendance att, Student std "
							+"where  std.student_id = att.student_id and std.class_id = att.inst_id and att.inst_id=:inst_id and att.div_id = :div_id and att.batch_id = :batch_id and att.presentee ='P' and att.att_date>=:start_date " +
							"and att.att_date<=:end_date group by att.att_date,att.student_id order by att.student_id,att.att_date";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", Integer.parseInt(batchname));
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);

			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	public List getStudentsWeeklyTotalCount(String batchname,
			int inst_id, int div_id, Date date) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(startDate);
		 calendar.add(calendar.DATE, 6);
		//calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		String queryString = " SELECT count(distinct schedule_id),att_date FROM Attendance "
							+"where inst_id=:inst_id and div_id = :div_id and batch_id = :batch_id and att_date>=:start_date " +
							"and att_date<=:end_date group by att_date order by att_date";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", Integer.parseInt(batchname));
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);

			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public boolean deleteAttendanceRelatedToSubject(int inst_id,int sub_id) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("DELETE FROM Attendance WHERE inst_id=:inst_id and sub_id=:sub_id");
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
		if(session!=null){
			session.close();
		}
		}
		return  true;

	}
	
	public boolean deleteAttendanceRelatedToStudent(int inst_id,int student_id) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("DELETE FROM Attendance WHERE inst_id=:inst_id and student_id=:student_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("student_id", student_id);
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
	
	public boolean deleteAttendanceRelatedToBatch(int inst_id,int div_id,int batch_id) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("DELETE FROM Attendance WHERE inst_id=:inst_id and div_id=:div_id"
				+ " and batch_id = :batch_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
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
	
	public List getMonthWiseTotalLecture(List<List<Integer>> list,int inst_id,int div_id,Date start_date,Date end_date){
		Transaction transaction=null;
		Session session=null;
		List resultList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			String queryString="select count( distinct schedule_id,att_date),batch_id,sub_id,MONTH(att_date),YEAR(att_date) from attendance "
					+ "where inst_id = :inst_id and (batch_id,sub_id) in :list and div_id = :div_id and att_date>= :start_date "
					+ "and att_date <= :end_date group by YEAR(att_date), MONTH(att_date), sub_id  order by YEAR(att_date), MONTH(att_date),sub_id";
			queryString = queryString.replace(":list", list.toString());
			Query query = session.createSQLQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("start_date", start_date);
			query.setParameter("end_date", end_date);
			
			resultList =query.list();
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
		return resultList;
	}
	
	public List getStartEndOfYearlyAttendance(int inst_id,int div_id,List<Integer> batchIDs){
		Transaction transaction=null;
		Session session=null;
		List resultList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			String queryString="select min(att_date),max(att_date),batch_id, YEAR(min(att_date)),YEAR(max(att_date)) from Attendance "
					+ "where inst_id = :inst_id and div_id = :div_id and batch_id in :batch_id group by batch_id";
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameterList("batch_id", batchIDs);
			resultList = query.list();
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
		return resultList;
	}
	
	public List getStudentsMonthWiseAttendance(List<List<Integer>> list,int inst_id,int div_id,Date start_date,Date end_date,int student_id){
		Transaction transaction=null;
		Session session=null;
		List resultList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			String queryString="select count(distinct schedule_id,att_date),batch_id,sub_id,MONTH(att_date),YEAR(att_date) from attendance "
					+ "where inst_id = :inst_id and (batch_id,sub_id) in :list and div_id = :div_id and att_date>= :start_date "
					+ "and att_date <= :end_date and student_id = :student_id and presentee = 'P'   group by YEAR(att_date), MONTH(att_date), sub_id  "
					+ "order by YEAR(att_date), MONTH(att_date),sub_id";
			queryString = queryString.replace(":list", list.toString());
			Query query = session.createSQLQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("start_date", start_date);
			query.setParameter("end_date", end_date);
			query.setParameter("student_id", student_id);
			resultList =query.list();
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
		return resultList;
	}
	
	public List<Integer> getDailyDistinctAttendance(int inst_id,int div_id,int batch_id,Date date){
		Transaction transaction=null;
		Session session=null;
		List<Integer> resultList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			String queryString="select distinct schedule_id from Attendance "
					+ "where inst_id = :inst_id and batch_id = :batch_id and div_id = :div_id and att_date= :date ";
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", batch_id);
			query.setParameter("date", date);
			resultList =query.list();
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
		return resultList;
	}
	
	public List getAllStudents(String batchID,
			int inst_id, int div_id) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = "Select reg.fname,reg.lname, reg.regId,std.batchIdNRoll " +
				" from Student std,RegisterBean reg "
				+ "where (std.batch_id like :batch_id1 or std.batch_id like :batch_id2 or std.batch_id like :batch_id3 or std.batch_id = :batch_id4) "
				+ "and std.class_id=:class_id and std.div_id=:div_id and reg.regId = std.student_id order by std.student_id ";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("batch_id1", batchID + ",%");
			query.setParameter("batch_id2", "%," + batchID + ",%");
			query.setParameter("batch_id3", "%," + batchID);
			query.setParameter("batch_id4", batchID);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);

			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public List getDailyLecturesCountForNotification(int inst_id,Date att_date){
		Transaction transaction=null;
		Session session=null;
		List resultList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			String queryString="select count(distinct schedule_id),div_id,batch_id from Attendance "
					+ "where inst_id = :inst_id and att_date = :att_date group by div_id,batch_id";
			Query query = session.createSQLQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("att_date", att_date);	
			resultList =query.list();
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
		return resultList;
	}
	
	public List getStudentsDailyPresentCountForNotification(int inst_id, Date att_date) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = " SELECT std.student_id,reg.fname,reg.lname,reg.phone1,reg.email,count(CASE WHEN att.presentee = 'P' THEN att.presentee ELSE NULL END),"
							+ "count(distinct att.schedule_id),std.parentFname,std.parentLname,"
							+ "std.parentPhone,std.parentEmail,att.div_id,att.batch_id,batch.batch_name FROM Attendance att, RegisterBean reg,Student std,Batch batch "
							+"where  reg.regId = att.student_id and std.student_id = att.student_id and std.class_id = att.inst_id"
							+ " and std.div_id = att.div_id and att.inst_id=:inst_id " +
							"and att.att_date=:att_date and batch.batch_id = att.batch_id and batch.class_id = att.inst_id and batch.div_id = att.div_id group by att.student_id,att.batch_id,att.div_id order by att.student_id,att.batch_id,att.div_id";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("att_date", att_date);
			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}

	public List getWeeklyLecturesCountForNotification(int inst_id, Date startDate,Date endDate) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = " SELECT count(distinct schedule_id),div_id,batch_id FROM Attendance "
							+"where inst_id=:inst_id and  att_date>=:start_date " +
							"and att_date<=:end_date group by div_id,batch_id";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);

			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public List getStudentsWeeklyPresentCountForNotification(int inst_id, Date startDate,Date endDate) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString =" SELECT std.student_id,reg.fname,reg.lname,reg.phone1,reg.email,count(CASE WHEN att.presentee = 'P' THEN att.presentee ELSE NULL END),"
							+ "count(distinct att.schedule_id,att_date),std.parentFname,std.parentLname,"
							+ "std.parentPhone,std.parentEmail,att.div_id,att.batch_id,batch.batch_name  FROM Attendance att, regtable reg,Student std,Batch batch "
							+ "where  reg.REG_ID = att.student_id and std.student_id = att.student_id and std.class_id = att.inst_id"
							+ " and std.div_id = att.div_id and att.inst_id=:inst_id  "
							+ "and  att_date>=:start_date and att_date<=:end_date and batch.batch_id = att.batch_id and batch.class_id = att.inst_id and batch.div_id = att.div_id"
							+ " group by att.student_id,att.batch_id,att.div_id "
							+ "order by att.student_id,att.batch_id,att.div_id";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);
			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public List getMonthlyLecturesCountForNotification(int inst_id, Date startDate,Date endDate) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = " SELECT count(distinct schedule_id),div_id,batch_id FROM Attendance "
							+"where inst_id=:inst_id and  att_date>=:start_date " +
							"and att_date<=:end_date group by div_id,batch_id";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);

			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public List getStudentsMonthlyPresentCountForNotification(int inst_id, Date startDate,Date endDate) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = " SELECT std.student_id,reg.fname,reg.lname,reg.phone1,reg.email,count(CASE WHEN att.presentee = 'P' THEN att.presentee ELSE NULL END),"
							+ "count(distinct att.schedule_id,att_date),std.parentFname,std.parentLname,"
							+ "std.parentPhone,std.parentEmail,att.div_id,std.batch_id,batch.batch_name  FROM Attendance att, regtable reg,Student std,Batch batch "
							+ "where  reg.REG_ID = att.student_id and std.student_id = att.student_id and std.class_id = att.inst_id"
							+ " and std.div_id = att.div_id and att.inst_id=:inst_id  "
							+ "and  att_date>=:start_date and att_date<=:end_date and batch.batch_id = att.batch_id and batch.class_id = att.inst_id and batch.div_id = att.div_id"
							+ " group by att.student_id,att.batch_id,att.div_id "
							+ "order by att.student_id,att.batch_id,att.div_id";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createSQLQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);
			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public List getStudentsDailyPresentCountForManulNotification(int inst_id,int div_id,int batch_id, Date att_date,List<Integer> studentIds) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = " SELECT std.student_id,reg.fname,reg.lname,reg.phone1,reg.email,count(CASE WHEN att.presentee = 'P' THEN att.presentee ELSE NULL END),"
							+ "count(distinct att.schedule_id),std.parentFname,std.parentLname,"
							+ "std.parentPhone,std.parentEmail,att.div_id,std.batch_id FROM Attendance att, RegisterBean reg,Student std "
							+"where  reg.regId = att.student_id and std.student_id = att.student_id and std.class_id = att.inst_id"
							+ " and std.div_id = att.div_id and att.inst_id=:inst_id and att.div_id =:div_id and att.batch_id=:batch_id and att.student_id in :list " +
							"and att.att_date=:att_date group by att.student_id,att.batch_id,att.div_id order by att.student_id,att.batch_id,att.div_id";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("att_date", att_date);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", batch_id);
			query.setParameterList("list", studentIds);
			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public List getStudentsWeeklyPresentCountForManulNotification(int inst_id,int div_id,int batch_id, Date startDate,Date endDate,List<Integer> studentIds) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString =" SELECT std.student_id,reg.fname,reg.lname,reg.phone1,reg.email,count(CASE WHEN att.presentee = 'P' THEN att.presentee ELSE NULL END),"
							+ "count(distinct att.schedule_id,att_date),std.parentFname,std.parentLname,"
							+ "std.parentPhone,std.parentEmail,att.div_id,std.batch_id  FROM attendance att, regtable reg,student std "
							+ "where  reg.REG_ID = att.student_id and std.student_id = att.student_id and std.class_id = att.inst_id"
							+ " and std.div_id = att.div_id and att.inst_id=:inst_id and att.div_id =:div_id and att.batch_id=:batch_id and att.student_id in :list "
							+ "and  att_date>=:start_date and att_date<=:end_date group by att.student_id,att.batch_id,att.div_id "
							+ "order by att.student_id,att.batch_id,att.div_id";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createSQLQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", batch_id);
			query.setParameterList("list", studentIds);
			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public List getStudentsMonthlyPresentCountForManulNotification(int inst_id,int div_id,int batch_id, Date startDate,Date endDate,List<Integer> studentIds) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString =" SELECT std.student_id,reg.fname,reg.lname,reg.phone1,reg.email,count(CASE WHEN att.presentee = 'P' THEN att.presentee ELSE NULL END),"
							+ "count(distinct att.schedule_id,att_date),std.parentFname,std.parentLname,"
							+ "std.parentPhone,std.parentEmail,att.div_id,std.batch_id  FROM attendance att, regtable reg,student std "
							+ "where  reg.REG_ID = att.student_id and std.student_id = att.student_id and std.class_id = att.inst_id"
							+ " and std.div_id = att.div_id and att.inst_id=:inst_id and att.div_id =:div_id and att.batch_id=:batch_id and att.student_id in :list "
							+ "and  att_date>=:start_date and att_date<=:end_date group by att.student_id,att.batch_id,att.div_id "
							+ "order by att.student_id,att.batch_id,att.div_id";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createSQLQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", batch_id);
			query.setParameterList("list", studentIds);
			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public List getStudentsDailyAttendanceForStudent(String batchname,
			int inst_id, int div_id, Date date,int student_id) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = "Select reg.fname,reg.lname, reg.regId,att.presentee,att.att_id, sch.start_time,sch.end_time,sub.subjectName,sub.subjectId,std.batchIdNRoll" +
				" from Student std,RegisterBean reg, Attendance att,Schedule sch,Subject sub "
				+ "where att.student_id=:student_id and (std.batch_id like :batch_id1 or std.batch_id like :batch_id2 or std.batch_id like :batch_id3 or std.batch_id = :batch_id4) "
				+ "and std.class_id=:class_id and std.div_id=:div_id and reg.regId = std.student_id and att.student_id = reg.regId and "
				+ "att.div_id = std.div_id and att.batch_id = :attbatch_id and att.att_date = :date and att.att_date = sch.date and  att.schedule_id = sch.schedule_id" +
				" and att.div_id = sch.div_id and att.batch_id = sch.batch_id and std.class_id = att.inst_id and std.class_id = sch.inst_id and att.inst_id = sch.inst_id" +
				" and sub.subjectId = att.sub_id order by reg.regId,sch.start_time ";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("batch_id1", batchname + ",%");
			query.setParameter("batch_id2", "%," + batchname + ",%");
			query.setParameter("batch_id3", "%," + batchname);
			query.setParameter("batch_id4", batchname);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("attbatch_id", Integer.parseInt(batchname));
			query.setParameter("date", date);
			query.setParameter("student_id", student_id);

			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	public List getStudentsWeeklyPresentCountForStudent(String batchname,
			int inst_id, int div_id, Date date,int student_id) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(startDate);
		 calendar.add(calendar.DATE, 6);
		//calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		String queryString = " SELECT reg.fname,reg.lname,count(att.presentee),att.att_date  ,att.student_id FROM Attendance att, RegisterBean reg "
							+"where att.student_id = :student_id and  reg.regId = att.student_id and att.inst_id=:inst_id and att.div_id = :div_id and att.batch_id = :batch_id and att.presentee ='P' and att.att_date>=:start_date " +
							"and att.att_date<=:end_date group by att.att_date,att.student_id order by att.student_id,att.att_date";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", Integer.parseInt(batchname));
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);
			query.setParameter("student_id", student_id);

			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public List getStudentsMonthlyPresentCountForStudent(String batchname,
			int inst_id, int div_id, Date date,int student_id) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(date);
		int lastdate = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		String queryString = " SELECT reg.fname,reg.lname,count(att.presentee),att.att_date  ,att.student_id FROM Attendance att, RegisterBean reg "
							+"where att.student_id = :student_id and  reg.regId = att.student_id and att.inst_id=:inst_id and att.div_id = :div_id and att.batch_id = :batch_id and att.presentee ='P' and att.att_date>=:start_date " +
							"and att.att_date<=:end_date group by att.att_date,att.student_id order by att.student_id,att.att_date";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("inst_id", inst_id);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", Integer.parseInt(batchname));
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);
			query.setParameter("student_id", student_id);
			list = query.list();
			if (list != null) {
				return list;
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		return null;
}
	
	public boolean deleteDaysBatchAttendance(int inst_id,int div_id,int batch_id,Date date) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("delete Attendance where  inst_id = :inst_id and div_id = :div_id and batch_id = :batch_id and att_date =:att_date");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		query.setParameter("att_date", date);
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
}
