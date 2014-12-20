package com.transaction.register;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.classapp.db.Schedule.Schedule;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterDB;
import com.classapp.db.register.RegisterUser;
import com.classapp.db.student.Student;

public class RegisterTransaction {

	public List getTeacherName(List TeacherIDs) {
		RegisterDB db=new RegisterDB();
		List list=db.getTeacherName(TeacherIDs);
		return list;
	}
	
	public List<RegisterBean> getScheduleTeacher(List<Schedule> list) {
		List<RegisterBean> teachers=new ArrayList<RegisterBean>();
		int i=0;
		RegisterDB db=new RegisterDB();
		while(i<list.size())
		{	
			RegisterBean bean=db.getscheduleTeacher(list.get(i).getTeacher_id());
			teachers.add(bean);
			i++;
		}
		
	
		return teachers;
	}

	public List<RegisterBean> getclassNames(List<Student> list) {
		RegisterDB registerDB=new RegisterDB();
		int counter=0;
		List<RegisterBean> registerBeans=new ArrayList<RegisterBean>();
		while(list.size()>counter)
		{
			RegisterBean bean=registerDB.getRegisterclass(list.get(counter).getClass_id());
			registerBeans.add(bean);
			counter++;
		}
		return registerBeans;
	}
	
	public List getStudentsInfo(List StudentIDs,int pagenumber,int resultPerPage) {
		RegisterDB db=new RegisterDB();
		List list=db.getStudentInfo(StudentIDs,pagenumber,resultPerPage);
		return list;
	}
	
	public List<RegisterBean> getTeachersclassNames(List classids) {
		RegisterDB registerDB=new RegisterDB();
		int counter=0;
		List<RegisterBean> registerBeans=new ArrayList<RegisterBean>();
		registerBeans=registerDB.getTeachersClassName(classids);
		return registerBeans;
	}
	
	public boolean isMobileExits(String mobile){
		RegisterDB registerDB = new RegisterDB();
		boolean registered = registerDB.isMobileExists(mobile);
		return registered;
	}
	
	public boolean isUserExits(String loginName){
		RegisterDB registerDB = new RegisterDB();
		boolean registered = registerDB.isUserExits(loginName);
		return registered;
	}
	
	public boolean updateUser(RegisterBean registerBean,Integer regId){
		RegisterDB registerDB = new RegisterDB();
		registerBean.setDob(registerBean.getDob().replace("-", ""));
		registerBean.setClassName(null==registerBean.getClassName()?"":registerBean.getClassName());
		registerBean.setLoginPass(null==registerBean.getLoginPass()?registerBean.getLoginPass():registerBean.getLoginPass());
		return registerDB.updateUser(registerBean, regId);
	}
	
	public String registerUser(String registerRequest,String username,String mobileNo){
		RegisterUser registerUser = new RegisterUser();
		
		if (isUserExits(username)) {
			return "User already registered";
		} else if (isMobileExits(mobileNo)) {
			return "Mobile number already registered";
		} else {
			String status = registerUser.registerUser(registerRequest);
			return status;
		}
	}
}
