package com.transaction.classownersettingtransaction;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationDb;
import com.classapp.db.institutestats.InstituteStatsDB;
import com.service.beans.ClassOwnerNotificationBean;
import com.service.beans.ClassownerSettingsNotification;
import com.service.beans.InstituteStats;
import com.util.NotificationEnum;

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
				if(null!=classOwnerNotificationBeanDb){
					BeanUtils.copyProperties(classownerSettingsNotificationBean, classOwnerNotificationBeanDb);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			classownerSettingsNotification.setClassOwnerNotificationBean(classownerSettingsNotificationBean);
			return classownerSettingsNotification;
	}
	
	public boolean reduceSmsCount(int smsCount,int classId){
		InstituteStatsDB instituteStatsDB = new InstituteStatsDB();
		instituteStatsDB.decreaseUsedSms(smsCount, classId);
		return false;
	}
	
	/*This method will verify whether you have correct access or not*/
	public String validateClassOwnerAccess(NotificationEnum.MessageCategery messageCategery, int instId){
		ClassownerSettingsNotification settings = getSettings(instId);
		if(messageCategery.equals(NotificationEnum.MessageCategery.ATTENDANCE_DAILY)){
			if(settings.getClassOwnerNotificationBean().isEmailAttendanceDaily() && !settings.getInstituteStats().isEmailAccess()){
				return "No access to email";
			}
			if(settings.getClassOwnerNotificationBean().isSmsAttendanceDaily() && !settings.getInstituteStats().isSmsAccess()){
				return "No access to sms";
			}else if(settings.getInstituteStats().getSmsAlloted()<settings.getInstituteStats().getSmsLeft()){
				return "No sms left";
			}
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.ATTENDANCE_MONTHLY)){
			if(settings.getClassOwnerNotificationBean().isEmailAttendanceMonthly() && !settings.getInstituteStats().isEmailAccess()){
				return "No access to email";
			}
			if(settings.getClassOwnerNotificationBean().isSmsAttendanceMonthly() && !settings.getInstituteStats().isSmsAccess()){
				return "No access to sms";
			}else if(settings.getInstituteStats().getSmsAlloted()<settings.getInstituteStats().getSmsLeft()){
				return "No sms left";
			}
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.ATTENDANCE_WEELY)){
			if(settings.getClassOwnerNotificationBean().isEmailAttendanceWeekly() && !settings.getInstituteStats().isEmailAccess()){
				return "No access to email";
			}
			if(settings.getClassOwnerNotificationBean().isSmsAttendanceWeekly() && !settings.getInstituteStats().isSmsAccess()){
				return "No access to sms";
			}else if(settings.getInstituteStats().getSmsAlloted()<settings.getInstituteStats().getSmsLeft()){
				return "No sms left";
			}
			
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.FEE_ON_PAYMENT)){
			if(settings.getClassOwnerNotificationBean().isEmailPayment() && !settings.getInstituteStats().isEmailAccess()){
				return "No access to email";
			}
			if(settings.getClassOwnerNotificationBean().isSmsPayment() && !settings.getInstituteStats().isSmsAccess()){
				return "No access to sms";
			}else if(settings.getInstituteStats().getSmsAlloted()<settings.getInstituteStats().getSmsLeft()){
				return "No sms left";
			}
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.FEE_PAYMENT_DUE)){
			if(settings.getClassOwnerNotificationBean().isEmailPaymentDue() && !settings.getInstituteStats().isEmailAccess()){
				return "No access to email";
			}
			if(settings.getClassOwnerNotificationBean().isSmsPaymentDue() && !settings.getInstituteStats().isSmsAccess()){
				return "No access to sms";
			}else if(settings.getInstituteStats().getSmsAlloted()<settings.getInstituteStats().getSmsLeft()){
				return "No sms left";
			}
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.PROGRESS_CARD_MANUAL)){
			if(settings.getClassOwnerNotificationBean().isEmailProgressCardManual() && !settings.getInstituteStats().isEmailAccess()){
				return "No access to email";
			}
			if(settings.getClassOwnerNotificationBean().isSmsProgressCardManual() && !settings.getInstituteStats().isSmsAccess()){
				return "No access to sms";
			}else if(settings.getInstituteStats().getSmsAlloted()<settings.getInstituteStats().getSmsLeft()){
				return "No sms left";
			}
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.PROGRESS_CARD_ON_EVERY_EXAM)){
			if(settings.getClassOwnerNotificationBean().isEmailProgressCardAfterEveryExam() && !settings.getInstituteStats().isEmailAccess()){
				return "No access to email";
			}
			if(settings.getClassOwnerNotificationBean().isSmsProgressCardAfterEveryExam() && !settings.getInstituteStats().isSmsAccess()){
				return "No access to sms";
			}else if(settings.getInstituteStats().getSmsAlloted()<settings.getInstituteStats().getSmsLeft()){
				return "No sms left";
			}
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.TIMETABLE_NEW)){
			if(settings.getClassOwnerNotificationBean().isEmailTimetableNewEntry() && !settings.getInstituteStats().isEmailAccess()){
				return "No access to email";
			}
			if(settings.getClassOwnerNotificationBean().isSmsTimetableNewEntry() && !settings.getInstituteStats().isSmsAccess()){
				return "No access to sms";
			}else if(settings.getInstituteStats().getSmsAlloted()<settings.getInstituteStats().getSmsLeft()){
				return "No sms left";
			}
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.TIMETABLE_UPDATED)){
			if(settings.getClassOwnerNotificationBean().isEmailTimetableEditEntry() && !settings.getInstituteStats().isEmailAccess()){
				return "No access to email";
			}
			if(settings.getClassOwnerNotificationBean().isSmsTimetableEditEntry() && !settings.getInstituteStats().isSmsAccess()){
				return "No access to sms";
			}else if(settings.getInstituteStats().getSmsAlloted()<settings.getInstituteStats().getSmsLeft()){
				return "No sms left";
			}
		}
		return null;
	}
	
	/*
	 * if(messageCategery.equals(NotificationEnum.MessageCategery.ATTENDANCE_DAILY)){
			
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.ATTENDANCE_DAILY)){
			
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.ATTENDANCE_MONTHLY)){
			
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.ATTENDANCE_WEELY)){
			
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.FEE_ON_PAYMENT)){
			
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.FEE_PAYMENT_DUE)){
			
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.PROGRESS_CARD_MANUAL)){
			
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.PROGRESS_CARD_ON_EVERY_EXAM)){
			
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.TIMETABLE_NEW)){
			
		}else if(messageCategery.equals(NotificationEnum.MessageCategery.TIMETABLE_UPDATED)){
			
		}
	 * 
	 */
}
