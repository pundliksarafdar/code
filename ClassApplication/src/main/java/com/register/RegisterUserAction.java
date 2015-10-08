package com.register;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.classapp.db.register.RegisterUser;
import com.classapp.logger.AppLogger;
import com.config.BaseAction;
import com.config.Constants;
import com.google.gson.Gson;
import com.mails.AllMail;
import com.signon.LoginBean;
import com.signon.LoginUser;
import com.transaction.register.RegisterTransaction;
import com.user.UserBean;

public class RegisterUserAction extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RegisterBean registerBean;

	public RegisterBean getRegisterBean() {
		return registerBean;
	}

	public void setRegisterBean(RegisterBean registerBean) {
		this.registerBean = registerBean;
	}

	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		RegisterUser registerUser = new RegisterUser();
		Gson gson = new Gson();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String formatedDate = dateFormat.format(date);
		registerBean.setDob(registerBean.getDob().replace("-", ""));
		registerBean.setRegistrationDate(formatedDate);
		Long activationcode=new Date().getTime();
		registerBean.setActivationcode(activationcode.toString());
		registerBean.setStartDate(dateFormat.format(new Date()));
		String registerReq = gson.toJson(registerBean);
		
		RegisterTransaction registerTransaction = new RegisterTransaction();
		String status =  registerTransaction.registerUser(registerReq,registerBean.getLoginName(), registerBean.getPhone1(),registerBean.getEmail());
		AppLogger.logger("In Register user action - Register User Status..."+status);
		if("success".equals(status)){
			AllMail allMail = new AllMail();
			HashMap<String, String> hashMap = new  HashMap();
			hashMap.put("HEADER", "Register!!!");
			hashMap.put("classLink", "http://jbdev-mycorex.rhcloud.com/login");
			hashMap.put("ACTIVATION_CODE", registerBean.getActivationcode());	
			hashMap.put("NAME", registerBean.getFname());
			allMail.sendMail(registerBean.getEmail(), hashMap, "registerSuccess.html","ClassApp - Registered");
			LoginBean loginBean = new LoginBean();
			loginBean.setLoginname(registerBean.getLoginName());
			loginBean.setLoginpass(registerBean.getLoginPass());
			LoginUser loginUser = new LoginUser();
			String forward = "success";
			if(null != loginBean){
				userBean.setLoginBean(loginBean);
			    loginUser.loadBean(userBean, loginBean,response,session);
			}
			
			return forward;
		}else{
			request = ServletActionContext.getRequest();
			request.setAttribute(Constants.ERROR_MESSAGE, status);
			return "error";
		}
	}
	
	public void sendEmail(RegisterBean registerBean){
		// Common variables
		String to = registerBean.getEmail();//change accordingly

	    // Sender's email ID needs to be mentioned
	    String from = "mycorex2015@gmail.com";//change accordingly
	    final String username = "mycorex2015";//change accordingly
	    final String password = "corex2015";//change accordingly

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
	       message.setSubject("Corex Activation message");

	       // Now set the actual message
	       message.setText("Hello, " +
	       		"Your Activation Code is :-"+registerBean.getActivationcode());

	       // Send message
	     Transport.send(message);

	       AppLogger.logger("Sent message successfully....");

	    } catch (MessagingException e) {
	    	AppLogger.logger("Invalid Internet Address");
	          throw new RuntimeException(e);
	    }
	}
	
}
