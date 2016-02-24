package com.classapp.db.schedule;

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
			Query query = session
					.createQuery("select IFNULL(max(schedule_id),1)+1 from Schedule where class_id=:class_id");
			query.setParameter("class_id", schedule.getClass_id());
			List<Integer> schedule_id = query.list();
			schedule.setSchedule_id(schedule_id.get(0));
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

		String isScheduleExists = isExistsLecture(schedule);
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

		}
		return result;
	}

	public void updateLecture(Schedule schedule) {
		Session session = null;
		Transaction transaction = null;
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
			query.setParameter("class_id", schedule.getClass_id());
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
		String isScheduleExists=validateBeforeUpdate(schedule);
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
				query.setParameter("class_id", schedule.getClass_id());
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

	public String validateBeforeUpdate(Schedule schedule) {
		Session session = null;
		Transaction transaction = null;
		List scheduleList = null;

		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"from Schedule where teacher_id =:teacherid and start_time=:starttime and end_time=:endtime and date=:date and class_id=:class_id and schedule_id!=:schedule_id");
			query.setParameter("teacherid", schedule.getTeacher_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameter("date", schedule.getDate());
			query.setParameter("class_id", schedule.getClass_id());
			query.setParameter("schedule_id", schedule.getSchedule_id());
			scheduleList = query.list();
			if (scheduleList.size() > 0) {
				return "teacher";
			} else {
				query = session.createQuery(
						"from Schedule where teacher_id =:teacherid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date=:date and class_id=:class_id and schedule_id!=:schedule_id");
				query.setParameter("teacherid", schedule.getTeacher_id());
				query.setParameter("starttime", schedule.getStart_time());
				query.setParameter("endtime", schedule.getEnd_time());
				query.setParameter("date", schedule.getDate());
				query.setParameter("class_id", schedule.getClass_id());
				query.setParameter("schedule_id", schedule.getSchedule_id());
				scheduleList = query.list();
				if (scheduleList.size() > 0) {
					return "teacher";
				}
			}

			query = session.createQuery(
					"from Schedule where batch_id =:batchid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date=:date and class_id=:class_id  and div_id=:div_id and schedule_id!=:schedule_id");
			query.setParameter("batchid", schedule.getBatch_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameter("date", schedule.getDate());
			query.setParameter("class_id", schedule.getClass_id());
			query.setParameter("div_id", schedule.getDiv_id());
			query.setParameter("schedule_id", schedule.getSchedule_id());
			scheduleList = query.list();
			if (scheduleList.size() > 0) {
				return "lecture";
			} else {
				query = session.createQuery(
						"from Schedule where batch_id =:batchid and start_time=:starttime and end_time=:endtime and date=:date and class_id=:class_id  and div_id=:div_id and schedule_id!=:schedule_id");
				query.setParameter("batchid", schedule.getBatch_id());
				query.setParameter("starttime", schedule.getStart_time());
				query.setParameter("endtime", schedule.getEnd_time());
				query.setParameter("date", schedule.getDate());
				query.setParameter("class_id", schedule.getClass_id());
				query.setParameter("div_id", schedule.getDiv_id());
				query.setParameter("schedule_id", schedule.getSchedule_id());
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
	public String isExistsLecture(Schedule schedule) {
		Session session = null;
		Transaction transaction = null;
		List scheduleList = null;

		try {
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(
					"from Schedule where teacher_id =:teacherid and start_time=:starttime and end_time=:endtime and date=:date and class_id=:class_id");
			query.setParameter("teacherid", schedule.getTeacher_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameter("date", schedule.getDate());
			query.setParameter("class_id", schedule.getClass_id());
			scheduleList = query.list();
			if (scheduleList.size() > 0) {
				return "teacher";
			} else {
				query = session.createQuery(
						"from Schedule where teacher_id =:teacherid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date=:date and class_id=:class_id");
				query.setParameter("teacherid", schedule.getTeacher_id());
				query.setParameter("starttime", schedule.getStart_time());
				query.setParameter("endtime", schedule.getEnd_time());
				query.setParameter("date", schedule.getDate());
				query.setParameter("class_id", schedule.getClass_id());
				scheduleList = query.list();
				if (scheduleList.size() > 0) {
					return "teacher";
				}
			}

			query = session.createQuery(
					"from Schedule where batch_id =:batchid and ((start_time <= :starttime and end_time> :starttime) OR (start_time < :endtime and end_time>= :endtime) OR (start_time > :starttime and end_time< :endtime)) and date=:date and class_id=:class_id  and div_id=:div_id");
			query.setParameter("batchid", schedule.getBatch_id());
			query.setParameter("starttime", schedule.getStart_time());
			query.setParameter("endtime", schedule.getEnd_time());
			query.setParameter("date", schedule.getDate());
			query.setParameter("class_id", schedule.getClass_id());
			query.setParameter("div_id", schedule.getDiv_id());
			scheduleList = query.list();
			if (scheduleList.size() > 0) {
				return "lecture";
			} else {
				query = session.createQuery(
						"from Schedule where batch_id =:batchid and start_time=:starttime and end_time=:endtime and date=:date and class_id=:class_id  and div_id=:div_id");
				query.setParameter("batchid", schedule.getBatch_id());
				query.setParameter("starttime", schedule.getStart_time());
				query.setParameter("endtime", schedule.getEnd_time());
				query.setParameter("date", schedule.getDate());
				query.setParameter("class_id", schedule.getClass_id());
				query.setParameter("div_id", schedule.getDiv_id());
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
			query.setParameter("class_id", schedule.getClass_id());
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
				query.setParameter("class_id", schedule.getClass_id());
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
			query.setParameter("class_id", schedule.getClass_id());
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
				query.setParameter("class_id", schedule.getClass_id());
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
					"from Schedule where batch_id=:batch_id and date=:date and class_id=:class_id and div_id=:div_id order by start_time");
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

	public static void main(String[] args) {
		ScheduleDB db = new ScheduleDB();
		// db.getScheduleForDate(68, "2014-09-09");
		// db.getStudentData(68);
	}
}
