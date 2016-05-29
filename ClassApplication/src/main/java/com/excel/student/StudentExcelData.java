package com.excel.student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.service.beans.RegisterBean;
import com.service.beans.StudentRegisterServiceBean;
import com.service.beans.Student_Fees;
import com.transaction.fee.FeesTransaction;
import com.transaction.register.RegisterTransaction;

public class StudentExcelData {
	private String fileName;
	private ArrayList<StudentRegisterServiceBean> students;
	private HashMap<String, ArrayList<String>> invalidStudentResponseMap;
	private HashMap<Integer,Boolean> successStudentMap;
	
	public StudentExcelData(String fileName){
		this.fileName=fileName;
		this.students=new ArrayList<StudentRegisterServiceBean>();
		this.invalidStudentResponseMap=new HashMap<String, ArrayList<String>>();
		this.successStudentMap= new HashMap<Integer, Boolean>();
	}

	public String getFileName() {
		return fileName;
	}

	public ArrayList<StudentRegisterServiceBean> getStudents() {
		return students;
	}

	public HashMap<Integer, Boolean> getSuccessStudentMap() {
		return successStudentMap;
	}

	public HashMap<String, ArrayList<String>> getInvalidStudentResponseMap() {
		return invalidStudentResponseMap;
	}
	
	/**
	 * This method is to load students present in .xsl file. It generates list
	 * of students as a output.
	 */
	public void loadStudents(int instId, int divId, int batchId) {

		File myFile = new File(this.fileName);
		POIFSFileSystem pfs = null;
		FileInputStream fis = null;
		org.apache.poi.ss.usermodel.Workbook workbook = null;

		try {

			fis = new FileInputStream(myFile);
			pfs = new POIFSFileSystem(fis);
			// Finds the workbook instance for XLSX file
			workbook = WorkbookFactory.create(pfs);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Return first sheet from the XLSX workbook
		// XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();

		if (rowIterator.hasNext()) {
			Row rowHeader = rowIterator.next();
			//skip reading of headers
		}
		
		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			ArrayList<String> listOfErrors = new ArrayList<String>();
			try{
				
				
				boolean isValidDate=true;
				boolean isValidFeesDiscountType=true;
				boolean isValidData=true;
								
				String firstName=row.getCell(1).getStringCellValue();
				String lastName= row.getCell(2).getStringCellValue();
				Long phoneNumber= Long.parseLong(Double.toString(row.getCell(3).getNumericCellValue()));
				String emailId=row.getCell(4).getStringCellValue();
				Date dob=null;
				
				if(HSSFDateUtil.isCellDateFormatted(row.getCell(5))){
					dob=row.getCell(5).getDateCellValue();
					isValidDate=true;
				}else{
					listOfErrors.add("Invalid Date of Birth!");		
					isValidDate=false;
				}
				
				String address=row.getCell(6).getStringCellValue();
				String city=row.getCell(8).getStringCellValue();
				String state=row.getCell(7).getStringCellValue();
				String parentFirstName=row.getCell(9).getStringCellValue();
				String parentLastName=row.getCell(10).getStringCellValue();
				Long parentPhoneNo= Long.parseLong(Double.toString(row.getCell(11).getNumericCellValue()));
				String parentEmailID=row.getCell(12).getStringCellValue();
				double feePaid=row.getCell(13).getNumericCellValue();
				isValidData=validateExcelData(firstName, lastName, phoneNumber, emailId, address, city, state, parentFirstName, parentLastName, parentPhoneNo, parentEmailID, feePaid, listOfErrors);
				double feesDiscount=0.0;
				String feesDiscountType="";
				if(row.getCell(14).getNumericCellValue()!=0.0 && row.getCell(14).getNumericCellValue()<=100.0){
					feesDiscountType="per";
					feesDiscount=row.getCell(14).getNumericCellValue();
				}else if(row.getCell(15).getNumericCellValue()!=0.0 && row.getCell(14).getNumericCellValue()==0.0){
					feesDiscountType="amt";
					feesDiscount=row.getCell(15).getNumericCellValue();
				}else{
					isValidFeesDiscountType=false;
					listOfErrors.add("Invalid input for column 'Discount in %' or 'Discount in  ₹'");
				}
					
				RegisterBean registerBean = new RegisterBean();
				registerBean.setFname(firstName);
				registerBean.setLname( lastName);
				registerBean.setPhone1(Long.toString(phoneNumber));
				registerBean.setEmail(emailId);
				SimpleDateFormat formatter= new SimpleDateFormat("yyyyddMM");		
				registerBean.setDob(formatter.format(dob));
				registerBean.setAddr1(address);
				registerBean.setCity(city);
				registerBean.setState(state);
				
				com.service.beans.Student student=new com.service.beans.Student();
				student.setParentEmail(parentEmailID);
				student.setParentFname(parentFirstName);
				student.setParentLname(parentLastName);
				student.setParentPhone(Long.toString(parentPhoneNo));
				
				Student_Fees studentFees=new Student_Fees();
				
				studentFees.setBatch_id(batchId);
				studentFees.setDiv_id(divId);
				studentFees.setInst_id(instId);
				
				studentFees.setFees_paid(feePaid);
				studentFees.setDiscount_type(feesDiscountType);
				FeesTransaction feesTransaction = new FeesTransaction();
				List<Integer> batchIds=  new ArrayList<Integer>();
				batchIds.add(batchId);
				List<com.classapp.db.fees.BatchFees> batchFees=feesTransaction.getBatchFeesList(instId, divId,batchIds);
				studentFees.setBatch_fees(batchFees.get(0).getBatch_fees());
				if(isValidFeesDiscountType && feesDiscountType.equalsIgnoreCase("amt") && batchFees.get(0).getBatch_fees()> feesDiscount){
					studentFees.setDiscount(feesDiscount);
				}else{
					studentFees.setDiscount(0.0);
					listOfErrors.add("Invalid discount amount! Discount can not be greater than batch fees. Discount set to 0. Please set it manually.");
				}
				List<Student_Fees> listOfStudentFeesHistory= new ArrayList<Student_Fees>();
				listOfStudentFeesHistory.add(studentFees);
				
				StudentRegisterServiceBean studentBean=new StudentRegisterServiceBean();
				studentBean.setRegisterBean(registerBean);
				studentBean.setStudent(student);
				studentBean.setStudent_FeesList(listOfStudentFeesHistory);
				
				System.out.println(student.toString());
				if(isValidData && isValidDate){
					students.add(studentBean);
				}				
			} catch(IllegalStateException e){
				listOfErrors.add(e.getMessage());
				invalidStudentResponseMap.put(""+row.getRowNum(),listOfErrors);
			}catch(NullPointerException e){
				listOfErrors.add(e.getMessage());
				invalidStudentResponseMap.put(""+row.getRowNum(),listOfErrors);
				System.out.println(listOfErrors);
			}
		}
		System.out.println("Number of students:" + students.size());
	}
	
