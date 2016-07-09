package com.transaction.attendance;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.Schedule.ScheduleDB;
import com.classapp.db.attendance.Attendance;
import com.classapp.db.attendance.AttendanceDB;
import com.classapp.db.batch.Batch;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationBean;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationDb;
import com.classapp.db.exam.ExamPaperDB;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterDB;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDB;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.SubjectDb;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.beans.AttendanceScheduleServiceBean;
import com.service.beans.DailyAttendance;
import com.service.beans.DailyTimeTable;
import com.service.beans.MonthlyAttendance;
import com.service.beans.MonthlyCount;
import com.service.beans.StudentAttendanceNotificationData;
import com.service.beans.StudentDetailAttendanceData;
import com.service.beans.StudentDetailBatchData;
import com.service.beans.StudentDetailMonthWiseAttendance;
import com.service.beans.StudentDetailMonthWiseTotalLectures;
import com.service.beans.StudentListForAttendance;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.student.StudentTransaction;

public class AttendanceTransaction {
	public List<AttendanceScheduleServiceBean> getScheduleForAttendance(int batchid, Date date, int inst_id, int div_id) {
		ScheduleDB db = new ScheduleDB();
		List<AttendanceScheduleServiceBean> attendanceScheduleServiceBeanList = new ArrayList<AttendanceScheduleServiceBean>(); 
		List list =  db.getScheduleForAttendance(batchid, date, inst_id, div_id);
		AttendanceDB attendanceDB = new AttendanceDB();
		List<Integer> scheduleIds = attendanceDB.getDailyDistinctAttendance(inst_id, div_id, batchid, date);
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
				bean.setAttendanceStatus(false);
				for (Integer schedule_id : scheduleIds) {
					if(((Number) object[4]).intValue() == schedule_id){
						bean.setAttendanceStatus(true);
						break;
					}
				}
				attendanceScheduleServiceBeanList.add(bean);

			}
		}
		return attendanceScheduleServiceBeanList;
	}
	
	public List<AttendanceScheduleServiceBean> getScheduleForUpdateAttendance(int batchid, Date date, int inst_id, int div_id) {
		ScheduleDB db = new ScheduleDB();
		List<AttendanceScheduleServiceBean> attendanceScheduleServiceBeanList = new ArrayList<AttendanceScheduleServiceBean>(); 
		List list =  db.getScheduleForAttendance(batchid, date, inst_id, div_id);
		AttendanceDB attendanceDB = new AttendanceDB();
		List<Integer> scheduleIds = attendanceDB.getDailyDistinctAttendance(inst_id, div_id, batchid, date);
		if(list != null){
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				for (Integer schedule_id : scheduleIds) {
					if(((Number) object[4]).intValue() == schedule_id){
				AttendanceScheduleServiceBean bean = new AttendanceScheduleServiceBean();
				bean.setTeacher(object[0]+" "+object[1]);
				bean.setSub_name((String) object[2]);
				bean.setSub_id(((Number) object[3]).intValue());
				bean.setSchedule_id(((Number) object[4]).intValue());
				bean.setStart_time( (Time) object[5]);
				bean.setEnd_time((Time) object[6]);
				bean.setAttendanceStatus(true);	
				attendanceScheduleServiceBeanList.add(bean);
				break;
					}
				}

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
				try{
				JsonParser parser = new JsonParser();
				JsonObject json = (JsonObject) parser.parse((String) object[3]);
				int roll_no = json.get(batchid).getAsInt();
				bean.setRoll_no(roll_no);
				}catch(Exception exception){
					bean.setRoll_no(0);
				}
				
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
				bean.setPresentee((String) object[3]);
				bean.setAtt_id(((Number) object[4]).intValue());
				try{
				JsonParser parser = new JsonParser();
				JsonObject json = (JsonObject) parser.parse((String) object[5]);
				int roll_no = json.get(batchid).getAsInt();
				bean.setRoll_no(roll_no);
				}catch(Exception e){
					bean.setRoll_no(0);
				}
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
				try{
				JsonParser parser = new JsonParser();
				JsonObject json = (JsonObject) parser.parse((String) object[9]);
				int roll_no = json.get(batchid).getAsInt();
				bean.setRoll_no(roll_no);
				}catch(Exception e){
				bean.setRoll_no(0);
				}
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
		List allStudents = db.getAllStudents(batchid, inst_id, div_id);
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
		for (Iterator itr = allStudents.iterator(); itr.hasNext();) {
			Object[] obj = (Object[]) itr.next();
			boolean stdFlag = false;
		for (Iterator iterator = presentList.iterator(); iterator
				.hasNext();) {
			int total_presentee = 0;
			Object[] object = (Object[]) iterator.next();
			if(((Number) object[4]).intValue() == ((Number) obj[2]).intValue()){
				stdFlag = true;
			MonthlyAttendance monthlyAttendance = new MonthlyAttendance();
			monthlyAttendance.setStudent_name((String) object[0]+" "+(String) object[1]);
			monthlyAttendance.setDate((Date) object[3]);
			monthlyAttendance.setStudent_id(((Number) object[4]).intValue());
			try{
			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse((String) obj[3]);
			int roll_no = json.get(batchid).getAsInt();
			monthlyAttendance.setRoll_no(roll_no);
			}catch(Exception e){
			monthlyAttendance.setRoll_no(0);	
			}
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
		}
			if(stdFlag == false){
				MonthlyAttendance monthlyAttendance = new MonthlyAttendance();
				monthlyAttendance.setStudent_name((String) obj[0]+" "+(String) obj[1]);
				monthlyAttendance.setStudent_id(((Number) obj[2]).intValue());
				try{
				JsonParser parser = new JsonParser();
				JsonObject json = (JsonObject) parser.parse((String) obj[3]);
				int roll_no = json.get(batchid).getAsInt();
				monthlyAttendance.setRoll_no(roll_no);
				}catch(Exception e){
					monthlyAttendance.setRoll_no(0);	
				}
				monthlyAttendance.setTotal_monthly_lectures(total_lecture_count);
				String[] daily_attendance = new String[monthlyCountList.size()];
				String[] daily_attendance_percentage = new String[monthlyCountList.size()];
				int i = 0;
				while (i<monthlyCountList.size()) {
		
							daily_attendance[i] = "0";
							daily_attendance_percentage[i] = "0";
							i++;
						
					}
				monthlyAttendance.setDaily_presentee(daily_attendance);
				monthlyAttendance.setDaily_presentee_percentange(daily_attendance_percentage);
				monthlyAttendance.setTotal_prsentee_percentage(0);
				monthlyAttendance.setTotal_monthly_presentee(0);
				monthlyAttendance.setMonthlyCountList(monthlyCountList);
				monthlyAttendanceList.add(monthlyAttendance);
			}
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
		List allStudents = db.getAllStudents(batchid, inst_id, div_id);
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
		for (Iterator itr = allStudents.iterator(); itr.hasNext();) {
			Object[] obj = (Object[]) itr.next();
			boolean stdFlag = false;
		for (Iterator iterator = presentList.iterator(); iterator
				.hasNext();) {
			int total_presentee = 0;
			Object[] object = (Object[]) iterator.next();
			if(((Number) object[4]).intValue() == ((Number) obj[2]).intValue()){
				stdFlag = true;
			MonthlyAttendance monthlyAttendance = new MonthlyAttendance();
			monthlyAttendance.setStudent_name((String) object[0]+" "+(String) object[1]);
			monthlyAttendance.setDate((Date) object[3]);
			monthlyAttendance.setStudent_id(((Number) object[4]).intValue());
			try{
			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse((String) obj[3]);
			int roll_no = json.get(batchid).getAsInt();
			monthlyAttendance.setRoll_no(roll_no);
			}catch(Exception e){
				monthlyAttendance.setRoll_no(0);
			}
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
		}
		if(stdFlag == false){
			MonthlyAttendance monthlyAttendance = new MonthlyAttendance();
			monthlyAttendance.setStudent_name((String) obj[0]+" "+(String) obj[1]);
			monthlyAttendance.setStudent_id(((Number) obj[2]).intValue());
			try{
			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse((String) obj[3]);
			int roll_no = json.get(batchid).getAsInt();
			monthlyAttendance.setRoll_no(roll_no);
			}catch(Exception e){
				monthlyAttendance.setRoll_no(0);
			}
			monthlyAttendance.setTotal_monthly_lectures(total_lecture_count);
			String[] daily_attendance = new String[monthlyCountList.size()];
			String[] daily_attendance_percentage = new String[monthlyCountList.size()];
			int i = 0;
			while (i<monthlyCountList.size()) {
	
						daily_attendance[i] = "0";
						daily_attendance_percentage[i] = "0";
						i++;
					
				}
			monthlyAttendance.setDaily_presentee(daily_attendance);
			monthlyAttendance.setDaily_presentee_percentange(daily_attendance_percentage);
			monthlyAttendance.setTotal_prsentee_percentage(0);
			monthlyAttendance.setTotal_monthly_presentee(0);
			monthlyAttendance.setMonthlyCountList(monthlyCountList);
			monthlyAttendanceList.add(monthlyAttendance);
		}
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
	
	public StudentDetailAttendanceData getStudentYearlyAttendance(int inst_id,int student_id) {
		StudentTransaction studentTransaction= new StudentTransaction();
		Student student =studentTransaction.getStudentByStudentID(student_id,inst_id);
		int div_id = student.getDiv_id();
		List<Integer> batchList = Stream.of(student.getBatch_id().split(","))
		        .map(Integer::parseInt)
		        .collect(Collectors.toList());
		AttendanceDB attendanceDB = new AttendanceDB();
		List duration = attendanceDB.getStartEndOfYearlyAttendance(inst_id, div_id, batchList);
		SubjectDb subjectDb = new SubjectDb();
		BatchTransactions batchTransactions = new BatchTransactions();
		StudentDetailAttendanceData attendanceData = new StudentDetailAttendanceData();
		List<StudentDetailBatchData> batchDataList = new ArrayList<StudentDetailBatchData>();
		List<StudentDetailMonthWiseTotalLectures> detailMonthWiseTotalLectureList = new ArrayList<StudentDetailMonthWiseTotalLectures>();
		List<StudentDetailMonthWiseAttendance> detailMonthWiseAttendances = new ArrayList<StudentDetailMonthWiseAttendance>(); 

 		for (Integer integer : batchList) {
			StudentDetailBatchData batchData = new StudentDetailBatchData();
			List<List<Integer>> subjectIds = new ArrayListOverride<List<Integer>>();
			Batch  batch = batchTransactions.getBatch(integer, inst_id, div_id);
			
			List<Integer> list = Stream.of(batch.getSub_id().split(","))
			        .map(Integer::parseInt)
			        .collect(Collectors.toList());
			for (Integer sub_id : list) {
				List<Integer> innerList = new ArrayListOverride<Integer>();
				innerList.add(integer);
				innerList.add(sub_id);
				subjectIds.add(innerList);
			}
			List<Subject> subjectList= subjectDb.getSubjectList(list);
			List<com.datalayer.subject.Subject> subjects = new  ArrayList<com.datalayer.subject.Subject>();
			for (Iterator iterator = subjectList.iterator(); iterator.hasNext();) {
				Subject subject = (Subject) iterator.next();
				com.datalayer.subject.Subject serviceSubject = new com.datalayer.subject.Subject();
				
				try {
					BeanUtils.copyProperties(serviceSubject, subject);
					subjects.add(serviceSubject);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			batchData.setBatch_id(batch.getBatch_id());
			batchData.setBatch_name(batch.getBatch_name());
			batchData.setSubjectList(subjects);
			
			for (Iterator iterator = duration.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				if(integer == ((Number)object[2]).intValue()){
				List list2 = 	attendanceDB.getMonthWiseTotalLecture(subjectIds, inst_id,student.getDiv_id(),(Date)object[0],(Date)object[1]);
				List list3 = attendanceDB.getStudentsMonthWiseAttendance(subjectIds, inst_id, div_id, (Date)object[0],(Date)object[1], student_id);
				for (Iterator iterator2 = list2.iterator(); iterator2.hasNext();) {
					Object[] obj = (Object[]) iterator2.next();
					StudentDetailMonthWiseTotalLectures detailMonthWiseTotalLectures = new StudentDetailMonthWiseTotalLectures();
					detailMonthWiseTotalLectures.setBatch_id(((Number)obj[1]).intValue());
					detailMonthWiseTotalLectures.setCount(((Number)obj[0]).intValue());
					detailMonthWiseTotalLectures.setMonth(((Number)obj[3]).intValue());
					detailMonthWiseTotalLectures.setSub_id(((Number)obj[2]).intValue());
					detailMonthWiseTotalLectures.setYear(((Number)obj[4]).intValue());
					detailMonthWiseTotalLectureList.add(detailMonthWiseTotalLectures);
				}
				
				for (Iterator iterator2 = list3.iterator(); iterator2.hasNext();) {
					Object[] obj = (Object[]) iterator2.next();
					StudentDetailMonthWiseAttendance detailMonthWiseAttendance = new StudentDetailMonthWiseAttendance();
					detailMonthWiseAttendance.setBatch_id(((Number)obj[1]).intValue());
					detailMonthWiseAttendance.setCount(((Number)obj[0]).intValue());
					detailMonthWiseAttendance.setMonth(((Number)obj[3]).intValue());
					detailMonthWiseAttendance.setSub_id(((Number)obj[2]).intValue());
					detailMonthWiseAttendance.setYear(((Number)obj[4]).intValue());
					detailMonthWiseAttendances.add(detailMonthWiseAttendance);
				}
				int startYear = ((Number)object[3]).intValue();
				int endYear = ((Number)object[4]).intValue();
				int totalYears = endYear - startYear;
				List<Integer> years = new ArrayList<Integer>();
				if(totalYears == 0){
					years.add(startYear);
				}else{
					for(int tempYear = startYear ; tempYear <= endYear ;tempYear++){
						years.add(tempYear);
					}
				}
				batchData.setYears(years);
				}
				
			}
			batchDataList.add(batchData);
			
		}
 		attendanceData.setBatchDataList(batchDataList);
 		attendanceData.setMonthWiseAttendanceList(detailMonthWiseAttendances);
 		attendanceData.setMonthWiseTotalLectureList(detailMonthWiseTotalLectureList);
 		for (StudentDetailBatchData batchData : attendanceData.getBatchDataList()) {
 			if(batchData.getYears() != null){
 			for (Integer year : batchData.getYears()) {
 				for(int month =1 ;month<=12;month++){
 				for (com.datalayer.subject.Subject subject : batchData.getSubjectList()) {
 					boolean totalFlag = false;
 					boolean attendanceFlag = false;
					for (StudentDetailMonthWiseTotalLectures detailMonthWiseTotalLectures : detailMonthWiseTotalLectureList) {
						if(detailMonthWiseTotalLectures.getBatch_id() == batchData.getBatch_id() &&
						   detailMonthWiseTotalLectures.getSub_id() == subject.getSubjectId() &&
						   detailMonthWiseTotalLectures.getMonth() == month &&
						   detailMonthWiseTotalLectures.getYear() == year){
							totalFlag = true;
							break;
						}
					}
					for (StudentDetailMonthWiseAttendance studentDetailMonthWiseAttendance : detailMonthWiseAttendances) {
						if(studentDetailMonthWiseAttendance.getBatch_id() == batchData.getBatch_id() &&
						   studentDetailMonthWiseAttendance.getSub_id() == subject.getSubjectId() &&
						   studentDetailMonthWiseAttendance.getMonth() == month &&
						   studentDetailMonthWiseAttendance.getYear() == year){
							attendanceFlag = true;
									break;
								}
					}
					if(totalFlag == false){
						StudentDetailMonthWiseTotalLectures detailMonthWiseTotalLectures = new StudentDetailMonthWiseTotalLectures();
						detailMonthWiseTotalLectures.setBatch_id(batchData.getBatch_id());
						detailMonthWiseTotalLectures.setCount(0);
						detailMonthWiseTotalLectures.setMonth(month);
						detailMonthWiseTotalLectures.setSub_id(subject.getSubjectId());
						detailMonthWiseTotalLectures.setYear(year);
						detailMonthWiseTotalLectureList.add(detailMonthWiseTotalLectures);
					}
					if(attendanceFlag == false){
						StudentDetailMonthWiseAttendance detailMonthWiseAttendance = new StudentDetailMonthWiseAttendance();
						detailMonthWiseAttendance.setBatch_id(batchData.getBatch_id());
						detailMonthWiseAttendance.setCount(0);
						detailMonthWiseAttendance.setMonth(month);
						detailMonthWiseAttendance.setSub_id(subject.getSubjectId());
						detailMonthWiseAttendance.setYear(year);
						detailMonthWiseAttendances.add(detailMonthWiseAttendance);
					}
				}
 				}
			}
 			}
		}
		return attendanceData;
		
	}
	
	public List<DailyAttendance> getStudentsDailyAttendanceForStudent(String batchid, int inst_id, int div_id,Date date,int student_id) {
		AttendanceDB db = new AttendanceDB();
		List<AttendanceScheduleServiceBean> attendanceScheduleServiceBeanList = new ArrayList<AttendanceScheduleServiceBean>(); 
		List list =  db.getStudentsDailyAttendanceForStudent(batchid, inst_id, div_id,date,student_id);
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
				try{
				JsonParser parser = new JsonParser();
				JsonObject json = (JsonObject) parser.parse((String) object[9]);
				int roll_no = json.get(batchid).getAsInt();
				bean.setRoll_no(roll_no);
				}catch(Exception e){
				bean.setRoll_no(0);
				}
				dailyAttendanceList.add(bean);
			}
		}
		return dailyAttendanceList;
	}
	
	public List<MonthlyAttendance> getStudentsWeeklyAttendanceForStudent(String batchid, int inst_id, int div_id,Date date,int student_id) {
		AttendanceDB db = new AttendanceDB();
		List<AttendanceScheduleServiceBean> attendanceScheduleServiceBeanList = new ArrayList<AttendanceScheduleServiceBean>(); 
		List presentList =  db.getStudentsWeeklyPresentCountForStudent(batchid, inst_id, div_id,date,student_id);
		List totalList =  db.getStudentsWeeklyTotalCount(batchid, inst_id, div_id,date);
		List<MonthlyAttendance> monthlyAttendanceList = new ArrayList<MonthlyAttendance>();
		List<MonthlyCount> monthlyCountList = new ArrayList<MonthlyCount>();
		StudentTransaction studentTransaction = new StudentTransaction();
		Student student = studentTransaction.getStudentByStudentID(student_id, inst_id);
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
			try{
			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(student.getBatchIdNRoll());
			int roll_no = json.get(batchid).getAsInt();
			monthlyAttendance.setRoll_no(roll_no);
			}catch(Exception e){
				monthlyAttendance.setRoll_no(0);
			}
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
	
	public List<MonthlyAttendance> getStudentsMonthlyAttendanceForStudent(String batchid, int inst_id, int div_id,Date date,int student_id) {
		AttendanceDB db = new AttendanceDB();
		List<AttendanceScheduleServiceBean> attendanceScheduleServiceBeanList = new ArrayList<AttendanceScheduleServiceBean>(); 
		List presentList =  db.getStudentsMonthlyPresentCountForStudent(batchid, inst_id, div_id,date,student_id);
		List totalList =  db.getStudentsMonthlyTotalCount(batchid, inst_id, div_id,date);
		List<MonthlyAttendance> monthlyAttendanceList = new ArrayList<MonthlyAttendance>();
		List<MonthlyCount> monthlyCountList = new ArrayList<MonthlyCount>();
		StudentTransaction studentTransaction = new StudentTransaction();
		Student student = studentTransaction.getStudentByStudentID(student_id, inst_id);
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
			try{
			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(student.getBatchIdNRoll());
			int roll_no = json.get(batchid).getAsInt();
			monthlyAttendance.setRoll_no(roll_no);
			}catch(Exception e){
			monthlyAttendance.setRoll_no(0);	
			}
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
	
	
	
	class ArrayListOverride<E> extends ArrayList<E>{
		@Override
		public String toString() {
			Iterator<E> it = iterator();
	        if (! it.hasNext())
	            return "()";

	        StringBuilder sb = new StringBuilder();
	        sb.append('(');
	        for (;;) {
	            E e = it.next();
	            sb.append(e == this ? "(this Collection)" : e);
	            if (! it.hasNext())
	                return sb.append(')').toString();
	            sb.append(',').append(' ');
	        }
	    }

		}
}
