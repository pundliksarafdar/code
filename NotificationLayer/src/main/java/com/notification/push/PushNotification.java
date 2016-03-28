package com.notification.push;

import java.util.List;

import com.notification.access.iNotificationAccess;
import com.notification.access.iNotify;
import com.notification.bean.MessageDetailBean;

public class PushNotification implements iNotify{

	@Override
	public String send(MessageDetailBean messageDetailBean,String message){
		System.out.println("Start sending push message for "+messageDetailBean.getStudentId()+" wait time:3sec");
		try {
			//wait(3000);
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("End sending push message for "+messageDetailBean.getStudentId()+" wait time:3sec");
		return null;
	}

}
