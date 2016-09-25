package com.classapp.db.register;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.printFees.PrintFees;
import com.classapp.persistence.HibernateUtil;

public class AdditionalFormFieldDb {
	public void saveAdditionalFormField(AdditionalFormFieldBean formFieldBean){
		/*
		Session session = HibernateUtil.getSessionfactory().openSession();
		Transaction transaction = session.beginTransaction();
		transaction.begin();
		session.saveOrUpdate(formFieldBean);
		transaction.commit();
		session.close();
		*/
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(formFieldBean);
		transaction.commit();
		if(session != null){session.close();}
		
	}
	
	public AdditionalFormFieldBean getAdditionalFormFieldBean(int instId){
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(AdditionalFormFieldBean.class);
		Criterion criterion = Restrictions.eq("instId", instId);
		criteria.add(criterion);
		List<AdditionalFormFieldBean> beans = criteria.list();
		AdditionalFormFieldBean additionalFormFieldBean = new AdditionalFormFieldBean();
		if(beans.size()>0){
			additionalFormFieldBean = beans.get(0);
		}
		return additionalFormFieldBean;
			
		
	}
	
	public static void main(String[] args) {
		AdditionalFormFieldBean formFieldBean = new AdditionalFormFieldBean();
		formFieldBean.setInstId(189);
		formFieldBean.setFormField("formField");
		AdditionalFormFieldDb db = new AdditionalFormFieldDb();
		db.saveAdditionalFormField(formFieldBean);
	}
}
