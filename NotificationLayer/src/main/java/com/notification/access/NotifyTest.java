package com.notification.access;

import java.util.ArrayList;
import java.util.List;

import com.classappp.notification.bean.MonthlyAttendanceBean;
import com.classappp.notification.bean.MonthlyAttendanceBean.ThisMonth;
import com.notification.bean.MessageDetailBean;
import com.service.beans.MonthlyAttendance;

public class NotifyTest {
	public static void main(String[] args) {
		System.out.println("Message......");
		List<MessageDetailBean> detailBeans = new ArrayList<MessageDetailBean>();
		for(int i =0; i<15; i++){
			MessageDetailBean messageDetailBean = new MessageDetailBean();
			messageDetailBean.setMessageTypeEmail(true);
			if(i == 1){
				messageDetailBean.setStudentEmail("sarafdarpundlik@gmail.com");
				messageDetailBean.setParentEmail("sarafdarpundlik@gmail.com");
			}
			if(i == 2){
				messageDetailBean.setStudentEmail("er.vinayakpatil@gmail.com");
				messageDetailBean.setParentEmail("er.vinayakpatil@gmail.com");
			}	
			if(i == 3){
				messageDetailBean.setStudentEmail("surajankush@gmail.com");
				messageDetailBean.setParentEmail("surajankush@gmail.com");
			}
			
			messageDetailBean.setMessageTypePush(false);
			messageDetailBean.setStudentPhoneId("100");
			messageDetailBean.setStudentPhoneType("Android");
			
			MonthlyAttendanceBean attendanceBean = new MonthlyAttendanceBean();
			
			List<ThisMonth> thisMonths = new ArrayList<ThisMonth>();
			for(int index=0; index<31;index++){
				MonthlyAttendanceBean.ThisMonth month = new ThisMonth();
				month.setDate(index+1+"-12-2016");
				if(index%10==0){
					month.setAbsentOrPresent("Absent");
				}else{
					month.setAbsentOrPresent("Present");
				}
				thisMonths.add(month);
			}
			attendanceBean.setThisMonths(thisMonths);
			attendanceBean.setMontj("Dec-2015");
			attendanceBean.setName("Vianayak");
			messageDetailBean.setEmailObject(attendanceBean);
			messageDetailBean.setEmailTemplate("attendance.tmpl");
			
			messageDetailBean.setMessageTypeSms(false);
			
			messageDetailBean.setStudentPhone(1000);
			
			messageDetailBean.setSendToStudent(true);
			messageDetailBean.setFrom("My test class");
			/***************************************/
			messageDetailBean.setMessageTypeEmail(true);
			//messageDetailBean.setParentEmail("student@gmail.com");
			
			messageDetailBean.setMessageTypePush(false);
			messageDetailBean.setParentPhoneId("100");
			messageDetailBean.setParentPhoneType("Android");
			
			messageDetailBean.setMessageTypeSms(false);
			messageDetailBean.setParentPhone(1000);
			
			messageDetailBean.setSendToParent(true);
			//messageDetailBean.setEmailMessage("hello....");
			
			
			/*******************************************/
			messageDetailBean.setStudentId(i);
			
			detailBeans.add(messageDetailBean);
		}
		
		NotifcationAccess notifcationAccess = new NotifcationAccess();
		
		notifcationAccess.send(detailBeans);
	}
}
