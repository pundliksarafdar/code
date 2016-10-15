package com.customrole;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.attendance.AttendanceDB;
import com.classapp.db.batch.division.Division;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationBean;
import com.classapp.db.classOwnerSettings.ClassOwnerNotificationDb;
import com.classapp.db.fees.FeesDB;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterDB;
import com.config.BaseAction;
import com.notification.access.NotifcationAccess;
import com.notification.bean.MessageDetailBean;
import com.service.beans.StudentAttendanceNotificationData;
import com.service.beans.StudentFessNotificationData;
import com.transaction.attendance.AttendanceTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.user.UserBean;

public class AttendanceAction extends BaseAction{

	private List<Division> divisions;

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		DivisionTransactions transactions = new DivisionTransactions();
		divisions = transactions.getAllDivisions(userBean.getInst_id());
		AttendanceTransaction attendanceTransaction =new AttendanceTransaction();
		return SUCCESS;
	}

	public List<Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}
	
	

}
