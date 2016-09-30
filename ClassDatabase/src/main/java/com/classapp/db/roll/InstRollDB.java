package com.classapp.db.roll;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

public class InstRollDB {
	public boolean saveInstRoll(Inst_roll inst_roll) {
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		session.saveOrUpdate(inst_roll);
		transaction.commit();
		if(session!=null){
			session.close();
		}
		return  true;

	}
	
	public boolean validateRoll(Inst_roll inst_roll) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("from Inst_roll where  inst_id = :inst_id and roll_desc=:roll_desc");
		query.setParameter("inst_id", inst_roll.getInst_id());
		query.setParameter("roll_desc", inst_roll.getRoll_desc());
		List list = query.list();
		if(list.size() > 0){
			return false;
		}
		}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  true;

	}
	
	public List<Inst_roll> getInstituteRoles(int inst_id) {
		Transaction transaction=null;
		Session session=null;
		List list = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("from Inst_roll where  inst_id = :inst_id");
		query.setParameter("inst_id", inst_id);
		 list = query.list();
		}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  list;

	}
	
	public Inst_roll getRole(int inst_id,int roll_id) {
		Transaction transaction=null;
		Session session=null;
		List<Inst_roll> list = null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("from Inst_roll where  inst_id = :inst_id and roll_id = :roll_id");
		query.setParameter("inst_id", inst_id);
		query.setParameter("roll_id", roll_id);
		 list = query.list();
		 if(list.size() > 0){
			 return list.get(0);
		 }
		}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  null;

	}
	
	public boolean updateRoll(Inst_roll inst_roll) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("update Inst_roll set  roll_desc=:roll_desc,parent_mod_access =:parent_mod_access,"
				+ "child_mod_access = :child_mod_access, teacher = :teacher  where  inst_id = :inst_id and roll_id = :roll_id");
		query.setParameter("inst_id", inst_roll.getInst_id());
		query.setParameter("roll_desc", inst_roll.getRoll_desc());
		query.setParameter("parent_mod_access", inst_roll.getParent_mod_access());
		query.setParameter("child_mod_access", inst_roll.getChild_mod_access());
		query.setParameter("teacher", inst_roll.isTeacher());
		query.setParameter("roll_id", inst_roll.getRoll_id());
		query.executeUpdate();
		}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  true;

	}
	
	public boolean validateUpdateRoll(Inst_roll inst_roll) {
		Transaction transaction=null;
		Session session=null;
		try{
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("from Inst_roll where  inst_id = :inst_id and roll_desc=:roll_desc and roll_id != :roll_id");
		query.setParameter("inst_id", inst_roll.getInst_id());
		query.setParameter("roll_desc", inst_roll.getRoll_desc());
		query.setParameter("roll_id", inst_roll.getRoll_id());
		List list = query.list();
		if(list.size() > 0){
			return false;
		}
		}catch(Exception e){
		e.printStackTrace();
		if(null!=transaction){
			transaction.rollback();
		}
		
		}finally{
		if(session!=null){
			session.close();
		}
		}
		return  true;

	}
}
