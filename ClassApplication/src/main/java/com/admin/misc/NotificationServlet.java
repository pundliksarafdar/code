package com.admin.misc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.dispatcher.SessionMap;

import com.user.UserBean;
import com.classapp.persistence.Constants;
import com.google.gson.JsonObject;
import com.transaction.notification.NotificationGlobalTransation;
import com.transaction.notification.NotificationResult;

public class NotificationServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String methodeToCall = (String) req.getParameter("methodToCall");
		PrintWriter writer = resp.getWriter();
		
		JsonObject resultJson = new JsonObject();
		
		HttpSession session = req.getSession();
		UserBean userBean = (UserBean) session.getAttribute("user");
		if(userBean.getRole()==0){
			if("notifyall".equalsIgnoreCase(methodeToCall)){
				String message = (String) req.getParameter("message");
				if(null != message && message.trim().length()!=0){
					NotificationGlobalTransation transation = new NotificationGlobalTransation();
					NotificationResult notificationResult = transation.notificationToAll(message);
					if(notificationResult.getFailure()>0){
						resultJson.addProperty("status", "error");
						resultJson.addProperty("message", "Some id are in failure");
					}else{
						resultJson.addProperty("status", "success");
						resultJson.addProperty("message", "Messages sent");
					}
				}else{
					resultJson.addProperty("status", "error");
					resultJson.addProperty("message", "Message Can not be null");
				}
			}
		}else{
			resultJson.addProperty("status", "unauthorised");	
		}
		
		writer.write(resultJson.toString());
	}
}
