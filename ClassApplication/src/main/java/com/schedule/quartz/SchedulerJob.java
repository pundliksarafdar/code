package com.schedule.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.classapp.db.notificationpkg.NotificationDB;
import com.classapp.db.register.RegisterDB;
import com.classapp.db.Schedule.ScheduleDB;
import com.classapp.logger.AppLogger;

public class SchedulerJob implements Job {
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		NotificationDB notificationDB=new NotificationDB();
		notificationDB.deleteNotifications();
		ScheduleDB scheduleDB=new ScheduleDB();
		scheduleDB.deleteScheduler();
		RegisterDB registerDB=new RegisterDB();
		registerDB.updateScheduler();
		AppLogger.logger("Struts 2.3.4 + Quartz 2.1.5");

	}
}