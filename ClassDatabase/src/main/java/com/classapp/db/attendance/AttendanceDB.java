package com.classapp.db.attendance;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
	
	public List getStudentsDailyAttendance(String batchname,
			int inst_id, int div_id, Date date) {
		Session session = null;
		boolean status = false;
		Transaction transaction = null;
		List list = null;
		String queryString = "Select reg.fname,reg.lname, reg.regId,att.presentee,att.att_id, sch.start_time,sch.end_time,sub.subjectName,sub.subjectId" +
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
		String queryString = " SELECT reg.fname,reg.lname,count(att.presentee),att.att_date  ,att.student_id FROM Attendance att, RegisterBean reg "
							+"where  reg.regId = att.student_id and att.inst_id=:inst_id and att.div_id = :div_id and att.batch_id = :batch_id and att.presentee ='P' and att.att_date>=:start_date " +
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
		String queryString = " SELECT reg.fname,reg.lname,count(att.presentee),att.att_date  ,att.student_id FROM Attendance att, RegisterBean reg "
							+"where  reg.regId = att.student_id and att.inst_id=:inst_id and att.div_id = :div_id and att.batch_id = :batch_id and att.presentee ='P' and att.att_date>=:start_date " +
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
	
}
