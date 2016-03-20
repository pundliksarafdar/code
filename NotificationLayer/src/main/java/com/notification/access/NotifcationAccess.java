package com.notification.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.notification.bean.MessageDetailBean;
import com.notification.email.EmailNotification;
import com.notification.push.PushNotification;
import com.notification.sms.SmsNotification;

public class NotifcationAccess implements iNotificationAccess{
	String msg = "";
	static String INVALID_STUDENT_PHONE = "Student phone number is invalid",
			INVALID_STUDENT_PHONE_ID = "Student phone id is invalid",
			INVALID_STUDENT_EMAIL = "Student email is invalid";
	
	static String INVALID_PARENT_PHONE = "Parent phone number is invalid",
			INVALID_PARENT_PHONE_ID = "Parent phone id is invalid",
			INVALID_PARENT_EMAIL = "Parent email is invalid";
	@Override
	public String send(List<MessageDetailBean> messageDetailBeans) {
		HashMap<MessageDetailBean,List<String>>  invalidIdMap = validate(messageDetailBeans);
		for(MessageDetailBean messageDetailBean:messageDetailBeans){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!invalidIdMap.containsKey(messageDetailBean)){
				if(messageDetailBean.isMessageTypeEmail()){
					sendEmail(messageDetailBean);
				}
				
				if(messageDetailBean.isMessageTypePush()){
					sendPush(messageDetailBean);
				}
				
				if(messageDetailBean.isMessageTypeSms()){
					sendSms(messageDetailBean);
				}
			}
		}
		
		return null;
	}
	
	public HashMap<MessageDetailBean,List<String>> validate(List<MessageDetailBean> messageDetailBeans) {
		HashMap<MessageDetailBean,List<String>> map = new HashMap<MessageDetailBean,List<String>>();
		List<String> messageList = new ArrayList<String>();
		for(MessageDetailBean messageDetailBean:messageDetailBeans){
			System.out.println("Starting message "+messageDetailBean.getStudentId());
			//Student messages
			if(messageDetailBean.isSendToStudent()){
				//Send email to student
				if(messageDetailBean.isMessageTypeEmail() &&
						(null == messageDetailBean.getStudentEmail() || messageDetailBean.getStudentEmail().isEmpty())){
					messageList.add(INVALID_STUDENT_EMAIL);
				}
				
				//Send sms to student
				if( messageDetailBean.isMessageTypeSms() &&
						0 == messageDetailBean.getStudentPhone()){
					messageList.add(INVALID_STUDENT_PHONE);
				}
				
				//Send app notification
				if( messageDetailBean.isMessageTypePush() &&
						(null == messageDetailBean.getStudentPhoneId() || messageDetailBean.getStudentPhoneId().isEmpty() ||
						null == messageDetailBean.getStudentPhoneType() || messageDetailBean.getStudentPhoneType().isEmpty())){
					messageList.add(INVALID_STUDENT_PHONE_ID);
				}
			}
			
			//Student messages
			if(messageDetailBean.isSendToParent()){
				//Send email to student
				if(messageDetailBean.isMessageTypeEmail() &&
						(null == messageDetailBean.getParentEmail() || messageDetailBean.getParentEmail().isEmpty())){
					messageList.add(INVALID_PARENT_EMAIL);
				}
				
				//Send sms to student
				if( messageDetailBean.isMessageTypeSms() &&
						0 == messageDetailBean.getParentPhone()){
					messageList.add(INVALID_PARENT_PHONE);
				}
				
				//Send app notification
				if( messageDetailBean.isMessageTypePush() &&
						(null == messageDetailBean.getParentPhoneId() || messageDetailBean.getParentPhoneId().isEmpty() ||
						null == messageDetailBean.getParentPhoneType() || messageDetailBean.getParentPhoneType().isEmpty())){
					messageList.add(INVALID_PARENT_PHONE_ID);
				}
			}
			if(!messageList.isEmpty()){
				map.put(messageDetailBean, messageList);
			}
		}
		
		return map;
	}
	
	private void sendEmail(final MessageDetailBean messageDetailBean){
		final EmailNotification inotify = new EmailNotification();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				inotify.send(messageDetailBean);
			}
		}).start();
		
	}
	
	private void sendSms(final MessageDetailBean messageDetailBean){
		final SmsNotification inotify = new SmsNotification();
		new Thread(new Runnable() {
			@Override
			public void run() {
				inotify.send(messageDetailBean);
			}
		}).start();
		
	}
	
	private void sendPush(final MessageDetailBean messageDetailBean){
		final PushNotification inotify = new PushNotification();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				inotify.send(messageDetailBean);
			}
		}).start();
	}
}
