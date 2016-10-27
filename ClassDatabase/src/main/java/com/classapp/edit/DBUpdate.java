package com.classapp.edit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.logger.AppLogger;
import com.classapp.persistence.HibernateUtil;

public class DBUpdate {
	public String updateDuration(String regId,String duration){
		String errorMessage = null;
		Date currDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String currDateStr = dateFormat.format(currDate);
		AppLogger.logger("CurrDate:"+currDateStr);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, Integer.parseInt(duration));
		String date = now.get(Calendar.DAY_OF_MONTH)+"";
		String year = now.get(Calendar.YEAR)+"";
		String month = now.get(Calendar.MONTH)+"";
		String dateConverted = dateConverter(year, month, date);
		String query = "UPDATE RegisterBean SET endDate =:endDt, startDate =:startDate WHERE regId = :regId";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query updateQuery = session.createQuery(query);
			updateQuery.setParameter("startDate", currDateStr);
			updateQuery.setParameter("endDt", dateConverted);
			updateQuery.setParameter("regId", Integer.parseInt(regId));
			int result = updateQuery.executeUpdate();
			transaction.commit();
			query = "Select endDate from RegisterBean WHERE regId = :regId"; 
			transaction = session.beginTransaction();
			updateQuery = session.createQuery(query);
			updateQuery.setParameter("regId", Integer.parseInt(regId));
			List resList = updateQuery.list();
			errorMessage = (String) resList.get(0);
			AppLogger.logger("Result "+result);
		}catch(Exception e){
			transaction.rollback();
			errorMessage = "error";
		}finally{
			if(null != session){
				session.close();
			}
		}
		return errorMessage;
	}
	
	public String blockUser(String regId,String role){
		String errorMessage = null;
		String query = "UPDATE RegisterBean SET role =:rolBlock WHERE regId = :regId";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query updateQuery = session.createQuery(query);
			Integer roleInt = Integer.parseInt("1"+role);
			updateQuery.setParameter("rolBlock", roleInt);
			updateQuery.setParameter("regId", Integer.parseInt(regId));
			int result = updateQuery.executeUpdate();
			transaction.commit();
			AppLogger.logger("Result "+result);
		}catch(Exception e){
			transaction.rollback();
		}finally{
			if(null != session){
				session.close();
			}
		}
		return errorMessage;
	}
	
	public String unBlockUser(String regId,String role){
		String errorMessage = null;
		String query = "UPDATE RegisterBean SET role =:rolBlock WHERE regId = :regId";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query updateQuery = session.createQuery(query);
			Integer roleInt = Integer.parseInt(role);
			updateQuery.setParameter("rolBlock", roleInt%10);
			updateQuery.setParameter("regId", Integer.parseInt(regId));
			int result = updateQuery.executeUpdate();
			transaction.commit();
			AppLogger.logger("Result "+result);
		}catch(Exception e){
			transaction.rollback();
		}finally{
			if(null != session){
				session.close();
			}
		}
		return errorMessage;
	}

	public boolean deleteUser(String regId){
		boolean result = true;
		String query = "DELETE FROM RegisterBean WHERE regId = :regId";
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionfactory().openSession();
			transaction = session.beginTransaction();
			Query updateQuery = session.createQuery(query);
			updateQuery.setParameter("regId", Integer.parseInt(regId));
			int rowaffected = updateQuery.executeUpdate();
			transaction.commit();
			AppLogger.logger("Result "+rowaffected);
			result = true;
		}catch(Exception e){
			e.printStackTrace();
			transaction.rollback();
			result = false;
		}finally{
			if(null != session){
				session.close();
			}
		}
		return result;
	}

	
	public String dateConverter(String year,String month,String day){
		String date = "";
		if (month.length() != 2) {
			month = "0"+month;
		}
		
		if (day.length() != 2) {
			day = "0"+day;
		}
		
		date = year+month+day;
		return date;
	}
	
}
