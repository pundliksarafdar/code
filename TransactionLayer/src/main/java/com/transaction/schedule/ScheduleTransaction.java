package com.transaction.schedule;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.batch.Batch;
import com.classapp.db.batch.division.Division;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterDB;
import com.classapp.db.Schedule.Groups;
import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Schedule.ScheduleDB;
import com.classapp.db.student.Student;
import com.classapp.db.subject.Subject;
import com.classapp.schedule.Scheduledata;
import com.service.beans.MonthlyScheduleServiceBean;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;

public class ScheduleTransaction {

	public String addLecture(String class_id,String batch_id,List sub_id,List teacher_id,
			List start_time,List end_time,List date,int div_id) {
		BatchTransactions batchTransactions=new BatchTransactions();
		Batch batch=batchTransactions.getBatch(Integer.parseInt(batch_id),Integer.parseInt(class_id),div_id);
		int i=0;
		for(i=0;i<sub_id.size();i++)
		{
		Schedule schedule=new Schedule();
		schedule.setBatch_id(Integer.parseInt(batch_id));
		schedule.setInst_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDiv_id(batch.getDiv_id());
		schedule.setEnd_time((Time)end_time.get(i));
		schedule.setStart_time((Time)start_time.get(i));
		schedule.setSub_id(Integer.parseInt((String)sub_id.get(i)));
		schedule.setTeacher_id(Integer.parseInt((String)teacher_id.get(i)));
		ScheduleDB scheduleDB=new ScheduleDB();
		scheduleDB.addSchedule(schedule);
		}
		return null;
	}
	
	public String isExistsLecture(String class_id,String batch_id,List sub_id,List teacher_id,
			List start_time,List end_time,List date,int divid) {
		
		int i=0;
		String status="";
		for(i=0;i<sub_id.size();i++)
		{
		Schedule schedule=new Schedule();
		schedule.setBatch_id(Integer.parseInt(batch_id));
		schedule.setInst_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDiv_id(divid);
		schedule.setEnd_time((Time)end_time.get(i));
		schedule.setStart_time((Time)start_time.get(i));
		schedule.setSub_id(Integer.parseInt((String)sub_id.get(i)));
		schedule.setTeacher_id(Integer.parseInt((String)teacher_id.get(i)));
		ScheduleDB scheduleDB=new ScheduleDB();
		/*String exists=scheduleDB.isExistsLecture(schedule);
		if(exists.equals("teacher"))
		{
			if(i==0)
			{
				status="teacher/"+i;
			}else{
			status=status+","+"teacher/"+i;
			}
		}else if(exists.equals("lecture"))
		{
			if(i==0)
			{
				status="lecture/"+i;
			}else{
			status=status+","+"lecture/"+i;
			}
		}*/
		}
		return status;
	}
	
	public String isTeacherUnavailable(String class_id,String batch_id,List sub_id,List teacher_id,
			List start_time,List end_time,List date,List scheduleids,int div_id) {
		ArrayList<Integer> scheduleid = new ArrayList<Integer>();

		for(int i = 0; i < scheduleids.size(); i++) {
			scheduleid.add(Integer.parseInt((String) scheduleids.get(i)));   
		}
		int i=0;
		String status="";
		for(i=0;i<sub_id.size();i++)
		{
		Schedule schedule=new Schedule();
		schedule.setBatch_id(Integer.parseInt(batch_id));
		schedule.setInst_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDiv_id(div_id);
		schedule.setEnd_time((Time)end_time.get(i));
		schedule.setStart_time((Time)start_time.get(i));
		schedule.setSub_id(Integer.parseInt((String)sub_id.get(i)));
		schedule.setTeacher_id(Integer.parseInt((String)teacher_id.get(i)));
		schedule.setSchedule_id(Integer.parseInt((String)scheduleids.get(i)));
		ScheduleDB scheduleDB=new ScheduleDB();
		String exists=scheduleDB.isTeacherUnavailable(schedule,scheduleid);
	//	String exists=scheduleDB.isExistsLecture(schedule);
		if(exists.equals("teacher"))
		{
			if(i==0)
			{
				status="teacher/"+i;
			}else{
			status=status+","+"teacher/"+i;
			}
		}else if(exists.equals("lecture"))
		{
			if(i==0)
			{
				status="lecture/"+i;
			}else{
			status=status+","+"lecture/"+i;
			}
		}
		}
		return status;
	}
	
