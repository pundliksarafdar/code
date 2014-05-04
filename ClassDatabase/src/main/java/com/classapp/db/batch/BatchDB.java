package com.classapp.db.batch;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.Constants;
import com.classapp.persistence.HibernateUtil;
import com.datalayer.batch.BatchDataClass;
import com.datalayer.batch.Timmings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
	
	public List<BatchDataClass> getBatchData(Integer regId){
		String status = "";
		Session session = null;
		Transaction transaction = null;
		List<Batch> batchList = null;
		List<BatchDataClass> batchDataClasses = new ArrayList<BatchDataClass>();
		BatchDataClass batchDataClass = new BatchDataClass();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Batch where regId =:regId");
			query.setParameter("regId", regId);
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
				batchDataClass.setBatchName(batchList.get(i).getBatch());
				batchDataClass.setCandidatesInBatch(10);
				batchDataClasses.add(batchDataClass);
			}
		}
		return batchDataClasses;
	}

	public JsonArray getBatch(Integer regId){
		String status = "";
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
			batch = ((Batch)batchList.get(0)).getBatch();
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
	
}
