package com.classapp.myclass;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.classapp.persistence.Constants;
import com.classapp.persistence.HibernateUtil;
import com.classapp.servicetable.ServiceMap;

public class SearchClassDb {
	static final String CNAME = "cname";
	static final String CITY = "city1";
	static final String STATE = "state1";
	static final String BLANK="";
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
		if(!"".equals(classSearchForm.getCcity()))
		{
			query.setParameter("city1",classSearchForm.getCcity()+"%");
		}else{
			query.setParameter("city1",BLANK+"%");
		}
		if(!"".equals(classSearchForm.getCstate()))
		{
			query.setParameter("state1", classSearchForm.getCstate()+"%");
		}else{
			query.setParameter("state1",BLANK+"%");
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
				query += "where className like :cname and  city like :city1 and state like :state1";
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
		if(!"".equals(classSearchForm.getCcity()))
		{
			query.setParameter("city1",classSearchForm.getCcity()+"%");
		}else{
			query.setParameter("city1",BLANK+"%");
		}
		if(!"".equals(classSearchForm.getCstate()))
		{
			query.setParameter("state1", classSearchForm.getCstate()+"%");
		}else{
			query.setParameter("state1",BLANK+"%");
		}
		int totalResult = query.list().size();
		count = totalResult/resultPerPage;
		return count;
	}

}
