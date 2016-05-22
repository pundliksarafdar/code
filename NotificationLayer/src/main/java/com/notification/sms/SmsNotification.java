package com.notification.sms;

import java.util.List;

import com.notification.access.iNotify;
import com.notification.bean.MessageDetailBean;
import com.service.beans.ClassownerSettingsNotification;
import com.transaction.classownersettingtransaction.ClassownerSettingstransaction;

public class SmsNotification implements iNotify{

	@Override
	public String send(MessageDetailBean messageDetailBean,String message) {
		System.out.println("Start sending sms message for "+messageDetailBean.getStudentId()+" wait time:1sec");
		try {
		//	wait(1000);
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("End sending sms message for "+messageDetailBean.getStudentId()+" wait time:1sec");
		return null;
	}
	
	public String sendSms(List<String>smss,String message,int classId){
		return "";
	}

}
