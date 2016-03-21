package com.classapp.db.batch;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.batch.division.Division;
import com.classapp.db.batch.division.DivisionDB;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.SubjectDb;
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
	
	public int getNextBatchID(int inst_id,int div_id){
		Session session = null;
		Transaction transaction = null;
		List<Integer> batchID=null;		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select max(batch_id)+1 from Batch where class_id=:class_id and div_id=:div_id");
			
			query.setInteger("class_id", inst_id);
			query.setInteger("div_id", div_id);
			batchID =  query.list();
			transaction.commit();
			if (batchID!=null) {
				if(batchID.get(0)!=null){
				return batchID.get(0);
				}
			}
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
		
		return 1;
	}
	
	public List<BatchDataClass> getBatchData(int class_id){
		Session session = null;
		Transaction transaction = null;
		List<Batch> batchList = null;
		List<BatchDataClass> batchDataClasses = new ArrayList<BatchDataClass>();
		BatchDataClass batchDataClass = new BatchDataClass();
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Batch where class_id =:class_id");
			query.setInteger("class_id", class_id);
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
	
	public List<BatchDetails> getAllBatchesOfClass(int class_id){
		Session session = null;
		Transaction transaction = null;
		List<Batch> batchList = null;
		List<BatchDetails> batchDetailsList = new ArrayList<BatchDetails>();
		BatchDetails batchDetails = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Batch where class_id =:class_id");
			query.setInteger("class_id", class_id);
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
			SubjectDb subjectDb= new SubjectDb();
			DivisionDB divisionDB= new DivisionDB();
			for(int i=0;i<batchList.size();i++){
				batchDetails = new BatchDetails();
				batchDetails.setBatch(batchList.get(i));
				batchDetails.setDivision(divisionDB.retriveByID(batchList.get(i).getDiv_id()));
				List<Subject> subjectList= new ArrayList<Subject>();
				String subjectIds= batchList.get(i).getSub_id();
				if(!subjectIds.equals("")){
				for (String  subjectId: subjectIds.split(",")) {
					Subject subject= subjectDb.retrive(Integer.parseInt(subjectId));
					if(subject!=null){
						subjectList.add(subject);
					}
				}
				}
				batchDetails.setSubjects(subjectList);
				batchDetailsList.add(batchDetails);
			}
		}
		return batchDetailsList;
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
	
	public List<Batch> retriveAllBatches(int class_id){
		Session session = null;
		Transaction transaction = null;
		List<Batch> batchList = null;
		List<Batch> batches = new ArrayList<Batch>();
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Batch where class_id =:class_id");
			query.setInteger("class_id", class_id);
			
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
				batches.add(batchList.get(i));
			}
		}
		return batches;
	}

	public boolean isBatchExist(int class_id,String batch_name,int div_id){
		Session session = null;
		Transaction transaction = null;
		List<Batch> batchList = null;
				
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Batch where class_id =:class_id and batch_name =:batch_name and div_id=:div_id");
			query.setInteger("class_id", class_id);
			query.setString("batch_name", batch_name);
			query.setParameter("div_id", div_id);
			batchList = query.list();
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
		if(null!=batchList && batchList.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean isUpdatedBatchExist(int batch_id,int class_id,String batch_name,int div_id){
		Session session = null;
		Transaction transaction = null;
		List<Batch> batchList = null;
				
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Batch where class_id =:class_id and batch_name =:batch_name and div_id=:div_id and batch_id != :batch_id");
			query.setInteger("class_id", class_id);
			query.setString("batch_name", batch_name);
			query.setParameter("div_id", div_id);
			query.setParameter("batch_id", batch_id);
			batchList = query.list();
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
		if(null!=batchList && batchList.size()>0){
			return true;
		}
		return false;
	}
	
	public Batch getBatchFromID(int batchId,int inst_id,int div_id){
		Session session = null;
		Transaction transaction = null;
		Batch batch=null;		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Batch where batch_id =:batch_id and class_id=:class_id and div_id=:div_id");
			query.setInteger("batch_id", batchId);
			query.setInteger("class_id", inst_id);
			query.setInteger("div_id", div_id);
			batch = (Batch) query.uniqueResult();
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
		
		return batch;
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
			
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return batchList;
	}
	
public List getBatchSubjects(String batchID,int inst_id,int div_id) {
		
		Session session = null;
		Transaction transaction = null;
		List batchList = null;
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("select sub_id from Batch where batch_id =:batchID and class_id=:class_id and div_id=:div_id");
			query.setParameter("batchID", Integer.parseInt(batchID));
			query.setParameter("class_id",inst_id);
			query.setParameter("div_id", div_id);
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
		return batchList;
	}

public List<Batch> retriveAllBatches(List<String> class_id) {
	Session session = null;
	Transaction transaction = null;
	List batchList = null;
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("select sub_id from Batch where batch_id in (:batchID)");
		query.setParameterList("batchID", class_id);
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
	return batchList;


}
	public List<Batch> retriveAllBatchesOfDivision(int divisionId, int class_id){
	Session session = null;
	Transaction transaction = null;
	List<Batch> batchList = null;
	List<Batch> batches = new ArrayList<Batch>();
	
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		DivisionDB divisionDB= new DivisionDB();
 		//Division division=divisionDB.retrive(divisionId);
		Division division=divisionDB.retriveByDivisionId(divisionId);
		int div_id= division.getDivId();
		Query query = session.createQuery("from Batch where class_id =:class_id and div_id =:div_id");
		query.setInteger("class_id", class_id);
		query.setInteger("div_id", div_id);
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
			batches.add(batchList.get(i));
		}
	}
	return batches;
}

