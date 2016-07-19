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
import com.classapp.db.batch.Batch;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationBean;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationDb;
import com.classapp.db.fees.FeesDB;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterDB;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDB;
import com.classapp.db.student.StudentDetails;
import com.classapp.persistence.Constants;
import com.notification.bean.MessageDafaultInterface;
import com.notification.bean.MessageDetailBean;
import com.notification.bean.ProgressCardMessage;
import com.notification.sms.SmsNotificationTransaction;
import com.service.beans.ClassownerSettingsNotification;
import com.service.beans.ManualRegisteredStudentNotificationData;
import com.service.beans.StudentAttendanceNotificationData;
import com.service.beans.StudentFessNotificationData;
import com.service.beans.StudentProgressCard;
import com.transaction.batch.BatchTransactions;
import com.transaction.classownersettingtransaction.ClassownerSettingstransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.util.NotificationEnum;

public class NotificationImpl {	
	public HashMap sendFeesDueNotification(int inst_id,int div_id,int batch_id,boolean sendEmail,boolean sendSMS,boolean sendToParent,boolean sendToStudent){
		ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingsTx.getSettings(inst_id);
		HashMap statusMap = new HashMap<>();
		String status = "";
		if(sendSMS == true && settings.getInstituteStats().isSmsAccess()==false){
			status = "You don't have SMS access";
		}
		if(sendEmail == true && settings.getInstituteStats().isEmailAccess() == false){
			if("".equals(status)){
				status = "You don't have Email access";
			}else{
				status = status + "and Email access";
			}
		}
		if("".equals(status)){
		RegisterDB db =new RegisterDB();
		FeesDB feesDB = new FeesDB();
		RegisterBean institute =db.getRegistereduser(inst_id);
		List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
		int msgCounter = 0;
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
				if(messageDetailBean.getStudentPhone()!= null && messageDetailBean.isSendToStudent() && messageDetailBean.getStudentPhone()!= 0){
					msgCounter ++;
				}
				if(messageDetailBean.getParentPhone()!= null && messageDetailBean.isSendToParent() && messageDetailBean.getParentPhone()!= 0){
					msgCounter ++;
				}
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
				messageDetailBean.setSmsMessage(null);
				messageDetailBean.setSmsTemplate("manualFeesDueAlertStudentSMS.tmpl");
				messageDetailBean.setSmsObject(data);
				messageDetailBean.setSmsParentMessage(null);
				messageDetailBean.setSmsParentObject(data);
				messageDetailBean.setSmsParentTemplate("manualFeesDueAlertParentSMS.tmpl");
				detailBeans.add(messageDetailBean);
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			if(sendSMS){
			SmsNotificationTransaction  notificationTransaction = new SmsNotificationTransaction();
			statusMap = notificationTransaction.validateNSendSms(msgCounter, "", inst_id,Constants.COMMON_ZERO,Constants.COMMON_ZERO,Constants.PARENT_ROLE,Constants.AUTO_DAILY_MSG);;
			if(statusMap.containsKey("access") && (boolean)statusMap.get("access")){
			notifcationAccess.send(detailBeans);
			ClassownerSettingstransaction settingstransaction = new ClassownerSettingstransaction();
			settingstransaction.reduceSmsCount(msgCounter, institute.getRegId());
			}
			}else{
			notifcationAccess.send(detailBeans);
			}
		}else{
			statusMap.put("status", status);
		}
		return statusMap;
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
		SmsNotificationTransaction  notificationTransaction = new SmsNotificationTransaction();
		HashMap statusMap = notificationTransaction.validateNSendSms(progressCardList.size(), "", inst_id,div_id,batch_id,Constants.PARENT_ROLE,Constants.MANUAL_PROGRESS_CARD);;
		if(statusMap.containsKey("access") && (boolean)statusMap.get("access")){
		notification.send(NotificationEnum.MessageCategery.PROGRESS_CARD_MANUAL,
				(HashMap<Integer, MessageDafaultInterface>) map, inst_id);
		StudentProgressCard progressCard = new StudentProgressCard();
		}
		return true;
	}
	
