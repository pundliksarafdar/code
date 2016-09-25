package com.transaction.student;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.criteria.Predicate;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.batch.Batch;
import com.classapp.db.batch.BatchDB;
import com.classapp.db.batch.division.Division;
import com.classapp.db.fees.FeesDB;
import com.classapp.db.register.AdditionalStudentInfoBean;
import com.classapp.db.register.AdditionalStudentInfoDb;
import com.classapp.db.register.RegisterBean;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDB;
import com.classapp.db.student.StudentData;
import com.classapp.db.student.StudentDetails;
import com.classapp.db.student.StudentMarks;
import com.classapp.persistence.Constants;
import com.classapp.servicetable.ServiceMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.service.beans.AttendanceScheduleServiceBean;
import com.service.beans.StudentListForAttendance;
import com.service.beans.StudentRegisterServiceBean;
import com.transaction.attendance.AttendanceTransaction;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.register.RegisterTransaction;
import com.transaction.studentmarks.StudentMarksTransaction;

public class StudentTransaction {
	StudentData studentData;
	int class_id;
	public StudentTransaction() {
		this.studentData=new StudentData();		
	}
	
	public StudentTransaction(int class_id) {
		this.studentData=new StudentData();
		this.class_id=class_id;
	}
	
	public boolean addUpdateDb(Student student){
		boolean status = false;
		BatchDB batchDB = new BatchDB();
		if (!this.studentData.isStudentExistInClass(student)){
			String batchIDArray[] = student.getBatch_id().split(",");
			Map<String, Integer> map = new HashMap<String, Integer>();
			for (String batchID : batchIDArray) {
					String rollNoStatus = batchDB.getBatchRollNoStstatus(student.getClass_id(), student.getDiv_id(), Integer.parseInt(batchID));
					if(rollNoStatus != null)
					if("rollGenerated=yes;".equals(rollNoStatus)){
					int rollno = batchDB.getNextRollNo(student.getClass_id(), student.getDiv_id(), Integer.parseInt(batchID));
					map.put(batchID, rollno);
					}
				
			}
			Gson gson = new Gson();
			String rollNo = gson.toJson(map);
			student.setBatchIdNRoll(rollNo);
			this.studentData.addOrUpdateStudent(student);
			return true;
		}	
		return status;
	}
	
	public boolean saveStudentAdditionalInfo(StudentRegisterServiceBean bean){
		AdditionalStudentInfoBean additionalStudentInfoBean = new AdditionalStudentInfoBean();
		additionalStudentInfoBean.setInstId(bean.getStudent().getClass_id());
		additionalStudentInfoBean.setStudentId(bean.getStudent().getStudent_id());
		Gson gson = new Gson();
		String jsonStr = gson.toJson(bean.getAdditionalFormFields());
		additionalStudentInfoBean.setStudentData(jsonStr);
		additionalStudentInfoBean.setInstStudentId(bean.getStudentClassId());
		AdditionalStudentInfoDb db = new AdditionalStudentInfoDb();
		db.saveAdditionalStudentInfo(additionalStudentInfoBean);
		return true;
	}
	
	public HashMap<String, String> getAdditionalStudentInfoBean(int studentId,int instId){
		AdditionalStudentInfoDb db = new AdditionalStudentInfoDb();
		AdditionalStudentInfoBean bean = db.getAdditionalStudetnInfoBean(studentId,instId);
		Type type = new TypeToken<HashMap<String, String>>(){}.getType();
		Map<String, String> retMap = new Gson().fromJson(bean.getStudentData(),type);
		return (HashMap<String, String>) retMap;
	}
	
	public AdditionalStudentInfoBean getAdditionalStudentInfoBean_(int studentId,int instId){
		AdditionalStudentInfoDb db = new AdditionalStudentInfoDb();
		AdditionalStudentInfoBean bean = db.getAdditionalStudetnInfoBean(studentId,instId);
		return bean;
	}
	
	public RegisterBean getStudentDetailsFromID(Student student){		
			return this.studentData.getStudentDetailsFromID(student.getStudent_id());
			
	}
	
