package com.customrole;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.persistence.Constants;
import com.config.BaseAction;
import com.helper.TeacherHelperBean;
import com.transaction.teacher.TeacherTransaction;
import com.user.UserBean;

public class ManageTeacherAction extends BaseAction{
	int teacherListSize = 0;
	int currentPage;
	int totalPages;
	int endIndex;
	int startIndex;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		
		
			/*TeacherHelperBean teacherHelperBean= new TeacherHelperBean();
			teacherHelperBean.setClass_id(userBean.getInst_id());
			List<TeacherDetails> teacherList = teacherHelperBean.getTeachers();
			//request.getSession().setAttribute(Constants.TEACHER_LIST,teacherHelperBean.getTeachers());
			if(null!=teacherList){
				teacherListSize = teacherList.size();
			}
			if(teacherList!=null){
				int totalcount=teacherList.size();
				int remainder=0;
				if(totalcount>0){
					totalPages=totalcount/10;
					remainder=totalcount%10;
					if (remainder>0) {
						totalPages++;
					}
			
				}
				if (currentPage==0) {
					currentPage++;
				}
				
				if(currentPage>totalPages){
					currentPage--;
				}
				startIndex=(currentPage-1)*10;
				endIndex=startIndex+10;
				if(currentPage==totalPages && remainder>0){
					endIndex=startIndex+remainder;
				}
			}*/
			 
		return SUCCESS;
	}
	public int getTeacherListSize() {
		return teacherListSize;
	}
	public void setTeacherListSize(int teacherListSize) {
		this.teacherListSize = teacherListSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	
}
