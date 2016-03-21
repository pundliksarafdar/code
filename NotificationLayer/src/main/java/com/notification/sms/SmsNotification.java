package com.notification.sms;

import com.notification.access.iNotify;
import com.notification.bean.MessageDetailBean;

public class SmsNotification implements iNotify{

	@Override
	public String send(MessageDetailBean messageDetailBean) {
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

}
