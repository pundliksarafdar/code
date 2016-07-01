package com.schedule.quartz;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.classapp.db.classOwnerSettings.ClassOwnerNotificationBean;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationDb;
import com.classapp.db.fees.FeesDB;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterDB;
import com.classapp.persistence.Constants;
import com.notification.access.NotifcationAccess;
import com.notification.bean.MessageDetailBean;
import com.notification.sms.SmsNotificationTransaction;
import com.service.beans.ClassownerSettingsNotification;
import com.service.beans.StudentFessNotificationData;
import com.transaction.classownersettingtransaction.ClassownerSettingstransaction;

public class FeesDueScheduler implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		sendFeesDueNotification();
		
	}
	
	public void sendFeesDueNotification() {
		Date date =new Date(new java.util.Date().getTime());
		ClassOwnerNotificationDb classOwnerNotificationDb = new ClassOwnerNotificationDb();
		List<ClassOwnerNotificationBean> classOwnerNotificationBeanList = classOwnerNotificationDb.getInstitutesForFeesDueNotifications(date);
		RegisterDB db =new RegisterDB();
		FeesDB feesDB = new FeesDB();
		for (ClassOwnerNotificationBean classOwnerNotificationBean : classOwnerNotificationBeanList) {
			ClassownerSettingstransaction settingsTx = new ClassownerSettingstransaction();
			ClassownerSettingsNotification settings = settingsTx.getSettings(classOwnerNotificationBean.getRegId());
			if(settings.getInstituteStats().isSmsAccess() || settings.getInstituteStats().isEmailAccess()){
			RegisterBean institute =db.getRegistereduser(classOwnerNotificationBean.getRegId());
			List studentList = feesDB.getStudentDueFeesForNotification(classOwnerNotificationBean.getRegId());
			int msgCounter = 0;
			List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
			for (Iterator iterator = studentList.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				MessageDetailBean messageDetailBean = new MessageDetailBean();
				messageDetailBean.setFrom(institute.getClassName());
				messageDetailBean.setEmailSubject("Fees Due");
				messageDetailBean.setStudentId(((Number)object[0]).intValue());
				messageDetailBean.setStudentEmail((String)object[4]);
				if(!"".equals((String)object[3])){
				messageDetailBean.setStudentPhone(Long.parseLong((String)object[3]));
				}
				messageDetailBean.setParentEmail((String)object[8]);
				if(!"".equals((String)object[7])){
				messageDetailBean.setParentPhone(Long.parseLong((String)object[7]));
				}
				if(settings.getInstituteStats().isEmailAccess()){
					messageDetailBean.setMessageTypeEmail(classOwnerNotificationBean.isEmailPaymentDue());
					}else{
					messageDetailBean.setMessageTypeEmail(false);	
					}
					if(settings.getInstituteStats().isSmsAccess()){
					messageDetailBean.setMessageTypeSms(classOwnerNotificationBean.isSmsPaymentDue());
					}else{
						messageDetailBean.setMessageTypeSms(false);
				}
				messageDetailBean.setSendToParent(classOwnerNotificationBean.isParentPaymentDue());
				messageDetailBean.setSendToStudent(classOwnerNotificationBean.isStudentPaymentDue());
				if(messageDetailBean.getStudentPhone()!= null && messageDetailBean.isSendToStudent() && messageDetailBean.getStudentPhone()!= 0){
					msgCounter ++;
				}
				if(messageDetailBean.getParentPhone()!= null && messageDetailBean.isSendToParent() && messageDetailBean.getParentPhone()!= 0){
					msgCounter ++;
				}
				StudentFessNotificationData data = new StudentFessNotificationData(); 
				data.setStudent_name((String)object[1]+" "+(String)object[2]);
				data.setParent_name((String)object[5]+" "+(String)object[6]);
				data.setTotal_fees(((Number)object[9]).doubleValue());
				data.setFees_paid(((Number)object[10]).doubleValue());
				data.setFee_due(((Number)object[11]).doubleValue());
				data.setBatch_name((String)object[12]);
				messageDetailBean.setEmailMessage(null);
				messageDetailBean.setEmailObject(data);
				messageDetailBean.setEmailTemplate("feesAlertStudent.tmpl");
				messageDetailBean.setParentEmailMessage(null);
				messageDetailBean.setParentEmailObject(data);
				messageDetailBean.setParentEmailTemplate("feesAlertParent.tmpl");
				messageDetailBean.setSmsMessage(null);
				messageDetailBean.setSmsTemplate("manualFeesDueAlertStudentSMS.tmpl");
				messageDetailBean.setSmsObject(data);
				messageDetailBean.setSmsParentMessage(null);
				messageDetailBean.setSmsParentObject(data);
				messageDetailBean.setSmsParentTemplate("manualFeesDueAlertParentSMS.tmpl");
				detailBeans.add(messageDetailBean);
			}
			NotifcationAccess notifcationAccess = new NotifcationAccess();
			if(classOwnerNotificationBean.isSmsPaymentDue()){
			SmsNotificationTransaction  notificationTransaction = new SmsNotificationTransaction();
			HashMap statusMap = notificationTransaction.validateNSendSms(msgCounter, "", classOwnerNotificationBean.getRegId(),Constants.COMMON_ZERO,Constants.COMMON_ZERO,Constants.PARENT_ROLE,Constants.AUTO_DAILY_MSG);;
			if(statusMap.containsKey("access") && (boolean)statusMap.get("access")){
			notifcationAccess.send(detailBeans);
			ClassownerSettingstransaction settingstransaction = new ClassownerSettingstransaction();
			settingstransaction.reduceSmsCount(msgCounter, institute.getRegId());
			}
			}else{
				notifcationAccess.send(detailBeans);
			}
			}
		}
	}

}