	public boolean updateStudentDb(Student student){
		boolean status = false;
		StudentDB studentDB = new  StudentDB();
		BatchDB batchDB = new BatchDB();
		if (this.studentData.isStudentExistInClass(student)){
			Student oldStudent = studentDB.getStudentByStudentID(student.getStudent_id(), student.getClass_id());
			if(oldStudent.getDiv_id() == student.getDiv_id() && (oldStudent.getBatchIdNRoll() != null && !"".equals(oldStudent.getBatchIdNRoll()))){
				if(!oldStudent.getBatch_id().equals(student.getBatch_id())){
					JsonParser jsonParser = new JsonParser();
					JsonObject jsonObject = jsonParser.parse(student.getBatchIdNRoll()).getAsJsonObject();
					String batchIDArray[] = student.getBatch_id().split(",");
					Map<String, Integer> map = new HashMap<String, Integer>();
					for (String batchID : batchIDArray) {
						if(jsonObject.has(batchID)){
							map.put(batchID, jsonObject.get(batchID).getAsInt());
						}else{
							
							String rollNoStatus = batchDB.getBatchRollNoStstatus(student.getClass_id(), student.getDiv_id(), Integer.parseInt(batchID));
							if(rollNoStatus != null)
							if("rollGenerated=yes;".equals(rollNoStatus)){
							int rollno = batchDB.getNextRollNo(student.getClass_id(), student.getDiv_id(), Integer.parseInt(batchID));
							map.put(batchID, rollno);
							}
						}
					}
					Gson gson = new Gson();
					String rollNo = gson.toJson(map);
					student.setBatchIdNRoll(rollNo);
				}
			}else{
				JsonParser jsonParser = new JsonParser();
				JsonObject jsonObject = jsonParser.parse(student.getBatchIdNRoll()).getAsJsonObject();
				String batchIDArray[] = student.getBatch_id().split(",");
				Map<String, Integer> map = new HashMap<String, Integer>();
				for (String batchID : batchIDArray) {
						String rollNoStatus = batchDB.getBatchRollNoStstatus(student.getClass_id(), student.getDiv_id(), Integer.parseInt(batchID));
						if(rollNoStatus != null)
						if("rollGenerated=yes;".equals(rollNoStatus)){
						int rollno = batchDB.getNextRollNo(student.getClass_id(), student.getDiv_id(), Integer.parseInt(batchID));
						map.put(batchID, rollno);
						}
					
				}
				Gson gson = new Gson();
				String rollNo = gson.toJson(map);
				student.setBatchIdNRoll(rollNo);
			}
			this.studentData.addOrUpdateStudent(student);
			return true;
		}	
		return status;
	}

	public boolean deleteStudent(int student_id, int inst_id) {
		AttendanceTransaction attendanceTransaction = new  AttendanceTransaction();
		attendanceTransaction.deleteAttendanceRelatedToStudent(inst_id, student_id);
		FeesDB feesDB =new FeesDB();
		feesDB.deleteStudentFeesRelatedToStudent(inst_id, student_id);
		feesDB.deleteStudentFeesTransactionRelatedToStudent(inst_id, student_id);
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		marksTransaction.deleteStudentMarksrelatedtostudentID(inst_id, student_id);
		StudentDB studentDB = new StudentDB();
		studentDB.deleteStudent(student_id, inst_id);
		return true;
	}
	public RegisterBean getStudent(String studentLoginName){
		return  this.studentData.getStudent(studentLoginName);
	}
	
	public Student getStudentDetailsFromClass(int student_id){
		return  this.studentData.getStudentDetailsFromClass(student_id,class_id);
	}
		
	public List<StudentDetails> getAllStudentDetailsFromID(){
		return  this.studentData.getAllStudentDetailsFromID(class_id);
	}
	public List<Student> getStudent(int regID) {
		StudentDB studentDB=new StudentDB();
		List<Student> list=studentDB.getStudentinfo(regID);
		return list;
		
	}
	
	public Student getclassStudent(int studentID,int classID) {
		StudentDB studentDB=new StudentDB();
		Student list=studentDB.getclassStudent(studentID,classID);
		return list;
		
	}
	
	public void removeBatchFromstudentslist(String Batchname,int inst_id,int div_id) {
		StudentDB db=new StudentDB();
		List<Student> students=db.getStudentrelatedtoBatch(Batchname,inst_id,div_id);
		if(students!=null)
		{
			for (int i = 0; i < students.size(); i++) 
			{
				String[] batchids=students.get(i).getBatch_id().split(",");
				String ids="";
				int index=1;
				for (int j = 0; j < batchids.length; j++) {
					
					if(!batchids[j].equals(Batchname)){
						if(index==1)
						{
							ids=batchids[j];
						}else{
							ids=ids+","+batchids[j];
						}
						index++;
					}
					
				}
				students.get(i).setBatch_id(ids);
				db.updateDb(students.get(i));
			}
			
		}
	}
	
	
	public boolean removebatchfromstudentlist(int classid) {
		StudentDB db=new StudentDB();
		//db.deletestudentrelatedtoclass(classid);
	return true;
	}
	
