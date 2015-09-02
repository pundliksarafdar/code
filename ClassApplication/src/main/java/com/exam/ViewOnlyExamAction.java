package com.exam;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classapp.db.exambean.ExamData;
import com.classapp.db.exambean.Option;
import com.classapp.db.exambean.QuestionData;
import com.config.BaseAction;
import com.config.Constants;
import com.datalayer.exam.MCQData;

import com.transaction.exams.ExamTransaction;
import com.user.UserBean;

public class ViewOnlyExamAction extends BaseAction{
	String examId;
	String examPath;
	String examName;
	String methodToCall;
	int questionNumber;
	String[] option;
	
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		
		if(null == methodToCall || "".equals(methodToCall)){
			/*Session object is set in GenerateExamAction*/
			List<MCQData> exams = (List<MCQData>) request.getSession().getAttribute(Constants.EXAM_SEARCH_RESULT);
			for (MCQData exam:exams) {
				if (null!=examId && examId.equals(exam.getExam_id()+"")) {
					examPath = exam.getUpload_path();
					examName = "name";
					break;
				}
			}
			
			File file = new File(examPath); 
			if(!file.exists()){
				addActionError("Exam is not available... Please contact class");
				return ERROR;
			}
			ExamTransaction examTransaction = new ExamTransaction();
/*			ExamData exam = examTransaction.getExam(examPath);
			request.setAttribute(Constants.EXAM_DATA, exam);
			request.getSession().setAttribute(Constants.EXAM_DATA, exam);
			request.setAttribute(Constants.EXAM_DATA_LENGTH, exam.getQuestionDatas().size());
			QuestionData questionData = exam.getQuestionDatas().get(questionNumber);
			request.setAttribute(Constants.QUESTION_DATA, questionData);
	*/		
			return SUCCESS;
		}else if("getQuestion".equals(methodToCall)){
			
			ExamData examData = (ExamData) request.getSession().getAttribute(Constants.EXAM_DATA);
			//request.getSession().setAttribute(Constants.EXAM_DATA, examData);
			request.setAttribute(Constants.EXAM_DATA_LENGTH, examData.getQuestionDatas().size());
			request.setAttribute(Constants.EXAM_DATA, examData);
			if (null!=examData) {
				if(questionNumber>0){
					questionNumber--;
			}
				QuestionData questionData = examData.getQuestionDatas().get(questionNumber);
				request.setAttribute(Constants.QUESTION_DATA, questionData);
				examName = "name";
			}
		return SUCCESS;	
		}else if("submitans".equals(methodToCall)){
			ExamData examData = (ExamData) request.getSession().getAttribute(Constants.EXAM_DATA);
			request.setAttribute(Constants.EXAM_DATA_LENGTH, examData.getQuestionDatas().size());
			request.setAttribute(Constants.EXAM_DATA, examData);
			if (null!=examData) {
			
				QuestionData questionData = examData.getQuestionDatas().get(questionNumber);
				List<Option> options = questionData.getOptions();
				
				int optionLength = option.length;
				for(int j=0;j<optionLength;j++){
					int optionSelected = Integer.parseInt(option[j]);
					options.get(optionSelected).setSelected(true);
				}
				request.setAttribute(Constants.QUESTION_DATA, questionData);
				examName = "name";
			}
			request.getSession().setAttribute(Constants.EXAM_DATA, examData);
			return SUCCESS;
		}
		
		return ERROR;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getExamPath() {
		return examPath;
	}

	public void setExamPath(String examPath) {
		this.examPath = examPath;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	public String getMethodToCall() {
		return methodToCall;
	}

	public void setMethodToCall(String methodToCall) {
		this.methodToCall = methodToCall;
	}

	public String[] getOption() {
		return option;
	}

	public void setOption(String[] option) {
		this.option = option;
	}

	
}
