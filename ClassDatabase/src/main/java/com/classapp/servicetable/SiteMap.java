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
		
		/*If role is null fetch default links for sitemap*/
		if(null==role){
			role = "-1";
		}
		
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery("from SiteMapData where  role like :role");
			query.setParameter("role", "%"+role+"%");
			siteMapDatas = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=session){
				session.close();
			}
		}
		return siteMapDatas;
	}
	
	public static void main(String[] args) {
		SiteMap map = new SiteMap();
		//List<SiteMapData> allLinkList = map.getSiteMapList("1");
	}
}
