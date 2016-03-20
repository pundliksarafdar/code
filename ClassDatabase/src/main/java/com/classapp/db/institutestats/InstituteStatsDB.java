package com.classapp.db.institutestats;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
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
		if(stats.getAlloc_ids()>stats.getUsed_ids()){
			stats.setAvail_ids(stats.getAlloc_ids()-stats.getUsed_ids());
		}
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
		if(stats.getAlloc_memory()>stats.getUsed_memory()){
			stats.setAvail_memory(stats.getAlloc_memory()-stats.getUsed_memory());
		}
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
		if(stats.getUsed_ids()<stats.getAlloc_ids()){
		stats.setAvail_ids(stats.getAvail_ids()+1);
		}
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
		if(stats.getAlloc_memory()>stats.getUsed_memory()){
		stats.setAvail_memory(stats.getAlloc_memory()-stats.getUsed_memory());
		}
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
	
	public List<InstituteStats> getAllInstStats() {
		List<InstituteStats> stats=new ArrayList<InstituteStats>();
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		Query query = session.createQuery("select a,b.className from InstituteStats a,RegisterBean b where a.inst_id = b.regId");
		List list = query.list();
		for(Object object:list){
			Object[] objects = (Object[]) object;
			InstituteStats instituteStats = (InstituteStats) objects[0];
			String sClassname = (String) objects[1];
			instituteStats.setClassname(sClassname);
			stats.add(instituteStats);
		}
		transaction.commit();
		session.close();
		return  stats;
	}
	
	public boolean updateLimit(InstituteStats instituteStats) {
		InstituteStats stats=new InstituteStats();
		stats.setInst_id(instituteStats.getInst_id());
		Transaction transaction=null;
		Session session=null;
		session=HibernateUtil.getSessionfactory().openSession();
		transaction=session.beginTransaction();
		stats=(InstituteStats) session.get(InstituteStats.class,instituteStats.getInst_id());
		
		stats.setAlloc_ids(instituteStats.getAlloc_ids());
		stats.setAlloc_memory(instituteStats.getAlloc_memory());
		
		stats.setSmsAccess(instituteStats.isSmsAccess());
		stats.setSmsAlloted(instituteStats.getSmsAlloted());
		
		stats.setEmailAccess(instituteStats.isEmailAccess());
		
		session.saveOrUpdate(stats);
		transaction.commit();
		if(null!=session){
			session.close();
		}
		return  true;
	}
}
