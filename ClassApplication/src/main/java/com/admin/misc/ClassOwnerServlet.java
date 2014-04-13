package com.admin.misc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionContext;
import com.user.UserBean;

public class ClassOwnerServlet extends HttpServlet{
	private static String STATUS = "status";
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter printWriter = resp.getWriter();
		JsonObject respObject = new JsonObject();
		respObject.addProperty(STATUS, "error");
		
		String methodToCall = (String) req.getParameter("methodToCall");
		if("addBatch".equals(methodToCall)){
			Integer regId = Integer.parseInt(req.getParameter("regId"));
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole()){
				
			}else{
				regId = userBean.getRegId();
			}
			String batchName = (String) req.getAttribute("batchName");
			respObject.addProperty(STATUS, "success");
		}
		
		printWriter.write(respObject.toString());
	}
}