	public List<Schedule> getSchedule(int batchid,Date date,int inst_id,int div_id) {
		List<Schedule> schedulelist=null;
		ScheduleDB db=new ScheduleDB();
		schedulelist=db.getSchedule(batchid,date,inst_id,div_id);
		return schedulelist;
		
	}
	
	public List<Schedule> getWeeklySchedule(int batchid,Date date,int inst_id,int div_id) {
		List<Schedule> schedulelist=null;
		ScheduleDB db=new ScheduleDB();
		schedulelist=db.getWeeklySchedule(batchid,date,inst_id,div_id);
		return schedulelist;
		
	}
	
	public List<Schedule> getWeeklySchedule(Date date,int inst_id,int div_id) {
		List<Schedule> schedulelist=null;
		ScheduleDB db=new ScheduleDB();
		schedulelist=db.getWeeklySchedule(date,inst_id,div_id);
		return schedulelist;
		
	}
	
	public List<MonthlyScheduleServiceBean> getTeachersSchedule(int classid,int teacherid, int month , int year) {
		List<Schedule> schedulelist=null;
		ScheduleDB db=new ScheduleDB();
		schedulelist=db.getTeachersSchedule(classid, teacherid,month,year);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		List<MonthlyScheduleServiceBean> serviceBeanList = new ArrayList<MonthlyScheduleServiceBean>();
		if(schedulelist!=null){
			for (Iterator iterator = schedulelist.iterator(); iterator
					.hasNext();) {
				Object[] object = (Object[]) iterator
						.next();
				MonthlyScheduleServiceBean bean = new MonthlyScheduleServiceBean();
				bean.setDivId(((Number) object[0]).intValue());
				bean.setDivname((String) object[1]+" "+object[2]);
				bean.setSubId(((Number) object[3]).intValue());
				bean.setSubjectname((String) object[4]);
				bean.setBatchId(((Number) object[5]).intValue());
				bean.setBatchName((String) object[6]);
				bean.setId(((Number) object[7]).intValue());
				bean.setDate((Date) object[8]);
				Timestamp timestamp = new Timestamp(((Date) object[8]).getYear(), ((Date) object[8]).getMonth(), ((Date) object[8]).getDate(), ((Time) object[9]).getHours(), ((Time) object[9]).getMinutes(), ((Time) object[9]).getSeconds(), ((Time) object[9]).getSeconds());
				bean.setStart(timestamp.getTime());
				timestamp = new Timestamp(((Date) object[8]).getYear(), ((Date) object[8]).getMonth(), ((Date) object[8]).getDate(), ((Time) object[10]).getHours(), ((Time) object[10]).getMinutes(), ((Time) object[10]).getSeconds(), ((Time) object[10]).getSeconds());
				bean.setEnd(timestamp.getTime());
				bean.setTeacher(object[11]+" "+object[12]);
				bean.setTeacher_id(((Number) object[13]).intValue());
				bean.setGrp_id(((Number) object[14]).intValue());
				if(bean.getGrp_id() != 0){
					bean.setDataClass("event-success");
				}else{
					bean.setDataClass("event-warning");
				}
				bean.setWeekTitle(bean.getSubjectname()+"<br/>"+dateFormat.format(new Date(bean.getStart()))+" - "+dateFormat.format(new Date(bean.getEnd())));
				bean.setTitle("Subject : "+bean.getSubjectname()+"<br/>"+
						"Start time : "+dateFormat.format(new Date(bean.getStart()))+"<br/>"+
						"End time : "+dateFormat.format(new Date(bean.getEnd()))+"<br/>");
				bean.setRep_days((String) object[15]);
				serviceBeanList.add(bean);
			}
		}
		
		return serviceBeanList;
		//return schedulelist;
		
	}
	
	public List<Schedule> getTeachersWeeklySchedule(int classid,int teacherid,Date scheduledate) {
		List<Schedule> schedulelist=null;
		ScheduleDB db=new ScheduleDB();
		schedulelist=db.getTeachersWeeklySchedule(classid, teacherid,scheduledate);
		return schedulelist;
		
	}
	
