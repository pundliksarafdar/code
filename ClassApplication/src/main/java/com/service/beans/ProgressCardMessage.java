package com.service.beans;

import com.notification.bean.MessageDafaultInterface;

public class ProgressCardMessage extends MessageDafaultInterface {
	//if message is null parse template and object 
			String smsMessage;
			String smsTemplate;
			Object smsObject;
			
			//if message is null parse template and object 
			String emailMessage;
			String emailTemplate;
			Object emailObject;
				
			//if message is null parse template and object 
			String pushMessage;
			String pushTemplate;
			Object pushObject;
			public String getSmsMessage() {
				return smsMessage;
			}
			public void setSmsMessage(String smsMessage) {
				this.smsMessage = smsMessage;
			}
			public String getSmsTemplate() {
				return smsTemplate;
			}
			public void setSmsTemplate(String smsTemplate) {
				this.smsTemplate = smsTemplate;
			}
			public Object getSmsObject() {
				return smsObject;
			}
			public void setSmsObject(Object smsObject) {
				this.smsObject = smsObject;
			}
			public String getEmailMessage() {
				return emailMessage;
			}
			public void setEmailMessage(String emailMessage) {
				this.emailMessage = emailMessage;
			}
			public String getEmailTemplate() {
				return emailTemplate;
			}
			public void setEmailTemplate(String emailTemplate) {
				this.emailTemplate = emailTemplate;
			}
			public Object getEmailObject() {
				return emailObject;
			}
			public void setEmailObject(Object emailObject) {
				this.emailObject = emailObject;
			}
			public String getPushMessage() {
				return pushMessage;
			}
			public void setPushMessage(String pushMessage) {
				this.pushMessage = pushMessage;
			}
			public String getPushTemplate() {
				return pushTemplate;
			}
			public void setPushTemplate(String pushTemplate) {
				this.pushTemplate = pushTemplate;
			}
			public Object getPushObject() {
				return pushObject;
			}
			public void setPushObject(Object pushObject) {
				this.pushObject = pushObject;
			}
			
}
