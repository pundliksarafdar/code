package com.notification.access;

import java.util.ArrayList;
import java.util.List;

import com.notification.bean.MessageDetailBean;

public class NotifyTest {
	public static void main(String[] args) {
		System.out.println("Message......");
		List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
		for(int i =0; i<15; i++){
			MessageDetailBean messageDetailBean = new MessageDetailBean();
			messageDetailBean.setMessageTypeEmail(true);
			messageDetailBean.setStudentEmail("student@gmail.com");
			
			messageDetailBean.setMessageTypePush(true);
			messageDetailBean.setStudentPhoneId("100");
			messageDetailBean.setStudentPhoneType("Android");
			
			messageDetailBean.setMessageTypeSms(true);
			messageDetailBean.setStudentPhone(1000);
			
			messageDetailBean.setSendToStudent(true);
			
			/***************************************/
			messageDetailBean.setMessageTypeEmail(true);
			messageDetailBean.setParentEmail("student@gmail.com");
			
			messageDetailBean.setMessageTypePush(true);
			messageDetailBean.setParentPhoneId("100");
			messageDetailBean.setParentPhoneType("Android");
			
			messageDetailBean.setMessageTypeSms(true);
			messageDetailBean.setParentPhone(1000);
			
			messageDetailBean.setSendToParent(true);
			
			/*******************************************/
			messageDetailBean.setStudentId(i);
			
			detailBeans.add(messageDetailBean);
		}
		
		NotifcationAccess notifcationAccess = new NotifcationAccess();
		notifcationAccess.send(detailBeans);
	}
}
