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
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class TeacherTimeTableAction extends BaseAction {

	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		
		BatchTransactions batchTransactions=new BatchTransactions();
		int regID=userBean.getRegId();
		TeacherTransaction teacherTransaction=new TeacherTransaction();
		List classids=teacherTransaction.getTeachersClass(regID);
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List<RegisterBean> classbeanes=registerTransaction.getTeachersInstitutes(classids);
		request.setAttribute("Classes", classbeanes);
		// TODO Auto-generated method stub
		return SUCCESS;
	}

}
