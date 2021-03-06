package com.timetable;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.config.BaseAction;
import com.transaction.batch.BatchTransactions;
import com.transaction.register.RegisterTransaction;
import com.transaction.student.StudentTransaction;
import com.user.UserBean;

public class StudentTimetableAction extends BaseAction {

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		
		BatchTransactions batchTransactions=new BatchTransactions();
		int regID=userBean.getRegId();
		StudentTransaction studentTransaction=new StudentTransaction();
		List<Student> list=studentTransaction.getStudent(regID);
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> beans= registerTransaction.getclassNames(list);
		request.setAttribute("Classes", beans);
		if(list!=null){
			request.setAttribute("studentdivision",list.get(0).getDiv_id());
		}
		// TODO Auto-generated method stub
		return SUCCESS;
	}

}
