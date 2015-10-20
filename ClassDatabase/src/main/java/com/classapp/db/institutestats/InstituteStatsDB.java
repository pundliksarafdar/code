package com.classapp.db.institutestats;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.Feedbacks.Feedback;
import com.classapp.db.exam.Exam;
import com.classapp.persistence.HibernateUtil;

public class InstituteStatsDB {
	public void save(InstituteStats stats) {
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.save(stats);
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
	
	public boolean updateStudentIdLimit(int inst_id,int noOfIds) {
		InstituteStats stats=new InstituteStats();
		stats.setInst_id(inst_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		stats=(InstituteStats) session.get(InstituteStats.class,inst_id);
		stats.setAlloc_ids(stats.getAlloc_ids()+noOfIds);
		session.saveOrUpdate(stats);
		transaction.commit();
		session.close();
		return  true;
	}
	
	public boolean updateMemoryLimit(int inst_id,double memory) {
		InstituteStats stats=new InstituteStats();
		stats.setInst_id(inst_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		stats=(InstituteStats) session.get(InstituteStats.class,inst_id);
		stats.setAlloc_memory(stats.getAlloc_memory()+memory);
		session.saveOrUpdate(stats);
		transaction.commit();
		session.close();
		return  true;
	}
	
	public boolean increaseUsedStudentIds(int inst_id) {
		InstituteStats stats=new InstituteStats();
		stats.setInst_id(inst_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		stats=(InstituteStats) session.get(InstituteStats.class,inst_id);
		stats.setUsed_ids(stats.getUsed_ids()+1);
		stats.setAvail_ids(stats.getAvail_ids()-1);
		session.saveOrUpdate(stats);
		transaction.commit();
		session.close();
		return  true;
	}
	
	public boolean decreaseUsedStudentIds(int inst_id) {
		InstituteStats stats=new InstituteStats();
		stats.setInst_id(inst_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		stats=(InstituteStats) session.get(InstituteStats.class,inst_id);
		stats.setUsed_ids(stats.getUsed_ids()-1);
		stats.setAvail_ids(stats.getAvail_ids()+1);
		session.saveOrUpdate(stats);
		transaction.commit();
		session.close();
		return  true;
	}
	
	public boolean increaseUsedMemory(int inst_id,double memory) {
		InstituteStats stats=new InstituteStats();
		stats.setInst_id(inst_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		stats=(InstituteStats) session.get(InstituteStats.class,inst_id);
		stats.setUsed_memory(stats.getUsed_memory()+memory);
		stats.setAvail_memory(stats.getAvail_memory()-memory);
		session.saveOrUpdate(stats);
		transaction.commit();
		session.close();
		return  true;
	}
	
	public boolean decreaseUsedMemory(int inst_id,double memory) {
		InstituteStats stats=new InstituteStats();
		stats.setInst_id(inst_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		stats=(InstituteStats) session.get(InstituteStats.class,inst_id);
		stats.setUsed_memory(stats.getUsed_memory()-memory);
		stats.setAvail_memory(stats.getAvail_memory()+memory);
		session.saveOrUpdate(stats);
		transaction.commit();
		session.close();
		return  true;
	}
	
	public InstituteStats getStats(int inst_id) {
		InstituteStats stats=new InstituteStats();
		stats.setInst_id(inst_id);
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		stats=(InstituteStats) session.get(InstituteStats.class,inst_id);
		transaction.commit();
		session.close();
		return  stats;
	}
	
	
}
