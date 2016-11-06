package com.classapp.db.fees;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.exam.Exam;
import com.classapp.db.exam.Exam_Paper;
import com.classapp.db.pattern.QuestionPaperPattern;
import com.classapp.db.student.Student;
import com.classapp.persistence.HibernateUtil;

public class FeesDB {
	public int saveFees(Fees fees) {
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(fees);
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
		return fees.getFees_id();
	}
	
public void saveFeesStructure(FeesStructure feesStructure) {
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(feesStructure);
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

	public boolean verifyFee(Fees fees) {
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(Fees.class);
		Criterion criterion = Restrictions.eq("inst_id", fees.getInst_id());
		criteria.add(criterion);
		criterion = Restrictions.eq("fees_desc", fees.getFees_desc());
		criteria.add(criterion);
		List<Fees> feesList = criteria.list();
		if(feesList != null){
			if(feesList.size()>0){
				return true;
			}
		}
		if(session!=null){
			session.close();
		}
		return  false;
	}
	
	public Fees getFees(int inst_id,int fees_id) {
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(Fees.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("fees_id", fees_id);
		criteria.add(criterion);
		List<Fees> feesList = criteria.list();
		if(feesList != null){
			if(feesList.size()>0){
				return feesList.get(0);
			}
		}
		if(session!=null){
			session.close();
		}
		return  null;
	}
	
	public List<FeesStructure> getFeesStructureList(int inst_id,int fees_id) {
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(FeesStructure.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("fees_id", fees_id);
		criteria.add(criterion);
		List<FeesStructure> feesStructureList = criteria.list();
		if(session!=null){
			session.close();
		}
		return  feesStructureList;
	}
	
	public List<Fees> getFeesList(int inst_id) {
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(Fees.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		List<Fees> feesList = criteria.list();
		if(session!=null){
			session.close();
		}
		return  feesList;
	}
	
	public boolean verifyUpdateFee(Fees fees) {
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(Fees.class);
		Criterion criterion = Restrictions.eq("inst_id", fees.getInst_id());
		criteria.add(criterion);
		criterion = Restrictions.ne("fees_id", fees.getFees_id());
		criteria.add(criterion);
		criterion = Restrictions.eq("fees_desc", fees.getFees_desc());
		criteria.add(criterion);
		List<Fees> feesList = criteria.list();
		if(feesList != null){
			if(feesList.size()>0){
				return true;
			}
		}
		if(session!=null){
			session.close();
		}
		return  false;
	}
	
	public boolean updateFees(Fees fees) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			Query query = session.createQuery("update Fees set fees_desc = :fees_desc where inst_id = :inst_id and fees_id = :fees_id ");
			query.setParameter("inst_id", fees.getInst_id());
			query.setParameter("fees_id",fees.getFees_id());
			query.setParameter("fees_desc", fees.getFees_desc());
			query.executeUpdate();
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
		return  true;

	}
	
	public boolean updateFeesStructure(FeesStructure feesStructure) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			Query query = session.createQuery("update FeesStructure set fees_structure_desc = :fees_structure_desc where inst_id = :inst_id and fees_id = :fees_id" +
					" and fees_structure_id = :fees_structure_id ");
			query.setParameter("inst_id", feesStructure.getInst_id());
			query.setParameter("fees_id",feesStructure.getFees_id());
			query.setParameter("fees_structure_id", feesStructure.getFees_structure_id());
			query.setParameter("fees_structure_desc", feesStructure.getFees_structure_desc());
			query.executeUpdate();
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
		return  true;

	}
	
	public boolean deleteFeesStructure(FeesStructure feesStructure) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			Query query = session.createQuery("delete FeesStructure where inst_id = :inst_id and fees_id = :fees_id" +
					"and fees_structure_id = :fees_structure_id ");
			query.setParameter("inst_id", feesStructure.getInst_id());
			query.setParameter("fees_id",feesStructure.getFees_id());
			query.setParameter("fees_structure_id", feesStructure.getFees_structure_id());
			query.executeUpdate();
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
		return  true;

	}
	
	public boolean deleteFees(int inst_id,int fees_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			Query query = session.createQuery("delete Fees where inst_id = :inst_id and fees_id = :fees_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("fees_id", fees_id);
			query.executeUpdate();
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
		return  true;

	}
	
	public boolean deleteFeesStructure(int inst_id,int fees_id) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			Query query = session.createQuery("delete FeesStructure where inst_id = :inst_id and fees_id = :fees_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("fees_id", fees_id);
			query.executeUpdate();
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
		return  true;

	}
	
	public int saveBatchFees(BatchFees batchFees) {
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(batchFees);
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
		return batchFees.getBatch_fees_id();
	}
	
	public boolean saveBatchFeesDistribution(BatchFeesDistribution batchFeesDistribution) {
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(batchFeesDistribution);
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
		return true;
	}
	
	public List<BatchFeesDistribution> getBatchFeesDistribution(int inst_id,int batch_fees_id) {
		
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(BatchFeesDistribution.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("batch_fees_id",batch_fees_id);
		criteria.add(criterion);
		List<BatchFeesDistribution> feesList = criteria.list();
		if(session!=null){
			session.close();
		}
		return  feesList;
	}
	
	public BatchFees getBatchFees(int inst_id,int div_id,int batch_id) {
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(BatchFees.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("div_id",div_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("batch_id", batch_id);
		criteria.add(criterion);
		List<BatchFees> feesList = criteria.list();
		if(feesList != null){
			if(feesList.size()>0){
				return feesList.get(0);
			}
		}
		if(session!=null){
			session.close();
		}
		return  null;
	}
	
	public boolean updateBatchFees(BatchFees batchFees) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			Query query = session.createQuery("update BatchFees set batch_fees = :batch_fees, fees_id = :fees_id" +
					" where inst_id = :inst_id and batch_fees_id = :batch_fees_id");
			query.setParameter("inst_id", batchFees.getInst_id());
			query.setParameter("batch_fees_id", batchFees.getBatch_fees_id());
			query.setParameter("batch_fees", batchFees.getBatch_fees());
			query.setParameter("fees_id", batchFees.getFees_id());
			query.executeUpdate();
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
		return  true;

	}
	
	public boolean updateBatchFeesDistribution(BatchFeesDistribution batchFees) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		try{
			Query query = session.createQuery("update BatchFeesDistribution set fees_amount = :fees_amount" +
					" where inst_id = :inst_id and batch_fees_id = :batch_fees_id and fees_id = :fees_id and fees_structure_id = :fees_structure_id");
			query.setParameter("inst_id", batchFees.getInst_id());
			query.setParameter("batch_fees_id", batchFees.getBatch_fees_id());
			query.setParameter("fees_amount", batchFees.getFees_amount());
			query.setParameter("fees_id", batchFees.getFees_id());
			query.setParameter("fees_structure_id", batchFees.getFees_structure_id());
			query.executeUpdate();
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
		return  true;

	}
	
public List<BatchFees> getBatchFeesList(int inst_id,int div_id,List<Integer> batch_id) {
		
		if(null == batch_id || batch_id.isEmpty()){
			return new ArrayList<BatchFees>();
		}
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		Criteria criteria = session.createCriteria(BatchFees.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.eq("div_id",div_id);
		criteria.add(criterion);
		criterion = Restrictions.in("batch_id", batch_id);
		criteria.add(criterion);
		List<BatchFees> feesList = criteria.list();
		if(session!=null){
			session.close();
		}
		return  feesList;
	}

public boolean deleteBatchFees(int inst_id,int div_id,int batch_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("Delete From BatchFees where inst_id = :inst_id and div_id = :div_id and batch_id=:batch_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		query.executeUpdate();
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
	return  true;

}

public boolean deleteBatchFeesDistribution(int inst_id,int batch_fees_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("Delete From BatchFeesDistribution where inst_id = :inst_id and batch_fees_id = :batch_fees_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("batch_fees_id", batch_fees_id);
		query.executeUpdate();
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
	return  true;

}

public boolean saveStudentFees(Student_Fees student_Fees) {
	
	Session session = null;
	Transaction transaction = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		session.saveOrUpdate(student_Fees);
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
	return true;
}

public boolean saveStudentFeesTransaction(Student_Fees_Transaction fees_Transaction) {
	
	Session session = null;
	Transaction transaction = null;
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		session.saveOrUpdate(fees_Transaction);
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
	return true;
}

public List<Student_Fees_Transaction> getStudentFeesTransaction(int inst_id,int student_id) {
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	Criteria criteria = session.createCriteria(Student_Fees_Transaction.class);
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("student_id",student_id);
	criteria.add(criterion);
	List<Student_Fees_Transaction> feesList = criteria.list();
	if(session!=null){
		session.close();
	}
	return  feesList;
}

public List<Student_Fees> getStudentFees(int inst_id,int student_id) {
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	Criteria criteria = session.createCriteria(Student_Fees.class);
	Criterion criterion = Restrictions.eq("inst_id", inst_id);
	criteria.add(criterion);
	criterion = Restrictions.eq("student_id",student_id);
	criteria.add(criterion);
	List<Student_Fees> feesList = criteria.list();
	if(session!=null){
		session.close();
	}
	return  feesList;
}

public List getAllBatchStudentsFees(int inst_id,int div_id,int batch_id) {
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	List BatchFessList = null;
	try{
		
		Query query = session.createQuery("Select fees.student_id ,std.fname,std.lname,fees.batch_fees,fees.discount,fees.discount_type,"
				+ "fees.final_fees_amt,fees.fees_paid,fees.fees_due  From Student_Fees fees ,Student std where "
				+ "fees.inst_id = :inst_id and fees.div_id = :div_id and fees.batch_id=:batch_id " +
				"and fees.student_id = std.student_id and std.class_id = fees.inst_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		BatchFessList = query.list();
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(null!=session){
			session.close();
		}
	}
	return  BatchFessList;
}

public List getBatchStudentsFees(int inst_id,int div_id,int batch_id) {
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	List BatchFessList = null;
	try{
		
		Query query = session.createQuery("Select fees.student_id ,reg.fname,reg.lname,fees.batch_fees,fees.discount,fees.discount_type,fees.final_fees_amt,fees.fees_paid,fees.fees_due  From Student_Fees fees ,RegisterBean reg where fees.inst_id = :inst_id and fees.div_id = :div_id and fees.batch_id=:batch_id " +
				"and fees.student_id = reg.regId");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		BatchFessList = query.list();
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(null!=session){
			session.close();
		}
	}
	return  BatchFessList;
}


public Student_Fees getStudentBatchFees(int inst_id,int div_id,int batch_id,int student_id) {
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	List<Student_Fees> BatchFessList = null;
	try{
		
		Query query = session.createQuery(" From Student_Fees where inst_id = :inst_id and div_id = :div_id and batch_id=:batch_id " +
				"and student_id = :student_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		query.setParameter("student_id", student_id);
		BatchFessList = query.list();
		if(BatchFessList != null){
			if(BatchFessList.size()>0){
			return BatchFessList.get(0);
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(null!=session){
			session.close();
		}
	}
	return  null;
}

public boolean updateStudentFees(int inst_id,int div_id,int batch_id, int student_id,double fees_paid,int batchFee,String amtType,float discount) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		
		Student_Fees student_Fees_key = new Student_Fees();
		student_Fees_key.setBatch_id(batch_id);
		student_Fees_key.setInst_id(inst_id);
		student_Fees_key.setDiv_id(div_id);
		student_Fees_key.setStudent_id(student_id);
		
		Student_Fees student_Fees = new Student_Fees();
		student_Fees = (Student_Fees) session.get(Student_Fees.class, student_Fees_key);
		
		student_Fees.setBatch_fees(batchFee);
		student_Fees.setBatch_id(batch_id);
		student_Fees.setDiscount(discount);
		student_Fees.setDiscount_type(amtType);
		student_Fees.setDiv_id(div_id);
		student_Fees.setFees_paid(student_Fees.getFees_paid()+fees_paid);
		if("amt".equalsIgnoreCase(amtType)){
		student_Fees.setFinal_fees_amt(batchFee - discount);
		student_Fees.setFees_due((batchFee - discount) - (student_Fees.getFees_paid()));
		}else if("per".equalsIgnoreCase(amtType)){
			student_Fees.setFinal_fees_amt(batchFee - ((discount/100)*batchFee));	
			student_Fees.setFees_due((batchFee - ((discount/100)*batchFee)) - (student_Fees.getFees_paid()));
		}
		student_Fees.setInst_id(inst_id);
		student_Fees.setStudent_id(student_id);
		
		/*
		Query query = session.createQuery("update Student_Fees set fees_paid = fees_paid + :fees_paid, fees_due = fees_due - :fees_paid" +
				" where inst_id = :inst_id and div_id=:div_id and batch_id = :batch_id and student_id = :student_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		query.setParameter("student_id", student_id);
		query.setParameter("fees_paid", fees_paid);
		query.executeUpdate();
		*/
		session.saveOrUpdate(student_Fees);
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
	return  true;

}

public boolean updateStudentFeesAmt(int inst_id,int div_id,int batch_id, int student_id,double discount,String discount_type) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("update Student_Fees set discount = :discount,discount_type = :discount_type,final_fees_amt = batch_fees - :discount" +
				", fees_due = (batch_fees - :discount) - fees_paid" +
				" where inst_id = :inst_id and div_id=:div_id and batch_id = :batch_id and student_id = :student_id");
		if("per".equals(discount_type)){
			query = session.createQuery("update Student_Fees set discount = :discount,discount_type = :discount_type,final_fees_amt = batch_fees * (:discount/100)" +
					", fees_due = (batch_fees * (:discount/100)) - fees_paid" +
					" where inst_id = :inst_id and div_id=:div_id and batch_id = :batch_id and student_id = :student_id");

		}
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		query.setParameter("student_id", student_id);
		query.setParameter("discount", discount);
		query.setParameter("discount_type", discount_type);
		query.executeUpdate();
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
	return  true;

}

public List getStudentsTransactionForPrint(int inst_id,int div_id,int batch_id,int student_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	List BatchFessList = null;
	try{
		Query query = session.createQuery("Select fees.student_id ,reg.fname,reg.lname,fees.batch_fees," +
				"fees.discount,fees.discount_type,fees.final_fees_amt,fees.fees_paid,fees.fees_due, " +
				" trans.amt_paid, trans.transaction_dt From Student_Fees fees ,RegisterBean reg,Student_Fees_Transaction trans where fees.inst_id = :inst_id " +
				"and fees.div_id = :div_id and fees.batch_id=:batch_id " +
				"and fees.student_id = reg.regId and fees.student_id = trans.student_id and fees.student_id = :student_id " +
				"and trans.fees_transaction_id = (select max(fees_transaction_id) from Student_Fees_Transaction where inst_id = :inst_id " +
				"and student_id = :student_id) ");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		query.setParameter("student_id", student_id);
		BatchFessList = query.list();
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
	return  BatchFessList;
}

public boolean deleteBatchFeesRelatedToClass(int inst_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query2 = session.createQuery("Delete From BatchFeesDistribution where inst_id = :inst_id "
											+ "and batch_fees_id in (select batch_fees_id from BatchFees "
											+ "where inst_id = :inst_id and div_id = :div_id)");
		query2.setParameter("inst_id", inst_id);
		query2.setParameter("div_id", div_id);
		query2.executeUpdate();
		Query query1 = session.createQuery("Delete From BatchFees where inst_id = :inst_id "
											+ "and div_id = :div_id");
		query1.setParameter("inst_id", inst_id);
		query1.setParameter("div_id", div_id);
		query1.executeUpdate();
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
	return  true;

}

public boolean updateStudentFeesRelatedToClass(int inst_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("update Student_Fees set div_id = 0,batch_id = 0" +
				" where inst_id = :inst_id and div_id=:div_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.executeUpdate();
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
	return  true;

}

public boolean updateStudentFeesTransactionRelatedToClass(int inst_id,int div_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("update Student_Fees_Transaction set div_id = 0,batch_id = 0" +
				" where inst_id = :inst_id and div_id=:div_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.executeUpdate();
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
	return  true;

}

public boolean deleteBatchFeesRelatedToBatch(int inst_id,int div_id,int batch_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query2 = session.createQuery("Delete From BatchFeesDistribution where inst_id = :inst_id "
											+ "and batch_fees_id in (select batch_fees_id from BatchFees "
											+ "where inst_id = :inst_id and div_id = :div_id  and batch_id=:batch_id)");
		query2.setParameter("inst_id", inst_id);
		query2.setParameter("div_id", div_id);
		query2.setParameter("batch_id", batch_id);
		query2.executeUpdate();
		Query query1 = session.createQuery("Delete From BatchFees where inst_id = :inst_id "
											+ "and div_id = :div_id and batch_id=:batch_id");
		query1.setParameter("inst_id", inst_id);
		query1.setParameter("div_id", div_id);
		query1.setParameter("batch_id", batch_id);
		query1.executeUpdate();
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
	return  true;

}

public boolean updateStudentFeesRelatedToBatch(int inst_id,int div_id,int batch_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("update Student_Fees set div_id = 0,batch_id = 0" +
				" where inst_id = :inst_id and div_id=:div_id and batch_id= :batch_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		query.executeUpdate();
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
	return  true;

}

public boolean updateStudentFeesTransactionRelatedToBatch(int inst_id,int div_id,int batch_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("update Student_Fees_Transaction set div_id = 0,batch_id = 0" +
				" where inst_id = :inst_id and div_id=:div_id and batch_id=:batch_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		query.executeUpdate();
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
	return  true;

}
	
public boolean deleteStudentFeesRelatedToStudent(int inst_id,int student_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("delete from  Student_Fees" +
				" where inst_id = :inst_id and student_id = :student_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("student_id", student_id);
		query.executeUpdate();
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
	return  true;

}

public boolean deleteStudentFeesTransactionRelatedToStudent(int inst_id,int student_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("delete from Student_Fees_Transaction " +
				" where inst_id = :inst_id and student_id = :student_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("student_id", student_id);
		query.executeUpdate();
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
	return  true;

}

public boolean updateStudentFeesRelatedToBatch(int inst_id,int div_id,int batch_id,double fees_amount) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("update Student_Fees set batch_fees = :fees_amount,final_fees_amt = (case"
				+ " when discount_type = 'amt' then (:fees_amount - discount) "
				+ " when discount_type = 'Amt' then (:fees_amount - discount) "
				+ " when discount_type = 'per' then (:fees_amount  - (:fees_amount * discount/100)) end) ,"
				+ " fees_due = (case " 
				+ " when discount_type = 'amt' then ((:fees_amount - discount) - fees_paid) "
				+ " when discount_type = 'Amt' then ((:fees_amount - discount) - fees_paid) "
				+ " when discount_type = 'per' then ((:fees_amount  - (:fees_amount * discount/100)) - fees_paid) end) "
				+ " where inst_id = :inst_id and div_id=:div_id and batch_id= :batch_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		query.setParameter("fees_amount", fees_amount);
		query.executeUpdate();
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
	return  true;

}

public boolean deleteBatchFees(int inst_id,int fees_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("delete BatchFees where inst_id = :inst_id and fees_id = :fees_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("fees_id", fees_id);
		query.executeUpdate();
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
	return  true;

}

public boolean deleteBatchFeesStructure(int inst_id,int fees_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	try{
		Query query = session.createQuery("delete BatchFeesDistribution where inst_id = :inst_id and fees_id = :fees_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("fees_id", fees_id);
		query.executeUpdate();
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
	return  true;

}

public List<Integer> getStudentIdsFromStudentFees(int inst_id,int div_id,int batch_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	List<Integer> studentIDList = null;
	try{
		Query query = session.createQuery("select student_id from Student_Fees" +
				" where inst_id = :inst_id and div_id = :div_id and batch_id = :batch_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		studentIDList = query.list();
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
	return  studentIDList;

}

public List getStudentDueFeesForNotification(int inst_id,int div_id,int batch_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	List studentIDList = null;
	try{
		Query query = session.createQuery("select std.student_id,reg.fname,reg.lname,reg.phone1,reg.email"
										+ ",std.parentFname,std.parentLname,std.parentPhone,std.parentEmail,sf.final_fees_amt,sf.fees_paid,sf.fees_due  "
										+ "from Student_Fees sf,Student std,RegisterBean reg" 
										+" where sf.inst_id = :inst_id and sf.div_id = :div_id and sf.batch_id = :batch_id and sf.student_id = std.student_id and "
										+ "sf.div_id = std.div_id and reg.regId = sf.student_id and sf.fees_due>0");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		studentIDList = query.list();
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
	return  studentIDList;

}

public List getStudentDueFeesForNotification(int inst_id) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	List studentIDList = null;
	try{
		Query query = session.createQuery("select std.student_id,reg.fname,reg.lname,reg.phone1,reg.email"
										+ ",std.parentFname,std.parentLname,std.parentPhone,std.parentEmail,sf.final_fees_amt,"
										+ "sf.fees_paid,sf.fees_due,batch.batch_name  "
										+ "from Student_Fees sf,Student std,RegisterBean reg,Batch batch" 
										+" where sf.inst_id = :inst_id and sf.student_id = std.student_id and "
										+ "sf.div_id = std.div_id and reg.regId = sf.student_id and sf.fees_due>0 and sf.batch_id = batch.batch_id "
										+ "and sf.div_id = batch.div_id and sf.inst_id = batch.class_id");
		query.setParameter("inst_id", inst_id);
		studentIDList = query.list();
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
	return  studentIDList;

}

public List getStudentForFeesPaymentNotification(int inst_id,List<Integer> studentIDs) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	List studentIDList = null;
	try{
		Query query = session.createQuery("select std.student_id,reg.fname,reg.lname,reg.phone1,reg.email"
										+ ",std.parentFname,std.parentLname,std.parentPhone,std.parentEmail,sf.final_fees_amt,"
										+ "sf.fees_paid,sf.fees_due,batch.batch_name  "
										+ "from Student_Fees sf,Student std,RegisterBean reg,Batch batch" 
										+" where sf.inst_id = :inst_id and sf.student_id = std.student_id and "
										+ "sf.div_id = std.div_id and reg.regId = sf.student_id and sf.batch_id = batch.batch_id "
										+ "and sf.div_id = batch.div_id and sf.inst_id = batch.class_id and sf.student_id in :list");
		query.setParameter("inst_id", inst_id);
		query.setParameterList("list", studentIDs);
		studentIDList = query.list();
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
	return  studentIDList;

}

public List getStudentDueFeesForNotification(int inst_id,int div_id,int batch_id,List<Integer> studentIdList) {
	Transaction transaction=null;
	Session session=null;
	session=HibernateUtil.getSessionfactory().openSession();
	transaction=session.beginTransaction();
	List studentFeesData = null;
	try{
		Query query = session.createQuery("select std.student_id,reg.fname,reg.lname,reg.phone1,reg.email"
										+ ",std.parentFname,std.parentLname,std.parentPhone,std.parentEmail,sf.final_fees_amt,sf.fees_paid,sf.fees_due  "
										+ "from Student_Fees sf,Student std,RegisterBean reg" 
										+" where sf.inst_id = :inst_id and sf.div_id = :div_id and sf.batch_id = :batch_id and sf.student_id = std.student_id and "
										+ "sf.div_id = std.div_id and reg.regId = sf.student_id and sf.fees_due>0 and sf.student_id in :list");
		query.setParameter("inst_id", inst_id);
		query.setParameter("div_id", div_id);
		query.setParameter("batch_id", batch_id);
		query.setParameterList("list", studentIdList);
		studentFeesData = query.list();
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
	return  studentFeesData;

}

}
