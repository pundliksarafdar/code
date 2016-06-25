package com.classowner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.classapp.db.batch.division.Division;
import com.classapp.db.question.Questionbank;
import com.classapp.db.subject.Subject;
import com.classapp.db.subject.Topics;
import com.classapp.logger.AppLogger;
import com.classapp.login.UserStatic;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.ExamData;
import com.datalayer.exam.ParagraphQuestion;
import com.datalayer.exam.QuestionData;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.tranaction.subject.SubjectTransaction;
import com.transaction.batch.division.DivisionTransactions;
import com.transaction.institutestats.InstituteStatTransaction;
import com.transaction.questionbank.QuestionBankTransaction;
import com.user.UserBean;

public class AddQuestionAction extends BaseAction{
	List<Division> divisions;
	@Override
	public String performBaseAction(UserBean userBean,HttpServletRequest request,HttpServletResponse response,Map<String, Object> session) {
		int inst_id=userBean.getRegId();
		DivisionTransactions divisionTransaction = new DivisionTransactions();
		divisions = divisionTransaction.getAllDivisions(inst_id);
		return "startaddingquestion";
	}
	public List<Division> getDivisions() {
		return divisions;
	}
	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}
	
}
