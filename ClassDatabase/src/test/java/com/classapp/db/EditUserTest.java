package com.classapp.db;

import com.classapp.db.register.RegisterBean;
import com.classapp.edit.EditUser;
import com.google.gson.Gson;

public class EditUserTest {
	public static void main(String[] args){
		EditUser editUser = new EditUser();
		RegisterBean registerBean = new RegisterBean();
		registerBean.setRegId(34);
		registerBean.setLoginName("nologin");
		registerBean.setLoginPass("passwd");
		registerBean.setFname("Mukesh");
		registerBean.setMname("Bhagtram");
		registerBean.setLname("Sarafdar");
		registerBean.setAddr1("adr1");
		registerBean.setAddr2("adr2");
		registerBean.setCity("cty");
		registerBean.setClassName("new");
		registerBean.setCountry("india");
		registerBean.setDob("19892211");
		registerBean.setPhone1("ph1");
		registerBean.setPhone2("ph2");
		registerBean.setState("state");
		Gson gson = new Gson();
		String josnStr = gson.toJson(registerBean);
		editUser.editUser(josnStr);
	}
	
}
