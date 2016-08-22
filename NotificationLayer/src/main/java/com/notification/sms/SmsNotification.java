package com.notification.sms;

import java.io.IOException;
import java.util.List;

import com.notification.access.MessageFormatter;
import com.notification.access.iNotify;
import com.notification.bean.MessageDetailBean;
import com.notification.test.SMSSentMock;
import com.util.NotificationEnum;

public class SmsNotification implements iNotify{

	@Override
	public String send(MessageDetailBean messageDetailBean,String message) {
		System.out.println("Start sending sms message for "+messageDetailBean.getStudentId()+" wait time:1sec");
		try {
			SMSSentMock smsSentMock = new SMSSentMock();
			if(messageDetailBean.isSendToStudent()){
				smsSentMock.sms(messageDetailBean.getStudentPhone(), message,messageDetailBean.getSmsType());
			} if(messageDetailBean.isSendToParent()){
				MessageFormatter formatter = new MessageFormatter();
				 message = formatter.formatParentMessage(messageDetailBean, NotificationEnum.MessageType.SMS);
				smsSentMock.sms(messageDetailBean.getParentPhone(), message,messageDetailBean.getSmsType());
			}
			if(messageDetailBean.isSendToTeacher()){
				smsSentMock.sms(messageDetailBean.getTeacherPhone(), message,messageDetailBean.getSmsType());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("End sending sms message for "+messageDetailBean.getStudentId()+" wait time:1sec");
		return null;
	}
	
	public String sendSms(List<String>smss,String message,int classId){
		return "";
	}

}
