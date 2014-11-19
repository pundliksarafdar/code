package com.classowner;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.exams.MCQDetails;
import com.config.BaseAction;
import com.config.Constants;
import com.helper.MCQDataHelper;
import com.user.UserBean;

public class ManageExamsAction extends BaseAction{
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		
		if(request.getSession().getAttribute(Constants.MCQS_LIST)==null){
			MCQDataHelper dataHelper= new MCQDataHelper();			
			dataHelper.setClass_id(userBean.getRegId());
			List<MCQDetails> mcqDetail = dataHelper.getListOfMCQs();
			request.getSession().setAttribute(Constants.MCQS_LIST, mcqDetail);		
		}
		return SUCCESS;
	}	
}
