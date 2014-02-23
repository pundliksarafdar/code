package com.classapp.edit;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.register.RegisterBean;
import com.classapp.persistence.Constants;
import com.classapp.persistence.HibernateUtil;
import com.google.gson.Gson;

public class EditUser {
	public String editUser(String editMap){
		String status = Constants.ERROR;
		Session session = HibernateUtil.getSessionfactory().openSession();
		Transaction transaction = session.beginTransaction();
		Gson gson = new Gson();
		try{
			RegisterBean registerBean = gson.fromJson(editMap, RegisterBean.class);
			session.update(registerBean);
			transaction.commit();
			status = Constants.SUCCESS;
		}catch(Exception e){
			transaction.rollback();
			status = Constants.ERROR;
		}
		session.close();
		return status;
	}
}
