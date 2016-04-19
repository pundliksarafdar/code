package com.teacher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.Exam;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.StudentMarks;
import com.classapp.db.subject.Subject;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.QuestionData;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class TeacherAttendance extends BaseAction {

	List<RegisterBean> registerBeanList; 
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		TeacherTransaction teacherTransaction = new TeacherTransaction();
		List list = teacherTransaction.getTeachersClass(userBean.getRegId());
		RegisterTransaction registerTransaction = new RegisterTransaction();
		registerBeanList = registerTransaction.getTeachersInstitutes(list);
		
		return SUCCESS;
	}
	public List<RegisterBean> getRegisterBeanList() {
		return registerBeanList;
	}
	public void setRegisterBeanList(List<RegisterBean> registerBeanList) {
		this.registerBeanList = registerBeanList;
	}
	
	
}