	public List<Student> getStudentsrelatedtobatch(String batchname,int inst_id,int div_id) {
	StudentDB studentDB=new StudentDB();
	List students =studentDB.getStudentIDSrelatedtoBatch(batchname,inst_id,div_id);
	return students;
	}
	
	/*This method is same as above but returning generic*/
	public List<com.service.beans.Student> getStudentsrelatedtobatchInDataLayer(String batchname,int inst_id,int div_id) {
		StudentDB studentDB=new StudentDB();
		 List<Student> students = studentDB.getStudentIDSrelatedtoBatch(batchname,inst_id,div_id);
		 List<com.service.beans.Student>studentsDataLayer = new ArrayList<com.service.beans.Student>();
		 for(int index = 0;index<students.size();index++){
			 com.service.beans.Student student = new com.service.beans.Student();
			 try {
				BeanUtils.copyProperties(student, students.get(index));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			studentsDataLayer.add(student); 
		 }
		return studentsDataLayer;
		}
	
	public List getUnallocatedStudentIDs(int inst_id) {
		StudentDB studentDB=new StudentDB();
		List students =studentDB.getUnallocatedStudentIDs(inst_id);
		return students;
		}
	
	public Integer getStudentscountrelatedtobatch(String batchname,int inst_id,int div_id) {
		StudentDB db=new StudentDB();
		return db.getStudentcountrelatedtobatch(batchname, inst_id, div_id);
		
	}
	
	public Integer getunallocatedStudentcount(int inst_id) {
		StudentDB db=new StudentDB();
		return db.getunallocatedStudentcount(inst_id);
		
	}
	
	public List<Integer> getStudentsFromBatches(int class_id,int div_id,String batchids) {
		StudentDB db=new StudentDB();
		return db.getStudentsFromBatches(class_id, div_id, batchids);
		
	}
	
	public Integer getStudentCount(int regID){
		StudentDB db=new StudentDB();
		return db.getStudentCount(regID);
	}
	
	public Student getStudentByStudentID(String studentID,int class_id) {
		StudentDB studentDB=new StudentDB();
		return studentDB.getStudentByStudentID(studentID,class_id);
	}
	
	public Student getStudentByStudentID(int studentID,int class_id) {
		StudentDB studentDB=new StudentDB();
		return studentDB.getStudentByStudentID(studentID,class_id);
	}
	
	public List<Student> getStudentByStudentIDs(List<Integer> studentID,int class_id) {
		StudentDB studentDB=new StudentDB();
		return studentDB.getStudentByStudentIDs(studentID,class_id);
	}
	
	public boolean addStudentByID(int inst_id,com.service.beans.Student serviceStudent) {
		serviceStudent.setClass_id(inst_id);
		Student student = new Student();
		try {
			BeanUtils.copyProperties(student, serviceStudent);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return addUpdateDb(student);
	}
	
	public List<StudentDetails> getAllStudentsDetails(String batchID,String batchdivision,int instId){
		StudentTransaction studentTransaction=new StudentTransaction();
		int count=0;
		if("".equals(batchID)){
			count=studentTransaction.getunallocatedStudentcount(instId);
		}else{
			count=studentTransaction.getStudentscountrelatedtobatch(batchID,instId,Integer.parseInt(batchdivision));
		}
		
		//Taking data from database
		/*int resultPerPage = Integer.parseInt(ServiceMap.getSystemParam(Constants.SERVICE_PAGINATION, "resultsperpage"));*/
		if(count>0){
			List<Student> students=new ArrayList();
			if("".equals(batchID)){
				students=studentTransaction.getUnallocatedStudentIDs(instId);
			}else{
				students=studentTransaction.getStudentsrelatedtobatch(batchID,instId,Integer.parseInt(batchdivision));
			}
			List<Integer> studentIDsList = new ArrayList<Integer>();
			for (int i = 0; i < students.size(); i++) {
				studentIDsList.add(students.get(i).getStudent_id());
			}
			RegisterTransaction registerTransaction=new RegisterTransaction();
			List<RegisterBean> registerBeans= registerTransaction.getStudentsInfo(studentIDsList);
			BatchTransactions batchTransactions=new BatchTransactions();
			List<Batch> batchList = batchTransactions.getBatchRelatedtoDivision(Integer.parseInt(batchdivision));
			List<StudentDetails> studentDetailsList = new ArrayList<StudentDetails>();
			DivisionTransactions divisionTransactions=new DivisionTransactions();
			Division division = divisionTransactions.getDidvisionByID(Integer.parseInt(batchdivision));
			if(students != null){
				for (int i = 0; i < students.size(); i++) {
					StudentDetails studentDetails=new StudentDetails();
					registerBeans.get(i).setLoginPass("");
					studentDetails.setStudentId(students.get(i).getStudent_id());
					studentDetails.setStudentUserBean(registerBeans.get(i));
					studentDetails.setDivision(division);
					studentDetails.setStudent(students.get(i));
					String batchIDArray[] = students.get(i).getBatch_id().split(",");
					List<Batch> studentBatchList = new ArrayList<Batch>();
					if(batchIDArray.length > 1){
						for (int j = 0; j < batchIDArray.length; j++) {
								for (int k = 0; k < batchList.size(); k++) {
									if(Integer.parseInt(batchIDArray[j]) ==  batchList.get(k).getBatch_id()){
										studentBatchList.add(batchList.get(k));
										break;
									}
								}	
						}
					}else{
						for (int k = 0; k < batchList.size(); k++) {
							if(Integer.parseInt(batchIDArray[0]) ==  batchList.get(k).getBatch_id()){
								studentBatchList.add(batchList.get(k));
								break;
							}
						}
					}
					studentDetails.setBatches(studentBatchList);
					studentDetailsList.add(studentDetails);
				}
			}
			return studentDetailsList;
		}else{
			List<StudentDetails> studentDetailsList = new ArrayList<StudentDetails>();
			return studentDetailsList ;
		}
		
	}
	
	public List<StudentDetails> generateRollNumber(String batchID,String batchdivision,int instId){
		List<StudentDetails> studentDetails = getAllStudentsDetails(batchID,batchdivision,instId);
		Collections.sort(studentDetails, new Comparator<StudentDetails>() {
			@Override
			public int compare(StudentDetails o1, StudentDetails o2) {
				return (o1.getStudentUserBean().getFname()+o1.getStudentUserBean().getLname()).toLowerCase().compareTo((o2.getStudentUserBean().getFname()+o2.getStudentUserBean().getLname()).toLowerCase());
			}
		});
		return studentDetails;
	}
	
	public List updateStudentRollNumber(String batchname,int inst_id,int div_id,List<StudentDetails> sDList){
		List<Student> students = getStudentsrelatedtobatch(batchname, inst_id, div_id);
		int rollNumber = 1;
		for(StudentDetails studentDetails:sDList){
			for(Student student:students){
				if(studentDetails.getStudentUserBean().getRegId()==student.getStudent_id()){
					String batchIDNRoll = student.getBatchIdNRoll();
					JsonParser jsonParser = new JsonParser();
					JsonObject object = new JsonObject();
					if(null!=batchIDNRoll){
						JsonElement element = jsonParser.parse(batchIDNRoll);
						object = element.getAsJsonObject();
					}object.addProperty(batchname, rollNumber);
					rollNumber++;
					student.setBatchIdNRoll(object.toString());
					break;
				}
			}			
		}
		StudentDB studentDB = new StudentDB();
		studentDB.updateDb(students);
		BatchDB batchDB = new BatchDB();
		batchDB.updateBatch(inst_id, div_id, Integer.parseInt(batchname), rollNumber-1);
		return students;
	}
	
	public List<com.service.beans.StudentData> getStudentForExamMarks(String batchid, int inst_id, int div_id,int exam_id) {
		StudentDB db = new StudentDB();
	//	List<com.service.beans.StudentData> attendanceScheduleServiceBeanList = new ArrayList<com.service.beans.StudentData>(); 
		List list =  db.getStudentrelatedtoBatchForExamMarks(batchid, inst_id, div_id);
		List<com.service.beans.StudentData> studentDatas = new ArrayList<com.service.beans.StudentData>();
		if(list != null){
			int i = 1;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				com.service.beans.StudentData bean = new com.service.beans.StudentData();
				bean.setFname((String) object[0]);
				bean.setLname((String) object[1]);
				bean.setStudent_id(((Number) object[2]).intValue());	
				try{
				JsonParser parser = new JsonParser();
				JsonObject json = (JsonObject) parser.parse((String) object[3]);
				int roll_no = json.get(batchid).getAsInt();
				bean.setRoll_no(roll_no);
				}catch(Exception e){
					bean.setRoll_no(0);	
				}
				bean.setDiv_id(div_id);
				bean.setInst_id(inst_id);
				bean.setBatch_id(Integer.parseInt(batchid));
				bean.setExam_id(exam_id);
				studentDatas.add(bean);
			}
		}
		return studentDatas;
	}
	
	public List<com.service.beans.StudentData> getStudentForExamMarksUpdate(String batchid, int inst_id, int div_id,int exam_id,int sub_id) {
		StudentDB db = new StudentDB();
	//	List<com.service.beans.StudentData> attendanceScheduleServiceBeanList = new ArrayList<com.service.beans.StudentData>(); 
		List list =  db.getStudentrelatedtoBatchForExamMarksUpdate(batchid, inst_id, div_id, exam_id, sub_id);
		List  studentList =db.getStudentrelatedtoBatchForExamMarks(batchid, inst_id, div_id);
		List<com.service.beans.StudentData> studentDatas = new ArrayList<com.service.beans.StudentData>();
		if(studentList != null){
			int i = 1;
			for (Iterator iterator2 = studentList.iterator(); iterator2.hasNext();) {
				Object[] obj = (Object[]) iterator2.next();
				boolean flag = false;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				if(((Number) object[2]).intValue() == ((Number) obj[2]).intValue()){
				com.service.beans.StudentData bean = new com.service.beans.StudentData();
				bean.setFname((String) object[0]);
				bean.setLname((String) object[1]);
				bean.setStudent_id(((Number) object[2]).intValue());
				try{
				JsonParser parser = new JsonParser();
				JsonObject json = (JsonObject) parser.parse((String) obj[3]);
				int roll_no = json.get(batchid).getAsInt();
				bean.setRoll_no(roll_no);
				}catch(Exception e){
				bean.setRoll_no(0);	
				}
				bean.setDiv_id(div_id);
				bean.setInst_id(inst_id);
				bean.setBatch_id(Integer.parseInt(batchid));
				bean.setMarks(((Number) object[3]).intValue());
				bean.setExam_id(exam_id);
				studentDatas.add(bean);
				flag = true;
				break;
				}
			}
			if(flag == false){
				com.service.beans.StudentData bean = new com.service.beans.StudentData();
				bean.setFname((String) obj[0]);
				bean.setLname((String) obj[1]);
				bean.setStudent_id(((Number) obj[2]).intValue());
				try{
				JsonParser parser = new JsonParser();
				JsonObject json = (JsonObject) parser.parse((String) obj[3]);
				int roll_no = json.get(batchid).getAsInt();
				bean.setRoll_no(roll_no);
				}catch(Exception e){
					bean.setRoll_no(0);	
				}
				bean.setDiv_id(div_id);
				bean.setInst_id(inst_id);
				bean.setBatch_id(Integer.parseInt(batchid));
				bean.setMarks(0);
				bean.setExam_id(exam_id);
				studentDatas.add(bean);
			}
			}
		}
		return studentDatas;
	}
	
