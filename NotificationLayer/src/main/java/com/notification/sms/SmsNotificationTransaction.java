package com.notification.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDetails;
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
		return sendSms(smsIds, message,instId);
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
		return sendSms(smsIds, message,instId);
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
		return sendSms(smsIds, message,instId);
	}
	
	private String sendSms(List<String>smss,String message,int classId){
		
		HashMap statusMap = validateNSendSms(smss, message, classId);
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
							//inotify.sendEmail(sms, message, subject, from);
						}
					}).start();
				}
			}
		}
		return (String) statusMap.get("status");
	}
	
	private HashMap validateNSendSms(List<String>smss,String message,int classId){
		ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingsTx.getSettings(classId);
		String status = null;
		HashMap statusMap = new HashMap();
		statusMap.put("access", true);
		int smsSize = smss.size();
		if(settings.getInstituteStats().getSmsLeft()==0 ){
			status = "No sms id remaining";
			statusMap.put("access", false);
			statusMap.put("status", status);
		}else if(smsSize>settings.getInstituteStats().getSmsLeft()){
			smss = smss.subList(0, settings.getInstituteStats().getSmsLeft());
			status = "Only %d sms is left so you can't send %d smses";
			status = String.format(status, settings.getInstituteStats().getSmsLeft(),smsSize);
			statusMap.put("status", status);
			statusMap.put("access", false);
			statusMap.put("leftsms", settings.getInstituteStats().getSmsLeft());
		}else{
			System.out.println("sending sms");
		}
		return statusMap;
	}
}
