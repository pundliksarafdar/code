package com.classowner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.util.ServletContextAware;

import com.classapp.db.Teacher.TeacherDetails;
import com.classapp.db.batch.division.Division;
import com.classapp.db.exams.MCQData;
import com.classapp.db.subject.Subject;
import com.config.BaseAction;
import com.config.Constants;
import com.google.gson.JsonObject;
import com.helper.DivisionHelperBean;
import com.helper.SubjectHelperBean;
import com.helper.TeacherHelperBean;
import com.transaction.exams.ExamTransaction;
import com.user.UserBean;

public class FileUploadAction extends BaseAction implements ServletContextAware {
	
	
	private String divisionName;
	private String teacherName;
	private String examSubjectName;
	private File uploadExam;
	private Date creationDate;
	private String uploadExamContentType;
    private String uploadExamFileName;
    private Properties configProp = new Properties();
    private int classId;
    private int subject_id;
	private int div_id;
	private static String STATUS = "status";
	private static String MESSAGE = "message";
	
	public int getSubject_id() {
		return subject_id;
	}


	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}


	public int getDiv_id() {
		return div_id;
	}


	public void setDiv_id(int div_id) {
		this.div_id = div_id;
	}


	public int getTeacher_id() {
		return teacher_id;
	}


	public void setTeacher_id(int teacher_id) {
		this.teacher_id = teacher_id;
	}

	private int teacher_id;
	public int getClassId() {
		return classId;
	}


	public void setClassId(int classId) {
		this.classId = classId;
	}


	public ServletContext getServletContext() {
		return servletContext;
	}


	public String getUploadExamContentType() {
		return uploadExamContentType;
	}


	public void setUploadExamContentType(String uploadExamContentType) {
		this.uploadExamContentType = uploadExamContentType;
	}


	public String getUploadExamFileName() {
		return uploadExamFileName;
	}


	public void setUploadExamFileName(String uploadExamFileName) {
		this.uploadExamFileName = uploadExamFileName;
	}

	private ServletContext servletContext;
	
	public String getDivisionName() {
		return divisionName;
	}


	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}


	public String getTeacherName() {
		return teacherName;
	}


	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}


	public String getExamSubjectName() {
		return examSubjectName;
	}


	public void setExamSubjectName(String examSubjectName) {
		this.examSubjectName = examSubjectName;
	}

	public void setServletContext(ServletContext context) {
		servletContext=context;
	}
	
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate() {
		this.creationDate = new Date();
	}
	
	public File getUploadExam() {
		return uploadExam;
	}

	public void setUploadExam(File uploadExam) {
		this.uploadExam = uploadExam;
	}

	private void loadProperties(){
		
		InputStream in = null;
        try {
        	in = new FileInputStream("exam_config.properties");
        	configProp.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public String performBaseAction(UserBean userBean,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		PrintWriter printWriter = null;
		JsonObject respObject = new JsonObject();
		respObject.addProperty("status", "error");
		try {	
			printWriter = response.getWriter();
			//loadProperties();
			setCreationDate();
			setClassId(userBean.getRegId());
			if(validateExamPaperInfo(request,response,session)){
				//Format to generate file name of exam Paper : CLASSID_DIVISIONID_TEACHERID_SUBJECTID_CREATIONTIME
				String fileName=this.classId+"_"+this.div_id+"_"+this.teacher_id+"_"+this.subject_id+"_"+this.creationDate.getTime();
				//String filePath = configProp.getProperty("exam.filepath"); 
				String filePath = Constants.FILEPATH;
				if(this.uploadExam==null){
					addActionError("You must select file to upload!");
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "You must select file to upload!");			
					return INPUT;
				}
		        File fileToCreate = new File(filePath, fileName);  
		        FileUtils.copyFile(this.uploadExam,fileToCreate);
		        updateMOCDB(fileName,filePath);
			}else{
				addActionError("Invalid input");
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "Invalid input");			
				return INPUT;
			}
			printWriter.write(respObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(e.getMessage());
			respObject.addProperty(STATUS, "error");
			respObject.addProperty(MESSAGE, "Invalid input");	
			return INPUT;
		}
		
		respObject.addProperty(STATUS, "success");
		respObject.addProperty(MESSAGE, "Successfully uploaded MCQ paper!!");	
		return SUCCESS;
	}


	private boolean validateExamPaperInfo(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> session) {
		boolean result = false;
		try{
			PrintWriter printWriter = response.getWriter();
			JsonObject respObject = new JsonObject();
			respObject.addProperty("status", "error");
			
			if(this.divisionName.equals("") ||this.divisionName==null||this.examSubjectName.equals("")||this.examSubjectName==null||this.teacherName.equals("")||this.teacherName==null){
				addActionError("Division name or Subject name or Teacher name can be blank!");
				respObject.addProperty(STATUS, "error");
				respObject.addProperty(MESSAGE, "Division name or Subject name or Teacher name can be blank!");
				return result;
			}
			
			DivisionHelperBean divisionHelperBean = new DivisionHelperBean();
	
			for (Division division : divisionHelperBean.getListOfDivision()) {
				if (division.getDivisionName().equalsIgnoreCase(divisionName)) {
					result = true;
					setDiv_id(division.getDivId());
					break;
				}
			}
			if (result) {
				result=false;
				SubjectHelperBean subjectHelperBean = new SubjectHelperBean();
				subjectHelperBean.setClass_id(classId);
	
				for (Subject subject : subjectHelperBean.getSubjects()) {
					if (subject.getSubjectName().equalsIgnoreCase(examSubjectName)) {
						result = true;
						setSubject_id(subject.getSubjectId());
						break;
					}
				}
			} else {
					addActionError("Invalid Division Name!");
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Invalid Division Name!");
				return result;
			}
			if (result) {
				result=false;
				TeacherHelperBean teacherHelperBean = new TeacherHelperBean();
				teacherHelperBean.setClass_id(classId);
				for (TeacherDetails teacherDetails : teacherHelperBean
						.getTeachers()) {
					if (teacherDetails.getTeacherBean().getLoginName()
							.equals(teacherName)) {
						result = true;
						setTeacher_id(teacherDetails.getTeacherBean().getRegId());
						break;
					}
				}
			} else {
					addActionError("Invalid subject Name!");
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Invalid subject Name!");
					return false;
			}
	
			if (!result) {
					addActionError("Invalid teacher name!");
					respObject.addProperty(STATUS, "error");
					respObject.addProperty(MESSAGE, "Invalid teacher Name!");
				
			}
			printWriter.write(respObject.toString());
		}catch(Exception e){
			e.printStackTrace();
			addActionError("Invalid input");
		}
		
	return result;
		
	}
	
	private void updateMOCDB(String fileName, String filePath){
		MCQData mcqData= new MCQData();
		mcqData.setClass_id(classId);
		mcqData.setDiv_id(div_id);
		mcqData.setTeacher_id(teacher_id);
		mcqData.setCreate_date(creationDate);
		mcqData.setSubject_id(subject_id);
		mcqData.setUpload_path(filePath.concat("\\"+fileName));
		
		ExamTransaction transaction= new ExamTransaction();
		//transaction.addUpdateDb(mcqData);
	}

}