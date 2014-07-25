package com.classuser;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.register.RegisterBean;
import com.classapp.persistence.HibernateUtil;
import com.config.BaseAction;
import com.user.UserBean;

public class DeleteUserAction extends BaseAction{
	private String userId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String performBaseAction(UserBean userBean) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionfactory().openSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("from RegisterBean where loginName = :lname");
		query.setParameter("lname", userId);
		List list = query.list();
		RegisterBean registerBean = (RegisterBean) list.get(0);
		session.delete(registerBean);
		transaction.commit();
		return "success";
	}

}
