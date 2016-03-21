package com.notification.email;

import com.notification.access.iNotify;
import com.notification.bean.MessageDetailBean;

public class EmailNotification implements iNotify{

	@Override
	public String send(MessageDetailBean messageDetailBean) {
		
		System.out.println("Start sending email message for "+messageDetailBean.getStudentId()+" wait time:5sec");
		try {
			//wait(5000);
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("End sending email message for "+messageDetailBean.getStudentId()+" wait time:5sec");
		return null;
	}

}
