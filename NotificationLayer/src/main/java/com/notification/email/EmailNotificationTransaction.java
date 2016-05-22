package com.notification.email;

import java.util.ArrayList;
import java.util.List;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDetails;
import com.notification.access.MessageFormatter;
import com.transaction.student.StudentTransaction;
import com.transaction.teacher.TeacherTransaction;

public class EmailNotificationTransaction {
	public void emailNotificationToParent(Integer batchId,Integer divId,Integer instId,String message,String subject,String from){
		StudentTransaction studentTransaction = new StudentTransaction();
		List<Student> parentData = studentTransaction.getStudentsrelatedtobatch(batchId+"", instId, divId);
		List<String>emailIds = new ArrayList<String>();
		for(Student student:parentData){
			String email = student.getParentEmail();
			if(email!=null && email.trim().length()>0){
				emailIds.add(email);
				System.out.println(email);
			}
		}
		sendEmail(emailIds, message, subject, from);
	}
	
	public void emailNotificationToStudent(Integer batchId,Integer divId,Integer instId,String message,String subject,String from){
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentDetails> studentData = studentTransaction.getAllStudentsDetails(batchId+"", divId+"" ,instId);
		List<String>emailIds = new ArrayList<String>();
		for(StudentDetails student:studentData){
			String email = student.getStudentUserBean().getEmail();
			if(email!=null && email.trim().length()>0){
				emailIds.add(email);
				System.out.println("Student email......."+email);
			}
		}
		sendEmail(emailIds, message, subject, from);
	}
	
	public void emailNotificationToTeacher(List teacherIds,String message,String subject,String from){
		TeacherTransaction teacherTransaction = new TeacherTransaction();
		List<RegisterBean> registerBeans = teacherTransaction.getTeacherDetail(teacherIds);
		List<String>emailIds = new ArrayList<String>();
		for(RegisterBean registerBean:registerBeans){
			String email = registerBean.getEmail();
			if(email!=null && email.trim().length()>0){
				emailIds.add(email);
				System.out.println(email);
			}
		}
		System.out.println(emailIds);
		sendEmail(emailIds, message,subject,from);
	}
	
	private void sendEmail(List<String>emails,String message,String subject,String from){
		final EmailNotification inotify = new EmailNotification();
		for(String email:emails){
			new Thread(new Runnable() {
				@Override
				public void run() {
					MessageFormatter formatter = new MessageFormatter();
					inotify.sendEmail(email, message, subject, from);
				}
			}).start();
		}
	}
}
