package com.classapp.db.batch.division;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

public class DivisionDB {
	public static String updateDb(Division division){
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
	
	public static List<Division> getAllDivisions(){
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
		}
		
		return listOfDivision;
	}
	
	public static int removeByDivID(int div_id){
		Session session = null;
		int status=0;
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
					status=1;
					break;
				}
			}
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return status;
	}
	
	public static int removeByDivisionName(String div_name){
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
		}
		
		return status;
	}
	
	public static Division retrive(String divisionName){
		Session session = null;
		Transaction transaction = null;
		Object queryResult=null;
		String queryString="from Division where div_name = :div_name";
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(queryString);
			query.setString("div_name", divisionName);  
			queryResult = query.uniqueResult();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return (Division)queryResult;
	}
}
