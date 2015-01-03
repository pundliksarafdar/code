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

public class ScheduleTransaction {

	public String addLecture(String class_id,String batch_id,List sub_id,List teacher_id,
			List start_time,List end_time,List date) {
		
		int i=0;
		for(i=0;i<sub_id.size();i++)
		{
		Schedule schedule=new Schedule();
		schedule.setBatch_id(Integer.parseInt(batch_id));
		schedule.setClass_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDay_id(2);
		schedule.setDiv_id(1);
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
			List start_time,List end_time,List date) {
		
		int i=0;
		String status="";
		for(i=0;i<sub_id.size();i++)
		{
		Schedule schedule=new Schedule();
		schedule.setBatch_id(Integer.parseInt(batch_id));
		schedule.setClass_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDay_id(2);
		schedule.setDiv_id(1);
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
			List start_time,List end_time,List date,List scheduleids) {
		
		int i=0;
		String status="";
		for(i=0;i<sub_id.size();i++)
		{
		Schedule schedule=new Schedule();
		schedule.setBatch_id(Integer.parseInt(batch_id));
		schedule.setClass_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDay_id(2);
		schedule.setDiv_id(1);
		schedule.setEnd_time((Time)end_time.get(i));
		schedule.setStart_time((Time)start_time.get(i));
		schedule.setSub_id(Integer.parseInt((String)sub_id.get(i)));
		schedule.setTeacher_id(Integer.parseInt((String)teacher_id.get(i)));
		schedule.setSchedule_id(Integer.parseInt((String)scheduleids.get(i)));
		ScheduleDB scheduleDB=new ScheduleDB();
		String exists=scheduleDB.isTeacherUnavailable(schedule);
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
	
	public List<Schedule> getSchedule(int batchid,Date date) {
		List<Schedule> schedulelist=null;
		ScheduleDB db=new ScheduleDB();
		schedulelist=db.getSchedule(batchid,date);
		return schedulelist;
		
	}
	
	public List<Schedule> getTeachersSchedule(int classid,int teacherid,Date scheduledate) {
		List<Schedule> schedulelist=null;
		ScheduleDB db=new ScheduleDB();
		schedulelist=db.getTeachersSchedule(classid, teacherid,scheduledate);
		return schedulelist;
		
	}
	
	public boolean deleteSchedule(int scheduleid)
	{
		
		ScheduleDB db=new ScheduleDB();
		db.deleteSchedule(scheduleid);
		return true;
	}
	
	public String updateLecture(String class_id,String batch_id,List sub_id,List teacher_id,
			List start_time,List end_time,List date,List scheduleid) {
		
		int i=0;
		for(i=0;i<sub_id.size();i++)
		{
		Schedule schedule=new Schedule();
		schedule.setBatch_id(Integer.parseInt(batch_id));
		schedule.setClass_id(Integer.parseInt(class_id));
		schedule.setDate((Date)date.get(i));
		schedule.setDay_id(2);
		schedule.setDiv_id(1);
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
		for (int i = 0; i < batchsubids.length; i++) {
			boolean flag=false;
			for (int j = 0; j < subids.length; j++) {
				if(batchsubids[i].equals(subids[j])){
					flag=true;
				}	
			}
			if(flag==false){
				db.deleteschedulerelatedtobatchsubject(batch.getBatch_id(), Integer.parseInt(batchsubids[i]));
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
}
