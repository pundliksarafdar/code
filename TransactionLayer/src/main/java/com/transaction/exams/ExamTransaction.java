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
import com.classapp.logger.AppLogger;
import com.datalayer.exam.QuestionSearchRequest;
import com.datalayer.exambean.ExamData;
import com.service.beans.QuestionPaperEditFileElement;
import com.service.beans.QuestionPaperFileElement;


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
	
	public List<Exam> getExamList(int div_id,int inst_id) {
		ExamPaperDB examPaperDB = new ExamPaperDB();
		List<Integer> examIdList = examPaperDB.getDistinctExams(inst_id, div_id);
		ExamDB examDB = new ExamDB();
		return examDB.getExamList(inst_id, examIdList);
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
