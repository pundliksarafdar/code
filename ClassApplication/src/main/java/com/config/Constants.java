package com.config;

import java.util.ArrayList;
import java.util.List;

public class Constants {
	
	private Constants(){
		
	}
	public static String LOGINNAME_BLANK = "Login name can not be blank";
	public static String LOGINPASS_BLANK = "Password can not be blank";	
	public static String SUCCESS = "success";
	public static String ERROR = "error";
	public static String PAGINATION_SERVICE_ID = "1";
	public static String ERROR_MESSAGE = "errormessage";
	
	
	
	/************************************************************************************************/
	public static String PAGINATION_CURRENTPAGES = "currentpages";
	public static String PAGINATION_RESULT_PER_PAGES = "resultsperpage";
	
	public static String SERVICE_ID = "serviceid";
	public static String SERVICE_URL = "serviceurl";
	public static String SERVICE_NAME = "serviceName";
	public static String SERVICE_NOTE = "serviceNote";
	public static String SERVICE_PARAM = "serviceParam";

	public static String CLASSSERACHFORM = "searchform";
	public static String PREV = "prev";
	public static String NEXT ="next";
	
	public static String CLASSOWNER = "success_classowner";
	public static String CLASSTEACHER = "success_classteacher";
	public static String CLASSSTUDENT = "success_student";
	public static String UNACCEPTED = "unaccepted";
	public static String ACCESSBLOCKED = "accessblocked";
	
	public static String BATCH_LIST = "batchList";
	public static String SUBJECT_LIST = "subjectList";
	public static String STUDENT_LIST = "listOfStudents";
	public static String TEACHER_LIST = "listOfteachers";
	public static String BATCHES_LIST = "listOfBatches";
	public static String MCQS_LIST= "listOfMCQs";	
	public static String FILEPATH="C:\\ExamPapers";
	
	public static String EXAM_SEARCH_RESULT = "examSearchResult";
	public static final String EXAM_DATA = "examData";
	public static String BATCHCOUNT="batchcount";
	public static String STUDENTCOUNT="studentcount";
	public static String TEACHERCOUNT="teachercount";
	
	public static final String EXAM_DATA_LENGTH = "examDataLength";
	public static final String QUESTION_DATA = "questionData";
	public static String DIVISION_NAMES="divisionNames";
	public static String ACTIVATION="activation";
	public static String RESET_PASSWORD="resetpassword";
	
	/*Action URL*/
	public static List<String> ALL_ACTION_URLs = new ArrayList<String>();
	public static String ACTION_URLS="actionUrl";
}
