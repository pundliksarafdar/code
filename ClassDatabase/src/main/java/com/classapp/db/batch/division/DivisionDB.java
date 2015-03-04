package com.classapp.db.batch.division;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

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
		Transaction transaction = null;
		ArrayList<Division> listOfDivision=new ArrayList<Division>();
		String queryString="from Division";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			List list=query.list();
			for (Object object : list) {
				listOfDivision.add((Division)object);
			}
			transaction.commit();
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
		Transaction transaction = null;
		String queryString="from Division";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
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
			transaction.commit();
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
		Transaction transaction = null;
		ArrayList<Division> listOfDivision=new ArrayList<Division>();
		String queryString="from Division";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
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
			transaction.commit();
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
		Transaction transaction = null;
		Object queryResult=null;
		String queryString="from Division where divisionName = :div_name and stream=:stream";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setString("div_name", divisionName); 
			query.setString("stream", stream); 
			queryResult = query.uniqueResult();
			transaction.commit();
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
		Transaction transaction = null;
		Object queryResult=null;
		String queryString="from Division where divId = :divId";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("divId", divisionId);  
			queryResult = query.uniqueResult();
			transaction.commit();
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
		Transaction transaction = null;
		List<Division> queryResult=null;
		String queryString="from Division where div_name = :div_name and stream=:stream";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
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
		}
		
		return 0;
	}

	public List<Division> getAllDivision(Integer regId) {
		Session session = null;
		Transaction transaction = null;
		List<Integer> queryResult=null;
		List<Division> divisions = new ArrayList<Division>();
		String queryString="from Division where institute_id=:institute_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setInteger("institute_id", regId);
			divisions = query.list();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return divisions;
		
	}
	
	public boolean isDivisionExists(Division division) {
		Session session = null;
		Transaction transaction = null;
		List queryResult=null;
		String queryString="from Division where div_name = :div_name and stream=:stream";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setString("div_name", division.getDivisionName()); 
			query.setString("stream", division.getStream());
			queryResult = query.list();
			if(queryResult.size()>0)
			{
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	public Division retrive(String divisionName){
		Session session = null;
		Transaction transaction = null;
		Object queryResult=null;
		String queryString="from Division where divisionName = :div_name";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setString("div_name", divisionName);  
			queryResult = query.uniqueResult();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		
		return (Division)queryResult;
	}
	
	public Division retriveByDivisionId(String divisionId){
		Session session = null;
		Transaction transaction = null;
		Object queryResult=null;
		String queryString="from Division where divId = :div_id";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setString("div_id", divisionId);  
			queryResult = query.uniqueResult();
			transaction.commit();
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
}
