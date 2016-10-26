package com.classowner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.certificate.certificate;
import com.classapp.db.student.StudentData;
import com.classapp.db.student.StudentDetails;
import com.classapp.persistence.Constants;
import com.config.BaseAction;
import com.helper.DivisionHelperBean;
import com.helper.StudentHelperBean;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.certificate.CertificateTransaction;
import com.user.UserBean;

public class ManageStudentAction extends BaseAction{
	int divisionSize;
	List<certificate> certificateList;
	String studentNameSearch;
	String sampleFilePath;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		sampleFilePath = userBean.getUserStatic().getStorageSpace()+File.separator+"SampleFiles"+File.separator+"SampleStudent.xls";
		StudentHelperBean studentHelperBean= new StudentHelperBean();	
		DivisionHelperBean divisionHelperBean = new DivisionHelperBean();
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		BatchTransactions batchTransactions=new BatchTransactions();
		List list=batchTransactions.getAllBatches(userBean.getRegId());
		CertificateTransaction certificateTransaction = new CertificateTransaction();
		certificateList = certificateTransaction.getCertificateTemplateList(userBean.getRegId());
		request.setAttribute("batches", list);
			studentHelperBean.setClass_id(userBean.getRegId());
			/*List<StudentDetails> studentList = studentHelperBean.getStudents();
			request.getSession().setAttribute(Constants.STUDENT_LIST, studentList);*/
			List<Division> divisions= divisionTransactions.getAllDivisions(userBean.getRegId());
			List<String> divisionNames= new ArrayList<String>();
			List<Integer> divisionId= new ArrayList<Integer>();
			List<String> divisionStream= new ArrayList<String>();
			if(null!=divisions){
			for (Division division : divisions) {
				divisionNames.add(division.getDivisionName());
				divisionStream.add(division.getStream());
				divisionId.add(division.getDivId());
			}
			setDivisionSize(divisionNames.size());
			request.setAttribute(Constants.DIVISION_NAMES, divisionNames);
			request.setAttribute(Constants.DIVISION_STREAM, divisionStream);
			request.setAttribute(Constants.DIVISION_ID, divisionId);
			request.setAttribute(Constants.DIVISIONS, divisions);
			}
		return SUCCESS;
	}

	public int getDivisionSize() {
		return divisionSize;
	}

	public void setDivisionSize(int divisionSize) {
		this.divisionSize = divisionSize;
	}

	public List<certificate> getCertificateList() {
		return certificateList;
	}

	public void setCertificateList(List<certificate> certificateList) {
		this.certificateList = certificateList;
	}

	public String getStudentNameSearch() {
		return studentNameSearch;
	}

	public void setStudentNameSearch(String studentNameSearch) {
		this.studentNameSearch = studentNameSearch;
	}

	public String getSampleFilePath() {
		return sampleFilePath;
	}

	public void setSampleFilePath(String sampleFilePath) {
		this.sampleFilePath = sampleFilePath;
	}
	
	
}
