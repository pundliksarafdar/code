package com.transaction.studentmarks;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.batch.Batch;
import com.classapp.db.batch.division.Division;
import com.classapp.db.exam.Exam;
import com.classapp.db.exam.ExamDB;
import com.classapp.db.exam.ExamPaperDB;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDB;
import com.classapp.db.student.StudentMarks;
import com.classapp.db.subject.StudentMarksDB;
import com.classapp.db.subject.Subject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.beans.BatchExamDistinctSubjects;
import com.service.beans.BatchExamSubjects;
import com.service.beans.BatchExams;
import com.service.beans.BatchStudentExamMarks;
import com.service.beans.ExamSubject;
import com.service.beans.ExamWiseStudentDetails;
import com.service.beans.ProgressCardServiceBean;
import com.service.beans.StudentData;
import com.service.beans.StudentDetailMarks;
import com.service.beans.StudentProgressCard;
import com.service.beans.StudentSubjectMarks;
import com.service.beans.SubjectsExam;
import com.transaction.batch.BatchTransactions;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.student.StudentTransaction;

public class StudentMarksTransaction {

	public boolean saveStudentMarks(StudentMarks studentMarks) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.saveStudentMarks(studentMarks);
	}
	
	public List<StudentMarks> getStudentMarksList(int inst_id,int student_id,int sub_id,int div_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.getStudentMarksList(inst_id, student_id, sub_id, div_id);
	}
	
	public boolean isExamSolvedByStudent(int exam_id,int inst_id,int sub_id,int div_id,int student_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.isExamSolvedByStudent(exam_id, inst_id, sub_id, div_id, student_id);
	}
	public int getStudentMarksCount(int inst_id,List<Integer> student_id,int sub_id,int div_id,int examID) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.getStudentMarksCount(inst_id, student_id, sub_id, div_id,examID);
	}
	
	public List<StudentMarks> getStudentMarks(int inst_id,List<Integer> student_id,int sub_id,int div_id,int currentPage,int examID) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.getStudentMarks(inst_id, student_id, sub_id, div_id,currentPage,examID);
	}
	
	public boolean deleteStudentMarksrelatedtosubject(int inst_id,int sub_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtosubject(inst_id,sub_id);
	}
	
	public boolean deleteStudentMarksrelatedtodivision(int inst_id,int div_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtodivision(inst_id,div_id);
	}
	
	public boolean deleteStudentMarksrelatedtobatch(int inst_id,int div_id,int batch_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtobatch(inst_id, div_id, batch_id);
	}
	
	public boolean deleteStudentMarksrelatedtoexam(int inst_id,int exam_ID) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtoexam(inst_id, exam_ID);
	}
	
	public boolean deleteStudentMarksrelatedtostudentID(int inst_id,int student_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtostudentID(inst_id, student_id);
	}
	
	public StudentMarks getStudentExamMark(int inst_id,int student_id,int div_id,int sub_id,int exam_ID) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.getStudentExamMark(inst_id, student_id, sub_id, div_id, exam_ID);
	}
	
	public BatchStudentExamMarks getStudentExamMarks(int inst_id, int div_id,int batch_id, int exam_id) {
		ExamPaperDB db = new ExamPaperDB();
		List<ExamSubject> subjectlist = new ArrayList<ExamSubject>(); 
		List list =  db.getExamSubjects(inst_id, div_id, batch_id, exam_id);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object [] object = (Object []) iterator.next();
			ExamSubject subject = new ExamSubject();
			subject.setSubjectName((String)object[0]);
			subject.setSubjectId(((Number)object[1]).intValue());
			subject.setMarks(((Number)object[2]).intValue());
			subjectlist.add(subject);
		}
		StudentMarksDB marksDB=new StudentMarksDB();
		BatchStudentExamMarks batchStudentExamMarks = new BatchStudentExamMarks();
		List<StudentData> dataList = new ArrayList<StudentData>();
		List marksList =  marksDB.getStudentExamMarks(inst_id, div_id, batch_id, exam_id);
		StudentDB studentDB = new StudentDB();
		List students =  studentDB.getStudentrelatedtoBatchForExamMarks(batch_id+"", inst_id, div_id);
		for (Iterator iterator = students.iterator(); iterator.hasNext();) {
			Object[] obj = (Object[]) iterator.next();
			for (Iterator iterator2 = subjectlist.iterator(); iterator2.hasNext();) {
				ExamSubject examSubject = (ExamSubject) iterator2.next();
				 boolean flag = false;
				for (Iterator iterator3 = marksList.iterator(); iterator3.hasNext();) {
					Object[] object = (Object[]) iterator3.next();
					if((examSubject.getSubjectId() == ((Number)object[4]).intValue()) && ((Number)object[5]).intValue()==((Number)obj[2]).intValue()){
						StudentData data = new StudentData();
						data.setFname((String) object[0]);
						data.setLname((String) object[1]);
						data.setMarks(((Number)object[2]).intValue());
						try{
						JsonParser parser = new JsonParser();
						JsonObject json = (JsonObject) parser.parse((String) obj[3]);
						int roll_no = json.get(batch_id+"").getAsInt();
						data.setRoll_no(roll_no);
						}catch(Exception e){
							data.setRoll_no(0);
						}
						dataList.add(data);
						flag = true;
						break;
					}
				}
				if(flag == false){
					StudentData data = new StudentData();
					data.setFname((String) obj[0]);
					data.setLname((String) obj[1]);
					data.setMarks(0);
					try{
					JsonParser parser = new JsonParser();
					JsonObject json = (JsonObject) parser.parse((String) obj[3]);
					int roll_no = json.get(batch_id+"").getAsInt();
					data.setRoll_no(roll_no);
					}catch(Exception e){
						data.setRoll_no(0);	
					}
					dataList.add(data);
				}
			}
		}
	//	boolean flag = true;
		/*Object[] object = null;
		
		for (Iterator iterator = marksList.iterator(); iterator.hasNext();) {
			 object = (Object[]) iterator.next();
			
			for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
				Object[] obj = (Object[]) iterator2.next();
				if(((Number)obj[1]).intValue() == ((Number)object[4]).intValue()){
					StudentData data = new StudentData();
					data.setFname((String) object[0]);
					data.setLname((String) object[1]);
					data.setMarks(((Number)object[2]).intValue());
					dataList.add(data);
					
					break;
				}
			}
			if(flag == false){
				
			}
			
		}*/
		batchStudentExamMarks.setExamSubjectList(subjectlist);
		batchStudentExamMarks.setStudentDataList(dataList);
		return batchStudentExamMarks;
	}
	
	public StudentSubjectMarks getStudentExamSubjectMarks(int inst_id, int div_id,int batch_id, int sub_id) {
		ExamPaperDB examPaperDB = new ExamPaperDB();
		List examList = examPaperDB.getSubjectsExam(inst_id, div_id, batch_id, sub_id);
		StudentMarksDB db = new StudentMarksDB();
		List list =  db.getStudentExamSubjectMarks(inst_id, div_id, batch_id, sub_id);
		List<SubjectsExam> subjectExamList = new ArrayList<SubjectsExam>();
		for (Iterator iterator = examList.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			SubjectsExam subjectsExam = new SubjectsExam();
			subjectsExam.setExam_id(((Number)object[0]).intValue());
			subjectsExam.setExam_name((String) object[1]);
			subjectExamList.add(subjectsExam);
		}
		StudentDB studentDB = new StudentDB();
		List<StudentData> dataList = new ArrayList<StudentData>();
		List students =  studentDB.getStudentrelatedtoBatchForExamMarks(batch_id+"", inst_id, div_id);
		for (Iterator iterator = students.iterator(); iterator.hasNext();) {
			Object[] obj = (Object[]) iterator.next();
			for (Iterator iterator2 = subjectExamList.iterator(); iterator2.hasNext();) {
				SubjectsExam subjectsExam = (SubjectsExam) iterator2.next();
				boolean flag = false;
				for (Iterator iterator3 = list.iterator(); iterator3.hasNext();) {
					Object[] object = (Object[]) iterator3.next();
					if(((Number)object[4]).intValue() == subjectsExam.getExam_id() && ((Number)object[5]).intValue() == ((Number)obj[2]).intValue()){
						StudentData data = new StudentData();
						data.setFname((String) object[0]);
						data.setLname((String) object[1]);
						data.setMarks(((Number)object[2]).intValue());
						try{
						JsonParser parser = new JsonParser();
						JsonObject json = (JsonObject) parser.parse((String) obj[3]);
						int roll_no = json.get(batch_id+"").getAsInt();
						data.setRoll_no(roll_no);
						}catch(Exception e){
							data.setRoll_no(0);	
						}
						dataList.add(data);
						flag = true;
						break;
					}
				}
				if(flag == false){
					StudentData data = new StudentData();
					data.setFname((String) obj[0]);
					data.setLname((String) obj[1]);
					data.setMarks(0);
					try{
					JsonParser parser = new JsonParser();
					JsonObject json = (JsonObject) parser.parse((String) obj[3]);
					int roll_no = json.get(batch_id+"").getAsInt();
					data.setRoll_no(roll_no);
					}catch(Exception e){
						data.setRoll_no(0);	
					}
					dataList.add(data);
				}
			}
		}
		
		StudentSubjectMarks studentSubjectMarks = new StudentSubjectMarks();
		studentSubjectMarks.setExamList(subjectExamList);
		studentSubjectMarks.setStudentDataList(dataList);
		return studentSubjectMarks;
	}
	
	public ProgressCardServiceBean getStudentProgressCard(int inst_id,int div_id,int batch_id,String exam_ids,int student_id) {
		int[] numbers = Arrays.stream(exam_ids.split(","))
                .map(String::trim).mapToInt(Integer::parseInt).toArray();
		List<Integer> list= Arrays.stream(numbers)
			      .boxed()
			      .collect(Collectors.toList());
		ExamDB examDB = new ExamDB();
		List<Exam> exams = examDB.getExamList(inst_id, list);
		ExamPaperDB examPaperDB = new ExamPaperDB();
		List subjects =  examPaperDB.getDistinctExamSubjects(inst_id, div_id, batch_id, list);
		List examSubjectsList = examPaperDB.getExamSubjects(inst_id, div_id, batch_id, list);
		StudentMarksDB marksDB = new StudentMarksDB();
		List marksList = marksDB.getStudentExamMarks(inst_id, div_id, batch_id, list, student_id);
		List<com.service.beans.Exam> examList = new ArrayList<com.service.beans.Exam>();
		for (Iterator iterator = exams.iterator(); iterator.hasNext();) {
			Exam exam = (Exam) iterator.next();
			com.service.beans.Exam examService = new com.service.beans.Exam();
			try {
				BeanUtils.copyProperties(examService,exam );
				examList.add(examService);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<com.datalayer.subject.Subject> distinctSubjectsList = new ArrayList<com.datalayer.subject.Subject>();
		for (Iterator iterator = subjects.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			com.datalayer.subject.Subject subject = new com.datalayer.subject.Subject();
			subject.setSubjectId(((Number)object[0]).intValue());
			subject.setSubjectName((String) object[1]);
			distinctSubjectsList.add(subject);
		}
		List<ExamSubject> examSubjectServiceList = new ArrayList<ExamSubject>();
		for (Iterator iterator = examSubjectsList.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			ExamSubject examSubject = new ExamSubject();
			examSubject.setExam_id(((Number)object[0]).intValue());
			examSubject.setSubjectId(((Number)object[1]).intValue());
			examSubject.setMarks(((Number)object[2]).intValue());
			examSubjectServiceList.add(examSubject);
		}
		
		List<StudentData> dataList = new ArrayList<StudentData>();
		for (com.service.beans.Exam exam : examList) {
		for (ExamSubject examSubject : examSubjectServiceList) {
			if(examSubject.getExam_id() == exam.getExam_id()){
			boolean flag = false;
		for (Iterator iterator = marksList.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			if(examSubject.getSubjectId() == ((Number)object[4]).intValue() && examSubject.getExam_id()== ((Number)object[5]).intValue()){
			StudentData data = new StudentData();
			data.setStudent_id(((Number)object[0]).intValue());
			data.setFname((String) object[1]);
			data.setLname((String) object[2]);
			data.setMarks(((Number)object[3]).intValue());
			data.setSub_id(((Number)object[4]).intValue());
			data.setExam_id(((Number)object[5]).intValue());
			data.setExamPresentee("P");
			dataList.add(data);
			flag = true;
			break;
		}
		}
		if(flag== false){
			StudentData data = new StudentData();
			data.setStudent_id(student_id);
			data.setMarks(0);
			data.setSub_id(examSubject.getSubjectId());
			data.setExam_id(examSubject.getExam_id());
			data.setExamPresentee("A");
			dataList.add(data);
		}
		}/*else{
			StudentData data = new StudentData();
			data.setStudent_id(student_id);
			data.setMarks(0);
			data.setExamPresentee("");
			data.setSub_id(examSubject.getSubjectId());
			data.setExam_id(exam.getExam_id());
			dataList.add(data);
		}*/
		}	
		}
		
		for (com.service.beans.Exam exam : examList) {
			int total_marks = 0;
			for (ExamSubject examSubject : examSubjectServiceList) {
				if(exam.getExam_id()==examSubject.getExam_id()){
					total_marks = total_marks +examSubject.getMarks();
				}
			}
			exam.setTotal_marks(total_marks);
		}
		ProgressCardServiceBean cardServiceBean = new ProgressCardServiceBean();
		cardServiceBean.setExamList(examList);
		cardServiceBean.setExamSubjectList(examSubjectServiceList);
		cardServiceBean.setStudentDataList(dataList);
		cardServiceBean.setSubjectList(distinctSubjectsList);
		return cardServiceBean;
		
	}
	
	public List<StudentProgressCard> sendStudentProgressCard(int inst_id,int div_id,int batch_id,String exam_ids) {
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		Division division =divisionTransactions.getDidvisionByID(div_id);
		BatchTransactions batchTransactions = new BatchTransactions();
		Batch batch = batchTransactions.getBatch(batch_id, inst_id, div_id);
		int[] numbers = Arrays.stream(exam_ids.split(","))
                .map(String::trim).mapToInt(Integer::parseInt).toArray();
		List<Integer> list= Arrays.stream(numbers)
			      .boxed()
			      .collect(Collectors.toList());
		ExamDB examDB = new ExamDB();
		List<Exam> exams = examDB.getExamList(inst_id, list);
		ExamPaperDB examPaperDB = new ExamPaperDB();
		List subjects =  examPaperDB.getDistinctExamSubjects(inst_id, div_id, batch_id, list);
		List examSubjectsList = examPaperDB.getExamSubjects(inst_id, div_id, batch_id, list);
		StudentMarksDB marksDB = new StudentMarksDB();
	//	List<StudentMarks> marksList = marksDB.getStudentsExamMarks(inst_id, div_id, batch_id, list);
		List<com.service.beans.Exam> examList = new ArrayList<com.service.beans.Exam>();
		for (Iterator iterator = exams.iterator(); iterator.hasNext();) {
			Exam exam = (Exam) iterator.next();
			com.service.beans.Exam examService = new com.service.beans.Exam();
			try {
				BeanUtils.copyProperties(examService,exam );
				examList.add(examService);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<com.datalayer.subject.Subject> distinctSubjectsList = new ArrayList<com.datalayer.subject.Subject>();
		for (Iterator iterator = subjects.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			com.datalayer.subject.Subject subject = new com.datalayer.subject.Subject();
			subject.setSubjectId(((Number)object[0]).intValue());
			subject.setSubjectName((String) object[1]);
			distinctSubjectsList.add(subject);
		}
		List<ExamSubject> examSubjectServiceList = new ArrayList<ExamSubject>();
		for (Iterator iterator = examSubjectsList.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			ExamSubject examSubject = new ExamSubject();
			examSubject.setExam_id(((Number)object[0]).intValue());
			examSubject.setSubjectId(((Number)object[1]).intValue());
			examSubject.setMarks(((Number)object[2]).intValue());
			examSubjectServiceList.add(examSubject);
		}
		
		List<StudentData> dataList = new ArrayList<StudentData>();
		for (com.service.beans.Exam exam : examList) {
			int total_marks = 0;
			for (ExamSubject examSubject : examSubjectServiceList) {
				if(exam.getExam_id()==examSubject.getExam_id()){
					total_marks = total_marks +examSubject.getMarks();
				}
			}
			exam.setTotal_marks(total_marks);
		}
		
		StudentTransaction studentTransaction = new StudentTransaction();
		List<StudentData> studentDatas = studentTransaction.getStudentForProgress(batch_id+"", inst_id, div_id);
		List<StudentMarks> marksList = marksDB.getStudentsExamMarks(inst_id, div_id, batch_id, list);
		
		
		for (StudentData studentData : studentDatas) {
			for (Iterator iterator = examList.iterator(); iterator.hasNext();) {
				com.service.beans.Exam exam = (com.service.beans.Exam) iterator.next();
				for (Iterator iterator2 = examSubjectServiceList.iterator(); iterator2.hasNext();) {
					ExamSubject examSubject = (ExamSubject) iterator2.next();
					if(examSubject.getExam_id() == exam.getExam_id()){
						boolean flag = false;
						
						List<StudentMarks> studentMarksObj = marksList.stream().filter(studentMarks->(studentMarks.getStudent_id() == studentData.getStudent_id()
								&& studentMarks.getSub_id() == examSubject.getSubjectId()
								&& studentMarks.getExam_id() == examSubject.getExam_id())).collect(Collectors.toList());
						
						if(!studentMarksObj.isEmpty()){
							StudentData data = new StudentData();
							data.setStudent_id(studentData.getStudent_id());
							data.setFname(studentData.getFname());
							data.setLname(studentData.getLname());
							data.setMarks(studentMarksObj.get(0).getMarks());
							data.setSub_id(studentMarksObj.get(0).getSub_id());
							data.setExam_id(exam.getExam_id());
							data.setExamPresentee("P");
							dataList.add(data);
							flag = true;
						}
						if(flag== false){
							StudentData data = new StudentData();
							data.setStudent_id(studentData.getStudent_id());
							data.setMarks(0);
							data.setSub_id(examSubject.getSubjectId());
							data.setExam_id(examSubject.getExam_id());
							data.setExamPresentee("A");
							dataList.add(data);
						}
					}
				}
			}
		}
		 DecimalFormat df2 = new DecimalFormat("###.##");
		List<StudentProgressCard> studentProgressCardList = new ArrayList<StudentProgressCard>();
		List<Integer> result = studentDatas.stream().map(StudentData::getStudent_id).collect(Collectors.toList());
		for (StudentData studentData : studentDatas) {
			StudentProgressCard studentProgressCard = new StudentProgressCard();
			studentProgressCard.setStudent_id(studentData.getStudent_id());
			studentProgressCard.setSubject_name(distinctSubjectsList.stream().map(com.datalayer.subject.Subject::getSubjectName).collect(Collectors.toList()));
			studentProgressCard.setStudent_name(studentData.getFname()+" "+studentData.getLname());
			List<String[]> dataArrayList = new ArrayList<String[]>();
			for (com.service.beans.Exam exam : examList) {
				int pos=0;
				String examData[] = new String[distinctSubjectsList.size()+4];
				examData[pos] =exam.getExam_name();
				int total_marks = 0;
				for (com.datalayer.subject.Subject subject : distinctSubjectsList) {
					pos++;
					List<StudentData> studentMarksObj = dataList.stream().filter(
							studentMarks->(studentMarks.getStudent_id() == studentData.getStudent_id()
							&& studentMarks.getSub_id() == subject.getSubjectId() 
							&& studentMarks.getExam_id() == exam.getExam_id())).collect(Collectors.toList());
					if(!studentMarksObj.isEmpty()){
						if("P".equals(studentMarksObj.get(0).getExamPresentee())){
						examData[pos] = String.valueOf(studentMarksObj.get(0).getMarks());
						total_marks = total_marks + studentMarksObj.get(0).getMarks();
					}else{
						examData[pos] = "A";
					}
					}else{
						examData[pos] = "-";
					}	
				
				}
				examData[++pos] = String.valueOf(total_marks);
				examData[++pos] = String.valueOf(exam.getTotal_marks());
				examData[++pos] = String.valueOf(df2.format(((float)total_marks/(float)exam.getTotal_marks())*100)+"%");
				dataArrayList.add(examData);
			}
			studentProgressCard.setDataList(dataArrayList);
			studentProgressCard.setBatch_name(batch.getBatch_name());
			studentProgressCard.setClass_name(division.getDivisionName());
			studentProgressCardList.add(studentProgressCard);
		}
		return studentProgressCardList;
		
	}
	
	public ExamWiseStudentDetails getStudentDetailedMarks(int inst_id,int student_id) {
		StudentTransaction studentTransaction= new StudentTransaction();
		Student student =studentTransaction.getStudentByStudentID(student_id,inst_id);
		int div_id = student.getDiv_id();
		ExamPaperDB examPaperDB = new ExamPaperDB();
		List<Integer> batchList = Stream.of(student.getBatch_id().split(","))
		        .map(Integer::parseInt)
		        .collect(Collectors.toList());
		List batchExams =examPaperDB.getDistinctBatchExam(inst_id, student.getDiv_id(), batchList);
		List<BatchExams> batchExamList = new ArrayList<BatchExams>();
		for (Iterator iterator = batchExams.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			BatchExams batchExam = new BatchExams();
			batchExam.setExam_id(((Number)object[0]).intValue());
			batchExam.setBatch_id(((Number)object[1]).intValue());
			batchExam.setExam_name((String) object[2]);
			batchExamList.add(batchExam);
		}
		List<Integer> examIds = batchExamList.stream().map(BatchExams::getExam_id).distinct().collect(Collectors.toList());
		ExamWiseStudentDetails examWiseStudentDetails = new ExamWiseStudentDetails();
		if(examIds.size()>0){
		List distinctSubjects = examPaperDB.getDistinctBatchExamSubjects(inst_id, div_id, batchList, examIds);
		List<BatchExamDistinctSubjects> batchExamDistinctSubjectList = new ArrayList<BatchExamDistinctSubjects>();
		for (Iterator iterator = distinctSubjects.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			BatchExamDistinctSubjects batchExamDistinctSubjects = new BatchExamDistinctSubjects();
			batchExamDistinctSubjects.setSubject_id(((Number)object[0]).intValue());
			batchExamDistinctSubjects.setSubject_name((String) object[1]);
			batchExamDistinctSubjects.setBatch_id(((Number)object[2]).intValue());
			batchExamDistinctSubjectList.add(batchExamDistinctSubjects);
		}
		List batchSubjects = examPaperDB.getBatchExamSubjects(inst_id, div_id, batchList, examIds);
		List<BatchExamSubjects> batchExamSubjectList = new ArrayList<BatchExamSubjects>();
		for (Iterator iterator = batchSubjects.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			BatchExamSubjects batchExamSubjects = new BatchExamSubjects();
			batchExamSubjects.setExam_id(((Number)object[0]).intValue());
			batchExamSubjects.setSubject_id(((Number)object[1]).intValue());
			batchExamSubjects.setMarks(((Number)object[2]).intValue());
			batchExamSubjects.setBatch_id(((Number)object[3]).intValue());
			batchExamSubjectList.add(batchExamSubjects);
		}
		StudentMarksDB marksDB = new StudentMarksDB();
		List marksList = marksDB.getStudentBatchExamMarks(inst_id, div_id, batchList, examIds, student_id);
		List list = marksDB.getBatchAvgExamMarks(inst_id, div_id, batchList, examIds);
		List<StudentDetailMarks> detailMarkList = new ArrayList<StudentDetailMarks>();
		for (BatchExams exams : batchExamList) {
			for (BatchExamSubjects batchExamSubjects : batchExamSubjectList) {
				if(batchExamSubjects.getBatch_id() == exams.getBatch_id() && batchExamSubjects.getExam_id() == exams.getExam_id()){
					boolean flag= false;
					for (Iterator iterator = marksList.iterator(); iterator.hasNext();) {
						Object[] object = (Object[]) iterator.next();
						if(((Number)object[1]).intValue()==batchExamSubjects.getSubject_id() 
								&& ((Number)object[2]).intValue()==batchExamSubjects.getExam_id() 
								&& ((Number)object[3]).intValue()== exams.getBatch_id()){
							StudentDetailMarks detailMarks = new StudentDetailMarks();
							detailMarks.setBatch_id(exams.getBatch_id());
							detailMarks.setSub_id(batchExamSubjects.getSubject_id());
							detailMarks.setExam_id(batchExamSubjects.getExam_id());
							detailMarks.setMarks(((Number)object[0]).intValue());
							detailMarks.setExamPresentee("P");
							detailMarks.setExamSubjectTotalMarks(batchExamSubjects.getMarks());
							detailMarkList.add(detailMarks);
							flag= true;
							break;
						}
						
					}
					if(flag == false){
						StudentDetailMarks detailMarks = new StudentDetailMarks();
						detailMarks.setBatch_id(exams.getBatch_id());
						detailMarks.setSub_id(batchExamSubjects.getSubject_id());
						detailMarks.setExam_id(batchExamSubjects.getExam_id());
						detailMarks.setMarks(0);
						detailMarks.setExamPresentee("A");
						detailMarkList.add(detailMarks);
					}
				}
			}
		}
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			for (StudentDetailMarks studentDetailMarks : detailMarkList) {
				if(studentDetailMarks.getSub_id() == ((Number)object[0]).intValue() && 
					studentDetailMarks.getExam_id() == ((Number)object[1]).intValue() && 
					studentDetailMarks.getBatch_id() == ((Number)object[2]).intValue()){
					studentDetailMarks.setAvgMarks(((Number)object[3]).intValue());
					studentDetailMarks.setTopperMarks(((Number)object[4]).intValue());
				}
			}
			
		}
		for (BatchExams exams : batchExamList) {
			int total_marks = 0;
			for (BatchExamSubjects batchExamSubjects : batchExamSubjectList) {
				if(exams.getBatch_id() == batchExamSubjects.getBatch_id() && 
					exams.getExam_id() == batchExamSubjects.getExam_id()){
					total_marks = total_marks + batchExamSubjects.getMarks();
				}
			}
			exams.setMarks(total_marks);
		}
		
		
		examWiseStudentDetails.setBatchExamDistinctSubjectList(batchExamDistinctSubjectList);
		examWiseStudentDetails.setBatchExamList(batchExamList);
		examWiseStudentDetails.setBatchExamSubjectList(batchExamSubjectList);
		examWiseStudentDetails.setDetailMarklist(detailMarkList);
		examWiseStudentDetails.setBatchIds(batchList);
		}
		return examWiseStudentDetails;
		
	}
	
	public static void main(String[] args) {
		StudentMarksTransaction transaction = new StudentMarksTransaction();
		transaction.sendStudentProgressCard(4, 14, 1, "1,2");
	}
}
