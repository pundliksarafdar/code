package com.classapp.db.fees;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.exam.Exam_Paper;
import com.classapp.db.pattern.QuestionPaperPattern;
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
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
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
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
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
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
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
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
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
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
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
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update Fees set fees_desc = :fees_desc where inst_id = :inst_id and fees_id = :fees_id ");
			query.setParameter("inst_id", fees.getInst_id());
			query.setParameter("fees_id",fees.getFees_id());
			query.setParameter("fees_desc", fees.getFees_desc());
			query.executeUpdate();
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
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update FeesStructure set fees_structure_desc = :fees_structure_desc where inst_id = :inst_id and fees_id = :fees_id" +
					" and fees_structure_id = :fees_structure_id ");
			query.setParameter("inst_id", feesStructure.getInst_id());
			query.setParameter("fees_id",feesStructure.getFees_id());
			query.setParameter("fees_structure_id", feesStructure.getFees_structure_id());
			query.setParameter("fees_structure_desc", feesStructure.getFees_structure_desc());
			query.executeUpdate();
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
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete FeesStructure where inst_id = :inst_id and fees_id = :fees_id" +
					"and fees_structure_id = :fees_structure_id ");
			query.setParameter("inst_id", feesStructure.getInst_id());
			query.setParameter("fees_id",feesStructure.getFees_id());
			query.setParameter("fees_structure_id", feesStructure.getFees_structure_id());
			query.executeUpdate();
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
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete Fees where inst_id = :inst_id and fees_id = :fees_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("fees_id", fees_id);
			query.executeUpdate();
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
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete FeesStructure where inst_id = :inst_id and fees_id = :fees_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("fees_id", fees_id);
			query.executeUpdate();
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
		
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(BatchFeesDistribution.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.ne("batch_fees_id",batch_fees_id);
		criteria.add(criterion);
		List<BatchFeesDistribution> feesList = criteria.list();
		if(session!=null){
			session.close();
		}
		return  feesList;
	}
	
	public BatchFees getBatchFees(int inst_id,int div_id,int batch_id) {
		
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Criteria criteria = session.createCriteria(BatchFeesDistribution.class);
		Criterion criterion = Restrictions.eq("inst_id", inst_id);
		criteria.add(criterion);
		criterion = Restrictions.ne("div_id",div_id);
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
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update BatchFees set batch_fees = :batch_fees, fees_id = :fees_id" +
					" where inst_id = :inst_id and batch_fees_id = :batch_fees_id");
			query.setParameter("inst_id", batchFees.getInst_id());
			query.setParameter("batch_fees_id", batchFees.getBatch_fees_id());
			query.setParameter("batch_fees", batchFees.getBatch_fees());
			query.setParameter("fees_id", batchFees.getFees_id());
			query.executeUpdate();
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
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update BatchFeesDistribution set fees_amount = :fees_amount" +
					" where inst_id = :inst_id and batch_fees_id = :batch_fees_id and fees_id = :fees_id and fees_structure_id = :fees_structure_id");
			query.setParameter("inst_id", batchFees.getInst_id());
			query.setParameter("batch_fees_id", batchFees.getBatch_fees_id());
			query.setParameter("fees_amount", batchFees.getFees_amount());
			query.setParameter("fees_id", batchFees.getFees_id());
			query.setParameter("fees_structure_id", batchFees.getFees_structure_id());
			query.executeUpdate();
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

}
