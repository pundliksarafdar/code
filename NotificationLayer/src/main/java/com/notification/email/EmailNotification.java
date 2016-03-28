package com.notification.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.notification.access.iNotify;
import com.notification.bean.MessageDetailBean;

public class EmailNotification implements iNotify{

	@Override
	public String send(MessageDetailBean messageDetailBean,String message) {
		if(messageDetailBean.isSendToStudent()){
			this.sendEmail(messageDetailBean.getStudentEmail(), message, "",messageDetailBean.getFrom());
		}
		
		if(messageDetailBean.isSendToParent()){
			this.sendEmail(messageDetailBean.getParentEmail(), message, "",messageDetailBean.getFrom());
		}
		return null;
	}
	
	private boolean sendEmail(String email, String htmlMessage,String subject,String fromAlice) {
		// Common variables
		String to = email;

		// Sender's email ID needs to be mentioned
		String from = "mycorex2015@gmail.com";// change accordingly
		final String username = "mycorex2015";// change accordingly
		final String password = "corex2015";// change accordingly

		// Assuming you are sending email through relay.jangosmtp.net
		String host = "smtp.gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			InternetAddress fromAddress = null;
			try {
				fromAddress=new InternetAddress(from, fromAlice);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message.setFrom(fromAddress);

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setContent(htmlMessage, "text/html");

			// Send message
			Transport.send(message);

			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}


}