	public boolean saveStudentMarks(List<com.service.beans.StudentData> studentDataList) {
		StudentDB db = new StudentDB();
		for (Iterator iterator = studentDataList.iterator(); iterator.hasNext();) {
			com.service.beans.StudentData studentData = (com.service.beans.StudentData) iterator.next();
			StudentMarks studentMarks = new StudentMarks();
			
			try {
				BeanUtils.copyProperties(studentMarks, studentData);
				db.saveStudentMarks(studentMarks);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public List<com.service.beans.StudentData> getStudentForProgress(String batchid, int inst_id, int div_id) {
		StudentDB db = new StudentDB();
	//	List<com.service.beans.StudentData> attendanceScheduleServiceBeanList = new ArrayList<com.service.beans.StudentData>(); 
		List list =  db.getStudentrelatedtoBatchForProgressCard(batchid, inst_id, div_id);
		List<com.service.beans.StudentData> studentDatas = new ArrayList<com.service.beans.StudentData>();
		if(list != null){
			int i = 1;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				com.service.beans.StudentData bean = new com.service.beans.StudentData();
				bean.setFname((String) object[0]);
				bean.setLname((String) object[1]);
				bean.setStudent_id(((Number) object[2]).intValue());	
				bean.setRoll_no(i++);
				studentDatas.add(bean);
			}
		}
		return studentDatas;
	}
	
	public boolean updateStudentRelatedToClass(int inst_id,int div_id) {
		StudentDB studentDB = new StudentDB();
		return studentDB.updatestudentrelatedtoclass(div_id, inst_id);
	}
	
}