	public boolean validateExcelData(
			String firstName,
			String lastName,
			Long phoneNumber,
			String emailId,
			String address,
			String city,
			String state,
			String parentFirstName,
			String parentLastName,
			Long parentPhoneNo,
			String parentEmailID,
			double feePaid,ArrayList<String> listOfErrors){
				
				// validate First Name
				if(null==firstName || firstName.equals("")){
					listOfErrors.add("First name column can not be blank or empty");
				}else if(firstName.length()>100){
					listOfErrors.add("First name value is too long.");
				}else if(!firstName.matches( "[A-Z][a-zA-Z]*" )){
					listOfErrors.add("Invalid characters in column First Name.");
				}
				
				//validate last name
				if(null==lastName || lastName.equals("")){
					listOfErrors.add("Last name column can not be blank or empty");
				}else if(lastName.length()>100){
					listOfErrors.add("Last name value is too long.");
				}else if(!lastName.matches( "[A-Z][a-zA-Z]*" )){
					listOfErrors.add("Invalid characters in column Last Name.");
				}
				//validate phone number
				if (phoneNumber==0l) {
					 listOfErrors.add("Phone Number can not be blank or empty.");
				 }				
				else if (!phoneNumber.toString().matches("\\d{10}") || 
						 !phoneNumber.toString().matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}") || 
						 !phoneNumber.toString().matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) 
				 {
					 listOfErrors.add("Invalid value in column Phone Number.");
				 }
				
