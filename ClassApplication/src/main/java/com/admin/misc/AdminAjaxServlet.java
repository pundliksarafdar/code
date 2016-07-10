package com.admin.misc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.edit.DBUpdate;
import com.classapp.logger.AppLogger;
import com.classapp.servicetable.ServiceMap;
import com.google.gson.JsonObject;
import com.miscfunction.MiscFunction;
import com.transaction.addtransaction.AdvertiseTransaction;
import com.transaction.register.RegisterTransaction;

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
			AppLogger.logger("MethodeToCall-"+methodeToCall);
			AppLogger.logger("Registration Id-"+regId);
			AppLogger.logger("Duration-"+duration);
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
			AppLogger.logger("MethodeToCall-"+methodeToCall);
			AppLogger.logger("Registration Id-"+regId);
			if("null".equalsIgnoreCase(role)){
				role = "0";
			}
			dbUpdate.blockUser(regId,role);
		}else if("unblock".equalsIgnoreCase(methodeToCall)){
			String regId = (String)req.getParameter("regId");
			String role = (String)req.getParameter("role");
			AppLogger.logger("MethodeToCall-"+methodeToCall);
			AppLogger.logger("Registration Id-"+regId);
			if("null".equalsIgnoreCase(role)){
				role = "0";
			}
			dbUpdate.unBlockUser(regId,role);
		}else if("deleteuser".equalsIgnoreCase(methodeToCall)){
			String regId = (String)req.getParameter("regId");
			AppLogger.logger("MethodeToCall-"+methodeToCall);
			AppLogger.logger("Registration Id-"+regId);
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
				
		}else if("checkuphonenumber".equalsIgnoreCase(methodeToCall)){
			String mobileNum = (String)req.getParameter("mobileNumber");
			AppLogger.logger("MethodeToCall-"+methodeToCall);
			AppLogger.logger("Registration Id-"+mobileNum);
			RegisterTransaction registerTransaction = new RegisterTransaction();
			boolean result = registerTransaction.isMobileExits(mobileNum);
			String isDebugging = ServiceMap.getSystemParam(com.classapp.persistence.Constants.DEBUGGING_MODE, "isdebug");
			boolean isDebuggingBool = null!=isDebugging && isDebugging.equals("yes");
			JsonObject resultJson = new JsonObject();
			resultJson.addProperty("exists", result&&!isDebuggingBool);
			String resultJsonStr = resultJson.toString();
			writer.print(resultJsonStr);	
		}else if("checkusername".equalsIgnoreCase(methodeToCall)){
			String userName = (String)req.getParameter("userName");
			AppLogger.logger("MethodeToCall-"+methodeToCall);
			AppLogger.logger("Registration Id-"+userName);
			RegisterTransaction registerTransaction = new RegisterTransaction();
			boolean result = registerTransaction.isUserExits(userName);
			JsonObject resultJson = new JsonObject();
			resultJson.addProperty("exists", result);
			String resultJsonStr = resultJson.toString();
			writer.print(resultJsonStr);	
		}else if("checkemail".equalsIgnoreCase(methodeToCall)){
			String email = (String)req.getParameter("emailId");
			AppLogger.logger("MethodeToCall-"+methodeToCall);
			AppLogger.logger("Email Id-"+email);
			RegisterTransaction registerTransaction = new RegisterTransaction();
			boolean result = registerTransaction.isEmailExists(email);
			JsonObject resultJson = new JsonObject();
			resultJson.addProperty("exists", result);
			String resultJsonStr = resultJson.toString();
			writer.print(resultJsonStr);	
		}else if("getAllCountries".equalsIgnoreCase(methodeToCall)){
			AdvertiseTransaction advertiseTransaction = new AdvertiseTransaction();
			List<String> allCountries = advertiseTransaction.getAllCounrty();
			JsonObject resultJson = new JsonObject();
			resultJson.addProperty("countryList", allCountries.toString());
			String resultJsonStr = resultJson.toString();
			writer.print(resultJsonStr);	
		}else if("getStateInCountries".equalsIgnoreCase(methodeToCall)){
			AdvertiseTransaction advertiseTransaction = new AdvertiseTransaction();
			String country = (String)req.getParameter("country");
			List<String> allStates = advertiseTransaction.getAllStateInCountry(country);
			JsonObject resultJson = new JsonObject();
			resultJson.addProperty("stateList", allStates.toString());
			String resultJsonStr = resultJson.toString();
			writer.print(resultJsonStr);	
		}else if("getCityInStateAndCountries".equalsIgnoreCase(methodeToCall)){
			AdvertiseTransaction advertiseTransaction = new AdvertiseTransaction();
			String country = (String)req.getParameter("country");
			String state = (String)req.getParameter("state");
			List<String> allCities = advertiseTransaction.getAllCityInStateAndCountry(country, state);
			JsonObject resultJson = new JsonObject();
			resultJson.addProperty("cityList", allCities.toString());
			String resultJsonStr = resultJson.toString();
			writer.print(resultJsonStr);	
		}
	}
}
