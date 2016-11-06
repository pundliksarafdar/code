package com.classapp.db.certificate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.classapp.db.question.Questionbank;
import com.classapp.persistence.HibernateUtil;

public class CertificateDB {
public int saveCertificate(certificate certificate) {
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(certificate);
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
		return certificate.getCert_id();
	}

	public boolean validateCertificateName(String cert_desc,int inst_id) {
		Session session = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery("select cert_id from certificate where"
					+ " cert_desc = :cert_desc and inst_id = :inst_id ");
			query.setParameter("inst_id", inst_id);
			query.setParameter("cert_desc",cert_desc);
			List list = query.list();
			if(list !=null){
				if(list.size() > 0){
					return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return true;
	}
	
	public List<certificate> getCertificateList(int inst_id) {
		Session session = null;
		List<certificate> certificateList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Criteria criteria = session.createCriteria(certificate.class);
			Criterion criterion = Restrictions.eq("inst_id", inst_id);
			criteria.add(criterion);
			certificateList = criteria.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return certificateList;
	}
	
	public boolean validateUpdateCertificateName(String cert_desc,int inst_id,int cert_id) {
		Session session = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery("select cert_id from certificate where"
					+ " cert_desc = :cert_desc and inst_id = :inst_id and cert_id != :cert_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("cert_desc",cert_desc);
			query.setParameter("cert_id",cert_id);
			List list = query.list();
			if(list !=null){
				if(list.size() > 0){
					return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return true;
	}
	
	public boolean updateCertificate(certificate certificate) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("update certificate set header_id = :header_id,"
					+ "cert_desc = :cert_desc where inst_id = :inst_id and cert_id = :cert_id");
			query.setParameter("inst_id", certificate.getInst_id());
			query.setParameter("cert_desc",certificate.getCert_desc());
			query.setParameter("cert_id",certificate.getCert_id());
			query.setParameter("header_id",certificate.getHeader_id());
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
		return true;
	}
	
	public boolean deleteCertificate(int inst_id,int cert_id) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from certificate  where inst_id = :inst_id and cert_id = :cert_id");
			query.setParameter("inst_id", inst_id);
			query.setParameter("cert_id",cert_id);
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
		return true;
	}
}
