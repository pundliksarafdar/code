package com.notification.access;

import java.io.IOException;
import java.io.StringWriter;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.notification.bean.MessageDetailBean;
import com.util.NotificationEnum;

public class MessageFormatter {
	public String formatMessage(MessageDetailBean messageDetailBean,NotificationEnum.MessageType messageType) {
		
		if(NotificationEnum.MessageType.SMS.equals(messageType)){
			if(messageDetailBean.getSmsMessage() != null && messageDetailBean.getSmsMessage().trim().length() == 0){
				return messageDetailBean.getSmsMessage();
			}else{
				String templates =  messageDetailBean.getSmsTemplate();
				Object bean = messageDetailBean.getSmsObject();
				MustacheFactory mustacheFactory = new DefaultMustacheFactory();
		        Mustache mustache = mustacheFactory.compile(templates);
		        StringWriter writer = new StringWriter();
		        try {
					mustache.execute(writer, bean).flush();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				return writer.toString();
			}
		}else if(NotificationEnum.MessageType.EMAIL.equals(messageType)){
			if(messageDetailBean.getEmailMessage() != null && messageDetailBean.getEmailMessage().trim().length() != 0){
				return messageDetailBean.getEmailMessage();
			}else{
				String templates =  messageDetailBean.getEmailTemplate();
				Object bean = messageDetailBean.getEmailObject();
				MustacheFactory mustacheFactory = new DefaultMustacheFactory();
		        Mustache mustache = mustacheFactory.compile(templates);
		        StringWriter writer = new StringWriter();
		        try {
					mustache.execute(writer, bean).flush();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				return writer.toString();
			}
		}else if(NotificationEnum.MessageType.PUSH.equals(messageType)){
			if(messageDetailBean.getPushMessage() != null && messageDetailBean.getPushMessage().trim().length() == 0){
				return messageDetailBean.getPushMessage();
			}else{
				String templates =  messageDetailBean.getPushTemplate();
				Object bean = messageDetailBean.getPushObject();
				MustacheFactory mustacheFactory = new DefaultMustacheFactory();
		        Mustache mustache = mustacheFactory.compile(templates);
		        StringWriter writer = new StringWriter();
		        try {
					mustache.execute(writer, bean).flush();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				return writer.toString();
			}
		}else{
			return null;
		}
	}
}