				 //validate address
				if(null==address || address.equals("")){
					listOfErrors.add("Address column can not be blank or empty");
				}else if(address.length()>250){
					listOfErrors.add("Address value is too long.");
				}else if(!address.matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)")){
					 listOfErrors.add("Invalid characters in column address.");
				}
				//validate email address
				if(null==emailId || emailId.equals("")){
					listOfErrors.add("EMail Address column can not be blank or empty");
				}else if(emailId.length()>150){
					listOfErrors.add("Email Address value is too long.");
				}else if(!validateEmailID(emailId)){
					 listOfErrors.add("Invalid email address.");
				}
				
				// city validation
				if(null==city || city.equals("")){
					listOfErrors.add("City column can not be blank or empty");
				}else if(city.length()>100){
					listOfErrors.add("City value is too long.");
				}else if(!city.matches("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)")){
					 listOfErrors.add("Invalid characters in column city.");
				}
				 
				//validate state
				if(null==state || state.equals("")){
					listOfErrors.add("State column can not be blank or empty");
				}else if(state.length()>100){
					listOfErrors.add("State value is too long.");
				}else if(!state.matches("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)")){
					 listOfErrors.add("Invalid characters in column State.");
				}
				
				// validate Parent's First Name
				if(null==parentFirstName || parentFirstName.equals("")){
					listOfErrors.add("Parent's First name column can not be blank or empty");
				}else if(parentFirstName.length()>100){
					listOfErrors.add("Parent's First name value is too long.");
				}else if(!parentFirstName.matches( "[A-Z][a-zA-Z]*" )){
					listOfErrors.add("Invalid characters in column Parent's First Name.");
				}
				
				//validate Parent's last name
				if(null==parentLastName || parentLastName.equals("")){
					listOfErrors.add("Parent's Last name column can not be blank or empty");
				}else if(parentLastName.length()>100){
					listOfErrors.add("Parent's Last name value is too long.");
				}else if(!parentLastName.matches( "[A-Z][a-zA-Z]*" )){
					listOfErrors.add("Invalid characters in column Parent's Last Name.");
					
				}
				//validate Parent's phone number
				if (parentPhoneNo==0l) {
					 listOfErrors.add("Paren's Phone Number can not be blank or empty.");
				 }				
				else if (!parentPhoneNo.toString().matches("\\d{10}") || 
						 !parentPhoneNo.toString().matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}") || 
						 !parentPhoneNo.toString().matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) 
				 {
					 listOfErrors.add("Invalid value in column Parent's Phone Number.");
				 }
				//validate parent's email address
				if(null==parentEmailID || parentEmailID.equals("")){
					listOfErrors.add("Parent's Email Address column can not be blank or empty");
				}else if(parentEmailID.length()>150){
					listOfErrors.add("Parent's Email Address value is too long.");
				}else if(!validateEmailID(parentEmailID)){
					 listOfErrors.add("Invalid parent's email address.");
				}
				
				this.invalidStudentResponseMap.put(emailId, listOfErrors);
				if(listOfErrors.size()>0){
					
					return false;
				}
		return true;
	}
	
	boolean validateEmailID(String emailId){
		String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher;
		if(emailId!=null){
			matcher=pattern.matcher(emailId);
			return matcher.matches();
		}
		return false;
	}
	
	public void processData(int instId, int divId, int batchId){
		FeesTransaction feesTransaction = new FeesTransaction();
		RegisterTransaction registerTransaction = new RegisterTransaction();
		for (StudentRegisterServiceBean serviceBean : students) {			
			int student_id = registerTransaction.registerStudentManually(instId,serviceBean.getRegisterBean(), serviceBean.getStudent(),divId,Integer.toString(batchId));
			for (Iterator iterator = serviceBean.getStudent_FeesList().iterator(); iterator
					.hasNext();) {
				Student_Fees student_Fees = (Student_Fees) iterator.next();
				student_Fees.setStudent_id(student_id);
			}
			boolean status = feesTransaction.saveStudentBatchFees(instId, serviceBean.getStudent_FeesList());
			this.successStudentMap.put(student_id, status);
		}
	}
	
	public static void main(String[] args) {
		StudentExcelData excelSheetData = new StudentExcelData("E:\\SampleStudent.xls");
		//excelSheetData.loadStudents(instId, divId, batchId);
	}
}
