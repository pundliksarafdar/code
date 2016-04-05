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
import com.service.beans.DailyAttendance;
import com.service.beans.DailyTimeTable;
import com.service.beans.MonthlyAttendance;
import com.service.beans.MonthlyCount;
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
	
	public List<StudentListForAttendance> getStudentForAttendanceUpdate(String batchid, int inst_id, int div_id,int sub_id,
																		Date date) {
		StudentDB db = new StudentDB();
		List<AttendanceScheduleServiceBean> attendanceScheduleServiceBeanList = new ArrayList<AttendanceScheduleServiceBean>(); 
		List list =  db.getStudentrelatedtoBatchForAttendanceUpdate(batchid, inst_id, div_id, sub_id, date);
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
				bean.setPresentee((String) object[3]);
				bean.setAtt_id(((Number) object[4]).intValue());
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
	
	public boolean updateAttendance(List<Attendance> attendanceList,int inst_id){
		AttendanceDB db = new AttendanceDB();
		for (Iterator iterator = attendanceList.iterator(); iterator.hasNext();) {
			Attendance attendance = (Attendance) iterator.next();
			attendance.setInst_id(inst_id);
			db.updateAttendance(attendance);
		}
		return true;
	}
	
	public List<DailyAttendance> getStudentsDailyAttendance(String batchid, int inst_id, int div_id,Date date) {
		AttendanceDB db = new AttendanceDB();
		List<AttendanceScheduleServiceBean> attendanceScheduleServiceBeanList = new ArrayList<AttendanceScheduleServiceBean>(); 
		List list =  db.getStudentsDailyAttendance(batchid, inst_id, div_id,date);
		List Timelist = db.getDistinctDailyScheduleTime(Integer.parseInt(batchid), inst_id, div_id,date);
		List<DailyTimeTable> dailyTimeTableList = new ArrayList<DailyTimeTable>();
		for (Iterator iterator = Timelist.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			DailyTimeTable dailyTimeTable = new DailyTimeTable();
			dailyTimeTable.setStart_time((Time) object[0]);
			dailyTimeTable.setEnd_time((Time) object[1]);
			dailyTimeTable.setSub_name((String) object[3]);
			dailyTimeTable.setSub_id(((Number) object[2]).intValue());
			dailyTimeTableList.add(dailyTimeTable);
		}
		List<DailyAttendance> dailyAttendanceList = new ArrayList<DailyAttendance>();
		if(list != null){
			
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				int present_count =0;
				int total_count = dailyTimeTableList.size();
				int i = 0;
				Object[] object = (Object[]) iterator.next();
				DailyAttendance bean = new DailyAttendance();
				bean.setStudent_name((String) object[0]+" "+(String) object[1]);
				bean.setStudent_id(((Number) object[2]).intValue());	
				bean.setDailyTimeTableList(dailyTimeTableList);
				String presentee[] =new String[dailyTimeTableList.size()];
				presentee[i]= (String) object[3];
				i++;
				while (i<dailyTimeTableList.size()) {
					if(iterator.hasNext()){
					object = (Object[]) iterator.next();
					presentee[i]= (String) object[3];
					i++;
					}else{
						break;
					}
				}
				for (int j = 0; j < presentee.length; j++) {
					String string = presentee[j];
					if("P".equals(string))
					{
						present_count++;
					}
				}
				
				bean.setPresent_lectures(present_count);
				bean.setTotal_lectures(total_count);
				bean.setAvg((present_count*100)/total_count);
				bean.setPresentee(presentee);
				dailyAttendanceList.add(bean);
			}
		}
		return dailyAttendanceList;
	}
	
	public List<MonthlyAttendance> getStudentsMonthlyAttendance(String batchid, int inst_id, int div_id,Date date) {
		AttendanceDB db = new AttendanceDB();
		List<AttendanceScheduleServiceBean> attendanceScheduleServiceBeanList = new ArrayList<AttendanceScheduleServiceBean>(); 
		List presentList =  db.getStudentsMonthlyPresentCount(batchid, inst_id, div_id,date);
		List totalList =  db.getStudentsMonthlyTotalCount(batchid, inst_id, div_id,date);
		List<MonthlyAttendance> monthlyAttendanceList = new ArrayList<MonthlyAttendance>();
		List<MonthlyCount> monthlyCountList = new ArrayList<MonthlyCount>();
		int total_lecture_count = 0;
		for (Iterator iterator = totalList.iterator(); iterator
				.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			MonthlyCount monthlyCount = new MonthlyCount();
			monthlyCount.setDate((Date) object[1]);
			monthlyCount.setTotal_count(((Number) object[0]).intValue());
			total_lecture_count = total_lecture_count + ((Number) object[0]).intValue();
			monthlyCountList.add(monthlyCount);
		}
		for (Iterator iterator = presentList.iterator(); iterator
				.hasNext();) {
			int total_presentee = 0;
			Object[] object = (Object[]) iterator.next();
			MonthlyAttendance monthlyAttendance = new MonthlyAttendance();
			monthlyAttendance.setStudent_name((String) object[0]+" "+(String) object[1]);
			monthlyAttendance.setDate((Date) object[3]);
			monthlyAttendance.setStudent_id(((Number) object[4]).intValue());
			String[] daily_attendance = new String[monthlyCountList.size()];
			String[] daily_attendance_percentage = new String[monthlyCountList.size()];
			int i = 0;
			boolean used_flag = false;
			if(((Date) object[3]).compareTo(monthlyCountList.get(i).getDate())==0){
			daily_attendance[i] = Long.toString(((Number)object[2]).longValue());
			total_presentee = total_presentee + ((Number)object[2]).intValue();
			daily_attendance_percentage[i] = Double.toString((((Number) object[2]).doubleValue()*100)/monthlyCountList.get(i).getTotal_count());
			monthlyAttendance.setTotal_monthly_lectures(total_lecture_count);
			used_flag = true;
			}else{
				daily_attendance[i] = "0";
				total_presentee = total_presentee + 0;
				daily_attendance_percentage[i] = "0";
				monthlyAttendance.setTotal_monthly_lectures(total_lecture_count);
			}
			i++;
			while (i<monthlyCountList.size()) {
				
				if(used_flag == false){
					if(((Date) object[3]).compareTo(monthlyCountList.get(i).getDate())==0){
						daily_attendance[i] = Long.toString(((Number)object[2]).longValue());
						total_presentee = total_presentee + ((Number)object[2]).intValue();
						daily_attendance_percentage[i] = Double.toString((((Number) object[2]).doubleValue()*100)/monthlyCountList.get(i).getTotal_count());
						used_flag =true;
					}else{
						daily_attendance[i] = "0";
						total_presentee = total_presentee + 0;
						daily_attendance_percentage[i] = "0";
						used_flag = false;
					}
				}else{
				if(iterator.hasNext()){
				object = (Object[]) iterator.next();
				if(((Date) object[3]).compareTo(monthlyCountList.get(i).getDate())==0){
				daily_attendance[i] = Long.toString(((Number)object[2]).longValue());
				total_presentee = total_presentee + ((Number)object[2]).intValue();
				daily_attendance_percentage[i] = Double.toString((((Number) object[2]).doubleValue()*100)/monthlyCountList.get(i).getTotal_count());
				used_flag =true;
				}else{
					daily_attendance[i] = "0";
					total_presentee = total_presentee + 0;
					daily_attendance_percentage[i] = "0";
					used_flag = false;
				}
				}else{
					break;
				}
		
			}
				
				i++;
			}
			monthlyAttendance.setDaily_presentee(daily_attendance);
			monthlyAttendance.setDaily_presentee_percentange(daily_attendance_percentage);
			monthlyAttendance.setTotal_prsentee_percentage((total_presentee * 100)/total_lecture_count);
			monthlyAttendance.setTotal_monthly_presentee(total_presentee);
			monthlyAttendance.setMonthlyCountList(monthlyCountList);
			monthlyAttendanceList.add(monthlyAttendance);
		}
		return monthlyAttendanceList;
	}
	
	public List<MonthlyAttendance> getStudentsWeeklyAttendance(String batchid, int inst_id, int div_id,Date date) {
		AttendanceDB db = new AttendanceDB();
		List<AttendanceScheduleServiceBean> attendanceScheduleServiceBeanList = new ArrayList<AttendanceScheduleServiceBean>(); 
		List presentList =  db.getStudentsWeeklyPresentCount(batchid, inst_id, div_id,date);
		List totalList =  db.getStudentsWeeklyTotalCount(batchid, inst_id, div_id,date);
		List<MonthlyAttendance> monthlyAttendanceList = new ArrayList<MonthlyAttendance>();
		List<MonthlyCount> monthlyCountList = new ArrayList<MonthlyCount>();
		int total_lecture_count = 0;
		for (Iterator iterator = totalList.iterator(); iterator
				.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			MonthlyCount monthlyCount = new MonthlyCount();
			monthlyCount.setDate((Date) object[1]);
			monthlyCount.setTotal_count(((Number) object[0]).intValue());
			total_lecture_count = total_lecture_count + ((Number) object[0]).intValue();
			monthlyCountList.add(monthlyCount);
		}
		for (Iterator iterator = presentList.iterator(); iterator
				.hasNext();) {
			int total_presentee = 0;
			Object[] object = (Object[]) iterator.next();
			MonthlyAttendance monthlyAttendance = new MonthlyAttendance();
			monthlyAttendance.setStudent_name((String) object[0]+" "+(String) object[1]);
			monthlyAttendance.setDate((Date) object[3]);
			monthlyAttendance.setStudent_id(((Number) object[4]).intValue());
			String[] daily_attendance = new String[monthlyCountList.size()];
			String[] daily_attendance_percentage = new String[monthlyCountList.size()];
			int i = 0;
			boolean used_flag = false;
			if(((Date) object[3]).compareTo(monthlyCountList.get(i).getDate())==0){
			daily_attendance[i] = Long.toString(((Number)object[2]).longValue());
			total_presentee = total_presentee + ((Number)object[2]).intValue();
			daily_attendance_percentage[i] = Double.toString((((Number) object[2]).doubleValue()*100)/monthlyCountList.get(i).getTotal_count());
			monthlyAttendance.setTotal_monthly_lectures(total_lecture_count);
			used_flag = true;
			}else{
				daily_attendance[i] = "0";
				total_presentee = total_presentee + 0;
				daily_attendance_percentage[i] = "0";
				monthlyAttendance.setTotal_monthly_lectures(total_lecture_count);
			}
			i++;
			while (i<monthlyCountList.size()) {
				
				if(used_flag == false){
					if(((Date) object[3]).compareTo(monthlyCountList.get(i).getDate())==0){
						daily_attendance[i] = Long.toString(((Number)object[2]).longValue());
						total_presentee = total_presentee + ((Number)object[2]).intValue();
						daily_attendance_percentage[i] = Double.toString((((Number) object[2]).doubleValue()*100)/monthlyCountList.get(i).getTotal_count());
						used_flag =true;
					}else{
						daily_attendance[i] = "0";
						total_presentee = total_presentee + 0;
						daily_attendance_percentage[i] = "0";
						used_flag = false;
					}
				}else{
				if(iterator.hasNext()){
				object = (Object[]) iterator.next();
				if(((Date) object[3]).compareTo(monthlyCountList.get(i).getDate())==0){
				daily_attendance[i] = Long.toString(((Number)object[2]).longValue());
				total_presentee = total_presentee + ((Number)object[2]).intValue();
				daily_attendance_percentage[i] = Double.toString((((Number) object[2]).doubleValue()*100)/monthlyCountList.get(i).getTotal_count());
				used_flag =true;
				}else{
					daily_attendance[i] = "0";
					total_presentee = total_presentee + 0;
					daily_attendance_percentage[i] = "0";
					used_flag = false;
				}
				}else{
					break;
				}
		
			}
				
				i++;
			}
			monthlyAttendance.setDaily_presentee(daily_attendance);
			monthlyAttendance.setDaily_presentee_percentange(daily_attendance_percentage);
			monthlyAttendance.setTotal_prsentee_percentage((total_presentee * 100)/total_lecture_count);
			monthlyAttendance.setTotal_monthly_presentee(total_presentee);
			monthlyAttendance.setMonthlyCountList(monthlyCountList);
			monthlyAttendanceList.add(monthlyAttendance);
		}
		return monthlyAttendanceList;
	}
	
	public boolean deleteAttendance(int inst_id,int div_id) {
		AttendanceDB attendanceDB = new AttendanceDB();
		attendanceDB.deleteAttendance(inst_id, div_id);
		return true;
		
	}
	
	public boolean deleteAttendance(int inst_id,int div_id,int batch_id) {
		AttendanceDB attendanceDB = new AttendanceDB();
		attendanceDB.deleteAttendance(inst_id, div_id,batch_id );
		return true;
		
	}
	
	public boolean deleteAttendanceRelatedToSubject(int inst_id,int sub_id) {
		AttendanceDB attendanceDB = new AttendanceDB();
		attendanceDB.deleteAttendanceRelatedToSubject(inst_id, sub_id);
		return true;
		
	}
	
	public boolean deleteAttendanceRelatedToStudent(int inst_id,int student_id) {
		AttendanceDB attendanceDB = new AttendanceDB();
		attendanceDB.deleteAttendanceRelatedToStudent(inst_id, student_id);
		return true;
		
	}
}
