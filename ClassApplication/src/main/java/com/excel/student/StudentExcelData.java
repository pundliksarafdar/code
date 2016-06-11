package com.excel.student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
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
import org.apache.poi.ss.util.NumberToTextConverter;

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
		System.out.println("Inst id:"+instId+" divId:"+divId);
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
				
				
				boolean isValidDateOfBirth=true;
				boolean isValidFeesDiscountType=true;
				boolean isValidData=true;
				boolean isValidFees=true;
				
				String firstName=row.getCell(1).getStringCellValue();
				String middleName=row.getCell(2).getStringCellValue();
				String lastName= row.getCell(3).getStringCellValue();
				String str = NumberToTextConverter.toText(row.getCell(4).getNumericCellValue());
						
				Long phoneNumber= Long.parseLong(str);
				String emailId=row.getCell(5).getStringCellValue();
				Date dob=null;
				SimpleDateFormat formatter1= new SimpleDateFormat("MM/dd/yyyy");
				if(HSSFDateUtil.isCellDateFormatted(row.getCell(6)) && isValidDate(formatter1.format(row.getCell(6).getDateCellValue()))){
						dob=row.getCell(6).getDateCellValue();
						isValidDateOfBirth=true;										
				}else{
					listOfErrors.add("Invalid Date of Birth!");		
					isValidDateOfBirth=false;
				}
				
				String address=row.getCell(7).getStringCellValue();
				String city=row.getCell(9).getStringCellValue();
				String state=row.getCell(8).getStringCellValue();
				String parentFirstName=row.getCell(10).getStringCellValue();
				String parentLastName=row.getCell(11).getStringCellValue();
				Long parentPhoneNo= Long.parseLong(NumberToTextConverter.toText(row.getCell(12).getNumericCellValue()));
				String parentEmailID=row.getCell(13).getStringCellValue();
				double feePaid=row.getCell(14).getNumericCellValue();
				isValidData=validateExcelData(firstName, middleName, lastName, phoneNumber, emailId, address, city, state, parentFirstName, parentLastName, parentPhoneNo, parentEmailID, feePaid, listOfErrors);
				double feesDiscount=0.0;
				String feesDiscountType="";
				if(row.getCell(15).getNumericCellValue()!=0.0 && row.getCell(15).getNumericCellValue()<=100.0){
					feesDiscountType="per";
					feesDiscount=row.getCell(15).getNumericCellValue();
				}else if(row.getCell(16).getNumericCellValue()!=0.0 && row.getCell(15).getNumericCellValue()==0.0){
					feesDiscountType="amt";
					feesDiscount=row.getCell(16).getNumericCellValue();
				}else{
					isValidFeesDiscountType=false;
					listOfErrors.add("Invalid input for column 'Discount in %' or 'Discount in  ₹'");
				}
				/*Date startDate=null;
				
						if(isValidDate(row.getCell(17).getStringCellValue())){
							startDate=row.getCell(17).getDateCellValue();	
						}else{
							startDate=new Date();
							listOfErrors.add("Invalid Date of start! please set it manually.");
						}*/
					
				
				RegisterBean registerBean = new RegisterBean();
				registerBean.setFname(firstName);
				registerBean.setMname(middleName);
				registerBean.setLname( lastName);
				registerBean.setPhone1(Long.toString(phoneNumber));
				registerBean.setEmail(emailId);
				SimpleDateFormat formatter= new SimpleDateFormat("yyyyddMM");		
				registerBean.setDob(formatter.format(dob));
				registerBean.setAddr1(address);
				registerBean.setCity(city);
				registerBean.setState(state);
				//registerBean.setStartDate(formatter.format(startDate));
				
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
				List<Student_Fees> listOfStudentFeesHistory= new ArrayList<Student_Fees>();
				if(batchFees!=null){
				studentFees.setBatch_fees(batchFees.get(0).getBatch_fees());
					if(isValidFeesDiscountType && feesDiscountType.equalsIgnoreCase("amt") && batchFees.get(0).getBatch_fees()> feesDiscount){
						studentFees.setDiscount(feesDiscount);
					}else{
						studentFees.setDiscount(0.0);
						listOfErrors.add("Invalid discount amount! Discount can not be greater than batch fees. Discount set to 0. Please set it manually.");
					}					
					listOfStudentFeesHistory.add(studentFees);
				}else{
					isValidFees=false;
					listOfErrors.add("Invalid Batch fees. Unable to add student without batch fees.");
				}
				StudentRegisterServiceBean studentBean=new StudentRegisterServiceBean();
				studentBean.setRegisterBean(registerBean);
				studentBean.setStudent(student);
				studentBean.setStudent_FeesList(listOfStudentFeesHistory);
				
				System.out.println(studentBean.toString()+" isValidData:"+isValidData+" isValidDateOfBirth:"+isValidDateOfBirth);
				if(isValidData && isValidDateOfBirth && isValidFees){
					students.add(studentBean);
					System.out.println("Student added successfully!");
				}
				System.out.println(this.invalidStudentResponseMap);
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
			String middleName,
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
				}else if(!firstName.matches( "[a-zA-Z]*" )){
					listOfErrors.add("Invalid characters in column First Name.");
				}
				
				//validate middle name
				if(null==middleName || middleName.equals("")){
					listOfErrors.add("Middle name column can not be blank or empty");
				}else if(middleName.length()>100){
					listOfErrors.add("Middle name value is too long.");
				}else if(!middleName.matches( "[a-zA-Z]*" )){
					listOfErrors.add("Invalid characters in column Middle Name.");
				}
				
				//validate last name
				if(null==lastName || lastName.equals("")){
					listOfErrors.add("Last name column can not be blank or empty");
				}else if(lastName.length()>100){
					listOfErrors.add("Last name value is too long.");
				}else if(!lastName.matches( "[a-zA-Z]*" )){
					listOfErrors.add("Invalid characters in column Last Name.");
				}
				//validate phone number
				if (phoneNumber==0l) {
					 listOfErrors.add("Phone Number can not be blank or empty.");
				 }				
				else if (!phoneNumber.toString().matches("\\d{10}")) 
				 {
					 listOfErrors.add("Invalid value in column Phone Number.");
				 }
				
				 //validate address
				if(null==address || address.equals("")){
					listOfErrors.add("Address column can not be blank or empty");
				}else if(address.length()>250){
					listOfErrors.add("Address value is too long.");
				}/*else if(!address.matches("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)")){
					 listOfErrors.add("Invalid characters in column address.");
				}*/
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
				}else if(!parentFirstName.matches( "[a-zA-Z]*" )){
					listOfErrors.add("Invalid characters in column Parent's First Name.");
				}
				
				//validate Parent's last name
				if(null==parentLastName || parentLastName.equals("")){
					listOfErrors.add("Parent's Last name column can not be blank or empty");
				}else if(parentLastName.length()>100){
					listOfErrors.add("Parent's Last name value is too long.");
				}else if(!parentLastName.matches( "[a-zA-Z]*" )){
					listOfErrors.add("Invalid characters in column Parent's Last Name.");
					
				}
				//validate Parent's phone number
				if (parentPhoneNo==0l) {
					 listOfErrors.add("Paren's Phone Number can not be blank or empty.");
				 }				
				else if (!parentPhoneNo.toString().matches("\\d{10}")) 
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
	
	boolean isValidDate(String inputDateStr){
		SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy");
		sdf.setLenient(false);
		
		boolean result=false;
		if(null!=inputDateStr && !inputDateStr.equals("")){
			try {
				Date date = sdf.parse(inputDateStr);
				result= true;
			} catch (ParseException e) {
				result= false;			
			}
		}
		return result;	
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
		excelSheetData.loadStudents(4, 28, 1);
		//System.out.println(excelSheetData.isValidateDate("31/12/1987"));
	}
}
