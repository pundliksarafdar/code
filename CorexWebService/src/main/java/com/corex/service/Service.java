package com.corex.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import com.classapp.login.LoginBeanMobile;
import com.classapp.login.UserBean;
import com.classapp.schedule.Schedule;
import com.classapp.schedule.ScheduleBean;
import com.corex.iservice.IService;
import com.corex.requestbean.IRequest;
import com.corex.requestbean.LoginBean;
import com.corex.responsebean.IResponse;
import com.corex.responsebean.LoginResponse;
import com.corex.responsebean.ScheduleResponse;
import com.tranaction.login.login;
import com.transaction.schedule.ScheduleTransaction;

public class Service implements IService {
	@Override
	@GET
	@Path("/test")
	public String test() {

		return "Service is On";
	}

	@Override
	@POST
	@Path("/login")
	public LoginResponse login(IRequest request) {
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setCode("000", "success");
		com.tranaction.login.login login = new login();
		LoginBeanMobile beanMobile = request.getLoginBeanMobile();
		UserBean userBean = new UserBean();
		login.loadBean(userBean, beanMobile);
		loginResponse.setUserBean(userBean);
		
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		HashMap<String, List> studentData = scheduleTransaction.getStudentData(userBean
				.getRegId());
		
		loginResponse.setStudentScheduleData(studentData);
		return loginResponse;
	}

	@Override
	@POST
	@Path("/getscheduledate")
	@Consumes("application/json")
	@Produces("application/json")
	public ScheduleResponse getScheduleByDate(IRequest request) {
		ScheduleResponse response = new ScheduleResponse();
		List<Schedule> scheduleData = null;
		HashMap<String, List> studentData = null;

		Object dateObj =  request.getObject();
		String date = (String) dateObj;
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		LoginBeanMobile beanMobile = request.getLoginBeanMobile();
		UserBean userBean = new UserBean();
		com.tranaction.login.login login = new login();
		login.loadBean(userBean, beanMobile);
		
		if(null!=userBean.getRole()){
		if (userBean.getRole() == 3) {
			if (null != userBean) {
				scheduleData = scheduleTransaction.getScheduleForDate(
						userBean.getRegId(), date);
			}
			studentData = scheduleTransaction.getStudentData(userBean
					.getRegId());

			ScheduleBean scheduleBean = new ScheduleBean();
			scheduleBean.setSchedule(scheduleData);
			scheduleBean.setStudentScheduleData(studentData);

			response.setCode("000", "success");
			response.setScheduleBean(scheduleBean);
		} else {
			response.setCode("002", "Invalid role");
		}
		}else{
			response.setCode("001", "Invalid credentials");
		}
		return response;
	}
}
