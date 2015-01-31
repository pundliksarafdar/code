package com.classapp.db.register;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.classapp.persistence.HibernateUtil;

public class AllUserIdDb {
	public List<AllUserId> getAllUserId(){
		List<AllUserId> allUserIds = new ArrayList<AllUserId>();
		
		Session session = HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery("from AllUserId");
		allUserIds = query.list();
		return allUserIds;
	}
}
