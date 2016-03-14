package com.classapp.db.Schedule;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.batch.Batch;
import com.classapp.persistence.Constants;
import com.classapp.persistence.HibernateUtil;

public class ScheduleDB {
	public void addSchedule(Schedule schedule) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(schedule);
			transaction.commit();
		} catch (Exception e) {

			e.printStackTrace();
			if (null != transaction) {
				transaction.rollback();
			}
		} finally {
			if (null != session) {
				session.close();
			}
		}
	}

	public String putSchedule(Schedule schedule) {
		Session session = null;
		Transaction transaction = null;
		String result = "";

	/*	String isScheduleExists = isExistsLecture(schedule);
		if (isScheduleExists.equals("notexists")) {
			try {
				session = HibernateUtil.getSessionfactory().openSession();
				transaction = session.beginTransaction();

				session.save(schedule);
				transaction.commit();
				result = "SUCCESS";
			} catch (Exception e) {
				result = "Error while insert schedule for date " + schedule.getDate() + " due to " + e.getMessage();
				if (null != transaction) {
					transaction.rollback();
				}
			} finally {
				if (null != session) {
					session.close();
				}
			}
		} else {
			String teacherbusy = "";
			String lectureexists = "";
			String error[] = isScheduleExists.split(",");
			for (int i = 0; i < error.length; i++) {
				if (error[i].contains("teacher")) {
					if (teacherbusy.equals("")) {
						teacherbusy = error[i];
					} else {
						teacherbusy = teacherbusy + "," + error[i];
					}
				}
				if (error[i].contains("lecture")) {
					if (lectureexists.equals("")) {
						lectureexists = error[i];
					} else {
						lectureexists = lectureexists + "," + error[i];
					}
				}
			}

			result = teacherbusy + "," + lectureexists;

		}*/
		return result;
	}

	public void updateLecture(Schedule schedule) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"update Schedule set sub_id=:sub_id , teacher_id=:teacher_id , start_time=:start_time , end_time=:end_time , " +
					"date=:date, grp_id =:grp_id, rep_days=:rep_days where schedule_id=:schedule_id and inst_id=:inst_id " +
					"and div_id = :div_id and batch_id = :batch_id and date = :date");
			query.setParameter("sub_id", schedule.getSub_id());
			query.setParameter("teacher_id", schedule.getTeacher_id());
			query.setParameter("start_time", schedule.getStart_time());
			query.setParameter("end_time", schedule.getEnd_time());
			query.setParameter("date", schedule.getDate());
			query.setParameter("schedule_id", schedule.getSchedule_id());
			query.setParameter("inst_id", schedule.getInst_id());
			query.setParameter("batch_id", schedule.getBatch_id());
			query.setParameter("div_id", schedule.getDiv_id());
			query.setParameter("grp_id", schedule.getGrp_id());
			query.setParameter("rep_days", schedule.getRep_days());
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {

			e.printStackTrace();
			if (null != transaction) {
				transaction.rollback();
			}
		} finally {
			if (null != session) {
				session.close();
			}
		}
	}
	
	public String updateSchedule(Schedule schedule) {
		Session session = null;
		Transaction transaction = null;
		String result="";
		System.out.println("calling validate");
		String isScheduleExists="";
		System.out.println("isScheduleExists="+isScheduleExists);
		//String isScheduleExists="notexists";
		int count=0;
		if(isScheduleExists.equals("notexists")){
			try {
				session = HibernateUtil.getSessionfactory().openSession();
				transaction = session.beginTransaction();
				Query query = session.createQuery(
						"update Schedule set sub_id=:sub_id , teacher_id=:teacher_id , start_time=:start_time , end_time=:end_time , date=:date where schedule_id=:schedule_id and class_id=:class_id");
				query.setParameter("sub_id", schedule.getSub_id());
				query.setParameter("teacher_id", schedule.getTeacher_id());
				query.setParameter("start_time", schedule.getStart_time());
				query.setParameter("end_time", schedule.getEnd_time());
				query.setParameter("date", schedule.getDate());
				query.setParameter("schedule_id", schedule.getSchedule_id());
				query.setParameter("class_id", schedule.getInst_id());
				count=query.executeUpdate();
				transaction.commit();
				if(count==1){
					result="Updated";
				}
			} catch (Exception e) {
	
				e.printStackTrace();
				if (null != transaction) {
					transaction.rollback();
				}
			} finally {
				if (null != session) {
					session.close();
				}
			}
		}else {
			String teacherbusy = "";
			String lectureexists = "";
			String error[] = isScheduleExists.split(",");
			for (int i = 0; i < error.length; i++) {
				if (error[i].contains("teacher")) {
					if (teacherbusy.equals("")) {
						teacherbusy = error[i];
					} else {
						teacherbusy = teacherbusy + "," + error[i];
					}
				}
				if (error[i].contains("lecture")) {
					if (lectureexists.equals("")) {
						lectureexists = error[i];
					} else {
						lectureexists = lectureexists + "," + error[i];
					}
				}
			}

			result = teacherbusy + "," + lectureexists;

		}		
		
		return result;
	}

	public String validateBeforeUpdate(Schedule schedule,List<Date> dateList) {
		Session session = null;
		Transaction transaction = null;
		List scheduleList = null;
		String status ="";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"from Schedule where teacher_id =:teacherid and start_time=:starttime and end_time=:endtime " +
					"and date in :date and inst_id=:inst_id and  ( (div_id =:div_id and batch_id = :batch_id and schedule_id != :schedule_id) or" +
					" (div_id =:div_id and batch_id != :batch_id) or " +
					" (div_id !=:div_id ))");
			query.setParameter("teacherid", schedule.getTeacher_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameterList("date", dateList);
			query.setParameter("inst_id", schedule.getInst_id());
			query.setParameter("div_id", schedule.getDiv_id());
			query.setParameter("batch_id", schedule.getBatch_id());
			query.setParameter("schedule_id", schedule.getSchedule_id());
			scheduleList = query.list();
			if (scheduleList.size() > 0) {
				status =  "Teacher is busy";
			} else {
				query = session.createQuery(
						"from Schedule where teacher_id =:teacherid and ((start_time <= :starttime and end_time> :starttime) " +
						"OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) " +
						"and date in :date and inst_id=:inst_id and  ( (div_id =:div_id and batch_id = :batch_id and schedule_id != :schedule_id) or" +
					" (div_id =:div_id and batch_id != :batch_id) or " +
					" (div_id !=:div_id ))");
				query.setParameter("teacherid", schedule.getTeacher_id());
				query.setParameter("starttime", schedule.getStart_time());
				query.setParameter("endtime", schedule.getEnd_time());
				query.setParameterList("date", dateList);
				query.setParameter("inst_id", schedule.getInst_id());
				query.setParameter("div_id", schedule.getDiv_id());
				query.setParameter("batch_id", schedule.getBatch_id());
				query.setParameter("schedule_id", schedule.getSchedule_id());
				scheduleList = query.list();
				if (scheduleList.size() > 0) {
					status =  "Teacher is busy";
				}
			}

			query = session.createQuery(
					"from Schedule where batch_id =:batchid and ((start_time <= :starttime and end_time> :starttime) OR " +
					"(start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) " +
					"and date in :date and inst_id=:inst_id  and div_id=:div_id and schedule_id != :schedule_id");
			query.setParameter("batchid", schedule.getBatch_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameterList("date", dateList);
			query.setParameter("inst_id", schedule.getInst_id());
			query.setParameter("div_id", schedule.getDiv_id());
			query.setParameter("schedule_id", schedule.getSchedule_id());
			scheduleList = query.list();
			if (scheduleList.size() > 0) {
				if("".equals(status)){
					return "Batch is busy";
				}
				return "Teacher and batch are busy";
			} else {
				query = session.createQuery(
						"from Schedule where batch_id =:batchid and start_time=:starttime and end_time=:endtime " +
						"and date in :date and inst_id=:inst_id  and div_id=:div_id and schedule_id != :schedule_id");
				query.setParameter("batchid", schedule.getBatch_id());
				query.setParameter("starttime", schedule.getStart_time());
				query.setParameter("endtime", schedule.getEnd_time());
				query.setParameterList("date", dateList);
				query.setParameter("inst_id", schedule.getInst_id());
				query.setParameter("div_id", schedule.getDiv_id());
				query.setParameter("schedule_id", schedule.getSchedule_id());
				scheduleList = query.list();
				if (scheduleList.size() > 0) {
					if("".equals(status)){
						return "Batch is busy";
					}
					return "Teacher and batch are busy";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			if (null != transaction) {
				transaction.rollback();
			}

		} finally {
			if (null != session) {
				session.close();
			}
		}

		return status;

	}
	public String isExistsLecture(Schedule schedule,List<Date> dateList) {
		Session session = null;
		Transaction transaction = null;
		List scheduleList = null;
		String status ="";
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"from Schedule where teacher_id =:teacherid and start_time=:starttime and end_time=:endtime and date in :date and inst_id=:inst_id");
			query.setParameter("teacherid", schedule.getTeacher_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameterList("date", dateList);
			query.setParameter("inst_id", schedule.getInst_id());
			scheduleList = query.list();
			if (scheduleList.size() > 0) {
				status =  "Teacher is busy";
			} else {
				query = session.createQuery(
						"from Schedule where teacher_id =:teacherid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date in :date and inst_id=:inst_id");
				query.setParameter("teacherid", schedule.getTeacher_id());
				query.setParameter("starttime", schedule.getStart_time());
				query.setParameter("endtime", schedule.getEnd_time());
				query.setParameterList("date", dateList);
				query.setParameter("inst_id", schedule.getInst_id());
				scheduleList = query.list();
				if (scheduleList.size() > 0) {
					status =  "Teacher is busy";
				}
			}

			query = session.createQuery(
					"from Schedule where batch_id =:batchid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date in :date and inst_id=:inst_id  and div_id=:div_id");
			query.setParameter("batchid", schedule.getBatch_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameterList("date", dateList);
			query.setParameter("inst_id", schedule.getInst_id());
			query.setParameter("div_id", schedule.getDiv_id());
			scheduleList = query.list();
			if (scheduleList.size() > 0) {
				if("".equals(status)){
					return "Batch is busy";
				}
				return "Teacher and batch are busy";
			} else {
				query = session.createQuery(
						"from Schedule where batch_id =:batchid and start_time=:starttime and end_time=:endtime and date in :date and inst_id=:inst_id  and div_id=:div_id");
				query.setParameter("batchid", schedule.getBatch_id());
				query.setParameter("starttime", schedule.getStart_time());
				query.setParameter("endtime", schedule.getEnd_time());
				query.setParameterList("date", dateList);
				query.setParameter("inst_id", schedule.getInst_id());
				query.setParameter("div_id", schedule.getDiv_id());
				scheduleList = query.list();
				if (scheduleList.size() > 0) {
					if("".equals(status)){
						return "Batch is busy";
					}
					return "Teacher and batch are busy";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			if (null != transaction) {
				transaction.rollback();
			}

		} finally {
			if (null != session) {
				session.close();
			}
		}

		return status;

	}

	public String isTeacherUnavailable(Schedule schedule, List<Integer> scheduleids) {
		Session session = null;
		Transaction transaction = null;
		List scheduleList = null;

		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"from Schedule where teacher_id =:teacherid and start_time=:starttime and end_time=:endtime and date=:date and schedule_id not in :schedule_id and class_id=:class_id");
			query.setParameter("teacherid", schedule.getTeacher_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameter("date", schedule.getDate());
			query.setParameterList("schedule_id", scheduleids);
			query.setParameter("class_id", schedule.getInst_id());
			scheduleList = query.list();
			if (scheduleList.size() > 0) {
				return "teacher";
			} else {
				query = session.createQuery(
						"from Schedule where teacher_id =:teacherid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date=:date and schedule_id not in :schedule_id and class_id=:class_id");
				query.setParameter("teacherid", schedule.getTeacher_id());
				query.setParameter("starttime", schedule.getStart_time());
				query.setParameter("endtime", schedule.getEnd_time());
				query.setParameter("date", schedule.getDate());
				query.setParameterList("schedule_id", scheduleids);
				query.setParameter("class_id", schedule.getInst_id());
				scheduleList = query.list();
				if (scheduleList.size() > 0) {
					return "teacher";
				}
			}
			query = session.createQuery(
					"from Schedule where batch_id =:batchid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date=:date and schedule_id not in :schedule_id and div_id=:div_id and class_id=:class_id");
			query.setParameter("batchid", schedule.getBatch_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameter("date", schedule.getDate());
			query.setParameterList("schedule_id", scheduleids);
			query.setParameter("div_id", schedule.getDiv_id());
			query.setParameter("class_id", schedule.getInst_id());
			scheduleList = query.list();
			if (scheduleList.size() > 0) {
				return "lecture";
			} else {
				query = session.createQuery(
						"from Schedule where batch_id =:batchid and start_time=:starttime and end_time=:endtime and date=:date and schedule_id not in :schedule_id and div_id=:div_id and class_id=:class_id");
				query.setParameter("batchid", schedule.getBatch_id());
				query.setParameter("starttime", schedule.getStart_time());
				query.setParameter("endtime", schedule.getEnd_time());
				query.setParameter("date", schedule.getDate());
				query.setParameterList("schedule_id", scheduleids);
				query.setParameter("div_id", schedule.getDiv_id());
				query.setParameter("class_id", schedule.getInst_id());
				scheduleList = query.list();
				if (scheduleList.size() > 0) {
					return "lecture";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			if (null != transaction) {
				transaction.rollback();
			}

		} finally {
			if (null != session) {
				session.close();
			}
		}

		return "notexists";

	}

	public List<Schedule> getSchedule(int batchid, Date date, int inst_id, int div_id) {

		Session session = null;
		Transaction transaction = null;
		List<Schedule> scheduleList = null;

		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"from Schedule where batch_id=:batch_id  and class_id=:class_id and div_id=:div_id order by start_time");
			query.setParameter("batch_id", batchid);
		//	query.setParameter("date", date);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
			scheduleList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return scheduleList;
	}

	public List<Schedule> getWeeklySchedule(int batchid, Date date, int inst_id, int div_id) {

		Session session = null;
		Transaction transaction = null;
		List<Schedule> scheduleList = null;
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear() + 1900, date.getMonth(), date.getDate());
		cal.add(Calendar.DATE, 7);
		Date enddate = new Date(cal.getTimeInMillis());
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"from Schedule where batch_id=:batch_id and date>=:startdate and date<:enddate and class_id=:class_id and div_id=:div_id order by date,start_time");
			query.setParameter("batch_id", batchid);
			query.setParameter("startdate", date);
			query.setParameter("enddate", enddate);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
			scheduleList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return scheduleList;
	}

	public List<Schedule> getWeeklySchedule(Date date, int inst_id, int div_id) {

		Session session = null;
		Transaction transaction = null;
		List<Schedule> scheduleList = null;
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear() + 1900, date.getMonth(), date.getDate());
		cal.add(Calendar.DATE, 7);
		Date enddate = new Date(cal.getTimeInMillis());
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"from Schedule where date>=:startdate and date<:enddate and class_id=:class_id and div_id=:div_id order by date,start_time");
			query.setParameter("startdate", date);
			query.setParameter("enddate", enddate);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
			scheduleList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return scheduleList;
	}

	public List<Schedule> getTeachersSchedule(int classid, int teacherid, Date scheduledate) {

		Session session = null;
		Transaction transaction = null;
		List<Schedule> scheduleList = null;

		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"from Schedule where class_id=:class_id and teacher_id=:teacher_id and date=:date order by start_time");
			query.setParameter("class_id", classid);
			query.setParameter("teacher_id", teacherid);
			query.setParameter("date", scheduledate);
			scheduleList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return scheduleList;
	}

	public int deleteSchedule(int scheduleid, int inst_id) {

		Session session = null;
		Transaction transaction = null;
		int count=0;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session
					.createQuery("delete from Schedule where schedule_id=:schedule_id and class_id=:class_id");
			query.setParameter("schedule_id", scheduleid);
			query.setParameter("class_id", inst_id);
			count=query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return count;
	}
	
	public int deleteSchedule(Schedule schedule) {

		Session session = null;
		Transaction transaction = null;
		int count=0;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session
					.createQuery("delete from Schedule where schedule_id=:schedule_id and inst_id=:inst_id and div_id=:div_id and batch_id = :batch_id");
			query.setParameter("schedule_id", schedule.getSchedule_id());
			query.setParameter("inst_id", schedule.getInst_id());
			query.setParameter("div_id", schedule.getDiv_id());
			query.setParameter("batch_id", schedule.getBatch_id());
			count=query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return count;
	}
	
	public int deleteGroupSchedule(Schedule schedule) {

		Session session = null;
		Transaction transaction = null;
		int count=0;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session
					.createQuery("delete from Schedule where inst_id=:inst_id and div_id=:div_id and batch_id = :batch_id and grp_id=:grp_id");
			query.setParameter("inst_id", schedule.getInst_id());
			query.setParameter("div_id", schedule.getDiv_id());
			query.setParameter("batch_id", schedule.getBatch_id());
			query.setParameter("grp_id", schedule.getGrp_id());
			count=query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return count;
	}

	public List<Schedule> getScheduleForDate(Integer studentId, String date) {

		Session session = null;
		Transaction transaction = null;
		List<Schedule> scheduleList = null;
		Object object = new Object();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date2 = formatter.parse(date);
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"FROM Schedule where date = :date and class_id in(select class_id from Student where student_id=:studentId)");
			// Query query = session.createQuery("FROM Schedule where date =
			// '2014-10-04' and class_id = 34");
			query.setParameter("studentId", studentId);
			query.setParameter("date", date2);
			scheduleList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return scheduleList;
	}

	/* Pundlik Sarafdar */
	public HashMap<String, List> getStudentData(Integer studentId) {
		Session session = null;
		Transaction transaction = null;
		List batchList = null;
		List divisionList = null;
		List subjectList = null;
		List classList = null;

		HashMap<String, List> studentDataMap = new HashMap();
		Object object = new Object();
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query queryBatch = session.createQuery(
					"Select batch_name,batch_id from Batch where batch_id in(select batch_id from Student where student_id = :studentId )");
			queryBatch.setParameter("studentId", studentId);
			batchList = queryBatch.list();

			Query queryDivision = session.createQuery(
					"Select divisionName,divId from Division where divId in(select div_id from Student where student_id = :studentId )");
			queryDivision.setParameter("studentId", studentId);
			divisionList = queryDivision.list();

			Query querySubject = session.createQuery(
					"Select className,regId from RegisterBean where regId in(select class_id from Student where student_id = :studentId )");
			querySubject.setParameter("studentId", studentId);
			classList = querySubject.list();

			studentDataMap.put(Constants.BATCH_LIST, batchList);
			studentDataMap.put(Constants.DIVISION_LIST, divisionList);
			studentDataMap.put(Constants.CLASS_LIST, classList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return studentDataMap;

	}

	public int deleteSchedulerelatedtoteacher(int teacherid, int classid) {

		Session session = null;
		Transaction transaction = null;
		int count = 0;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session
					.createQuery("delete FROM Schedule where teacher_id = :teacher_id and class_id =:class_id");
			// Query query = session.createQuery("FROM Schedule where date =
			// '2014-10-04' and class_id = 34");
			query.setParameter("teacher_id", teacherid);
			query.setParameter("class_id", classid);

			count = query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return count;
	}

	public int deleteschedulerelatedtobatchsubject(int batchid, int subid, int div_id, int inst_id) {
		Session session = null;
		Transaction transaction = null;
		int count=0;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"delete FROM Schedule where batch_id = :batch_id and sub_id =:sub_id and class_id=:class_id and div_id=:div_id");
			// Query query = session.createQuery("FROM Schedule where date =
			// '2014-10-04' and class_id = 34");
			query.setParameter("batch_id", batchid);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);

			count=query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return count;
	}

	public int deleteschedulerelatedsubject(int subid) {
		Session session = null;
		Transaction transaction = null;
		int count=0;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete FROM Schedule where sub_id =:sub_id");
			query.setParameter("sub_id", subid);

			count=query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return count;
	}
	
	public int deleteSubjectSchedulesOfClass(int subjectId,int classId) {
		Session session = null;
		Transaction transaction = null;
		int count=0;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete FROM Schedule where sub_id =:sub_id and class_id=:class_id");
			// Query query = session.createQuery("FROM Schedule where date =
			// '2014-10-04' and class_id = 34");
			query.setParameter("sub_id", subjectId);
			query.setParameter("class_id", classId);
			count=query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return count;
	}

	public int deleteschedulerelatedtoteachersubject(int teacherid, int sub_id) {

		Session session = null;
		Transaction transaction = null;
		int count=0;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session
					.createQuery("delete FROM Schedule where teacher_id = :teacher_id and sub_id =:sub_id");
			// Query query = session.createQuery("FROM Schedule where date =
			// '2014-10-04' and class_id = 34");
			query.setParameter("teacher_id", teacherid);
			query.setParameter("sub_id", sub_id);
			count=query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return count;

	}

	public int deleteschedulerelatedtoclass(int classid) {

		Session session = null;
		Transaction transaction = null;
		int count=0;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete FROM Schedule where div_id = :classid");
			// Query query = session.createQuery("FROM Schedule where date =
			// '2014-10-04' and class_id = 34");
			query.setParameter("classid", classid);
			count=query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return count;

	}

	public int deleteschedulerelatedtoBatch(Batch batch) {

		Session session = null;
		Transaction transaction = null;
		int count=0;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"delete FROM Schedule where batch_id = :batch_id and class_id=:class_id and div_id=:div_id");
			
			query.setParameter("batch_id", batch.getBatch_id());
			query.setParameter("class_id", batch.getClass_id());
			query.setParameter("div_id", batch.getDiv_id());
			count=query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return count;

	}
	
	public int deleteAllSchedulesOfBatch(int batchId, int divId, int classId) {

		Session session = null;
		Transaction transaction = null;
		int count=0;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"delete FROM Schedule where batch_id = :batch_id and class_id=:class_id and div_id=:div_id");
			
			query.setParameter("batch_id", batchId);
			query.setParameter("class_id", classId);
			query.setParameter("div_id", divId);
			count=query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return count;

	}

	public List<Schedule> getTeachersWeeklySchedule(int classid, int teacherid, Date scheduledate) {

		Session session = null;
		Transaction transaction = null;
		List<Schedule> scheduleList = null;
		Calendar cal = Calendar.getInstance();
		cal.set(scheduledate.getYear() + 1900, scheduledate.getMonth(), scheduledate.getDate());
		cal.add(Calendar.DATE, 7);
		Date enddate = new Date(cal.getTimeInMillis());
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"from Schedule where class_id=:class_id and teacher_id=:teacher_id and date>=:startdate and date <:enddate order by date,start_time");
			query.setParameter("class_id", classid);
			query.setParameter("teacher_id", teacherid);
			query.setParameter("startdate", scheduledate);
			query.setParameter("enddate", enddate);
			scheduleList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return scheduleList;
	}

	public boolean deleteScheduler() {

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Schedule where date>=CURRENT_DATE-30");
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return true;
	}

	public List getScheduleForAttendance(int batchid, Date date, int inst_id, int div_id) {

		Session session = null;
		Transaction transaction = null;
		List<Schedule> scheduleList = null;

		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"select reg.fname, reg.lname ,sub.subjectName,sub.subjectId,schedule.schedule_id,schedule.start_time,schedule.end_time from Schedule schedule,Teacher teacher,Subject sub,RegisterBean reg " +
					"where schedule.inst_id=teacher.class_id and schedule.teacher_id = teacher.user_id and " +
					"teacher.user_id = reg.regId and sub.subjectId=schedule.sub_id and schedule.batch_id=:batch_id  and schedule.inst_id=:class_id and schedule.div_id=:div_id and schedule.date = :date order by schedule.start_time");
			query.setParameter("batch_id", batchid);
			query.setParameter("date", date);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
			scheduleList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return scheduleList;
	}
	
	public List getMonthSchedule(int batchid, Date date, int inst_id, int div_id) {

		Session session = null;
		Transaction transaction = null;
		List scheduleList = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(date);
		int lastdate = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"Select  div.divId , div.divisionName, div.stream,sub.subjectId,sub.subjectName,batch.batch_id,batch.batch_name,schedule.schedule_id," +
					"  schedule.date, schedule.start_time,schedule.end_time,reg.fname,reg.lname,schedule.teacher_id,schedule.grp_id,schedule.rep_days from Schedule schedule,Division div,Subject sub,Batch batch,RegisterBean reg " +
					"where div.divId=schedule.div_id and div.institute_id = schedule.inst_id and sub.subjectId = schedule.sub_id and " +
					" sub.institute_id = schedule.inst_id and batch.div_id = schedule.div_id and batch.class_id = schedule.inst_id and " +
					"batch.batch_id = schedule.batch_id and schedule.batch_id=:batch_id  and schedule.inst_id=:class_id and schedule.div_id=:div_id and " +
					" schedule.teacher_id=reg.regId and schedule.date >= :startDate and schedule.date <= :endDate order by schedule.start_time");
			query.setParameter("batch_id", batchid);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
			scheduleList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return scheduleList;
	}
	
	public List getMonthSchedule(int batchid, Date startDate,Date endDate, int inst_id, int div_id) {

		Session session = null;
		Transaction transaction = null;
		List scheduleList = null;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"Select div.divId , div.divisionName, div.stream,sub.subjectId,sub.subjectName,batch.batch_id,batch.batch_name,schedule.schedule_id," +
					"  schedule.date, schedule.start_time,schedule.end_time,reg.fname,reg.lname,schedule.teacher_id,schedule.grp_id,schedule.rep_days from Schedule schedule,Division div,Subject sub,Batch batch,RegisterBean reg " +
					"where div.divId=schedule.div_id and div.institute_id = schedule.inst_id and sub.subjectId = schedule.sub_id and " +
					" sub.institute_id = schedule.inst_id and batch.div_id = schedule.div_id and batch.class_id = schedule.inst_id and " +
					"batch.batch_id = schedule.batch_id and schedule.batch_id=:batch_id  and schedule.inst_id=:class_id and schedule.div_id=:div_id and " +
					"schedule.teacher_id=reg.regId and schedule.date >= :startDate and schedule.date <= :endDate order by schedule.start_time");
			query.setParameter("batch_id", batchid);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			query.setParameter("class_id", inst_id);
			query.setParameter("div_id", div_id);
			scheduleList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return scheduleList;
	}
	
	public int addGroup(Groups groups) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(groups);
			transaction.commit();
		} catch (Exception e) {

			e.printStackTrace();
			if (null != transaction) {
				transaction.rollback();
			}
		} finally {
			if (null != session) {
				session.close();
			}
		}
		return groups.getGrp_id();
	}
	
	public static void main(String[] args) {
		ScheduleDB db = new ScheduleDB();
		// db.getScheduleForDate(68, "2014-09-09");
		// db.getStudentData(68);
	}
}
