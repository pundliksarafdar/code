package com.admin.misc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.edit.DBUpdate;
import com.google.gson.JsonObject;
import com.miscfunction.MiscFunction;

public class AdminAjaxServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DBUpdate dbUpdate = new DBUpdate();
		PrintWriter writer = resp.getWriter();
		String methodeToCall = (String) req.getParameter("methodToCall");
		if("reg".equalsIgnoreCase(methodeToCall)){
			String regId = (String)req.getParameter("regId");
			String duration = (String)req.getParameter("duration");
			System.out.println("MethodeToCall-"+methodeToCall);
			System.out.println("Registration Id-"+regId);
			System.out.println("Duration-"+duration);
			try{
				String result = dbUpdate.updateDuration(regId, duration);
				JsonObject resultJson = new JsonObject();
				resultJson.addProperty("status", "success");
				resultJson.addProperty("endDate", MiscFunction.dateFormater(result));
				String resultJsonStr = resultJson.toString();
				writer.print(resultJsonStr);
			}catch(Exception e){
				
				writer.print("{'status':'error'}");
			}
		}else if("block".equalsIgnoreCase(methodeToCall)){
			String regId = (String)req.getParameter("regId");
			String role = (String)req.getParameter("role");
			System.out.println("MethodeToCall-"+methodeToCall);
			System.out.println("Registration Id-"+regId);
			if("null".equalsIgnoreCase(role)){
				role = "0";
			}
			dbUpdate.blockUser(regId,role);
		}else if("unblock".equalsIgnoreCase(methodeToCall)){
			String regId = (String)req.getParameter("regId");
			String role = (String)req.getParameter("role");
			System.out.println("MethodeToCall-"+methodeToCall);
			System.out.println("Registration Id-"+regId);
			if("null".equalsIgnoreCase(role)){
				role = "0";
			}
			dbUpdate.unBlockUser(regId,role);
		}
		else if("deleteuser".equalsIgnoreCase(methodeToCall)){
			String regId = (String)req.getParameter("regId");
			String role = (String)req.getParameter("role");
			System.out.println("MethodeToCall-"+methodeToCall);
			System.out.println("Registration Id-"+regId);
			if(dbUpdate.deleteUser(regId)){
				JsonObject resultJson = new JsonObject();
				resultJson.addProperty("status", "success");
				String resultJsonStr = resultJson.toString();
				writer.print(resultJsonStr);
			}else{
				JsonObject resultJson = new JsonObject();
				resultJson.addProperty("status", "error");
				String resultJsonStr = resultJson.toString();
				writer.print(resultJsonStr);
			}
				
		}
	}
}
