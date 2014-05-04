package com.admin.misc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.persistence.Constants;
import com.datalayer.batch.Batch;
import com.datalayer.subject.Subject;
import com.google.gson.JsonObject;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.user.UserBean;

public class ClassOwnerServlet extends HttpServlet{
	private static String STATUS = "status";
	private static String MESSAGE = "message";
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
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String batchName = (String) req.getParameter("batchName");
			Batch batch = new Batch();
			batch.setBatch(batchName);
			batch.setRegId(regId);
			BatchTransactions batchTransactions = new BatchTransactions();
			if(batchTransactions.addUpdateDb(batch)){
				respObject.addProperty(STATUS, "success");
			}else{
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, " Batch is already exist");
			}
		}else if(Constants.ADD_SUBJECT.equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String subjectName = (String) req.getParameter("subjectName");
			respObject.addProperty(STATUS, "success");
			Subject subject = new Subject();
			subject.setRegId(regId);
			subject.setSubjectName(subjectName);
			SubjectTransaction subjectTransaction = new SubjectTransaction();
			if(subjectTransaction.addUpdateSubjectToDb(subject)){
				respObject.addProperty(STATUS, "success");
			}else{
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, " Subject is already exist");
			}
		}else if("deleteBatch".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String batchName = req.getParameter("batchName");
			BatchTransactions batchTransactions = new BatchTransactions();
			Batch batch = new Batch();
			batch.setRegId(regId);
			batch.setBatch(batchName);
			if(batchTransactions.deleteBatch(batch)){
				respObject.addProperty(STATUS, "success");
			}else{
				respObject.addProperty(STATUS, "error");
			}
		}
		printWriter.write(respObject.toString());
	}
}
