package com.classapp.Class;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.logger.AppLogger;
import com.classapp.persistence.HibernateUtil;
import com.google.gson.Gson;

public class SearchClassDb {
	public List searchClassData(String searchData){
		Query query =null;
		Session session =null;
		List list =null;
		try{
		Gson gson = new Gson();
		ClassSearchForm classSearchForm = gson.fromJson(searchData, ClassSearchForm.class);
		String queryStr = formQuery(classSearchForm);
		session = HibernateUtil.getSessionfactory().openSession();
		query = session.createQuery(queryStr);
		
		if(queryStr.contains("cname"))
			query.setParameter("cname",classSearchForm.getCname()+"%");
		list = query.list();
		AppLogger.logger("In ClassSeachForm-DB.."+classSearchForm);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(null!=session){
				session.close();
			}
		}
		return list;
	}
	
	private String formQuery(ClassSearchForm classSearchForm){
		String query = "from RegisterBean "; 
		boolean isParameter = false;
		if (null != classSearchForm.getCname() && !"".equals(classSearchForm.getCname())) {
			if(!isParameter)
				query += "where className like :cname ";
			else
				query += "and className like :cname ";
			isParameter = true;
		}
		/*
		if (null != classSearchForm.getCexactdate() && !"".equals(classSearchForm.getCexactdate())) {
			if(!isParameter)
				query += "where className like %:cname% ";
			else
				query += "and className = :cname ";
			isParameter = true;
				
		}

		/*
		if (null != classSearchForm.getCname() && "".equals(classSearchForm.getCname())) {
			
		}

		if (null != classSearchForm.getCname() && "".equals(classSearchForm.getCname())) {
			
		}

		if (null != classSearchForm.getCname() && "".equals(classSearchForm.getCname())) {
			
		}
		*/
		return query;
	}
	
}
