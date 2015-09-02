package com.schedule.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.classapp.db.notificationpkg.NotificationDB;

public class SchedulerJob implements Job {
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		NotificationDB notificationDB=new NotificationDB();
		notificationDB.deleteNotifications();
		
		System.out.println("Struts 2.3.4 + Quartz 2.1.5");

	}
}