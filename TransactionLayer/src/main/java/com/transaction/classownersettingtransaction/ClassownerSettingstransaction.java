package com.transaction.classownersettingtransaction;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.classapp.db.classOwnerSettings.ClassOwnerNotificationDb;
import com.classapp.db.institutestats.InstituteStatsDB;
import com.classapp.persistence.HibernateUtil;
import com.service.beans.ClassOwnerNotificationBean;
import com.service.beans.ClassownerSettingsNotification;
import com.service.beans.InstituteStats;

public class ClassownerSettingstransaction {
	public boolean saveSettings(ClassOwnerNotificationBean bean,int inst_id ){
		ClassOwnerNotificationDb db= new ClassOwnerNotificationDb();
		com.classapp.db.classOwnerSettings.ClassOwnerNotificationBean dbBean = new com.classapp.db.classOwnerSettings.ClassOwnerNotificationBean(); 
		bean.setRegId(inst_id);
		try {
			BeanUtils.copyProperties(dbBean, bean);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		db.saveClassownerNotification(dbBean);
		return true;
	}
	
	public ClassownerSettingsNotification getSettings(int inst_id) {
		ClassownerSettingsNotification classownerSettingsNotification = new ClassownerSettingsNotification();
		
			//Copy institute state
			InstituteStatsDB instituteStatsDB = new InstituteStatsDB();
			InstituteStats instituteStats = new InstituteStats();
			com.classapp.db.institutestats.InstituteStats instituteStatsDb = instituteStatsDB.getStats(inst_id);
			try {
				BeanUtils.copyProperties(instituteStats, instituteStatsDb);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			classownerSettingsNotification.setInstituteStats(instituteStats);
			
			//Copy classowner notification bean
			ClassOwnerNotificationDb db= new ClassOwnerNotificationDb();
			com.classapp.db.classOwnerSettings.ClassOwnerNotificationBean classOwnerNotificationBeanDb = db.getClassOwnerNotification(inst_id);
			ClassOwnerNotificationBean classownerSettingsNotificationBean = new ClassOwnerNotificationBean();
			try {
				BeanUtils.copyProperties(classownerSettingsNotificationBean, classOwnerNotificationBeanDb);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			classownerSettingsNotification.setClassOwnerNotificationBean(classownerSettingsNotificationBean);
			return classownerSettingsNotification;
	}
}
