package com.classapp.edit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.persistence.HibernateUtil;

public class DBUpdate {
	public String updateDuration(String regId,String duration){
		String errorMessage = null;
		Date currDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String currDateStr = dateFormat.format(currDate);
		System.out.println("CurrDate:"+currDateStr);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, Integer.parseInt(duration));
		String date = now.get(Calendar.DAY_OF_MONTH)+"";
		String year = now.get(Calendar.YEAR)+"";
		String month = now.get(Calendar.MONTH)+"";
		String dateConverted = dateConverter(year, month, date);
		String query = "UPDATE RegisterBean SET endDate =:endDt, startDate =:startDate WHERE regId = :regId"; 
		Session session = HibernateUtil.getSessionfactory().openSession();
		Transaction transaction = session.beginTransaction();
		Query updateQuery = session.createQuery(query);
		updateQuery.setParameter("startDate", currDateStr);
		updateQuery.setParameter("endDt", dateConverted);
		updateQuery.setParameter("regId", Integer.parseInt(regId));
		int result = updateQuery.executeUpdate();
		transaction.commit();
		System.out.println("Result "+result);
		return errorMessage;
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
