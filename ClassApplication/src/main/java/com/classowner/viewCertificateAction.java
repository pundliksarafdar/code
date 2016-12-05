package com.classowner;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.certificate.certificate;
import com.classapp.db.register.AdditionalFormFieldBeanDl;
import com.config.BaseAction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.certificate.CertificateTransaction;
import com.transaction.register.AdditionalFormFieldTransaction;
import com.user.UserBean;

public class viewCertificateAction extends BaseAction {
	List<certificate> certificateList;
	List<Division> divisions;
	String AdditionalFieldJson;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		CertificateTransaction certificateTransaction = new CertificateTransaction();
		certificateList = certificateTransaction.getCertificateTemplateList(userBean.getRegId());
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		divisions= divisionTransactions.getAllDivisions(userBean.getRegId());
		AdditionalFormFieldTransaction transaction = new AdditionalFormFieldTransaction();
		AdditionalFormFieldBeanDl bean = transaction.getAdditionalFormFieldBean(userBean.getRegId());
		AdditionalFieldJson = bean.getFormField();
		return SUCCESS;
	}
	public List<certificate> getCertificateList() {
		return certificateList;
	}
	public void setCertificateList(List<certificate> certificateList) {
		this.certificateList = certificateList;
	}
	public List<Division> getDivisions() {
		return divisions;
	}
	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}
	public String getAdditionalFieldJson() {
		return AdditionalFieldJson;
	}
	public void setAdditionalFieldJson(String additionalFieldJson) {
		AdditionalFieldJson = additionalFieldJson;
	}
	
}
