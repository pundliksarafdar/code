package com.classapp.db.register;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.persistence.HibernateUtil;

public class AdditionalStudentInfoDb {
	public void saveAdditionalStudentInfo(AdditionalStudentInfoBean studentInfoBean){
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(studentInfoBean);
		transaction.commit();
		if(session != null){session.close();}
		
	}
	
	public AdditionalStudentInfoBean getAdditionalStudetnInfoBean(int studentId,int instId){
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(AdditionalStudentInfoBean.class);
		Criterion criterion = Restrictions.eq("instId", instId);
		criteria.add(criterion);
		criterion = Restrictions.eq("studentId", studentId);
		criteria.add(criterion);
		List<AdditionalStudentInfoBean> beans = criteria.list();
		return beans.size()>0?beans.get(0):new AdditionalStudentInfoBean();
		
	}
	
	public static void main(String[] args) {
		AdditionalStudentInfoBean formFieldBean = new AdditionalStudentInfoBean();
		formFieldBean.setInstId(189);
		formFieldBean.setInstStudentId(100);
		formFieldBean.setStudentData("studentData");
		formFieldBean.setStudentId(100);
		AdditionalStudentInfoDb db = new AdditionalStudentInfoDb();
		db.saveAdditionalStudentInfo(formFieldBean);
	}
}
