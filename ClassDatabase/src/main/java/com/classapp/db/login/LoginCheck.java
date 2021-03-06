package com.classapp.db.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.exam.ExamDB;
import com.classapp.db.question.QuestionbankDB;
import com.classapp.db.register.AllUserId;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.user.UserBean;
import com.classapp.persistence.HibernateUtil;
import com.google.gson.Gson;

public class LoginCheck implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(LoginCheck.class);
	RegisterBean registerBean;
	public String loadUserObject(String username,String password){
		Gson gson = new Gson();
		Session session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("from RegisterBean where loginName = :lname and loginPass = :lpass");
		query.setParameter("lname", username);
		query.setParameter("lpass", password);
		List list = query.list();
		System.out.println(list);
		LOGGER.debug("In LoginCheck......");
		
		UserBean userBean = new UserBean();
		Iterator iterator = list.iterator();
		
		while (iterator.hasNext()) {
			
			registerBean = (RegisterBean)iterator.next();
			System.out.println(registerBean);
		}
		try{
			userBean.setFirstname(registerBean.getFname());
			userBean.setLastname(registerBean.getLname());
			userBean.setMiddlename(registerBean.getMname());
			userBean.setDob(registerBean.getDob());
			userBean.setAddr1(registerBean.getAddr1());
			userBean.setAddr2(registerBean.getAddr2());
			userBean.setCity(registerBean.getCity());
			userBean.setState(registerBean.getState());
			userBean.setCountry(registerBean.getCountry());
			userBean.setPhone1(registerBean.getPhone1());
			userBean.setPhone2(registerBean.getPhone2());
			userBean.setRole(registerBean.getRole());
			userBean.setStartdate(registerBean.getStartDate());
			userBean.setEnddate(registerBean.getEndDate());
			userBean.setRegId(registerBean.getRegId());
			userBean.setActivationcode(registerBean.getActivationcode());
			userBean.setStatus(registerBean.getStatus());
			userBean.setUsername(registerBean.getLoginName());
			userBean.setEmail(registerBean.getEmail());
			userBean.setClassName(registerBean.getClassName());
			return gson.toJson(userBean);
		}catch(Exception ex){
			return null;
		}
		
	}
	
	public void updateIdForUser(Integer regId,String googleId, String deviceId){
		Session session = HibernateUtil.getSessionfactory().openSession();
		Transaction transaction = session.beginTransaction();
		AllUserId allUserId = new AllUserId();
		allUserId.setDeviceId(deviceId);
		allUserId.setGoogleId(googleId);
		allUserId.setRegId(regId);
		//Query query = session.createQuery("from AllUserId where regId = :regId");
		session.saveOrUpdate(allUserId);
		transaction.commit();
	}
		
}
