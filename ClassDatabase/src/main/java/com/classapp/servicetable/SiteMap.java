package com.classapp.servicetable;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;
import com.google.gson.Gson;

public class SiteMap {
	public static List<SiteMapData> getSiteMapList(String role){
		List<SiteMapData> siteMapDatas = new ArrayList<SiteMapData>();
		
		/*
		for(int i=0;i<10;i++){
			SiteMapData siteMapData = new SiteMapData();
			siteMapData.setSrno("1"+i);
			siteMapData.setHref("href1"+i);
			siteMapData.setLinkName("link1"+i);
			siteMapData.setTitle("title1"+i);
			siteMapDatas.add(siteMapData);
		}
		*/
		
		/*If role is null fetch default links for sitemap*/
		if(null==role){
			role = "-1";
		}
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from SiteMapData where  role in :role");
			query.setParameter("role", role);
			siteMapDatas = query.list();
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
		return siteMapDatas;
	}
	
	public static void main(String[] args) {
		SiteMap map = new SiteMap();
		List<SiteMapData> allLinkList = map.getSiteMapList("1");
	}
}
