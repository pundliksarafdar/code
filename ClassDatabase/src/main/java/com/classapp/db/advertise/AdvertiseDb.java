package com.classapp.db.advertise;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import com.classapp.persistence.HibernateUtil;

public class AdvertiseDb {
	public List<String> getAllCounrty(){
		Session session = null;
		List<String> countryList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery("select distinct country from RegisterBean");
			countryList = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (null!=session) {
				session.close();
			}
		}
		return countryList;
	}
	
	
	public List<String> getAllStateInCountry(String country){
		Session session = null;
		List<String> countryList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery("select distinct state from RegisterBean where country=:country");
			query.setString("country", country);
			countryList = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (null!=session) {
				session.close();
			}
		}
		return countryList;
	}
	
	public List<String> getAllCityInStateAndCountry(String country,String state){
		Session session = null;
		List<String> countryList = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			Query query = session.createQuery("select distinct city from RegisterBean where country=:country and state=:state");
			query.setString("country", country);
			query.setString("state", state);
			countryList = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (null!=session) {
				session.close();
			}
		}
		return countryList;
	}
	
	public static void main(String[] args) {
		AdvertiseDb advertiseDb = new AdvertiseDb();
		List<String> advCountryList = advertiseDb.getAllCounrty();
		System.out.println(advCountryList);
		
		List<String> advStateList = advertiseDb.getAllStateInCountry("India");
		System.out.println(advStateList);
		
		List<String> advCityList = advertiseDb.getAllCityInStateAndCountry("India", "Mah");
		System.out.println(advCityList);
	}
}
