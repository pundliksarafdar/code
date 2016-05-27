package com.notification.access;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.classapp.db.attendance.AttendanceDB;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationBean;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationDb;
import com.classapp.db.fees.FeesDB;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterDB;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDB;
import com.notification.bean.MessageDafaultInterface;
import com.notification.bean.MessageDetailBean;
import com.notification.bean.ProgressCardMessage;
import com.service.beans.StudentAttendanceNotificationData;
import com.service.beans.StudentFessNotificationData;
import com.service.beans.StudentProgressCard;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.util.NotificationEnum;

public class NotificationImpl {
	public void sendDailyAttendanceNotification(){
		Date date =new Date(new java.util.Date().getTime());
		DateFormat dateFormat =new SimpleDateFormat("EEEE");
		dateFormat.format(date);
		ClassOwnerNotificationDb classOwnerNotificationDb = new ClassOwnerNotificationDb();
		List<ClassOwnerNotificationBean> classOwnerNotificationBeanList = classOwnerNotificationDb.getInstitutesForDailyAttendance(dateFormat.format(date));
		AttendanceDB attendanceDB = new AttendanceDB();
		RegisterDB db =new RegisterDB();
		for (ClassOwnerNotificationBean classOwnerNotificationBean : classOwnerNotificationBeanList) {
			RegisterBean institute =db.getRegistereduser(classOwnerNotificationBean.getRegId());
			List<StudentAttendanceNotificationData> studentAttendanceNotificationDatas = new ArrayList<StudentAttendanceNotificationData>();
			List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
			List studentList = attendanceDB.getStudentsDailyPresentCountForNotification(classOwnerNotificationBean.getRegId(), date);
			for (Iterator iterator = studentList.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				MessageDetailBean messageDetailBean = new MessageDetailBean();
				messageDetailBean.setFrom(institute.getClassName());
				messageDetailBean.setEmailSubject("Daily Attendance Report");
				messageDetailBean.setStudentId(((Number)object[0]).intValue());
				messageDetailBean.setStudentEmail((String)object[4]);
				if(!"".equals((String)object[3])){
				messageDetailBean.setStudentPhone(Long.parseLong((String)object[3]));
				}
				messageDetailBean.setParentEmail((String)object[10]);
				if(!"".equals((String)object[9])){
				messageDetailBean.setParentPhone(Long.parseLong((String)object[9]));
				}
				messageDetailBean.setMessageTypeEmail(classOwnerNotificationBean.isEmailAttendanceDaily());
				messageDetailBean.setMessageTypeSms(classOwnerNotificationBean.isSmsAttendanceDaily());
				messageDetailBean.setSendToParent(true);
				messageDetailBean.setSendToStudent(false);
				
				StudentAttendanceNotificationData data = new StudentAttendanceNotificationData(); 
				data.setAtt_date(date);
				data.setStudent_id(((Number)object[0]).intValue());
				data.setStudent_name((String)object[1]+" "+(String)object[2]);
				data.setParent_name((String)object[7]+" "+(String)object[8]);
				data.setPresent_lectures(((Number)object[5]).intValue());
				data.setTotal_lectures(((Number)object[6]).intValue());
				data.setAverage((double) (((Number)object[5]).intValue()*100)/(double)((Number)object[6]).intValue());
				messageDetailBean.setEmailMessage(null);
				messageDetailBean.setEmailObject(data);
				messageDetailBean.setEmailTemplate("attendance.tmpl");
				messageDetailBean.setParentEmailMessage(null);
				messageDetailBean.setParentEmailObject(data);
				messageDetailBean.setParentEmailTemplate("attendanceAlertParent.tmpl");
				detailBeans.add(messageDetailBean);
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			
			notifcationAccess.send(detailBeans);
		}
	}
	
	public void sendWeeklyAttendanceNotification(){
		Date date =new Date(new java.util.Date().getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -7);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(startDate);
		 calendar.add(calendar.DATE, 6);
		//calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		DateFormat dateFormat =new SimpleDateFormat("EEEE");
		dateFormat.format(date);
		ClassOwnerNotificationDb classOwnerNotificationDb = new ClassOwnerNotificationDb();
		List<ClassOwnerNotificationBean> classOwnerNotificationBeanList = classOwnerNotificationDb.getInstitutesForWeeklyAttendance();
		AttendanceDB attendanceDB = new AttendanceDB();
		RegisterDB db =new RegisterDB();
		for (ClassOwnerNotificationBean classOwnerNotificationBean : classOwnerNotificationBeanList) {
			RegisterBean institute =db.getRegistereduser(classOwnerNotificationBean.getRegId());
			List<StudentAttendanceNotificationData> studentAttendanceNotificationDatas = new ArrayList<StudentAttendanceNotificationData>();
			List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
			List studentList = attendanceDB.getStudentsWeeklyPresentCountForNotification(classOwnerNotificationBean.getRegId(), startDate, endDate);
			for (Iterator iterator = studentList.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				MessageDetailBean messageDetailBean = new MessageDetailBean();
				messageDetailBean.setFrom(institute.getClassName());
				messageDetailBean.setEmailSubject("Weekly Attendance Report");
				messageDetailBean.setStudentId(((Number)object[0]).intValue());
				messageDetailBean.setStudentEmail((String)object[4]);
				if(!"".equals((String)object[3])){
				messageDetailBean.setStudentPhone(Long.parseLong((String)object[3]));
				}
				messageDetailBean.setParentEmail((String)object[10]);
				if(!"".equals((String)object[9])){
				messageDetailBean.setParentPhone(Long.parseLong((String)object[9]));
				}
				messageDetailBean.setMessageTypeEmail(classOwnerNotificationBean.isEmailAttendanceWeekly());
				messageDetailBean.setMessageTypeSms(classOwnerNotificationBean.isSmsAttendanceWeekly());
				messageDetailBean.setSendToParent(true);
				messageDetailBean.setSendToStudent(false);
				
				StudentAttendanceNotificationData data = new StudentAttendanceNotificationData(); 
				data.setAtt_date(date);
				data.setStudent_id(((Number)object[0]).intValue());
				data.setStudent_name((String)object[1]+" "+(String)object[2]);
				data.setParent_name((String)object[7]+" "+(String)object[8]);
				data.setPresent_lectures(((Number)object[5]).intValue());
				data.setTotal_lectures(((Number)object[6]).intValue());
				data.setAverage((double) (((Number)object[5]).intValue()*100)/(double)((Number)object[6]).intValue());
				messageDetailBean.setEmailMessage(null);
				messageDetailBean.setEmailObject(data);
				messageDetailBean.setEmailTemplate("attendance.tmpl");
				messageDetailBean.setParentEmailMessage(null);
				messageDetailBean.setParentEmailObject(data);
				messageDetailBean.setParentEmailTemplate("attendanceAlertParent.tmpl");
				detailBeans.add(messageDetailBean);
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			
			notifcationAccess.send(detailBeans);
		}
	}
	
	public void sendMonthlyAttendanceNotification(){
		Date date =new Date(new java.util.Date().getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(date);
		int lastdate = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		DateFormat dateFormat =new SimpleDateFormat("EEEE");
		dateFormat.format(date);
		ClassOwnerNotificationDb classOwnerNotificationDb = new ClassOwnerNotificationDb();
		List<ClassOwnerNotificationBean> classOwnerNotificationBeanList = classOwnerNotificationDb.getInstitutesForWeeklyAttendance();
		AttendanceDB attendanceDB = new AttendanceDB();
		RegisterDB db =new RegisterDB();
		for (ClassOwnerNotificationBean classOwnerNotificationBean : classOwnerNotificationBeanList) {
			RegisterBean institute =db.getRegistereduser(classOwnerNotificationBean.getRegId());
			List<StudentAttendanceNotificationData> studentAttendanceNotificationDatas = new ArrayList<StudentAttendanceNotificationData>();
			List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
			List studentList = attendanceDB.getStudentsMonthlyPresentCountForNotification(classOwnerNotificationBean.getRegId(), startDate, endDate);
			for (Iterator iterator = studentList.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				MessageDetailBean messageDetailBean = new MessageDetailBean();
				messageDetailBean.setFrom(institute.getClassName());
				messageDetailBean.setEmailSubject("Monthly Attendance Report");
				messageDetailBean.setStudentId(((Number)object[0]).intValue());
				messageDetailBean.setStudentEmail((String)object[4]);
				if(!"".equals((String)object[3])){
				messageDetailBean.setStudentPhone(Long.parseLong((String)object[3]));
				}
				messageDetailBean.setParentEmail((String)object[10]);
				if(!"".equals((String)object[9])){
				messageDetailBean.setParentPhone(Long.parseLong((String)object[9]));
				}
				messageDetailBean.setMessageTypeEmail(classOwnerNotificationBean.isEmailAttendanceWeekly());
				messageDetailBean.setMessageTypeSms(classOwnerNotificationBean.isSmsAttendanceWeekly());
				messageDetailBean.setSendToParent(true);
				messageDetailBean.setSendToStudent(false);
				
				StudentAttendanceNotificationData data = new StudentAttendanceNotificationData(); 
				data.setAtt_date(date);
				data.setStudent_id(((Number)object[0]).intValue());
				data.setStudent_name((String)object[1]+" "+(String)object[2]);
				data.setParent_name((String)object[7]+" "+(String)object[8]);
				data.setPresent_lectures(((Number)object[5]).intValue());
				data.setTotal_lectures(((Number)object[6]).intValue());
				data.setAverage((double) (((Number)object[5]).intValue()*100)/(double)((Number)object[6]).intValue());
				messageDetailBean.setEmailMessage(null);
				messageDetailBean.setEmailObject(data);
				messageDetailBean.setEmailTemplate("attendance.tmpl");
				messageDetailBean.setParentEmailMessage(null);
				messageDetailBean.setParentEmailObject(data);
				messageDetailBean.setParentEmailTemplate("attendanceAlertParent.tmpl");
				detailBeans.add(messageDetailBean);
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			
			notifcationAccess.send(detailBeans);
		}
	}
	
	public void sendFeesDueNotification(int inst_id,int div_id,int batch_id,boolean sendEmail,boolean sendSMS,boolean sendToParent,boolean sendToStudent){
		
		RegisterDB db =new RegisterDB();
		FeesDB feesDB = new FeesDB();
		RegisterBean institute =db.getRegistereduser(inst_id);
		List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
		List studentList = feesDB.getStudentDueFeesForNotification(inst_id, div_id, batch_id);
			for (Iterator iterator = studentList.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				MessageDetailBean messageDetailBean = new MessageDetailBean();
				messageDetailBean.setFrom(institute.getClassName());
				messageDetailBean.setEmailSubject("Fees Due");
				messageDetailBean.setStudentId(((Number)object[0]).intValue());
				messageDetailBean.setStudentEmail((String)object[4]);
				if(!"".equals((String)object[3])){
				messageDetailBean.setStudentPhone(Long.parseLong((String)object[3]));
				}
				messageDetailBean.setParentEmail((String)object[8]);
				if(!"".equals((String)object[7])){
				messageDetailBean.setParentPhone(Long.parseLong((String)object[7]));
				}
				messageDetailBean.setMessageTypeEmail(sendEmail);
				messageDetailBean.setMessageTypeSms(sendSMS);
				messageDetailBean.setSendToParent(sendToParent);
				messageDetailBean.setSendToStudent(sendToStudent);
				
				StudentFessNotificationData data = new StudentFessNotificationData(); 
				data.setStudent_name((String)object[1]+" "+(String)object[2]);
				data.setParent_name((String)object[5]+" "+(String)object[6]);
				data.setTotal_fees(((Number)object[9]).doubleValue());
				data.setFees_paid(((Number)object[10]).doubleValue());
				data.setFee_due(((Number)object[11]).doubleValue());
				messageDetailBean.setEmailMessage(null);
				messageDetailBean.setEmailObject(data);
				messageDetailBean.setEmailTemplate("feesAlertStudent.tmpl");
				messageDetailBean.setParentEmailMessage(null);
				messageDetailBean.setParentEmailObject(data);
				messageDetailBean.setParentEmailTemplate("feesAlertParent.tmpl");
				detailBeans.add(messageDetailBean);
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			
			notifcationAccess.send(detailBeans);
		
	}
	
	public boolean sendStudentProgressCard(int inst_id,int div_id, int batch_id, String exam_ids) {
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		List<StudentProgressCard> progressCardList = marksTransaction.sendStudentProgressCard(inst_id, div_id,
				batch_id, exam_ids);
		Map<Integer, MessageDafaultInterface> map = new HashMap<Integer, MessageDafaultInterface>();
		for (StudentProgressCard studentProgressCard : progressCardList) {
			MessageDafaultInterface cardMessage = new ProgressCardMessage();
			cardMessage.setEmailObject(studentProgressCard);
			cardMessage.setEmailTemplate("progressCard.tmpl");
			map.put(studentProgressCard.getStudent_id(), cardMessage);
		}
		Notification notification = new Notification();
		notification.send(NotificationEnum.MessageCategery.PROGRESS_CARD_MANUAL,
				(HashMap<Integer, MessageDafaultInterface>) map, inst_id);
		StudentProgressCard progressCard = new StudentProgressCard();
		return true;
	}
	
	public String sendDailyAttendanceManulNotification(Date date,int inst_id,int div_id,int batch_id,boolean sendEmail,
												boolean sendSMS,boolean sendToParent,boolean sendToStudent,int threshold){
		StudentDB studentDB = new StudentDB();
		List<Student> students=studentDB.getStudentrelatedtoBatch(batch_id+"", inst_id, div_id);
		List<Integer> studentIds=students.stream().map(Student::getStudent_id).collect(Collectors.toList());
		if(studentIds.size()>0){
		AttendanceDB attendanceDB = new AttendanceDB();
		RegisterDB db = new RegisterDB();
		RegisterBean institute = db.getRegistereduser(inst_id);
		List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
		List studentList = attendanceDB.getStudentsDailyPresentCountForManulNotification(inst_id, div_id, batch_id, date, studentIds);
		for (Iterator iterator = studentList.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			if (((double) (((Number) object[5]).intValue() * 100)
					/ (double) ((Number) object[6]).intValue()) < threshold) {
				MessageDetailBean messageDetailBean = new MessageDetailBean();
				messageDetailBean.setFrom(institute.getClassName());
				messageDetailBean.setEmailSubject("Daily Attendance Report");
				messageDetailBean.setStudentId(((Number) object[0]).intValue());
				messageDetailBean.setStudentEmail((String) object[4]);
				if (!"".equals((String) object[3])) {
					messageDetailBean.setStudentPhone(Long.parseLong((String) object[3]));
				}
				messageDetailBean.setParentEmail((String) object[10]);
				if (!"".equals((String) object[9])) {
					messageDetailBean.setParentPhone(Long.parseLong((String) object[9]));
				}
				messageDetailBean.setMessageTypeEmail(sendEmail);
				messageDetailBean.setMessageTypeSms(sendSMS);
				messageDetailBean.setSendToParent(sendToParent);
				messageDetailBean.setSendToStudent(sendToStudent);

				StudentAttendanceNotificationData data = new StudentAttendanceNotificationData();
				data.setAtt_date(date);
				data.setStudent_id(((Number) object[0]).intValue());
				data.setStudent_name((String) object[1] + " " + (String) object[2]);
				data.setParent_name((String) object[7] + " " + (String) object[8]);
				data.setPresent_lectures(((Number) object[5]).intValue());
				data.setTotal_lectures(((Number) object[6]).intValue());
				data.setAverage(
						(double) (((Number) object[5]).intValue() * 100) / (double) ((Number) object[6]).intValue());
				messageDetailBean.setEmailMessage(null);
				messageDetailBean.setEmailObject(data);
				messageDetailBean.setEmailTemplate("attendance.tmpl");
				messageDetailBean.setParentEmailMessage(null);
				messageDetailBean.setParentEmailObject(data);
				messageDetailBean.setParentEmailTemplate("attendanceAlertParent.tmpl");
				detailBeans.add(messageDetailBean);
			}
		}
		NotifcationAccess notifcationAccess = new NotifcationAccess();
		if(detailBeans.size()>0){
		notifcationAccess.send(detailBeans);
		}else{
			return "All student have paid more fees than given threshold";
		}
		}else{
			return "Students Not Available in batch";
		}
		return "";
	}

	public String sendWeeklyAttendanceManulNotification(Date date, int inst_id, int div_id, int batch_id,
			boolean sendEmail, boolean sendSMS, boolean sendToParent, boolean sendToStudent, int threshold) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(startDate);
		 calendar.add(calendar.DATE, 6);
		//calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		StudentDB studentDB = new StudentDB();
		List<Student> students = studentDB.getStudentrelatedtoBatch(batch_id + "", inst_id, div_id);
		List<Integer> studentIds = students.stream().map(Student::getStudent_id).collect(Collectors.toList());
		if (studentIds.size() > 0) {
			AttendanceDB attendanceDB = new AttendanceDB();
			RegisterDB db = new RegisterDB();
			RegisterBean institute = db.getRegistereduser(inst_id);
			List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
			List studentList = attendanceDB.getStudentsWeeklyPresentCountForManulNotification(inst_id, div_id, batch_id, startDate, endDate, studentIds);
			for (Iterator iterator = studentList.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				if (((double) (((Number) object[5]).intValue() * 100)
						/ (double) ((Number) object[6]).intValue()) < threshold) {
					MessageDetailBean messageDetailBean = new MessageDetailBean();
					messageDetailBean.setFrom(institute.getClassName());
					messageDetailBean.setEmailSubject("Daily Attendance Report");
					messageDetailBean.setStudentId(((Number) object[0]).intValue());
					messageDetailBean.setStudentEmail((String) object[4]);
					if (!"".equals((String) object[3])) {
						messageDetailBean.setStudentPhone(Long.parseLong((String) object[3]));
					}
					messageDetailBean.setParentEmail((String) object[10]);
					if (!"".equals((String) object[9])) {
						messageDetailBean.setParentPhone(Long.parseLong((String) object[9]));
					}
					messageDetailBean.setMessageTypeEmail(sendEmail);
					messageDetailBean.setMessageTypeSms(sendSMS);
					messageDetailBean.setSendToParent(sendToParent);
					messageDetailBean.setSendToStudent(sendToStudent);

					StudentAttendanceNotificationData data = new StudentAttendanceNotificationData();
					data.setAtt_date(date);
					data.setStudent_id(((Number) object[0]).intValue());
					data.setStudent_name((String) object[1] + " " + (String) object[2]);
					data.setParent_name((String) object[7] + " " + (String) object[8]);
					data.setPresent_lectures(((Number) object[5]).intValue());
					data.setTotal_lectures(((Number) object[6]).intValue());
					data.setAverage((double) (((Number) object[5]).intValue() * 100)
							/ (double) ((Number) object[6]).intValue());
					messageDetailBean.setEmailMessage(null);
					messageDetailBean.setEmailObject(data);
					messageDetailBean.setEmailTemplate("attendance.tmpl");
					messageDetailBean.setParentEmailMessage(null);
					messageDetailBean.setParentEmailObject(data);
					messageDetailBean.setParentEmailTemplate("attendanceAlertParent.tmpl");
					detailBeans.add(messageDetailBean);
				}
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			if (detailBeans.size() > 0) {
				notifcationAccess.send(detailBeans);
			} else {
				return "All student have paid more fees than given threshold";
			}
		} else {
			return "Students Not Available in batch";
		}
		return "";
	}
	
	public String sendMonthlyAttendanceManulNotification(Date date, int inst_id, int div_id, int batch_id,
			boolean sendEmail, boolean sendSMS, boolean sendToParent, boolean sendToStudent, int threshold) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(date);
		int lastdate = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		StudentDB studentDB = new StudentDB();
		List<Student> students = studentDB.getStudentrelatedtoBatch(batch_id + "", inst_id, div_id);
		List<Integer> studentIds = students.stream().map(Student::getStudent_id).collect(Collectors.toList());
		if (studentIds.size() > 0) {
			AttendanceDB attendanceDB = new AttendanceDB();
			RegisterDB db = new RegisterDB();
			RegisterBean institute = db.getRegistereduser(inst_id);
			List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
			List studentList = attendanceDB.getStudentsWeeklyPresentCountForManulNotification(inst_id, div_id, batch_id, startDate, endDate, studentIds);
			for (Iterator iterator = studentList.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				if (((double) (((Number) object[5]).intValue() * 100)
						/ (double) ((Number) object[6]).intValue()) < threshold) {
					MessageDetailBean messageDetailBean = new MessageDetailBean();
					messageDetailBean.setFrom(institute.getClassName());
					messageDetailBean.setEmailSubject("Daily Attendance Report");
					messageDetailBean.setStudentId(((Number) object[0]).intValue());
					messageDetailBean.setStudentEmail((String) object[4]);
					if (!"".equals((String) object[3])) {
						messageDetailBean.setStudentPhone(Long.parseLong((String) object[3]));
					}
					messageDetailBean.setParentEmail((String) object[10]);
					if (!"".equals((String) object[9])) {
						messageDetailBean.setParentPhone(Long.parseLong((String) object[9]));
					}
					messageDetailBean.setMessageTypeEmail(sendEmail);
					messageDetailBean.setMessageTypeSms(sendSMS);
					messageDetailBean.setSendToParent(sendToParent);
					messageDetailBean.setSendToStudent(sendToStudent);

					StudentAttendanceNotificationData data = new StudentAttendanceNotificationData();
					data.setAtt_date(date);
					data.setStudent_id(((Number) object[0]).intValue());
					data.setStudent_name((String) object[1] + " " + (String) object[2]);
					data.setParent_name((String) object[7] + " " + (String) object[8]);
					data.setPresent_lectures(((Number) object[5]).intValue());
					data.setTotal_lectures(((Number) object[6]).intValue());
					data.setAverage((double) (((Number) object[5]).intValue() * 100)
							/ (double) ((Number) object[6]).intValue());
					messageDetailBean.setEmailMessage(null);
					messageDetailBean.setEmailObject(data);
					messageDetailBean.setEmailTemplate("attendance.tmpl");
					messageDetailBean.setParentEmailMessage(null);
					messageDetailBean.setParentEmailObject(data);
					messageDetailBean.setParentEmailTemplate("attendanceAlertParent.tmpl");
					detailBeans.add(messageDetailBean);
				}
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			if (detailBeans.size() > 0) {
				notifcationAccess.send(detailBeans);
			} else {
				return "All student have paid more fees than given threshold";
			}
		} else {
			return "Students Not Available in batch";
		}
		return "";
	}
	
	
}
