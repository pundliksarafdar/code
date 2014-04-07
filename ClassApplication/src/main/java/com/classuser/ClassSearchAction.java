package com.classuser;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.classapp.myclass.ClassSearchForm;
import com.classapp.myclass.SearchClassDb;
import com.config.BaseAction;
import com.config.Constants;
import com.opensymphony.xwork2.ActionContext;
import com.user.UserBean;

public class ClassSearchAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	private com.classapp.myclass.ClassSearchForm classSearchForm;
	
	public com.classapp.myclass.ClassSearchForm getClassSearchForm() {
		return classSearchForm;
	}

	public void setClassSearchForm(com.classapp.myclass.ClassSearchForm classSearchForm) {
		this.classSearchForm = classSearchForm;
	}

	@Override
	public String performBaseAction(UserBean userBean) {
		String forward = null;
		ActionContext.getContext().getSession().put("isSearched", "true");
		SearchClassDb searchClassDb = new SearchClassDb();
		String task = "";
		if(null != classSearchForm){
			task = classSearchForm.getTask();
		}else{
			classSearchForm = (ClassSearchForm) ActionContext.getContext().getSession().get(Constants.CLASSSERACHFORM);
		}
		String currentPage = "0";

		if(null == classSearchForm.getTask()){
			classSearchForm.setCurrentPage("0");
			ActionContext.getContext().getSession().put(Constants.CLASSSERACHFORM, classSearchForm);
		}else if (Constants.NEXT.equalsIgnoreCase(classSearchForm.getTask())) {
			currentPage = (Integer.parseInt(classSearchForm.getCurrentPage())+1)+"";
			classSearchForm = (ClassSearchForm) ActionContext.getContext().getSession().get(Constants.CLASSSERACHFORM);
			classSearchForm.setCurrentPage(currentPage);
			classSearchForm.setTask(task);
		}else if(Constants.PREV.equalsIgnoreCase(classSearchForm.getTask())){
			currentPage = (Integer.parseInt(classSearchForm.getCurrentPage())-1)+"";
			classSearchForm = (ClassSearchForm) ActionContext.getContext().getSession().get(Constants.CLASSSERACHFORM);
			classSearchForm.setCurrentPage(currentPage);
			classSearchForm.setTask(task);
		}
		classSearchForm.setCurrentPage(currentPage);
		classSearchForm.setTask(task);
		List list = searchClassDb.searchClassData(classSearchForm);
		int totalPages = searchClassDb.getPagesCount(classSearchForm);
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("searchresult", list);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("currentPage", currentPage);
		forward = Constants.SUCCESS;
		return forward;
	}
	
}