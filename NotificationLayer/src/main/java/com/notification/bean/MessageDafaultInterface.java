package com.notification.bean;

public abstract class MessageDafaultInterface {
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
		
		public abstract String getSmsMessage();
		public abstract void setSmsMessage(String smsMessage);
		public abstract String getSmsTemplate();
		public abstract void setSmsTemplate(String smsTemplate);
		public abstract Object getSmsObject();
		public abstract void setSmsObject(Object smsObject);
		public abstract String getEmailMessage();
		public abstract void setEmailMessage(String emailMessage);
		public abstract String getEmailTemplate();
		public abstract void setEmailTemplate(String emailTemplate);
		public abstract Object getEmailObject();
		public abstract void setEmailObject(Object emailObject);
		public abstract String getPushMessage();
		public abstract void setPushMessage(String pushMessage);
		public abstract String getPushTemplate();
		public abstract void setPushTemplate(String pushTemplate);
		public abstract Object getPushObject();
		public abstract void setPushObject(Object pushObject);
		
}
