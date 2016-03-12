package com.transaction.attendance;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.classapp.db.Schedule.ScheduleDB;
import com.classapp.db.attendance.Attendance;
import com.classapp.db.attendance.AttendanceDB;
import com.classapp.db.student.StudentDB;
import com.service.beans.AttendanceScheduleServiceBean;
import com.service.beans.StudentListForAttendance;

public class AttendanceTransaction {
	public List<AttendanceScheduleServiceBean> getScheduleForAttendance(int batchid, Date date, int inst_id, int div_id) {
		ScheduleDB db = new ScheduleDB();
		List<AttendanceScheduleServiceBean> attendanceScheduleServiceBeanList = new ArrayList<AttendanceScheduleServiceBean>(); 
		List list =  db.getScheduleForAttendance(batchid, date, inst_id, div_id);
		if(list != null){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				AttendanceScheduleServiceBean bean = new AttendanceScheduleServiceBean();
				bean.setTeacher(object[0]+" "+object[1]);
				bean.setSub_name((String) object[2]);
				bean.setSub_id(((Number) object[3]).intValue());
				bean.setSchedule_id(((Number) object[4]).intValue());
				bean.setStart_time( (Time) object[5]);
				bean.setEnd_time((Time) object[6]);			
				attendanceScheduleServiceBeanList.add(bean);

			}
		}
		return attendanceScheduleServiceBeanList;
	}
	
	public List<StudentListForAttendance> getStudentForAttendance(String batchid, int inst_id, int div_id) {
		StudentDB db = new StudentDB();
		List<AttendanceScheduleServiceBean> attendanceScheduleServiceBeanList = new ArrayList<AttendanceScheduleServiceBean>(); 
		List list =  db.getStudentrelatedtoBatchForAttendance(batchid, inst_id, div_id);
		List<StudentListForAttendance> listForAttendanceList = new ArrayList<StudentListForAttendance>();
		if(list != null){
			int i = 1;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				StudentListForAttendance bean = new StudentListForAttendance();
				bean.setFname((String) object[0]);
				bean.setLname((String) object[1]);
				bean.setStudent_id(((Number) object[2]).intValue());	
				bean.setRoll_no(i++);
				listForAttendanceList.add(bean);
			}
		}
		return listForAttendanceList;
	}
	
	public boolean save(List<Attendance> attendanceList,int inst_id){
		AttendanceDB db = new AttendanceDB();
		for (Iterator iterator = attendanceList.iterator(); iterator.hasNext();) {
			Attendance attendance = (Attendance) iterator.next();
			attendance.setInst_id(inst_id);
			db.save(attendance);
		}
		return true;
	}
}
