package com.transaction.register;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterDB;
import com.classapp.db.register.RegisterUser;
import com.classapp.db.schedule.Schedule;
import com.classapp.db.student.Student;
import com.classapp.persistence.Constants;
import com.classapp.servicetable.ServiceMap;

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
		registerDB.updateScheduler();
		int counter=0;
		List<RegisterBean> registerBeans=new ArrayList<RegisterBean>();
		while(list.size()>counter)
		{
			RegisterBean bean=registerDB.getRegisterclass(list.get(counter).getClass_id());
			if(bean!=null){
				if(!"disabled".equals(bean.getInst_status())){
				registerBeans.add(bean);
				}
			}
			counter++;
		}
		return registerBeans;
	}
	
	public List getStudentsInfo(List StudentIDs,int resultPerPage) {
		RegisterDB db=new RegisterDB();
		List list=db.getStudentInfo(StudentIDs,resultPerPage);
		return list;
	}
	public List getStudents(List StudentIDs) {
		RegisterDB db=new RegisterDB();
		List list=db.getStudents(StudentIDs);
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
	
	public String registerUser(String registerRequest,String username,String mobileNo,String email){
		RegisterUser registerUser = new RegisterUser();
		
		/*if debugging or testing user are able to use same email id*/
		String isDebugging = ServiceMap.getSystemParam(Constants.DEBUGGING_MODE, "isdebug");
		boolean isDebuggingBool = null!=isDebugging && isDebugging.equals("yes");
		if (isUserExits(username)) {
			return "User already registered";
		} else if (isMobileExits(mobileNo)) {
			return "Mobile number already registered";
		}else if (isEmailExists(email) && !isDebuggingBool) {
			return "Email ID already registered";	
		}else {
			String status = registerUser.registerUser(registerRequest);
			return status;
		}
	}
	
	public String registerUser(RegisterBean registerRequest){
		RegisterUser registerUser=new RegisterUser();
		return registerUser.registerUser(registerRequest);
	}
	
	public boolean isEmailAndMobileValid(String email,String phone) {
		RegisterDB registerDB=new RegisterDB();
		return registerDB.isEmailAndMobileValid(email, phone);
	}
	
	public boolean ActivationCodeValidation(int regID,String code) {
		RegisterDB registerDB=new RegisterDB();
		return registerDB.ActivationCodeValidation(regID, code);
		
	}
	
	public void removeActivationCode(int regID) {
		RegisterDB registerDB=new RegisterDB();
		registerDB.removeActivationCode(regID);
	}
	public String getPassword(String email,String phone) {
		RegisterDB registerDB=new RegisterDB();
		return registerDB.getPassword(email, phone);
		
	}
	public void resetpassword(int regID,String password) {
		RegisterDB db=new RegisterDB();
		db.resetpassword(regID, password);
	}
	public boolean isEmailExists(String email) {
		RegisterDB db=new RegisterDB();
		return db.isEmailExists(email);
		
	}
	
	public List<RegisterBean> getAllLogins(){
		List<RegisterBean> registerBeans;
		RegisterDB registerDB = new RegisterDB();
		registerBeans = registerDB.getAllRegisters();
		
		return registerBeans;
		
	}
	
	public RegisterBean getregistereduser(int regID) {
		RegisterDB registerDB=new RegisterDB();
		return registerDB.getRegistereduser(regID);
	}
	
	public RegisterBean getInstitute(String loginName){
		RegisterDB registerDB=new RegisterDB();
		return registerDB.getInstitute(loginName);
	}
	
	public boolean updateInstituteStatus(int regId,String inst_status) {
		RegisterDB registerDB=new RegisterDB();
		return registerDB.updateInstituteStatus(regId, inst_status);
	}
	
	public boolean updateRenewalDates(int regId) {
		RegisterDB registerDB=new RegisterDB();
		return registerDB.updateRenewalDates(regId);
	}
	
	public RegisterBean getRegisteredTeacher(String username,String email) {
		RegisterDB registerDB=new RegisterDB();
		return registerDB.getRegisteredTeacher(username, email);
	}
	
	public RegisterBean getRegisteredUserByLoginID(String username) {
		RegisterDB registerDB=new RegisterDB();
		return registerDB.getRegisteredUserByLoginID(username);
	}
	
	public List<String> getNamesForSuggestion(int inst_id) {
		RegisterDB registerDB=new RegisterDB();
		return registerDB.getNamesForSuggestion(inst_id);
	}
	
	public List<RegisterBean> getStudentByName(int inst_id,String fname,String lname) {
		RegisterDB registerDB=new RegisterDB();
		return registerDB.getStudentByNames(inst_id, fname, lname);
	}
	}
	