	public HashMap sendStudentProgressCard(int inst_id,int div_id, int batch_id, String exam_ids,boolean sendEmail,
			boolean sendSMS,boolean sendToParent,boolean sendToStudent) {
		HashMap statusMap = new HashMap<>();
		String status = "";
		ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingsTx.getSettings(inst_id);
		
		if(sendSMS == true && settings.getInstituteStats().isSmsAccess()==false){
			status = "You don't have SMS access";
		}
		if(sendEmail == true && settings.getInstituteStats().isEmailAccess() == false){
			if("".equals(status)){
				status = "You don't have Email access";
			}else{
				status = status + "and Email access";
			}
		}
		if("".equals(status)){
		int msgCounter = 0;
		RegisterDB db = new RegisterDB();
		RegisterBean institute = db.getRegistereduser(inst_id);
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentDetails> studentData = studentTransaction.getAllStudentsDetails(batch_id+"", div_id+"" ,inst_id);
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		List<StudentProgressCard> progressCardList = marksTransaction.sendStudentProgressCard(inst_id, div_id,
				batch_id, exam_ids);
		List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
		
		for (StudentDetails studentDetails : studentData) {
			List<StudentProgressCard> progressCards = progressCardList.stream().filter(cardList -> cardList.getStudent_id() ==  studentDetails.getStudentId()).collect(Collectors.toList());
			for (StudentProgressCard progressCard : progressCards) {
				MessageDetailBean messageDetailBean = new MessageDetailBean();
				messageDetailBean.setFrom(institute.getClassName());
				messageDetailBean.setEmailSubject("Progress Report");
				messageDetailBean.setStudentId(progressCard.getStudent_id());
				messageDetailBean.setStudentEmail(studentDetails.getStudentUserBean().getEmail());
				if (!"".equals(studentDetails.getStudentUserBean().getPhone1())) {
					messageDetailBean.setStudentPhone(Long.parseLong(studentDetails.getStudentUserBean().getPhone1()));
				}else{
					messageDetailBean.setStudentPhone(0l);
				}
				messageDetailBean.setParentEmail(studentDetails.getStudent().getParentEmail());
				if (!"".equals(studentDetails.getStudent().getParentPhone())) {
					messageDetailBean.setParentPhone(Long.parseLong(studentDetails.getStudent().getParentPhone()));
				}else{
					messageDetailBean.setParentPhone(0l);
				}
				messageDetailBean.setMessageTypeEmail(sendEmail);
				messageDetailBean.setMessageTypeSms(sendSMS);
				messageDetailBean.setSendToParent(sendToParent);
				messageDetailBean.setSendToStudent(sendToStudent);
				if(messageDetailBean.getStudentPhone()!= null && messageDetailBean.isSendToStudent() && messageDetailBean.getStudentPhone()!= 0){
					msgCounter ++;
				}
				if(messageDetailBean.getParentPhone()!= null && messageDetailBean.isSendToParent() && messageDetailBean.getParentPhone()!= 0){
					msgCounter ++;
				}
				progressCard.setInst_name(institute.getClassName());
				messageDetailBean.setEmailObject(progressCard);
				messageDetailBean.setEmailTemplate("progressCard.tmpl");
				messageDetailBean.setParentEmailMessage(null);
				messageDetailBean.setParentEmailObject(progressCard);
				messageDetailBean.setParentEmailTemplate("progressCardParentEmail.tmpl");
				
				messageDetailBean.setSmsMessage(null);
				messageDetailBean.setSmsObject(progressCard);
				messageDetailBean.setSmsTemplate("progressCardSMS.tmpl");
				messageDetailBean.setSmsParentMessage(null);
				messageDetailBean.setSmsParentObject(progressCard);
				messageDetailBean.setSmsParentTemplate("progressCardParentSMS.tmpl");
				detailBeans.add(messageDetailBean);
			}
		}
		NotifcationAccess notifcationAccess = new NotifcationAccess();
		if(sendSMS){
		SmsNotificationTransaction  notificationTransaction = new SmsNotificationTransaction();
		statusMap = notificationTransaction.validateNSendSms(msgCounter, "", inst_id,Constants.COMMON_ZERO,Constants.COMMON_ZERO,Constants.PARENT_ROLE,Constants.AUTO_DAILY_MSG);;
		if(statusMap.containsKey("access") && (boolean)statusMap.get("access")){
		notifcationAccess.send(detailBeans);
		ClassownerSettingstransaction settingstransaction = new ClassownerSettingstransaction();
		settingstransaction.reduceSmsCount(msgCounter, institute.getRegId());
		}
		}else{
		notifcationAccess.send(detailBeans);	
		}
		}else{
			statusMap.put("status", status);
		}
		return statusMap;
	}
	
