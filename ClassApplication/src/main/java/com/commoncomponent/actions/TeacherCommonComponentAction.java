package com.commoncomponent.actions;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.config.BaseAction;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.register.RegisterTransaction;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class TeacherCommonComponentAction extends BaseAction{
	String forwardAction;
	String lable;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		// TODO Auto-generated method stub
		String notes=(String) request.getParameter("notesadded");
		SubjectTransaction subjectTransaction=new SubjectTransaction();
		TeacherTransaction teacherTransaction=new TeacherTransaction();
		List classids=teacherTransaction.getTeachersClass(userBean.getRegId());
		RegisterTransaction registerTransaction=new RegisterTransaction();
		List classes=registerTransaction.getTeachersInstitutes(classids);
		request.setAttribute("classes", classes);
		lable=getheader();
		request.setAttribute("notes", notes);
		
		return SUCCESS;
	}
	public String getForwardAction() {
		return forwardAction;
	}
	public void setForwardAction(String forwardAction) {
		this.forwardAction = forwardAction;
	}	
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public String getheader() {
		if("uploadexams".equals(forwardAction)){
			return "Add Question";
		}else if("searchQuestion".equals(forwardAction)){
			return "Search Question";
		}else if("generateexampreaction".equals(forwardAction)){
			return "Generate Exam";
		}else if("manualexam".equals(forwardAction)){
			return "Manual Exam";
		}else if("listExam".equals(forwardAction)){
			return "Search Exam";
		}else if("attemptexamlist".equals(forwardAction)){
			return "Attempt Exam";
		}else if("studentexammarks".equals(forwardAction)){
			return "Student Marks";
		}else if("addteachernotesoption".equals(forwardAction)){
			return "Add Notes";
		}else if("seenotes".equals(forwardAction)){
			return "See Notes";
		}
		
		return "";
	}

}
