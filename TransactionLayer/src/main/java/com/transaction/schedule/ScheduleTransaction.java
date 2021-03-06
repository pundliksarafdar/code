package com.transaction.schedule;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.Schedule.ScheduleDB;
import com.classapp.db.batch.Batch;
import com.classapp.db.batch.division.Division;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterDB;
import com.classapp.db.student.Student;
import com.classapp.db.subject.Subject;
import com.classapp.schedule.Scheduledata;
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
		schedule.setClass_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDay_id(2);
		schedule.setDiv_id(batch.getDiv_id());
		schedule.setEnd_time((Time)end_time.get(i));
		schedule.setStart_time((Time)start_time.get(i));
		schedule.setSub_id(Integer.parseInt((String)sub_id.get(i)));
		schedule.setTeacher_id(Integer.parseInt((String)teacher_id.get(i)));
		ScheduleDB scheduleDB=new ScheduleDB();
		scheduleDB.addLecture(schedule);
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
		schedule.setClass_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDay_id(2);
		schedule.setDiv_id(divid);
		schedule.setEnd_time((Time)end_time.get(i));
		schedule.setStart_time((Time)start_time.get(i));
		schedule.setSub_id(Integer.parseInt((String)sub_id.get(i)));
		schedule.setTeacher_id(Integer.parseInt((String)teacher_id.get(i)));
		ScheduleDB scheduleDB=new ScheduleDB();
		String exists=scheduleDB.isExistsLecture(schedule);
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
		schedule.setClass_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDay_id(2);
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
	
	public List<Schedule> getTeachersSchedule(int classid,int teacherid,Date scheduledate) {
		List<Schedule> schedulelist=null;
		ScheduleDB db=new ScheduleDB();
		schedulelist=db.getTeachersSchedule(classid, teacherid,scheduledate);
		return schedulelist;
		
	}
	
	public List<Schedule> getTeachersWeeklySchedule(int classid,int teacherid,Date scheduledate) {
		List<Schedule> schedulelist=null;
		ScheduleDB db=new ScheduleDB();
		schedulelist=db.getTeachersWeeklySchedule(classid, teacherid,scheduledate);
		return schedulelist;
		
	}
	
	public boolean deleteSchedule(int scheduleid,int inst_id)
	{
		
		ScheduleDB db=new ScheduleDB();
		db.deleteSchedule(scheduleid,inst_id);
		return true;
	}
	
	public String updateLecture(String class_id,String batch_id,List sub_id,List teacher_id,
			List start_time,List end_time,List date,List scheduleid,int div_id) {
		
		int i=0;
		for(i=0;i<sub_id.size();i++)
		{
		Schedule schedule=new Schedule();
		schedule.setBatch_id(Integer.parseInt(batch_id));
		schedule.setClass_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDay_id(2);
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
	
	public boolean deleteschedulerelatedtoclass(int classid) {
		ScheduleDB db=new ScheduleDB();
		db.deleteschedulerelatedtoclass(classid);
		return true;
		
	}
	
	public boolean deleteschedulerelatedsubject(int subid) {
		ScheduleDB scheduleDB=new ScheduleDB();
		scheduleDB.deleteschedulerelatedsubject(subid);
		
		return true;
		
	}
	
	public boolean deleteSchedulerelatedoBatch(Batch batch) {
		ScheduleDB scheduleDB=new ScheduleDB();
		scheduleDB.deleteschedulerelatedtoBatch(batch);
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
					for (int j = 0; j < list.get(i).size(); j++) {
						Batch batch=batchTransactions.getBatch(list.get(i).get(j).getBatch_id(), list.get(i).get(j).getClass_id(), list.get(i).get(j).getDiv_id());
						Subject subject=subjectTransaction.getSubject(list.get(i).get(j).getSub_id());
						Scheduledata scheduledata=new Scheduledata();
						scheduledata.setBatch_name(batch.getBatch_name());
						scheduledata.setStart_time(list.get(i).get(j).getStart_time());
						scheduledata.setEnd_time(list.get(i).get(j).getEnd_time());
						scheduledata.setInst_id(list.get(i).get(j).getClass_id());
						scheduledata.setSubject_name(subject.getSubjectName());
						scheduledatas.add(scheduledata);
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
						Batch batch=batchTransactions.getBatch(list.get(i).get(j).getBatch_id(), list.get(i).get(j).getClass_id(), list.get(i).get(j).getDiv_id());
						Subject subject=subjectTransaction.getSubject(list.get(i).get(j).getSub_id());
						DivisionTransactions divisionTransactions=new DivisionTransactions();
						Division division=new Division();
						division=divisionTransactions.getDidvisionByID(list.get(i).get(j).getDiv_id());
						Scheduledata scheduledata=new Scheduledata();
						scheduledata.setBatch_name(batch.getBatch_name());
						scheduledata.setStart_time(list.get(i).get(j).getStart_time());
						scheduledata.setEnd_time(list.get(i).get(j).getEnd_time());
						scheduledata.setInst_id(list.get(i).get(j).getClass_id());
						scheduledata.setSubject_name(subject.getSubjectName());
						scheduledata.setDivision_name(division.getDivisionName());
						RegisterBean bean=db.getRegisterclass(list.get(i).get(j).getClass_id());
						scheduledata.setInst_name(bean.getClassName());
						scheduledatas.add(scheduledata);
					}
				}
			}
			
		}
		return scheduledatas;
	}
}
