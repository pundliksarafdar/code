package com.transaction.certificate;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.classapp.db.certificate.CertificateDB;
import com.classapp.db.certificate.certificate;
import com.classapp.db.register.RegisterBean;
import com.transaction.register.RegisterTransaction;

public class CertificateTransaction {
	String storageURL;
	public CertificateTransaction(String storageURL) {
		this.storageURL = storageURL;
	}
	
	public CertificateTransaction(){
		
	}
	
	public boolean saveCertificate(int inst_id,String cert_desc,String cert_body,String header_id) {
		CertificateDB db = new CertificateDB();
		boolean status =db.validateCertificateName(cert_desc, inst_id);
		if(status){
			certificate certificate = new certificate();
			certificate.setCert_desc(cert_desc);
			certificate.setInst_id(inst_id);
			certificate.setHeader_id(header_id);
			int cert_id =db.saveCertificate(certificate);
			String certificatePath = this.storageURL + File.separatorChar+ inst_id +File.separatorChar+ "certificateTemplate" + File.separatorChar + cert_id + ".html";
			File file = new File(certificatePath);
			try {
				FileUtils.writeStringToFile(file, cert_body);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return status;
	}
	
	public List<certificate> getCertificateTemplateList(int inst_id) {
		CertificateDB db = new CertificateDB();
		return db.getCertificateList(inst_id);
	}
	
	public String getCertificate(int inst_id,int cert_id) {
		CertificateDB db = new CertificateDB();
		String certificatePath = this.storageURL + File.separatorChar+ inst_id +File.separatorChar+ "certificateTemplate" + File.separatorChar + cert_id + ".html";
		File file = new File(certificatePath);
		String fileData = "";
		try {
			fileData = FileUtils.readFileToString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileData;
	}
	
	public boolean updateCertificate(int inst_id,String cert_desc,String cert_body,String header_id,int cert_id) {
		CertificateDB db = new CertificateDB();
		boolean status =db.validateUpdateCertificateName(cert_desc, inst_id,cert_id);
		if(status){
			certificate certificate = new certificate();
			certificate.setCert_desc(cert_desc);
			certificate.setInst_id(inst_id);
			certificate.setHeader_id(header_id);
			certificate.setCert_id(cert_id);
			db.updateCertificate(certificate);
			String certificatePath = this.storageURL + File.separatorChar+ inst_id +File.separatorChar+ "certificateTemplate" + File.separatorChar + cert_id + ".html";
			File file = new File(certificatePath);
			try {
				FileUtils.writeStringToFile(file, cert_body);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return status;
	}
	
	public String deleteCertificate(int inst_id,int cert_id) {
		CertificateDB db = new CertificateDB();
		db.deleteCertificate(inst_id, cert_id);
		String certificatePath = this.storageURL + File.separatorChar+ inst_id +File.separatorChar+ "certificateTemplate" + File.separatorChar + cert_id + ".html";
		File file = new File(certificatePath);
		String fileData = "";
		file.delete();
		return fileData;
	}
	
	public String getPrintCertificateData(int inst_id,int cert_id,int student_id) {
		CertificateDB db = new CertificateDB();
		String certificatePath = this.storageURL + File.separatorChar+ inst_id +File.separatorChar+ "certificateTemplate" + File.separatorChar + cert_id + ".html";
		File file = new File(certificatePath);
		String fileData = "";
		try {
			fileData = FileUtils.readFileToString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RegisterTransaction registerTransaction = new RegisterTransaction();
		RegisterBean registerBean= registerTransaction.getregistereduser(student_id);
		fileData = fileData.replace("{{StudentName}}", registerBean.getFname()+" "+registerBean.getLname());
		fileData = fileData.replace("{{StudentAddress}}", registerBean.getAddr1()+","+registerBean.getCity()+","+registerBean.getState());
		return fileData;
	}
	
	
}
