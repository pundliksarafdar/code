package com.signon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.institutestats.InstituteStats;
import com.classapp.db.login.LoginCheck;
import com.classapp.db.notificationpkg.Notification;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.login.UserStatic;
import com.classapp.schedule.Scheduledata;
import com.config.BaseAction;
import com.config.Constants;
import com.google.gson.Gson;
import com.service.beans.ClassownerSettingsNotification;
import com.sun.org.apache.bcel.internal.generic.LUSHR;
import com.tranaction.login.login;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.classownersettingtransaction.ClassownerSettingstransaction;
import com.transaction.institutestats.InstituteStatTransaction;
import com.transaction.notification.Data;
import com.transaction.notification.NotificationTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.schedule.ScheduleTransaction;
import com.transaction.student.StudentTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class LoginUser extends BaseAction{
	
	private static final long serialVersionUID = 1L;
	private LoginBean loginBean;
	
	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		String forward = "";
		//when login is hit by homepage link no login bean is available then redirect to login page without error message
		//in this case userbean is not having regestration id
		if(null == loginBean && null==userBean.getRegId()){
			forward = ERROR;
		}else if(loginBean!=null && (loginBean.getLoginname().isEmpty()|| loginBean.getLoginpass().isEmpty())){
			//login bean null check is alrady done so it will not go to next else loop
			//If loginname or password is empty send login page
			forward = ERROR;
		}else{
			//login bean is available need to validate
			//First check is userBean is available
			//If register id is available then user bean is available and action is hit by reload login
			if(null!=userBean.getRegId()){
				forward = loadBean(userBean, loginBean, response, session);
				Cookie cookie = new Cookie("logincreation", "loggedin");
				response.addCookie(cookie);
			}else{
				//if register id is not avialable then check for login
				forward = loadBean(userBean, loginBean, response, session);
				Cookie cookie = new Cookie("logincreation", "loggedin");
				response.addCookie(cookie);
				if(!forward.equalsIgnoreCase(ERROR) && !validateTime(userBean)){
					forward = ERROR;
				}
			}
			
			if(!forward.equals(ERROR)){
				//If user is classowner then add institute statistics.
				if(userBean.getRole()==1){
					InstituteStatTransaction transaction = new InstituteStatTransaction();
					InstituteStats instituteStats = transaction.getStats(userBean.getRegId());
					request.setAttribute("instituteStats", instituteStats);
				}

			}
			
		}
		
		if(forward.equalsIgnoreCase(ERROR)){
			addActionError("Please enter correct login name or password");
		}
		return forward;
	}
	
	/*Method to check validation*/
	private boolean validateTime(UserBean userBean){
		Date formDate = new Date(loginBean.getLastLogin());
	    long lastdateLong = 0;
	    
		if(null!=userBean.getLastlogin()){
	    	lastdateLong = userBean.getLastlogin().getTime();
	    }
	    long formDateLong = formDate.getTime();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String dateFormatted = dateFormat.format(formDate);
	    try {
			formDateLong = dateFormat.parse(dateFormatted).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    boolean isValid = formDateLong > lastdateLong;
		if(isValid){
		    LoginCheck loginCheck = new LoginCheck();
    		loginCheck.setLastLogin(userBean.getRegId(), formDate);
	    }
		
		//Need to check login
		
		isValid = true;
		return isValid;
	}
	public String loadBean(UserBean userBean,LoginBean loginBean,HttpServletResponse response,Map<String, Object> session){
		login loginCheck=new login();
		if(null!=loginBean){
			com.classapp.login.UserBean userBeanLg = loginCheck.loginck(loginBean.getLoginname(), loginBean.getLoginpass());
			if(null!=userBeanLg){
				try {
					BeanUtils.copyProperties(userBean, userBeanLg);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}else{
				return ERROR;
			}
		}
		
		if(userBean!=null){
			loadUserStaticData(userBean, session);
			//Check for acceptance
			/*
			if(null != userBean.getRole() && 0 != userBean.getRole() && 10 != userBean.getRole()){
				if(null!=userBean.getStartdate()){
					return SUCCESS;
				}else{
					return Constants.UNACCEPTED;
				}
			}
			*/
			
			if (null != userBean.getRole() && 0 != userBean.getRole()
					&& 10 != userBean.getRole()) {
				if(!"".equals(userBean.getActivationcode()) && !"E".equals(userBean.getStatus())){
					return Constants.ACTIVATION;
				}else if(userBean.getStatus()!=null){
					if(userBean.getStatus().equals("F") || userBean.getStatus().equals("M"))
					return Constants.RESET_PASSWORD;
					if(userBean.getStatus().equals("E"))
						return Constants.REGISTER_EMAIL;
				}
				//if (null != userBean.getStartdate()) {
					
					if (null != userBean.getRole() && 9 < userBean.getRole()) {
						return Constants.ACCESSBLOCKED;
					}
					if ((null != userBean.getRole()) && 0 == userBean.getRole()) {
						return SUCCESS;
					} else if ((null != userBean.getRole())
							&& 1 == userBean.getRole()) {
						TeacherTransaction teacherTransaction=new TeacherTransaction();
						int teachercount=teacherTransaction.getTeacherCount(userBean.getRegId());
						StudentTransaction studentTransaction=new StudentTransaction();
						int studentcount=studentTransaction.getStudentCount(userBean.getRegId());
						BatchTransactions batchTransactions=new BatchTransactions();
						int batchcount=batchTransactions.getBatchCount(userBean.getRegId());
						NotificationTransaction notificationTransaction=new NotificationTransaction();
						List<Notification> notifications=notificationTransaction.getMessageforOwner(userBean.getRegId());
						session.put("notifications", notifications);
						session.put(Constants.BATCHCOUNT, batchcount);
						session.put(Constants.TEACHERCOUNT, teachercount);
						session.put(Constants.STUDENTCOUNT, studentcount);
						return Constants.CLASSOWNER;
					} else if ((null != userBean.getRole())
							&& 2 == userBean.getRole()) {
						TeacherTransaction teacherTransaction=new TeacherTransaction();
						List<Integer> classids=teacherTransaction.getTeachersClass(userBean.getRegId());
						List<Notification> notifications = new ArrayList<Notification>();
						List<RegisterBean> classbeanes = new ArrayList<RegisterBean>();
						Map<String, List<Scheduledata>> map=new HashMap<String, List<Scheduledata>>();
						if(classids.size()>0){
						RegisterTransaction registerTransaction=new RegisterTransaction();
						classbeanes =registerTransaction.getTeachersInstitutes(classids);
						ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
						List<Scheduledata> scheduledatas=scheduleTransaction.getteacherstodaysSchedule(classids, userBean.getRegId());
						List<String> divisionNames=new ArrayList<String>();
						DivisionTransactions divisionTransactions=new DivisionTransactions();
						if(scheduledatas!=null){
							if(scheduledatas.size()>0){
							for (int i = 0; i < classids.size(); i++) {
								List<Scheduledata> data=new ArrayList<Scheduledata>();
								for (int j = 0; j < scheduledatas.size(); j++) {
									if(classids.get(i)==scheduledatas.get(j).getInst_id()){
										data.add(scheduledatas.get(j));
									}
								}
								if(data.size()>0){
								map.put(data.get(0).getInst_name(),data);
								}
							}
							}
						}
						
						NotificationTransaction notificationTransaction=new NotificationTransaction();
						for (int i = 0; i < classids.size(); i++) {
							List<Notification> notificationsList=  notificationTransaction.getMessageforTeacher(classids.get(i));
							if(notificationsList!=null){
								for (int j = 0; j < notificationsList.size(); j++) {
									notifications.add(notificationsList.get(j));
								}
							}
						}
						}
						session.put("notifications", notifications);
						session.put("classes", classbeanes);
						session.put("todayslect", map);
						return Constants.CLASSTEACHER;
					} else if ((null != userBean.getRole())
							&& 3 == userBean.getRole()) {
						StudentTransaction studentTransaction=new StudentTransaction();
						List<Student> list=studentTransaction.getStudent(userBean.getRegId());
						RegisterTransaction registerTransaction=new RegisterTransaction();
						List<RegisterBean> beans= registerTransaction.getclassNames(list);
						NotificationTransaction notificationTransaction=new NotificationTransaction();
						List<Notification> notifications=new ArrayList<Notification>();
						for (int i = 0; i < list.size(); i++) {
							List<Notification> notificationsList=  notificationTransaction.getMessageforStudent(list.get(i));
							if(notificationsList!=null){
								for (int j = 0; j < notificationsList.size(); j++) {
									notifications.add(notificationsList.get(j));
								}
							}
						}
						ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
						List<Scheduledata> scheduledatas=scheduleTransaction.gettodaysSchedule(list);
						Map<String, List<Scheduledata>> map=new HashMap<String, List<Scheduledata>>();
						if(scheduledatas!=null){
							if(scheduledatas.size()>0){
							for (int i = 0; i < beans.size(); i++) {
								List<Scheduledata> data=new ArrayList<Scheduledata>();
								for (int j = 0; j < scheduledatas.size(); j++) {
									if(beans.get(i).getRegId()==scheduledatas.get(j).getInst_id()){
										scheduledatas.get(j).setInst_name(beans.get(i).getClassName());
										data.add(scheduledatas.get(j));
									}
								}
								if(data.size()>0){
								map.put(beans.get(i).getClassName(),data);
								}
							}
							}
						}
						session.put("notifications", notifications);
						session.put("classes", beans);
						session.put("todayslect", map);
						return Constants.CLASSSTUDENT;
					} else {
						return ERROR;
					}
				/*} else {
					//return Constants.UNACCEPTED;
					return Constants.SUCCESS;
				}*/
			} else {
				return SUCCESS;
			}
		}else{
			return ERROR;
		}
	}
	
	public String loadUpdatedBean(UserBean userBean,LoginBean loginBean,HttpServletResponse response,Map<String, Object> session){
		login loginCheck=new login();
		
			com.classapp.login.UserBean userBeanLg = loginCheck.UpdatedBean(userBean.getUsername());
			if(null!=userBeanLg){
				try {
					BeanUtils.copyProperties(userBean, userBeanLg);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}else{
				return ERROR;
			}
		
		
		if(userBean!=null){
			loadUserStaticData(userBean, session);
			//Check for acceptance
			/*
			if(null != userBean.getRole() && 0 != userBean.getRole() && 10 != userBean.getRole()){
				if(null!=userBean.getStartdate()){
					return SUCCESS;
				}else{
					return Constants.UNACCEPTED;
				}
			}
			*/
			
			if (null != userBean.getRole() && 0 != userBean.getRole()
					&& 10 != userBean.getRole()) {
				if(!userBean.getActivationcode().equals("")){
					return Constants.ACTIVATION;
				}else if(userBean.getStatus()!=null){
					if(userBean.getStatus().equals("F"))
					return Constants.RESET_PASSWORD;
				}
				//if (null != userBean.getStartdate()) {
					
					if (null != userBean.getRole() && 9 < userBean.getRole()) {
						return Constants.ACCESSBLOCKED;
					}
					if ((null != userBean.getRole()) && 0 == userBean.getRole()) {
						return SUCCESS;
					} else if ((null != userBean.getRole())
							&& 1 == userBean.getRole()) {
						TeacherTransaction teacherTransaction=new TeacherTransaction();
						int teachercount=teacherTransaction.getTeacherCount(userBean.getRegId());
						StudentTransaction studentTransaction=new StudentTransaction();
						int studentcount=studentTransaction.getStudentCount(userBean.getRegId());
						BatchTransactions batchTransactions=new BatchTransactions();
						int batchcount=batchTransactions.getBatchCount(userBean.getRegId());
						NotificationTransaction notificationTransaction=new NotificationTransaction();
						List<Notification> notifications=notificationTransaction.getMessageforOwner(userBean.getRegId());
						session.put("notifications", notifications);
						session.put(Constants.BATCHCOUNT, batchcount);
						session.put(Constants.TEACHERCOUNT, teachercount);
						session.put(Constants.STUDENTCOUNT, studentcount);
						return Constants.CLASSOWNER;
					} else if ((null != userBean.getRole())
							&& 2 == userBean.getRole()) {
						TeacherTransaction teacherTransaction=new TeacherTransaction();
						List<Integer> classids=teacherTransaction.getTeachersClass(userBean.getRegId());
						RegisterTransaction registerTransaction=new RegisterTransaction();
						List<RegisterBean> classbeanes=registerTransaction.getTeachersInstitutes(classids);
						List<Notification> notifications=new ArrayList<Notification>();
						ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
						List<Scheduledata> scheduledatas=scheduleTransaction.getteacherstodaysSchedule(classids, userBean.getRegId());
						Map<String, List<Scheduledata>> map=new HashMap<String, List<Scheduledata>>();
						List<String> divisionNames=new ArrayList<String>();
						DivisionTransactions divisionTransactions=new DivisionTransactions();
						if(scheduledatas!=null){
							if(scheduledatas.size()>0){
							for (int i = 0; i < classids.size(); i++) {
								List<Scheduledata> data=new ArrayList<Scheduledata>();
								for (int j = 0; j < scheduledatas.size(); j++) {
									if(classids.get(i)==scheduledatas.get(j).getInst_id()){
										data.add(scheduledatas.get(j));
									}
								}
								if(data.size()>0){
								map.put(data.get(0).getInst_name(),data);
								}
							}
							}
						}
						
						NotificationTransaction notificationTransaction=new NotificationTransaction();
						for (int i = 0; i < classids.size(); i++) {
							List<Notification> notificationsList=  notificationTransaction.getMessageforTeacher(classids.get(i));
							if(notificationsList!=null){
								for (int j = 0; j < notificationsList.size(); j++) {
									notifications.add(notificationsList.get(j));
								}
							}
						}
						session.put("notifications", notifications);
						session.put("classes", classbeanes);
						session.put("todayslect", map);
						return Constants.CLASSTEACHER;
					} else if ((null != userBean.getRole())
							&& 3 == userBean.getRole()) {
						StudentTransaction studentTransaction=new StudentTransaction();
						List<Student> list=studentTransaction.getStudent(userBean.getRegId());
						RegisterTransaction registerTransaction=new RegisterTransaction();
						List<RegisterBean> beans= registerTransaction.getclassNames(list);
						NotificationTransaction notificationTransaction=new NotificationTransaction();
						List<Notification> notifications=new ArrayList<Notification>();
						for (int i = 0; i < list.size(); i++) {
							List<Notification> notificationsList=  notificationTransaction.getMessageforStudent(list.get(i));
							if(notificationsList!=null){
								for (int j = 0; j < notificationsList.size(); j++) {
									notifications.add(notificationsList.get(j));
								}
							}
						}
						ScheduleTransaction scheduleTransaction=new ScheduleTransaction();
						List<Scheduledata> scheduledatas=scheduleTransaction.gettodaysSchedule(list);
						Map<String, List<Scheduledata>> map=new HashMap<String, List<Scheduledata>>();
						if(scheduledatas!=null){
							if(scheduledatas.size()>0){
							for (int i = 0; i < beans.size(); i++) {
								List<Scheduledata> data=new ArrayList<Scheduledata>();
								for (int j = 0; j < scheduledatas.size(); j++) {
									if(beans.get(i).getRegId()==scheduledatas.get(j).getInst_id()){
										scheduledatas.get(j).setInst_name(beans.get(i).getClassName());
										data.add(scheduledatas.get(j));
									}
								}
								if(data.size()>0){
								map.put(beans.get(i).getClassName(),data);
								}
							}
							}
						}
						session.put("notifications", notifications);
						session.put("classes", beans);
						session.put("todayslect", map);
						return Constants.CLASSSTUDENT;
					} else {
						return ERROR;
					}
				/*} else {
					//return Constants.UNACCEPTED;
					return Constants.SUCCESS;
				}*/
			} else {
				return SUCCESS;
			}
		}else{
			return ERROR;
		}
	}
	
	public void loadUserStaticData(UserBean userBean, Map<String, Object> session){
		UserStatic userStatic = new UserStatic();
		String storagePath = Constants.STORAGE_PATH+File.separator+userBean.getRegId();
		userStatic.setStorageSpace(storagePath);
		
		boolean isSuccessFull = provisionDirectories(storagePath);
		
		if(!isSuccessFull){
			userStatic.getAlarms().add("Error cause while creating space");
		}
		
		double totalStorage = 100; //set to default
		/*double examSize = getFolderSize(new File(storagePath+File.separator+"exam"))/(1024.0*1024.0);
		double noteSize = getFolderSize(new File(storagePath+File.separator+"notes"))/(1024.0*1024.0);*/
		double usedSize = getFolderSize(new File(storagePath))/(1024.0*1024.0);
		/*userStatic.setExamSpace(examSize);
		
		userStatic.setNotesSpace(noteSize);*/
		//session.put(Constants.USER_STATIC, userStatic);
		if(userBean.getRole()== 1){
		ClassownerSettingstransaction settingstransaction = new ClassownerSettingstransaction();
		ClassownerSettingsNotification settings = settingstransaction.getSettings(userBean.getRegId());
		userStatic.setSettings(settings);
		totalStorage = settings.getInstituteStats().getAlloc_memory();
		}
		userStatic.setTotalStorage(totalStorage);
		userStatic.setUsedSpace(usedSize);
		userStatic.setRemainingSpace(totalStorage-usedSize);
		userBean.setUserStatic(userStatic);
		
		
	}
	
	public boolean provisionDirectories(String storagePath){
		boolean isSuccessFull = true;
		try{
			File storageData = new File(storagePath);
			if(!storageData.exists()){
				storageData.mkdir();
			}
			
			File storageNotes = new File(storagePath+File.separator+"notes");
			if(!storageNotes.exists()){
				storageNotes.mkdir();
			}
			
			File storageExam = new File(storagePath+File.separator+"exam");
			if(!storageExam.exists()){
				storageExam.mkdir();
			}
		}catch(Exception e){
			isSuccessFull = false;
			e.printStackTrace();
		}
		return isSuccessFull;
	}
	
	
	public long getFolderSize(File directory) {
	    long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += file.length();
	        else
	            length += getFolderSize(file);
	    }
	    return length;
	}
}