	public List<Schedule> getMonthlySchedule(int batchid,Date date,int inst_id,int div_id) {
		List<Schedule> schedulelist=null;
		ScheduleDB db=new ScheduleDB();
		schedulelist=db.getWeeklySchedule(batchid,date,inst_id,div_id);
		return schedulelist;
		
	}
	
	public int deleteSchedule(int scheduleid,int inst_id,int div_id,int batch_id,Date date,int grp_id)
	{
		Schedule schedule = new Schedule();
		schedule.setInst_id(inst_id);
		schedule.setDiv_id(div_id);
		schedule.setBatch_id(batch_id);
		schedule.setGrp_id(grp_id);
		ScheduleDB db=new ScheduleDB();
		if(grp_id != 0){
		return db.deleteGroupSchedule(schedule);
		}
		return db.deleteSchedule(scheduleid, inst_id, div_id, batch_id,date);
	}
	
	public int updateScheduleOfTeacher(int teacherId,int classId)
	{
		
		ScheduleDB db=new ScheduleDB();
		return db.updateSchedulerelatedtoteacher(teacherId, classId);
	}
	
	public int deleteScheduleOfBatchForSubject(int batchId,int subjectId, int divId, int classId)
	{
		
		ScheduleDB db=new ScheduleDB();
		return db.deleteschedulerelatedtobatchsubject(batchId, subjectId, divId, classId);
	}
	
	public int deleteSubjectSchedulesOfClass(int subjectId, int classId)
	{
		
		ScheduleDB db=new ScheduleDB();
		return db.deleteSubjectSchedulesOfClass(subjectId, classId);
	}
	
	public int deleteSubjectSchedulesOfTeacher(int subjectId, int teacherId)
	{
		
		ScheduleDB db=new ScheduleDB();
		return db.deleteschedulerelatedtoteachersubject(teacherId, subjectId);
	}
	
	public String updateLecture(String class_id,String batch_id,List sub_id,List teacher_id,
			List start_time,List end_time,List date,List scheduleid,int div_id) {
		
		int i=0;
		for(i=0;i<sub_id.size();i++)
		{
		Schedule schedule=new Schedule();
		schedule.setBatch_id(Integer.parseInt(batch_id));
		schedule.setInst_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDiv_id(div_id);
		schedule.setEnd_time((Time)end_time.get(i));
		schedule.setStart_time((Time)start_time.get(i));
		schedule.setSub_id(Integer.parseInt((String)sub_id.get(i)));
		schedule.setTeacher_id(Integer.parseInt((String)teacher_id.get(i)));
		schedule.setSchedule_id(Integer.parseInt((String) scheduleid.get(i)));
		ScheduleDB scheduleDB=new ScheduleDB();
		scheduleDB.updateLecture(schedule);
		}
		return null;
	}
	
	/*Pundlik*/
	public HashMap<String, List> getStudentData(Integer studentId){
		ScheduleDB scheduleDB = new ScheduleDB();
		HashMap<String, List> studentData = scheduleDB.getStudentData(studentId);
		return studentData;
	}
	
