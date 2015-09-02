package com.classowner;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.batch.division.Division;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Subjects;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.ExamData;
import com.datalayer.exam.MCQData;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.exams.ExamTransaction;
import com.user.UserBean;

public class EditExamAction extends BaseAction{
	List<MCQData> mcqDatas;
	String divisionName;
	String subjectName;
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		
		String division = (String) request.getSession().getAttribute(Constants.DIVISION);
		String subject = (String) request.getSession().getAttribute(Constants.SUBJECT);
		String batch = (String) request.getSession().getAttribute(Constants.BATCH);
		
		ExamTransaction examTransaction = new ExamTransaction();
		mcqDatas = examTransaction.searchExamData(userBean.getRegId(), null, null, Integer.parseInt(division), false, Integer.parseInt(subject), null, null,null);
		
		DivisionTransactions divisionTransactions = new DivisionTransactions();
		List<Division> divisions = divisionTransactions.getAllDivisions(userBean.getRegId());
		SubjectTransaction subjectTransaction = new SubjectTransaction();
		List<Subjects> subjectList = subjectTransaction.getAllClassSubjects(userBean.getRegId());
		
		for (Division divisionData:divisions) {
			if(Integer.parseInt(division)==divisionData.getDivId()){
				divisionName = divisionData.getDivisionName();	
			}
		}
		
		for(Subjects subjectdata:subjectList){
			if(Integer.parseInt(subject)==subjectdata.getSubjectId()){
				subjectName = subjectdata.getSubjectName();	
			}
		}
		return SUCCESS;
	}
	public List<MCQData> getMcqDatas() {
		return mcqDatas;
	}
	public void setMcqDatas(List<MCQData> mcqDatas) {
		this.mcqDatas = mcqDatas;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	}
