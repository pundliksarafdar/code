package com.notification.sms;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.classapp.db.notificationpkg.Institute_MsgLog;
import com.classapp.db.notificationpkg.NotificationDB;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDetails;
import com.classapp.persistence.Constants;
import com.notification.access.MessageFormatter;
import com.service.beans.ClassownerSettingsNotification;
import com.transaction.classownersettingtransaction.ClassownerSettingstransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.teacher.TeacherTransaction;

public class SmsNotificationTransaction {
	public String smsNotificationToParent(Integer batchId,Integer divId,Integer instId,String message){
		StudentTransaction studentTransaction = new StudentTransaction();
		List<Student> parentData = studentTransaction.getStudentsrelatedtobatch(batchId+"", instId, divId);
		List<String>smsIds = new ArrayList<String>();
		for(Student student:parentData){
			String sms = student.getParentPhone();
			if(sms!=null && sms.trim().length()>0){
				smsIds.add(sms);
				System.out.println(sms);
			}
		}
		return sendSms(smsIds, message,instId,divId,batchId,Constants.PARENT_ROLE,Constants.GENERAL_PARENT_MSG);
	}
	
	public String smsNotificationToStudent(Integer batchId,Integer divId,Integer instId,String message){
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentDetails> studentData = studentTransaction.getAllStudentsDetails(batchId+"", divId+"" ,instId);
		List<String>smsIds = new ArrayList<String>();
		for(StudentDetails student:studentData){
			String sms = student.getStudentUserBean().getPhone1();
			if(sms!=null && sms.trim().length()>0){
				smsIds.add(sms);
				System.out.println("Student sms......."+sms);
			}
		}
		return sendSms(smsIds, message,instId,divId,batchId,Constants.STUDENT_ROLE,Constants.GENERAL_STUDENT_MSG);
	}
	
	public String smsNotificationToTeacher(List teacherIds,String message,int instId){
		TeacherTransaction teacherTransaction = new TeacherTransaction();
		List<RegisterBean> registerBeans = teacherTransaction.getTeacherDetail(teacherIds);
		List<String>smsIds = new ArrayList<String>();
		for(RegisterBean registerBean:registerBeans){
			String sms = registerBean.getPhone1();
			if(sms!=null && sms.trim().length()>0){
				smsIds.add(sms);
				System.out.println(sms);
			}
		}
		System.out.println(smsIds);
		return sendSms(smsIds, message,instId,Constants.COMMON_ZERO,Constants.COMMON_ZERO,Constants.TEACHER_ROLE,Constants.GENERAL_TEACHER_MSG);
	}
	
	private String sendSms(List<String>smss,String message,int classId,int div_id,int batch_id,int role,String msg_type){
		HashMap statusMap = validateNSendSms(smss.size(), message, classId,div_id,batch_id,role,msg_type);
		if(statusMap.containsKey("access") && (boolean)statusMap.get("access")){
			final SmsNotification inotify = new SmsNotification();
			//This property shows how much message left
			/*
			if(statusMap.containsKey("leftsms")){
				int leftSms = (int) statusMap.get("leftsms");
				smss = smss.subList(0, leftSms);
			}*/
			if(smss.size()>0){
				ClassownerSettingstransaction settingstransaction = new ClassownerSettingstransaction();
				settingstransaction.reduceSmsCount(smss.size(), classId);
				for(String sms:smss){
					new Thread(new Runnable() {
						@Override
						public void run() {
							inotify.sendSms(smss, message, classId);
						}
					}).start();
				}
			}
		}
		return (String) statusMap.get("status");
	}
	
	public HashMap validateNSendSms(int smsSize,String message,int inst_id,int div_id,int batch_id,int role,String msg_type){
		ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingsTx.getSettings(inst_id);
		Institute_MsgLog msgLog = new Institute_MsgLog();
		msgLog.setBatch_id(batch_id);
		msgLog.setDiv_id(div_id);
		msgLog.setInst_id(inst_id);
		msgLog.setMsg_count(smsSize);
		msgLog.setMsg_date(new Date(new java.util.Date().getTime()));
		msgLog.setMsg_text(message);
		msgLog.setMsg_type(msg_type);
		msgLog.setRole(role);
		String status = null;
		HashMap statusMap = new HashMap();
		statusMap.put("access", true);
		if(settings.getInstituteStats().getSmsLeft()==0 ){
			status = "No sms id remaining";
			statusMap.put("access", false);
			statusMap.put("status", status);
			msgLog.setMsg_status("F");
			msgLog.setMsg_remark("Messages Not available");
		}else if(smsSize>settings.getInstituteStats().getSmsLeft()){
			status = "Only %d sms is left so you can't send %d smses";
			status = String.format(status, settings.getInstituteStats().getSmsLeft(),smsSize);
			statusMap.put("status", status);
			statusMap.put("access", false);
			statusMap.put("leftsms", settings.getInstituteStats().getSmsLeft());
			msgLog.setMsg_status("F");
			msgLog.setMsg_remark("Enough Messages Not available");
		}else{
			msgLog.setMsg_status("S");
			msgLog.setMsg_remark("");
			System.out.println("sending sms");
		}
		NotificationDB notificationDB = new NotificationDB();
		notificationDB.add(msgLog);
		return statusMap;
	}
}
