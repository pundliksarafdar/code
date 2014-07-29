package com.admin.misc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.register.RegisterBean;
import com.classapp.persistence.Constants;
import com.datalayer.batch.Batch;
import com.datalayer.subject.Subject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.register.RegisterTransaction;
import com.transaction.teacher.TeaherTransaction;
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
				e.printStackTrace();	
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
			/*batch.setBatch(batchName);
			batch.setRegId(regId);*/
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
				e.printStackTrace();
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
			/*subject.setRegId(regId);*/
			subject.setSubjectName(subjectName);
			SubjectTransaction subjectTransaction = new SubjectTransaction();
			if(subjectTransaction.addUpdateSubjectToDb(subject,regId)){
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
				e.printStackTrace();
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
			/*batch.setRegId(regId);
			batch.setBatch(batchName);*/
			if(batchTransactions.deleteBatch(batch)){
				respObject.addProperty(STATUS, "success");
			}else{
				respObject.addProperty(STATUS, "error");
			}
		}else if("addTeacher".equals(methodToCall)){
			Integer regId = null;
			String subjects=req.getParameter("subjects");
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String teacherID = req.getParameter("teacherID");
			TeaherTransaction teaherTransaction=new TeaherTransaction();
			String stat=teaherTransaction.addTeacher(teacherID,regId,subjects);
			if("added".equals(stat))
			{
				respObject.addProperty(STATUS, "success");
			}else if("exists".equals(stat)){
				respObject.addProperty(MESSAGE, " Teacher is already exist");
				respObject.addProperty(STATUS, "error");
			}else if("false".equals(stat)){
				respObject.addProperty(MESSAGE, "Invalid Teacher ID");
				respObject.addProperty(STATUS, "error");
			}
		}else if("fetchBatchSubject".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String batchID=req.getParameter("batchName");
			BatchTransactions batchTransactions=new BatchTransactions();
			String Batchsubjects=batchTransactions.getBatcheSubjects(batchID);
			
			respObject.addProperty("Batchsubjects", Batchsubjects);
			respObject.addProperty(STATUS, "success");
			respObject.remove(STATUS);
			/*String teacherID = req.getParameter("teacherID");
			TeaherTransaction teaherTransaction=new TeaherTransaction();
			String stat=teaherTransaction.addTeacher(teacherID,regId);
			if("added".equals(stat))
			{
				respObject.addProperty(STATUS, "success");
			}else if("exists".equals(stat)){
				respObject.addProperty(MESSAGE, " Teacher is already exist");
				respObject.addProperty(STATUS, "error");
			}else if("false".equals(stat)){
				respObject.addProperty(MESSAGE, "Invalid Teacher ID");
				respObject.addProperty(STATUS, "error");
			}*/
			
			
		}else if("fetchSubjectTeacher".equals(methodToCall)){
			Integer regId = null;
			try{
				regId = Integer.parseInt(req.getParameter("regId"));
			}catch(Exception e){
				e.printStackTrace();
			}
			UserBean userBean = (UserBean) req.getSession().getAttribute("user");
			if(0 == userBean.getRole() || !"".equals(regId)){
				if(null == regId){
					regId = userBean.getRegId();
				}
			}else{
				regId = userBean.getRegId();
			}
			String subname=req.getParameter("subname");
			SubjectTransaction subjectTransaction=new SubjectTransaction();
			int subid=subjectTransaction.getSubjectID(regId, subname);
			TeaherTransaction teaherTransaction=new TeaherTransaction();
			List<Integer> list=teaherTransaction.getSubjectTeacher(subid+"");
			RegisterTransaction registerTransaction=new RegisterTransaction();
			List<RegisterBean> teacherNames=registerTransaction.getTeacherName(list);
			int i=0;
			String firstname="";
			for(i=0;i<teacherNames.size();i++)
			{
				if(i==0)
				{
				firstname=teacherNames.get(i).getFname();
				}else{
					firstname=firstname+","+teacherNames.get(i).getFname();
				}
			}
			String lastname="";
			for(i=0;i<teacherNames.size();i++)
			{
				if(i==0)
				{
					lastname=teacherNames.get(i).getLname();
				}else{
				lastname=lastname+","+teacherNames.get(i).getLname();
				}
			}
			String teacherid="";
			for(i=0;i<teacherNames.size();i++)
			{
				if(i==0)
				{
					teacherid=teacherNames.get(i).getRegId()+"";
				}else{
				teacherid=teacherid+","+teacherNames.get(i).getRegId();
				}
			}
			
			respObject.addProperty("firstname", firstname);
			respObject.addProperty("lastname", lastname);
			respObject.addProperty("teacherid", teacherid);
			respObject.addProperty(STATUS, "success");
			/*String teacherID = req.getParameter("teacherID");
			TeaherTransaction teaherTransaction=new TeaherTransaction();
			String stat=teaherTransaction.addTeacher(teacherID,regId);
			if("added".equals(stat))
			{
				respObject.addProperty(STATUS, "success");
			}else if("exists".equals(stat)){
				respObject.addProperty(MESSAGE, " Teacher is already exist");
				respObject.addProperty(STATUS, "error");
			}else if("false".equals(stat)){
				respObject.addProperty(MESSAGE, "Invalid Teacher ID");
				respObject.addProperty(STATUS, "error");
			}*/
			
			
		}
		printWriter.write(respObject.toString());
	}
}
