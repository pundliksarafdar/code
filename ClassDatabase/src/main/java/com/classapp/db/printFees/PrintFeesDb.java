package com.classapp.db.printFees;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.fees.Fees;
import com.classapp.persistence.HibernateUtil;

public class PrintFeesDb {
	
	public void savePrintFeesMeta(PrintFees printFees){
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(printFees);
		transaction.commit();
		if(session != null){session.close();}
	}
	
	public PrintFees getLastFeesDetail(int studentId,int instId){
		PrintFees printFees = new PrintFees();
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(PrintFees.class);
		Criterion criterion = Restrictions.eq("inst_id", instId);
		criteria.add(criterion);
		criterion = Restrictions.eq("student_id", studentId);
		criteria.add(criterion);
		List<PrintFees> feesList = criteria.list();
		if(!feesList.isEmpty()){
			return feesList.get(feesList.size()-1);
		}
		if(session != null){session.close();}
		return printFees;
	}
	
	public List<PrintFees> getFeesDetails(int studentId,int instId){
		PrintFees printFees = new PrintFees();
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(PrintFees.class);
		Criterion criterion = Restrictions.eq("inst_id", instId);
		criteria.add(criterion);
		criterion = Restrictions.eq("student_id", studentId);
		criteria.add(criterion);
		List<PrintFees> feesList = criteria.list();
		if(session != null){session.close();}
		return feesList;
	}
}