public List<Batch> retriveAllRelatedBatches(int class_id, int div_id){
		Session session = null;
		Transaction transaction = null;
		List<Batch> batchList = null;
		List<Batch> batches = new ArrayList<Batch>();
		
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from Batch where class_id =:class_id and div_id =:div_id");
			query.setInteger("class_id", class_id);
			query.setInteger("div_id", div_id);
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
				batches.add(batchList.get(i));
			}
		}
		return batches;
	}

public List<Batch> getbachesrelatedtosubject(String subjectid) {
	Session session = null;
	Transaction transaction = null;
	List<Batch> batchList = null;
	List<Batch> batches = new ArrayList<Batch>();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("from Batch where sub_id LIKE :sub_id1 OR sub_id LIKE :sub_id2 OR sub_id LIKE :sub_id3 OR sub_id = :sub_id4");
		query.setParameter("sub_id1", subjectid+",%");
		query.setParameter("sub_id2", "%,"+subjectid);
		query.setParameter("sub_id3", "%,"+subjectid+",%");
		query.setParameter("sub_id4", subjectid);
		
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
	return batchList;
}

public List<Batch> getbachesrelatedtoclass(int classid) {
	Session session = null;
	Transaction transaction = null;
	List<Batch> batchList = null;
	List<Batch> batches = new ArrayList<Batch>();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("from Batch where div_id=:div_id ");
		query.setParameter("div_id", classid);		
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
	return batchList;
}

public boolean deletebatchrelatedtoclass(int classid) {
	Session session = null;
	Transaction transaction = null;
	List<Batch> batchList = null;
	List<Batch> batches = new ArrayList<Batch>();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("DELETE from Batch where div_id=:div_id");
		query.setParameter("div_id", classid);
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

public Integer getBatchCount(int class_id){
	
	Session session = null;
	Transaction transaction = null;
	List<Batch> batchList = null;
	List<Long> batches = new ArrayList<Long>();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("select count(*) from Batch where class_id=:class_id");
		query.setParameter("class_id", class_id);
	batches=query.list();
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
	return batches.get(0).intValue();
}

public List<Batch> getbachesrelatedtodivandsubject(String subjectid,int divId,int class_id) {
	Session session = null;
	Transaction transaction = null;
	List<Batch> batchList = null;
	List<Batch> batches = new ArrayList<Batch>();
	try{
		session = HibernateUtil.getSessionfactory().openSession();
		transaction = session.beginTransaction();
		Query query = session.createQuery("from Batch where div_id=:div_id and class_id=:class_id and (sub_id LIKE :sub_id1 OR sub_id LIKE :sub_id2 OR sub_id LIKE :sub_id3 OR sub_id = :sub_id4)");
		query.setParameter("sub_id1", subjectid+",%");
		query.setParameter("sub_id2", "%,"+subjectid);
		query.setParameter("sub_id3", "%,"+subjectid+",%");
		query.setParameter("sub_id4", subjectid);
		query.setParameter("div_id", divId);
		query.setParameter("class_id", class_id);
		
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
	return batchList;
}


}


