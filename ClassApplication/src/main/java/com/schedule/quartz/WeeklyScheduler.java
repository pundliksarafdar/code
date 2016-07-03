package com.schedule.quartz;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.classapp.db.attendance.AttendanceDB;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationBean;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationDb;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterDB;
import com.classapp.persistence.Constants;
import com.notification.access.NotifcationAccess;
import com.notification.bean.MessageDetailBean;
import com.notification.sms.SmsNotificationTransaction;
import com.service.beans.ClassownerSettingsNotification;
import com.service.beans.StudentAttendanceNotificationData;
import com.transaction.classownersettingtransaction.ClassownerSettingstransaction;

public class WeeklyScheduler implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		sendWeeklyAttendanceNotification();
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
			ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
			ClassownerSettingsNotification settings = settingsTx.getSettings(classOwnerNotificationBean.getRegId());
			if(settings.getInstituteStats().isSmsAccess() || settings.getInstituteStats().isEmailAccess()){
			RegisterBean institute =db.getRegistereduser(classOwnerNotificationBean.getRegId());
			List<StudentAttendanceNotificationData> studentAttendanceNotificationDatas = new ArrayList<StudentAttendanceNotificationData>();
			List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
			List studentList = attendanceDB.getStudentsWeeklyPresentCountForNotification(classOwnerNotificationBean.getRegId(), startDate, endDate);
			int msgCounter = 0;
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
				if(settings.getInstituteStats().isEmailAccess()){
				messageDetailBean.setMessageTypeEmail(classOwnerNotificationBean.isEmailAttendanceWeekly());
				}else{
				messageDetailBean.setMessageTypeEmail(false);	
				}
				if(settings.getInstituteStats().isSmsAccess()){
				messageDetailBean.setMessageTypeSms(classOwnerNotificationBean.isSmsAttendanceWeekly());
				}else{
				messageDetailBean.setMessageTypeSms(false);	
				}
				messageDetailBean.setSendToParent(classOwnerNotificationBean.isParentAttendanceWeekly());
				messageDetailBean.setSendToStudent(classOwnerNotificationBean.isStudentAttendanceWeekly());
				if(messageDetailBean.getStudentPhone()!= null && messageDetailBean.isSendToStudent() && messageDetailBean.getStudentPhone()!= 0){
					msgCounter ++;
				}
				if(messageDetailBean.getParentPhone()!= null && messageDetailBean.isSendToParent() && messageDetailBean.getParentPhone()!= 0){
					msgCounter ++;
				}
				StudentAttendanceNotificationData data = new StudentAttendanceNotificationData(); 
				data.setAtt_date(date);
				data.setStudent_id(((Number)object[0]).intValue());
				data.setStudent_name((String)object[1]+" "+(String)object[2]);
				data.setParent_name((String)object[7]+" "+(String)object[8]);
				data.setPresent_lectures(((Number)object[5]).intValue());
				data.setTotal_lectures(((Number)object[6]).intValue());
				data.setAverage((double) (((Number)object[5]).intValue()*100)/(double)((Number)object[6]).intValue());
				data.setStart_date(startDate);
				data.setEnd_date(endDate);
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
				messageDetailBean.setSmsObject(data);
				messageDetailBean.setSmsParentTemplate("weeklyAttendanceParentSMS.tmpl");
				detailBeans.add(messageDetailBean);
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			if(classOwnerNotificationBean.isSmsAttendanceWeekly()){
			SmsNotificationTransaction  notificationTransaction = new SmsNotificationTransaction();
			HashMap statusMap = notificationTransaction.validateNSendSms(msgCounter, "", classOwnerNotificationBean.getRegId(),Constants.COMMON_ZERO,Constants.COMMON_ZERO,Constants.PARENT_ROLE,Constants.AUTO_WEEKLY_MSG);;
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
	}
}
