package com.schedule.listner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.schedule.quartz.DailyScheduler;
import com.schedule.quartz.SchedulerJob;

public class QuartzSchedulerListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		//
	}

	public void contextInitialized(ServletContextEvent arg0) {

		JobDetail job = JobBuilder.newJob(SchedulerJob.class)
				.withIdentity("anyJobName", "group1").build();
		JobDetail dailyJob = JobBuilder.newJob(DailyScheduler.class)
				.withIdentity("anyJobName", "dailyGroup").build();
		JobDetail weeklyJob = JobBuilder.newJob(DailyScheduler.class)
				.withIdentity("anyJobName", "weeklyGroup").build();
		JobDetail monthlyJob = JobBuilder.newJob(DailyScheduler.class)
				.withIdentity("anyJobName", "monthlyGroup").build();
		try {

			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity("anyTriggerName", "group1")
					.withSchedule(
							CronScheduleBuilder.cronSchedule("0 00 0 ? * SAT"))
					.build();

			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			
			Trigger dailyTrigger = TriggerBuilder
					.newTrigger()
					.withIdentity("anyTriggerName", "dailyGroup")
					.withSchedule(
							CronScheduleBuilder.cronSchedule("0 00 6 * * ?"))
					.build();
			Scheduler dailyScheduler = new StdSchedulerFactory().getScheduler();
			dailyScheduler.start();
			dailyScheduler.scheduleJob(dailyJob, dailyTrigger);
			
			Trigger weeklyTrigger = TriggerBuilder
					.newTrigger()
					.withIdentity("anyTriggerName", "weeklyGroup")
					.withSchedule(
							CronScheduleBuilder.cronSchedule("0 00 0 ? * SUN"))
					.build();
			Scheduler weeklyScheduler = new StdSchedulerFactory().getScheduler();
			weeklyScheduler.start();
			weeklyScheduler.scheduleJob(weeklyJob, weeklyTrigger);
			
			Trigger monthlyTrigger = TriggerBuilder
					.newTrigger()
					.withIdentity("anyTriggerName", "monthlyGroup")
					.withSchedule(
							CronScheduleBuilder.cronSchedule("0 00 0 1 * ?"))
					.build();
			Scheduler monthlyScheduler = new StdSchedulerFactory().getScheduler();
			monthlyScheduler.start();
			monthlyScheduler.scheduleJob(monthlyJob, monthlyTrigger);

		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}
}