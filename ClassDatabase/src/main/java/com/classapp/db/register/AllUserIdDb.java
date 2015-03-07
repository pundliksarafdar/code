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
	
	public List<String> getAllUserIdrelatedtoBatch(String Batchid){
		List<String> allUserIds = new ArrayList<String>();
		
		Session session = HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery("select googleId from AllUserId where regId in (select student_id from Student where (batch_id like :batch1 or batch_id like :batch2 or batch_id like :batch3 or batch_id= :batch4))");
		query.setParameter("batch1", Batchid+",%");
		query.setParameter("batch2", "%,"+Batchid+",%");
		query.setParameter("batch3", "%,"+Batchid);
		query.setParameter("batch4", Batchid);
		allUserIds = query.list();
		return allUserIds;
	}
}
