package com.notification.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.classapp.db.classOwnerSettings.ClassOwnerNotificationDb;
import com.classapp.db.classOwnerSettings.ContactDetailBean;
import com.classapp.db.user.ContactBean;
import com.notification.bean.MessageDafaultInterface;
import com.notification.bean.MessageDetailBean;
import com.transaction.classownersettingtransaction.ClassownerSettingstransaction;
import com.util.NotificationEnum;

public class Notification {
	public void send(NotificationEnum.MessageCategery messageCategery,
				HashMap<Integer,MessageDafaultInterface> studentIdNMessageBeanMap,
				int instId){
		ClassownerSettingstransaction transaction = new ClassownerSettingstransaction();
		String validationMessage = transaction.validateClassOwnerAccess(messageCategery, instId);
		//if validation message are null then having error
		if(validationMessage != null){
			return;
		}				
		
		List<MessageDetailBean> messageDetailBeans = formObject(messageCategery, studentIdNMessageBeanMap, instId);
		NotifcationAccess na = new NotifcationAccess();
		na.send(messageDetailBeans);
		
	}
	
	private List<MessageDetailBean> formObject(NotificationEnum.MessageCategery messageCategery,
			HashMap<Integer,MessageDafaultInterface> studentIdNMessageBeanMap,
			int instId){
		List<MessageDetailBean> messageDetailBeans = new ArrayList<MessageDetailBean>();
		
		Iterator iterator = studentIdNMessageBeanMap.entrySet().iterator();
		List<Integer> studentIds = new ArrayList<Integer>();
		List<MessageDafaultInterface> messageDafaultInterfaces = new ArrayList<MessageDafaultInterface>();
		while (iterator.hasNext()) {
			Map.Entry<Integer, MessageDafaultInterface> entry = 
					(Map.Entry<Integer, MessageDafaultInterface>)iterator.next();
			studentIds.add(entry.getKey());
		}
		
		ClassOwnerNotificationDb db = new ClassOwnerNotificationDb();
		List<ContactDetailBean> contacts = db.getContactDetailStudentParent(studentIds, instId);
		
		while (iterator.hasNext()) {
			MessageDetailBean messageDetailBean = new MessageDetailBean();
			Map.Entry<Integer, MessageDafaultInterface> entry = 
					(Map.Entry<Integer, MessageDafaultInterface>)iterator.next();
			MessageDafaultInterface dafaultInterface = entry.getValue();
			ContactDetailBean contactBean = contacts.stream().filter(c->c.getStudentId() == entry.getKey()).collect(Collectors.toList()).get(0);
			messageDetailBean.setEmailMessage(dafaultInterface.getEmailMessage());
			messageDetailBean.setEmailTemplate(dafaultInterface.getEmailTemplate());
			messageDetailBean.setEmailObject(dafaultInterface.getEmailObject());
			
			messageDetailBean.setSmsMessage(dafaultInterface.getSmsMessage());
			messageDetailBean.setSmsTemplate(dafaultInterface.getSmsTemplate());
			messageDetailBean.setSmsObject(dafaultInterface.getSmsObject());
			
			messageDetailBean.setPushMessage(dafaultInterface.getPushMessage());
			messageDetailBean.setPushTemplate(dafaultInterface.getPushTemplate());
			messageDetailBean.setPushObject(dafaultInterface.getPushObject());
			
			messageDetailBean.setStudentEmail(contactBean.getStudentEmail());
			messageDetailBean.setStudentPhone(contactBean.getStudentPhone());
			messageDetailBean.setParentEmail(contactBean.getParentEmail());
			messageDetailBean.setParentPhone(contactBean.getParentPhone());
			messageDetailBean.setFrom(instId+"");
			messageDetailBeans.add(messageDetailBean);
		}
		return messageDetailBeans;
	}
	
	public static void main(String[] args) {
		ClassOwnerNotificationDb db = new ClassOwnerNotificationDb();
		List list = new ArrayList<Integer>();
		list.add(8);
		list.add(11);
		db.getContactDetailStudentParent(list, 4);
	}
}
