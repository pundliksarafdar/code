package com.signon;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.classapp.db.login.LoginCheck;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.config.BaseAction;
import com.config.Constants;
import com.google.gson.Gson;
import com.tranaction.login.login;
import com.transaction.batch.BatchTransactions;
import com.transaction.register.RegisterTransaction;
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
						session.put(Constants.BATCHCOUNT, batchcount);
						session.put(Constants.TEACHERCOUNT, teachercount);
						session.put(Constants.STUDENTCOUNT, studentcount);
						return Constants.CLASSOWNER;
					} else if ((null != userBean.getRole())
							&& 2 == userBean.getRole()) {
						TeacherTransaction teacherTransaction=new TeacherTransaction();
						List classids=teacherTransaction.getTeachersClass(userBean.getRegId());
						RegisterTransaction registerTransaction=new RegisterTransaction();
						List<RegisterBean> classbeanes=registerTransaction.getTeachersclassNames(classids);
						session.put("classes", classbeanes);
						return Constants.CLASSTEACHER;
					} else if ((null != userBean.getRole())
							&& 3 == userBean.getRole()) {
						StudentTransaction studentTransaction=new StudentTransaction();
						List<Student> list=studentTransaction.getStudent(userBean.getRegId());
						RegisterTransaction registerTransaction=new RegisterTransaction();
						List<RegisterBean> beans= registerTransaction.getclassNames(list);
						session.put("classes", beans);
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
}