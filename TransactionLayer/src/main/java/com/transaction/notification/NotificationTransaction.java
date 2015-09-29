package com.transaction.notification;

import java.util.ArrayList;
import java.util.List;

import com.classapp.db.notificationpkg.*;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.transaction.batch.BatchTransactions;

public class NotificationTransaction {
	public Boolean add(Notification notification) {
		NotificationDB db=new NotificationDB();
		db.add(notification);
		return  true;
	
	}
	
	public List<Notification> getMessageforStudent(Student student) {
		NotificationDB db=new NotificationDB();
		List<String> batchids=new ArrayList<String>();
		String[] ids=student.getBatch_id().split(",");
		for (int i = 0; i < ids.length; i++) {
			batchids.add(ids[i]);
		}
		return db.getMessageforStudent(student.getClass_id(), batchids,student.getDiv_id());
		  
	
	}
	
	public List<Notification> getMessageforOwner(int institute_id) {
		NotificationDB db=new NotificationDB();
		return db.getMessageforOwner(institute_id);
		  
	
	}
	
	public List<Notification> getMessageforTeacher(int institute_id) {
		NotificationDB db=new NotificationDB();
		return db.getMessageforTeachers(institute_id);
		  
	
	}
	
}
