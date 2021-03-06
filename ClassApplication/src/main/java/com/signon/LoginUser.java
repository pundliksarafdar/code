package com.signon;

import java.util.ArrayList;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.classapp.db.login.LoginCheck;
import com.classapp.db.notificationpkg.Notification;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.login.UserStatic;
import com.classapp.schedule.Scheduledata;
import com.config.BaseAction;
import com.config.Constants;
import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.LUSHR;
import com.tranaction.login.login;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
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
		String forward = null;
		if(null != loginBean){
			userBean.setLoginBean(loginBean);
		    forward = loadBean(userBean, loginBean,response,session);
		}if(null == userBean.getRegId()){
			addActionError("Invalid Username/Password");
			forward = ERROR;
		}else{
			loginBean = userBean.getLoginBean();
			if(null != loginBean){
				userBean.setLoginBean(loginBean);
			    forward = loadBean(userBean, loginBean,response,session);
			    loadUserStaticData(userBean,session);
			}else{
				forward = ERROR;
			}
		}
		return forward;
	}
	
	public String loadBean(UserBean userBean,LoginBean loginBean,HttpServletResponse response,Map<String, Object> session){
		login loginCheck=new login();
		String userBeanJson = loginCheck.loginck(loginBean.getLoginname(), loginBean.getLoginpass());
		if(null!=userBeanJson){
			Gson gson = new Gson();
			userBean.setAddr1( gson.fromJson(userBeanJson, UserBean.class).getAddr1());
			userBean.setAddr2( gson.fromJson(userBeanJson, UserBean.class).getAddr2());
			userBean.setCity( gson.fromJson(userBeanJson, UserBean.class).getCity());
			userBean.setCountry( gson.fromJson(userBeanJson, UserBean.class).getCountry());
			userBean.setDob( gson.fromJson(userBeanJson, UserBean.class).getDob());
			userBean.setFirstname( gson.fromJson(userBeanJson, UserBean.class).getFirstname());
			userBean.setLastname( gson.fromJson(userBeanJson, UserBean.class).getLastname());
			userBean.setMiddlename(gson.fromJson(userBeanJson, UserBean.class).getMiddlename());
			userBean.setPhone1(gson.fromJson(userBeanJson, UserBean.class).getPhone1());
			userBean.setPhone2(gson.fromJson(userBeanJson, UserBean.class).getPhone2());
			userBean.setRole(gson.fromJson(userBeanJson, UserBean.class).getRole());
			userBean.setState(gson.fromJson(userBeanJson, UserBean.class).getState());
			userBean.setUsername(gson.fromJson(userBeanJson, UserBean.class).getUsername());
			userBean.setStartdate(gson.fromJson(userBeanJson, UserBean.class).getStartdate());
			userBean.setEnddate(gson.fromJson(userBeanJson, UserBean.class).getEnddate());
			userBean.setRegId(gson.fromJson(userBeanJson, UserBean.class).getRegId());
			userBean.setActivationcode(gson.fromJson(userBeanJson, UserBean.class).getActivationcode());
			userBean.setStatus(gson.fromJson(userBeanJson, UserBean.class).getStatus());
			userBean.setEmail(gson.fromJson(userBeanJson, UserBean.class).getEmail());
			userBean.setClassName(gson.fromJson(userBeanJson, UserBean.class).getClassName());
			userBean.setLoginBean(loginBean);

			
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
						List<RegisterBean> classbeanes=registerTransaction.getTeachersclassNames(classids);
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
		
		double totalStorage = 150.0; //set to default
		double examSize = getFolderSize(new File(storagePath+File.separator+"exam"))/(1024.0*1024.0);
		double noteSize = getFolderSize(new File(storagePath+File.separator+"notes"))/(1024.0*1024.0);
		double usedSize = examSize+noteSize;
		
		userStatic.setTotalStorage(totalStorage);
		userStatic.setUsedSpace(usedSize);
		userStatic.setRemainingSpace(totalStorage-usedSize);
		userStatic.setExamSpace(examSize);
		
		userStatic.setNotesSpace(noteSize);
		//session.put(Constants.USER_STATIC, userStatic);
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