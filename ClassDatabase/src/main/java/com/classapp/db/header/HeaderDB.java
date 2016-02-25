package com.classapp.db.header;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.Feedbacks.Feedback;
import com.classapp.db.exam.Exam;
import com.classapp.db.question.Questionbank;
import com.classapp.persistence.HibernateUtil;

public class HeaderDB {
	public void save(String headerId,String headerName,int instId) {
		Header header = new Header();
		header.setHeader_id(headerId);
		header.setHeader_name(headerName);
		header.setInst_id(instId);
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(header);
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
	}
	
	public List<Header> getHeaderList(int inst_id) {
		Exam exam=new Exam();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(Header.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		List<Header> headerList = criteria.list();
		transaction.commit();
		session.close();
		return  headerList;	
	}

}
