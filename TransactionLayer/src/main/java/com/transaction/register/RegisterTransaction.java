package com.transaction.register;

import java.util.List;

import com.classapp.db.register.RegisterDB;

public class RegisterTransaction {

	public List getTeacherName(List TeacherIDs) {
		RegisterDB db=new RegisterDB();
		List list=db.getTeacherName(TeacherIDs);
		return list;
	}
}
