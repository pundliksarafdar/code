package com.classapp.db.register;

import org.hibernate.Session;

import com.classapp.db.register.RegisterBean;
import com.classapp.persistence.Constants;
import com.classapp.persistence.HibernateUtil;
import com.classapp.servicetable.ServiceMap;
import com.google.gson.Gson;
import com.classapp.logger.AppLogger;

public class RegisterUser {
	public String registerUser(String registerRequest){
			AppLogger.logger("");
			final String success = "success";
			Gson gson = new Gson();
			RegisterBean registerBean = gson.fromJson(registerRequest, RegisterBean.class);
			RegisterBean registerBeantoSave = registerBean;
			String errorMessage = success;
			
			Session session = HibernateUtil.getSessionfactory().openSession();
			session.beginTransaction();
			
			try{
				session.save(registerBeantoSave);
				session.getTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				session.getTransaction().rollback();
				errorMessage = formatMessage(e.getCause().getMessage());
				return errorMessage;
			}finally{
				if(null!=session){
					session.close();
				}
			}
			return errorMessage;
	
	}
	
	public String formatMessage(String message){
		String errorMessage = "";
		String duplicate = "Duplicate";
		String isDebugging = ServiceMap.getSystemParam(Constants.DEBUGGING_MODE, "isdebug");
		boolean isDebuggingBool = null!=isDebugging && isDebugging.equals("yes");
		
		if (message.contains("UNI_PHONE1") && message.contains(duplicate)) {
			errorMessage = "This contact number is already registered, Please contact administrator"; 
		}else if(message.contains("LGNAME") && message.contains(duplicate)){
			errorMessage = "This username is already registered, You can not use this username";
		}
		return errorMessage;
	}
}
