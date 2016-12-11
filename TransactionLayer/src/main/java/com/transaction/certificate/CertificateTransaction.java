package com.transaction.certificate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.classapp.db.certificate.CertificateDB;
import com.classapp.db.certificate.certificate;
import com.classapp.db.register.AdditionalFormFieldBeanDl;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.transaction.register.AdditionalFormFieldTransaction;
import com.transaction.register.RegisterTransaction;
import com.transaction.student.StudentTransaction;

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
		RegisterBean registerBean = registerTransaction.getregistereduser(inst_id);
		StudentTransaction studentTransaction = new StudentTransaction();
		Student student= studentTransaction.getclassStudent(student_id,inst_id);
		AdditionalFormFieldTransaction transaction = new AdditionalFormFieldTransaction();
		AdditionalFormFieldBeanDl bean = transaction.getAdditionalFormFieldBean(inst_id);
		fileData = fileData.replace("{{StudentFullName}}", student.getFname()+" "+student.getLname());
		fileData = fileData.replace("{{StudentFirstName}}", student.getFname());
		fileData = fileData.replace("{{StudentLastName}}", student.getLname());
		fileData = fileData.replace("{{ParentFullName}}", student.getParentFname()+" "+student.getLname());
		fileData = fileData.replace("{{ParentFirstName}}", student.getParentFname());
		fileData = fileData.replace("{{ParentLastName}}", student.getParentLname());
		fileData = fileData.replace("{{StudentAddress}}", student.getAddr()+","+student.getCity()+","+student.getState());
		fileData = fileData.replace("{{StudentDOB}}", student.getDob()+"");
		fileData = fileData.replace("{{StudentJoiningDate}}", student.getJoiningDate()+"");
		fileData = fileData.replace("{{StudentMobile}}", student.getPhone()==null?"":student.getPhone());
		fileData = fileData.replace("{{StudentEmail}}", student.getEmail()==null?"":student.getEmail());
		fileData = fileData.replace("{{StudentRegNo}}", student.getStudentInstRegNo()==null?"":student.getStudentInstRegNo());
		fileData = fileData.replace("{{InstituteName}}", registerBean.getClassName());
		fileData = fileData.replace("{{InstituteAddress}}", registerBean.getAddr1()+","+registerBean.getCity()+","+registerBean.getState());
		fileData = fileData.replace("{{InstitutePhone}}", registerBean.getPhone1()==null?"":registerBean.getPhone1());
		fileData = fileData.replace("{{InstituteEmail}}", registerBean.getEmail()==null?"":registerBean.getEmail());
		String str = bean.getFormField();
		String studentAdditionalData = student.getStudentAdditionalInfo();
		if(studentAdditionalData != null){ 
		JsonParser parser = new JsonParser();
		 Object obj = parser.parse(str);
		 JsonObject studentAdditionalDataObject = (JsonObject) parser.parse(studentAdditionalData);
        JsonObject object = (JsonObject)obj;
        Set<Entry<String, JsonElement>> ens = object.entrySet();
            for (Entry<String, JsonElement> en : ens) {
            	if( studentAdditionalDataObject.get(en.getKey()) != null){
            	fileData = fileData.replace("{{"+en.getKey()+"}}", studentAdditionalDataObject.get(en.getKey()).toString().replace("\"",""));
            	}else{
            		fileData = fileData.replace("{{"+en.getKey()+"}}", "-");	
            	}
            }
		}
		return fileData;
	}
	
	
}
