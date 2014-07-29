package com.classapp.db.batch;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;
import com.datalayer.batch.BatchDataClass;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class BatchDB {
	public String updateDb(Batch batch){
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(batch);
			transaction.commit();
		}catch(Exception e){
			status = "1";
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return status;
	}
	
	public List<BatchDataClass> getBatchData(int batch_id){
		Session session = null;
		Transaction transaction = null;
		List<Batch> batchList = null;
		List<BatchDataClass> batchDataClasses = new ArrayList<BatchDataClass>();
		BatchDataClass batchDataClass = new BatchDataClass();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Batch where batch_id =:batch_id");
			query.setParameter("batch_id", batch_id);
			batchList = query.list();
			
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
		if(null!=batchList){
			for(int i=0;i<batchList.size();i++){
				batchDataClass = new BatchDataClass();
				batchDataClass.setBatchName(batchList.get(i).getBatch_name());
				batchDataClass.setBatchId(batchList.get(i).getBatch_id());
				batchDataClass.setCandidatesInBatch(10);
				batchDataClasses.add(batchDataClass);
			}
		}
		return batchDataClasses;
	}

	public JsonArray getBatch(Integer regId){
		Session session = null;
		Transaction transaction = null;
		List<Batch> batchList = null;
		String batch = null;
		JsonArray jsonArray = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Batch where regId =:regId");
			query.setParameter("regId", regId);
			batchList = query.list();
			batch = ((Batch)batchList.get(0)).getBatch_name();
			JsonParser jsonParser = new JsonParser();
			jsonArray = jsonParser.parse(batch).getAsJsonArray();
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			jsonArray = null;
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return jsonArray;
	}

	public String deleteBatch(Batch batch){
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.delete(batch);
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			status = "1";
			
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return status;
	}
	
	public List getAllBatches(int regID) {
		
		Session session = null;
		Transaction transaction = null;
		List batchList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Batch where class_id =:regId");
			query.setParameter("regId", regID);
			batchList = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return batchList;
	}
	
public List getBatchSubjects(String batchID) {
		
		Session session = null;
		Transaction transaction = null;
		List batchList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select sub_id from Batch where batch_id =:batchID");
			query.setParameter("batchID", Integer.parseInt(batchID));
			batchList = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
			if(null!=transaction){
				transaction.rollback();
			}
			
		}
		return batchList;
	}
	
}
