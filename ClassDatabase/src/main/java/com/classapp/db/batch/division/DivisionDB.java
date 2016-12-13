package com.classapp.db.batch.division;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;

import com.classapp.db.batch.Batch;
import com.classapp.persistence.HibernateUtil;
import com.datalayer.division.InstituteData;

public class DivisionDB {
	public String updateDb(Division division){
		String status = "0";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(division);
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
	
	public List<Division> getAllDivisions(){
		Session session = null;
		ArrayList<Division> listOfDivision=new ArrayList<Division>();
		String queryString="from Division";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery(queryString);
			List list=query.list();
			for (Object object : list) {
				listOfDivision.add((Division)object);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return listOfDivision;
	}
	
	public boolean removeByDivID(int div_id){
		Session session = null;
		boolean status=false;
		String queryString="from Division";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery(queryString);
			List list=query.list();
			for (Object object : list) {
				Division division=(Division)object;
				if(division.getDivId()==div_id){
					session.delete(object);
					status=true;
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return status;
	}
	
	public int removeByDivisionName(String div_name){
		Session session = null;
		int status=0;
		ArrayList<Division> listOfDivision=new ArrayList<Division>();
		String queryString="from Division";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery(queryString);
			List list=query.list();
			for (Object object : list) {
				Division division=(Division)object;
				if(division.getDivisionName().equalsIgnoreCase(div_name)){
					session.delete(object);
					status=1;
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return status;
	}
	
	public Division retrive(String divisionName,String stream){
		Session session = null;
		Object queryResult=null;
		String queryString="from Division where divisionName = :div_name and stream=:stream";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery(queryString);
			query.setString("div_name", divisionName); 
			query.setString("stream", stream); 
			queryResult = query.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return (Division)queryResult;
	}
	
	public Division retriveByID(int divisionId){
		Session session = null;
		Object queryResult=null;
		String queryString="from Division where divId = :divId";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery(queryString);
			query.setInteger("divId", divisionId);  
			queryResult = query.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
			return (Division)queryResult;
	}
	
	
	public int getdivisionID(Division division) {
		Session session = null;
		List<Division> queryResult=null;
		String queryString="from Division where div_name = :div_name and stream=:stream";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery(queryString);
			query.setString("div_name", division.getDivisionName()); 
			query.setString("stream", division.getStream());
			queryResult = query.list();
			if(queryResult.size()>0)
			{
				return queryResult.get(0).getDivId();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
			
		
		return 0;
	}

	public List<Division> getAllDivision(Integer regId) {
		Session session = null;
		List<Integer> queryResult=null;
		List<Division> divisions = new ArrayList<Division>();
		String queryString="from Division where institute_id=:institute_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery(queryString);
			query.setInteger("institute_id", regId);
			divisions = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return divisions;		
	}
	
	
	public boolean isDivisionExists(Division division) {
		Session session = null;
		List queryResult=null;
		String queryString="from Division where div_name = :div_name and stream=:stream and institute_id=:institute_id and divId!=:divId";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery(queryString);
			query.setString("div_name", division.getDivisionName()); 
			query.setString("stream", division.getStream());
			query.setInteger("institute_id", division.getInstitute_id());
			query.setInteger("divId", division.getDivId());
			queryResult = query.list();
			if(queryResult.size()>0)
			{
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
			
		
		return false;
		
	}
	
	public Division retrive(String divisionName){
		Session session = null;
		Object queryResult=null;
		String queryString="from Division where divisionName = :div_name";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery(queryString);
			query.setString("div_name", divisionName);  
			queryResult = query.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return (Division)queryResult;
	}
	
	public Division retriveByDivisionId(int divisionId){
		Session session = null;
		Object queryResult=null;
		String queryString="from Division where divId = :div_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery(queryString);
			query.setInteger("div_id", divisionId);  
			queryResult = query.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return (Division)queryResult;
	}
	public boolean deletedivision(int classid) {
		Session session = null;
		Transaction transaction = null;
		Object queryResult=null;
		String queryString="DELETE from Division where divId = :divId";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter("divId", classid); 
		query.executeUpdate();
		transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return true;
	}
	
	//Mobile function
	 public List<InstituteData> getBatchMobile(int instId){
		 String batchString = "select regtable.reg_id as classId,regtable.classname as classname ,division.div_id as divId,division.div_name as divisionName,"
		 		+ "division.stream as stream,batch.batch_id as batchId,batch.batch_name as batchName from division,batch,regtable "
		 		+ "where division.institute_id=batch.class_id and regtable.reg_id=:instId and batch.class_id=:instId and batch.div_id = division.div_id";
		 Session session = null;
			List<InstituteData> status = null;
			try{
				session = HibernateUtil.getSessionfactory().openSession();
				Query query = session.createSQLQuery(batchString);
				query.setParameter("instId", instId).setResultTransformer(Transformers.aliasToBean(InstituteData.class));
				
				status = query.list();
				Set<InstituteData> instituteDatas = new HashSet<InstituteData>();
				instituteDatas.addAll(status);
				status = new ArrayList<InstituteData>();
				status.addAll(instituteDatas);
				return status;
				}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(null!=session){
					session.close();
				}
			}
			return null;
	 }
	 
	 public static void main(String[] args) {
		DivisionDB divisionDB = new DivisionDB();
		divisionDB.getBatchMobile(191);
	}
	}


