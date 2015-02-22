package com.mails;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.miscfunction.MiscFunction;

public class AllMail {
	public static void main(String[] args) {
		AllMail allMail = new AllMail();
		HashMap<String, String> hashMap = new  HashMap();
		hashMap.put("HEADER", "Register!!!");
		hashMap.put("classLink", "http://jbdev-mycorex.rhcloud.com/login");
		allMail.sendMail("sarafdarpundlik@gmail.com", hashMap, "registerSuccess.html","Success");
	}
	
	/**
	 * 
	 * <p>Function to send the email</p>
	 * @param email
	 * @param message
	 * @param htmlMailTemplateName
	 * @param subject
	 * @author Pundlik Sarafdar
	 * 
	 */
	public boolean sendMail(String email,HashMap<String, String>message,String htmlMailTemplateName,String subject){
		String rawEmail = readHtmlFile(htmlMailTemplateName);
		String emailString = formatMessage(rawEmail, message);
		boolean result = sendEmail(email, emailString,subject);
		return result;
		
	}
	
	private String formatMessage(String rawEmail,HashMap<String, String> hashMap){
		Iterator it = hashMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        String key = (String) pair.getKey();
	        String value = (String) pair.getValue();
	        rawEmail = rawEmail.replace("%"+key+"%", value);
	    }
	    return rawEmail;
	}
	
	private String readHtmlFile(String htmlFileName){
		String htmlText = null;
		StringBuilder result = new StringBuilder("");
		//ClassLoader classLoader = getClass().getClassLoader();
		//ServletContext context = ServletActionContext.getServletContext();// = getContext();
		ServletContext context = MiscFunction.getServletContext();
		String fullPath = context.getRealPath("/WEB-INF/classes/htmlfile/"+htmlFileName);
		//File file = new File(classLoader.getResource("htmlfile/"+htmlFileName).getFile());
		File file = new File(fullPath);
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
	 
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	private boolean sendEmail(String email, String htmlMessage,String subject) {
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
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setContent(htmlMessage, "text/html");

			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");
			return true;
		} catch (MessagingException e) {
			System.out.println("Invalid Internet Address");
			return false;
		}
	}

}
