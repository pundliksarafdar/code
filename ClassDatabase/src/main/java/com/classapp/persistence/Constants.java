package com.classapp.persistence;

public class Constants {

private Constants(){
	}
	public static String ERROR = "error";
	public static String SUCCESS = "success";
	
	public static String SERVICE_ID = "serviceid";
	public static String SERVICE_URL = "serviceurl";
	public static String SERVICE_NAME = "serviceName";
	public static String SERVICE_NOTE = "serviceNote";
	public static String SERVICE_PARAM = "serviceParam";

	public static String SERVICE_PAGINATION = "1";
	public static String DEBUGGING_MODE = "6";
	public static String SHOW_STACK_TRACE = "4";
	public static String RESULT_PER_PAGE = "resultsperpage";
	public static String SERVICE_LOGGER = "8";
	public static String APP_PAGINATION = "9";
	
	public static String BATCH_NAME = "batchName";
	public static String BATCH_TIME = "batchTime";
	public static String START_TIME = "startTime";
	public static String END_TIME = "endTime";

	public static String ADD_SUBJECT = "addSubject";
	public static String ADD_STUDENT= "addStudent";
	public static String STUDENT_DETAILS= "getStudentDetails";
	public static String SEARCH_TEACHER= "searchTeacher";
	public static String SEARCH_BATCH= "searchBatch";
	public static String STUDENT_LIST="listOfStudents";
	public static final String UPDATE_STUDENT = "updateStudent";
	public static final String UPDATE_TEACHER = "updateTeacher";
	public static final String DELETE_TEACHER = "deleteTeacher";
	public static final String TEACHER_LIST = "listOfteachers";
	public static final String UPDATE_BATCH = "updateBatch";
	public static final String DELETE_BATCH = "deleteBatch";
	public static final String UPLOAD_EXAM = "uploadExam";
	public static String DIVISION_NAMES="divisionNames";
	public static String FETCH_BATCHES="fetchBatchesForDivision";
	public static final String BATCH_LIST = "batchList";
	public static final String DIVISION_LIST = "divisionList";
	public static final String CLASS_LIST = "classList";
	public static final String DIVISION_STREAM = "divisionStream";
	public static final String DIVISION_ID = "divisionId";
	public static final String DIVISIONS = "divisions";
	public static final String ALL_LOGINS = "alllogins";
	public static String STORAGE_PATH = "";
	public static int COMMON_ZERO = 0;
	/*Message Types*/
	public static String AUTO_DAILY_MSG = "1";
	public static String AUTO_WEEKLY_MSG = "2";
	public static String AUTO_MONTHLY_MSG = "3";
	public static String MANUAL_DAILY_MSG = "4";
	public static String MANUAL_WEEKLY_MSG = "5";
	public static String MANUAL_MONTHLY_MSG = "6";
	public static String GENERAL_STUDENT_MSG = "7";
	public static String GENERAL_PARENT_MSG = "8";
	public static String GENERAL_TEACHER_MSG = "9";
	public static String MANUAL_PROGRESS_CARD = "10";
	public static String PRAMOTIONAL = "1";
	public static String TRANSACTIONAL = "2";
	//Roles
	public static int CLASSOWNER_ROLE = 1;
	public static int TEACHER_ROLE = 2;
	public static int STUDENT_ROLE = 3 ;
	public static int PARENT_ROLE = 4;
	
	public static final String USER_DELETED = "R";
	public static final String USER_DISABLED = "D";
}
