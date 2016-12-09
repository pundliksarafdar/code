package com.transaction.instroll;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.classapp.db.register.RegisterBean;
import com.classapp.db.register.RegisterUser;
import com.classapp.db.roll.CustomUser;
import com.classapp.db.roll.EditInstUserObject;
import com.classapp.db.roll.InstRollDB;
import com.classapp.db.roll.Inst_roll;
import com.classapp.db.roll.Inst_user;
import com.classapp.db.syllabusplanner.SyllabusPlannerDb;
import com.transaction.schedule.ScheduleTransaction;

public class InstRollTransaction {
	public boolean saveInstRoll(Inst_roll inst_roll) {
		InstRollDB db = new InstRollDB();
		if(db.validateRoll(inst_roll)){
		return db.saveInstRoll(inst_roll);
		}
		return false;
	}
	
	public List<Inst_roll> getInstituteRoles(int inst_id) {
		InstRollDB db = new InstRollDB();
		return db.getInstituteRoles(inst_id);
	}
	
	public Inst_roll getRole(int inst_id,int roll_id) {
		InstRollDB db = new InstRollDB();
		return db.getRole(inst_id,roll_id);
	}
	
	public boolean updateInstRoll(Inst_roll inst_roll) {
		InstRollDB db = new InstRollDB();
		if(db.validateUpdateRoll(inst_roll)){
		return db.updateRoll(inst_roll);
		}
		return false;
	}
	
	public List<CustomUser>  getInstUsers(int inst_id) {
		InstRollDB db = new InstRollDB();
		List list = db.getInstUsers(inst_id);
		List<CustomUser> customUserList = new ArrayList<CustomUser>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			CustomUser customUser = new CustomUser();
			customUser.setReg_id(((Number)object[0]).intValue());
			customUser.setFname((String)object[1]);
			customUser.setLname((String)object[2]);
			customUser.setRole_desc((String)object[3]);
			customUserList.add(customUser);
		}
		return customUserList;
	}
	
	public EditInstUserObject  getInstUsers(int inst_id,int user_id) {
		InstRollDB db = new InstRollDB();
		RegisterBean registerBean = db.getInstUserRegisterBean(inst_id, user_id);
		Inst_user inst_user = db.getInstUser(inst_id, user_id);
		EditInstUserObject userObject = new EditInstUserObject();
		userObject.setInst_user(inst_user);
		userObject.setRegisterBean(registerBean);
		return userObject;
	}
	
	public boolean updateInstituteUser(RegisterBean registerBean,String education,Date date){
		InstRollDB db = new InstRollDB();
		registerBean.setDob(registerBean.getDob().replace("-", ""));
		db.updateInstUserRegisterBean(registerBean);
		Inst_user user = new Inst_user();
		user.setEducation(education);
		user.setInst_id(registerBean.getInst_id());
		user.setJoining_date(date);
		user.setRole_id(registerBean.getInst_roll());
		user.setUser_id(registerBean.getRegId());
		db.saveInstUser(user);
		return true;
	}
	
	public boolean isCustomUserTeacher(int inst_id,int user_id) {
		InstRollDB db = new InstRollDB();
		return db.isCustomUserTeacher(inst_id,user_id);
	}
	
	public boolean deleteInstUser(int inst_id,int user_id) {
		ScheduleTransaction scheduleTransaction = new ScheduleTransaction();
		scheduleTransaction.updateScheduleOfTeacher(user_id, inst_id);
		SyllabusPlannerDb plannerDb = new SyllabusPlannerDb();
		plannerDb.deleteSyllabus(user_id, null, null, null, inst_id);
		InstRollDB db = new InstRollDB();
		db.deleteInstUser(inst_id, user_id);
		return true;
	}
}