	public HashMap sendDailyAttendanceManulNotification(Date date,int inst_id,int div_id,int batch_id,boolean sendEmail,
												boolean sendSMS,boolean sendToParent,boolean sendToStudent,int threshold){
		ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingsTx.getSettings(inst_id);
		HashMap statusMap = new HashMap<>();
		String status = "";
		if(sendSMS == true && settings.getInstituteStats().isSmsAccess()==false){
			status = "You don't have SMS access";
		}
		if(sendEmail == true && settings.getInstituteStats().isEmailAccess() == false){
			if("".equals(status)){
				status = "You don't have Email access";
			}else{
				status = status + "and Email access";
			}
		}
		if("".equals(status)){
		int msgCounter = 0;
		BatchTransactions batchTransactions = new BatchTransactions();
		Batch batch = batchTransactions.getBatch(batch_id, inst_id, div_id);
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
				if(messageDetailBean.getStudentPhone()!= null && messageDetailBean.isSendToStudent() && messageDetailBean.getStudentPhone()!= 0){
					msgCounter ++;
				}
				if(messageDetailBean.getParentPhone()!= null && messageDetailBean.isSendToParent() && messageDetailBean.getParentPhone()!= 0){
					msgCounter ++;
				}
				
				StudentAttendanceNotificationData data = new StudentAttendanceNotificationData();
				data.setInst_name(institute.getClassName());
				data.setAtt_date(date);
				data.setStudent_id(((Number) object[0]).intValue());
				data.setStudent_name((String) object[1] + " " + (String) object[2]);
				data.setParent_name((String) object[7] + " " + (String) object[8]);
				data.setPresent_lectures(((Number) object[5]).intValue());
				data.setTotal_lectures(((Number) object[6]).intValue());
				data.setAverage(
						(double) (((Number) object[5]).intValue() * 100) / (double) ((Number) object[6]).intValue());
				data.setBatch_name(batch.getBatch_name());
				messageDetailBean.setEmailMessage(null);
				messageDetailBean.setEmailObject(data);
				messageDetailBean.setEmailTemplate("attendance.tmpl");
				messageDetailBean.setParentEmailMessage(null);
				messageDetailBean.setParentEmailObject(data);
				messageDetailBean.setParentEmailTemplate("attendanceAlertParent.tmpl");
				
				messageDetailBean.setSmsMessage(null);
				messageDetailBean.setSmsObject(data);
				messageDetailBean.setSmsTemplate("dailyAttendanceMSG.tmpl");
				messageDetailBean.setSmsParentMessage(null);
				messageDetailBean.setSmsParentObject(data);
				messageDetailBean.setSmsParentTemplate("dailyAttendanceParentSMS.tmpl");
				detailBeans.add(messageDetailBean);
			}
		}
		NotifcationAccess notifcationAccess = new NotifcationAccess();
		if(detailBeans.size()>0){
			if(sendSMS){
			SmsNotificationTransaction  notificationTransaction = new SmsNotificationTransaction();
			statusMap = notificationTransaction.validateNSendSms(msgCounter, "", inst_id,div_id,batch_id,Constants.PARENT_ROLE,Constants.MANUAL_DAILY_MSG);;
			if(statusMap.containsKey("access") && (boolean)statusMap.get("access")){
		    notifcationAccess.send(detailBeans);
		    ClassownerSettingstransaction settingstransaction = new ClassownerSettingstransaction();
			settingstransaction.reduceSmsCount(msgCounter, institute.getRegId());
			}
			}else{
			notifcationAccess.send(detailBeans);	
			}
		}else{
			status = "All student have more attendance than given threshold";
			statusMap.put("status", status);
		}
		}else{
			status = "Students Not Available in batch";
			statusMap.put("status", status);
		}
		}else{
			statusMap.put("status", status);
		}
		return statusMap;
	}

	public HashMap sendWeeklyAttendanceManulNotification(Date date, int inst_id, int div_id, int batch_id,
			boolean sendEmail, boolean sendSMS, boolean sendToParent, boolean sendToStudent, int threshold) {
		ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingsTx.getSettings(inst_id);
		HashMap statusMap = new HashMap<>();
		String status = "";
		if(sendSMS == true && settings.getInstituteStats().isSmsAccess()==false){
			status = "You don't have SMS access";
		}
		if(sendEmail == true && settings.getInstituteStats().isEmailAccess() == false){
			if("".equals(status)){
				status = "You don't have Email access";
			}else{
				status = status + "and Email access";
			}
		}
		if("".equals(status)){
		int msgCounter = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(startDate);
		 calendar.add(calendar.DATE, 6);
		//calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		BatchTransactions batchTransactions = new BatchTransactions();
		Batch batch = batchTransactions.getBatch(batch_id, inst_id, div_id);
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
					messageDetailBean.setEmailSubject("Weekly Attendance Report");
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
					if(messageDetailBean.getStudentPhone()!= null && messageDetailBean.isSendToStudent() && messageDetailBean.getStudentPhone()!= 0){
						msgCounter ++;
					}
					if(messageDetailBean.getParentPhone()!= null && messageDetailBean.isSendToParent() && messageDetailBean.getParentPhone()!= 0){
						msgCounter ++;
					}
					
					StudentAttendanceNotificationData data = new StudentAttendanceNotificationData();
					data.setInst_name(institute.getClassName());
					data.setAtt_date(date);
					data.setStudent_id(((Number) object[0]).intValue());
					data.setStudent_name((String) object[1] + " " + (String) object[2]);
					data.setParent_name((String) object[7] + " " + (String) object[8]);
					data.setPresent_lectures(((Number) object[5]).intValue());
					data.setTotal_lectures(((Number) object[6]).intValue());
					data.setAverage((double) (((Number) object[5]).intValue() * 100)
							/ (double) ((Number) object[6]).intValue());
					data.setBatch_name(batch.getBatch_name());
					data.setStart_date(startDate);
					data.setEnd_date(endDate);
					/*data.setBatch_name((String) object[13]);*/
					messageDetailBean.setEmailMessage(null);
					messageDetailBean.setEmailObject(data);
					messageDetailBean.setEmailTemplate("weeklyAttendanceStudentEmail.tmpl");
					messageDetailBean.setParentEmailMessage(null);
					messageDetailBean.setParentEmailObject(data);
					messageDetailBean.setParentEmailTemplate("weeklyAttendanceParentEmail.tmpl");
					
					messageDetailBean.setSmsMessage(null);
					messageDetailBean.setSmsObject(data);
					messageDetailBean.setSmsTemplate("weeklyAttendanceStudentSMS.tmpl");
					messageDetailBean.setSmsParentMessage(null);
					messageDetailBean.setSmsParentObject(data);
					messageDetailBean.setSmsParentTemplate("weeklyAttendanceParentSMS.tmpl");
					detailBeans.add(messageDetailBean);
				}
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			if (detailBeans.size() > 0) {
				if(sendSMS){
				SmsNotificationTransaction  notificationTransaction = new SmsNotificationTransaction();
				statusMap = notificationTransaction.validateNSendSms(msgCounter, "", inst_id,div_id,batch_id,Constants.PARENT_ROLE,Constants.MANUAL_WEEKLY_MSG);;
				if(statusMap.containsKey("access") && (boolean)statusMap.get("access")){
				notifcationAccess.send(detailBeans);
				ClassownerSettingstransaction settingstransaction = new ClassownerSettingstransaction();
				settingstransaction.reduceSmsCount(msgCounter, institute.getRegId());
				}
				}else{
				notifcationAccess.send(detailBeans);	
				}
			}else{
				status = "All student have more attendance than given threshold";
				statusMap.put("status", status);
			}
			}else{
				status = "Students Not Available in batch";
				statusMap.put("status", status);
			}
			}else{
				statusMap.put("status", status);
			}
		return statusMap;
	}
	
	public HashMap sendMonthlyAttendanceManulNotification(Date date, int inst_id, int div_id, int batch_id,
			boolean sendEmail, boolean sendSMS, boolean sendToParent, boolean sendToStudent, int threshold) {
		ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingsTx.getSettings(inst_id);
		HashMap statusMap = new HashMap<>();
		String status = "";
		if(sendSMS == true && settings.getInstituteStats().isSmsAccess()==false){
			status = "You don't have SMS access";
		}
		if(sendEmail == true && settings.getInstituteStats().isEmailAccess() == false){
			if("".equals(status)){
				status = "You don't have Email access";
			}else{
				status = status + "and Email access";
			}
		}
		if("".equals(status)){
		int msgCounter = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = new Date(calendar.getTime().getTime());
		calendar.setTime(date);
		int lastdate = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DAY_OF_MONTH, lastdate);
		Date endDate =  new Date(calendar.getTime().getTime());
		BatchTransactions batchTransactions = new BatchTransactions();
		Batch batch = batchTransactions.getBatch(batch_id, inst_id, div_id);
		StudentDB studentDB = new StudentDB();
		List<Student> students = studentDB.getStudentrelatedtoBatch(batch_id + "", inst_id, div_id);
		List<Integer> studentIds = students.stream().map(Student::getStudent_id).collect(Collectors.toList());
		if (studentIds.size() > 0) {
			AttendanceDB attendanceDB = new AttendanceDB();
			RegisterDB db = new RegisterDB();
			RegisterBean institute = db.getRegistereduser(inst_id);
			List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
			List studentList = attendanceDB.getStudentsMonthlyPresentCountForManulNotification(inst_id, div_id, batch_id, startDate, endDate, studentIds);
			for (Iterator iterator = studentList.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				if (((double) (((Number) object[5]).intValue() * 100)
						/ (double) ((Number) object[6]).intValue()) < threshold) {
					MessageDetailBean messageDetailBean = new MessageDetailBean();
					messageDetailBean.setFrom(institute.getClassName());
					messageDetailBean.setEmailSubject("Monthly Attendance Report");
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
					if(messageDetailBean.getStudentPhone()!= null && messageDetailBean.isSendToStudent() && messageDetailBean.getStudentPhone()!= 0){
						msgCounter ++;
					}
					if(messageDetailBean.getParentPhone()!= null && messageDetailBean.isSendToParent() && messageDetailBean.getParentPhone()!= 0){
						msgCounter ++;
					}
					StudentAttendanceNotificationData data = new StudentAttendanceNotificationData();
					data.setInst_name(institute.getClassName());
					data.setAtt_date(date);
					data.setStudent_id(((Number) object[0]).intValue());
					data.setStudent_name((String) object[1] + " " + (String) object[2]);
					data.setParent_name((String) object[7] + " " + (String) object[8]);
					data.setPresent_lectures(((Number) object[5]).intValue());
					data.setTotal_lectures(((Number) object[6]).intValue());
					data.setAverage((double) (((Number) object[5]).intValue() * 100)
							/ (double) ((Number) object[6]).intValue());
					data.setStart_date(startDate);
					data.setEnd_date(endDate);
					data.setBatch_name(batch.getBatch_name());
					data.setMonth(new SimpleDateFormat("MMM").format(date)+" "+new SimpleDateFormat("YYYY").format(date));
					messageDetailBean.setEmailMessage(null);
					messageDetailBean.setEmailObject(data);
					messageDetailBean.setEmailTemplate("monthlyAttendanceStudentEmail.tmpl");
					messageDetailBean.setParentEmailMessage(null);
					messageDetailBean.setParentEmailObject(data);
					messageDetailBean.setParentEmailTemplate("monthlyAttendanceParentEmail.tmpl");
					
					messageDetailBean.setSmsMessage(null);
					messageDetailBean.setSmsObject(data);
					messageDetailBean.setSmsTemplate("monthlyAttendanceStudentSMS.tmpl");
					messageDetailBean.setSmsParentMessage(null);
					messageDetailBean.setSmsParentObject(data);
					messageDetailBean.setSmsParentTemplate("monthlyAttendanceParentSMS.tmpl");
					detailBeans.add(messageDetailBean);
				}
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			if (detailBeans.size() > 0) {
				SmsNotificationTransaction  notificationTransaction = new SmsNotificationTransaction();
				if(sendSMS){
			    statusMap = notificationTransaction.validateNSendSms(msgCounter, "", inst_id,div_id,batch_id,Constants.PARENT_ROLE,Constants.MANUAL_MONTHLY_MSG);;
				if(statusMap.containsKey("access") && (boolean)statusMap.get("access")){
				notifcationAccess.send(detailBeans);
				ClassownerSettingstransaction settingstransaction = new ClassownerSettingstransaction();
				settingstransaction.reduceSmsCount(msgCounter, institute.getRegId());
				}
				}else{
				notifcationAccess.send(detailBeans);	
				}
			}else{
				status = "All student have more attendance than given threshold";
				statusMap.put("status", status);
			}
			}else{
				status = "Students Not Available in batch";
				statusMap.put("status", status);
			}
			}else{
				statusMap.put("status", status);
			}
		return statusMap;
	}
	
	public HashMap sendFeesPaymentNotification(int inst_id,int div_id,int batch_id,int student_id,List<StudentFessNotificationData> paymentDataList){
		ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingsTx.getSettings(inst_id);
		HashMap statusMap = new HashMap<>();
		String status = "";
		if(settings.getInstituteStats().isSmsAccess() || settings.getInstituteStats().isEmailAccess()){
		RegisterDB db =new RegisterDB();
		FeesDB feesDB = new FeesDB();
		RegisterBean institute =db.getRegistereduser(inst_id);
		List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
		int msgCounter = 0;
		StudentTransaction studentTransaction = new StudentTransaction();
		Student student = studentTransaction.getclassStudent(student_id, inst_id);
		RegisterBean studentRegisterBean = db.getRegistereduser(student_id);
		for (StudentFessNotificationData data : paymentDataList) {
				MessageDetailBean messageDetailBean = new MessageDetailBean();
				messageDetailBean.setFrom(institute.getClassName());
				messageDetailBean.setEmailSubject("Fees Payment");
				messageDetailBean.setStudentId(student_id);
				messageDetailBean.setStudentEmail(studentRegisterBean.getEmail());
				if(!"".equals(studentRegisterBean.getPhone1())){
				messageDetailBean.setStudentPhone(Long.parseLong(studentRegisterBean.getPhone1()));
				}
				messageDetailBean.setParentEmail(student.getParentEmail());
				if(!"".equals(student.getParentPhone())){
				messageDetailBean.setParentPhone(Long.parseLong(student.getParentPhone()));
				}
				messageDetailBean.setMessageTypeEmail(settings.getClassOwnerNotificationBean().isEmailPayment());
				messageDetailBean.setMessageTypeSms(settings.getClassOwnerNotificationBean().isSmsPayment());
				messageDetailBean.setSendToParent(settings.getClassOwnerNotificationBean().isParentPayment());
				messageDetailBean.setSendToStudent(settings.getClassOwnerNotificationBean().isStudentPayment());
				if(messageDetailBean.getStudentPhone()!= null && messageDetailBean.isSendToStudent() && messageDetailBean.getStudentPhone()!= 0){
					msgCounter ++;
				}
				if(messageDetailBean.getParentPhone()!= null && messageDetailBean.isSendToParent() && messageDetailBean.getParentPhone()!= 0){
					msgCounter ++;
				}
				data.setInst_name(institute.getClassName());
				data.setParent_name(student.getParentFname()+" "+student.getParentLname());
				data.setStudent_name(studentRegisterBean.getFname()+" "+studentRegisterBean.getLname());
				messageDetailBean.setEmailMessage(null);
				messageDetailBean.setEmailObject(data);
				messageDetailBean.setEmailTemplate("feesPaymentStudentAlertEmail.tmpl");
				messageDetailBean.setParentEmailMessage(null);
				messageDetailBean.setParentEmailObject(data);
				messageDetailBean.setParentEmailTemplate("feesPaymentParentAlertEmail.tmpl");
				messageDetailBean.setSmsMessage(null);
				messageDetailBean.setSmsTemplate("feesPaymentStudentAlertSMS.tmpl");
				messageDetailBean.setSmsObject(data);
				messageDetailBean.setSmsParentMessage(null);
				messageDetailBean.setSmsParentObject(data);
				messageDetailBean.setSmsParentTemplate("feesPaymentParentAlertSMS.tmpl");
				detailBeans.add(messageDetailBean);
		}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			if(settings.getClassOwnerNotificationBean().isSmsPayment()){
			SmsNotificationTransaction  notificationTransaction = new SmsNotificationTransaction();
			statusMap = notificationTransaction.validateNSendSms(msgCounter, "", inst_id,Constants.COMMON_ZERO,Constants.COMMON_ZERO,Constants.PARENT_ROLE,Constants.AUTO_DAILY_MSG);;
			if(statusMap.containsKey("access") && (boolean)statusMap.get("access")){
			notifcationAccess.send(detailBeans);
			ClassownerSettingstransaction settingstransaction = new ClassownerSettingstransaction();
			settingstransaction.reduceSmsCount(msgCounter, institute.getRegId());
			}
			}else{
			notifcationAccess.send(detailBeans);
			}
		}else{
			statusMap.put("status", status);
		}
		return statusMap;
	}
	
	public void sendFeesPaymentNotification(int inst_id,List<Integer> studentIDs) {
		Date date =new Date(new java.util.Date().getTime());
		RegisterDB db =new RegisterDB();
		FeesDB feesDB = new FeesDB();
		ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingsTx.getSettings(inst_id);
		RegisterBean institute =db.getRegistereduser(inst_id);
		List students = feesDB.getStudentForFeesPaymentNotification(inst_id, studentIDs);
		int msgCounter = 0;
		List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
		if(settings.getInstituteStats().isSmsAccess() || settings.getInstituteStats().isEmailAccess()){
		for (Iterator iterator = students.iterator(); iterator.hasNext();) {
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
				messageDetailBean.setMessageTypeEmail(settings.getClassOwnerNotificationBean().isEmailPayment());
				messageDetailBean.setMessageTypeSms(settings.getClassOwnerNotificationBean().isSmsPayment());
				messageDetailBean.setSendToParent(settings.getClassOwnerNotificationBean().isParentPayment());
				messageDetailBean.setSendToStudent(settings.getClassOwnerNotificationBean().isStudentPayment());
				if(messageDetailBean.getStudentPhone()!= null && messageDetailBean.isSendToStudent() && messageDetailBean.getStudentPhone()!= 0){
					msgCounter ++;
				}
				if(messageDetailBean.getParentPhone()!= null && messageDetailBean.isSendToParent() && messageDetailBean.getParentPhone()!= 0){
					msgCounter ++;
				}
				StudentFessNotificationData data = new StudentFessNotificationData(); 
				data.setStudent_name((String)object[1]+" "+(String)object[2]);
				data.setParent_name((String)object[5]+" "+(String)object[6]);
				data.setTotal_fees(((Number)object[9]).doubleValue());
				data.setAmount_paid(((Number)object[10]).doubleValue());
				data.setFees_paid(((Number)object[10]).doubleValue());
				data.setFee_due(((Number)object[11]).doubleValue());
				data.setBatch_name((String)object[12]);
				messageDetailBean.setEmailMessage(null);
				messageDetailBean.setEmailObject(data);
				messageDetailBean.setEmailTemplate("feesPaymentStudentAlertEmail.tmpl");
				messageDetailBean.setParentEmailMessage(null);
				messageDetailBean.setParentEmailObject(data);
				messageDetailBean.setParentEmailTemplate("feesPaymentParentAlertEmail.tmpl");
				messageDetailBean.setSmsMessage(null);
				messageDetailBean.setSmsTemplate("feesPaymentStudentAlertSMS.tmpl");
				messageDetailBean.setSmsObject(data);
				messageDetailBean.setSmsParentMessage(null);
				messageDetailBean.setSmsParentObject(data);
				messageDetailBean.setSmsParentTemplate("feesPaymentParentAlertSMS.tmpl");
				detailBeans.add(messageDetailBean);
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			if(settings.getClassOwnerNotificationBean().isSmsPayment()){
			SmsNotificationTransaction  notificationTransaction = new SmsNotificationTransaction();
			HashMap statusMap = notificationTransaction.validateNSendSms(msgCounter, "", inst_id,Constants.COMMON_ZERO,Constants.COMMON_ZERO,Constants.PARENT_ROLE,Constants.AUTO_DAILY_MSG);;
			if(statusMap.containsKey("access") && (boolean)statusMap.get("access")){
			notifcationAccess.send(detailBeans);
			ClassownerSettingstransaction settingstransaction = new ClassownerSettingstransaction();
			settingstransaction.reduceSmsCount(msgCounter, institute.getRegId());
			}
			}else{
				notifcationAccess.send(detailBeans);
			}
		}
	}
	
	public void sendManualRegistrationNotification(int inst_id,List<Integer> studentIDs) {
		Date date =new Date(new java.util.Date().getTime());
		RegisterDB db =new RegisterDB();
		ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingsTx.getSettings(inst_id);
		RegisterBean institute =db.getRegistereduser(inst_id);
		List students = db.getManuallyRegisteredStudentData(inst_id, studentIDs);
		int msgCounter = 0;
		List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
		if(settings.getInstituteStats().isEmailAccess()){
		for (Iterator iterator = students.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				MessageDetailBean messageDetailBean = new MessageDetailBean();
				messageDetailBean.setFrom(institute.getClassName());
				messageDetailBean.setEmailSubject("Registered!!");
				messageDetailBean.setStudentId(((Number)object[0]).intValue());
				messageDetailBean.setStudentEmail((String)object[5]);
				messageDetailBean.setParentEmail((String)object[8]);
				messageDetailBean.setMessageTypeEmail(true);
				messageDetailBean.setMessageTypeSms(false);
				if(messageDetailBean.getStudentEmail() != null && !"".equals(messageDetailBean.getStudentEmail())){
				messageDetailBean.setSendToStudent(true);
				messageDetailBean.setSendToParent(false);
				}else{
				messageDetailBean.setSendToStudent(false);
				messageDetailBean.setSendToParent(true);
				}
				ManualRegisteredStudentNotificationData data = new ManualRegisteredStudentNotificationData(); 
				data.setStudent_name((String)object[3]+" "+(String)object[4]);
				data.setParent_name((String)object[6]+" "+(String)object[7]);
				data.setUser_name((String)object[1]);
				data.setPassword((String)object[2]);
				messageDetailBean.setEmailMessage(null);
				messageDetailBean.setEmailObject(data);
				messageDetailBean.setEmailTemplate("manualRegisteredStudentEmail.tmpl");
				messageDetailBean.setParentEmailMessage(null);
				messageDetailBean.setParentEmailObject(data);
				messageDetailBean.setParentEmailTemplate("manualRegisteredParentEmail.tmpl");
				detailBeans.add(messageDetailBean);
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
				notifcationAccess.send(detailBeans);
			}
		
	}
	
	
}
