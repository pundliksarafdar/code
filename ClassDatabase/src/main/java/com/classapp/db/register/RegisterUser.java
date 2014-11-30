package com.classapp.db.register;

import org.hibernate.Session;

import com.classapp.db.register.RegisterBean;
import com.classapp.persistence.HibernateUtil;
import com.google.gson.Gson;

public class RegisterUser {
	public String registerUser(String registerRequest){
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
		if (message.contains("UNI_PHONE1") && message.contains(duplicate)) {
			errorMessage = "This contact number is already registered, Please contact administrator"; 
		}else if(message.contains("LGNAME") && message.contains(duplicate)){
			errorMessage = "This username is already registered, You can not use this username";
		}
		return errorMessage;
	}
}