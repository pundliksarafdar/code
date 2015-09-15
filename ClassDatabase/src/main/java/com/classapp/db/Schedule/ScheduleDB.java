package com.classapp.db.Schedule;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.batch.Batch;
import com.classapp.persistence.Constants;
import com.classapp.persistence.HibernateUtil;

public class ScheduleDB {
public void addLecture(Schedule schedule) {
	Session session = null;
	Transaction transaction = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("select IFNULL(max(schedule_id),1)+1 from Schedule where class_id=:class_id");
		query.setParameter("class_id", schedule.getClass_id());
		List<Integer> schedule_id=query.list();
		schedule.setSchedule_id(schedule_id.get(0));
		session.saveOrUpdate(schedule);
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

public void updateLecture(Schedule schedule) {
	Session session = null;
	Transaction transaction = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("update Schedule set sub_id=:sub_id , teacher_id=:teacher_id , start_time=:start_time , end_time=:end_time , day_id=:day_id , date=:date where schedule_id=:schedule_id and class_id=:class_id");
		query.setParameter("sub_id", schedule.getSub_id());
		query.setParameter("teacher_id", schedule.getTeacher_id());
		query.setParameter("start_time", schedule.getStart_time());
		query.setParameter("end_time", schedule.getEnd_time());
		query.setParameter("day_id", schedule.getDay_id());
		query.setParameter("date", schedule.getDate());
		query.setParameter("schedule_id", schedule.getSchedule_id());
		query.setParameter("class_id", schedule.getClass_id());
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
}

public String isExistsLecture(Schedule schedule) {
	Session session = null;
	Transaction transaction = null;
	List scheduleList = null;
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("from Schedule where teacher_id =:teacherid and start_time=:starttime and end_time=:endtime and date=:date and class_id=:class_id");
		query.setParameter("teacherid", schedule.getTeacher_id());
		query.setParameter("starttime", schedule.getStart_time());
		query.setParameter("endtime", schedule.getEnd_time());
		query.setParameter("date", schedule.getDate());
		query.setParameter("class_id", schedule.getClass_id());
		scheduleList = query.list();
		if(scheduleList.size()>0){
			return "teacher";
		}
		else{
			query = session.createQuery("from Schedule where teacher_id =:teacherid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date=:date and class_id=:class_id");
			query.setParameter("teacherid", schedule.getTeacher_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameter("date", schedule.getDate());
			query.setParameter("class_id", schedule.getClass_id());
			scheduleList = query.list();
			if(scheduleList.size()>0){
				return "teacher";
			}
		}
		
		query = session.createQuery("from Schedule where batch_id =:batchid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date=:date and class_id=:class_id");
		query.setParameter("batchid", schedule.getBatch_id());
		query.setParameter("starttime", schedule.getStart_time());
		query.setParameter("endtime", schedule.getEnd_time());
		query.setParameter("date", schedule.getDate());
		query.setParameter("class_id", schedule.getClass_id());
		scheduleList = query.list();
		if(scheduleList.size()>0){
			return "lecture";
		}else{
			query = session.createQuery("from Schedule where batch_id =:batchid and start_time=:starttime and end_time=:endtime and date=:date and class_id=:class_id");
			query.setParameter("batchid", schedule.getBatch_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameter("date", schedule.getDate());
			query.setParameter("class_id", schedule.getClass_id());
			scheduleList = query.list();
			if(scheduleList.size()>0){
				return "lecture";
			}
			
		}
		
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}
	 
	return "notexists";

	}

public String isTeacherUnavailable(Schedule schedule) {
	Session session = null;
	Transaction transaction = null;
	List scheduleList = null;
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("from Schedule where teacher_id =:teacherid and start_time=:starttime and end_time=:endtime and date=:date and schedule_id !=:schedule_id and class_id=:class_id");
		query.setParameter("teacherid", schedule.getTeacher_id());
		query.setParameter("starttime", schedule.getStart_time());
		query.setParameter("endtime", schedule.getEnd_time());
		query.setParameter("date", schedule.getDate());
		query.setParameter("schedule_id", schedule.getSchedule_id());
		query.setParameter("class_id", schedule.getClass_id());
		scheduleList = query.list();
		if(scheduleList.size()>0){
			return "teacher";
		}
		else{
			query = session.createQuery("from Schedule where teacher_id =:teacherid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date=:date and schedule_id !=:schedule_id and class_id=:class_id");
			query.setParameter("teacherid", schedule.getTeacher_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameter("date", schedule.getDate());
			query.setParameter("schedule_id", schedule.getSchedule_id());
			query.setParameter("class_id", schedule.getClass_id());
			scheduleList = query.list();
			if(scheduleList.size()>0){
				return "teacher";
			}
		}
		query = session.createQuery("from Schedule where batch_id =:batchid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date=:date and schedule_id!=:schedule_id and div_id=:div_id and class_id=:class_id");
		query.setParameter("batchid", schedule.getBatch_id());
		query.setParameter("starttime", schedule.getStart_time());
		query.setParameter("endtime", schedule.getEnd_time());
		query.setParameter("date", schedule.getDate());
		query.setParameter("schedule_id", schedule.getSchedule_id());
		query.setParameter("div_id", schedule.getDiv_id());
		query.setParameter("class_id", schedule.getClass_id());
		scheduleList = query.list();
		if(scheduleList.size()>0){
			return "lecture";
		}else{
			query = session.createQuery("from Schedule where batch_id =:batchid and start_time=:starttime and end_time=:endtime and date=:date and schedule_id!=:schedule_id and div_id=:div_id and class_id=:class_id");
			query.setParameter("batchid", schedule.getBatch_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameter("date", schedule.getDate());
			query.setParameter("schedule_id", schedule.getSchedule_id());
			query.setParameter("div_id", schedule.getDiv_id());
			query.setParameter("class_id", schedule.getClass_id());
			scheduleList = query.list();
			if(scheduleList.size()>0){
				return "lecture";
			}
			
		}
		
	}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
	}
	 
	return "notexists";

	}

public List<Schedule> getSchedule(int batchid,Date date,int inst_id,int div_id) {
	
	Session session = null;
	Transaction transaction = null;
	List<Schedule> scheduleList = null;
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("from Schedule where batch_id=:batch_id and date=:date and class_id=:class_id and div_id=:div_id");
		query.setParameter("batch_id", batchid);
		query.setParameter("date", date);
		query.setParameter("class_id", inst_id);
		query.setParameter("div_id", div_id);
		scheduleList = query.list();
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	return scheduleList;
}

public List<Schedule> getTeachersSchedule(int classid,int teacherid,Date scheduledate) {
	
	Session session = null;
	Transaction transaction = null;
	List<Schedule> scheduleList = null;
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("from Schedule where class_id=:class_id and teacher_id=:teacher_id and date=:date");
		query.setParameter("class_id", classid);
		query.setParameter("teacher_id", teacherid);
		query.setParameter("date", scheduledate);
		scheduleList = query.list();
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	return scheduleList;
}

public boolean deleteSchedule(int scheduleid,int inst_id) {
	
	Session session = null;
	Transaction transaction = null;
	
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("delete from Schedule where schedule_id=:schedule_id and class_id=:class_id");
		query.setParameter("schedule_id", scheduleid);
		query.setParameter("class_id", inst_id);
		query.executeUpdate();
		transaction.commit();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	return true;
}


public List<Schedule> getScheduleForDate(Integer studentId,String date) {
	
	Session session = null;
	Transaction transaction = null;
	List<Schedule> scheduleList = null;
	Object object = new Object();
	try{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date2 = formatter.parse(date);
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("FROM Schedule where date = :date and class_id in(select class_id from Student where student_id=:studentId)");
		//Query query = session.createQuery("FROM Schedule where date = '2014-10-04' and class_id = 34");
		query.setParameter("studentId", studentId);
		query.setParameter("date", date2);
		scheduleList = query.list();
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	return scheduleList;
}

/*Pundlik Sarafdar*/
public HashMap<String, List> getStudentData(Integer studentId){
	Session session = null;
	Transaction transaction = null;
	List batchList = null;
	List divisionList = null;
	List subjectList = null;
	List classList = null;
	
	HashMap<String, List> studentDataMap = new HashMap();
	Object object = new Object();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query queryBatch = session.createQuery("Select batch_name,batch_id from Batch where batch_id in(select batch_id from Student where student_id = :studentId )");
		queryBatch.setParameter("studentId", studentId);
		batchList = queryBatch.list();
		
		Query queryDivision = session.createQuery("Select divisionName,divId from Division where divId in(select div_id from Student where student_id = :studentId )");
		queryDivision.setParameter("studentId", studentId);
		divisionList = queryDivision.list();
		
		
		Query querySubject = session.createQuery("Select className,regId from RegisterBean where regId in(select class_id from Student where student_id = :studentId )");
		querySubject.setParameter("studentId", studentId);
		classList = querySubject.list();
		
		
		studentDataMap.put(Constants.BATCH_LIST, batchList);
		studentDataMap.put(Constants.DIVISION_LIST, divisionList);
		studentDataMap.put(Constants.CLASS_LIST, classList);
		
	}catch(Exception e)
		{
			e.printStackTrace();
		}
	return studentDataMap;
	
}

public List<Schedule> deleteSchedulerelatedtoteacher(int teacherid,int classid) {
	
	Session session = null;
	Transaction transaction = null;
	List<Schedule> scheduleList = null;
	Object object = new Object();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("delete FROM Schedule where teacher_id = :teacher_id and class_id =:class_id");
		//Query query = session.createQuery("FROM Schedule where date = '2014-10-04' and class_id = 34");
		query.setParameter("teacher_id", teacherid);
		query.setParameter("class_id", classid);
		
	query.executeUpdate();
		transaction.commit();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	return scheduleList;
}

public void deleteschedulerelatedtobatchsubject(int batchid,int subid,int div_id,int inst_id) {
	Session session = null;
	Transaction transaction = null;
	List<Schedule> scheduleList = null;
	Object object = new Object();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("delete FROM Schedule where batch_id = :batch_id and sub_id =:sub_id and class_id=:class_id and div_id=:div_id");
		//Query query = session.createQuery("FROM Schedule where date = '2014-10-04' and class_id = 34");
		query.setParameter("batch_id", batchid);
		query.setParameter("class_id", inst_id);
		query.setParameter("div_id", div_id);
		
	query.executeUpdate();
		transaction.commit();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
}

public boolean deleteschedulerelatedsubject(int subid) {
	Session session = null;
	Transaction transaction = null;
	List<Schedule> scheduleList = null;
	Object object = new Object();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("delete FROM Schedule where sub_id =:sub_id");
		//Query query = session.createQuery("FROM Schedule where date = '2014-10-04' and class_id = 34");
		query.setParameter("sub_id", subid);
		
	query.executeUpdate();
		transaction.commit();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	return true;
}

public boolean deleteschedulerelatedtoteachersubject(int teacherid,int sub_id) {
	
	Session session = null;
	Transaction transaction = null;
	List<Schedule> scheduleList = null;
	Object object = new Object();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("delete FROM Schedule where teacher_id = :teacher_id and sub_id =:sub_id");
		//Query query = session.createQuery("FROM Schedule where date = '2014-10-04' and class_id = 34");
		query.setParameter("teacher_id", teacherid);
		query.setParameter("sub_id", sub_id);	
	query.executeUpdate();
		transaction.commit();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	return true;
	
}
public boolean deleteschedulerelatedtoclass(int classid) {
	
	Session session = null;
	Transaction transaction = null;
	List<Schedule> scheduleList = null;
	Object object = new Object();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("delete FROM Schedule where div_id = :classid");
		//Query query = session.createQuery("FROM Schedule where date = '2014-10-04' and class_id = 34");
		query.setParameter("classid", classid);	
		query.executeUpdate();
		transaction.commit();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	return true;
	
}

public boolean deleteschedulerelatedtoBatch(Batch batch) {
	
	Session session = null;
	Transaction transaction = null;
	List<Schedule> scheduleList = null;
	Object object = new Object();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("delete FROM Schedule where batch_id = :batch_id and class_id=:class_id and div_id=:div_id");
		//Query query = session.createQuery("FROM Schedule where date = '2014-10-04' and class_id = 34");
		query.setParameter("batch_id", batch.getBatch_id());	
		query.setParameter("class_id", batch.getClass_id());
		query.setParameter("div_id", batch.getDiv_id());
		query.executeUpdate();
		transaction.commit();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	return true;
	
}

public static void main(String[] args) {
	ScheduleDB db = new ScheduleDB();
	//db.getScheduleForDate(68, "2014-09-09");
	//db.getStudentData(68);
}
}
