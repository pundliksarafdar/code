package com.service.helper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.notification.access.NotificationImpl;
import com.notification.email.EmailNotificationTransaction;
import com.notification.sms.SmsNotificationTransaction;
import com.service.beans.ClassownerSettingsNotification;
import com.service.beans.SendAcademicAlertAttendanceBean;
import com.service.beans.SendAcademicAlertFeeDueBean;
import com.service.beans.SendAcademicAlertProgressCardBean;
import com.service.beans.SendNotificationMesssageBean;
import com.transaction.classownersettingtransaction.ClassownerSettingstransaction;

public class NotificationServiceHelper {
	public List<String> sendMessage(SendNotificationMesssageBean bean,String subject,String from,Integer regid){
		List<String> statusList = new ArrayList<String>();
		String status = "";
		String emailStatus = "Email sent successfully";
		if(bean.getTeacher() != null && bean.getTeacher().length != 0){
			//this message is for teacher
			for(String messageType:bean.getMessageType()){
				if(messageType.equals("email")){
					sendEmailToTeacher(bean.getTeacher(), bean.getMessage(),subject,from);
					statusList.add(emailStatus);
				}
				if(messageType.equals("sms")){
					status = sendMessageToTeacher(bean.getTeacher(), bean.getMessage(),regid);
					statusList.add(status);
				}
			}
		}else{
			//this message can be for student or for parent or both
			for(String messageType:bean.getMessageTypeTOST()){
				if(messageType.equals("email")){
					for(String sendTo:bean.getSendTo()){
						if(sendTo.equals("parent")){
							sendEmailToParent(Integer.parseInt(bean.getBatchSelect()),
									Integer.parseInt(bean.getDivisionSelect()),
									regid,bean.getMessage(),
									subject,from);
							statusList.add(emailStatus);
						}else if(sendTo.equals("student")){
							sendEmailToStudent(Integer.parseInt(bean.getBatchSelect()),
									Integer.parseInt(bean.getDivisionSelect()),
									regid,bean.getMessage(),
									subject,from);
							statusList.add(emailStatus);
						}
					}
				}
				if(messageType.equals("sms")){
					for(String sendTo:bean.getSendTo()){
						if(sendTo.equals("parent")){
							status = sendMessageToParent(Integer.parseInt(bean.getBatchSelect()),
									Integer.parseInt(bean.getDivisionSelect()),
									regid,bean.getMessage());
							if(status!=null){
								statusList.add(status + " to parents");
							}
						}else if(sendTo.equals("student")){
							status = sendMessageToStudent(Integer.parseInt(bean.getBatchSelect()),
									Integer.parseInt(bean.getDivisionSelect()),
									regid,bean.getMessage());
							if(status!=null){
								statusList.add(status+" to students");
							}
						}
					}
				}
			}
		}
		
		return statusList;
	}
	
	private void sendEmailToParent(Integer batchId,Integer divId,Integer instId,String message,String subject,String from){
		EmailNotificationTransaction emailNotificationTransaction = new EmailNotificationTransaction();
		emailNotificationTransaction.emailNotificationToParent(batchId, divId, instId,message,subject,from);
		
	}
	
	private void sendEmailToStudent(Integer batchId,Integer divId,Integer instId,String message,String subject,String from){
		EmailNotificationTransaction emailNotificationTransaction = new EmailNotificationTransaction();
		emailNotificationTransaction.emailNotificationToStudent(batchId, divId, instId,message,subject,from);
	}
	
	private String sendMessageToStudent(Integer batchId,Integer divId,Integer instId,String message){
		SmsNotificationTransaction smsNotificationTransaction = new SmsNotificationTransaction();
		return smsNotificationTransaction.smsNotificationToStudent(batchId, divId, instId, message);
	}
	
	private String sendMessageToParent(Integer batchId,Integer divId,Integer instId,String message){
		SmsNotificationTransaction smsNotificationTransaction = new SmsNotificationTransaction();
		return smsNotificationTransaction.smsNotificationToParent(batchId, divId, instId, message);
	}
	
	private String sendMessageToTeacher(Integer[] teacherList,String message,int instId){
		SmsNotificationTransaction smsNotificationTransaction = new SmsNotificationTransaction();
		List<Integer> teacherIds = Arrays.asList(teacherList);
		return smsNotificationTransaction.smsNotificationToTeacher(teacherIds, message,instId);
	}
	
	private void sendEmailToTeacher(Integer[] teacherList,String message,String subject,String from){
		EmailNotificationTransaction emailNotificationTransaction = new EmailNotificationTransaction();
		List<Integer> teacherIds = Arrays.asList(teacherList);
		emailNotificationTransaction.emailNotificationToTeacher(teacherIds, message,subject,from);
	}
	
	public List<String> validateAccess(SendNotificationMesssageBean bean,int classId){
		List<String> status = new ArrayList<String>(); 
		ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingsTx.getSettings(classId);
		List<String> messageType = bean.getMessageType()!=null ? Arrays.asList(bean.getMessageType()):new ArrayList<String>();
		List<String> messageTypeTOST =  bean.getMessageTypeTOST()!=null?Arrays.asList(bean.getMessageTypeTOST()):new ArrayList<String>();
		if((messageType.contains("email")||messageTypeTOST.contains("email")) && !settings.getInstituteStats().isEmailAccess()){
			status.add("No email access");
		}
		if((messageType.contains("sms")||messageTypeTOST.contains("sms")) && !settings.getInstituteStats().isSmsAccess()){
			status.add("No sms access");
		}
		return status;
	}
	
	public void sendFeesDue(SendAcademicAlertFeeDueBean bean,int inst_id ){
		NotificationImpl notificationImpl = new NotificationImpl();
		notificationImpl.sendFeesDueNotification(inst_id, bean.getDivId(), bean.getBatchId(), bean.isEmail(), bean.isSms(), 
				bean.isParent(), bean.isParent());
	}
	
	public void sendAttendance(SendAcademicAlertAttendanceBean bean,int inst_id ){
		NotificationImpl notificationImpl = new NotificationImpl();
		if("day".equals(bean.getType())){
			notificationImpl.sendDailyAttendanceManulNotification(new Date(bean.getDate().getTime()), inst_id, bean.getDivId(),
					bean.getBatchId(), bean.isEmail(), bean.isSms(), bean.isParent(), bean.isStudent(), bean.getMinAttendace());
		}else if("week".equals(bean.getType())){
			notificationImpl.sendWeeklyAttendanceManulNotification(new Date(bean.getDate().getTime()), inst_id, bean.getDivId(),
					bean.getBatchId(), bean.isEmail(), bean.isSms(), bean.isParent(), bean.isStudent(), bean.getMinAttendace());
		}else if("month".equals(bean.getType())){
			notificationImpl.sendMonthlyAttendanceManulNotification(new Date(bean.getDate().getTime()), inst_id, bean.getDivId(),
					bean.getBatchId(), bean.isEmail(), bean.isSms(), bean.isParent(), bean.isStudent(), bean.getMinAttendace());
		}
	}
	
	public void sendProgressCard(SendAcademicAlertProgressCardBean bean,int inst_id){
		NotificationImpl notificationImpl = new NotificationImpl();
		String csv = bean.getExamList().toString().replace("[", "").replace("]", "")
	            .replace(", ", ",");
		notificationImpl.sendStudentProgressCard(inst_id, bean.getDivId(), bean.getBatchId(), csv, bean.isEmail(), bean.isSms(), bean.isParent(), bean.isStudent());
	}
}
