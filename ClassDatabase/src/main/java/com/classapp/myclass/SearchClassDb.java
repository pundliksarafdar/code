package com.classapp.myclass;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.classapp.persistence.Constants;
import com.classapp.persistence.HibernateUtil;
import com.classapp.servicetable.ServiceMap;

public class SearchClassDb {
	static final String CNAME = "cname";
	public List searchClassData(ClassSearchForm classSearchForm){
		int startPage = 0;
		if(null != classSearchForm.getCurrentPage()){
			startPage = Integer.parseInt(classSearchForm.getCurrentPage());
		}
		int resultPerPage = Integer.parseInt(ServiceMap.getSystemParam(Constants.SERVICE_PAGINATION, Constants.RESULT_PER_PAGE));
		String queryStr = formQuery(classSearchForm);
		Session session = HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery(queryStr);
		
		if(queryStr.contains(CNAME)){
			query.setParameter(CNAME,classSearchForm.getCname()+"%");
		}
		
		if(0 != startPage || 0 != resultPerPage){
			query.setFirstResult(startPage*resultPerPage);
			query.setMaxResults(resultPerPage);
		}
		
		List list = query.list();
		
		System.out.println("In ClassSeachForm-DB.."+classSearchForm);
		return list;
	}
	
	private String formQuery(ClassSearchForm classSearchForm){
		String query = "from RegisterBean "; 
		boolean isParameter = false;
		if (null != classSearchForm.getCname() && !"".equals(classSearchForm.getCname())) {
			if(!isParameter){
				query += "where className like :cname ";
			}else{
				query += "and className like :cname ";
			}
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

	public int getPagesCount(ClassSearchForm classSearchForm){
		int count = 0;
		int resultPerPage = Integer.parseInt(ServiceMap.getSystemParam(Constants.SERVICE_PAGINATION, "resultsperpage"));
		String queryStr = formQuery(classSearchForm);
		Session session = HibernateUtil.getSessionfactory().openSession();
		Query query = session.createQuery(queryStr);
		
		if(queryStr.contains(CNAME)){
			query.setParameter(CNAME,classSearchForm.getCname()+"%");
		}
		int totalResult = query.list().size();
		count = totalResult/resultPerPage;
		return count;
	}

}
