package com.util;

public class NotificationEnum{
	public static enum MessageType {
		SMS,EMAIL,PUSH
	}
	
	public static enum MessageTo{
		STUDENT,PARENT,BOTH
	}
	
	public static enum MessageCategery{
		FEE_ON_PAYMENT,
		FEE_PAYMENT_DUE,
		ATTENDANCE_DAILY,
		ATTENDANCE_WEELY,
		ATTENDANCE_MONTHLY,
		TIMETABLE_NEW,
		TIMETABLE_UPDATED,
		PROGRESS_CARD_MANUAL,
		PROGRESS_CARD_ON_EVERY_EXAM
	}
}


