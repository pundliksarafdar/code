package com.transaction.exams;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.classapp.db.exam.CompExam;
import com.classapp.db.exam.Exam;
import com.classapp.db.exam.ExamDB;
import com.classapp.db.exam.ExamPaperDB;
import com.classapp.db.exam.Exam_Paper;
import com.classapp.db.exams.MCQData;
import com.classapp.db.exams.MCQDataDB;
import com.classapp.db.question.QuestionbankDB;
import com.classapp.db.subject.StudentMarksDB;
import com.classapp.db.subject.Subject;
import com.classapp.logger.AppLogger;
import com.datalayer.exam.QuestionSearchRequest;
import com.datalayer.exambean.ExamData;
import com.service.beans.ExamSubject;
import com.service.beans.OnlineExamPaperSubjects;
import com.service.beans.QuestionPaperEditFileElement;
import com.service.beans.QuestionPaperFileElement;
import com.transaction.studentmarks.StudentMarksTransaction;


public class ExamTransaction {
	
	public void saveExam(ExamData examData) {
		MCQDataDB mcqDataDB = new MCQDataDB();
		MCQData mcqData = new MCQData();
		try {
			BeanUtils.copyProperties(mcqData, examData);
			mcqDataDB.updateDb(mcqData);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public List<Exam> getExamList(int inst_id) {
		ExamDB examDB = new ExamDB();
		return examDB.getExamList(inst_id);
	}
	
	public int save(int inst_id,String examName) {
		Exam exam = new Exam();
		exam.setExam_name(examName);
		exam.setInst_id(inst_id);
		ExamDB examDB = new ExamDB();
		return examDB.saveExam(exam);
	}
	
	public boolean saveExamPaper(int inst_id,String examName,List<Exam_Paper> exam_PaperList,int created_by) {
		ExamDB examDB = new ExamDB();
		int exam_id =0;
		if(!"-1".equals(examName)){
			Exam exam = new Exam();
			exam.setExam_name(examName);
			exam.setInst_id(inst_id);
			exam_id = examDB.saveExam(exam);
			}
		ExamPaperDB examPaperDB = new ExamPaperDB();
		for (Iterator iterator = exam_PaperList.iterator(); iterator.hasNext();) {
			Exam_Paper exam_Paper = (Exam_Paper) iterator.next();
			if(exam_id != 0){
				exam_Paper.setExam_id(exam_id);
			}
			exam_Paper.setCreated_by(created_by);
			exam_Paper.setCreated_dt(new java.sql.Date(new Date().getTime()));
			exam_Paper.setInst_id(inst_id);
			int exam_paper_id = examPaperDB.saveExamPaper(exam_Paper);
		}
		return true;
	}
	
	public List<Exam> getExamList(int div_id,int inst_id,int batch_id) {
		ExamPaperDB examPaperDB = new ExamPaperDB();
		List<Integer> examIdList = examPaperDB.getDistinctExams(inst_id, div_id,batch_id);
		List<Exam> examList = new ArrayList<Exam>(); 
		if(examIdList.size()>0){
		ExamDB examDB = new ExamDB();
		examList = examDB.getExamList(inst_id, examIdList);
		}
		return examList;
	}
	
	public List<Exam_Paper> getExamPapers(int div_id,int inst_id,int exam_id,int batch_id) {
		ExamPaperDB examPaperDB = new ExamPaperDB();
		return examPaperDB.getExamPapers(inst_id, div_id, exam_id,batch_id);
	}
	
	public boolean updateExamPapers(int div_id,int inst_id,int exam_id,List<Exam_Paper> updateExam_PaperList,int modified_by,int batch_id) {
		ExamPaperDB examPaperDB = new ExamPaperDB();
		List<Exam_Paper> exam_PaperList = examPaperDB.getExamPapers(inst_id, div_id, exam_id,batch_id);
		for (Iterator iterator = exam_PaperList.iterator(); iterator.hasNext();) {
			Exam_Paper exam_Paper = (Exam_Paper) iterator.next();
			boolean flag = false;
			for (Iterator innerIterator = updateExam_PaperList.iterator(); innerIterator
					.hasNext();) {
				Exam_Paper updatedExam_Paper = (Exam_Paper) innerIterator.next();
				if( updatedExam_Paper.getExam_paper_id() == exam_Paper.getExam_paper_id()){
					exam_Paper.setMarks(updatedExam_Paper.getMarks());
					exam_Paper.setDuration(updatedExam_Paper.getDuration());
					exam_Paper.setQuestion_paper_id(updatedExam_Paper.getQuestion_paper_id());
					exam_Paper.setHeader_id(updatedExam_Paper.getHeader_id());
					exam_Paper.setModified_by(modified_by);
					exam_Paper.setModified_dt(new java.sql.Date(new Date().getTime()));
					examPaperDB.updateExamPaper(exam_Paper);
					flag = true;
					break;
				}
			}
			if(flag == false){
			examPaperDB.deleteExamPaper(exam_Paper);
			}
			}
		for (Iterator innerIterator = updateExam_PaperList.iterator(); innerIterator
				.hasNext();) {
			Exam_Paper updatedExam_Paper = (Exam_Paper) innerIterator.next();
			if(updatedExam_Paper.getExam_paper_id() == 0){
				updatedExam_Paper.setCreated_by(modified_by);
				updatedExam_Paper.setCreated_dt(new java.sql.Date(new Date().getTime()));
				updatedExam_Paper.setInst_id(inst_id);
				examPaperDB.saveExamPaper(updatedExam_Paper);
			}
		}
			
		return true;
	}
	
	public List<ExamSubject> getExamSubjects(int inst_id,int div_id,int batch_id,int exam_id) {
		ExamPaperDB examPaperDB = new ExamPaperDB();
		StudentMarksDB marksDB = new StudentMarksDB();
		List<Integer> marksSubject = marksDB.getDistinctMarksSubject(inst_id, div_id, batch_id, exam_id);
		List<ExamSubject> subjectlist = new ArrayList<ExamSubject>(); 
		List list =  examPaperDB.getExamSubjects(inst_id, div_id, batch_id, exam_id);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object [] object = (Object []) iterator.next();
			ExamSubject subject = new ExamSubject();
			subject.setSubjectName((String)object[0]);
			subject.setSubjectId(((Number)object[1]).intValue());
			subject.setMarks(((Number)object[2]).intValue());
			subject.setMarksFlag(false);
			for (Iterator iterator2 = marksSubject.iterator(); iterator2.hasNext();) {
				Integer integer = (Integer) iterator2.next();
				if(integer == ((Number)object[1]).intValue()){
					subject.setMarksFlag(true);
				}
			}
			subjectlist.add(subject);
		}
		return subjectlist;
	}
	
	public boolean deleteExam(int inst_id,int exam_id) {
		ExamPaperDB examPaperDB = new ExamPaperDB();
		examPaperDB.deleteExamPaperRelatedToExam(inst_id, exam_id);
		StudentMarksTransaction marksTransaction = new StudentMarksTransaction();
		marksTransaction.deleteStudentMarksrelatedtoexam(inst_id, exam_id);
		ExamDB examDB = new ExamDB();
		return examDB.deleteExam(inst_id, exam_id);
	}
	
	public List<Exam> getOnlineExamList(int inst_id,int div_id,int batch_id) {
		ExamPaperDB examPaperDB = new ExamPaperDB();
		return examPaperDB.getOnlineExamList(inst_id, div_id, batch_id);
	}
	
	public List<OnlineExamPaperSubjects> getOnlineExamSubjectList(int inst_id,int div_id,int batch_id,int exam_id) {
		ExamPaperDB examPaperDB = new ExamPaperDB();
		List<OnlineExamPaperSubjects> examPaperSubjectList = new ArrayList<OnlineExamPaperSubjects>();
		List list =  examPaperDB.getOnlineExamSubjectList(inst_id, div_id, batch_id,exam_id);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] object = (Object[]) iterator.next();
			OnlineExamPaperSubjects paperSubjects = new OnlineExamPaperSubjects();
			paperSubjects.setQuestion_paper_id(((Number)object[0]).intValue());
			paperSubjects.setSub_id(((Number)object[1]).intValue());
			paperSubjects.setSub_name((String) object[2]);
			examPaperSubjectList.add(paperSubjects);
		}
		return examPaperSubjectList;
	}
	
	class ArrayListOverride<E> extends ArrayList<E>{
		@Override
		public String toString() {
			Iterator<E> it = iterator();
	        if (! it.hasNext())
	            return "()";

	        StringBuilder sb = new StringBuilder();
	        sb.append('(');
	        for (;;) {
	            E e = it.next();
	            sb.append(e == this ? "(this Collection)" : e);
	            if (! it.hasNext())
	                return sb.append(')').toString();
	            sb.append(',').append(' ');
	        }
	    }

		}
	
}