	public List<com.classapp.schedule.Schedule> getScheduleForDate(Integer studentId,String date) {
		ScheduleDB scheduleDB = new ScheduleDB();
		List<com.classapp.schedule.Schedule> scheduleDst = new ArrayList();
		List<Schedule> schedulesSrc = scheduleDB.getScheduleForDate(studentId, date);
		for(Schedule schedule : schedulesSrc){
			com.classapp.schedule.Schedule schedule2 = new com.classapp.schedule.Schedule();
			try {
				BeanUtils.copyProperties(schedule2, schedule);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			scheduleDst.add(schedule2);
		}
		return scheduleDst;
	}
	
	public void deleteschedulerelatedtobatchsubject(Batch batch,String subid) {
		ScheduleDB db=new ScheduleDB();
		String[] subids=subid.split(",");
		String[] batchsubids=batch.getSub_id().split(",");
		if(!batchsubids[0].equals("")){
		for (int i = 0; i < batchsubids.length; i++) {
			boolean flag=false;
			for (int j = 0; j < subids.length; j++) {
				if(batchsubids[i].equals(subids[j])){
					flag=true;
				}	
			}
			if(flag==false){
				db.deleteschedulerelatedtobatchsubject(batch.getBatch_id(), Integer.parseInt(batchsubids[i]),batch.getDiv_id(),batch.getClass_id());
			}
			
		}
		}
		
	}
	
	public int deleteschedulerelatedtoclass(int classid) {
		ScheduleDB db=new ScheduleDB();
		return db.deleteschedulerelatedtoclass(classid);		
	}
	
	
	public int deleteAllSchedulesOfBatch(int batchId, int divId, int classId) {
		ScheduleDB db=new ScheduleDB();
		return db.deleteAllSchedulesOfBatch(batchId, divId, classId);	
	}
	
	public int deleteschedulerelatedsubject(int inst_id,int subid) {
		ScheduleDB scheduleDB=new ScheduleDB();
		return scheduleDB.deleteschedulerelatedsubject(inst_id,subid);		
		
		
	}
	
	public boolean deleteSchedulerelatedoBatch(int  inst_id,int div_id,int batch_id) {
		ScheduleDB scheduleDB=new ScheduleDB();
		scheduleDB.deleteschedulerelatedtoBatch(inst_id, div_id, batch_id);
		return true;
	}
	
	public List<Scheduledata> gettodaysSchedule(List<Student> students){
		List<Scheduledata> scheduledatas=new ArrayList<Scheduledata>();
		if(students!=null){
			ScheduleDB scheduleDB=new ScheduleDB();
			List<List<Schedule>> list=new ArrayList<List<Schedule>>();
			for (int i = 0; i < students.size(); i++) {
				String batchid[]=students.get(i).getBatch_id().split(",");
				for (int k = 0; k < batchid.length; k++) {
				List<Schedule> schedule=scheduleDB.getSchedule(Integer.parseInt(batchid[k]), new Date(new java.util.Date().getTime()), students.get(i).getClass_id(), students.get(i).getDiv_id());
				list.add(schedule);
				}
			}
			if(list!=null && list.size()>0){
				BatchTransactions batchTransactions=new BatchTransactions();
				SubjectTransaction subjectTransaction=new SubjectTransaction();
				for (int i = 0; i < list.size(); i++) {
					if(null!=list.get(i)){
					for (int j = 0; j < list.get(i).size(); j++) {
						Batch batch=batchTransactions.getBatch(list.get(i).get(j).getBatch_id(), list.get(i).get(j).getInst_id(), list.get(i).get(j).getDiv_id());
						Subject subject=subjectTransaction.getSubject(list.get(i).get(j).getSub_id());
						Scheduledata scheduledata=new Scheduledata();
						scheduledata.setBatch_name(batch.getBatch_name());
						scheduledata.setStart_time(list.get(i).get(j).getStart_time());
						scheduledata.setEnd_time(list.get(i).get(j).getEnd_time());
						scheduledata.setInst_id(list.get(i).get(j).getInst_id());
						scheduledata.setSubject_name(subject.getSubjectName());
						scheduledatas.add(scheduledata);
					}
				}
				}
				}
		}
		return scheduledatas;
	}
	
	public List<Scheduledata> getteacherstodaysSchedule(List<Integer> inst_ids,int teachersid){
		List<Scheduledata> scheduledatas=new ArrayList<Scheduledata>();
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		if(inst_ids!=null){
			ScheduleDB scheduleDB=new ScheduleDB();
			List<List<Schedule>> list=new ArrayList<List<Schedule>>();
			for (int i = 0; i < inst_ids.size(); i++) {
				List<Schedule> schedules=scheduleDB.getTeachersSchedule(inst_ids.get(i), teachersid, new Date(new java.util.Date().getTime()));
				list.add(schedules);
			}
			if(list.size()>0){
				BatchTransactions batchTransactions=new BatchTransactions();
				RegisterDB db=new RegisterDB();
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).size(); j++) {
						Batch batch=batchTransactions.getBatch(list.get(i).get(j).getBatch_id(), list.get(i).get(j).getInst_id(), list.get(i).get(j).getDiv_id());
						Subject subject=subjectTransaction.getSubject(list.get(i).get(j).getSub_id());
						DivisionTransactions divisionTransactions=new DivisionTransactions();
						Division division=new Division();
						division=divisionTransactions.getDidvisionByID(list.get(i).get(j).getDiv_id());
						Scheduledata scheduledata=new Scheduledata();
						scheduledata.setBatch_name(batch.getBatch_name());
						scheduledata.setStart_time(list.get(i).get(j).getStart_time());
						scheduledata.setEnd_time(list.get(i).get(j).getEnd_time());
						scheduledata.setInst_id(list.get(i).get(j).getInst_id());
						scheduledata.setSubject_name(subject.getSubjectName());
						scheduledata.setDivision_name(division.getDivisionName());
						RegisterBean bean=db.getRegisterclass(list.get(i).get(j).getInst_id());
						scheduledata.setInst_name(bean.getClassName());
						scheduledatas.add(scheduledata);
					}
				}
			}
			
		}
		return scheduledatas;
	}
	
	public List<Scheduledata> getScheduleOfMonth(){
		return null;
	}
	
	public List<Scheduledata> getScheduleOfWeek(){
		java.util.Date date= new java.util.Date();
		//GregorianCalendar.
		return null;
	}
	
	public List<Scheduledata> getScheduleOfDay(){
		return null;
	}
	
	public List<Scheduledata> getScheduleInDateRange(Date startDate, Date endDate){
		return null;
	}
	
	public List<Scheduledata> getScheduleOfDayInTimeRange(Time startTime, Time endTime){
		return null;
	}

	public String addSchedule(com.service.beans.Schedule serviceSchedule,int inst_id) {
		ScheduleDB db = new ScheduleDB();
		String msg = "";
		List<Date> dateList = new ArrayList<Date>();
		if ("".equals(serviceSchedule.getRep_days())) {
			Schedule schedule = new Schedule();
			try {
				BeanUtils.copyProperties(schedule, serviceSchedule);
				schedule.setInst_id(inst_id);
				dateList.add(schedule.getDate());
				msg = db.isExistsLecture(schedule, dateList);
				if("".equals(msg)){
				db.addSchedule(schedule);
				}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			
			Groups groups = new Groups();
			groups.setInst_id(inst_id);
			groups.setDiv_id(serviceSchedule.getDiv_id());
			groups.setBatch_id(serviceSchedule.getBatch_id());
			groups.setStart_date(serviceSchedule.getStart_date());
			groups.setEnd_date(serviceSchedule.getEnd_date());
			int grp_id = db.addGroup(groups);
			String [] days = serviceSchedule.getRep_days().split(",");
			 dateList = getScheduleDates(serviceSchedule.getStart_date(), serviceSchedule.getEnd_date(), days);
			 if(dateList.size() != 0){
			Schedule tempSchedule = new Schedule();
			try {
				BeanUtils.copyProperties(tempSchedule, serviceSchedule);
				tempSchedule.setInst_id(inst_id);
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			msg = db.isExistsLecture(tempSchedule, dateList);
			if("".equals(msg)){
			for (Iterator iterator = dateList.iterator(); iterator.hasNext();) {
				Date date = (Date) iterator.next();
				Schedule schedule = new Schedule();
				try {
					BeanUtils.copyProperties(schedule, serviceSchedule);
					schedule.setGrp_id(grp_id);
					schedule.setInst_id(inst_id);
					schedule.setDate(date);
					db.addSchedule(schedule);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			 }else{
				 msg = "Invalid Date Range";
			 }
		}
		
		/*if(!"".equals(msg)){
			if(msg.contains("and")){
				msg = msg + " are busy";
			}else{
				msg = msg + " is busy";
			}
		}*/
		return msg;
	}
	
	public String updateSchedule(com.service.beans.Schedule serviceSchedule,int inst_id) {
		ScheduleDB db = new ScheduleDB();
		String msg = "";
		List<Date> dateList = new ArrayList<Date>();
		if ("".equals(serviceSchedule.getRep_days())) {
			Schedule schedule = new Schedule();
			try {
				BeanUtils.copyProperties(schedule, serviceSchedule);
				schedule.setInst_id(inst_id);
				dateList.add(schedule.getDate());
				msg = db.validateBeforeUpdate(schedule, dateList);
				if("".equals(msg)){
				db.deleteSchedule(schedule);
				schedule.setSchedule_id(0);
				db.addSchedule(schedule);
				}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if(serviceSchedule.getGrp_id() != 0){
				Schedule scheduleBean = new Schedule();
				try {
					BeanUtils.copyProperties(scheduleBean, serviceSchedule);
					scheduleBean.setInst_id(inst_id);
					db.deleteGroupSchedule(scheduleBean);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String [] days = serviceSchedule.getRep_days().split(",");
				dateList = getScheduleDates(serviceSchedule.getStart_date(), serviceSchedule.getEnd_date(), days);
				if(dateList.size() > 0){
				msg = db.validateBeforeUpdate(scheduleBean, dateList);
				if("".equals(msg)){
				for (Iterator iterator = dateList.iterator(); iterator.hasNext();) {
					Date date = (Date) iterator.next();
					Schedule schedule = new Schedule();
					try {
						BeanUtils.copyProperties(schedule, serviceSchedule);
						schedule.setGrp_id(serviceSchedule.getGrp_id());
						schedule.setInst_id(inst_id);
						schedule.setDate(date);
						db.addSchedule(schedule);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}
				}else{
					 msg = "Invalid Date Range";
				}
				
			}else{
				Schedule scheduleBean = new Schedule();
				try {
					BeanUtils.copyProperties(scheduleBean, serviceSchedule);
					scheduleBean.setInst_id(inst_id);
					db.deleteSchedule(scheduleBean);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			Groups groups = new Groups();
			groups.setInst_id(inst_id);
			groups.setDiv_id(serviceSchedule.getDiv_id());
			groups.setBatch_id(serviceSchedule.getBatch_id());
			groups.setStart_date(serviceSchedule.getStart_date());
			groups.setEnd_date(serviceSchedule.getEnd_date());
			int grp_id = db.addGroup(groups);
			String [] days = serviceSchedule.getRep_days().split(",");
		    dateList = getScheduleDates(serviceSchedule.getStart_date(), serviceSchedule.getEnd_date(), days);
		    if(dateList.size() > 0){
			msg = db.validateBeforeUpdate(scheduleBean, dateList);
			if("".equals(msg)){
			for (Iterator iterator = dateList.iterator(); iterator.hasNext();) {
				Date date = (Date) iterator.next();
				Schedule schedule = new Schedule();
				try {
					BeanUtils.copyProperties(schedule, serviceSchedule);
					schedule.setGrp_id(grp_id);
					schedule.setInst_id(inst_id);
					schedule.setDate(date);
					schedule.setSchedule_id(0);
					db.addSchedule(schedule);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			}else{
				
					 msg = "Invalid Date Range";	
			}
			}
		}
		
		/*if(!"".equals(msg)){
			if(msg.contains("and")){
				msg = msg + " are busy";
			}else{
				msg = msg + " is busy";
			}
		}*/
		return msg;
	}
	
	public HashMap<String, String> addScheduleByDays(Integer classid, Integer batchid, 
			Integer subject, Integer teacher, Time starttime,
			Time endtime, Date startDate, Date endDate, Integer divId, 
			boolean monday, boolean tuesday, boolean wednesday, boolean thursday, 
			boolean friday, boolean saturday, boolean sunday) {
		//BatchTransactions batchTransactions=new BatchTransactions();
		//Batch batch=batchTransactions.getBatch(batchid,classid,divId);
		HashMap<String, String> resultMap=new HashMap<String, String>();
		String result ="ERROR";
		ScheduleDB scheduleDB=new ScheduleDB();
		if (endDate != null) {
			Date scheduleDate = startDate;
			while (scheduleDate.compareTo(endDate) <= 0) {
				Calendar c = Calendar.getInstance();
				c.setTime(scheduleDate);
				int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				//boolean isSchuduleDay=false;
				if((dayOfWeek==1 && monday) || 
						(dayOfWeek==2 && tuesday) ||
						(dayOfWeek==3 && wednesday) ||
						(dayOfWeek==4 && thursday) ||
						(dayOfWeek==5 && friday) ||
						(dayOfWeek==6 && saturday) ||
						(dayOfWeek==7 && sunday)){
					result = scheduleDB.putSchedule(createScheduleData(classid, batchid, subject, teacher, starttime, endtime, scheduleDate, divId));
					String key=getAsString(classid.toString(), batchid.toString(), subject.toString(), teacher.toString(), starttime.toString(), endtime.toString(), scheduleDate.toString(), divId.toString());
					resultMap.put(key, result);	
					System.out.println("Result of schedule for "+dayOfWeek+" "+scheduleDate+" is "+result);
				}
						
									
					c.add(Calendar.DATE, 1);
				
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				scheduleDate = Date.valueOf(sdf.format(c.getTime())); 
			}
		}
		return resultMap;
	}
	
	private Schedule createScheduleData(Integer classid, Integer batchid, Integer subject, Integer teacher, Time starttime,
			Time endtime, Date date,Integer divId){
		Schedule schedule=new Schedule();
		schedule.setBatch_id(batchid);
		schedule.setInst_id(classid);
		schedule.setDate(date);
		schedule.setDiv_id(divId);
		schedule.setEnd_time(endtime);
		schedule.setStart_time(starttime);
		schedule.setSub_id(subject);
		schedule.setTeacher_id(teacher);
		
		return schedule;
	}
	
	private String getAsString(String...params){
		StringBuilder builder= new StringBuilder();
		for (String param : params) {
			builder.append(param);
		}		
		return builder.toString();
	}
	
	public String updateSchedule(Integer classid, Integer batchid, Integer subject, Integer teacher, Time starttime,
			Time endtime, Date date,Integer divId, Integer scheduleId){
			
		Schedule schedule=new Schedule();
		schedule.setBatch_id(batchid);
		schedule.setInst_id(classid);
		schedule.setDate(date);
		schedule.setDiv_id(divId);
		schedule.setEnd_time(endtime);
		schedule.setStart_time(starttime);
		schedule.setSub_id(subject);
		schedule.setTeacher_id(teacher);
		schedule.setSchedule_id(scheduleId);
		System.out.println("calling db..");
		ScheduleDB scheduleDB=new ScheduleDB();
		return scheduleDB.updateSchedule(schedule);
		 
	}
	
	public List getMonthSchedule(int batchid, Date date, int inst_id, int div_id) {
		ScheduleDB db = new ScheduleDB();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		List list =  db.getMonthSchedule(batchid, date, inst_id, div_id);
		List<MonthlyScheduleServiceBean> serviceBeanList = new ArrayList<MonthlyScheduleServiceBean>();
		List<Groups> groupList = db.getGroups(batchid, inst_id, div_id);
		if(list!=null){
			for (Iterator iterator = list.iterator(); iterator
					.hasNext();) {
				Object[] object = (Object[]) iterator
						.next();
				MonthlyScheduleServiceBean bean = new MonthlyScheduleServiceBean();
				bean.setDivId(((Number) object[0]).intValue());
				bean.setDivname((String) object[1]+" "+object[2]);
				bean.setSubId(((Number) object[3]).intValue());
				bean.setSubjectname((String) object[4]);
				bean.setBatchId(((Number) object[5]).intValue());
				bean.setBatchName((String) object[6]);
				bean.setId(((Number) object[7]).intValue());
				bean.setDate((Date) object[8]);
				Timestamp timestamp = new Timestamp(((Date) object[8]).getYear(), ((Date) object[8]).getMonth(), ((Date) object[8]).getDate(), ((Time) object[9]).getHours(), ((Time) object[9]).getMinutes(), ((Time) object[9]).getSeconds(), ((Time) object[9]).getSeconds());
				bean.setStart(timestamp.getTime());
				timestamp = new Timestamp(((Date) object[8]).getYear(), ((Date) object[8]).getMonth(), ((Date) object[8]).getDate(), ((Time) object[10]).getHours(), ((Time) object[10]).getMinutes(), ((Time) object[10]).getSeconds(), ((Time) object[10]).getSeconds());
				bean.setEnd(timestamp.getTime());
				bean.setTeacher(object[11]+" "+object[12]);
				bean.setTeacher_id(((Number) object[13]).intValue());
				bean.setGrp_id(((Number) object[14]).intValue());
				if(bean.getGrp_id() != 0){
					bean.setDataClass("event-success");
				}else{
					bean.setDataClass("event-warning");
				}
				bean.setWeekTitle(bean.getSubjectname()+"<br/>"+dateFormat.format(new Date(bean.getStart()))+" - "+dateFormat.format(new Date(bean.getEnd())));
				bean.setTitle("Subject : "+bean.getSubjectname()+"<br/>"+
						"Start time : "+dateFormat.format(new Date(bean.getStart()))+"<br/>"+
						"End time : "+dateFormat.format(new Date(bean.getEnd()))+"<br/>");
				bean.setRep_days((String) object[15]);
				if(((Number) object[14]).intValue() != 0 ){
				for (Iterator iterator2 = groupList.iterator(); iterator2
						.hasNext();) {
					Groups groups = (Groups) iterator2.next();
					if(groups.getGrp_id() == ((Number) object[14]).intValue()){
						bean.setStart_date(groups.getStart_date());
						bean.setEnd_date(groups.getEnd_date());
						break;
					}
				}
				}
				serviceBeanList.add(bean);
			}
		}
		
		return serviceBeanList;
	}
	
	public List getMonthSchedule(int batchid, Date startDate,Date endDate, int inst_id, int div_id) {
		ScheduleDB db = new ScheduleDB();
		List list =  db.getMonthSchedule(batchid, startDate, endDate, inst_id, div_id);
		List<MonthlyScheduleServiceBean> serviceBeanList = new ArrayList<MonthlyScheduleServiceBean>();
		List<Groups> groupList = db.getGroups(batchid, inst_id, div_id);
		if(list!=null){
			for (Iterator iterator = list.iterator(); iterator
					.hasNext();) {
				Object[] object = (Object[]) iterator
						.next();
				MonthlyScheduleServiceBean bean = new MonthlyScheduleServiceBean();
				bean.setDivId(((Number) object[0]).intValue());
				bean.setDivname((String) object[1]+" "+object[2]);
				bean.setSubId(((Number) object[3]).intValue());
				bean.setSubjectname((String) object[4]);
				bean.setBatchId(((Number) object[5]).intValue());
				bean.setBatchName((String) object[6]);
				bean.setId(((Number) object[7]).intValue());
				bean.setDate((Date) object[8]);
				Timestamp timestamp = new Timestamp(((Date) object[8]).getYear(), ((Date) object[8]).getMonth(), ((Date) object[8]).getDate(), ((Time) object[9]).getHours(), ((Time) object[9]).getMinutes(), ((Time) object[9]).getSeconds(), ((Time) object[9]).getSeconds());
				bean.setStart(timestamp.getTime());
				timestamp = new Timestamp(((Date) object[8]).getYear(), ((Date) object[8]).getMonth(), ((Date) object[8]).getDate(), ((Time) object[10]).getHours(), ((Time) object[10]).getMinutes(), ((Time) object[10]).getSeconds(), ((Time) object[10]).getSeconds());
				bean.setEnd(timestamp.getTime());
				bean.setTeacher(object[11]+" "+object[12]);
				bean.setTeacher_id(((Number) object[13]).intValue());
				bean.setGrp_id(((Number) object[14]).intValue());
				if(bean.getGrp_id() != 0){
					bean.setDataClass("event-success");
				}else{
					bean.setDataClass("event-warning");
				}
				bean.setRep_days((String) object[15]);
				if(((Number) object[14]).intValue() != 0 ){
					for (Iterator iterator2 = groupList.iterator(); iterator2
							.hasNext();) {
						Groups groups = (Groups) iterator2.next();
						if(groups.getGrp_id() == ((Number) object[14]).intValue()){
							bean.setStart_date(groups.getStart_date());
							bean.setEnd_date(groups.getEnd_date());
							break;
						}
					}
					}
				serviceBeanList.add(bean);
			}
		}
		
		return serviceBeanList;
	}
	
	private List<Date> getScheduleDates(Date start_date,Date end_date,String[] days) {
		Calendar calendar = Calendar.getInstance();
		List<Date> dateList = new ArrayList<Date>();
		for (int i = 0; i < days.length; i++) {
		calendar.setTime(start_date);
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		int day = Integer.parseInt(days[i]);
		if(weekday > day){
			weekday = (day+7)-weekday;
			calendar.add(Calendar.DAY_OF_YEAR, weekday);
		}if(weekday < day)
		{
			weekday = day - weekday ;
			calendar.add(Calendar.DAY_OF_YEAR, weekday);
		}
		 
		 Date date = new Date(calendar.getTime().getTime());
		 System.out.println(date.toString());
		 if(date.after(end_date)){
				continue;
			}else{
				dateList.add(date);
			}
		 boolean flag = true;
		 while (flag) {
			    calendar.add(Calendar.DAY_OF_YEAR, 7);
			     date = new Date(calendar.getTime().getTime());
				System.out.println(date.toString());
				if(date.after(end_date)){
					flag = false;
				}else{
					dateList.add(date);
				}
				}	
		}
		return dateList;
}
}
