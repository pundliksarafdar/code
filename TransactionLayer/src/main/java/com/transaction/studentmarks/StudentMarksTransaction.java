package com.transaction.studentmarks;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.exam.Exam;
import com.classapp.db.exam.ExamDB;
import com.classapp.db.exam.ExamPaperDB;
import com.classapp.db.student.Student;
import com.classapp.db.student.StudentDB;
import com.classapp.db.student.StudentMarks;
import com.classapp.db.subject.StudentMarksDB;
import com.classapp.db.subject.Subject;
import com.service.beans.BatchStudentExamMarks;
import com.service.beans.ExamSubject;
import com.service.beans.ProgressCardServiceBean;
import com.service.beans.StudentData;
import com.service.beans.StudentProgressCard;
import com.service.beans.StudentSubjectMarks;
import com.service.beans.SubjectsExam;
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
	
	public boolean deleteStudentMarksrelatedtosubject(int sub_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtosubject(sub_id);
	}
	
	public boolean deleteStudentMarksrelatedtodivision(int div_id) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtodivision(div_id);
	}
	
	public boolean deleteStudentMarksrelatedtoexam(int inst_id,int div_id,int sub_id,int exam_ID) {
		StudentMarksDB marksDB=new StudentMarksDB();
		return marksDB.deleteStudentMarksrelatedtoexam(inst_id, div_id, sub_id, exam_ID);
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
		ProgressCardServiceBean cardServiceBean = new ProgressCardServiceBean();
		cardServiceBean.setExamList(examList);
		cardServiceBean.setExamSubjectList(examSubjectServiceList);
		cardServiceBean.setStudentDataList(dataList);
		cardServiceBean.setSubjectList(distinctSubjectsList);
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
				String examData[] = new String[distinctSubjectsList.size()+1];
				examData[pos] =exam.getExam_name();
				for (com.datalayer.subject.Subject subject : distinctSubjectsList) {
					pos++;
					List<StudentData> studentMarksObj = dataList.stream().filter(
							studentMarks->(studentMarks.getStudent_id() == studentData.getStudent_id()
							&& studentMarks.getSub_id() == subject.getSubjectId() 
							&& studentMarks.getExam_id() == exam.getExam_id())).collect(Collectors.toList());
					if(!studentMarksObj.isEmpty()){
						if("P".equals(studentMarksObj.get(0).getExamPresentee())){
						examData[pos] = String.valueOf(studentMarksObj.get(0).getMarks());
					}else{
						examData[pos] = "A";
					}
					}else{
						examData[pos] = "-";
					}	
				
				}
				dataArrayList.add(examData);
			}
			studentProgressCard.setDataList(dataArrayList);
			studentProgressCardList.add(studentProgressCard);
		}
		return studentProgressCardList;
		
	}
	
	public static void main(String[] args) {
		StudentMarksTransaction transaction = new StudentMarksTransaction();
		transaction.sendStudentProgressCard(4, 14, 1, "1,2");
	}
}
